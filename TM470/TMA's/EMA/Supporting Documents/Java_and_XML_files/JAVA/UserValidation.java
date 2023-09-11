package com.example.tm470talkingcash;




import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This class acts as a abstracted validation hub where multiple methods which are used to validate input are created
 *
 */
public class UserValidation {


    public static boolean isNullOrEmpty(String text){
        return text == null || text.trim().isEmpty();
    }

    public static boolean isInvalidUsername(String username) {

        // Check null or empty first as it's the cheapest operation.
        if (username == null || username.trim().isEmpty()) {
            return true;
        }

        // If the length is not within the required range, return true.
        int length = username.length();
        if (length < 4 || length > 15) {
            return true;
        }

        // Check character set.
        if (!username.matches("[A-Za-z0-9_\\W&&[^\\s]]+")) {
            return true;
        }

        return false;
    }

    public static boolean isValidEmail(String email){
        //Initially used matcher however this caused issues in Test so reverted to the appropriate
        //previous code was -> return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        Pattern emailPattern = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
        return emailPattern.matcher(email).matches();
    }

    public static boolean doPasswordsMatch(String password, String confirmPassword){
        return password.equals(confirmPassword);
    }


    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
}
