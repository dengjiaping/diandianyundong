package com.fox.exercise.newversion.entity;

public class YePaoInfo {
    private int id;
    private String order_nu;//订单号
    private int uid;//用户id
    private int mlsid;//活动id
    private String mls_name;//活动名字
    private int mls_price;//活动报名费
    private String user_name;//用户名字

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrder_nu() {
        return order_nu;
    }

    public void setOrder_nu(String order_nu) {
        this.order_nu = order_nu;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getMlsid() {
        return mlsid;
    }

    public void setMlsid(int mlsid) {
        this.mlsid = mlsid;
    }

    public String getMls_name() {
        return mls_name;
    }

    public void setMls_name(String mls_name) {
        this.mls_name = mls_name;
    }

    public int getMls_price() {
        return mls_price;
    }

    public void setMls_price(int mls_price) {
        this.mls_price = mls_price;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
