package com.unilorin.vividmotion.pre_cbtapp.network.services;

/**
 * Created by Eloka Augustine on 24/12/2016.
 */
public class ServiceFactory {
    private UserAccountService userAccountService;
    private static ServiceFactory ourInstance = new ServiceFactory();

    public static ServiceFactory getInstance() {
        return ourInstance;
    }

    private ServiceFactory() {
        userAccountService = new MockUserAccountService();
    }

    public UserAccountService getUserAccountService() {
        return userAccountService;
    }
}
