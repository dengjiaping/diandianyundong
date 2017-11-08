package com.fox.exercise.api;

import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fox.exercise.HistoryAllActivity;
import com.fox.exercise.R;
import com.fox.exercise.SportsUtilities;
import com.fox.exercise.SreachDevice;
import com.fox.exercise.WarnActivity;
import com.fox.exercise.api.entity.NewCommentInfo;
import com.fox.exercise.api.entity.UserDetail;
import com.fox.exercise.api.entity.UserPrimsgAll;
import com.fox.exercise.api.entity.UserPrimsgOne;
import com.fox.exercise.db.PrimsgDeleteDb;
import com.fox.exercise.db.SportDatabase;
import com.fox.exercise.db.SportSubTaskDB;
import com.fox.exercise.db.SportsContent.PrivateMessageAllTable;
import com.fox.exercise.login.LoginActivity;
import com.fox.exercise.login.SportAction;
import com.fox.exercise.login.SportMain;
import com.fox.exercise.map.SportTaskDetailActivityGaode;
import com.fox.exercise.newversion.UUIDGenerator;
import com.fox.exercise.newversion.entity.TrainCount;
import com.fox.exercise.pedometer.SportContionTaskDetail;
import com.fox.exercise.util.LogUtils;
import com.fox.exercise.util.SportTaskUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import cn.ingenic.indroidsync.DefaultSyncManager;
import cn.ingenic.indroidsync.SportsApp;
import cn.ingenic.indroidsync.services.SyncData;
import cn.ingenic.indroidsync.services.SyncException;
import cn.ingenic.indroidsync.services.SyncModule;

public class WatchService extends Service {

    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    private static final int FRESH_LIST = 6;
    public static final String DEVICE_NAME = "device_name";
    SharedPreferences spf;
    private ExecutorService cachedThreadExecutor;
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private int mStepValue = 0;
    // public static BluetoothUtilNew mBluetoothUtilNew;


    private static final String TAG = "WatchService";
    private static final int ADD_ITEM = 10;
    public static final int PRI_ALL = 11;
    public static final int USERDETAIL = 12;
    private static final int USERExit = 13;
    private static final int OTHERLOGIN = 19;
    private Boolean isExitBoolean = true;
    private int uid = 0;
    private String sessionId = "";

    private String strDate;
    private String strTime;

    private Boolean isFirst;



    private SportsApp mSportsApp;
    private CustomModule cModule;
    private int mState;
    private DefaultSyncManager mgr;
    private XMLReader reader;
    private ScheduledExecutorService scheduledThreadPool;

