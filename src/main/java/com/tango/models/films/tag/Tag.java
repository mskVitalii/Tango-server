package com.tango.models.films.tag;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tango.models.films.film.Film;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity(name = "Tag")
@Table(name = "tags")
@NoArgsConstructor
@Data
public class Tag {

    @Id
    @SequenceGenerator(
            name = "tag_id",
            sequenceName = "tag_id",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "tag_id"
    )
    @Column(name = "tag_id")
    private long tagId;

    @JsonProperty(value = "tag_name")
    @Column(name = "tag_name")
    private String tagName;

//    @JsonBackReference
//    @ManyToMany(mappedBy = "tags")
//    private Set<Film> films = new HashSet<>();

    public Tag(String tagName) {
        this.tagName = tagName;
   }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return tagId == tag.tagId && tagName.equals(tag.tagName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagId, tagName);
    }

    @Override
    public String toString() {
        return "Tag{" +
                "tagId=" + tagId +
                ", tagName='" + tagName + '\'' +
                '}';
    }
}
