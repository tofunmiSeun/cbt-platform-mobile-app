package com.unilorin.vividmotion.pre_cbtapp.network;

/**
 * Created by Eloka Augustine on 24/12/2016.
 */
public interface URLContract {
    String SERVER_URL = "http://192.168.43.104:9000";
    String REGISTER_USER_URL = SERVER_URL + "/api/user/register";
    String LOGIN_USER_URL = SERVER_URL + "/api/user/login";
    String GET_FACULTIES = SERVER_URL + "/api/student-profile/faculties";
    String GET_DEPARTMENTS = SERVER_URL + "/api/student-profile/departments/";
}
