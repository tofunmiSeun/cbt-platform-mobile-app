package com.unilorin.vividmotion.pre_cbtapp.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.unilorin.vividmotion.pre_cbtapp.R;
import com.unilorin.vividmotion.pre_cbtapp.fragments.AddCourseFragment;
import com.unilorin.vividmotion.pre_cbtapp.fragments.CourseQuizFragment;
import com.unilorin.vividmotion.pre_cbtapp.fragments.UserProfileFragment;
import com.unilorin.vividmotion.pre_cbtapp.managers.data.CourseDBHelper;
import com.unilorin.vividmotion.pre_cbtapp.managers.data.SharedPreferenceContract;
import com.unilorin.vividmotion.pre_cbtapp.models.Course;
import com.unilorin.vividmotion.pre_cbtapp.models.User;
import com.unilorin.vividmotion.pre_cbtapp.network.services.HTTPCourseService;
import com.unilorin.vividmotion.pre_cbtapp.network.services.HTTPTestService;
import com.unilorin.vividmotion.pre_cbtapp.network.services.HTTPUserAccountService;
import com.unilorin.vividmotion.pre_cbtapp.services.DownloadQuestionService;
import com.unilorin.vividmotion.pre_cbtapp.services.UploadTestResultService;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, CourseQuizFragment.OnCourseQuizSelectedListener {

    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceContract.FILE_NAME, MODE_PRIVATE);
        currentUser = new Gson().fromJson(sharedPreferences.getString(SharedPreferenceContract.USER_ACCOUNT_JSON_STRING, null), User.class);

        setTitle(currentUser.getName());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.userNameTextView)).setText(currentUser.getName());
        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.trendTextView)).setText("On a streak.");


        CourseQuizFragment courseQuizFragment = CourseQuizFragment.newInstance(1);
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        fm.beginTransaction().replace(R.id.fragment_container, courseQuizFragment).commit();

        if (!sharedPreferences.contains(SharedPreferenceContract.SRRVICES_STARTED)){
            tryAndStartServices();

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(SharedPreferenceContract.SRRVICES_STARTED, true);
            editor.apply();
        }

    }

    private void tryAndStartServices(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                long nextScheduledTime = System.currentTimeMillis() + (1000 * 3600 * 24);

                Intent uploadResultIntent = new Intent(DashboardActivity.this, UploadTestResultService.class);
                PendingIntent uploadResultPendingIntent =
                        PendingIntent.getBroadcast(DashboardActivity.this, 0, uploadResultIntent, PendingIntent.FLAG_UPDATE_CURRENT);


                alarmManager.set(AlarmManager.RTC, nextScheduledTime, uploadResultPendingIntent);


                Intent downloadQuestionsIntent = new Intent(DashboardActivity.this, DownloadQuestionService.class);
                PendingIntent downloadQuestionsPendingIntent =
                        PendingIntent.getBroadcast(DashboardActivity.this, 0, downloadQuestionsIntent, PendingIntent.FLAG_UPDATE_CURRENT);


                alarmManager.set(AlarmManager.RTC, nextScheduledTime, downloadQuestionsPendingIntent);
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            return true;
        } else if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_feedback) {
            return true;
        } else if (id == R.id.action_logout) {
            new LogOutTask().execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        navigateMenu(id);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void navigateMenu(int id) {
        if (id == R.id.nav_add_course) {
            startActivity(new Intent(getBaseContext(), AddCourseActivity.class));
        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(getBaseContext(), UserProfileActivity.class));
        } else if (id == R.id.nav_pdf_reader) {

        }
    }

    @Override
    public void onCourseQuizSelected(Course item) {
        Intent takeQuizIntent = new Intent(DashboardActivity.this, TakeQuizActivity.class);
        takeQuizIntent.putExtra("courseForQuiz", new Gson().toJson(item));
        startActivity(takeQuizIntent);
    }

    private class LogOutTask extends AsyncTask<Void, Void, Void> {
        boolean logoutSuccessful;
        ProgressDialog prog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prog = new ProgressDialog(DashboardActivity.this, R.style.alertDialog);
            prog.setMessage("Logging out");
            prog.setCancelable(false);
            prog.setIndeterminate(true);
            prog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            prog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            startService(new Intent(getApplicationContext(), UploadTestResultService.class));
            logoutSuccessful = new HTTPUserAccountService(getApplicationContext()).signOutUser();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            prog.dismiss();
            if (logoutSuccessful) {
                startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
                finish();
            } else {
                Toast.makeText(getBaseContext(), "logout unsuccessful!", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
