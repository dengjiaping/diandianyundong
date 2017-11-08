package cn.ingenic.indroidsync.contactsms.contacts;

import java.util.ArrayList;
import java.util.EnumSet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.provider.ContactsContract.Contacts;
import android.util.Log;

import cn.ingenic.indroidsync.Config;
import cn.ingenic.indroidsync.DefaultSyncManager;
import cn.ingenic.indroidsync.Transaction;

import cn.ingenic.indroidsync.contactsms.contacts.provider.OperateDB;
import cn.ingenic.indroidsync.contactsms.contacts.provider.ContactSyncDatabaseHelper.ContactColumns;
import cn.ingenic.indroidsync.contactsms.manager.ContactAndSms2Columns;
import cn.ingenic.indroidsync.data.DefaultProjo;
import cn.ingenic.indroidsync.data.Projo;
import cn.ingenic.indroidsync.data.ProjoType;

public class ContactTransaction extends Transaction {
	
	private String TAG="ContactTransaction";
	
	private String tag="In ContactTransaction...";
    private String DEBUG="contact_debug";
    private boolean debug=true;
	
	private OperateDB db=new OperateDB();
	
	
	
	
	private ExecuteDatasHandler datasHandler;
	

	@Override
	public void onStart(final ArrayList<Projo> datas) {
		// TODO Auto-generated method stub
		super.onStart(datas);
		
		if(datas==null){
			return;
		}
		if(datas.size()==0){
			return;
		}
        datasHandler=getExecuteDatasHandler();
        String tag=datas.get(0).get(ContactColumn.tag).toString();
        if(debug)Log.d(DEBUG,"in ContactTransaction tag is :"+tag+" , and receiver data size is :"+datas.size());

        if(tag.equals(ContactAndSms2Columns.ContactColumn.WATCH_TAG_INSERT_ALL)){
        	
   		    this.handlerSendMessage(ContactAndSms2Columns.ContactColumn.FOR_WATCH_INSERT_ALL, datas);
        }else if(tag.equals(ContactAndSms2Columns.ContactColumn.WATCH_TAG_INSERT)){
   		   
   		    this.handlerSendMessage(ContactAndSms2Columns.ContactColumn.FOR_WATCH_INSERT, datas);
        }else if(tag.equals(ContactAndSms2Columns.ContactColumn.WATCH_TAG_DELETE)){
        	
		    this.handlerSendMessage(ContactAndSms2Columns.ContactColumn.FOR_WATCH_DELETE, datas);
        }else if(tag.equals(ContactAndSms2Columns.ContactColumn.WATCH_TAG_UPDATE)){
        	
   		    this.handlerSendMessage(ContactAndSms2Columns.ContactColumn.FOR_WATCH_UPDATE, datas);
        }else if(tag.equals(ContactAndSms2Columns.ContactColumn.PHONE_TAG)){
        	
   		    this.handlerSendMessage(ContactAndSms2Columns.ContactColumn.FOR_PHONE_TAG, datas);
        }else if(tag.equals(ContactAndSms2Columns.ContactColumn.PHONE_DELETE)){
        	
   		    this.handlerSendMessage(ContactAndSms2Columns.ContactColumn.FOR_PHONE_DELETE, datas);
        }else if(tag.equals(ContactAndSms2Columns.ContactColumn.SAVE_POWER_MESSAGE)){
        	
        	handlerSendMessage(ContactAndSms2Columns.ContactColumn.FOR_SAVE_POWER_MESSAGE,datas);
        	
        }else if(tag.equals(ContactAndSms2Columns.ContactColumn.CONTACT_WANT_DATAS)){
        	
        	handlerSendMessage(ContactAndSms2Columns.ContactColumn.FOR_CONTACT_WANT_DATAS,datas);
        	
        }else if(tag.equals(ContactAndSms2Columns.ContactColumn.PHONE_HAVE_SEND_CONTACTS)){
        	
        	handlerSendMessage(ContactAndSms2Columns.ContactColumn.FOR_PHONE_HAVE_SEND_CONTACTS,datas);
        	
        }else if(tag.equals(ContactAndSms2Columns.ContactColumn.SAVE_POWER_TO_RIGHT_AND_DATAS_CHANGED)){
        	
        	handlerSendMessage(ContactAndSms2Columns.ContactColumn.FOR_SAVE_POWER_TO_RIGHT_AND_DATAS_CHANGED,datas);
        	
        }else if(tag.equals(ContactAndSms2Columns.ContactColumn.SEND_WATCHKEY_MESSAGE)){
        	
        	handlerSendMessage(ContactAndSms2Columns.ContactColumn.FOR_SEND_WATCHKEY_MESSAGE,datas);
        }else if(tag.equals(ContactAndSms2Columns.ContactColumn.SYNC_CONTACTS_MESSAGE)){
        	
        	handlerSendMessage(ContactAndSms2Columns.ContactColumn.FOR_SYNC_CONTACTS_MESSAGE,datas);
        	
        }else if(tag.equals(ContactAndSms2Columns.ContactColumn.WATCH_SEND_INIT_CONTACT_MESSAGE)){
//        	Log.i("yangliu","contact receiver watch init message !!!!!!!!!!!!!!!!!!!!!!!!");
//        	mContext.sendBroadcast(new Intent(ContactAndSms2Columns.ContactColumn.CATCH_ALL_COTNACTS_DATAS_ACTION));
        }
 
	}
	
	
	public void handlerSendMessage(int what,ArrayList<Projo> datas){
		Message message=new Message();
		message.what=what;
		Bundle bundle=new Bundle();
	    ArrayList arrayList=new ArrayList();
	    arrayList.add(datas);
	    bundle.putParcelableArrayList("list", arrayList);
	    message.setData(bundle);
		datasHandler.sendMessage(message);
	}
	
	
	private static ExecuteDatasHandler Instance=null;
	private static HandlerThread thread=null;
	private synchronized ExecuteDatasHandler getExecuteDatasHandler(){
		if(thread==null||!thread.isAlive()){
			thread=new HandlerThread("datas_thread");
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
			switch(msg.what){
			case ContactAndSms2Columns.ContactColumn.FOR_WATCH_UPDATE:
				ArrayList<Projo> exeListUpdate=(ArrayList<Projo>) msg.getData().getParcelableArrayList("list").get(0);

				handlerSendMessage(ContactAndSms2Columns.ContactColumn.FOR_RESPONSE,executeWatchUpdate(exeListUpdate));

				break;
			case ContactAndSms2Columns.ContactColumn.FOR_WATCH_DELETE:
				ArrayList<Projo> exeListDelete=(ArrayList<Projo>) msg.getData().getParcelableArrayList("list").get(0);
				executeWatchDelete(exeListDelete);
				break;
			case ContactAndSms2Columns.ContactColumn.FOR_WATCH_INSERT_ALL:
				db.getWatchContactDB(mContext).deleteAll();
				ArrayList<Projo> exeListInsertAll=(ArrayList<Projo>) msg.getData().getParcelableArrayList("list").get(0);

				handlerSendMessage(ContactAndSms2Columns.ContactColumn.FOR_RESPONSE,executeWatchInsert(exeListInsertAll));

				break;
			case ContactAndSms2Columns.ContactColumn.FOR_WATCH_INSERT:
				ArrayList<Projo> exeListInsert=(ArrayList<Projo>) msg.getData().getParcelableArrayList("list").get(0);

				handlerSendMessage(ContactAndSms2Columns.ContactColumn.FOR_RESPONSE,executeWatchInsert(exeListInsert));

				break;
			case ContactAndSms2Columns.ContactColumn.FOR_PHONE_TAG:
				ArrayList<Projo> exeListPhone=(ArrayList<Projo>) msg.getData().getParcelableArrayList("list").get(0);
				phoneTag(exeListPhone);
				break;
			case ContactAndSms2Columns.ContactColumn.FOR_PHONE_DELETE:
				
				ArrayList<Projo> exeListPhoneDelete=(ArrayList<Projo>) msg.getData().getParcelableArrayList("list").get(0);
				phoneDelete(exeListPhoneDelete);
				break;
			case ContactAndSms2Columns.ContactColumn.FOR_RESPONSE:
				ArrayList<Projo> responseList=(ArrayList<Projo>) msg.getData().getParcelableArrayList("list").get(0);
				if(responseList.size()!=0){
					sendContactsList(responseList);
				}
				break;
			case ContactAndSms2Columns.ContactColumn.FOR_SEND_ADDRESS:
//				String localSaveAddress=getLocalPhoneAddress(mContext);
//				ArrayList<Projo> sendList=(ArrayList<Projo>) msg.getData().getParcelableArrayList("list").get(0);
//	        	String phoneAddress=sendList.get(0).get(ContactColumn.address).toString();
//                Log.e("yangliu",tag+"phoneAddress is "+phoneAddress);
//	            Cursor allCursor=db.getWatchContactDB(mContext).queryAll();
//	            ArrayList<Projo> list=null;
//	        	if(localSaveAddress!=null&&!localSaveAddress.equals("")
//	        			&&localSaveAddress.equals(phoneAddress)&&allCursor.getCount()!=0){
//
//	        		list=ResponseAddressList(ContactAndSms2Columns.ContactColumn.SAME_PHONE,null);
//	        		db.getWatchContactDB(mContext).updateWatchDeleteContacts();
//
//	        	}else{
//	        		Log.i(TAG,"send diff_phone message this case because watch contacts count is 0 or different phone");
//	        	    list=ResponseAddressList(ContactAndSms2Columns.ContactColumn.DIFF_PHONE,null);
//	        	
//	        		setLocalPhoneAddress(phoneAddress,mContext);
//	        	}
//	        	sendContactsList(list);
//	        	allCursor.close();
				break;
			case ContactAndSms2Columns.ContactColumn.FOR_SAME_PHONE_DATAS:
				ArrayList<Projo> samePhoneList=(ArrayList<Projo>) msg.getData().getParcelableArrayList("list").get(0);
				executeNOConnectDelete(samePhoneList);
				break;
			case ContactAndSms2Columns.ContactColumn.For_SAME_PHONE:
				mContext.sendBroadcast(new Intent(ContactAndSms2Columns.SAME_PHONE_ACTION));
				break;
				
			case ContactAndSms2Columns.ContactColumn.FOR_SEND_WATCHKEY_MESSAGE:
				ArrayList<Projo> executeList=(ArrayList<Projo>) msg.getData().getParcelableArrayList("list").get(0);
				executeWatchKey(executeList);
				break;
			case ContactAndSms2Columns.ContactColumn.FOR_SAVE_POWER_MESSAGE:
				mContext.sendBroadcast(new Intent(ContactAndSms2Columns.ContactColumn.SAVE_POWER_MESSAGE_ACTION));
				break;
			case ContactAndSms2Columns.ContactColumn.FOR_CONTACT_WANT_DATAS:
				mContext.sendBroadcast(new Intent(ContactAndSms2Columns.ContactColumn.UPDATE_ACTION));
				break;
			case ContactAndSms2Columns.ContactColumn.FOR_PHONE_HAVE_SEND_CONTACTS:
				mContext.sendBroadcast(new Intent(ContactAndSms2Columns.ContactColumn.PHONE_HAVE_SEND_CONTACTS_ACTION));
				break;
			case ContactAndSms2Columns.ContactColumn.FOR_SAVE_POWER_TO_RIGHT_AND_DATAS_CHANGED:
				mContext.sendBroadcast(new Intent(ContactAndSms2Columns.ContactColumn.UPDATE_ACTION));
				break;
			case ContactAndSms2Columns.ContactColumn.FOR_SYNC_CONTACTS_MESSAGE:
				mContext.sendBroadcast(new Intent(ContactAndSms2Columns.ContactColumn.CONTACT_SYNC_ACTION));
				break;
 
			}
		}
		
	} 
    