    class GetGiftsThread implements Runnable {
        @Override
        public void run() {

            ArrayList<UserPrimsgAll> privateMsgAllList = new ArrayList<UserPrimsgAll>();
            List<UserPrimsgAll> privateMsgAll = new ArrayList<UserPrimsgAll>();
            try {
                int page = 0;
                for (int i = 0; i <= page; i++) {
                    privateMsgAllList = (ArrayList<UserPrimsgAll>) ApiJsonParser
                            .getPrimsgAll(mSportsApp.getSessionId(), i);
                    LogUtils.e("privateMsgAllListONE:", "prilist"
                            + privateMsgAllList.toString());
                    privateMsgAll.addAll(privateMsgAllList);
                    if (privateMsgAllList.size() > 0) {
                        page++;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (privateMsgAll != null && privateMsgAll.size() > 0) {
                mSportsApp.getSportsDAO().insertPrivateMsgAll(
                        PrivateMessageAllTable.TABLE_NAME, privateMsgAll,
                        "unread");
                for (UserPrimsgAll pa : privateMsgAll) {
                    int uid = pa.getUid();
                    String uname = pa.getName();
                    getPrimsg(uid, uname);

                }
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        LogUtils.i("SERVICE", "I have started");
        mSportsApp = (SportsApp) getApplication();
        TelephonyManager tm = (TelephonyManager) getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
        return Service.START_STICKY;
    }

    PhoneStateListener listener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            // 注意，方法必须写在super方法后面，否则incomingNumber无法获取到值
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    if (!cModule.isCreated()) {
                        Toast.makeText(mSportsApp,
                                "SyncService has not been started",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    SyncData data = new SyncData();
                    data.putString("type", "incall");
                    try {
                        cModule.send(data);
                    } catch (SyncException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
    private SharedPreferences pSpf;
    private HomeKeyEventBroadCastReceiver backgrondReceiver;

    private int coinsNum = 0;

    class CustomTask implements Runnable {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            String mTAG = "customtask";

            LogUtils.e(mTAG, "------------------------1--"
                    + mSportsApp.getSessionId().toString());
            if (mSportsApp.getSessionId() == null
                    || mSportsApp.getSessionId().toString().equals("")) {
                return;
            }
            LogUtils.i("mIsNetWork", "mIsNetWork1" + mSportsApp.mIsNetWork);
            if (mSportsApp.mIsNetWork) {
                LogUtils.e(mTAG, "------------------------2--"
                        + mSportsApp.getSessionId().toString());
                try {
                    // 发送未奖励的随机金币
//					final SharedPreferences cPreferences = getSharedPreferences(
//							"sports" + mSportsApp.getSportUser().getUid(), 0);
                    int randomCoins = pSpf.getInt("randomCoins", 0);
                    if (randomCoins > 0) {
                        if (coinsNum == 0) {
                            new AddCoinsThread(randomCoins, 2, new Handler() {

                                @Override
                                public void handleMessage(Message msg) {
                                    // TODO Auto-generated method
                                    // stub
                                    ApiBack apiBack = (ApiBack) msg.obj;
                                    if (apiBack != null && apiBack.getFlag() == 0) {
                                        Editor edit = pSpf.edit();
                                        edit.putInt("randomCoins", 0);
                                        edit.commit();
                                    }
                                }

                            }, -1).start();
                            coinsNum++;
                        }
                    }
                    if (mSportsApp.isOpenNetwork() && !mSportsApp.LoginNet) {
                        SharedPreferences sps = getSharedPreferences(
                                "user_login_info", Context.MODE_PRIVATE);
                        SharedPreferences sp_umeng = getSharedPreferences("UmengDeviceToken", Activity.MODE_PRIVATE);
                        ApiBack back = null;
                        try {
                            // 获取登陆的返回值
                            // LogUtils.e(TAG, "时间分割线----------------------3准备登录");
                            back = ApiJsonParser.login(
                                    sps.getString("account", ""),
                                    sps.getString("pwd", ""), 1, sp_umeng.getString("device_token", ""));
                        } catch (ApiNetException e) {
                            e.printStackTrace();
                            return;
                        }

                        if (back != null) {
                            LogUtils.e("develop_debug", "WatchService.java 695 " + back.getFlag());
                        }

                        if (back != null && back.getFlag() != -1 && back.getFlag() != -11) {
                            mSportsApp.LoginNet = true;
                            mSportsApp.setSessionId(back.getMsg().substring(7));
                            mSportsApp.setLogin(true);
                        }
                    }
                    UserDetail mUserDetail = ApiJsonParser
                            .refreshRank(mSportsApp.getSessionId());

                    //下载训练数据
                    ApiMessage totalTrainTask = ApiJsonParser
                            .getTotalTrainTask(mSportsApp.getSessionId());
                    if ((totalTrainTask != null) && (totalTrainTask.isFlag())) {
                        TrainCount mCount = new TrainCount();
                        JSONObject obj;
                        try {
                            //保存训练总时长
                            SharedPreferences preferences = getSharedPreferences("TrainCounts", 0);
                            Editor edit = preferences.edit();
                            edit.putString("TrainCounts_info", totalTrainTask.getMsg());
                            edit.commit();

                            obj = new JSONObject(totalTrainTask.getMsg())
                                    .getJSONObject("data");
                            if (obj.has("traintime")) {
                                mCount.setTraintime(obj
                                        .getInt("traintime"));
                            }
                            if (obj.has("train_calorie")) {
                                mCount.setTrain_calorie(obj
                                        .getDouble("train_calorie"));
                            }
                            if (obj.has("countnum")) {
                                mCount.setCountnum(obj
                                        .getInt("countnum"));
                            }
                            if (obj.has("countday")) {
                                mCount.setCountday(obj
                                        .getInt("countday"));
                            }
                            mSportsApp.setmCount(mCount);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }


                    }


                   //加载运动秀的消息数量
                    NewCommentInfo commentInfo=ApiJsonParser.getNewCommentCount(mSportsApp
                            .getSessionId(), mSportsApp.getSportUser().getUid());
                    if(commentInfo!=null){
                        if(commentInfo.getCommentCount()>0){
                            mSportsApp.setSports_Show(commentInfo.getCommentCount());
                        }
                    }




                    if (mUserDetail != null) {
                        mSportsApp.setSportUser(mUserDetail);
                        Handler mainHandler = SportsApp.getInstance()
                                .getMainHandler();
                        if (mainHandler != null) {
                            mainHandler
                                    .sendMessage(mainHandler
                                            .obtainMessage(ApiConstant.UPDATE_COINS_NOW));
                        }
                        isExitBoolean = false;
                        SharedPreferences sp = getSharedPreferences(
                                "user_login_info", Context.MODE_PRIVATE);
                        if (!(mSportsApp.isAppOnForeground())
                                && (!"".equals(sp.getString("account", "")))) {
                            isExitBoolean = true;
                        }
                        if (!isExitBoolean) {
                            Message message = Message.obtain(sHandler,
                                    USERDETAIL);
                            message.obj = mUserDetail;
                            message.sendToTarget();
                        } else {
                            Message message = Message
                                    .obtain(sHandler, USERExit);
                            message.obj = mUserDetail;
                            message.sendToTarget();
                        }
                    }
                } catch (ApiNetException e) {
//                    LogUtils.e(mTAG, "------------------------3--"
//                            + mSportsApp.getSessionId().toString());
                    LogUtils.i("Exception1-->", e.exceMsg());
                    e.printStackTrace();
                } catch (ApiSessionOutException e) {
//                    LogUtils.e(mTAG, "------------------------4--"
//                            + mSportsApp.getSessionId().toString());
                    // LogUtils.i("Exception2-->", e.exceMsg());
                    if (mSportsApp.isLogin()) {
                        Message message = Message.obtain(sHandler, OTHERLOGIN);
                        message.sendToTarget();
                    } else {
                        return;
                    }

                    // e.printStackTrace();
                }
            }
            // 第一次链接尝试是否连接上
            SyncData sdata = new SyncData();
            sdata.putString("type", "try");
            try {
                cModule.send(sdata);
            } catch (SyncException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (mConnect && spf.getBoolean("isuser", false)) {
                if (!cModule.isCreated()) {
                    Toast.makeText(mSportsApp,
                            "SyncService has not been started",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                String content = getData();
                SyncData data = new SyncData();
                data.putString("type", "user");
                data.putString("user", content);
                try {
                    cModule.send(data);
                    LogUtils.i(TAG, "cModule" + data.toString());
                } catch (SyncException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Editor editor = spf.edit();
                editor.putBoolean("isuser", false);
                editor.commit();
            }
        }
    }

    @Override
    public void onCreate() {
        IntentFilter filter = new IntentFilter();
        filter.setPriority(1000);

        if (cModule == null) {
            cModule = new CustomModule(this);
        }
        mgr = DefaultSyncManager.getDefault();
        mState = mgr.getState();
        SAXParserFactory sspf = SAXParserFactory.newInstance();
        SAXParser sp;
        try {
            sp = sspf.newSAXParser();
            reader = sp.getXMLReader();
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // ControlReceiver的IntentFilter
        mSportsApp = (SportsApp) getApplication();
        spf = getSharedPreferences("sports", 0);
        pSpf = getSharedPreferences("sports"
                + mSportsApp.getSportUser().getUid(), 0);
        backgrondReceiver = new HomeKeyEventBroadCastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        intentFilter.addAction("com.fox.exercise.exits");
        registerReceiver(backgrondReceiver, intentFilter);
        cachedThreadExecutor = Executors.newCachedThreadPool();
        scheduledThreadPool = Executors.newScheduledThreadPool(1);
        if (mSportsApp.isAppOnForeground()) {
            scheduledThreadPool.scheduleWithFixedDelay(new CustomTask(), 30,
                    30, TimeUnit.SECONDS);
            // isnotAppon=false;
        } else
            scheduledThreadPool.scheduleWithFixedDelay(new CustomTask(), 1, 5,
                    TimeUnit.MINUTES);
        super.onCreate();
    }

    class HomeKeyEventBroadCastReceiver extends BroadcastReceiver {

        // private NotificationManager mNotifMan;

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // mNotifMan = (NotificationManager) context
            // .getSystemService(NOTIFICATION_SERVICE);
            // 监听home键
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                // 区分程序是否在后台运行
                if (mSportsApp.isAppOnForeground()) {
                    UserDetail detail = SportsApp.getInstance().getSportUser();
                    mSportsApp.setDataToLocal(detail);
                    // updateScheduleWithFixedDelay(1, 300);
                    mSportsApp.isHomeKey = true;
                }
            }
            if (action.equals("com.fox.exercise.exits")) {
                context.stopService(new Intent(
                        "com.fox.exercise.pedometer.STEPSERVICE"));
                // mNotifMan.cancel(5);
                // isnotAppon=true;
                updateScheduleWithFixedDelay(1, 300);
                mSportsApp.isHomeKey = false;
                // System.exit(0);
            }
        }

    }

    @Override
    public void onDestroy() {
        // unregisterReceiver(msgReceiver);
        // mBluetoothUtilNew.onDestroy();
        if (cachedThreadExecutor != null)
            cachedThreadExecutor.shutdownNow();
        if (scheduledThreadPool != null)
            scheduledThreadPool.shutdown();
        if (backgrondReceiver != null) {
            unregisterReceiver(backgrondReceiver);
        }
        cModule = null;
        super.onDestroy();
    }

    public void updateScheduleWithFixedDelay(long initialDelay, long delay) {
        // TODO Auto-generated method stub
        if (scheduledThreadPool != null) {
            scheduledThreadPool.shutdown();
            scheduledThreadPool = Executors.newScheduledThreadPool(1);
            scheduledThreadPool.scheduleWithFixedDelay(new CustomTask(),
                    initialDelay, delay, TimeUnit.SECONDS);
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }

    private final IBinder mBinder = new WBinder();

    public interface ICallback {
        public void stepsChanged(int value);
    }

    public interface SCallback {
        public void setUser(UserDetail user);
    }

    private ICallback mCallback;
    private SCallback sCallback;
    private String uname;

    public void registerCallback(ICallback cb) {
        mCallback = cb;
    }

    public void unregisterCallback() {
        mCallback = null;
    }

    public class WBinder extends Binder {
        public WatchService getService() {
            return WatchService.this;
        }
    }

	/*
     * boolean wbool = PreferenceManager.getDefaultSharedPreferences(
	 * WatchService .this).getBoolean(MSyncSettings.WEATHER_KEY, false);
	 * if(wbool)
	 */
    // else
    // Toast.makeText(WatchService.this, "当前设置天气为不可同步状态",
    // Toast.LENGTH_SHORT).show();

    public void registerSettingCallback(SCallback wb) {
        // TODO Auto-generated method stub
        sCallback = wb;
    }

    public void sendUser() {

        LogUtils.i("userconnected", "send user here");
        String content = getData();
        SyncData data = new SyncData();
        data.putString("type", "user");
        data.putString("user", content);
        if (!cModule.isCreated()) {
            Toast.makeText(mSportsApp, "SyncService has not been started",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            if (cModule.send(data)) {

            } else {
                Editor editor = spf.edit();
                editor.putBoolean("isuser", true);
                editor.commit();
            }
            LogUtils.i(TAG, "cModule user" + data.toString());
        } catch (SyncException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // mBluetoothUtilNew.sendMessage(content);
    }

    private String getData() {
        // TODO Auto-generated method stub
        JSONObject json = new JSONObject();
        JSONObject userjo = new JSONObject();
        LogUtils.i("iiiiuser", "isuser");
        try {
            userjo.put("sex", spf.getString("sex", "woman"));
            userjo.put("height", spf.getInt("height", 170));
            userjo.put("weight", spf.getInt("weight", 60));
            json.put("set_time", spf.getString("date", ""));
            // LogUtils.i("date", "sportsdate is"+date);
            json.put("personInfo", userjo);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String user = String.valueOf(json);
        return user;
    }

    public void getPrimsg(int uid, String uname) {

        // TODO Auto-generated method stub
        List<UserPrimsgOne> latestMsgList = null;
        this.uname = uname;
        LogUtils.i(TAG, "priuid=" + uid + "uname=" + uname);
        // get the latest msg from server, then save to db
        try {
            latestMsgList = ApiJsonParser.getPrimsgOne(
                    mSportsApp.getSessionId(), uid, 0);
            LogUtils.i(TAG, "msglist" + latestMsgList.size());
        } catch (ApiNetException e) {
            e.printStackTrace();

        } catch (ApiSessionOutException e) {
            e.printStackTrace();
        }

        if (latestMsgList != null && latestMsgList.size() > 0) {
            LogUtils.d(TAG, "latestMsgList size:" + latestMsgList.size());
            Message message = Message.obtain(sHandler, ADD_ITEM);
            message.obj = latestMsgList;
            message.sendToTarget();
            // mAdapter.addItem(latestMsgList.get(i));


            final List<UserPrimsgOne> saveToDBMsgList = latestMsgList;
            new Thread() {
                @Override
                public void run() {

                    int result = mSportsApp.getSportsDAO().savePrivateMsgInfo(
                            saveToDBMsgList);
                    LogUtils.d(TAG, "save latest msg to db :" + result);
                }
            }.start();
        }
    }

    private void showDialog(final Context context) {
        final Dialog dialog;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dialog = new Dialog(mSportsApp, R.style.sports_dialog);
        View v = inflater.inflate(R.layout.sports_dialog, null);
        // 提示内容：尽快修改密码
        ((TextView) v.findViewById(R.id.message)).setText(getResources()
                .getString(R.string.sports_offline_notice_detils));
        TextView tv = (TextView) v.findViewById(R.id.title);
        // 标题：下拉通知
        tv.setText(getResources().getString(R.string.sports_offline_notice));
        v.findViewById(R.id.bt_cancel).setVisibility(View.GONE);
        Button button = (Button) v.findViewById(R.id.bt_ok);
        button.setText(getResources().getString(R.string.button_ok));
        v.findViewById(R.id.bt_ok).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View arg0) {
                        dialog.dismiss();
                        SharedPreferences sp = getSharedPreferences(
                                "user_login_info", Context.MODE_PRIVATE);
                        Editor edit = sp.edit();
                        edit.remove("account").commit();
                        Intent intents = new Intent(WatchService.this,
                                LoginActivity.class);
                        intents.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getBaseContext().startActivity(intents);
                        mSportsApp.removeAllActivity();
                        context.stopService(new Intent(
                                "com.fox.exercise.pedometer.STEPSERVICE"));
                        updateScheduleWithFixedDelay(1, 300);
                        System.exit(0);
                    }
                });
        dialog.getWindow()
                .setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕其他地方，dialog不消失
        dialog.setCancelable(false);// 设置点击返回键和HOme键，dialog不消失
        dialog.setContentView(v);
        if (dialog.isShowing()) {
            return;
        } else {
            dialog.show();
        }
        LogUtils.i("PLog", "2");
    }

    private Handler sHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case OTHERLOGIN:
                    showDialog(mSportsApp);
                    break;
                case USERExit:
                    UserDetail exitmUserDetail = (UserDetail) msg.obj;
                    int exitmprimsg = spf.getInt("primsgcount", 0)
                            + exitmUserDetail.getMsgCounts().getPrimsg();
                    int exitsysmsg = exitmUserDetail.getMsgCounts()
                            .getSysmsgsports();
                    if (exitmprimsg > mSportsApp.ExitPreMessage
                            || exitsysmsg > mSportsApp.ExitSysMessage) {
                        int exitmsgcount = exitmprimsg + exitsysmsg;
                        // 测试 原来的new Notification(R.drawable.icon
                        // 现在改为R.drawable.notification_icon
                        Notification notification = new Notification(
                                R.drawable.notification_icon, getResources()
                                .getString(R.string.service_you_have)
                                + exitmsgcount
                                + getResources().getString(
                                R.string.service_num_new_message),
                                System.currentTimeMillis());
                        notification.defaults |= Notification.DEFAULT_SOUND;
                        notification.flags = Notification.FLAG_AUTO_CANCEL;
                        CharSequence contentTitle = getResources().getString(
                                R.string.app_name);
                        CharSequence contentText = "";
                        if (exitmprimsg > mSportsApp.ExitPreMessage
                                && exitsysmsg > mSportsApp.ExitSysMessage) {
                            contentText = contentText
                                    + getResources().getString(
                                    R.string.service_you_now_have)
                                    + exitmprimsg
                                    + getResources().getString(
                                    R.string.service_new_direct_message)
                                    + exitsysmsg
                                    + getResources().getString(
                                    R.string.service_system_message);
                        }
                        if (exitmprimsg > mSportsApp.ExitPreMessage
                                && !(exitsysmsg > mSportsApp.ExitSysMessage)) {
                            contentText = contentText
                                    + getResources().getString(
                                    R.string.service_you_now_have)
                                    + exitmprimsg
                                    + getResources()
                                    .getString(
                                            R.string.service_only_new_direct_message);
                        }
                        if (!(exitmprimsg > mSportsApp.ExitPreMessage)
                                && (exitsysmsg > mSportsApp.ExitSysMessage)) {
                            contentText = contentText
                                    + getResources().getString(
                                    R.string.service_you_now_have)
                                    + exitsysmsg
                                    + getResources().getString(
                                    R.string.service_system_message);
                        }
                        mSportsApp.ExitPreMessage = exitmprimsg;
                        mSportsApp.ExitSysMessage = exitsysmsg;
                        contentText = contentText
                                + getResources().getString(R.string.service_to_see);
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
                    int mprimsg = pSpf.getInt("primsgcount", 0)
                            + mUserDetail.getMsgCounts().getPrimsg();
                    int fuploads = mUserDetail.getActmsgs();
                    int fans = mUserDetail.getMsgCounts().getFans();
                    int visitor = mUserDetail.getMsgCounts().getSportVisitor();
                    int sysmsg = mUserDetail.getMsgCounts().getSysmsgsports();
                    int totalMsg = invite + fuploads + fans + visitor + sysmsg;
                    mUserDetail.getMsgCounts().setPrimsg(mprimsg);
                    mSportsApp.setSportUser(mUserDetail);
                    LogUtils.i("WatchService", "mUserDetail5-->" + mUserDetail);
                    if (mprimsg > 0) {
                        Handler mainHandler = mSportsApp.getMainHandler();
                        if (mainHandler != null) {
                            mainHandler.sendMessage(mainHandler.obtainMessage(
                                    ApiConstant.UPDATE_FRIENDS_MSG, mprimsg, 0));
                        }
                        Handler friendsHandler = mSportsApp.getFriendsHandler();
                        if (friendsHandler != null) {
                            friendsHandler.sendMessage(friendsHandler
                                    .obtainMessage(ApiConstant.UPDATE_FRIENDS_MSG,
                                            mprimsg, 0));
                        }
                        if (mConnect) {
                            // 如果连接上手表，则获取私信同步到手表，此时服务器私信数目清零，
                            // 所以需要把同步的私信数保存到本地，表示手机未读
                            Editor editor = pSpf.edit();
                            editor.putInt("primsgcount", mprimsg);
                            editor.commit();
                            GetGiftsThread newGiftsThread = new GetGiftsThread();
                            cachedThreadExecutor.execute(newGiftsThread);
                        }
                    }
                    if (totalMsg > 0) {
                        LocalBroadcastManager localBroadcasrManager = LocalBroadcastManager
                                .getInstance(WatchService.this);
                        Intent intent = new Intent();
                        intent.setAction(SportAction.DETAIL_ACTION);
                        intent.putExtra(SportAction.DETAIL_KEY, mUserDetail);
                        LogUtils.i(TAG, "user" + mUserDetail.toString());
                        if (isMe) { // 点击头像以外的item
                            Handler mainHandler = mSportsApp.getMainHandler();
                            if (mainHandler != null) {
                                mainHandler.sendMessage(mainHandler.obtainMessage(
                                        ApiConstant.UPDATE_MSG, totalMsg, 0));
                            }
                        }
                        Handler fHandler = mSportsApp.getfHandler();// 更新消息盒子总数
                        if (fHandler != null)
                            fHandler.sendMessage(fHandler.obtainMessage(
                                    ApiConstant.UPDATE_MSG, totalMsg, 0));
                        localBroadcasrManager.sendBroadcast(intent);
                    }
                    break;
                case PRI_ALL:
                    ArrayList<UserPrimsgAll> ol = (ArrayList<UserPrimsgAll>) msg.obj;
                    mSportsApp.getSportsDAO().insertPrivateMsgAll(
                            PrivateMessageAllTable.TABLE_NAME, ol, "unread");
                    for (UserPrimsgAll pa : ol) {
                        int uid = pa.getUid();
                        String uname = pa.getName();
                        getPrimsg(uid, uname);

                    }
                    break;
                case ADD_ITEM:
                    List<UserPrimsgOne> ml = (List<UserPrimsgOne>) msg.obj;
                    JSONObject jo = new JSONObject();
                    JSONArray ja = new JSONArray();
                    int l_size = ml.size();
                    try {
                        for (int i = 0; i < l_size; i++) {
                            String add_time = SportsUtilities
                                    .millionsToStringDate(ml.get(i).getAddTime());
                            String msgtxt = ml.get(i).getCommentText();
                            if (msgtxt.isEmpty())
                                msgtxt = getResources().getString(
                                        R.string.primsg_empty);
                            JSONObject ujo = new JSONObject();

                            ujo.put("receive_time", add_time);
                            ujo.put("msgtxt", msgtxt);
                            ujo.put("username", uname);
                            ja.put(ujo);

                        }
                        jo.put("primsg", ja);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    String content = String.valueOf(jo);
                    SyncData data = new SyncData();
                    data.putString("type", "primsg");
                    data.putString("primsg", content);
                    if (!cModule.isCreated()) {
                        Toast.makeText(mSportsApp,
                                "SyncService has not been started",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    try {
                        cModule.send(data);
                        LogUtils.i(TAG, "cModule" + data.toString());
                    } catch (SyncException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    // mBluetoothUtilNew.sendMessage(content);
                    break;
            }
        }

    };
    private SearchCallback searchCallback;
    public boolean mConnect;
    private SpecialCallback specialcallback;
    private boolean isMe = true;

    public interface SearchCallback {
        public void setSearch(boolean isdevice);
    }

    public void registerSearchCallback(SearchCallback searchCallback) {
        // TODO Auto-generated method stub
        this.searchCallback = searchCallback;

    }

    public void searchDevice(boolean isStart) {

        LogUtils.i("userconnected", "send search here");
        SyncData data = new SyncData();
        if (isStart) {
            data.putString("type", "searchDevice");
        } else {
            data.putString("type", "finishSearch");
        }

        if (!cModule.isCreated()) {
            Toast.makeText(mSportsApp, "SyncService has not been started",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            if (cModule.send(data)) {

            } else {
                searchCallback.setSearch(false);
            }

            LogUtils.i(TAG, "cModule" + data.toString());
        } catch (SyncException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // mBluetoothUtilNew.sendMessage(content);

    }

    class CustomModule extends SyncModule {

        private static final String TAG = "CustomModule";
        private boolean isCreate = false;

        public CustomModule(Context context) {
            super(TAG, context);
            // TODO Auto-generated constructor stub
        }

        @Override
        protected void onCreate() {
            // TODO Auto-generated method stub
            LogUtils.e(TAG, "is created");
            isCreate = true;
        }

        public boolean isCreated() {
            return isCreate;
        }

        // 监听手机与手表链接状态
        @Override
        protected void onConnectionStateChanged(boolean connect) {
            // TODO Auto-generated method stub
            super.onConnectionStateChanged(connect);
            mConnect = connect;
            if (connect) {
                Cursor cs = null;
                PrimsgDeleteDb db = PrimsgDeleteDb
                        .getInstance(getApplicationContext());
                try {
                    cs = db.query();
                    if (cs != null && cs.getCount() > 0 && cs.moveToFirst()) {
                        do {
                            String name = cs.getString(cs
                                    .getColumnIndexOrThrow("name"));
                            SyncData sdata = new SyncData();
                            // 删除私信同步
                            if ("".equals(name)) {
                                sdata.putString("type", "deleteallpri");
                                if (cModule.send(sdata)) {
                                    db.deleteAll();
                                }
                            } else {
                                sdata.putString("type", "deletepri");
                                sdata.putString("delete", name);
                                if (cModule.send(sdata)) {
                                    db.delete(name);
                                }
                            }
                        } while (cs.moveToNext());
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                } finally {
                    cs.close();
                    db.close();
                }
            }
            LogUtils.i(TAG, "mmconnect" + connect);
        }

        // 接收手表的数据
        @Override
        protected void onRetrive(SyncData data) {

            SportDatabase helper = SportDatabase
                    .getInstance(getApplicationContext());

            SharedPreferences sp = getSharedPreferences("sport_data", 0);
            isFirst = sp.getBoolean("isfirst", true);
            if (isFirst) {
                ContentValues values1 = new ContentValues();
                values1.put("date", Time());
                values1.put("type", 0);
                helper.insertDateType(values1);

                ContentValues values2 = new ContentValues();
                values2.put("timer", "00:00:00");
                helper.insertData(values2);

                ContentValues values3 = new ContentValues();
                values3.put("step", "0");
                values3.put("dis", "0.000");
                values3.put("cal", "0");
                helper.insertOther(values3);

                Editor ed = sp.edit();
                ed.putBoolean("isfirst", false);
                ed.commit();
            }

            String type = data.getString("type");
            LogUtils.i(TAG, "type=" + type);
            if ("steps".equals(type)) {
                int steps = data.getInt("stepvalue");
                if (mCallback != null)
                    LogUtils.i(TAG, "mCallback" + steps);
                mCallback.stepsChanged(steps);
            } else if ("search".equals(type)) {
                String search = data.getString("search");
                JSONObject jo;
                try {
                    jo = new JSONObject(search);
                    if (jo.has("startsearch")) {
                        Intent intent = new Intent(WatchService.this,
                                WarnActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getBaseContext().startActivity(intent);
                    }
                    if (jo.has("searchfinish")) {
                        WarnActivity.onFinish();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } else  if ("specialsports".equals(type)) {
                String specialsports = data.getString("specialsports");
                readSpecial(specialsports);
            } else if ("specialtype".equals(type)) {
                int stype = data.getInt("specialtype");
                String sdate = data.getString("specialdate");

                LogUtils.i("typedate", "stype:" + stype + "sdate:" + sdate);
                ContentValues values = new ContentValues();
                values.put("date", sdate);
                values.put("type", stype);
                helper.updateTypeDate(values);
                // Editor ed = sp.edit();
                // ed.putInt("type", stype);
                // ed.putString("date", sdate);
                // ed.commit();

                LogUtils.i(TAG, "stype" + stype + "," + sdate);
                if (specialcallback != null)
                    specialcallback.typeValue(stype, sdate);
            } else if ("stopsearch".equals(type)) {
                SreachDevice.stopSearch();
            } else if ("specialtime".equals(type)) {
                String timer = data.getString("specialtime");

                LogUtils.i("typedate", "timer:" + timer);
                ContentValues values = new ContentValues();
                values.put("timer", timer);
                helper.updateTime(values);
                // Editor ed = sp.edit();
                // ed.putString("timer", timer);
                // ed.commit();

                if (specialcallback != null)
                    specialcallback.timerValue(timer);
            } else if ("fixedvalue".equals(type)) {
                String dis = data.getString("dis");
                String step = data.getString("step");
                String cal = data.getString("cal");

                LogUtils.i("typedate", "dis:" + dis + "step:" + step + "cal:" + cal);
                ContentValues values = new ContentValues();
                values.put("step", step);
                values.put("dis", dis);
                values.put("cal", cal);
                helper.updateOther(values);

                // Editor ed = sp.edit();
                // ed.putString("dis", dis);
                // ed.putString("step", step);
                // ed.putString("cal", cal);
                // ed.commit();

                if (specialcallback != null) {
                    specialcallback.stepValue(step);
                    specialcallback.disValue(dis);
                    specialcallback.calValue(cal);
                }
            } else if ("specialheart".equals(type)) {
                String heart = data.getString("specialheart");
                if (specialcallback != null)
                    specialcallback.heartValue(heart);
            }

            SharedPreferences foxSportSettings = getSharedPreferences("sports"
                    + mSportsApp.getSportUser().getUid(), 0);
            Editor editor = foxSportSettings.edit();
            editor.putBoolean("isupload", false);
            editor.commit();
        }

        private void readSpecial(String specialsports) {
            // TODO Auto-generated method stub
            JSONObject jo;
            try {
                LogUtils.i("WatchService", "specialsports:" + specialsports);
                jo = new JSONObject(specialsports);
                JSONObject psjo = jo.getJSONObject("pedometerSpecial");
                int state = psjo.getInt("state");
                long start_time = psjo.getLong("start_time");
                LogUtils.i(TAG, "start_time=" + start_time);
                int spend_time = psjo.getInt("spend_time");
                int sport_type = psjo.getInt("sport_type");
                int typeId = 0;
                int typeDetailId = 0;
                switch (sport_type) {
                    case 0:
                        typeId = 0;
                        typeDetailId = 0;
                        break;
                    case 1:
                        typeId = 1;
                        typeDetailId = 0;
                        break;
                    case 2:
                        typeId = 2;
                        typeDetailId = 0;
                        break;
                    case 3:
                        typeId = 2;
                        typeDetailId = 1;
                        break;
                    case 4:
                        typeId = 2;
                        typeDetailId = 2;
                        break;
                    case 5:
                        typeId = 2;
                        typeDetailId = 3;
                        break;
                    case 6:
                        typeId = 3;
                        typeDetailId = 0;
                        break;
                    case 7:
                        typeId = 4;
                        typeDetailId = 0;
                        break;
                }
                if (sport_type > 7) {
                    typeId = sport_type - 3;
                    typeDetailId = 0;
                }

                int step = psjo.getInt("step");
                int cal = psjo.getInt("cal");
                double distance = psjo.getDouble("distance");
                // double heart = psjo.getDouble("hr");
                boolean isnotify = psjo.getBoolean("isnotify");
                double speed = distance / spend_time * 3600;
                Date startDate = new Date(start_time);
                SimpleDateFormat formatter = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss");
                String startTime = formatter.format(startDate);
                SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
                String sportDate = formatter2.format(startDate);
                String markCode = SportsApp.getInstance().getSportUser().getUid()
                        + UUIDGenerator.getUUID();// 唯一标示

                // saveAllDate2DB(sportDate);
                checkTodayTask(sportDate, startTime);

                SportSubTaskDB db = SportSubTaskDB
                        .getInstance(getApplicationContext());
                ContentValues values = new ContentValues();
                values.put(SportSubTaskDB.UID, mSportsApp.getSportUser()
                        .getUid());
                values.put(SportSubTaskDB.LIMIT, 0);
                values.put(SportSubTaskDB.SPORT_TYPE, typeId);
                values.put(SportSubTaskDB.SPORT_START_TIME, startTime);
                values.put(SportSubTaskDB.SPORT_TIME, spend_time);
                values.put(SportSubTaskDB.SPORT_STEP, step);
                values.put(SportSubTaskDB.SPORT_DISTANCE, distance);
                values.put(SportSubTaskDB.SPORT_CALORIES, cal);
                values.put(SportSubTaskDB.SPORT_ISUPLOAD, -1);
                values.put(SportSubTaskDB.SPORT_DATE, sportDate);
                values.put(SportSubTaskDB.SPORT_SWIM_TYPE, typeDetailId);
                values.put(SportSubTaskDB.SPORT_DEVICE, 1);
                values.put(SportSubTaskDB.SPORT_SPEED, speed);
                // values.put(SportSubTaskDB.SPORT_HEART_RATE, heart);
                values.put(SportSubTaskDB.SPORT_TASKID, 0);
                values.put(SportSubTaskDB.SPORT_LAT_LNG, "");
                values.put(SportSubTaskDB.SPORT_MARKCODE, markCode);// 新增唯一标识码
                int result = db.insert(values, true);

                LogUtils.i("ContentValues1", "wmh values:" + values);
                LogUtils.i("insert1---", "wmh insert id=" + result);
                // if (result > 0) {
                // saveDate2DB(sportDate);
                // }
                if (isnotify) {
                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    Notification notification = new Notification(
                            R.drawable.icon, getResources().getString(
                            R.string.service_get_message_from_watch),
                            System.currentTimeMillis());
                    notification.defaults |= Notification.DEFAULT_SOUND;
                    // n.sound = Uri.withAppendedPath(
                    // Audio.Media.INTERNAL_CONTENT_URI, "2");
                    notification.flags = Notification.FLAG_AUTO_CANCEL;
                    // if (result > 0) {
                    // saveDate2DB(sportDate);
                    // }
                    CharSequence contentTitle = getResources().getString(
                            R.string.app_name);
                    SimpleDateFormat format = new SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss");
                    String time = format.format(new Date());
                    CharSequence contentText = getResources().getString(
                            R.string.service_get_message_from_watch);

                    Intent notificationIntent = new Intent();

                    if (mSportsApp.mCurMapType == SportsApp.MAP_TYPE_GAODE) {
                        notificationIntent.setClass(getApplicationContext(),
                                state == 1 ? SportTaskDetailActivityGaode.class
                                        : HistoryAllActivity.class);
                    }
//					else {
//						notificationIntent.setClass(getApplicationContext(),
//								state == 1 ? SportTaskDetailActivity.class
//										: HistoryAllActivity.class);
//					}
                    notificationIntent.putExtra("taskid", 0);
                    notificationIntent.putExtra("startTime", startTime);
                    notificationIntent.putExtra("uid", mSportsApp
                            .getSportUser().getUid());
                    PendingIntent contentIntent = PendingIntent.getActivity(
                            getApplicationContext(), 0, notificationIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    notification.setLatestEventInfo(getApplicationContext(),
                            contentTitle, contentText, contentIntent);

                    // 用mNotificationManager的notify方法通知用户生成标题栏消息通知
                    mNotificationManager.notify(100, notification);
                }
                int uid = mSportsApp.getSportUser().getUid();
                int sportGoal = getSharedPreferences("sports" + uid, 0).getInt(
                        "editDistance", 0);
                LogUtils.v(TAG, "wmh typeId=" + typeId + " sportGoal=" + sportGoal
                        + " startTime=" + startTime + " start_time="
                        + start_time + " uid=" + uid);
                SportTaskUtil.send2QQ(getApplicationContext(), typeId,
                        sportGoal, startTime, start_time / 1000L + "", uid,
                        null);// 运动数据上传至qq健康平台
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    public void stopSearch() {
        // TODO Auto-generated method stub
        SyncData data = new SyncData();
        data.putString("type", "stopsearch");
        try {
            cModule.send(data);

        } catch (SyncException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public interface SpecialCallback {
        public void timerValue(String timer);

        public void typeValue(int type, String date);

        public void stepValue(String step);

        public void disValue(String dis);

        public void calValue(String cal);

        public void heartValue(String heart);
    }

    public void registerSpecialCallback(SpecialCallback specialcallback) {
        // TODO Auto-generated method stub
        this.specialcallback = specialcallback;
    }

    public void unregisterSpecialCallback() {
        // TODO Auto-generated method stub
        specialcallback = null;
    }

    public void setMe(boolean isMe) {
        // TODO Auto-generated method stub
        this.isMe = isMe;
    }

    // 保存运动碎片到本地数据库
    // private void saveAllDate2DB(String sportDate) {
    // final String date = sportDate;
    // new Thread(new Runnable() {
    // @Override
    // public void run() {
    // List<CurrentTimeList> list = null;
    // SportsTimeDB timeDBhelper = SportsTimeDB
    // .getInstance(getApplicationContext());
    // list = timeDBhelper.getTasksList(uid);
    // LogUtils.i("mTimeList1", "mTimeList:" + date);
    // if (list == null || list.size() == 0) {
    // try {
    // if (sessionId.equals("")) {
    // sessionId = mSportsApp.getSessionId();
    // }
    // if (uid == 0) {
    // uid = SportsApp.getInstance().getSportUser()
    // .getUid();
    // }
    // list = ApiJsonParser.getTaskTimeById(sessionId, uid);
    // LogUtils.i("mTimeList2", "mTimeList i = " + list);
    // } catch (ApiNetException e) {
    // e.printStackTrace();
    // } catch (ApiSessionOutException e) {
    // e.printStackTrace();
    // }
    //
    // if (list != null && list.size() > 0) {
    // LogUtils.i("mTimeList3", "mTimeList:" + list);
    // try {
    // for (int i = 0; i < list.size(); i++) {
    // String time = list.get(i).getCurrentTime();
    // SimpleDateFormat sdf = new SimpleDateFormat(
    // "yyyy-MM-dd");
    // long millionSeconds = sdf.parse(time).getTime();// 毫秒
    // ContentValues values = new ContentValues();
    // values.put(SportsTimeDB.UID, uid);
    // values.put(SportsTimeDB.TIME, millionSeconds);
    // values.put(SportsTimeDB.DATE, time);
    // timeDBhelper.insert(values);
    // }
    // } catch (Exception e) {
    // e.printStackTrace();
    // } finally {
    // timeDBhelper.close();
    // }
    // }
    // }
    // saveDate2DB(date);
    // }
    // }).start();
    // }

    private void checkTodayTask(String sportDate, String startTime) {
        LogUtils.e(TAG, "wmh sportDate is " + sportDate);
        LogUtils.e(TAG, "wmh strTime is " + startTime);
        if (uid == 0) {
            uid = SportsApp.getInstance().getSportUser().getUid();
        }
        strDate = sportDate;
        strTime = startTime;
        LogUtils.e(TAG, "wmh uid is " + uid);
        new Thread(new Runnable() {
            @Override
            public void run() {
                SportSubTaskDB taskDBhelper = SportSubTaskDB
                        .getInstance(getApplicationContext());
                if (taskDBhelper.getTasksList(uid, strDate) == null) {
                    List<SportContionTaskDetail> list = null;
                    try {
                        if (sessionId.equals("")) {
                            sessionId = mSportsApp.getSessionId();
                        }
                        list = ApiJsonParser.getSportsTaskByDate(sessionId,
                                strDate, uid);
                        LogUtils.i("mTimeList4", "wmh mTimeList:" + list);
                    } catch (ApiNetException e) {
                        e.printStackTrace();
                    } catch (ApiSessionOutException e) {
                        e.printStackTrace();
                    }
                    if (list != null) {
                        for (SportContionTaskDetail sportContionTaskDetail : list) {
                            LogUtils.e(TAG, "wmh startTime is "
                                    + sportContionTaskDetail.getStartTime());
                            if (!strTime.equals(sportContionTaskDetail
                                    .getStartTime())) {
                                LogUtils.i("mTimeList5", "wmh mTimeList:"
                                        + sportContionTaskDetail);
                                saveSportDB2(sportContionTaskDetail);
                            }
                        }
                    }
                }
            }
        }).start();
    }

    // 保存运动碎片到本地数据库
    private void saveSportDB2(SportContionTaskDetail detail) {
        SportSubTaskDB db = SportSubTaskDB.getInstance(this);
        try {
            ContentValues values = new ContentValues();
            values.put(SportSubTaskDB.UID, detail.getUserId());
            values.put(SportSubTaskDB.SPORT_TYPE, detail.getSports_type());
            values.put(SportSubTaskDB.SPORT_SWIM_TYPE, detail.getSwimType());
            values.put(SportSubTaskDB.SPORT_DEVICE,
                    detail.getMonitoringEquipment());
            values.put(SportSubTaskDB.SPORT_START_TIME, detail.getStartTime());
            values.put(SportSubTaskDB.SPORT_TIME, detail.getSportTime());
            values.put(SportSubTaskDB.SPORT_DISTANCE, detail.getSportDistance());
            values.put(SportSubTaskDB.SPORT_SPEED, detail.getSportVelocity());
            values.put(SportSubTaskDB.SPORT_CALORIES,
                    detail.getSprots_Calorie());
            values.put(SportSubTaskDB.SPORT_HEART_RATE, detail.getHeartRate());
            values.put(SportSubTaskDB.SPORT_LAT_LNG, detail.getLatlng());
            values.put(SportSubTaskDB.SPORT_ISUPLOAD, 1);
            values.put(SportSubTaskDB.SPORT_DATE, detail.getSportDate());
            values.put(SportSubTaskDB.SPORT_TASKID, detail.getTaskid());
            values.put(SportSubTaskDB.SPORT_STEP, detail.getStepNum());
            db.insert(values, false);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    // 保存时间到本地数据库
    // private void saveDate2DB(String sportDate) {
    // SportsTimeDB db = SportsTimeDB.getInstance(this);
    // Cursor cursor = db.query(uid, sportDate);
    // try {
    // if (!cursor.moveToFirst()) {
    // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    // long millionSeconds = sdf.parse(sportDate).getTime();// 毫秒
    // ContentValues v = new ContentValues();
    // v.put(SportsTimeDB.UID, uid);
    // v.put(SportsTimeDB.TIME, millionSeconds);
    // v.put(SportsTimeDB.DATE, sportDate);
    // db.insert(v);
    // LogUtils.v(TAG, "插入date到数据库成功 sportDate =" + sportDate);
    // }
    //
    // } catch (Exception e) {
    // e.printStackTrace();
    // } finally {
    // if (cursor != null) {
    // try {
    // cursor.close();
    // } catch (Exception e2) {
    // e2.printStackTrace();
    // }
    // }
    // db.close();
    // }
    // }

    public String Time() {
        // Calendar c = Calendar.getInstance();
        // String time = c.get(Calendar.YEAR) + "." + (c.get(Calendar.MONTH) +
        // 1)
        // + "." + c.get(Calendar.DAY_OF_MONTH);
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String time = df.format(date);
        LogUtils.i("date", "date" + date);
        return time;

    }

    public boolean isDelete(String name) {
        // TODO Auto-generated method stub
        SyncData sdata = new SyncData();
        sdata.putString("type", "deletepri");
        sdata.putString("delete", name);
        try {
            if (cModule.send(sdata))
                return false;
        } catch (SyncException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }

    public boolean isDeleteAll() {
        // TODO Auto-generated method stub
        SyncData sdata = new SyncData();
        sdata.putString("type", "deleteallpri");
        try {
            if (cModule.send(sdata))
                return false;
        } catch (SyncException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }

    public void appOnForeground() {
        // TODO Auto-generated method stub
        // if(isnotAppon){
        // isnotAppon=false;
        updateScheduleWithFixedDelay(1, 30);
        // }
    }
}
