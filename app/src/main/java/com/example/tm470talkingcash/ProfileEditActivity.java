package com.example.tm470talkingcash;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.tm470talkingcash.databinding.ActivityProfileEditBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfileEditActivity extends AppCompatActivity {


    private TextInputEditText usernameText;
    private TextInputEditText emailText;
    private TextInputEditText confirmNewPasswordText;
    private TextInputEditText newPasswordText;

    private Button saveBtn;

    private FirebaseAuth fAuth;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        db = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        //Finds the views based on their ID's
        usernameText = findViewById(R.id.edit_username_input);
        emailText = findViewById(R.id.edit_email_input);
        newPasswordText = findViewById(R.id.edit_password_input);
        confirmNewPasswordText = findViewById(R.id.confirm_edit_password_input);

        saveBtn = findViewById(R.id.save_user_profile);


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileEdit();
            }
        });



        String currentUserID = fAuth.getCurrentUser().getUid();
        DocumentReference docRef = db.collection("users").document(currentUserID);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String username = document.getString("username");
                        String email = document.getString("userEmail");
                        usernameText.setText(username);
                        emailText.setText(email);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }


        /**
         * Here I am already populatin the usrname and email based on the data in the users collection on firebse
         * (I leave pssword empty as it will then act as a check against the users authenticity by having to input it to chnage their password)
         * **/

        //TODO: this is technically working as it allows the user to change email, username, passwords ect HOWEVER it also
        //TODO: causes the app to crash
        private void saveProfileEdit(){
            String username;
            String email;
            String confirmNewPassword;
            String newPassword;
            Integer minLength;

            //initializes the variables based oin their inputs
            username = usernameText.getText().toString().trim();
            email = emailText.getText().toString().trim();
            newPassword = newPasswordText.getText().toString().trim();
            confirmNewPassword = confirmNewPasswordText.getText().toString().trim();
            minLength = 6;



            //TODO: review if more validation techniques are required, restructure into UserValidation class where possible. Maybe change this to Case structure?
            //Validates the user inputs using methods imported from the UserValidation class
            if (!UserValidation.isValidUsername(username, minLength)){
                Toast.makeText(getApplicationContext(), "Please enter a Valid Username (greater than 6 characters)", Toast.LENGTH_LONG).show();
                return;
            } else if (!UserValidation.isValidEmail(email)){
                Toast.makeText(getApplicationContext(), "Please enter a Valid Email", Toast.LENGTH_LONG).show();
                return;
            } else if (!UserValidation.isValidPassword(newPassword, minLength)){
                Toast.makeText(getApplicationContext(), "Please enter Valid Password (greater than 6 characters)", Toast.LENGTH_LONG).show();
                return;
            } else if (!UserValidation.doPasswordsMatch(newPassword, confirmNewPassword)){
                Toast.makeText(getApplicationContext(), "Please ensure you have confirmed passwords appropriately", Toast.LENGTH_LONG).show();
                return;
            }


            String currentUserID = fAuth.getCurrentUser().getUid();
            DocumentReference docRef = db.collection("users").document(currentUserID);

            Map<String, Object> updates = new HashMap<>();
            updates.put("username", username);
            updates.put("userEmail", email); // If userEmail is the key for email in your Firestore

            docRef.update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // Updates Firebase Authentication
                    fAuth.getCurrentUser().updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                fAuth.getCurrentUser().updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(ProfileEditActivity.this, ProfileActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                        } else {
                                            Log.w(TAG, "Error updating password", task.getException());
                                            Toast.makeText(getApplicationContext(), "Error updating password", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                Log.w(TAG, "Error updating email", task.getException());
                                Toast.makeText(getApplicationContext(), "Error updating email", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error updating Firestore document", e);
                    Toast.makeText(getApplicationContext(), "Error updating Firestore document", Toast.LENGTH_SHORT).show();
                }
            });



        }


}

