package com.fox.exercise.util;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import com.fox.exercise.R;
import com.fox.exercise.SportsType;
import com.fox.exercise.api.QQHealthTask;
import com.fox.exercise.api.QQHealthTask.QQHealthResult;
import com.fox.exercise.api.ShowCoinsDialog;
import com.fox.exercise.db.SportSubTaskDB;
import com.fox.exercise.pedometer.SportContionTaskDetail;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.ingenic.indroidsync.SportsApp;

public class SportTaskUtil {

    public static final String[] device = {"手机", "手表"};
    public static final int[] type = {R.string.walk_gps, R.string.run_gps,
            R.string.swim, R.string.mountain, R.string.golf,
            R.string.walk_race, R.string.cycling,
            R.string.tennis, R.string.badminton,
            R.string.football, R.string.table_tennis,
            R.string.rowing, R.string.skating, R.string.roller_skating,R.string.drive};
    public static final String[] swimDetail = {"自由泳", "蛙泳", "蝶泳", "仰泳"};
    public static final String[] walkDetail = {"户外(GPS测距)", "户内(步数测距)"};
    public static final int[][] detailType = {
            {R.string.walk_gps, R.string.walk_gps},
            {R.string.run_gps, R.string.run_gps},
            {R.string.swim_free, R.string.swim_frog, R.string.swim_fly, R.string.swim_face},
            {R.string.mountain}, {R.string.golf},
            {R.string.walk_race}, {R.string.cycling},
            {R.string.tennis}, {R.string.badminton},
            {R.string.football}, {R.string.table_tennis},
            {R.string.rowing}, {R.string.skating}, {R.string.roller_skating}};

    private final static double EARTH_RADIUS = 6378137.0;
    private static final String path1 = SportsApp.getContext().getFilesDir()
            .toString()
            + "/sportsCoins";
    private static final String path2 = Environment
            .getExternalStorageDirectory()
            + "/android/data/"
            + SportsApp.getContext().getPackageName()
            + ".zuimei"
            + "/.sportsCoins";

    private static double IMPERIAL_WALKING_FACTOR = 0.517;
    private static double IMPERIAL_RUNNING_FACTOR = 0.75031498;
    private static String sharePath1 = SportsApp.getContext().getFilesDir()
            .toString()
            + "/shareCoins";
    ;
    private static final String sharePath2 = Environment
            .getExternalStorageDirectory()
            + "/android/data/"
            + SportsApp.getContext().getPackageName()
            + ".zuimei"
            + "/.shareCoins";

    // 时间计数器，最多只能到99小时
    public static String showTimeCount(long time) {
        if (time >= 360000) {
            return "00:00:00";
        }
        String timeCount = "";
        long hourc = time / 3600;
        String hour = "0" + hourc;
        hour = hour.substring(hour.length() - 2, hour.length());

        long minuec = (time - hourc * 3600) / (60);
        String minue = "0" + minuec;
        minue = minue.substring(minue.length() - 2, minue.length());

        long secc = (time - hourc * 3600 - minuec * 60) / 1;
        String sec = "0" + secc;
        sec = sec.substring(sec.length() - 2, sec.length());
        timeCount = hour + ":" + minue + ":" + sec;
        return timeCount;
    }

    public static String showOtherTimeCount(long time) {
        if (time >= 360000) {
            return "00h00m00s";
        }
        String timeCount = "";
        long hourc = time / 3600;
        String hour = "0" + hourc;
        hour = hour.substring(hour.length() - 2, hour.length());

        long minuec = (time - hourc * 3600) / (60);
        String minue = "0" + minuec;
        minue = minue.substring(minue.length() - 2, minue.length());

        long secc = (time - hourc * 3600 - minuec * 60) / 1;
        String sec = "0" + secc;
        sec = sec.substring(sec.length() - 2, sec.length());
        timeCount = hour + "h" + minue + "m" + sec + "s";
        return timeCount;
    }

    //只显示分钟秒
    public static String showMsCount(long time) {
        if (time >= 360000) {
            return "00:00";
        }
        String timeCount = "";
        long hourc = time / 3600;
        String hour = "0" + hourc;
        hour = hour.substring(hour.length() - 2, hour.length());

        long minuec = (time - hourc * 3600) / (60);
        String minue = "0" + minuec;
        minue = minue.substring(minue.length() - 2, minue.length());

        long secc = (time - hourc * 3600 - minuec * 60) / 1;
        String sec = "0" + secc;
        sec = sec.substring(sec.length() - 2, sec.length());
        timeCount = minue + ":" + sec;
        return timeCount;
    }

