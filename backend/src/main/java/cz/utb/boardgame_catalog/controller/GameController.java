package cz.utb.boardgame_catalog.controller;

import java.time.Year;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cz.utb.boardgame_catalog.model.Game;
import cz.utb.boardgame_catalog.repository.GameRepository;
import cz.utb.boardgame_catalog.repository.GenreRepository;

@RestController
public class GameController {

    private final GameRepository gameRepository;
    private final GenreRepository genreRepository;
    private final MongoTemplate mongoTemplate;

    public GameController(GameRepository gameRepository, GenreRepository genreRepository,
            MongoTemplate mongoTemplate) {
        this.gameRepository = gameRepository;
        this.genreRepository = genreRepository;
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

    /**
     * Validate the attributes of a game object.
     *
     * @param game The game object to validate, except _id and id.
     * @return ResponseEntity indicating validation errors, or null if validation
     *         passes.
     */
    private ResponseEntity<?> validateGame(Game game) {
        if (game.getName() == null) {
            return ResponseEntity.badRequest().body("Game name is missing.");
        }
        String name = game.getName().trim();
        if (name.length() <= 1 || name.length() > 500) {
            return ResponseEntity.status(400).body("Game name has wrong length or is missing.");
        }
        game.setName(name);

        if (game.getPrice() != null && game.getPrice() < 0) {
            return ResponseEntity.status(400).body("Game price is invalid.");
        }
        if (game.getYear() != null && game.getYear() > Year.now().getValue()) {
            return ResponseEntity.status(400).body("Game year is invalid.");
        }

        if (game.getDescription() == null) {
            return ResponseEntity.badRequest().body("Game description is missing.");
        }
        String descr = game.getDescription().trim();
        if (descr.length() <= 2 || descr.length() > 5000) {
            return ResponseEntity.status(400).body("Game description is too long or is missing.");
        }
        game.setDescription(descr);

        if (game.getGenre_id() == null || game.getGenre_id() <= 0) {
            return ResponseEntity.status(400).body("Genre ID is invalid or missing.");
        }
        // check genre exists
        if (genreRepository.findGenreById(game.getGenre_id()).isEmpty()) {
            return ResponseEntity.status(400).body("Genre does not exist.");
        }

        return null;
    }

    /**
     * Create a new game in the database.
     *
     * @param game The game object to be created.
     * @return ResponseEntity indicating the result of the creation operation.
     */
    @PostMapping("/api/games")
    public ResponseEntity<?> createGame(@RequestBody Game game) {
        if (game.getId() == null || game.getId() <= 0) {
            return ResponseEntity.status(400).body("Game ID is invalid or missing.");
        }

        // atribute validation
        ResponseEntity<?> validationResponse = validateGame(game);
        if (validationResponse != null) {
            return validationResponse;
        }

        // check game id does not exist yet
        if (gameRepository.findGameById(game.getId()).isPresent()) {
            return ResponseEntity.status(409).body("Game with this ID already exists.");
        }

        gameRepository.save(game);
        return ResponseEntity.status(201).body("Game created successfully.");
    }

    @PutMapping("/api/games/{id}")
    public ResponseEntity<?> editGame(@PathVariable Integer id, @RequestBody Game game) {
        // check id
        if (game.getId() == null || game.getId() <= 0) {
            return ResponseEntity.status(400).body("Game ID is invalid or missing.");
        }

        // check game id in body matches url parameter
        if (!id.equals(game.getId())) {
            return ResponseEntity.status(400).body("Game ID in the body does not match the URL parameter.");
        }

        // check the game exists
        Game existing = gameRepository.findGameById(game.getId()).orElse(null);
        if (existing == null) {
            return ResponseEntity.status(404).body("Game not found.");
        }

        // atribute validation
        ResponseEntity<?> validationResponse = validateGame(game);
        if (validationResponse != null) {
            return validationResponse;
        }

        // preserve the existing _id field to avoid creating a new document in MongoDB
        game.set_id(existing.get_id());

        gameRepository.save(game);
        return ResponseEntity.status(200).body("Game edited successfully.");
    }
}
