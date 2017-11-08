package cn.ingenic.indroidsync;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.util.Log;

import com.fox.exercise.R;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.ingenic.indroidsync.contactsms.contacts.ContactModule;
import cn.ingenic.indroidsync.contactsms.sms2.SmsModule2;
import cn.ingenic.indroidsync.data.FeatureConfigCmd;
import cn.ingenic.indroidsync.data.Projo;
import cn.ingenic.indroidsync.devicemanager.DeviceModule;

/**
 * A Preference , setting about which to sync ; use isSync(key) to get it
 * 
 * @author dfdun
 * */
public class MSyncSettings extends PreferenceActivity {
	private static final String TAG = "SyncSettings";

	public static final String MMS_KEY = SmsModule2.SMS2,
			CONTACT_KEY = ContactModule.CONTACT,
			CALLL0G_KEY = DeviceModule.FEATURE_CALLLOG,
			// EMAIL_KEY="email",
			//CALENDAR_KEY = "calendar",
			WEATHER_KEY = DeviceModule.FEATURE_WEATHER,
			TIME_KEY = DeviceModule.FEATURE_TIME,
			// CLOCK_KEY="clock",
			// MUSIC_KEY="music",
			BATTERY_KEY = DeviceModule.FEATURE_BATTERY;
	static final Entry[] ENTRIES = {
			new Entry(MMS_KEY, R.string.sync_mms),
			new Entry(CONTACT_KEY, R.string.sync_contact),
			new Entry(CALLL0G_KEY, R.string.sync_calllog),
			// new Entry(EMAIL_KEY, R.string.sync_email),
			//new Entry(CALENDAR_KEY, R.string.sync_calendar),
			new Entry(WEATHER_KEY, R.string.sync_weather),
			new Entry(TIME_KEY, R.string.sync_time),
			// new Entry(CLOCK_KEY, R.string.sync_clock),
			// new Entry(MUSIC_KEY, R.string.sync_music),
			new Entry(BATTERY_KEY, R.string.sync_battery) };

	static class Entry {
		final String key;
		final int title;

		Entry(String key, int title) {
			this.key = key;
			this.title = title;
		}
	}

	private static class Arg {
		final String key;
		final boolean value;

		Arg(String key, boolean value) {
			this.key = key;
			this.value = value;
		}
	}

	private static final int MSG_NOTIFY = 0;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_NOTIFY:
				Arg arg = (Arg) msg.obj;
				String key = arg.key;
				boolean value = arg.value;

				DefaultSyncManager manager = DefaultSyncManager.getDefault();

				Config config = new Config(SystemModule.SYSTEM);
				Map<String, Boolean> map = new HashMap<String, Boolean>();
				map.put(key, value);
				Projo projo = new FeatureConfigCmd();
				projo.put(FeatureConfigCmd.FeatureConfigColumn.feature_map, map);
				ArrayList<Projo> datas = new ArrayList<Projo>();
				datas.add(projo);
				manager.request(config, datas);

				manager.featureStateChange(key, value);
				break;
			}
		}

	};

	@SuppressWarnings("deprecation")
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.sync_settings);
		final PreferenceScreen preferenceScreen = getPreferenceScreen();
		preferenceScreen.removeAll();
		for (Entry entry : ENTRIES) {
			CheckBoxPreference pref = new CheckBoxPreference(this);
			pref.setDefaultValue(true);
			pref.setKey(entry.key);
			pref.setTitle(entry.title);
			pref.setOnPreferenceChangeListener(mListener);
			preferenceScreen.addPreference(pref);
		}
	}

	private OnPreferenceChangeListener mListener = new OnPreferenceChangeListener() {
		@Override
		public boolean onPreferenceChange(Preference p, Object arg1) {
			Log.i(TAG, p.getTitle() + " is changed; " + arg1);
			if (p instanceof CheckBoxPreference
					&& !p.getKey().equals(WEATHER_KEY)) {
				Message msg = mHandler.obtainMessage(MSG_NOTIFY);
				msg.obj = new Arg(p.getKey(), (Boolean) arg1);
				msg.sendToTarget();
			}
			return true;
		}
	};

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("MSyncSettings");
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("MSyncSettings");
		MobclickAgent.onPause(this);
	}
}
