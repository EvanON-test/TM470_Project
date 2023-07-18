package com.example.tm470talkingcash;

import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;


/** I thought it would likely comply with OOP by making use of an extra class in which to focus on the
 * validation methods
 *
 */
public class UserValidation {

//TODO:Review these techniques again in the next iteration. Could you implement more specific
// checks and toasts here?
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
