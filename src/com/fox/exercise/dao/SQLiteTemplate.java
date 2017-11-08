package com.fox.exercise.dao;

import java.util.ArrayList;
import java.util.List;

import com.fox.exercise.api.entity.PrivateMsgStatus;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class SQLiteTemplate {
    private static final String TAG = "SQLiteTemplate";
    private SQLiteOpenHelper sportsDatabasbOpenHelper;
    private String mPrimaryKey;

    public SQLiteTemplate(SQLiteOpenHelper sportsDataHelper) {
        this.sportsDatabasbOpenHelper = sportsDataHelper;
    }

    public SQLiteTemplate(SQLiteOpenHelper sportsDataHelper, String primaryKey) {
        this(sportsDataHelper);
        setPrimaryKey(primaryKey);
    }


    public void insertPrivateMsgAll(String table, ContentValues values) {

        getDb(true).insert(table, null, values);
    }


    // 根据uid查询单条数据
    public Cursor queryPrivateMsgByUID(String table, int uid) {

        return getDb(false).rawQuery("select * from " + table + " where " + "uid=?",
                new String[]{String.valueOf(uid)});
    }

    public PrivateMsgStatus queryPrivateMsgByUidAndName(RowMapper<PrivateMsgStatus> rowMapper, String table, int uid, String name) {
        PrivateMsgStatus status = null;
        final Cursor cursor = getDb(false).rawQuery("select * from " + table + " where " + " touid=? " + " AND " + " name=? ",
                new String[]{String.valueOf(uid), name});
        Log.v(TAG, "cursor :" + cursor);
        Log.v(TAG, "cursor cont" + cursor.getCount());
        try {
            while (cursor.moveToNext()) {
                Log.e(TAG, "cursor status" + status);
                status = rowMapper.mapRow(cursor, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return status;

    }

    // 查询所有的数据
    public <T> List<T> queryPrivateMsgAll(RowMapper<T> rowMapper, String table, int touid) {
        List<T> list = new ArrayList<T>();
        final Cursor cursor = getDb(false).rawQuery(
                "select * from " + table + " where " + "touid=?" + " order by " + " addTime " + " desc ",
                new String[]{String.valueOf(touid)});
        try {
            while (cursor.moveToNext()) {
                list.add(rowMapper.mapRow(cursor, 1));

            }
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            cursor.close();
        }
        return list;
    }

    public void deletePrivateMsgByUID(String table, int uid) {
        getDb(true).delete(table, "uid=?", new String[]{String.valueOf(uid)});
    }

    public void deletePrivateMsgAll(String table) {
        getDb(true).delete(table, null, null);
    }

    public void deletePrivateMsg(String table, int uid) {
        getDb(true).delete(table, "uid=?", new String[]{String.valueOf(uid)});
    }


    public void deletePrivateMsg(String table, int uid, int touid) {
        getDb(true).delete(table, "(( " + " uid=? " + " AND " + " touid=? " + " ) "
                + " OR " + "( " + " touid=? " + " AND " + " uid=? " + " )) ", new String[]{Integer.toString(uid), Integer.toString(touid),
                Integer.toString(touid), Integer.toString(uid)});
    }

    /**
     * @param rowMapper
     * @param table
     * @param columns
     * @param selection
     * @param selectionArgs
     * @param groupBy
     * @param having
     * @param orderBy
     * @return
     */
    public <T> List<T> queryForPrivateMSGList(RowMapper<T> rowMapper, String table, String[] columns,
                                              String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        List<T> list = new ArrayList<T>();

        final Cursor cursor = getDb(false).query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
        try {
            while (cursor.moveToNext()) {
                list.add(rowMapper.mapRow(cursor, 1));

            }
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            cursor.close();
        }
        return list;

    }

    public <T> List<T> rawQueryForPrivateMSGList(RowMapper<T> rowMapper, String sql, String[] selectionArgs) {
        List<T> list = new ArrayList<T>();
        final Cursor cursor = getDb(false).rawQuery(sql, selectionArgs);
//		final Cursor cursor=getDb(false).query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
        try {
            while (cursor.moveToNext()) {
                list.add(rowMapper.mapRow(cursor, 1));

            }
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            cursor.close();
        }
        return list;

    }

    /**
     * @param rowMapper
     * @param table
     * @param columns
     * @param selection
     * @param selectionArgs
     * @param groupBy
     * @param having
     * @param orderBy
     * @return
     */
    public <T> List<T> queryForAdsList(RowMapper<T> rowMapper, String table, String[] columns,
                                       String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        List<T> list = new ArrayList<T>();
        final Cursor cursor = getDb(false).query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
        try {
            while (cursor.moveToNext()) {
                list.add(rowMapper.mapRow(cursor, 1));

            }
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            cursor.close();
        }
        return list;

    }

    public int updateAdsInfoByID(String table, String id, ContentValues values) {
        return getDb(true).update(table, values, mPrimaryKey + " = ?", new String[]{id});

    }

    /**
     * @param table which table to be queried
     * @param id    the data id use as the query param
     * @return true if the data is in db
     */
    public boolean isExistById(String table, String id) {
        return isExistById(table, mPrimaryKey, id);
    }

    /**
     * @param table       which table to be queried
     * @param mPrimaryKey add the mPrimaryKey as param
     * @param id          the data id use as the query param
     * @return
     */
    private boolean isExistById(String table, String mPrimaryKey, String id) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT COUNT(*) FROM ").append(table)
                .append(" WHERE ").append(mPrimaryKey).append(" =?");

        return isExistBySQL(sqlBuilder.toString(), new String[]{id});
    }

    /**
     * @param sql           the generated query sql
     * @param selectionArgs query values which will replace the where clause params
     * @return
     */
    public boolean isExistBySQL(String sql, String[] selectionArgs) {
        boolean result = false;
        final Cursor cursor = getDb(false).rawQuery(sql, selectionArgs);
        try {
            if (cursor.moveToFirst()) {
                result = (cursor.getInt(0) > 0);
            }
        } finally {
            cursor.close();
        }
        return result;
    }

    /**
     * @param primaryKey set the primaryKey for query
     */
    private void setPrimaryKey(String primaryKey) {
        this.mPrimaryKey = primaryKey;
    }

    /**
     * Get Database Connection
     *
     * @param writeable
     * @return
     */
    public SQLiteDatabase getDb(boolean writeable) {
        if (writeable) {
            return sportsDatabasbOpenHelper.getWritableDatabase();
        } else {
            return sportsDatabasbOpenHelper.getReadableDatabase();
        }
    }

    /**
     * row mapper
     *
     * @param <T>
     */
    public interface RowMapper<T> {
        public T mapRow(Cursor cursor, int rowNum);
    }
}
