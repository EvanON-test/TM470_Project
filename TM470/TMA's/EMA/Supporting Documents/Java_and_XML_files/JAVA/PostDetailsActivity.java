package com.example.tm470talkingcash;


import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


import java.util.Date;


public class PostDetailsActivity extends AppCompatActivity {

    private TextInputEditText userComment;
    private String postID;
    private RecyclerView commentRecyclerView;
    private FirestoreRecyclerAdapter commentAdapter;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        TextView postUsernameTextView = findViewById(R.id.post_deats_username);
        TextView postTitleTextView = findViewById(R.id.post_deats_title);
        TextView postTagsTextView = findViewById(R.id.post_deats_tags);
        Button postHyperlinkButton = findViewById(R.id.post_deats_hyperlink);


        Toolbar toolbar = (Toolbar) findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);

        ImageView logoView = findViewById(R.id.logo_view);
        Button createPostButton = findViewById(R.id.create_post_main);

        postID = getIntent().getStringExtra("POST_ID");
        Log.d("PostDetailsActivity1", "PostID: " + postID.toString());

        userComment = findViewById(R.id.edit_text_new_comment);
        Button submitCommentButton = findViewById(R.id.button_submit_comment);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        /**
         * Sets click listens for the logo (home) button, create post button and submit post button respectively.
         *
         */

        //Refreshes main thread to show whole list of posts
        logoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostDetailsActivity.this, MainThreadActivity.class);
                startActivity(intent);
            }
        });

        //Navigates to create post activity
        createPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostDetailsActivity.this, PostCreationActivity.class);
                startActivity(intent);
            }
        });

        submitCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createComment();
            }
        });


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



        //Reference to the recyclerview layout item in the activity_main_thread.xml file
        commentRecyclerView = findViewById(R.id.comment_recycler_view);

        //Sets layout manager for the recyclerview. This is responsible for arranging the individual elements.
        // Linear layout means it's scrollable in a linear way
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Builds FirestoreRecyclerOptions which includes a query to fetch posts and order them by Timestamp in descending order//TODO: add that only the relevant comment post ID is - i tried .whereEqualTo("postID", postID)
        FirestoreRecyclerOptions<Comment> options = new FirestoreRecyclerOptions.Builder<Comment>().setQuery(FirebaseFirestore.getInstance().collection("comments").whereEqualTo("postID", postID).orderBy("timestamp", Query.Direction.DESCENDING), Comment.class).build();

        //Creates a FirestoreRecyclerAdapter, it listens to chnages in the firebase databse and updates he view accordingly
        commentAdapter = new FirestoreRecyclerAdapter<Comment, CommentViewHolder>(options) {

            @NonNull
            @Override
            //Displays the data at a specific position, updates the contents of the itemview to reflect the post ata given postion
            public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                //The LayoutInflater takes the provided xml layout file and inflates it to create the View object.
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false); //TODO: create this layout
                //Returns a new ViewHolder that contains the View for each list item
                return new CommentViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull CommentViewHolder holder, int position, @NonNull Comment model) {
                holder.bindPost(model);
            }


        };
        commentRecyclerView.setAdapter(commentAdapter);




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
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(PostDetailsActivity.this, ProfileActivity.class);
                    intent.putExtra("USER_ID", user.getUid());
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "ERROR: You're not recognised as logged in", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_about:
                Intent intentTwo = new Intent(PostDetailsActivity.this, AboutActivity.class);
                startActivity(intentTwo);
                return true;
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                Intent intentThree = new Intent(PostDetailsActivity.this, LoginActivity.class);
                startActivity(intentThree);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    /**TODO: possible code could be improved with:
     *
     * For convenience, you can also create references by specifying the path to a document or collection as a string, with path components separated by a forward slash (/).
     * For example, to create a reference to the alovelace document:
     * DocumentReference alovelaceDocumentRef = db.document("users/alovelace"); But instead of 'lovelace' you use 'Post_ID'??
     :**/

    /**
     *
     *
     * Creates a post based on the user inputs into title, tag and hyperlink inputs. Some simple validation
     * is implemented.
     */
    private void createComment() {
        String commentText = userComment.getText().toString();


        /**
         * validates that inputs aren't empty and that url is valid
         */
        if (commentText.isEmpty()) {
            Toast.makeText(PostDetailsActivity.this, "Make sure comment isn't empty", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = mAuth.getCurrentUser().getUid();

        db.collection("users").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Use the username
                        String username = document.getString("username");

                        // Generate a unique ID for the comment
                        String commentId = db.collection("comments").document().getId();

                        Comment userComment = new Comment(username, commentText, new Date().getTime(), postID, commentId);

                        db.collection("comments").document(commentId).set(userComment)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(PostDetailsActivity.this, "Comment created", Toast.LENGTH_SHORT).show();
                                        //Refreshes activity after comment created
                                        recreate();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(PostDetailsActivity.this, "Failed to create Comment", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        commentAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        commentAdapter.stopListening();
    }







}

















/**
 *
 * CommentViewholder represents the layout of each item in the recyclerview
 */
class CommentViewHolder extends RecyclerView.ViewHolder {
    TextView usernameView;
    TextView commentView;
    TextView timestampView;


    public CommentViewHolder(@NonNull View itemView) {
        super(itemView);

        usernameView = itemView.findViewById(R.id.comment_username);
        commentView = itemView.findViewById(R.id.comment_text);
        timestampView = itemView.findViewById(R.id.timestamp);
    }

    //binds the comment to the viewholder
    void bindPost(Comment comment) {
        usernameView.setText(comment.getUsername());
        commentView.setText(comment.getCommentText());
        //Calculates, using the comment documents timestamp, how long ago the comment was created and dispalys that.
        long commentTimestamp = comment.getTimestamp();
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                commentTimestamp,
                System.currentTimeMillis(),
                DateUtils.SECOND_IN_MILLIS);

        timestampView.setText(timeAgo);


    }
}
