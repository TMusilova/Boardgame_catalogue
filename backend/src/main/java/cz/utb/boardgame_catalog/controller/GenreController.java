package cz.utb.boardgame_catalog.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
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
        return genreRepository.findAll();
    }
}
