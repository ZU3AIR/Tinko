package com.example.tinko;

import static org.junit.Assert.*;

import android.view.View;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

// we want to test the activity
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mainTestRule = new ActivityTestRule<MainActivity>(MainActivity.class); // created a test rule enables launching of activity

    private MainActivity mainmain = null; // create a reference to activity

    @Before
    public void setUp() throws Exception {
        mainmain = mainTestRule.getActivity();
    }

    @Test

    public void runTest()// testing mainActivity page
    {
        View v = mainmain.findViewById(R.id.imageView); // if the view is not null that means the test is successful
        assertNotNull(v);
    }



    @After
    public void tearDown() throws Exception {
        mainmain = null;
    }
}