package com.tango.services;

import com.tango.models.films.film.Film;
import com.tango.models.films.genre.FilmGenreRepository;
import com.tango.models.films.genre.Genre;
import com.tango.models.films.genre.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GenreService {

   private final GenreRepository genreRepository;
   private final FilmGenreRepository filmGenreRepository;

    public GenreService(@Autowired GenreRepository genreRepository,
                        @Autowired FilmGenreRepository filmGenreRepository) {
        this.genreRepository = genreRepository;
        this.filmGenreRepository = filmGenreRepository;
    }

    public List<Genre> getGenresByFilm(Film film) {
        return filmGenreRepository.findAllByFilmId(film.getFilmId());
    }

    public Page<Film> getFilmsByGenre(long genreId, Pageable pageable) {
        return filmGenreRepository.findAllByGenreId(genreId, pageable);
    }

    public Page<Film> getFilmsByGenre(long genreId, String search, Pageable pageable) {
        return filmGenreRepository.findAllByGenreIdAndTitle(genreId, search.trim() + "%", pageable);
    }
}
