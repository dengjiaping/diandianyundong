package com.fox.exercise.newversion.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.amap.api.maps.model.LatLng;

/**
 * @author loujungang  存放运动中时数据丢失的bean
 */
public class GaoDeInstanceBean implements Serializable {
    private String startTime;
    private String markCode;// 运动唯一标识
    private int recLen = 0;
    private int mTempCount = 0;
    //	private String mGeoPoints;
//	private String mDrawPoints;
    private int typeId, deviceId;
    private long startTimeSeconds;
    private double dis, speed;// , heart;
    private double heart;
    private int typeDetailId;
    private Boolean isFirstSave;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getMarkCode() {
        return markCode;
    }

    public void setMarkCode(String markCode) {
        this.markCode = markCode;
    }

    public int getRecLen() {
        return recLen;
    }

    public void setRecLen(int recLen) {
        this.recLen = recLen;
    }

    public int getmTempCount() {
        return mTempCount;
    }

    public void setmTempCount(int mTempCount) {
        this.mTempCount = mTempCount;
    }

    //	public String getmGeoPoints() {
//		return mGeoPoints;
//	}
//	public void setmGeoPoints(String mGeoPoints) {
//		this.mGeoPoints = mGeoPoints;
//	}
//	public String getmDrawPoints() {
//		return mDrawPoints;
//	}
//	public void setmDrawPoints(String mDrawPoints) {
//		this.mDrawPoints = mDrawPoints;
//	}
    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public long getStartTimeSeconds() {
        return startTimeSeconds;
    }

    public void setStartTimeSeconds(long startTimeSeconds) {
        this.startTimeSeconds = startTimeSeconds;
    }

    public double getDis() {
        return dis;
    }

    public void setDis(double dis) {
        this.dis = dis;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getHeart() {
        return heart;
    }

    public void setHeart(double heart) {
        this.heart = heart;
    }

    public int getTypeDetailId() {
        return typeDetailId;
    }

    public void setTypeDetailId(int typeDetailId) {
        this.typeDetailId = typeDetailId;
    }

    public Boolean getIsFirstSave() {
        return isFirstSave;
    }

    public void setIsFirstSave(Boolean isFirstSave) {
        this.isFirstSave = isFirstSave;
    }


}
