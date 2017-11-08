package com.fox.exercise.api.entity;

public class UserMeComments {
    private int uid;
    private String uimg;
    private String uname;
    private String commentText;
    private String commentWav;
    private long addTime;
    private int wavDuration;
    private int pid;
    private String sex;
    private String birthday;
    private String pimg;
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

    public int getWavDuration() {
        return wavDuration;
    }

    public void setWavDuration(int wavDuration) {
        this.wavDuration = wavDuration;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPimg() {
        return pimg;
    }

    public void setPimg(String pimg) {
        this.pimg = pimg;
    }

    @Override
    public String toString() {
        return "UserMeComments [uid=" + uid + ", uimg=" + uimg + ", uname="
                + uname + ", commentText=" + commentText + ", commentWav="
                + commentWav + ", addTime=" + addTime + ", wavDuration="
                + wavDuration + ", pid=" + pid + ", sex=" + sex + ", birthday="
                + birthday + ", pimg=" + pimg + ", id=" + id + "]";
    }


}
