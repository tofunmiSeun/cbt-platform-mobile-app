package com.unilorin.vividmotion.pre_cbtapp.network.services;

import com.unilorin.vividmotion.pre_cbtapp.models.LoginResponseStatus;
import com.unilorin.vividmotion.pre_cbtapp.models.SignUpResponseStatus;
import com.unilorin.vividmotion.pre_cbtapp.models.User;

/**
 * Created by Eloka Augustine on 24/12/2016.
 */
public class MockUserAccountService implements UserAccountService {

    @Override
    public SignUpResponseStatus registerNewUser(User user) {
        return null;
    }

    @Override
    public LoginResponseStatus loginUser(String emailAddress, String password) {
        return null;
    }
}
