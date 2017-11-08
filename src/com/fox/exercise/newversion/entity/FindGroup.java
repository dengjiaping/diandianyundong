package com.fox.exercise.newversion.entity;

import java.io.Serializable;
import java.util.ArrayList;

import android.R.integer;

public class FindGroup implements Serializable {
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
    private ArrayList<SportCircleComments> talkdetils;
    private ArrayList<PraiseUsers> pArrayList;
    private SportRecord sportRecord;
    private ArrayList<TopicContent> topicList;
    private int flog = 0;
    private boolean isShow = false;

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean isShow) {
        this.isShow = isShow;
    }

    public int getFlog() {
        return flog;
    }

    public void setFlog(int flog) {
        this.flog = flog;
    }

    //登录人是否点赞
    private boolean good;
    //点赞人数
    private int goodpeople;

    private int cCount; //评论的人数

    private String comefrom;
    private int isFriends;//1表示是好友 2表示不是好友

    public int getOtheruid() {
        return otheruid;
    }

    public void setOtheruid(int otheruid) {
        this.otheruid = otheruid;
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

    public String[] getBiggerImgs() {
        return biggerImgs;
    }

    public void setBiggerImgs(String[] biggerImgs) {
        this.biggerImgs = biggerImgs;
    }

    public Long getTimes() {
        return times;
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

    public ArrayList<SportCircleComments> getTalkdetils() {
        return talkdetils;
    }

    public void setTalkdetils(ArrayList<SportCircleComments> talkdetils) {
        this.talkdetils = talkdetils;
    }

    public ArrayList<PraiseUsers> getpArrayList() {
        return pArrayList;
    }

    public void setpArrayList(ArrayList<PraiseUsers> pArrayList) {
        this.pArrayList = pArrayList;
    }


    public ArrayList<TopicContent> getTopicList() {
        return topicList;
    }

    public void setTopicList(ArrayList<TopicContent> topicList) {
        this.topicList = topicList;
    }

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

    public int getcCount() {
        return cCount;
    }

    public void setcCount(int cCount) {
        this.cCount = cCount;
    }

    public String getComefrom() {
        return comefrom;
    }

    public void setComefrom(String comefrom) {
        this.comefrom = comefrom;
    }

    public int getIsFriends() {
        return isFriends;
    }

    public void setIsFriends(int isFriends) {
        this.isFriends = isFriends;
    }

    public SportRecord getSportRecord() {
        return sportRecord;
    }

    public void setSportRecord(SportRecord sportRecord) {
        this.sportRecord = sportRecord;
    }
}
