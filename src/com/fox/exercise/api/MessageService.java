package com.fox.exercise.api;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;

import com.fox.exercise.R;
import com.fox.exercise.StateActivity;
import com.fox.exercise.api.entity.UserDetail;
import com.fox.exercise.login.SportAction;
import com.fox.exercise.login.SportMain;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cn.ingenic.indroidsync.SportsApp;

public class MessageService extends Service {

    private ScheduledExecutorService scheduledThreadPool;
    private SportsApp mSportsApp;
    private SharedPreferences spf;
    private static final int USERDETAIL = 12;
    private static final int USERExit = 13;
    private Boolean isExitBoolean;
    private SharedPreferences pSpf;
    private static final String TAG = "MessageService";

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        mSportsApp = (SportsApp) getApplication();
        spf = getSharedPreferences("sports", 0);
        pSpf = getSharedPreferences("sports" + mSportsApp.getSportUser().getUid(), 0);
        //Log.e(TAG, "1--------isExitBoolean"+isExitBoolean);
        scheduledThreadPool = Executors.newScheduledThreadPool(1);
        scheduledThreadPool.scheduleWithFixedDelay(new CustomTask(), 5, 5,
                TimeUnit.MINUTES);
        super.onCreate();
    }

    class CustomTask implements Runnable {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            if (mSportsApp.getSessionId() == null || "".equals(mSportsApp.getSessionId()))
                return;
            try {
                UserDetail mUserDetail = ApiJsonParser
                        .refreshRank(mSportsApp.getSessionId());
                //Log.e(TAG, "接收------------------------进入1");
                if (mUserDetail != null) {
                    //Log.e(TAG, "接收------------------------进入2");
                    isExitBoolean = false;
                    SharedPreferences sp = getSharedPreferences("user_login_info",
                            Context.MODE_PRIVATE);
                    //Log.e(TAG, "---------------------isApp--"+mSportsApp.isAppOnForeground());
                    if (!(mSportsApp.isAppOnForeground()) && (!"".equals(sp.getString("account", "")))) {
                        isExitBoolean = true;
                    }
                    //Log.e(TAG, "2--------isExitBoolean--"+isExitBoolean);
                    if (!isExitBoolean) {
                        //Log.e(TAG, "接收全部消息-------------------");
                        Message message = Message.obtain(sHandler, USERDETAIL);
                        message.obj = mUserDetail;
                        message.sendToTarget();
                    } else {
                        //Log.e(TAG, "接收私人消息-------------------");
                        Message message = Message.obtain(sHandler, USERExit);
                        message.obj = mUserDetail;
                        message.sendToTarget();
                    }
                }
            } catch (ApiNetException e) {
                e.printStackTrace();
            } catch (ApiSessionOutException e) {
                e.printStackTrace();
            }
        }
    }

    private Handler sHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case USERExit:
                    UserDetail exitmUserDetail = (UserDetail) msg.obj;
                    int exitmprimsg = spf.getInt("primsgcount", 0) + exitmUserDetail.getMsgCounts().getPrimsg();
                    int exitsysmsg = exitmUserDetail.getMsgCounts().getSysmsgsports();
                    if (exitmprimsg > mSportsApp.ExitPreMessage || exitsysmsg > mSportsApp.ExitSysMessage) {
                        int exitmsgcount = exitmprimsg + exitsysmsg;
                        Notification notification = new Notification(R.drawable.icon,
                                "您有" + exitmsgcount + "条新的消息", System.currentTimeMillis());
                        notification.defaults |= Notification.DEFAULT_SOUND;
                        // n.sound = Uri.withAppendedPath(
                        // Audio.Media.INTERNAL_CONTENT_URI, "2");
                        notification.flags = Notification.FLAG_AUTO_CANCEL;
                        CharSequence contentTitle = "云狐运动";
                        CharSequence contentText = "";
                        if (exitmprimsg > mSportsApp.ExitPreMessage) {
                            contentText = contentText + "您现在有" + exitmprimsg + "条新的私信----";
                        }
                        if (exitsysmsg > mSportsApp.ExitSysMessage) {
                            contentText = contentText + "和" + exitsysmsg + "条系统消息----";
                        }
                        mSportsApp.ExitPreMessage = exitmprimsg;
                        mSportsApp.ExitSysMessage = exitsysmsg;
                        contentText = contentText + "快去看看吧";
                        Intent notificationIntent = new Intent();
                        notificationIntent.setClass(getApplicationContext(),
                                SportMain.class);
                        PendingIntent contentIntent = PendingIntent.getActivity(
                                getApplicationContext(), 0, notificationIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT);
                        notification.setLatestEventInfo(getApplicationContext(),
                                contentTitle, contentText, contentIntent);

                        // 用mNotificationManager的notify方法通知用户生成标题栏消息通知
                        mSportsApp.mNotificationManager.notify(100, notification);
                    }

                    break;
                case USERDETAIL:
                    UserDetail mUserDetail = (UserDetail) msg.obj;
                    int invite = mUserDetail.getMsgCounts().getInvitesports();
                    int mprimsg = spf.getInt("primsgcount", 0) + mUserDetail.getMsgCounts().getPrimsg();
                    int fuploads = mUserDetail.getActmsgs();
                    int fans = mUserDetail.getMsgCounts().getFans();
                    int visitor = mUserDetail.getMsgCounts().getSportVisitor();
                    int sysmsg = mUserDetail.getMsgCounts().getSysmsgsports();
                    int totalMsg = mprimsg + fuploads + fans + visitor + sysmsg;
                    mUserDetail.getMsgCounts().setPrimsg(mprimsg);
                    mSportsApp.setSportUser(mUserDetail);
                    //FoxSportsSetting.setMsgbox(totalMsg);
                    if (invite > 0) {
                        Handler mHandler = mSportsApp.getmHandler();
                        if (mHandler != null) {
                            mHandler.sendMessage(mHandler.obtainMessage(
                                    StateActivity.UPDATE_INVITE, invite, 0));
                        }

                    }
                    if (totalMsg > 0) {
                        LocalBroadcastManager localBroadcasrManager = LocalBroadcastManager
                                .getInstance(MessageService.this);
                        Intent intent = new Intent();
                        intent.setAction(SportAction.DETAIL_ACTION);
                        intent.putExtra(SportAction.DETAIL_KEY, mUserDetail);
                        Handler mainHandler = mSportsApp.getMainHandler();
                        if (mainHandler != null) {
                            mainHandler.sendMessage(mainHandler.obtainMessage(
                                    ApiConstant.UPDATE_MSG, totalMsg, 0));
                        }
                        localBroadcasrManager.sendBroadcast(intent);
                    }
                    break;

            }
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (scheduledThreadPool != null)
            scheduledThreadPool.shutdown();
        super.onDestroy();
    }
}
