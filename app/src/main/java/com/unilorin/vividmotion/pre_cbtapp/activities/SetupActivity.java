package com.unilorin.vividmotion.pre_cbtapp.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.unilorin.vividmotion.pre_cbtapp.R;
import com.unilorin.vividmotion.pre_cbtapp.models.Department;
import com.unilorin.vividmotion.pre_cbtapp.models.Faculty;
import com.unilorin.vividmotion.pre_cbtapp.network.services.HTTPStudentProfileService;

import java.util.List;

public class SetupActivity extends AppCompatActivity {

    private static final int TASK_GET_FACULTIES = 0;
    private static final int TASK_GET_DEPARTMENTS = 1;

    private Faculty selectedFaculty;
    private Department selectedDepartment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
    }

    @Override
    protected void onStart(){
        super.onStart();
        new SetUpStudentProfileTask(TASK_GET_FACULTIES).execute();
    }

    private void setUpFacultySelectBox(List<Faculty> faculties){
        String [] facultyNames = new String [faculties.size()];
        for (int i = 0; i < facultyNames.length; i++){
            facultyNames[i] = faculties.get(i).getName();
        }

        selectedFaculty = faculties.get(0);
        new SetUpStudentProfileTask(TASK_GET_DEPARTMENTS).execute();
    }
    private void setUpDepartmentSelectBox(List<Department> departments){
        String [] departmentNames = new String [departments.size()];
        for (int i = 0; i < departmentNames.length; i++){
            departmentNames[i] = departments.get(i).getName();
        }


    }

    private void setUpLevelSelectBox(){
        String [] levels = new String[selectedDepartment.getCourseDurationInYears()];
        for (int i = 0; i < levels.length; i++){
            levels[i] = "" + ((i + 1) * 100);
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
            HTTPStudentProfileService studentProfileService = new HTTPStudentProfileService(getApplicationContext());
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
