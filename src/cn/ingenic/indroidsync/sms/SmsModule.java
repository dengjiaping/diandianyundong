package cn.ingenic.indroidsync.sms;

import android.content.Context;
import cn.ingenic.indroidsync.services.SyncModule;
import cn.ingenic.indroidsync.services.mid.MidTableManager;

public class SmsModule extends SyncModule {
	
	private static SmsModule mInstance=null;
	private static String SMS_NAME="sms_module";
	private Context mContext;
	private static SmsMidManager mSmsMidManager;
	
	public static SmsModule getInstance(Context context){
		if(mInstance==null){
			mInstance=new SmsModule(SMS_NAME,context);
			mSmsMidManager=new SmsMidManager(context,mInstance);
		}
		return mInstance;
	}
	

	public SmsModule(String name, Context context) {
		super(name, context);
		
		mContext=context;
	}

	@Override
	protected void onCreate() {
	
	}

	@Override
	public MidTableManager getMidTableManager() {
		
		return mSmsMidManager;
	}
	

}
