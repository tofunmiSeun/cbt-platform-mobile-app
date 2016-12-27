package com.unilorin.vividmotion.pre_cbtapp.network.services;

import android.content.Context;
import android.util.Log;

import com.unilorin.vividmotion.pre_cbtapp.models.Course;
import com.unilorin.vividmotion.pre_cbtapp.models.Question;
import com.unilorin.vividmotion.pre_cbtapp.network.URLContract;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tofunmi on 27/12/2016.
 */

public class HTTPTestService{
    private static final String TAG = "HTTPCourseService";
    private RestTemplate restTemplate;
    private Context appContext;

    public List<Question> getTestQuestionsFor(Course course){
        try{
            String url = URLContract.GET_TEST_QUESTION_FOR_COURSE + course.getId();
            return restTemplate.getForObject(url, QuestionResponseObject.class).questions;
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

    private class QuestionResponseObject{
        private List<Question> questions;
    }
}
