package com.a503.onjeong.domain.videocall.controller;

import com.a503.onjeong.global.firebase.service.FirebaseService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notification")
@AllArgsConstructor
public class TestController {

    private final FirebaseService firebaseService;

    @PostMapping
    public void createNotification(@RequestBody NotiDto notiDto){
        System.out.println(notiDto);
        String response = firebaseService.sendNotification(notiDto.getToken(), notiDto.getContent());
        System.out.println("data is " + notiDto.getContent());
        System.out.println(response);
    }

}