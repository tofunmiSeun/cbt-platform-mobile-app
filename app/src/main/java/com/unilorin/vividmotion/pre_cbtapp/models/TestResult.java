package com.unilorin.vividmotion.pre_cbtapp.models;

import java.util.Date;
import java.util.List;

/**
 * Created by Tofunmi on 29/12/2016.
 */

public class TestResult {
    private Long id;

    private Long userId;
    private Long courseId;

    private List<Long> questionsId;

    private int totalQuestionsCount;
    private int questionsAnsweredCount;
    private int correctAnswersCount;

    private Date timeOfTest;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public List<Long> getQuestionsId() {
        return questionsId;
    }

    public void setQuestionsId(List<Long> questionsId) {
        this.questionsId = questionsId;
    }

    public int getTotalQuestionsCount() {
        return totalQuestionsCount;
    }

    public void setTotalQuestionsCount(int totalQuestionsCount) {
        this.totalQuestionsCount = totalQuestionsCount;
    }

    public int getQuestionsAnsweredCount() {
        return questionsAnsweredCount;
    }

    public void setQuestionsAnsweredCount(int questionsAnsweredCount) {
        this.questionsAnsweredCount = questionsAnsweredCount;
    }

    public int getCorrectAnswersCount() {
        return correctAnswersCount;
    }

    public void setCorrectAnswersCount(int correctAnswersCount) {
        this.correctAnswersCount = correctAnswersCount;
    }

    public Date getTimeOfTest() {
        return timeOfTest;
    }

    public void setTimeOfTest(Date timeOfTest) {
        this.timeOfTest = timeOfTest;
    }
}
