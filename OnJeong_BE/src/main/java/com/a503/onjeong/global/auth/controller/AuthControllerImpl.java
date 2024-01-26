package com.a503.onjeong.global.auth.controller;

import com.a503.onjeong.global.auth.dto.KakaoDto;
import com.a503.onjeong.global.auth.dto.LoginResponseDto;
import com.a503.onjeong.global.auth.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;


/* 인증, 인가 관련 컨트롤러 */
@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthControllerImpl implements AuthController {

    private final AuthService authService;

    /* 회원가입 */
    @GetMapping("/signup")
    public Long signup(@RequestHeader("Kakao-Access-Token") String kakaoAccessToken,
                       @RequestHeader("Kakao-Refresh-Token") String kakaoRefreshToken,
                       @RequestParam String phoneNumber, HttpServletResponse response) {
        return authService.signup(kakaoAccessToken, kakaoRefreshToken, phoneNumber, response);
    }

    /* 자동 로그인 */
    @GetMapping("/login")
    public Long login(@RequestHeader(value = "Kakao-Access-Token") String kakaoAccessToken,
                      @RequestParam Long userId, HttpServletResponse response) throws UnknownHostException, IllegalAccessException {
        return authService.login(kakaoAccessToken, userId, response);
    }

    /* 인가 코드를 통해 토큰 발급 */
    @GetMapping("/kakao/token")
    public KakaoDto.Token kakaoRedirect(@RequestParam("code") String code) {
        return authService.kakaoLogin(code);
    }

    /* JWT 리프레시 토큰으로 재발급 */
    @GetMapping("/reissue")
    public void reissue(@RequestHeader(value = "Refresh-Token") String refreshToken,
                        HttpServletResponse response) {
        authService.reissueToken(refreshToken, response);
    }

    /* 전화번호 인증 */
    @GetMapping("/phone")
    public String phoneVerification(@RequestParam String phoneNumber) {
        return authService.phoneVerification(phoneNumber);
    }

    /* 로그아웃 */
    @GetMapping("/logout")
    public void logout() {
        // 나중에
    }

}
