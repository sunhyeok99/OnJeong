package com.a503.onjeong.domain.user.service;

import com.a503.onjeong.domain.user.dto.KakaoDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;


@RequiredArgsConstructor
@Service
public class KakaoService {

    @Value("${kakao.REST_KEY}")
    private String REST_API_KEY;

    /* 배포시 수정 */
    @Value("${kakao.REDIRECT_URI}")
    private String REDIRECT_URI;

    WebClient webClient;

    @PostConstruct
    public void initWebClient() {
        webClient = WebClient.create();
    }

    /* 카카오 액세스 토큰 추출 */
    public String getKakaoToken(String code){
        MultiValueMap<String, String> getTokenBody = new LinkedMultiValueMap<>();
        getTokenBody.add("grant_type", "authorization_code");
        getTokenBody.add("client_id", REST_API_KEY);
        getTokenBody.add("redirect_uri", REDIRECT_URI);
        getTokenBody.add("code", code);

        //카카오 토큰 발급 API 호출
        KakaoDto.Token token = webClient.post()
                .uri("https://kauth.kakao.com/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(getTokenBody))
                .retrieve()
                .bodyToMono(KakaoDto.Token.class)
                .block();

        return token.getAccess_token();
    }

    /* 카카오 액세스 토큰으로 사용자 정보 추출 */
    public String getUserInfo(String accessToken) {
        return webClient.get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

}

