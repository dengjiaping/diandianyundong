package cn.ingenic.indroidsync.contactsms.sms2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class SmsSyncDatabaseHelper extends SQLiteOpenHelper{

private static String SMS_SYNC_DATABASE="smssync.db";
	
	private String smsTable="sms_table";
	
	private static final int DATABASE_VERSION = 704;
	
	private static SQLiteDatabase db =null;
	
	
	public interface Tables{
		public static final String SMSTSYNC="sms_sync";
	}
	public interface SmsColumns{
		public static final String phone_id="phone_id";
		public static final String read="_read";
		public static final String error_code="_error_code";
		public static final String watch_id="watch_id";
		public static final String delete="sync_delete";
		public static final String type="_type";
	}
	
	private static SmsSyncDatabaseHelper sSingleton=null;
	
	 public static synchronized SmsSyncDatabaseHelper getInstance(Context context) {
	        if (sSingleton == null) {
	            sSingleton = new SmsSyncDatabaseHelper(context, SMS_SYNC_DATABASE, null,DATABASE_VERSION);
	        }
	        return sSingleton;
	 }
	 
//	 public SQLiteDatabase getDatabase(){
//		return sSingleton.getWritableDatabase();
//	 }
	 
	

	public SmsSyncDatabaseHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		
//		// TODO Auto-generated constructor stub
//		db=context.openOrCreateDatabase(name, 0, factory);
//		//yangliu 
//		db.execSQL("DROP TABLE IF EXISTS " + Tables.SMSTSYNC + ";");
//		onCreate(db);
		
	}
	

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		//create table contact_sync
		db.execSQL("CREATE TABLE IF NOT EXISTS  " + Tables.SMSTSYNC + " (" +
				SmsColumns.phone_id + " INTEGER NOT NULL DEFAULT -1," +
				SmsColumns.watch_id + " INTEGER NOT NULL DEFAULT -1," +
				SmsColumns.read + " INTEGER NOT NULL DEFAULT -1," +
				SmsColumns.delete + " INTEGER NOT NULL DEFAULT 0," +
				SmsColumns.type + " INTEGER NOT NULL DEFAULT 0," +
				SmsColumns.error_code + " INTEGER NOT NULL DEFAULT -1" +
                
        ");");
		
		
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + Tables.SMSTSYNC + ";");
		onCreate(db);
		return;
		
	}

}
