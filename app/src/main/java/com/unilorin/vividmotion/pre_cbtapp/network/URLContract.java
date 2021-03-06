package com.unilorin.vividmotion.pre_cbtapp.network;

/**
 * Created by Eloka Augustine on 24/12/2016.
 */
public interface URLContract {
    String SERVER_URL = "http://35.164.41.247:9000"; //Live server
    //String SERVER_URL = "http://192.168.43.104:9000";  //Test server
    String REGISTER_USER_URL = SERVER_URL + "/api/user/register";
    String LOGIN_USER_URL = SERVER_URL + "/api/user/login";
    String LOGOUT_USER_URL = SERVER_URL + "/api/user/logout";
    String GET_FACULTIES = SERVER_URL + "/api/student-profile/faculties";
    String GET_DEPARTMENTS = SERVER_URL + "/api/student-profile/departments/";
    String GET_ALL_COURSES = SERVER_URL + "/api/courses/all-courses";
    String UPDATE_USER_ASSIGNED_COURSES = SERVER_URL + "/api/courses/update-courses";
    String GET_TEST_QUESTION_FOR_COURSE = SERVER_URL + "/api/questions/";
    String GET_TEST_QUESTIONS = SERVER_URL + "/api/questions";
    String SAVE_TEST_RESULTS = SERVER_URL + "/api/questions/save-results";
}
