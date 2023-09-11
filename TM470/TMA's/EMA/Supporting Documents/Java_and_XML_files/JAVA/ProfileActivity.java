package com.example.tm470talkingcash;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);

        ImageView logoView = findViewById(R.id.logo_view);
        Button createPostButton = findViewById(R.id.create_post_main);

        TextView usernameTextView = findViewById(R.id.text_username);
        TextView emailTextView = findViewById(R.id.text_email);
        Button editProfileButton = findViewById(R.id.edit_user_profile);
        Button deleteProfileButton = findViewById(R.id.delete_user_profile);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        String userID = mAuth.getCurrentUser().getUid();


        /**
         * Sets click listens for the logo (home) button, create post button and edit profile button respectively.
         *
         */

        //Navigates to mainthreadactivity
        logoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, MainThreadActivity.class);
                startActivity(intent);
            }
        });

        //Navigates to create post activity
        createPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, PostCreationActivity.class);
                startActivity(intent);
            }
        });

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String context = "edit";
                showPasswordDialog(context);
            }
        });

        deleteProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String context = "delete";
                showPasswordDialog(context);
            }
        });


        /**
         * Fetches information from the users document, specific to users ID, to populate the text views with their details
         */
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

    }


    /**
     * Deletes document in users collection based on the current userID. If successful will then attempt to delete the user in Authentication database as well,
     * Will flash up toasts and tags in the response to the outcome
     */
        private void deleteUser() {
            String userID = mAuth.getCurrentUser().getUid();

            db.collection("users").document(userID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
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
                                        Log.w(TAG, "Error deleting user Authentication account", task.getException());
                                        Toast.makeText(getApplicationContext(), "Error deleting user Authentication account", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error deleting document", e);
                            Toast.makeText(getApplicationContext(), "Error deleting document", Toast.LENGTH_LONG).show();
                        }
                    });
        }




    /**
     * Creates the options menu in the toolbar which currently allows for the navigation to the profile and about pages as well as
     *  the logout function
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_profile:
                //navigates to the profile identified by the currewnt user's ID
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                    intent.putExtra("USER_ID", user.getUid());
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "ERROR: You're not recognised as logged in", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_about:
                //Navigates tto the about page
                Intent intentTwo = new Intent(ProfileActivity.this, AboutActivity.class);
                startActivity(intentTwo);
                return true;
            case R.id.action_logout:
                //Logs out the user
                FirebaseAuth.getInstance().signOut();
                Intent intentThree = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intentThree);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * Creates a password check before navigation to profile edit activity
     */
    private void showPasswordDialog(String context){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Password");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);


        //Actions taken when confirm button in alert dialog pressed. Essentially uses the check password method further below
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String enteredPassword = input.getText().toString();
                checkPasswordAndRedirect(enteredPassword, context);
            }
        });

        //Actions taken when cancel button in alert dialog pressed. Cancels the event
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }


    //Gets current users email and actions a another sign in event. Based on context will either navigate to profile edit or delete user
    private void checkPasswordAndRedirect(String enteredPassword, String context){
        String emailCheck = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        switch (context){
            case "edit":
                FirebaseAuth.getInstance().signInWithEmailAndPassword(emailCheck, enteredPassword).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(ProfileActivity.this, ProfileEditActivity.class);
                        startActivity(intent);
                    }
                });
                break;
            case "delete":
                FirebaseAuth.getInstance().signInWithEmailAndPassword(emailCheck, enteredPassword).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        deleteUser();
                    }
                });
                break;

            default:
                Toast.makeText(this, "Can't find context", Toast.LENGTH_SHORT).show();
                break;
        }

    }

}