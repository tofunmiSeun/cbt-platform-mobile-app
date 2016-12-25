package com.unilorin.vividmotion.pre_cbtapp.network.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.unilorin.vividmotion.pre_cbtapp.managers.data.SharedPreferenceContract;
import com.unilorin.vividmotion.pre_cbtapp.models.Department;
import com.unilorin.vividmotion.pre_cbtapp.models.Faculty;
import com.unilorin.vividmotion.pre_cbtapp.models.StudentProfile;
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

    public boolean saveStudentProfileForCurrentUser(StudentProfile studentProfile){
        try {
            SharedPreferences sharedPreferences = appContext.getSharedPreferences(SharedPreferenceContract.FILE_NAME, MODE_PRIVATE);
            User currentUser = new Gson().fromJson(sharedPreferences.getString(SharedPreferenceContract.USER_ACCOUNT_JSON_STRING, null), User.class);

            String url = URLContract.SERVER_URL + "/api/user/" + currentUser.getEmailAddress() + "/enrich-student";
            ResponseEntity responseEntity = restTemplate.postForEntity(url, studentProfile, null);

            if (responseEntity.getStatusCode() == HttpStatus.ACCEPTED){
                currentUser.setStudentProfile(studentProfile);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(SharedPreferenceContract.USER_ACCOUNT_JSON_STRING, new Gson().toJson(currentUser));
                editor.apply();
            }

            return true;
        }
        catch (HttpClientErrorException clientErrorException) {
            Log.e(TAG, clientErrorException.getMessage());
            return false;
        } catch (HttpServerErrorException serverErrorException) {
            Log.e(TAG, serverErrorException.getMessage());
            return false;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return false;
        }
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
