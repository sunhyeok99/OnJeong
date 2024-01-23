package com.a503.onjeong.domain.news.controller;

import com.a503.onjeong.domain.news.News;
import com.a503.onjeong.domain.news.service.NewsService;
<<<<<<< HEAD
import lombok.RequiredArgsConstructor;
=======
>>>>>>> 85ca971af4c43f0b48c3898bb60ae544ba344aaf
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/news")
<<<<<<< HEAD
@RequiredArgsConstructor
public class NewsControllerImpl implements NewsController {

    private final NewsService newsService;

    @GetMapping("/lists")
    // 뉴스리스트 받아옴
    public List<News> newsList() {

=======
public class NewsControllerImpl implements NewsController {
    @Autowired
    private final NewsService newsService;

    public NewsControllerImpl(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/getNews")
    public List<News> getNews() throws IOException {
        System.out.println("연결!!");
>>>>>>> 85ca971af4c43f0b48c3898bb60ae544ba344aaf
        return newsService.getNewsDatas();
    }
}