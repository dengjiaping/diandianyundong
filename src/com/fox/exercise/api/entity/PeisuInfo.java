package com.fox.exercise.api.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangjian on 2016/8/8.
 */
public class PeisuInfo {
    public String gongli;//运动的公里数/每公里
    public String peisu;//运动配速/每公里
    public String yongshi;//运动耗时/每公里
    public String onlyone;//运动唯一标识

    public int ishave;//是否有数据
    public double sprots_svgvelocity;//运动的平均速度
    public double sports_minvelocity;//最快配速

    public List<GetPeisu> listpeis;//配速的数组

    public int peisuid;//上传配速返回的id

    public int sport_distance;//运动的总公里数

    public int getIshave() {
        return ishave;
    }

    public void setIshave(int ishave) {
        this.ishave = ishave;
    }

    public int getSport_distance() {
        return sport_distance;
    }

    public void setSport_distance(int sport_distance) {
        this.sport_distance = sport_distance;
    }

    public int getPeisuid() {
        return peisuid;
    }

    public void setPeisuid(int peisuid) {
        this.peisuid = peisuid;
    }

    public double getSports_minvelocity() {
        return sports_minvelocity;
    }

    public void setSports_minvelocity(double sports_minvelocity) {
        this.sports_minvelocity = sports_minvelocity;
    }

    public List<GetPeisu> getListpeis() {
        return listpeis;
    }

    public void setListpeis(List<GetPeisu> listpeis) {
        this.listpeis = listpeis;
    }

    public double getSprots_svgvelocity() {
        return sprots_svgvelocity;
    }

    public void setSprots_svgvelocity(double sprots_svgvelocity) {
        this.sprots_svgvelocity = sprots_svgvelocity;
    }

//    public double getSport_alltime() {
//        return sport_alltime;
//    }
//
//    public void setSport_alltime(double sport_alltime) {
//        this.sport_alltime = sport_alltime;
//    }


    public String getOnlyone() {
        return onlyone;
    }

    public void setOnlyone(String onlyone) {
        this.onlyone = onlyone;
    }

    public String getGongli() {
        return gongli;
    }

    public void setGongli(String gongli) {
        this.gongli = gongli;
    }

    public String getPeisu() {
        return peisu;
    }

    public void setPeisu(String peisu) {
        this.peisu = peisu;
    }

    public String getYongshi() {
        return yongshi;
    }

    public void setYongshi(String yongshi) {
        this.yongshi = yongshi;
    }


}
