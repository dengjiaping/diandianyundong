package cn.ingenic.indroidsync.contactsms.sms2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
//import android.provider.Telephony;
//import android.provider.Telephony.MmsSms;
//import android.provider.Telephony.Sms;
//import android.provider.Telephony.Threads;
import android.util.Log;
import cn.ingenic.indroidsync.Config;
import cn.ingenic.indroidsync.DefaultSyncManager;

import cn.ingenic.indroidsync.contactsms.manager.ContactAndSms2Columns;
import cn.ingenic.indroidsync.contactsms.sms2.SmsSyncDatabaseHelper.SmsColumns;
import cn.ingenic.indroidsync.data.DefaultProjo;
import cn.ingenic.indroidsync.data.Projo;
import cn.ingenic.indroidsync.data.ProjoList;
import cn.ingenic.indroidsync.data.ProjoType;


public class SmsSyncService extends Service{
	
	private String TAG="SmsSyncService";
	
	private String tag="In SmsSyncService....";
    private String DEBUG="sms2_debug";
    private boolean debug=true;
	private SmsDB mSmsDB;
	
	private ExecuteSmsHandler smsHandler;
	
	private final int INSERT_LOCAL_SMS = 0;
	
	private final int GET_CHANGED_SMS = 1;
	
	private final int SEND_THREAD_DATAS=2;
	
	private final int GET_CHECK=3;
	
	private final int DELAY_MESSAGE=4;
	
	private final int REMOVE_DELAY_MESSAGE=5;
	
    private final int CAN_LOADING_ALL=6;
	
	private final int CAN_GET_WATCH_NEED=7;
	
	private SmsChangeObserver mSmsChangeObserver=null;
	
	private  ExecuteDelayHandler delayHandler;
	
//	private LoadingHandler loadingHandler;
	
	private Pdu pdu=new Pdu(this);
	
	private final String INSERT_ALL_LOCAL_SMS="insert_all_local_sms";
	
	private boolean whenSavePowerModeUpdate=false;
	
	private int all_mode=11;
	
	private int add_mode=12;
	
	private int update_mode=13;
	
	private int check_mode=14;
	
	private long checkDelayTime=1200000;
	
	private final long DELAY_TIME=10000;
	
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
	private static final int SEND_THREAD=13;
	
	private static boolean Sms_Sync=false;
	private boolean canGetNeedDatas=true;
	
	private ResponseHandler mResponseSend;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
    
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mSmsDB=new SmsDB();
		mSmsDB.getSmsSyncProvider(this);
		if(debug)Log.i(DEBUG,"in SmsSyncService onCreate.");
		IntentFilter filter = new IntentFilter();
        filter.addAction(ContactAndSms2Columns.SYNC_CONNECTIVITY_TRUE_ACTION);
        filter.addAction(ContactAndSms2Columns.SYNC_CONNECTIVITY_FALSE_ACTION);
        filter.addAction(ContactAndSms2Columns.SmsColumn.CATCH_ALL_SMS_ACTION);
        filter.addAction(ContactAndSms2Columns.SmsColumn.SMS_WANT_DATAS_ACTION);
        filter.addAction(ContactAndSms2Columns.SmsColumn.SMS_SYNC_ACTION);
        registerReceiver(mBluetoothStateReceiver, filter);
        
        HandlerThread smsThread=new HandlerThread("change_thread");
		smsThread.start();
		smsHandler=new ExecuteSmsHandler(smsThread.getLooper());
		mSmsChangeObserver=new SmsChangeObserver();
		this.getContentResolver().registerContentObserver(Attribute.MMSSMS_CONTENT_URI, true, mSmsChangeObserver);
		
		HandlerThread delayThread=new HandlerThread("delay_thread");
		delayThread.start();
		delayHandler=new ExecuteDelayHandler(delayThread.getLooper());
		
		HandlerThread responseThread=new HandlerThread("response_thread");
		responseThread.start();
		mResponseSend=new ResponseHandler(responseThread.getLooper());
		
//		HandlerThread loadingThread=new HandlerThread("loading_thread");
//		loadingThread.start();
//		loadingHandler=new LoadingHandler(loadingThread.getLooper());
		
		
		Start(GET_CHECK);
		Start(GET_CHANGED_SMS);
		Start(SEND_THREAD_DATAS);
//		smsHandler.sendEmptyMessageDelayed(GET_CHECK, checkDelayTime);
		
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
            if(debug)Log.d(DEBUG,"in SmsSyncService action is :"+action);

