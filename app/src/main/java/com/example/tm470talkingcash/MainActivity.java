package com.example.tm470talkingcash;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.FirebaseApp;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO:This means on opening the app opens to the login page, I don't think this is the appropriate way to do it. Once rest is fleshed out return to this and sort out :)
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
    }
}