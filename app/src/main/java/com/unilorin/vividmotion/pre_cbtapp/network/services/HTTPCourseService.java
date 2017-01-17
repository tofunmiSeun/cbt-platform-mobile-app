package com.unilorin.vividmotion.pre_cbtapp.network.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.unilorin.vividmotion.pre_cbtapp.managers.data.CourseDBHelper;
import com.unilorin.vividmotion.pre_cbtapp.managers.data.QuestionDBHelper;
import com.unilorin.vividmotion.pre_cbtapp.managers.data.SharedPreferenceContract;
import com.unilorin.vividmotion.pre_cbtapp.models.Course;
import com.unilorin.vividmotion.pre_cbtapp.models.Question;
import com.unilorin.vividmotion.pre_cbtapp.models.UpdateUserAssignedCoursesRequestObject;
import com.unilorin.vividmotion.pre_cbtapp.models.User;
import com.unilorin.vividmotion.pre_cbtapp.network.URLContract;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Tofunmi on 26/12/2016.
 */

public class HTTPCourseService {
    private static final String TAG = "HTTPCourseService";
    private RestTemplate restTemplate;
    private Context appContext;

    public HTTPCourseService(Context c){
        restTemplate = new RestTemplate();
        Gson gsonObject = new Gson();
        restTemplate.getMessageConverters().add(new GsonHttpMessageConverter(gsonObject));

        appContext = c;
    }

    public List<Course> getAvailableCourses(){
        try {
            CourseResponseObject responseObject = restTemplate.getForObject(URLContract.GET_ALL_COURSES, CourseResponseObject.class);
            CourseDBHelper courseDBHelper = new CourseDBHelper(appContext);
            List<String> courseCodes = courseDBHelper.getCodesForAssignedCourses();

            List<Course> filteredCourses = new ArrayList<>();
            for(Course c  : responseObject.courses){
                if (!courseCodes.contains(c.getCourseCode())){
                    filteredCourses.add(c);
                }
            }

            return filteredCourses;
        }
        catch (HttpClientErrorException clientErrorException) {
            Log.e(TAG, clientErrorException.getMessage());
            return new ArrayList<>();
        } catch (HttpServerErrorException serverErrorException) {
            Log.e(TAG, serverErrorException.getMessage());
            return new ArrayList<>();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean assignCourseToUser(Course course){
        List<Course> courseList = new ArrayList<>();
        courseList.add(course);
        return assignCoursesToUser(courseList);
    }

    private boolean assignCoursesToUser(List<Course> courses){
        try {
            SharedPreferences sharedPreferences = appContext.getSharedPreferences(SharedPreferenceContract.FILE_NAME, MODE_PRIVATE);
            User currentUser = new Gson().fromJson(sharedPreferences.getString(SharedPreferenceContract.USER_ACCOUNT_JSON_STRING, null), User.class);

            UpdateUserAssignedCoursesRequestObject requestObject = new UpdateUserAssignedCoursesRequestObject();
            requestObject.emailAddress = currentUser.getEmailAddress();
            requestObject.coursesToUpdate = courses;

            ResponseEntity<UpdateCoursesResponseObject> responseEntity = restTemplate.postForEntity(URLContract.UPDATE_USER_ASSIGNED_COURSES,
                    requestObject, UpdateCoursesResponseObject.class );

            if (responseEntity.getStatusCode() == HttpStatus.OK){
                CourseDBHelper courseDBHelper = new CourseDBHelper(appContext);
                for (Course c : courses) {
                    courseDBHelper.registerNewCourse(c);
                }

                QuestionDBHelper questionDBHelper = new QuestionDBHelper(appContext);
                questionDBHelper.saveQuestions(responseEntity.getBody().questions);
            }
            return true;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return false;
        }
    }

    private class CourseResponseObject {
        private List<Course> courses;
    }

    private class UpdateCoursesResponseObject{
        private List<Question> questions;
    }
}