			if(action.equals(ContactAndSms2Columns.SYNC_CONNECTIVITY_TRUE_ACTION)){
				if(Sms_Sync)SmsSync();
			}else if(action.equals(ContactAndSms2Columns.SYNC_CONNECTIVITY_FALSE_ACTION)){
				canGetNeedDatas=true;
				localSms=true;
			}else if(action.equals(ContactAndSms2Columns.SmsColumn.SMS_WANT_DATAS_ACTION)){
				
//				whenSavePowerModeUpdate=true;
				Start(GET_CHANGED_SMS);
				Start(SEND_THREAD_DATAS);
			}else if(action.equals(ContactAndSms2Columns.SmsColumn.CATCH_ALL_SMS_ACTION)){
				Start(INSERT_LOCAL_SMS);
				Start(SEND_THREAD_DATAS);
			}else if(action.equals(ContactAndSms2Columns.SmsColumn.SMS_SYNC_ACTION)){
					SmsSync();
			}
		}
		
	};
	
	private void SmsSync(){
		if(canGetNeedDatas){
			SYNC_STATUS=SYNC_NO_CHANGE;
//			whenSavePowerModeUpdate=true;
			Start(GET_CHECK);
			Start(GET_CHANGED_SMS);
			Start(SEND_THREAD_DATAS);
//			smsHandler.sendEmptyMessageDelayed(GET_CHECK, checkDelayTime);
			canGetNeedDatas=false;
		}
		
	}
	
	private String getAddress(){
   	 BluetoothAdapter ba=BluetoothAdapter.getDefaultAdapter();
        return ba.getAddress();
    }

	public void Start(int what){
        if(debug)Log.d(DEBUG,"in SmsSyncService start what is :"+((what==0)?"insert_all":"get_change"));
		
		 Message startMessage=new Message();
		 startMessage.what=what;
		 smsHandler.sendMessage(startMessage);
		
	} 
	
	private void Stop(){
		if(mSmsChangeObserver!=null)
		this.getContentResolver().unregisterContentObserver(mSmsChangeObserver);
	}
	
	private class ExecuteSmsHandler extends Handler{
		
		public ExecuteSmsHandler(Looper looper){
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case INSERT_LOCAL_SMS:
				delayHandler.sendEmptyMessageDelayed(DELAY_MESSAGE,DELAY_TIME);
				insertLocalSms();
				delayHandler.sendEmptyMessage(REMOVE_DELAY_MESSAGE);
				break;
			case GET_CHANGED_SMS:
				delayHandler.sendEmptyMessageDelayed(DELAY_MESSAGE,DELAY_TIME);
				getchange();
				delayHandler.sendEmptyMessage(REMOVE_DELAY_MESSAGE);
				break;
			case GET_CHECK:
				delayHandler.sendEmptyMessageDelayed(DELAY_MESSAGE,DELAY_TIME);
				checkSyncSms();
				delayHandler.sendEmptyMessage(REMOVE_DELAY_MESSAGE);
				break;
			case SEND_THREAD_DATAS:
				getThreadInfoAndSend();
				break;
		
			}
			
		}
	}
	
	private void getThreadInfoAndSend(){
		
		Cursor canonicalCursor=mSmsDB.getLocalSmsDB(this).getCanonicalDatas();
		
		ArrayList<Projo> sendList=new ArrayList<Projo>();
		//package canonicalCursor info to canonicalList
		ArrayList<Map<String,Object>> canonicalList=new ArrayList<Map<String,Object>>();
		if(canonicalCursor.getCount()!=0){
			canonicalCursor.moveToFirst();
			do{
				Map<String,Object> map=new HashMap<String,Object>();
				String address=canonicalCursor.getString(canonicalCursor.getColumnIndex(
						Attribute.CanonicalAddressesColumns.ADDRESS));
				String id=canonicalCursor.getString(canonicalCursor.getColumnIndex(
						Attribute.CanonicalAddressesColumns._ID));
				map.put("address", address);
				map.put("id", id);
				canonicalList.add(map);
			}while(canonicalCursor.moveToNext());
			
		}
		canonicalCursor.close();
		
		Cursor threadCursor=mSmsDB.getLocalSmsDB(this).getAllThreadDatas();
		//get data snippet and address to send
		if(threadCursor.getCount()!=0){
			threadCursor.moveToFirst();
			do{
				String id=threadCursor.getString(threadCursor.getColumnIndex(Attribute.Threads._ID));
				String data=threadCursor.getString(threadCursor.getColumnIndex(Attribute.Threads.DATE));
				String snippent=threadCursor.getString(threadCursor.getColumnIndex(Attribute.Threads.SNIPPET));
				String recipientIds=threadCursor.getString(threadCursor.getColumnIndex(Attribute.Threads.RECIPIENT_IDS));
				String[] recipentIdArray=recipientIds.split(" ");
				int length=recipentIdArray.length;
				String addressString="";
				for(int i=0;i<length;i++){
					String recipentid=recipentIdArray[i];
					for(Map<String,Object> map:canonicalList){
						if(map.get("id").equals(recipentid)){
							String add=map.get("address").toString();
							if(i==0){
								addressString=add;
							}else{
								addressString=addressString+";"+add;
							}
							break;
						}
					}
					
				}
				Projo projo = new DefaultProjo(EnumSet.allOf(SmsColumn2.class), ProjoType.DATA);
				projo.put(SmsColumn2.tag, ContactAndSms2Columns.SmsColumn.SMS_SEND_THREAD_DATAS);
				projo.put("thread_id", id);
				projo.put("address", addressString);
				projo.put("thread_data", data);
				projo.put("thread_snippet", snippent);
				sendList.add(projo);
				
			}while(threadCursor.moveToNext());
			
		}
		threadCursor.close();
		
		sendSmsToWatch(sendList,SEND_THREAD);
	}
	
	
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
				if(debug)Log.d(DEBUG,"in SmsSyncService send delay time sms message .");
				delayHandler.removeMessages(DELAY_MESSAGE);
				sendTitleMessage(ContactAndSms2Columns.SmsColumn.START_GET_SMS_CHANGE_MESSAGE,START_GET_ALL_CHANGE_MESSAGE);
