package com.unilorin.vividmotion.pre_cbtapp.network.services;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.unilorin.vividmotion.pre_cbtapp.database.UserAccountDBHelper;
import com.unilorin.vividmotion.pre_cbtapp.models.LoginResponseStatus;
import com.unilorin.vividmotion.pre_cbtapp.models.SignUpResponseStatus;
import com.unilorin.vividmotion.pre_cbtapp.models.User;
import com.unilorin.vividmotion.pre_cbtapp.models.UserLoginResponseObject;
import com.unilorin.vividmotion.pre_cbtapp.models.UserSignUpResponseObject;
import com.unilorin.vividmotion.pre_cbtapp.network.URLContract;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

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
            switch (responseObject.getStatus()) {
                case UserSignUpResponseObject.ACCEPTED:
                    UserAccountDBHelper userAccountDBHelper = new UserAccountDBHelper(appContext);
                    userAccountDBHelper.insertUser(responseObject.getUser());
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
            switch (userLoginResponseObject.getStatus()) {
                case UserLoginResponseObject.ACCEPTED:
                    UserAccountDBHelper userAccountDBHelper = new UserAccountDBHelper(appContext);
                    userAccountDBHelper.insertUser(userLoginResponseObject.getUser());
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

}
