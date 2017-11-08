package com.fox.exercise;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.fox.exercise.api.WatchService;
import com.umeng.analytics.MobclickAgent;

import java.util.Timer;
import java.util.TimerTask;

public class SreachDevice extends Activity implements OnClickListener {

    private Button startBtn;
    private ImageView sreachImg;

    private boolean isStart = false;
    private boolean isWait = false;
    private Timer mTimer;
    private TimerTask task;
    private static SreachDevice context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean findMethod = SmartBarUtils.findActionBarTabsShowAtBottom();
        if (!findMethod) {
            // 取消ActionBar拆分，换用TabHost
            getWindow().setUiOptions(0);
            getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        setContentView(R.layout.sreach_device);
        context = this;
        bindService(new Intent(this, WatchService.class), wConnection,
                Context.BIND_AUTO_CREATE);
        startBtn = (Button) findViewById(R.id.startBtn);
        startBtn.setOnClickListener(this);
        sreachImg = (ImageView) findViewById(R.id.sreachImg);
        if (findMethod) {
            getActionBar().setDisplayShowHomeEnabled(false);
            getActionBar().setDisplayShowTitleEnabled(false);
            SmartBarUtils.setActionModeHeaderHidden(getActionBar(), true);
            SmartBarUtils.setActionBarViewCollapsable(getActionBar(), true);
        }
        task = new TimerTask() {
            @Override
            public void run() {
                if (isStart) {
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                }
            }
        };
        mTimer = new Timer(true);
        // 延时0ms后执行，2分钟执行一次
        mTimer.schedule(task, 0, 1000 * 2);
    }

    private WatchService wService;
    private ServiceConnection wConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            wService = ((WatchService.WBinder) service).getService();
            wService.registerSearchCallback(sCallback);
        }

        public void onServiceDisconnected(ComponentName className) {
            wService = null;
        }
    };
    private WatchService.SearchCallback sCallback = new WatchService.SearchCallback() {

        @Override
        public void setSearch(boolean isdevice) {
            // TODO Auto-generated method stub

        }

    };
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    animationSet(isStart);
                    break;
            }
        }

        ;
    };

    private void animationSet(Boolean isStart) {
        Animation am = AnimationUtils.loadAnimation(this, R.anim.sreach_anim);
        if (isStart) {
            am.setRepeatCount(Animation.INFINITE);
            sreachImg.startAnimation(am);
        } else {
            sreachImg.clearAnimation();
        }
    }

    @Override
    public void onClick(View v) {
        int key = v.getId();
        switch (key) {
            case R.id.startBtn:
                if (isWait)
                    return;
                if (isStart) {
                    isStart = false;
                    startBtn.setText(getResources().getText(
                            R.string.remote_startTxt));
                    animationSet(isStart);
                } else {
                    isStart = true;
                    startBtn.setText(getResources().getText(
                            R.string.remote_finishTxt));
                    animationSet(isStart);
                }
                if (wService != null)
                    wService.searchDevice(isStart);
                isWait = true;
                handler.removeCallbacks(mThread);
                handler.postDelayed(mThread, 1000);
                break;
        }
    }

    private Runnable mThread = new Runnable() {
        @Override
        public void run() {
            isWait = false;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (task != null)
            task.cancel();
        if (mTimer != null)
            mTimer.cancel();
        handler.removeCallbacks(mThread);
    }

    public static void stopSearch() {
        // TODO Auto-generated method stub
        if (context != null) {
            context.isStart = false;
            context.startBtn.setText(context.getResources().getText(
                    R.string.remote_startTxt));
            context.animationSet(false);
        }

    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("SreachDevice");
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("SreachDevice");
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
