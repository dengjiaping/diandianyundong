package com.fox.exercise;

public class FollowAndFun {
    public int uid = 0;
    public int status = 0;
    public String oppoFollow = "";
    public int position = 0;

    public FollowAndFun(int uid, int status, String follow, int position) {
        this.oppoFollow = follow;
        this.status = status;
        this.uid = uid;
        this.position = position;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOppoFollow() {
        return oppoFollow;
    }

    public void setOppoFollow(String oppoFollow) {
        this.oppoFollow = oppoFollow;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

}
