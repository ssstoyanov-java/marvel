package io.ssstoyanov.marvel.repositories;

import io.ssstoyanov.marvel.entities.Comic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ComicRepository extends MongoRepository<Comic, Long> {
    Optional<Page<Comic>> findComicsByCharactersId(Long id, Pageable page);

    Optional<Page<Comic>> findComicsByTitleContains(String title, Pageable page);
}
