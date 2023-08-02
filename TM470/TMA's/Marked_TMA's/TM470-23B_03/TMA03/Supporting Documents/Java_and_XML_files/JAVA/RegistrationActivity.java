package com.example.tm470talkingcash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Objects;

//TODO: Make sure the comments are all appropriate i.e. elaborate and make sure they conform to language syntax
//TODO:In later iterations review other login methods e.g. google accounts/twitter accounts
//TODO: Also investigate the implementation of an email verification functionality
//TODO: review the registration process, as in is it worth logging the user in automatically after registration

/**
 * provides the functionality to register a new account using a username, email and password.
 * Navigates to the login page if successful
 *
 */
public class RegistrationActivity extends AppCompatActivity {
    private TextInputEditText usernameText;
    private TextInputEditText emailText;
    private TextInputEditText passwordText;

    private TextInputEditText confirmPasswordText;
    private Button registerBtn;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //sets the view to the login activity layout
        setContentView(R.layout.activity_registration);
        //Gets the firebase instance for the firebase authentication service
        fAuth = FirebaseAuth.getInstance();
        //Finds the views based on their ID's
        usernameText = findViewById(R.id.reg_user_input);
        emailText = findViewById(R.id.reg_email_input);
        passwordText = findViewById(R.id.reg_pass_input);
        confirmPasswordText = findViewById(R.id.reg_pass_confirm_input);
        registerBtn = findViewById(R.id.reg_button);

        //Sets a click listener for the register button, calls the registerNewUser method when clicked
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUser();
            }
        });
    }

    /**
     * This method registers the user into the firebase authentication database and also adds their
     * username and email, alongside a generated userId, to the users collection in the Cloud
     * firestore database
     */
    private void registerNewUser() {
        String username;
        String email;
        String password;
        String confirmPassword;
        Integer minLength;

        //initializes the variables based oin their inputs
        username = usernameText.getText().toString().trim();
        email = emailText.getText().toString().trim();
        password = passwordText.getText().toString().trim();
        confirmPassword = confirmPasswordText.getText().toString().trim();
        minLength = 6;

        //TODO: review if more validation techniques are required, restructure into UserValidation class where possible
        //Validates the user inputs using methods imported from the UserValidation class
        if (!UserValidation.isValidUsername(username, minLength)){
            Toast.makeText(getApplicationContext(), "Please enter a Valid Username", Toast.LENGTH_LONG).show();
            return;
        } else if (!UserValidation.isValidEmail(email)){
            Toast.makeText(getApplicationContext(), "Please enter a Valid Email", Toast.LENGTH_LONG).show();
            return;
        } else if (!UserValidation.isValidPassword(password, minLength)){
            Toast.makeText(getApplicationContext(), "Please enter Valid Password", Toast.LENGTH_LONG).show();
            return;
        } else if (!UserValidation.doPasswordsMatch(password, confirmPassword)){
            Toast.makeText(getApplicationContext(), "Please make sure that Passwords match", Toast.LENGTH_LONG).show();
            return;
        }


        /**
         * Creates a new user with the email and password combonation, and saves the user profile
         * information to 'users' collection in the firestore database
         */
        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Successful Registration", Toast.LENGTH_LONG).show();
                    String userId = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
                    UserProfile userProfile = new UserProfile(userId, username, email);
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("users").document(userId).set(userProfile).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(), "Registration Details saved to user database", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    });
                } else {
                    String errorMessage = task.getException().getMessage();
                    Toast.makeText(getApplicationContext(), "Registration Failed: " + errorMessage, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
