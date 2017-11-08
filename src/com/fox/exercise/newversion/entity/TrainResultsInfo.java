package com.fox.exercise.newversion.entity;

import java.io.Serializable;

/**
 * @author loujungang 训练成绩
 */
public class TrainResultsInfo implements Serializable {
    private String sessionId;// 验证用户是否登陆
    private int train_id;// 训练id
    private int traintime; // 训练时间/秒
    private double train_calorie;// 训练消耗的卡路里
    private String train_action;// 训练动作的id字符串以,分割
    private String train_position; // 训练部位
    private int train_completion;// 训练完成度：如80%传80
    private String train_starttime;// 训练开始时间格式2016-01-01 01：01：01
    private String train_endtime; // 训练结束时间格式2016-01-01 01：01：02
    private int is_total; // 是否是训练的总成绩：1表示是，0表示是某个动作的成绩
    private String unique_id;// 该成绩的唯一标识码，用于区分不同过的训练成绩最长32为
    private String datasource;// 数据来源，ios android

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public int getTrain_id() {
        return train_id;
    }

    public void setTrain_id(int train_id) {
        this.train_id = train_id;
    }

    public int getTraintime() {
        return traintime;
    }

    public void setTraintime(int traintime) {
        this.traintime = traintime;
    }

    public double getTrain_calorie() {
        return train_calorie;
    }

    public void setTrain_calorie(double train_calorie) {
        this.train_calorie = train_calorie;
    }

    public String getTrain_action() {
        return train_action;
    }

    public void setTrain_action(String train_action) {
        this.train_action = train_action;
    }

    public String getTrain_position() {
        return train_position;
    }

    public void setTrain_position(String train_position) {
        this.train_position = train_position;
    }

    public int getTrain_completion() {
        return train_completion;
    }

    public void setTrain_completion(int train_completion) {
        this.train_completion = train_completion;
    }

    public String getTrain_starttime() {
        return train_starttime;
    }

    public void setTrain_starttime(String train_starttime) {
        this.train_starttime = train_starttime;
    }

    public String getTrain_endtime() {
        return train_endtime;
    }

    public void setTrain_endtime(String train_endtime) {
        this.train_endtime = train_endtime;
    }

    public int getIs_total() {
        return is_total;
    }

    public void setIs_total(int is_total) {
        this.is_total = is_total;
    }

    public String getUnique_id() {
        return unique_id;
    }

    public void setUnique_id(String unique_id) {
        this.unique_id = unique_id;
    }

    public String getDatasource() {
        return datasource;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource;
    }

}
