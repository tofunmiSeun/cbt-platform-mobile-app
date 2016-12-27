package com.unilorin.vividmotion.pre_cbtapp.activities;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;
import com.unilorin.vividmotion.pre_cbtapp.R;
import com.unilorin.vividmotion.pre_cbtapp.models.Course;
import com.unilorin.vividmotion.pre_cbtapp.models.Question;
import com.unilorin.vividmotion.pre_cbtapp.network.services.HTTPTestService;

import java.util.List;

public class TakeQuizActivity extends AppCompatActivity {

    private Course courseForQuiz;
    private List<Question> testQuestions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_quiz);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String courseObjectJson = getIntent().getStringExtra("courseForQuiz");
        courseForQuiz = new Gson().fromJson(courseObjectJson, Course.class);
        setTitle(courseForQuiz.getCourseCode());

        new GetTestQuestionsTask().execute();
    }

    private class GetTestQuestionsTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... params) {
            HTTPTestService testService = new HTTPTestService();
            testQuestions = testService.getTestQuestionsFor(courseForQuiz);
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // TODO: initialise questions in recycler view
        }
    }
}
