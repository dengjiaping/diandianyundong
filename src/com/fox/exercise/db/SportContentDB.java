package com.fox.exercise.db;

import java.util.ArrayList;
import java.util.List;

import cn.ingenic.indroidsync.SportsApp;

import com.fox.exercise.R;
import com.fox.exercise.api.entity.SportTask;
import com.fox.exercise.db.SportsContent.PrivateMsgTable;
import com.fox.exercise.db.SportsContent.PhotoFramsTable.Columns;
import com.fox.exercise.newversion.entity.FirstPageContent;
import com.fox.exercise.newversion.entity.SleepEffect;
import com.fox.exercise.newversion.entity.SportRecord;
import com.fox.exercise.pedometer.SportContionTaskDetail;
import com.fox.exercise.util.SportTaskUtil;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class SportContentDB extends SQLiteOpenHelper {
    private Context context;
    private SQLiteDatabase db = null;
    private static SportContentDB mInstance = null;
    public final static String DB_NAME = "firstpageshow.db";
    public final static String TAB_NAME = "firstpageshow_sub";
    public final static String UID = "uid";
    public final static String SPORT_TIME = "times";
    public final static String SPORT_INDEX = "indexs";

    public String CREATE_TAB = "create table if not exists " + TAB_NAME
            + " (_id integer primary key autoincrement," + UID + " integer,"
            + SPORT_TIME + " text," + SPORT_INDEX + " integer)";
    public String CREATE_TAB_SLEEP = "create table IF NOT EXISTS SleepEffect(unique_id  text,starttime text,endtime text,"
            + "sleep_effect  varchar(120),uid varchar(120))";
    private String TAG = "SportSubTaskDB";
    public String CREATE_TAB_XINLV = "create table IF NOT EXISTS XinLv(unique_id  text,starttime text,endtime text,"
            + "sleep_effect  varchar(120),uid varchar(120))";

    public synchronized static SportContentDB getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SportContentDB(context);
        }
        return mInstance;
    }

    private SportContentDB(Context context) {
        super(context, DB_NAME, null, 4);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        db.execSQL(CREATE_TAB);
        db.execSQL(CREATE_TAB_SLEEP);
        db.execSQL(CREATE_TAB_XINLV);
    }

    public Cursor query() {
        if (db == null) {
            db = getWritableDatabase();
        }
        return db.rawQuery("select * from firstpageshow_sub", null);
    }

    public Cursor querySleep(String unique_id, String uid, String table_name) {
        if (db == null) {
            db = getWritableDatabase();
        }
        return db.rawQuery("select * from " + table_name
                        + " where unique_id=? and uid=?",
                new String[]{unique_id, uid});
    }

    public List<SleepEffect> queryUidSleep(String uid) {
        if (db == null) {
            db = getWritableDatabase();
        }
        List<SleepEffect> sList = new ArrayList<SleepEffect>();
        Cursor rawQuery = null;
        SleepEffect sleepEffect;
        try {
            rawQuery = db.rawQuery("select * from SleepEffect where uid=?",
                    new String[]{uid});
            while (rawQuery.moveToNext()) {
                sleepEffect = new SleepEffect();
                String string = rawQuery.getString(rawQuery
                        .getColumnIndex("unique_id"));
                String string1 = rawQuery.getString(rawQuery
                        .getColumnIndex("starttime"));
                String string2 = rawQuery.getString(rawQuery
                        .getColumnIndex("endtime"));
                String string3 = rawQuery.getString(rawQuery
                        .getColumnIndex("sleep_effect"));
                String string4 = rawQuery.getString(rawQuery
                        .getColumnIndex("uid"));
                sleepEffect.setUnique_id(string);
                sleepEffect.setStarttime(string1);
                sleepEffect.setEndtime(string2);
                sleepEffect.setHart_rate(string3);
                sleepEffect.setUid(string4);
                sList.add(sleepEffect);
            }
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            if (rawQuery != null) {
                rawQuery.close();
                rawQuery = null;
            }
        }

        return sList;

    }

    // 模糊查询 查询日期是最近七天的
    public List<SleepEffect> queryUidTimeSleep(String uid, String sleepTime) {
        if (db == null) {
            db = getWritableDatabase();
        }
        List<SleepEffect> sList = new ArrayList<SleepEffect>();
        Cursor rawQuery = null;
        SleepEffect sleepEffect;
        try {
            rawQuery = db
                    .rawQuery(
                            "select * from SleepEffect where uid=? and starttime like ?",
                            new String[]{uid, sleepTime + "%"});
            while (rawQuery.moveToNext()) {
                sleepEffect = new SleepEffect();
                String string = rawQuery.getString(rawQuery
                        .getColumnIndex("unique_id"));
                String string1 = rawQuery.getString(rawQuery
                        .getColumnIndex("starttime"));
                String string2 = rawQuery.getString(rawQuery
                        .getColumnIndex("endtime"));
                String string3 = rawQuery.getString(rawQuery
                        .getColumnIndex("sleep_effect"));
                String string4 = rawQuery.getString(rawQuery
                        .getColumnIndex("uid"));
                sleepEffect.setUnique_id(string);
                sleepEffect.setStarttime(string1);
                sleepEffect.setEndtime(string2);
                sleepEffect.setHart_rate(string3);
                sleepEffect.setUid(string4);
                sList.add(sleepEffect);
            }
        } catch (Exception e) {
            // TODO: handle exception
            String eString = e.toString();
        } finally {
            if (rawQuery != null) {
                rawQuery.close();
                rawQuery = null;
            }
        }

        return sList;

    }

    public FirstPageContent queryData() {
        if (db == null) {
            db = getWritableDatabase();
        }
        FirstPageContent firstPageContent = null;
        Cursor rawQuery = null;
        try {
            rawQuery = db.rawQuery("select * from firstpageshow_sub", null);
            while (rawQuery.moveToNext()) {
                firstPageContent = new FirstPageContent();
                int int1 = rawQuery.getInt(rawQuery
                        .getColumnIndex(SportContentDB.UID));
                int int2 = rawQuery.getInt(rawQuery
                        .getColumnIndex(SportContentDB.SPORT_INDEX));
                String string = rawQuery.getString(rawQuery
                        .getColumnIndex(SportContentDB.SPORT_TIME));
                firstPageContent.setIndex(int2);
                firstPageContent.setUid(int1);
                firstPageContent.setTime(string);
            }
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            if (rawQuery != null) {
                rawQuery.close();
                rawQuery = null;
            }
        }
        return firstPageContent;
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
        long id = db.update(TAB_NAME, values, "uid=?",
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

    public void update(ContentValues values, String start_time) {
        if (db == null) {
            db = getWritableDatabase();
        }
        long id = db.update(TAB_NAME, values, "sport_start_time=?",
                new String[]{start_time});
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

    public int insertSleep(Boolean isShow, SleepEffect sleepEffect) {
        if (db == null) {
            db = getWritableDatabase();
        }
        Cursor querySleep = querySleep(sleepEffect.getUnique_id(),
                sleepEffect.getUid(), "SleepEffect");
        ContentValues values = new ContentValues();
        values.put("unique_id", sleepEffect.getUnique_id());
        values.put("starttime", sleepEffect.getStarttime());
        values.put("endtime", sleepEffect.getEndtime());
        values.put("sleep_effect", sleepEffect.getHart_rate());
        values.put("uid", sleepEffect.getUid());
        int id;
        if (querySleep != null && querySleep.getCount() > 0) {
            id = db.update(
                    "SleepEffect",
                    values,
                    "unique_id=? and uid=?",
                    new String[]{sleepEffect.getUnique_id(),
                            sleepEffect.getUid()});
        } else {
            id = (int) db.insert("SleepEffect", null, values);
        }
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

    // ----------------------------------------------------------------------------------------------------------------------------------

    public List<SleepEffect> queryUidXinLv(String uid) {
        if (db == null) {
            db = getWritableDatabase();
        }
        List<SleepEffect> sList = new ArrayList<SleepEffect>();
        Cursor rawQuery = null;
        SleepEffect sleepEffect;
        try {
            rawQuery = db.rawQuery("select * from XinLv where uid=? order by endtime",
                    new String[]{uid});
            while (rawQuery.moveToNext()) {
                sleepEffect = new SleepEffect();
                String string = rawQuery.getString(rawQuery
                        .getColumnIndex("unique_id"));
                String string1 = rawQuery.getString(rawQuery
                        .getColumnIndex("starttime"));
                String string2 = rawQuery.getString(rawQuery
                        .getColumnIndex("endtime"));
                String string3 = rawQuery.getString(rawQuery
                        .getColumnIndex("sleep_effect"));
                String string4 = rawQuery.getString(rawQuery
                        .getColumnIndex("uid"));
                sleepEffect.setUnique_id(string);
                sleepEffect.setStarttime(string1);
                sleepEffect.setEndtime(string2);
                sleepEffect.setHart_rate(string3);
                sleepEffect.setUid(string4);
                sList.add(sleepEffect);
            }
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            if (rawQuery != null) {
                rawQuery.close();
                rawQuery = null;
            }
        }

        return sList;

    }

    // 插入或者更新睡眠数据
    public int deleteSleep(Boolean isShow, SleepEffect sleepEffect) {
        if (db == null) {
            db = getWritableDatabase();
        }

        int id = db.delete(
                "XinLv",
                "unique_id=? and starttime=?",
                new String[]{sleepEffect.getUnique_id(),
                        sleepEffect.getStarttime()});

        if (id > 0) {
            // Log.i("数据库插入", "插入数据成功，id=" + id);
            if (isShow) {
                // Toast.makeText(
                // context,
                // context.getResources().getString(
                // R.string.sports_save_local_success),
                // Toast.LENGTH_SHORT).show();
            }
        } else {
            // Log.i("数据库插入", "插入数据失败，id=" + id);
            if (isShow) {
                // Toast.makeText(
                // context,
                // context.getResources().getString(
                // R.string.sports_save_local_failed),
                // Toast.LENGTH_SHORT).show();
            }
        }
        return id;
    }

    // 删除全部；
    public int deleteAll(Boolean isShow, SportsApp msApp) {
        if (db == null) {
            db = getWritableDatabase();
        }

        int id = db.delete("XinLv", "uid=?", new String[]{msApp
                .getSportUser().getUid() + ""});

        if (id > 0) {
            // Log.i("数据库插入", "插入数据成功，id=" + id);
            if (isShow) {
                // Toast.makeText(
                // context,
                // context.getResources().getString(
                // R.string.sports_save_local_success),
                // Toast.LENGTH_SHORT).show();
            }
        } else {
            // Log.i("数据库插入", "插入数据失败，id=" + id);
            if (isShow) {
                // Toast.makeText(
                // context,
                // context.getResources().getString(
                // R.string.sports_save_local_failed),
                // Toast.LENGTH_SHORT).show();
            }
        }
        return id;
    }

    public Cursor queryXinlv(String unique_id, String uid) {
        if (db == null) {
            db = getWritableDatabase();
        }
        return db.rawQuery("select * from Xinlv where unique_id=? and uid=?",
                new String[]{unique_id, uid});
    }

    public int insertXinlv(Boolean isShow, SleepEffect sleepEffect) {
        if (db == null) {
            db = getWritableDatabase();
        }
        Cursor querySleep = querySleep(sleepEffect.getUnique_id(),
                sleepEffect.getUid(), "XinLv");
        ContentValues values = new ContentValues();
        values.put("unique_id", sleepEffect.getUnique_id());
        values.put("starttime", sleepEffect.getStarttime());
        values.put("endtime", sleepEffect.getEndtime());
        values.put("sleep_effect", sleepEffect.getHart_rate());
        values.put("uid", sleepEffect.getUid());
        int id;
        if (querySleep != null && querySleep.getCount() > 0) {
            id = db.update(
                    "XinLv",
                    values,
                    "unique_id=? and uid=?",
                    new String[]{sleepEffect.getUnique_id(),
                            sleepEffect.getUid()});
        } else {
            id = (int) db.insert("XinLv", null, values);
        }
        if (id > 0) {
            Log.i("数据库插入", "插入数据成功，id=" + id);
            if (isShow) {
                // Toast.makeText(
                // context,context.getResources().getString(R.string.sports_save_local_success),Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.i("数据库插入", "插入数据失败，id=" + id);
            if (isShow) {
                // Toast.makeText(
                // context,
                // context.getResources().getString(
                // R.string.sports_save_local_failed),
                // Toast.LENGTH_SHORT).show();
            }
        }
        return id;
    }

    // 查询前100条记录、超过100忽略
    public List<SleepEffect> queryUidXinLvCount(String uid, String sleepTime) {
        if (db == null) {
            db = getWritableDatabase();
        }
        List<SleepEffect> sList = new ArrayList<SleepEffect>();
        Cursor rawQuery = null;
        SleepEffect sleepEffect;
        try {
            rawQuery = db
                    .rawQuery(
                            "select * from XinLv where uid=? and rownum<=100 where rm>=0",
                            // "select * from XinLv where uid=? and starttime like ?",
                            new String[]{uid, sleepTime + "%"});
            while (rawQuery.moveToNext()) {
                sleepEffect = new SleepEffect();
                String string = rawQuery.getString(rawQuery
                        .getColumnIndex("unique_id"));
                String string1 = rawQuery.getString(rawQuery
                        .getColumnIndex("starttime"));
                String string2 = rawQuery.getString(rawQuery
                        .getColumnIndex("endtime"));
                String string3 = rawQuery.getString(rawQuery
                        .getColumnIndex("sleep_effect"));
                String string4 = rawQuery.getString(rawQuery
                        .getColumnIndex("uid"));
                sleepEffect.setUnique_id(string);
                sleepEffect.setStarttime(string1);
                sleepEffect.setEndtime(string2);
                sleepEffect.setHart_rate(string3);
                sleepEffect.setUid(string4);
                sList.add(sleepEffect);
            }
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            if (rawQuery != null) {
                rawQuery.close();
                rawQuery = null;
            }
        }

        return sList;

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL(CREATE_TAB_SLEEP);
        db.execSQL(CREATE_TAB_XINLV);

    }

}
