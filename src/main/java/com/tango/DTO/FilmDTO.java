package com.tango.DTO;

import com.tango.models.films.film.Film;
import com.tango.models.films.genre.Genre;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FilmDTO {
    Film film;
    List<Genre> genres;
}
