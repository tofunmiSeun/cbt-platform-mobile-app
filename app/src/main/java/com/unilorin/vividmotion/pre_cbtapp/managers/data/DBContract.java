package com.unilorin.vividmotion.pre_cbtapp.managers.data;

import android.provider.BaseColumns;

/**
 * Created by Tofunmi on 24/12/2016.
 */

public class DBContract {
    public interface CoursesEntry extends BaseColumns{
        String tableName = "assignedCourses";
        String courseTitleColumn = "courseTitleColumn";
        String courseCodeColumn = "courseCodeColumn";
        String departmentIdColumn = "departmentIdColumn";
        String levelValueColumn = "levelValueColumn";
    }
}
