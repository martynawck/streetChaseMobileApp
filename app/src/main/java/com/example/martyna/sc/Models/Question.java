package com.example.martyna.sc.Models;

/**
 * Created by Martyna on 2016-01-18.
 */
public class Question {

    private int id;
    private String question;
    private String answer;

    public int getControlPoint() {
        return controlPoint;
    }

    public void setControlPoint(int controlPoint) {
        this.controlPoint = controlPoint;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    private int controlPoint;
}
