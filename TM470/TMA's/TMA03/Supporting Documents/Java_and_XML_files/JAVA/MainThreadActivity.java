package com.example.tm470talkingcash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

//TODO: Need to implement some kind of unit and integration testing, will need to do some reading on these
//TODO: Remove the old Main activity
//TODO: create a post edit/delete function (this will come after the user profile creation element is flesh out)
//TODO: Also all posts need to have a username/profile associated to the poster so
// create a Profile page whereby users can change their username(which then needs to be linked to posts properly)
// and change emails/passwords ect
//TODO: create a comments page whereby the comments are viewable + can add to the comment thread from the Postdetails view
//TODO: Build a search element into the main page which searches the tags: ideally just a small text thing at top then a search results page?
//TODO: Need to adjust login page so that it is scrollable or is better at adjusting to diffeent phones (josh wasn't able to see register button)
//TODO: think about involving anoter language version, should be simple enough as it's just building of off stuff already in strings XML
//TODO: add a legal disclaimer, terms of use and GDPR data element to the registration activity
//TODO: review the xml against accesible principles. e.g. make usre it's useable for a screen reader. Will need to research this more
//TODO: add a reporting function to the user posts, to report any htat users deem break the terms of use

/**
 * Displays the user created posts to the users
 */
public class MainThreadActivity extends AppCompatActivity {

    private RecyclerView postRecyclerView;
    private FirestoreRecyclerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_thread);


        Button logoutButton = findViewById(R.id.logout_button);
        Button createPostButton = findViewById(R.id.create_post_main);

        /**
         * Sets click listens for the logout button and create post button respectively. Logging out will also sing out the current user
         */
        logoutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainThreadActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        createPostButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainThreadActivity.this, PostCreationActivity.class);
                startActivity(intent);
            }
        });

        //Reference to the recyclerview layout item in the activity_main_thread.xml file
        postRecyclerView = findViewById(R.id.post_recycler_view);

        //Sets layout manager for the recyclerview. This is responsible for arranging the individual elements.
        // Linear layout means it's scrollable in a linear way
        postRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Builds FirestoreRecyclerOptions which includes a query to fetch posts and order them by Timestamp in descending order
        FirestoreRecyclerOptions<Post> options = new FirestoreRecyclerOptions.Builder<Post>().setQuery(FirebaseFirestore.getInstance().collection("posts").orderBy("timestamp", Query.Direction.DESCENDING), Post.class).build();

        //Creates a FirestoreRecyclerAdapter, it listens to chnages in the firebase databse and updates he view accordingly
        adapter = new FirestoreRecyclerAdapter<Post, PostViewHolder>(options) {
            @NonNull
            @Override
            //Displays the data at a specific position, updates the contents of the itemview to reflect the post ata given postion
            public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
                //The LayoutInflater takes the provided xml layout file and inflates it to create the View object.
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_post_details, parent, false);
                //Returns a new ViewHolder that contains the View for each list item
                return new PostViewHolder(view);
            }

            @Override
            public void onBindViewHolder(PostViewHolder holder, int position, Post model){
                //Binds the post to the viewholder
                holder.bindPost(model);
                //sets an onclicklistener to the itemview so that it starts a post details activity when clicked
                holder.itemView.setOnClickListener(v -> {
                    //Creates intent to start post details activity
                    Intent intent = new Intent(MainThreadActivity.this, PostDetailsActivity.class);
                    //passes teh post_id to the post detail activity
                    intent.putExtra("POST_ID", model.getPostId());
                    //starts teh post details activity
                    startActivity(intent);
                });
            }


        };
        postRecyclerView.setAdapter(adapter);
    }

    /**
     * Sets the adapter to listen (and stop listening) for changes to the Firestore database
     */
    @Override
    protected void onStart(){
        super.onStart();
        if(adapter != null){
            adapter.startListening();
        }
    }
    @Override
    protected void onStop(){
        super.onStop();
        if(adapter != null){
            adapter.stopListening();
        }
    }
}

/**
 * Postviewholder represents the layout of each item in the recyclerview
 */
class PostViewHolder extends RecyclerView.ViewHolder{
    TextView titleView;
    TextView tagsView;
    Button hyperlinkView;

    public PostViewHolder(@NonNull View itemView){
        super(itemView);

        titleView = itemView.findViewById(R.id.post_deats_title);
        tagsView = itemView.findViewById(R.id.post_deats_tags);
        hyperlinkView = itemView.findViewById(R.id.post_deats_hyperlink);
    }
    //binds the post to the viewholder
    void bindPost (Post post){
        titleView.setText(post.getTitle());
        tagsView.setText(TextUtils.join(", ", post.getTags()));
        hyperlinkView.setText(post.getHyperlink());
    }
}