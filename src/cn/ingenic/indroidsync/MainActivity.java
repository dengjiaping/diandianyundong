package cn.ingenic.indroidsync;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceGroup;
import android.preference.PreferenceScreen;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.fox.exercise.R;
import com.fox.exercise.SmartBarUtils;
import com.fox.exercise.api.ApiConstant;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Set;

import cn.ingenic.indroidsync.utils.CompatibilityTools;
import cn.ingenic.indroidsync.utils.CompatibilityTools.CompatibilityException;

@SuppressWarnings("deprecation")
public class MainActivity extends PreferenceActivity implements
        OnPreferenceClickListener {

    private BluetoothAdapter mAdapter;
    private DefaultSyncManager mManager;
    private Preference mBondedPreference;
    private Preference mSearchPreference;
    private PreferenceGroup mPairedDevicesCategory;
    private PreferenceGroup mBoundedDeviceCategory;
    private PreferenceGroup mAvailableDevicesCategory;
    private PreferenceGroup mSearchCategory;
    private boolean mShowingScanDevices = false;
    private boolean mScanning = false;
    private HashMap<String, BluetoothDevice> mScanDevices = new HashMap<String, BluetoothDevice>();
    private Editor edit;

    private static final int REQUEST_ENABLE_BT = 0;

    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    public static String EXTRA_DEVICE_NAME = "device_name";
    private static Preference pairedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (LogTag.V) {
            Log.d(LogTag.APP, "MainActivity onCreate");
        }
        boolean findMethod = SmartBarUtils.findActionBarTabsShowAtBottom();
        /*
		 * if (!findMethod) { // 取消ActionBar拆分，换用TabHost
		 * getWindow().setUiOptions(0);
		 * //getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE); }
		 */
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mManager = DefaultSyncManager.getDefault();
        SharedPreferences sps = getSharedPreferences("sprots_uid", 0);
        int sportUid = sps.getInt("sportsUid", 0);
        SharedPreferences sharedPreferences = getSharedPreferences("sports"
                + sportUid, 0);
        edit = sharedPreferences.edit();
        addPreferencesFromResource(R.xml.settings);
        initiateProgressDialog();
        if (findMethod) {
            getActionBar().setDisplayShowHomeEnabled(false);
            getActionBar().setDisplayShowTitleEnabled(false);
            SmartBarUtils.setActionModeHeaderHidden(getActionBar(), true);
            SmartBarUtils.setActionBarViewCollapsable(getActionBar(), true);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_ENABLE_BT == requestCode) {
            switch (resultCode) {
                case RESULT_OK:
                    break;
                case RESULT_CANCELED:
                    Toast.makeText(this,
                            getResources().getString(R.string.disable_bt),
                            Toast.LENGTH_LONG).show();
                    finish();
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("MainActivity");
        MobclickAgent.onResume(this);

        if (LogTag.V) {
            Log.d(LogTag.APP, "MainActivity onResume");
        }

        if (mBindProgressDialog.isShowing()) {
            DefaultSyncManager mgr = DefaultSyncManager.getDefault();
            int state = mgr.getState();
            if (state == DefaultSyncManager.IDLE
                    || state == DefaultSyncManager.CONNECTED) {
                mBindProgressDialog.dismiss();
            }
        }

        ensureBluetoothIsEnabled();

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(DefaultSyncManager.RECEIVER_ACTION_STATE_CHANGE);
        registerReceiver(mBluetoothReceiver, filter);

        updateUI();
    }

    private void ensureBluetoothIsEnabled() {
        if (mAdapter != null && !mAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MainActivity");
        MobclickAgent.onPause(this);
        if (LogTag.V) {
            Log.d(LogTag.APP, "MainActivity onPause");
        }

        unregisterReceiver(mBluetoothReceiver);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mShowingScanDevices = false;
        mScanning = false;
        // invalidateOptionsMenuPossible();
        mBondedPreference = null;
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Log.i("onDestroy", "MainActivity onDestroy");
    }

    private final BroadcastReceiver mBluetoothReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(LogTag.APP, "receiver comes:" + action);
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                updateUI();
            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() == BluetoothDevice.BOND_BONDED)
                    luluBindPair(pairedPreference);
                updateUI();
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if (mScanDevices.get(device.getAddress()) == null) {
                    mScanDevices.put(device.getAddress(), device);
                }
                updateUI();

            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                mShowingScanDevices = true;
                mScanning = true;
                // invalidateOptionsMenuPossible();
                updateUI();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
                    .equals(action)) {
                mScanning = false;
                if (mSearchPreference != null)
                    mSearchPreference.setSummary("");
                // invalidateOptionsMenuPossible();
            } else if (DefaultSyncManager.RECEIVER_ACTION_STATE_CHANGE
                    .equals(action)) {
                int state = intent.getIntExtra(DefaultSyncManager.EXTRA_STATE,
                        DefaultSyncManager.IDLE);
                boolean isConnect = (state == DefaultSyncManager.CONNECTED) ? true
                        : false;

                if (LogTag.V) {
                    Log.v(LogTag.APP, "sync is "
                            + (isConnect ? "connected" : "disconnected"));
                }

                if (mConfirmDialog != null && mConfirmDialog.isShowing()) {
                    mConfirmDialog.dismiss();
                }

                if (mBindProgressDialog.isShowing()) {
                    if (!isConnect) {
                        Toast.makeText(
                                MainActivity.this,
                                getResources().getString(R.string.fail_to_bond),
                                Toast.LENGTH_LONG).show();
                    }
                    mBindProgressDialog.dismiss();
                }
                if (isConnect) {
                    String address = mManager.getLockedAddress();
                    BluetoothDevice device = mAdapter.getRemoteDevice(address);
                    title = device.getName();
                    Log.i("", "lulu是否绑定2");
                    if (TextUtils.isEmpty(title)) {
                        title = device.getAddress();
                    }
                    onFinish();
                } else
                    updateUI();
            }
        }

    };
    private String title;

    private void addDeviceCategory() {
        if (mPairedDevicesCategory == null) {
            mPairedDevicesCategory = new PreferenceCategory(this);
        } else {
            mPairedDevicesCategory.removeAll();
        }
        if (mManager.hasLockedAddress()) {
            mPairedDevicesCategory
                    .setTitle(R.string.bluetooth_preference_paired_devices);
        } else {
            mPairedDevicesCategory
                    .setTitle(R.string.bluetooth_preference_paired_devices_bond);
        }
        getPreferenceScreen().addPreference(mPairedDevicesCategory);
        Set<BluetoothDevice> set = mAdapter.getBondedDevices();

        if (set != null) {
            for (BluetoothDevice device : set) {
                initDevicePreference(device, mPairedDevicesCategory);
            }
        }
    }

    private void initDevicePreference(BluetoothDevice device,
                                      PreferenceGroup group) {
        Preference pref = new Preference(this);
        pref.setKey(device.getAddress());
        pref.setTitle(device.getName());
        Log.i("", "lulu是否绑定3");
        pref.setOnPreferenceClickListener(this);

        if (mManager.hasLockedAddress()) {
            if (mManager.getLockedAddress().equals(device.getAddress())) {
                pref.setEnabled(false);
            } else {
                pref.setEnabled(true);
            }

        } else {
            pref.setEnabled(true);
        }
        if (pref.isEnabled())
            group.addPreference(pref);

    }

    private void updateUI() {
        final PreferenceScreen preferenceScreen = getPreferenceScreen();
        preferenceScreen.removeAll();
        preferenceScreen.setOrderingAsAdded(true);

        addBondedDeviceCategory();
        addDeviceCategory();
        addScanDeviceCategory();
        addSearchCategory();
    }

    private void addSearchCategory() {
        // TODO Auto-generated method stub
        if (mSearchCategory == null)
            mSearchCategory = new PreferenceCategory(this);
        else
            mSearchCategory.removeAll();
        if (mSearchPreference == null) {
            mSearchPreference = new Preference(this);
        }
        mSearchPreference.setTitle(R.string.menu_scan_title);
        getPreferenceScreen().addPreference(mSearchCategory);
        mSearchPreference
                .setOnPreferenceClickListener(new OnPreferenceClickListener() {

                    @Override
                    public boolean onPreferenceClick(Preference arg0) {
                        // TODO Auto-generated method stub
                        if (mAdapter.getState() == BluetoothAdapter.STATE_ON) {
                            if (!mAdapter.isDiscovering()) {
                                mShowingScanDevices = true;
                                mScanning = true;
                                mSearchPreference
                                        .setSummary(R.string.menu_scanning);
                                mAdapter.startDiscovery();
                                updateUI();
                            }
                        }
                        return true;
                    }

                });
        mSearchCategory.addPreference(mSearchPreference);
    }

    private void addScanDeviceCategory() {
        if (mShowingScanDevices) {
            if (mAvailableDevicesCategory == null) {
                mAvailableDevicesCategory = new PreferenceCategory(this);
                mAvailableDevicesCategory
                        .setTitle(R.string.bluetooth_preference_devices);
            } else {
                mAvailableDevicesCategory.removeAll();
            }

            for (BluetoothDevice device : mScanDevices.values()) {
                if (device.getBondState() != BluetoothDevice.BOND_BONDED)
                    addScanDevice(device);
                Log.i("", "lulu" + "配对开始啦成功啦");

            }

            getPreferenceScreen().addPreference(mAvailableDevicesCategory);

        } else {
            mScanDevices.clear();
            if (mAvailableDevicesCategory != null) {
                mAvailableDevicesCategory.removeAll();
            }
        }

    }

    private void addScanDevice(final BluetoothDevice device) {
        Preference pref = new Preference(this);
        String title = device.getName();
        Log.i("", "lulu是否绑定4");
        if (TextUtils.isEmpty(title)) {
            title = device.getAddress();
        }

        pref.setTitle(title);
        pref.setKey(device.getAddress());
        if (device.getBondState() == BluetoothDevice.BOND_BONDING) {
            pref.setSummary(R.string.bluetooth_pairing);
        }
        pref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                MainActivity.pairedPreference = preference;
                if (mAdapter.isDiscovering()) {
                    mAdapter.cancelDiscovery();
                    Log.i("", "lulu" + "配对开始啦");
                }
                try {
                    CompatibilityTools.invokeWithReflect(BluetoothDevice.class,
                            device, "createBond", null, null);
                } catch (CompatibilityException e) {
                    Log.e(LogTag.APP, "", e);
                }
                return true;
            }
        });
        mAvailableDevicesCategory.addPreference(pref);

    }

    private void addBondedDeviceCategory() {
        if (mManager.hasLockedAddress()) {
            if (mBoundedDeviceCategory == null) {
                mBoundedDeviceCategory = new PreferenceCategory(this);
            } else {
                mBoundedDeviceCategory.removeAll();
            }

            String address = mManager.getLockedAddress();
            final BluetoothDevice device = mAdapter.getRemoteDevice(address);
            if (mBondedPreference == null) {
                mBondedPreference = new Preference(this);
            }

            mBoundedDeviceCategory
                    .setTitle(R.string.bluetooth_preference_bonded_devices);

            title = device.getName();
            Log.i("", "lulu是否绑定1");
            if (TextUtils.isEmpty(title)) {
                title = device.getAddress();
            }
            mBondedPreference.setTitle(title);

            getPreferenceScreen().addPreference(mBoundedDeviceCategory);
            mBondedPreference
                    .setOnPreferenceClickListener(new OnPreferenceClickListener() {
                        @Override
                        public boolean onPreferenceClick(Preference preference) {
                            Log.i("MainActivity",
                                    "bonded" + preference.getKey());
                            Log.i("", "lulu" + "这是在干吗");
                            onFinish();
                            return true;
                        }
                    });
            mBoundedDeviceCategory.addPreference(mBondedPreference);
        }
    }

    protected void onFinish() {
        // TODO Auto-generated method stub

        Intent intent = new Intent();
        Bundle data = new Bundle();
        data.putString(EXTRA_DEVICE_NAME, title);
        intent.putExtras(data);
        edit.putString(EXTRA_DEVICE_NAME, title);
        edit.commit();
        setResult(ApiConstant.CONNECTOK, intent);
        finish();
    }

    @Override
    public boolean onPreferenceClick(final Preference preference) {
        Log.i("", "lulu是否绑定");
        luluBind(preference);
        return true;
    }

    private ProgressDialog mBindProgressDialog = null;
    private Dialog mConfirmDialog = null;

    private void initiateProgressDialog() {
        if (mBindProgressDialog == null) {
            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle(getString(R.string.bluetooth_bonding));
            dialog.setMessage(getString(R.string.waiting));
            dialog.setCancelable(false);
            dialog.setIndeterminate(true);
            dialog.setCanceledOnTouchOutside(false);
            mBindProgressDialog = dialog;
        }
    }

    public void luluBind(final Preference preference) {
        if (!mManager.hasLockedAddress()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle(R.string.bond)
                    .setMessage(
                            String.format(getString(R.string.bond_message),
                                    preference.getTitle()))
                    .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    if (which == DialogInterface.BUTTON_POSITIVE) {
                                        mManager.connect(preference.getKey());
                                        mBindProgressDialog.show();
                                    }
                                }
                            });
            mConfirmDialog = builder.create();
            mConfirmDialog.show();

        } else {
            Builder alertDialog = new AlertDialog.Builder(this);

            alertDialog.setMessage(getResources().getString(
                    R.string.tip_message));

            alertDialog.setTitle(getResources().getString(
                    R.string.clear_settings));

            alertDialog.setPositiveButton(
                    getResources().getString(R.string.button_ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            final int CREATE_PROCESS_BAR = 0, CLEAR_PROCESS_BAR = 1;
                            final ProgressDialog progressDialog = new ProgressDialog(
                                    MainActivity.this);
                            progressDialog.setIndeterminate(true);
                            progressDialog.setCancelable(false);
                            progressDialog
                                    .setMessage(getString(R.string.waiting));
                            final Handler handler = new Handler() {

                                @Override
                                public void handleMessage(Message msg) {
                                    switch (msg.what) {
                                        case CREATE_PROCESS_BAR:
                                            progressDialog.show();
                                            break;
                                        case CLEAR_PROCESS_BAR:
                                            updateUI();

                                            mManager.disconnect();
                                            break;
                                    }
                                }

                            };
                            handler.sendEmptyMessageDelayed(CREATE_PROCESS_BAR,
                                    1000);
                            new Thread() {

                                @Override
                                public void run() {
                                    mManager.setLockedAddress("", true);
                                    edit.putString(EXTRA_DEVICE_NAME, "");
                                    edit.commit();
                                    getPreferenceScreen().removePreference(
                                            mBondedPreference);
                                    handler.removeMessages(CREATE_PROCESS_BAR);
                                    progressDialog.dismiss();
                                    handler.sendEmptyMessage(CLEAR_PROCESS_BAR);
                                }

                            }.start();

                        }
                    });

            alertDialog.setNegativeButton(
                    getResources().getString(R.string.button_cancel),
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    });

            alertDialog.show();

        }
    }

    public void luluBindPair(final Preference preference) {
        if (!mManager.hasLockedAddress()) {
            mManager.connect(preference.getKey());
            mBindProgressDialog.show();

        } else {
            Builder alertDialog = new AlertDialog.Builder(this);

            alertDialog.setMessage(getResources().getString(
                    R.string.tip_message));

            alertDialog.setTitle(getResources().getString(
                    R.string.clear_settings));

            alertDialog.setPositiveButton(
                    getResources().getString(R.string.button_ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            final int CREATE_PROCESS_BAR = 0, CLEAR_PROCESS_BAR = 1;
                            final ProgressDialog progressDialog = new ProgressDialog(
                                    MainActivity.this);
                            progressDialog.setIndeterminate(true);
                            progressDialog.setCancelable(false);
                            progressDialog
                                    .setMessage(getString(R.string.waiting));
                            final Handler handler = new Handler() {

                                @Override
                                public void handleMessage(Message msg) {
                                    switch (msg.what) {
                                        case CREATE_PROCESS_BAR:
                                            progressDialog.show();
                                            break;
                                        case CLEAR_PROCESS_BAR:
                                            updateUI();

                                            mManager.disconnect();
                                            break;
                                    }
                                }

                            };
                            handler.sendEmptyMessageDelayed(CREATE_PROCESS_BAR,
                                    1000);
                            new Thread() {

                                @Override
                                public void run() {
                                    mManager.setLockedAddress("", true);
                                    edit.putString(EXTRA_DEVICE_NAME, "");
                                    edit.commit();
                                    getPreferenceScreen().removePreference(
                                            mBondedPreference);
                                    handler.removeMessages(CREATE_PROCESS_BAR);
                                    progressDialog.dismiss();
                                    handler.sendEmptyMessage(CLEAR_PROCESS_BAR);
                                }

                            }.start();

                        }
                    });

            alertDialog.setNegativeButton(
                    getResources().getString(R.string.button_cancel),
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    });

            alertDialog.show();
        }
    }
}
