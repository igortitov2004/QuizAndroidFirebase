package com.example.quiz.models;

import java.util.List;

public class Question {
    String qText;
    List<Option> options;

    public Question(){

    }

    public String getqText() {
        return qText;
    }

    public void setqText(String qText) {
        this.qText = qText;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }
}
