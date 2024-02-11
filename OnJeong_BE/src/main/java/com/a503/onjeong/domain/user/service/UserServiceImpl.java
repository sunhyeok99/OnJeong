package com.a503.onjeong.domain.user.service;

import com.a503.onjeong.domain.user.User;
import com.a503.onjeong.domain.user.dto.FcmTokenRequestDto;
import com.a503.onjeong.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public void updateFcmToken(FcmTokenRequestDto fcmTokenRequestDto) {
        Long userId = fcmTokenRequestDto.getUserId();
        String fcmToken = fcmTokenRequestDto.getFcmToken();

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return;
        if (fcmToken.equals(user.getFcmToken())) return;

        user.setFcmToken(fcmToken);
        userRepository.save(user);
    }
}
