package cn.ingenic.indroidsync.contactsms.sms2;

import android.R.array;
import cn.ingenic.indroidsync.Column;

public enum SmsColumn2 implements Column{
	phone_id(Integer.class),pdu(byte[][].class),tag(String.class)
	,body(String.class),date(String.class),threadid(String.class),subject(String.class)
	,address(String.class),phoneid(String.class),read(Integer.class),errorcode(Integer.class)
	,status(String.class),phone_mac_address(String.class),watch_id(Integer.class),type(Integer.class)
	,service_center(String.class),data_send(String.class),protocol(String.class),send(String.class)
	,reply_path_present(String.class),threaddata(Long.class),delete(Integer.class),phone_address(String.class);
	
		
		private final Class<?> mClass;
		SmsColumn2(Class<?> c) {
			mClass = c;
		}
		
		@Override
		public Class<?> type() {
			return mClass;
		}

		@Override
		public String key() {
			return name();
		}
		
		
		
		
		
		
		
		
		
		
		
}
