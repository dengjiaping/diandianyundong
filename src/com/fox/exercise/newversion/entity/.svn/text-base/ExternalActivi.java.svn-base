package com.fox.exercise.newversion.entity;

import java.io.Serializable;

/**
 * @author loujungang 外部活动bean
 */
public class ExternalActivi implements Serializable {
    private int id;// 活动id
    private String title;// 活动题目
    private String thumb;// 活动图片
    private String start_time;// 开始时间，现在都是按的00：00：00算的，
    private String end_time;// 结束时间，现在都是按的00：00：00算的，
    private String url;// 跳转的地址，要加上参数session_id和id两个
    private int price;// 报名金额
    private int href_id;// 跳转类型id，1，第三方外部连接跳转根据url值2活动详情跳转根据web_id跳转，3说说详情跳转根据web_id跳转，4商城商品详情地址为http://dev-kupao.mobifox.cn/Beauty/kupao.php?m=Webapp&a=url_jump&id=3&session_id=kp_5739603ca9d57&gid＝web_id拼接的
    private int web_id;// 商品/活动/说说的id

//    private int start_hour;//开始小时默认0
//    private int end_hour;//结束小时默认24这个必须比开始小时大
    private int frequency;//频率默认为0即只弹一次，如果不为0即是几天一次，这个是天数

//    public int getStart_hour() {
//        return start_hour;
//    }
//
//    public void setStart_hour(int start_hour) {
//        this.start_hour = start_hour;
//    }
//
//    public int getEnd_hour() {
//        return end_hour;
//    }
//
//    public void setEnd_hour(int end_hour) {
//        this.end_hour = end_hour;
//    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }


    public int getHref_id() {
        return href_id;
    }

    public void setHref_id(int href_id) {
        this.href_id = href_id;
    }

    public int getWeb_id() {
        return web_id;
    }

    public void setWeb_id(int web_id) {
        this.web_id = web_id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
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

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
