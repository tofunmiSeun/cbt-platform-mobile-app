package com.unilorin.vividmotion.pre_cbtapp.managers.data;

import android.provider.BaseColumns;

/**
 * Created by Tofunmi on 24/12/2016.
 */

public class DBContract {
    public interface CoursesEntry extends BaseColumns{
        String tableName = "assignedCourses";
        String idColumn = "idColumn";
        String courseTitleColumn = "courseTitleColumn";
        String courseCodeColumn = "courseCodeColumn";
        String departmentIdColumn = "departmentIdColumn";
        String levelValueColumn = "levelValueColumn";
    }
    public interface QuestionEntry extends BaseColumns{
        String tableName = "question";
        String idColumn = "idColumn";
        String questionColumn = "questionColumn";
        String optionsColumn = "optionsColumn";
        String correctAnswerIndexColumn = "correctAnswerIndexColumn";
        String courseIdColumn = "courseIdColumn";
    }
}
