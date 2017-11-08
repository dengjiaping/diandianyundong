package com.fox.exercise.api.entity;

public class UserRank {
    private int id;
    private String name;
    private String img;
    private int rankNumber;
    //1——已认证 其他——未认证
    private int authStatus;
    private int sex;

    public int getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(int authStatus) {
        this.authStatus = authStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getRankNumber() {
        return rankNumber;
    }

    public void setRankNumber(int rankNumber) {
        this.rankNumber = rankNumber;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "UserRank [id=" + id + ", name=" + name + ", img=" + img
                + ", rankNumber=" + rankNumber + "]";
    }

}
