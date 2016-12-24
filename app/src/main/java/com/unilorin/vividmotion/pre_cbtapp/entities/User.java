package com.unilorin.vividmotion.pre_cbtapp.entities;

/**
 * Created by Tofunmi on 21/12/2016.
 */

public class User {
    private Long id;
    private String name;
    private String emailAddress;
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
