package com.unilorin.vividmotion.pre_cbtapp.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.unilorin.vividmotion.pre_cbtapp.managers.data.QuestionDBHelper;
import com.unilorin.vividmotion.pre_cbtapp.models.Course;
import com.unilorin.vividmotion.pre_cbtapp.models.Question;
import com.unilorin.vividmotion.pre_cbtapp.network.services.HTTPTestService;

import java.util.List;

public class DownloadQuestionService extends Service {

    public DownloadQuestionService(){
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final long courseId = intent.getLongExtra("courseId", -1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Let it continue running until it is stopped.
                    HTTPTestService testService = new HTTPTestService(getApplicationContext());
                    List<Question> questionList;

                    if (courseId != -1) {
                        questionList = testService.getTestQuestionsFor(courseId, 40);
                    } else {
                        questionList = testService.getQuestionsForRegisteredCourses(40);
                    }

                    QuestionDBHelper questionDBHelper = new QuestionDBHelper(getApplicationContext());
                    questionDBHelper.saveQuestions(questionList);
                }
                catch (Exception e){
                    Log.e("DownloadQuestionService", e.getMessage());
                }
                finally {
                    rescheduleNextTry();
                }
            }
        }).start();

        return START_STICKY;
    }

    private void rescheduleNextTry(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        long nextScheduledTime = System.currentTimeMillis() + (1000 * 3600 * 24);

        Intent downloadQuestionsIntent = new Intent(this, DownloadQuestionService.class);
        PendingIntent downloadQuestionsPendingIntent =
                PendingIntent.getBroadcast(this, 0, downloadQuestionsIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.set(AlarmManager.RTC, nextScheduledTime, downloadQuestionsPendingIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
