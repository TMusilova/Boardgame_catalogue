package cz.utb.boardgame_catalog.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
        return gameRepository.findAllByOrderByNameAsc();
    }

    @GetMapping("/api/games/count")
    public long getGamesCount() {
        return gameRepository.count();
    }

    @GetMapping("/api/games/{id}")
    public ResponseEntity<?> getGameById(@PathVariable Integer id) {
        Optional<Game> game = gameRepository.findGameById(id);

        if (game.isEmpty()) {
            return ResponseEntity.status(404).body("Game with ID " + id + " not found.");
        }

        return ResponseEntity.status(200).body(game);
    }
}
