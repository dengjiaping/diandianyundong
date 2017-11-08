package cn.ingenic.indroidsync;

import cn.ingenic.indroidsync.utils.internal.YesNoPreference;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import com.fox.exercise.R;
import com.fox.exercise.bluetooth.BluetoothServiceNew;
import com.fox.exercise.bluetooth.DeviceListActivity;

public class ClearSettingsDialogPreference extends YesNoPreference {

	private static final String TAG = "ClearSettingsDialogPreference";

	// public ClearSettingsDialogPreference(Context context) {
	// super(context);
	// init();
	// }

	public ClearSettingsDialogPreference(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public ClearSettingsDialogPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		DefaultSyncManager manager = DefaultSyncManager.getDefault();
		String address = manager.getLockedAddress();
		if (BluetoothAdapter.checkBluetoothAddress(address)) {
			BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
			BluetoothDevice device = adapter.getRemoteDevice(address);
			String dialogMessage = String.format(
					getContext().getString(R.string.clear_message),
					device.getName());
			setDialogMessage(dialogMessage);
		}
	}

	// watch out leaks problem, so don't hold handler in thread below for long
	// time.
	@SuppressLint("HandlerLeak")
	@Override
	public void onClick(DialogInterface dialog, int which) {
		super.onClick(dialog, which);
		if (which == DialogInterface.BUTTON_POSITIVE) {
			Log.d(TAG, "clear settings");
			final DefaultSyncManager manager = DefaultSyncManager.getDefault();
			final int CREATE_PROCESS_BAR = 0, CLEAR_PROCESS_BAR = 1;
			final ProgressDialog progressDialog = new ProgressDialog(
					getContext());
			progressDialog.setIndeterminate(true);
			progressDialog.setCancelable(false);
			progressDialog.setMessage(getContext().getString(R.string.waiting));
			final Handler handler = new Handler() {

				@Override
				public void handleMessage(Message msg) {
					switch (msg.what) {
					case CREATE_PROCESS_BAR:
						progressDialog.show();
						break;
					case CLEAR_PROCESS_BAR:
						manager.disconnect();
						setEnabled(false);
						break;
					}
				}

			};
			handler.sendEmptyMessageDelayed(CREATE_PROCESS_BAR, 1000);
			new Thread() {

				@Override
				public void run() {
					manager.setLockedAddress("", true);
					SharedPreferences sps = getContext().getSharedPreferences("sprots_uid", 0);
					int sportUid = sps.getInt("sportsUid", 0);
			        SharedPreferences sharedPreferences =getContext().getSharedPreferences("sports" + sportUid, 0);
					Editor edit=sharedPreferences.edit();
					edit.putString(DeviceListActivity.EXTRA_DEVICE_NAME, "");
					edit.commit();
					handler.removeMessages(CREATE_PROCESS_BAR);
					progressDialog.dismiss();
					handler.sendEmptyMessage(CLEAR_PROCESS_BAR);
				}

			}.start();
		}
	}

}
