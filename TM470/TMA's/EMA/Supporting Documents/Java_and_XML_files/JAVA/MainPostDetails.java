package com.example.tm470talkingcash;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This class creates a slimmed version of the postDetialsActivity for useage in the post view holder of the Main thread recyclerview
 */
public class MainPostDetails extends AppCompatActivity {

    /**
     * TODO: I realsied that this code wasn't required it was just the XML however I won't fully delete it just yet
     * TODO: when deleted make sure that the XML doesnt refrence it any more :)
    private TextInputEditText userComment;
    private String postID;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_post_details);

        TextView postUsernameTextView = findViewById(R.id.mpost_deats_username);
        TextView postTitleTextView = findViewById(R.id.mpost_deats_title);
        TextView postTagsTextView = findViewById(R.id.mpost_deats_tags);
        Button postHyperlinkButton = findViewById(R.id.mpost_deats_hyperlink);

        postID = getIntent().getStringExtra("POST_ID");
        Log.d("MainPostDetailsActivity1", "PostID: " + postID.toString());

        //Fetches the document from the 'posts' firestore collection using the postID
        FirebaseFirestore.getInstance().collection("posts").document(postID).get().addOnSuccessListener(documentSnapshot -> {
                    //converts the firestore document ot a post object
                    Post post = documentSnapshot.toObject(Post.class);


                    //Sets the text of the postUsernameTextView to the username of the poster.
                    postUsernameTextView.setText(post.getUsername());
                    //Sets the text of the postTitleTextView to the title of the post.
                    postTitleTextView.setText(post.getTitle());
                    //Joins the list of tags from the post inot a string seprated buy a comma ','
                    String tags = TextUtils.join(", ", post.getTags());
                    postTagsTextView.setText(tags);
                    //Sets teh text of the posthyperlinkbutton to the hyperlink of the post
                    postHyperlinkButton.setText(post.getHyperlink());
                    Log.d("PostDetailsActivity", "User DocumentSnapshot: " + documentSnapshot.toString());

                    //Sets an onclick listener on the posthyperlink button that opens the hyperlink in the devices browser when the button is clicked
                    postHyperlinkButton.setOnClickListener(view -> {
                        String url = post.getHyperlink();
                        //TODO: Review if this validation is needed as it's already been validatd in creation. Maybe make sure is implemented fully elsewhere and just have a simple check here
                        if (url != null && (url.startsWith("http://")) || (url.startsWith("https://"))) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(post.getHyperlink()));
                            startActivity(browserIntent);
                        } else {
                            Toast.makeText(this, "Invalid URL", Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                //If there is an error getting the document then a error toast is shown and entered into the log
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error getting document", Toast.LENGTH_SHORT).show();
                    Log.w(TAG, "Error getting document", e);
                });
    }
    **/
}