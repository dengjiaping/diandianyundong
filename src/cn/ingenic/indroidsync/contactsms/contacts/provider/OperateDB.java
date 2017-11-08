package cn.ingenic.indroidsync.contactsms.contacts.provider;

import cn.ingenic.indroidsync.contactsms.contacts.provider.ContactSyncDatabaseHelper.ContactColumns;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;

public class OperateDB {
	
	
	private interface DB{
		
		public Cursor queryAllDB();
	}
	
	private static ContactsSyncProvider contactProvider=null;
	
	private static ContactDB mContactDB=null;
	
	private static SyncDB mSyncDB=null;
	
	private static WatchContactDB mWatchContactDB=null;
	
	public void CreateSyncDB(Context context){
		contactProvider=new ContactsSyncProvider(context);
		
	}
	public ContactsSyncProvider getContactSyncProvider(Context context){
		if(contactProvider==null){
			contactProvider=new ContactsSyncProvider(context);
		}
		return contactProvider;
	}
	public ContactDB getContactDB(Context context){
		if(mContactDB==null){
			mContactDB=new ContactDB(context);
		}
		return mContactDB;
	}
	
	public SyncDB getSyncDB(){
		if(mSyncDB==null){
			mSyncDB=new SyncDB();
		}
		return mSyncDB;
	}
	
	public WatchContactDB getWatchContactDB(Context context){
		if(mWatchContactDB==null){
			mWatchContactDB=new WatchContactDB(context);
		}
		return mWatchContactDB;
	}
	
	public class ContactDB implements DB{
		
		private Context mContext;
		
		private String[] LOCALPROJECTION={
				Contacts._ID,
				Contacts.LOOKUP_KEY,
		};
		
		private String[] LOCALID={
				Contacts._ID,
		};
		
        public ContactDB(Context context){
        	this.mContext=context;
        }
	
		@Override
		public Cursor queryAllDB() {
			// TODO Auto-generated method stub
			return mContext.getContentResolver().query(Contacts.CONTENT_URI, LOCALPROJECTION,
					Contacts.IN_VISIBLE_GROUP + "!=0", null, null);
		}
		public int getLocalCursorId(Cursor cursor){
			return cursor.getInt(cursor.getColumnIndex(Contacts._ID));
		}
		public String getLocalCursorLookupKey(Cursor cursor){
			return cursor.getString(cursor.getColumnIndex(Contacts.LOOKUP_KEY));
		}
		
		public Cursor queryRawContactVersion(int id){
			return mContext.getContentResolver().query(RawContacts.CONTENT_URI, new String[]{"version"}, 
	    			RawContacts.CONTACT_ID+"=?", new String[]{String.valueOf(id)}, null);
		}
		public Cursor queryLocalContactId(String lookupkey){
			return mContext.getContentResolver().query(Contacts.CONTENT_URI, LOCALID,Contacts.LOOKUP_KEY+"=?",
                    new String[]{lookupkey}, null);
		}
		
		public void deleteContactByPhoneKey(String phoneKey){
			Cursor cursor=mContext.getContentResolver().query(Contacts.CONTENT_URI, new String[]{Contacts._ID},
					Contacts.LOOKUP_KEY+"=?", new String[]{phoneKey}, null);
			if(cursor.getCount()!=0){
				Uri deleteUri = ContactsContract.RawContacts.CONTENT_URI;
				cursor.moveToFirst();
				mContext.getContentResolver().delete(deleteUri, RawContacts.CONTACT_ID+"=?", 
						new String[]{cursor.getString(cursor.getColumnIndex(Contacts._ID))});
				
			}
			cursor.close();
			
		}
		
		
	}
	
	public class SyncDB implements DB{
		
		String projection[]={
				ContactColumns.VERSION ,
				ContactColumns.WATCHLOOKUPKEY,
				ContactColumns.PHONELOOKUPKEY,
				ContactColumns.SYNC_DELETE,
				};


		@Override
		public Cursor queryAllDB() {
			// TODO Auto-generated method stub
			return contactProvider.query(null, projection, null, null, null);
		}
		
		public void deleteAll(){
			contactProvider.delete(null, null, null);
		}
		
	    public Cursor querySyncContactByPhoneKey(String phoneLookupkey){
			
			return contactProvider.query(null, projection, ContactColumns.PHONELOOKUPKEY+"=?",
					new String[]{phoneLookupkey}, null);
			
		}
	    
	    public Cursor querySyncContactByWatchKey(String watchKey){
	    	return contactProvider.query(null, projection, ContactColumns.WATCHLOOKUPKEY+"=?",
	    			new String[]{watchKey}, null);
	    }
	    
	    public void updateContactByPhoneKey(String phoneLookUpKey,String watchlookupkey,int version){
	    	ContentValues cv=new ContentValues();
			cv.put(ContactColumns.WATCHLOOKUPKEY, watchlookupkey);
			cv.put(ContactColumns.VERSION, version);
			contactProvider.updateForWatchLookupKey(phoneLookUpKey,cv);
	    }
	    public void updateContactByPhoneKey(String phoneLookUpKey,String watchlookupkey){
	    	ContentValues cv=new ContentValues();
			cv.put(ContactColumns.WATCHLOOKUPKEY, watchlookupkey);
			contactProvider.updateForWatchLookupKey(phoneLookUpKey,cv);
	    }
	    
