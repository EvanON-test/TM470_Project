package com.example.tm470talkingcash;

import static org.junit.Assert.*;

import org.junit.Test;
import java.util.Arrays;
import java.util.List;

public class PostTest {

    // Constructor Tests
    @Test
    public void post_multiArgConstructor_initializesCorrectly() {
        List<String> tags = Arrays.asList("tag1", "tag2");
        Post post = new Post("p1", "u1", "user1", "TestTitle", tags, "http://example.com", 12345678L);

        assertEquals("p1", post.getPostId());
        assertEquals("u1", post.getUserID());
        assertEquals("user1", post.getUsername());
        assertEquals("TestTitle", post.getTitle());
        assertEquals(tags, post.getTags());
        assertEquals("http://example.com", post.getHyperlink());
        assertEquals(12345678L, post.getTimestamp());
    }

    // Setter and Getter Tests
    @Test
    public void post_setAndGetPostId_worksCorrectly() {
        Post post = new Post();
        post.setPostId("p1");
        assertEquals("p1", post.getPostId());
    }

    @Test
    public void post_setAndGetUserID_worksCorrectly() {
        Post post = new Post();
        post.setUserID("u1");
        assertEquals("u1", post.getUserID());
    }

    @Test
    public void post_setAndGetUsername_worksCorrectly() {
        Post post = new Post();
        post.setUsername("user1");
        assertEquals("user1", post.getUsername());
    }

    @Test
    public void post_setAndGetTitle_worksCorrectly() {
        Post post = new Post();
        post.setTitle("TestTitle");
        assertEquals("TestTitle", post.getTitle());
    }

    @Test
    public void post_setAndGetTags_worksCorrectly() {
        Post post = new Post();
        List<String> tags = Arrays.asList("tag1", "tag2");
        post.setTags(tags);
        assertEquals(tags, post.getTags());
    }

    @Test
    public void post_setAndGetHyperlink_worksCorrectly() {
        Post post = new Post();
        post.setHyperlink("http://example.com");
        assertEquals("http://example.com", post.getHyperlink());
    }

    @Test
    public void post_setAndGetTimestamp_worksCorrectly() {
        Post post = new Post();
        post.setTimestamp(12345678L);
        assertEquals(12345678L, post.getTimestamp());
    }

    // toString Test
    @Test
    public void post_toString_returnsExpectedString() {
        List<String> tags = Arrays.asList("tag1", "tag2");
        Post post = new Post("p1", "u1", "user1", "TestTitle", tags, "http://example.com", 12345678L);
        String expectedString = "Post{postId='p1', userID='u1', username='user1', title='TestTitle', tags=[tag1, tag2], hyperlink='http://example.com', timestamp=12345678}";
        assertEquals(expectedString, post.toString());
    }

}
