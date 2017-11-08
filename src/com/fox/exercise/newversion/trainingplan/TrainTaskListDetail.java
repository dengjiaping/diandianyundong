package com.fox.exercise.newversion.trainingplan;

public class TrainTaskListDetail {

    private int uid;                    //用户id
    private int train_id;               //训练id
    private int traintime;              //训练时间/秒
    private double train_calorie;          //训练消耗的卡路里
    private String train_action;           //训练动作的id字符串以,分割
    private String train_position;         //训练部位
    private int train_completion;       //训练完成度：如80%传80
    private String train_starttime;        //训练开始时间格式2016-01-01 01：01：01
    private String train_endtime;          //训练结束时间格式2016-01-01 01：01：02
    private int is_total;               //是否是训练的总成绩：1表示是，0表示是某个动作的成绩
    private int is_upload;              //是否已上传标志：1表示已上传，0表示未上传
    private String unique_id;              // 该成绩的唯一标识码，用于区分不同过的训练成绩最长32为

    public TrainTaskListDetail() {

    }

    ;

    public TrainTaskListDetail(int uid, int train_id, int traintime, double train_calorie,
                               String train_action, String train_position, int train_completion,
                               String train_starttime, String train_endtime, int is_total,
                               int is_upload, String unique_id) {

        this.uid = uid;
        this.train_id = train_id;
        this.traintime = traintime;
        this.train_calorie = train_calorie;
        this.train_action = train_action;
        this.train_position = train_position;
        this.train_completion = train_completion;
        this.train_starttime = train_starttime;
        this.train_endtime = train_endtime;
        this.is_total = is_total;
        this.is_upload = is_upload;
        this.unique_id = unique_id;
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
     * @return the train_id
     */
    public int getTrain_id() {
        return train_id;
    }

    /**
     * @param train_id the train_id to set
     */
    public void setTrain_id(int train_id) {
        this.train_id = train_id;
    }

    /**
     * @return the traintime
     */
    public int getTraintime() {
        return traintime;
    }

    /**
     * @param traintime the traintime to set
     */
    public void setTraintime(int traintime) {
        this.traintime = traintime;
    }

    /**
     * @return the train_calorie
     */
    public double getTrain_calorie() {
        return train_calorie;
    }

    /**
     * @param train_calorie the train_calorie to set
     */
    public void setTrain_calorie(double train_calorie) {
        this.train_calorie = train_calorie;
    }

    /**
     * @return the train_action
     */
    public String getTrain_action() {
        return train_action;
    }

    /**
     * @param train_action the train_action to set
     */
    public void setTrain_action(String train_action) {
        this.train_action = train_action;
    }

    /**
     * @return the train_position
     */
    public String getTrain_position() {
        return train_position;
    }

    /**
     * @param train_position the train_position to set
     */
    public void setTrain_position(String train_position) {
        this.train_position = train_position;
    }

    /**
     * @return the train_completion
     */
    public int getTrain_completion() {
        return train_completion;
    }

    /**
     * @param train_completion the train_completion to set
     */
    public void setTrain_completion(int train_completion) {
        this.train_completion = train_completion;
    }

    /**
     * @return the train_starttime
     */
    public String getTrain_starttime() {
        return train_starttime;
    }

    /**
     * @param train_starttime the train_starttime to set
     */
    public void setTrain_starttime(String train_starttime) {
        this.train_starttime = train_starttime;
    }

    /**
     * @return the train_endtime
     */
    public String getTrain_endtime() {
        return train_endtime;
    }

    /**
     * @param train_endtime the train_endtime to set
     */
    public void setTrain_endtime(String train_endtime) {
        this.train_endtime = train_endtime;
    }

    /**
     * @return the is_total
     */
    public int getIs_total() {
        return is_total;
    }

    /**
     * @param is_total the is_total to set
     */
    public void setIs_total(int is_total) {
        this.is_total = is_total;
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


    /**
     * @return the unique_id
     */
    public String getUnique_id() {
        return unique_id;
    }

    /**
     * @param unique_id the unique_id to set
     */
    public void setUnique_id(String unique_id) {
        this.unique_id = unique_id;
    }
}