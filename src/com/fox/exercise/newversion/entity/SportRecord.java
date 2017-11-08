package com.fox.exercise.newversion.entity;

import java.io.Serializable;

public class SportRecord implements Serializable {
    private String time;//	运动天数
    private String sport_distance;//	运动距离

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSport_distance() {
        return sport_distance;
    }

    public void setSport_distance(String sport_distance) {
        this.sport_distance = sport_distance;
    }


}
