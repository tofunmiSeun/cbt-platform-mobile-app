package com.unilorin.vividmotion.pre_cbtapp.network.services;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.unilorin.vividmotion.pre_cbtapp.models.Department;
import com.unilorin.vividmotion.pre_cbtapp.models.Faculty;
import com.unilorin.vividmotion.pre_cbtapp.network.URLContract;

import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tofunmi on 25/12/2016.
 */

public class HTTPStudentProfileService {

    private static final String TAG = "StudentProfileService";
    private RestTemplate restTemplate;
    private Context appContext;

    public HTTPStudentProfileService(Context c){
        restTemplate = new RestTemplate();
        Gson gsonObject = new Gson();
        restTemplate.getMessageConverters().add(new GsonHttpMessageConverter(gsonObject));

        appContext = c;
    }

    public List<Faculty> getAllFaculties(){
        try {
            FacultyResponseObject facultyResponseObject = restTemplate.getForObject(URLContract.GET_FACULTIES, FacultyResponseObject.class);
            return facultyResponseObject.faculties;
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

    public List<Department> getDepartmentForFaculty(Long facultyId){
        try {
            DepartmentResponseObject departmentResponseObject = restTemplate.getForObject(URLContract.GET_DEPARTMENTS + facultyId, DepartmentResponseObject.class);
            return departmentResponseObject.departments;
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

    private class FacultyResponseObject {
        private List<Faculty> faculties;
    }

    private class DepartmentResponseObject{
        private List<Department> departments;
    }
}
