package com.unilorin.vividmotion.pre_cbtapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
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
import com.unilorin.vividmotion.pre_cbtapp.network.services.HTTPUserAccountService;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AddCourseFragment.OnCourseSelectedListener,
        CourseQuizFragment.OnCourseQuizSelectedListener {

    private AddCourseFragment addCourseFragment;
    private CourseQuizFragment courseQuizFragment;
    private UserProfileFragment userProfileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigateMenu(R.id.nav_take_quiz);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
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
        }
        else if(id == R.id.action_settings){
            return true;
        }
        else if(id == R.id.action_feedback){
            return true;
        }
        else if(id == R.id.action_logout){
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

    public void navigateMenu(int id){
        if (id == R.id.nav_take_quiz) {
            setTitle("Take Test");
            courseQuizFragment = CourseQuizFragment.newInstance(1);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, courseQuizFragment)
                    .addToBackStack(null)
                    .commit();
        }
        else if (id == R.id.nav_add_course) {
            setTitle("Add Course");
            addCourseFragment = AddCourseFragment.newInstance(1);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, addCourseFragment)
                    .addToBackStack(null)
                    .commit();
        }
        else if (id == R.id.nav_profile) {
            setTitle("My Profile");
            userProfileFragment = new UserProfileFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, userProfileFragment)
                    .addToBackStack(null)
                    .commit();
        }
        else if (id == R.id.nav_pdf_reader) {

        }
    }

    @Override
    public void onCourseQuizSelected(Course item) {
        Intent takeQuizIntent = new Intent(DashboardActivity.this, TakeQuizActivity.class);
        takeQuizIntent.putExtra("courseForQuiz", new Gson().toJson(item));
        startActivity(takeQuizIntent);
    }

    @Override
    public void onCourseSelected(final Course item) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HTTPCourseService courseService = new HTTPCourseService(getApplicationContext());
                boolean result = courseService.assignCourseToUser(item);
                if (result) {
                    addCourseFragment.onStart();
                }
            }
        }).start();
    }

    private class LogOutTask extends AsyncTask<Void, Void, Void>{
        boolean logoutSuccessful;
        ProgressDialog prog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prog = new ProgressDialog(DashboardActivity.this);
            prog.setMessage("Logging out");
            prog.setCancelable(false);
            prog.setIndeterminate(true);
            prog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            prog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            logoutSuccessful = new HTTPUserAccountService(getApplicationContext()).signOutUser();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            prog.dismiss();
            if (logoutSuccessful){
                Toast.makeText(getBaseContext(), "logout successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
            }else{
                Toast.makeText(getBaseContext(), "logout unsuccessful!", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
