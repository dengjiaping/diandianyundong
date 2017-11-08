package com.fox.exercise.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.fox.exercise.R;
import com.fox.exercise.api.entity.SportTask;
import com.fox.exercise.newversion.entity.SportRecord;
import com.fox.exercise.pedometer.SportContionTaskDetail;
import com.fox.exercise.util.SportTaskUtil;

import java.util.ArrayList;

public class SportSubTaskDB extends SQLiteOpenHelper {
    private Context context;
    private SQLiteDatabase db = null;
    private static SportSubTaskDB mInstance = null;
    public final static String DB_NAME = "sport_task_db.db";
    public final static String TAB_NAME = "sport_sub";
    public static final String TABLE_NAME_2 = "sport_sub2";
    public final static String ID = "_id";
    public final static String UID = "uid";
    public final static String LIMIT = "taskId"; // 第几次任务
    public final static String SPORT_TYPE = "sport_type"; // 运动类型
    public final static String SPORT_SWIM_TYPE = "sport_swim_type"; // 游泳类型
    public final static String SPORT_DEVICE = "sport_device"; // 监测设备
    public final static String SPORT_START_TIME = "sport_start_time"; // 运动开始时间
    public final static String SPORT_TIME = "sport_time";// 运动用时
    public final static String SPORT_DISTANCE = "sport_distance"; // 运动距离
    public final static String SPORT_SPEED = "sport_speed";// 运动速度
    public final static String SPORT_CALORIES = "sport_calories";// 消耗卡路里
    public final static String SPORT_HEART_RATE = "sport_heart_rate";// 心率
    public final static String SPORT_LAT_LNG = "sport_lat_lng";// 经纬度数组
    public final static String SPORT_ISUPLOAD = "sport_isupload";// 是否上传到服务器
    public final static String SPORT_DATE = "sport_date";// 运动日期
    public final static String SPORT_TASKID = "sport_taskid";// task表中的id
    public final static String SPORT_STEP = "sport_step";// 运动步数
    public final static String SPORT_MAPTYPE = "sport_map_type";// 运动步数

    public final static String SPORT_LASTRUNTIME = "sport_last_runtime";// 最后运动日期
    public final static String SPORT_ALLDAYS = "sport_alldays";// 运动天数

    public final static String SPORT_MARKCODE = "sport_markcode";// 运动唯一标示码
    public final  static  String SPORT_SPEEDLIST="sport_speedlist";//速度列表
    public final  static  String SPORT_MARKLIST="coordinate_list";//整公里是的gps点

    public String CREATE_TAB = "create table if not exists " + TAB_NAME
            + " (_id integer primary key autoincrement," + UID + " integer,"
            + SPORT_TYPE + " integer," + SPORT_SWIM_TYPE + " integer,"
            + SPORT_DEVICE + " integer," + SPORT_START_TIME + " text,"
            + SPORT_TIME + " integer," + SPORT_DISTANCE + " text,"
            + SPORT_SPEED + " text," + SPORT_CALORIES + " text,"
            + SPORT_HEART_RATE + " text," + SPORT_LAT_LNG + " text,"
            + SPORT_ISUPLOAD + " integer," + SPORT_TASKID + " integer,"
            + SPORT_DATE + " text," + SPORT_STEP + " integer," + LIMIT
            + " integer," + SPORT_MAPTYPE + " integer," + SPORT_MARKCODE
            +" text," + SPORT_SPEEDLIST+ " text,"+SPORT_MARKLIST+ " text)";
    private String TAG = "SportSubTaskDB";

