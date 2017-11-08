package com.fox.exercise.db;


import com.fox.exercise.db.SportsContent.AdsTable;
import com.fox.exercise.db.SportsContent.PhotoFramsTable;
import com.fox.exercise.db.SportsContent.PrivateMessageAllTable;
import com.fox.exercise.db.SportsContent.PrivateMsgTable;
import com.fox.exercise.db.SportsContent.RankTable;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * @author hanks.han
 */
public class SportsDatabase {

    private static final String TAG = "SportsDatabase";

    /**
     * SQLite Database file name
     */
    private static final String DATABASE_NAME = "sports.db";

    /**
     * SQLite Database version, if you add/delete/modified tables, please update the database version
     * otherwise the database will not be updated.
     */
    private static final int DATABASE_VERSION = 6;

    /**
     * instance
     */
    private static SportsDatabase bDatabaseInstance = null;

    /**
     * database open helper
     */
    private SportsDataHelper sportsDataHelper = null;

    /**
     * context
     *
     * @param context
     */
    public SportsDatabase(Context context) {
        Log.d(TAG, "SportsDatabase initialed");
        sportsDataHelper = new SportsDataHelper(context);
    }

    /**
     * get database
     *
     * @param context
     * @return bDatabaseInstance database instance
     */
    public static synchronized SportsDatabase getInstance(Context context) {
        if (null == bDatabaseInstance) {
            bDatabaseInstance = new SportsDatabase(context);
        }
        return bDatabaseInstance;
    }

    public SQLiteOpenHelper getSqLiteOpenHelper() {
        return sportsDataHelper;
    }

    /**
     * @param isWriteable which kind of database to return
     * @return true, return the writable database. false, return the readable
     * database.
     */
    public SQLiteDatabase getDB(boolean isWriteable) {
        if (isWriteable) {
            return sportsDataHelper.getWritableDatabase();
        } else {
            return sportsDataHelper.getReadableDatabase();
        }

    }

    /**
     * close the db
     */
    public void close() {
        if (null != bDatabaseInstance) {
            sportsDataHelper.close();
            bDatabaseInstance = null;
        }
    }

    /**
     * SQLiteOpenHelper
     */
    private static class SportsDataHelper extends SQLiteOpenHelper {

        public SportsDataHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            Log.d(TAG, "SportsDataHelper initialed");
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(TAG, "-------------------------Create Database.");
            createTables(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d(TAG, "-------------------------Upgrade Database.");
            //dropTables(db);
            //onCreate(db);
            ChangePrivateMsgTable(db);
        }

    }

    private static void ChangePrivateMsgTable(SQLiteDatabase db) {
        db.execSQL(PrivateMsgTable.getTempTableSQL());
        db.execSQL(PrivateMsgTable.getGeneratedSQL());
        db.execSQL(PrivateMsgTable.InsertStringTableSQL());
        db.execSQL(PrivateMsgTable.DropTempTableSQL());
    }

    /**
     * create all tables
     *
     * @param db
     */
    private static void createTables(SQLiteDatabase db) {
        db.execSQL(PrivateMsgTable.getGeneratedSQL());
        db.execSQL(AdsTable.getGeneratedSQL());
        db.execSQL(RankTable.getGeneratedSQLWeek());
        db.execSQL(RankTable.getGeneratedSQLMonth());
        db.execSQL(RankTable.getGeneratedSQLQuarter());
        db.execSQL(RankTable.getGeneratedSQLTotal());
        db.execSQL(PrivateMessageAllTable.getGeneratedSQL());
        db.execSQL(PhotoFramsTable.getGeneratedSQL());
        Log.d(TAG, "execSQL CREATE Table ");
    }

    /**
     * drop all tables
     *
     * @param db
     */
//	private static void dropTables(SQLiteDatabase db) {
//		db.execSQL(PrivateMsgTable.getDropTableSQL());
//		db.execSQL(AdsTable.getDropTableSQL());
//		db.execSQL(RankTable.getDropTableSQLWeek());
//		db.execSQL(RankTable.getDropTableSQLMonth());
//		db.execSQL(RankTable.getDropTableSQLQuarter());
//		db.execSQL(RankTable.getDropTableSQLTotal());
//		db.execSQL(PrivateMessageAllTable.getDropTableSQL());
//		db.execSQL(PhotoFramsTable.getDropTableSQL());
//	}
}
