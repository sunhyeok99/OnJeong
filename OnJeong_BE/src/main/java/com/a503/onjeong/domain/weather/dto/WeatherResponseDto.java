package com.a503.onjeong.domain.weather.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class WeatherResponseDto {

    // 현재 날짜
    private LocalDate date;

    // 현재 기온
    private double temperatures;

    // 최저 기온
    private double temperaturesLow;

    // 최고 기온
    private double temperaturesHigh;

    // 하늘 상태
    // 맑음(1), 구름많음(3), 흐림(4)
    private int sky;

    // 강수 형태
    // 없음(0), 비(1), 비/눈(2), 눈(3), 소나기(4)
    private int pty;

}
