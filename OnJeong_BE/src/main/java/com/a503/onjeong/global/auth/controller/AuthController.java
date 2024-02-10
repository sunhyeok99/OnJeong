package com.a503.onjeong.global.auth.controller;

import com.a503.onjeong.global.auth.dto.KakaoDto;
import com.a503.onjeong.global.auth.dto.LoginInfoResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;

@RequestMapping("/auth")
@RestController
public interface AuthController {
    @GetMapping("/signup")
    Long signup(@RequestHeader("Kakao-Access-Token") String kakaoAccessToken,
                @RequestHeader("Kakao-Refresh-Token") String kakaoRefreshToken,
                @RequestParam String phoneNumber, HttpServletResponse response);

    @GetMapping("/login")
    ResponseEntity<LoginInfoResponseDto> login(@RequestHeader(value = "Kakao-Access-Token") String kakaoAccessToken,
                                               @RequestParam Long userId, HttpServletResponse response) throws UnknownHostException, IllegalAccessException;

    @GetMapping("/kakao/redirect")
    KakaoDto.Token kakaoRedirect(@RequestParam("code") String code);

    @GetMapping("/reissue")
    public void reissue(@RequestHeader(value = "Refresh-Token") String refreshToken,
                        HttpServletResponse response);

    @GetMapping("/phone")
    public String phoneVerification(@RequestParam String phoneNumber);

    @GetMapping("/logout")
    public void logout();
}
