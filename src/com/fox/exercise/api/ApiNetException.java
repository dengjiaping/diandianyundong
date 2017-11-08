package com.fox.exercise.api;

public class ApiNetException extends Exception {

    private String msg;

    public ApiNetException(String s) {
        this.msg = s;
    }

    public String exceMsg() {
        return msg;
    }

}
