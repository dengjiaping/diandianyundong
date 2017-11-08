package com.fox.exercise.newversion.bushutongji;

import java.util.ArrayList;

import com.fox.exercise.R;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class BuShuTongJiDB extends SQLiteOpenHelper {

    private static BuShuTongJiDB mInstance = null;
    private Context context;
    private SQLiteDatabase db = null;
    private String TAG = "develop_debug";
    public final static String DB_NAME = "bushutongji.db";
    public final static String TAB_NAME = "bushutongji_table";

    public final static String UID_I = "uid";                 //用户id
    public final static String ID_I = "id";                  //记录id
    public final static String STEP_NUM_I = "step_num";            //步数
    public final static String DISTANCE_D = "distance";            //距离公里数
    public final static String STEP_CALORIE_D = "step_Calorie";        //卡路里
    public final static String DAY_S = "day";                 //日期 格式”2015-12-23”
    public final static String IS_UPLOAD_I = "is_upload";           //是否已上传标志：1表示已上传，0表示未上传

    public String CREATE_TAB = "create table if not exists " + TAB_NAME + " (_id integer primary key autoincrement," +
            UID_I + " integer," + ID_I + " integer," + STEP_NUM_I + " integer," + DISTANCE_D + " double," +
            STEP_CALORIE_D + " double," + DAY_S + " text," + IS_UPLOAD_I + " integer)";

    public synchronized static BuShuTongJiDB getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new BuShuTongJiDB(context);
        }
        return mInstance;
    }

    private BuShuTongJiDB(Context context) {

        super(context, DB_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        db.execSQL(CREATE_TAB);
    }

    public int insert(ContentValues values, Boolean isShow) {
        Log.e(TAG, "insert");
        if (db == null) {
            db = getWritableDatabase();
        }

        int id = (int) db.insert(TAB_NAME, null, values);
        if (id > 0) {
            Log.e(TAG, "插入数据成功，id=" + id);
            if (isShow) {
                Toast.makeText(
                        context,
                        context.getResources().getString(
                                R.string.sports_save_local_success),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e(TAG, "插入数据失败，id=" + id);
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

    public int update(ContentValues values, int uid, String day, Boolean isShow) {
        Log.e(TAG, "update");

        if (db == null) {
            db = getWritableDatabase();
        }
        long id = db.update(TAB_NAME, values,
                "uid=? and day=? ",
                new String[]{Integer.toString(uid), day});
        if (id > 0) {
            if (isShow) {
                Toast.makeText(
                        context,
                        context.getResources().getString(
                                R.string.sports_save_local_success),
                        Toast.LENGTH_SHORT).show();
            }
            Log.e(TAG, "更新数据成功，id=" + id);
        } else {
            if (isShow) {
                Toast.makeText(
                        context,
                        context.getResources().getString(
                                R.string.sports_save_local_failed),
                        Toast.LENGTH_SHORT).show();
            }
            Log.e(TAG, "更新数据失败，id=" + id);
        }
        return (int) id;
    }

    public void deleteRecord(int uid, String day) {
        if (db == null) {
            db = getWritableDatabase();
        }
        int id = db.delete(TAB_NAME, "uid=? and day=?",
                new String[]{Integer.toString(uid), day});

        if (id > 0) {
            Log.e(TAG, "删除数据成功，id=" + id);
        } else {
            Log.e(TAG, "删除数据失败，id=" + id);
        }
    }

    public BuShuTongJiDetail getUnLoginRecord() {
        if (db == null) {
            db = getReadableDatabase();
        }

        BuShuTongJiDetail detail = null;
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from " + TAB_NAME + " where " + UID_I + "=?" + " order by " +
                    DAY_S + " asc", new String[]{Integer.toString(0)});

            if (cursor.moveToFirst()) {
                detail = new BuShuTongJiDetail(cursor.getInt(cursor.getColumnIndex(UID_I)),
                        cursor.getInt(cursor.getColumnIndex(ID_I)),
                        cursor.getInt(cursor.getColumnIndex(STEP_NUM_I)),
                        cursor.getDouble(cursor.getColumnIndex(DISTANCE_D)),
                        cursor.getDouble(cursor.getColumnIndex(STEP_CALORIE_D)),
                        cursor.getString(cursor.getColumnIndex(DAY_S)),
                        cursor.getInt(cursor.getColumnIndex(IS_UPLOAD_I)));
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
        return detail;
    }

    public int getBuShuByDay(int uid, String day) {
        if (db == null) {
            db = getReadableDatabase();
        }

        int steps = -1;
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from " + TAB_NAME + " where " + UID_I + "=? and " + DAY_S + "=?",
                    new String[]{Integer.toString(uid), day});

            if (cursor.moveToFirst()) {
                do {
                    steps = cursor.getInt(cursor.getColumnIndex(STEP_NUM_I));
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
        return steps;
    }

    public String getFirstRecord(int uid) {
        if (db == null) {
            db = getReadableDatabase();
        }

        String date = null;
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from " + TAB_NAME + " where " + UID_I + "=?" + " order by " +
                    DAY_S + " asc", new String[]{Integer.toString(uid)});

            if (cursor.moveToFirst()) {
                date = cursor.getString(cursor.getColumnIndex(DAY_S));
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
        return date;
    }

    public BuShuTongJiDetail getRecordByDay(int uid, String day) {
        if (db == null) {
            db = getReadableDatabase();
        }

        BuShuTongJiDetail detail = null;
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from " + TAB_NAME + " where " + UID_I + "=? and " + DAY_S + "=?",
                    new String[]{Integer.toString(uid), day});

            if (cursor.moveToFirst()) {
                detail = new BuShuTongJiDetail(cursor.getInt(cursor.getColumnIndex(UID_I)),
                        cursor.getInt(cursor.getColumnIndex(ID_I)),
                        cursor.getInt(cursor.getColumnIndex(STEP_NUM_I)),
                        cursor.getDouble(cursor.getColumnIndex(DISTANCE_D)),
                        cursor.getDouble(cursor.getColumnIndex(STEP_CALORIE_D)),
                        cursor.getString(cursor.getColumnIndex(DAY_S)),
                        cursor.getInt(cursor.getColumnIndex(IS_UPLOAD_I)));
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
        return detail;
    }

    public BuShuTongJiDetail getUnUploadTask(int uid) {
        if (db == null) {
            db = getReadableDatabase();
        }

        BuShuTongJiDetail detail = null;
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from " + TAB_NAME + " where " + UID_I + "=? and " + IS_UPLOAD_I + "=?" + " order by " +
                            DAY_S + " desc",
                    new String[]{Integer.toString(uid), Integer.toString(0)});

            if (cursor.moveToFirst()) {
                detail = new BuShuTongJiDetail(cursor.getInt(cursor.getColumnIndex(UID_I)),
                        cursor.getInt(cursor.getColumnIndex(ID_I)),
                        cursor.getInt(cursor.getColumnIndex(STEP_NUM_I)),
                        cursor.getDouble(cursor.getColumnIndex(DISTANCE_D)),
                        cursor.getDouble(cursor.getColumnIndex(STEP_CALORIE_D)),
                        cursor.getString(cursor.getColumnIndex(DAY_S)),
                        cursor.getInt(cursor.getColumnIndex(IS_UPLOAD_I)));
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
        return detail;
    }

    public ArrayList<BuShuTongJiDetail> getUnUploadTasksList(int uid, String nowDate) {
        if (db == null) {
            db = getReadableDatabase();
        }

        ArrayList<BuShuTongJiDetail> list = new ArrayList<BuShuTongJiDetail>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from " + TAB_NAME + " where " + UID_I + "=? and " + IS_UPLOAD_I + "=?" + " order by " +
                            DAY_S + " asc",
                    new String[]{Integer.toString(uid), Integer.toString(0)});

            if (cursor.moveToFirst()) {
                do {
                    if (!cursor.getString(cursor.getColumnIndex(DAY_S)).equals(nowDate)) {
                        BuShuTongJiDetail detail = new BuShuTongJiDetail(cursor.getInt(cursor.getColumnIndex(UID_I)),
                                cursor.getInt(cursor.getColumnIndex(ID_I)),
                                cursor.getInt(cursor.getColumnIndex(STEP_NUM_I)),
                                cursor.getDouble(cursor.getColumnIndex(DISTANCE_D)),
                                cursor.getDouble(cursor.getColumnIndex(STEP_CALORIE_D)),
                                cursor.getString(cursor.getColumnIndex(DAY_S)),
                                cursor.getInt(cursor.getColumnIndex(IS_UPLOAD_I)));
                        list.add(detail);
                    }
                }while(cursor.moveToNext());
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

    public ArrayList<BuShuTongJiDetail> getTasksList(int uid, int pages) {
        if (db == null) {
            db = getReadableDatabase();
        }

        ArrayList<BuShuTongJiDetail> list = new ArrayList<BuShuTongJiDetail>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from " + TAB_NAME + " where " + UID_I + "=? order by " +
                            DAY_S + " desc limit ?,?",
                    new String[]{Integer.toString(uid), Integer.toString(pages * 10),
                            Integer.toString(10)});

            if (cursor.moveToFirst()) {
                do {
                    BuShuTongJiDetail detail = new BuShuTongJiDetail(cursor.getInt(cursor.getColumnIndex(UID_I)),
                            cursor.getInt(cursor.getColumnIndex(ID_I)),
                            cursor.getInt(cursor.getColumnIndex(STEP_NUM_I)),
                            cursor.getDouble(cursor.getColumnIndex(DISTANCE_D)),
                            cursor.getDouble(cursor.getColumnIndex(STEP_CALORIE_D)),
                            cursor.getString(cursor.getColumnIndex(DAY_S)),
                            cursor.getInt(cursor.getColumnIndex(IS_UPLOAD_I)));
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

    public Cursor query(int uid, String day) {
        if (db == null) {
            db = getReadableDatabase();
        }

        return db.rawQuery("select * from " + TAB_NAME + " where " + UID_I + "=? and " + DAY_S + "=?",
                new String[]{Integer.toString(uid), day});
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