package com.a503.onjeong.global.auth.filter;

import com.a503.onjeong.domain.user.User;
import com.a503.onjeong.domain.user.repository.UserRepository;
import com.a503.onjeong.global.util.JwtUtil;
import com.sun.jdi.InternalException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.time.LocalDateTime;

/* JWT 재발급 필터 */
public class JwtReissueFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationEntryPoint authenticationEntryPoint;

    private final UserRepository userRepository;

    private static final String BEARER = "Bearer ";

    private static final String REFRESH_TOKEN = "Refresh-Token";

    public JwtReissueFilter(AuthenticationManager authenticationManager,
                            UserRepository userRepository,
                            AuthenticationEntryPoint authenticationEntryPoint) {
        super(authenticationManager);
        this.userRepository = userRepository;
        this.authenticationEntryPoint = authenticationEntryPoint;
        setFilterProcessesUrl("/reissue");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) {
        String refreshToken = request.getHeader(REFRESH_TOKEN).substring(BEARER.length());
        User user = null;
        Long userId = null;

        // 받은 리프레시 토큰 유효성 검사
        try {
            if (JwtUtil.verify(refreshToken)) {
                userId = Long.valueOf(JwtUtil.getSubject(refreshToken));
                user = userRepository.findById(userId).orElseThrow(
                        () -> new InternalException("필터 예외 처리")
                );
            }
        } catch (Exception e) {
            throw new InternalException("리프레시 토큰 만료");
        }

        String refreshTokenDb = user.getRefreshToken().substring(BEARER.length());
        Long userIdDb = null;

        // db에 있는 리프레시 토큰 유효성 검사
        try {
            if (JwtUtil.verify(refreshTokenDb)) {
                userIdDb = Long.valueOf(JwtUtil.getSubject(refreshToken));
            }
        } catch (Exception e) {
            throw new InternalException("리프레시 토큰 만료");
        }

        // 받은 리프레시 토큰과 db에 있는 리프레시 토큰 체크 (검증)
        if (!userId.equals(userIdDb)) {
            throw new InternalException("잘못된 토큰 예외");
        }

        // 시큐리티 검증
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userId, userId
        );
        return getAuthenticationManager().authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult) throws IOException {

        String userId = authResult.getName();
        User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(
                () -> new InternalException("필터 예외")
        );

        String accessToken = BEARER + JwtUtil.createAccessToken(userId, LocalDateTime.now());
        String refreshToken = BEARER + JwtUtil.createRefreshToken(userId, LocalDateTime.now());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");

        JSONObject json = new JSONObject();

        json.put("userId", user.getId());
        json.put("accessToken", BEARER + accessToken);
        json.put("refreshToken", BEARER + refreshToken);
        response.getWriter().write(String.valueOf(json));
    }

}