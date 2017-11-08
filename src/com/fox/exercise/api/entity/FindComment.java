package com.fox.exercise.api.entity;

import java.io.Serializable;

public class FindComment implements Serializable {
    //X回复Y
    private String firstName;
    private String secondName;
    //评论文字
    private String commentText;
    private String commentId;
    //评论音频
    private String commentWav;
    private String to_id; //回复评论的id
    private String to_uid;//回复评论的人的uid，回复评论时，要传这两个id
    //评论音频长度
    private String wavDuration;

    public String getTo_id() {
        return to_id;
    }

    public void setTo_id(String to_id) {
        this.to_id = to_id;
    }

    public String getTo_uid() {
        return to_uid;
    }

    public void setTo_uid(String to_uid) {
        this.to_uid = to_uid;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getCommentWav() {
        return commentWav;
    }

    public void setCommentWav(String commentWav) {
        this.commentWav = commentWav;
    }

    public String getWavDuration() {
        return wavDuration;
    }

    public void setWavDuration(String wavDuration) {
        this.wavDuration = wavDuration;
    }
}
