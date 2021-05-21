package com.tango.DTO;

import com.tango.models.films.film.Film;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FilmRequest {
    List<Long> genres = new ArrayList<>();
    String title;
    Double desc_rating;
    String desc_image;
    String desc_preview;
    String desc_text;
    String film_link;

    public Film toFilmWithoutGenre() {
        return new Film(title,
                desc_rating,
                desc_image,
                desc_preview,
                desc_text,
                film_link);
    }
}
