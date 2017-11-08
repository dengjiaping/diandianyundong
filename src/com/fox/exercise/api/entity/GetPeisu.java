package com.fox.exercise.api.entity;

/**
 * Created by admin on 2016/8/16.
 */
public class GetPeisu {
    public String sport_distance;//运动的公里数/每公里
    public String sprots_velocity;//运动配速/每公里
    public String sprots_time;//运动耗时/每公里
    private String gPS_type;//登山是否获得gps信号 1表示是获得 2表示没有获得

    public String getgPS_type() {
        return gPS_type;
    }

    public void setgPS_type(String gPS_type) {
        this.gPS_type = gPS_type;
    }

    public String getSprots_time() {
        return sprots_time;
    }

    public void setSprots_time(String sprots_time) {
        this.sprots_time = sprots_time;
    }

    public String getSport_distance() {
        return sport_distance;
    }

    public void setSport_distance(String sport_distance) {
        this.sport_distance = sport_distance;
    }

    public String getSprots_velocity() {
        return sprots_velocity;
    }

    public void setSprots_velocity(String sprots_velocity) {
        this.sprots_velocity = sprots_velocity;
    }
}
