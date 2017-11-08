package com.fox.exercise.newversion.trainingplan;

import java.util.ArrayList;

import com.fox.exercise.R;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class TrainPlanDataBase extends SQLiteOpenHelper {
    private static TrainPlanDataBase mInstance = null;
    private Context context;
    private SQLiteDatabase db = null;

    public final static String DB_NAME = "train_plan.db";
    public final static String TAB_NAME = "train_plan_task";

    public final static String UID_I = "uid";                 //用户id
    public final static String TRAIN_ID_I = "train_id";            //训练id
    public final static String TRAIN_TIME_I = "traintime";           //训练时间/秒
    public final static String TRAIN_CALORIE_D = "train_calorie";       //训练消耗的卡路里
    public final static String TRAIN_ACTION_S = "train_action";        //训练动作的id字符串以,分割
    public final static String TRAIN_POSITION_S = "train_position";      //训练部位
    public final static String TRAIN_COMPLETION_I = "train_completion";    //训练完成度：如80%传80
    public final static String TRAIN_STARTTIME_S = "train_starttime";     //训练开始时间格式2016-01-01 01：01：01
    public final static String TRAIN_ENDTIME_S = "train_endtime";       //训练结束时间格式2016-01-01 01：01：02
    public final static String IS_TOTAL_I = "is_total";            //是否是训练的总成绩：1表示是，0表示是某个动作的成绩
    public final static String IS_UPLOAD_I = "is_upload";           //是否已上传标志：1表示已上传，0表示未上传
    public final static String TRAIN_MARKCODE = "train_markcode";      // 训练唯一标示码

    public String CREATE_TAB = "create table if not exists " + TAB_NAME + " (_id integer primary key autoincrement," +
            UID_I + " integer," + TRAIN_ID_I + " integer," + TRAIN_TIME_I + " integer," +
            TRAIN_CALORIE_D + " double," + TRAIN_ACTION_S + " text," + TRAIN_POSITION_S + " text," +
            TRAIN_COMPLETION_I + " integer," + TRAIN_STARTTIME_S + " text," + TRAIN_ENDTIME_S + " text," +
            IS_TOTAL_I + " integer," + IS_UPLOAD_I + " integer," + TRAIN_MARKCODE + " text)";

    public synchronized static TrainPlanDataBase getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new TrainPlanDataBase(context);
        }
        return mInstance;
    }

    private TrainPlanDataBase(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        db.execSQL(CREATE_TAB);
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

//    public void update(ContentValues values) {
//        if (db == null) {
//            db = getWritableDatabase();
//        }
//
//        db.update(TAB_NAME, values, TRAIN_STARTTIME_S + "=?", new String[] {values.getAsString(TRAIN_STARTTIME_S)});
//    }

    public int update(ContentValues values, int uid, String start_time,
                      Boolean isShow, String markCode) {
        if (db == null) {
            db = getWritableDatabase();
        }
        long id = db.update(TAB_NAME, values,
                "uid=? and train_starttime=? and train_markcode=? ",
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

    public ArrayList<TrainTaskListDetail> getTasksList(int uid, int pages) {
        if (db == null) {
            db = getReadableDatabase();
        }

        ArrayList<TrainTaskListDetail> list = new ArrayList<TrainTaskListDetail>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from " + TAB_NAME + " where " + UID_I + "=? order by " +
                            TRAIN_STARTTIME_S + " desc limit ?,?",
                    new String[]{Integer.toString(uid), Integer.toString(pages * 10),
                            Integer.toString(10)});

            if (cursor.moveToFirst()) {
                do {
                    TrainTaskListDetail detail = new TrainTaskListDetail();
                    Log.e("develop_debug", "cursor.getColumnIndex(TRAIN_ENDTIME_S) : " + cursor.getString(cursor.getColumnIndex(TRAIN_ENDTIME_S)));
                    detail.setUid(cursor.getInt(cursor.getColumnIndex(UID_I)));
                    detail.setTrain_id(cursor.getInt(cursor.getColumnIndex(TRAIN_ID_I)));
                    detail.setTraintime(cursor.getInt(cursor.getColumnIndex(TRAIN_TIME_I)));
                    detail.setTrain_calorie(cursor.getDouble(cursor.getColumnIndex(TRAIN_CALORIE_D)));
                    detail.setTrain_action(cursor.getString(cursor.getColumnIndex(TRAIN_ACTION_S)));
                    detail.setTrain_position(cursor.getString(cursor.getColumnIndex(TRAIN_POSITION_S)));
                    detail.setTrain_completion(cursor.getInt(cursor.getColumnIndex(TRAIN_COMPLETION_I)));
                    detail.setTrain_starttime(cursor.getString(cursor.getColumnIndex(TRAIN_STARTTIME_S)));
                    detail.setTrain_endtime(cursor.getString(cursor.getColumnIndex(TRAIN_ENDTIME_S)));
                    detail.setIs_total(cursor.getInt(cursor.getColumnIndex(IS_TOTAL_I)));
                    detail.setIs_upload(cursor.getInt(cursor.getColumnIndex(IS_UPLOAD_I)));
                    detail.setUnique_id(cursor.getString(cursor.getColumnIndex(TRAIN_MARKCODE)));

                    list.add(detail);
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
        return list;
    }

    public Cursor query(int uid, String start_time, String markCode) {
        if (db == null) {
            db = getReadableDatabase();
        }

        return db.rawQuery("select * from " + TAB_NAME + " where " + UID_I + "=? and " + TRAIN_STARTTIME_S + "=? and " + TRAIN_MARKCODE + "=?",
                new String[]{Integer.toString(uid), start_time, markCode});
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

    }
}