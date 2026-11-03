package cz.utb.boardgame_catalog.repository;

import cz.utb.boardgame_catalog.model.Genre;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GenreRepository extends MongoRepository<Genre, String> {

}
