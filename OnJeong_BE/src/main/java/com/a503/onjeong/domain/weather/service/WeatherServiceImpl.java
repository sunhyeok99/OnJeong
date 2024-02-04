package com.a503.onjeong.domain.weather.service;

import com.a503.onjeong.domain.weather.ShortForecast;
import com.a503.onjeong.domain.weather.dto.*;
import com.a503.onjeong.domain.weather.dto.mediumforecast.SkyDto;
import com.a503.onjeong.domain.weather.dto.mediumforecast.SkyDtoWrapper;
import com.a503.onjeong.domain.weather.dto.mediumforecast.TemperaturesDtoWrapper;
import com.a503.onjeong.domain.weather.dto.mediumforecast.TemperaturesDto;
import com.a503.onjeong.domain.weather.dto.shortforecast.ShortForecastDto;
import com.a503.onjeong.domain.weather.dto.shortforecast.ShortForecastDtoWrapper;
import com.a503.onjeong.domain.weather.repository.MediumForecastLandRepository;
import com.a503.onjeong.domain.weather.repository.MediumForecastTemperaturesRepository;
import com.a503.onjeong.domain.weather.repository.ShortForecastRepository;
import com.sun.jdi.InternalException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {

    private String[] days = new String[7];

    private final ShortForecastRepository shortForecastRepository;
    private final MediumForecastTemperaturesRepository mediumForecastRepository;
    private final MediumForecastLandRepository mediumForecastLandRepository;

    WebClient webClient;

    @PostConstruct
    public void initWebClient() {
        // uri 인코딩 x
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);

        webClient = WebClient.builder().uriBuilderFactory(factory).build();
    }

    @Value("${weather.API_KEY}")
    private String API_KEY;

    public List<WeatherResponseDto> getWeatherInfo(WeatherRequestDto requestDto) {

        List<WeatherResponseDto> responseDto = new ArrayList<>();

        getDays();
        String oneDay = days[0].substring(0, 8);
        String oneTime = days[0].substring(8);
        String twoDay = days[1];
        String threeDay = days[2];

        WeatherResponseDto oneWeatherResponseDto = new WeatherResponseDto();
        WeatherResponseDto twoWeatherResponseDto = new WeatherResponseDto();
        WeatherResponseDto threeWeatherResponseDto = new WeatherResponseDto();
        WeatherResponseDto fourWeatherResponseDto = new WeatherResponseDto();
        WeatherResponseDto fiveWeatherResponseDto = new WeatherResponseDto();
        WeatherResponseDto sixWeatherResponseDto = new WeatherResponseDto();
        WeatherResponseDto sevenWeatherResponseDto = new WeatherResponseDto();

        oneWeatherResponseDto.setDate(LocalDate.now());
        twoWeatherResponseDto.setDate(LocalDate.now());
        threeWeatherResponseDto.setDate(LocalDate.now());
        fourWeatherResponseDto.setDate(LocalDate.now());
        fiveWeatherResponseDto.setDate(LocalDate.now());
        sixWeatherResponseDto.setDate(LocalDate.now());
        sevenWeatherResponseDto.setDate(LocalDate.now());

        /* 1일~3일 날씨 조회 */

        // 현재 위치의 x, y 좌표 구하기
        String sido = requestDto.getSido();
        String gugun = requestDto.getGugun();
        String dong = requestDto.getDong();

        List<ShortForecast> shortForecastList = shortForecastRepository.findCodeBySidoAndGugunAndDong(sido, gugun, dong);
        if (shortForecastList.isEmpty()) {
            shortForecastList = shortForecastRepository.findCodeBySidoAndGugun(sido, gugun);
            if (shortForecastList.isEmpty()) {
                shortForecastList = shortForecastRepository.findCodeBySido(sido);
                if (shortForecastList.isEmpty()) {
                    throw new InternalException("존재하지 않는 지역 예외");
                }
            }
        }

        ShortForecast shortForecast = shortForecastList.get(0);

        int coordinateX = shortForecast.getCoordinateX();
        int coordinateY = shortForecast.getCoordinateY();

        LocalDateTime now = LocalDateTime.now();
        String base = getBaseTime(now);
        String baseDays = base.substring(0, 8);
        String baseTime = base.substring(8);

        // API 요청
        MultiValueMap<String, String> shortWeatherParam = new LinkedMultiValueMap<>();
        shortWeatherParam.add("serviceKey", API_KEY);
        shortWeatherParam.add("pageNo", "1");
        shortWeatherParam.add("numOfRows", "800");
        shortWeatherParam.add("dataType", "JSON");
        shortWeatherParam.add("base_date", baseDays);
        shortWeatherParam.add("base_time", baseTime);
        shortWeatherParam.add("nx", String.valueOf(coordinateX));
        shortWeatherParam.add("ny", String.valueOf(coordinateY));

        ShortForecastDtoWrapper shortWrapper = webClient.get()
                .uri(uriBuilder ->
                        uriBuilder.scheme("http")
                                .host("apis.data.go.kr")
                                .path("/1360000/VilageFcstInfoService_2.0/getVilageFcst")
                                .queryParams(shortWeatherParam)
                                .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ShortForecastDtoWrapper.class)
                .block();

        List<ShortForecastDto> shortForecastDtoList = shortWrapper
                .getResponse()
                .getBody()
                .getItems()
                .getItem();

        for (ShortForecastDto data : shortForecastDtoList) {

            if (data.getFcstDate().equals(oneDay)) {
                // 1일 현재 기온
                if (data.getFcstTime().equals(oneTime) && data.getCategory().equals("TMP")) {
                    oneWeatherResponseDto.setTemperatures(Double.valueOf(data.getFcstValue()));
                }

                // 1일 최고 기온
                if (data.getCategory().equals("TMX")) {
                    oneWeatherResponseDto.setTemperaturesHigh(Double.valueOf(data.getFcstValue()));
                }

                // 1일 최저 기온
                if (data.getCategory().equals("TMN")) {
                    oneWeatherResponseDto.setTemperaturesLow(Double.valueOf(data.getFcstValue()));
                }

                // 1일 하늘 상태
                if (data.getCategory().equals("SKY")) {
                    oneWeatherResponseDto.setSky(Integer.valueOf(data.getFcstValue()));
                }

                // 1일 강수 형태
                if (data.getCategory().equals("PTY")) {
                    oneWeatherResponseDto.setPty(Integer.valueOf(data.getFcstValue()));
                }
            }

            if (data.getFcstDate().equals(twoDay)) {
                // 2일 최고 기온
                if (data.getCategory().equals("TMX")) {
                    twoWeatherResponseDto.setTemperaturesHigh(Double.valueOf(data.getFcstValue()));
                }

                // 2일 최저 기온
                if (data.getCategory().equals("TMN")) {
                    twoWeatherResponseDto.setTemperaturesLow(Double.valueOf(data.getFcstValue()));
                }

                // 2일 하늘 상태
                if (data.getCategory().equals("SKY")) {
                    twoWeatherResponseDto.setSky(Integer.valueOf(data.getFcstValue()));
                }

                // 2일 강수 형태
                if (data.getCategory().equals("PTY")) {
                    twoWeatherResponseDto.setPty(Integer.valueOf(data.getFcstValue()));
                }
            }

            if (data.getFcstDate().equals(threeDay)) {
                // 3일 최고 기온
                if (data.getCategory().equals("TMX")) {
                    threeWeatherResponseDto.setTemperaturesHigh(Double.valueOf(data.getFcstValue()));
                }

                // 3일 최저 기온
                if (data.getCategory().equals("TMN")) {
                    threeWeatherResponseDto.setTemperaturesLow(Double.valueOf(data.getFcstValue()));
                }

                // 2일 하늘 상태
                if (data.getCategory().equals("SKY")) {
                    threeWeatherResponseDto.setSky(Integer.valueOf(data.getFcstValue()));
                }

                // 3일 강수 형태
                if (data.getCategory().equals("PTY")) {
                    threeWeatherResponseDto.setPty(Integer.valueOf(data.getFcstValue()));
                }
            }

        }

        responseDto.add(oneWeatherResponseDto);
        responseDto.add(twoWeatherResponseDto);
        responseDto.add(threeWeatherResponseDto);


        /* 3일~7일 날씨 조회 */

        // 1. 최저, 최고 기온 API
        String code;
        String gugunCode;

        // code 값 조회
        if ((code = mediumForecastRepository.findCodeBySido(sido)) == null) {
            if ((gugunCode = mediumForecastRepository.findCodeByGugun(gugun.substring(0, gugun.length() - 1))) == null) {
                throw new InternalException("존재하지 않는 지역 예외");
            } else {
                code = gugunCode;
            }
        }

        // API 요청
        MultiValueMap<String, String> mediumWeatherParam = new LinkedMultiValueMap<>();
        mediumWeatherParam.add("serviceKey", API_KEY);
        mediumWeatherParam.add("pageNo", "1");
        mediumWeatherParam.add("numOfRows", "10");
        mediumWeatherParam.add("dataType", "JSON");
        mediumWeatherParam.add("regId", code);
        if (now.getHour() < 6) {
            mediumWeatherParam.add("tmFc", now.minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "1800");
        }
        else{
            mediumWeatherParam.add("tmFc", oneDay + "0600");
        }


        TemperaturesDtoWrapper temperaturesDtoWrapper = webClient.get()
                .uri(uriBuilder ->
                        uriBuilder.scheme("http")
                                .host("apis.data.go.kr")
                                .path("1360000/MidFcstInfoService/getMidTa")
                                .queryParams(mediumWeatherParam)
                                .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(TemperaturesDtoWrapper.class)
                .block();

        List<TemperaturesDto> temperaturesDtoList = temperaturesDtoWrapper
                .getResponse()
                .getBody()
                .getItems()
                .getItem();

        for (TemperaturesDto mediumForecastDto : temperaturesDtoList) {

            // 4일 최고, 최저 기온
            fourWeatherResponseDto.setTemperaturesHigh(Double.parseDouble(mediumForecastDto.getTaMax3()));
            fourWeatherResponseDto.setTemperaturesLow(Double.parseDouble(mediumForecastDto.getTaMin3()));

            // 5일 최고, 최저 기온
            fiveWeatherResponseDto.setTemperaturesHigh(Double.parseDouble(mediumForecastDto.getTaMax4()));
            fiveWeatherResponseDto.setTemperaturesLow(Double.parseDouble(mediumForecastDto.getTaMin4()));

            // 6일 최고, 최저 기온
            sixWeatherResponseDto.setTemperaturesHigh(Double.parseDouble(mediumForecastDto.getTaMax5()));
            sixWeatherResponseDto.setTemperaturesLow(Double.parseDouble(mediumForecastDto.getTaMin5()));

            // 7일 최고, 최저 기온
            sevenWeatherResponseDto.setTemperaturesHigh(Double.parseDouble(mediumForecastDto.getTaMax6()));
            sevenWeatherResponseDto.setTemperaturesLow(Double.parseDouble(mediumForecastDto.getTaMin6()));
        }


        // 2. 하늘/강수 상태/형태 API

        // 코드 값 조회
        code = mediumForecastLandRepository.findCodeBySido(sido);
        mediumWeatherParam.set("regId", code);

        // API 요청
        SkyDtoWrapper skyDtoWrapper = webClient.get()
                .uri(uriBuilder ->
                        uriBuilder.scheme("http")
                                .host("apis.data.go.kr")
                                .path("1360000/MidFcstInfoService/getMidLandFcst")
                                .queryParams(mediumWeatherParam)
                                .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(SkyDtoWrapper.class)
                .block();

        List<SkyDto> skyDtoList = skyDtoWrapper
                .getResponse()
                .getBody()
                .getItems()
                .getItem();

        for (SkyDto skyDto : skyDtoList) {

            // 4일차 하늘상태
            int[] value = changeSkyStatus(skyDto.getWf3Pm());
            fourWeatherResponseDto.setSky(value[0]);
            fourWeatherResponseDto.setPty(value[1]);

            // 5일차 하늘상태
            value = changeSkyStatus(skyDto.getWf4Pm());
            fiveWeatherResponseDto.setSky(value[0]);
            fiveWeatherResponseDto.setPty(value[1]);

            // 6일차 하늘상태
            value = changeSkyStatus(skyDto.getWf5Pm());
            sixWeatherResponseDto.setSky(value[0]);
            sixWeatherResponseDto.setPty(value[1]);

            // 7일차 하늘상태
            value = changeSkyStatus(skyDto.getWf6Pm());
            sevenWeatherResponseDto.setSky(value[0]);
            sevenWeatherResponseDto.setPty(value[1]);
        }

        responseDto.add(fourWeatherResponseDto);
        responseDto.add(fiveWeatherResponseDto);
        responseDto.add(sixWeatherResponseDto);
        responseDto.add(sevenWeatherResponseDto);

        return responseDto;
    }

    public String getBaseTime(LocalDateTime now) {
        int hours = now.getHour();
        if (hours < 2)
            return now.minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "2300";
        return now.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "0200";
    }

    public void getDays() {
        LocalDateTime today = LocalDateTime.now();

        String hours = String.valueOf(today.getHour());
        if (hours.length() == 1) hours = "0" + hours;

        days[0] = today.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + hours + "00";
        for (int i = 1; i < 7; i++) {
            today = today.plusDays(1);
            days[i] = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        }
    }


    public int[] changeSkyStatus(String status) {

        int[] value = new int[2];

        if (status.equals("맑음")) {
            value[0] = 1;
            value[1] = 0;
        } else if (status.equals("구름많음")) {
            value[0] = 3;
            value[1] = 0;
        } else if (status.equals("구름많고 비")) {
            value[0] = 3;
            value[1] = 1;
        } else if (status.equals("구름많고 눈")) {
            value[0] = 3;
            value[1] = 3;
        } else if (status.equals("구름많고 비/눈")) {
            value[0] = 3;
            value[1] = 2;
        } else if (status.equals("구름많고 소나기")) {
            value[0] = 3;
            value[1] = 4;
        } else if (status.equals("흐림")) {
            value[0] = 4;
            value[1] = 0;
        } else if (status.equals("흐리고 비")) {
            value[0] = 4;
            value[1] = 1;
        } else if (status.equals("흐리고 눈")) {
            value[0] = 4;
            value[1] = 3;
        } else if (status.equals("흐리고 비/눈")) {
            value[0] = 4;
            value[1] = 2;
        } else if (status.equals("흐리고 소나기")) {
            value[0] = 4;
            value[1] = 4;
        }
        return value;
    }

}
