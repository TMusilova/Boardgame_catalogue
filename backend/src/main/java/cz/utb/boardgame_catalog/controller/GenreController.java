package cz.utb.boardgame_catalog.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import cz.utb.boardgame_catalog.model.Genre;
import cz.utb.boardgame_catalog.repository.GenreRepository;

@RestController
public class GenreController {

    private final GenreRepository genreRepository;

    public GenreController(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    /**
     * Get all genres from the database.
     *
     * @return List of all genres.
     */
    @GetMapping("/api/genres")
    public List<Genre> getGenres() {
        return genreRepository.findAllByOrderByNameAsc();
    }

    /**
     * Get the count of all genres in the database.
     *
     * @return Count of all genres.
     */
    @GetMapping("/api/genres/count")
    public long getGenresCount() {
        return genreRepository.count();
    }

    /**
     * Get a genre by its ID.
     *
     * @param id The ID of the genre to retrieve.
     * @return ResponseEntity containing the genre if found, or a 404 status if not
     *         found.
     */
    @GetMapping("/api/genres/{id}")
    public ResponseEntity<?> getGenreById(@PathVariable Integer id) {
        Optional<Genre> genre = genreRepository.findGenreById(id);

        if (genre.isEmpty()) {
            return ResponseEntity.status(404).body("Genre with ID " + id + " not found.");
        }

        return ResponseEntity.status(200).body(genre);
    }
}
