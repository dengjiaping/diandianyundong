package com.fox.exercise.newversion.entity;

/**
 * @author loujungang 用户训练总成绩
 */
public class TrainCount {
    private int traintime;//训练总时间
    private double train_calorie;//训练总消耗卡路里
    private int countnum;//训练总条数
    private int countday;//训练总天数

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

    public int getCountnum() {
        return countnum;
    }

    public void setCountnum(int countnum) {
        this.countnum = countnum;
    }

    public int getCountday() {
        return countday;
    }

    public void setCountday(int countday) {
        this.countday = countday;
    }

}
