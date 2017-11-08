package com.fox.exercise.api.entity;

import java.io.Serializable;

public class SportMediaFile implements Serializable {
    private static final long serialVersionUID = -2826739009473002087L;

    private int uid;

    private int mediaId;

    private int taskID;

    private int mediaTypeID;

    private int durations;

    private String mediaFilePath;

    private int width;
    private int height;

    private int mapType;

    public int getMapType() {
        return mapType;
    }

    public void setMapType(int mapType) {
        this.mapType = mapType;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getMediaId() {
        return mediaId;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public int getMediaTypeID() {
        return mediaTypeID;
    }

    public void setMediaTypeID(int mediaTypeID) {
        this.mediaTypeID = mediaTypeID;
    }

    public int getDurations() {
        return durations;
    }

    public void setDurations(int durations) {
        this.durations = durations;
    }

    public String getMediaFilePath() {
        return mediaFilePath;
    }

    public void setMediaFilePath(String mediaFilePath) {
        this.mediaFilePath = mediaFilePath;
    }

    public String getMediaFileName() {
        return mediaFileName;
    }

    public void setMediaFileName(String mediaFileName) {
        this.mediaFileName = mediaFileName;
    }

    public String getPointStr() {
        return pointStr;
    }

    public void setPointStr(String pointStr) {
        this.pointStr = pointStr;
    }

    private String mediaFileName;

    private String pointStr;

}
