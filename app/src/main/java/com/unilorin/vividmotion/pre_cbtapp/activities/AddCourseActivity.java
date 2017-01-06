package com.unilorin.vividmotion.pre_cbtapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, addCourseFragment)
                .addToBackStack(null)
                .commit();
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
}
