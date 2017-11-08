package com.fox.exercise;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.ingenic.indroidsync.SportsApp;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.api.entity.ImageDetail;
import com.fox.exercise.api.entity.UserDetail;
import com.fox.exercise.db.SportSubTaskDB;
import com.fox.exercise.http.FunctionStatic;
import com.fox.exercise.login.BiaoZhuScrollLayout.OnViewChangeListener;
import com.fox.exercise.login.LocationInfo;
import com.fox.exercise.login.SportMain;
import com.fox.exercise.pedometer.RunningUtils;
import com.fox.exercise.pedometer.SportContionTaskDetail;
import com.fox.exercise.pedometer.WalkingUtils;
import com.fox.exercise.util.SportTaskUtil;
import com.fox.exercise.view.DrawNewImageView;
import com.umeng.analytics.MobclickAgent;

public class StateActivity extends AbstractBaseFragment implements
        OnClickListener, OnDismissListener, OnViewChangeListener,
        OnTouchListener {
    // AMapLocalWeatherListener
    private static final String TAG = "StateActivity";
    public static final String DEVICE_NAME = "device_name";

    public static final int SPORT_GOAL = 0;
    public static final int SPORT_STATE = 1;
    public static final int SPORT_DEVICE = 2;
    public static final int SPORT_MAP = 3;
    public static final int SPORT_DEL = 4;

    private long preTime = 0;

    // private ImageView sportType;
    // 默认当天没有记录
    public static boolean mHasPlan = false;
    public static boolean mIn = false;
    SportsApp mSportsApp;

    //	private ImageWorkManager mImageWorkerMan_Icon;
//	private ImageResizer mImageWorker_Icon;
    List<ImageDetail> mImageDetails = new ArrayList<ImageDetail>();
    // 获取当天的日期
    String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    String[] states;

    private ExecutorService cachedThreadExecutor;
//	private SQLiteDatabase db;

    public static LocationClient mClient = null;
    public static MyLocationListenner myListener;

    private String mCitycode = null;
    private TextView mWeek;
    private TextView mAear;
    private PopupWindow window;
    //	private LinearLayout mview;
    private RelativeLayout mPopMenuBack;
    // private Button hDis;
    // private Button hCal;
    private int mUid;
    // private TextView nameText;
    // private RoundedImage userImage;
    private SharedPreferences sharedPreferences;
    private SportSubTaskDB mdb;
    private SharedPreferences spf;
    // private static Button inviteText;
//	private static final String DB_PATH = "/data"
//			+ Environment.getDataDirectory().getAbsolutePath() + File.separator
//			+ SportsApp.getInstance().getPackageName() + File.separator
//			+ "databases";

    //	private static final String CITY_DB_NAME = "city.db";
    protected static final int READSTEPS_MSG = 1;
    private static final int SPORTS_TYPE = 0x2;
    public static final int LAST_SPORTS = 0x3;
    public static final int UPDATE_HEADER = 0x4;
    private static final int SPORT_INVITE = 0x5;
    public static final int UPDATE_INVITE = 0x6;
    public static final int UPDATE_TRYTOLOGIN = 0x9;
    public static final int UPDATE_ANIMATION = 0x10;
    public static final int STOP_ANIMATION = 0x11;
    public static final int UPDATE_ANIMATION_TODAY = 0x12;
    public static final int STOP_ANIMATION_TODAY = 0x13;
    private static final int bind_device_text_view = 111;

    public static final int ANIMATION_TOTAL_DIS = 1;
    public static final int ANIMATION_TODAY_DIS = 2;
    // private LinearLayout bind_devices;
    // private ImageView sport_bind_device;
//	private Dialog alertDialog;

    private TextView dis_zheng;
    private TextView dis_xiao_1;
    private TextView dis_xiao_2;
    private TextView sport_cishu;
    private TextView sport_sudu;
    private TextView sport_reliang;
    private TextView bind_device_text;
    private ImageView image_biao_yuan;
    private ImageView image_zhu_yuan;

    AnimationSet animationSet1;
    AnimationSet animationSet2;
    TranslateAnimation translateAnimation1;
    TranslateAnimation translateAnimation2;

    AnimationSet animationSetDis1;
    AnimationSet animationSetDis2;
    TranslateAnimation translateAnimationDis1;
    TranslateAnimation translateAnimationDis2;

    AnimationSet animationSetCal1;
    AnimationSet animationSetCal2;
    TranslateAnimation translateAnimationCal1;
    TranslateAnimation translateAnimationCal2;

    private int count = 0;
    private int mTimes = 0;
    private Boolean history_see = false;
    private SportSubTaskDB histDB;

    private static FragmentActivity context;

    private String cityname;

    @Override
    public void beforeInitView() {
        title = getActivity().getResources()
                .getString(R.string.sport_condition);
    }

    @Override
    public void setViewStatus() {
        setContentView(R.layout.sportcondition);
        Log.v(TAG, "wmh setViewStatus");
        type = 0;
        context = getActivity();
        mSportsApp = (SportsApp) getActivity().getApplication();
        mSportsApp.addActivity(getActivity());
        mUid = SportsApp.getInstance().getSportUser().getUid();
        sharedPreferences = getActivity().getSharedPreferences("sports" + mUid,
                0);
        spf = getActivity().getSharedPreferences("sports", 0);
        mdb = SportSubTaskDB.getInstance(getActivity());
        mIn = true;
        if ((mSportsApp.getSportUser() == null
                || mSportsApp.getSportUser().equals("") || mSportsApp
                .getSportUser().getUid() == 0) && mSportsApp.LoginOption) {
            Log.v(TAG, "onActivityCreated         no doing");

        } else {
            cachedThreadExecutor = Executors.newCachedThreadPool();
            initView();

            SportsApp.getInstance().setmHandler(mHandler);
            SportsApp.getInstance().setStateActivity(this);
        }
        if (mClient == null) {
            mClient = new LocationClient(getActivity());
        }
        registerLocation();
    }

    @Override
    public void onPageResume() {
        if (mSportsApp.getSportUser() != null) {
            mUid = mSportsApp.getSportUser().getUid();
            sharedPreferences = getActivity().getSharedPreferences(
                    "sports" + mUid, 0);
        }
        Log.v(TAG, "wmh onPageResume mUid is " + mUid);
        allSportsData();
        biaoPanEffect();
        initBiaoZhu();
        getKmCal();
        setSportsgoal();

        preTime = FunctionStatic.onResume();
        MobclickAgent.onPageStart("StateActivity");

    }

    @Override
    public void onPagePause() {
        // TODO Auto-generated method stub
        FunctionStatic.onPause(getActivity(), FunctionStatic.FUNCTION_MYSPORTS,
                preTime);
        MobclickAgent.onPageEnd("StateActivity");
    }

    @Override
    public void onPageDestroy() {
        // TODO Auto-generated method stub
        if (histDB != null) {
            histDB.close();
            histDB = null;
        }
        mSportsApp.removeActivity(getActivity());
        if (mClient != null) {
            unregisterLocation();
            mClient = null;
        }
        type = 0;
        mSportsApp = null;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view.setOnTouchListener(this);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public boolean onTouch(View arg0, MotionEvent arg1) {
        // TODO Auto-generated method stub
        return true;
    }

    private void initView() {

        MainFragmentActivity parentActivity = (MainFragmentActivity) getActivity();
        ResideMenu resideMenu = parentActivity.getResideMenu();
        RelativeLayout ignored_view = (RelativeLayout) parentView
                .findViewById(R.id.state_activity_layout_id);
        resideMenu.addIgnoredView(ignored_view);
        bind_device_text = new TextView(getActivity());
        bind_device_text.setLayoutParams(new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        bind_device_text.setTextColor(getActivity().getResources().getColor(
                R.color.white));
        bind_device_text.setTextSize(18);
        bind_device_text.setText(getActivity().getResources().getString(
                R.string.waishe));
        bind_device_text.setId(bind_device_text_view);
        bind_device_text.setOnClickListener(this);
        right_btn.setPadding(0, 0, SportsApp.dip2px(17), 0);
        showRightBtn(bind_device_text);
        mWeek = (TextView) getActivity().findViewById(R.id.sport_week);
        mAear = (TextView) getActivity().findViewById(R.id.sport_area);
        mWeek.setText(getWeek());

        // setLastsports();

        if (mSportsApp.config == 1) {

        } else {

            LinearLayout linear_detail = (LinearLayout) getActivity()
                    .findViewById(R.id.linear_detail);
            linear_detail.setWeightSum(3);
        }


        UserDetail detail = SportsApp.getInstance().getSportUser();
        Log.i(TAG, "username is" + detail.getUname());
        mAear.setText(spf.getString("cityname", ""));
        mPopMenuBack = (RelativeLayout) getActivity().findViewById(
                R.id.pop_menu_background);

        // /////////////////////////////////////////////////////////
        dis_zheng = (TextView) getActivity().findViewById(R.id.dis_zheng);
        dis_xiao_1 = (TextView) getActivity().findViewById(R.id.dis_xiao_1);
        dis_zheng.setText("0.");
        dis_xiao_1.setText("00");
        dis_xiao_2 = (TextView) getActivity().findViewById(R.id.dis_xiao_2);
        sport_cishu = (TextView) getActivity().findViewById(R.id.sport_cishu);
        sport_sudu = (TextView) getActivity().findViewById(R.id.sport_sudu);
        sport_reliang = (TextView) getActivity().findViewById(
                R.id.sport_reliang);
        image_biao_yuan = (ImageView) getActivity().findViewById(
                R.id.image_biao_yuan);
        image_zhu_yuan = (ImageView) getActivity().findViewById(
                R.id.image_zhu_yuan);
        getActivity().findViewById(R.id.linear_detail).setOnClickListener(this);
        getActivity().findViewById(R.id.yundong_cishu).setOnClickListener(this);
        getActivity().findViewById(R.id.pic_1).setOnClickListener(this);
        getActivity().findViewById(R.id.sports_cishu_data).setOnClickListener(
                this);
        getActivity().findViewById(R.id.sport_cishu).setOnClickListener(this);
        getActivity().findViewById(R.id.cishu_dan).setOnClickListener(this);

        getActivity().findViewById(R.id.yundong_sudu).setOnClickListener(this);
        getActivity().findViewById(R.id.pic_2).setOnClickListener(this);
        getActivity().findViewById(R.id.sports_sudu_data).setOnClickListener(
                this);
        getActivity().findViewById(R.id.sport_sudu).setOnClickListener(this);
        getActivity().findViewById(R.id.sudu_dan).setOnClickListener(this);

        getActivity().findViewById(R.id.yundong_reliang).setOnClickListener(
                this);
        getActivity().findViewById(R.id.pic_3).setOnClickListener(this);
        getActivity().findViewById(R.id.sports_kaluli_data).setOnClickListener(
                this);
        getActivity().findViewById(R.id.sport_reliang).setOnClickListener(this);
        getActivity().findViewById(R.id.kaluli_dan).setOnClickListener(this);

        // 开始运动监听
        getActivity().findViewById(R.id.sports_start_state).setOnClickListener(
                this);

        animationSet1 = new AnimationSet(true);
        animationSet2 = new AnimationSet(true);
        translateAnimation1 = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0);
        translateAnimation2 = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1);
        animationSet1.setDuration(800);
        animationSet2.setDuration(800);
        animationSet1.addAnimation(translateAnimation1);
        animationSet1.setRepeatMode(Animation.REVERSE);
        animationSet2.addAnimation(translateAnimation2);
        animationSet2.setRepeatMode(Animation.REVERSE);

        animationSetDis1 = new AnimationSet(true);
        animationSetDis2 = new AnimationSet(true);
        translateAnimationDis1 = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0);
        translateAnimationDis2 = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1);
        animationSetDis1.setDuration(800);
        animationSetDis2.setDuration(800);
        animationSetDis1.addAnimation(translateAnimationDis1);
        animationSetDis1.setRepeatMode(Animation.REVERSE);
        animationSetDis2.addAnimation(translateAnimationDis2);
        animationSetDis2.setRepeatMode(Animation.REVERSE);

        animationSetCal1 = new AnimationSet(true);
        animationSetCal2 = new AnimationSet(true);
        translateAnimationCal1 = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0);
        translateAnimationCal2 = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1);
        animationSetCal1.setDuration(800);
        animationSetCal2.setDuration(800);
        animationSetCal1.addAnimation(translateAnimationCal1);
        animationSetCal1.setRepeatMode(Animation.REVERSE);
        animationSetCal2.addAnimation(translateAnimationCal2);
        animationSetCal2.setRepeatMode(Animation.REVERSE);

        List<SportContionTaskDetail> list_one = null;
        SportSubTaskDB taskDBhelper = SportSubTaskDB.getInstance(getActivity()
                .getApplicationContext());
        list_one = taskDBhelper.getAllTasks(mUid);
        if (list_one.size() <= 0 && mSportsApp.mIsNetWork) {

            new Thread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    List<SportContionTaskDetail> list = null;
                    // history_see = false;
                    histDB = SportSubTaskDB.getInstance(getActivity());
                    try {
                        while (true) {
                            if (history_see || mSportsApp.getSessionId() == null) {
                                break;
                            }
                            list = ApiJsonParser.getSportsTaskAll(
                                    mSportsApp.getSessionId(), mTimes, mUid);
                            Log.i("list--list", "list--list-->" + list);
                            mTimes++;
                            // && list.size() == 16
                            // list != null&& !"[]".equalsIgnoreCase(list + "")
                            if (list.size() > 0) {
                                for (SportContionTaskDetail sportContionTaskDetail : list) {
                                    if (getActivity() != null && !history_see) {
                                        saveDate2DB(sportContionTaskDetail);
                                    } else {
                                        break;
                                    }
                                }
                            } else {
                                history_see = true;
                                break;
                            }
                            Message msg = mHandler.obtainMessage();
                            msg.what = 20141106;
                            mHandler.sendMessage(msg);
                        }
                    } catch (ApiNetException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (ApiSessionOutException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if (histDB != null) {
                        histDB.close();
                        histDB = null;
                    }
                }
            }).start();
        } else {
            history_see = true;
        }

        // 总路程计算
        // allSportsData();
        initBiaoZhu();
        biaoPanEffect();
    }

    private void setLastsports() {
        // TODO Auto-generated method stub
        // SportContionTaskDetail st = mdb.getLastTask(mUid);
//		SportContionTaskDetail st = mdb.getAllHistorySports(mUid);
        // hDis.setText(SportTaskUtil.getDoubleNum(st.getSportDistance()) + "");
        // hCal.setText(st.getSprots_Calorie() + "");
    }

    // public void exit(){
    // getActivity().finish();
    // }

    @Override
    public void onStart() {
        super.onStart();
    }

    private String getWeek() {
        // TODO Auto-generated method stub
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(mWay)) {
            mWay = getActivity().getResources().getString(R.string.sundy);
        } else if ("2".equals(mWay)) {
            mWay = getActivity().getResources().getString(R.string.monday);
        } else if ("3".equals(mWay)) {
            mWay = getActivity().getResources().getString(R.string.tuesday);
        } else if ("4".equals(mWay)) {
            mWay = getActivity().getResources().getString(R.string.wednesday);
        } else if ("5".equals(mWay)) {
            mWay = getActivity().getResources().getString(R.string.thursday);
        } else if ("6".equals(mWay)) {
            mWay = getActivity().getResources().getString(R.string.friday);
        } else if ("7".equals(mWay)) {
            mWay = getActivity().getResources().getString(R.string.saturday);
        }
        return mWay;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {

		/*
         * case R.id.sport_state_layout: case R.id.type_layout: Intent intent =
		 * new Intent(getActivity(),
		 * FoxSportsType.classSportingMapActivity.class); Bundle bundle = new
		 * Bundle(); bundle.putInt("setting", 1); intent.putExtras(bundle);
		 * startActivity(intent);
		 */
            case bind_device_text_view:
                startActivity(new Intent(getActivity(), BindingDevice.class));
                break;
            case R.id.sport_dis:
            case R.id.sports_cal:
            case R.id.goal_dis:
            case R.id.goal_cal:
            case R.id.sport_step_layout:
            case R.id.goal_gongli:
            case R.id.sport_step_drawimage:
            case R.id.sport_calories_layout:
            case R.id.sport_cal_drawimage:
            case R.id.goal_caluli:
            case R.id.sport_dis_layout:
            case R.id.sports_cal_relative:
                shotSportsGoal();
                break;
            case R.id.sports_start_state:
//			if (isOPen(getActivity())) {
//				Intent start_sport = new Intent(getActivity(),
//						FoxSportsType.class);
//				startActivity(start_sport);
//			} else {
//				Bundle bundle = new Bundle();
//				bundle.putString("title", "提示");
//				bundle.putString("message", "您需要开启GPS定位,是否开启？");
//				onCreateDialog(2, bundle);
//			}
                break;
            // case R.id.linear_detail:
            // case R.id.yundong_cishu:
            // case R.id.pic_1:
            // case R.id.sports_cishu_data:
            // case R.id.sport_cishu:
            // case R.id.cishu_dan:
            // case R.id.yundong_sudu:
            // case R.id.pic_2:
            // case R.id.sports_sudu_data:
            // case R.id.sport_sudu:
            // case R.id.sudu_dan:
            // case R.id.yundong_reliang:
            // case R.id.pic_3:
            // case R.id.sports_kaluli_data:
            // case R.id.sport_reliang:
            // case R.id.kaluli_dan:
            // // shotSportsGoal();
            // Intent kintent = new Intent(getActivity(), HistoryAllActivity.class);
            // Bundle kbundle = new Bundle();
            // kbundle.putInt("ID", mUid);
            // kintent.putExtras(kbundle);
            // startActivity(kintent);
            // Intent kintent = new Intent(getActivity(),
            // DaoJiShiSetting.class);
            // startActivity(kintent);
            // break;
            case R.id.zhuzhuang_layout:
                // if (history_see) {
                history_see = true;
                Intent kintent = new Intent(getActivity(), HistoryAllActivity.class);
                Bundle kbundle = new Bundle();
                kbundle.putInt("ID", mUid);
                kintent.putExtras(kbundle);
                startActivity(kintent);
                // } else {
                // Toast.makeText(getActivity(),
                // getResources().getString(R.string.update_all_date),
                // Toast.LENGTH_SHORT).show();
                // }
                break;

            // case R.id.sport_state_layout:
            // case R.id.type_layout:
            // Intent intent = new Intent(getActivity(), FoxSportsType.class);
            // Bundle bundle = new Bundle();
            // bundle.putInt("setting", 1);
            // intent.putExtras(bundle);
            // startActivity(intent);
            // break;
        /*
         * case R.id.sport_bind_device: Intent ibind = new Intent(getActivity(),
		 * BindingDevice.class); startActivity(ibind); break;
		 */
            // case R.id.user_layout:
            // if (mSportsApp.LoginOption) {
            // Intent uinvite = new Intent(getActivity(),
            // UserEditActivity.class);
            // startActivity(uinvite);
            // } else {
            // mSportsApp.TyrLoginAction(getActivity(),
            // getString(R.string.sports_love_title),
            // getString(R.string.try_to_login));
            // }
            //
            // break;
            // case R.id.invite_layout:
            // case R.id.sport_invite:
            // case R.id.inviteText:
            // if (mSportsApp.LoginOption) {
            // Intent iinvite = new Intent(getActivity(),
            // InviteSportsActivity.class);
            // startActivityForResult(iinvite, SPORT_INVITE);
            // } else {
            // mSportsApp.TyrLoginAction(getActivity(),
            // getString(R.string.sports_love_title),
            // getString(R.string.try_to_login));
            // }
            //
            // break;
            // case R.id.share_layout:
            // case R.id.sport_share:
            // Tools.delAllFile(thisLarge);
            // if (Environment.getExternalStorageState().equals(
            // Environment.MEDIA_MOUNTED)) {
            // shot();
            // } else
            // Toast.makeText(mContext, "对不起lulu，未找到SD卡！", 1000).show();
            // break;

//		case R.id.xlwb:
//			Log.i(TAG, "share xinlang");
//			if (window != null && window.isShowing()) {
//				window.dismiss();
//			}
//			// shareToXinlangWeibo();
//			break;
//		case R.id.txwb:
//			if (window != null && window.isShowing()) {
//				window.dismiss();
//			}
//			// shareToTenxunWeibo();
//			break;
//		case R.id.wx:
//			if (window != null && window.isShowing()) {
//				window.dismiss();
//			}
//			// shareToWeixin();
//			break;
//		case R.id.cancel:
//			if (window != null && window.isShowing()) {
//				window.dismiss();
//			}
//			break;
            // case R.id.history_layout:
            // case R.id.sport_km:
            // case R.id.sport_cal:
            // if (mSportsApp.LoginOption) {
            // Intent kintent = new Intent(getActivity(), HistoryAllActivity.class);
            // Bundle kbundle = new Bundle();
            // kbundle.putInt("ID", mUid);
            // kintent.putExtras(kbundle);
            // startActivity(kintent);
            // }else {
            // mSportsApp.TyrLoginAction(getActivity(),
            // getString(R.string.sports_love_title),
            // getString(R.string.try_to_login));
            // }
            // break;
            // case R.id.schedule_layout:
            // Intent sintent = new Intent(getActivity(), FoxSportsGoal.class);
            // startActivityForResult(sintent, SPORTS_TYPE);
            // shotSportsGoal();
            // break;
            // case R.id.add_layout:
            // if (mSportsApp.LoginOption) {
            // Intent acintent = new Intent(getActivity(),
            // AddFriendActivity.class);
            // startActivity(acintent);
            // } else {
            // mSportsApp.TyrLoginAction(getActivity(),
            // getString(R.string.sports_love_title),
            // getString(R.string.try_to_login));
            // }
            // break;
            // case R.id.bind_devices:
            // case R.id.sport_bind_device:
            // startActivity(new Intent(getActivity(), BindingDevice.class));
            // break;

            case R.id.sports_goal_back:
                myWindow.dismiss();
                myView.setVisibility(View.GONE);
                mPopMenuBack.setVisibility(View.GONE);
                break;
            case R.id.sports_goal_ok:
                if (myEditStep.getText().toString().trim().equals("")
                        || myEditCalories.getText().toString().trim().equals("")) {
                    Toast.makeText(
                            getActivity(),
                            getActivity().getResources().getString(
                                    R.string.juliorcal), Toast.LENGTH_SHORT).show();
                } else {
                    myNumberStep = Integer
                            .parseInt(myEditStep.getText().toString());
                    myNumberCalories = Integer.parseInt(myEditCalories.getText()
                            .toString());
                    SharedPreferences sharedPreferences = getActivity()
                            .getSharedPreferences(
                                    "sports" + mSportsApp.getSportUser().getUid(),
                                    0);
                    Editor editor = sharedPreferences.edit();
                    editor.putInt("editDistance", myNumberStep);
                    editor.putInt("editCalories", myNumberCalories);
                    editor.commit();
                    myWindow.dismiss();
                    myView.setVisibility(View.GONE);
                    mPopMenuBack.setVisibility(View.GONE);
                    setSportsgoal();
                }
                break;
            // case R.id.sport_pm:
            // initPM();
            // break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SPORTS_TYPE:
                setSportsgoal();
                break;
            case SPORT_INVITE:
                // inviteText.setVisibility(View.GONE);
                break;
        }

    }

	/*
     * private void updateHeader() { // TODO Auto-generated method stub
	 * mImageWorker_Icon.setLoadingImage("man".equals(SportsApp.getInstance()
	 * .getSportUser().getSex()) ? R.drawable.sports_user_edit_portrait_male :
	 * R.drawable.sports_user_edit_portrait );
	 * mImageWorker_Icon.loadImage(SportsApp.getInstance().getSportUser()
	 * .getUimg(), userImage, null, null, false); }
	 */

    // private void updateHeader() {
    // // TODO Auto-generated method stub
    // mImageWorker_Icon.setLoadingImage("man".equals(SportsApp.getInstance()
    // .getSportUser().getSex()) ? R.drawable.local_image
    // : R.drawable.local_image_female);
    // mImageWorker_Icon.loadImage(SportsApp.getInstance().getSportUser()
    // .getUimg(), userImage, null, null, false);
    // }
    private TextView dis;
    private TextView dis_xiao1;
    private TextView dis_xiao2;
    private TextView cal;
    private TextView cal2;
    private TextView goal_dis;
    private TextView goal_cal;
    private TextView step_counter;
    private double todayDis = 0.00f;
    private int todayCal = 0;
    private int todayDisFloat = 0;
    private int todayDisCountdown = 0;
    private int todayCalCountdown = 0;

    private void setSportsgoal() {
        dis.setText("");
        dis_xiao1.setText("");
        dis_xiao2.setText("");
        cal.setText("");
        cal2.setText("");

        // 得到指定那一天的运动数据（距离和卡路里）
        SportContionTaskDetail st = mdb.getToadyHistorySports(mUid,
                getTodayDate());

        SharedPreferences sharedPreferences = getActivity()
                .getSharedPreferences(
                        "sports" + mSportsApp.getSportUser().getUid(), 0);
        goal_dis.setText(sharedPreferences.getInt("editDistance", 0) + "");
        goal_cal.setText(sharedPreferences.getInt("editCalories", 0) + "");

        todayDis = st.getSportDistance();

        Log.i("dis_strdis_str", "todayDis-->" + todayDis);

        if (todayDis != 0.00) {
            String dis_str = SportTaskUtil.getDoubleNumber(todayDis);
            todayDisFloat = Integer
                    .parseInt(dis_str.substring(dis_str.length() - 2));
            dis.setText(dis_str.substring(0, dis_str.length() - 2));
            if (todayDisFloat > 0) {
                if (todayDisFloat > 3) {
                    todayDisCountdown = 3;
                } else {
                    todayDisCountdown = todayDisFloat;
                }
                int d1 = todayDisFloat - todayDisCountdown;
                dis_xiao1.setText("");
                dis_xiao2.setText(String.format("%02d", d1));
                Log.v(TAG, "wmh todayDisFloat is " + todayDisFloat);
                Log.v(TAG, "wmh todayDisCountdown is " + todayDisCountdown);
            } else {
                dis_xiao2.setText("00");
            }
        } else {
            dis.setText("0.");
            dis_xiao2.setText("00");
        }

        todayCal = st.getSprots_Calorie();

        Log.i("dis_strdis_str", "todayCal-->" + todayCal);

        if (todayCal > 0) {
            cal.setText(st.getSprots_Calorie() + "");
            if (todayCal > 3) {
                todayCalCountdown = 3;
            } else {
                todayCalCountdown = todayCal;
            }
            int d1 = todayCal - todayCalCountdown;
            cal.setText("");
            cal2.setText(String.format("%02d", d1));
            Log.v(TAG, "wmh todayCal is " + todayCal);
            Log.v(TAG, "wmh todayCalCountdown is " + todayCalCountdown);
        } else {
            cal2.setText(st.getSprots_Calorie() + "");
        }
        if (todayDisFloat > 0 || todayCal > 0)
            mHandler.sendEmptyMessageDelayed(UPDATE_ANIMATION_TODAY, 800);

        biaoPanEffect();
        getKmCal();

    }


    private Handler mHandler = new Handler() {
        SharedPreferences spf;

        @Override
        public void handleMessage(Message msg) {
            if (getActivity() == null)
                return;
            try {
                spf = getActivity().getSharedPreferences("sports", 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            switch (msg.what) {

                case 20141106:
                    allSportsData();
                    biaoPanEffect();
                    getKmCal();
                    setSportsgoal();
                    break;

                case LAST_SPORTS:
                    Log.i(TAG, "handle receive");
                    setLastsports();
                    setSportsgoal();
                    break;
                case UPDATE_HEADER:
                    Log.i(TAG, "Shandle receive");
                    Intent data = (Intent) msg.obj;
                    Bundle bundle = data.getBundleExtra("useredit");
                    boolean isfacechanged = bundle.getBoolean("isfacechanged");
                    if (isfacechanged) {
                        // updateHeader();
                    }
                    break;
                case UPDATE_INVITE:
                    break;
                case UPDATE_TRYTOLOGIN:
                    initView();
                    break;
                case UPDATE_ANIMATION:
                    startTextAnimation(ANIMATION_TOTAL_DIS);
                    break;
                case STOP_ANIMATION:
                    dis_xiao_1.setText("");
                    break;
                case UPDATE_ANIMATION_TODAY:
                    startTextAnimation(ANIMATION_TODAY_DIS);
                    break;
                case STOP_ANIMATION_TODAY:
                    if (todayDisCountdown == 0)
                        dis_xiao1.setText("");
                    if (todayCalCountdown == 0)
                        cal.setText("");
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

    private void startTextAnimation(int animIndex) {
        switch (animIndex) {
            case ANIMATION_TOTAL_DIS:
                if (countdown > 0 && history_see) {
                    int d1 = juli_float - countdown;
                    countdown--;
                    int d2 = juli_float - countdown;
                    dis_xiao_1.setText(String.format("%02d", d1));
                    dis_xiao_2.setText(String.format("%02d", d2));
                    dis_xiao_1.startAnimation(animationSet2);
                    dis_xiao_2.startAnimation(animationSet1);
                    if (countdown == 0)
                        mHandler.sendEmptyMessageDelayed(STOP_ANIMATION, 800);
                    mHandler.sendEmptyMessageDelayed(UPDATE_ANIMATION, 800);
                }
                break;
            case ANIMATION_TODAY_DIS:
                if (todayDisCountdown > 0) {
                    int d1 = todayDisFloat - todayDisCountdown;
                    todayDisCountdown--;
                    int d2 = todayDisFloat - todayDisCountdown;
                    dis_xiao1.setText(String.format("%02d", d1));
                    dis_xiao2.setText(String.format("%02d", d2));
                    dis_xiao1.startAnimation(animationSetDis2);
                    dis_xiao2.startAnimation(animationSetDis1);
                    if (todayDisCountdown == 0)
                        mHandler.sendEmptyMessageDelayed(STOP_ANIMATION_TODAY, 800);
                    mHandler.sendEmptyMessageDelayed(UPDATE_ANIMATION_TODAY, 800);
                }
                if (todayCalCountdown > 0) {
                    int d1 = todayCal - todayCalCountdown;
                    todayCalCountdown--;
                    int d2 = todayCal - todayCalCountdown;
                    cal.setText(String.format("%02d", d1));
                    cal2.setText(String.format("%02d", d2));
                    cal.startAnimation(animationSetCal2);
                    cal2.startAnimation(animationSetCal1);
                    if (todayCalCountdown == 0)
                        mHandler.sendEmptyMessageDelayed(STOP_ANIMATION_TODAY, 800);
                    mHandler.sendEmptyMessageDelayed(UPDATE_ANIMATION_TODAY, 800);
                }
                break;
        }

    }



    private static int type = 0;

    public static int registerLocation() {
        int result = 1;
        if (type == 0) {
            myListener = new MyLocationListenner();
            if (mClient == null && context != null) {
                Looper.prepare();
                mClient = new LocationClient(context);
                Looper.loop();
            }
            if (mClient != null) {
                mClient.registerLocationListener(myListener);
                LocationClientOption option = new LocationClientOption();
                // option.setOpenGps(true);// 打开gps
                option.setLocationMode(LocationMode.Hight_Accuracy);
                option.setCoorType("bd0911");
                // option.setScanSpan(5000);
                mClient.setLocOption(option);
                mClient.start();
                Log.i("registerLocation", "注册定位监听！--->new");
                type = 1;
            }
        }
        if (mClient != null)
            result = mClient.requestLocation();
        Log.i("requestLocation", "id = " + result);
        return result;
    }

    public static void unregisterLocation() {
        if (type == 1 && mClient != null) {
            mClient.unRegisterLocationListener(myListener);
            mClient.stop();
            Log.i("unregisterLocation", "注销定位监听！");
            type = 0;
        }
    }

    /**
     * 定位SDK监听函数
     */
    public static class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) {
                return;
            }
            LocationInfo.latitude = Double.toString(location.getLatitude());
            LocationInfo.longitude = Double.toString(location.getLongitude());
            Log.i("location",
                    "这里" + location.getLatitude() + ","
                            + location.getLongitude());
            if (SportMain.isAutoLogin)
                SportMain.isAutoLogin = false;
        }
    }

    @Override
    public void onDismiss() {
        mPopMenuBack.setVisibility(View.GONE);
        myView.setVisibility(View.GONE);
    }

    private String getTodayDate() {
        String todayDate = null;
        Long todayTime = System.currentTimeMillis();
        Date date = new Date(todayTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        todayDate = sdf.format(date);
        Log.i("sport_sub", "todayDate:" + todayDate);
        return todayDate;
    }

    /*
     * 以下是PopupWindow弹出设置运动目标代码
     */
    private PopupWindow myWindow = null;
    private RelativeLayout myView;
    private EditText myEditStep;
    private EditText myEditCalories;
    private int myNumberStep;
    private int myNumberCalories;
    private int myNum = 0;
    private SharedPreferences mySP;
    private int kllMultiple;

    public void shotSportsGoal() {
        if (myWindow != null && myWindow.isShowing())
            return;
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        myView = (RelativeLayout) inflater.inflate(R.layout.fox_sports_goal,
                null);

        myView.findViewById(R.id.sports_goal_back).setOnClickListener(this);
        myView.findViewById(R.id.sports_goal_ok).setOnClickListener(this);
        myEditStep = (EditText) myView.findViewById(R.id.edit_step);
        myEditCalories = (EditText) myView.findViewById(R.id.edit_calories);

        SharedPreferences getDate = getActivity().getSharedPreferences(
                "sports" + mSportsApp.getSportUser().getUid(), 0);
        myEditStep.setText(String.valueOf(getDate.getInt("editDistance", 0)));
        myEditCalories
                .setText(String.valueOf(getDate.getInt("editCalories", 0)));

        myEditStep.addTextChangedListener(editSteplistener);
        myEditCalories.addTextChangedListener(editCaloriesListener);

        myWindow = new PopupWindow(myView,
                RelativeLayout.LayoutParams.FILL_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        myWindow.setAnimationStyle(R.style.AnimationPopup);
        myWindow.setOutsideTouchable(true);
        myWindow.setBackgroundDrawable(new BitmapDrawable());
        myWindow.showAtLocation(dis_zheng, Gravity.BOTTOM, 0, 0);
        myWindow.setOnDismissListener(this);
        final Animation animation = (Animation) AnimationUtils.loadAnimation(
                getActivity(), R.anim.slide_in_from_bottom);
        myView.startAnimation(animation);
        mPopMenuBack.setVisibility(View.VISIBLE);
    }

    TextWatcher editSteplistener = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                  int arg3) {
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
        }

        @Override
        public void afterTextChanged(Editable arg0) {
            mySP = getActivity().getSharedPreferences(
                    "sport_state_" + mSportsApp.getSportUser().getUid(), 0);
            int type = mySP.getInt("typeId", 1);
            int typeDetailId = mySP.getInt("typeDetailId", 0);
            if ("".equals(myEditStep.getText().toString())) {
                return;
            }
            int nameStep = Integer.parseInt(myEditStep.getText().toString());

            myNum++;
            if (myNum == 2) {
                myNum = 0;
                return;
            }
            kllMultiple = (int) KALULIMultiple(type, typeDetailId);
            if (!myEditStep.getText().toString().equals("")
                    && myEditStep.getText().toString() != null) {
                switch (type) {
                    case SportsType.TYPE_WALK:
                    case SportsType.TYPE_RUN:
                    case SportsType.TYPE_CLIMBING:
                    case SportsType.TYPE_GOLF:
                    case SportsType.TYPE_WALKRACE:
                    case SportsType.TYPE_CYCLING:
                    case SportsType.TYPE_FOOTBALL:
                    case SportsType.TYPE_ROWING:
                    case SportsType.TYPE_SWIM:
                        if (nameStep > 200) {
                            myEditStep.setText(200 + "");
                            Toast.makeText(
                                    getActivity(),
                                    getActivity().getResources().getString(
                                            R.string.julichao), Toast.LENGTH_SHORT)
                                    .show();
                            myEditCalories.setText((200 * kllMultiple) + "");
                        } else {
                            myEditCalories.setText((nameStep * kllMultiple) + "");
                        }
                        break;
                    default:
                        break;
                }
            }

        }
    };

    TextWatcher editCaloriesListener = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                  int arg3) {
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
        }

        @Override
        public void afterTextChanged(Editable arg0) {
            // mySP = getActivity().getSharedPreferences(
            // "sports" + mSportsApp.getSportUser().getUid(), 0);
            mySP = getActivity().getSharedPreferences(
                    "sport_state_" + mSportsApp.getSportUser().getUid(), 0);
            int type = mySP.getInt("typeId", 1);
            int typeDetailId = mySP.getInt("typeDetailId", 0);
            if ("".equals(myEditCalories.getText().toString())) {
                return;
            }
            myEditCalories.setFilters(new InputFilter[]{

            });
            int nameCalories = Integer.parseInt(myEditCalories.getText()
                    .toString());

            myNum++;
            if (myNum == 2) {
                myNum = 0;
                return;
            }
            kllMultiple = (int) KALULIMultiple(type, typeDetailId);
            if (!myEditCalories.getText().toString().equals("")) {
                switch (type) {
                    case SportsType.TYPE_WALK:
                    case SportsType.TYPE_RUN:
                    case SportsType.TYPE_CLIMBING:
                    case SportsType.TYPE_GOLF:
                    case SportsType.TYPE_WALKRACE:
                    case SportsType.TYPE_CYCLING:
                    case SportsType.TYPE_FOOTBALL:
                    case SportsType.TYPE_ROWING:
                    case SportsType.TYPE_SWIM:
                        if (nameCalories > kllMultiple * 200) {
                            myEditCalories.setText(200 * kllMultiple + "");
                            Toast.makeText(
                                    getActivity(),
                                    getActivity().getResources().getString(
                                            R.string.caltaida), Toast.LENGTH_SHORT)
                                    .show();
                            myEditStep.setText(200 + "");
                        } else {
                            myEditStep.setText((nameCalories / kllMultiple) + "");
                        }
                        break;
                    default:
                        break;
                }
            } else {
                myEditStep.setText("");
            }
        }
    };

    public double KALULIMultiple(int type, int detailType) {
        int weight = SportsApp.getInstance().getSportUser().getWeight();
        if (weight == 0) {
            weight = 65;
        }
        double KLL = 1;
        switch (type) {
            case SportsType.TYPE_WALK:
                KLL = WalkingUtils.getCalories(1);// weight * 3.15 / 4;
                break;
            case SportsType.TYPE_RUN:
                KLL = RunningUtils.getCalories(1);// weight * 9.375 / 10;
                break;
            case SportsType.TYPE_CLIMBING:
                KLL = weight * 8.0375 / 6;
                break;
            case SportsType.TYPE_GOLF:
                KLL = weight * 4.025 / 3;
                break;
            case SportsType.TYPE_WALKRACE:
                KLL = weight * 9.375 / 9;
                break;
            case SportsType.TYPE_CYCLING:
                KLL = weight * 9.375 / 20;
                break;
            case SportsType.TYPE_FOOTBALL:
                KLL = weight * 9.375 / 10;
                break;
            case SportsType.TYPE_ROWING:
                KLL = weight * 2.6675 / 5;
                break;
            case SportsType.TYPE_SWIM: {
                switch (detailType) {
                    case 0:
                        KLL = weight * 8.715 / 6;
                        break;
                    case 1:
                        KLL = weight * 8.19 / 6;
                        break;
                    case 2:
                        KLL = weight * 14.49 / 6;
                        break;
                    case 3:
                        KLL = weight * 7.5075 / 6;
                        break;
                }

            }
            break;
            default:
                break;
        }
        return KLL;
    }

    /*
     * 运动总路程，运动总次数，平均速度，运动消耗热量
     */
    private double sports_juli = 0.00;
    private int sports_cishu = 0;
    private double sports_sudu = 0.00;
    private int sports_reliang = 0;
    private int juli_float = 0;
    int countdown = 0;

    private void allSportsData() {
        sports_juli = 0.00;
        sports_cishu = 0;
        sports_sudu = 0.00;
        sports_reliang = 0;
        List<SportContionTaskDetail> list = null;
        SportSubTaskDB taskDBhelper = SportSubTaskDB.getInstance(getActivity()
                .getApplicationContext());
        list = taskDBhelper.getAllTasks(mUid);
        sports_cishu = list.size();
        count = sports_cishu;
        while (count > 0) {
            sports_juli += list.get(count - 1).getSportDistance();
            sports_sudu += list.get(count - 1).getSportVelocity();
            sports_reliang += list.get(count - 1).getSprots_Calorie();
            count--;
        }
        if (sports_cishu != 0) {
            sports_sudu /= sports_cishu;
        }
        String sudu_str = SportTaskUtil.getDoubleNumber(sports_sudu);
        if (sports_juli != 0.00) {
            String dis_str = SportTaskUtil.getDoubleNumber(sports_juli);
            juli_float = Integer
                    .parseInt(dis_str.substring(dis_str.length() - 2));
            dis_zheng.setText(dis_str.substring(0, dis_str.length() - 2));
            if (juli_float > 0) {
                if (juli_float > 2) {
                    countdown = 2;
                } else {
                    countdown = juli_float;
                }
                int d1 = juli_float - countdown;
                dis_xiao_1.setText("");
                dis_xiao_2.setText(String.format("%02d", d1));
                Log.v(TAG, "wmh juli_float is " + juli_float);
                Log.v(TAG, "wmh countdown is " + countdown);
                mHandler.sendEmptyMessageDelayed(UPDATE_ANIMATION, 800);
            }
        } else {
            dis_zheng.setText("0.");
            dis_xiao_1.setText("");
            dis_xiao_2.setText("00");
            // dis_xiao_1.startAnimation(animationSet2);
        }

        sport_cishu.setText(sports_cishu + "");
        sport_sudu.setText(sudu_str + "");
        sport_reliang.setText(sports_reliang + "");
    }

    private String CalcStepCounter() {
        double sports_juli = 0;
        int sports_cishu = 0;

        List<SportContionTaskDetail> list = null;
        SportSubTaskDB taskDBhelper = SportSubTaskDB.getInstance(getActivity()
                .getApplicationContext());

        list = taskDBhelper.getTasksByDate(mUid, getStringDate(0));
        sports_cishu = list.size();

        if (sports_cishu > 0) {
            for (int i = 0; i < sports_cishu; i++) {
                sports_juli += list.get(i).getSportDistance();
            }
        } else {
            sports_juli = 0;
        }

        sports_cishu = (int) (sports_juli * 10000 / 6);
        return String.valueOf(sports_cishu);
    }

    /*
     * 表盘效果图
     */
    DrawNewImageView sport_step_drawimage;
    DrawNewImageView sport_cal_drawimage;

    private void biaoPanEffect() {

        Display mDisplay = getActivity().getWindowManager().getDefaultDisplay();
        int displayWidth = mDisplay.getWidth();
        // int displayHeight = mDisplay.getHeight();

        SportContionTaskDetail st = mdb.getToadyHistorySports(mSportsApp
                .getSportUser().getUid(), getTodayDate());
        double sports_dis = st.getSportDistance();
        int sportsgoal_dis = sharedPreferences.getInt("editDistance", 0);
        if (sportsgoal_dis != 0 && sports_dis != 0) {
            sport_step_drawimage.setAngle(sports_dis, sportsgoal_dis,
                    displayWidth);
        } else if (sportsgoal_dis == 0 && sports_dis != 0) {
            sport_step_drawimage.setAngle(110, 100, displayWidth);
        } else {
            sport_step_drawimage.setDisplayWidth(displayWidth);
        }

        int sports_cal = st.getSprots_Calorie();
        int sportsgoal_cal = sharedPreferences.getInt("editCalories", 0);
        if (sportsgoal_cal != 0 && sports_cal != 0) {
            sport_cal_drawimage.setAngle(sports_cal, sportsgoal_cal,
                    displayWidth);
        } else if (sportsgoal_cal == 0 && sports_cal != 0) {
            sport_cal_drawimage.setAngle(110, 100, displayWidth);
        } else {
            sport_cal_drawimage.setDisplayWidth(displayWidth);
        }

    }

    private ViewPager viewPager;
    private List<View> viewList = new ArrayList<View>();
    private String[] months;
    private TextView month_english;
    private TextView month_num;
    private TextView day1;
    private TextView day2;
    private TextView day3;
    private TextView day4;
    private TextView day5;
    private ImageView cal_1;
    private ImageView cal_2;
    private ImageView cal_3;
    private ImageView cal_4;
    private ImageView cal_5;
    private ImageView km_1;
    private ImageView km_2;
    private ImageView km_3;
    private ImageView km_4;
    private ImageView km_5;
    private LinearLayout mCalKm;

    private void initBiaoZhu() {
        viewPager = (ViewPager) getActivity().findViewById(
                R.id.viewpager_biaozhu);
        initViewPagerScroll();
        addView();
    }

    /**
     * 设置ViewPager的滑动速度
     */
    private void initViewPagerScroll() {
        try {
            Field mScroller = null;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            ViewPagerScroller scroller = new ViewPagerScroller(
                    viewPager.getContext());
            mScroller.set(viewPager, scroller);
        } catch (NoSuchFieldException e) {

        } catch (IllegalArgumentException e) {

        } catch (IllegalAccessException e) {

        }
    }

    private void addView() {
        LayoutInflater inflater_biaopan = getActivity().getLayoutInflater();
        View biaopan = inflater_biaopan.inflate(R.layout.biaopantu, null);

        dis = (TextView) biaopan.findViewById(R.id.sport_dis);
        dis_xiao1 = (TextView) biaopan.findViewById(R.id.sport_dis_xiao_1);
        dis_xiao2 = (TextView) biaopan.findViewById(R.id.sport_dis_xiao_2);
        if (dis != null) {
            Log.i("dis", "dis is not null");
        }
        cal = (TextView) biaopan.findViewById(R.id.sports_cal);
        cal2 = (TextView) biaopan.findViewById(R.id.sports_cal2);
        goal_dis = (TextView) biaopan.findViewById(R.id.goal_dis);
        goal_cal = (TextView) biaopan.findViewById(R.id.goal_cal);

        dis.setOnClickListener(this);
        biaopan.findViewById(R.id.sport_dis_layout).setOnClickListener(this);
        cal.setOnClickListener(this);
        biaopan.findViewById(R.id.sports_cal_relative).setOnClickListener(this);
        goal_dis.setOnClickListener(this);
        goal_cal.setOnClickListener(this);

        biaopan.findViewById(R.id.sport_step_layout).setOnClickListener(this);
        biaopan.findViewById(R.id.goal_gongli).setOnClickListener(this);
        biaopan.findViewById(R.id.sport_calories_layout).setOnClickListener(
                this);
        biaopan.findViewById(R.id.goal_caluli).setOnClickListener(this);
        sport_step_drawimage = (DrawNewImageView) biaopan
                .findViewById(R.id.sport_step_drawimage);
        sport_step_drawimage.setOnClickListener(this);
        sport_cal_drawimage = (DrawNewImageView) biaopan
                .findViewById(R.id.sport_cal_drawimage);
        sport_cal_drawimage.setOnClickListener(this);
        step_counter = (TextView) biaopan.findViewById(R.id.step_counter);
        String tempStep = CalcStepCounter();
        int tempLength = tempStep.length();
        SpannableString ss = new SpannableString(" 今日步数 " + tempStep + " 步 ");
        ss.setSpan(new ForegroundColorSpan(Color.parseColor("#8B8686")), 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(Color.parseColor("#F49000")), 6, 6 + tempLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(Color.parseColor("#8B8686")), 6 + tempLength, 9 + tempLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        step_counter.setText(ss);
        // scrollLayout.addView(biaopan);

        LayoutInflater inflater_zhuzhuang = getActivity().getLayoutInflater();
        View zhuzhuang = inflater_zhuzhuang.inflate(R.layout.zhuzhuangtu, null);
        zhuzhuang.findViewById(R.id.zhuzhuang_layout).setOnClickListener(this);

        months = getActivity().getResources().getStringArray(
                R.array.month_english);
        month_english = (TextView) zhuzhuang.findViewById(R.id.month_english);
        month_num = (TextView) zhuzhuang.findViewById(R.id.month_num);

        day1 = (TextView) zhuzhuang.findViewById(R.id.day1);
        day2 = (TextView) zhuzhuang.findViewById(R.id.day2);
        day3 = (TextView) zhuzhuang.findViewById(R.id.day3);
        day4 = (TextView) zhuzhuang.findViewById(R.id.day4);
        day5 = (TextView) zhuzhuang.findViewById(R.id.day5);

        cal_1 = (ImageView) zhuzhuang.findViewById(R.id.cal_1);
        cal_2 = (ImageView) zhuzhuang.findViewById(R.id.cal_2);
        cal_3 = (ImageView) zhuzhuang.findViewById(R.id.cal_3);
        cal_4 = (ImageView) zhuzhuang.findViewById(R.id.cal_4);
        cal_5 = (ImageView) zhuzhuang.findViewById(R.id.cal_5);

        km_1 = (ImageView) zhuzhuang.findViewById(R.id.km_1);
        km_2 = (ImageView) zhuzhuang.findViewById(R.id.km_2);
        km_3 = (ImageView) zhuzhuang.findViewById(R.id.km_3);
        km_4 = (ImageView) zhuzhuang.findViewById(R.id.km_4);
        km_5 = (ImageView) zhuzhuang.findViewById(R.id.km_5);

        mCalKm = (LinearLayout) zhuzhuang.findViewById(R.id.mCalKm);

        // Log.i("mCalKm", "mCalKm:" + mCalKm.getLayoutParams().height);

        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        month_english.setText(months[month - 1] + "");
        month_num.setText(month + "");

        day1.setText(getDateBefore(1));
        day2.setText(getDateBefore(2));
        day3.setText(getDateBefore(3));
        day4.setText(getDateBefore(4));
        day5.setText(getDateBefore(5));

        getKmCal();
        if (viewList.size() != 0) {
            viewList.clear();
        }
        viewList.add(biaopan);
        viewList.add(zhuzhuang);

        viewPager.setAdapter(new MyPagerAdapter(viewList));
        switch (viewPager.getCurrentItem()) {
            case 0:
                image_biao_yuan.setBackgroundDrawable(getActivity().getResources()
                        .getDrawable(R.drawable.page_focus));
                image_zhu_yuan.setBackgroundDrawable(getActivity().getResources()
                        .getDrawable(R.drawable.page));
                break;
            case 1:
                image_biao_yuan.setBackgroundDrawable(getActivity().getResources()
                        .getDrawable(R.drawable.page));
                image_zhu_yuan.setBackgroundDrawable(getActivity().getResources()
                        .getDrawable(R.drawable.page_focus));
                break;
        }

        viewPager.setOnPageChangeListener(new MyPageListener());
    }

    class MyPageListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
            switch (arg0) {
                case 0:
                    image_biao_yuan.setBackgroundDrawable(getActivity()
                            .getResources().getDrawable(R.drawable.page_focus));
                    image_zhu_yuan.setBackgroundDrawable(getActivity()
                            .getResources().getDrawable(R.drawable.page));
                    break;
                case 1:
                    image_biao_yuan.setBackgroundDrawable(getActivity()
                            .getResources().getDrawable(R.drawable.page));
                    image_zhu_yuan.setBackgroundDrawable(getActivity()
                            .getResources().getDrawable(R.drawable.page_focus));
                    break;
            }
        }
    }

    private String getDateBefore(int day) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DATE, -day);
        SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd");
        Log.i("getDateBefore", "getDateBefore:" + sdfd.format(now.getTime()));
        String days[] = sdfd.format(now.getTime()).split("-");
        return days[2];
    }

    private String getStringDate(int day) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DATE, -day);
        SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd");
        String str_date = sdfd.format(now.getTime());
        return str_date;
    }

    private void getKmCal() {
        getKmCalDate();

        int mcalkm_height = mCalKm.getLayoutParams().height;

        if (sharedPreferences.getInt("editDistance", 0) != 0) {
            showImageView();
            int dis_goal = sharedPreferences.getInt("editDistance", 0);
            LayoutParams para1 = km_1.getLayoutParams();
            if (juli_list.get(0) >= dis_goal) {
                para1.height = mcalkm_height;
                km_1.setLayoutParams(para1);
            } else {
                para1.height = (int) (mcalkm_height * juli_list.get(0) * 1.0 / dis_goal);
                km_1.setLayoutParams(para1);
            }

            LayoutParams para2 = km_2.getLayoutParams();
            if (juli_list.get(1) >= dis_goal) {
                para2.height = mcalkm_height;
                km_2.setLayoutParams(para2);
            } else {
                para2.height = (int) (mcalkm_height * juli_list.get(1) * 1.0 / dis_goal);
                km_2.setLayoutParams(para2);
            }

            LayoutParams para3 = km_3.getLayoutParams();
            if (juli_list.get(2) >= dis_goal) {
                para3.height = mcalkm_height;
                km_3.setLayoutParams(para3);
            } else {
                para3.height = (int) (mcalkm_height * juli_list.get(2) * 1.0 / dis_goal);
                km_3.setLayoutParams(para3);
            }

            LayoutParams para4 = km_4.getLayoutParams();
            if (juli_list.get(3) >= dis_goal) {
                para4.height = mcalkm_height;
                km_4.setLayoutParams(para4);
            } else {
                para4.height = (int) (mcalkm_height * juli_list.get(3) * 1.0 / dis_goal);
                km_4.setLayoutParams(para4);
            }

            LayoutParams para5 = km_5.getLayoutParams();
            if (juli_list.get(4) >= dis_goal) {
                para5.height = mcalkm_height;
                km_5.setLayoutParams(para5);
            } else {
                para5.height = (int) (mcalkm_height * juli_list.get(4) * 1.0 / dis_goal);
                km_5.setLayoutParams(para5);
            }

        } else {
            Collections.sort(juli_list2);
            double max = juli_list2.get(juli_list2.size() - 1);
            if (max != 0) {
                showImageView();
                LayoutParams para1 = km_1.getLayoutParams();
                para1.height = (int) (mcalkm_height * juli_list.get(0) * 1.0 / max);
                km_1.setLayoutParams(para1);
                LayoutParams para2 = km_2.getLayoutParams();
                para2.height = (int) (mcalkm_height * juli_list.get(1) * 1.0 / max);
                km_2.setLayoutParams(para2);
                LayoutParams para3 = km_3.getLayoutParams();
                para3.height = (int) (mcalkm_height * juli_list.get(2) * 1.0 / max);
                km_3.setLayoutParams(para3);
                LayoutParams para4 = km_4.getLayoutParams();
                para4.height = (int) (mcalkm_height * juli_list.get(3) * 1.0 / max);
                km_4.setLayoutParams(para4);
                LayoutParams para5 = km_5.getLayoutParams();
                para5.height = (int) (mcalkm_height * juli_list.get(4) * 1.0 / max);
                km_5.setLayoutParams(para5);

            } else {
                hideImageView();
            }

        }

		/*
		 * 卡路里
		 */
        if (sharedPreferences.getInt("editCalories", 0) != 0) {
            int dis_goal = sharedPreferences.getInt("editCalories", 0);
            showImageView();
            LayoutParams para1 = cal_1.getLayoutParams();
            if (reliang_list.get(0) >= dis_goal) {
                para1.height = mcalkm_height;
                cal_1.setLayoutParams(para1);
            } else {
                para1.height = (int) (mcalkm_height * reliang_list.get(0) * 1.0 / dis_goal);
                cal_1.setLayoutParams(para1);
            }
            LayoutParams para2 = cal_2.getLayoutParams();
            if (reliang_list.get(1) >= dis_goal) {
                para2.height = mcalkm_height;
                cal_2.setLayoutParams(para2);
            } else {
                para2.height = (int) (mcalkm_height * reliang_list.get(1) * 1.0 / dis_goal);
                cal_2.setLayoutParams(para2);
            }
            LayoutParams para3 = cal_3.getLayoutParams();
            if (reliang_list.get(2) >= dis_goal) {
                para3.height = mcalkm_height;
                cal_3.setLayoutParams(para3);
            } else {
                para3.height = (int) (mcalkm_height * reliang_list.get(2) * 1.0 / dis_goal);
                cal_3.setLayoutParams(para3);
            }
            LayoutParams para4 = cal_4.getLayoutParams();
            if (reliang_list.get(3) >= dis_goal) {
                para4.height = mcalkm_height;
                cal_4.setLayoutParams(para4);
            } else {
                para4.height = (int) (mcalkm_height * reliang_list.get(3) * 1.0 / dis_goal);
                cal_4.setLayoutParams(para4);
            }
            LayoutParams para5 = cal_5.getLayoutParams();
            if (reliang_list.get(4) >= dis_goal) {
                para5.height = mcalkm_height;
                cal_5.setLayoutParams(para5);
            } else {
                para5.height = (int) (mcalkm_height * reliang_list.get(4) * 1.0 / dis_goal);
                cal_5.setLayoutParams(para5);
            }
        } else {
            Collections.sort(reliang_list2);
            int max = reliang_list2.get(reliang_list2.size() - 1);
            if (max != 0) {
                showImageView();
                LayoutParams para1 = cal_1.getLayoutParams();
                para1.height = (int) (mcalkm_height * reliang_list.get(0) * 1.0 / max);
                cal_1.setLayoutParams(para1);
                LayoutParams para2 = cal_2.getLayoutParams();
                para2.height = (int) (mcalkm_height * reliang_list.get(1) * 1.0 / max);
                cal_2.setLayoutParams(para2);
                LayoutParams para3 = cal_3.getLayoutParams();
                para3.height = (int) (mcalkm_height * reliang_list.get(2) * 1.0 / max);
                cal_3.setLayoutParams(para3);
                LayoutParams para4 = cal_4.getLayoutParams();
                para4.height = (int) (mcalkm_height * reliang_list.get(3) * 1.0 / max);
                cal_4.setLayoutParams(para4);
                LayoutParams para5 = cal_5.getLayoutParams();
                para5.height = (int) (mcalkm_height * reliang_list.get(4) * 1.0 / max);
                cal_5.setLayoutParams(para5);
            } else {
                hideImageView();
            }

        }
    }

    List<Double> juli_list = new ArrayList<Double>();
    List<Double> juli_list2 = new ArrayList<Double>();
    List<Integer> reliang_list = new ArrayList<Integer>();
    List<Integer> reliang_list2 = new ArrayList<Integer>();

    private void getKmCalDate() {
        if (!juli_list.isEmpty()) {
            juli_list.clear();
            reliang_list.clear();
            juli_list2.clear();
            reliang_list2.clear();
        }
        SportSubTaskDB taskDBhelper = SportSubTaskDB.getInstance(getActivity()
                .getApplicationContext());
        for (int day = 1; day < 6; day++) {
            List<SportContionTaskDetail> list = null;
            list = taskDBhelper.getTasksByDate(mUid, getStringDate(day));
            int count = 0;
            count = list.size();
            double juli = 0.00;
            int reliang = 0;
            while (count > 0) {
                juli += list.get(count - 1).getSportDistance();
                reliang += list.get(count - 1).getSprots_Calorie();
                count--;
            }
            juli_list.add(juli);
            reliang_list.add(reliang);
            juli_list2.add(juli);
            reliang_list2.add(reliang);
        }

    }

    @Override
    public void OnViewChange(int view) {
        // TODO Auto-generated method stub

    }

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @param context
     * @return true 表示开启
     */
    public static final boolean isOPen(final Context context) {
        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        // boolean network =
        // locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps) {
            return true;
        }
        return false;
    }

    @Override
    protected Dialog onCreateDialog(int id, Bundle args) {

        switch (id) {
            case 2:
                String title = args.getString("title");
                String message = args.getString("message");
                AlertDialog.Builder builder = new Builder(getActivity());
                builder.setMessage(message);
                builder.setTitle(title);
                builder.setPositiveButton(getString(R.string.button_ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent intent = new Intent(
                                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivityForResult(intent, 1);
                            }
                        });
                builder.setNegativeButton(getString(R.string.button_cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                Dialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                break;
        }
        return super.onCreateDialog(id, args);
    }

    private void showImageView() {
        cal_1.setVisibility(View.VISIBLE);
        cal_2.setVisibility(View.VISIBLE);
        cal_3.setVisibility(View.VISIBLE);
        cal_4.setVisibility(View.VISIBLE);
        cal_5.setVisibility(View.VISIBLE);

        km_1.setVisibility(View.VISIBLE);
        km_2.setVisibility(View.VISIBLE);
        km_3.setVisibility(View.VISIBLE);
        km_4.setVisibility(View.VISIBLE);
        km_5.setVisibility(View.VISIBLE);
    }

    private void hideImageView() {
        cal_1.setVisibility(View.GONE);
        cal_2.setVisibility(View.GONE);
        cal_3.setVisibility(View.GONE);
        cal_4.setVisibility(View.GONE);
        cal_5.setVisibility(View.GONE);

        km_1.setVisibility(View.GONE);
        km_2.setVisibility(View.GONE);
        km_3.setVisibility(View.GONE);
        km_4.setVisibility(View.GONE);
        km_5.setVisibility(View.GONE);
    }

    private void saveDate2DB(SportContionTaskDetail detail) {

        Cursor cursor = histDB.query(mUid, detail.getStartTime());
        try {
            if (!cursor.moveToFirst()) {
                ContentValues values = new ContentValues();
                values.put(SportSubTaskDB.UID, detail.getUserId());
                values.put(SportSubTaskDB.SPORT_TYPE, detail.getSports_type());
                values.put(SportSubTaskDB.SPORT_SWIM_TYPE, detail.getSwimType());
                values.put(SportSubTaskDB.SPORT_DEVICE,
                        detail.getMonitoringEquipment());
                values.put(SportSubTaskDB.SPORT_START_TIME,
                        detail.getStartTime());
                values.put(SportSubTaskDB.SPORT_TIME, detail.getSportTime());
                values.put(SportSubTaskDB.SPORT_DISTANCE,
                        detail.getSportDistance());
                values.put(SportSubTaskDB.SPORT_SPEED,
                        detail.getSportVelocity());
                values.put(SportSubTaskDB.SPORT_CALORIES,
                        detail.getSprots_Calorie());
                values.put(SportSubTaskDB.SPORT_HEART_RATE,
                        detail.getHeartRate());
                values.put(SportSubTaskDB.SPORT_LAT_LNG, detail.getLatlng());
                values.put(SportSubTaskDB.SPORT_ISUPLOAD, 1);
                values.put(SportSubTaskDB.SPORT_DATE, detail.getSportDate());
                values.put(SportSubTaskDB.SPORT_TASKID, detail.getTaskid());
                values.put(SportSubTaskDB.SPORT_STEP, detail.getStepNum());
                values.put(SportSubTaskDB.SPORT_MAPTYPE, detail.getMapType());
                histDB.insert(values, false);
                Log.v(TAG, "插入date到数据库成功 taskID =" + detail.getTaskid());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            // db.close();
        }
    }

}
