package com.example.tm470talkingcash;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;



import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        //Initialises the toolbar - same in every activity where i hav included a custom toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);

        //Finds the views within the layout to attach onclick listenrs to them
        ImageView logoView = findViewById(R.id.logo_view);
        Button createPostButton = findViewById(R.id.create_post_main);


        /**
         * Sets on click listens for the logo to navigate to the Mainthreadactivity and the create post button .
         *
         */

        //Refreshed main thread to show whole list of posts
        logoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AboutActivity.this, MainThreadActivity.class);
                startActivity(intent);
            }
        });

        //Navigates to create post activity
        createPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AboutActivity.this, PostCreationActivity.class);
                startActivity(intent);
            }
        });
    }




        /**
         * Create the options menu in the toolbar which currently allows for the navigation to the profile and about pages as well as
         * the logout function
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
                    //Will navigate to the profile activity, puts the users UserID so that the Profile views can populate based on current the users details
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        Intent intent = new Intent(AboutActivity.this, ProfileActivity.class);
                        intent.putExtra("USER_ID", user.getUid());
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "ERROR: You're not recognised as logged in", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                case R.id.action_about:
                    //Navigates to the about page - same for all users
                    Intent intentTwo = new Intent(AboutActivity.this, AboutActivity.class);
                    startActivity(intentTwo);
                    return true;
                case R.id.action_logout:
                    //Logs users out of the app & instance
                    FirebaseAuth.getInstance().signOut();
                    Intent intentThree = new Intent(AboutActivity.this, LoginActivity.class);
                    startActivity(intentThree);
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }



    }


