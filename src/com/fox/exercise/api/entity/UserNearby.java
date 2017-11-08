package com.fox.exercise.api.entity;

public class UserNearby {
    private int id;
    private String name;
    private String img;
    private int distance;
    private long time;
    //关注状态  1——已关注   2——未关注
    private int followStatus;
    //man——男   woman——女
    private String sex;
    private String birthday;

    private double lat;
    private double lng;

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getFollowStatus() {
        return followStatus;
    }

    public void setFollowStatus(int followStatus) {
        this.followStatus = followStatus;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "UserNearby [id=" + id + ", name=" + name + ", img=" + img
                + ", distance=" + distance + ", time=" + time
                + ", followStatus=" + followStatus + ", sex=" + sex
                + ", birthday=" + birthday + ", lat=" + lat + ", lng=" + lng + "]";
    }


}