//				sendTitleMessage(ContactAndSms2Columns.SmsColumn.SMS_DELAY_TIME_MESSAGE,DELAY_TIME_MESSAGE);
				delayHandler.sendEmptyMessageDelayed(DELAY_MESSAGE,DELAY_TIME);
				break;
			case REMOVE_DELAY_MESSAGE:
				delayHandler.removeMessages(DELAY_MESSAGE);
				break;
			}
		}
		
	}
    
//     private class LoadingHandler extends Handler{
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
//            	localSms=true;
//				break;
//            case CAN_GET_WATCH_NEED:
//            	canGetNeedDatas=true;
//            	break;
//			}
//		}
//		
//	}
	
	
	private boolean localSms=true;
	
	public void insertLocalSms(){
		if(!localSms){
			return;
		}
		Cursor cursor=mSmsDB.getLocalSmsDB(this).queryAllSms();
        if(debug)Log.d(DEBUG,"in SmsSyncService insertLocalSms local datas size is :"+cursor.getCount());
		if(cursor.getCount()==0){
			cursor.close();
			return;
		}
		ArrayList<Projo> smsList=new ArrayList<Projo>();
		cursor.moveToFirst();
		mSmsDB.getSyncSmsDB().deleteAllSms();
		sendTitleMessage(ContactAndSms2Columns.SmsColumn.START_INSERT_ALL_SMS_MESSAGE,START_GAT_ALL_MESSAGE);
		
		do{
			smsList.add(getDefaultInfo(
					cursor,all_mode,-1,-1,-1,-1,-1,-1));
		}while(cursor.moveToNext());
		cursor.close();
		sendTitleMessage(ContactAndSms2Columns.SmsColumn.STOP_INSERT_ALL_SMS_MESSAGE,STOP_GET_ALL_MESSAGE);
		if(smsList.size()!=0)
		sendSmsToWatch(smsList,SEND_ALL);
//		smsHandler.sendEmptyMessageDelayed(GET_CHANGED_SMS, checkDelayTime);
		
		localSms=false;
		canGetNeedDatas=false;
		smsHandler.removeMessages(INSERT_LOCAL_SMS);
//		loadingHandler.sendEmptyMessageDelayed(CAN_LOADING_ALL, 60000);
	}
	
	
	 private Projo getDefaultInfo(Cursor cursor,int mode,int syncError,int syncRead,int syncId,int watchId,int delete,int syncType){
		    //base info
		    int idLocal=-1;
		    int readLocal=-1;
		    int errorLocal=-1;
		    String data=null;
		    String body=null;
		    String threadId=null;
		    String address=null;
		    String status=null;
		    int type=-1;
		    String subject=null;
		    String service_center=null;
		    String data_send=null;
		    String send=null;
		    String protocol=null;
		    String reply_path_present=null;
		    if(SmsDB.proList.contains(Attribute.Sms._ID))idLocal=cursor.getInt(cursor.getColumnIndex(Attribute.Sms._ID));
		    if(SmsDB.proList.contains(Attribute.Sms.READ))readLocal=cursor.getInt(cursor.getColumnIndex(Attribute.Sms.READ));
		    if(SmsDB.proList.contains(Attribute.Sms.ERROR_CODE)) errorLocal=cursor.getInt(cursor.getColumnIndex(Attribute.Sms.ERROR_CODE));
			if(SmsDB.proList.contains(Attribute.Sms.DATE))data=cursor.getString(cursor.getColumnIndex(Attribute.Sms.DATE));
			if(SmsDB.proList.contains(Attribute.Sms.BODY))body=cursor.getString(cursor.getColumnIndex(Attribute.Sms.BODY));
			if(SmsDB.proList.contains(Attribute.Sms.THREAD_ID))threadId=cursor.getString(cursor.getColumnIndex(Attribute.Sms.THREAD_ID));
			if(SmsDB.proList.contains(Attribute.Sms.ADDRESS))address=cursor.getString(cursor.getColumnIndex(Attribute.Sms.ADDRESS));
			
			if(address==null)address=mSmsDB.getLocalSmsDB(this).getAddress(threadId);
			
			
			if(SmsDB.proList.contains(Attribute.Sms.STATUS))status=cursor.getString(cursor.getColumnIndex(Attribute.Sms.STATUS));
			
			
			if(SmsDB.proList.contains(Attribute.Sms.TYPE))type=cursor.getInt(cursor.getColumnIndex(Attribute.Sms.TYPE));
			if(SmsDB.proList.contains(Attribute.Sms.SUBJECT))subject=cursor.getString(cursor.getColumnIndex(Attribute.Sms.SUBJECT));
			if(SmsDB.proList.contains(Attribute.Sms.SERVICE_CENTER))service_center=cursor.getString(cursor.getColumnIndex(Attribute.Sms.SERVICE_CENTER));
			if(SmsDB.proList.contains(Attribute.Sms.DATE_SENT))data_send=cursor.getString(cursor.getColumnIndex(Attribute.Sms.DATE_SENT));
			if(SmsDB.proList.contains(Attribute.Sms.SEEN))send=cursor.getString(cursor.getColumnIndex(Attribute.Sms.SEEN));
			if(SmsDB.proList.contains(Attribute.Sms.PROTOCOL))protocol=cursor.getString(cursor.getColumnIndex(Attribute.Sms.PROTOCOL));
			if(SmsDB.proList.contains(Attribute.Sms.REPLY_PATH_PRESENT))reply_path_present=cursor.getString(cursor.getColumnIndex(Attribute.Sms.REPLY_PATH_PRESENT));
			
			
			Projo projo = new DefaultProjo(EnumSet.allOf(SmsColumn2.class), ProjoType.DATA);
			
//			long threadData=0;
			//sync db
			if(mode==all_mode){
				mSmsDB.getSyncSmsDB().insertOneSms(idLocal, readLocal, errorLocal,-1,0,type);
				projo.put(SmsColumn2.tag, ContactAndSms2Columns.SmsColumn.SMS_INSERT_ALL_TAG);
//				threadData=mSmsDB.getLocalSmsDB(this).getThreadData(threadId);
			}else if(mode==add_mode){
//				 if(SmsModule2.getMode()!=DefaultSyncManager.SAVING_POWER_MODE||whenSavePowerModeUpdate){
   				     mSmsDB.getSyncSmsDB().insertOneSms(idLocal, readLocal, errorLocal,-1,0,type);
//   				     threadData=mSmsDB.getLocalSmsDB(this).getThreadData(threadId);
//   			     }
				 
				 projo.put(SmsColumn2.tag, ContactAndSms2Columns.SmsColumn.SMS_INSERT_TAG);
			}else if(mode==update_mode){
				boolean change=false;
				
				  if(syncRead!=readLocal){
//					  if(SmsModule2.getMode()!=DefaultSyncManager.SAVING_POWER_MODE||whenSavePowerModeUpdate){
						  mSmsDB.getSyncSmsDB().updateSyncRead(readLocal, syncId); 
						 
//					  }
					  change=true;
					 
				  }
				 
				  if(syncError!=errorLocal){
//					  if(SmsModule2.getMode()!=DefaultSyncManager.SAVING_POWER_MODE||whenSavePowerModeUpdate){
						  mSmsDB.getSyncSmsDB().updateSyncError(errorLocal, syncId);
						
    					  
//					  }
					  change=true;
				  }
				  if(syncType!=type){
//					  if(SmsModule2.getMode()!=DefaultSyncManager.SAVING_POWER_MODE||whenSavePowerModeUpdate){
						  mSmsDB.getSyncSmsDB().updateSyncType(type, syncId);
						 
//					  }
					  change=true;
				  }
				 
				  if(change){
					  projo.put(SmsColumn2.watch_id, watchId);
					  projo.put(SmsColumn2.tag, ContactAndSms2Columns.SmsColumn.SMS_UPDATE_TAG);
					  projo.put("sync_id", syncId);
					  projo.put("sync_read", syncRead);
					  projo.put("sync_error", syncError);
					  projo.put("sync_type", syncType);
				  }else{
					  return null;
				  }
			}else if(mode==check_mode){
				projo.put(SmsColumn2.tag, ContactAndSms2Columns.SmsColumn.SMS_CHECK_MESSAGE);
				projo.put(SmsColumn2.watch_id, watchId);
				if(delete==1){
				   mSmsDB.getSyncSmsDB().deleteOneSms(idLocal); 
				}
			}
			
			
			projo.put(SmsColumn2.phone_id, idLocal);
			
//			projo.put(SmsColumn2.pdu, pduArray);
			projo.put(SmsColumn2.errorcode, errorLocal);
			projo.put(SmsColumn2.read, readLocal);
			projo.put(SmsColumn2.date, data);
			projo.put(SmsColumn2.type, type);
			projo.put(SmsColumn2.body, body);
			projo.put(SmsColumn2.address, address);
			projo.put(SmsColumn2.reply_path_present, reply_path_present);
			projo.put(SmsColumn2.status, status);
			projo.put(SmsColumn2.threadid, threadId);
			projo.put(SmsColumn2.subject, subject);
			projo.put(SmsColumn2.service_center, service_center);
			projo.put(SmsColumn2.data_send, data_send);
			projo.put(SmsColumn2.send, send);
			projo.put(SmsColumn2.protocol, protocol);
			projo.put(SmsColumn2.delete, delete);
			projo.put(SmsColumn2.phone_address,getAddress());
//			projo.put(SmsColumn2.threaddata, threadData);
			
			return projo;
			
	 }
	
	
	
      private class SmsChangeObserver extends ContentObserver{

		
		
		public SmsChangeObserver() {
			super(new Handler());
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onChange(boolean selfChange) {
			// TODO Auto-generated method stub
			super.onChange(selfChange);
			smsHandler.removeMessages(GET_CHANGED_SMS);
			smsHandler.sendEmptyMessageDelayed(GET_CHANGED_SMS, 1000);
			Start(SEND_THREAD_DATAS);
				
		}
		
	}
      
      private void getchange(){
    	  Cursor localCursor=mSmsDB.getLocalSmsDB(this).queryAllSms();
    	  Cursor syncCursor=mSmsDB.getSyncSmsDB().queryAllSms();
    	  LinkedList<String> localIdList=new LinkedList<String>();
    	  LinkedList<String> syncIdList=new LinkedList<String>();
    	  if(localCursor.getCount()!=0){
    		  localCursor.moveToFirst();
    		  do{
    			  String id=localCursor.getString(localCursor.getColumnIndex(Attribute.Sms._ID));
    			  localIdList.add(id);
    		  }while(localCursor.moveToNext());
    	  }
    	  localCursor.close();
    	  if(syncCursor.getCount()!=0){
    		  syncCursor.moveToFirst();
    		  do{
    			  String id=syncCursor.getString(syncCursor.getColumnIndex(SmsColumns.phone_id));
    			  syncIdList.add(id);
    		  }while(syncCursor.moveToNext());
    	  }
    	  syncCursor.close();
    	  ArrayList<String> addList=new ArrayList<String>();
    	  for(String localId:localIdList){
    		  if(syncIdList.contains(localId)){
    			  continue;
    		  }
    		  addList.add(localId);
    	  }
    	  ArrayList<String> deleteList=new ArrayList<String>();
    	  for(String syncId:syncIdList){
    		  if(localIdList.contains(syncId)){
    			  continue;
    		  }
    		  deleteList.add(syncId);
    	  }
    	  sendTitleMessage(ContactAndSms2Columns.SmsColumn.START_GET_SMS_CHANGE_MESSAGE,START_GET_ALL_CHANGE_MESSAGE);
    	  if(addList.size()!=0){
    		  doSmsAdd(addList);
    	  }
    	  if(deleteList.size()!=0){
    		  doSmsDelete(deleteList);
    	  }
    	  doSmsUpdate();
    	  sendTitleMessage(ContactAndSms2Columns.SmsColumn.STOP_GET_SMS_CHANGE_MESSAGE,STOP_GET_ALL_CHANGE_MESSAGE);
      }
      
    private int SYNC_NO_CHANGE=0;
  	private int SYNC_CHECK_CHANGED=1;
  	private int SYNC_ADD=2;
  	private int SYNC_DELETE=3;
  	private int SYNC_UPDATE=4;
  	private int SYNC_ALL=5;
  	private int SYNC_STATUS=-1;
      
      private void onChanged(ArrayList<Projo> addList,ArrayList<Projo> deleteList,ArrayList<Projo> updateList){
    	  if(addList!=null){
              if(debug)Log.d(DEBUG,"in SmsSyncService onChanged addList size is :"+addList.size());
    		  execute(addList,SEND_CHANGE_INSERT);
    		  SYNC_STATUS=SYNC_ADD;
    	  }
    	  if(deleteList!=null){
              if(debug)Log.d(DEBUG,"in SmsSyncService onChanged deleteList size is :"+deleteList.size());
    		  execute(deleteList,SEND_CHANGE_DELETE);
    		  SYNC_STATUS=SYNC_DELETE;
    	  }
    	  if(updateList!=null){
    		  if(updateList.size()!=0){
                  if(debug)Log.d(DEBUG,"in SmsSyncService onChanged updateList size is :"+updateList.size());
    			  execute(updateList,SEND_CHANGE_UPDATE);
    			  SYNC_STATUS=SYNC_UPDATE;
    		  }else{
    			  if(SYNC_STATUS==SYNC_NO_CHANGE){
                      if(debug)Log.d(DEBUG,"in SmsSyncService onChanged send on datas changed message");
    				  sendTitleMessage(ContactAndSms2Columns.SmsColumn.SMS_SYNC_NO_DATAS_CHANGED_MESSAGE,NO_DATAS_CHANGES);
    			  }
    		  }
    		  SYNC_STATUS=-1;
//    		  else if(whenSavePowerModeUpdate){
//    			  sendTitleMessage(ContactAndSms2Columns.SmsColumn.SMS_PHONE_HAVE_SEND_CHANGED_DATAS,SEND_HAVE_SEND_SAVE_POWER_DATAS_MESSAGE);
//    		  }
//    		  whenSavePowerModeUpdate=false;
    	  }
    	  
      }
      
      private void execute(ArrayList<Projo> smsList,int sendTag){
//    	  Log.i("yangliu",tag+"execute list size is :"+smsList.size()+"....and mode is :"+SmsModule2.getMode()+".....and whenSavePowerModeUpdate is "
//    			  +whenSavePowerModeUpdate);
//    	  if(SmsModule2.getMode()==DefaultSyncManager.SAVING_POWER_MODE&&!whenSavePowerModeUpdate){
//    			sendTitleMessage(ContactAndSms2Columns.SmsColumn.SMS_SAVE_POWER_CHANGED_MESSAGE,SEND_SAVE_POWER_MESSAGE);
//    		}else if(whenSavePowerModeUpdate){
//    			sendSmsToWatch(smsList,sendTag);
//    			sendTitleMessage(ContactAndSms2Columns.SmsColumn.SMS_PHONE_HAVE_SEND_CHANGED_DATAS,SEND_HAVE_SEND_SAVE_POWER_DATAS_MESSAGE);
//    		}else{
    			sendSmsToWatch(smsList,sendTag);
//    		}
      }
      
      private ArrayList<Projo> checkSyncSms(){
 		 Cursor syncCursor=mSmsDB.getSyncSmsDB().queryAllSms();
 		 ArrayList<Projo> responseList=new ArrayList<Projo>();
         if(debug)Log.i(DEBUG,"in SmsSyncService check Sync sms syncData size is :"+syncCursor.getCount());
 		 if(syncCursor.getCount()!=0){
 			sendTitleMessage(ContactAndSms2Columns.SmsColumn.START_GET_SMS_CHANGE_MESSAGE,START_GET_ALL_CHANGE_MESSAGE);
 			 syncCursor.moveToFirst();
 			 do{
 				 int phoneId=syncCursor.getInt(syncCursor.getColumnIndex(SmsColumns.phone_id));
 				 int watchId=syncCursor.getInt(syncCursor.getColumnIndex(SmsColumns.watch_id));
 				 int delete=syncCursor.getInt(syncCursor.getColumnIndex(SmsColumns.delete));
 				 if(watchId==-1){
 					 Cursor cursor=mSmsDB.getLocalSmsDB(this).querySmsById(phoneId);
 					 if(cursor.getCount()!=0){
 						 cursor.moveToFirst();
 						 Projo projo=getDefaultInfo(cursor,check_mode,-1,-1,-1,watchId,delete,-1);
 						 responseList.add(projo);
 					 }
 					 cursor.close();
 				 }
 			 }while(syncCursor.moveToNext());
 			sendTitleMessage(ContactAndSms2Columns.SmsColumn.STOP_GET_SMS_CHANGE_MESSAGE,STOP_GET_ALL_CHANGE_MESSAGE);
 		 }else{
 			 insertLocalSms();
 			SYNC_STATUS=SYNC_ALL;
 		 }
 		 syncCursor.close();
 		 if(responseList.size()!=0){
 			 if(debug)Log.d(DEBUG,"in smsSyncService checkSyncSms responseList is :"+responseList.size());
 			 sendSmsToWatch(responseList,SEND_WATCH_NEEDED_DATAS);
 			SYNC_STATUS=SYNC_CHECK_CHANGED;
 		 }
 		 return responseList;
 	  }
     
      
      private void doSmsAdd(ArrayList<String> addList){
    	  ArrayList<Projo> smsList=new ArrayList<Projo>();
    	  for(String id:addList){
    		  Cursor cursor=mSmsDB.getLocalSmsDB(this).querySmsById(Integer.parseInt(id));
    		  if(cursor.getCount()!=0){
    			  cursor.moveToFirst();
    			 
    			  smsList.add(getDefaultInfo(cursor,add_mode,-1,-1,-1,-1,-1,-1));
    		  }
    		  cursor.close();
    	  }
    	  
    	  onChanged(smsList,null,null);
      }

      private void doSmsDelete(ArrayList<String> deleteList){
    	  
    	  ArrayList<Projo> smsList=new ArrayList<Projo>();

    	  for(String id:deleteList){
    		  Cursor cursor=mSmsDB.getSyncSmsDB().queryOneSmsWatchIdCursor(Integer.parseInt(id));
    		  if(cursor.getCount()!=0){
    			  cursor.moveToFirst();
    			  int watchId=cursor.getInt(cursor.getColumnIndex(SmsColumns.watch_id));
    			  Projo projo = new DefaultProjo(EnumSet.allOf(SmsColumn2.class), ProjoType.DATA);
    			  projo.put(SmsColumn2.watch_id, watchId);
    			  projo.put(SmsColumn2.phone_id, Integer.parseInt(id));
    			  projo.put(SmsColumn2.address,getAddress());
    			  projo.put(SmsColumn2.tag, ContactAndSms2Columns.SmsColumn.SMS_DELETE_TAG);
    			  smsList.add(projo);
    		  }
    		  cursor.close();
//    		  if(SmsModule2.getMode()!=DefaultSyncManager.SAVING_POWER_MODE||whenSavePowerModeUpdate){
    			  mSmsDB.getSyncSmsDB().deleteOneSms(Integer.parseInt(id)); 
//    		  }
    		   
    	  }
    	  onChanged(null,smsList,null); 
      }
      
    
      private void doSmsUpdate(){
    	  Cursor syncCursor=mSmsDB.getSyncSmsDB().queryAllSms();
    	  ArrayList<Projo> smsList=new ArrayList<Projo>();
    	  if(syncCursor.getCount()!=0){
    		  syncCursor.moveToFirst();
    		  do{
    			  int syncid=syncCursor.getInt(syncCursor.getColumnIndex(SmsColumns.phone_id));
    			  int syncread=syncCursor.getInt(syncCursor.getColumnIndex(SmsColumns.read));
    			  int syncerror=syncCursor.getInt(syncCursor.getColumnIndex(SmsColumns.error_code));
    			  int watchid=syncCursor.getInt(syncCursor.getColumnIndex(SmsColumns.watch_id));
    			  int watchType=syncCursor.getInt(syncCursor.getColumnIndex(SmsColumns.type));
    			  Cursor localCursor=mSmsDB.getLocalSmsDB(this).querySmsById(syncid);
    			  if(localCursor.getCount()!=0){
    				  localCursor.moveToFirst();
    				      Projo projo=getDefaultInfo(localCursor,update_mode,syncerror,syncread,syncid,watchid,-1,watchType);
    				      if(projo!=null){
    				    	  smsList.add(projo);
    				      }
    				      
    			  }
    			  localCursor.close();
    		  }while(syncCursor.moveToNext());
    		    
    	  }
    	  syncCursor.close();
    	  onChanged(null,null,smsList);
      }
      
      private void returnCheckSendFailed(ArrayList<Projo> list){
          if(debug)Log.e(DEBUG,"in SmsSyncService returnCheckSendFailed list size is :"+list.size());
  		 for(Projo projo:list){
  			 int delete=Integer.parseInt(projo.get(SmsColumn2.delete).toString());
  			 int phoneId=Integer.parseInt(projo.get(SmsColumn2.phone_id).toString());
  			 if(delete==1){
  				mSmsDB.getSyncSmsDB().insertOneSms(phoneId, -1, -1,-1,1,-1);
  			 }
  		 }
  	  }
      
      
      
      private void responseDeleteFailed(ArrayList<Projo> deleteSmsList){
    	  int size=deleteSmsList.size();
          if(debug)Log.e(DEBUG,"in SmsSyncService responseDeleteFailed delete list size is :"+deleteSmsList.size());
    	  if(size==0){
    		  Log.e(TAG,"when return delete failed delete list size is 0 !");
    		  return;
    	  }
    	  for(Projo projo:deleteSmsList){
    		 int phoneId= Integer.parseInt(projo.get(SmsColumn2.phone_id).toString());
    		 mSmsDB.getSyncSmsDB().updateDeleteFailed(phoneId);
    	  }
    	 
      }
      
     
      
      private void responseUpdateFailed(ArrayList<Projo> updateList){
    	  for(Projo projo:updateList){
    		  Object object;
    		  int syncId=(object=projo.get("sync_id"))==null?-1:Integer.parseInt(object.toString());
    		  if(syncId==-1){
    			  continue;
    		  }
//    		  int syncRead=Integer.parseInt(projo.get("sync_read").toString());
//    		  int syncError=Integer.parseInt(projo.get("sync_error").toString());
//    		  int syncType=Integer.parseInt(projo.get("sync_type").toString());
    		  mSmsDB.getSyncSmsDB().updateSyncRead(-1, syncId); 
    		  mSmsDB.getSyncSmsDB().updateSyncError(-1, syncId);
    		  mSmsDB.getSyncSmsDB().updateSyncType(-1, syncId);
    	  }
      }
      
      
      private static ArrayList<Map<String,Object>> SEND_LIST=new ArrayList<Map<String,Object>>();
  	  private static int codeNumber=0;
      private synchronized void sendSmsToWatch(ArrayList<Projo> list,int sendTag){
  		if(debug)Log.d(DEBUG,tag+"sendSmsToWatch list size :"+list.size());
  		
  		Message sendMessage=mResponseSend.obtainMessage();
		sendMessage.obj=sendTag+","+codeNumber;
  		
  		DefaultSyncManager manager = DefaultSyncManager.getDefault();
  		Config config = new Config(SmsModule2.SMS2);
  		config.mCallback=sendMessage;
  		
  		
  		Map<String,Object> sendMap=new HashMap<String,Object>();
		sendMap.put("title", sendTag+","+codeNumber);
		
		sendMap.put("code_number", codeNumber);
		sendMap.put("list", list);
		SEND_LIST.add(sendMap);
		codeNumber++;

  		manager.request(config, list);//send

  		
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
    			if(debug)Log.i(DEBUG,"in SmsSyncService mResponseSend msg.obj is :"+msg.obj+" and ,msg.arg1 is :"+msg.arg1 );
    			for(int i=0;i<SEND_LIST.size();i++){
    				title=SEND_LIST.get(i).get("title").toString();
    				
    				if(title.equals(msg.obj)){
    					sendList=(ArrayList<Projo>)SEND_LIST.get(i).get("list");
    					index=i;
    					break;
    				}
    			}
    			if(title==null||index==-1){
    				Log.e(TAG,"when returnd title is null!!! or index = -1");
    				return;
    			}
    			String[] titleArray=title.split(",");
    			switch(Integer.parseInt(titleArray[0])){
    			case SEND_ALL:
    				if(debug)Log.i(DEBUG,"return SEND_ALL.");
    				if(msg.arg1!=DefaultSyncManager.SUCCESS){
    					Sms_Sync=true;
    					localSms=true;
    				}else{
    					Sms_Sync=false;
    				}
    				break;
    			case SEND_CHANGE_INSERT:
    				if(debug)Log.i(DEBUG,"return SEND_CHANGE_INSERT .");
    				if(msg.arg1!=DefaultSyncManager.SUCCESS){
//    					responseUpdateFailed(sendList);
    					Sms_Sync=true;
    				}else{
    					Sms_Sync=false;
    				}
    				break;
    			case SEND_CHANGE_UPDATE:
    				if(debug)Log.i(DEBUG,"return SEND_CHANGE_UPDATE .");
    				if(msg.arg1!=DefaultSyncManager.SUCCESS){
    					responseUpdateFailed(sendList);
    					Sms_Sync=true;
    				}else{
    					Sms_Sync=false;
    				}
    				break;
    			case SEND_CHANGE_DELETE:
    				if(debug)Log.i(DEBUG,"return SEND_CHANGE_DELETE . return is :"+msg.arg1);
    				if(msg.arg1!=DefaultSyncManager.SUCCESS){
    					responseDeleteFailed(sendList);
    					Sms_Sync=true;
    				}else{
    					
    					Sms_Sync=false;
    				}
    				break;
    			case SEND_WATCH_NEEDED_DATAS:
    				if(debug)Log.i(DEBUG,"return SEND_WATCH_NEEDED_DATAS .");
    				if(msg.arg1!=DefaultSyncManager.SUCCESS){
    					returnCheckSendFailed(sendList);
    					Sms_Sync=true;
    					canGetNeedDatas=true;
    				}else{
    					Sms_Sync=false;
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
    			case SEND_THREAD:
    				if(msg.arg1!=DefaultSyncManager.SUCCESS){
//    					sendSmsToWatch(sendList,SEND_THREAD);
    				}
    				
    			}
    			SEND_LIST.remove(index);
    		}
      };

      
      
      private synchronized void sendTitleMessage(String tag,int sendTag){
  		if(debug)Log.d(DEBUG,this.tag+"sendTitleMessage tag is "+tag);
  		ArrayList<Projo> list=new ArrayList<Projo>();
  		Projo projo = new DefaultProjo(EnumSet.allOf(SmsColumn2.class), ProjoType.DATA);
  		projo.put(SmsColumn2.tag, tag);
  		list.add(projo);
  		
  		sendSmsToWatch(list,sendTag);
  	}

}
