package com.fox.exercise.lockscreen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;

import com.fox.exercise.R;
import com.fox.exercise.SmartBarUtils;

import cn.ingenic.indroidsync.SportsApp;

public class LockScreen extends Activity {
    private static String TAG = "QINZDLOCK";
    private SliderRelativeLayout sliderLayout = null;
    private Context mContext = null;
    public static int MSG_LOCK_SUCESS = 1;
    private SharedPreferences foxSportSetting;
    private SportsApp mSportsApp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = LockScreen.this;

		/* 设置全屏，无标题 */
        /*this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        boolean findMethod = SmartBarUtils.findActionBarTabsShowAtBottom();
        if (!findMethod) {
            // 取消ActionBar拆分，换用TabHost
            getWindow().setUiOptions(0);
            getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        setContentView(R.layout.lockscreen);
        initViews();
        if (findMethod) {
            getActionBar().setDisplayShowHomeEnabled(false);
            getActionBar().setDisplayShowTitleEnabled(false);
            SmartBarUtils.setActionModeHeaderHidden(getActionBar(), true);
            SmartBarUtils.setActionBarViewCollapsable(getActionBar(), true);
        }
        mSportsApp = (SportsApp) LockScreen.this.getApplication();
        sliderLayout.setMainHandler(mHandler);
    }

    private void initViews() {
        sliderLayout = (SliderRelativeLayout) findViewById(R.id.slider_layout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 设置动画
        mHandler.postDelayed(AnimationDrawableTask, 300);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected void onDestory() {
        super.onDestroy();
    }

    // 通过延时控制当前绘制bitmap的位置坐标
    private Runnable AnimationDrawableTask = new Runnable() {

        public void run() {
            mHandler.postDelayed(AnimationDrawableTask, 300);
        }
    };

    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {

            Log.i(TAG, "handleMessage :  #### ");

            if (MSG_LOCK_SUCESS == msg.what) {
                foxSportSetting = LockScreen.this.getSharedPreferences("sports"
                        + mSportsApp.getSportUser().getUid(), 0);
                Editor editor = foxSportSetting.edit();
                editor.putBoolean("lockisopen", false);
                editor.commit();
                setResult(RESULT_OK, new Intent());
                finish(); // 锁屏成功时，结束我们的Activity界面
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
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