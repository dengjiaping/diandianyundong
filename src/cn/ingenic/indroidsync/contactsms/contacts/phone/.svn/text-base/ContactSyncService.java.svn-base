package cn.ingenic.indroidsync.contactsms.contacts.phone;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


import cn.ingenic.indroidsync.Config;
import cn.ingenic.indroidsync.DefaultSyncManager;
import cn.ingenic.indroidsync.LogTag;


import cn.ingenic.indroidsync.contactsms.contacts.ContactColumn;
import cn.ingenic.indroidsync.contactsms.contacts.ContactModule;
import cn.ingenic.indroidsync.contactsms.contacts.provider.OperateDB;
import cn.ingenic.indroidsync.contactsms.contacts.provider.ContactSyncDatabaseHelper.ContactColumns;
import cn.ingenic.indroidsync.contactsms.manager.ContactAndSms2Columns;
import cn.ingenic.indroidsync.data.DefaultProjo;
import cn.ingenic.indroidsync.data.Projo;
import cn.ingenic.indroidsync.data.ProjoType;

import com.android.vcard.VCardComposer;
import com.android.vcard.VCardConfig;



import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;

public class ContactSyncService extends Service{
	
	private String TAG="ContactSyncService";
	
	private String tag="In ContactSyncService ....";
    private String DEBUG="contact_debug";
    private boolean debug=true;
	
	private final int INSERT_CONTACT_START=1;
	
	private final int CHANGE_CONTACT=2;
	
	private final int CONTACT_LISTENER=3;
	
	private final int DELAY_MESSAGE=4;
	
	private final int REMOVE_DELAY_MESSAGE=5;
	
	private final int CAN_LOADING_ALL=6;
	
	private final int CAN_GET_WATCH_NEED=7;
	
	private final long listener_time=1200000;
	
	private final long dalay_time=1000;
	
	private final long DELAY_TIME=10000;
	
	private ExecuteContactsHandler contactHandler;
	
	private  ExecuteDelayHandler delayHandler;
	
//	private LoadingHandler loadingHandler;
    
    private ContentObserver mContactChangeObserver=null;
    
	
	private boolean whenSavePowerModeUpdate=false;
	
	private static final int SEND_ALL=0;
	
	private static final int SEND_CHANGE_INSERT=1;
	
	private static final int SEND_CHANGE_DELETE=2;
	
	private static final int SEND_CHANGE_UPDATE=3;
	
	private static final int SEND_SAVE_POWER_MESSAGE=4;
	
	private static final int SEND_HAVE_SEND_SAVE_POWER_DATAS_MESSAGE=5;
	
	private static final int SEND_WATCH_NEEDED_DATAS=6;
	
	private static final int START_GAT_ALL_MESSAGE=7;
	
	private static final int STOP_GET_ALL_MESSAGE=8;
	
	private static final int START_GET_ALL_CHANGE_MESSAGE=9;
	
	private static final int STOP_GET_ALL_CHANGE_MESSAGE=10;
	
	private static final int NO_DATAS_CHANGES=11;
	
	private static final int DELAY_TIME_MESSAGE=12;
	
	private static boolean Connect_sync=false;
	
	private boolean canGetNeedDatas=true;
	
	private HandlerThread responseThread;
	
	private ResponseHandler mResponseSend;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private OperateDB db;
	 @Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();	
		if(debug)Log.d(DEBUG,"in ContactSyncService onCreate .");
        db=new OperateDB();
		db.CreateSyncDB(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ContactAndSms2Columns.SYNC_CONNECTIVITY_FALSE_ACTION);
        filter.addAction(ContactAndSms2Columns.SYNC_CONNECTIVITY_TRUE_ACTION);
        filter.addAction(ContactAndSms2Columns.ContactColumn.UPDATE_ACTION);
        filter.addAction(ContactAndSms2Columns.ContactColumn.CATCH_ALL_COTNACTS_DATAS_ACTION);
        filter.addAction(ContactAndSms2Columns.ContactColumn.CONTACT_SYNC_ACTION);
        registerReceiver(mBluetoothStateReceiver, filter);
       
		HandlerThread changeThread=new HandlerThread("change_thread");
		changeThread.start();
		contactHandler=new ExecuteContactsHandler(changeThread.getLooper());
		mContactChangeObserver=new ContactChangeObserver();
		this.getContentResolver().registerContentObserver(Contacts.CONTENT_URI, false, mContactChangeObserver);
		
