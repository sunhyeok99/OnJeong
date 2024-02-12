package com.a503.onjeong.domain.userGame.service;

import com.a503.onjeong.domain.game.repository.GameRepository;
import com.a503.onjeong.domain.user.repository.UserRepository;
import com.a503.onjeong.domain.userGame.UserGame;
import com.a503.onjeong.domain.userGame.dto.UserGameDto;
import com.a503.onjeong.domain.userGame.repository.UserGameRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserGameServiceImpl implements UserGameService {

    private final UserGameRepository userGameRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;

    // 랭킹 리스트 점수 top10개 반환
    @Override
    @Transactional
    // gameId에 해당하는 유저들의 score를 불러와서
    // userGameDto로 전환후 return
    public List<UserGameDto> userGameList(Long gameId) {
        List<UserGame> userGameList = userGameRepository.findTop10ByGameIdOrderByUserGameScoreDesc(gameId);
        List<UserGameDto> userGameDtoList = userGameList.stream()
                .map(userGame -> UserGameDto.builder()
                        .id(userGame.getId())
                        .userId(userGame.getUser().getId())
                        .userName(userGame.getUser().getName())
                        .gameId(userGame.getGame().getId())
                        .userGameScore(userGame.getUserGameScore())
                        .build())
                .collect(Collectors.toList());

        return userGameDtoList;
    }
    @Override
    @Transactional
    // 게임에 관한 정보를 db에 저장
    public UserGame save(UserGameDto userGameDto) {
        UserGame userGame = UserGame.builder()
                .user(userRepository.findById(userGameDto.getUserId()).orElseThrow(() -> new EntityNotFoundException("User not found")))
                .game(gameRepository.findById(userGameDto.getGameId()).orElseThrow(() -> new EntityNotFoundException("Game not found")))
                .userGameScore(userGameDto.getUserGameScore())
                .build();
        return userGameRepository.save(userGame);
    }
    @Override
    @Transactional
    // 만약 방금 플레이한 점수(input)가 더 높을 경우 점수 갱신
    public UserGame updateScore(UserGameDto userGameDto) {
        UserGame userGame = userGameRepository.findByUserIdAndGameId(userGameDto.getUserId(), userGameDto.getGameId());
        if (userGameDto.getUserGameScore() > userGame.getUserGameScore()) {
            userGame.setUserGameScore(userGameDto.getUserGameScore());
        }
        return userGame;
    }

    @Override
    // userId와 gameId에 해당하는 유저 정보 반환
    public UserGameDto userGameDetails(Long userId, Long gameId) {
        UserGame userGame = userGameRepository.findByUserIdAndGameId(userId, gameId);
        UserGameDto userGameDto = UserGameDto.builder()
                .id(userGame.getId())
                .userId(userGame.getUser().getId())
                .userName(userGame.getUser().getName())
                .gameId(userGame.getGame().getId())
                .userGameScore(userGame.getUserGameScore())
                .build();
        return userGameDto;
    }


}
