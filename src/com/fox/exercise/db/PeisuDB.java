package com.fox.exercise.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.fox.exercise.R;
import com.fox.exercise.api.entity.GetPeisu;
import com.fox.exercise.api.entity.PeisuInfo;
import com.fox.exercise.util.SportTrajectoryUtilGaode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangjian on 2016/8/5.
 */
public class PeisuDB extends SQLiteOpenHelper {
    private Context context;
    private static PeisuDB mInstance = null;
    private SQLiteDatabase db = null;
    public final static String DB_NAME = "peisu.db";
    public final static String TB_NAME = "peisu_table";
    public final static String GongLi = "gongli";
    public final static String Peisu = "peisu";
    public final static String Time = "yongshi";
    public final static String SPORT_MARKCODE = "sport_markcode";// 运动唯一标示码
    public final static String SPORT_ISUPLOAD = "sport_isupload";// 是否上传到服务器
    public final static String GPS_TYPE = "gps_type";// 是否有gps
    public String CREATE_TAble = "create table if not exists " + TB_NAME + "(_id primary key,"
            + GongLi + " text," + Peisu + " text," + Time + " text," + SPORT_MARKCODE + " text,"+ GPS_TYPE + " text,"  + SPORT_ISUPLOAD + " text" + ")";

    public synchronized static PeisuDB getmInstance(Context context) {
        if (mInstance == null) {
            mInstance = new PeisuDB(context);
        }
        return mInstance;
    }


    public PeisuDB(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        db.execSQL(CREATE_TAble);
    }

    /**
     * 插入数据
     */
    public int insert(ContentValues values) {
        if (db == null) {
            db = getWritableDatabase();
        }
        int id = (int) db.insert(TB_NAME, null, values);
//        if (id > 0) {
//            Log.i("数据库插入", "----------配速插入数据成功!id=" + id);
//            Toast.makeText(
//                    context,
//                    context.getResources().getString(
//                            R.string.sports_save_local_success),
//                    Toast.LENGTH_SHORT).show();
//        } else {
//            Log.i("数据库插入", "----------配速数据库插入失败!id=" + id);
//            Toast.makeText(
//                    context,
//                    context.getResources().getString(
//                            R.string.sports_save_local_failed),
//                    Toast.LENGTH_SHORT).show();
//        }
        return id;
    }

    /**
     * 根据运动唯一标识查询数据库
     */
    public List<GetPeisu> selectAll(String bs) {
        if (db == null) {

            db = getReadableDatabase();
        }
        List<GetPeisu> list = new ArrayList<GetPeisu>();
        Cursor c = null;
        try {

            c = db.rawQuery("select * from " + TB_NAME + " where SPORT_MARKCODE= ?", new String[]{bs});
            if (c.moveToFirst()) {
                do {
                    String gongli = c.getString(c.getColumnIndex(PeisuDB.GongLi));
                    String peisu = c.getString(c.getColumnIndex(PeisuDB.Peisu));
                    String time = c.getString(c.getColumnIndex(PeisuDB.Time));
                    String gps_type=c.getString(c.getColumnIndex(PeisuDB.GPS_TYPE));
                    List<String> gps_list=new ArrayList<String>();
                    if(gps_type!=null&&!"".equals(gps_type)){
                        gps_list = SportTrajectoryUtilGaode.strToList(gps_type);
                    }
                    List<String> listgongl = SportTrajectoryUtilGaode.strToList(gongli);
                    List<String> listpeis = SportTrajectoryUtilGaode.strToList(peisu);
                    List<String> listtime = SportTrajectoryUtilGaode.strToList(time);
                    if ((listgongl.size() == listpeis.size()) && (listpeis.size() == listtime.size())) {
                        GetPeisu getPeisu;
                        for (int i = 0; i < listgongl.size(); i++) {
                            getPeisu = new GetPeisu();
                            getPeisu.setSport_distance(listgongl.get(i));
                            getPeisu.setSprots_velocity(listpeis.get(i));
                            getPeisu.setSprots_time(listtime.get(i));
                            getPeisu.setgPS_type(gps_list.get(i));
                            list.add(getPeisu);
                        }
                    }

                    gongli = null;
                    peisu = null;
                    time = null;
                    listgongl = null;
                    listpeis = null;
                    listtime = null;


                } while (c.moveToNext());
            }
        } catch (Exception e) {

        } finally {
            if(c!=null){
                c.close();
            }
        }
        return list;
    }

    /**
     * 根据运动唯一标识查询数据库、得到是否上传服务器
     */
    public String selectisUpload(String bs) {
        if (db == null) {
            db = getReadableDatabase();
        }

        String isUpload =null;
        Cursor c = null;
        try {

            c = db.rawQuery("select * from " + TB_NAME + " where SPORT_MARKCODE= ?", new String[]{bs});
            if (c.moveToFirst()) {
                do {
                    isUpload = c.getString(c.getColumnIndex(PeisuDB.SPORT_ISUPLOAD));
                } while (c.moveToNext());
            }
        } catch (Exception e) {

        } finally {
            if(c!=null){
                c.close();
            }
        }
        return isUpload;
    }




    //更新数据库的配速数据
    public int update(ContentValues values, String bs, Boolean isShow) {
        if (db == null) {
            db = getWritableDatabase();
        }
        long id = db.update(TB_NAME, values, "SPORT_MARKCODE=?", new String[]{bs});
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


    public boolean deleteSportByMarkCode(String sport_markcode) {
        boolean result = true;
        if (db == null) {
            db = getWritableDatabase();
        }
        int id = db.delete(TB_NAME, "sport_markcode=?",
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

    /**
     * 删除数据库
     *
     * @return
     */
    public int delete() {
        if (db == null) {
            db = getWritableDatabase();
        }
        int id = db.delete(TB_NAME, null, null);
        return id;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
