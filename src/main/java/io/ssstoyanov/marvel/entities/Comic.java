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
@Document(collection = "comics")
@TypeAlias("comic")
public class Comic {
    @Id
    private Long id;

    private String title;

    private ArrayList<Character> characters;

}
