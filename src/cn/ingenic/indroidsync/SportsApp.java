package cn.ingenic.indroidsync;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.fox.exercise.MainFragmentActivity;
import com.fox.exercise.R;
import com.fox.exercise.SportsExceptionHandler;
import com.fox.exercise.SportsUtilities;
import com.fox.exercise.StateActivity;
import com.fox.exercise.api.ApiBack;
import com.fox.exercise.api.ApiConstant;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.api.MessageService;
import com.fox.exercise.api.WatchService;
import com.fox.exercise.api.entity.FindMore;
import com.fox.exercise.api.entity.ItemsCate;
import com.fox.exercise.api.entity.UserDetail;
import com.fox.exercise.bitmap.util.ImageCache;
import com.fox.exercise.bitmap.util.ImageCache.ImageCacheParams;
import com.fox.exercise.bitmap.util.ImageFetcher;
import com.fox.exercise.bitmap.util.ImageResizer;
import com.fox.exercise.bitmap.util.Utils;
import com.fox.exercise.commentuser;
import com.fox.exercise.dao.SportsDAO;
import com.fox.exercise.login.LoginActivity;
import com.fox.exercise.login.SportMain;
import com.fox.exercise.map.SportsTaskUploader;
import com.fox.exercise.newversion.entity.TrainCount;
import com.fox.exercise.publish.SendMsgDetail;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.ingenic.indroidsync.camera.CameraModule;
import cn.ingenic.indroidsync.contactsms.contacts.ContactModule;
import cn.ingenic.indroidsync.contactsms.manager.ManagerModule;
import cn.ingenic.indroidsync.contactsms.sms2.SmsModule2;
import cn.ingenic.indroidsync.devicemanager.DeviceModule;
import cn.ingenic.indroidsync.phone.PhoneModule;
import cn.ingenic.indroidsync.updater.UpdaterModule;
import cn.ingenic.indroidsync.utils.DeviceUuidFactory;
import cn.ingenic.indroidsync.utils.internal.XmlUtils;


