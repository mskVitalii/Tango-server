package com.tango.models.films.genre;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tango.models.films.film.Film;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "films_genres")
@Getter
@Setter
@NoArgsConstructor
public class FilmGenre {

    @JsonIgnore
    @Id
    @SequenceGenerator(
            name = "film_genre_id",
            sequenceName = "film_genre_id",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "film_genre_id"
    )
    @Column(name = "film_genre_id")
    private long id;

    @ManyToOne(optional = false)
    private Genre genre;

    @ManyToOne(optional = false)
    private Film film;

    public FilmGenre(Film film, Genre genre) {
        this.film = film;
        this.genre = genre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FilmGenre filmGenre = (FilmGenre) o;
        return id == filmGenre.id
                && genre.equals(filmGenre.genre)
                && film.equals(filmGenre.film);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, genre, film);
    }

    @Override
    public String toString() {
        return "FilmGenre{" +
                "id=" + id +
                ", genre=" + genre +
                ", film=" + film +
                '}';
    }
}
