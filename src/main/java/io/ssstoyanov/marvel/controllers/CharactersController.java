package io.ssstoyanov.marvel.controllers;

import io.ssstoyanov.marvel.entities.Character;
import io.ssstoyanov.marvel.entities.Comic;
import io.ssstoyanov.marvel.repositories.CharacterRepository;
import io.ssstoyanov.marvel.repositories.ComicRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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

    @Operation(summary = "Return all characters",
            description = "Returns a page with all characters, filtering by the parameters size, page, sort is available")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Characters found")
    })
    @RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Page<Character>> getPageOfCharacters(Pageable pageable) {
        return new ResponseEntity<>(characterRepository.findAll(pageable), HttpStatus.OK);
    }

    @Operation(summary = "Return character by name",
            description = "Returns character, if exist. Search in name and description field")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Character probably found"),
            @ApiResponse(responseCode = "404", description = "Character not found")
    })
    @RequestMapping(value = "/search", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Page<Character>> getCharacterByName(@RequestParam(value = "name") String name, Pageable page) {
        return characterRepository.findByNameAndDescriptionContains(name, name, page)
                .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
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

    @Operation(summary = "Return comics by character id",
            description = "Returns a page with all character comics, filtering by the parameters size, page, sort is available")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comics found"),
            @ApiResponse(responseCode = "404", description = "Comics not found")
    })
    @RequestMapping(value = "/{id}/comics", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Page<Comic>> getComicsByCharacterId(@PathVariable(value = "id") Long id, Pageable pageable) {
        return comicRepository.findComicsByCharactersId(id, pageable)
                .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Transactional
    @Operation(summary = "Insert a new character")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Character created"),
            @ApiResponse(responseCode = "422", description = "Character already exist")
    })
    @RequestMapping(value = "", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<Character> insertCharacter(@Valid @RequestBody Character character) {
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
    public ResponseEntity<Character> updateCharacter(@Valid @RequestBody Character character) {
        return characterRepository.existsById(character.getId()) ?
                new ResponseEntity<>(characterRepository.save(character), HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
