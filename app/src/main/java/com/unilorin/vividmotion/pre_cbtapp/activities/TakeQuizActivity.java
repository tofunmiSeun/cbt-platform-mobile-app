package com.unilorin.vividmotion.pre_cbtapp.activities;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.unilorin.vividmotion.pre_cbtapp.R;
import com.unilorin.vividmotion.pre_cbtapp.fragments.TestAssessmentDialogFragment;
import com.unilorin.vividmotion.pre_cbtapp.managers.data.QuestionDBHelper;
import com.unilorin.vividmotion.pre_cbtapp.managers.data.SharedPreferenceContract;
import com.unilorin.vividmotion.pre_cbtapp.managers.data.TestResultDBHelper;
import com.unilorin.vividmotion.pre_cbtapp.models.Course;
import com.unilorin.vividmotion.pre_cbtapp.models.Question;
import com.unilorin.vividmotion.pre_cbtapp.models.TestResult;
import com.unilorin.vividmotion.pre_cbtapp.models.User;
import com.unilorin.vividmotion.pre_cbtapp.utils.AttemptedQuestionsUtils;
import com.unilorin.vividmotion.pre_cbtapp.views.adapters.QuestionsRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class TakeQuizActivity extends AppCompatActivity {

    private Course courseForQuiz;
    private List<Question> testQuestions;
    private RecyclerView questionsRecyclerView;
    private QuestionsRecyclerViewAdapter questionsRecyclerViewAdapter;
    private FloatingActionButton fab;
    private TextView timerTextView;
    private CountDownTimer timer;
    private boolean testSubmitted;
    private int totalQuestions;

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

        totalQuestions = 10;

        new GetTestQuestionsTask().execute();
    }

    String timeFormat(long remainingSeconds){
        String s;
        long minutes = remainingSeconds / 60;
        long seconds = remainingSeconds % 60;
        s = String.format("%02d:%02d", minutes, seconds);
        return s;
    }

    private List<Long> getIdsOfQuestions(List<Question> questionList){
        List<Long> ids = new ArrayList<>();
        for (Question q : questionList){
            ids.add(q.getId());
        }
        return ids;
    }

    private class GetTestQuestionsTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... params) {
            testQuestions = getQuestions();
            return null;
        }

        private List<Question> getQuestions(){
            List<Question> questionList;
            QuestionDBHelper questionDBHelper = new QuestionDBHelper(getApplicationContext());
            questionList = questionDBHelper.getFreshQuestionsForCourse(courseForQuiz.getId(), totalQuestions);

            if (questionList.size() < totalQuestions){
                int remainingQuestions = totalQuestions - questionList.size();
                List<Question> backUpQuestions = questionDBHelper.getQuestionsForCourse(courseForQuiz.getId(), remainingQuestions,
                        getIdsOfQuestions(questionList));

                for (Question q : backUpQuestions){
                    questionList.add(q);
                }
            }

            return questionList;
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
                    timer = null;

                    fab.setImageResource(R.drawable.ic_assessment_white_24dp);

                    AttemptedQuestionsUtils attemptedQuestionsUtils = new AttemptedQuestionsUtils(getApplicationContext());
                    attemptedQuestionsUtils.addIds(getIdsOfQuestions(testQuestions));

                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            TestResult testResult = new TestResult();
                            testResult.setCourseId(courseForQuiz.getId());

                            SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceContract.FILE_NAME, MODE_PRIVATE);
                            User currentUser = new Gson().fromJson(sharedPreferences.getString(SharedPreferenceContract.USER_ACCOUNT_JSON_STRING, null), User.class);

                            testResult.setUserId(currentUser.getId());
                            testResult.setTotalQuestionsCount(testQuestions.size());
                            testResult.setQuestionsAnsweredCount(params.get("answeredQuestions"));
                            testResult.setCorrectAnswerCount(params.get("correctAnswers"));
                            testResult.setTimeOfTestInMilliseconds(new Date().getTime());

                            TestResultDBHelper testResultDBHelper = new TestResultDBHelper(getApplicationContext());
                            testResultDBHelper.saveTestResult(testResult);

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
