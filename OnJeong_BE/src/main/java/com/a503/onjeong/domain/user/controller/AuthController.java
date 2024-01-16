package com.a503.onjeong.domain.user.controller;

import com.a503.onjeong.domain.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

    private final AuthService authService;

    /* 자동 로그인 (JWT 토큰 활용) */
    @GetMapping("/login")
    public void login(@RequestHeader(value="Authorization") String accessToken) throws UnknownHostException {
        authService.login(accessToken);
    }

    /* 로그아웃 */
    @GetMapping("/logout")
    public void logout(){
        // 나중에
    }

    /* 클라에서 인가 코드 받은 후 로그인 및 회원가입 처리 */
    @GetMapping("kakao/login")
    public void kakaoLogin(@RequestParam String code) throws UnknownHostException {
        authService.kakaoLoginOrSignUp(code);
    }

    /* 전화번호 저장 */
    @GetMapping("/phone-number")
    public void savePhoneNumber(@RequestParam String phoneNumber,
                                @RequestParam Long userId){
        authService.savePhoneNumber(userId, phoneNumber);
    }

}
