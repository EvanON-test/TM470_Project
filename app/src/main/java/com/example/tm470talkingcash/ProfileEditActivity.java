package com.example.tm470talkingcash;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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





        //Saves inputs when clicked
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
                        Toast.makeText(getApplicationContext(), "No such document", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d(TAG, "Get failed", task.getException());
                    Toast.makeText(getApplicationContext(), "Get failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




        /**
         * This method updaets the users details based on their inputs
         * **/


        private void saveProfileEdit(){

            //initializes the variables based oin their inputs
            String username = usernameText.getText().toString().trim();
            String email = emailText.getText().toString().trim();
            String newPassword = newPasswordText.getText().toString().trim();
            String confirmNewPassword = confirmNewPasswordText.getText().toString().trim();




            //Validates the user inputs using methods imported from the UserValidation class
            if (UserValidation.isInvalidUsername(username)){
                Toast.makeText(getApplicationContext(), "Please enter a Valid Username", Toast.LENGTH_LONG).show();
                return;
            } else if (!UserValidation.isValidEmail(email)){
                Toast.makeText(getApplicationContext(), "Please enter a Valid Email", Toast.LENGTH_LONG).show();
                return;
            } else if (!UserValidation.isValidPassword(newPassword)){
                Toast.makeText(getApplicationContext(), "Please conform to password requirements", Toast.LENGTH_LONG).show();
                return;
            } else if (!UserValidation.doPasswordsMatch(newPassword, confirmNewPassword)){
                Toast.makeText(getApplicationContext(), "Please ensure you have confirmed passwords appropriately", Toast.LENGTH_LONG).show();
                return;
            }


            //Gets userID and creates a docref to the specific user document based on that ID
            String currentUserID = fAuth.getCurrentUser().getUid();
            DocumentReference docRef = db.collection("users").document(currentUserID);

            Map<String, Object> updates = new HashMap<>();
            updates.put("username", username);
            updates.put("userEmail", email);




            //TODO: need to add a username and email check here that checks all users, except from the current, so that there wont be any duplicate usernames and emails
            //Updates the specific document based on the Hashmap. On success will also update the auth with hte new details
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


        /**
        //TODO: This is the username check so far....needs further work
        private void usernameCheck(String username, String currentUserId){

            db.collection("users").whereEqualTo("username", username).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()){
                        if (!task.getResult().isEmpty()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (!document.getString("userId").equals(currentUserId)) {
                                    // The username is taken by another user
                                    Toast.makeText(getApplicationContext(), "Username is taken", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }
                            Toast.makeText(getApplicationContext(), "Username unchanged", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Username is available", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.w(TAG, "Error updating username", task.getException());
                        Toast.makeText(getApplicationContext(), "Error updating username", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }**/


}

