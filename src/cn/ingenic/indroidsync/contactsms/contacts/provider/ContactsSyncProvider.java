package cn.ingenic.indroidsync.contactsms.contacts.provider;


import cn.ingenic.indroidsync.contactsms.contacts.provider.ContactSyncDatabaseHelper.ContactColumns;
import cn.ingenic.indroidsync.contactsms.contacts.provider.ContactSyncDatabaseHelper.Tables;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;





public class ContactsSyncProvider extends ContentProvider{
	
	private ContactSyncDatabaseHelper contactSyncHelper=null;
	private Context mContext;
	
	public ContactsSyncProvider(Context context){
		contactSyncHelper=ContactSyncDatabaseHelper.getInstance(context);
		this.mContext=context;
	}
	

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		return contactSyncHelper.getReadableDatabase().query(Tables.CONTACTSYNC, 
				projection, selection, selectionArgs, null, null, null);
	}



	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return contactSyncHelper.getWritableDatabase().delete(Tables.CONTACTSYNC, 
				selection, selectionArgs);
	}



	@Override
	public Uri insert(Uri uri, ContentValues values) {
		return uri;
	}
	
	public long insert(ContentValues values){
		return contactSyncHelper.getWritableDatabase().insert(Tables.CONTACTSYNC,null, values);
	}
	
	public long insert(String phoneLookupKey,String watchLookupKey,int version){
		ContentValues cv=new ContentValues();
		cv.put(ContactColumns.PHONELOOKUPKEY, phoneLookupKey);
		cv.put(ContactColumns.WATCHLOOKUPKEY, watchLookupKey);
		cv.put(ContactColumns.VERSION, version);
		return  contactSyncHelper.getWritableDatabase().insert(Tables.CONTACTSYNC,null, cv);
	}



	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return contactSyncHelper.getWritableDatabase().update(Tables.CONTACTSYNC,values,selection,selectionArgs);
		
	}
	
	public void updateForWatchLookupKey(String phoneLookUpKey,ContentValues cv){
		
		contactSyncHelper.getWritableDatabase().update(Tables.CONTACTSYNC,cv,
				ContactColumns.PHONELOOKUPKEY+"=?",new String[]{phoneLookUpKey});
	}

	
	

}