    public static ArrayList<String> getBaseCalories(int type) {
        // 单位CM
        double pace = 0;
        boolean isRunning = type != 0;
        if (type == 0) {
            pace = (SportsApp.getInstance().getSportUser().getHeight() * 0.37);
        } else if (type == 1) {
            pace = (SportsApp.getInstance().getSportUser().getHeight() * 0.45);
        } else if (type == 2) {
            pace = 20;
        } else if (type == 3) {
            pace = (SportsApp.getInstance().getSportUser().getHeight() * 0.37);
            ;
        } else if (type == 4 || type == 9) {
            pace = 20;
        }
        int weight = SportsApp.getInstance().getSportUser().getWeight();
        weight = (weight == 0 ? 65 : weight);
        double baseCalories = ((weight * 2.2) * (isRunning ? IMPERIAL_RUNNING_FACTOR
                : IMPERIAL_WALKING_FACTOR))
                * pace / 100000.0;

        ArrayList<String> list = new ArrayList<String>();
        list.add(Double.toString(pace / 100));
        list.add(Double.toString(baseCalories));
        return list;
    }

    public static String getDoubleNum(double num) {
        DecimalFormat df = new DecimalFormat("0.000");
        String numStr = df.format(num);
        return numStr;
    }

    public static String getDoubleOneNum(double num) {
        DecimalFormat df = new DecimalFormat("0.0");
        String numStr = df.format(num);
        return numStr;
    }

    public static String getDoubleNumber(double num) {
        DecimalFormat df = new DecimalFormat("0.00");
        String numStr = df.format(num);
        return numStr;
    }

    public static float getFloatNum(float num) {
        float b = (float) (Math.round(num * 100));
        return b;
    }

    public static int getTypeName(int num) {
        return type[num];
    }

    public static String getTypeDetailName(int num) {
        return swimDetail[num];
    }

    public static String getDeviceName(int num) {
        return device[num];
    }

    public static int getDetailTypeName(int one, int second) {
        return detailType[one][second];
    }

    // 计算两点距离
    public static double gps2m(double lat_a, double lng_a, double lat_b,
                               double lng_b) {

        double radLat1 = (lat_a * Math.PI / 180.0);
        double radLat2 = (lat_b * Math.PI / 180.0);
        double a = radLat1 - radLat2;
        double b = (lng_a - lng_b) * Math.PI / 180.0;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;

        return s;
    }

    /*
     * 存储运动日期和获取的金币
     */
    public static void saveSportsCoinsToFile(String date, int coins, int uid) {
        saveCoinsToFile(path1, path2, date, coins, uid);
    }

