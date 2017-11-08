package cn.ingenic.indroidsync;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.text.TextUtils;

import com.fox.exercise.R;
import com.fox.exercise.SmartBarUtils;
import com.fox.exercise.SreachDevice;
import com.fox.exercise.bluetooth.DeviceListActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import cn.ingenic.indroidsync.data.ModeSendCmd;
import cn.ingenic.indroidsync.data.Projo;
import cn.ingenic.indroidsync.phone.QuickSmsSettings;

/**
 * A Preference , settings about the sync application
 * 
 * @author dfdun
 */
public class MSettings extends PreferenceActivity implements
		OnPreferenceChangeListener, OnPreferenceClickListener {

	public static final String KEY_CAMERA_WINDOW = "camera_window_settings";
	private ListPreference mMode;
	private CheckBoxPreference cameraPreviewSetting;
	private CheckBoxPreference mTimeoutNotificationSettings;
	private ClearSettingsDialogPreference mClearSettings;
	private Handler mHandler;
	private PreferenceScreen mSync;
	private PreferenceScreen mMms;
	private PreferenceScreen mBT;
	private PreferenceScreen mSearch;
	private static final int NOTIFY_MODE_CHANGED_MSG = 1;
	private static final int NOTIF_TIMEOUT_NOTI_CHANGED_MSG = 2;
	private static final String KEY_SYNC = "sync_settings";
	private static final String KEY_MMS = "mms_settings";
	private static final String KEY_BT = "bind_bluetooth";
	private static final CharSequence KEY_SEARCH = "search_device";
	private SportsApp mSportsApp;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		boolean findMethod = SmartBarUtils.findActionBarTabsShowAtBottom();
		addPreferencesFromResource(R.xml.setting);
		mMode = (ListPreference) findPreference(DefaultSyncManager.MODE_SETTINGS_KEY);
		mSync = (PreferenceScreen) findPreference(KEY_SYNC);
		mSync.setOnPreferenceClickListener(this);
		mMms = (PreferenceScreen) findPreference(KEY_MMS);
		mMms.setOnPreferenceClickListener(this);
		mMode.setOnPreferenceChangeListener(this);
		mBT = (PreferenceScreen) findPreference(KEY_BT);
		mBT.setOnPreferenceClickListener(this);
		mSearch = (PreferenceScreen) findPreference(KEY_SEARCH);
		mSearch.setOnPreferenceClickListener(this);
		getPreferenceScreen().removePreference(mMode);// hide Mode, now
		mTimeoutNotificationSettings = (CheckBoxPreference) findPreference(DefaultSyncManager.TIMEOUT_NOTIFICATION_KEY);
		mTimeoutNotificationSettings.setOnPreferenceChangeListener(this);
		cameraPreviewSetting = (CheckBoxPreference) findPreference(KEY_CAMERA_WINDOW);
		cameraPreviewSetting.setOnPreferenceChangeListener(this);
		mBT.setOnPreferenceChangeListener(this);
		// android.util.Log.i("dfdun",
		// "Settings] mode values = "+mMode.getValue());
		mClearSettings = (ClearSettingsDialogPreference) findPreference(DefaultSyncManager.CLEAR_SETTINGS_KEY);

		updateBTSum();
		 if(findMethod){
		        getActionBar().setDisplayShowHomeEnabled(false); 
		        getActionBar().setDisplayShowTitleEnabled(false); 
		  	SmartBarUtils.setActionModeHeaderHidden(getActionBar(), true);
			SmartBarUtils.setActionBarViewCollapsable(getActionBar(), 	true);
		}
		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case NOTIFY_MODE_CHANGED_MSG:
					int mode = msg.arg1;
					DefaultSyncManager manager = DefaultSyncManager
							.getDefault();
					snedModeCmd(mode);
					manager.notifyModeChanged(mode);
					break;
				case NOTIF_TIMEOUT_NOTI_CHANGED_MSG:
					boolean enable = msg.arg1 == 1 ? true : false;
					DefaultSyncManager.getDefault()
							.notifyTimeoutNotificationChange(enable);
					break;
				}
			}

		};
	}

	// /**
	// * return current sync mode .<br>
	// * Settings.getMode().equals(Settings.AUTO_MODE)
	// * to judge whether it's auto_mode
	// */
	// public static String getMode(){
	// return mMode.getValue();
	// }

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("MSettings"); 
		MobclickAgent.onResume(this);
		DefaultSyncManager manager = DefaultSyncManager.getDefault();
		mClearSettings.setEnabled(manager.hasLockedAddress());
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("MSettings"); 
		MobclickAgent.onPause(this);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		SportsApp app = (SportsApp) getApplication();
		SharedPreferences sp = getSharedPreferences("user_login_info", Context.MODE_PRIVATE);
		if(!"".equals(sp.getString("account", ""))&&app.LoginOption){
			if (app.getSessionId() == null || app.getSessionId().equals("")) {
				finish();
			}
		}else if("".equals(sp.getString("account", ""))&&app.LoginOption){
			if (app.getSessionId() == null || app.getSessionId().equals("")) {
					finish();
			}
		}
	}
	
	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		if (preference == mMode) {
			int mode = Integer.valueOf(newValue.toString());
			Message msg = mHandler.obtainMessage(NOTIFY_MODE_CHANGED_MSG);
			msg.arg1 = mode;
			msg.sendToTarget();
			return true;
		} else if (preference == cameraPreviewSetting) {
			DefaultSyncManager.getDefault().featureStateChange(
					preference.getKey(), ((Boolean) newValue).booleanValue());
			return true;
		} else if (preference == mTimeoutNotificationSettings) {
			boolean value = (Boolean) newValue;
			Message msg = mHandler
					.obtainMessage(NOTIF_TIMEOUT_NOTI_CHANGED_MSG);
			msg.arg1 = value ? 1 : 0;
			msg.sendToTarget();
			return true;
		}
		return false;
	}

	private void snedModeCmd(int mode) {
		Config config = new Config(SystemModule.SYSTEM);
		ArrayList<Projo> datas = new ArrayList<Projo>();
		Projo modeCmd = new ModeSendCmd();
		modeCmd.put(ModeSendCmd.ModeSendColumn.mode, mode);
		datas.add(modeCmd);

		DefaultSyncManager manager = DefaultSyncManager.getDefault();
		manager.request(config, datas);
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		if (preference == mSync) {
			intent.setClass(MSettings.this, MSyncSettings.class);
			startActivity(intent);
		} else if (preference == mMms) {
			intent.setClass(MSettings.this, QuickSmsSettings.class);
			startActivity(intent);
		} else if (preference == mBT) {
			intent.setClass(MSettings.this, MainActivity.class);
			startActivityForResult(intent, 22);
		} else if (preference == mSearch) {
			intent.setClass(MSettings.this, SreachDevice.class);
			startActivity(intent);
		}

		return true;
	}

	private void updateBTSum() {
		SharedPreferences sps = getSharedPreferences("sprots_uid", 0);
		int sportUid = sps.getInt("sportsUid", 0);
		SharedPreferences sharedPreferences = getSharedPreferences("sports"
				+ sportUid, 0);
		String name = sharedPreferences.getString(
				DeviceListActivity.EXTRA_DEVICE_NAME, "");
		mBT.setSummary(name);
		if (TextUtils.isEmpty(name)) {
			getPreferenceScreen().removePreference(mMms);
			getPreferenceScreen().removePreference(cameraPreviewSetting);
			getPreferenceScreen().removePreference(mClearSettings);
			getPreferenceScreen().removePreference(mSync);
			getPreferenceScreen()
					.removePreference(mTimeoutNotificationSettings);
			getPreferenceScreen().removePreference(mSearch);
		} else {
			getPreferenceScreen().addPreference(mSync);
			getPreferenceScreen().addPreference(mMms);
			getPreferenceScreen().addPreference(cameraPreviewSetting);
			getPreferenceScreen().addPreference(mTimeoutNotificationSettings);
			getPreferenceScreen().addPreference(mClearSettings);
			getPreferenceScreen().addPreference(mSearch);
			mSearch.setSummary(getString(R.string.start_search, name));
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 22:
			updateBTSum();
			break;
		}
	}

	/**
	 *@method
	 *@author suhu
	 *@time 2016/10/11 13:23
	 *@param
	 *
	 */
	@Override
	public Resources getResources() {
		Resources res = super.getResources();
		Configuration config=new Configuration();
		config.setToDefaults();
		res.updateConfiguration(config,res.getDisplayMetrics() );
		return res;
	}
}
