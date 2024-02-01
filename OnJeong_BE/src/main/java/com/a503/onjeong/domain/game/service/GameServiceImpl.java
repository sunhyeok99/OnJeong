package com.a503.onjeong.domain.game.service;

import com.a503.onjeong.domain.game.Game;
import com.a503.onjeong.domain.game.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService{

    private final GameRepository gameRepository;

    // 랭킹 리스트 전체 반환
//    @Override
//    public List<Game> gameList() {
//        return gameRepository.findAll();
//    }

}