public class SportsApp extends Application implements
        Enviroment.EnviromentCallback {
    private static final String TAG = "LocationInfoManager";
    // 登录的标识，true代表正常登录 false代表非正常登录
    public boolean LoginOption;
    // public boolean isFirst;
    private MainFragmentActivity mainFragmentActivity;
    // private MenuActivity menuActivity;
    public FindMore mFindMore;
    // 有网登录的标识
    public boolean LoginNet;
    private SportMain sportMain = null;
    //    private LocationManagerProxy mLocationManagerProxy;
    // 点击忘记密码后的时间
    public int forgetPwdTime;

    public int getForgetPwdTime() {
        return forgetPwdTime;
    }

    public void setForgetPwdTime(int forgetPwdTime) {
        this.forgetPwdTime = forgetPwdTime;
    }

    private LoginActivity loginActivity = null;

    private SportsExceptionHandler mExceptionHandler = null;

    // 信息存储
    public static Message eMsg = null;

    // 是否最高权限登陆者（默认：NO）
    public static boolean mIsAdmin = false;

    public int config = 1;

    // 默认登陆是失败状态
    private boolean isLogin = false;

    private boolean isFirst=false;//表示第一次加载
    public boolean isRunning=false;//表示正在运动

    private UserDetail mSportUser = new UserDetail();
    private SendMsgDetail mSendMsgDetail = null;
    private String theFirstName = null;
    private String userName = null;
    private int personalTomUid;
    public int getPersonalTomUid() {
        return personalTomUid;
    }
    public void setPersonalTomUid(int personalTomUid) {
        this.personalTomUid = personalTomUid;
    }
    public String gettheFirstName() {
        return theFirstName;
    }
    public void settheFirstName(String theFirstName) {
        this.theFirstName = theFirstName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public SendMsgDetail getmSendMsgDetail() {
        return mSendMsgDetail;
    }

    public void setmSendMsgDetail(SendMsgDetail mSendMsgDetail) {
        this.mSendMsgDetail = mSendMsgDetail;
    }

    private static Context mContext;

    private static SportsApp mInstance;

    public static final int DEFAULT_DISK_CACHE_SIZE = 1024 * 1024 * 5;

    public static int ScreenWidth = 0;
    public static int ScreenHeight = 0;
    private String sessionId;
    public final int managerId = 13016;
    private Bitmap previewBitmap = null;
    private static SportsDAO sportsDAO = null;
    // private SlimDetails slimDetails = null;
    public static final String DEFAULT_ICON = "http://kupao.mobifox.cn";
    private List<ItemsCate> itemCates = new ArrayList<ItemsCate>();

    public static String DB_PATH = null;

    private StateActivity stateActivity = null;

    public ActivityManager activityManager;

    private String packageName;


    private Handler mHandler;

    public Handler getNotifiRefreshHandler() {
        return notifiRefreshHandler;
    }

    public void setNotifiRefreshHandler(Handler notifiRefreshHandler) {
        this.notifiRefreshHandler = notifiRefreshHandler;
    }

    private Handler notifiRefreshHandler;

    private Handler fHandler;

    private Handler mainHandler;

    private Handler myHandler;
    private Handler findHandler;
    private Handler goodfriendsHandler;// 好友列表动态
    private Handler personalFindHandler;// 个人主页面
    private Handler mSportsCircleMainhandler;// 运动圈主页面
    private Handler mBaseFragmentHandler;// 基类fragment handler
    private Handler mYePaoHandler;// 夜跑的handler
    private Handler mSettingHandler;//运动中语音播报handler
    private TrainCount mCount = new TrainCount();// 训练数据
    private commentuser commentuser;
    private String findId;
    private String textString;
    private String toName;
    private String toId;
    private String wav;//语音地址
    private String wavtime;//语音时长,以秒为单位
    //同步个人中心的动态关注和粉丝的数量
    private int guanzhu_personalceter;
    private int dongtai_personalceter = 0;
    private int fensi_personalceter;
    private int sports_Show=0;
    private int sport_below_main;
    private int sport_below_main_guanzhu;

    public int getSport_below_main_guanzhu() {
        return sport_below_main_guanzhu;
    }

    public void setSport_below_main_guanzhu(int sport_below_main_guanzhu) {
        this.sport_below_main_guanzhu = sport_below_main_guanzhu;
    }

    public int getSport_below_main() {
        return sport_below_main;
    }

    public void setSport_below_main(int sport_below_main) {
        this.sport_below_main = sport_below_main;
    }

    public int getSports_Show() {
        return sports_Show;
    }

    public void setSports_Show(int sports_Show) {
        this.sports_Show = sports_Show;
    }

    public boolean isDownloading() {
        return isDownloading;
    }

    public void setDownloading(boolean downloading) {
        isDownloading = downloading;
    }

    private boolean isDownloading;

    public int getFensi_personalceter() {
        return fensi_personalceter;
    }

    public void setFensi_personalceter(int fensi_personalceter) {
        this.fensi_personalceter = fensi_personalceter;
    }

    public int getDongtai_personalceter() {
        return dongtai_personalceter;
    }

    public void setDongtai_personalceter(int dongtai_personalceter) {
        this.dongtai_personalceter = dongtai_personalceter;
    }

    public int getGuanzhu_personalceter() {
        return guanzhu_personalceter;
    }

    public void setGuanzhu_personalceter(int guanzhu_personalceter) {
        this.guanzhu_personalceter = guanzhu_personalceter;
    }

    public String getWav() {
        return wav;
    }

    public void setWav(String wav) {
        this.wav = wav;
    }

    public String getWavtime() {
        return wavtime;
    }

    public void setWavtime(String wavtime) {
        this.wavtime = wavtime;
    }

    public String getFindId() {
        return findId;
    }

    public void setFindId(String findId) {
        this.findId = findId;
    }

    public String getTextString() {
        return textString;
    }

    public void setTextString(String textString) {
        this.textString = textString;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public TrainCount getmCount() {
        return mCount;
    }

    public void setmCount(TrainCount mCount) {
        this.mCount = mCount;
    }

    private Handler mShopHandler;// 金币商城的handler

    public Handler getmShopHandler() {
        return mShopHandler;
    }

    public void setmShopHandler(Handler mShopHandler) {
        this.mShopHandler = mShopHandler;
    }

    public Handler getmYePaoHandler() {
        return mYePaoHandler;
    }

    public void setmYePaoHandler(Handler mYePaoHandler) {
        this.mYePaoHandler = mYePaoHandler;
    }

    public Handler getmBaseFragmentHandler() {
        return mBaseFragmentHandler;
    }

    public void setmBaseFragmentHandler(Handler mBaseFragmentHandler) {
        this.mBaseFragmentHandler = mBaseFragmentHandler;
    }

    public Handler getmSportsCircleMainhandler() {
        return mSportsCircleMainhandler;
    }

    public void setmSportsCircleMainhandler(Handler mSportsCircleMainhandler) {
        this.mSportsCircleMainhandler = mSportsCircleMainhandler;
    }

    public Handler getPersonalFindHandler() {
        return personalFindHandler;
    }

    public void setPersonalFindHandler(Handler personalFindHandler) {
        this.personalFindHandler = personalFindHandler;
    }

    public Handler getGoodfriendsHandler() {
        return goodfriendsHandler;
    }

    public void setGoodfriendsHandler(Handler goodfriendsHandler) {
        this.goodfriendsHandler = goodfriendsHandler;
    }

    public Handler getmSettingHandler() {
        return mSettingHandler;
    }

    public void setmSettingHandler(Handler mSettingHandler) {
        this.mSettingHandler = mSettingHandler;
    }


    private static final String CITY_DB_NAME = "city.db";

    public static final int MAP_TYPE_BAIDU = 1;
    public static final int MAP_TYPE_GAODE = 0;
    // public int mCurMapType = MAP_TYPE_BAIDU;
    public int mCurMapType = MAP_TYPE_GAODE;
    public String curCity;

    // 注册广播
    private BroadcastReceiver mNetworkStateIntentReceiver;
    // 默认没有网络任务
    // public boolean mNoNetwork = false;
    // 网络是否链接工作
    public boolean mIsNetWork = true;
    public Dialog alertDialog = null;

    public boolean isHomeKey = false;
    // 好友界面的输入框
    public EditText mFriends_editext;

    private List<Activity> activitys = new ArrayList<Activity>();

    public EditText getmFriends_editext() {
        return mFriends_editext;
    }

    public void addActivity(Activity activity) {
        if (activitys != null && activitys.size() > 0) {
            if (!activitys.contains(activity)) {
                activitys.add(activity);
            }
        } else {
            activitys.add(activity);
        }
    }

    public void removeActivity(Activity activity) {
        if (activitys != null && activitys.size() > 0) {
            if (activitys.contains(activity)) {
                activitys.remove(activity);
            }
        }
    }

    public void removeAllActivity() {
        if (activitys != null && activitys.size() > 0) {
            for (Activity activity : activitys) {
                activity.finish();
            }
        }
        if (activitys != null)
            activitys.clear();
    }

    public List<Activity> getActivitys() {
        return activitys;
    }

    public void setmFriends_editext(EditText mFriends_editext) {
        this.mFriends_editext = mFriends_editext;
    }

    public LoginActivity getLoginActivity() {
        return loginActivity;
    }

    public void setLoginActivity(LoginActivity loginActivity) {
        this.loginActivity = loginActivity;
    }

    public void clearSportUser() {
        mSportUser = null;
    }

    public UserDetail getSportUser() {
        return mSportUser;
    }

    public void setSportUser(UserDetail SportUser) {
        this.mSportUser = SportUser;
        if(!isFirst){
            isFirst=true;
            if(sessionId!=null&&!"".equals(sessionId)){
                if (!LoginNet) {
                    // 如果现在是无网登录状态
                } else {
                    // 网络连接，并且是已经登录之后，启动检查程序
                    if (sessionId != null) {
                        try {
                            SportsTaskUploader.getInstance()
                                    .startBackgroundUpload(
                                            getApplicationContext(),
                                            mSportUser.getUid(), sessionId);
                        }catch (Exception e){

                        }
                    }
                }
            }
        }

    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public StateActivity getStateActivity() {
        return stateActivity;
    }

    public void setStateActivity(StateActivity stateActivity) {
        this.stateActivity = stateActivity;
    }

    public static Context getContext() {
        return mContext;
    }

    public MainFragmentActivity getMainFragmentActivity() {
        return mainFragmentActivity;
    }

    public void setMainFragmentActivity(
            MainFragmentActivity mainFragmentActivity) {
        this.mainFragmentActivity = mainFragmentActivity;
    }

	/*
     * public MenuActivity getMenuActivity() { return menuActivity; }
	 *
	 * public void setMenuActivity( MenuActivity menuActivity) {
	 * this.menuActivity = menuActivity; }
	 */

    public SportMain getSportMain() {
        return sportMain;
    }

    public void setSportMain(SportMain sportMain) {
        this.sportMain = sportMain;
    }

    // 记录退出时服务器上的私信数和系统消息数
    public int ExitPreMessage;
    public int ExitSysMessage;
    // 定义通知栏
    public NotificationManager mNotificationManager;
    private Handler friendsHandler;
    private Handler mapHandler;
    public Boolean isStartY = false;

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化ImageLoader
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
        // 定义收集BUG信息
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
        // 测试 注意上线时要一定要去掉
//        OtCrashHandler otCrashHandler = OtCrashHandler.getInstance();
//        otCrashHandler.init(this);

        LoginOption = true;
        LoginNet = true;
        ExitPreMessage = 0;
        ExitSysMessage = 0;
        isHomeKey = false;
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mInstance = this;
        mContext = getApplicationContext();
        SDKInitializer.initialize(mContext);
        DeviceUuidFactory.initialize(mContext);
        activityManager = (ActivityManager) this
                .getSystemService(Context.ACTIVITY_SERVICE);
        packageName = this.getPackageName();
        isConnectNetwork();
        DB_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath()
                + File.separator + this.getPackageName() + File.separator
                + "databases";
        config = getResources().getInteger(R.integer.config_withsync);
        if (config == 1) {
            if (ApiConstant.isservice) {
                Intent newIntent = new Intent(mContext, WatchService.class);
                mContext.startService(newIntent);
            }

            if (LogTag.V) {
            }
            Enviroment.init(this);
            DefaultSyncManager manager = DefaultSyncManager.init(this);

            SystemModule systemModule = new SystemModule();
            if (manager.registModule(systemModule)) {
            }
            ManagerModule mm = new ManagerModule();
            if (manager.registModule(mm)) {
            }

            SmsModule2 sm2 = new SmsModule2();
            if (manager.registModule(sm2)) {
            }

            UpdaterModule um = new UpdaterModule();
            if (manager.registModule(um)) {
            }

            PhoneModule pm = new PhoneModule();
            if (manager.registModule(pm)) {
            }

            CameraModule cm = new CameraModule();
            if (manager.registModule(cm)) {
            }

            ContactModule contactM = new ContactModule();
            if (manager.registModule(contactM)) {
            }

            DeviceModule dm = DeviceModule.getInstance();
            if (manager.registModule(dm)) {
            }

            XmlResourceParser parser = getResources().getXml(R.xml.modules);
            try {
                XmlUtils.beginDocument(parser, "modules");
                loadModules(parser);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                parser.close();
            }
        } else {
            mContext.startService(new Intent(mContext, MessageService.class));
        }

        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... strings) {
                try {
                    // final long minUploadInterval = 8 * 3600 * 1000; // at
                    // least 原来8个小时
                    final long minUploadInterval = 10 * 60 * 1000; // at least
                    // 现在10分钟
                    // 8 hours
                    // before
                    // last
                    // request
                    String serial = strings[0];
                    String model = strings[1];
                    SharedPreferences spf = getSharedPreferences(
                            "openequipmentTask", 0);
                    long last = spf.getLong("last", 0);
                    long now = System.currentTimeMillis();

                    if (now - last >= minUploadInterval) {
                        ApiJsonParser.openequipment(serial, model);
                        spf.edit().putLong("last", now).apply();
                    } else {
                    }
                } catch (Exception e) {
                    return "error" + e.getMessage();
                }
                return "";
            }
        }.execute(DeviceUuidFactory.getDeviceSerial(), "Android");
    }

    /**
     * 对网络连接状态进行判断
     */
    public boolean isOpenNetwork() {
        return mIsNetWork;
    }

    // 判断网络连接
    private ConnectivityManager mConnectivityManager;
    private NetworkInfo netInfo;

    /*
     * 判断网络连接
     */
    public void isConnectNetwork() {
        IntentFilter mNetworkStateChangedFilter = new IntentFilter();
        // 监听网络状态
        mNetworkStateChangedFilter
                .addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        mNetworkStateIntentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(
                        ConnectivityManager.CONNECTIVITY_ACTION)) {

                    mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    netInfo = mConnectivityManager.getActiveNetworkInfo();
                    // 判断网络是否不工作
                    // mNoNetwork = intent.getBooleanExtra(
                    // ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
                    if (netInfo != null && netInfo.isAvailable()) {
                        // if (Tools.pingIp("https://www.baidu.com")) {
                        mIsNetWork = true;
                        if (!LoginNet) {
                            // 如果现在是无网登录状态
                            (new HaveNetLoginTask()).execute();
                        } else {
                            // 网络连接，并且是已经登录之后，启动检查程序
                            if (sessionId != null) {
                                if (!isRunning) {
                                    SportsTaskUploader.getInstance()
                                            .startBackgroundUpload(
                                                    getApplicationContext(),
                                                    mSportUser.getUid(), sessionId);
                                }
                            }
                        }

                    } else {
                        mIsNetWork = false;
                    }
                    Log.i("mIsNetWork", "mIsNetWork2" + mIsNetWork);
                }
            }
        };
        registerReceiver(mNetworkStateIntentReceiver,
                mNetworkStateChangedFilter);
    }

    // 从无网登录状态到有网登录状态
    class HaveNetLoginTask extends AsyncTask<Void, Void, ApiBack> {

        @Override
        protected ApiBack doInBackground(Void... arg0) {
            SharedPreferences sps = getSharedPreferences("user_login_info",
                    Context.MODE_PRIVATE);
            SharedPreferences sp_umeng = getSharedPreferences("UmengDeviceToken", Activity.MODE_PRIVATE);
            ApiBack back = null;
            try {
                back = ApiJsonParser.login(sps.getString("account", ""),
                        sps.getString("pwd", ""), 1, sp_umeng.getString("device_token", ""));
                return back;
            } catch (ApiNetException e) {
                e.printStackTrace();
            }
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        protected void onPostExecute(ApiBack back) {
            // TODO Auto-generated method stub
            if (back == null) {
                return;
            }
            if (back != null && back.getFlag() != -1 && back.getFlag() != -11 && !"".equals(back.getMsg()) && back.getMsg() != null) {
                LoginNet = true;
                setSessionId(back.getMsg().substring(7));
                setLogin(true);
            }
            new AsyncTask<Void, Void, UserDetail>() {
                @Override
                protected UserDetail doInBackground(Void... arg0) {
                    // TODO Auto-generated method stub
                    UserDetail mUserDetail;
                    try {
                        if (!"".equals(getSessionId()) && getSessionId() != null) {
                            mUserDetail = ApiJsonParser.refreshRank(getSessionId());
                            return mUserDetail;
                        }
                    } catch (ApiNetException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (ApiSessionOutException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(UserDetail result) {
                    // TODO Auto-generated method stub
                    super.onPostExecute(result);
                    if (result != null) {
                        setSportUser(result);
                    }
                }

            }.execute();
        }
    }

    ;

    public static SportsApp getInstance() {
        return mInstance;

    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public List<ItemsCate> getItemCates() {
        return itemCates;
    }

    public void setItemCates(List<ItemsCate> itemCates) {
        this.itemCates = itemCates;
    }

    public SportsExceptionHandler getmExceptionHandler() {
        if (mExceptionHandler == null) {
            mExceptionHandler = new SportsExceptionHandler();
        }
        return mExceptionHandler;
    }

    public Bitmap getPreviewBitmap() {
        return previewBitmap;
    }

    public void setPreviewBitmap(Bitmap previewBitmap) {
        this.previewBitmap = previewBitmap;
    }

    public ImageResizer getImageWorker(Context mContext, int reqWidth,
                                       int reqHeight) {
        if (reqWidth <= 0 || reqHeight <= 0) {
            reqWidth = 480;
            reqHeight = 640;
        }
        boolean hasSDCard = SportsUtilities.hasStorage();
        File externalDir = null;
        if (hasSDCard) {
            File path = Utils.getExternalCacheDir(mContext);
            if (path != null)
                externalDir = new File(path.getPath());
        }

        SportsUtilities.DOWNLOAD_BASE_PATH = hasSDCard
                && (externalDir != null)
                && !(Utils.getUsableSpace(externalDir) < DEFAULT_DISK_CACHE_SIZE) ? Utils
                .getExternalCacheDir(mContext).getPath() : mContext
                .getCacheDir().getPath();
        SportsUtilities.DOWNLOAD_SAVE_PATH = SportsUtilities.DOWNLOAD_BASE_PATH
                + "/" + ".download/";
        SportsUtilities.RECYCLE_PATH = SportsUtilities.DOWNLOAD_BASE_PATH + "/"
                + ".recycle/";
        SportsUtilities.DOWNLOAD_PHOTOFRAMES_PATH = SportsUtilities.DOWNLOAD_BASE_PATH
                + "/" + ".photoframes/";
        ImageResizer mReqImageWorker = new ImageFetcher(mContext, reqWidth,
                reqHeight, SportsUtilities.DOWNLOAD_SAVE_PATH);
        ImageCacheParams cacheParams = new ImageCacheParams(
                SportsUtilities.DOWNLOAD_SAVE_PATH);
        mReqImageWorker.setImageCache(new ImageCache(mContext, cacheParams));
        return mReqImageWorker;
    }

    public SportsDAO getSportsDAO() {
        if (sportsDAO == null) {
            sportsDAO = new SportsDAO(mContext);
        }
        return sportsDAO;
    }




    private void loadModules(XmlResourceParser parser) {
        if (parser != null) {
            while (true) {
                try {
                    XmlUtils.nextElement(parser);
                    if (!"module".equals(parser.getName())) {
                        break;
                    }
                    registModule(parser);
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void registModule(XmlResourceParser parser) {
        String className = parser.getAttributeValue(null, "class");
        try {
            Class c = Class.forName(className);
            Constructor constructor = c.getConstructor(Context.class);
            constructor.newInstance(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Enviroment createEnviroment() {
        return new PhoneEnviroment(this);
    }

    public boolean isAppOnForeground() {
        List<RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;
        for (RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    public boolean isServiceRunning(Context mContext, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(30);
        if (!(serviceList.size() > 0)) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    public void StartMessageTask() {
        (new MessageTask()).execute();
    }

    class MessageTask extends AsyncTask<Integer, Integer, UserDetail> {

        @Override
        protected UserDetail doInBackground(Integer... arg0) {
            // TODO Auto-generated method stub
            UserDetail mUserDetail = null;
            try {
                if (!"".equals(getSessionId()) && getSessionId() != null) {
                    mUserDetail = ApiJsonParser.refreshRank(getSessionId());
                }

            } catch (ApiNetException e) {
                e.printStackTrace();
            } catch (ApiSessionOutException e) {
                e.printStackTrace();
            }
            return mUserDetail;
        }

        @Override
        protected void onPostExecute(UserDetail result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (result != null) {
                SharedPreferences spf = getSharedPreferences("sports", 0);
                ExitPreMessage = spf.getInt("primsgcount", 0)
                        + result.getMsgCounts().getPrimsg();
                ExitSysMessage = result.getMsgCounts().getSysmsgsports();
            }
        }

    }

    public Handler getmHandler() {
        return mHandler;
    }

    public void setmHandler(Handler mHandler) {
        this.mHandler = mHandler;
    }

    public Handler getmofHandler() {
        return mHandler;
    }

    public void setmofHandler(Handler mHandler) {
        this.mHandler = mHandler;
    }

    public Handler getmyHandler() {
        return myHandler;
    }

    public Handler getFindHandler() {
        return findHandler;
    }

    public void setFindHandler(Handler findHandler) {
        this.findHandler = findHandler;
    }

    public void setmyHandler(Handler myHandler) {
        this.myHandler = myHandler;
    }

    public void setfHandler(Handler fHandler) {
        // TODO Auto-generated method stub
        this.fHandler = fHandler;
    }

    public Handler getfHandler() {
        return fHandler;
    }

    public Handler getMainHandler() {
        return mainHandler;
    }

    public void setMainHandler(Handler mainHandler) {
        this.mainHandler = mainHandler;
    }

    public void setFriendsHandler(Handler friendsHandler) {
        // TODO Auto-generated method stub
        this.friendsHandler = friendsHandler;
    }

    public Handler getFriendsHandler() {
        return friendsHandler;
    }

    public Handler getMapHandler() {
        // TODO Auto-generated method stub
        return mapHandler;
    }

    public void setMapHandler(Handler mapHandler) {
        // TODO Auto-generated method stub
        this.mapHandler = mapHandler;
    }

    public void TyrLoginAction(final Context context, String title,
                               String message) {
        if (alertDialog == null) {
            initTryLoginDialog(context, title, message);
        }
        if (alertDialog.isShowing()) {
            return;
        } else {
            alertDialog.show();
        }

    }

    private void initTryLoginDialog(final Context context, String title,
                                    String message) {
        // TODO Auto-generated method stub
        alertDialog = new Dialog(context, R.style.sports_dialog);
        alertDialog.setCanceledOnTouchOutside(false);
        LayoutInflater mInflater = ((Activity) context).getLayoutInflater();
        View v = mInflater.inflate(R.layout.sports_dialog, null);
        ((TextView) v.findViewById(R.id.message)).setText(message);
        ((TextView) v.findViewById(R.id.title)).setText(title);
        Button button = (Button) v.findViewById(R.id.bt_ok);
        button.setText(getResources().getString(R.string.login_now));
        v.findViewById(R.id.bt_ok).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        // (sessionId+"");//登录时用于统计
                        LoginOption = false;
                        if (mainHandler != null) {
                            mainHandler.sendEmptyMessage(MainFragmentActivity.UNLOGINRESULT);
                        }
                        Intent intent = new Intent(context, LoginActivity.class);
                        intent.putExtra("isfirst_try", false);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("notLogin", true);
                        context.startActivity(intent);
                        System.exit(0);
                        alertDialog.dismiss();

                    }
                });
        v.findViewById(R.id.bt_cancel).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        alertDialog.dismiss();
                    }
                });
        v.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
        alertDialog.setCancelable(true);
        alertDialog.setContentView(v);
        alertDialog.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface arg0) {
                // TODO Auto-generated method stub
                alertDialog = null;
            }
        });
    }

    // 提示用户没有网，不能进行当前操作
    public void NoNetLogin(Context context) {
        Toast.makeText(context,
                getResources().getString(R.string.sports_no_net_play),
                Toast.LENGTH_SHORT).show();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 获取当前日期
     */

    public String getCurrentDate() {
        // TODO Auto-generated method stub
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }

    // 为了离线的方法
    public void setDataToLocal(UserDetail detail) {
        SharedPreferences sps = getSharedPreferences("user_login_info",
                Context.MODE_PRIVATE);
        Editor eds = sps.edit();
        eds.putString("sessionid", getSessionId());
        eds.putInt("uid", detail.getUid());
        eds.putString("uname", detail.getUname());
        eds.putString("uimg", detail.getUimg());
        eds.putInt("counts", detail.getCounts());
        eds.putInt("totalrank", detail.getTotalRank());
        eds.putString("sex", detail.getSex());
        eds.putString("birthday", detail.getBirthday());
        eds.putInt("height", detail.getHeight());
        eds.putInt("weight", detail.getWeight());
        eds.putInt("actmsgs", detail.getFcount());
        eds.putInt("totallikes", detail.getTotalLikes());
        eds.putString("phoneno", detail.getPhoneno());
        eds.putInt("fancounts", detail.getFanCounts());
        eds.putInt("followcounts", detail.getFollowCounts());
        eds.putString("email", detail.getEmail());
        eds.putInt("giftcounts", detail.getGiftCounts());
        eds.putInt("golds", detail.getGolds());
        eds.putInt("coins", detail.getCoins());
        eds.putString("authpic", detail.getAuthPic());
        eds.putInt("authstatus", detail.getAuthStatus());
        eds.putInt("kindrank", detail.getKindrank());
        eds.putInt("charmsrank", detail.getCharmsrank());
        eds.putInt("actmsgs", detail.getActmsgs());
        eds.putString("findimg", detail.getFindimg());
        eds.commit();
    }

    public void getLocalData() {
        SharedPreferences sps = getSharedPreferences("user_login_info",
                Context.MODE_PRIVATE);
        UserDetail userDetail = new UserDetail();
        userDetail.setUid(sps.getInt("uid", 0));
        userDetail.setUname(sps.getString("uname", ""));
        userDetail.setUimg(sps.getString("uimg", ""));
        userDetail.setCounts(sps.getInt("counts", 0));
        userDetail.setTotalRank(sps.getInt("totalrank", 0));
        userDetail.setSex(sps.getString("sex", ""));
        userDetail.setBirthday(sps.getString("birthday", ""));
        userDetail.setHeight(sps.getInt("height", 0));
        userDetail.setWeight(sps.getInt("weight", 0));
        userDetail.setFcount(sps.getInt("actmsgs", 0));
        userDetail.setTotalLikes(sps.getInt("totallikes", 0));
        userDetail.setPhoneno(sps.getString("phoneno", ""));
        userDetail.setFanCounts(sps.getInt("fancouts", 0));
        userDetail.setFollowCounts(sps.getInt("followcounts", 0));
        userDetail.setEmail(sps.getString("email", ""));
        userDetail.setGiftCounts(sps.getInt("giftcounts", 0));
        userDetail.setGolds(sps.getInt("golds", 0));
        userDetail.setCoins(sps.getInt("coins", 0));
        userDetail.setAuthPic(sps.getString("authpic", ""));
        userDetail.setAuthStatus(sps.getInt("authstatus", 0));
        userDetail.setKindrank(sps.getInt("kindrank", 0));
        userDetail.setCharmsrank(sps.getInt("charmsrank", 0));
        userDetail.setActmsgs(sps.getInt("actmsgs", 0));
        userDetail.setFindimg(sps.getString("findimg", ""));
        setSportUser(userDetail);
    }


}
