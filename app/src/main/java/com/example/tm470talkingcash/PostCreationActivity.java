package com.example.tm470talkingcash;

import static android.webkit.URLUtil.isValidUrl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * TODO: Review XML against your code a final time before testing
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

        titleText = findViewById(R.id.pc_title_input);
        tagText = findViewById(R.id.pc_tag_input);
        hyperlinkText = findViewById(R.id.pc_link_input);
        postBtn = findViewById(R.id.create_post_button);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPost();
            }
        });
    }

    /**
     * Creates a post based on the user inputs into title, tag and hyperlink inputs. Some simple validation
     * is implemented. Once validated a post is created based on the user inputs and user details
     */
    private void createPost(){
            String title = titleText.getText().toString();
            String tagsString = tagText.getText().toString();
            String hyperlink = hyperlinkText.getText().toString();

            //TODO: review and increase validation here or in a validation class
        /**
         * validates that inputs aren't empty and that url is valid
         */
            if (title.isEmpty() || tagsString.isEmpty() || hyperlink.isEmpty()){
                Toast.makeText(PostCreationActivity.this, "All fields are required. Please review content ", Toast.LENGTH_SHORT).show();
                return;
            }

            if(!isValidUrl(hyperlink)){
                Toast.makeText(PostCreationActivity.this, "Invalid URL", Toast.LENGTH_SHORT).show();
                return;
            }

            //TODO: Need to implement validation for this
            //Split and trim the list of tags
            List<String> tags = new ArrayList<>();
            for (String tag : tagsString.split(",")){
                tags.add(tag.trim());
            }

        /**
         * Creates a new post after assessing current user and getting their details as well as their inputs.
         * Adds post to the Posts database. If successful creates a toast in the affirmative and starts MainThreadActivity else
         * creates toast in the negative.
         */
        //TODO: Review if it's a better scenario to implement this in a separate class
            if(mAuth.getCurrentUser() != null){
                Post post = new Post(UUID.randomUUID().toString(), mAuth.getCurrentUser().getUid(), title, tags, hyperlink, new Date().getTime());
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
        }
}


