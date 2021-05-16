package com.tango.models.professional;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tango.models.films.film.Film;
import com.tango.models.roles.Role;
import com.tango.models.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity(name = "Professional")
@Table(name = "professionals")
@NoArgsConstructor
@Getter
@Setter
public class Professional {

    @Id
    private long professionalId;

    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "professional_id")
    private User user;

    @Column(name = "rating")
    private int rating;

    @JsonManagedReference
    @ManyToMany(mappedBy = "professionals")
    private Set<Role> roles = new HashSet<>();

    @JsonBackReference
    @ManyToMany(mappedBy = "professionals")
    private Set<Film> films = new HashSet<>();

    public Professional(int rating,
                        User user,
                        Set<Role> roles) {
        this.rating = rating;
        this.user = user;
        this.roles = roles;
    }

    //------------------------------------UTILS
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Professional that = (Professional) o;
        return professionalId == that.professionalId && user.equals(that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(professionalId, user);
    }

    @Override
    public String toString() {
        return "Professional{" +
                "professionalId=" + professionalId +
                ", rating=" + rating +
                ", user=" + user.getId() + ". " + user.getEmail() +
                ", roles=" + roles +
                '}';
    }
}
