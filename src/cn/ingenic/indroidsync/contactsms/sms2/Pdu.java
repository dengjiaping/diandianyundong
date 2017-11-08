package cn.ingenic.indroidsync.contactsms.sms2;

import java.util.ArrayList;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
//import android.database.sqlite.SqliteWrapper;
import android.net.Uri;
//import android.provider.Telephony.Sms;
import android.telephony.SmsManager;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.util.Log;

//import com.android.internal.telephony.IccUtils;
//import com.android.internal.telephony.SmsHeader;
//import com.android.internal.telephony.SmsMessageBase.TextEncodingDetails;
//import com.android.internal.telephony.gsm.SmsMessage;
//
//import com.google.android.mms.MmsException;

public class Pdu {
	
	private Context mContext;
	private Attribute mAttribute;
	
	public static final String RECEIVE_SMS_PERMISSION = "android.permission.RECEIVE_SMS";
	
	public Pdu(Context context){
		this.mContext=context;
		mAttribute=new Attribute(mContext);
	}
	   
//	   // This must match the column IDs below.
//	      private static final String[] SEND_PROJECTION = new String[] {
//	          Sms.THREAD_ID,  //0
//	          Sms.ADDRESS,    //1
//	          Sms.BODY,       //2
//	          Sms.STATUS,     //3
//	      };
//	      
//	   // This must match SEND_PROJECTION.
//	      private static final int SEND_COLUMN_THREAD_ID  = 0;
//	      private static final int SEND_COLUMN_ADDRESS    = 1;
//	      private static final int SEND_COLUMN_BODY       = 2;
//	      private static final int SEND_COLUMN_STATUS     = 3;
//	      
//	      public byte[][] getPdu(String id){
////	    	  final Uri uri = Uri.parse("content://sms/queued");
//	    	  final Uri uri=Sms.CONTENT_URI;
//	          ContentResolver resolver = mContext.getContentResolver();
//	          Cursor c = SqliteWrapper.query(mContext, resolver, uri,
//	                          SEND_PROJECTION, Sms._ID+"=?", new String[]{id}, "date ASC");
////	          Log.e("yangliu","in getPdu 1111111 c size is "+c.getCount()+", and id is "+id);
//	          if(c.getCount()!=0){
//	        	  c.moveToFirst();
//	        	  String msgText = c.getString(SEND_COLUMN_BODY);
//	              String address = c.getString(SEND_COLUMN_ADDRESS);
//	              int threadId = c.getInt(SEND_COLUMN_THREAD_ID);
//	              int status = c.getInt(SEND_COLUMN_STATUS);
//	              int msgId=Integer.parseInt(id);
//	              Uri msgUri = ContentUris.withAppendedId(Sms.CONTENT_URI, msgId);
//	              Log.e("yangliu","in getPdu msgText is "+msgText+", and address is "+address+",and threadId is "
//	            		  +threadId+", and status is "+status+", and msgId is "+msgId+", and msgUri is "+msgUri);
//	              c.close();
//	             return getPdu(mContext,address, msgText, threadId, status == Sms.STATUS_PENDING,
//	                      msgUri);
//	          }
//	          c.close();
//	          return null;
//	          
//	          
//	      }
//	      
//	      private byte[][] getPdu(Context context, String dest, String msgText, long threadId,
//	              boolean requestDeliveryReport, Uri uri){
//	    
//	    	  SmsManager smsManager = SmsManager.getDefault();
//	    	  ArrayList<String> messages = null;
//	    	 
//	          messages = smsManager.divideMessage(msgText);
//	              // remove spaces and dashes from destination number
//	              // (e.g. "801 555 1212" -> "8015551212")
//	              // (e.g. "+8211-123-4567" -> "+82111234567")
//	           dest = PhoneNumberUtils.stripSeparators(dest);
//	      
//	    	  int messageCount = messages.size();
////	          Log.e("yangliu","messageCount is "+messageCount);
//	          if (messageCount == 0) {
//	              // Don't try to send an empty message.
//	              try {
//					throw new MmsException("SmsMessageSender.sendMessage: divideMessage returned " +
//					          "empty messages. Original message is \"" + msgText + "\"");
//				} catch (MmsException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//	          }
//	         return getPdu(dest,getOutgoingServiceCenter(threadId),messages);
//	    	  
//	      }
//	      
//	      private static final String[] SERVICE_CENTER_PROJECTION = new String[] {
//	          Sms.Conversations.REPLY_PATH_PRESENT,
//	          Sms.Conversations.SERVICE_CENTER,
//	      };
//
//	      private static final int COLUMN_REPLY_PATH_PRESENT = 0;
//	      private static final int COLUMN_SERVICE_CENTER     = 1;
//	      
//	      private String getOutgoingServiceCenter(long threadId) {
//	          Cursor cursor = null;
//
//	          try {
//	              cursor = SqliteWrapper.query(mContext, mContext.getContentResolver(),
//	                              Sms.CONTENT_URI, SERVICE_CENTER_PROJECTION,
//	                              "thread_id = " + threadId, null, "date DESC");
//
//	              if ((cursor == null) || !cursor.moveToFirst()) {
//	                  return null;
//	              }
//
//	              boolean replyPathPresent = (1 == cursor.getInt(COLUMN_REPLY_PATH_PRESENT));
//	              return replyPathPresent ? cursor.getString(COLUMN_SERVICE_CENTER) : null;
//	          } finally {
//	              if (cursor != null) {
//	                  cursor.close();
//	              }
//	          }
//	      }
//	      
//	      private byte[][] getPdu(String destinationAddress, String scAddress, ArrayList<String> parts){
//
////	          int refNumber = getNextConcatenatedRef() & 0x00FF;
//	          int msgCount = parts.size();
//	          int encoding = android.telephony.SmsMessage.ENCODING_UNKNOWN;
//
////	          int mRemainingMessages = msgCount;
////	          Log.e("yangliu","destinationAddress is "+destinationAddress+", and scAddress is "+scAddress+", and part size is "+parts.size());
//	          TextEncodingDetails[] encodingForParts = new TextEncodingDetails[msgCount];
//	          for (int i = 0; i < msgCount; i++) {
//	        	  
//	              TextEncodingDetails details = SmsMessage.calculateLength(parts.get(i), false);;
//	              if (encoding != details.codeUnitSize
//	                      && (encoding == android.telephony.SmsMessage.ENCODING_UNKNOWN
//	                              || encoding == android.telephony.SmsMessage.ENCODING_7BIT)) {
//	                  encoding = details.codeUnitSize;
//	              }
//	              encodingForParts[i] = details;
//	          }
//	          
//	          byte[][] pduArray=new byte[msgCount][];
//	          Log.e("yangliu","-------------one sms------------------");
//	          for (int i = 0; i < msgCount; i++) {
//	              SmsHeader.ConcatRef concatRef = new SmsHeader.ConcatRef();
////	              concatRef.refNumber = refNumber;
//	              concatRef.seqNumber = i + 1;  // 1-based sequence
//	              concatRef.msgCount = msgCount;
//	              // TODO: We currently set this to true since our messaging app will never
//	              // send more than 255 parts (it converts the message to MMS well before that).
//	              // However, we should support 3rd party messaging apps that might need 16-bit
//	              // references
//	              // Note:  It's not sufficient to just flip this bit to true; it will have
//	              // ripple effects (several calculations assume 8-bit ref).
//	              concatRef.isEightBits = true;
//	              SmsHeader smsHeader = new SmsHeader();
//	              smsHeader.concatRef = concatRef;
//
//	              // Set the national language tables for 3GPP 7-bit encoding, if enabled.
//	              if (encoding == android.telephony.SmsMessage.ENCODING_7BIT) {
//	                  smsHeader.languageTable = encodingForParts[i].languageTable;
//	                  smsHeader.languageShiftTable = encodingForParts[i].languageShiftTable;
//	              }
//
//	              SmsMessage.SubmitPdu pdu = SmsMessage.getSubmitPdu(scAddress, destinationAddress,
//	                      parts.get(i), false, /*SmsHeader.toByteArray(smsHeader)*/null,
//	                      encoding, smsHeader.languageTable, smsHeader.languageShiftTable);
//	              if(pdu!=null)
//	              Log.e("yangliu","one pdu is :"+pdu.toString());
//	              if(pdu!=null&&!pdu.equals(""))
//	              pduArray[i]=pdu.encodedMessage;
//	          }
//	          return pduArray;
//
//	          
//	      }
	     
