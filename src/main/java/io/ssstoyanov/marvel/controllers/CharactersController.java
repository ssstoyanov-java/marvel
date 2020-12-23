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

@Tag(name = "Character API", description = "Provides methods for working with characters")
@RequestMapping("/v1/public/characters")
@RestController
public class CharactersController {

    private final CharacterRepository characterRepository;
    private final ComicRepository comicRepository;

    public CharactersController(CharacterRepository characterRepository, ComicRepository comicRepository) {
        this.characterRepository = characterRepository;
        this.comicRepository = comicRepository;
    }

    @Operation(summary = "Return all characters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Characters found")
    })
    @RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Iterable<Character>> getIterableComicCharacters(Pageable pageable) {
        return new ResponseEntity<>(characterRepository.findAll(pageable), HttpStatus.OK);
        // TODO: 23/12/2020 add filtering
    }


    @Operation(summary = "Return character by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Character found"),
            @ApiResponse(responseCode = "404", description = "Character not found")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Character> getCharacterById(@PathVariable(value = "id") Long id) {
        return characterRepository.findById(id)
                .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Return comics by character id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comics found"),
            @ApiResponse(responseCode = "404", description = "Comics not found")
    })
    @RequestMapping(value = "/{id}/comics", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Iterable<Comic>> getComicsByCharacterId(@PathVariable(value = "id") Long id, Pageable pageable) {
        return new ResponseEntity<>(comicRepository.findComicsByCharactersId(id, pageable), HttpStatus.OK);
        // TODO: 23/12/2020 add 404 logic 
    }

    @Transactional
    @Operation(summary = "Insert a new character")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Character created"),
            @ApiResponse(responseCode = "422", description = "Character already exist")
    })
    @RequestMapping(value = "", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<Character> createCharacter(@RequestBody Character character) {
        return characterRepository.existsById(character.getId()) ?
                new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY) :
                new ResponseEntity<>(characterRepository.save(character), HttpStatus.CREATED);
    }

    @Transactional
    @Operation(summary = "Update an existing character")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Character not exist"),
            @ApiResponse(responseCode = "200", description = "Character updated")
    })
    @RequestMapping(value = "", method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity<Character> updateCharacter(@RequestBody Character character) {
        return characterRepository.existsById(character.getId()) ?
                new ResponseEntity<>(characterRepository.save(character), HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