    private static void saveCoinsToFile(String path1, String path2,
                                        String date, int coins, int uid) {
        // TODO Auto-generated method stub
        try {
            File file = new File(path1 + uid);
            File dir = file.getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (file.exists()) {
                file.delete();
            }
            if (!file.createNewFile()) {
                return;
            } else {
                FileWriter writer = new FileWriter(file);
                writer.write(date + "#" + coins);
                writer.flush();
                writer.close();
            }

            file = new File(path2 + uid);
            dir = file.getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (file.exists()) {
                file.delete();
            }
            if (!file.createNewFile()) {
                return;
            } else {
                FileWriter writer = new FileWriter(file);
                writer.write(date);
                writer.flush();
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*
     * 存储分享日期和当天分享的次数
     */
    public static void saveShareCoinsToFile(String time, int tShare, int uid) {
        // TODO Auto-generated method stub
        saveCoinsToFile(sharePath1, sharePath2, time, tShare, uid);
    }

    /*
     * 读取保存运动日期和获取的金币
     */
    public static String readSportsCoinsFromFile(int uid) {
        return readCoinsFromFile(path1, path2, uid);
    }

    private static String readCoinsFromFile(String path1, String path2,
                                            int uid) {
        // TODO Auto-generated method stub
        String coins = "";
        File f = null;
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        try {
            f = new File(path1 + uid);
            fis = new FileInputStream(f);
            bis = new BufferedInputStream(fis);
            byte[] buff = new byte[512];
            bis.read(buff);
            coins = new String(buff).trim();
            fis.close();
            bis.close();
            if (!"".equals(coins))
                return coins;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            f = new File(path2 + uid);
            fis = new FileInputStream(f);
            bis = new BufferedInputStream(fis);
            byte[] buff = new byte[512];
            bis.read(buff);
            coins = new String(buff).trim();
            fis.close();
            bis.close();
            if (!"".equals(coins))
                return coins;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return coins;
    }

    /*
     * 读取分享日期和当天分享的次数
     */
    public static String readShareCoinsFromFile(int uid) {
        // TODO Auto-generated method stub
        return readCoinsFromFile(sharePath1, sharePath2, uid);
    }

    public static Boolean getNormalRange(int typeId, double speed, int recLen) {
        // TODO Auto-generated method stub
        double maxSpeed = speed;
        switch (typeId) {
            case SportsType.TYPE_WALK:
                if (maxSpeed >= 35.0) {
                    return false;
                }
                break;
            case SportsType.TYPE_RUN:
                if (maxSpeed >= 35.0) {
                    return false;
                }
                break;
            case SportsType.TYPE_SWIM:
                if (maxSpeed >= 6.0) {
                    return false;
                }
                break;
            case SportsType.TYPE_CLIMBING:
                if (maxSpeed >= 35.0) {
                    return false;
                }
                break;
            case SportsType.TYPE_GOLF:
                if (maxSpeed >= 5.0) {
                    return false;
                }
                break;
            case SportsType.TYPE_WALKRACE:
                if (maxSpeed >= 18.0) {
                    return false;
                }
                break;
            case SportsType.TYPE_CYCLING:
//			if (maxSpeed >= 50.0) {
                if (maxSpeed >= 120.0) {
                    return false;
                }
                break;
            case SportsType.TYPE_TENNIS:
                if (recLen >= 4 * 3600) {
                    return false;
                }
                break;
            case SportsType.TYPE_BADMINTON:
                if (recLen >= 4 * 3600) {
                    return false;
                }
                break;
            case SportsType.TYPE_FOOTBALL:
                if (maxSpeed >= 18.0 || recLen >= 4 * 3600) {
                    return false;
                }
                break;
            case SportsType.TYPE_TABLETENNIS:
                if (recLen >= 4 * 3600) {
                    return false;
                }
                break;
            case SportsType.TYPE_ROWING:
                if (maxSpeed >= 14.4) {
                    return false;
                }
                break;
            case SportsType.TYPE_SKATING:
                if (maxSpeed >= 20.0) {
                    return false;
                }
                break;
            case SportsType.TYPE_ROLLERSKATING:
                if (maxSpeed >= 20.0) {
                    return false;
                }
                break;
        }
        return true;
    }

    public static void jump2CoinsDialog(Context context, String conis) {
        // TODO Auto-generated method stub
        Intent cIntent = new Intent(context, ShowCoinsDialog.class);
        cIntent.putExtra("message", conis);
        context.startActivity(cIntent);
    }

    //距离转换为步数
    public static String dis2step(double dis) {
        // TODO Auto-generated method stub
        return dis / 0.6 + "";
    }

    public static String date2seconds(String date) {
        // TODO Auto-generated method stub
        String re_time = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d;
        try {
            d = sdf.parse(date);
            long l = d.getTime();
            String str = String.valueOf(l);
            re_time = str.substring(0, 10);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return re_time;
    }

    /**
     * execute里的参数必须按:1 运动开始时间(时间戳,2 当天运动总时间 单位秒,3 当天消耗总热量 千卡,4 当天运动速度 米/秒 的顺序上传
     *
     * @param mContext
     * @param typeId           运动类型
     * @param sportGoal        运动目标
     * @param startTime        运动开始时间(yyyy-MM-dd HH:mm:ss)
     * @param startTimeSeconds 运动开始时间(时间戳）
     * @param mUid             用户uid
     */
    public static boolean send2QQ(Context mContext, int typeId,
                                  int sportGoal, String startTime, String startTimeSeconds, int mUid, QQHealthResult qResult) {
        if (mContext.getSharedPreferences("user_login_info", Context.MODE_PRIVATE).getString("weibotype", "").equals("qqzone")
                && (typeId == SportsType.TYPE_WALK || typeId == SportsType.TYPE_RUN || typeId == SportsType.TYPE_CYCLING)) {
            SportContionTaskDetail st = SportSubTaskDB.getInstance(mContext).getToadySportsByType(mUid, startTime.substring(0, 10), typeId);
            new QQHealthTask(mContext, typeId, st.getSportDistance() * 1000, sportGoal, startTime, qResult)
                    .execute(startTimeSeconds, st.getSportTime(), st.getSprots_Calorie() + "", (float) (Math.round(st.getSportVelocity() * 100)) / 100 + "");
            return true;
        }
        return false;
    }
}
