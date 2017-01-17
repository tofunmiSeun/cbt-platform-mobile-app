package com.unilorin.vividmotion.pre_cbtapp.activities;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.unilorin.vividmotion.pre_cbtapp.R;
import com.unilorin.vividmotion.pre_cbtapp.fragments.AddCourseFragment;
import com.unilorin.vividmotion.pre_cbtapp.fragments.UserProfileFragment;

public class UserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blank_content);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("User Profile");

        UserProfileFragment userProfileFragment = new UserProfileFragment();
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        fm.beginTransaction().replace(R.id.content, userProfileFragment).commit();
    }
}
