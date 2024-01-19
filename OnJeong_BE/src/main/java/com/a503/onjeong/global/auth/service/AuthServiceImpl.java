package com.a503.onjeong.global.auth.service;

import com.a503.onjeong.domain.user.User;
import com.a503.onjeong.domain.user.UserType;
import com.a503.onjeong.domain.user.repository.UserRepository;
import com.a503.onjeong.global.auth.dto.*;
import com.sun.jdi.InternalException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


/* 인증, 인가 관련 서비스 */
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService, UserDetailsService {
    WebClient webClient;
    private final UserRepository userRepository;
    private final KakaoService kakaoService;

    @PostConstruct
    public void initWebClient() {
        //서버 배포시 서버에 할당된 IP로 변경 예정
        webClient = WebClient.create("http://localhost:8080");
    }

    /* 회원 가입 */
    @Override
    @Transactional
    public void signup(String kakaoAccessToken, String kakaoRefreshToken,
                       String phoneNumber, HttpServletResponse response) {
        // 유저 정보 추출
        KakaoDto.UserInfo userInfo = kakaoService.getUserInfo(kakaoAccessToken, kakaoRefreshToken);

        User user = userRepository.findByKakaoId(userInfo.getKakaoId()).orElse(null);

        // 유저가 없으면 저장 (신규)
        if (user == null) {

            // 유저 생성
            User userOpt = User.builder()
                    .kakaoId(userInfo.getKakaoId())
                    .name(userInfo.getNickname())
                    .type(UserType.USER)
                    .build();

            user = userRepository.save(userOpt);
        }

        // 전화번호 세팅
        user.setPhoneNumber(phoneNumber);
        // 카카오 리프레시 토큰 갱신
        user.setKakaoRefreshToken(userInfo.getKakaoRefreshToken());
    }

    /* 로그인 (검증) */
    @Override
    @Transactional
    public LoginResponseDto login(String kakaoAccessToken, Long userId, HttpServletResponse response) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new InternalException("존재 하지 않은 유저 예외")
        );

        // 검증
        LoginFilterResponseDto responseDto =
                loginFilter(kakaoAccessToken, user.getKakaoRefreshToken(), user.getId()).getBody();

        user = userRepository.findById(responseDto.getUserId()).orElseThrow(
                () -> new InternalException("존재 하지 않은 유저 예외")
        );

        // 카카오 액세스 토큰, JWT 액세스 토큰 발급
        response.setHeader(HttpHeaders.AUTHORIZATION, responseDto.getAccessToken());
        response.setHeader("Refresh-Token", responseDto.getRefreshToken());
        response.setHeader("Kakao-Access-Token", responseDto.getKakaoAccessToken());

        // 카카오 리프레시 토큰, JWT 리프레시 토큰 갱신
        user.setKakaoRefreshToken(responseDto.getKakaoRefreshToken());
        user.setRefreshToken(responseDto.getRefreshToken());

        return new LoginResponseDto(user.getId());
    }

    /* 카카오 로그인 */
    @Override
    public KakaoDto.Token kakaoLogin(String code) {
        return kakaoService.getKakaoToken(code);
    }

    /* JWT 재발급 필터 호출하는 함수 */
    @Transactional
    public void reissueToken(String refreshToken, HttpServletResponse response){
        ReissueFilterResponseDto responseDto = webClient.post()
                .uri("/reissue")
                .header("Refresh-Token", refreshToken)
                .retrieve()
                .toEntity(ReissueFilterResponseDto.class)
                .block()
                .getBody();

        User user = userRepository.findById(responseDto.getUserId()).orElseThrow(
                () -> new InternalException("존재 하지 않은 유저 예외")
        );

        response.setHeader("Refresh-Token", responseDto.getRefreshToken());
        response.setHeader(HttpHeaders.AUTHORIZATION, responseDto.getAccessToken());

        user.setRefreshToken(responseDto.getRefreshToken());
    }

    /* AuthenticationManager가 User를 검증하는 함수 */
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        User user = userRepository.findById(Long.valueOf(id)).orElseThrow(
                () -> new InternalException("필터 오류 예외")
        );

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return org.springframework.security.core.userdetails.User.builder()
                .username(id)
                .password(passwordEncoder.encode(String.valueOf(user.getId())))
                .authorities("BASIC")
                .build();
    }

    /* 로그인 필터 호출하는 함수 */
    public ResponseEntity<LoginFilterResponseDto> loginFilter(String kakaoAccessToken, String kakaoRefreshToken, Long userId) {

        LoginFilterRequestDto loginFilterRequestDto = LoginFilterRequestDto.builder()
                .kakaoAccessToken(kakaoAccessToken)
                .kakaoRefreshToken(kakaoRefreshToken)
                .userId(userId)
                .build();

        //필터 호출
        return webClient.post()
                .uri("/login")
                .body(Mono.just(loginFilterRequestDto), LoginFilterRequestDto.class)
                .retrieve()
                .toEntity(LoginFilterResponseDto.class)
                .block();
    }
}
