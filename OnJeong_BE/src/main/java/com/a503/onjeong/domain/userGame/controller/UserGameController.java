package com.a503.onjeong.domain.userGame.controller;

import com.a503.onjeong.domain.userGame.UserGame;
import com.a503.onjeong.domain.userGame.dto.UserGameDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public interface UserGameController {
    public List<UserGame> topScoreList(@RequestParam Long gameId);

    public UserGame scoreSave(@RequestBody UserGameDto userGameDto);

        public UserGame scoreDetails(@RequestParam Long userId, Long gameId);


    }
