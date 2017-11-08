package cn.ingenic.indroidsync.contactsms.manager;

import java.util.ArrayList;
import java.util.EnumSet;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import cn.ingenic.indroidsync.Config;
import cn.ingenic.indroidsync.DefaultSyncManager;
import cn.ingenic.indroidsync.Transaction;

import cn.ingenic.indroidsync.data.DefaultProjo;
import cn.ingenic.indroidsync.data.Projo;
import cn.ingenic.indroidsync.data.ProjoType;


public class ManagerTransaction extends Transaction{

	
	@Override
	public void onStart(final ArrayList<Projo> datas) {
		// TODO Auto-generated method stub
		super.onStart(datas);
		if(datas==null||datas.size()==0){
			return;
		}
		
		String tag=datas.get(0).get(ManagerColumns.tag).toString();
		if(tag.equals(ContactAndSms2Columns.MANAGER_SEND_ADDRESS)){
			//phone compare address
			String saveAddress=(String) datas.get(0).get(ManagerColumns.address);
			int contactSize=(Integer)datas.get(0).get(ManagerColumns.contactsize);
			int smsSize=(Integer)datas.get(0).get(ManagerColumns.smssize);
			if(saveAddress!=null&&saveAddress.equals(getPhoneAddress())){
				Intent intent=new Intent(ContactAndSms2Columns.SAME_PHONE_ACTION);
				intent.putExtra("contact_size", contactSize);
				intent.putExtra("sms_size", smsSize);
				mContext.sendBroadcast(intent);
			}else{
				mContext.sendBroadcast(new Intent(ContactAndSms2Columns.DIFF_PHONE_ACTION));
				sendManagerList(getPhoneAddress());
			}
		}else if(tag.equals(ContactAndSms2Columns.DIFF_PHONE_SEND_ADDRESS)){
			String phoneAddress=(String)datas.get(0).get(ManagerColumns.address);
			setLocalPhoneAddress(phoneAddress,mContext);
		}else if(tag.equals(ContactAndSms2Columns.INIT_MESSAGE)){
			Log.e("yangliu","in phone receiver init message .........");
			mContext.sendBroadcast(new Intent(ContactAndSms2Columns.SmsColumn.CATCH_ALL_SMS_ACTION));
			mContext.sendBroadcast(new Intent(ContactAndSms2Columns.ContactColumn.CATCH_ALL_COTNACTS_DATAS_ACTION));
		}
	}
	
	private String getPhoneAddress(){
		BluetoothAdapter ba=BluetoothAdapter.getDefaultAdapter();
        return ba.getAddress();
	}
	
	private void sendManagerList(String phoneAddress){
		ArrayList<Projo> managerList=new ArrayList<Projo>();
		Projo mProjo = new DefaultProjo(EnumSet.allOf(ManagerColumns.class), ProjoType.DATA);
		mProjo.put(ManagerColumns.tag,ContactAndSms2Columns.DIFF_PHONE_SEND_ADDRESS);
		mProjo.put(ManagerColumns.address,phoneAddress);
		managerList.add(mProjo);
		DefaultSyncManager manager = DefaultSyncManager.getDefault();
		Config config = new Config(ManagerModule.MANAGER);
		manager.request(config, managerList);
	}
	
	private final static String PHONE_ADDRESS="phone_address";
    private final static String ADDRESS="address";
    
    public static void setLocalPhoneAddress(String address,Context context){
    	SharedPreferences.Editor editor=context.getSharedPreferences(PHONE_ADDRESS, Context.MODE_PRIVATE).edit();
    	
		editor.putString(ADDRESS, address);
		editor.apply();
    }
}
