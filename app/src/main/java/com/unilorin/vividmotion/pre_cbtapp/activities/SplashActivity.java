package com.unilorin.vividmotion.pre_cbtapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.unilorin.vividmotion.pre_cbtapp.R;
import com.unilorin.vividmotion.pre_cbtapp.database.UserAccountDBHelper;
import com.unilorin.vividmotion.pre_cbtapp.managers.data.SharedPreferenceContract;
import com.unilorin.vividmotion.pre_cbtapp.models.StudentProfile;
import com.unilorin.vividmotion.pre_cbtapp.models.User;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart(){
        super.onStart();
        new Thread(new Runnable() {
            @Override
            public void run() {
                UserAccountDBHelper userAccountDBHelper = new UserAccountDBHelper(getApplicationContext());
                User loggedInUser = userAccountDBHelper.getUser();

                if (loggedInUser == null){
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
                else if (loggedInUser.getStudentProfile() == null){
                    startActivity(new Intent(SplashActivity.this, SetupActivity.class));
                }
                else {
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                }
                finish();
            }
        }).start();
    }
}
