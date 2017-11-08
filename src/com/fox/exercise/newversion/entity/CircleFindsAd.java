package com.fox.exercise.newversion.entity;

import java.io.Serializable;

import android.R.integer;

/**
 * @author loujungang 发现页面的头部的图片和id
 */
public class CircleFindsAd implements Serializable {
    private int id;
    private String img;//图片地址

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

}
