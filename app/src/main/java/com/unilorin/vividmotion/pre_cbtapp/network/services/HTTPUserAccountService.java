package com.unilorin.vividmotion.pre_cbtapp.network.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.unilorin.vividmotion.pre_cbtapp.managers.data.CourseDBHelper;
import com.unilorin.vividmotion.pre_cbtapp.managers.data.QuestionDBHelper;
import com.unilorin.vividmotion.pre_cbtapp.managers.data.SharedPreferenceContract;
import com.unilorin.vividmotion.pre_cbtapp.managers.data.TestResultDBHelper;
import com.unilorin.vividmotion.pre_cbtapp.models.Course;
import com.unilorin.vividmotion.pre_cbtapp.models.LoginResponseStatus;
import com.unilorin.vividmotion.pre_cbtapp.models.Question;
import com.unilorin.vividmotion.pre_cbtapp.models.SignOutUserRequestObject;
import com.unilorin.vividmotion.pre_cbtapp.models.SignUpResponseStatus;
import com.unilorin.vividmotion.pre_cbtapp.models.User;
import com.unilorin.vividmotion.pre_cbtapp.network.URLContract;
import com.unilorin.vividmotion.pre_cbtapp.utils.AttemptedQuestionsUtils;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Tofunmi on 20/12/2016.
 */

public class HTTPUserAccountService implements UserAccountService {

    private static final String TAG = "UserAccountService";
    private RestTemplate restTemplate;
    private Context appContext;

    public HTTPUserAccountService(Context context) {
        appContext = context;
        restTemplate = new RestTemplate();
        Gson gsonObject = new Gson();
        restTemplate.getMessageConverters().add(new GsonHttpMessageConverter(gsonObject));
    }