    private final static String PHONE_ADDRESS="phone_address";
    private final static String ADDRESS="address";
      
    public static String getLocalPhoneAddress(Context context){  
    	return context.getSharedPreferences(PHONE_ADDRESS, Context.MODE_PRIVATE).getString(ADDRESS,"");
    }
//    public static void setLocalPhoneAddress(String address,Context context){
//    	SharedPreferences.Editor editor=context.getSharedPreferences(PHONE_ADDRESS, Context.MODE_PRIVATE).edit();
//    	
//		editor.putString(ADDRESS, address);
//		editor.apply();
//    }
    
//    private ArrayList<Projo> ResponseAddressList(String tag,Cursor deletecursor){
//    	ArrayList<Projo> list=new ArrayList<Projo>();
//    	if(deletecursor==null){
//    	Projo mProjo = new DefaultProjo(EnumSet.allOf(ContactColumn.class), ProjoType.DATA);
//    	mProjo.put(ContactColumn.tag, tag);
//    	list.add(mProjo);
//    	}else{
//    		deletecursor.moveToFirst();
//    		do{
//    			Projo mProjo = new DefaultProjo(EnumSet.allOf(ContactColumn.class), ProjoType.DATA);
//    	    	mProjo.put(ContactColumn.tag, tag);
//    			String watchKey=deletecursor.getString(deletecursor.getColumnIndex(Contacts.LOOKUP_KEY));
//    			mProjo.put(ContactColumn.watchkey, watchKey);
//    			list.add(mProjo);
//    		}while(deletecursor.moveToNext());
//    		deletecursor.close();
//    	}
//    	return list;
//    }
    
