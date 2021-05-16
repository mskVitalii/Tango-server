package com.tango.models.films.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
//    @Query("select u from Comment u where u.film.filmId = ?1")
//    List<Comment> findAllByFilmId(long id);

    Page<Comment> findAllByFilmFilmIdOrderByCommentIdDesc(Long film_filmId, Pageable pageable);
}
