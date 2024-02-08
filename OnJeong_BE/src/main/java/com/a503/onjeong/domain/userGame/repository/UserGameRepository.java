package com.a503.onjeong.domain.userGame.repository;

import com.a503.onjeong.domain.userGame.UserGame;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserGameRepository extends JpaRepository<UserGame, Long> {

    // 해당 유저의 게임에 해당하는 점수를 반환해서 있으면 update, 없으면 save할거임
    UserGame findByUserIdAndGameId(Long userId, Long gameId);

    // 랭킹에서 점수가 높은 10개의 레코드 가져오기
    List<UserGame> findTop10ByGameIdOrderByUserGameScoreDesc(Long gameId);

}
