package cn.ingenic.indroidsync.contactsms.sms2;

import java.util.ArrayList;
import java.util.EnumSet;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;

import cn.ingenic.indroidsync.Config;
import cn.ingenic.indroidsync.DefaultSyncManager;
import cn.ingenic.indroidsync.Transaction;


import cn.ingenic.indroidsync.contactsms.contacts.ContactModule;
import cn.ingenic.indroidsync.contactsms.contacts.ContactTransaction;
import cn.ingenic.indroidsync.contactsms.manager.ContactAndSms2Columns;
import cn.ingenic.indroidsync.contactsms.sms2.SmsSyncDatabaseHelper.SmsColumns;
import cn.ingenic.indroidsync.data.DefaultProjo;
import cn.ingenic.indroidsync.data.Projo;
import cn.ingenic.indroidsync.data.ProjoType;


public class Sms2Transaction extends Transaction{
	
	private Pdu pdu;
	
	private String TAG="SMS2Transaction";
	
	private String tag="In Sms2Transaction";

    private String DEBUG="sms2_debug";
    private boolean debug=true;
	
	private SmsDB smsDB=new SmsDB();
	
	private static ExecuteDatasHandler Instance=null;
	private static HandlerThread thread=null;
	
	private ExecuteDatasHandler datasHandler;

