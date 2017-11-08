package com.fox.exercise.pedometer;

import java.util.ArrayList;
import java.util.List;

public class SportConditionDetail {
    private int sports_type;
    private int score_step;
    private int score_calorie;
    private int gain_step;
    private int gain_calorie;
    private int uid;
    private String current_times;
    private int calorieCount;
    private List<SportContionTaskDetail> sportTasksPhone = new ArrayList<SportContionTaskDetail>();
    private List<SportContionTaskDetail> sportTasksWatch = new ArrayList<SportContionTaskDetail>();

    public int getSports_type() {
        return sports_type;
    }

    public void setSports_type(int sports_type) {
        this.sports_type = sports_type;
    }

    public int getScore_step() {
        return score_step;
    }

    public void setScore_step(int score_step) {
        this.score_step = score_step;
    }

    public int getScore_calorie() {
        return score_calorie;
    }

    public void setScore_calorie(int score_calorie) {
        this.score_calorie = score_calorie;
    }

    public int getGain_step() {
        return gain_step;
    }

    public void setGain_step(int gain_step) {
        this.gain_step = gain_step;
    }

    public int getGain_calorie() {
        return gain_calorie;
    }

    public void setGain_calorie(int gain_calorie) {
        this.gain_calorie = gain_calorie;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getCalorieCount() {
        return calorieCount;
    }

    public void setCalorieCount(int calorieCount) {
        this.calorieCount = calorieCount;
    }

    public String getCurrent_times() {
        return current_times;
    }

    public void setCurrent_times(String current_times) {
        this.current_times = current_times;
    }

    public List<SportContionTaskDetail> getSportTasks() {
        return sportTasksPhone;
    }

    public void setSportTasks(List<SportContionTaskDetail> sportTasks) {
        this.sportTasksPhone = sportTasks;
    }

    public List<SportContionTaskDetail> getSportTasksWatch() {
        return sportTasksWatch;
    }

    public void setSportTasksWatch(List<SportContionTaskDetail> sportTasks) {
        this.sportTasksWatch = sportTasks;
    }

    @Override
    public String toString() {
        return "SportConditionDetail [sports_type=" + sports_type
                + ", score_step=" + score_step + ", score_calorie="
                + score_calorie + ", gain_step=" + gain_step
                + ", gain_calorie=" + gain_calorie + ", uid=" + uid
                + ", current_times=" + current_times + ", calorieCount=" + calorieCount + "]";
    }
}
