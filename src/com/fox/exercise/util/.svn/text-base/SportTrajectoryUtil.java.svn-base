package com.fox.exercise.util;

import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.model.LatLng;

import android.util.Log;


public class SportTrajectoryUtil {

    private static final String TAG = "SportTrajectoryUtil";

    private static final double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
    public static double INVALID_LATLNG = 0.0f;

    private static LatLng bd_encrypt(double gg_lat, double gg_lon, double dLat, double dLng) {
        if (dLat != 0.0f && dLng != 0.0f) {
            if (gg_lat == INVALID_LATLNG && gg_lon == INVALID_LATLNG) {
                return new LatLng(gg_lat, gg_lon);
            }
            double x = gg_lon, y = gg_lat;
            double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
            double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
            double bd_lon = z * Math.cos(theta) + 0.0065;
            double bd_lat = z * Math.sin(theta) + 0.006;
            return new LatLng(bd_lat, bd_lon);
        } else {
            return new LatLng(gg_lat, gg_lon);
        }
    }

    public static String listLatLngToString(List<LatLng> geoPoints) {
        StringBuilder builder = new StringBuilder();
        for (LatLng point : geoPoints) {
            builder.append((int) (point.latitude * 1E6)).append(",").append((int) (point.longitude * 1E6)).append(";");
            String content = builder.toString();
            Log.v(TAG, "LatLngToString latitude = " + point.latitude);
            Log.v(TAG, "LatLngToString longitude = " + point.longitude);
            Log.v(TAG, "LatLngToString content = " + content);
        }
        String content = builder.toString();
        if (content != null && content.length() > 0)
            return content.substring(0, content.length() - 1);
        else
            return "";
    }

    public static String LatLngToString(LatLng point) {
        StringBuilder builder = new StringBuilder();
        builder.append((int) (point.latitude * 1E6)).append(",").append((int) (point.longitude * 1E6)).append(";");
        String content = builder.toString();
        return content.substring(0, content.length() - 1);
    }

    public static List<LatLng> stringToLatLng(String content, double dLat, double dLng) {
        String[] latlngPointContents = content.split(";");
        List<LatLng> latlngPointList = new ArrayList<LatLng>();
        for (String tempContent : latlngPointContents) {
            String[] temp = tempContent.split(",");
            double lat = (double) (Integer.parseInt(temp[0]) / 1E6);
            double lng = (double) (Integer.parseInt(temp[1]) / 1E6);
            LatLng latlngPoint = bd_encrypt(lat, lng, dLat, dLng);
            latlngPointList.add(latlngPoint);
        }
        return latlngPointList;
    }

    public static LatLng stringToLatLngPoint(String content, double dLat, double dLng) {
        if (content != null) {
            String[] temp = content.split(",");
            double lat = (double) (Integer.parseInt(temp[0]) / 1E6);
            double lng = (double) (Integer.parseInt(temp[1]) / 1E6);
            LatLng latlngPoint = bd_encrypt(lat, lng, dLat, dLng);
            return latlngPoint;
        }
        return null;
    }

}
