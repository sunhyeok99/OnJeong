package com.a503.onjeong.domain.user.service;

import com.a503.onjeong.domain.user.User;
import com.a503.onjeong.domain.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService{
    WebClient webClient;
    private final UserRepository userRepository;
    private final KakaoService kakaoService;

    @PostConstruct
    public void initWebClient() {
        webClient = WebClient.create();
    }

    /* 회원 저장 */
    public void save() {
        // sign 필터에서 호출하는 함수
    }

    /* 자동 로그인 */
    public void login(String accessToken) throws UnknownHostException {
        // 로그인 필터로
        webClient.get()
                .uri("http://"+ InetAddress.getLocalHost().getHostAddress()+":8080/auto/login")
                .header("Authorization", accessToken)
                .retrieve()
                .bodyToMono(String.class);
    }

    /* 회원 로그인 및 회원가입 */
    public void kakaoLoginOrSignUp(String code) throws UnknownHostException {
        // 회원가입 필터로
        webClient.get()
                .uri("http://"+ InetAddress.getLocalHost().getHostAddress()+":8080/login")
                .header("Kakao-Access-Token", kakaoService.getKakaoToken(code))
                .retrieve()
                .bodyToMono(String.class);
    }

    @Transactional
    public void savePhoneNumber(Long userId, String phoneNumber){
        User user = userRepository.findById(userId).orElse(null);
        user.savePhoneNumber(phoneNumber);
    }
}
