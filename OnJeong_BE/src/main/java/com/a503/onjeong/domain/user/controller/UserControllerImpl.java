package com.a503.onjeong.domain.user.controller;

import com.a503.onjeong.domain.user.dto.FcmTokenDto;
import com.a503.onjeong.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService userService;

    @Override
    @PatchMapping("/fcm-token")
    public ResponseEntity<Void> updateFcmToken(@RequestBody FcmTokenDto fcmTokenDto) {
        userService.updateFcmToken(fcmTokenDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
