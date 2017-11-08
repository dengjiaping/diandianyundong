package com.fox.exercise.api.entity;

public class Gifts {
    private int id;
    private String giftPic;
    private int giftPrice;
    private String giftName;
    private int charmNo;
    private int wealthNo;

    public int getCharmNo() {
        return charmNo;
    }

    public void setCharmNo(int charmNo) {
        this.charmNo = charmNo;
    }

    public int getWealthNo() {
        return wealthNo;
    }

    public void setWealthNo(int wealthNo) {
        this.wealthNo = wealthNo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGiftPic() {
        return giftPic;
    }

    public void setGiftPic(String giftPic) {
        this.giftPic = giftPic;
    }

    public int getGiftPrice() {
        return giftPrice;
    }

    public void setGiftPrice(int giftPrice) {
        this.giftPrice = giftPrice;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    @Override
    public String toString() {
        return "Gifts [id=" + id + ", giftPic=" + giftPic + ", giftPrice="
                + giftPrice + ", giftName=" + giftName + ", charmNo=" + charmNo
                + ", wealthNo=" + wealthNo + "]";
    }


}
