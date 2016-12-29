package com.unilorin.vividmotion.pre_cbtapp.activities;

import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.unilorin.vividmotion.pre_cbtapp.R;
import com.unilorin.vividmotion.pre_cbtapp.fragments.TestAssessmentDialogFragment;
import com.unilorin.vividmotion.pre_cbtapp.models.Course;
import com.unilorin.vividmotion.pre_cbtapp.models.Question;
import com.unilorin.vividmotion.pre_cbtapp.models.TestResult;
import com.unilorin.vividmotion.pre_cbtapp.network.services.HTTPTestService;
import com.unilorin.vividmotion.pre_cbtapp.views.adapters.QuestionsRecyclerViewAdapter;

import java.util.List;
import java.util.Map;
import java.util.TimerTask;

public class TakeQuizActivity extends AppCompatActivity {

    private Course courseForQuiz;
    private List<Question> testQuestions;
    private RecyclerView questionsRecyclerView;
    private QuestionsRecyclerViewAdapter questionsRecyclerViewAdapter;
    private FloatingActionButton fab;
    private TextView timerTextView;
    private CountDownTimer timer;
    private boolean testSubmitted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_quiz);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        timerTextView = (TextView) findViewById(R.id.timerTextView);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        String courseObjectJson = getIntent().getStringExtra("courseForQuiz");
        courseForQuiz = new Gson().fromJson(courseObjectJson, Course.class);
        setTitle(courseForQuiz.getCourseCode());

        new GetTestQuestionsTask().execute();
    }

    String timeFormat(long remainingSeconds){
        String s;
        long minutes = remainingSeconds / 60;
        long seconds = remainingSeconds % 60;
        s = String.format("%02d:%02d", minutes, seconds);
        return s;
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
            questionsRecyclerView = (RecyclerView) findViewById(R.id.questionsRecyclerView);
            questionsRecyclerViewAdapter = new QuestionsRecyclerViewAdapter(testQuestions, TakeQuizActivity.this);
            questionsRecyclerView.setAdapter(questionsRecyclerViewAdapter);

            long testTime = 2 * 60 *1000L;
            timer = new CountDownTimer(testTime, 1000) {

                public void onTick(long millisUntilFinished) {
                    timerTextView.setText(timeFormat(millisUntilFinished / 1000));
                }

                public void onFinish() {
                    if (!testSubmitted) {
                        timerTextView.setText(timeFormat(0));
                        Toast.makeText(getBaseContext(), "Time up!", Toast.LENGTH_SHORT).show();
                        fab.callOnClick();
                    }
                }

            };

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO: 27/12/2016 Submit Quiz

                    final Map<String, Integer> params = questionsRecyclerViewAdapter.mark();
                    testSubmitted = true;
                    timer.cancel();

                    fab.setImageResource(R.drawable.ic_assessment_white_24dp);

                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            TestResult testResult = new TestResult();
                            testResult.setCourseId(courseForQuiz.getId());
                            //testResult.setUserId();
                            testResult.setTotalQuestionsCount(testQuestions.size());
                            testResult.setQuestionsAnsweredCount(params.get("answeredQuestions"));
                            testResult.setCorrectAnswersCount(params.get("correctAnswers"));

                            TestAssessmentDialogFragment dialogFragment =
                                    TestAssessmentDialogFragment.getInstance(testResult, courseForQuiz.getCourseCode());

                            dialogFragment.show(getSupportFragmentManager(), "dialog");
                        }
                    });
                }
            });

            timer.start();
        }
    }
}