    private void executeNOConnectDelete(ArrayList<Projo> list){
    	for(Projo projo:list){
    		String watchKey=projo.get(ContactColumn.watchkey).toString();
    		Cursor cursor=db.getSyncDB().querySyncContactByWatchKey(watchKey);
    		if(cursor.getCount()==0){
    			cursor.close();
    			continue;
    		}
    		cursor.moveToFirst();
    		String phoneKey=cursor.getString(cursor.getColumnIndex(ContactColumns.PHONELOOKUPKEY)).toString();
    		db.getContactDB(mContext).deleteContactByPhoneKey(phoneKey);
    		cursor.close();
    	}
    }
      
    private ArrayList<Projo> executeWatchInsert(ArrayList<Projo> responseList){
    	ArrayList<Projo> reponseList=new ArrayList<Projo>();
		int size=responseList.size();
		for(int i=0;i<size;i++){
			Projo projo=responseList.get(i);
			String oneVCard=projo.get(ContactColumn.onevcard).toString();
			String phoneKey=projo.get(ContactColumn.phonekey).toString();
			ParseOneContact parse=new ParseOneContact(mContext,oneVCard);
			
			String newWatchKey=parse.start();
			db.getWatchContactDB(mContext).updatePhoneKey(newWatchKey, phoneKey);
			changesShowToContacts(size,i,ContactAndSms2Columns.ContactColumn.WATCH_TAG_INSERT);
			
			Projo mProjo = new DefaultProjo(EnumSet.allOf(ContactColumn.class), ProjoType.DATA);
			mProjo.put(ContactColumn.phonekey, phoneKey);
			mProjo.put(ContactColumn.watchkey, newWatchKey);
			mProjo.put(ContactColumn.tag, ContactAndSms2Columns.ContactColumn.PHONE_TAG);
			reponseList.add(mProjo);
		}
		changesShowToContacts(-1,-1,"");
		return reponseList;
    }
    
