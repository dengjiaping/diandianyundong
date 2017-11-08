package com.fox.exercise.login;

public class Item {
    String name;
    String version;
    int resId;
    long flag;
    int messageNum;

    public Item(String name, String version, int resId, long flag, int messageNum) {
        super();
        this.name = name;
        this.version = version;
        this.resId = resId;
        this.flag = flag;
        this.messageNum = messageNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public long getFlag() {
        return flag;
    }

    public void setFlag(long flag) {
        this.flag = flag;
    }

    public int getMessageNum() {
        return messageNum;
    }

    public void setMessageNum(int messageNum) {
        this.messageNum = messageNum;
    }

}
