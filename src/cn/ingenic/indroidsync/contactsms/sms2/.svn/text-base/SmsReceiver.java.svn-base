package cn.ingenic.indroidsync.contactsms.sms2;

import java.util.ArrayList;
import java.util.EnumSet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import cn.ingenic.indroidsync.Config;
import cn.ingenic.indroidsync.DefaultSyncManager;

import cn.ingenic.indroidsync.contactsms.manager.ContactAndSms2Columns;
import cn.ingenic.indroidsync.data.DefaultProjo;
import cn.ingenic.indroidsync.data.Projo;
import cn.ingenic.indroidsync.data.ProjoType;

public class SmsReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            DefaultSyncManager manager = DefaultSyncManager.getDefault();
            
//            SmsMessage[] msgs = Intents.getMessagesFromIntent(intent);
//
//            SmsMessage sms = msgs[0];
//            String address = sms.getOriginatingAddress();
//            String body = sms.getMessageBody();
            
            Projo projo = new DefaultProjo(EnumSet.allOf(SmsColumn2.class), ProjoType.DATA);
//            projo.put(SmsColumn2.address, address);
//            projo.put(SmsColumn2.body, body);
            projo.put(SmsColumn2.tag,ContactAndSms2Columns.SmsColumn.SEND_NEW_SMS);
            
            Config config = new Config(SmsModule2.SMS2);
            ArrayList<Projo> datas = new ArrayList<Projo>(1);
            datas.add(projo);
            manager.request(config, datas);
        }
	}
	

}
