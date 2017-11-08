package com.fox.exercise.api.entity;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;

import com.fox.exercise.activityinfo2;

public class ActivityInfo {

    public int actionId;
    public String title;
    //public String thumb;
    public String description;
    public String biaoti_img;
    public String jiangli_img;
    public String content;    //活动内容
    public String canjia_type;    //参加方式
    public String act_explain;    //活动说明
    public String activity_time;    //活动时间
    public int isshow;//是否显示
    public String activity_URl;
    private long start_time, end_time;//


    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }

    public String getActivity_URl() {
        return activity_URl;
    }

    public void setActivity_URl(String activity_URl) {
        this.activity_URl = activity_URl;
    }

    public int getIsshow() {
        return isshow;
    }

    public void setIsshow(int isshow) {
        this.isshow = isshow;
    }

    public List<activityinfo2> list_catInfo = new ArrayList<activityinfo2>();

    public List<activityinfo2> getList_catInfo() {
        return list_catInfo;
    }

    public void setList_catInfo(List<activityinfo2> list_catInfo) {
        this.list_catInfo = list_catInfo;
    }

    public int getActionId() {
        return actionId;
    }

    public void setActionId(int actionId) {
        this.actionId = actionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBiaoti_img() {
        return biaoti_img;
    }

    public void setBiaoti_img(String biaoti_img) {
        this.biaoti_img = biaoti_img;
    }

    public String getJiangli_img() {
        return jiangli_img;
    }

    public void setJiangli_img(String jiangli_img) {
        this.jiangli_img = jiangli_img;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCanjia_type() {
        return canjia_type;
    }

    public void setCanjia_type(String canjia_type) {
        this.canjia_type = canjia_type;
    }

    public String getAct_explain() {
        return act_explain;
    }

    public void setAct_explain(String act_explain) {
        this.act_explain = act_explain;
    }

    public String getActivity_time() {
        return activity_time;
    }

    public void setActivity_time(String activity_time) {
        this.activity_time = activity_time;
    }


}
