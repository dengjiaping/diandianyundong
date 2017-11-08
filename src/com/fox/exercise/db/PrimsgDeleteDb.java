package com.fox.exercise.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PrimsgDeleteDb extends SQLiteOpenHelper {
    private SQLiteDatabase db = null;
    private static PrimsgDeleteDb mInstance = null;
    public final static String DB_NAME = "primsg_delete.db";
    public final static String TAB_NAME = "primsg_delete";
    public final static String ID = "_id";
    public final static String NAME = "name";
    public String CREATE_TAB = "create table " + TAB_NAME + " (_id integer primary key autoincrement," +
            NAME + " text)";

    private PrimsgDeleteDb(Context context) {
        super(context, DB_NAME, null, 1);
    }

    public synchronized static PrimsgDeleteDb getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new PrimsgDeleteDb(context);
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        db.execSQL(CREATE_TAB);
    }

    @Override
    public synchronized void close() {
        if (db != null) {
            db.close();
            db = null;
        }
        super.close();
    }

    public void delete(String name) {
        if (db == null) {
            db = getReadableDatabase();
        }
        db.delete(TAB_NAME, "name=?", new String[]{name});
    }

    public void deleteAll() {
        if (db == null) {
            db = getReadableDatabase();
        }
        db.delete(TAB_NAME, null, null);
    }

    public void insert(ContentValues values) {
        if (db == null) {
            db = getWritableDatabase();
        }
        db.insert(TAB_NAME, null, values);
    }

    public Cursor query() {
        if (db == null) {
            db = getWritableDatabase();
        }
        return db.rawQuery("select * from " + TAB_NAME, null);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
        db.execSQL("DROP TABLE IF EXISTS " + TAB_NAME);
        onCreate(db);
    }

}