    public synchronized static SportSubTaskDB getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SportSubTaskDB(context);
        }
        return mInstance;
    }

    private SportSubTaskDB(Context context) {
        super(context, DB_NAME, null, 5);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        db.execSQL(CREATE_TAB);
    }

    public Cursor query(int uid, String start_time) {
        if (db == null) {
            db = getWritableDatabase();
        }
        return db
                .rawQuery(
                        "select * from sport_sub where (uid=0 or uid=?)and sport_start_time=? ",
                        new String[]{Integer.toString(uid), start_time});
    }

    public Cursor newquery(int uid, String start_time, String sport_time,
                           String sport_distance) {
        if (db == null) {
            db = getWritableDatabase();
        }
        return db
                .rawQuery(
                        "select * from sport_sub where (uid=0 or uid=?)and sport_start_time=? and sport_time=? and sport_distance=?",
                        new String[]{Integer.toString(uid), start_time,
                                sport_time, sport_distance});
    }

    public int insert(ContentValues values, Boolean isShow) {
        if (db == null) {
            db = getWritableDatabase();
        }
        int id = (int) db.insert(TAB_NAME, null, values);
        if (id > 0) {
            Log.i("数据库插入", "插入数据成功，id=" + id);
            if (isShow) {
                Toast.makeText(
                        context,
                        context.getResources().getString(
                                R.string.sports_save_local_success),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.i("数据库插入", "插入数据失败，id=" + id);
            if (isShow) {
                Toast.makeText(
                        context,
                        context.getResources().getString(
                                R.string.sports_save_local_failed),
                        Toast.LENGTH_SHORT).show();
            }
        }
        return id;
    }

    public void update(ContentValues values, int taskid) {
        if (db == null) {
            db = getWritableDatabase();
        }
        long id = db.update(TAB_NAME, values, "sport_taskid=?",
                new String[]{Integer.toString(taskid)});
        if (id > 0) {
            // Toast.makeText(context,
            // context.getResources().getString(R.string.sports_save_local_success),
            // Toast.LENGTH_SHORT).show();
            Log.i("数据库更新", "更新数据成功，id=" + id);
        } else {
            // Toast.makeText(context,
            // context.getResources().getString(R.string.sports_save_local_failed),
            // Toast.LENGTH_SHORT).show();
            Log.i("数据库更新", "更新数据失败，id=" + id);
        }
    }

    public void update(ContentValues values, String start_time, int uid) {
        if (db == null) {
            db = getWritableDatabase();
        }
        long id = db.update(TAB_NAME, values, "(uid=0 or uid=?) and sport_start_time=?",
                new String[]{Integer.toString(uid), start_time});
        if (id > 0) {
            // Toast.makeText(context,
            // context.getResources().getString(R.string.sports_save_local_success),
            // Toast.LENGTH_SHORT).show();
            Log.i("数据库更新", "更新数据成功，id=" + id);
        } else {
            // Toast.makeText(context,
            // context.getResources().getString(R.string.sports_save_local_failed),
            // Toast.LENGTH_SHORT).show();
            Log.i("数据库更新", "更新数据失败，id=" + id);
        }
    }

    // new update
    public void newUpdate(ContentValues values, String start_time,
                          String markcode, int uid) {
        if (db == null) {
            db = getWritableDatabase();
        }
        long id = db.update(TAB_NAME, values,
                "(uid=0 or uid=?) and sport_start_time=? and sport_markcode=?", new String[]{
                        Integer.toString(uid), start_time, markcode});
        if (id > 0) {
            // Toast.makeText(context,
            // context.getResources().getString(R.string.sports_save_local_success),
            // Toast.LENGTH_SHORT).show();
            Log.i("数据库更新", "更新数据成功，id=" + id);
        } else {
            // Toast.makeText(context,
            // context.getResources().getString(R.string.sports_save_local_failed),
            // Toast.LENGTH_SHORT).show();
            Log.i("数据库更新", "更新数据失败，id=" + id);
        }
    }

    // 每一分钟更新一次数据
    public int update(ContentValues values, int uid, String start_time,
                      Boolean isShow, String markCode) {
        if (db == null) {
            db = getWritableDatabase();
        }
        long id = db.update(TAB_NAME, values,
                "uid=? and sport_start_time=? and sport_markcode=? ",
                new String[]{Integer.toString(uid), start_time, markCode});
        if (id > 0) {
            if (isShow) {
                Toast.makeText(
                        context,
                        context.getResources().getString(
                                R.string.sports_save_local_success),
                        Toast.LENGTH_SHORT).show();
            }
            Log.i("数据库更新", "更新数据成功，id=" + id);
        } else {
            if (isShow) {
                Toast.makeText(
                        context,
                        context.getResources().getString(
                                R.string.sports_save_local_failed),
                        Toast.LENGTH_SHORT).show();
            }
            Log.i("数据库更新", "更新数据失败，id=" + id);
        }
        return (int) id;
    }

    // 每一分钟数据删除
    public void delete(int uid, String start_time) {
        if (db == null) {
            db = getWritableDatabase();
        }
        int id = db.delete(TAB_NAME, "uid=? and sport_start_time=?",
                new String[]{Integer.toString(uid), start_time});
    }

    public boolean delete(int taskid) {
        boolean result = true;
        if (db == null) {
            db = getWritableDatabase();
        }
        int id = db.delete(TAB_NAME, "sport_taskid=?",
                new String[]{Integer.toString(taskid)});

        if (id > 0) {
            Log.i("数据库删除", "删除数据成功，id=" + id);
            result = true;
        } else {
            Log.i("数据库删除", "删除数据失败，id=" + id);
            result = false;
        }
        return result;
    }

    public boolean deleteSportByStartTime(String startTime) {
        boolean result = true;
        if (db == null) {
            db = getWritableDatabase();
        }
        int id = db.delete(TAB_NAME, "sport_start_time=?",
                new String[]{startTime});

        if (id > 0) {
            Log.i("数据库删除", "删除数据成功，id=" + id);
            result = true;
        } else {
            Log.i("数据库删除", "删除数据失败，id=" + id);
            result = false;
        }
        return result;
    }

    public boolean deleteSportByMarkCode(String sport_markcode) {
        boolean result = true;
        if (db == null) {
            db = getWritableDatabase();
        }
        int id = db.delete(TAB_NAME, "sport_markcode=?",
                new String[]{sport_markcode});

        if (id > 0) {
            Log.i("数据库删除", "删除数据成功，id=" + id);
            result = true;
        } else {
            Log.i("数据库删除", "删除数据失败，id=" + id);
            result = false;
        }
        return result;
    }

    public int getTasksCount(int uid, String sportDate) {
        Log.v(TAG, "getTasksCount uid is " + uid + " sportDateis " + sportDate);
        int result = 0;
        if (db == null) {
            db = getReadableDatabase();
        }
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(
                    "select * from sport_sub where uid=? and sport_date=?",
                    new String[]{Integer.toString(uid), sportDate});
            result = cursor.getCount();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
        Log.v(TAG, "getTasksCount result is " + result);
        return result;
    }

    public ArrayList<SportContionTaskDetail> getTasksList(int uid,
                                                          String sportDate) {
        if (db == null) {
            db = getReadableDatabase();
        }
        ArrayList<SportContionTaskDetail> sports = null;
        Cursor cursor = null;
        try {
            cursor = db
                    .rawQuery(
                            "select * from sport_sub where (uid=0 or uid=?) and sport_date=? order by _id desc",
                            new String[]{Integer.toString(uid), sportDate});
            if (cursor.moveToFirst()) {
                sports = new ArrayList<SportContionTaskDetail>();
                do {
                    String time = cursor.getString(cursor
                            .getColumnIndex(SPORT_START_TIME));
                    Log.e("wmh", "time is " + time);
                    sports.add(getTaskByTime(uid, time));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
        return sports;
    }

    public ArrayList<SportContionTaskDetail> getTasks(int uid, String sportDate) {
        if (db == null) {
            db = getReadableDatabase();
        }
        ArrayList<SportContionTaskDetail> sports = new ArrayList<SportContionTaskDetail>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(
                    "select * from sport_sub where uid=? and sport_date=?",
                    new String[]{Integer.toString(uid), sportDate});
            if (cursor.moveToFirst()) {
                do {
                    SportContionTaskDetail sport = new SportContionTaskDetail();
                    sport.setTimes(cursor.getString(
                            cursor.getColumnIndex("sport_start_time"))
                            .substring(10));
                    sport.setStartTime(cursor.getString(cursor
                            .getColumnIndex("sport_start_time")));
                    sport.setTaskid(cursor.getInt(cursor
                            .getColumnIndex("sport_taskid")));
                    sport.setLatlng(cursor.getString(cursor
                            .getColumnIndex("sport_lat_lng")));
                    sport.setSports_type(cursor.getInt(cursor
                            .getColumnIndex("sport_type")));
                    sport.setSprots_Calorie(cursor.getInt(cursor
                            .getColumnIndex("sport_calories")));
                    sports.add(sport);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
        return sports;
    }

    public ArrayList<SportContionTaskDetail> getTasksPhone(int uid,
                                                           String sportDate) {
        if (db == null) {
            db = getReadableDatabase();
        }
        Log.e("SportSubTaskDB", "sportDate is " + sportDate);
        ArrayList<SportContionTaskDetail> sports = null;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(
                    "select * from sport_sub where uid=? and sport_date=?",
                    new String[]{Integer.toString(uid), sportDate});
            if (cursor.moveToFirst()) {
                do {
                    SportContionTaskDetail sport = new SportContionTaskDetail();
                    int task_id = cursor.getInt(cursor
                            .getColumnIndex("sport_taskid"));
                    Log.e("SportSubTaskDB", "task_id is " + task_id);
                    SportContionTaskDetail taskDetail = getTaskByTaskID(uid,
                            task_id);
                    if (taskDetail.getMonitoringEquipment() == 0) {
                        if (sports == null)
                            sports = new ArrayList<SportContionTaskDetail>();
                        sport.setTimes(cursor.getString(
                                cursor.getColumnIndex("sport_start_time"))
                                .substring(10));
                        sport.setStartTime(cursor.getString(cursor
                                .getColumnIndex("sport_start_time")));
                        sport.setTaskid(cursor.getInt(cursor
                                .getColumnIndex("sport_taskid")));
                        sport.setLatlng(cursor.getString(cursor
                                .getColumnIndex("sport_lat_lng")));
                        sport.setSports_type(cursor.getInt(cursor
                                .getColumnIndex("sport_type")));
                        sport.setSprots_Calorie(cursor.getInt(cursor
                                .getColumnIndex("sport_calories")));
                        sports.add(taskDetail);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
        return sports;
    }

    public ArrayList<SportContionTaskDetail> getTasksWatch(int uid,
                                                           String sportDate) {
        if (db == null) {
            db = getReadableDatabase();
        }
        ArrayList<SportContionTaskDetail> sports = null;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(
                    "select * from sport_sub where uid=? and sport_date=?",
                    new String[]{Integer.toString(uid), sportDate});
            if (cursor.moveToFirst()) {
                do {
                    SportContionTaskDetail sport = new SportContionTaskDetail();
                    int task_id = cursor.getInt(cursor
                            .getColumnIndex("sport_taskid"));
                    SportContionTaskDetail taskDetail = getTaskByTaskID(uid,
                            task_id);
                    if (taskDetail.getMonitoringEquipment() == 1) {
                        if (sports == null)
                            sports = new ArrayList<SportContionTaskDetail>();
                        sport.setTimes(cursor.getString(
                                cursor.getColumnIndex("sport_start_time"))
                                .substring(10));
                        sport.setStartTime(cursor.getString(cursor
                                .getColumnIndex("sport_start_time")));
                        sport.setTaskid(cursor.getInt(cursor
                                .getColumnIndex("sport_taskid")));
                        sport.setLatlng(cursor.getString(cursor
                                .getColumnIndex("sport_lat_lng")));
                        sport.setSports_type(cursor.getInt(cursor
                                .getColumnIndex("sport_type")));
                        sport.setSprots_Calorie(cursor.getInt(cursor
                                .getColumnIndex("sport_calories")));
                        sports.add(sport);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
        return sports;
    }

    // ////只按UID查到本地历史记录
    public ArrayList<SportContionTaskDetail> getTasks(int uid, int times) {
        if (db == null) {
            db = getReadableDatabase();
        }
        ArrayList<SportContionTaskDetail> sports = new ArrayList<SportContionTaskDetail>();
        Cursor cursor = null;
        try {
            if (times == 0){
                cursor = db
                        .rawQuery(
                                "select * from sport_sub where (uid=0 or uid=?) order by sport_start_time desc limit ?,?",
                                new String[]{Integer.toString(uid),
                                        Integer.toString(times * 16),
                                        Integer.toString(16)});
            }else{
                cursor = db
                        .rawQuery(
                                "select * from sport_sub where uid=? order by sport_start_time desc limit ?,?",
                                new String[]{Integer.toString(uid),
                                        Integer.toString(times * 16),
                                        Integer.toString(16)});
            }

            if (cursor.moveToFirst()) {
                Log.v(TAG, "wmh get local list");
                do {
                    SportContionTaskDetail sport = new SportContionTaskDetail();

                    sport.setStartTime(cursor.getString(cursor
                            .getColumnIndex(SPORT_START_TIME)));
                    sport.setUserId(cursor.getInt(cursor.getColumnIndex(UID)));
                    sport.setSportDate(cursor.getString(cursor
                            .getColumnIndex(SPORT_DATE)));
                    sport.setSportTime(cursor.getString(cursor
                            .getColumnIndex(SPORT_TIME)));
                    sport.setSports_type(cursor.getInt(cursor
                            .getColumnIndex(SPORT_TYPE)));
                    sport.setSwimType(cursor.getInt(cursor
                            .getColumnIndex(SPORT_SWIM_TYPE)));
                    sport.setMonitoringEquipment(cursor.getInt(cursor
                            .getColumnIndex(SPORT_DEVICE)));
                    sport.setSportDistance(cursor.getDouble(cursor
                            .getColumnIndex(SPORT_DISTANCE)));
                    sport.setSprots_Calorie((int) cursor.getDouble(cursor
                            .getColumnIndex(SPORT_CALORIES)));
                    sport.setHeartRate(cursor.getDouble(cursor
                            .getColumnIndex(SPORT_HEART_RATE)));
                    sport.setLatlng(cursor.getString(cursor
                            .getColumnIndex(SPORT_LAT_LNG)));
                    sport.setTaskid(cursor.getInt(cursor.getColumnIndex(LIMIT)));
                    sport.setStepNum(cursor.getInt(cursor
                            .getColumnIndex(SPORT_STEP)));
                    sport.setSportVelocity(cursor.getDouble(cursor
                            .getColumnIndex(SPORT_SPEED)));
                    sport.setTaskid(cursor.getInt(cursor
                            .getColumnIndex(SPORT_TASKID)));
                    sport.setMapType(cursor.getInt(cursor
                            .getColumnIndex(SPORT_MAPTYPE)));
                    sport.setSport_isupload(cursor.getInt(cursor
                            .getColumnIndex(SPORT_ISUPLOAD)));
                    sport.setSport_speedList(cursor.getString(cursor
                            .getColumnIndex(SPORT_SPEEDLIST)));
                    sport.setCoordinate_list(cursor.getString(cursor
                            .getColumnIndex(SPORT_MARKLIST)));
                    sports.add(sport);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
        return sports;
    }

    // 根据uid查询获取最新的sport_start_time，再查询相应的运动详情(只显示最新的那一条数据)
    public SportContionTaskDetail getLastTask(int uid) {
        if (db == null) {
            db = getReadableDatabase();
        }
        SportContionTaskDetail sport = new SportContionTaskDetail();
        Cursor cursor = null;
        Cursor cs = null;
        try {
            cursor = db
                    .rawQuery(
                            "select sport_taskid,sport_start_time from sport_sub where uid=? order by sport_start_time desc",
                            new String[]{Integer.toString(uid)});
            if (cursor.moveToFirst()) {
                String start_time = cursor.getString(cursor
                        .getColumnIndex("sport_start_time"));
                Log.i("sport_sub", "sport_sub:" + start_time);// 格式:2014-08-12
                // 10:41:43

                cs = db.rawQuery(
                        "select sport_distance,sport_calories from sport_sub where sport_start_time=?",
                        new String[]{start_time});
                if (cs.moveToFirst()) {
                    sport.setSportDistance(cs.getDouble(cs
                            .getColumnIndex(SPORT_DISTANCE)));
                    sport.setSprots_Calorie((int) cs.getDouble(cs
                            .getColumnIndex(SPORT_CALORIES)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            if (cs != null) {
                try {
                    cs.close();
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
            }
        }
        return sport;
    }

    public SportContionTaskDetail getToadyHistorySports(int uid, String toady) {
        if (db == null) {
            db = getReadableDatabase();
        }

        Log.i("sport_sub", "toady:" + toady);

        double dis = 0.0;
        int cal = 0;
        int dur = 0;
        String start_time;

        SportContionTaskDetail sportContionTaskDetail = new SportContionTaskDetail();

        Cursor cursor = null;
        try {
            cursor = db
                    .rawQuery(
                            "select sport_taskid,sport_distance,sport_calories,sport_time,sport_start_time,sport_date from sport_sub where uid=? and sport_date=? order by sport_start_time desc",
                            new String[]{Integer.toString(uid), toady});
            if (cursor.moveToFirst()) {
                do {
                    start_time = cursor.getString(cursor
                            .getColumnIndex("sport_start_time"));
                    // 格式:2014-08-12 10:41:43
                    Log.i("sport_sub", "sport_sub_1:" + start_time);

                    start_time = start_time.substring(0, 10);
                    // 格式:2014-08-12
                    Log.i("sport_sub", "sport_sub2:" + start_time);

                    if (start_time.equals(toady)) {
                        dis += cursor.getDouble(cursor
                                .getColumnIndex(SPORT_DISTANCE));
                        cal += (int) cursor.getDouble(cursor
                                .getColumnIndex(SPORT_CALORIES));
                        dur += cursor.getInt(cursor.getColumnIndex(SPORT_TIME));
                    }

                } while (cursor.moveToNext());

                double speed = dis * 3600 / dur;
                Log.i("sport_sub", "dis:" + dis + "--" + "cal:" + cal
                        + "--dur:" + dur + "--speed:" + speed);
                sportContionTaskDetail.setSportDistance(dis);
                sportContionTaskDetail.setSprots_Calorie(cal);
                sportContionTaskDetail.setSportTime(Integer.toString(dur));
                sportContionTaskDetail.setSportVelocity(speed);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
        return sportContionTaskDetail;
    }

    public SportContionTaskDetail getToadySportsByType(int uid, String toady,
                                                       int typeId) {
        if (db == null) {
            db = getReadableDatabase();
        }

        Log.i("sport_sub", "toady:" + toady);

        double dis = 0.0;
        int cal = 0;
        int dur = 0;
        String start_time;

        SportContionTaskDetail sportContionTaskDetail = new SportContionTaskDetail();

        Cursor cursor = null;
        try {
            cursor = db
                    .rawQuery(
                            "select sport_taskid,sport_distance,sport_calories,sport_time,sport_start_time,sport_date from sport_sub where uid=? and sport_date=? and sport_type=? order by sport_start_time desc",
                            new String[]{Integer.toString(uid), toady,
                                    Integer.toString(typeId)});
            if (cursor.moveToFirst()) {
                do {
                    start_time = cursor.getString(cursor
                            .getColumnIndex("sport_date"));
                    // 格式:2014-08-12 10:41:43
                    Log.i("sport_sub", "sport_sub_1:" + start_time);

                    // start_time = start_time.substring(0, 10);
                    // // 格式:2014-08-12
                    // Log.i("sport_sub", "sport_sub2:" + start_time);

                    if (start_time.equals(toady)) {
                        dis += cursor.getDouble(cursor
                                .getColumnIndex(SPORT_DISTANCE));
                        cal += (int) cursor.getDouble(cursor
                                .getColumnIndex(SPORT_CALORIES));
                        dur += cursor.getInt(cursor.getColumnIndex(SPORT_TIME));
                    }

                } while (cursor.moveToNext());

                double speed = dis * 3600 / dur;
                Log.i("sport_sub", "dis:" + dis + "--" + "cal:" + cal
                        + "--dur:" + dur + "--speed:" + speed);
                sportContionTaskDetail.setSportDistance(dis);
                sportContionTaskDetail.setSprots_Calorie(cal);
                sportContionTaskDetail.setSportTime(Integer.toString(dur));
                sportContionTaskDetail.setSportVelocity(speed);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
        return sportContionTaskDetail;
    }

    public ArrayList<String> getStartTimeByType(int uid, String toady,
                                                int typeId) {
        if (db == null) {
            db = getReadableDatabase();
        }
        ArrayList<String> startTimeList = new ArrayList<String>();
        Cursor cursor = null;
        try {
            cursor = db
                    .rawQuery(
                            "select sport_start_time from sport_sub where uid=? and sport_date=? and sport_type=? order by sport_start_time desc",
                            new String[]{Integer.toString(uid), toady,
                                    Integer.toString(typeId)});
            if (cursor.moveToFirst()) {

                do {
                    startTimeList.add(cursor.getString(cursor
                            .getColumnIndex("sport_start_time")));

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
        return startTimeList;
    }

    public SportContionTaskDetail getAllHistorySports(int uid) {
        if (db == null) {
            db = getReadableDatabase();
        }

        double dis = 0.0;
        int cal = 0;

        SportContionTaskDetail sportContionTaskDetail = new SportContionTaskDetail();

        Cursor cursor = null;
        try {
            cursor = db
                    .rawQuery(
                            "select sport_taskid,sport_distance,sport_calories,sport_start_time from sport_sub where (uid=0 or uid=?) order by sport_start_time desc",
                            new String[]{Integer.toString(uid)});
            if (cursor.moveToFirst()) {
                do {
                    dis += cursor.getDouble(cursor
                            .getColumnIndex(SPORT_DISTANCE));
                    cal += (int) cursor.getDouble(cursor
                            .getColumnIndex(SPORT_CALORIES));
                } while (cursor.moveToNext());
            }

            Log.i("sport_sub", "dis:" + dis + "--" + "cal" + cal);
            sportContionTaskDetail.setSportDistance(dis);
            sportContionTaskDetail.setSprots_Calorie(cal);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
        return sportContionTaskDetail;
    }

    public SportContionTaskDetail getTaskByTaskID(int uid, int taskid) {
        if (db == null) {
            db = getReadableDatabase();
        }
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(
                    "select * from sport_sub where uid=?and sport_taskid=? ",
                    new String[]{Integer.toString(uid),
                            Integer.toString(taskid)});
            SportContionTaskDetail sport = new SportContionTaskDetail();
            if (cursor.moveToFirst()) {
                sport.setStartTime(cursor.getString(cursor
                        .getColumnIndex(SPORT_START_TIME)));
                sport.setSportDate(cursor.getString(cursor
                        .getColumnIndex(SPORT_DATE)));
                sport.setSportTime(cursor.getString(cursor
                        .getColumnIndex(SPORT_TIME)));
                sport.setSports_type(cursor.getInt(cursor
                        .getColumnIndex(SPORT_TYPE)));
                sport.setSwimType(cursor.getInt(cursor
                        .getColumnIndex(SPORT_SWIM_TYPE)));
                sport.setMonitoringEquipment(cursor.getInt(cursor
                        .getColumnIndex(SPORT_DEVICE)));
                sport.setSportDistance(cursor.getDouble(cursor
                        .getColumnIndex(SPORT_DISTANCE)));
                sport.setSprots_Calorie((int) cursor.getDouble(cursor
                        .getColumnIndex(SPORT_CALORIES)));
                sport.setHeartRate(cursor.getDouble(cursor
                        .getColumnIndex(SPORT_HEART_RATE)));
                sport.setLatlng(cursor.getString(cursor
                        .getColumnIndex(SPORT_LAT_LNG)));
                sport.setTaskid(cursor.getInt(cursor.getColumnIndex(LIMIT)));
                sport.setStepNum(cursor.getInt(cursor
                        .getColumnIndex(SPORT_STEP)));
                sport.setSportVelocity(cursor.getDouble(cursor
                        .getColumnIndex(SPORT_SPEED)));
                sport.setMapType(cursor.getInt(cursor
                        .getColumnIndex(SPORT_MAPTYPE)));
                sport.setTaskid(taskid);
                return sport;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
        return null;
    }

    public SportContionTaskDetail getTaskByTime(int uid, String time) {
        if (db == null) {
            db = getReadableDatabase();
        }
        Cursor cursor = null;
        try {
            cursor = db
                    .rawQuery(
                            "select * from sport_sub where (uid=0 or uid=?)and sport_start_time=? ",
                            new String[]{Integer.toString(uid), time});
            SportContionTaskDetail sport = new SportContionTaskDetail();
            if (cursor.moveToFirst()) {
                sport.setStartTime(cursor.getString(cursor
                        .getColumnIndex(SPORT_START_TIME)));
                sport.setSportDate(cursor.getString(cursor
                        .getColumnIndex(SPORT_DATE)));
                sport.setSportTime(cursor.getString(cursor
                        .getColumnIndex(SPORT_TIME)));
                sport.setSports_type(cursor.getInt(cursor
                        .getColumnIndex(SPORT_TYPE)));
                sport.setSwimType(cursor.getInt(cursor
                        .getColumnIndex(SPORT_SWIM_TYPE)));
                sport.setMonitoringEquipment(cursor.getInt(cursor
                        .getColumnIndex(SPORT_DEVICE)));
                sport.setSportDistance(cursor.getDouble(cursor
                        .getColumnIndex(SPORT_DISTANCE)));
                sport.setSprots_Calorie((int) cursor.getDouble(cursor
                        .getColumnIndex(SPORT_CALORIES)));
                sport.setHeartRate(cursor.getDouble(cursor
                        .getColumnIndex(SPORT_HEART_RATE)));
                sport.setLatlng(cursor.getString(cursor
                        .getColumnIndex(SPORT_LAT_LNG)));
                sport.setTaskid(cursor.getInt(cursor.getColumnIndex(LIMIT)));
                sport.setStepNum(cursor.getInt(cursor
                        .getColumnIndex(SPORT_STEP)));
                sport.setSportVelocity(cursor.getDouble(cursor
                        .getColumnIndex(SPORT_SPEED)));
                sport.setTaskid(cursor.getInt(cursor
                        .getColumnIndex(SPORT_TASKID)));
                sport.setMapType(cursor.getInt(cursor
                        .getColumnIndex(SPORT_MAPTYPE)));
                return sport;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
        return null;
    }

    //
    public SportTask getTask(int uid, int taskid) {
        if (db == null) {
            db = getReadableDatabase();
        }
        Cursor cursor = null;
        try {
            cursor = db
                    .rawQuery(
                            "select * from sport_sub where (uid=0 or uid=?)and sport_taskid=? ",
                            new String[]{Integer.toString(uid),
                                    Integer.toString(taskid)});
            SportTask sport = new SportTask();
            if (cursor.moveToFirst()) {
                sport.start_time = cursor.getString(cursor
                        .getColumnIndex("sport_start_time"));
                sport.sport_time = cursor.getInt(cursor
                        .getColumnIndex("sport_time"));
                sport.Sport_type_task = cursor.getInt(cursor
                        .getColumnIndex("sport_type"));
                sport.sports_swim_type = cursor.getInt(cursor
                        .getColumnIndex("sport_swim_type"));
                sport.sport_device = cursor.getInt(cursor
                        .getColumnIndex("sport_device"));
                sport.sport_distance = cursor.getDouble(cursor
                        .getColumnIndex("sport_distance"));
                sport.sport_speed = cursor.getDouble(cursor
                        .getColumnIndex("sport_speed"));
                sport.sport_calories = cursor.getDouble(cursor
                        .getColumnIndex("sport_calories"));
                sport.sport_heart_rate = cursor.getDouble(cursor
                        .getColumnIndex("sport_heart_rate"));
                sport.sport_lat_lng = cursor.getString(cursor
                        .getColumnIndex("sport_lat_lng"));
                sport.sport_isupload = cursor.getInt(cursor
                        .getColumnIndex("sport_isupload"));
                sport.sport_taskid = cursor.getInt(cursor
                        .getColumnIndex("sport_taskid"));
                sport.sport_step = cursor.getInt(cursor
                        .getColumnIndex("sport_step"));
                sport.sport_map_type = cursor.getInt(cursor
                        .getColumnIndex("sport_map_type"));
                sport.setSport_mark_code(cursor.getString(cursor
                        .getColumnIndex("sport_markcode")));
                sport.setSport_speedlsit(cursor.getString(cursor
                        .getColumnIndex("sport_speedlist")));
                sport.setCoordinate_list(cursor.getString(cursor
                        .getColumnIndex(SPORT_MARKLIST)));
                return sport;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
        return null;
    }

    public SportTask getTaskByStartTime(int uid, String stratTime) {
        if (db == null) {
            db = getReadableDatabase();
        }
        Cursor cursor = null;
        try {
            cursor = db
                    .rawQuery(
                            "select * from sport_sub where (uid=0 or uid=?)and sport_start_time=? ",
                            new String[]{Integer.toString(uid), stratTime});
            SportTask sport = new SportTask();
            if (cursor.moveToFirst()) {
                sport.start_time = cursor.getString(cursor
                        .getColumnIndex("sport_start_time"));
                sport.sport_time = cursor.getInt(cursor
                        .getColumnIndex("sport_time"));
                sport.Sport_type_task = cursor.getInt(cursor
                        .getColumnIndex("sport_type"));
                sport.sports_swim_type = cursor.getInt(cursor
                        .getColumnIndex("sport_swim_type"));
                sport.sport_device = cursor.getInt(cursor
                        .getColumnIndex("sport_device"));
                sport.sport_distance = cursor.getDouble(cursor
                        .getColumnIndex("sport_distance"));
                sport.sport_speed = cursor.getDouble(cursor
                        .getColumnIndex("sport_speed"));
                sport.sport_calories = cursor.getDouble(cursor
                        .getColumnIndex("sport_calories"));
                sport.sport_heart_rate = cursor.getDouble(cursor
                        .getColumnIndex("sport_heart_rate"));
                sport.sport_lat_lng = cursor.getString(cursor
                        .getColumnIndex("sport_lat_lng"));
                sport.sport_isupload = cursor.getInt(cursor
                        .getColumnIndex("sport_isupload"));
                sport.sport_taskid = cursor.getInt(cursor
                        .getColumnIndex("sport_taskid"));
                sport.sport_step = cursor.getInt(cursor
                        .getColumnIndex("sport_step"));
                sport.sport_map_type = cursor.getInt(cursor
                        .getColumnIndex("sport_map_type"));
                sport.setSport_mark_code(cursor.getString(cursor
                        .getColumnIndex("sport_markcode")));
                sport.setSport_speedlsit(cursor.getString(cursor
                        .getColumnIndex("sport_speedlist")));
                sport.setCoordinate_list(cursor.getString(cursor
                        .getColumnIndex(SPORT_MARKLIST)));
                return sport;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
        return null;
    }


    public SportTask getTaskByTimeAndCode(int uid, String stratTime, String markCode) {
        if (db == null) {
            db = getReadableDatabase();
        }
        Cursor cursor = null;
        try {
            cursor = db
                    .rawQuery(
                            "select * from sport_sub where (uid=0 or uid=?)and sport_start_time=? and sport_markcode=?",
                            new String[]{Integer.toString(uid), stratTime, markCode});
            SportTask sport = new SportTask();
            if (cursor.moveToFirst()) {
                sport.start_time = cursor.getString(cursor
                        .getColumnIndex("sport_start_time"));
                sport.sport_time = cursor.getInt(cursor
                        .getColumnIndex("sport_time"));
                sport.Sport_type_task = cursor.getInt(cursor
                        .getColumnIndex("sport_type"));
                sport.sports_swim_type = cursor.getInt(cursor
                        .getColumnIndex("sport_swim_type"));
                sport.sport_device = cursor.getInt(cursor
                        .getColumnIndex("sport_device"));
                sport.sport_distance = cursor.getDouble(cursor
                        .getColumnIndex("sport_distance"));
                sport.sport_speed = cursor.getDouble(cursor
                        .getColumnIndex("sport_speed"));
                sport.sport_calories = cursor.getDouble(cursor
                        .getColumnIndex("sport_calories"));
                sport.sport_heart_rate = cursor.getDouble(cursor
                        .getColumnIndex("sport_heart_rate"));
                sport.sport_lat_lng = cursor.getString(cursor
                        .getColumnIndex("sport_lat_lng"));
                sport.sport_isupload = cursor.getInt(cursor
                        .getColumnIndex("sport_isupload"));
                sport.sport_taskid = cursor.getInt(cursor
                        .getColumnIndex("sport_taskid"));
                sport.sport_step = cursor.getInt(cursor
                        .getColumnIndex("sport_step"));
                sport.sport_map_type = cursor.getInt(cursor
                        .getColumnIndex("sport_map_type"));
                sport.setSport_mark_code(cursor.getString(cursor
                        .getColumnIndex("sport_markcode")));
                sport.setSport_speedlsit(cursor.getString(cursor
                        .getColumnIndex("sport_speedlist")));
                sport.setCoordinate_list(cursor.getString(cursor
                        .getColumnIndex(SPORT_MARKLIST)));
                return sport;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public synchronized void close() {
        if (db != null) {
            db.close();
            db = null;
        }
        super.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        ChangePrivateMsgTable(db);
    }

    private static void ChangePrivateMsgTable(SQLiteDatabase db) {
        db.execSQL(getTempTableSQL());
        db.execSQL(getGeneratedSQL());
        db.execSQL(InsertStringTableSQL());
        db.execSQL(DropTempTableSQL());
    }

    public static String getGeneratedSQL() {
        String createString = "create table if not exists " + TAB_NAME
                + " (_id integer primary key autoincrement," + UID
                + " integer," + SPORT_TYPE + " integer," + SPORT_SWIM_TYPE
                + " integer," + SPORT_DEVICE + " integer," + SPORT_START_TIME
                + " text," + SPORT_TIME + " integer," + SPORT_DISTANCE
                + " text," + SPORT_SPEED + " text," + SPORT_CALORIES + " text,"
                + SPORT_HEART_RATE + " text," + SPORT_LAT_LNG + " text,"
                + SPORT_ISUPLOAD + " integer," + SPORT_TASKID + " integer,"
                + SPORT_DATE + " text," + SPORT_STEP + " integer," + LIMIT
                + " integer," + SPORT_MAPTYPE + " integer," + SPORT_MARKCODE + " text,"
                + SPORT_SPEEDLIST + " text,"+ SPORT_MARKLIST+" text)";
        return createString;

    }

    // 将原表变为临时表
    public static String getTempTableSQL() {
        return "ALTER TABLE " + TAB_NAME + " RENAME TO " + TABLE_NAME_2;

    }

    // 将临时表中的数据插入新表
    // "insert into tb_private_msg select *,' ' from tb_private_msg2";
    public static String InsertStringTableSQL() {
        return "INSERT INTO " + TAB_NAME + " select *,'0','0' from " + TABLE_NAME_2;
    }

    // 删除临时表
    public static String DropTempTableSQL() {
        return "DROP TABLE IF EXISTS " + TABLE_NAME_2;
    }

    // ///////////////////////////////////////////////////////////////////////
    /*
     * 获取本地全部历史记录
	 */
    public ArrayList<SportContionTaskDetail> getAllTasks(int uid) {
        if (db == null) {
            db = getReadableDatabase();
        }
        ArrayList<SportContionTaskDetail> sports = new ArrayList<SportContionTaskDetail>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(
                    "select * from sport_sub where (uid=0 or uid=?)",
                    new String[]{Integer.toString(uid)});
            if (cursor.moveToFirst()) {
                Log.v(TAG, "wmh get local list");
                do {
                    SportContionTaskDetail sport = new SportContionTaskDetail();

                    SportRecord sportRecord = new SportRecord();

                    if (SportTaskUtil.getNormalRange(cursor.getInt(cursor
                                    .getColumnIndex(SPORT_TYPE)), cursor
                                    .getDouble(cursor.getColumnIndex(SPORT_SPEED)),
                            Integer.parseInt(cursor.getString(cursor
                                    .getColumnIndex(SPORT_TIME))))) {

                        sport.setStartTime(cursor.getString(cursor
                                .getColumnIndex(SPORT_START_TIME)));
                        sport.setSportDate(cursor.getString(cursor
                                .getColumnIndex(SPORT_DATE)));
                        sport.setSportTime(cursor.getString(cursor
                                .getColumnIndex(SPORT_TIME)));
                        sport.setSports_type(cursor.getInt(cursor
                                .getColumnIndex(SPORT_TYPE)));
                        sport.setSwimType(cursor.getInt(cursor
                                .getColumnIndex(SPORT_SWIM_TYPE)));
                        sport.setMonitoringEquipment(cursor.getInt(cursor
                                .getColumnIndex(SPORT_DEVICE)));
                        sport.setSportDistance(cursor.getDouble(cursor
                                .getColumnIndex(SPORT_DISTANCE)));
                        sport.setSprots_Calorie((int) cursor.getDouble(cursor
                                .getColumnIndex(SPORT_CALORIES)));
                        sport.setHeartRate(cursor.getDouble(cursor
                                .getColumnIndex(SPORT_HEART_RATE)));
                        sport.setLatlng(cursor.getString(cursor
                                .getColumnIndex(SPORT_LAT_LNG)));
                        sport.setTaskid(cursor.getInt(cursor
                                .getColumnIndex(LIMIT)));
                        sport.setStepNum(cursor.getInt(cursor
                                .getColumnIndex(SPORT_STEP)));
                        sport.setSportVelocity(cursor.getDouble(cursor
                                .getColumnIndex(SPORT_SPEED)));
                        sport.setTaskid(cursor.getInt(cursor
                                .getColumnIndex(SPORT_TASKID)));
                        sport.setMapType(cursor.getInt(cursor
                                .getColumnIndex(SPORT_MAPTYPE)));
                        // sportRecord.setTime(cursor.getString(cursor
                        // .getColumnIndex(SPORT_LASTRUNTIME)));
                        // sportRecord.setSport_distance(cursor.getString(cursor
                        // .getColumnIndex(SPORT_ALLDAYS)));
                        sport.setSportRecord(sportRecord);
                        sports.add(sport);

                    }

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
        return sports;
    }

    /*
     * 获取本地全部历史记录 带上超速的
     */
    public ArrayList<SportContionTaskDetail> getNewAllTasks(int uid) {
        if (db == null) {
            db = getReadableDatabase();
        }
        ArrayList<SportContionTaskDetail> sports = new ArrayList<SportContionTaskDetail>();
        Cursor cursor = null;
        try {
            if(uid==0){
                cursor = db.rawQuery(
                        "select * from sport_sub where (uid=0)", null);
            }else{
                cursor = db.rawQuery(
                        "select * from sport_sub where (uid=0 or uid=?)",
                        new String[]{Integer.toString(uid)});
            }

            if (cursor.moveToFirst()) {
                Log.v(TAG, "wmh get local list");
                do {
                    SportContionTaskDetail sport = new SportContionTaskDetail();

                    SportRecord sportRecord = new SportRecord();

                    // if (SportTaskUtil.getNormalRange(cursor.getInt(cursor
                    // .getColumnIndex(SPORT_TYPE)), cursor
                    // .getDouble(cursor.getColumnIndex(SPORT_SPEED)),
                    // Integer.parseInt(cursor.getString(cursor
                    // .getColumnIndex(SPORT_TIME))))) {

                    sport.setStartTime(cursor.getString(cursor
                            .getColumnIndex(SPORT_START_TIME)));
                    sport.setSportDate(cursor.getString(cursor
                            .getColumnIndex(SPORT_DATE)));
                    sport.setSportTime(cursor.getString(cursor
                            .getColumnIndex(SPORT_TIME)));
                    sport.setSports_type(cursor.getInt(cursor
                            .getColumnIndex(SPORT_TYPE)));
                    sport.setSwimType(cursor.getInt(cursor
                            .getColumnIndex(SPORT_SWIM_TYPE)));
                    sport.setMonitoringEquipment(cursor.getInt(cursor
                            .getColumnIndex(SPORT_DEVICE)));
                    sport.setSportDistance(cursor.getDouble(cursor
                            .getColumnIndex(SPORT_DISTANCE)));
                    sport.setSprots_Calorie((int) cursor.getDouble(cursor
                            .getColumnIndex(SPORT_CALORIES)));
                    sport.setHeartRate(cursor.getDouble(cursor
                            .getColumnIndex(SPORT_HEART_RATE)));
                    sport.setLatlng(cursor.getString(cursor
                            .getColumnIndex(SPORT_LAT_LNG)));
                    sport.setTaskid(cursor.getInt(cursor.getColumnIndex(LIMIT)));
                    sport.setStepNum(cursor.getInt(cursor
                            .getColumnIndex(SPORT_STEP)));
                    sport.setSportVelocity(cursor.getDouble(cursor
                            .getColumnIndex(SPORT_SPEED)));
                    sport.setTaskid(cursor.getInt(cursor
                            .getColumnIndex(SPORT_TASKID)));
                    sport.setMapType(cursor.getInt(cursor
                            .getColumnIndex(SPORT_MAPTYPE)));
                    // sportRecord.setTime(cursor.getString(cursor
                    // .getColumnIndex(SPORT_LASTRUNTIME)));
                    // sportRecord.setSport_distance(cursor.getString(cursor
                    // .getColumnIndex(SPORT_ALLDAYS)));
                    sport.setSport_markcode(cursor.getString(cursor
                            .getColumnIndex(SPORT_MARKCODE)));
                    sport.setSportRecord(sportRecord);
                    sports.add(sport);

                    // }

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
        return sports;
    }

    public ArrayList<SportContionTaskDetail> getTasksByDate(int uid,
                                                            String sportDate) {
        if (db == null) {
            db = getReadableDatabase();
        }
        ArrayList<SportContionTaskDetail> sports = new ArrayList<SportContionTaskDetail>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(
                    "select * from sport_sub where uid=? and sport_date=?",
                    new String[]{Integer.toString(uid), sportDate});
            if (cursor.moveToFirst()) {
                do {
                    SportContionTaskDetail sport = new SportContionTaskDetail();

                    sport.setStartTime(cursor.getString(cursor
                            .getColumnIndex(SPORT_START_TIME)));
                    sport.setSportDate(cursor.getString(cursor
                            .getColumnIndex(SPORT_DATE)));
                    sport.setSportTime(cursor.getString(cursor
                            .getColumnIndex(SPORT_TIME)));
                    sport.setSports_type(cursor.getInt(cursor
                            .getColumnIndex(SPORT_TYPE)));
                    sport.setSwimType(cursor.getInt(cursor
                            .getColumnIndex(SPORT_SWIM_TYPE)));
                    sport.setMonitoringEquipment(cursor.getInt(cursor
                            .getColumnIndex(SPORT_DEVICE)));
                    sport.setSportDistance(cursor.getDouble(cursor
                            .getColumnIndex(SPORT_DISTANCE)));
                    sport.setSprots_Calorie((int) cursor.getDouble(cursor
                            .getColumnIndex(SPORT_CALORIES)));
                    sport.setHeartRate(cursor.getDouble(cursor
                            .getColumnIndex(SPORT_HEART_RATE)));
                    sport.setLatlng(cursor.getString(cursor
                            .getColumnIndex(SPORT_LAT_LNG)));
                    sport.setTaskid(cursor.getInt(cursor.getColumnIndex(LIMIT)));
                    sport.setStepNum(cursor.getInt(cursor
                            .getColumnIndex(SPORT_STEP)));
                    sport.setSportVelocity(cursor.getDouble(cursor
                            .getColumnIndex(SPORT_SPEED)));
                    sport.setTaskid(cursor.getInt(cursor
                            .getColumnIndex(SPORT_TASKID)));
                    sport.setMapType(cursor.getInt(cursor
                            .getColumnIndex(SPORT_MAPTYPE)));
                    sports.add(sport);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
        return sports;
    }

    public ArrayList<SportTask> getTasksByUnUpload(int uid) {
        if (db == null) {
            db = getReadableDatabase();
        }
        ArrayList<SportTask> sports = new ArrayList<SportTask>();
        Cursor cursor = null;
        try {
            cursor = db
                    .rawQuery(
                            "select * from sport_sub where uid=? and sport_isupload!=1",
                            new String[]{Integer.toString(uid)});
            if (cursor.moveToFirst()) {
                do {
                    SportTask sport = new SportTask();
                    sport.start_time = cursor.getString(cursor
                            .getColumnIndex("sport_start_time"));
                    sport.sport_time = cursor.getInt(cursor
                            .getColumnIndex("sport_time"));
                    sport.Sport_type_task = cursor.getInt(cursor
                            .getColumnIndex("sport_type"));
                    sport.sports_swim_type = cursor.getInt(cursor
                            .getColumnIndex("sport_swim_type"));
                    sport.sport_device = cursor.getInt(cursor
                            .getColumnIndex("sport_device"));
                    sport.sport_distance = cursor.getDouble(cursor
                            .getColumnIndex("sport_distance"));
                    sport.sport_speed = cursor.getDouble(cursor
                            .getColumnIndex("sport_speed"));
                    sport.sport_calories = cursor.getDouble(cursor
                            .getColumnIndex("sport_calories"));
                    sport.sport_heart_rate = cursor.getDouble(cursor
                            .getColumnIndex("sport_heart_rate"));
                    sport.sport_lat_lng = cursor.getString(cursor
                            .getColumnIndex("sport_lat_lng"));
                    sport.sport_isupload = cursor.getInt(cursor
                            .getColumnIndex("sport_isupload"));
                    sport.sport_taskid = cursor.getInt(cursor
                            .getColumnIndex("sport_taskid"));
                    sport.sport_step = cursor.getInt(cursor
                            .getColumnIndex("sport_step"));
                    sport.sport_map_type = cursor.getInt(cursor
                            .getColumnIndex("sport_map_type"));
                    sports.add(sport);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
        return sports;
    }

    public ArrayList<SportTask> getTasksByUnOrIngUpload(int uid) {
        if (db == null) {
            db = getReadableDatabase();
        }
        ArrayList<SportTask> sports = new ArrayList<SportTask>();
        Cursor cursor = null;
        try {
            cursor = db
                    .rawQuery(
                            "select * from sport_sub where uid=? and sport_isupload!=1 and sport_isupload!=2",
                            new String[]{Integer.toString(uid)});
            if (cursor.moveToFirst()) {
                do {
                    SportTask sport = new SportTask();
                    sport.uid = cursor.getInt(cursor
                            .getColumnIndex("uid"));
                    sport.start_time = cursor.getString(cursor
                            .getColumnIndex("sport_start_time"));
                    sport.sport_time = cursor.getInt(cursor
                            .getColumnIndex("sport_time"));
                    sport.Sport_type_task = cursor.getInt(cursor
                            .getColumnIndex("sport_type"));
                    sport.sports_swim_type = cursor.getInt(cursor
                            .getColumnIndex("sport_swim_type"));
                    sport.sport_device = cursor.getInt(cursor
                            .getColumnIndex("sport_device"));
                    sport.sport_distance = cursor.getDouble(cursor
                            .getColumnIndex("sport_distance"));
                    sport.sport_speed = cursor.getDouble(cursor
                            .getColumnIndex("sport_speed"));
                    sport.sport_calories = cursor.getDouble(cursor
                            .getColumnIndex("sport_calories"));
                    sport.sport_heart_rate = cursor.getDouble(cursor
                            .getColumnIndex("sport_heart_rate"));
                    sport.sport_lat_lng = cursor.getString(cursor
                            .getColumnIndex("sport_lat_lng"));
                    sport.sport_isupload = cursor.getInt(cursor
                            .getColumnIndex("sport_isupload"));
                    sport.sport_taskid = cursor.getInt(cursor
                            .getColumnIndex("sport_taskid"));
                    sport.sport_step = cursor.getInt(cursor
                            .getColumnIndex("sport_step"));
                    sport.sport_map_type = cursor.getInt(cursor
                            .getColumnIndex("sport_map_type"));

                    sport.sport_mark_code = cursor.getString(cursor
                            .getColumnIndex("sport_markcode"));
                    sport.sport_speedlsit=cursor.getString(cursor
                            .getColumnIndex("sport_speedlist"));
                    sport.setCoordinate_list(cursor.getString(cursor
                            .getColumnIndex(SPORT_MARKLIST)));
                    sports.add(sport);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
        return sports;
    }

}
