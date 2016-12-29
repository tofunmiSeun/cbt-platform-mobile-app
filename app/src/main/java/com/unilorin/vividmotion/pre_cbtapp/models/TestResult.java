package com.unilorin.vividmotion.pre_cbtapp.models;

/**
 * Created by Tofunmi on 29/12/2016.
 */

public class TestResult {
    private int totalQuestionsCount;
    private int questionsAnsweredCount;
    private int correctAnswersCount;
    private Long [] questionsId;
    private Long userId;
    private Long courseId;

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

    public Long[] getQuestionsId() {
        return questionsId;
    }

    public void setQuestionsId(Long[] questionsId) {
        this.questionsId = questionsId;
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
}
