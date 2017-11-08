package cn.ingenic.indroidsync.contactsms.manager;

import java.util.ArrayList;
import java.util.EnumSet;

import cn.ingenic.indroidsync.Config;
import cn.ingenic.indroidsync.DefaultSyncManager;


import cn.ingenic.indroidsync.contactsms.contacts.ContactColumn;
import cn.ingenic.indroidsync.contactsms.contacts.ContactModule;
import cn.ingenic.indroidsync.contactsms.contacts.provider.OperateDB;
import cn.ingenic.indroidsync.contactsms.sms2.SmsColumn2;
import cn.ingenic.indroidsync.contactsms.sms2.SmsDB;
import cn.ingenic.indroidsync.contactsms.sms2.SmsModule2;
import cn.ingenic.indroidsync.data.DefaultProjo;
import cn.ingenic.indroidsync.data.Projo;
import cn.ingenic.indroidsync.data.ProjoType;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.util.Log;

public class SyncReceiver extends BroadcastReceiver{
	
	
	
	private OperateDB db=new OperateDB();
	
	private SmsDB smsdb;
	
	private boolean hasConnectivity=false;
	
	 

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action=intent.getAction();
		smsdb=new SmsDB();
		boolean isWatch=DefaultSyncManager.isWatch();
		if(action.equals(Intent.ACTION_BOOT_COMPLETED)){
			Log.i("SyncReceiver","i am is "+(isWatch?"watch":"phone"));
			

			
		}else if(action.equals("com.android.contact.DELETE")){
			ArrayList<String> deleteList=intent.getStringArrayListExtra("deletelist");
			
			ArrayList<Projo> contactList=new ArrayList<Projo>();
			for(String deleteKey:deleteList){
				Projo projo = new DefaultProjo(EnumSet.allOf(ContactColumn.class), ProjoType.DATA);
				projo.put(ContactColumn.watchkey, deleteKey);
			    projo.put(ContactColumn.tag, ContactAndSms2Columns.ContactColumn.PHONE_DELETE);
			    contactList.add(projo);
			}
			sendContactsList(contactList);
			int mode=intent.getIntExtra("mode", -2);
			if(mode!=1&&mode!=2)return;
			//get new datas
			ArrayList<Projo> list=new ArrayList<Projo>();
			Projo projo = new DefaultProjo(EnumSet.allOf(ContactColumn.class), ProjoType.DATA);
		    projo.put(ContactColumn.tag, ContactAndSms2Columns.ContactColumn.CONTACT_WANT_DATAS);
		    list.add(projo);
		    sendContactsList(list);
			
		}else if(action.equals(ContactAndSms2Columns.ContactColumn.CONTACT_WANT_UPDATE_DATAS_ACTION)){
			ArrayList<Projo> list=new ArrayList<Projo>();
			Projo projo = new DefaultProjo(EnumSet.allOf(ContactColumn.class), ProjoType.DATA);
		    projo.put(ContactColumn.tag, ContactAndSms2Columns.ContactColumn.CONTACT_WANT_DATAS);
		    list.add(projo);
		    sendContactsList(list);
		}else if(action.equals(ContactAndSms2Columns.ContactColumn.SAVE_POWER_TO_RIGHT_AND_DATAS_CHANGED_ACTION)){
			Log.e("yangliu","in save power to right now datas changed action");
			ArrayList<Projo> list=new ArrayList<Projo>();
			Projo projo = new DefaultProjo(EnumSet.allOf(ContactColumn.class), ProjoType.DATA);
			projo.put(ContactColumn.tag, ContactAndSms2Columns.ContactColumn.SAVE_POWER_TO_RIGHT_AND_DATAS_CHANGED);
		    list.add(projo);
		    sendContactsList(list);
		}else if(action.equals(ContactAndSms2Columns.SmsColumn.SMS_HAVE_DELETE_ACTION)){
			ArrayList<Integer> intList=intent.getIntegerArrayListExtra("list");
			Log.e("yangliu","in SyncReceiver intList size is ::::"+intList.size());
			ArrayList<Projo> dList=new ArrayList<Projo>();
			for(Integer id:intList){
				Projo projo = new DefaultProjo(EnumSet.allOf(SmsColumn2.class), ProjoType.DATA);
				projo.put(SmsColumn2.tag, ContactAndSms2Columns.SmsColumn.WATCH_DELETE_SMS_TAG);
				projo.put(SmsColumn2.watch_id, id);
				dList.add(projo);
			}
			sendSmsList(dList);
			
			int mode=intent.getIntExtra("mode", -2);
			if(mode!=1&&mode!=2)return;
			
			ArrayList<Projo> list=new ArrayList<Projo>();
			Projo projo = new DefaultProjo(EnumSet.allOf(SmsColumn2.class), ProjoType.DATA);
			projo.put(SmsColumn2.tag, ContactAndSms2Columns.SmsColumn.SMSAPP_WANT_GET_NEW_DATAS_TAG);
			list.add(projo);
			sendSmsList(list);
		}else if(action.equals(ContactAndSms2Columns.SmsColumn.SMS_WANT_GET_NEW_DATAS_ACTION)){
			Log.e("yangliu","in syncReceiver sms want get new datas action ");
			ArrayList<Projo> list=new ArrayList<Projo>();
			Projo projo = new DefaultProjo(EnumSet.allOf(SmsColumn2.class), ProjoType.DATA);
			projo.put(SmsColumn2.tag, ContactAndSms2Columns.SmsColumn.SMSAPP_WANT_GET_NEW_DATAS_TAG);
			list.add(projo);
			sendSmsList(list);
			
		}else if(action.equals(ContactAndSms2Columns.SmsColumn.SMS_SAVE_POWER_TO_RIGHT_AND_DATAS_CHANGED_ACTION)){
			ArrayList<Projo> list=new ArrayList<Projo>();
			Projo projo = new DefaultProjo(EnumSet.allOf(SmsColumn2.class), ProjoType.DATA);
			projo.put(SmsColumn2.tag, ContactAndSms2Columns.SmsColumn.SMSAPP_WANT_GET_NEW_DATAS_TAG);
			list.add(projo);
			sendSmsList(list);
		}else if(action.equals(ContactAndSms2Columns.SmsColumn.WATCH_RESPONSE_SMS_ACTION)){
			ArrayList<String> responseList=intent.getStringArrayListExtra("list");
			ArrayList<Projo> resList=new ArrayList<Projo>();
			for(String oneSms:responseList){
				String[] info=oneSms.split(",");
				if(info.length==2){
				   int phoneId=Integer.parseInt(info[0]);
				   int watchId=Integer.parseInt(info[1]);
				   Log.e("yangliu","in syncReceiver when watch_response_sms_action");
				   Projo resProjo = new DefaultProjo(EnumSet.allOf(SmsColumn2.class), ProjoType.DATA);
				   resProjo.put(SmsColumn2.phone_id, phoneId);
				   resProjo.put(SmsColumn2.watch_id, watchId);
				   resProjo.put(SmsColumn2.tag, ContactAndSms2Columns.SmsColumn.RESPONSE_PHONE);
				   resList.add(resProjo);
				}else{
					Log.e("yangliu","we don not get watch Id!!!");
				}
			}
			if(resList.size()!=0)
			sendSmsList(resList);
		}else if(action.equals(ContactAndSms2Columns.SmsColumn.SMS_SEND_READ_ACTION)){
			String address=intent.getStringExtra("address");
			Log.e("yangliu","in sync receiver sms send read address is :"+address);
			ArrayList<Projo> resList=new ArrayList<Projo>();
			Projo resProjo = new DefaultProjo(EnumSet.allOf(SmsColumn2.class), ProjoType.DATA);
			resProjo.put(SmsColumn2.address, address);
			resProjo.put(SmsColumn2.tag, ContactAndSms2Columns.SmsColumn.SEND_READ);
			resList.add(resProjo);
			sendSmsList(resList);
			
			int mode=intent.getIntExtra("mode", -2);
			if(mode!=1&&mode!=2)return;
			
			ArrayList<Projo> list=new ArrayList<Projo>();
			Projo projo = new DefaultProjo(EnumSet.allOf(SmsColumn2.class), ProjoType.DATA);
			projo.put(SmsColumn2.tag, ContactAndSms2Columns.SmsColumn.SMSAPP_WANT_GET_NEW_DATAS_TAG);
			list.add(projo);
			sendSmsList(list);
			
		}
		
		
		
	}
	
	public void sendContactsList(ArrayList<Projo> contactList){
		DefaultSyncManager manager = DefaultSyncManager.getDefault();
		Config config = new Config(ContactModule.CONTACT);
		manager.request(config, contactList);
	}
	
	private void sendSmsList(ArrayList<Projo> list){
		DefaultSyncManager manager = DefaultSyncManager.getDefault();
		Config config=new Config(SmsModule2.SMS2);
		manager.request(config, list);
	}

}
