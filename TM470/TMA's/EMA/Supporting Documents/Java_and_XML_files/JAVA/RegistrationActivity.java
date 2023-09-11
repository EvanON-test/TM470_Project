package com.example.tm470talkingcash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
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
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;







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
    private FirebaseFirestore db;
    private CheckBox checkboxDisclaimer;
    private CheckBox checkboxTermsOfUse;
    private CheckBox checkboxGDPR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //sets the view to the login activity layout
        setContentView(R.layout.activity_registration);
        //Gets the firebase instance for the firebase authentication service
        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        //Finds the views based on their ID's
        usernameText = findViewById(R.id.reg_user_input);
        emailText = findViewById(R.id.reg_email_input);
        passwordText = findViewById(R.id.reg_pass_input);
        confirmPasswordText = findViewById(R.id.reg_pass_confirm_input);
        registerBtn = findViewById(R.id.reg_button);

        //This set up of viewable activities is sufficient in this case however would not be in the case of an official application
        TextView legalDisclaimerLink = findViewById(R.id.legal_disclaimer_link);
        TextView termsOfUseLink = findViewById(R.id.terms_of_use_link);
        TextView gdprLink = findViewById(R.id.gdpr_link);
        checkboxDisclaimer = findViewById(R.id.legal_disclaimer_checkbox);
        checkboxTermsOfUse = findViewById(R.id.terms_of_use_checkbox);
        checkboxGDPR = findViewById(R.id.gdpr_checkbox);

        //Onclick listener for legal disclaimer
        legalDisclaimerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LegalDisclaimerActivity.class);
                startActivity(intent);
            }
        });

        //Onclick listener for terms of use
        termsOfUseLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TermsOfUseActivity.class);
                startActivity(intent);
            }
        });

        //Onclick listener for gdpr
        gdprLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GdprActivity.class);
                startActivity(intent);
            }
        });

        //Sets a click listener for the register button, calls the registerNewUser method when clicked
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //registerNewUser();
                if (checkboxDisclaimer.isChecked() && checkboxTermsOfUse.isChecked() && checkboxGDPR.isChecked()){
                    registerNewUser();
                } else {
                    Toast.makeText(getApplicationContext(), "Please tick checkboxes", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * This method registers the user into the firebase authentication database and also adds their
     * username and email, alongside a generated userId, to the users collection in the Cloud
     * firestore database
     */
    private void registerNewUser() {

        //initializes the variables based oin their inputs
        String username = usernameText.getText().toString().trim().toLowerCase();
        String email = emailText.getText().toString().trim().toLowerCase();
        String password = passwordText.getText().toString().trim();
        String confirmPassword = confirmPasswordText.getText().toString().trim();


        //Requirements shown to users in tooltips in XML
        //Validates the user inputs using methods imported from the UserValidation class
        if (UserValidation.isInvalidUsername(username)) {
            Toast.makeText(getApplicationContext(), "Please enter a Valid Username", Toast.LENGTH_LONG).show();
            return;
        } else if (!UserValidation.isValidEmail(email)) {
            Toast.makeText(getApplicationContext(), "Please enter a Valid Email", Toast.LENGTH_LONG).show();
            return;
        } else if (!UserValidation.isValidPassword(password)) {
            Toast.makeText(getApplicationContext(), "Please conform to password requirements", Toast.LENGTH_LONG).show();
            return;
        } else if (!UserValidation.doPasswordsMatch(password, confirmPassword)) {
            Toast.makeText(getApplicationContext(), "Please make sure that Passwords match", Toast.LENGTH_LONG).show();
            return;
        }


        db.collection("users").whereEqualTo("username", username).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().isEmpty()) {
                        //username exists
                        Toast.makeText(getApplicationContext(), "Username already exists", Toast.LENGTH_SHORT).show();
                    } else {
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
                } else {
                    Toast.makeText(getApplicationContext(), "Error checking username: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}






