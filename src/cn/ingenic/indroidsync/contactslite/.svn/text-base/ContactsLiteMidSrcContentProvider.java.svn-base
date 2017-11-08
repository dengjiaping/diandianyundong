package cn.ingenic.indroidsync.contactslite;

import cn.ingenic.indroidsync.services.SyncModule;
import cn.ingenic.indroidsync.services.mid.MidSrcContentProvider;


public class ContactsLiteMidSrcContentProvider extends MidSrcContentProvider {

	@Override
	public SyncModule getSyncModule() {
		return ContactsLiteModule.getInstance(
				getContext().getApplicationContext());
	}

}
