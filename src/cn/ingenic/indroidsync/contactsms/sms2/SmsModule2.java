package cn.ingenic.indroidsync.contactsms.sms2;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import cn.ingenic.indroidsync.DefaultSyncManager;
import cn.ingenic.indroidsync.Module;
import cn.ingenic.indroidsync.Transaction;

import cn.ingenic.indroidsync.contactsms.contacts.ContactModule;
import cn.ingenic.indroidsync.contactsms.contacts.provider.OperateDB;
import cn.ingenic.indroidsync.contactsms.manager.ContactAndSms2Columns;


public class SmsModule2 extends Module{
	
	public static final String SMS2 = "SMS2";
	
//    public static int SYNC_MODE=-1;
	
	private Context mContext;

	public SmsModule2() {
		super(SMS2);
	}

	public final String TAG = "M-SMS2";
    private String DEBUG="sms2_debug";
    private boolean debug=true;
	private static boolean serviceSmsHaveStarted=false;

	@Override
	protected Transaction createTransaction() {
		return new Sms2Transaction();
	}

	@Override
	protected void onCreate(Context context) {
		
		Log.d(TAG, "Sms2Module created.");
		
		this.mContext=context;	
		boolean smsEnable=DefaultSyncManager.getDefault().isFeatureEnabled(SMS2);
		boolean haveInit=DefaultSyncManager.getDefault().hasLockedAddress();
        if(debug)Log.d(DEBUG,"in SmsModule2 onCreate smsEnable is :"+smsEnable+" ,and haveInit is :"+haveInit);
		if(smsEnable&&haveInit){
			startService();
		}
	}
	
	private void startService(){
		Intent srartService=new Intent(mContext,SmsSyncService.class);
		mContext.startService(srartService);
	    serviceSmsHaveStarted=true;
	}
	
	private void stopService(){
		Intent stopService=new Intent(mContext,SmsSyncService.class);
		mContext.stopService(stopService);
		serviceSmsHaveStarted=false;
	}

	@Override
	protected void onClear(String address) {
		// TODO Auto-generated method stub
		super.onClear(address);
		clear();
	}
	private void clear(){
		if(DefaultSyncManager.getDefault().isFeatureEnabled(SMS2)&&serviceSmsHaveStarted){
			stopService();
		}
		SmsDB smsDb=new SmsDB();
        smsDb.getSmsSyncProvider(mContext);
		smsDb.getSyncSmsDB().deleteAllSms();
	}

	public static int getMode(){
        //use contact mode
		return ContactModule.getMode();
	}
	
	
	@Override
	protected void onInit() {
		// TODO Auto-generated method stub
		super.onInit();
		if(debug)Log.i(DEBUG,"in SmsModule2 onInit .");
		init(DefaultSyncManager.getDefault().isFeatureEnabled(SMS2));
	}

	private void init(boolean enableSms){
//		boolean enableSms=DefaultSyncManager.getDefault().isFeatureEnabled(SMS2);
		if(debug)Log.d(DEBUG,"in SmsModule2 init feature is :"+enableSms+" , and serviceSmsHaveStarted is :"+serviceSmsHaveStarted);
		if(enableSms&&!serviceSmsHaveStarted){
			startService();
		}else if(!enableSms){
			return;
		}else{
			mContext.sendBroadcast(new Intent(ContactAndSms2Columns.SmsColumn.CATCH_ALL_SMS_ACTION));
		}
		
	}
	
	@Override
	protected void onFeatureStateChange(String feature, boolean enabled) {
		// TODO Auto-generated method stub
		super.onFeatureStateChange(feature, enabled);
		if(debug)Log.d(DEBUG,"in Sms2Module onFeatureStateChange feature is :"+feature+", and enabled is :"+enabled);
		if(enabled){
			init(enabled);
		}else{
			clear();
		}
	}

}
