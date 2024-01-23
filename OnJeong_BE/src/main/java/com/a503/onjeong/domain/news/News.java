package com.a503.onjeong.domain.news;

import com.a503.onjeong.global.BaseEntity;
import jakarta.persistence.*;

import lombok.*;

@Entity
@Builder
<<<<<<< HEAD
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
=======
@Getter @Setter
@NoArgsConstructor
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
>>>>>>> 85ca971af4c43f0b48c3898bb60ae544ba344aaf
public class News {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_id")
    private Long Id;
<<<<<<< HEAD
    @Column(name = "news_title")
    private String title;
=======
    @Column(name = "news_name")
    private String name;
>>>>>>> 85ca971af4c43f0b48c3898bb60ae544ba344aaf
    @Column(name = "news_category")
    private int category;
    @Column(name = "news_description")
    private String description;
    @Column(name = "news_url")
    private String url;
    @Column(name = "news_image")
    private String image;

<<<<<<< HEAD
    @Builder
    public News(
            Long Id,
            String title,
            int category,
            String description,
            String url,
            String image
    ){
        this.Id = Id;
        this.title = title;
        this.category = category;
        this.description = description;
        this.url = url;
        this.image = image;
    }
=======
>>>>>>> 85ca971af4c43f0b48c3898bb60ae544ba344aaf
}
