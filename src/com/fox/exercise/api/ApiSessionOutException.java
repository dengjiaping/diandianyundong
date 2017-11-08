package com.fox.exercise.api;

public class ApiSessionOutException extends Exception {
    private String msg;

    public ApiSessionOutException(String s) {
        this.msg = s;
    }

    public String exceMsg() {
        return msg;
    }
}
