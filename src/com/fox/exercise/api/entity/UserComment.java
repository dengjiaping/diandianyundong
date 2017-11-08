package com.fox.exercise.api.entity;

public class UserComment {
    private int uid;
    private String uimg;
    private String uname;
    private String commentText;
    private String commentWav;
    private long addTime;
    private int wavDuration;
    private String sex;
    private String touname;

    public String getTouname() {
        return touname;
    }

    public void setTouname(String touname) {
        this.touname = touname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getWavDuration() {
        return wavDuration;
    }

    public void setWavDuration(int wavDuration) {
        this.wavDuration = wavDuration;
    }

    //解决错位问题
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUimg() {
        return uimg;
    }

    public void setUimg(String uimg) {
        this.uimg = uimg;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
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

    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    @Override
    public String toString() {
        return "UserComment [uid=" + uid + ", uimg=" + uimg + ", uname="
                + uname + ", commentText=" + commentText + ", commentWav="
                + commentWav + ", addTime=" + addTime + ", wavDuration="
                + wavDuration + ", sex=" + sex + ", touname=" + touname
                + ", id=" + id + "]";
    }


}
