package com.unilorin.vividmotion.pre_cbtapp.models;

/**
 * Created by Tofunmi on 27/12/2016.
 */

public class Question {
    private static final int NONE_SELECTED = -1;
    private Long id;
    private String question;
    private String[] options;
    private int correctAnswerIndex;
    private int selectedAnswerIndex = NONE_SELECTED;
    private Long courseId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }

    public void setCorrectAnswerIndex(int correctAnswerIndex) {
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
}
