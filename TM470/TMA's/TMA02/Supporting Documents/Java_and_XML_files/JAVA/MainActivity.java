package com.example.tm470talkingcash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.FirebaseApp;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //sets the view to the login activity layout
        setContentView(R.layout.activity_main);

        //Gets user information from login
        Intent intent = getIntent();
        String userEmail = intent.getStringExtra("userEmail");
        String userUid = intent.getStringExtra("userUid");
        String userName = intent.getStringExtra("userName");
    }
}