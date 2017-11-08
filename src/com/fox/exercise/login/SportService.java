package com.fox.exercise.login;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import cn.ingenic.indroidsync.SportsApp;

import com.fox.exercise.R;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.api.WatchService;
import com.fox.exercise.api.entity.UserDetail;

public class SportService extends Service {

    private String mSessionId = "";
    private static final String TAG = "SportService";
    private UserDetail mUserDetail = null;
    private boolean mIsAdmin = false;
//	private SportsApp mSportsApp;
//	private boolean isbind;

    //	private static int PUSH_CHECK_COUNT = 0;
    private static final String NOTIFICATION_SP = "notification_sp";
    private static final String NOTIFICATION_KEY = "notification_id";
    private static final String NOTIFICATION_LOCAL_KEY = "notification_local_id";
    public static final String NOTIFICATION_MSG_ID = "notification_msg_id";
    public static final String NOTIFICATION_USERBROWSE_ID = "notification_userbrowse_id";
    private Timer timer;
    private SportsTask task;
    // private LocalBroadcastManager mBroadcasrManager = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate");
//		mSportsApp = (SportsApp) getApplication();
        ClearThread clearThread = new ClearThread();
        clearThread.start();
        // mBroadcasrManager = LocalBroadcastManager.getInstance(this);
        final IntentFilter intentFilter = new IntentFilter();
        //intentFilter.addAction(Intent.ACTION_TIME_TICK);
        //intentFilter.addAction(Intent.ACTION_TIME_CHANGED);
        intentFilter.addAction(SportAction.LOGIN_ACTION);
        intentFilter.addAction(SportAction.EXIT_ACTION);
        registerReceiver(serviceReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(serviceReceiver);
        if (task != null)
            task.cancel();
        if (timer != null)
            timer.cancel();
        //unbindService(wConnection);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        Log.e(TAG, "onStart");
        if (intent == null)
            mSessionId = "";
        try {
            mSessionId = intent.getStringExtra(SportAction.SEESION_ID_KEY);
            mIsAdmin = intent.getBooleanExtra(SportAction.IS_ADMIN_KEY, false);
        } catch (NullPointerException e) {
            e.printStackTrace();
            mSessionId = "";
        }
        timer = new Timer(true);
        if (task != null) {
            task.cancel();
        }
        task = new SportsTask();
        timer.schedule(task, 5000, 10000);
        super.onStart(intent, startId);
    }

    /*@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        if (intent == null)
            mSessionId = "";
        try {
            mSessionId = intent.getStringExtra(SportAction.SEESION_ID_KEY);
            mIsAdmin = intent.getBooleanExtra(SportAction.IS_ADMIN_KEY, false);
        } catch (NullPointerException e) {
            e.printStackTrace();
            mSessionId = "";
        }
        if (mSessionId == null || "".equals(mSessionId)) {
            Log.e(TAG, "get session fail");
            ClearThread clearThread = new ClearThread();
            clearThread.start();
            return super.onStartCommand(intent, flags, startId);
        }

        //bindService(new Intent(this, WatchService.class),
        //		wConnection, Context.BIND_AUTO_CREATE);

        return Service.START_STICKY;
    }*/
    private WatchService wService;

