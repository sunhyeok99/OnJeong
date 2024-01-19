package com.a503.onjeong.global.auth.service;

import com.a503.onjeong.global.auth.dto.KakaoDto;
import com.sun.jdi.InternalException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;


/* 카카오 관련 서비스 */
@RequiredArgsConstructor
@Service
public class KakaoService {

    private static String GRANT_TYPE = "grant_type";
    private static String CLIENT_ID = "client_id";
    private static String REDIRECT_URI_KEY = "redirect_uri";
    private static final String BEARER = "Bearer ";

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

    /* 인가 코드로 카카오 토큰 발급하는 함수 */
    public KakaoDto.Token getKakaoToken(String code) {
        MultiValueMap<String, String> getTokenBody = new LinkedMultiValueMap<>();
        getTokenBody.add(GRANT_TYPE, "authorization_code");
        getTokenBody.add(CLIENT_ID, REST_API_KEY);
        getTokenBody.add(REDIRECT_URI_KEY, REDIRECT_URI);
        getTokenBody.add("code", code);

        //카카오 토큰 발급 API 호출
        KakaoDto.Token token = webClient.post()
                .uri("https://kauth.kakao.com/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(getTokenBody))
                .retrieve()
                .bodyToMono(KakaoDto.Token.class)
                .block();

        return KakaoDto.Token.builder()
                .access_token(token.getAccess_token())
                .refresh_token(token.getRefresh_token())
                .build();
    }

    /* 카카오 액세스 토큰으로 사용자 정보 추출하는 함수 */
    public KakaoDto.UserInfo getUserInfo(String kakaoAccessToken, String kakaoRefreshToken) {
        String userInfo;
        try {
            userInfo = webClient.get()
                    .uri("https://kapi.kakao.com/v2/user/me")
                    .header("Authorization", BEARER + kakaoAccessToken)
                    .retrieve()
                    // 원래는 커스텀 예외를 반환하여 클라가 다시 요청 해야 하지만 클라가 자신이 없음 ㅎ.. (추후 처리)
                    //.onStatus(HttpStatusCode::is4xxClientError, error -> Mono.just(new InternalException("액세스 토큰 유효x")))
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientResponseException e) {
            // 토큰 재발급
            KakaoDto.Token token = reissueKakaoToken(kakaoRefreshToken);
            kakaoAccessToken = token.getAccess_token();
            // 재요청
            if (token.getRefresh_token() != null) kakaoRefreshToken = token.getRefresh_token();
            userInfo = webClient.get()
                    .uri("https://kapi.kakao.com/v2/user/me")
                    .header("Authorization", BEARER + kakaoAccessToken)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        }

        JSONParser jsonParser = new JSONParser();

        JSONObject jsonObject;
        try {
            jsonObject = (JSONObject) jsonParser.parse(userInfo); //Cast: String -> Json Object
        } catch (ParseException e) {
            throw new InternalException("카카오 오류 예외");
        }

        if (jsonObject == null) {
            throw new InternalException("카카오 오류 예외");
        }

        long kakaoId = (long) jsonObject.get("id"); // 카카오 회원번호 추출
        jsonObject = (JSONObject) jsonObject.get("kakao_account"); // 필요한 회원 정보가 있는 Object 분리
        JSONObject profile = (JSONObject) jsonObject.get("profile"); // 프로필 관련한 Object 분리

        return KakaoDto.UserInfo.builder()
                .kakaoId(kakaoId)
                .kakaoAccessToken(kakaoAccessToken)
                .kakaoRefreshToken(kakaoRefreshToken)
                .nickname((String) profile.get("nickname"))
                .profileImageUrl((String) profile.get("profile_image_url"))
                .build();
    }

    /* 카카오 리프레시 토큰으로 카카오 토큰 재발급 하는 함수*/
    public KakaoDto.Token reissueKakaoToken(String refreshToken) {
        MultiValueMap<String, String> reissueTokenBody = new LinkedMultiValueMap<>();
        reissueTokenBody.add(GRANT_TYPE, "refresh_token");
        reissueTokenBody.add(CLIENT_ID, REST_API_KEY);
        reissueTokenBody.add(REDIRECT_URI_KEY, REDIRECT_URI);
        reissueTokenBody.add("refresh_token", refreshToken);

        KakaoDto.ReissueToken reissueToken = webClient.post()
                .uri("https://kauth.kakao.com/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(reissueTokenBody))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, error -> Mono.just(new InternalException("리프레시 토큰 유효x"))) // 로그인 페이지로 (추후 예외 처리)
                .bodyToMono(KakaoDto.ReissueToken.class)
                .block();

        return KakaoDto.Token.builder()
                .access_token(reissueToken.getAccess_token())
                .refresh_token(reissueToken.getRefresh_token())
                .build();
    }

}

