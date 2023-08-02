package com.example.tm470talkingcash;

public class Comment {
    private String username;
    private String commentText;
    private Long timestamp;
    private String PostID;

    private String commentID;


    public Comment(){

    }
    public Comment(String username, String commentText, long timestamp, String postID, String commentID) {
        this.username = username;
        this.commentText = commentText;
        this.timestamp = timestamp;
        this.PostID = postID;
        this.commentID = commentID;
    }

    //TODO: Review unused methods after finalisation - remove the unused

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getPostID() {
        return PostID;
    }

    public void setPostID(String postID) {
        PostID = postID;
    }

    public String getCommentID() {
        return commentID;
    }

    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "username='" + username + '\'' +
                ", commentText='" + commentText + '\'' +
                ", timestamp=" + timestamp +
                ", PostID='" + PostID + '\'' +
                ", commentID='" + commentID + '\'' +
                '}';
    }
}
