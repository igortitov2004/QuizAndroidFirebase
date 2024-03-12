package com.example.quiz.models;

public class UserResult {
    String username;
    int result;

    public UserResult(){

    }

    public UserResult(String username, int result) {
        this.username = username;
        this.result = result;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
