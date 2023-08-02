package com.example.tm470talkingcash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;



public class MainActivity extends AppCompatActivity {

//TODO: I think this can be deleted but probabaly better to do at a later date after reviewing any footprint it has
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //sets the view to the main activity layout
        setContentView(R.layout.activity_main);

        //Gets user information from login
        Intent intent = getIntent();
        String userEmail = intent.getStringExtra("userEmail");
        String userUid = intent.getStringExtra("userUid");
        String userName = intent.getStringExtra("userName");
    }
}