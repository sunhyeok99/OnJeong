package com.a503.onjeong.domain.news;

import com.a503.onjeong.global.BaseEntity;
import jakarta.persistence.*;

import lombok.*;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class News {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_id")
    private Long Id;
    @Column(name = "news_title")
    private String title;
    @Column(name = "news_category")
    private int category;
    @Column(name = "news_description")
    private String description;
    @Column(name = "news_url")
    private String url;
    @Column(name = "news_image")
    private String image;

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
}
