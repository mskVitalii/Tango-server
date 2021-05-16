package com.tango.DTO;

import com.tango.models.films.comment.Comment;
import lombok.Data;

@Data
public class CommentRequest {
    private String text;
    private boolean spoiler;

    public Comment from() {
        return new Comment(text, spoiler);
    }
}
