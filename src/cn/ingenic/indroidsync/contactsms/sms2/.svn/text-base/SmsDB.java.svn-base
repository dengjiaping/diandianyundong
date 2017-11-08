package cn.ingenic.indroidsync.contactsms.sms2;

import java.util.ArrayList;

import cn.ingenic.indroidsync.contactsms.sms2.Attribute.Threads;
import cn.ingenic.indroidsync.contactsms.sms2.SmsSyncDatabaseHelper.SmsColumns;


import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class SmsDB {
	
	private LocalSmsDB mLocalSmsDB=null;
	
	private SyncSmsDB mSyncSmsDB=null;
	
	private WatchSmsDB mWatchSmsDB=null;
	
	private static SmsSyncProvider mSmsSyncProvider=null;
    private String DEBUG="sms2_debug";
    private boolean debug=true;
	
	public static ArrayList<String> exclude=new ArrayList<String>();
	public static ArrayList<String> proList=new ArrayList<String>();
	
	
	public SmsSyncProvider getSmsSyncProvider(Context context){
		if(mSmsSyncProvider==null){
			mSmsSyncProvider=new SmsSyncProvider(context);
		}
		return mSmsSyncProvider;
	}
	
	public LocalSmsDB getLocalSmsDB(Context context){
		if(mLocalSmsDB==null){
		    mLocalSmsDB= new LocalSmsDB(context);
		}
		return mLocalSmsDB;
	}
	
	public SyncSmsDB getSyncSmsDB(){
		if(mSyncSmsDB==null){
			
			mSyncSmsDB= new SyncSmsDB(mSmsSyncProvider);
		}
		return mSyncSmsDB;
	}
	
	public WatchSmsDB getWatchSmsDB(Context context){
		if(mWatchSmsDB==null){
			mWatchSmsDB=new WatchSmsDB(context);
		}
		return mWatchSmsDB;
	}
	
	
	public class LocalSmsDB{
		private Uri sAllCanonical =
	            Uri.parse("content://mms-sms/canonical-addresses");
		private static final String UNREAD_SELECTION = "(read=0 OR seen=0)";
		
		public final Uri sAllThreadsUri =
				Attribute.THREAD_CONTENT_URI.buildUpon().appendQueryParameter("simple", "true").build();
		
		Uri uri=Attribute.SMS_CONTENT_URI;
		private Context mContext;
		private String[] SmsProjects1={
				Attribute.Sms._ID ,
				Attribute.Sms.READ,
				Attribute.Sms.ERROR_CODE,
				Attribute.Sms.ADDRESS,
				Attribute.Sms.BODY,
				Attribute.Sms.DATE,
				Attribute.Sms.SUBJECT,
				Attribute.Sms.STATUS,
				Attribute.Sms.THREAD_ID,
				Attribute.Sms.TYPE,
				Attribute.Sms.SERVICE_CENTER,
				Attribute.Sms.DATE_SENT,
				Attribute.Sms.PROTOCOL,
				Attribute.Sms.SEEN,
				Attribute.Sms.REPLY_PATH_PRESENT,
				
		};
		
		private String[] threadProject={
				Attribute.Threads._ID,
				Attribute.Threads.RECIPIENT_IDS,
				Attribute.Threads.SNIPPET,
				Attribute.Threads.DATE,
		};
		
	
		public LocalSmsDB(Context context){
			this.mContext=context;
		}
		public Cursor queryAllSms(){
			return mContext.getContentResolver().query(uri, getCurrentProject(), null, null, null);
		}
		
		
		
		private String[] getCurrentProject(){
			if(proList.size()!=0){
				String[] proArray=new String[proList.size()];
				for(int i=0;i<proList.size();i++){
			    	proArray[i]=proList.get(i);
			    }
				return proArray;
			}
			
			Cursor cursor=mContext.getContentResolver().query(uri, null, null, null, null);
			
			String[] columns=cursor.getColumnNames();
			ArrayList<String> curColumnsList=new ArrayList<String>();
			for(String column:columns){
				curColumnsList.add(column);
			}
			cursor.close();
			proList.clear();
			exclude.clear();
		    for(String smsColumn:SmsProjects1){
		    	if(curColumnsList.contains(smsColumn)){
		    		proList.add(smsColumn);
		    	}else{
		    		exclude.add(smsColumn);
		    		Log.e("SmsDB","sms column :"+smsColumn+" not exist!!!");
		    	}
		    }
		    String[] proArray=new String[proList.size()];
		    String test="";
		    for(int i=0;i<proList.size();i++){
		    	proArray[i]=proList.get(i);
		    	test=test+proList.get(i)+",";
		    }
			if(debug)Log.d(DEBUG,"in SmsDB getCurrentProject get columns is :"+test);
			return proArray;
			
		}
		
		public String getAddress(String threadId){
			Cursor cursor=mContext.getContentResolver().query(sAllCanonical,new String[]{Attribute.CanonicalAddressesColumns.ADDRESS},
					Attribute.CanonicalAddressesColumns._ID+" IN (SELECT recipient_ids FROM threads WHERE _id=?)",new String[]{threadId},null);
			String address=null;
			if(cursor.getCount()!=0){
				cursor.moveToFirst();
				address=cursor.getString(cursor.getColumnIndex(Attribute.CanonicalAddressesColumns.ADDRESS));
			}
			cursor.close();
			return address;
		}
		
		public Cursor querySmsById(int id){
			return mContext.getContentResolver().query(uri, getCurrentProject(), Attribute.Sms._ID+"=?",
					new String[]{String.valueOf(id)}, null);
		}
		public void deleteOneSmsById(int id){
			mContext.getContentResolver().delete(uri, Attribute.Sms._ID+"=?", new String[]{String.valueOf(id)});
		}
		public void updateRead(String address){
			if(debug)Log.d(DEBUG,"in SmsDB updateRead address is:"+address);
		    ContentValues sReadContentValues = new ContentValues(2);
	        sReadContentValues.put("read", 1);
	        sReadContentValues.put("seen", 1);
	        long threadId=Threads.getOrCreateThreadId(mContext, address);
	        Uri mThreadUri=ContentUris.withAppendedId(Attribute.THREAD_CONTENT_URI, threadId);
	        mContext.getContentResolver().update(mThreadUri, sReadContentValues,
                    UNREAD_SELECTION, null);
	       
		}
		
		public long getThreadData(String threadId){
			Cursor cursor=mContext.getContentResolver().query(sAllThreadsUri, new String[]{Attribute.Threads.DATE}, 
					Attribute.Threads._ID+"=?", new String[]{threadId}, null);
			long data=0;
			if(cursor.getCount()!=0){
				cursor.moveToFirst();
				data=cursor.getLong(0);
			}
			cursor.close();
			return data;
		}
		
		public Cursor getAllThreadDatas(){
			return mContext.getContentResolver().query(sAllThreadsUri, threadProject, null, null, null);
			
		}
		
		public Cursor getCanonicalDatas(){
			return mContext.getContentResolver().query(sAllCanonical,new String[]{Attribute.CanonicalAddressesColumns.ADDRESS,Attribute.CanonicalAddressesColumns._ID},
					null,null,null);
		}
		
	}
	
	public class SyncSmsDB{
		
		private SmsSyncProvider smsSyncProvider;
		public SyncSmsDB(SmsSyncProvider mSmsSyncProvider){
			this.smsSyncProvider=mSmsSyncProvider;
		}
		
		private String[] WatchProjects={
				SmsColumns.watch_id,
		};
		
		private String[] PhoneProjects={
				SmsColumns.phone_id,
		};
		
		
		public void insertOneSms(int phoneId,int read,int error,int watchId,int delete,int type){
			ContentValues cv=new ContentValues();
			cv.put(SmsColumns.phone_id, phoneId);
			cv.put(SmsColumns.read, read);
			cv.put(SmsColumns.error_code, error);
			cv.put(SmsColumns.watch_id, watchId);
			cv.put(SmsColumns.delete, delete);
			cv.put(SmsColumns.type, type);
			smsSyncProvider.insert(cv);
		}
		public void deleteAllSms(){
			smsSyncProvider.delete(null, null);
		}
		
		public Cursor queryAllSms(){
			return smsSyncProvider.queryAll();
		}
		public void updateSyncRead(int read,int id){
			smsSyncProvider.updateRead(read, id);
		}
		public void updateSyncError(int error,int id){
			smsSyncProvider.updateError(error, id);
		}
		
		public void updateSyncType(int type,int id){
			smsSyncProvider.updateType(type, id);
		}
		
		public void deleteOneSms(int id){
			smsSyncProvider.deleteOneSms(id);
		}
		
		public void updateWatchId(int phoneId,int watchId){
			ContentValues cv=new ContentValues();
			cv.put(SmsColumns.watch_id, watchId);
			smsSyncProvider.updateOneSmsWatchId(phoneId, cv);
		}
		public Cursor queryOneSmsWatchIdCursor(int phoneId){
			return smsSyncProvider.query(null, WatchProjects, SmsColumns.phone_id+"=?", 
					new String[]{String.valueOf(phoneId)}, null);
		}
		public Cursor queryOneSmsPhoneIdByWatchId(int watchId){
			return smsSyncProvider.query(null, PhoneProjects, SmsColumns.watch_id+"=?",
					new String[]{String.valueOf(watchId)}, null);
		}
		
		public void updateDeleteFailed(int phoneId){
			ContentValues cv=new ContentValues();
			cv.put(SmsColumns.phone_id, phoneId);
			cv.put(SmsColumns.watch_id, -1);
			cv.put(SmsColumns.delete, 1);
			smsSyncProvider.insert(cv);
		}
		
	}
	
	public class WatchSmsDB{
		
		private Context mContext;
		
		private String[] SmsIdProjections={
				Attribute.Sms._ID,
		};
		
		public WatchSmsDB(Context context){
			this.mContext=context;
		}
		
		
		public void deleteOneSmsById(int id){
			mContext.getContentResolver().delete(Attribute.MMSSMS_CONTENT_URI, Attribute.Sms._ID+"=?", new String[]{String.valueOf(id)});
		}
		
		public Cursor querySmsIdByThreadId(long threadId){
			return mContext.getContentResolver().query(Attribute.MMSSMS_CONTENT_URI, SmsIdProjections, Attribute.Sms.THREAD_ID+"=?", 
					new String[]{String.valueOf(threadId)}, null);
		}
		public void deleteAll(){
			mContext.getContentResolver().delete(Attribute.MMSSMS_CONTENT_URI, Attribute.Sms._ID+"!=-1", null);
		}
		
		public void updateWatchSmsDB(int id,int read,int error,int type){
			ContentValues cv=new ContentValues();
			cv.put(Attribute.Sms.READ, read);
			cv.put(Attribute.Sms.ERROR_CODE, error);
			cv.put(Attribute.Sms.TYPE, type);
			mContext.getContentResolver().update(Attribute.MMSSMS_CONTENT_URI, cv, Attribute.Sms._ID+"=? ", new String[]{String.valueOf(id)});
		}
	}

}