	    public void deleteSyncContactByPhonekey(String lookupkey){
	    	contactProvider.delete(null, ContactColumns.PHONELOOKUPKEY+"=?", new String[]{lookupkey});
	    }
	    
	    public void addDeleteFailedDatas(String phoneKey){
	    	ContentValues cv=new ContentValues();
	    	cv.put(ContactColumns.PHONELOOKUPKEY, phoneKey);
	    	cv.put(ContactColumns.SYNC_DELETE, 1);
	    	contactProvider.insert(cv);
	    }
	    
	    public void insertDB(String phoneKey,String watchKey,int version){
	    	contactProvider.insert(phoneKey,watchKey,version);
	    }
	    
	    public void updataWatchKey(String phoneKey,String watchKey){
	    	ContentValues cv=new ContentValues();
	    	cv.put(ContactColumns.WATCHLOOKUPKEY, watchKey);
	    	contactProvider.update(null, cv, ContactColumns.PHONELOOKUPKEY+"=?", new String[]{phoneKey});
	    }
		
	}
	
	public class WatchContactDB {
		
		private Context mContext;
		
		public WatchContactDB(Context context){
			this.mContext=context;
		}
		private String[] GETWATCHKEYPROJECTION={
				Contacts.LOOKUP_KEY,
		};
		
		 
		
		public Cursor queryAll(){
			return mContext.getContentResolver().query(Contacts.CONTENT_URI, 
				 new String[]{Contacts._ID,Contacts.LOOKUP_KEY}, null, null, null);
		}
		
		public void deleteAll(){
			Uri deleteUri = ContactsContract.RawContacts.CONTENT_URI;
			mContext.getContentResolver().delete(deleteUri, RawContacts._ID+"!=?", new String[]{"-1"});
		}
		
		public void deleteOneContactByWatchKey(String oldLookupkey){
	        Uri deleteUri = ContactsContract.RawContacts.CONTENT_URI;
	        Cursor cursor=mContext.getContentResolver().query(Contacts.CONTENT_URI, new String[]{Contacts._ID}, 
	        		Contacts.LOOKUP_KEY+"=?", new String[]{oldLookupkey}, null);
	       
	        if(cursor.getCount()!=0){
	        	cursor.moveToFirst();
		        mContext.getContentResolver().delete(deleteUri, RawContacts.CONTACT_ID+"=?", 
		        		new String[]{cursor.getString(cursor.getColumnIndex(Contacts._ID))});
	        }
	        cursor.close();
	        
		}
		
		public void deleteOneContactByPhoneKey(String phoneKey){
			Uri deleteUri = ContactsContract.RawContacts.CONTENT_URI;
			Cursor cursor=mContext.getContentResolver().query(Contacts.CONTENT_URI, new String[]{Contacts._ID}, 
					"phone_lookup_key"+"=?", new String[]{phoneKey}, null);
	       
	        if(cursor.getCount()!=0){
	        	cursor.moveToFirst();
		        mContext.getContentResolver().delete(deleteUri, RawContacts.CONTACT_ID+"=?", 
		        		new String[]{cursor.getString(cursor.getColumnIndex(Contacts._ID))});
	        }
	        cursor.close();
		}
		
		public Cursor getWatchKeyCursor(int rawContactId){
			return mContext.getContentResolver().query(Contacts.CONTENT_URI, GETWATCHKEYPROJECTION, 
					Contacts._ID+"IN("+"SELECT"+Contacts._ID+"FROM"+ContactsContract.RawContacts.CONTENT_URI
					+"WHERE"+RawContacts._ID+"=?)", new String[]{String.valueOf(rawContactId)}, null);
		}
		
		public void updatePhoneKey(String watchKey,String phoneKey){
			Log.i("yangliu","in updatePhoneKey watch key is "+watchKey+", and phone key is "+phoneKey);
			Uri updateUri=Uri.withAppendedPath(Contacts.CONTENT_LOOKUP_URI, watchKey);
			ContentValues cv=new ContentValues();
			cv.put("phone_lookup_key", phoneKey);
			mContext.getContentResolver().update(updateUri, cv,null,null);
		}
		public Cursor queryHaveDeleteContacts(){
			return mContext.getContentResolver().query(Contacts.CONTENT_URI, GETWATCHKEYPROJECTION, 
					"watch_delete"+"=1", null, null);
		}
		
//		public void updateWatchDeleteContacts(){
//			Log.e("yangliu","in updateWatchDeleteContacts");
//			ContentValues cv=new ContentValues();
//			cv.put(Contacts.WATCH_CONTACT_DELETE, 0);
//			mContext.getContentResolver().update(Contacts.CONTENT_URI, cv, Contacts.WATCH_CONTACT_DELETE+"=1", null);
//		}
		
	}
	
	
	
	

}
