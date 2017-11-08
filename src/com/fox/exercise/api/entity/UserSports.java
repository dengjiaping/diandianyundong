package com.fox.exercise.api.entity;

public class UserSports {
    private int uid;
    //0——走路 1——跑步
    private int sportsType;
    //目标步数
    private int scoreStep;
    //目标卡路里
    private int scoreCalorie;
    //上传消耗卡路里
    private int calories;
    //上传步数
    private int step;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getSportsType() {
        return sportsType;
    }

    public void setSportsType(int sportsType) {
        this.sportsType = sportsType;
    }

    public int getScoreStep() {
        return scoreStep;
    }

    public void setScoreStep(int scoreStep) {
        this.scoreStep = scoreStep;
    }

    public int getScoreCalorie() {
        return scoreCalorie;
    }

    public void setScoreCalorie(int scoreCalorie) {
        this.scoreCalorie = scoreCalorie;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    @Override
    public String toString() {
        return "UserSports [calories=" + calories + ", scoreCalorie="
                + scoreCalorie + ", scoreStep=" + scoreStep + ", sportsType="
                + sportsType + ", step=" + step + ", uid=" + uid + "]";
    }

}