	@Override
	public void onStart(ArrayList<Projo> datas) {
		// TODO Auto-generated method stub
		super.onStart(datas);
		if(datas==null){
			return;
		}
		if(datas.size()==0){
			return;
		}
		pdu=new Pdu(mContext);
		
		datasHandler=getExecuteDatasHandler();
		String tag=datas.get(0).get(SmsColumn2.tag).toString();
		if(debug)Log.d(DEBUG,this.tag+" datas size is "+datas.size()+", and tag is "+tag);

		if(tag.equals(ContactAndSms2Columns.SmsColumn.SMS_SAVE_POWER_CHANGED_MESSAGE)){
			//watch phone sms changed
			this.handlerSendMessage(ContactAndSms2Columns.SmsColumn.FOR_SMS_SAVE_POWER_MESSAGE, datas);
		}else if(tag.equals(ContactAndSms2Columns.SmsColumn.SMS_PHONE_HAVE_SEND_CHANGED_DATAS)){
			//watch phone have send sms changed datas
			
			this.handlerSendMessage(ContactAndSms2Columns.SmsColumn.FOR_SMS_PHONE_HAVE_SEND_CHANGED_DATAS, datas);
			
		}else if(tag.equals(ContactAndSms2Columns.SmsColumn.SMS_DELETE_TAG)){
			//watch delete
			this.handlerSendMessage(ContactAndSms2Columns.SmsColumn.FOR_SMS_DELETE_TAG, datas);
		}else if(tag.equals(ContactAndSms2Columns.SmsColumn.SMS_UPDATE_TAG)){
			//watch update
			this.handlerSendMessage(ContactAndSms2Columns.SmsColumn.FOR_SMS_UPDATE_TAG, datas);
		}else if(tag.equals(ContactAndSms2Columns.SmsColumn.SMS_INSERT_TAG)){
			//watch add 
			this.handlerSendMessage(ContactAndSms2Columns.SmsColumn.FOR_SMS_INSERT_TAG, datas);
		}else if(tag.equals(ContactAndSms2Columns.SmsColumn.SMS_INSERT_ALL_TAG)){
			//watch add all
			this.handlerSendMessage(ContactAndSms2Columns.SmsColumn.FOR_SMS_INSERT_ALL_TAG, datas);
		}else if(tag.equals(ContactAndSms2Columns.SmsColumn.RESPONSE_PHONE)){
			//phone update sync table
			this.handlerSendMessage(ContactAndSms2Columns.SmsColumn.FOR_RESPONSE_PHONE, datas);
		}else if(tag.equals(ContactAndSms2Columns.SmsColumn.WATCH_DELETE_SMS_TAG)){
			//phone delete sms
			this.handlerSendMessage(ContactAndSms2Columns.SmsColumn.FOR_WATCH_DELETE_SMS_TAG, datas);
		}else if(tag.equals(ContactAndSms2Columns.SmsColumn.SMSAPP_WANT_GET_NEW_DATAS_TAG)){
			//send broadcase to smsSyncService
			this.handlerSendMessage(ContactAndSms2Columns.SmsColumn.FOR_SMSAPP_WANT_GET_NEW_DATAS_TAG,datas);
			
		}else if(tag.equals(ContactAndSms2Columns.SmsColumn.SEND_NEW_SMS)){
			
			//watch accept new sms
//			SendNotification sn=new SendNotification(mContext);
//			sn.start(datas);
			PowerManager pm = ((PowerManager) mContext.getSystemService(Context.POWER_SERVICE));
			PowerManager.WakeLock mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
		                | PowerManager.ACQUIRE_CAUSES_WAKEUP, "indroidsync");
			
			mWakeLock.acquire();
			
			mWakeLock.release();
		}else if(tag.equals(ContactAndSms2Columns.SmsColumn.SEND_READ)){
			
			//phone accept send read 
			this.handlerSendMessage(ContactAndSms2Columns.SmsColumn.FOR_SEND_READ,datas);
		}else if(tag.equals(ContactAndSms2Columns.SmsColumn.SMS_WANT_SYNC_DATAS_MESSAGE)){
			
			this.handlerSendMessage(ContactAndSms2Columns.SmsColumn.FOR_SMS_WANT_SYNC_DATAS_MESSAGE,datas);
			
		}else if(tag.equals(ContactAndSms2Columns.SmsColumn.UPDATE_RESPONSE_TAG)){
			
			this.handlerSendMessage(ContactAndSms2Columns.SmsColumn.FOR_UPDATE_RESPONSE_TAG,datas);
		}else if(tag.equals(ContactAndSms2Columns.SmsColumn.WATCH_SEND_INIT_MESSAGE)){
//			Log.i("yangliu","sms receiver watch init message !!!!!!!!!!!!!!!!!!!!!!!!");
//			mContext.sendBroadcast(new Intent(ContactAndSms2Columns.SmsColumn.CATCH_ALL_SMS_ACTION));
		}
		
		
	}
	
	private void handlerSendMessage(int what,ArrayList<Projo> datas){
		Message message=new Message();
		message.what=what;
		Bundle bundle=new Bundle();
	    ArrayList arrayList=new ArrayList();
	    arrayList.add(datas);
	    bundle.putParcelableArrayList("list", arrayList);
	    message.setData(bundle);
		datasHandler.sendMessage(message);
	}
	
	
	private synchronized ExecuteDatasHandler getExecuteDatasHandler(){
		if(thread==null||!thread.isAlive()){
			thread=new HandlerThread("datas_sms_thread");
			thread.start();
		}
		if(Instance==null){
			Instance=new ExecuteDatasHandler(thread.getLooper());
		}
		return Instance;
	}
	  private class ExecuteDatasHandler extends Handler{
	    	
	    	public ExecuteDatasHandler(Looper looper){
	    			super(looper);	
			}

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				ArrayList<Projo> executeList=(ArrayList<Projo>) msg.getData().getParcelableArrayList("list").get(0);
				switch(msg.what){
				case ContactAndSms2Columns.SmsColumn.FOR_SMS_INSERT_ALL_TAG:
					smsDB.getWatchSmsDB(mContext).deleteAll();

					handlerSendMessage(ContactAndSms2Columns.FOR_RESPONSE,executeWatchInsert(executeList));
					break;

				case ContactAndSms2Columns.SmsColumn.FOR_SMS_INSERT_TAG:

					handlerSendMessage(ContactAndSms2Columns.FOR_RESPONSE,executeWatchInsert(executeList));
					break;
				case ContactAndSms2Columns.SmsColumn.FOR_SMS_DELETE_TAG:

					executeWatchDelete(executeList);
					break;
				case ContactAndSms2Columns.SmsColumn.FOR_SMS_UPDATE_TAG:

					handlerSendMessage(ContactAndSms2Columns.FOR_RESPONSE,executeWatchUpdate(executeList));
					break;
				case ContactAndSms2Columns.SmsColumn.FOR_WATCH_DELETE_SMS_TAG:
					executePhoneDelete(executeList);
					break;
				case ContactAndSms2Columns.SmsColumn.FOR_SMS_SAVE_POWER_MESSAGE:
					mContext.sendBroadcast(new Intent(
							ContactAndSms2Columns.SmsColumn.SMS_SAVE_POWER_CHANGED_MESSAGE_ACTION));
					break;
				
				case ContactAndSms2Columns.SmsColumn.FOR_SEND_ADDRESS_SMS2:
//					String phoneAddress= executeList.get(0).get(SmsColumn2.address).toString();
//					String localAddress=ContactTransaction.getLocalPhoneAddress(mContext);
//					if(localAddress!=null&&!localAddress.equals("")&&localAddress.equals(phoneAddress)){
//						ResponseAddressList(ContactAndSms2Columns.SmsColumn.SMS_SAME_PHONE_TAG);
//					}else{
//						ResponseAddressList(ContactAndSms2Columns.SmsColumn.SMS_DIFF_PHONE_TAG);
//						ContactTransaction.setLocalPhoneAddress(phoneAddress, mContext);
//					}
//					break;
				case ContactAndSms2Columns.SmsColumn.FOR_RESPONSE_PHONE:

					executePhoneInsert(executeList);
					break;
				case ContactAndSms2Columns.FOR_RESPONSE:

					if(executeList.size()!=0)
					sendSmsList(executeList);
					break;
				case ContactAndSms2Columns.SmsColumn.FOR_SEND_READ:
//					String address=executeList.get(0).get("address").toString();
//					smsDB.getLocalSmsDB(mContext).updateRead(address);
					break;
				case ContactAndSms2Columns.SmsColumn.FOR_UPDATE_RESPONSE_TAG:
					updateResponseRead(executeList);
					break;
				case ContactAndSms2Columns.SmsColumn.FOR_SMS_WANT_SYNC_DATAS_MESSAGE:
					mContext.sendBroadcast(new Intent(ContactAndSms2Columns.SmsColumn.SMS_SYNC_ACTION));
					break;
				case ContactAndSms2Columns.SmsColumn.FOR_SMSAPP_WANT_GET_NEW_DATAS_TAG:
					mContext.sendBroadcast(new Intent(ContactAndSms2Columns.SmsColumn.SMS_WANT_DATAS_ACTION));
					break;
				case ContactAndSms2Columns.SmsColumn.FOR_SMS_PHONE_HAVE_SEND_CHANGED_DATAS:
					mContext.sendBroadcast(new Intent(ContactAndSms2Columns.SmsColumn.SMS_PHONE_HAVE_SEND_CHANGED_DATAS_ACTION));
					break;
				}
				
				
			}
	    	
	    	
	  }
	  
	  private void updateResponseRead(ArrayList<Projo> list){
		  for(Projo projo:list){
			  Object objP=projo.get(SmsColumns.phone_id);
			  Object objW=projo.get(SmsColumns.watch_id);
			  if(objP==null||objW==null){
				  continue;
			  }
			  int phoneId=Integer.parseInt(projo.get(SmsColumns.phone_id).toString());
			  int watchId=Integer.parseInt(projo.get(SmsColumns.watch_id).toString());
			  smsDB.getSyncSmsDB().updateWatchId(phoneId, watchId);
		  }
	  }
	  
