package com.unilorin.vividmotion.pre_cbtapp.database;

import android.provider.BaseColumns;

/**
 * Created by Tofunmi on 24/12/2016.
 */

public class DBContract {
    public static class UserContract implements BaseColumns{
        public static final String tableName  = "userTable";
        public static final String userObjectColumn  = "userObjectColumn";
    }
}
