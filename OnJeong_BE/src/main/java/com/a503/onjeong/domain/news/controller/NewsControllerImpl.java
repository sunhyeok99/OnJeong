package com.a503.onjeong.domain.news.controller;

import com.a503.onjeong.domain.news.News;
import com.a503.onjeong.domain.news.service.NewsService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class NewsControllerImpl implements NewsController {

    private final NewsService newsService;

    @GetMapping("/lists")
    // 뉴스리스트 받아옴
    public List<News> newsList() {

        return newsService.getNewsDatas();

    }
}