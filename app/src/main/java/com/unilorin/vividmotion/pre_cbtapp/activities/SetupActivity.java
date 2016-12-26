package com.unilorin.vividmotion.pre_cbtapp.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.unilorin.vividmotion.pre_cbtapp.R;
import com.unilorin.vividmotion.pre_cbtapp.models.Department;
import com.unilorin.vividmotion.pre_cbtapp.models.Faculty;
import com.unilorin.vividmotion.pre_cbtapp.models.StudentProfile;
import com.unilorin.vividmotion.pre_cbtapp.network.services.HTTPStudentProfileService;

import java.util.List;

public class SetupActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int TASK_GET_FACULTIES = 0;
    private static final int TASK_GET_DEPARTMENTS = 1;

    private AppCompatSpinner facultySpinner;
    private AppCompatSpinner departmentSpinner;
    private AppCompatSpinner levelSpinner;
    private Button proceedButton;

    HTTPStudentProfileService studentProfileService;

    private Faculty selectedFaculty;
    private Department selectedDepartment;
    private int selectedLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        studentProfileService = new HTTPStudentProfileService(getApplicationContext());
        instantiateViewObjects();
    }

    void instantiateViewObjects(){
        facultySpinner = (AppCompatSpinner) findViewById(R.id.facultySpinner);
        departmentSpinner = (AppCompatSpinner) findViewById(R.id.departmentSpinner);
        levelSpinner = (AppCompatSpinner) findViewById(R.id.levelSpinner);
        proceedButton = (Button) findViewById(R.id.continueButton);

        proceedButton.setOnClickListener(this);
    }

    @Override
    protected void onStart(){
        super.onStart();
        new SetUpStudentProfileTask(TASK_GET_FACULTIES).execute();
    }

    private void setUpFacultySelectBox(final List<Faculty> faculties){
        String [] facultyNames = new String [faculties.size()];
        for (int i = 0; i < facultyNames.length; i++){
            facultyNames[i] = faculties.get(i).getName();
        }

        ArrayAdapter<String> facultyArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, facultyNames);
        facultySpinner.setAdapter(facultyArrayAdapter);

        facultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedFaculty = faculties.get(position);
                new SetUpStudentProfileTask(TASK_GET_DEPARTMENTS).execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getBaseContext(), "Select a faculty", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setUpDepartmentSelectBox(final List<Department> departments){
        String [] departmentNames = new String [departments.size()];
        for (int i = 0; i < departmentNames.length; i++){
            departmentNames[i] = departments.get(i).getName();
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, departmentNames);
        departmentSpinner.setAdapter(arrayAdapter);

        departmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDepartment = departments.get(position);
                setUpLevelSelectBox();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getBaseContext(), "Select a department", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpLevelSelectBox(){
        String [] levels = new String[selectedDepartment.getCourseDurationInYears()];
        for (int i = 0; i < levels.length; i++){
            levels[i] = "" + ((i + 1) * 100);
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, levels);
        levelSpinner.setAdapter(arrayAdapter);

        levelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedLevel = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getBaseContext(), "Select a level", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == proceedButton){
            StudentProfile studentProfile = new StudentProfile();
            studentProfile.setFaculty(selectedFaculty);
            studentProfile.setDepartment(selectedDepartment);
            studentProfile.setLevel(selectedLevel);

            final StudentProfile profileToSave = studentProfile;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean result = studentProfileService.saveStudentProfileForCurrentUser(profileToSave);
                    if (result){
                        startActivity(new Intent(SetupActivity.this, DashboardActivity.class));
                    }else{
                        //TODO: issue 'an error occurred' message
                    }
                }
            }).start();
        }
    }

    private class SetUpStudentProfileTask extends AsyncTask<Void, Void, Void>{
        private int taskTag;
        List<Faculty> faculties;
        List<Department> departments;

        private SetUpStudentProfileTask(Integer tag){
            this.taskTag = tag;
        }

        @Override
        protected Void doInBackground(Void... params) {
            switch (taskTag){
                case TASK_GET_FACULTIES:
                    faculties = studentProfileService.getAllFaculties();
                    break;
                case TASK_GET_DEPARTMENTS:
                    departments = studentProfileService.getDepartmentForFaculty(selectedFaculty.getId());
                    break;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            switch (taskTag){
                case TASK_GET_FACULTIES:
                    setUpFacultySelectBox(faculties);
                    break;
                case TASK_GET_DEPARTMENTS:
                    setUpDepartmentSelectBox(departments);
                    break;
            }
        }
    }
}
