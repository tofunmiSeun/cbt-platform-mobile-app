package com.unilorin.vividmotion.pre_cbtapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.internal.Excluder;
import com.unilorin.vividmotion.pre_cbtapp.R;
import com.unilorin.vividmotion.pre_cbtapp.managers.data.SharedPreferenceContract;
import com.unilorin.vividmotion.pre_cbtapp.models.User;

public class SplashActivity extends AppCompatActivity {

    Runnable redirectRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart() {
        super.onStart();

        redirectRunnable = new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceContract.FILE_NAME, MODE_PRIVATE);
                User currentUser = new Gson().fromJson(sharedPreferences.getString(SharedPreferenceContract.USER_ACCOUNT_JSON_STRING, null), User.class);

                if (currentUser == null) {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                } else if (currentUser.getStudentProfile() == null) {
                    startActivity(new Intent(SplashActivity.this, SetupActivity.class));
                }
                //else if (!sharedPreferences.contains(SharedPreferenceContract.PROFILE_PICTURE_STRING)) {
                //  startActivity(new Intent(SplashActivity.this, ChooseProfilePictureActivity.class));
                //}
                else {
                    startActivity(new Intent(SplashActivity.this, DashboardActivity.class));
                }
                finish();
            }
        };

        init(redirectRunnable, 3);
    }

    void init(final Runnable runnable, final long delayInSeconds) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(delayInSeconds * 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                runOnUiThread(runnable);
            }
        }).start();
    }
}
