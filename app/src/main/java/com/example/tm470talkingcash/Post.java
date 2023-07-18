package com.example.tm470talkingcash;

import java.util.List;

//TODO: certain setters are not used and could be removed however I intend to review the
// possibility of editing posts and as such do not want to remove them just yet
/**
 * Represents a Post with postid, userid, username, identifying tags, a hyperlink and timestamp.
 * Includes constructors, getters and setters.
 */
public class Post {
    private String postId;
    private String userID;
    private String username;
    private String title;
    private List<String> tags;
    private String hyperlink;
    private long timestamp;

    public Post(){}

    public Post(String postId, String userID, String username, String title, List<String> tags, String hyperlink, long timestamp) {
        this.postId = postId;
        this.userID = userID;
        this.username = username;
        this.title = title;
        this.tags = tags;
        this.hyperlink = hyperlink;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Post{" +
                "postId='" + postId + '\'' +
                ", userID='" + userID + '\'' +
                ", username='" + username + '\'' +
                ", title='" + title + '\'' +
                ", tags=" + tags +
                ", hyperlink='" + hyperlink + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername(){
        return username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getHyperlink() {
        return hyperlink;
    }

    public void setHyperlink(String hyperlink) {
        this.hyperlink = hyperlink;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

