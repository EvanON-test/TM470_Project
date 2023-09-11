package com.example.tm470talkingcash;

import static org.junit.Assert.*;

import org.junit.Test;

public class UserValidationTest {

    // Tests for isNullOrEmpty()
    @Test
    public void isNullOrEmpty_emptyString_returnsTrue() {
        assertTrue(UserValidation.isNullOrEmpty(""));
    }

    @Test
    public void isNullOrEmpty_null_returnsTrue() {
        assertTrue(UserValidation.isNullOrEmpty(null));
    }

    @Test
    public void isNullOrEmpty_stringWithSpacesOnly_returnsTrue() {
        assertTrue(UserValidation.isNullOrEmpty("   "));
    }

    @Test
    public void isNullOrEmpty_nonEmptyString_returnsFalse() {
        assertFalse(UserValidation.isNullOrEmpty("test"));
    }

    // Tests for isInvalidUsername()
    @Test
    public void isInvalidUsername_null_returnsTrue() {
        assertTrue(UserValidation.isInvalidUsername(null));
    }

    @Test
    public void isInvalidUsername_emptyString_returnsTrue() {
        assertTrue(UserValidation.isInvalidUsername(""));
    }

    @Test
    public void isInvalidUsername_tooShort_returnsTrue() {
        assertTrue(UserValidation.isInvalidUsername("abc"));
    }

    @Test
    public void isInvalidUsername_tooLong_returnsTrue() {
        assertTrue(UserValidation.isInvalidUsername("abcdefghijklmnop"));
    }

    @Test
    public void isInvalidUsername_validUsername_returnsFalse() {
        assertFalse(UserValidation.isInvalidUsername("valid_Username9"));
    }

    @Test
    public void isInvalidUsername_containsSpaces_returnsTrue() {
        assertTrue(UserValidation.isInvalidUsername("invalid username"));
    }

    // Tests for isValidEmail()
    @Test
    public void isValidEmail_validEmail_returnsTrue() {
        assertTrue(UserValidation.isValidEmail("test@email.com"));
    }

    @Test
    public void isValidEmail_invalidEmailWithoutAtSymbol_returnsFalse() {
        assertFalse(UserValidation.isValidEmail("testemail.com"));
    }

    // Tests for doPasswordsMatch()
    @Test
    public void doPasswordsMatch_matchingPasswords_returnsTrue() {
        assertTrue(UserValidation.doPasswordsMatch("password123", "password123"));
    }

    @Test
    public void doPasswordsMatch_nonMatchingPasswords_returnsFalse() {
        assertFalse(UserValidation.doPasswordsMatch("password123", "password124"));
    }

    // Tests for isValidPassword()
    @Test
    public void isValidPassword_validPasswordWithUppercaseNumberSpecial_returnsTrue() {
        assertTrue(UserValidation.isValidPassword("Password1@"));
    }

    @Test
    public void isValidPassword_withoutUppercase_returnsFalse() {
        assertFalse(UserValidation.isValidPassword("password1@"));
    }

    @Test
    public void isValidPassword_withoutNumber_returnsFalse() {
        assertFalse(UserValidation.isValidPassword("Password@"));
    }

    @Test
    public void isValidPassword_withoutSpecialCharacter_returnsFalse() {
        assertFalse(UserValidation.isValidPassword("Password1"));
    }

    @Test
    public void isValidPassword_withWhitespace_returnsFalse() {
        assertFalse(UserValidation.isValidPassword("Password 1@"));
    }

    @Test
    public void isValidPassword_justBelowMinLength_returnsFalse() {
        assertFalse(UserValidation.isValidPassword("P1@"));
    }

    @Test
    public void isValidPassword_atMinLength_returnsTrue() {
        assertTrue(UserValidation.isValidPassword("P1@a"));
    }

    @Test
    public void isValidPassword_withUnsupportedSpecialCharacter_returnsFalse() {
        assertFalse(UserValidation.isValidPassword("Password1*"));
    }

    @Test
    public void isValidPassword_withoutUppercaseButOtherRequirementsMet_returnsFalse() {
        assertFalse(UserValidation.isValidPassword("password1@"));
    }

    @Test
    public void isValidPassword_withoutNumberButOtherRequirementsMet_returnsFalse() {
        assertFalse(UserValidation.isValidPassword("Password@"));
    }

    @Test
    public void isValidPassword_withoutSpecialCharacterButOtherRequirementsMet_returnsFalse() {
        assertFalse(UserValidation.isValidPassword("Password1"));
    }

    @Test
    public void isValidPassword_withAllLowercase_returnsFalse() {
        assertFalse(UserValidation.isValidPassword("password"));
    }

    @Test
    public void isValidPassword_withAllUppercase_returnsFalse() {
        assertFalse(UserValidation.isValidPassword("PASSWORD"));
    }

    @Test
    public void isValidPassword_withAllDigits_returnsFalse() {
        assertFalse(UserValidation.isValidPassword("1234567890"));
    }

    @Test
    public void isValidPassword_withAllSpecialCharactersSupported_returnsFalse() {
        assertFalse(UserValidation.isValidPassword("@#$%^&+=!"));
    }



}