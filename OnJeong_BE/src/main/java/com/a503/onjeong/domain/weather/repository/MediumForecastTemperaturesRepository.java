package com.a503.onjeong.domain.weather.repository;

import com.a503.onjeong.domain.weather.MediumForecastTemperatures;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MediumForecastTemperaturesRepository extends JpaRepository<MediumForecastTemperatures, String> {

    // 시도로 code 찾기 (광역시)
    @Query("select f.code from MediumForecastTemperatures f where f.sido like %:sido%")
    String findCodeBySido(String sido);


    // 구군으로 code 찾기
    @Query("select f.code from MediumForecastTemperatures f where f.sido like %:gugun%")
    String findCodeByGugun(String gugun);

}
