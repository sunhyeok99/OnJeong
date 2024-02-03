package com.a503.onjeong.domain.weather.controller;

import com.a503.onjeong.domain.weather.dto.WeatherRequestDto;
import com.a503.onjeong.domain.weather.dto.WeatherResponseDto;
import com.a503.onjeong.domain.weather.service.WeatherServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
public class WeatherControllerImpl implements WeatherController {

    private final WeatherServiceImpl weatherService;

    @PostMapping("/info")
    public List<WeatherResponseDto> getWeather(@RequestBody WeatherRequestDto requestDto) {
        List<WeatherResponseDto> weatherInfo = weatherService.getWeatherInfo(requestDto);
        System.out.println(weatherInfo);
        return weatherInfo;
    }
}
