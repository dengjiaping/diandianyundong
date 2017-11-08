package com.fox.exercise;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.fox.exercise.api.WatchService;

public class WarnActivity extends Activity {

    private ImageView bgImg;
    private ImageView clickImg;

    // 键盘管理器
    KeyguardManager mKeyguardManager;
    // 键盘锁
    private KeyguardLock mKeyguardLock;
    // 电源管理器
    private PowerManager mPowerManager;
    // 唤醒锁
    private PowerManager.WakeLock mWakeLock;

    private Vibrator vibrator;

    private MediaPlayer mMediaPlayer;
    private Uri alert;
    //	private boolean isbind;
    private static WarnActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //	requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        boolean findMethod = SmartBarUtils.findActionBarTabsShowAtBottom();
        if (!findMethod) {
            // 取消ActionBar拆分，换用TabHost
            getWindow().setUiOptions(0);
            getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        setContentView(R.layout.warn_view);
        mContext = this;
        mPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mKeyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);

        clickImg = (ImageView) findViewById(R.id.imgID);

        bgImg = (ImageView) findViewById(R.id.bgID);
        bgImg.setOnTouchListener(new MyTouchLinster());
        bindService(
                new Intent(this, WatchService.class), wConnection,
                Context.BIND_AUTO_CREATE);
//		isbind=true;
        startWarn();
        startAudioAndVibrator();
        if (findMethod) {
            getActionBar().setDisplayShowHomeEnabled(false);
            getActionBar().setDisplayShowTitleEnabled(false);
            SmartBarUtils.setActionModeHeaderHidden(getActionBar(), true);
            SmartBarUtils.setActionBarViewCollapsable(getActionBar(), true);
        }
    }

    private WatchService wService;
    private ServiceConnection wConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            wService = ((WatchService.WBinder) service).getService();
        }

        public void onServiceDisconnected(ComponentName className) {
            wService = null;
        }
    };

    private void startWarn() {
        // 点亮亮屏
        mWakeLock = mPowerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK,
                "Tag");
        mWakeLock.acquire();
        // 初始化键盘锁
        mKeyguardLock = mKeyguardManager.newKeyguardLock("");
        // 键盘解锁
        mKeyguardLock.disableKeyguard();
    }

    // 开始播放手机铃声及震动
    private void startAudioAndVibrator() {

        try {
            alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(this, alert);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
            mMediaPlayer.setLooping(true);
            mMediaPlayer.prepare();
            mMediaPlayer.start();

            vibrator(new long[]{1000, 500, 1000, 500}, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void vibrator(long[] pattern, boolean isRepeat) {
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(pattern, isRepeat ? 1 : -1);
    }

    // 手指移动监听
    private class MyTouchLinster implements OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            final int action = event.getAction();
            float x = event.getX();
            float y = event.getY();
            // Log.i("onTouch", "x = " + x + ",y = " + y);
            switch (action) {
                case MotionEvent.ACTION_MOVE:
                    if (x < -90 || x > 160) {
                        stop();
                        finish();
                    }
                    break;
                case MotionEvent.ACTION_DOWN:
                    clickImg.setVisibility(View.VISIBLE);
                    Animation am = AnimationUtils.loadAnimation(WarnActivity.this, R.anim.click_down_anim);
                    am.setRepeatCount(Animation.INFINITE);
                    clickImg.startAnimation(am);
                    vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(50);
                    break;
                case MotionEvent.ACTION_UP:
                    Animation an = AnimationUtils.loadAnimation(WarnActivity.this, R.anim.click_up_anim);
                    an.setRepeatCount(Animation.INFINITE);
                    clickImg.startAnimation(an);
                    clickImg.setVisibility(View.GONE);
                    break;
                default:
                    clickImg.clearAnimation();
                    break;
            }
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stop();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWakeLock != null) {
            System.out.println("----> 终止服务,释放唤醒锁");
            mWakeLock.release();
            mWakeLock = null;
        }
        if (mKeyguardLock != null) {
            System.out.println("----> 终止服务,重新锁键盘");
            mKeyguardLock.reenableKeyguard();
        }
        if (wService != null) {
            wService.stopSearch();
            unbindService(wConnection);
        }
    }

    private void stop() {
        // TODO Auto-generated method stub
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            mMediaPlayer = null;
            vibrator.cancel();
        }
    }

    public static void onFinish() {
        // TODO Auto-generated method stub
        if (mContext != null) {
            mContext.stop();
            mContext.finish();
        }
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
