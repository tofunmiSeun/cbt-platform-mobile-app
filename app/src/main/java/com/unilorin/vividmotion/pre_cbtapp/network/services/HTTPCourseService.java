package com.unilorin.vividmotion.pre_cbtapp.network.services;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.unilorin.vividmotion.pre_cbtapp.models.Course;
import com.unilorin.vividmotion.pre_cbtapp.network.URLContract;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

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
            String url = URLContract.SERVER_URL;
            ResponseEntity<CourseResponseObject> responseEntity = restTemplate.getForEntity(url, CourseResponseObject.class);
            return responseEntity.getBody().courses;
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

    public void assignCoursesToUser(List<Course> courses){
        try {

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private class CourseResponseObject {
        private List<Course> courses;
    }
}
