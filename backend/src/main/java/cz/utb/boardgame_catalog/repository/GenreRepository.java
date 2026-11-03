package cz.utb.boardgame_catalog.repository;

import cz.utb.boardgame_catalog.model.Genre;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface GenreRepository extends MongoRepository<Genre, String> {
    List<Genre> findAllByOrderByNameAsc();

    Optional<Genre> findGenreById(Integer id);
}
