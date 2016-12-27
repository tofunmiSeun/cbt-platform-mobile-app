package com.unilorin.vividmotion.pre_cbtapp.models;

import java.util.List;

/**
 * Created by Tofunmi on 27/12/2016.
 */

public class UpdateUserAssignedCoursesRequestObject {
    public UpdateUserAssignedCoursesRequestObject(){}
    public String emailAddress;
    public List<Course> coursesToUpdate;
}
