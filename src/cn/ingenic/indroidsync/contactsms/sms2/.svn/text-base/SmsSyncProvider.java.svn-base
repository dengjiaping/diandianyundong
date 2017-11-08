package cn.ingenic.indroidsync.contactsms.sms2;



import cn.ingenic.indroidsync.contactsms.sms2.SmsSyncDatabaseHelper.SmsColumns;
import cn.ingenic.indroidsync.contactsms.sms2.SmsSyncDatabaseHelper.Tables;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class SmsSyncProvider extends ContentProvider{
	
	private SmsSyncDatabaseHelper mSmsSyncDatabaseHelper;
	
	private String[] syncProject={
			SmsColumns.phone_id,
			SmsColumns.read,
			SmsColumns.error_code,
			SmsColumns.watch_id,
			SmsColumns.delete,
			SmsColumns.type,
	};
	
	public SmsSyncProvider(Context context){
		mSmsSyncDatabaseHelper=SmsSyncDatabaseHelper.getInstance(context);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public void delete(String selection, String[] selectionArgs){
		mSmsSyncDatabaseHelper.getWritableDatabase().delete(Tables.SMSTSYNC, selection, selectionArgs);
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}
	public long insert(ContentValues values){
		return mSmsSyncDatabaseHelper.getWritableDatabase().insert(Tables.SMSTSYNC, null, values);
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		return mSmsSyncDatabaseHelper.getReadableDatabase().query(Tables.SMSTSYNC, projection, selection, selectionArgs, 
				null, null, null);
	}
	public Cursor queryAll(){
		return mSmsSyncDatabaseHelper.getReadableDatabase().query(Tables.SMSTSYNC, syncProject, null, null, null, null, null);
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public void updateRead(int read,int id){
		ContentValues cv=new ContentValues();
		cv.put(SmsColumns.read, read);
		cv.put(SmsColumns.watch_id, -1);
		mSmsSyncDatabaseHelper.getWritableDatabase().update(Tables.SMSTSYNC, cv, SmsColumns.phone_id+"=?", new String[]{String.valueOf(id)});
	}
	
	public void updateError(int error,int id){
		ContentValues cv=new ContentValues();
		cv.put(SmsColumns.error_code, error);
		cv.put(SmsColumns.watch_id, -1);
		mSmsSyncDatabaseHelper.getWritableDatabase().update(Tables.SMSTSYNC, cv, SmsColumns.phone_id+"=?", new String[]{String.valueOf(id)});
	}
	public void updateType(int type,int id){
		ContentValues cv=new ContentValues();
		cv.put(SmsColumns.type, type);
		cv.put(SmsColumns.watch_id, -1);
		mSmsSyncDatabaseHelper.getWritableDatabase().update(Tables.SMSTSYNC, cv, SmsColumns.phone_id+"=?", new String[]{String.valueOf(id)});
	}
	
	public void deleteOneSms(int id){
		mSmsSyncDatabaseHelper.getWritableDatabase().delete(Tables.SMSTSYNC, SmsColumns.phone_id+"=?", new String[]{String.valueOf(id)});
	}
	
	public void updateOneSmsWatchId(int phoneId,ContentValues values){
		mSmsSyncDatabaseHelper.getWritableDatabase().update(Tables.SMSTSYNC, values, SmsColumns.phone_id+"=?",
				new String[]{String.valueOf(phoneId)});
	}
	

}
