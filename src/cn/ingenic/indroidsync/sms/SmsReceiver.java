package cn.ingenic.indroidsync.sms;

import cn.ingenic.indroidsync.services.SyncData;
import cn.ingenic.indroidsync.services.SyncException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals
				("android.provider.Telephony.SMS_RECEIVED")) {
			SmsMessage[] msgs = getMessagesFromIntent(intent);
			SmsMessage sms = msgs[0];
			String address = sms.getOriginatingAddress();
			String body = sms.getMessageBody();
			send(context,address,body);
		}
		
	}
	
	private void send(Context context,String address,String body){
		SyncData data=new SyncData();
		data.putString("address", address);
		data.putString("body", body);
		data.putString("command", "new_sms");
		try {
			SmsModule.getInstance(context).send(data);
		} catch (SyncException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static SmsMessage[] getMessagesFromIntent(
            Intent intent) {
        Object[] messages = (Object[]) intent.getSerializableExtra("pdus");
        String format = intent.getStringExtra("format");
        byte[][] pduObjs = new byte[messages.length][];

        for (int i = 0; i < messages.length; i++) {
            pduObjs[i] = (byte[]) messages[i];
        }
        byte[][] pdus = new byte[pduObjs.length][];
        int pduCount = pdus.length;
        SmsMessage[] msgs = new SmsMessage[pduCount];
        for (int i = 0; i < pduCount; i++) {
            pdus[i] = pduObjs[i];
            msgs[i] = SmsMessage.createFromPdu(pdus[i]);
        }
        return msgs;
    }

}
