package com.example.tm470talkingcash;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        TextView usernameTextView = findViewById(R.id.text_username);
        TextView emailTextView = findViewById(R.id.text_email);
        Button editProfileButton = findViewById(R.id.edit_user_profile);
        Button deleteProfileButton = findViewById(R.id.delete_user_profile);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPasswordDialog();
            }
        });

        String userID = getIntent().getStringExtra("USER_ID");
        //Fetches the document from the 'posts' firestore collection using the userID
        FirebaseFirestore.getInstance().collection("users").document(userID).get().addOnSuccessListener(documentSnapshot -> {
                    //converts the firestore document ot a userprofile object
                    UserProfile userProfile = documentSnapshot.toObject(UserProfile.class);

                    //Sets the text of the postTitleTextView to the title of the post.
                    usernameTextView.setText(userProfile.getUsername());

                    emailTextView.setText(userProfile.getUserEmail());



                })
                //If there is an error getting the document then a error toast is shown and entered into the log
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error getting document", Toast.LENGTH_SHORT).show();
                    Log.w(TAG, "Error getting document", e);
                });



        //TODO: maybe require password to be entered before delting also?

        deleteProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId = mAuth.getCurrentUser().getUid();

                db.collection("users").document(userId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Document deleted");
                        mAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Log.d(TAG, "User Account Deleted");
                                    Toast.makeText(getApplicationContext(), "Account deleted", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Log.w(TAG, "Error deleting user account", task.getException());
                                }
                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
            }
        });


        }



    private void showPasswordDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Password");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);


        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String enteredPassword = input.getText().toString();
                checkPasswordAndRedirect(enteredPassword);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    private void checkPasswordAndRedirect(String enteredPassword){
        String emailCheck = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        FirebaseAuth.getInstance().signInWithEmailAndPassword(emailCheck, enteredPassword).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Intent intent = new Intent(ProfileActivity.this, ProfileEditActivity.class);
                startActivity(intent);
            }
        });
    }

}