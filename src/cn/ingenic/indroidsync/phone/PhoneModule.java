package cn.ingenic.indroidsync.phone;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import cn.ingenic.indroidsync.DefaultSyncManager;
import cn.ingenic.indroidsync.LogTag;
import cn.ingenic.indroidsync.Module;
import cn.ingenic.indroidsync.Transaction;


public class PhoneModule extends Module {

	public static final String TAG = "M-PHONE";
	public static final boolean V = true;

	static final String PHONE = "PHONE";

	public PhoneModule() {
		super(PHONE);
	}

	@Override
	protected Transaction createTransaction() {
		return new PhoneTransaction();
	}

	@Override
    protected void onCreate(Context context) {
        if (V) {
            Log.d(TAG, "PhoneMudule created.");
        }
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService("phone");
        ListenPhone lp = ListenPhone.getInstance(context);
        tm.listen(lp, PhoneStateListener.LISTEN_CALL_STATE);
        Log.i(TAG, "Phone[Server] listen phone state");
    }

}
