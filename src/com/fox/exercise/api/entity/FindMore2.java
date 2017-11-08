package com.fox.exercise.api.entity;

public class FindMore2 {
    public int type = 0; //0评论，1赞
    public String picPath;
    public String findId;
    public String commentId;//评论的id该评论的唯一标识
    public String uId;
    public String toId;
    public String toUid;
    public String content;
    public String wavAddress;
    public String wavTime;
    public String inputTime;
    public String name;
    public String toName;
    public String userImg;
    public String zId;//贊的id唯一标识

    public String time;

    public String getzId() {
        return zId;
    }

    public void setzId(String zId) {
        this.zId = zId;
    }

    public boolean iszanOrPing() {
        return iszanOrPing;
    }

    public void setIszanOrPing(boolean iszanOrPing) {
        this.iszanOrPing = iszanOrPing;
    }

    public boolean iszanOrPing;

    public int getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(int is_delete) {
        this.is_delete = is_delete;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public String getFindId() {
        return findId;
    }

    public void setFindId(String findId) {
        this.findId = findId;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public String getToUid() {
        return toUid;
    }

    public void setToUid(String toUid) {
        this.toUid = toUid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWavAddress() {
        return wavAddress;
    }

    public void setWavAddress(String wavAddress) {
        this.wavAddress = wavAddress;
    }

    public String getWavTime() {
        return wavTime;
    }

    public void setWavTime(String wavTime) {
        this.wavTime = wavTime;
    }

    public String getInputTime() {
        return inputTime;
    }

    public void setInputTime(String inputTime) {
        this.inputTime = inputTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int is_delete;
}
