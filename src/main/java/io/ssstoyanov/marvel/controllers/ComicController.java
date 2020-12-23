package io.ssstoyanov.marvel.controllers;

import io.ssstoyanov.marvel.entities.Character;
import io.ssstoyanov.marvel.entities.Comic;
import io.ssstoyanov.marvel.repositories.CharacterRepository;
import io.ssstoyanov.marvel.repositories.ComicRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Comic API", description = "Provides methods for working with comics")
@RequestMapping("/v1/public/comics")
@RestController
public class ComicController {

    private final CharacterRepository characterRepository;
    private final ComicRepository comicRepository;

    public ComicController(CharacterRepository characterRepository, ComicRepository comicRepository) {
        this.characterRepository = characterRepository;
        this.comicRepository = comicRepository;
    }

    @Operation(summary = "Return all comics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comics found")
    })
    @RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Iterable<Comic>> getIterableComics(Pageable pageable) {
        return new ResponseEntity<>(comicRepository.findAll(pageable), HttpStatus.OK);
        // TODO: 23/12/2020 add filtering
    }

    @Operation(summary = "Return comic by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comic found"),
            @ApiResponse(responseCode = "404", description = "Comic not found")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Comic> getComicById(@PathVariable(value = "id") Long id) {
        return comicRepository.findById(id)
                .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Return characters by comic id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Characters found"),
            @ApiResponse(responseCode = "404", description = "Characters not found")
    })
    @RequestMapping(value = "/{id}/characters", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<Character>> getComicsByCharacterId(@PathVariable(value = "id") Long id, Pageable pageable) {
        return new ResponseEntity<>(characterRepository.findCharactersByComicsId(id, pageable), HttpStatus.OK);
        // TODO: 23/12/2020 add 404 logic
    }

    @Transactional
    @Operation(summary = "Insert a new comic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comic created"),
            @ApiResponse(responseCode = "422", description = "Comic already exist")
    })
    @RequestMapping(value = "", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<Comic> createComic(@RequestBody Comic comic) {
        return comicRepository.existsById(comic.getId()) ?
                new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY) :
                new ResponseEntity<>(comicRepository.save(comic), HttpStatus.CREATED);
    }

    @Transactional
    @Operation(summary = "Update an existing comic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Comic not exist"),
            @ApiResponse(responseCode = "200", description = "Comic updated")
    })
    @RequestMapping(value = "", method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity<Comic> updateComic(@RequestBody Comic comic) {
        return comicRepository.existsById(comic.getId()) ?
                new ResponseEntity<>(comicRepository.save(comic), HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
