package com.fox.exercise.location;

import java.io.Serializable;

public class SportLocationInfo implements Serializable {


    /**
     *
     */
    private static final long serialVersionUID = 4196822462575671043L;

    private static SportLocationInfo mInstance = null;
    private static Object lock = new Object();

    public static SportLocationInfo getInstance() {
        if (mInstance == null) {
            synchronized (lock) {
                if (mInstance == null) {
                    mInstance = new SportLocationInfo();
                    return mInstance;
                }
            }
            return mInstance;
        }
        return mInstance;
    }

    private String latitude = null;
    private String longitude = null;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

}
