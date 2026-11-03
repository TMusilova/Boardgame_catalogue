package cz.utb.boardgame_catalog.repository;

import cz.utb.boardgame_catalog.model.Game;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GameRepository extends MongoRepository<Game, String> {

}
