package com.example.tm470talkingcash;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CommentTest {

    // Constructor Tests
    @Test
    public void comment_multiArgConstructor_initializesCorrectly(){
        Comment comment = new Comment("Allen", "This is a comment.", 12345678L, "p1", "c1" );

        assertEquals("Allen", comment.getUsername());
        assertEquals("This is a comment.", comment.getCommentText());
        //Had to utilise Long.valueOf to overcome Object/Long confusion by the assertEquals method
        assertEquals(Long.valueOf(12345678L), Long.valueOf(comment.getTimestamp()));
        assertEquals("p1", comment.getPostID());
        assertEquals("c1", comment.getCommentID());
    }

    // Setter and Getter Tests
    @Test
    public void comment_setAndGetUsername_worksCorrectly() {
        Comment comment = new Comment();
        comment.setUsername("Allen");
        assertEquals("Allen", comment.getUsername());
    }

    //Update bodies below
    @Test
    public void comment_setAndGetCommentText_worksCorrectly() {
        Comment comment = new Comment();
        comment.setCommentText("This is a comment.");
        assertEquals("This is a comment.", comment.getCommentText());
    }

    @Test
    public void comment_setAndGetTimestamp_worksCorrectly() {
        Comment comment = new Comment();
        comment.setTimestamp(12345678L);
        assertEquals(Long.valueOf(12345678L), Long.valueOf(comment.getTimestamp()));
    }

    @Test
    public void comment_setAndGetPostID_worksCorrectly() {
        Comment comment = new Comment();
        comment.setPostID("p1");
        assertEquals("p1", comment.getPostID());
    }

    @Test
    public void comment_setAndGetCommentID_worksCorrectly() {
        Comment comment = new Comment();
        comment.setCommentID("c1");
        assertEquals("c1", comment.getCommentID());
    }

}