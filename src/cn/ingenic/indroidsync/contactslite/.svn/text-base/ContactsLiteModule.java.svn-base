package cn.ingenic.indroidsync.contactslite;

import android.content.Context;
import cn.ingenic.indroidsync.services.SyncModule;
import cn.ingenic.indroidsync.services.mid.MidTableManager;


public class ContactsLiteModule extends SyncModule {
	private static final String MODULE_NAME = "ContactsLiteModule";
	private static final String TAG = MODULE_NAME;
	private static SyncModule sInstance = null;
	private Context mContext;
	
	private ContactsLiteModule(Context context) {
		super(MODULE_NAME, context);
		mContext = context;
	}
	
	public synchronized static SyncModule getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new ContactsLiteModule(context);
		}
		return sInstance;
	}
	
	@Override
	protected void onCreate() {
		
	}
	
	@Override
	public MidTableManager getMidTableManager() {
		return ContactsLiteMidSrcManager.getInstance(getcontext(), this);
	}

	private Context getcontext() {
		return mContext;
	}

}
