package com.example.tm470talkingcash;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.SignInMethodQueryResult;

//TODO: I'm not sure if password ect needs o be encrypted at this point in the java? once main body is done review this point
public class LoginActivity extends AppCompatActivity {

    private TextInputEditText emailText;
    private TextInputEditText passwordText;
    private Button loginBtn;
    private Button registerBtn;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fAuth = FirebaseAuth.getInstance();

        emailText = findViewById(R.id.log_email_input);
        passwordText = findViewById(R.id.log_pass_input);
        loginBtn = findViewById(R.id.log_button);
        registerBtn = findViewById(R.id.reg_on_click);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userAccountLogin();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }

    private void userAccountLogin(){
        String email;
        String password;
        Integer minLength;
        //String emailRegex;
        //String passwordRegex;

        email = emailText.getText().toString();
        password = passwordText.getText().toString();
        minLength = 6;
        //TODO:Review this regex stuff later (Might not even be needed here)
        //emailRegex = "^(.+)@(\\\\S+)$";
        //passwordRegex =  "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";



        //TODO: implement more validation techniques that you will enter into userValidationclass

        if (!UserValidation.isValidEmail(email)){
            Toast.makeText(getApplicationContext(), "Please enter Valid Email", Toast.LENGTH_LONG).show();
            return;
        } else if (!UserValidation.isValidPassword(password, minLength)){
            Toast.makeText(getApplicationContext(), "Please enter Valid Password", Toast.LENGTH_LONG).show();
            return;
        }

       /** TODO:same as above regex bits and cheeck may not be required here
        if(!email.matches(emailRegex)){
            Toast.makeText(getApplicationContext(), "Please enter a valid email address", Toast.LENGTH_LONG).show();
            return;
        }

        if(!password.matches(passwordRegex)){
            Toast.makeText(getApplicationContext(), "Please enter a password with at least one digit, lowercase letter, upper case letter, special character, no whitespace and at least eight characters long", Toast.LENGTH_LONG).show();
            return;
        }**/


       //this checks if the email address is valid(and in the database)
       fAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
           @Override
           public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
               if(task.isSuccessful()){
                   SignInMethodQueryResult result = task.getResult();
                   if (result!= null && result.getSignInMethods() != null && result.getSignInMethods().size()>0){
                       Toast.makeText(getApplicationContext(), "Password does not match the Email address ", Toast.LENGTH_LONG).show();
                       return;
                   }
               }
           }
       });


        fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    String errorMessage = "Login Failed: ";
                    if (task.getException() != null){
                        errorMessage = errorMessage + task.getException().getMessage();
                    }
                    Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                }
            }
        });



    }


}
