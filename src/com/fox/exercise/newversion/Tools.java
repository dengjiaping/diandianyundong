package com.fox.exercise.newversion;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.util.Log;

public class Tools {

    public static String formatDuring(long mss) {
        long days = mss / (1000 * 60 * 60 * 24);
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;
        long millisecondCutSingleDigit = (mss % 1000) / 10;

        String resultString = "";
        String daysString = "";
        String hourString = "";
        String mituneString = "";
        String secondString = "";
        String millisecondCutSingleDigitString = "";
        if (days <= 0) {
            daysString = "";
        } else {
            daysString = days + "";
        }
        if (hours <= 0) {
            hourString = "";
        } else if (hours < 10) {
            hourString = "0" + hours;
        } else {
            hourString = "" + hours;
        }
        if (minutes <= 0) {
            mituneString = "00";
        } else if (minutes < 10) {
            mituneString = "0" + minutes;
        } else {
            mituneString = "" + minutes;
        }
        if (seconds <= 0) {
            secondString = "00";
        } else if (seconds < 10) {
            secondString = "0" + seconds;
        } else {
            secondString = "" + seconds;
        }
        if (millisecondCutSingleDigit <= 0) {
            millisecondCutSingleDigitString = "00";
        } else if (millisecondCutSingleDigit < 10) {
            millisecondCutSingleDigitString = "0" + millisecondCutSingleDigit;
        } else {
            millisecondCutSingleDigitString = "" + millisecondCutSingleDigit;
        }

        if (days <= 0) {
            if (hours <= 0) {
                resultString = mituneString + ":" + secondString + ":"
                        + millisecondCutSingleDigitString;
            } else {
                resultString = hourString + ":" + mituneString + ":"
                        + secondString + ":" + millisecondCutSingleDigitString;
            }
        } else {
            resultString = daysString + "day" + "-" + hourString + ":"
                    + mituneString + ":" + secondString + ":"
                    + millisecondCutSingleDigitString;
        }

        return resultString;
    }

    public static String getTime(long mss) {
        long days = mss / (1000 * 60 * 60 * 24);
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;

        String resultString = "";
        String daysString = "";
        String hourString = "";
        String mituneString = "";
        String secondString = "";
        if (days <= 0) {
            daysString = "";
        } else {
            daysString = days + "";
        }
        if (hours <= 0) {
            hourString = "";
        } else if (hours < 10) {
            hourString = "0" + hours;
        } else {
            hourString = "" + hours;
        }
        if (minutes <= 0) {
            mituneString = "00";
        } else if (minutes < 10) {
            mituneString = "0" + minutes;
        } else {
            mituneString = "" + minutes;
        }
        if (seconds <= 0) {
            secondString = "00";
        } else if (seconds < 10) {
            secondString = "0" + seconds;
        } else {
            secondString = "" + seconds;
        }

        if (days <= 0) {
            if (hours <= 0) {
                resultString = mituneString + ":" + secondString;
            } else {
                resultString = hourString + ":" + mituneString + ":"
                        + secondString;
            }
        } else {
            resultString = daysString + "day" + "-" + hourString + ":"
                    + mituneString + ":" + secondString;
        }

        return resultString;
    }

    public static int getRadomNumber() {
        int randomInt = (int) (Math.random() * 50);
        return randomInt;
    }

