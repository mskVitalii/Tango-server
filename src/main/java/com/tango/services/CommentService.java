package com.tango.services;

import com.tango.models.films.comment.Comment;
import com.tango.models.films.comment.CommentRepository;
import com.tango.models.films.film.Film;
import com.tango.models.films.film.FilmRepository;
import com.tango.models.user.User;
import com.tango.models.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final FilmRepository filmRepository;
    private final UserRepository userRepository;

    public CommentService(@Autowired CommentRepository commentRepository,
                          @Autowired FilmRepository filmRepository,
                          @Autowired UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.filmRepository = filmRepository;
        this.userRepository = userRepository;
    }

    public Page<Comment> getComments(long film_id, Pageable pageable) {
        return commentRepository.findAllByFilmFilmIdOrderByCommentIdDesc(film_id, pageable);
    }

    @Transactional
    public Comment makeReview(long filmId, long userId, Comment comment) {
        Film film = filmRepository.findById(filmId).orElseThrow(()
                -> new NoSuchElementException("FILM with id='" + filmId + "' does not exist"));
        User user = userRepository.findById(userId).orElseThrow(()
                -> new NoSuchElementException("USER with id='" + userId + "' does not exist"));

        comment.setFilm(film);
        comment.setWriter(user);
        return commentRepository.save(comment);
    }

    public Comment editReview(long commentId, Comment comment) {
        Comment oldComment = commentRepository.getOne(commentId);
        oldComment.setText(comment.getText());
        oldComment.setSpoiler(comment.getSpoiler());
        return commentRepository.save(oldComment);
    }

    public void deleteReview(long commentId) {
        commentRepository.deleteById(commentId);
    }

    //------------------------ Лайки

    public Comment dislike(long commentId) {
        Comment comment = commentRepository.getOne(commentId);
        comment.setDislikes(comment.getDislikes() + 1);
        return commentRepository.save(comment);
    }

    public Comment like(long commentId) {
        Comment comment = commentRepository.getOne(commentId);
        comment.setLikes(comment.getLikes() + 1);
        return commentRepository.save(comment);
    }

    public Comment getComment(long commentId) {
        return commentRepository.findById(commentId).orElseThrow(()
                -> new NoSuchElementException("COMMENT with id='" + commentId + "' does not exist"));
    }
}