		HandlerThread delayThread=new HandlerThread("delay_thread");
		delayThread.start();
		delayHandler=new ExecuteDelayHandler(delayThread.getLooper());
		
		responseThread=new HandlerThread("response_thread");
		responseThread.start();
		mResponseSend=new ResponseHandler(responseThread.getLooper());
		
//		HandlerThread loadingThread=new HandlerThread("loading_thread");
//		loadingThread.start();
//		loadingHandler=new LoadingHandler(loadingThread.getLooper());
		
		
		sendMessage(CONTACT_LISTENER);
		sendMessage(CHANGE_CONTACT);
//		contactHandler.sendEmptyMessageDelayed(CONTACT_LISTENER, listener_time); 
	}
	 
	 @Override
	public void onStart(Intent intent, int startId) {
			// TODO Auto-generated method stub
			super.onStart(intent, startId);
		}
	 

	 @Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		 
		return super.onStartCommand(intent, flags, startId);
	}
	 
	

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Stop();
	}

	private final BroadcastReceiver mBluetoothStateReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) { 
				String action = intent.getAction();
                if(debug)Log.d(DEBUG,"in ContactSyncService receiver action is :"+action);

				if(ContactAndSms2Columns.SYNC_CONNECTIVITY_FALSE_ACTION.equals(action)){
					canLoadAll=true;
					canGetNeedDatas=true;
				}else if(ContactAndSms2Columns.SYNC_CONNECTIVITY_TRUE_ACTION.equals(action)){
					if(Connect_sync)ContactSync();
				}else if(ContactAndSms2Columns.ContactColumn.UPDATE_ACTION.equals(action)){
					//only for save power mode
//					whenSavePowerModeUpdate=true;
					sendMessage(CHANGE_CONTACT);
				}else if(ContactAndSms2Columns.ContactColumn.CATCH_ALL_COTNACTS_DATAS_ACTION.equals(action)){
					sendMessage(INSERT_CONTACT_START);
				}else if(ContactAndSms2Columns.ContactColumn.CONTACT_SYNC_ACTION.equals(action)){
						ContactSync();
					
				}
			}
			
		};
		
     private void ContactSync(){
    	 if(canGetNeedDatas){
// 			whenSavePowerModeUpdate=true;
 			SYNC_STATUS=SYNC_NO_CHANGE;
 			sendMessage(CONTACT_LISTENER);
 			sendMessage(CHANGE_CONTACT);
// 			contactHandler.sendEmptyMessageDelayed(CONTACT_LISTENER, listener_time);
 			canGetNeedDatas=false;
    	 }

     }
		
     private String getAddress(){
    	 BluetoothAdapter ba=BluetoothAdapter.getDefaultAdapter();
         return ba.getAddress();
     }
     

	 private void sendMessage(int what){
		 if(debug)Log.d(DEBUG,"in ContactSyncService sendMessage what is :"+what);
		 Message startMessage=new Message();
		 startMessage.what=what;
		 contactHandler.sendMessage(startMessage);
	 }
	 
	 private void Stop(){
         if(mContactChangeObserver!=null)
		 this.getContentResolver().unregisterContentObserver(mContactChangeObserver);
	 }
	 
	 private boolean canLoadAll=true;
	 
	 private void insertSyncDB(){
		 if(!canLoadAll){
			 return;
		 }
		 ArrayList<Projo> contactList=new ArrayList<Projo>();
			Cursor cursor=db.getContactDB(this).queryAllDB();
	    	if(cursor.getCount()==0){
	    		//if local is null return 
	    		cursor.close();
	    		return;
	    	}
	    	cursor.moveToFirst();
	    	db.getSyncDB().deleteAll();
	    	sendTitleMessage(ContactAndSms2Columns.ContactColumn.START_INSERT_CHANGE_MESSAGE,START_GET_ALL_CHANGE_MESSAGE);
//	    	sendTitleMessage(ContactAndSms2Columns.ContactColumn.START_INSERT_ALL_MESSAGE,START_GAT_ALL_MESSAGE);
	    	do{	
	    	int id=db.getContactDB(this).getLocalCursorId(cursor);
	    	String lookupkey=db.getContactDB(this).getLocalCursorLookupKey(cursor);
	    	 
	    	  int version=getRawContactVersion(id);
	    	  if(version==-1){
	    		  return;
	    	  }
	    	 db.getSyncDB().insertDB(lookupkey, "", version);
	    	 Projo projo = new DefaultProjo(EnumSet.allOf(ContactColumn.class), ProjoType.DATA);
		     projo.put(ContactColumn.phonekey, lookupkey);
		     projo.put(ContactColumn.address,getAddress());
		     projo.put(ContactColumn.onevcard, getOneContactVcard(id));
		     projo.put(ContactColumn.tag, ContactAndSms2Columns.ContactColumn.WATCH_TAG_INSERT_ALL);
		     contactList.add(projo);
	    	}while(cursor.moveToNext());
	    	cursor.close();
	    	sendTitleMessage(ContactAndSms2Columns.ContactColumn.STOP_INSERT_CHANGE_MESSAGE,STOP_GET_ALL_CHANGE_MESSAGE);
//	    	sendTitleMessage(ContactAndSms2Columns.ContactColumn.STOP_INSERT_ALL_MESSAGE,STOP_GET_ALL_MESSAGE);
	    	sendContactsList(contactList,SEND_ALL);
//	    	contactHandler.sendEmptyMessageDelayed(CONTACT_LISTENER, listener_time);
	    	
//	    	loadingHandler.sendEmptyMessageDelayed(CAN_LOADING_ALL,60000);//In one minute can not catch all twice
	    	
	    	finishLoadAll();
	    	
	    	
	 }
	 
	 private void finishLoadAll(){
		 canLoadAll=false;
		 canGetNeedDatas=false;
		 contactHandler.removeMessages(INSERT_CONTACT_START);
	 }

		

	/**
	 * through contact_id package one Contact info to a vcard String
	 * @param id
	 * @return
	 */
	private String getOneContactVcard(int id){
		VCardComposer composer = new VCardComposer(this, VCardConfig.VCARD_TYPE_DEFAULT, true);
    	composer.init(Contacts.CONTENT_URI, Contacts._ID+"=?",
    			new String[]{String.valueOf(id)}, null);
    	String oneContactEntry=composer.createOneEntry();
    	composer.terminate();
    	return oneContactEntry;
	}

	/**
	 * one contentObserver register for local Contact.db changed,
	 * do insert,delete,update sync.db ,send data to remote device
	 * @author yangliu
	 *
	 */
	private class ContactChangeObserver extends ContentObserver{

		public ContactChangeObserver() {
			super(new Handler());
			// TODO Auto-generated constructor stub
		}
		@Override
		public void onChange(boolean selfChange) {
			// TODO Auto-generated method stub
			super.onChange(selfChange);
			
			contactHandler.removeMessages(CHANGE_CONTACT);
			contactHandler.sendEmptyMessageDelayed(CHANGE_CONTACT, dalay_time);	
		}
	}
	
	private class ExecuteContactsHandler extends Handler{
		
		public ExecuteContactsHandler(Looper looper){
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case INSERT_CONTACT_START:
				delayHandler.sendEmptyMessageDelayed(DELAY_MESSAGE,DELAY_TIME);
				insertSyncDB();
				delayHandler.sendEmptyMessage(REMOVE_DELAY_MESSAGE);
				
				break;
			case CHANGE_CONTACT:
				delayHandler.sendEmptyMessageDelayed(DELAY_MESSAGE,DELAY_TIME);
				getChange();
				delayHandler.sendEmptyMessage(REMOVE_DELAY_MESSAGE);
				break;
			case CONTACT_LISTENER:
				delayHandler.sendEmptyMessageDelayed(DELAY_MESSAGE,DELAY_TIME);
				ArrayList<Projo> changeList=getWatchNeedDatas();
				delayHandler.sendEmptyMessage(REMOVE_DELAY_MESSAGE);
//				if(changeList!=0)
//				contactHandler.sendEmptyMessageDelayed(CONTACT_LISTENER, listener_time);
				break;
			
			}
		}
		
	}
	
