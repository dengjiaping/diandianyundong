package com.fox.exercise.db;


public abstract class SportsContent {

    private static final String TAG = "SportsContent";

    /**
     * table for Ads
     *
     * @author hanks.han
     */
    public static class AdsTable {
        public static final String TABLE_NAME = "tb_ads";

        public static class Columns {
            public static final String ID = "id";
            public static final String TITLE = "title";
            public static final String IMG_URL = "imgUrl";
            public static final String URL = "url";
        }

        public static String getGeneratedSQL() {
            String createString = TABLE_NAME + "( " + Columns.ID
                    + " INTEGER PRIMARY KEY, " + Columns.TITLE + " varchar, "
                    + Columns.IMG_URL + " varchar, " + Columns.URL
                    + " varchar " + ");";

            return "CREATE TABLE " + createString;

        }

        public static String getDropTableSQL() {
            return "DROP TABLE IF EXISTS " + TABLE_NAME;
        }
    }

    public static class PrivateMessageAllTable {
        public static final String TABLE_NAME = "tb_privatemessageAll";

        public static class Columns {
            public static final String ID = "_id";
            public static final String SEX = "sex";
            public static final String UIMG = "uimg";
            public static final String NAME = "name";
            public static final String BIRTHDAY = "birthday";
            public static final String UID = "uid";
            public static final String ADDTIME = "addTime";
            public static final String COUNTS = "counts";
            public static final String TOUID = "touid";
            public static final String STATUS = "status";
        }

        public static String getGeneratedSQL() {
            String createString = TABLE_NAME + "( " + Columns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + Columns.SEX
                    + " varchar, " + Columns.UIMG + " varchar, "
                    + Columns.NAME + " varchar, " + Columns.BIRTHDAY
                    + " varchar ," + Columns.UID + " varchar unique," + Columns.ADDTIME + " varchar ," + Columns.COUNTS
                    + " varchar ," + Columns.TOUID + " varchar , " + Columns.STATUS + " varchar " + ");";
            return "CREATE TABLE " + createString;

        }

        public static String getDropTableSQL() {
            return "DROP TABLE IF EXISTS " + TABLE_NAME;
        }
    }

    /**
     * table for private msg
     */
    public static class PrivateMsgTable {
        public static final String TABLE_NAME = "tb_private_msg";
        public static final String TABLE_NAME_2 = "tb_private_msg2";


        public static class Columns {
            public static final String ID = "id";
            public static final String UID = "uid";
            public static final String TOUID = "toUid";
            public static final String ADDTIME = "addTime";
            public static final String COUNTS = "counts";
            public static final String COMMENT_TEXT = "commentText";
            public static final String COMMENT_WAV = "commentWav";
            public static final String COMMENT_DURATION = "commentDuration";
            public static final String OWNERID = "ownerid";
        }

        public static String getGeneratedSQL() {
            String createString = TABLE_NAME + "( " + Columns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + Columns.UID
                    + " varchar, " + Columns.TOUID + " varchar, "
                    + Columns.ADDTIME + " varchar, " + Columns.COMMENT_TEXT
                    + " varchar ," + Columns.COMMENT_WAV + " varchar ," + Columns.COMMENT_DURATION
                    + " varchar ," + Columns.OWNERID + " varchar" + ");";

            return "CREATE TABLE " + createString;

        }


        public static String getDropTableSQL() {
            return "DROP TABLE IF EXISTS " + TABLE_NAME;
        }

        //将原表变为临时表
        public static String getTempTableSQL() {
            return "ALTER TABLE " + TABLE_NAME + " RENAME TO " + TABLE_NAME_2;

        }

        //将临时表中的数据插入新表
        // "insert into tb_private_msg select *,' ' from tb_private_msg2";
        public static String InsertStringTableSQL() {
            return "INSERT INTO " + TABLE_NAME + " select *,'0' from " + TABLE_NAME_2;
        }

        //删除临时表
        public static String DropTempTableSQL() {
            return "DROP TABLE IF EXISTS " + TABLE_NAME_2;
        }

    }

    public static class PhotoFramsTable {
        public static final String TABLE_NAME = "tb_photo_frames";

        public static class Columns {
            public static final String ID = "id";
            public static final String UID = "uid";
            public static final String FRAMENAME = "frameName";
            public static final String PHOTOFRAMS = "photoFrames";
            public static final String ADDTIME = "addTime";
            public static final String TOPLEVEL = "topLevel";
            public static final String FRAMETYPE = "frametype";
            public static final String STATUS = "status";
        }

        public static String getGeneratedSQL() {
            String createString = TABLE_NAME + "( " + Columns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    + Columns.UID + " varchar, " + Columns.FRAMENAME + " varchar, " + Columns.PHOTOFRAMS + " varchar, "
                    + Columns.ADDTIME + " varchar, " + Columns.TOPLEVEL + " varchar, " + Columns.FRAMETYPE + " varchar," + Columns.STATUS + " varchar"
                    + ");";

            return "CREATE TABLE " + createString;

        }

        public static String getDropTableSQL() {
            return "DROP TABLE IF EXISTS " + TABLE_NAME;
        }
    }


    public static class RankTable {
        public static final String TABLE_NAME_WEEK = "tb_rank_week";
        public static final String TABLE_NAME_MONTH = "tb_rank_month";
        public static final String TABLE_NAME_QUARTER = "tb_rank_quarter";
        public static final String TABLE_NAME_TOTAL = "tb_rank_total";

        public static class Colunms {
            public static final String KEY = "key";
            public static final String ID = "id";
            public static final String IMG_URL = "imgUrl";
            public static final String LIKES = "likes";
        }

        public static String getGeneratedSQLWeek() {

            String creatString = TABLE_NAME_WEEK + "( " + Colunms.KEY
                    + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                    + Colunms.ID + " INTEGER," + Colunms.IMG_URL + " varchar,"
                    + Colunms.LIKES + " INTEGER);";
            return "CREATE TABLE " + creatString;
        }

        public static String getGeneratedSQLMonth() {
            String creatString = TABLE_NAME_MONTH + "( " + Colunms.KEY
                    + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                    + Colunms.ID + " INTEGER," + Colunms.IMG_URL + " varchar,"
                    + Colunms.LIKES + " INTEGER);";
            return "CREATE TABLE " + creatString;
        }

        public static String getGeneratedSQLQuarter() {
            String creatString = TABLE_NAME_QUARTER + "( " + Colunms.KEY
                    + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                    + Colunms.ID + " INTEGER," + Colunms.IMG_URL + " varchar,"
                    + Colunms.LIKES + " INTEGER);";
            return "CREATE TABLE " + creatString;
        }

        public static String getGeneratedSQLTotal() {
            String creatString = TABLE_NAME_TOTAL + "( " + Colunms.KEY
                    + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                    + Colunms.ID + " INTEGER," + Colunms.IMG_URL + " varchar,"
                    + Colunms.LIKES + " INTEGER);";
            return "CREATE TABLE " + creatString;
        }

        public static String getDropTableSQLWeek() {
            return "DROP TABLE IF EXISTS " + TABLE_NAME_WEEK;
        }

        public static String getDropTableSQLMonth() {
            return "DROP TABLE IF EXISTS " + TABLE_NAME_MONTH;
        }

        public static String getDropTableSQLQuarter() {
            return "DROP TABLE IF EXISTS " + TABLE_NAME_QUARTER;
        }

        public static String getDropTableSQLTotal() {
            return "DROP TABLE IF EXISTS " + TABLE_NAME_TOTAL;
        }
    }

}

