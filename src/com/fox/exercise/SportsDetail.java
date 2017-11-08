package com.fox.exercise;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.fox.exercise.api.WatchService;
import com.fox.exercise.db.SportDatabase;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.ingenic.indroidsync.SportsApp;

public class SportsDetail extends Activity implements OnClickListener {

    private Context context;
    private static final int TYPE_MSG = 0;
    private static final int TIME_MSG = 1;
    private static final int DIS_MSG = 2;
    private static final int STEP_MSG = 3;
    private static final int CAL_MSG = 4;

    private TextView phone_sports;
    private TextView phone_parts;

    private TextView date_one;
    // private TextView date_two;
    private TextView date_three;
    private TextView date_four;

    private ImageView sports_type;
    private TextView sports_times;
    // private TextView sports_step;
    private TextView sports_cals;
    private TextView sports_dis;

    private TextView detail_date;

    private ImageView image;
    private int mMoveSize;

    private int currentIndex = 1;

    private int[] types = new int[]{R.drawable.zoulu, R.drawable.paobu,
            R.drawable.ziyouyong, R.drawable.dieyong, R.drawable.wayong,
            R.drawable.yangyong, R.drawable.dengshan, R.drawable.gaoerfu,
            R.drawable.jingzou, R.drawable.qixing, R.drawable.wangqiu,
            R.drawable.yumaoqiu, R.drawable.zuqiu, R.drawable.pingpangqiu,
            R.drawable.huachuan, R.drawable.liubing, R.drawable.lunhua};

    private SportsApp mSportsApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean findMethod = SmartBarUtils.findActionBarTabsShowAtBottom();
        if (!findMethod) {
            // 取消ActionBar拆分，换用TabHost
            getWindow().setUiOptions(0);
            getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        setContentView(R.layout.sports_detail);
        context = SportsDetail.this;
        mSportsApp = (SportsApp) getApplication();
        mSportsApp.addActivity(this);
        //友盟推送
        PushAgent.getInstance(this).onAppStart();
        InitImage();
        init();
        if (findMethod) {
            getActionBar().setDisplayShowHomeEnabled(false);
            getActionBar().setDisplayShowTitleEnabled(false);
            SmartBarUtils.setActionModeHeaderHidden(getActionBar(), true);
            SmartBarUtils.setActionBarViewCollapsable(getActionBar(), true);
        }
        bindService(new Intent(this, WatchService.class), wConnection,
                Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SportsApp app = (SportsApp) getApplication();
        SharedPreferences sp = getSharedPreferences("user_login_info", Context.MODE_PRIVATE);
        if (!"".equals(sp.getString("account", "")) && app.LoginOption) {
            if (app.getSessionId() == null || app.getSessionId().equals("")) {
                finish();
            }
        } else if ("".equals(sp.getString("account", "")) && app.LoginOption) {
            if (app.getSessionId() == null || app.getSessionId().equals("")) {
                finish();
            }
        }
    }

    private void init() {
        date_one = (TextView) findViewById(R.id.date_one);
        // date_two = (TextView) findViewById(R.id.date_two);
        date_three = (TextView) findViewById(R.id.date_three);
        date_four = (TextView) findViewById(R.id.date_four);

        date_one.setText(Time());
        // date_two.setText(Time());
        date_three.setText(Time());
        date_four.setText(Time());

        sports_type = (ImageView) findViewById(R.id.sports_type);
        sports_times = (TextView) findViewById(R.id.sports_times);
        // sports_step = (TextView) findViewById(R.id.sports_step);
        sports_cals = (TextView) findViewById(R.id.sports_cals);
        sports_dis = (TextView) findViewById(R.id.sports_dis);

        detail_date = (TextView) findViewById(R.id.detail_date);

        SportDatabase helper = SportDatabase
                .getInstance(getApplicationContext());
        Cursor cursor1 = null;
        cursor1 = helper.queryOne();
        Log.i("cursor1", "cursor1:" + cursor1);
        if (cursor1 != null && cursor1.getCount() > 0 && cursor1.moveToFirst()) {
            sports_type.setBackgroundDrawable(getResources().getDrawable(
                    types[cursor1.getInt(cursor1.getColumnIndex("type"))]));
            Log.i("SportsDetail",
                    "SportsDetail:" + cursor1.getColumnIndex("date"));
            if (!cursor1.getString(cursor1.getColumnIndex("date")).toString()
                    .substring(0, 10).equalsIgnoreCase(Time())) {
                detail_date.setText(cursor1
                        .getString(cursor1.getColumnIndex("date")).toString()
                        .substring(0, 10));
            }
        }

        Cursor cursor2 = null;
        cursor2 = helper.queryTwo();
        Log.i("cursor2", "cursor2:" + cursor2);
        if (cursor2 != null && cursor2.getCount() > 0 && cursor2.moveToFirst()) {
            if (!cursor2.getString(cursor2.getColumnIndex("timer")).equals(
                    "00:00:00")) {
                sports_times.setText(cursor2.getString(cursor2
                        .getColumnIndex("timer")));
            }
        }

        Cursor cursor3 = null;
        cursor3 = helper.queryThree();
        Log.i("cursor3", "cursor3:" + cursor3);
        if (cursor3 != null && cursor3.getCount() > 0 && cursor3.moveToFirst()) {
            // sports_step.setText(cursor3.getString(cursor3
            // .getColumnIndex("step")));
            sports_dis
                    .setText(cursor3.getString(cursor3.getColumnIndex("dis")));
            sports_cals
                    .setText(cursor3.getString(cursor3.getColumnIndex("cal")));
        }

        phone_sports = (TextView) findViewById(R.id.title_week);
        phone_parts = (TextView) findViewById(R.id.title_month);

        phone_sports.setOnClickListener(this);
        phone_parts.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_week:
                phone_sports.setTextColor(getResources().getColor(
                        R.color.sports_popular_title_selected));
                phone_parts.setTextColor(getResources().getColor(
                        R.color.white));
                if (currentIndex != 0) {
                    TranslateAnimation anim1 = new TranslateAnimation(
                            mMoveSize * 0, mMoveSize * -1, 0, 0);
                    anim1.setFillAfter(true);
                    anim1.setDuration(300);
                    image.startAnimation(anim1);
                }
                currentIndex = 0;
                finish();
                break;
            case R.id.title_month:
                phone_sports.setTextColor(getResources().getColor(
                        R.color.sports_popular_title_normal));
                phone_parts.setTextColor(getResources().getColor(
                        R.color.sports_popular_title_selected));
                if (currentIndex == 0) {
                    TranslateAnimation anim2 = new TranslateAnimation(
                            mMoveSize * -1, mMoveSize * 0, 0, 0);
                    anim2.setFillAfter(true);
                    anim2.setDuration(300);
                    image.startAnimation(anim2);
                }
                currentIndex = 1;
                // case R.id.phone_parts:
                // phone_sports.setTextColor(getResources().getColor(
                // R.color.detail_unfoucs));
                // phone_parts.setTextColor(getResources().getColor(
                // R.color.detail_foucs));
                // break;
        }
    }

