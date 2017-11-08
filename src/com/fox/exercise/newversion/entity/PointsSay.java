package com.fox.exercise.newversion.entity;

import java.util.ArrayList;

/**
 * @author loujungang 点点说详情页面
 */
public class PointsSay {

    private int id;// id
    private String title;// 标题
    private String connent;// 内容
    private String img;// 图片
    private String start_time;// 开始时间
    private String end_time;// 结束时间
    private ArrayList<SysSportCircleComments> fList;//评论数组

    public ArrayList<SysSportCircleComments> getfList() {
        return fList;
    }

    public void setfList(ArrayList<SysSportCircleComments> fList) {
        this.fList = fList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getConnent() {
        return connent;
    }

    public void setConnent(String connent) {
        this.connent = connent;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }


}
