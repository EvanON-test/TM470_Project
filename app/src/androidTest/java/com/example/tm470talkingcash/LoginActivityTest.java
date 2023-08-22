package com.example.tm470talkingcash;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;


//TODO: This does not work. I've attempted and struggled with this but will return after more reading

@RunWith(MockitoJUnitRunner.class)
@LargeTest
public class LoginActivityTest {

    private FirebaseAuth mockFirebaseAuth;

    @Before
    public void setUp() {

        Intents.init();
        mockFirebaseAuth = Mockito.mock(FirebaseAuth.class);

        ActivityScenario<LoginActivity> activityScenario = ActivityScenario.launch(LoginActivity.class);
        activityScenario.onActivity(new ActivityScenario.ActivityAction<LoginActivity>() {
            @Override
            public void perform(LoginActivity activity) {
                activity.setFirebaseAuth(mockFirebaseAuth);
            }
        });

    }

    @After
    public void tearDown() {
        // Release Intents
        Intents.release();
    }



    @Test
    public void testNavigateToRegistration() {



    }


}

