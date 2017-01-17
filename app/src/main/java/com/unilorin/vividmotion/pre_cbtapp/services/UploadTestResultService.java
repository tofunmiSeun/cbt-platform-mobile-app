package com.unilorin.vividmotion.pre_cbtapp.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.unilorin.vividmotion.pre_cbtapp.activities.DashboardActivity;
import com.unilorin.vividmotion.pre_cbtapp.managers.data.QuestionDBHelper;
import com.unilorin.vividmotion.pre_cbtapp.models.Question;
import com.unilorin.vividmotion.pre_cbtapp.models.TestResult;
import com.unilorin.vividmotion.pre_cbtapp.network.services.HTTPTestService;

import java.util.List;

public class UploadTestResultService extends Service {
    public UploadTestResultService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // upload test results that have not been uploaded from the db
                    HTTPTestService testService = new HTTPTestService(getApplicationContext());
                    testService.uploadTestResults();
                }catch (Exception e){
                    Log.e("UploadTestResultService", e.getMessage());
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

        Intent uploadResultIntent = new Intent(this, UploadTestResultService.class);
        PendingIntent uploadResultPendingIntent =
                PendingIntent.getBroadcast(this, 0, uploadResultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.set(AlarmManager.RTC, nextScheduledTime, uploadResultPendingIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
