package cn.ingenic.indroidsync.sms;

import cn.ingenic.indroidsync.services.SyncModule;
import cn.ingenic.indroidsync.services.mid.MidSrcContentProvider;

public class SmsMidProvider extends MidSrcContentProvider {

	@Override
	public SyncModule getSyncModule() {
		// TODO Auto-generated method stub
		return SmsModule.getInstance(
				getContext().getApplicationContext());
	}

}
