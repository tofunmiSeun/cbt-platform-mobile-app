package com.unilorin.vividmotion.pre_cbtapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;
import com.unilorin.vividmotion.pre_cbtapp.R;
import com.unilorin.vividmotion.pre_cbtapp.models.Course;

public class TakeQuizActivity extends AppCompatActivity {

    private Course courseForQuiz;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_quiz);

        String courseObjectJson = getIntent().getStringExtra("courseForQuiz");
        courseForQuiz = new Gson().fromJson(courseObjectJson, Course.class);
    }
}
