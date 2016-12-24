package com.unilorin.vividmotion.pre_cbtapp.entities;

/**
 * Created by Tofunmi on 24/12/2016.
 */

public class UserSignUpResponseObject {
    public static final int ACCEPTED = 0;
    public static final int EMAIL_ALREADY_IN_USE = 1;

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
