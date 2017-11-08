package cn.ingenic.indroidsync.contactsms.sms2;

import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.TelephonyManager;

public class Attribute {

	public static final Uri MMSSMS_CONTENT_URI = Uri
			.parse("content://mms-sms/");
	public static final Uri SMS_CONTENT_URI = Uri.parse("content://sms");
	public static final Uri THREAD_CONTENT_URI = Uri.withAppendedPath(
			MMSSMS_CONTENT_URI, "conversations");
	private static final Uri THREAD_ID_CONTENT_URI = Uri
			.parse("content://mms-sms/threadID");

	public Context mContext;

	public Attribute(Context context) {
		this.mContext = context;
	}

	public TelephonyManager getTelephonyManager() {
		return (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);
	}

	public class CanonicalAddressesColumns {
		public static final String _ID = "_id";
		public static final String ADDRESS = "address";
	}

	public static class Threads {
		public static final String _ID = "_id";
		public static final String DATE = "date";
		public static final String RECIPIENT_IDS = "recipient_ids";
		public static final String SNIPPET = "snippet";

		public static long getOrCreateThreadId(Context context, String recipient) {
			Set<String> recipients = new HashSet<String>();
			recipients.add(recipient);
			Uri.Builder uriBuilder = THREAD_ID_CONTENT_URI.buildUpon();

			for (String rec : recipients) {

				uriBuilder.appendQueryParameter("recipient", rec);
			}

			Uri uri = uriBuilder.build();
			// if (DEBUG) Log.v(TAG, "getOrCreateThreadId uri: " + uri);
			Cursor cursor = context.getContentResolver().query(uri,
					new String[] { "_id" }, null, null, null);
			long id = 0;
			if (cursor.getCount() != 0) {
                cursor.moveToFirst();
                while(cursor.moveToNext()){
                    id = cursor.getLong(0);
                }
			}
			cursor.close();
			return id;
		}
	}

	public class SmsMessage {
		public static final String FORMAT_3GPP = "3gpp";
		public static final String FORMAT_3GPP2 = "3gpp2";
	}

	public class Sms {
		public static final String _ID = "_id";
		public static final String TYPE = "type";

		public static final String THREAD_ID = "thread_id";

		public static final String ADDRESS = "address";

		public static final String PERSON_ID = "person";

		public static final String DATE = "date";

		public static final String DATE_SENT = "date_sent";

		public static final String READ = "read";

		public static final String SEEN = "seen";

		public static final String STATUS = "status";

		public static final String SUBJECT = "subject";

		public static final String BODY = "body";

		public static final String PERSON = "person";

		public static final String PROTOCOL = "protocol";

		public static final String REPLY_PATH_PRESENT = "reply_path_present";

		public static final String SERVICE_CENTER = "service_center";

		public static final String LOCKED = "locked";

		public static final String ERROR_CODE = "error_code";

		public static final String META_DATA = "meta_data";
	}

}
