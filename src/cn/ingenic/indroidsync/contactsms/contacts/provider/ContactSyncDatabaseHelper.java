package cn.ingenic.indroidsync.contactsms.contacts.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;


public class ContactSyncDatabaseHelper extends SQLiteOpenHelper{
	
	private static String CONTACT_SYNC_DATABASE="contactsync.db";
	
	
	private static final int DATABASE_VERSION = 704;
	
	private static SQLiteDatabase db =null;
	
	
	public interface Tables{
		public static final String CONTACTSYNC="contact_sync";
	}
	public interface ContactColumns{
		public static final String PHONELOOKUPKEY="_phone_lookup_key";
		public static final String WATCHLOOKUPKEY="_watch_lookup_key";
		public static final String VERSION="_version";
		public static final String SYNC_DELETE="sync_delete";
	}
	
	private static ContactSyncDatabaseHelper sSingleton=null;
	
	 public static synchronized ContactSyncDatabaseHelper getInstance(Context context) {
	        if (sSingleton == null) {
	            sSingleton = new ContactSyncDatabaseHelper(context, CONTACT_SYNC_DATABASE, null,DATABASE_VERSION);
	        }
	        return sSingleton;
	 }
	 
//	 public SQLiteDatabase getDatabase(){
//		 Log.e("yangliu","in getDatabase .................");
//		 return sSingleton.getWritableDatabase();
//	 }
	 
	

	public ContactSyncDatabaseHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		
//		// TODO Auto-generated constructor stub
//		db=context.openOrCreateDatabase(name, 0, factory);
//		//yangliu 
//		db.execSQL("DROP TABLE IF EXISTS " + Tables.CONTACTSYNC + ";");
//		onCreate(db);
		
	}
	

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		//create table contact_sync
		db.execSQL("CREATE TABLE " + Tables.CONTACTSYNC + " (" +
				ContactColumns.VERSION + " INTEGER NOT NULL DEFAULT 1," +
				ContactColumns.PHONELOOKUPKEY + " TEXT," +
				ContactColumns.WATCHLOOKUPKEY + " TEXT," +
				ContactColumns.SYNC_DELETE + " INTEGER NOT NULL DEFAULT 0" +
                
        ");");
		
		
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + Tables.CONTACTSYNC + ";");
		onCreate(db);
		return;
		
	}
	
}