    //	private ServiceConnection wConnection = new ServiceConnection() {
//		public void onServiceConnected(ComponentName className, IBinder service) {
//			wService = ((WatchService.WBinder) service).getService();
//			
//		}
//
//		public void onServiceDisconnected(ComponentName className) {
//			wService = null;
//		}
//	};
    class SportsTask extends TimerTask {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            Log.i(TAG, "customthread");
            if (mSessionId == null || "".equals(mSessionId)) {
                return;
            }
            try {
                mUserDetail = ApiJsonParser.refreshRank(mSessionId);
                //if(mUserDetail.getMsgCounts().getPrimsg()>mSportsApp.getSportUser().getMsgCounts().getPrimsg()){
                //	wService.getPrimsg(mUserDetail.getUid(),mUserDetail.getUname());
                // Log.i(TAG, "wservice"+wService.toString()+","+mUserDetail.toString());
                //}
                Log.i(TAG, "user" + mUserDetail.toString());
                LocalBroadcastManager localBroadcasrManager = LocalBroadcastManager.getInstance(SportService.this);
                Intent intent = new Intent();
                intent.setAction(SportAction.DETAIL_ACTION);
                intent.putExtra(SportAction.DETAIL_KEY, mUserDetail);

                Log.e(TAG, "mIsAdmin:" + mIsAdmin);
                if (mIsAdmin) {
                    int verify = ApiJsonParser.getExamine().size();
                    intent.putExtra(SportAction.VERIFY_KEY, verify);
                } else {
                    intent.putExtra(SportAction.VERIFY_KEY, -1);
                }
                localBroadcasrManager.sendBroadcast(intent);
            } catch (ApiNetException e) {
                e.printStackTrace();
            } catch (ApiSessionOutException e) {
                e.printStackTrace();
            }
        }
    }


    private BroadcastReceiver serviceReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent intent) {
            Log.e(TAG, "onReceive");

//			if (!mIsFirstLaunch) {
//				PUSH_CHECK_COUNT++;
//				if (PUSH_CHECK_COUNT >= 1) {
//					PUSH_CHECK_COUNT = 0;
//					(new PushCheckThread()).start();
//				}
//			} else {
//				if (mFirstLaunch == 0L) {
//					mFirstLaunch = getSharedPreferences(SportService.NOTIFICATION_SP, MODE_PRIVATE).getLong(
//							SportService.NOTIFICATION_FIRST_TIME_KEY, 0L);
//				}
//				if (mFirstLaunch != 0L) {
//					long dTime = System.currentTimeMillis() - mFirstLaunch;
//					Log.e(TAG, "dTime:" + dTime);
//					if (dTime > 86400000L)
//						mIsFirstLaunch = false;
//				}
//			}
            if (SportAction.LOGIN_ACTION.equals(intent.getAction())) {
                mSessionId = intent.getStringExtra(SportAction.SEESION_ID_KEY);
                mIsAdmin = intent.getBooleanExtra(SportAction.IS_ADMIN_KEY, false);
                Log.i(TAG, "sessionid is" + mSessionId);
            } else if (SportAction.EXIT_ACTION.equals(intent.getAction())) {
                mSessionId = "";
                return;
            }
            //(new RefreshAndBroadcastThread()).start();
        }
    };

    class PushCheckThread extends Thread {

        @Override
        public void run() {

            Log.e(TAG, "PushCheckThread");
            try {
                com.fox.exercise.api.entity.Notification notifycation = ApiJsonParser.getNotification();
                SharedPreferences sp = getSharedPreferences(NOTIFICATION_SP, MODE_PRIVATE);
                int oldId = sp.getInt(NOTIFICATION_KEY, -1);
                if (oldId == notifycation.getId()) {
                    Log.e(TAG, "same notification");
                    return;
                }
                sp.edit().putInt(NOTIFICATION_KEY, notifycation.getId()).commit();
                Notification pushNotify = new Notification(R.drawable.icon, notifycation.getMsg(),
                        System.currentTimeMillis());
                pushNotify.flags = Notification.FLAG_AUTO_CANCEL;
                Intent intent = new Intent();
                if (notifycation.getMsgId() == 18) {
                    intent.setClass(SportService.this, SportMain.class);//
                    intent.putExtra(NOTIFICATION_MSG_ID, notifycation.getMsgId());
                    Log.e(TAG, "###uid:" + notifycation.getUid());
                    intent.putExtra(NOTIFICATION_USERBROWSE_ID, notifycation.getUid());
                } else {
                    intent.setClass(SportService.this, SportMain.class);
                    intent.putExtra(NOTIFICATION_MSG_ID, notifycation.getMsgId());
                    Log.e(TAG, "###uid:" + notifycation.getUid());
                    intent.putExtra(NOTIFICATION_USERBROWSE_ID, notifycation.getUid());
                }

                int localId = sp.getInt(NOTIFICATION_LOCAL_KEY, 0);
                PendingIntent pendingIntent = PendingIntent.getActivity(SportService.this, ++localId, intent,
                        PendingIntent.FLAG_ONE_SHOT);
                sp.edit().putInt(NOTIFICATION_LOCAL_KEY, localId).commit();
                pushNotify.setLatestEventInfo(SportService.this, getString(R.string.sports_name),
                        notifycation.getMsg(), pendingIntent);

                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                manager.notify(0, pushNotify);

            } catch (ApiNetException e) {
                e.printStackTrace();
            }
        }
    }

    class ClearThread extends Thread {

        @Override
        public void run() {

            File f = new File(SportUtilities.RECYCLE_PATH);
            if (f.exists()) {
                File[] files = f.listFiles();
                if (files != null && files.length > 0) {
                    for (int i = 0; i < files.length; i++) {
                        if (files[i] != null) {
                            files[i].delete();
                        } else {
                            continue;
                        }
                    }
                }
            }
            if (f != null)
                f.delete();
            f = new File("/sdcard/Recording/");
            if (f.exists()) {
                File[] files = f.listFiles();
                if (files != null && files.length > 0) {
                    for (int i = 0; i < files.length; i++) {
                        if (files[i] != null) {
                            files[i].delete();
                        } else {
                            continue;
                        }
                    }
                }
            }
            if (f != null)
                f.delete();

            f = new File(Util.CAMERA_MANAGER_BUCKET_NAME + "/MTZS/.temp");
            if (f.exists()) {
                File[] files = f.listFiles();
                if (files != null && files.length > 0) {
                    for (int i = 0; i < files.length; i++) {
                        if (files[i] != null) {
                            files[i].delete();
                        } else {
                            continue;
                        }
                    }
                }
            }
            if (f != null)
                f.delete();
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}
