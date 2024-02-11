package com.a503.onjeong.domain.game.controller;

import com.a503.onjeong.domain.game.Game;
import com.a503.onjeong.domain.game.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/game")
public class GameControllerImpl implements GameController {


}
