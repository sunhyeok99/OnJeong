package com.a503.onjeong.domain.news.repository;

import com.a503.onjeong.domain.news.News;
<<<<<<< HEAD
=======
import com.a503.onjeong.domain.user.User;
>>>>>>> 85ca971af4c43f0b48c3898bb60ae544ba344aaf
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

<<<<<<< HEAD
//    Optional<News> findByCategory(int category);
=======
//    Optional<News> findById(Long aLong);
>>>>>>> 85ca971af4c43f0b48c3898bb60ae544ba344aaf

}
