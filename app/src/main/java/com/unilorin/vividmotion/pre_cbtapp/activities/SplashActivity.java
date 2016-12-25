package com.unilorin.vividmotion.pre_cbtapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.unilorin.vividmotion.pre_cbtapp.R;
import com.unilorin.vividmotion.pre_cbtapp.managers.data.SharedPreferenceContract;

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
                SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceContract.FILE_NAME, MODE_PRIVATE);

                if (!sharedPreferences.contains(SharedPreferenceContract.USER_ACCOUNT_JSON_STRING)){
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
                else if (!sharedPreferences.contains(SharedPreferenceContract.STUDENT_PROFILE_JSON_STRING)){
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
