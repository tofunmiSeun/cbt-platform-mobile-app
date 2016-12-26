package com.unilorin.vividmotion.pre_cbtapp.database;

import android.provider.BaseColumns;

/**
 * Created by Tofunmi on 24/12/2016.
 */

public class DBContract {
    public static class CoursesEntry implements BaseColumns{
        public static final String tableName = "assignedCourses";
        public static final String courseTitleColumn = "courseTitleColumn";
        public static final String courseCodeColumn = "courseCodeColumn";
        public static final String departmentIdColumn = "departmentIdColumn";
        public static final String levelValueColumn = "levelValueColumn";
    }
}
