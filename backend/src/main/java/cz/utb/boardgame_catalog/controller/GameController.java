package cz.utb.boardgame_catalog.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import cz.utb.boardgame_catalog.model.Game;
import cz.utb.boardgame_catalog.repository.GameRepository;

@RestController
public class GameController {

    private final GameRepository gameRepository;

    public GameController(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @GetMapping("/api/games")
    public List<Game> getGames() {
        return gameRepository.findAll();
    }
}
