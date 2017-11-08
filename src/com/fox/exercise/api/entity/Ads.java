package com.fox.exercise.api.entity;

public class Ads {
    /**
     * pp_focus:广告表
     */
    private int id;
    private String title;
    private String imgUrl;
    private String url;
    /**
     * status
     * 0——其他广告
     * 1——活动广告
     * 2——用户广告（用户广告用到uid）
     */
    private int status;
    private int uid;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public String toString() {
        return "Ads [id=" + id + ", title=" + title + ", imgUrl=" + imgUrl
                + ", url=" + url + ", status=" + status + ", uid=" + uid + "]";
    }

}