    private void executeWatchDelete(ArrayList<Projo> responseList){
		int size=responseList.size();
		for(int i=0;i<size;i++){
			Projo projo=responseList.get(i);
			String phoneKey=projo.get(ContactColumn.phonekey).toString();
			String watchKey=projo.get(ContactColumn.watchkey).toString();
			if(watchKey==null||watchKey.equals("")){
//				db.getWatchContactDB(mContext).deleteOneContactByPhoneKey(phoneKey);
			}else{
				db.getWatchContactDB(mContext).deleteOneContactByWatchKey(watchKey);
			}
			
			changesShowToContacts(size,i,ContactAndSms2Columns.ContactColumn.WATCH_TAG_DELETE);
		}
		changesShowToContacts(-1,-1,"");
    }
    
    private ArrayList<Projo> executeWatchUpdate(ArrayList<Projo> responseList){
    	ArrayList<Projo> reponseList=new ArrayList<Projo>();
		int size=responseList.size();
		for(int i=0;i<size;i++){
			Projo projo=responseList.get(i);
			String oneVCard=projo.get(ContactColumn.onevcard).toString();
			String phoneKey=projo.get(ContactColumn.phonekey).toString();
			String watchKey=projo.get(ContactColumn.watchkey).toString();
            if(watchKey==null||watchKey.equals("")){
//				db.getWatchContactDB(mContext).deleteOneContactByPhoneKey(phoneKey);
			}else{
				db.getWatchContactDB(mContext).deleteOneContactByWatchKey(watchKey);
			}
			ParseOneContact parse=new ParseOneContact(mContext,oneVCard.toString());
			   
			   String newWatchKey=parse.start();
			   db.getWatchContactDB(mContext).updatePhoneKey(newWatchKey, phoneKey);
			   changesShowToContacts(size,i,ContactAndSms2Columns.ContactColumn.WATCH_TAG_UPDATE);
			   
			   Projo mProjo = new DefaultProjo(EnumSet.allOf(ContactColumn.class), ProjoType.DATA);
			   mProjo.put(ContactColumn.phonekey, phoneKey);
			   mProjo.put(ContactColumn.watchkey, newWatchKey);
			   mProjo.put(ContactColumn.tag, ContactAndSms2Columns.ContactColumn.PHONE_TAG);
			   reponseList.add(mProjo);
		}
		changesShowToContacts(-1,-1,"");
		return reponseList;
    }
    private void phoneTag(ArrayList<Projo> responseList){
    	int size=responseList.size();
		for(int i=0;i<size;i++){
			 Projo projo=responseList.get(i);
			 String phoneKey=projo.get(ContactColumn.phonekey).toString();
			 Object watch=projo.get(ContactColumn.watchkey);
			 String watchKey=null;
			 if(watch!=null){
				 watchKey=projo.get(ContactColumn.watchkey).toString();
			 }
			 
			 db.getSyncDB().updateContactByPhoneKey(phoneKey, watchKey);
		}
		ArrayList<Projo> list=new ArrayList<Projo>();
		Projo mProjo = new DefaultProjo(EnumSet.allOf(ContactColumn.class), ProjoType.DATA);
		mProjo.put(ContactColumn.tag, ContactAndSms2Columns.ContactColumn.PHONE_HAVE_INSERT_WATCH_LOOKUPKEY);
		list.add(mProjo);
		sendContactsList(list);
    }
    
