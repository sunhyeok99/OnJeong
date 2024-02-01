package com.a503.onjeong.domain.weather.repository;

import com.a503.onjeong.domain.weather.ShortForecast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ShortForecastRepository extends JpaRepository<ShortForecast, String> {

    @Query("select f from ShortForecast f " +
            "where f.sido like %:sido% and " +
            "f.gugun like %:gugun% and " +
            "f.dong like %:dong%" )
    List<ShortForecast> findCodeBySidoAndGugunAndDong(String sido, String gugun, String dong);

    @Query("select f from ShortForecast f " +
            "where f.sido like %:sido% and " +
            "f.gugun like %:gugun%")
    List<ShortForecast> findCodeBySidoAndGugun(String sido, String gugun);

    @Query("select f from ShortForecast f " +
            "where f.sido like %:sido%")
    List<ShortForecast> findCodeBySido(String sido);

}
