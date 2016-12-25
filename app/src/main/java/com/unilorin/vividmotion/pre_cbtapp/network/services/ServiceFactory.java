package com.unilorin.vividmotion.pre_cbtapp.network.services;

import android.content.Context;

/**
 * Created by Eloka Augustine on 24/12/2016.
 */
public class ServiceFactory {
    private static ServiceFactory ourInstance = new ServiceFactory();

    public static ServiceFactory getInstance() {
        return ourInstance;
    }

    private ServiceFactory() {

    }

    public UserAccountService getUserAccountService(Context context) {
        return new HTTPUserAccountService(context);
    }
}
