package com.a503.onjeong.domain.user;

import com.a503.onjeong.domain.attachment.Attachment;
import com.a503.onjeong.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long Id;

    @Column(name = "kakao_id")
    private Long kakaoId;

    @Column(name = "name")
    private String name;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false)
    private UserType type;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Builder
    public User(
            Long kakaoId,
            String name,
            UserType type,
            String refreshToken
    ){
        this.kakaoId = kakaoId;
        this.name = name;
        this.type = type;
        this.refreshToken = refreshToken;
    }

    public void savePhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }
}