	      private final String RECEIVER_SMS_ACTION="indroid.provider.Telephony.SMS_RECEIVER";
	      
	      public void sendBroadCase(int phoneId,byte[][] pdus,int error,int read,long data,
	    		  String address,String body,int type,int threadId,String subject
				  ,int status,String phoneMacAddress,String serviceCenter,
				  long dataSend,String protocol,int send,String replyPathPresent){
//	    	  Log.e("yangliu","in sendBroadCase...................pdus is "+pdus.toString());
//	    	  for(int i=0;i<pdus.length;i++){
////	    		 SmsMessage sm= SmsMessage.createFromPdu(pdus[i]);
//	    		  Log.i("yangliu","pdu is =============="+IccUtils.bytesToHexString(pdus[i]));
//	    	  }
	    	  Intent intent = new Intent(RECEIVER_SMS_ACTION);
	    	  
	    	  intent.putExtra("phone_id", phoneId);
	    	  intent.putExtra("pdus", pdus);
	    	  intent.putExtra("errorCode", error);
	    	  intent.putExtra("read", read);
	    	  intent.putExtra("data", data);
	    	  intent.putExtra("address", address);
	    	  intent.putExtra("body", body);
	    	  intent.putExtra("type", type);
	    	  intent.putExtra("threadId", threadId);
	    	  intent.putExtra("subject", subject);
	    	  intent.putExtra("status", status);
	    	  intent.putExtra("phone_mac_address", phoneMacAddress);
	    	  intent.putExtra("service_center", serviceCenter);
	    	  intent.putExtra("datasend", dataSend);
	    	  intent.putExtra("protocol", protocol);
	    	  intent.putExtra("send", send);
	    	  intent.putExtra("reply_path_present", replyPathPresent);
	    	  TelephonyManager tm=mAttribute.getTelephonyManager();
	    	  int phoneType=tm.getPhoneType();
	    	  String format=null;
	    	  if(phoneType==TelephonyManager.PHONE_TYPE_GSM){
	    		  format=Attribute.SmsMessage.FORMAT_3GPP;
	    	  }else if(phoneType==TelephonyManager.PHONE_TYPE_CDMA){
	    		  format=Attribute.SmsMessage.FORMAT_3GPP2;
	    	  }
	          intent.putExtra("format", format);
	          mContext.sendBroadcast(intent);
	      }
	      
//	    //yangliu
//	      public Uri myStoreMessage(Map<String,Object> map){
//	      	
//	      	// Store the message in the content provider.
//	      	ContentValues values = myExtractContentValues(map);
//	      	values.put(Sms.ERROR_CODE, Integer.parseInt(map.get("errorCode").toString()));
//	      	
//	      	values.put(Inbox.BODY, replaceFormFeeds(map.get("body").toString()));
//	      	
//	      	 // Make sure we've got a thread id so after the insert we'll be able to delete
//	          // excess messages.
//	          Long threadId = values.getAsLong(Sms.THREAD_ID);
//	          String address = values.getAsString(Sms.ADDRESS);
//
//	          // Code for debugging and easy injection of short codes, non email addresses, etc.
//	          // See Contact.isAlphaNumber() for further comments and results.
////	          switch (count++ % 8) {
////	              case 0: address = "AB12"; break;
////	              case 1: address = "12"; break;
////	              case 2: address = "Jello123"; break;
////	              case 3: address = "T-Mobile"; break;
////	              case 4: address = "Mobile1"; break;
////	              case 5: address = "Dogs77"; break;
////	              case 6: address = "****1"; break;
////	              case 7: address = "#4#5#6#"; break;
////	          }
//
////	          if (!TextUtils.isEmpty(address)) {
////	              Contact cacheContact = Contact.get(address,true);
////	              if (cacheContact != null) {
////	                  address = cacheContact.getNumber();
////	              }
////	          } else {
////	              address = mContext.getString(R.string.unknown_sender);
//	              values.put(Sms.ADDRESS, address);
////	          }
//
//	          if (((threadId == null) || (threadId == 0)) && (address != null)) {
//	              threadId = Conversation.getOrCreateThreadId(mContext, address);
//	              values.put(Sms.THREAD_ID, threadId);
//	          }
//
//	          ContentResolver resolver = mContext.getContentResolver();
//
//	          Uri insertedUri = SqliteWrapper.insert(mContext, resolver, Inbox.CONTENT_URI, values);
//
//	          // Now make sure we're not over the limit in stored messages
//	          Recycler.getSmsRecycler().deleteOldMessagesByThreadId(mContext, threadId);
//	          MmsWidgetProvider.notifyDatasetChanged(mContext);
//
//	          return insertedUri;
//	      	
//	      }
//	      
//	      private ContentValues myExtractContentValues(Map<String,Object> map){
//	      	// Store the message in the content provider.
//	          ContentValues values = new ContentValues();
//	          int type=Integer.parseInt(map.get("type").toString());
//	          values.put(Inbox.TYPE, type);
//	          values.put(Inbox.ADDRESS, map.get("address").toString());
//	          values.put(Inbox.DATE, Long.valueOf(map.get("data").toString()));
//	          values.put(Inbox.DATE_SENT, Long.valueOf(map.get("datasend").toString()));
//	          values.put(Inbox.PROTOCOL, Integer.parseInt(map.get("protocol").toString()));
//	          values.put(Inbox.READ, Integer.parseInt(map.get("read").toString()));
//	          values.put(Inbox.SEEN, Integer.parseInt(map.get("send").toString()));
//	          if(map.get("subject").toString().length()>0){
//	          	values.put(Inbox.SUBJECT,map.get("subject").toString());
//	          }
//	          values.put(Inbox.REPLY_PATH_PRESENT, Integer.parseInt(map.get("reply_path_present").toString()));
//	          values.put(Inbox.SERVICE_CENTER, Integer.parseInt(map.get("service_center").toString()));
//	          return values;
//	      }
//	      
//	      public static String replaceFormFeeds(String s) {
//	          // Some providers send formfeeds in their messages. Convert those formfeeds to newlines.
//	          return s.replace('\f', '\n');
//	      }

}
