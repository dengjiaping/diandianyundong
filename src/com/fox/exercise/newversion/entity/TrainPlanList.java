package com.fox.exercise.newversion.entity;

import java.io.Serializable;


/**
 * @author loujungang 训练计划列表
 */
public class TrainPlanList implements Serializable {
    private int Id;//训练id
    private String train_name;//训练名字
    private String thumb;//训练图片地址
    private int grade;//难度指数
    private String position;//训练部位
    private int train_time;//训练时长
    private double train_calorie;//训练消耗卡路里
    private int traincount;//该训练有多少人训练了

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
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

    public int getTraincount() {
        return traincount;
    }

    public void setTraincount(int traincount) {
        this.traincount = traincount;
    }


}
