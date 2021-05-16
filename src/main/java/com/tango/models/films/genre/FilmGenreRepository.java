package com.tango.models.films.genre;

import com.tango.models.films.film.Film;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilmGenreRepository extends JpaRepository<FilmGenre, Long> {

    @Query("select fg.genre from FilmGenre fg where fg.film.filmId=?1")
    List<Genre> findAllByFilmId(long filmId);

    @Query("select fg.film from FilmGenre fg where fg.genre.genreId=?1")
    Page<Film> findAllByGenreId(long genreId, Pageable pageable);

    @Query("select fg.film from FilmGenre fg where fg.genre.genreId=?1 and fg.film.title like ?2")
    Page<Film> findAllByGenreIdAndTitle(long genreId, String search, Pageable pageable);
}
