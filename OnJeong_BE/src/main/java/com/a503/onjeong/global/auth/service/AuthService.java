package com.a503.onjeong.global.auth.service;

import com.a503.onjeong.global.auth.dto.KakaoDto;
import com.a503.onjeong.global.auth.dto.LoginResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;

@Service
public interface AuthService {

    void signup(String kakaoAccessToken, String kakaoRefreshToken,
                String phoneNumber, HttpServletResponse response);

    LoginResponseDto login(String kakaoAccessToken, Long userId, HttpServletResponse response) throws UnknownHostException, IllegalAccessException;

    KakaoDto.Token kakaoLogin(String code);

    void reissueToken(String refreshToken, HttpServletResponse response);

    UserDetails loadUserByUsername(String id) throws UsernameNotFoundException;

}
