package com.fox.exercise;

import org.json.JSONException;
import org.json.JSONObject;

import com.fox.exercise.login.LocationInfo;

public class LocationJsonParse {

    private String mRes = "";

    public LocationJsonParse(String res) {
        this.mRes = res;
    }

    public void parse() {

        try {
            JSONObject original = new JSONObject(mRes);

            JSONObject content = original.getJSONObject("content");

            JSONObject point = content.getJSONObject("point");

            LocationInfo.latitude = point.getString("x");
            LocationInfo.longitude = point.getString("y");

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