    private void phoneDelete(ArrayList<Projo> responseList){
    	int size=responseList.size();
		for(int i=0;i<size;i++){
			Projo projo=responseList.get(i);
			String watchKey=projo.get(ContactColumn.watchkey).toString();
			String phoneKey=projo.get(ContactColumn.phonekey).toString();
//			Cursor cursor=db.getSyncDB().querySyncContactByWatchKey(watchKey);
//			if(cursor.getCount()!=0){
//				cursor.moveToFirst();
//				db.getContactDB(mContext).deleteContactByPhoneKey(
//						cursor.getString(cursor.getColumnIndex(ContactColumns.PHONELOOKUPKEY)));
//			}else{
				db.getContactDB(mContext).deleteContactByPhoneKey(phoneKey);
//			}
//			cursor.close();
		}
    }
    
    private void executeWatchKey(ArrayList<Projo> responseList){
    	for(Projo projo:responseList){
    		String watchKey=projo.get(ContactColumn.watchkey).toString();
    		String phoneKey=projo.get(ContactColumn.phonekey).toString();
    		if(watchKey==null||watchKey.equals("")){
    			mContext.sendBroadcast(new Intent(ContactAndSms2Columns.ContactColumn.CATCH_ALL_COTNACTS_DATAS_ACTION));
    			return;
    		}
    		db.getSyncDB().updataWatchKey(phoneKey, watchKey);
    	}
    }

	
	private void changesShowToContacts(int totleSize,int nowSize,String tag){
		//send BroadcastReceiver
		Intent broadcasetocontact=new Intent(ContactAndSms2Columns.ContactColumn.ToContactReceiverAction);
		broadcasetocontact.putExtra("totle_size", totleSize);
		broadcasetocontact.putExtra("now_size", nowSize);
		broadcasetocontact.putExtra("tag", tag);
		mContext.sendBroadcast(broadcasetocontact);
	}
    
	public void sendContactsList(ArrayList<Projo> contactList){
		DefaultSyncManager manager = DefaultSyncManager.getDefault();
		Config config = new Config(ContactModule.CONTACT);
		manager.request(config, contactList);
	}
	
}
