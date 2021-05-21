package com.tango.services;

import com.tango.DTO.FilmDTO;
import com.tango.models.films.film.Film;
import com.tango.models.films.film.FilmRepository;
import com.tango.models.films.genre.FilmGenre;
import com.tango.models.films.genre.FilmGenreRepository;
import com.tango.models.films.genre.Genre;
import com.tango.models.user.User;
import com.tango.models.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class FilmService {

    final UserRepository userRepository;
    final FilmRepository filmRepository;
    final FilmGenreRepository filmGenreRepository;
    final GenreService genreService;


    public FilmService(@Autowired UserRepository userRepository,
                       @Autowired FilmRepository filmRepository,
                       @Autowired FilmGenreRepository filmGenreRepository,
                       @Autowired GenreService genreService) {
        this.userRepository = userRepository;
        this.filmRepository = filmRepository;
        this.filmGenreRepository = filmGenreRepository;
        this.genreService = genreService;
    }

    @Transactional
    public User addFilmToFavorite(long filmId, long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("[ERROR] No user with id=" + userId));
        Film film = filmRepository.findById(filmId).orElseThrow(() -> new NoSuchElementException("[ERROR] No film with id=" + filmId));
        user.addFavorite(film);
        return userRepository.save(user);
    }

    @Transactional
    public User removeFilmFromFavorite(long filmId, long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("[ERROR] No user with id=" + userId));
        Film film = filmRepository.findById(filmId).orElseThrow(() -> new NoSuchElementException("[ERROR] No film with id=" + filmId));
        user.removeFavorite(film);
        return userRepository.save(user);
    }

    // фильмы без условий
    public Page<FilmDTO> getFilms(Pageable pageable) {
        List<FilmDTO> filmDTOS = getFilmDTOS(
                filmRepository.findAll(pageable));

        return new PageImpl<>(filmDTOS, pageable, filmDTOS.size());
    }

    // Поиск по названию
    public Page<FilmDTO> getFilms(String search, Pageable pageable) {
        List<FilmDTO> filmDTOS = getFilmDTOS(
                filmRepository.findAllByTitleLike(search.trim() + "%", pageable));

        return new PageImpl<>(filmDTOS, pageable, filmDTOS.size());
    }

    // Поиск по жанру
    public Page<FilmDTO> getFilms(long genreId, Pageable pageable) {

        List<FilmDTO> filmDTOS = getFilmDTOS(
                genreService.getFilmsByGenre(genreId, pageable));

        return new PageImpl<>(filmDTOS, pageable, filmDTOS.size());
    }

    // Поиск по строке и по жанру
    public Page<FilmDTO> getFilms(long genreId, String search, Pageable pageable) {
        List<FilmDTO> filmDTOS = getFilmDTOS(
                genreService.getFilmsByGenre(genreId, search, pageable));

        return new PageImpl<>(filmDTOS, pageable, filmDTOS.size());
    }

    public FilmDTO getFilm(long filmId) {
        return getFilmDTO(filmRepository.findById(filmId)
                .orElseThrow(() -> new NoSuchElementException("[ERROR] No film with id=" + filmId)));
    }

    /////////////////////////////////////// UTILS
//    private List<FilmDTO> getFilmDTOS(List<Film> films) {
//        List<FilmDTO> filmDTOS = new ArrayList<>();
//
//        for (var film : films)
//            filmDTOS.add(getFilmDTO(film));
//
//        return filmDTOS;

//    }

    private List<FilmDTO> getFilmDTOS(Page<Film> films) {
        List<FilmDTO> filmDTOS = new ArrayList<>();

        for (var film : films)
            filmDTOS.add(getFilmDTO(film));

        return filmDTOS;
    }

    private FilmDTO getFilmDTO(Film film) {
        List<Genre> genres = genreService.getGenresByFilm(film);
        return new FilmDTO(film, genres);
    }

    public FilmDTO addFilm(Film film, List<Long> genres) {
        Film savedFilm = filmRepository.save(film);
        List<Genre> listGenres = new ArrayList<>();

        for (var genreId : genres) {
            Genre genreById = genreService.getGenreById(genreId);
            listGenres.add(genreById);
            filmGenreRepository.save(new FilmGenre(savedFilm, genreById));
        }

        return new FilmDTO(savedFilm, listGenres);
    }
}
