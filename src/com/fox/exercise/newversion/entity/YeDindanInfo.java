package com.fox.exercise.newversion.entity;

public class YeDindanInfo {
    private int id;// id
    private String order_nu;// 订单号
    private int uid;// 用户id
    private int mlsid;// 活动id
    private String mls_name;// 活动名字
    private int mls_price;// 活动报名费
    private String user_name;// 用户名字
    private String create_time;// 创建订单的时间时间戳
    private int pay_status;// 支付订单状况1表示未支付，2表示支付成功
    private String transaction_id;// 微信支付的订单号未支付状态下值无
    private String opened;// 用户在我们app下的唯一表示码
    private int pay_money;// 支付的钱单位分
    private String pay_time;// 支付的时间时间戳
    private String goods_name;// 商品名
    private int goods_xjprice;// 商品现金价格
    private int gid;// 商品id

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public int getGoods_xjprice() {
        return goods_xjprice;
    }

    public void setGoods_xjprice(int goods_xjprice) {
        this.goods_xjprice = goods_xjprice;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

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

    public String getOpened() {
        return opened;
    }

    public void setOpened(String opened) {
        this.opened = opened;
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
