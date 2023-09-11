package com.example.tm470talkingcash;

import static org.junit.Assert.*;

import org.junit.Test;

public class UserProfileTest {

    // Constructor Tests
    @Test
    public void userProfile_shortMultiArgConstructor_initializesCorrectly(){
        UserProfile UserProfile = new UserProfile("Allen", "Allen@gmail.com");

        assertEquals("Allen", UserProfile.getUsername());
        assertEquals("Allen@gmail.com", UserProfile.getUserEmail());
    }

    @Test
    public void userProfile_longMultiArgConstructor_initializesCorrectly(){
        UserProfile UserProfile = new UserProfile("u1","Allen", "Allen@gmail.com");

        assertEquals("u1", UserProfile.getUserId());
        assertEquals("Allen", UserProfile.getUsername());
        assertEquals("Allen@gmail.com", UserProfile.getUserEmail());
    }

    // Setter and Getter Tests
    @Test
    public void userProfile_setAndGetUserId_worksCorrectly() {
        UserProfile userProfile = new UserProfile();
        userProfile.setUserId("u1");
        assertEquals("u1", userProfile.getUserId());
    }

    @Test
    public void userProfile_setAndGetUsername_worksCorrectly() {
        UserProfile userProfile = new UserProfile();
        userProfile.setUsername("Allen");
        assertEquals("Allen", userProfile.getUsername());
    }

    @Test
    public void userProfile_setAndGetEmail_worksCorrectly() {
        UserProfile userProfile = new UserProfile();
        userProfile.setUserEmail("Allen@gmail.com");
        assertEquals("Allen@gmail.com", userProfile.getUserEmail());
    }
}