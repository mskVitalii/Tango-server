package com.tango.tango;

import com.github.javafaker.Faker;
import com.tango.models.films.comment.Comment;
import com.tango.models.films.comment.CommentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CommentsTests {

    @Autowired
    private CommentRepository commentRepository;

    private Faker faker = new Faker();

    @Test
    public Comment commentCreationTest() {

        // Достать фильм
        var comment = new Comment(

        );

        commentRepository.save(comment);
        Assertions.assertEquals(commentRepository.count(), 1);
        return comment;
    }



    @Test
    public Comment commentFindTest() {

        return null;
//        var comment = commentRepository
//                .findById(commentCreationTest()())
//                .orElseThrow(() -> new Exception("Дурак что ли?"));
//
//        System.out.println(film);
//        return film;
    }
}
