package com.tango.models.user;

import com.fasterxml.jackson.annotation.*;
import com.tango.models.films.comment.Comment;
import com.tango.models.films.film.Film;
import com.tango.models.professional.Professional;
import com.tango.security.roles.AuthRole;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

import static java.time.temporal.ChronoUnit.YEARS;

@Entity(name = "User")
@Table(name = "users")
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

    @JsonProperty(value = "user_id")
    @Id
    @SequenceGenerator(
            name = "user_id",
            sequenceName = "user_id",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_id"
    )
    @Column(name = "user_id")
    private long Id;

    @JsonProperty(value = "favorite")
    @JsonManagedReference
    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "user_favorite_films",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "film_id"))
    private final Set<Film> favoriteFilms = new HashSet<>();

    @JsonBackReference
    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Comment> userComments = new ArrayList<>();

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "login", nullable = false)
    private String email;

    @JsonIgnore
    @Column(name = "password", nullable = false)
    private String password;

    @JsonProperty(value = "sub_deadline")
    @Column(name = "sub_deadline", nullable = false)
    private LocalDate subscriptionDeadline;

    @JsonProperty(value = "date_of_birth")
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @JsonManagedReference
    @OneToOne(
            mappedBy = "user",
            fetch = FetchType.LAZY,
            orphanRemoval = true,
            cascade = CascadeType.ALL)
    private Professional professional;

    @Transient
    private int age;

    @JsonProperty(value = "user_roles")
    @JsonManagedReference
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<AuthRole> authRoles = new HashSet<>();

    @Column(name = "avatar", columnDefinition = "text")
    private String avatar;

    public User(String email,
                String username,
                String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

//    public User(String email,
//                String username,
//                String password,
//                LocalDate dateOfBirth) {
//        this.email = email;
//        this.username = username;
//        this.password = password;
//        this.dateOfBirth = dateOfBirth;
//    }

    public User(String email,
                String username,
                String password,
                LocalDate subscriptionDeadline,
                LocalDate dateOfBirth) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.subscriptionDeadline = subscriptionDeadline;
        this.dateOfBirth = dateOfBirth;
    }

    public User(String email,
                String username,
                String password,
                LocalDate subscriptionDeadline,
                LocalDate dateOfBirth,
                Set<AuthRole> authRoles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.subscriptionDeadline = subscriptionDeadline;
        this.dateOfBirth = dateOfBirth;
        this.authRoles = authRoles;
    }

    public int getAge() {
        return (int) YEARS.between(dateOfBirth, LocalDate.now());
    }

    //////////////////////////////////// FAVORITE FILMS

    public void addFavorite(Film film) {
        this.favoriteFilms.add(film);
//        film.getConnoisseurs().add(this);
    }

    public void removeFavorite(Film film) {
        this.favoriteFilms.remove(film);
//        film.getConnoisseurs().remove(this);
    }

    public boolean isNull() {
        return this.username == null || this.email == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Id == user.Id && username.equals(user.username) && email.equals(user.email) && password.equals(user.password) && Objects.equals(subscriptionDeadline, user.subscriptionDeadline) && dateOfBirth.equals(user.dateOfBirth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id, username, email, password, dateOfBirth);
    }

    @Override
    public String toString() {
        return "User{" +
                "Id=" + Id +
                ", username='" + username + '\'' +
                ", login='" + email + '\'' +
                ", subscriptionDeadline=" + subscriptionDeadline +
                ", dateOfBirth=" + dateOfBirth +
                ", age=" + age +
                '}';
    }
}