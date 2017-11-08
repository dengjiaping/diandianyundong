package com.fox.exercise.util;

import com.amap.api.maps.model.LatLng;
import com.fox.exercise.newversion.entity.SportsMarkDis;

import java.util.ArrayList;
import java.util.List;

public class SportTrajectoryUtilGaode {

    private static final String TAG = "SportTrajectoryUtilGaode";

    private static final double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
    public static double INVALID_LATLNG = 0.0f;

    private static LatLng bd_decrypt(double bd_lat, double bd_lon, double dLat, double dLng) {
        if (dLat != 0.0f && dLng != 0.0f) {
            if (bd_lat == INVALID_LATLNG && bd_lon == INVALID_LATLNG) {
                return new LatLng(bd_lat, bd_lon);
            }
            double x = bd_lon - 0.0065, y = bd_lat - 0.006;
            double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
            double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
            double gg_lon = z * Math.cos(theta);
            double gg_lat = z * Math.sin(theta);
            return new LatLng(gg_lat, gg_lon);
        } else {
            return new LatLng(bd_lat, bd_lon);
        }
    }

    public static String listLatLngToString(List<LatLng> geoPoints) {
        StringBuilder builder = new StringBuilder();
        for (LatLng point : geoPoints) {
            builder.append((int) (point.latitude * 1E6)).append(",").append((int) (point.longitude * 1E6)).append(";");
            String content = builder.toString();
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
        try{
            for (String tempContent : latlngPointContents) {
                if(!"".equals(tempContent)){
                    String[] temp = tempContent.split(",");
                    if ((temp[0] !=null &&!"".equals(temp[0]))&&(temp[1] !=null &&!"".equals(temp[1]))){
                        double lat = (double) (Integer.parseInt(temp[0]) / 1E6);
                        double lng = (double) (Integer.parseInt(temp[1]) / 1E6);
                        LatLng latlngPoint = bd_decrypt(lat, lng, dLat, dLng);
                        latlngPointList.add(latlngPoint);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return latlngPointList;
    }

    public static LatLng stringToLatLngPoint(String content, double dLat, double dLng) {
        if (content != null) {
            String[] temp = content.split(",");
            double lat = (double) (Integer.parseInt(temp[0]) / 1E6);
            double lng = (double) (Integer.parseInt(temp[1]) / 1E6);
            LatLng latlngPoint = bd_decrypt(lat, lng, dLat, dLng);
            return latlngPoint;
        }
        return null;
    }

    public static String peiListToString(ArrayList<String> list){
        StringBuilder builder = new StringBuilder();
        for (int j = 0; j < list.size(); j++){
            builder.append(list.get(j)).append(",");
        }
        return  builder.toString();
    }

    public static List<String> strToList(String peiStr) {
        String[] split2 = peiStr.split(",");
        List<String> list = new ArrayList<String>();
        if (split2 != null) {

            for (int i = 0; i < split2.length; i++) {
                list.add(split2[i]);
            }
        }
        return list;
    }


    public static String listSportsMarkDisToString(ArrayList<SportsMarkDis> sportsMarkDisList) {
        StringBuilder builder = new StringBuilder();
        for (SportsMarkDis sportsMarkDis : sportsMarkDisList) {
            builder.append(sportsMarkDis.mLat+"").append(",").append(sportsMarkDis.mLng+"").append(",").append(sportsMarkDis.dis+"").append(";");
            String content = builder.toString();
        }
        String content = builder.toString();

        if (content != null && content.length() > 0){
            return content.substring(0, content.length() - 1);
        }else {
            return "";
        }

    }


    public static ArrayList<SportsMarkDis> stringToSportsMarkDis(String content) {
        ArrayList<SportsMarkDis> sportsMarkDises = new ArrayList<SportsMarkDis>();
        try {
            String[] latlngPointContents = content.split(";");
            SportsMarkDis sportsMarkDis;
            for (String tempContent : latlngPointContents) {
                sportsMarkDis=new SportsMarkDis();
                String[] temp = tempContent.split(",");
                double lat = Double.parseDouble(temp[0]);
                sportsMarkDis.mLat=lat;
                double lng =  Double.parseDouble(temp[1]);
                sportsMarkDis.mLng=lng;
                int dis=Integer.parseInt(temp[2]);
                sportsMarkDis.dis=dis;
                sportsMarkDises.add(sportsMarkDis);
            }
        }catch (Exception e){

        }
        return sportsMarkDises;
    }


}
