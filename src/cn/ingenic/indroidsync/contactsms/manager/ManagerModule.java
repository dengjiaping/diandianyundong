package cn.ingenic.indroidsync.contactsms.manager;

import java.util.ArrayList;
import java.util.EnumSet;


import cn.ingenic.indroidsync.DefaultSyncManager;
import cn.ingenic.indroidsync.Module;

import cn.ingenic.indroidsync.Config;
import cn.ingenic.indroidsync.data.DefaultProjo;
import cn.ingenic.indroidsync.data.Projo;
import cn.ingenic.indroidsync.data.ProjoType;
import cn.ingenic.indroidsync.Transaction;
import android.content.Context;
import android.content.Intent;
import android.util.Log;




public class ManagerModule extends Module{

public static final String MANAGER = "MANAGER";
	
	
	private Context mContext;

	public ManagerModule() {
		super(MANAGER);
	}

	public final String TAG = "M-MANAGER";
	
	private static String myTag="In ManagerModule+++";
	
	@Override
	protected Transaction createTransaction() {
		return new ManagerTransaction();
	}
	
	@Override
	protected void onCreate(Context context) {
		
		Log.d(TAG, "ManagerModule created.");
		
		this.mContext=context;
		
	}

	@Override
	protected void onClear(String address) {
		// TODO Auto-generated method stub
		super.onClear(address);
	}

	@Override
	protected void onConnectivityStateChange(boolean connected) {
		// TODO Auto-generated method stub
		super.onConnectivityStateChange(connected);
		Log.e("yangliu",myTag+"onConnectivityStateChange connected is "+connected);
		if(connected){
			Intent intent=new Intent(ContactAndSms2Columns.SYNC_CONNECTIVITY_TRUE_ACTION);
			
			intent.putExtra("mode", getMode());
			this.mContext.sendBroadcast(intent);

		}else{
			this.mContext.sendBroadcast(new Intent(ContactAndSms2Columns.SYNC_CONNECTIVITY_FALSE_ACTION));
		}
		
	}
	
	private final static String PHONE_ADDRESS="phone_address";
	private final static String ADDRESS="address";
	      
	public static String getLocalPhoneAddress(Context context){  
	    return context.getSharedPreferences(PHONE_ADDRESS, Context.MODE_PRIVATE).getString(ADDRESS,"");
	}

	@Override
	protected void onModeChanged(int mode) {
		// TODO Auto-generated method stub
		super.onModeChanged(mode);
	}
	public static int getMode(){
		Log.e("yangliu",myTag+"getMode mode is :"+DefaultSyncManager.getDefault().getCurrentMode());
		return DefaultSyncManager.getDefault().getCurrentMode();
	}

	@Override
	protected void onFeatureStateChange(String feature, boolean enabled) {
		// TODO Auto-generated method stub
		super.onFeatureStateChange(feature, enabled);
		
	}
}