//	private class LoadingHandler extends Handler{
//		
//		public LoadingHandler(Looper looper){
//			super(looper);
//		}
//
//		@Override
//		public void handleMessage(Message msg) {
//			// TODO Auto-generated method stub
//			super.handleMessage(msg);
//			switch(msg.what){
//            case CAN_LOADING_ALL:
//            	canLoadAll=true;
//				break;
//            case CAN_GET_WATCH_NEED:
//            	canGetNeedDatas=true;
//            	break;
//			}
//		}
//		
//	}
	
	
	private class ExecuteDelayHandler extends Handler{
		
		public ExecuteDelayHandler(Looper looper){
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case DELAY_MESSAGE:
                if(debug)Log.d(DEBUG,"in ExecuteDelayHandler send delay message .");
				delayHandler.removeMessages(DELAY_MESSAGE);
				sendTitleMessage(ContactAndSms2Columns.ContactColumn.START_INSERT_CHANGE_MESSAGE,START_GET_ALL_CHANGE_MESSAGE);
//				sendTitleMessage(ContactAndSms2Columns.ContactColumn.CONTACT_DELAY_TIME_MESSAGE,DELAY_TIME_MESSAGE);
				delayHandler.sendEmptyMessageDelayed(DELAY_MESSAGE,DELAY_TIME);
				break;
			case REMOVE_DELAY_MESSAGE:
				delayHandler.removeMessages(DELAY_MESSAGE);
				break;
			}
		}
		
	}
	
	
	
	private ArrayList<Projo> getWatchNeedDatas(){
		Cursor syncCursor=db.getSyncDB().queryAllDB();
		ArrayList<Projo> changeList=new ArrayList<Projo>();
        if(debug)Log.d(DEBUG,"In contactSyncService getWatchNeedDatas syncData size is :"+syncCursor.getCount());
		if(syncCursor.getCount()!=0){
			sendTitleMessage(ContactAndSms2Columns.ContactColumn.START_INSERT_CHANGE_MESSAGE,START_GET_ALL_CHANGE_MESSAGE);
			syncCursor.moveToFirst();
			do{
				String phoneKey=syncCursor.getString(syncCursor.getColumnIndex(ContactColumns.PHONELOOKUPKEY));
				String watchKey=syncCursor.getString(syncCursor.getColumnIndex(ContactColumns.WATCHLOOKUPKEY));
				int syncDelete=syncCursor.getInt(syncCursor.getColumnIndex(ContactColumns.SYNC_DELETE));
				if(watchKey==null||watchKey.equals("")){
					Projo projo = new DefaultProjo(EnumSet.allOf(ContactColumn.class), ProjoType.DATA);
					projo.put(ContactColumn.phonekey, phoneKey);
					Cursor idCursor=db.getContactDB(this).queryLocalContactId(phoneKey);
					if(idCursor.getCount()!=0){
						idCursor.moveToFirst();
						int id=idCursor.getInt(0);
						projo.put(ContactColumn.onevcard, getOneContactVcard(id));
					}
					idCursor.close();
					projo.put(ContactColumn.tag, ContactAndSms2Columns.ContactColumn.SEND_WATCH_NEED_DATAS);
					projo.put(ContactColumn.address, getAddress());
					projo.put(ContactColumn.delete,syncDelete);
					changeList.add(projo);
					if(syncDelete==1){
						db.getSyncDB().deleteSyncContactByPhonekey(phoneKey);
					}
				}
				
			}while(syncCursor.moveToNext());
			sendTitleMessage(ContactAndSms2Columns.ContactColumn.STOP_INSERT_CHANGE_MESSAGE,STOP_GET_ALL_CHANGE_MESSAGE);
		}else{
			insertSyncDB();
			SYNC_STATUS=SYNC_ALL;
		}
		syncCursor.close();
		if(debug)Log.e(DEBUG,"in ContactSyncService getWatchNeedDatas check change sync size is:"+changeList.size());
		if(changeList.size()!=0){
			sendContactsList(changeList,SEND_WATCH_NEEDED_DATAS);
			SYNC_STATUS=SYNC_CHECK_CHANGED;
		}
		
		return changeList;
	}
	
	private void returnCheckContactFailed(ArrayList<Projo> list){
		for(Projo projo:list){
			String phoneKey=projo.get(ContactColumn.phonekey).toString();
			int delete=Integer.parseInt(projo.get(ContactColumn.delete).toString());
			if(delete==1){
				db.getSyncDB().addDeleteFailedDatas(phoneKey);
			}
		}
	}
	
	
	/**
	 * get changes and to dealWith
	 */
	private synchronized void getChange(){
		Cursor syncCursor=db.getSyncDB().queryAllDB();//get sync db 
		Cursor localContacts=db.getContactDB(this).queryAllDB();//get local db
		LinkedList<String> syncLookupkeyList=new LinkedList<String>();//sync list for add phone_lookup_key
		LinkedList<String> localLookupkeyList=new LinkedList<String>();//local list for add phone_lookup_key
		syncCursor.moveToFirst();
		if(syncCursor.getCount()!=0){
		  do{
			  String syncPhoneLookupkey=syncCursor.getString(syncCursor.getColumnIndex(ContactColumns.PHONELOOKUPKEY));
			  syncLookupkeyList.add(syncPhoneLookupkey);
		  }while(syncCursor.moveToNext());
		}
		syncCursor.close();
		localContacts.moveToFirst();
		if(localContacts.getCount()!=0){
		do{
			String localPhoneLookupkey=localContacts.getString(localContacts.getColumnIndex(Contacts.LOOKUP_KEY));
			localLookupkeyList.add(localPhoneLookupkey);
		}while(localContacts.moveToNext());
		}
		localContacts.close();
		ArrayList<String> addList=new ArrayList<String>();//insert list
		for(String localPhoneLookupkey:localLookupkeyList){
			if(syncLookupkeyList.contains(localPhoneLookupkey)){
				continue;
			}
			addList.add(localPhoneLookupkey);
		}
		ArrayList<String> deleteList=new ArrayList<String>();//delete list
		for(String syncPhoneLookupkey:syncLookupkeyList){
			if(localLookupkeyList.contains(syncPhoneLookupkey)){
				continue;
			}
			deleteList.add(syncPhoneLookupkey);
		}
		sendTitleMessage(ContactAndSms2Columns.ContactColumn.START_INSERT_CHANGE_MESSAGE,START_GET_ALL_CHANGE_MESSAGE);
		if(addList.size()!=0){
			doInsert(addList);
		}
		if(deleteList.size()!=0){
			doDelete(deleteList);
		}
		doUpdate();
		sendTitleMessage(ContactAndSms2Columns.ContactColumn.STOP_INSERT_CHANGE_MESSAGE,STOP_GET_ALL_CHANGE_MESSAGE);
//		sendPhoneIsOK();
	}
	
	private int SYNC_NO_CHANGE=0;
	private int SYNC_CHECK_CHANGED=1;
	private int SYNC_ADD=2;
	private int SYNC_DELETE=3;
	private int SYNC_UPDATE=4;
	private int SYNC_ALL=5;
	private int SYNC_STATUS=-1;
	
	private synchronized void onChanged(ArrayList<Projo> addList,ArrayList<Projo> deleteList,ArrayList<Projo> updateList){
		if(addList!=null){
            if(debug)Log.d(DEBUG,"in ContactSyncService onChanged send addList size is :"+addList.size());
			executeList(addList,SEND_CHANGE_INSERT);
			SYNC_STATUS=SYNC_ADD;
		}
		if(deleteList!=null){
            if(debug)Log.d(DEBUG,"in ContactSyncService onChanged send deleteList size is :"+deleteList.size());
			executeList(deleteList,SEND_CHANGE_DELETE);
			SYNC_STATUS=SYNC_DELETE;
		}
		if(updateList!=null){
			if(updateList.size()==0){
//				if(whenSavePowerModeUpdate){
//					sendTitleMessage(ContactAndSms2Columns.ContactColumn.PHONE_HAVE_SEND_CONTACTS,SEND_HAVE_SEND_SAVE_POWER_DATAS_MESSAGE);
//				}
//				whenSavePowerModeUpdate=false;
				if(SYNC_STATUS==SYNC_NO_CHANGE){
                    if(debug)Log.d(DEBUG,"in ContactSyncService onChanged send no datas changed message");
					sendTitleMessage(ContactAndSms2Columns.ContactColumn.CONTACT_SYNC_NO_DATAS_CHANGED_MESSAGE,NO_DATAS_CHANGES);
				}
				
			}else{
                if(debug)Log.d(DEBUG,"in ContactSyncService onChanged send updateList size is :"+updateList.size());
				executeList(updateList,SEND_CHANGE_UPDATE);
				SYNC_STATUS=SYNC_UPDATE;
			}
			SYNC_STATUS=-1;
//			whenSavePowerModeUpdate=false;
		}
			
	}
	
	private synchronized void executeList(ArrayList<Projo> contactList,int sendTag){
//		if(ContactModule.getMode()==DefaultSyncManager.SAVING_POWER_MODE&&!whenSavePowerModeUpdate){
//			sendTitleMessage(ContactAndSms2Columns.ContactColumn.SAVE_POWER_MESSAGE,SEND_SAVE_POWER_MESSAGE);
//		}else if(whenSavePowerModeUpdate){
//			sendContactsList(contactList,sendTag);
//			sendTitleMessage(ContactAndSms2Columns.ContactColumn.PHONE_HAVE_SEND_CONTACTS,SEND_HAVE_SEND_SAVE_POWER_DATAS_MESSAGE);
//		}else{
			sendContactsList(contactList,sendTag);
//		}
	}
	/**
	 * if Contact.db insert one or more Contacts do this
	 * @param addList 
	 */
	private synchronized void doInsert(ArrayList<String> addList){
		ArrayList<Projo> contactList=new ArrayList<Projo>();
		for(String lookupkey:addList){
			Cursor cursor=db.getContactDB(this).queryLocalContactId(lookupkey);
			if(cursor.getCount()==0){
				//data in addList but not in local db,so return this operation failed
				cursor.close();
				continue;
			}
			cursor.moveToFirst();
			int id=cursor.getInt(cursor.getColumnIndex(Contacts._ID));
			int version=getRawContactVersion(id);
//			if(ContactModule.getMode()!=DefaultSyncManager.SAVING_POWER_MODE||whenSavePowerModeUpdate){
				//insert sync db
				db.getSyncDB().insertDB(lookupkey, "", version);
//			}
			

	    	Projo projo = new DefaultProjo(EnumSet.allOf(ContactColumn.class), ProjoType.DATA);
	    	projo.put(ContactColumn.phonekey, lookupkey);
	    	projo.put(ContactColumn.onevcard, getOneContactVcard(id));
	    	projo.put(ContactColumn.address,getAddress());
	    	projo.put(ContactColumn.tag, ContactAndSms2Columns.ContactColumn.WATCH_TAG_INSERT);
	    	contactList.add(projo);
	    	cursor.close();
		}
//		Log.e("yangliu",tag+"insert current mode is :"+ContactModule.getMode()+"...and whenSavePowerModeUpdate is :"
//				+whenSavePowerModeUpdate);
		
		onChanged(contactList,null,null);
	}
	
	
	/**
	 * if contact.db delete some contacts do this
	 * @param deleteList
	 */
	private synchronized void doDelete(ArrayList<String> deleteList){
		ArrayList<Projo> contactList=new ArrayList<Projo>();
		
		for(String lookupkey:deleteList){
			Cursor cursor=db.getSyncDB().querySyncContactByPhoneKey(lookupkey);
			if(cursor.getCount()==0){
				cursor.close();
				continue;
			}
			cursor.moveToFirst();
			String watchLookupkey=cursor.getString(cursor.getColumnIndex(ContactColumns.WATCHLOOKUPKEY));
			String phoneLookupKey=cursor.getString(cursor.getColumnIndex(ContactColumns.PHONELOOKUPKEY));

			Projo projo = new DefaultProjo(EnumSet.allOf(ContactColumn.class), ProjoType.DATA);
			projo.put(ContactColumn.phonekey, phoneLookupKey);
			projo.put(ContactColumn.watchkey, watchLookupkey);
			projo.put(ContactColumn.address,getAddress());
			projo.put(ContactColumn.tag, ContactAndSms2Columns.ContactColumn.WATCH_TAG_DELETE);
			contactList.add(projo);
//			if(ContactModule.getMode()!=DefaultSyncManager.SAVING_POWER_MODE||whenSavePowerModeUpdate){
				//delete contacts info in sync db
				db.getSyncDB().deleteSyncContactByPhonekey(lookupkey);
//			}
			
			cursor.close();
		}
//		Log.e("yangliu",tag+"delete current mode is :"+ContactModule.getMode()+"...and whenSavePowerModeUpdate is :"
//				+whenSavePowerModeUpdate);

		onChanged(null,contactList,null);
	}
	
	
	
	/**
	 * compare one Contact version 
	 * if version change contact change 
	 */
	private synchronized void doUpdate(){
		Cursor localContacts=db.getContactDB(this).queryAllDB();
		if(localContacts.getCount()==0){
			Log.e("ContactSyncService","when doupdate find local Contacts is null");
			localContacts.close();
			return;
		}
		localContacts.moveToFirst();
		ArrayList<Projo> contactList=new ArrayList<Projo>();
		do{
			int id=db.getContactDB(this).getLocalCursorId(localContacts);
			String phoneLookupKey=db.getContactDB(this).getLocalCursorLookupKey(localContacts);
			int localPhoneVersion=getRawContactVersion(id);
			if(localPhoneVersion==-1){
				localContacts.close();
				return;
			}
			Cursor syncCursor=db.getSyncDB().querySyncContactByPhoneKey(phoneLookupKey);
			if(syncCursor.getCount()==0){
				syncCursor.close();
				return;
			}
			syncCursor.moveToFirst();
			int syncVersion=syncCursor.getInt(syncCursor.getColumnIndex(ContactColumns.VERSION));
		
			//if local Contact Version != sync Version this contact changed
			if(localPhoneVersion!=syncVersion){
				String watchLookupKey=syncCursor.getString(syncCursor.getColumnIndex(ContactColumns.WATCHLOOKUPKEY));
				Projo projo = new DefaultProjo(EnumSet.allOf(ContactColumn.class), ProjoType.DATA);
				projo.put(ContactColumn.phonekey, phoneLookupKey);
				projo.put(ContactColumn.onevcard, getOneContactVcard(id));
				projo.put(ContactColumn.watchkey, watchLookupKey);
				projo.put(ContactColumn.address,getAddress());
				projo.put(ContactColumn.tag, ContactAndSms2Columns.ContactColumn.WATCH_TAG_UPDATE);
				contactList.add(projo);
//				if(ContactModule.getMode()!=DefaultSyncManager.SAVING_POWER_MODE||whenSavePowerModeUpdate){
					//update changed contact and set watch_lookupKey null
					db.getSyncDB().updateContactByPhoneKey(phoneLookupKey, "",localPhoneVersion);
//				}
				
			}
			syncCursor.close();
		}while(localContacts.moveToNext());
		localContacts.close();
		onChanged(null,null,contactList);
		
	}
	
	 
	/**
	 * because version in rawcontact table so here select version from rawcontact by contact_id
	 * @param id
	 * @return
	 */
	private synchronized int getRawContactVersion(int id){
		  Cursor rawCursor=db.getContactDB(this).queryRawContactVersion(id);
		  //if this id's contact is null return,this failed
		  if(rawCursor.getCount()==0)return -1;
    	  rawCursor.moveToFirst();
    	  int version=rawCursor.getInt(0);
    	  rawCursor.close();
    	  return version;
	}
	private static ArrayList<Map<String,Object>> SEND_LIST=new ArrayList<Map<String,Object>>();
	private static int codeNumber=0;
	private synchronized void sendContactsList(ArrayList<Projo> contactList,int sendTag){
        if(debug)Log.d(DEBUG,"in ContactSyncService sendContactList send list size is :"+contactList.size());
		Message sendMessage=mResponseSend.obtainMessage();
		sendMessage.obj=sendTag+","+codeNumber;
		DefaultSyncManager manager = DefaultSyncManager.getDefault();
		Config config = new Config(ContactModule.CONTACT);
		config.mCallback=sendMessage;
		Map<String,Object> sendMap=new HashMap<String,Object>();
		sendMap.put("title", sendTag+","+codeNumber);
		sendMap.put("code_number", codeNumber);
		sendMap.put("list", contactList);
		SEND_LIST.add(sendMap);
		codeNumber++;
        manager.request(config, contactList);//send
	}
	
	private class ResponseHandler extends Handler{
        
		public ResponseHandler(Looper looper){
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			ArrayList<Projo> sendList=new ArrayList<Projo>();
			int index=-1;
			String title=null;
			if(debug)Log.i(DEBUG,"in ContactSyncService mResponseSend msg.obj is :"+msg.obj+" and ,msg.arg1 is :"+msg.arg1 );
			for(int i=0;i<SEND_LIST.size();i++){
				title=SEND_LIST.get(i).get("title").toString();
				if(title.equals(msg.obj)){
					sendList=(ArrayList<Projo>)SEND_LIST.get(i).get("list");
					index=i;
					break;
				}
			}
			if(title==null||index==-1){
				Log.e(TAG,"when returnd title is null!!! or index = -1!!!!");
				return;
			}
			String[] titleArray=title.split(",");
			switch(Integer.parseInt(titleArray[0])){
			case SEND_ALL:
				if(debug)Log.i(DEBUG,"return SEND_ALL.");
				if(msg.arg1!=DefaultSyncManager.SUCCESS){
					Connect_sync=true;
					canLoadAll=true;
				}else{
					Connect_sync=false;
				}
				break;
			case SEND_CHANGE_INSERT:
				if(debug)Log.i(DEBUG,"return SEND_CHANGE_INSERT .");
                if(msg.arg1!=DefaultSyncManager.SUCCESS){
                	Connect_sync=true;
				}else{
					Connect_sync=false;
				}
				break;
			case SEND_CHANGE_UPDATE:
				if(debug)Log.i(DEBUG,"return SEND_CHANGE_UPDATE .");
				if(msg.arg1!=DefaultSyncManager.SUCCESS){
					Connect_sync=true;
				}else{
					Connect_sync=false;
				}
				break;
			case SEND_CHANGE_DELETE:
				if(debug)Log.i(DEBUG,"return SEND_CHANGE_DELETE .");
				if(msg.arg1!=DefaultSyncManager.SUCCESS){
					deleteReturnFailed(sendList);
					Connect_sync=true;
				}else{
					Connect_sync=false;
				}
				break;
			case SEND_WATCH_NEEDED_DATAS:
				if(debug)Log.i(DEBUG,"return SEND_WATCH_NEEDED_DATAS .");
				if(msg.arg1!=DefaultSyncManager.SUCCESS){
					returnCheckContactFailed(sendList);
					Connect_sync=true;
					canGetNeedDatas=true;
				}else{
					Connect_sync=false;
				}
				
				break;
			case SEND_SAVE_POWER_MESSAGE:
				if(debug)Log.i(DEBUG,"return SEND_SAVE_POWER_MESSAGE .");
				break;
			case SEND_HAVE_SEND_SAVE_POWER_DATAS_MESSAGE:
				if(debug)Log.i(DEBUG,"return SEND_HAVE_SEND_SAVE_POWER_DATAS_MESSAGE.");
				break;
			case START_GAT_ALL_MESSAGE:
				if(debug)Log.i(DEBUG,"return START_GAT_ALL_MESSAGE .");
				break;
			case STOP_GET_ALL_MESSAGE:
				if(debug)Log.i(DEBUG,"return STOP_GET_ALL_MESSAGE .");
				break;
			case START_GET_ALL_CHANGE_MESSAGE:
				if(debug)Log.i(DEBUG,"return START_GET_ALL_CHANGE_MESSAGE .");
				break;
			case STOP_GET_ALL_CHANGE_MESSAGE:
				if(debug)Log.i(DEBUG,"return STOP_GET_ALL_CHANGE_MESSAGE .");
				break;
			case NO_DATAS_CHANGES:
				if(debug)Log.i(DEBUG,"return NO_DATAS_CHANGES .");
				break;
			case DELAY_TIME_MESSAGE:
				if(debug)Log.i(DEBUG,"return DELAY_TIME_MESSAGE .");
				break;
				
			}
			SEND_LIST.remove(index);
		}
		
	};
	

	

	private void deleteReturnFailed(ArrayList<Projo> deleteContactsList){
		int size=deleteContactsList.size();
		if(debug)Log.e(DEBUG,"in ContactSyncService deleteReturnFailed size is :"+size);
		if(size == 0){
			Log.e(TAG,"when delete return failed delete list size is 0 !");
			return ;
		}
		for(Projo projo:deleteContactsList){
			String phonekey=(String)projo.get(ContactColumn.phonekey);
			db.getSyncDB().addDeleteFailedDatas(phonekey);
		}
		deleteContactsList.clear();
	}
	
	private synchronized void sendTitleMessage(String tag,int sendTag){
		if(debug)Log.d(DEBUG,this.tag+"sendTitleMessage tag is "+tag);
		ArrayList<Projo> list=new ArrayList<Projo>();
		Projo projo = new DefaultProjo(EnumSet.allOf(ContactColumn.class), ProjoType.DATA);
		projo.put(ContactColumn.tag, tag);
		list.add(projo);
		sendContactsList(list,sendTag);
		
	}
	

	
}
