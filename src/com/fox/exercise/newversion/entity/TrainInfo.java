package com.fox.exercise.newversion.entity;

import java.util.ArrayList;

import android.R.array;

/**
 * @author loujungang 训练计划的详情
 */
public class TrainInfo {
    private int id;//训练id
    private String train_name;//训练名字
    private String thumb;//训练图片地址
    private int grade;//难道指数
    private String position;//训练部位
    private int train_time;//训练时长
    private double train_calorie;//训练消耗卡路里
    private String train_fileurl;//训练下载地址
    private int action_num;//训练动作数量
    private int traincount;//该训练有多少人训练了
    private ArrayList<TrainUserInfo> tUserList;//训练过的用户的信息列表10条
    private ArrayList<TrainAction> actionlist;//该训练的训练动作

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTrain_name() {
        return train_name;
    }

    public void setTrain_name(String train_name) {
        this.train_name = train_name;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getTrain_time() {
        return train_time;
    }

    public void setTrain_time(int train_time) {
        this.train_time = train_time;
    }

    public double getTrain_calorie() {
        return train_calorie;
    }

    public void setTrain_calorie(double train_calorie) {
        this.train_calorie = train_calorie;
    }

    public String getTrain_fileurl() {
        return train_fileurl;
    }

    public void setTrain_fileurl(String train_fileurl) {
        this.train_fileurl = train_fileurl;
    }

    public int getAction_num() {
        return action_num;
    }

    public void setAction_num(int action_num) {
        this.action_num = action_num;
    }

    public int getTraincount() {
        return traincount;
    }

    public void setTraincount(int traincount) {
        this.traincount = traincount;
    }

    public ArrayList<TrainUserInfo> gettUserList() {
        return tUserList;
    }

    public void settUserList(ArrayList<TrainUserInfo> tUserList) {
        this.tUserList = tUserList;
    }

    public ArrayList<TrainAction> getActionlist() {
        return actionlist;
    }

    public void setActionlist(ArrayList<TrainAction> actionlist) {
        this.actionlist = actionlist;
    }


}
