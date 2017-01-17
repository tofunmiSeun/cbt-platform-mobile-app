package com.unilorin.vividmotion.pre_cbtapp.activities;

import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.unilorin.vividmotion.pre_cbtapp.R;
import com.unilorin.vividmotion.pre_cbtapp.fragments.AddCourseFragment;
import com.unilorin.vividmotion.pre_cbtapp.models.Course;
import com.unilorin.vividmotion.pre_cbtapp.network.services.HTTPCourseService;

public class AddCourseActivity extends AppCompatActivity implements AddCourseFragment.OnCourseSelectedListener {

    private AddCourseFragment addCourseFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blank_content);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Add Course");

        addCourseFragment = AddCourseFragment.newInstance(1);
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        fm.beginTransaction().replace(R.id.content, addCourseFragment).commit();
    }


    @Override
    public void onCourseSelected(final Course item) {
        new AddCourseTask().execute(item);
    }

    private class AddCourseTask extends AsyncTask<Course, Void, Boolean>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getBaseContext(), "Adding course..",Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Boolean doInBackground(Course... courses) {
            HTTPCourseService courseService = new HTTPCourseService(getApplicationContext());
            return courseService.assignCourseToUser(courses[0]);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result){
                Toast.makeText(getBaseContext(), "Successful!",Toast.LENGTH_SHORT).show();
                addCourseFragment.onStart();
            }else{
                Toast.makeText(getBaseContext(), "Unable to add course, please try again.",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
