package com.fox.exercise.newversion.bushutongji;

public class BuShuTongJiDetail {

    private int uid;                 //用户id
    private int id;                     //记录id
    private int step_num;            //步数
    private double distance;         //距离公里数
    private double step_Calorie;     //卡路里
    private String day;              //日期 格式”2015-12-23”
    private int is_upload;           //是否已上传标志：1表示已上传，0表示未上传

    public BuShuTongJiDetail(int uid, int id, int step_num, double distance,
                             double step_Calorie, String day, int is_upload) {
        this.uid = uid;
        this.id = id;
        this.step_num = step_num;
        this.distance = distance;
        this.step_Calorie = step_Calorie;
        this.day = day;
        this.is_upload = is_upload;
    }

    /**
     * @return the uid
     */
    public int getUid() {
        return uid;
    }

    /**
     * @param uid the uid to set
     */
    public void setUid(int uid) {
        this.uid = uid;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the step_num
     */
    public int getStep_num() {
        return step_num;
    }

    /**
     * @param step_num the step_num to set
     */
    public void setStep_num(int step_num) {
        this.step_num = step_num;
    }

    /**
     * @return the distance
     */
    public double getDistance() {
        return distance;
    }

    /**
     * @param distance the distance to set
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }

    /**
     * @return the step_Calorie
     */
    public double getStep_Calorie() {
        return step_Calorie;
    }

    /**
     * @param step_Calorie the step_Calorie to set
     */
    public void setStep_Calorie(double step_Calorie) {
        this.step_Calorie = step_Calorie;
    }

    /**
     * @return the day
     */
    public String getDay() {
        return day;
    }

    /**
     * @param day the day to set
     */
    public void setDay(String day) {
        this.day = day;
    }

    /**
     * @return the is_upload
     */
    public int getIs_upload() {
        return is_upload;
    }

    /**
     * @param is_upload the is_upload to set
     */
    public void setIs_upload(int is_upload) {
        this.is_upload = is_upload;
    }
}