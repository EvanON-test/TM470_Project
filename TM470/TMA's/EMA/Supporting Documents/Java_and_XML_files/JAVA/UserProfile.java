package com.example.tm470talkingcash;



/**
 * Represents a user with a id, username and email
 */
public class UserProfile {
    private String userId;
    private String username;
    private String userEmail;

    public UserProfile(){}

    public UserProfile( String username, String userEmail){
        this.username = username;
        this.userEmail = userEmail;
    }

    //Constructs a userProfile object
    public UserProfile(String userId, String username, String userEmail){
        this.userId = userId;
        this.username = username;
        this.userEmail = userEmail;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
