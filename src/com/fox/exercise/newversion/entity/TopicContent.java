package com.fox.exercise.newversion.entity;

import java.io.Serializable;

public class TopicContent implements Serializable {

    private String id;//话题的id号
    private String title;//	话题的名字即是#个人秀，#还是推荐

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
