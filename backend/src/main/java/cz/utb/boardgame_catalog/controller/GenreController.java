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

    @GetMapping("/api/genres")
    public List<Genre> getGenres() {
        return genreRepository.findAllByOrderByNameAsc();
    }

    @GetMapping("/api/genres/count")
    public long getGenresCount() {
        return genreRepository.count();
    }

    @GetMapping("/api/genres/{id}")
    public ResponseEntity<?> getGenreById(@PathVariable Integer id) {
        Optional<Genre> genre = genreRepository.findGenreById(id);

        if (genre.isEmpty()) {
            return ResponseEntity.status(404).body("Genre with ID " + id + " not found.");
        }

        return ResponseEntity.status(200).body(genre);
    }
}
