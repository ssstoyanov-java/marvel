package io.ssstoyanov.marvel.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@Document(collection = "characters")
@TypeAlias("character")
public class Character {
    @Id
    private Long id;

    private String name;

    private String description;

    private Thumbnail thumbnail;
    private ArrayList<Comic> comics;

    @Getter
    @Setter
    @AllArgsConstructor
    private static class Thumbnail {
        private String path;
        private String extension;
    }

}
