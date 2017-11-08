package com.fox.exercise.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.view.PagerTitleStrip;
import android.util.Log;
import android.widget.Toast;

import com.fox.exercise.R;
import com.fox.exercise.api.entity.FindMore2;
import com.fox.exercise.newversion.newact.NewCommentsActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/7/6.
 */
public class SportShowDb extends SQLiteOpenHelper {
    private Context context;
    private static SportShowDb mInstance = null;
    private SQLiteDatabase db = null;
    public final static String DB_NAME = "sportshow.db";
    public final static String TB_NAME = "spotshow_table";
    public final static String TYPE = "type";
    public final static String IMG = "picPath";
    public final static String FINDID = "findId";
    public final static String COMMENTID = "commentId";
    public final static String UID = "uId";
    public final static String CONTENT = "content";
    public final static String ADDRESS = "wavAddress";
    public final static String WAVTIME = "wavTime";
    public final static String INPUTTIME = "inputTime";
    public final static String NAME = "name";
    public final static String USERIMG = "userImg";
    public final static String TIME = "time";
    public final static String IS_DELETE = "is_delete";
    public final static String ZID = "zid";
    public String CREATE_TABLE = "create table if not exists " + TB_NAME + "(_id integer primary key ,"
            + TYPE + " integer," + IMG + " text," + FINDID + " text," + COMMENTID + " text," + UID + " text,"
            + CONTENT + " text," + ADDRESS + " text," + WAVTIME + " text," + INPUTTIME + " text," + NAME
            + " text," + USERIMG + " text," + TIME + " text," + IS_DELETE + " integer," + ZID + " text" + " )";

    public synchronized static SportShowDb getmInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SportShowDb(context);
        }
        return mInstance;
    }

    public SportShowDb(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        db.execSQL(CREATE_TABLE);

    }

    /**
     * @param values
     * @param isShow
     * @return 插入数据库
     */
    public int insert(ContentValues values, Boolean isShow) {
        if (db == null) {
            db = getWritableDatabase();
        }
        int id = (int) db.insert(TB_NAME, null, values);
        if (id > 0) {

            Log.i("数据库插入", "插入数据成功!id=" + id);
            if (isShow) {
                Toast.makeText(
                        context,
                        context.getResources().getString(
                                R.string.sports_save_local_success),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.i("数据库插入", "数据库插入失败!id=" + id);
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

    /**
     * 查询数据库所有数据
     *
     * @return
     */
    public Cursor query(String id, boolean flag) {
        if (db == null) {

            db = getReadableDatabase();
        }

        if (flag) {
            return db.rawQuery("select * from " + TB_NAME + " where " + COMMENTID + "= ?", new String[]{id});
        } else {
            return db.rawQuery("select * from " + TB_NAME + " where " + ZID + "= ?", new String[]{id});
        }
    }


    public List<FindMore2> getContent(int page) {
        if (db == null) {
            db = getReadableDatabase();
        }
        List<FindMore2> list = new ArrayList<FindMore2>();
        Cursor c = null;
        c = db.rawQuery("select * from " + TB_NAME + " order by " + INPUTTIME + " desc limit ?,?", new String[]{Integer.toString(page * 20), Integer.toString(20)});
        if (c.moveToFirst()) {
            do {
                FindMore2 findMore2 = new FindMore2();
                findMore2.setType(c.getInt(c.getColumnIndex(SportShowDb.TYPE)));
                findMore2.setPicPath(c.getString(c.getColumnIndex(SportShowDb.IMG)));
                findMore2.setFindId(c.getString(c.getColumnIndex(SportShowDb.FINDID)));
                findMore2.setCommentId(c.getString(c.getColumnIndex(SportShowDb.COMMENTID)));
                findMore2.setuId(c.getString(c.getColumnIndex(SportShowDb.UID)));
                findMore2.setContent(c.getString(c.getColumnIndex(SportShowDb.CONTENT)));
                findMore2.setWavAddress(c.getString(c.getColumnIndex(SportShowDb.ADDRESS)));
                findMore2.setWavTime(c.getString(c.getColumnIndex(SportShowDb.WAVTIME)));
                findMore2.setInputTime(c.getString(c.getColumnIndex(SportShowDb.INPUTTIME)));
                findMore2.setName(c.getString(c.getColumnIndex(SportShowDb.NAME)));
                findMore2.setUserImg(c.getString(c.getColumnIndex(SportShowDb.USERIMG)));
                findMore2.setTime(c.getString(c.getColumnIndex(SportShowDb.TIME)));
                findMore2.setIs_delete(c.getInt(c.getColumnIndex(SportShowDb.IS_DELETE)));
                list.add(findMore2);
            } while (c.moveToNext());
        }
        if (c != null) {
            c.close();
        }
        return list;
    }


    /**
     * @return 删除数据库
     */
    public int delete() {
        if (db == null) {
            db = getWritableDatabase();
        }
        int id = db.delete(TB_NAME, null, null);
//        if (id > 0) {
//            Log.i("数据库删除", "删除数据成功，id=" + id);
//        } else {
//            Log.i("数据库删除", "删除数据失败，id=" + id);
//        }
        return id;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
