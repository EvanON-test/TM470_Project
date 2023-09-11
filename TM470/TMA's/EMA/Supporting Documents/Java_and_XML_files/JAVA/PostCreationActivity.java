package com.example.tm470talkingcash;

import static android.webkit.URLUtil.isValidUrl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * PostCreationAcitivity handles the User interface for creating a new post and adding it to the posts collection of the firestore dtabse
 */
public class PostCreationActivity extends AppCompatActivity {
    private TextInputEditText titleText, tagText, hyperlinkText;
    private Button postBtn;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_creation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);

        ImageView logoView = findViewById(R.id.logo_view);
        Button createPostButton = findViewById(R.id.create_post_main);

        titleText = findViewById(R.id.pc_title_input);
        tagText = findViewById(R.id.pc_tag_input);
        hyperlinkText = findViewById(R.id.pc_link_input);
        postBtn = findViewById(R.id.create_post_button);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


        //Refreshes main thread to show whole list of posts
        logoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostCreationActivity.this, MainThreadActivity.class);
                startActivity(intent);
            }
        });

        //Navigates to create post activity
        createPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostCreationActivity.this, PostCreationActivity.class);
                startActivity(intent);
            }
        });



        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPost();
            }
        });
    }


    /**
     * Creates the options menu in the toolbar which currently allows for the navigation to the profile and about pages as well as
     *  the logout function
     *
     *
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
                //Will navigate to the profile activity, puts the users UserID so that the Profile views can populate based on current the users details
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(PostCreationActivity.this, ProfileActivity.class);
                    intent.putExtra("USER_ID", user.getUid());
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "ERROR: You're not recognised as logged in", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_about:
                //Navigates to the about page - same for all users
                Intent intentTwo = new Intent(PostCreationActivity.this, AboutActivity.class);
                startActivity(intentTwo);
                return true;
            case R.id.action_logout:
                //Logs users out of the app & instance
                FirebaseAuth.getInstance().signOut();
                Intent intentThree = new Intent(PostCreationActivity.this, LoginActivity.class);
                startActivity(intentThree);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Creates a post based on the user inputs into title, tag and hyperlink inputs. Some simple validation
     * is implemented. Once validated a post is created based on the user inputs and user details
     */
    private void createPost() {
        String title = titleText.getText().toString();
        String tagsString = tagText.getText().toString();
        String hyperlink = hyperlinkText.getText().toString();
        String userId = mAuth.getCurrentUser().getUid();


        /**
         * validates that inputs aren't empty and that url is valid
         */
        if (title.isEmpty() || tagsString.isEmpty() || hyperlink.isEmpty()) {
            Toast.makeText(PostCreationActivity.this, "All fields are required. Please review content ", Toast.LENGTH_SHORT).show();
            return;
        }



        if (!isValidUrl(hyperlink)) {
            Toast.makeText(PostCreationActivity.this, "Invalid URL", Toast.LENGTH_SHORT).show();
            return;
        }


        //Split and trim the list of tags
        List<String> tags = new ArrayList<>();
        for (String tag : tagsString.split(",")) {
            String lowerTag = tag.toLowerCase();
            tags.add(lowerTag.trim());
        }


        db.collection("users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
            String username = documentSnapshot.getString("username");



            /**
             * Creates a new post after assessing current user and getting their details as well as their inputs.
             * Adds post to the Posts database. If successful creates a toast in the affirmative and starts MainThreadActivity else
             * creates toast in the negative.
             */
            if (mAuth.getCurrentUser() != null) {
                Post post = new Post(UUID.randomUUID().toString(), mAuth.getCurrentUser().getUid(), username, title, tags, hyperlink, new Date().getTime());
                Log.d("PostCreationActivity", "Post: " + post.toString());
                db.collection("posts").document(post.getPostId()).set(post)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(PostCreationActivity.this, "Post created", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(PostCreationActivity.this, MainThreadActivity.class);
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(PostCreationActivity.this, "Failed to create Post", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(PostCreationActivity.this, "You are not recognised as logged in", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e ->{
            Toast.makeText(this, "Failed to fetch users document", Toast.LENGTH_SHORT).show();
        });
    }
}


