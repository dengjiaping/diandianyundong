package com.fox.exercise.login;

import android.app.Dialog;
import android.widget.EditText;

public class OriginalLoginInfoViews {
    private EditText name;
    private EditText password;
    private Dialog progressDialog;
    private int loginFinish = 0;
    private int loginFail = 0;

    public OriginalLoginInfoViews(EditText name, EditText password, Dialog progressDialog, int loginFinish,
                                  int loginFail) {
        this.name = name;
        this.password = password;
        this.progressDialog = progressDialog;
        this.loginFail = loginFail;
        this.loginFinish = loginFinish;
    }

    public int getLoginFinish() {
        return loginFinish;
    }

    public int getLoginFail() {
        return loginFail;
    }

    public Dialog getProgressDialog() {
        return progressDialog;
    }

    public EditText getName() {
        return name;
    }

    public EditText getPassword() {
        return password;
    }
}
