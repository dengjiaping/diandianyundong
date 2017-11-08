package cn.ingenic.indroidsync.contactsms.contacts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import cn.ingenic.indroidsync.DefaultSyncManager;
import cn.ingenic.indroidsync.Module;
import cn.ingenic.indroidsync.RemoteBinderImpl;
import cn.ingenic.indroidsync.Transaction;
import cn.ingenic.indroidsync.contactsms.contacts.phone.ContactSyncService;
import cn.ingenic.indroidsync.contactsms.contacts.provider.OperateDB;
import cn.ingenic.indroidsync.contactsms.manager.ContactAndSms2Columns;



public class ContactModule extends Module{
	
	public static final String CONTACT = "CONTACT";
	
	
	private Context mContext;

	public ContactModule() {
		super(CONTACT);
	}

	public final String TAG = "M-CONTACT";
    private String DEBUG="contact_debug";
    private boolean debug =true;
	
	
	private static boolean serviceHaveStarted=false;
	
	

	@Override
	protected Transaction createTransaction() {
		return new ContactTransaction();
	}

	@Override
	protected void onCreate(Context context) {
		
		this.mContext=context;
		boolean contactEnable=DefaultSyncManager.getDefault().isFeatureEnabled(CONTACT);
		boolean haveInit=DefaultSyncManager.getDefault().hasLockedAddress();
        if(debug)Log.d(DEBUG,"in ContactModule onCreate haveInit is :"+haveInit+", and contactEnable is :"+contactEnable);
		if(contactEnable&&haveInit){
		    startService();
		}
	}

	@Override
	protected void onClear(String address) {
		// TODO Auto-generated method stub
		super.onClear(address);
		clear();
	}
	
	private void clear(){
		if(DefaultSyncManager.getDefault().isFeatureEnabled(CONTACT)&&serviceHaveStarted){
			stopService();
			OperateDB db=new OperateDB();
			db.getSyncDB().deleteAll();
		}
	}
	

	@Override
	protected void onInit() {
		// TODO Auto-generated method stub
		super.onInit();
		if(debug)Log.i(DEBUG,"in ContactModule onInit .");
		init(DefaultSyncManager.getDefault().isFeatureEnabled(CONTACT));
	}

	@Override
	protected void onConnectivityStateChange(boolean connected) {
		// TODO Auto-generated method stub
		super.onConnectivityStateChange(connected);
	}

	@Override
	protected void onModeChanged(int mode) {
		// TODO Auto-generated method stub
		super.onModeChanged(mode);
	}
	
	private void startService(){
		Intent srartService=new Intent(mContext,ContactSyncService.class);
	    mContext.startService(srartService);
	    serviceHaveStarted=true;
	}
	
	private void stopService(){
		Intent stopService=new Intent(mContext,ContactSyncService.class);
		mContext.stopService(stopService);
		serviceHaveStarted=false;
	}
	
	private void init(boolean enableContact){
		if(enableContact&&!serviceHaveStarted){
			startService();
		}else if(!enableContact){
			return;
		}else{
			mContext.sendBroadcast(new Intent(ContactAndSms2Columns.ContactColumn.CATCH_ALL_COTNACTS_DATAS_ACTION));
		}
	}
	
	public static int getMode(){
		return DefaultSyncManager.getDefault().getCurrentMode();
	}

	@Override
	protected void onFeatureStateChange(String feature, boolean enabled) {
		// TODO Auto-generated method stub
		super.onFeatureStateChange(feature, enabled);
		if(debug)Log.e(DEBUG,"in ContactModule onFeatureStateChange feature is :"+feature+", and enabled is :"+enabled);
		if(enabled){
			init(enabled);
		}else{
			clear();
		}
	}
	
	

}
