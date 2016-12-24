package com.unilorin.vividmotion.pre_cbtapp.services;

import android.util.Log;

import com.google.gson.Gson;
import com.unilorin.vividmotion.pre_cbtapp.entities.LoginResponseStatus;
import com.unilorin.vividmotion.pre_cbtapp.entities.SignUpResponseStatus;
import com.unilorin.vividmotion.pre_cbtapp.entities.User;
import com.unilorin.vividmotion.pre_cbtapp.entities.UserLoginResponseObject;
import com.unilorin.vividmotion.pre_cbtapp.entities.UserSignUpResponseObject;

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

public class UserAccountService {

    private static final String TAG = "UserAccountService";

    private final static String URL = "http://192.168.43.104:9000";
    private RestTemplate restTemplate;

    public UserAccountService(){
        restTemplate = new RestTemplate();
        Gson gsonObject = new Gson();
        restTemplate.getMessageConverters().add(new GsonHttpMessageConverter(gsonObject));
    }

    public SignUpResponseStatus registerNewUser(User user){
        try {
            ResponseEntity<UserSignUpResponseObject> responseEntity = restTemplate.postForEntity(URL + "/api/user/register", user, UserSignUpResponseObject.class);

            UserSignUpResponseObject responseObject = responseEntity.getBody();
            switch (responseObject.getStatus()){
                case UserSignUpResponseObject.ACCEPTED:
                    // TODO: do something appropriate
                    return SignUpResponseStatus.ACCEPTED;
                case UserSignUpResponseObject.EMAIL_ALREADY_IN_USE:
                    // TODO: do something appropriate
                    return SignUpResponseStatus.EMAIL_ALREADY_IN_USE;
                default:
                    return SignUpResponseStatus.UNKNOWN_ERROR;
            }

        }catch (HttpClientErrorException clientErrorException) {
            Log.e(TAG, clientErrorException.getMessage());
            return SignUpResponseStatus.UNKNOWN_ERROR;
        }
        catch (HttpServerErrorException serverErrorException) {
            Log.e(TAG, serverErrorException.getMessage());
            return SignUpResponseStatus.UNKNOWN_ERROR;
        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return SignUpResponseStatus.UNKNOWN_ERROR;
        }
    }

    public LoginResponseStatus loginUser(String emailAddress, String password){
        try {
            Map<String, String> loginCredentials = new HashMap<>();
            loginCredentials.put("emailAddress", emailAddress);
            loginCredentials.put("password", password);

            ResponseEntity<UserLoginResponseObject> responseEntity = restTemplate.postForEntity(URL + "/api/user/login", loginCredentials, UserLoginResponseObject.class);

            UserLoginResponseObject userLoginResponseObject = responseEntity.getBody();
            switch (userLoginResponseObject.getStatus()){
                case UserLoginResponseObject.ACCEPTED:
                    //TODO: do appropriate stuff
                    User user = userLoginResponseObject.getUser();
                    return LoginResponseStatus.ACCEPTED;

                case UserLoginResponseObject.INCORRECT_PASSWORD:
                    return LoginResponseStatus.INCORRECT_PASSWORD;

                case UserLoginResponseObject.NO_ACCOUNT_FOR_THIS_EMAIL:
                    return LoginResponseStatus.NO_ACCOUNT_FOR_THIS_EMAIL;
                default:
                    return LoginResponseStatus.UNKNOWN_ERROR;
            }

        }catch (HttpClientErrorException clientErrorException) {
            Log.e(TAG, clientErrorException.getMessage());
            return LoginResponseStatus.UNKNOWN_ERROR;
        }
        catch (HttpServerErrorException serverErrorException) {
            Log.e(TAG, serverErrorException.getMessage());
            return LoginResponseStatus.UNKNOWN_ERROR;
        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return LoginResponseStatus.UNKNOWN_ERROR;
        }
    }

}
