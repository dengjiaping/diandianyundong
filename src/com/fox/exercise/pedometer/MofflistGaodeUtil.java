package com.fox.exercise.pedometer;

import java.util.List;

import com.amap.api.maps.offlinemap.OfflineMapCity;

public class MofflistGaodeUtil {
    private String cityName;
    private int completeCode;
    private String completeCodeStr;
    private List<OfflineMapCity> cities;
    private int state;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getCompleteCodeStr() {
        return completeCodeStr;
    }

    public void setCompleteCodeStr(String completeCodeStr) {
        this.completeCodeStr = completeCodeStr;
    }

    public List<OfflineMapCity> getCities() {
        return cities;
    }

    public void setCities(List<OfflineMapCity> cities) {
        this.cities = cities;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCompleteCode() {
        return completeCode;
    }

    public void setCompleteCode(int completeCode) {
        this.completeCode = completeCode;
    }
}
