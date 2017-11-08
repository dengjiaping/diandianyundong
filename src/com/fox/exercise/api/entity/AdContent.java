package com.fox.exercise.api.entity;

public class AdContent {
    //广告图片地址
    private String adimg;
    //播放秒数
    private String play_seconds;
    private String jumpurl;
    private String linkrul;
    private String xfadid;//讯飞广告位id
    private int is_xunfei;//是否是讯飞广告 1代表是 0代表不是

    public String getXfadid() {
        return xfadid;
    }

    public void setXfadid(String xfadid) {
        this.xfadid = xfadid;
    }

    public int getIs_xunfei() {
        return is_xunfei;
    }

    public void setIs_xunfei(int is_xunfei) {
        this.is_xunfei = is_xunfei;
    }

    public String getAdimg() {
        return adimg;
    }

    public void setAdimg(String adimg) {
        this.adimg = adimg;
    }

    public String getPlay_seconds() {
        return play_seconds;
    }

    public void setPlay_seconds(String play_seconds) {
        this.play_seconds = play_seconds;
    }

    public String getJumpurl() {
        return jumpurl;
    }

    public void setJumpurl(String jumpurl) {
        this.jumpurl = jumpurl;
    }

    public String getLinkrul() {
        return linkrul;
    }

    public void setLinkrul(String linkrul) {
        this.linkrul = linkrul;
    }

}
