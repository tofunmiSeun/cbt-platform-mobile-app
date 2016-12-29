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
            //return restTemplate.getForObject(url, QuestionResponseObject.class).questions;
            return getMockQuestions();
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

    private List<Question> getMockQuestions(){
        List<Question> qList = new ArrayList<>();

        Long [] ids = new Long[]{1L, 2L, 3L, 4L, 5L};
        int [] correctIndices = new int []{0, 1, 0, 2, 2};
        String [] quests = new String[]{
                "What is the chemical formula of Iron?",
                "On a PH scale, which number represents neutral PH?",
                "'Matter cannot be created nor destroyed' is which law?",
                "Which of these people wasn't a scientist?",
                "The by-product of neutralisation is?"
        };
        String [] [] options = new String[][]{
                {"Fe", "Ir", "Ag", "He"},
                {"14", "7", "0", "1"},
                {"Law of Conservation of mass", "Faraday's 2nd Law of Electrolysis", "Law of Combining Volume",
                        "Darwin's Law of natural selection"},
                {"Gay Lusacs", "Micheal Faraday", "Robert Schumann", "Ada Lovelace"},
                {"Acid and Base", "Soap and Water", "Salt and Water", "Power and Energy"}
        };
        for (int i = 0; i < ids.length; i++){
            Question q = new Question();
            q.setId(ids[i]);
            q.setCorrectAnswerIndex(correctIndices[i]);
            q.setQuestion(quests[i]);
            q.setOptions(options[i]);
            q.setCourseId(6L);

            qList.add(q);
        }

        return qList;
    }

    private class QuestionResponseObject{
        private List<Question> questions;
    }
}
