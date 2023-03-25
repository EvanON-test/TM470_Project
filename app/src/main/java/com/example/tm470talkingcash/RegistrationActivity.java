package com.example.tm470talkingcash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

//TODO: Make sure the comments are all appropriate i.e. elaborate and make sure they conform to language syntax
//Below is just for auth for emai land password. Username to be stored i profile late and confirmpassword must match password(need to code that elsewhere?/later)
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
        setContentView(R.layout.activity_registration);

        fAuth = FirebaseAuth.getInstance();
        //initialises inputs from the forms into variables
        usernameText = findViewById(R.id.reg_user_input);
        emailText = findViewById(R.id.reg_email_input);
        passwordText = findViewById(R.id.reg_pass_input);
        confirmPasswordText = findViewById(R.id.reg_pass_confirm_input);
        registerBtn = findViewById(R.id.reg_button);

        //When button is clicked a new user is registered
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUser();
            }
        });
    }

    //converts the aquired inputs and converts them to Strings so they can be
    private void registerNewUser() {
        String username;
        String email;
        String password;
        String confirmPassword;
        Integer minLength;

        username = usernameText.getText().toString().trim();
        email = emailText.getText().toString().trim();
        password = passwordText.getText().toString().trim();
        confirmPassword = confirmPasswordText.getText().toString().trim();
        minLength = 6;

        //TODO: implement more validation techniques that you will enter into userValidationclass
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


        //TODO: Need review and probs a bit more testing
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
