package com.tango.tango;

import com.github.javafaker.Faker;
import com.tango.models.films.film.Film;
import com.tango.models.films.film.FilmRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TangoApplicationTest {

    @Autowired
    private FilmRepository filmRepository;
    private Faker faker = new Faker();

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void contextLoads() {
        assertThat(faker).isNotNull();

        assertThat(this.restTemplate.getForObject(
                "http://localhost:" + port + "/", String.class)
        ).contains("Hello");
    }

    @Test
    @DisplayName("Сохранение фильма и подсчёт количества фильмов")
    public void filmCreationTest() {

        var count = filmRepository.count();

        var film = new Film(
                faker.name().title(),
                faker.random().nextDouble(),
                "", "",
                faker.lorem().characters(200),
                "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8");

        filmRepository.save(film);
        Assertions.assertEquals(filmRepository.count(), count + 1);
    }

    @Test
    @DisplayName("Сохранение фильма и его поиск фильма")
    public Film filmFindTest() throws Exception {

        var testFilm = new Film(
                faker.name().title(),
                faker.random().nextDouble(),
                "", "",
                faker.lorem().characters(200),
                "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8");
        filmRepository.save(testFilm);

        var film = filmRepository
                .findById(testFilm.getFilmId())
                .orElseThrow(() -> new Exception("Дурак что ли?"));
        System.out.println(film);

        Assertions.assertEquals(testFilm, film);
        return film;
    }

}