package com.example.tm470talkingcash;

import static androidx.fragment.app.FragmentManager.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

//TODO: MUST HAVE

//TODO: make a class holding the toolbar/ oncreate options ect stuff - the code is repeated across too many activites
//TODO post creation and post edit need Functional toolbars
//TODO: need to increase the validation on the profile edit names and email
//TODO: sort out green background of logo on start up
//TODO: Need to further develop the unit, integration and accessibility testing, requires further reading
//TODO: create a post edit/delete function
//TODO: Flesh out the template for legal disclaimer, terms of use and GDPR


//TODO: COULD HAVE

//TODO: add a reporting function to the user posts, to report any htat users deem break the terms of use
//TODO: Review implementation of fragment at a later date (even possbily replaceing tooolbar??)









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

        //Initialises the toolbar - same in every activity where i hav included a custom toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);

        //Finds the views within the layout to attach onclick and query listeenrs to them
        ImageView logoView = findViewById(R.id.logo_view);
        SearchView searchView = findViewById(R.id.toolbar_searchview);
        Button createPostButton = findViewById(R.id.create_post_main);

        /**
         * Sets on click listens for the logo to navigate to the Mainthreadactivity and the create post button . Sets a query
         * listener to the searchview
         *
         */

        //Refreshes main thread to show whole list of posts
        logoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainThreadActivity.class);
                startActivity(intent);
            }
        });

        //Navigates to create post activity
        createPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PostCreationActivity.class);
                startActivity(intent);
            }
        });

        //Both query listeners takes users query as input for filtering the posts. The 'submit' option is currently empty however the 'text change' will return filter posts without physically actioning it
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //TODO: Review wha tto include here later
                return false;
            }

            /**
             * returns the fileter posts without having to press 'enter'
             * @param query the new content of the query text field.
             *
             * @return
             */
            @Override
            public boolean onQueryTextChange(String query) {
                //TODO: this needs to be done in a better structure
                if (query.isEmpty()){
                    Intent intent = new Intent(MainThreadActivity.this, MainThreadActivity.class);
                    startActivity(intent);
                } else {
                    String lowerAndTrimQuery = query.trim().toLowerCase();
                    searchPosts(lowerAndTrimQuery);
                }
                return false;
            }
        });


        //Reference to the recyclerview layout item in the mainthread xml file
        postRecyclerView = findViewById(R.id.post_recycler_view);

        //Sets layout manager for the recyclerview. Responsible for arranging the individual elements.
        //Linear layout means it's scrollable in a linear way
        postRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        
        //Builds FirestoreRecyclerOptions which includes a query to fetch posts and order them by timestamp in descending order
        FirestoreRecyclerOptions<Post> options = new FirestoreRecyclerOptions.Builder<Post>().setQuery(FirebaseFirestore.getInstance().collection("posts").orderBy("timestamp", Query.Direction.DESCENDING), Post.class).build();

        //Creates a FirestoreRecyclerAdapter, it listens to changes in the firebase databse and updates he view accordingly
        adapter = new FirestoreRecyclerAdapter<Post, PostViewHolder>(options) {
            @NonNull
            @Override
            //Displays the data at a specific position, updates the contents of the itemview to reflect the post ata given postion
            public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                //The LayoutInflater takes the provided xml layout file and inflates it to create the View object.
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_post_details, parent, false);
                //Returns a new ViewHolder that contains the View for each list item
                return new PostViewHolder(view);
            }

            @Override
            public void onBindViewHolder(PostViewHolder holder, int position, Post model) {
                //Binds the post to the viewholder
                holder.bindPost(model);
                //sets an onclicklistener to the itemview so that it starts a post details activity when clicked
                holder.itemView.setOnClickListener(v -> {
                    //Creates intent to start post details activity
                    Intent intent = new Intent(MainThreadActivity.this, PostDetailsActivity.class);
                    //passes teh post_id to the post detail activity
                    intent.putExtra("POST_ID", model.getPostId());
                    Log.d("MainActivity", "Post ID:" + model.getPostId());

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
    protected void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

    /**
     *
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
                    Intent intent = new Intent(MainThreadActivity.this, ProfileActivity.class);
                    intent.putExtra("USER_ID", user.getUid());
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "ERROR: You're not recognised as logged in", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_about:
                //Navigates to the about page - same for all users
                Intent intentTwo = new Intent(MainThreadActivity.this, AboutActivity.class);
                startActivity(intentTwo);
                return true;
            case R.id.action_logout:
                //Logs users out of instances and app
                FirebaseAuth.getInstance().signOut();
                Intent intentThree = new Intent(MainThreadActivity.this, LoginActivity.class);
                startActivity(intentThree);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     *This method searches the list of posts on firestore for posts that have tag's that matches the query parameter
     *
     *
     */
    private void searchPosts(String query) {
        //Retrieves an instance of the database
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //Builds a refrence to the posts collection
        CollectionReference postsRef = db.collection("posts");
        //Builds a query object, essientailly searcing hte tags field of the posts collection for anything matching the query
        Query queryObj = postsRef.whereArrayContains("tags", query);
        // Build options for the FirestoreRecyclerAdapter using the query and the Post model class.
        FirestoreRecyclerOptions<Post> options = new FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(queryObj, Post.class)
                .build();

        // Create a new FirestoreRecyclerAdapter, using the Post model and PostViewHolder.
        adapter = new FirestoreRecyclerAdapter<Post, PostViewHolder>(options) {
            @NonNull
            @Override
            public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                //
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_post_details, parent, false);
                return new PostViewHolder(view);
            }

            // The onBindViewHolder method is used to bind data to each item in the RecyclerView.
            @Override
            public void onBindViewHolder(PostViewHolder holder, int position, Post model) {
                holder.bindPost(model);
                holder.itemView.setOnClickListener(v -> {
                    //
                    Intent intent = new Intent(MainThreadActivity.this, PostDetailsActivity.class);
                    intent.putExtra("POST_ID", model.getPostId());
                    startActivity(intent);
                });
            }
        };
        postRecyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}

/**
 * Postviewholder represents the layout of each item in the recyclerview
 * Utlises the activtymainpostdetails as this is a slimmed version of the posts without the comments element
 */
class PostViewHolder extends RecyclerView.ViewHolder{
    TextView usernameView;
    TextView titleView;
    TextView tagsView;
    Button hyperlinkView;




    public PostViewHolder(@NonNull View itemView){
        super(itemView);

        //TODO - changes made here too
        usernameView = itemView.findViewById(R.id.mpost_deats_username);
        titleView = itemView.findViewById(R.id.mpost_deats_title);
        tagsView = itemView.findViewById(R.id.mpost_deats_tags);
        hyperlinkView = itemView.findViewById(R.id.mpost_deats_hyperlink);

        ;
    }
    //binds the post to the viewholder
    void bindPost (Post post){
        usernameView.setText(post.getUsername());
        titleView.setText(post.getTitle());
        tagsView.setText(TextUtils.join(", ", post.getTags()));
        //The below code parses and 'trims' the Uri to a shortened, simplified version to display
        String fullHyperlink = post.getHyperlink();
        hyperlinkView.setText(Uri.parse(fullHyperlink).getHost());
    }
}

