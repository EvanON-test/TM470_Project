package com.example.tm470talkingcash;

import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;


/**Admittedly the validation could have been done in the individual classes however I thought it would likely comply with oop by
 * using a seperate class to hold most of the validation techniques. Even though this has realistically added more code **/
public class UserValidation {

//TODO:maybe worth implementing the toast into here would need ot switch out a few buits (especially boolean bit)
    public static boolean isNullOrEmpty(String text){
        return text == null || text.trim().isEmpty();
    }

    public static boolean isValidUsername(String username, int minLength){
        return !isNullOrEmpty(username) && username.length() >= minLength;
    }

    public static boolean isValidEmail(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPassword(String password, int minLength){
        return !isNullOrEmpty(password) && password.length() >= minLength;
    }

    public static boolean doPasswordsMatch(String password, String confirmPassword){
        return password.equals(confirmPassword);
    }


}
