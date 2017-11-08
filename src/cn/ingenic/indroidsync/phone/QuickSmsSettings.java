package cn.ingenic.indroidsync.phone;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;

import com.fox.exercise.R;
import com.fox.exercise.SmartBarUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.EnumSet;

import cn.ingenic.indroidsync.Config;
import cn.ingenic.indroidsync.DefaultSyncManager;
import cn.ingenic.indroidsync.data.DefaultProjo;
import cn.ingenic.indroidsync.data.Projo;
import cn.ingenic.indroidsync.data.ProjoType;
/**
 * merage from Phone/src/...
 * Helper class to manage the "Respond via SMS" feature for incoming calls.
 *
 * @author dfdun
 * @see InCallScreen.internalRespondViaSms()
 */

/**
 * Settings activity under "Call settings" to let you manage the canned
 * responses; see respond_via_sms_settings.xml
 */
public class QuickSmsSettings extends PreferenceActivity implements
        Preference.OnPreferenceChangeListener,
        Preference.OnPreferenceClickListener {
    public static final String SHARED_PREFERENCES_NAME = "respond_via_sms_prefs";
    private static final String KEY_CANNED_RESPONSE_PREF_1 = "canned_response_pref_1";
    private static final String KEY_CANNED_RESPONSE_PREF_2 = "canned_response_pref_2";
    private static final String KEY_CANNED_RESPONSE_PREF_3 = "canned_response_pref_3";
    private static final String KEY_CANNED_RESPONSE_PREF_4 = "canned_response_pref_4";
    public static final int SYNC_SMS_CODE = 30;// opeator code

    final String KEY_SYNC_VIA_SMS = "sync_via_sms";
    final String[] keys = {KEY_CANNED_RESPONSE_PREF_1,
            KEY_CANNED_RESPONSE_PREF_2, KEY_CANNED_RESPONSE_PREF_3,
            KEY_CANNED_RESPONSE_PREF_4};
    ArrayList<Preference> mPreferences = new ArrayList<Preference>();

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        boolean findMethod = SmartBarUtils.findActionBarTabsShowAtBottom();
    /*	if (!findMethod) {
			 // 取消ActionBar拆分，换用TabHost
		 	getWindow().setUiOptions(0);
		        getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
		}*/
        getPreferenceManager()
                .setSharedPreferencesName(SHARED_PREFERENCES_NAME);

        addPreferencesFromResource(R.xml.respond_via_sms_settings);

        EditTextPreference pref;
        pref = (EditTextPreference) findPreference(KEY_CANNED_RESPONSE_PREF_1);
        pref.setTitle(pref.getText());
        pref.setOnPreferenceChangeListener(this);
        mPreferences.add(pref);

        pref = (EditTextPreference) findPreference(KEY_CANNED_RESPONSE_PREF_2);
        pref.setTitle(pref.getText());
        pref.setOnPreferenceChangeListener(this);
        mPreferences.add(pref);

        pref = (EditTextPreference) findPreference(KEY_CANNED_RESPONSE_PREF_3);
        pref.setTitle(pref.getText());
        pref.setOnPreferenceChangeListener(this);
        mPreferences.add(pref);

        pref = (EditTextPreference) findPreference(KEY_CANNED_RESPONSE_PREF_4);
        pref.setTitle(pref.getText());
        pref.setOnPreferenceChangeListener(this);
        mPreferences.add(pref);
        if (findMethod) {
            getActionBar().setDisplayShowHomeEnabled(false);
            getActionBar().setDisplayShowTitleEnabled(false);
            SmartBarUtils.setActionModeHeaderHidden(getActionBar(), true);
            SmartBarUtils.setActionBarViewCollapsable(getActionBar(), true);
        }
        findPreference(KEY_SYNC_VIA_SMS).setOnPreferenceClickListener(this);
    }

    // Preference.OnPreferenceChangeListener implementation
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        EditTextPreference pref = (EditTextPreference) preference;
        pref.setTitle((String) newValue);
        Projo projo = new DefaultProjo(EnumSet.allOf(PhoneColumn.class),
                ProjoType.DATA);
        projo.put(PhoneColumn.state, SYNC_SMS_CODE);
        projo.put(PhoneColumn.name, preference.getKey());
        projo.put(PhoneColumn.phoneNumber, ((String) newValue));
        Config config = new Config(PhoneModule.PHONE);
        ArrayList<Projo> datas = new ArrayList<Projo>(1);
        datas.add(projo);
        DefaultSyncManager.getDefault().request(config, datas);
        return true; // means it's OK to update the state of the Preference with
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference.getKey().equals(KEY_SYNC_VIA_SMS)) {
            Log.i("dfdun", "preference is clicked");
            Config config = new Config(PhoneModule.PHONE);
            ArrayList<Projo> datas = new ArrayList<Projo>(1);
            for (Preference pref : mPreferences) {
                Projo projo = new DefaultProjo(
                        EnumSet.allOf(PhoneColumn.class), ProjoType.DATA);
                projo.put(PhoneColumn.state, SYNC_SMS_CODE);
                projo.put(PhoneColumn.name, pref.getKey());
                projo.put(PhoneColumn.phoneNumber, pref.getTitle());
                datas.add(projo);
            }
            DefaultSyncManager.getDefault().request(config, datas);
        }
        return true;
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("QuickSmsSettings");
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("QuickSmsSettings");
        MobclickAgent.onPause(this);
    }

    /**
     *@method 固定字体大小方法
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
