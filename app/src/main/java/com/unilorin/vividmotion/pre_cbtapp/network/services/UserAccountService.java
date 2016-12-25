package com.unilorin.vividmotion.pre_cbtapp.network.services;

import com.unilorin.vividmotion.pre_cbtapp.models.LoginResponseStatus;
import com.unilorin.vividmotion.pre_cbtapp.models.SignUpResponseStatus;
import com.unilorin.vividmotion.pre_cbtapp.models.User;

/**
 * Created by Eloka Augustine on 24/12/2016.
 */
public interface UserAccountService {
    SignUpResponseStatus registerNewUser(User user);
    LoginResponseStatus loginUser(String emailAddress, String password);
}
