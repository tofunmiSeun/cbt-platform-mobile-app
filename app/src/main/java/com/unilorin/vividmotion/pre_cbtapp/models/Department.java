package com.unilorin.vividmotion.pre_cbtapp.models;

/**
 * Created by Tofunmi on 25/12/2016.
 */

public class Department {
    private Long id;
    private String name;
    private Long facultyId;
    private Integer courseDurationInYears;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(Long facultyId) {
        this.facultyId = facultyId;
    }

    public Integer getCourseDurationInYears() {
        return courseDurationInYears;
    }

    public void setCourseDurationInYears(Integer courseDurationInYears) {
        this.courseDurationInYears = courseDurationInYears;
    }
}
