package com.tango.models.films.film;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tango.models.films.comment.Comment;
import com.tango.models.professional.Professional;
import com.tango.models.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;

@Entity(name = "Film")
@Table(name = "films")
@NoArgsConstructor
@Data
public class Film {

    @JsonBackReference
    @OneToMany(mappedBy = "film", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Comment> comments = new ArrayList<>();

//    @JsonManagedReference
//    @ManyToMany(cascade = {
//            CascadeType.PERSIST,
//            CascadeType.MERGE
//    })
//    @JoinTable(name = "films_tags",
//            joinColumns = @JoinColumn(name = "film_id"),
//            inverseJoinColumns = @JoinColumn(name = "tag_id")
//    )
//    private final List<Tag> tags = new ArrayList<>();

    @JsonManagedReference
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "films_professionals",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "professional_id")
    )
    private final Set<Professional> professionals = new HashSet<>();

    @JsonBackReference
    @ManyToMany(mappedBy = "favoriteFilms")
    private final Set<User> connoisseurs = new HashSet<>();

//    @JsonManagedReference
//    @ManyToMany(cascade = {
//            CascadeType.ALL
//    }, fetch = FetchType.EAGER)
//    @JoinTable(name = "films_genres",
//            joinColumns = @JoinColumn(name = "film_id"),
//            inverseJoinColumns = @JoinColumn(name = "genre_id")
//    )
//    private Set<Genre> genres = new HashSet<>();
    @JsonProperty(value = "film_link")
    @Column(name = "film_link")
    String filmLink;
    @JsonProperty(value = "film_id")
    @Id
    @SequenceGenerator(
            name = "film_id",
            sequenceName = "film_id",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "film_id"
    )
    @Column(name = "film_id")
    private long filmId;
    @Column(name = "title", columnDefinition = "text")
    private String title;
    @JsonProperty(value = "desc_rating")
    @Column(name = "desc_rating")
    private double descRating;
    @JsonProperty(value = "desc_image")
    @Column(name = "desc_image", columnDefinition = "text")
    private String descImage;

    @JsonProperty(value = "desc_preview")
    @Column(name = "desc_preview", columnDefinition = "text")
    private String descPreview;

    @JsonProperty(value = "desc_text")
    @Column(name = "desc_text", columnDefinition = "text")
    private String descText;


    public Film(String title,
                double descRating,
                String descImage,
                String descPreview,
                String descText,
                String filmLink) {
        this.title = title;
        this.descRating = descRating;
        this.descImage = descImage;
        this.descPreview = descPreview;
        this.descText = descText;
        this.filmLink = filmLink;
    }

    //////////////////////////////////// COMMENTS

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setFilm(this);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setFilm(null);
    }

    //////////////////////////////////// TAGS

//    public void addTag(Tag tag) {
//        this.tags.add(tag);
//        tag.getFilms().add(this);
//    }
//
//    public void removeTag(Tag tag) {
//        this.tags.remove(tag);
//        tag.getFilms().remove(this);
//    }

    //////////////////////////////////// GENRE

//    public void addGenre(Genre genre) {
//        this.genres.add(genre);
//        genre.getFilms().add(this);
//    }
//
//    public void removeGenre(Genre genre) {
//        this.genres.remove(genre);
//        genre.getFilms().remove(this);
//    }

    //////////////////////////////////// CONNOISSEURS

    public void addConnoisseurs(User connoisseur) {
        connoisseurs.add(connoisseur);
        connoisseur.getFavoriteFilms().add(this);
    }

    public void removeConnoisseurs(User connoisseur) {
        connoisseurs.remove(connoisseur);
        if (connoisseur.isNull()) {
            connoisseur.getFavoriteFilms().remove(this);
        }
    }

    //////////////////////////////////// PROFESSIONALS

    public void addProfessional(Professional professional) {
        professionals.add(professional);
        professional.getFilms().add(this);
    }

    public void removeProfessional(Professional professional) {
        professionals.remove(professional);
        professional.getFilms().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return filmId == film.filmId && title.equals(film.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filmId, title);
    }

    @Override
    public String toString() {
        return "Film{" +
                "filmId=" + filmId +
                ", title='" + title + '\'' +
                ", descRating=" + descRating +
                ", descImage='" + descImage + '\'' +
                ", descPreview='" + descPreview + '\'' +
                ", descText='" + descText + '\'' +
                '}';
    }
}
