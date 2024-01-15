package com.a503.onjeong.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController("/auth")
public class AuthController {


    // 카카오 액세스 토큰으로 판단
    @GetMapping("/login")
    public void login(){
    }

    // 카카오 로그인 인가 코드 받기
    @GetMapping("/kakao/login")
    public void kakaoLogin(@RequestParam String code){
    }
}
