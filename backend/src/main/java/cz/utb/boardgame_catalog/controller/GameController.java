package cz.utb.boardgame_catalog.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cz.utb.boardgame_catalog.model.Game;
import cz.utb.boardgame_catalog.repository.GameRepository;

@RestController
public class GameController {

    private final GameRepository gameRepository;
    private final MongoTemplate mongoTemplate;

    public GameController(GameRepository gameRepository, MongoTemplate mongoTemplate) {
        this.gameRepository = gameRepository;
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * Get all games from the database.
     *
     * @return List of all games.
     */
    @GetMapping("/api/games")
    public List<Game> getGames() {
        return gameRepository.findAllByOrderByNameAsc();
    }

    /**
     * Get the count of all games in the database.
     *
     * @return Count of all games.
     */
    @GetMapping("/api/games/count")
    public long getGamesCount() {
        return gameRepository.count();
    }

    /**
     * Get a game by its ID.
     *
     * @param id The ID of the game to retrieve.
     * @return ResponseEntity containing the game if found, or a 404 status if not
     *         found.
     */
    @GetMapping("/api/games/{id}")
    public ResponseEntity<?> getGameById(@PathVariable Integer id) {
        Optional<Game> game = gameRepository.findGameById(id);

        if (game.isEmpty()) {
            return ResponseEntity.status(404).body("Game with ID " + id + " not found.");
        }

        return ResponseEntity.status(200).body(game);
    }

    /**
     * Search for games based on a text query.
     *
     * @param query The text to search for.
     * @return ResponseEntity containing the list of matching games.
     */
    @GetMapping("/api/games/search")
    public ResponseEntity<?> getGameBySearch(@RequestParam String query) {
        TextCriteria criteria = TextCriteria.forDefaultLanguage().matching(query);
        Query searchQuery = TextQuery.queryText(criteria);

        List<Game> games = mongoTemplate.find(searchQuery, Game.class);
        return ResponseEntity.status(200).body(games);
    }
}
