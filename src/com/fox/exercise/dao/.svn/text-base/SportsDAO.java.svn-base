package com.fox.exercise.dao;

import java.util.ArrayList;
import java.util.List;

import com.fox.exercise.api.entity.Ads;
import com.fox.exercise.api.entity.LocalPhotoFrames;
import com.fox.exercise.api.entity.PhotoFrames;
import com.fox.exercise.api.entity.PicsAndIds;
import com.fox.exercise.api.entity.PrivateMsgStatus;
import com.fox.exercise.api.entity.UserPrimsgAll;
import com.fox.exercise.api.entity.UserPrimsgOne;
import com.fox.exercise.dao.SQLiteTemplate.RowMapper;
import com.fox.exercise.db.SportsContent.AdsTable;
import com.fox.exercise.db.SportsContent.PhotoFramsTable;
import com.fox.exercise.db.SportsContent.PrivateMessageAllTable;
import com.fox.exercise.db.SportsContent.PrivateMsgTable;
import com.fox.exercise.db.SportsContent.RankTable;
import com.fox.exercise.db.SportsDatabase;


import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SportsDAO {
    private static final String TAG = "SportsDAO";

    private SQLiteTemplate sqLiteTemplate;

    public SportsDAO(Context context) {
        Log.d(TAG, "SportsDAO initial");
        this.sqLiteTemplate = new SQLiteTemplate(SportsDatabase.getInstance(context).getSqLiteOpenHelper());

    }

    public int insertPrivateMsgAll(String table, List<UserPrimsgAll> list, String status) {
        int result = 0;
        SQLiteDatabase db = sqLiteTemplate.getDb(true);
        try {
            db.beginTransaction();
            for (UserPrimsgAll pms : list) {
                db.execSQL("replace into " + PrivateMessageAllTable.TABLE_NAME + " ("
                        + PrivateMessageAllTable.Columns.SEX + "," + PrivateMessageAllTable.Columns.UIMG + ","
                        + PrivateMessageAllTable.Columns.NAME + "," + PrivateMessageAllTable.Columns.BIRTHDAY + ","
                        + PrivateMessageAllTable.Columns.UID + "," + PrivateMessageAllTable.Columns.ADDTIME + "," + PrivateMessageAllTable.Columns.COUNTS + ","
                        + PrivateMessageAllTable.Columns.TOUID + "," + PrivateMessageAllTable.Columns.STATUS
                        + ") values (\"" + pms.getSex() + "\",\"" + pms.getUimg() + "\",\"" + pms.getName() + "\",\""
                        + pms.getBirthday() + "\"," + pms.getUid() + "," + pms.getAddTime() + "," + pms.getCounts() + "," + pms.getTouid()
                        + "," + "\"" + status + "\" )");
                // long id =
                // db.insertWithOnConflict(PrivateMessageAllTable.TABLE_NAME,
                // null,
                // privateMSGAllToContentValues(pms),
                // SQLiteDatabase.CONFLICT_IGNORE);
                // if (-1 == id) {
                // Log.d(TAG, "insert to db failed, pms=" + pms.toString());
                // } else {
                // ++result;
                // / Log.d(TAG,
                // "insert to db SUCCESSFULLY, pms=" + pms.toString());
                // }
            }
            db.setTransactionSuccessful();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        // sqLiteTemplate.insertPrivateMsgAll(table, values);
        return result;
    }

    public int insertPrivateMsg(String table, UserPrimsgAll pms, String status) {
        int result = 0;
        SQLiteDatabase db = sqLiteTemplate.getDb(true);
        try {

            db.execSQL("replace into " + PrivateMessageAllTable.TABLE_NAME + " (" + PrivateMessageAllTable.Columns.SEX
                    + "," + PrivateMessageAllTable.Columns.UIMG + "," + PrivateMessageAllTable.Columns.NAME + ","
                    + PrivateMessageAllTable.Columns.BIRTHDAY + "," + PrivateMessageAllTable.Columns.UID + ","
                    + PrivateMessageAllTable.Columns.ADDTIME + "," + PrivateMessageAllTable.Columns.TOUID + ","
                    + PrivateMessageAllTable.Columns.STATUS + ") values (\"" + pms.getSex() + "\",\"" + pms.getUimg()
                    + "\",\"" + pms.getName() + "\",\"" + pms.getBirthday() + "\"," + pms.getUid() + ","
                    + pms.getAddTime() + "," + pms.getTouid() + "," + "\"" + status + "\" )");

        } catch (Exception e) {
            e.printStackTrace();
        }

        // sqLiteTemplate.insertPrivateMsgAll(table, values);
        return result;
    }

    // 根据uid查询单条数据
    public Cursor queryPrivateMsgByUID(String table, int uid) {

        return sqLiteTemplate.queryPrivateMsgByUID(table, uid);
    }

    public PrivateMsgStatus queryPrivateMsgByUidAndName(String table, int uid, String name) {
        return sqLiteTemplate.queryPrivateMsgByUidAndName(privateMSGAllowMapper, table, uid, name);
    }

    // 查询所有的数据
    public List<PrivateMsgStatus> queryPrivateMsgAll(String table, int touid) {

        return sqLiteTemplate.queryPrivateMsgAll(privateMSGAllowMapper, table, touid);
    }

    public void deletePrivateMsgAllByUID(String table, int uid) {
        sqLiteTemplate.deletePrivateMsgByUID(table, uid);
    }

    public void deletePrivateMsgAll(String table) {
        sqLiteTemplate.deletePrivateMsgAll(table);
    }

    public void deletePrivateMsg(String table, int uid) {
        sqLiteTemplate.deletePrivateMsg(table, uid);
    }

    public void deletePrivateMsgByUID(String table, int uid, int toUid) {
        sqLiteTemplate.deletePrivateMsg(table, uid, toUid);
    }

    public List<UserPrimsgOne> retrievePrivateMSG(String[] projections, String selection, String[] selectionArgs,
                                                  String order) {

        return sqLiteTemplate.queryForPrivateMSGList(privateMSGRowMapper, PrivateMsgTable.TABLE_NAME, projections,
                selection, selectionArgs, null, null, order);

    }

    public List<UserPrimsgOne> retrievePrivateMSG(String sql, String[] selectionArgs) {

        return sqLiteTemplate.rawQueryForPrivateMSGList(privateMSGRowMapper, sql, selectionArgs);

    }

    public long savePrivateMsgInfo(UserPrimsgOne priMsg) {

        return sqLiteTemplate.getDb(true).insert(PrivateMsgTable.TABLE_NAME, null, privateMSGToContentValues(priMsg));

    }

    public int savePrivateMsgInfo(List<UserPrimsgOne> msgList) {
        int result = 0;
        SQLiteDatabase db = sqLiteTemplate.getDb(true);
        try {
            db.beginTransaction();
            for (UserPrimsgOne msg : msgList) {
                long id = db.replace(PrivateMsgTable.TABLE_NAME, null, privateMSGToContentValues(msg));
                if (-1 == id) {
                    Log.d(TAG, "insert to db failed, msg=" + msg.toString());
                } else {
                    ++result;
                    Log.d(TAG, "insert to db SUCCESSFULLY, msg=" + msg.toString());
                }
            }
            db.setTransactionSuccessful();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        return result;

    }

    public List<Ads> retrieveAds() {

        return sqLiteTemplate.queryForAdsList(adsRowMapper, AdsTable.TABLE_NAME, null, null, null, null, null, null);

    }

    /**
     * check if ad exist in db
     *
     * @param ad
     * @return
     */
    public boolean isExist(Ads ad) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ").append(AdsTable.TABLE_NAME).append(" WHERE ").append(AdsTable.Columns.ID)
                .append(" =? ");

        return sqLiteTemplate.isExistBySQL(sql.toString(), new String[]{Integer.toString(ad.getId())});
    }

    /**
     * update ads info
     *
     * @param ad
     * @return
     */
    public int updateAdsInfo(Ads ad) {
        return updateAdsInfo(ad.getId(), adsToContentValues(ad));
    }

    /**
     * update ads info by ads id
     *
     * @param id
     * @param values
     * @return
     */
    private int updateAdsInfo(int id, ContentValues values) {
        return sqLiteTemplate.updateAdsInfoByID(AdsTable.TABLE_NAME, Integer.toString(id), values);
    }

    /**
     * save ads info, check if already exist in db, if true,update, else insert.
     *
     * @param ad
     * @return
     */
    public long saveAdsInfo(Ads ad) {
        if (isExist(ad)) {
            return updateAdsInfo(ad);
        } else {
            return sqLiteTemplate.getDb(true).insert(AdsTable.TABLE_NAME, null, adsToContentValues(ad));
        }

    }

    /**
     * save a ads list into db
     *
     * @param ads ads list to be inserted to db
     * @return result the row ID of the newly inserted row OR the primary key of
     * the existing row if the input param 'conflictAlgorithm' =
     * CONFLICT_IGNORE OR -1 if any error
     */
    public int saveAdsInfo(List<Ads> ads) {
        int result = 0;
        SQLiteDatabase db = sqLiteTemplate.getDb(true);
        try {
            db.beginTransaction();
            for (Ads ad : ads) {
                long id = db.insertWithOnConflict(AdsTable.TABLE_NAME, null, adsToContentValues(ad),
                        SQLiteDatabase.CONFLICT_IGNORE);
                if (-1 == id) {
                    Log.d(TAG, "insert to db failed, ads=" + ad.toString());
                } else {
                    ++result;
                    Log.d(TAG, "insert to db SUCCESSFULLY, ads=" + ad.toString());
                }
            }
            db.setTransactionSuccessful();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        return result;

    }

    /**
     * convert ads to content values
     *
     * @param ad
     * @return
     */
    private ContentValues adsToContentValues(Ads ad) {
        final ContentValues values = new ContentValues();
        values.put(AdsTable.Columns.ID, ad.getId());
        values.put(AdsTable.Columns.TITLE, ad.getTitle());
        values.put(AdsTable.Columns.IMG_URL, ad.getImgUrl());
        values.put(AdsTable.Columns.URL, ad.getUrl());
        return values;
    }

    private static final RowMapper<Ads> adsRowMapper = new RowMapper<Ads>() {

        @Override
        public Ads mapRow(Cursor cursor, int rowNum) {
            Ads ads = new Ads();
            ads.setId(cursor.getInt(cursor.getColumnIndex(AdsTable.Columns.ID)));
            ads.setTitle(cursor.getString(cursor.getColumnIndex(AdsTable.Columns.TITLE)));
            ads.setImgUrl(cursor.getString(cursor.getColumnIndex(AdsTable.Columns.IMG_URL)));
            ads.setUrl(cursor.getString(cursor.getColumnIndex(AdsTable.Columns.URL)));
            return ads;
        }

    };
    private static final RowMapper<PrivateMsgStatus> privateMSGAllowMapper = new RowMapper<PrivateMsgStatus>() {

        @Override
        public PrivateMsgStatus mapRow(Cursor cursor, int rowNum) {
            PrivateMsgStatus privateMsgStatus = new PrivateMsgStatus();
            UserPrimsgAll privateMSG = new UserPrimsgAll();
            privateMSG.setSex(cursor.getString(cursor.getColumnIndex(PrivateMessageAllTable.Columns.SEX)));
            privateMSG.setUimg(cursor.getString(cursor.getColumnIndex(PrivateMessageAllTable.Columns.UIMG)));
            privateMSG.setName(cursor.getString(cursor.getColumnIndex(PrivateMessageAllTable.Columns.NAME)));
            privateMSG.setBirthday(cursor.getString(cursor.getColumnIndex(PrivateMessageAllTable.Columns.BIRTHDAY)));
            privateMSG.setUid(cursor.getInt(cursor.getColumnIndex(PrivateMessageAllTable.Columns.UID)));
            privateMSG.setTouid(cursor.getInt(cursor.getColumnIndex(PrivateMessageAllTable.Columns.TOUID)));
            privateMSG.setAddTime(cursor.getInt(cursor.getColumnIndex(PrivateMessageAllTable.Columns.ADDTIME)));
            privateMSG.setCounts(cursor.getInt(cursor.getColumnIndex(PrivateMessageAllTable.Columns.COUNTS)));
            privateMsgStatus.setUserPrimsgAll(privateMSG);
            privateMsgStatus
                    .setMsgStatus(cursor.getString(cursor.getColumnIndex(PrivateMessageAllTable.Columns.STATUS)));
            return privateMsgStatus;
        }

    };

//	private ContentValues privateMSGAllToContentValues(UserPrimsgAll msg) {
//		final ContentValues values = new ContentValues();
//		values.put(PrivateMessageAllTable.Columns.SEX, msg.getSex());
//		values.put(PrivateMessageAllTable.Columns.UIMG, msg.getUimg());
//		values.put(PrivateMessageAllTable.Columns.NAME, msg.getName());
//		values.put(PrivateMessageAllTable.Columns.BIRTHDAY, msg.getBirthday());
//		values.put(PrivateMessageAllTable.Columns.UID, msg.getUid());
//		values.put(PrivateMessageAllTable.Columns.TOUID, msg.getTouid());
//		values.put(PrivateMessageAllTable.Columns.ADDTIME, msg.getAddTime());
//		return values;
//	}

    /**
     * convert ads to content values
     *
     * @param ad
     * @return
     */
    private ContentValues privateMSGToContentValues(UserPrimsgOne msg) {
        final ContentValues values = new ContentValues();
        values.put(PrivateMsgTable.Columns.UID, msg.getUid());
        values.put(PrivateMsgTable.Columns.TOUID, msg.getTouid());
        values.put(PrivateMsgTable.Columns.ADDTIME, msg.getAddTime());
        values.put(PrivateMsgTable.Columns.COMMENT_TEXT, msg.getCommentText());
        values.put(PrivateMsgTable.Columns.COMMENT_WAV, msg.getCommentWav());
        values.put(PrivateMsgTable.Columns.COMMENT_DURATION, msg.getWavDuration());
        values.put(PrivateMsgTable.Columns.OWNERID, msg.getOwnerid());
        return values;
    }

    private static final RowMapper<UserPrimsgOne> privateMSGRowMapper = new RowMapper<UserPrimsgOne>() {

        @Override
        public UserPrimsgOne mapRow(Cursor cursor, int rowNum) {
            UserPrimsgOne privateMSG = new UserPrimsgOne();
            privateMSG.setUid(cursor.getInt(cursor.getColumnIndex(PrivateMsgTable.Columns.UID)));
            privateMSG.setTouid(cursor.getInt(cursor.getColumnIndex(PrivateMsgTable.Columns.TOUID)));
            privateMSG.setAddTime(cursor.getLong(cursor.getColumnIndex(PrivateMsgTable.Columns.ADDTIME)));
            privateMSG.setCommentText(cursor.getString(cursor.getColumnIndex(PrivateMsgTable.Columns.COMMENT_TEXT)));
            privateMSG.setCommentWav(cursor.getString(cursor.getColumnIndex(PrivateMsgTable.Columns.COMMENT_WAV)));
            privateMSG.setWavDuration(cursor.getInt(cursor.getColumnIndex(PrivateMsgTable.Columns.COMMENT_DURATION)));
            return privateMSG;
        }

    };

    public void updateRankInfo(List<PicsAndIds> weekList, List<PicsAndIds> monthList, List<PicsAndIds> quarterList,
                               List<PicsAndIds> totalList) {
        SQLiteDatabase db = sqLiteTemplate.getDb(true);
        db.beginTransaction();
        db.execSQL("delete from " + RankTable.TABLE_NAME_WEEK);
        for (int i = 0; i < weekList.size(); i++) {
            String sql = "insert into " + RankTable.TABLE_NAME_WEEK + " (" + RankTable.Colunms.ID + ","
                    + RankTable.Colunms.IMG_URL + "," + RankTable.Colunms.LIKES + ") values(" + weekList.get(i).getId()
                    + "," + "\"" + weekList.get(i).getImgUrl() + "\"" + "," + weekList.get(i).getLikes() + ");";
            db.execSQL(sql);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        Log.d("dao", "save week info finish");
        db.beginTransaction();
        db.execSQL("delete from " + RankTable.TABLE_NAME_MONTH);
        for (int i = 0; i < monthList.size(); i++) {
            String sql = "insert into " + RankTable.TABLE_NAME_MONTH + " (" + RankTable.Colunms.ID + ","
                    + RankTable.Colunms.IMG_URL + "," + RankTable.Colunms.LIKES + ") values("
                    + monthList.get(i).getId() + "," + "\"" + monthList.get(i).getImgUrl() + "\"" + ","
                    + monthList.get(i).getLikes() + ");";
            db.execSQL(sql);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        Log.d("dao", "save month info finish");
        db.beginTransaction();
        db.execSQL("delete from " + RankTable.TABLE_NAME_QUARTER);
        for (int i = 0; i < quarterList.size(); i++) {
            String sql = "insert into " + RankTable.TABLE_NAME_QUARTER + " (" + RankTable.Colunms.ID + ","
                    + RankTable.Colunms.IMG_URL + "," + RankTable.Colunms.LIKES + ") values("
                    + quarterList.get(i).getId() + "," + "\"" + quarterList.get(i).getImgUrl() + "\"" + ","
                    + quarterList.get(i).getLikes() + ");";
            db.execSQL(sql);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        Log.d("dao", "save quarter info finish");
        db.beginTransaction();
        db.execSQL("delete from " + RankTable.TABLE_NAME_TOTAL);
        for (int i = 0; i < totalList.size(); i++) {
            String sql = "insert into " + RankTable.TABLE_NAME_TOTAL + " (" + RankTable.Colunms.ID + ","
                    + RankTable.Colunms.IMG_URL + "," + RankTable.Colunms.LIKES + ") values("
                    + totalList.get(i).getId() + "," + "\"" + totalList.get(i).getImgUrl() + "\"" + ","
                    + totalList.get(i).getLikes() + ");";
            db.execSQL(sql);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        Log.d("dao", "save total info finish");
        db.close();
    }

    public List<PicsAndIds> getRankInfo(String table) {
        List<PicsAndIds> list = new ArrayList<PicsAndIds>();
        String sql = "select * from " + table;
        SQLiteDatabase db = sqLiteTemplate.getDb(true);
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            PicsAndIds p = new PicsAndIds();
            p.setId(cursor.getInt(1));
            p.setImgUrl(cursor.getString(2));
            p.setLikes(cursor.getInt(3));
            list.add(p);
        }
        return list;
    }

    public int insertPhotoFraLmesList(String table, List<PhotoFrames> list, String status) {
        int result = 0;
        SQLiteDatabase db = sqLiteTemplate.getDb(true);
        try {
            db.beginTransaction();
            for (PhotoFrames pms : list) {
                db.execSQL("replace into " + PhotoFramsTable.TABLE_NAME + " (" + PhotoFramsTable.Columns.UID + ","
                        + PhotoFramsTable.Columns.FRAMENAME + "," + PhotoFramsTable.Columns.PHOTOFRAMS + ","
                        + PhotoFramsTable.Columns.ADDTIME + "," + PhotoFramsTable.Columns.TOPLEVEL + "," + PhotoFramsTable.Columns.FRAMETYPE + ","
                        + PhotoFramsTable.Columns.STATUS + ") values (\"" + pms.getId() + "\",\"" + pms.getFrameName()
                        + "\",\"" + pms.getPhotoFrame() + "\",\"" + pms.getAddTime() + "\",\"" + pms.getTopLevel()
                        + "\",\"" + pms.getFrametype() + "\",\"" + status + "\")");
            }
            db.setTransactionSuccessful();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        return result;
    }

    public int insertPhotoFrames(String table, PhotoFrames photoFrames, String status) {
        int result = 0;
        SQLiteDatabase db = sqLiteTemplate.getDb(true);
        try {
            db.execSQL("replace into " + PhotoFramsTable.TABLE_NAME + " (" + PhotoFramsTable.Columns.UID + ","
                    + PhotoFramsTable.Columns.FRAMENAME + "," + PhotoFramsTable.Columns.PHOTOFRAMS + ","
                    + PhotoFramsTable.Columns.ADDTIME + "," + PhotoFramsTable.Columns.TOPLEVEL + "," + PhotoFramsTable.Columns.FRAMETYPE + ","
                    + PhotoFramsTable.Columns.STATUS + ") values (\"" + photoFrames.getId() + "\",\""
                    + photoFrames.getFrameName() + "\",\"" + photoFrames.getPhotoFrame() + "\",\""
                    + photoFrames.getAddTime() + "\",\"" + photoFrames.getTopLevel() + "\",\""
                    + photoFrames.getFrametype() + "\",\"" + status + "\")");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public List<LocalPhotoFrames> queryPhotoFrames(String table, int frameType) {
        List<LocalPhotoFrames> list = new ArrayList<LocalPhotoFrames>();
        SQLiteDatabase db = sqLiteTemplate.getDb(true);
        String sql = "select * from " + PhotoFramsTable.TABLE_NAME + " where " + PhotoFramsTable.Columns.FRAMETYPE + " = " + frameType;
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            PhotoFrames p = new PhotoFrames();
            LocalPhotoFrames b = new LocalPhotoFrames();
            p.setId(cursor.getInt(1));
            p.setFrameName(cursor.getString(2));
            p.setPhotoFrame(cursor.getString(3));
            p.setAddTime(cursor.getInt(4));
            p.setTopLevel(cursor.getInt(5));
            p.setFrametype(cursor.getInt(6));
            b.setPhotoFrames(p);
            b.setStatus(cursor.getString(7));
            list.add(b);
        }
        return list;
    }

    public LocalPhotoFrames queryPhotoFramesByID(String table, int topLevel, int frameType) {
        LocalPhotoFrames localPhotoFrames = new LocalPhotoFrames();
        SQLiteDatabase db = sqLiteTemplate.getDb(true);
        String sql = "select * from " + PhotoFramsTable.TABLE_NAME + " where " + PhotoFramsTable.Columns.UID + " = " + topLevel + " AND " + PhotoFramsTable.Columns.FRAMETYPE + " = " + frameType;

        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            PhotoFrames p = new PhotoFrames();
            p.setId(cursor.getInt(cursor.getColumnIndex(PhotoFramsTable.Columns.UID)));
            p.setFrameName(cursor.getString(cursor.getColumnIndex(PhotoFramsTable.Columns.FRAMENAME)));
            p.setPhotoFrame(cursor.getString(cursor.getColumnIndex(PhotoFramsTable.Columns.PHOTOFRAMS)));
            p.setAddTime(cursor.getInt(cursor.getColumnIndex(PhotoFramsTable.Columns.ADDTIME)));
            p.setTopLevel(cursor.getInt(cursor.getColumnIndex(PhotoFramsTable.Columns.TOPLEVEL)));
            p.setFrametype(cursor.getInt(cursor.getColumnIndex(PhotoFramsTable.Columns.FRAMETYPE)));
            localPhotoFrames.setPhotoFrames(p);
            localPhotoFrames.setStatus(cursor.getString(cursor.getColumnIndex(PhotoFramsTable.Columns.STATUS)));
        }
        return localPhotoFrames;
    }

    public LocalPhotoFrames queryPhotoFramesBIG(String table, int topLevel, int frameType) {
        LocalPhotoFrames localPhotoFrames = new LocalPhotoFrames();
        SQLiteDatabase db = sqLiteTemplate.getDb(true);
        String sql = "select * from " + PhotoFramsTable.TABLE_NAME + " where " + PhotoFramsTable.Columns.TOPLEVEL + " = " + topLevel + " AND " + PhotoFramsTable.Columns.FRAMETYPE + " = " + frameType;

        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            PhotoFrames p = new PhotoFrames();
            p.setId(cursor.getInt(cursor.getColumnIndex(PhotoFramsTable.Columns.UID)));
            p.setFrameName(cursor.getString(cursor.getColumnIndex(PhotoFramsTable.Columns.FRAMENAME)));
            p.setPhotoFrame(cursor.getString(cursor.getColumnIndex(PhotoFramsTable.Columns.PHOTOFRAMS)));
            p.setAddTime(cursor.getInt(cursor.getColumnIndex(PhotoFramsTable.Columns.ADDTIME)));
            p.setTopLevel(cursor.getInt(cursor.getColumnIndex(PhotoFramsTable.Columns.TOPLEVEL)));
            p.setFrametype(cursor.getInt(cursor.getColumnIndex(PhotoFramsTable.Columns.FRAMETYPE)));
            localPhotoFrames.setPhotoFrames(p);
            localPhotoFrames.setStatus(cursor.getString(cursor.getColumnIndex(PhotoFramsTable.Columns.STATUS)));
        }
        return localPhotoFrames;
    }

    public LocalPhotoFrames queryPhotoFramesStatus(String table, int id) {
        LocalPhotoFrames localPhotoFrames = new LocalPhotoFrames();
        SQLiteDatabase db = sqLiteTemplate.getDb(true);
        String sql = "select * from " + PhotoFramsTable.TABLE_NAME + " where " + PhotoFramsTable.Columns.UID + " = " + id;

        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            PhotoFrames p = new PhotoFrames();

            p.setId(cursor.getInt(1));
            p.setFrameName(cursor.getString(2));
            p.setPhotoFrame(cursor.getString(3));
            p.setAddTime(cursor.getInt(4));
            p.setTopLevel(cursor.getInt(5));
            p.setFrametype(cursor.getInt(6));
            localPhotoFrames.setPhotoFrames(p);
            localPhotoFrames.setStatus(cursor.getString(7));

        }
        return localPhotoFrames;
    }


    public void updatePhotoFramePath(PhotoFrames photoFrames, String filePath) {
        SQLiteDatabase db = sqLiteTemplate.getDb(true);
        String updatesql = "update " + PhotoFramsTable.TABLE_NAME + " set " + PhotoFramsTable.Columns.PHOTOFRAMS + " = " + "\"" + filePath + "\"" + " where " + PhotoFramsTable.Columns.UID + " = " + photoFrames.getId();
        db.execSQL(updatesql);
    }

    public void updatePhotoFrameStatus(PhotoFrames photoFrames, String status) {
        SQLiteDatabase db = sqLiteTemplate.getDb(true);
        String updatesql = "update " + PhotoFramsTable.TABLE_NAME + " set " + PhotoFramsTable.Columns.STATUS + " = \"" + status + "\" where " + PhotoFramsTable.Columns.UID + " = " + photoFrames.getTopLevel();
        db.execSQL(updatesql);
    }
}