    /**
     * ???????????
     *
     * @param date         ???? yyyy-MM-dd HH:mm:ss
     * @param strDateBegin ???? 00:00:00
     * @param strDateEnd   ???? 00:05:00
     * @return
     */
    public static boolean isInDate(Date date, String strDateBegin,
                                   String strDateEnd) {
        SimpleDateFormat formate = new SimpleDateFormat("yy-MM-dd HH:mm:ss");

        try {
            Date date1 = formate.parse(strDateBegin+" 00:00:00");
            Date date2 = formate.parse(strDateEnd+" 23:59:59");
            if (date.getTime() >= date1.getTime()
                    && date.getTime() <= date2.getTime()) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // String strDate = sdf.format(date);
        // // ?????????
        // int strDateH = Integer.parseInt(strDate.substring(11, 13));
        // int strDateM = Integer.parseInt(strDate.substring(14, 16));
        // int strDateS = Integer.parseInt(strDate.substring(17, 19));
        // // ?????????
        // int strDateBeginH = Integer.parseInt(strDateBegin.substring(0, 2));
        // int strDateBeginM = Integer.parseInt(strDateBegin.substring(3, 5));
        // int strDateBeginS = Integer.parseInt(strDateBegin.substring(6, 8));
        // // ?????????
        // int strDateEndH = Integer.parseInt(strDateEnd.substring(0, 2));
        // int strDateEndM = Integer.parseInt(strDateEnd.substring(3, 5));
        // int strDateEndS = Integer.parseInt(strDateEnd.substring(6, 8));
        // if ((strDateH >= strDateBeginH && strDateH <= strDateEndH)) {
        // // ??????????????????????
        // if (strDateH > strDateBeginH && strDateH < strDateEndH) {
        // return true;
        // // ????????????????,???????????
        // } else if (strDateH == strDateBeginH && strDateM >= strDateBeginM
        // && strDateM <= strDateEndM) {
        // return true;
        // // ????????????????,????????????,??????????
        // } else if (strDateH == strDateBeginH && strDateM == strDateBeginM
        // && strDateS >= strDateBeginS && strDateS <= strDateEndS) {
        // return true;
        // }
        // // ?????????????????,?????????,?????????????
        // else if (strDateH >= strDateBeginH && strDateH == strDateEndH
        // && strDateM <= strDateEndM) {
        // return true;
        // // ?????????????????,?????????,????????????,???????????
        // } else if (strDateH >= strDateBeginH && strDateH == strDateEndH
        // && strDateM == strDateEndM && strDateS <= strDateEndS) {
        // return true;
        // } else {
        // return false;
        // }
        // } else {
        // return false;
        // }
    }

    //?ping?????????????,???????pingIP("180.97.33.107"),ping????3,???????
    public static boolean pingIp(String address) {
        Process p;
        int status = -1;
        try {
            p = Runtime.getRuntime().exec("/system/bin/ping -c 3 " + address);
            status = p.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        Log.i("pingIp", "pingIp" + status);
        if (status == 0) {
            return true;
        } else {
            return false;
        }

    }



    public static boolean isTanchu(String starttime1, String starttime2,
                                   String endtime1, String endtime2, Date cruDate, int n) {
        boolean isBoo = false;
        Timestamp time1 = null;
        Timestamp time2 = null;
        Timestamp time3 = Timestamp.valueOf(endtime1);
        Timestamp time4 = Timestamp.valueOf(endtime2);
        ArrayList<Timestamp> list4 = new ArrayList<Timestamp>();
        ArrayList<Timestamp> list5 = new ArrayList<Timestamp>();
        for (int i = 0; i < 1000; i = i + n) {
            time1 = Timestamp.valueOf(starttime1);
            time2 = Timestamp.valueOf(starttime2);
            long l = time1.getTime() + 24 * 60 * 60 * i * 1000;
            // long l = time.getTime() + 10*60*m*1000;
            time1.setTime(l);
            long l1 = time2.getTime() + 24 * 60 * 60 * i * 1000;
            // long l = time.getTime() + 10*60*m*1000;
            time2.setTime(l1);
            if (time1.getTime() <= time3.getTime()) {
                list4.add(time1);
            } else {
                break;
            }
            if (time2.getTime() <= time4.getTime()) {
                list5.add(time2);
            }

        }

        if (list4.size() == 0) {
            isBoo = false;
        } else {
            if (list4.size() != 0 && list5.size() != 0&&list4.size()==list5.size()) {
                for (int i = 0; i < list4.size(); i++) {
                    if (cruDate.getTime() >= list4.get(i).getTime()
                            && cruDate.getTime() <= list5.get(i).getTime()) {
                        isBoo = true;
                    }
                }
            }

        }

        return isBoo;
    }
}
