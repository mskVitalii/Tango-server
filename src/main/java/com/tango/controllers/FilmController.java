package com.tango.controllers;

import com.tango.DTO.FilmDTO;
import com.tango.DTO.FilmRequest;
import com.tango.DTO.PaginationResponse;
import com.tango.models.films.film.Film;
import com.tango.services.FilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/film")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("{film_id}")
    public FilmDTO getFilm(@PathVariable("film_id") long filmId) {
        return filmService.getFilm(filmId);
    }

    @GetMapping("/list")
    public ResponseEntity<?> getFilms(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long genre) {

        System.out.println(genre);
        Page<FilmDTO> films;

        boolean hasSearch = search != null && !search.trim().isEmpty();
        boolean hasGenre = genre != null;

        // Поиск по жанру и по названию
        if (hasGenre && hasSearch)
            films = filmService.getFilms(genre, search, PageRequest.of(page, size));

        // Поиск только по названию
        else if (hasSearch)
            films = filmService.getFilms(search, PageRequest.of(page, size));

        // Поиск только по жанру
        else if (hasGenre)
            films = filmService.getFilms(genre, PageRequest.of(page, size));

        // Без ничего
        else
            films = filmService.getFilms(PageRequest.of(page, size));


        return ResponseEntity.ok(new PaginationResponse<>(films));
    }

    @PostMapping
    public FilmDTO addFilm(@RequestBody FilmRequest film) {
        return filmService.addFilm(film.toFilmWithoutGenre(), film.getGenres());
    }

    ////////////////////////////////////////////// FAVORITE
    @PostMapping("{film_id}/favorite/{user_id}")
    public ResponseEntity<?> addFilmToFavorite(
            @PathVariable("film_id") long filmId,
            @PathVariable("user_id") long userId) {
        return ResponseEntity.ok(
                filmService.addFilmToFavorite(filmId, userId));
    }

    @DeleteMapping("{film_id}/favorite/{user_id}")
    public ResponseEntity<?> removeFilmFromFavorite(
            @PathVariable("film_id") long filmId,
            @PathVariable("user_id") long userId) {
        return ResponseEntity.ok(
                filmService.removeFilmFromFavorite(filmId, userId));
    }
}
