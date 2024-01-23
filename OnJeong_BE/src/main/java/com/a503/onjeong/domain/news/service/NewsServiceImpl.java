package com.a503.onjeong.domain.news.service;

import com.a503.onjeong.domain.news.News;
import com.a503.onjeong.domain.news.repository.NewsRepository;
<<<<<<< HEAD
=======
import jakarta.annotation.PostConstruct;
>>>>>>> 85ca971af4c43f0b48c3898bb60ae544ba344aaf
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
<<<<<<< HEAD
=======
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
>>>>>>> 85ca971af4c43f0b48c3898bb60ae544ba344aaf
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
<<<<<<< HEAD
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;

    @Override
    // 크롤링해서 뉴스 데이터 받아옴
    public List<News> getNewsDatas() {
        // 먼저 db 초기화
        newsRepository.deleteAll();
        // 네이버 뉴스 사이트
=======
public class NewsServiceImpl implements NewsService {
    @Autowired
    NewsRepository newsRepository;

    @PostConstruct
    @Override
    public List<News> getNewsDatas() {
        newsRepository.deleteAll();
>>>>>>> 85ca971af4c43f0b48c3898bb60ae544ba344aaf
        final String NEWS_URL = "https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=";
        int[] urlCode = {100, 101, 102, 103, 104}; // 정치 / 경제 / 사회 / 생활 및 문화 / 세계
        List<News> newsList = new ArrayList<>();
        try {
            for (int index = 0; index < urlCode.length; index++) {
<<<<<<< HEAD
                Document document = Jsoup.connect(NEWS_URL + urlCode[index]).get(); // 주소뒤에 100, 101 이런식
=======

                Document document = Jsoup.connect(NEWS_URL + urlCode[index]).get(); // 주소뒤에 100, 101 이런식

>>>>>>> 85ca971af4c43f0b48c3898bb60ae544ba344aaf
                Elements content1 = document.select(".sh_text_headline"); // 제목 불러옴
                Elements content2 = document.select(".sh_text_lede"); // 내용 불러옴
                Elements content3 = document.select(".sh_thumb_link > img"); // 이미지 주소 불러옴

                // 각 카테고리 마다 5개씩 저장하기 위해
                // News 빈객체 만들고 안에 넣음
                for (int i = 0; i < 5; i++) {
                    String title = content1.get(i).text(); // 제목은 text만 추출
                    String description = content2.get(i).text(); // 설명도 text만 추출
                    String url = content1.get(i).absUrl("href"); // url은 제목의 a태그 추출
                    String image = content3.get(i).absUrl("src"); // image 사진 링크 추출

                    // news 객체를 생성해서 각 인자 설정
<<<<<<< HEAD
                    News news = News.builder()
                            .title(title)
                            .category(urlCode[index])
                            .description(description)
                            .url(url)
                            .image(image)
                            .build();
                    // newsList에 news 추가 (총 25개 생성)
                    newsList.add(news); // 리스트와 db에 추가
                    newsRepository.save(news);
                }
            }
        } catch (IOException e) {
            // 크롤링 중에 예외가 발생할 경우
            e.printStackTrace(); // 예외를 출력하거나 로깅하도록 수정해도 좋습니다.
        }
=======
                    News news = new News();
                    news.setName(title);
                    news.setCategory(urlCode[index]);
                    news.setDescription(description);
                    news.setUrl(url);
                    news.setImage(image);
                    // newsList에 news 추가 (총 20개 생성)
                    newsList.add(news); // 리스트와 db에 추가
                    newsRepository.save(news);
//                    System.out.println("Title: " + title);
//                    System.out.println("Description: " + description);
//                    System.out.println(url);
//                    System.out.println(image);
//                    System.out.println(index);
                }
            }
        } catch (IOException e) {
            // 크롤링 중에 예외가 발생할 경우 예외 처리 로직 추가
            e.printStackTrace(); // 예외를 출력하거나 로깅하도록 수정해도 좋습니다.
        }

>>>>>>> 85ca971af4c43f0b48c3898bb60ae544ba344aaf
        return newsList;
    }

}
