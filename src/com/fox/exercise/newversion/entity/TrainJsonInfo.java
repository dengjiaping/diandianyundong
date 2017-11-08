package com.fox.exercise.newversion.entity;

/**
 * @author loujungang 获取的json文件内容
 */
public class TrainJsonInfo {
    private String playname;//播放文件名称
    private int posion;//播放顺序
    private double size;//文件大小
    private int time;//文件播放时长
    private int actionNum;//动作次数
    private String actionName;//动作名称
    private int actionFlag;//表示播放录音背景时播放的是秒数还是次数 1表示次数 0表示秒数
    private double actionCal;//表示视频消耗的卡路里
    private int otherFlag;//表示播放录音背景时是否插播其他话  1表示插播 0表示不
    private int sleepTime;//表示播放完录音后是否有休息时间  大于0表示休息的秒数


    public int getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    public int getOtherFlag() {
        return otherFlag;
    }

    public void setOtherFlag(int otherFlag) {
        this.otherFlag = otherFlag;
    }

    public double getActionCal() {
        return actionCal;
    }

    public void setActionCal(double actionCal) {
        this.actionCal = actionCal;
    }

    public int getActionFlag() {
        return actionFlag;
    }

    public void setActionFlag(int actionFlag) {
        this.actionFlag = actionFlag;
    }

    public String getPlayname() {
        return playname;
    }

    public void setPlayname(String playname) {
        this.playname = playname;
    }

    public int getPosion() {
        return posion;
    }

    public void setPosion(int posion) {
        this.posion = posion;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getActionNum() {
        return actionNum;
    }

    public void setActionNum(int actionNum) {
        this.actionNum = actionNum;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }


}
