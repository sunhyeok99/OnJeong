package com.a503.onjeong.domain.user.service;

import com.a503.onjeong.domain.user.dto.FcmTokenDto;
import com.a503.onjeong.domain.user.dto.UserDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface UserService {
    void updateFcmToken(FcmTokenDto fcmTokenDto);

    void deleteProfileImg(Long userId);

    void updateProfileImg(Long userId, MultipartFile file) throws IOException;

    UserDTO getUserInfo(Long userId);
}
