package com.unilorin.vividmotion.pre_cbtapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.unilorin.vividmotion.pre_cbtapp.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart(){
        super.onStart();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//            }
//        }).start();
    }

}
