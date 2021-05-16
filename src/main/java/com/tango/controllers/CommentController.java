package com.tango.controllers;

import com.tango.DTO.CommentRequest;
import com.tango.DTO.PaginationResponse;
import com.tango.models.films.comment.Comment;
import com.tango.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "api/comment")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("{film_id}/list")
    public ResponseEntity<PaginationResponse<?>> getComments(
            @PathVariable("film_id") long filmId,
            @RequestParam int page,
            @RequestParam int size) {
        Page<Comment> comments = commentService.getComments(filmId, PageRequest.of(page, size));
        return ResponseEntity.ok(new PaginationResponse<>(comments));
    }

    @GetMapping("{comment_id}")
    public Comment getComments(@PathVariable("comment_id") long commentId) {
        return commentService.getComment(commentId);
    }


    @PostMapping("{film_id}/{user_id}")
    public Comment makeReview(@PathVariable("film_id") long filmId,
                              @PathVariable("user_id") long userId,
                              @RequestBody CommentRequest commentRequest) {
        return commentService.makeReview(filmId, userId, commentRequest.from());
    }

    @PutMapping("{comment_id}")
    public Comment editReview(@PathVariable("comment_id") long commentId,
                              @RequestBody CommentRequest commentRequest) {
        return commentService.editReview(commentId, commentRequest.from());
    }

    @DeleteMapping("{comment_id}")
    public ResponseEntity<?> deleteReview(@PathVariable("comment_id") long commentId) {
        commentService.deleteReview(commentId);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "OK");
        return ResponseEntity.ok(response);
    }

    //------------------------ Лайки

    @PostMapping("{comment_id}/dislike")
    public Comment like(@PathVariable("comment_id") long commentId) {
        return commentService.dislike(commentId);
    }

    @PostMapping("{comment_id}/like")
    public Comment dislike(@PathVariable("comment_id") long commentId) {
        return commentService.like(commentId);
    }
}