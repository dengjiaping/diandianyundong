package com.fox.exercise.api.entity;

public class PrivateMsgStatus {

    private UserPrimsgAll userPrimsgAll;
    private String msgStatus;

    public UserPrimsgAll getUserPrimsgAll() {
        return userPrimsgAll;
    }

    public void setUserPrimsgAll(UserPrimsgAll userPrimsgAll) {
        this.userPrimsgAll = userPrimsgAll;
    }

    public String getMsgStatus() {
        return msgStatus;
    }

    public void setMsgStatus(String msgStatus) {
        this.msgStatus = msgStatus;
    }

    @Override
    public String toString() {
        return "PrivateMsgStatus [userPrimsgAll=" + userPrimsgAll + ", msgStatus=" + msgStatus + "]";
    }


}