    private WatchService wService;
    private ServiceConnection wConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            wService = ((WatchService.WBinder) service).getService();
            wService.registerSpecialCallback(sCallback);
        }

        public void onServiceDisconnected(ComponentName className) {
            wService = null;
        }
    };
    protected Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TYPE_MSG:
                    String typeDate = (String) msg.obj;
                    Log.i("tyepdate", "typeDate:" + typeDate);
                    String[] str = typeDate.split(",");
                    int type = Integer.valueOf(str[0]);
                    sports_type.setBackgroundDrawable(getResources().getDrawable(
                            types[type]));
                    String date = str[1];
                    date = date.substring(0, 10);
                    date_one.setText(date);
                    // date_two.setText(date);
                    date_three.setText(date);
                    date_four.setText(date);
                    break;
                case TIME_MSG:
                    String timerValue = (String) msg.obj;
                    sports_times.setText(timerValue);
                    break;
                case STEP_MSG:
                    String stepValue = (String) msg.obj;
                    // sports_step.setText(stepValue);
                    break;
                case DIS_MSG:
                    String disValue = (String) msg.obj;
                    sports_dis.setText(disValue);
                    break;
                case CAL_MSG:
                    String calValue = (String) msg.obj;
                    sports_cals.setText(calValue);
                    break;
            }
        }

    };

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(SportsDetail.this, BindingDevice.class));
            finish();
        }
        return false;
    }

    ;

    private WatchService.SpecialCallback sCallback = new WatchService.SpecialCallback() {
        public void timerValue(String timer) {
            Message msg = Message.obtain(mHandler, TIME_MSG, timer);
            msg.sendToTarget();
        }

        public void stepValue(String step) {
            Message msg = Message.obtain(mHandler, STEP_MSG, step);
            msg.sendToTarget();
        }

        public void disValue(String dis) {
            Message msg = Message.obtain(mHandler, DIS_MSG, dis);
            msg.sendToTarget();
        }

        public void calValue(String cal) {
            Message msg = Message.obtain(mHandler, CAL_MSG, cal);
            msg.sendToTarget();
        }

        public void heartValue(String heart) {

        }

        public void typeValue(int type, String date) {
            String str = type + "," + date;
            Log.i("str", "str:" + str);
            Message msg = Message.obtain(mHandler, TYPE_MSG, str);
            msg.sendToTarget();
        }
    };

    // SharedPreferences prefs = PreferenceManager
    // .getDefaultSharedPreferences(this);
    // OnSharedPreferenceChangeListener myPrefListner = new
    // OnSharedPreferenceChangeListener() {
    //
    // @Override
    // public void onSharedPreferenceChanged(SharedPreferences arg0,
    // String arg1) {
    //
    // }
    // };
    // prefs.registerOnSharedPreferenceChangeListener(myPrefListner);

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wService != null)
            wService.unregisterSpecialCallback();
        unbindService(wConnection);
        mSportsApp.removeActivity(this);
        // SportDatabase helper = SportDatabase
        // .getInstance(getApplicationContext());
        // helper.deleteOne();
        // helper.deleteTwo();
        // helper.deleteThree();
    }

    private void InitImage() {

        image = (ImageView) findViewById(R.id.my_cursor_bar);
//		DisplayMetrics dm = new DisplayMetrics();
//		getWindowManager().getDefaultDisplay().getMetrics(dm);
//		int screenW = dm.widthPixels;

        BitmapDrawable bitmap = (BitmapDrawable) image.getDrawable();
        mMoveSize = bitmap.getBitmap().getWidth()
                + SportsApp.getInstance().dip2px(2);

        // mMoveSize = (screenW - SportsApp.getInstance().dip2px(85)) / 2;
//		LayoutParams para = image.getLayoutParams();
//		para.width = mMoveSize;
//		image.setLayoutParams(para);
    }

    public String Time() {
        // Calendar c = Calendar.getInstance();
        // String time = c.get(Calendar.YEAR) + "." + (c.get(Calendar.MONTH) +
        // 1)
        // + "." + c.get(Calendar.DAY_OF_MONTH);
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String time = df.format(date);
        Log.i("date", "date" + date);
        return time;

    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("SportsDetail");
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("SportsDetail");
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
