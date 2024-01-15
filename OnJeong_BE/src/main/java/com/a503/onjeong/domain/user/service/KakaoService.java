package com.a503.onjeong.domain.user.service;

import com.a503.onjeong.domain.user.dto.KakaoDto;
import com.a503.onjeong.domain.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class KakaoService {

    WebClient webClient;

    @PostConstruct
    public void initWebClient() {
        webClient = WebClient.create();
    }

    // 카카오 토큰 추출
    public Map<String, String> getKakaoToken(String code){
        MultiValueMap<String, String> getTokenBody = new LinkedMultiValueMap<>();
        getTokenBody.add("grant_type", "authorization_code");
        getTokenBody.add("client_id", "보지마!");
        getTokenBody.add("redirect_uri", "http://localhost:8080/auth/kakao/redirect");
        getTokenBody.add("code", code);

        //카카오 토큰 발급 API 호출
        KakaoDto.Token token = webClient.post()
                .uri("https://kauth.kakao.com/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(getTokenBody))
                .retrieve()
                .bodyToMono(KakaoDto.Token.class)
                .block();

        Map<String, String> tokens = new HashMap<>();
        tokens.put("Access-Token", token.getAccess_token());
        tokens.put("Refresh-Token", token.getRefresh_token());

        return tokens;
    }

    // 카카오 액세스 토큰으로 사용자 정보 추출
    public void getUserInfoByKakaoToken(String accessToken) {
        String userKakaoInfo = webClient.get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

}

