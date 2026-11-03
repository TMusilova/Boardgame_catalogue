package cz.utb.boardgame_catalog.repository;

import cz.utb.boardgame_catalog.model.Game;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface GameRepository extends MongoRepository<Game, String> {
    List<Game> findAllByOrderByNameAsc();

    Optional<Game> findGameById(Integer id);
}
