package com.fox.exercise.pedometer;

import com.fox.exercise.newversion.entity.SportRecord;

public class SportContionTaskDetail {
    private String times;//
    private int sprots_Calorie;
    private int taskid;
    private String latlng;
    private int sports_type;
    private String startTime;
    private int monitoringEquipment;
    private double sportDistance;
    private String sportTime;
    private double sportVelocity;
    private double heartRate;
    private int userId;
    private int stepNum;
    private String sportDate;
    private int swimType;
    private int mapType;
    /**
     * 是否上传到服务器
     * -1:表示还没上传到服务器
     */
    public int sport_isupload;

    private SportRecord sportRecord;//运动总天数和最近运动日期

    private String sport_markcode;//运动唯一标示
    private String sport_speedList;//运动速度列表
    private String coordinate_list;//整公里是的gps点

    public String getCoordinate_list() {
        return coordinate_list;
    }

    public void setCoordinate_list(String coordinate_list) {
        this.coordinate_list = coordinate_list;
    }

    public String getSport_speedList() {
        return sport_speedList;
    }

    public void setSport_speedList(String sport_speedList) {
        this.sport_speedList = sport_speedList;
    }

    public String getSport_markcode() {
        return sport_markcode;
    }

    public void setSport_markcode(String sport_markcode) {
        this.sport_markcode = sport_markcode;
    }

    public SportRecord getSportRecord() {
        return sportRecord;
    }

    public void setSportRecord(SportRecord sportRecord) {
        this.sportRecord = sportRecord;
    }

    public int getSport_isupload() {
        return sport_isupload;
    }

    public void setSport_isupload(int sport_isupload) {
        this.sport_isupload = sport_isupload;
    }

    public int getMapType() {
        return mapType;
    }

    public void setMapType(int mapType) {
        this.mapType = mapType;
    }

    public int getSwimType() {
        return swimType;
    }

    public void setSwimType(int swimType) {
        this.swimType = swimType;
    }

    public String getSportDate() {
        return sportDate;
    }

    public void setSportDate(String sportDate) {
        this.sportDate = sportDate;
    }

    public int getMonitoringEquipment() {
        return monitoringEquipment;
    }

    public void setMonitoringEquipment(int monitoringEquipment) {
        this.monitoringEquipment = monitoringEquipment;
    }

    public double getSportDistance() {
        return sportDistance;
    }

    public void setSportDistance(double sportDistance) {
        this.sportDistance = sportDistance;
    }

    public String getSportTime() {
        return sportTime;
    }

    public void setSportTime(String sportTime) {
        this.sportTime = sportTime;
    }

    public double getSportVelocity() {
        return sportVelocity;
    }

    public void setSportVelocity(double sportVelocity) {
        this.sportVelocity = sportVelocity;
    }

    public double getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(double heartRate) {
        this.heartRate = heartRate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getStepNum() {
        return stepNum;
    }

    public void setStepNum(int stepNum) {
        this.stepNum = stepNum;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public int getSprots_Calorie() {
        return sprots_Calorie;
    }

    public void setSprots_Calorie(int sprots_Calorie) {
        this.sprots_Calorie = sprots_Calorie;
    }

    public int getTaskid() {
        return taskid;
    }

    public void setTaskid(int taskid) {
        this.taskid = taskid;
    }

    public String getLatlng() {
        return latlng;
    }

    public void setLatlng(String latlng) {
        this.latlng = latlng;
    }

    public int getSports_type() {
        return sports_type;
    }

    public void setSports_type(int sports_type) {
        this.sports_type = sports_type;
    }

    @Override
    public String toString() {
        return "SportContionTaskDetail [times=" + times + ", sprots_Calorie=" + sprots_Calorie + ", taskid=" + taskid
                + ", latlng=" + latlng + ", sports_type=" + sports_type + ", startTime=" + startTime
                + ", monitoringEquipment=" + monitoringEquipment + ", sportDistance=" + sportDistance + ", sportTime="
                + sportTime + ", sportVelocity=" + sportVelocity + ", heartRate=" + heartRate + ", userId=" + userId
                + ",stepNum=" + stepNum + "]";
    }


}
