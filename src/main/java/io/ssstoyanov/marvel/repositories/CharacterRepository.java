package io.ssstoyanov.marvel.repositories;

import io.ssstoyanov.marvel.entities.Character;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacterRepository extends MongoRepository<Character, Long> {
    List<Character> findCharactersByComicsId(Long id, Pageable pageable);
}

