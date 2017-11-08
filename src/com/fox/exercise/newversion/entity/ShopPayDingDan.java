package com.fox.exercise.newversion.entity;

public class ShopPayDingDan {
    private int id;
    private int uid;// 商户id
    private String order_nu;// 订单号
    private int gid;// 商品id
    private String goods_name;// 商品名字
    private String goods_pic;//图片地址
    private int goods_xjprice;// 商品现金价格
    private String goods_num;//
    private String user_name;// 客户名字
    private String user_phone;// 客户电话
    private String create_time;// 订单创建时间
    private int pay_status;// 订单支付状态
    private String transaction_id;
    private String openid;
    private int pay_money;// 支付的金额
    private String pay_time;// 支付的时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getOrder_nu() {
        return order_nu;
    }

    public void setOrder_nu(String order_nu) {
        this.order_nu = order_nu;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getGoods_pic() {
        return goods_pic;
    }

    public void setGoods_pic(String goods_pic) {
        this.goods_pic = goods_pic;
    }

    public int getGoods_xjprice() {
        return goods_xjprice;
    }

    public void setGoods_xjprice(int goods_xjprice) {
        this.goods_xjprice = goods_xjprice;
    }

    public String getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(String goods_num) {
        this.goods_num = goods_num;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getPay_status() {
        return pay_status;
    }

    public void setPay_status(int pay_status) {
        this.pay_status = pay_status;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public int getPay_money() {
        return pay_money;
    }

    public void setPay_money(int pay_money) {
        this.pay_money = pay_money;
    }

    public String getPay_time() {
        return pay_time;
    }

    public void setPay_time(String pay_time) {
        this.pay_time = pay_time;
    }

}
