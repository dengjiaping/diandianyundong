package com.fox.exercise.api.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class FindMore implements Serializable {
    private int otheruid;
    private String findId;
    private String otherimg;
    private String othername;
    private String detils;
    private String[] imgs;
    private String[] biggerImgs;
    private Long times;
    private String sex;
    private int width;
    private int height;
    private ArrayList<FindComment> talkdetils;

    public ArrayList<FindComment> getTalkdetils() {
        return talkdetils;
    }

    public void setTalkdetils(ArrayList<FindComment> talkdetils) {
        this.talkdetils = talkdetils;
    }

    //登录人是否点赞
    private boolean good;
    //点赞人数
    private int goodpeople;

    public int getOtheruid() {
        return otheruid;
    }

    public void setOtheruid(int otheruid) {
        this.otheruid = otheruid;
    }

    private int cCount; //评论的人数

    public boolean isGood() {
        return good;
    }

    public void setGood(boolean good) {
        this.good = good;
    }

    public int getGoodpeople() {
        return goodpeople;
    }

    public void setGoodpeople(int goodpeople) {
        this.goodpeople = goodpeople;
    }

    public String getFindId() {
        return findId;
    }

    public void setFindId(String findId) {
        this.findId = findId;
    }

    public String getOtherimg() {
        return otherimg;
    }

    public void setOtherimg(String otherimg) {
        this.otherimg = otherimg;
    }

    public String getOthername() {
        return othername;
    }

    public void setOthername(String othername) {
        this.othername = othername;
    }

    public String getDetils() {
        return detils;
    }

    public void setDetils(String detils) {
        this.detils = detils;
    }

    public String[] getImgs() {
        return imgs;
    }

    public void setImgs(String[] imgs) {
        this.imgs = imgs;
    }

    public Long getTimes() {
        return times;
    }

    public String[] getBiggerImgs() {
        return biggerImgs;
    }

    public void setBiggerImgs(String[] biggerImgs) {
        this.biggerImgs = biggerImgs;
    }

    public void setTimes(Long times) {
        this.times = times;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getCommentCount() {
        return cCount;
    }

    public void setCommentCount(int cCount) {
        this.cCount = cCount;
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

}