    @Override
    public SignUpResponseStatus registerNewUser(User user) {
        try {
            ResponseEntity<UserSignUpResponseObject> responseEntity = restTemplate.postForEntity(URLContract.REGISTER_USER_URL, user, UserSignUpResponseObject.class);

            UserSignUpResponseObject responseObject = responseEntity.getBody();
            switch (responseObject.status) {
                case UserSignUpResponseObject.ACCEPTED:

                    SharedPreferences sharedPreferences = appContext.getSharedPreferences(SharedPreferenceContract.FILE_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(SharedPreferenceContract.USER_ACCOUNT_JSON_STRING, new Gson().toJson(responseObject.user));
                    editor.apply();

                    return SignUpResponseStatus.ACCEPTED;

                case UserSignUpResponseObject.EMAIL_ALREADY_IN_USE:
                    return SignUpResponseStatus.EMAIL_ALREADY_IN_USE;

                default:
                    return SignUpResponseStatus.UNKNOWN_ERROR;
            }

        } catch (HttpClientErrorException clientErrorException) {
            Log.e(TAG, clientErrorException.getMessage());
            return SignUpResponseStatus.UNKNOWN_ERROR;
        } catch (HttpServerErrorException serverErrorException) {
            Log.e(TAG, serverErrorException.getMessage());
            return SignUpResponseStatus.UNKNOWN_ERROR;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return SignUpResponseStatus.UNKNOWN_ERROR;
        }
    }

    @Override
    public LoginResponseStatus loginUser(String emailAddress, String password) {
        try {
            Map<String, String> loginCredentials = new HashMap<>();
            loginCredentials.put("emailAddress", emailAddress);
            loginCredentials.put("password", password);

            ResponseEntity<UserLoginResponseObject> responseEntity = restTemplate.postForEntity(URLContract.LOGIN_USER_URL, loginCredentials, UserLoginResponseObject.class);

            UserLoginResponseObject userLoginResponseObject = responseEntity.getBody();
            switch (userLoginResponseObject.status) {
                case UserLoginResponseObject.ACCEPTED:

                    SharedPreferences sharedPreferences = appContext.getSharedPreferences(SharedPreferenceContract.FILE_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(SharedPreferenceContract.USER_ACCOUNT_JSON_STRING, new Gson().toJson(userLoginResponseObject.user));
                    editor.apply();

                    if (userLoginResponseObject.registeredCourses != null){
                        CourseDBHelper courseDBHelper = new CourseDBHelper(appContext);
                        courseDBHelper.registerNewCourses(userLoginResponseObject.registeredCourses);
                    }

                    if (userLoginResponseObject.questions != null) {
                        QuestionDBHelper questionDBHelper = new QuestionDBHelper(appContext);
                        questionDBHelper.saveQuestions(userLoginResponseObject.questions);
                    }

                    return LoginResponseStatus.ACCEPTED;

                case UserLoginResponseObject.INCORRECT_PASSWORD:
                    return LoginResponseStatus.INCORRECT_PASSWORD;

                case UserLoginResponseObject.NO_ACCOUNT_FOR_THIS_EMAIL:
                    return LoginResponseStatus.NO_ACCOUNT_FOR_THIS_EMAIL;
                default:
                    return LoginResponseStatus.UNKNOWN_ERROR;
            }

        } catch (HttpClientErrorException clientErrorException) {
            Log.e(TAG, clientErrorException.getMessage());
            return LoginResponseStatus.UNKNOWN_ERROR;
        } catch (HttpServerErrorException serverErrorException) {
            Log.e(TAG, serverErrorException.getMessage());
            return LoginResponseStatus.UNKNOWN_ERROR;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return LoginResponseStatus.UNKNOWN_ERROR;
        }
    }

    @Override
    public boolean signOutUser() {
        CourseDBHelper courseDBHelper = new CourseDBHelper(appContext);
        QuestionDBHelper questionDBHelper = new QuestionDBHelper(appContext);
        TestResultDBHelper testResultDBHelper = new TestResultDBHelper(appContext);
        AttemptedQuestionsUtils attemptedQuestionsUtils = new AttemptedQuestionsUtils(appContext);

        SharedPreferences sharedPreferences = appContext.getSharedPreferences(SharedPreferenceContract.FILE_NAME, MODE_PRIVATE);
        String userJson = sharedPreferences.getString(SharedPreferenceContract.USER_ACCOUNT_JSON_STRING, null);
        User currentUser = new Gson().fromJson(userJson, User.class);

        SignOutUserRequestObject requestObject = new SignOutUserRequestObject();
        requestObject.emailAddress = currentUser.getEmailAddress();
        requestObject.testResultsPendingUpload = testResultDBHelper.getTestResultsPendingUpload();
        requestObject.attemptedQuestionsIds = attemptedQuestionsUtils.getIdsOfAttemptedQuestions();

        try {
            restTemplate.postForEntity(URLContract.LOGOUT_USER_URL, requestObject, null);
        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        // Delete test results and course and questions and attempted questions list
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(SharedPreferenceContract.USER_ACCOUNT_JSON_STRING);
        editor.remove(SharedPreferenceContract.PROFILE_PICTURE_STRING);
        editor.apply();

        courseDBHelper.emptyDatabase();
        questionDBHelper.emptyDatabase();
        testResultDBHelper.emptyDatabase();
        attemptedQuestionsUtils.deleteAttemptedQuestionsList();

        new AttemptedQuestionsUtils(appContext).deleteAttemptedQuestionsList();
        return true;
    }

    private class UserLoginResponseObject {
        private static final int ACCEPTED = 0;
        private static final int INVALID_CREDENTIALS = 1;
        private static final int NO_ACCOUNT_FOR_THIS_EMAIL = 2;
        private static final int INCORRECT_PASSWORD = 3;

        private int status;
        private User user;
        private List<Course> registeredCourses;
        private List<Question> questions;
    }

    private class UserSignUpResponseObject {
        private static final int ACCEPTED = 0;
        private static final int EMAIL_ALREADY_IN_USE = 1;

        private int status;
        private User user;
    }

}
