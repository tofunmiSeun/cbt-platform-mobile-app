package com.unilorin.vividmotion.pre_cbtapp.models;

/**
 * Created by Tofunmi on 24/12/2016.
 */

public class UserLoginResponseObject {
    public static final int ACCEPTED = 0;
    public static final int INVALID_CREDENTIALS = 1;
    public static final int NO_ACCOUNT_FOR_THIS_EMAIL = 2;
    public static final int INCORRECT_PASSWORD = 3;

    private int status;
    private User user;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
