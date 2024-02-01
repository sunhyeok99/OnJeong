package com.a503.onjeong.domain.userGame.service;

import com.a503.onjeong.domain.game.Game;
import com.a503.onjeong.domain.userGame.UserGame;
import com.a503.onjeong.domain.userGame.dto.UserGameDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserGameService {

        // 전체의 랭킹반환(받아서 게임마다 top10만 전송)
        public List<UserGame> userGameList(Long gameId);

        public UserGame save(UserGameDto userGameDto);

        public UserGame updateScore(UserGameDto userGameDto);

        public UserGame userGameDetails(Long userId, Long gameId);



        }
