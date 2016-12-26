package com.unilorin.vividmotion.pre_cbtapp.models;

/**
 * Created by Tofunmi on 24/12/2016.
 */

public class StudentProfile {
    private Faculty faculty;
    private Department department;
    private Integer numericalValueOfStudentLevel;

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Integer getNumericalValueOfStudentLevel() {
        return numericalValueOfStudentLevel;
    }

    public void setNumericalValueOfStudentLevel(Integer numericalValueOfStudentLevel) {
        this.numericalValueOfStudentLevel = numericalValueOfStudentLevel;
    }
}
