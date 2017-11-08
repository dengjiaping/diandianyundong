package com.fox.exercise.location;

import org.json.JSONException;
import org.json.JSONObject;

public class LocationJsonParse {

    private String mRes = "";

    public LocationJsonParse(String res) {
        this.mRes = res;
    }

    public SportLocationInfo parse() {

        try {
            JSONObject original = new JSONObject(mRes);

            JSONObject content = original.getJSONObject("content");

            JSONObject point = content.getJSONObject("point");

            SportLocationInfo info = SportLocationInfo.getInstance();
            info.setLatitude(point.getString("y"));
            info.setLongitude(point.getString("x"));

            return info;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

    }

    public String getLocateName() {
        try {
            JSONObject original = new JSONObject(mRes);
            JSONObject content = original.getJSONObject("content");
            JSONObject point = content.getJSONObject("addr");
            return point.getString("detail");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
