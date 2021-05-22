package com.tango.tango;

import com.github.javafaker.Faker;
import com.tango.models.films.film.Film;
import com.tango.models.films.film.FilmRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FilmsTests {

    @Autowired
    private FilmRepository filmRepository;

    private Faker faker = new Faker();

    @Test
    @DisplayName("Сохранение фильма и подсчёт количества фильмов")
    public Film filmCreationTest() {

        var count = filmRepository.count();

        var film = new Film(
                faker.name().title(),
                faker.random().nextDouble(),
                "", "",
                faker.lorem().characters(200),
                "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8");

        filmRepository.save(film);
        Assertions.assertEquals(filmRepository.count(), count + 1);

        return film;
    }

    @Test
    @DisplayName("Сохранение фильма и его поиск фильма")
    public Film filmFindTest() throws Exception {

        var testFilm = filmCreationTest();

        var film = filmRepository
                .findById(testFilm.getFilmId())
                .orElseThrow(() -> new Exception("Not found"));
        System.out.println(film);

        Assertions.assertEquals(testFilm, film);
        return film;
    }
}