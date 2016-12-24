package com.unilorin.vividmotion.pre_cbtapp.services;

import android.util.Log;

import com.google.gson.Gson;
import com.unilorin.vividmotion.pre_cbtapp.entities.User;
import com.unilorin.vividmotion.pre_cbtapp.entities.UserLoginResponseObject;

import org.springframework.http.HttpStatus;
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

    public boolean registerNewUser(User user){
        try {
            ResponseEntity<User> responseEntity = restTemplate.postForEntity(URL + "/api/user/register", user, User.class);
            return responseEntity.getStatusCode() == HttpStatus.ACCEPTED;

        }catch (HttpClientErrorException clientErrorException) {
            Log.e(TAG, clientErrorException.getMessage());
            return false;
        }
        catch (HttpServerErrorException serverErrorException) {
            Log.e(TAG, serverErrorException.getMessage());
            return false;
        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return false;
        }
    }

    public UserLoginResponseObject loginUser(String emailAddress, String password){
        try {
            Map<String, String> loginCredentials = new HashMap<>();
            loginCredentials.put("emailAddress", emailAddress);
            loginCredentials.put("password", password);

            ResponseEntity<UserLoginResponseObject> responseEntity = restTemplate.postForEntity(URL + "/api/user/login", loginCredentials, UserLoginResponseObject.class);
            return responseEntity.getBody();

        }catch (HttpClientErrorException clientErrorException) {
            Log.e(TAG, clientErrorException.getMessage());
            return null;
        }
        catch (HttpServerErrorException serverErrorException) {
            Log.e(TAG, serverErrorException.getMessage());
            return null;
        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }
}
