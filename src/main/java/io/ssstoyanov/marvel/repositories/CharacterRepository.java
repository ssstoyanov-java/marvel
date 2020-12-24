package io.ssstoyanov.marvel.repositories;

import io.ssstoyanov.marvel.entities.Character;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CharacterRepository extends MongoRepository<Character, Long> {
    Optional<Page<Character>> findCharactersByComicsId(Long id, Pageable page);

    Optional<Page<Character>> findByNameAndDescriptionContains(String name, String description, Pageable page);
}

