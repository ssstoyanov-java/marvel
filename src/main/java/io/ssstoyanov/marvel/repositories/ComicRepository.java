package io.ssstoyanov.marvel.repositories;

import io.ssstoyanov.marvel.entities.Comic;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComicRepository extends MongoRepository<Comic, Long> {
    List<Comic> findComicsByCharactersId(Long id, Pageable page);
}