//	  private void ResponseAddressList(String tag){
//	    	ArrayList<Projo> list=new ArrayList<Projo>();
//	    	
//	    	Projo mProjo = new DefaultProjo(EnumSet.allOf(SmsColumn2.class), ProjoType.DATA);
//	    	mProjo.put(SmsColumn2.tag, tag);
//	    	list.add(mProjo);
//	    	sendSmsList(list);
//	  }

	  
	  private void executePhoneInsert(ArrayList<Projo> list){
		  for(Projo projo:list){
			  int phoneId=Integer.parseInt(projo.get(SmsColumn2.phone_id).toString());
			  int watchId=Integer.parseInt(projo.get(SmsColumn2.watch_id).toString());
			  smsDB.getSyncSmsDB().updateWatchId(phoneId, watchId);
		  }
	  }
	  
	  
	  private ArrayList<Projo> executeWatchInsert(ArrayList<Projo> list){
		  ArrayList<Projo> responseList=new ArrayList<Projo>();
		  int size=list.size();
		  for(int i=0;i<size;i++){
			  Object object;
			  int phoneId=Integer.parseInt((object=list.get(i).get(SmsColumn2.phone_id))==null?null:object.toString());
			  byte[][] pdus=(byte[][]) list.get(i).get(SmsColumn2.pdu);
			  int error=Integer.parseInt((object=list.get(i).get(SmsColumn2.errorcode))==null?null:object.toString());
			  int read=Integer.parseInt((object=list.get(i).get(SmsColumn2.read)) == null?null:object.toString());
			  long data=Long.valueOf((object=list.get(i).get(SmsColumn2.date)) == null ?null:object.toString());
			  String address=(object=list.get(i).get(SmsColumn2.address)) == null? null:object.toString();
			  String body=list.get(i).get(SmsColumn2.body).toString();
			  int type=Integer.parseInt(list.get(i).get(SmsColumn2.type).toString());
			  int threadId=Integer.parseInt(list.get(i).get(SmsColumn2.threadid).toString());
			  String subject=(object=list.get(i).get(SmsColumn2.subject))==null ?null:object.toString();
			  int status=Integer.parseInt((object=list.get(i).get(SmsColumn2.status))== null?null:object.toString());
			  String phoneMacAddress=(object=list.get(i).get(SmsColumn2.phone_mac_address))==null ? null:object.toString();
			  
			  String serviceCenter=(object=list.get(i).get(SmsColumn2.service_center)) == null? null:object.toString();
			  long dataSend=Long.valueOf((object=list.get(i).get(SmsColumn2.data_send)) == null? null:object.toString());
			  String protocol=(object=list.get(i).get(SmsColumn2.protocol)) == null? null:object.toString();
			  int send=Integer.parseInt(list.get(i).get(SmsColumn2.send).toString());
			  String replyPathPresent=(object=list.get(i).get(SmsColumn2.reply_path_present)) == null?null:object.toString();
			  

			  //send pdu
			  pdu.sendBroadCase(phoneId,pdus,error,read,data,address,body,type,threadId,subject,status,
					  phoneMacAddress,serviceCenter,dataSend,protocol,send,replyPathPresent);
			  

			  changesShowToMms(size,i,ContactAndSms2Columns.insert_watch_tag);

		  }
		  //end send null
		  pdu.sendBroadCase(-1,null,-1,-1,-1,null,null,-1,-1,null,-1,null,null,-1,null,-1,null);
		  changesShowToMms(-1,-1,"");
		  return responseList;
	  }
	  
	  private void executeWatchDelete(ArrayList<Projo> list){
		  int size=list.size();
		  for(int i=0;i<size;i++){
			  int watchId=Integer.parseInt(list.get(i).get(SmsColumn2.watch_id).toString());
			  smsDB.getWatchSmsDB(mContext).deleteOneSmsById(watchId);
			  changesShowToMms(size,i,ContactAndSms2Columns.delete_watch_tag);
		  }
		  changesShowToMms(-1,-1,"");
	  }
	  
	  private ArrayList<Projo> executeWatchUpdate(ArrayList<Projo> list){
		  ArrayList<Projo> responseList=new ArrayList<Projo>();
		  int size=list.size();
		  for(int i=0;i<size;i++){
			  Object object;
			  int phoneId=Integer.parseInt((object=list.get(i).get(SmsColumn2.phone_id))==null?null:object.toString());
			  byte[][] pdus=(byte[][]) list.get(i).get(SmsColumn2.pdu);
			  int error=Integer.parseInt((object=list.get(i).get(SmsColumn2.errorcode))==null?null:object.toString());
			  int read=Integer.parseInt((object=list.get(i).get(SmsColumn2.read)) == null?null:object.toString());
			  long data=Long.valueOf((object=list.get(i).get(SmsColumn2.date)) == null ?null:object.toString());
			  String address=(object=list.get(i).get(SmsColumn2.address)) == null? null:object.toString();
			  String body=list.get(i).get(SmsColumn2.body).toString();
			  int type=Integer.parseInt(list.get(i).get(SmsColumn2.type).toString());
			  int threadId=Integer.parseInt(list.get(i).get(SmsColumn2.threadid).toString());
			  String subject=(object=list.get(i).get(SmsColumn2.subject))==null ?null:object.toString();
			  int status=Integer.parseInt((object=list.get(i).get(SmsColumn2.status))== null?null:object.toString());
			  String phoneMacAddress=(object=list.get(i).get(SmsColumn2.phone_mac_address))==null ? null:object.toString();
			  
			  String serviceCenter=(object=list.get(i).get(SmsColumn2.service_center)) == null? null:object.toString();
			  long dataSend=Long.valueOf((object=list.get(i).get(SmsColumn2.data_send)) == null? null:object.toString());
			  String protocol=(object=list.get(i).get(SmsColumn2.protocol)) == null? null:object.toString();
			  int send=Integer.parseInt(list.get(i).get(SmsColumn2.send).toString());
			  String replyPathPresent=(object=list.get(i).get(SmsColumn2.reply_path_present)) == null?null:object.toString();
			  
			  
			  //yangliu
			  int watchId=Integer.parseInt(list.get(i).get(SmsColumn2.watch_id).toString());
//			  smsDB.getWatchSmsDB(mContext).deleteOneSmsById(watchId);

			  changesShowToMms(size,i,ContactAndSms2Columns.update_watch_tag);
			  smsDB.getWatchSmsDB(mContext).updateWatchSmsDB(watchId, read, error,type);
			  //send pdu
//			  pdu.sendBroadCase(phoneId,pdus,error,read,data,address,body,type,threadId,subject,status,
//					  phoneMacAddress,serviceCenter,dataSend,protocol,send,replyPathPresent);

		  }
		  //end send null
		  pdu.sendBroadCase(-1,null,-1,-1,-1,null,null,-1,-1,null,-1,null,null,-1,null,-1,null);
		  changesShowToMms(-1,-1,"");
		  return responseList;
	  }
	  
	  private void executePhoneDelete(ArrayList<Projo> list){
		  for(Projo projo:list){
			  int watchId=Integer.parseInt(projo.get(SmsColumn2.watch_id).toString());
			  Cursor cursor=smsDB.getSyncSmsDB().queryOneSmsPhoneIdByWatchId(watchId);
			  if(cursor.getCount()!=0){
				  cursor.moveToFirst();
				  int phoneId=cursor.getInt(cursor.getColumnIndex(SmsColumns.phone_id));
				  smsDB.getLocalSmsDB(mContext).deleteOneSmsById(phoneId);
			  }
			  cursor.close();
		  }
		 
	  }
	  
	  private void changesShowToMms(int totleSize,int nowSize,String tag){
			//send BroadcastReceiver
			Intent broadcasetocontact=new Intent(ContactAndSms2Columns.SmsColumn.SMS_SHOW_PROCESSBAR_ACTION);
			broadcasetocontact.putExtra("totle_size", totleSize);
			broadcasetocontact.putExtra("now_size", nowSize);
			broadcasetocontact.putExtra("tag", tag);
			mContext.sendBroadcast(broadcasetocontact);
		}
	 
	  
	  public void sendSmsList(ArrayList<Projo> smsList){
           if(debug)Log.d(DEBUG,"in Sms2Transaction smsList size is :"+smsList.size());
			DefaultSyncManager manager = DefaultSyncManager.getDefault();
			Config config = new Config(SmsModule2.SMS2);
			manager.request(config, smsList);
		}
	  

}
