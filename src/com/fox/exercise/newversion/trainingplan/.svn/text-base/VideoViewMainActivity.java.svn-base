package com.fox.exercise.newversion.trainingplan;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.VideoView;

import com.fox.exercise.R;
import com.fox.exercise.api.Api;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiMessage;
import com.fox.exercise.newversion.TrainVoiceBgPrompt;
import com.fox.exercise.newversion.TrainVoicePrompt;
import com.fox.exercise.newversion.UUIDGenerator;
import com.fox.exercise.newversion.entity.ScreenListener;
import com.fox.exercise.newversion.entity.ScreenListener.ScreenStateListener;
import com.fox.exercise.newversion.entity.TrainJsonInfo;
import com.fox.exercise.newversion.entity.TrainResultsInfo;
import com.fox.exercise.newversion.view.MyProgressView;
import com.fox.exercise.newversion.view.TrainVideoView;
import com.fox.exercise.util.FileSaveRead;
import com.fox.exercise.util.SportTaskUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.ingenic.indroidsync.SportsApp;

public class VideoViewMainActivity extends Activity implements OnClickListener {
    TrainVideoView main_videoview;
    private MediaController mController;
    // 存放所有视频端的url
    private ArrayList<String> VideoListQueue = new ArrayList<String>();
    private int currentVideoIndex = 0;
    private ImageView last_btn, next_btn, pause_btn, back_btn, music_btn;
    private MyProgressView progress_viewid;
    private String videoDir;
    private ArrayList<TrainJsonInfo> lJsonInfos = new ArrayList<TrainJsonInfo>();
    private TextView train_actionname, train_position, train_time,
            train_action_nextname;
    private boolean isPause = false;
    private boolean isStart = false;
    private boolean isSleep = false;// 默认不是睡眠
    private boolean isScreen = false;// 判断锁屏时是不是视频在暂停状态
    private int cureNum;// 当前动作播放的第几次
    public static final int VOICEPLAY_ONE = 10;
    public static final int VOICEPLAY_TWO = 11;
    public static final int VOICEPLAY_THREE = 12;
    public static final int VOICEPLAY_FOUR = 13;
    public static final int UPLOAD_RETURN = 14;
    private Map<String, String> hashMap;
    private TrainVoicePrompt trainVoicePrompt;
    private boolean isYuPlay;// 表示是否是预播放 false 表示是在预播放视频
    private PopupWindow myWindow = null;// 退出播放popwindow
    private LinearLayout myView;
    private PopupWindow comPleteWindow;// 训练完成弹出框
    private ArrayList<Float> lsList;// 表示是每段视频的时长
    private int num1, num2, num3, num4;// 表示坚持五秒和倒数五秒的时长和中间插播的时长 休息结束时长
    private int runNum;// 表示运行的秒数
    private int sleepTime;// 休息时长
    private RelativeLayout set_music_background;
    private LinearLayout set_menu_background;
    private TextView background_sleeptime, background_train_action_nextname;
    private ImageView close_set_music;// 关闭音乐设置按钮
    private SharedPreferences foxSportSetting;
    private ToggleButton train_isclose_tbtn;// 关闭按钮
    private SeekBar mySeekBar1;
    private int maxVolume = 150; // 最大音量值
    private int seekBarNum;// seekbar的值
    private float curVolume = 0; // 背景音量值
    private double train_calorie;// 训练的消耗卡路里
    private ArrayList<Integer> train_actionList = new ArrayList<Integer>();// 训练动作的id字符串以,分割
    private SportsApp mSportsApp;
    private int train_id;// 训练id
    private String train_name;// 训练部位
    private LinearLayout pause_menu_background;// 点击暂停弹出页面
    private TextView background_train_action_cruname;// 当前动作名
    private LinearLayout start_play_layout;
    private TrainVoiceBgPrompt trainVoiceBgPrompt = null;
    private boolean isopen_bgMusic;
    private TrainPlanDataBase db;
    private Activity mContext;
    private String markCode;// 训练唯一标识
    private Boolean isFirstSave = true;
    private int isUpload = 0;// 判断是否上传了
    private Dialog mUploadDialog = null;
    private LinearLayout train_sleep_layout;// 休息弹出框的秒数
    private ScreenListener screenListener;// 屏幕锁定监控
    private boolean isFirst=false;//是不是第一次播放
    private CallReceiver callReceiver;
    private String readJsonFile;//本地json文件内容

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏

        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.activity_video_view_main);
        mContext = this;
        mSportsApp = (SportsApp) getApplication();

        callReceiver=new CallReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        intentFilter.setPriority(Integer.MAX_VALUE);
        registerReceiver(callReceiver, intentFilter);


        Intent intent = getIntent();
        if (intent != null) {
            videoDir = intent.getStringExtra("videoDir");
            train_id = intent.getIntExtra("train_id", 0);
            train_name = intent.getStringExtra("train_name");
        }
        Bundle bundle1 = new Bundle();
        bundle1.putString(
                "message",
                getResources().getString(
                        R.string.sports_authentication_uploading));
        mUploadDialog = onCreateDialog(1, bundle1, 0);
        screenListener = new ScreenListener(mContext);
        screenListener.begin(new ScreenStateListener() {

            @Override
            public void onUserPresent() {
                // TODO Auto-generated method stub
                //解锁
                if (!isScreen) {
                    if (main_videoview != null && !main_videoview.isPlaying()
                            && isPause) {
                        main_videoview.start();
                        isPause = false;
                        startTimeSeconds = System.currentTimeMillis();
                        mTempCount = recLen;
                    }
                    if (trainVoiceBgPrompt != null) {
                        trainVoiceBgPrompt.startPlay();
                    }
                    if (trainVoicePrompt != null) {
                        trainVoicePrompt.startPlay();
                    }
                }
            }

            @Override
            public void onScreenOn() {
                // TODO Auto-generated method stub
                Log.e("onScreenOn", "onScreenOn");
                //开屏
//				if (!isScreen) {
//					if (main_videoview != null && !main_videoview.isPlaying()
//							&& isPause) {
//						main_videoview.start();
//						isPause = false;
//						startTimeSeconds = System.currentTimeMillis();
//						mTempCount = recLen;
//					}
//					if (trainVoiceBgPrompt != null) {
//						trainVoiceBgPrompt.startPlay();
//					}
//					if (trainVoicePrompt != null) {
//						trainVoicePrompt.startPlay();
//					}
//				}

            }

            @Override
            public void onScreenOff() {
                // TODO Auto-generated method stub
                //锁屏
                if (!isScreen) {
                    if (main_videoview != null && main_videoview.isPlaying()) {
                        main_videoview.pause();
                        isPause = true;
                        startTimeSeconds = System.currentTimeMillis();
                        mTempCount = recLen;
                    }
                    if (trainVoiceBgPrompt != null) {
                        trainVoiceBgPrompt.stopPlay();
                    }
                    if (trainVoicePrompt != null) {
                        trainVoicePrompt.stopOtherPlay();
                    }
                }

            }
        });
        // 读取json文件
        readJsonFile = FileSaveRead.readJsonFile(videoDir
                + "/train.json");
        if(readJsonFile!=null&&!"".equals(readJsonFile)){
            init();
        }else{
            Toast.makeText(this,"文件解析错误",Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void init() {
        hashMap = new HashMap<String, String>();
        foxSportSetting = getSharedPreferences("sports"
                + SportsApp.getInstance().getSportUser().getUid(), 0);
        isopen_bgMusic = foxSportSetting.getBoolean("bgMusic_isopen", true);
        if (isopen_bgMusic) {
            seekBarNum = foxSportSetting.getInt("SeekBarProgress",
                    maxVolume / 2);
            curVolume = (float) (1 - (Math.log(maxVolume - seekBarNum) / Math
                    .log(maxVolume)));
        } else {
            seekBarNum = 0;
            curVolume = 0;
        }
        File f3 = new File(videoDir + "/" + "female_bjy.mp3");
        if (f3.exists()) {
            trainVoiceBgPrompt = new TrainVoiceBgPrompt(this, "female",
                    "train_bg", videoDir);
            trainVoiceBgPrompt.playVoice(curVolume);
        }
        try {
            JSONObject jsonObject = new JSONObject(readJsonFile);
            String filename = jsonObject.getString("filename");
            TrainJsonInfo trainJsonInfo;
            JSONArray jsonArray = jsonObject.getJSONArray("playfilelist");
            for (int i = 0; i < jsonArray.length(); i++) {
                trainJsonInfo = new TrainJsonInfo();
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                String string = jsonObject2.getString("playname");
                trainJsonInfo.setPlayname(string);
                int int1 = jsonObject2.getInt("posion");
                trainJsonInfo.setPosion(int1);
                double double1 = jsonObject2.getDouble("size");
                trainJsonInfo.setSize(double1);
                int int2 = jsonObject2.getInt("actionNum");
                trainJsonInfo.setActionNum(int2);
                String string2 = jsonObject2.getString("actionName");
                trainJsonInfo.setActionName(string2);
                int int3 = jsonObject2.getInt("time");
                trainJsonInfo.setTime(int3);
                int actionFlag = jsonObject2.getInt("actionFlag");
                trainJsonInfo.setActionFlag(actionFlag);
                double actionCal = jsonObject2.getDouble("actionCal");
                trainJsonInfo.setActionCal(actionCal);
                int otherFlag = jsonObject2.getInt("otherFlag");
                trainJsonInfo.setOtherFlag(otherFlag);
                int sleepTime = jsonObject2.getInt("sleepTime");
                trainJsonInfo.setSleepTime(sleepTime);

                lJsonInfos.add(trainJsonInfo);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        getVideoUrls();
        last_btn = (ImageView) findViewById(R.id.last_btn);
        next_btn = (ImageView) findViewById(R.id.next_btn);
        pause_btn = (ImageView) findViewById(R.id.pause_btn);
        back_btn = (ImageView) findViewById(R.id.trainback_btn);
        music_btn = (ImageView) findViewById(R.id.music_btn);
        if (isopen_bgMusic) {
            music_btn.setImageResource(R.drawable.train_music_bg);
        } else {
            music_btn.setImageResource(R.drawable.train_nomusic_bg);
        }
        last_btn.setOnClickListener(this);
        next_btn.setOnClickListener(this);
        pause_btn.setOnClickListener(this);
        back_btn.setOnClickListener(this);
        music_btn.setOnClickListener(this);
        train_actionname = (TextView) findViewById(R.id.train_actionname);
        train_position = (TextView) findViewById(R.id.train_actionnum);
        train_time = (TextView) findViewById(R.id.train_time);
        train_action_nextname = (TextView) findViewById(R.id.train_action_nextname);
        progress_viewid = (MyProgressView) findViewById(R.id.progress_viewid);
        main_videoview = (TrainVideoView) findViewById(R.id.main_videoview);
        set_menu_background = (LinearLayout) findViewById(R.id.set_menu_background);
        set_music_background = (RelativeLayout) findViewById(R.id.set_music_background);
        background_sleeptime = (TextView) findViewById(R.id.background_sleeptime);
        background_train_action_nextname = (TextView) findViewById(R.id.background_train_action_nextname);
        close_set_music = (ImageView) findViewById(R.id.close_set_music);
        close_set_music.setOnClickListener(this);
        train_isclose_tbtn = (ToggleButton) findViewById(R.id.train_isclose_tbtn);
        train_isclose_tbtn.setChecked(isopen_bgMusic);
        mySeekBar1 = (SeekBar) findViewById(R.id.mySeekBar1);
        train_sleep_layout = (LinearLayout) findViewById(R.id.train_sleep_layout);
        train_sleep_layout.setOnClickListener(this);
        // //获取当前值
        mySeekBar1.setMax(maxVolume);
        mySeekBar1.setProgress(seekBarNum);
        mySeekBar1.setOnSeekBarChangeListener(new OnSeekBarChangeListener() // 调音监听器
        {
            public void onProgressChanged(SeekBar arg0, int progress,
                                          boolean fromUser) {
                mySeekBar1.setProgress(progress);
                if (progress == 0) {
                    train_isclose_tbtn.setChecked(false);
                } else {
                    train_isclose_tbtn.setChecked(true);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                curVolume = (float) (1 - (Math.log(maxVolume
                        - seekBar.getProgress()) / Math.log(maxVolume)));
                if (trainVoiceBgPrompt != null) {
                    trainVoiceBgPrompt.setPlay(curVolume);
                }
                Editor editor1 = foxSportSetting.edit();
                editor1.putInt("SeekBarProgress", seekBar.getProgress());
                editor1.commit();

            }
        });

        train_isclose_tbtn
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        // TODO Auto-generated method stub
                        // 使用三目运算符来响应按钮变换的事件
                        Editor editor1 = foxSportSetting.edit();
                        editor1.putBoolean("bgMusic_isopen", isChecked);
                        editor1.commit();
                        if (isChecked) {
                            music_btn
                                    .setImageResource(R.drawable.train_music_bg);
                            if (seekBarNum == 0) {
                                seekBarNum = maxVolume / 2;
                            }
                            mySeekBar1.setProgress(seekBarNum);
                        } else {
                            music_btn
                                    .setImageResource(R.drawable.train_nomusic_bg);
                            mySeekBar1.setProgress(0);
                            seekBarNum = 0;
                            curVolume = 0;
                        }
                        curVolume = (float) (1 - (Math.log(maxVolume
                                - seekBarNum) / Math.log(maxVolume)));
                        if (trainVoiceBgPrompt != null) {
                            trainVoiceBgPrompt.playVoice(curVolume);
                        }
                    }
                });

        pause_menu_background = (LinearLayout) findViewById(R.id.pause_menu_background);
        background_train_action_cruname = (TextView) findViewById(R.id.background_train_action_cruname);
        start_play_layout = (LinearLayout) findViewById(R.id.start_play_layout);
        start_play_layout.setOnClickListener(this);

        mController = new MediaController(this, false);
        mController.setVisibility(View.GONE);
        main_videoview.setVideoPath(VideoListQueue.get(currentVideoIndex));
        main_videoview.setMediaController(mController);
        isYuPlay = false;
        hashMap.put("actionFirst", videoDir + File.separator
                + "female_first.wav");
        hashMap.put("actionName", videoDir + File.separator
                + getVoiceName(lJsonInfos.get(currentVideoIndex).getPlayname())
                + ".wav");
        if (lJsonInfos.get(currentVideoIndex).getActionFlag() == 1) {
            // 表示是次数
            hashMap.put("actionNum", lJsonInfos.get(currentVideoIndex)
                    .getActionNum() + "");
        } else {
            // 表示是秒数
            hashMap.put("actionNum", lJsonInfos.get(currentVideoIndex)
                    .getActionNum()
                    * lJsonInfos.get(currentVideoIndex).getTime() + "");
        }

        hashMap.put("actionFlag", lJsonInfos.get(currentVideoIndex)
                .getActionFlag() + "");
        hashMap.put("321go", videoDir + File.separator + "female_321go.wav");
        trainVoicePrompt = new TrainVoicePrompt(this, "female", "start",
                hashMap, mHandler, videoDir);
        trainVoicePrompt.playVoice();
        isPause = true;
        cureNum = 1;
        train_actionname.setText(lJsonInfos.get(0).getActionName());
        train_action_nextname.setText(lJsonInfos.get(1).getActionName());
        setPlayJinDu("1" + "/" + lJsonInfos.get(0).getActionNum(), "1");
        main_videoview.setOnPreparedListener(new OnPreparedListener() {

            public void onPrepared(MediaPlayer arg0) {

                //FixMe 在资源尺寸可以播放观看时处理
                if (arg0.getVideoHeight() > 0 && arg0.getVideoWidth() > 0) {
                    if(!isFirst){
                        //FixMe 获取视频资源的宽度
                        int mVideoWidth = arg0.getVideoWidth();
                        //FixMe 获取视频资源的高度
                        int mVideoHeight = arg0.getVideoHeight();
                        //FixMe 获取屏幕的宽度
                        DisplayMetrics display = getResources().getDisplayMetrics();
                        //FixMe 拉伸比例
                        float scale = (float) mVideoWidth / (float) mVideoHeight;
                        //FixMe 视频资源拉伸至屏幕宽度，横屏竖屏需结合传感器等特殊处理
//                      mVideoWidth = display.widthPixels;
                        //FixMe 拉伸VideoView高度
                        mVideoHeight=display.heightPixels;
                        mVideoWidth=(int)(mVideoHeight*scale);
//                      mVideoHeight = (int) (mVideoWidth / scale);//FixMe 设置surfaceview画布大小

                        isFirst=true;
                        main_videoview.getHolder().setFixedSize(mVideoWidth, mVideoHeight);
                        //FixMe 重绘VideoView大小，这个方法是在重写VideoView时对外抛出方法
                        main_videoview.setMeasure(mVideoWidth, mVideoHeight);
                    }
                    //FixMe 请求调整
//                            main_videoview.requestLayout();
//                            main_videoview.invalidate();
                }
                    arg0.start();
                }
            }

            );
            main_videoview.setOnCompletionListener(new

            OnCompletionListener() {

                @Override
                public void onCompletion (MediaPlayer arg0){
                    // TODO Auto-generated method stub
                    if ((currentVideoIndex == lJsonInfos.size() - 1)
                            && cureNum >= lJsonInfos.get(currentVideoIndex)
                            .getActionNum()) {
                        //表示最后播放那一次的cal
                        train_calorie += lJsonInfos.get(currentVideoIndex)
                                .getActionCal();

                        hashMap.put("female_complete", videoDir + "/"
                                + "female_complete.wav");
                        trainVoicePrompt = new TrainVoicePrompt(
                                VideoViewMainActivity.this, "female", "end",
                                hashMap, mHandler, videoDir);
                        trainVoicePrompt.playVoice();
                        Date startDate = new Date(System.currentTimeMillis());
                        SimpleDateFormat formatter = new SimpleDateFormat(
                                "yyyy-MM-dd HH:mm:ss");
                        endTime = formatter.format(startDate);
                        return;
                    }

                    if (isYuPlay) {
                        if (cureNum >= lJsonInfos.get(currentVideoIndex)
                                .getActionNum()) {
                            //表示最后播放那一次的cal
                            train_calorie += lJsonInfos.get(currentVideoIndex)
                                    .getActionCal();


                            currentVideoIndex++;
                            cureNum = 1;
                            isPause = true;
                            if (lJsonInfos.get(currentVideoIndex - 1)
                                    .getSleepTime() == 0) {
                                // 表示没有中间休息时间
                                if (currentVideoIndex > 0
                                        && currentVideoIndex < lJsonInfos.size() - 1) {
                                    hashMap.put("actionFirst", videoDir + "/"
                                            + "female_next.wav");
                                    hashMap.put(
                                            "actionName",
                                            videoDir
                                                    + "/"
                                                    + getVoiceName(lJsonInfos.get(
                                                    currentVideoIndex)
                                                    .getPlayname())
                                                    + ".wav");
                                    if (lJsonInfos.get(currentVideoIndex)
                                            .getActionFlag() == 1) {
                                        // 表示是次数
                                        hashMap.put("actionNum",
                                                lJsonInfos.get(currentVideoIndex)
                                                        .getActionNum() + "");
                                    } else {
                                        // 表示是秒数
                                        hashMap.put(
                                                "actionNum",
                                                lJsonInfos.get(currentVideoIndex)
                                                        .getActionNum()
                                                        * lJsonInfos.get(
                                                        currentVideoIndex)
                                                        .getTime() + "");
                                    }

                                    hashMap.put("actionFlag",
                                            lJsonInfos.get(currentVideoIndex)
                                                    .getActionFlag() + "");
                                    hashMap.put("321go", videoDir + "/"
                                            + "female_321go.wav");
                                    trainVoicePrompt = new TrainVoicePrompt(
                                            VideoViewMainActivity.this, "female",
                                            "start", hashMap, mHandler, videoDir);
                                    trainVoicePrompt.playVoice();

                                    isYuPlay = false;
                                    isPause = true;
                                    mTempCount = recLen;
                                    main_videoview.setVideoPath(VideoListQueue
                                            .get(currentVideoIndex));
                                    // main_videoview.start();

                                } else if (currentVideoIndex == lJsonInfos.size() - 1) {
                                    hashMap.put("actionFirst", videoDir + "/"
                                            + "female_last.wav");
                                    hashMap.put(
                                            "actionName",
                                            videoDir
                                                    + "/"
                                                    + getVoiceName(lJsonInfos.get(
                                                    currentVideoIndex)
                                                    .getPlayname())
                                                    + ".wav");
                                    if (lJsonInfos.get(currentVideoIndex)
                                            .getActionFlag() == 1) {
                                        // 表示是次数
                                        hashMap.put("actionNum",
                                                lJsonInfos.get(currentVideoIndex)
                                                        .getActionNum() + "");
                                    } else {
                                        // 表示是秒数
                                        hashMap.put(
                                                "actionNum",
                                                lJsonInfos.get(currentVideoIndex)
                                                        .getActionNum()
                                                        * lJsonInfos.get(
                                                        currentVideoIndex)
                                                        .getTime() + "");
                                    }

                                    hashMap.put("actionFlag",
                                            lJsonInfos.get(currentVideoIndex)
                                                    .getActionFlag() + "");
                                    hashMap.put("321go", videoDir + "/"
                                            + "female_321go.wav");
                                    trainVoicePrompt = new TrainVoicePrompt(
                                            VideoViewMainActivity.this, "female",
                                            "start", hashMap, mHandler, videoDir);
                                    trainVoicePrompt.playVoice();

                                    isYuPlay = false;
                                    isPause = true;
                                    mTempCount = recLen;
                                    main_videoview.setVideoPath(VideoListQueue
                                            .get(currentVideoIndex));
                                    // main_videoview.start();
                                }
                            } else {
                                // 表示中间有休息时间
                                main_videoview.pause();
                                isPause = true;
                                mTempCount = recLen;
                                pause_btn
                                        .setImageResource(R.drawable.train_playstart);
                                sleepTime = lJsonInfos.get(currentVideoIndex - 1)
                                        .getSleepTime();
                                curSleepNum = sleepTime;
                                set_menu_background.setVisibility(View.VISIBLE);
                                set_music_background.setVisibility(View.GONE);
                                last_btn.setClickable(false);
                                next_btn.setClickable(false);
                                pause_btn.setClickable(false);
                                back_btn.setClickable(false);

                                background_sleeptime.setText(curSleepNum + "s");
                                background_train_action_nextname.setText(lJsonInfos
                                        .get(currentVideoIndex).getActionName());
                                hashMap.put("rest_little", videoDir + "/"
                                        + "female_rest_little.wav");
                                hashMap.put("rest", videoDir + "/"
                                        + "female_rest.wav");
                                hashMap.put("sleepTime", sleepTime + "");
                                trainVoicePrompt = new TrainVoicePrompt(
                                        VideoViewMainActivity.this, "female",
                                        "sleepTime", hashMap, mHandler, videoDir);
                                trainVoicePrompt.playVoice();

                            }
                        } else {
                            if (!train_actionList.contains(lJsonInfos.get(
                                    currentVideoIndex).getPosion())) {
                                train_actionList.add(lJsonInfos.get(
                                        currentVideoIndex).getPosion());
                            }
                            train_calorie += lJsonInfos.get(currentVideoIndex)
                                    .getActionCal();

                            cureNum++;
                            if (lJsonInfos.get(currentVideoIndex).getActionFlag() == 1) {
                                hashMap.put("cishu", cureNum + "");
                                trainVoicePrompt = new TrainVoicePrompt(
                                        VideoViewMainActivity.this, "female",
                                        "cishu", hashMap, mHandler, videoDir);
                                trainVoicePrompt.playVoice();
                            }
                            main_videoview.setVideoPath(VideoListQueue
                                    .get(currentVideoIndex));
                            main_videoview.requestFocus();
                            // main_videoview.start();
                        }

                        train_actionname.setText(lJsonInfos.get(currentVideoIndex)
                                .getActionName());
                        if (currentVideoIndex == lJsonInfos.size() - 1) {
                            train_action_nextname.setText(lJsonInfos.get(
                                    currentVideoIndex).getActionName());
                        } else {
                            train_action_nextname.setText(lJsonInfos.get(
                                    currentVideoIndex + 1).getActionName());
                        }

                        setPlayJinDu(
                                cureNum
                                        + "/"
                                        + lJsonInfos.get(currentVideoIndex)
                                        .getActionNum(), cureNum + "");
                    } else {
                        main_videoview.setVideoPath(VideoListQueue
                                .get(currentVideoIndex));
                        // main_videoview.start();
                    }

                }
            }

            );

            lsList=new ArrayList<Float>();
            float max = 0;
            MediaMetadataRetriever mmr;
            for(
            int i = 0;
            i<VideoListQueue.size();i++)

            {
                File f = new File(VideoListQueue.get(i));
                if (f.exists()) {
                    mmr = new MediaMetadataRetriever();
                    mmr.setDataSource(VideoListQueue.get(i));
                    String duration = mmr
                            .extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                    int aa = Integer.parseInt(duration);
                    float ss = (float) ((aa * lJsonInfos.get(i).getActionNum() * 1.0) / 1000L);
                    lsList.add(ss);
                    max = max + ss;
                } else {
                    Toast.makeText(mContext, "读取音频文件失败", Toast.LENGTH_LONG).show();
                    finish();
                }

            }

            File f2 = new File(videoDir + "/" + "female_54321go.wav");
            if(f2.exists())

            {
                mmr = new MediaMetadataRetriever();
                mmr.setDataSource(videoDir + "/" + "female_54321go.wav");
                String duration1 = mmr
                        .extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                num1 = (int) (Integer.parseInt(duration1) * 1.0 / 1000L);
            }

            File f4 = new File(videoDir + "/" + "female_54321.wav");
            if(f4.exists())

            {
                mmr = new MediaMetadataRetriever();
                mmr.setDataSource(videoDir + "/" + "female_54321.wav");
                String duration2 = mmr
                        .extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                num2 = (int) (Integer.parseInt(duration2) * 1.0 / 1000L);
            }

            File f = new File(videoDir + "/" + "female_other.wav");
            if(f.exists())

            {
                mmr = new MediaMetadataRetriever();
                mmr.setDataSource(videoDir + "/" + "female_other.wav");
                String duration3 = mmr
                        .extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                num3 = (int) (Integer.parseInt(duration3) * 1.0 / 1000L);
            }

            File f1 = new File(videoDir + "/" + "female_rest_ok.wav");
            if(f1.exists())

            {
                mmr = new MediaMetadataRetriever();
                mmr.setDataSource(videoDir + "/" + "female_rest_ok.wav");
                String duration4 = mmr
                        .extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                num4 = (int) (Integer.parseInt(duration4) * 1.0 / 1000L);
            }

            progress_viewid.setMax(max,lsList);
            task=new

            TimerTask() {
                public void run () {
                    if (!isSleep) {
                        if (isStart && !isPause) {
                            Message message = new Message();
                            message.what = 200;
                            mHandler.sendMessage(message);
                        }
                    } else {
                        Message message = new Message();
                        message.what = 300;
                        mHandler.sendMessage(message);
                    }

                }
            }

            ;
            timer=new Timer(true);
            // // 延时1000ms后执行，1000ms执行一次
            // timer.schedule(task, 0, 1000);
            // startTimeSeconds = System.currentTimeMillis();
        }

    private void getVideoUrls() {
        for (int i = 0; i < lJsonInfos.size(); i++) {
            VideoListQueue
                    .add(videoDir + "/" + lJsonInfos.get(i).getPlayname());
        }
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.last_btn:
                if (currentVideoIndex == 0) {
                    Toast.makeText(VideoViewMainActivity.this, "视频播放已经是第一个..",
                            Toast.LENGTH_SHORT).show();
                    if (cureNum <= 1) {
                        if (trainVoicePrompt != null) {
                            trainVoicePrompt.stopPlay();
                            trainVoicePrompt = null;
                        }
                        runNum = 0;
                        isYuPlay = false;
                        isPause = true;
                        hashMap.put("actionFirst", videoDir + File.separator
                                + "female_first.wav");
                        hashMap.put("actionName", videoDir
                                + File.separator
                                + getVoiceName(lJsonInfos.get(currentVideoIndex)
                                .getPlayname()) + ".wav");
                        if (lJsonInfos.get(currentVideoIndex).getActionFlag() == 1) {
                            // 表示是次数
                            hashMap.put("actionNum", lJsonInfos.get(currentVideoIndex)
                                    .getActionNum() + "");
                        } else {
                            // 表示是秒数
                            hashMap.put("actionNum", lJsonInfos.get(currentVideoIndex)
                                    .getActionNum()
                                    * lJsonInfos.get(currentVideoIndex).getTime() + "");
                        }

                        hashMap.put("actionFlag", lJsonInfos.get(currentVideoIndex)
                                .getActionFlag() + "");
                        hashMap.put("321go", videoDir + File.separator
                                + "female_321go.wav");
                        trainVoicePrompt = new TrainVoicePrompt(this, "female",
                                "start", hashMap, mHandler, videoDir);
                        trainVoicePrompt.playVoice();

                        cureNum = 1;
                        if (currentVideoIndex == 0) {
                            progress = 0;
                            mCuNum = 0;
                            progress_viewid.setProgress(progress);
                            // mFowLastCount = -recLen;
                        }
                        main_videoview.setVideoPath(VideoListQueue
                                .get(currentVideoIndex));
                        main_videoview.requestFocus();
                        // main_videoview.start();
                    }

                } else {
                    if (trainVoicePrompt != null) {
                        trainVoicePrompt.stopPlay();
                        trainVoicePrompt = null;
                    }
                    runNum = 0;
                    currentVideoIndex--;
                    isYuPlay = false;
                    isPause = true;
                    if (currentVideoIndex > 0
                            && currentVideoIndex < lJsonInfos.size() - 1) {
                        hashMap.put("actionFirst", videoDir + "/"
                                + "female_next.wav");
                        hashMap.put("actionName", videoDir
                                + "/"
                                + getVoiceName(lJsonInfos.get(currentVideoIndex)
                                .getPlayname()) + ".wav");
                        if (lJsonInfos.get(currentVideoIndex).getActionFlag() == 1) {
                            // 表示是次数
                            hashMap.put("actionNum",
                                    lJsonInfos.get(currentVideoIndex)
                                            .getActionNum() + "");
                        } else {
                            // 表示是秒数
                            hashMap.put(
                                    "actionNum",
                                    lJsonInfos.get(currentVideoIndex)
                                            .getActionNum()
                                            * lJsonInfos.get(currentVideoIndex)
                                            .getTime() + "");
                        }

                        hashMap.put("actionFlag", lJsonInfos.get(currentVideoIndex)
                                .getActionFlag() + "");
                        hashMap.put("321go", videoDir + "/" + "female_321go.wav");
                        trainVoicePrompt = new TrainVoicePrompt(
                                VideoViewMainActivity.this, "female", "start",
                                hashMap, mHandler, videoDir);
                        trainVoicePrompt.playVoice();
                    } else if (currentVideoIndex == lJsonInfos.size() - 1) {
                        hashMap.put("actionFirst", videoDir + "/"
                                + "female_last.wav");
                        hashMap.put("actionName", videoDir
                                + "/"
                                + getVoiceName(lJsonInfos.get(currentVideoIndex)
                                .getPlayname()) + ".wav");
                        if (lJsonInfos.get(currentVideoIndex).getActionFlag() == 1) {
                            // 表示是次数
                            hashMap.put("actionNum",
                                    lJsonInfos.get(currentVideoIndex)
                                            .getActionNum() + "");
                        } else {
                            // 表示是秒数
                            hashMap.put(
                                    "actionNum",
                                    lJsonInfos.get(currentVideoIndex)
                                            .getActionNum()
                                            * lJsonInfos.get(currentVideoIndex)
                                            .getTime() + "");
                        }

                        hashMap.put("actionFlag", lJsonInfos.get(currentVideoIndex)
                                .getActionFlag() + "");
                        hashMap.put("321go", videoDir + "/" + "female_321go.wav");
                        trainVoicePrompt = new TrainVoicePrompt(
                                VideoViewMainActivity.this, "female", "start",
                                hashMap, mHandler, videoDir);
                        trainVoicePrompt.playVoice();
                    } else if (currentVideoIndex == 0) {
                        hashMap.put("actionFirst", videoDir + File.separator
                                + "female_first.wav");
                        hashMap.put("actionName", videoDir
                                + File.separator
                                + getVoiceName(lJsonInfos.get(currentVideoIndex)
                                .getPlayname()) + ".wav");
                        if (lJsonInfos.get(currentVideoIndex).getActionFlag() == 1) {
                            // 表示是次数
                            hashMap.put("actionNum",
                                    lJsonInfos.get(currentVideoIndex)
                                            .getActionNum() + "");
                        } else {
                            // 表示是秒数
                            hashMap.put(
                                    "actionNum",
                                    lJsonInfos.get(currentVideoIndex)
                                            .getActionNum()
                                            * lJsonInfos.get(currentVideoIndex)
                                            .getTime() + "");
                        }

                        hashMap.put("actionFlag", lJsonInfos.get(currentVideoIndex)
                                .getActionFlag() + "");
                        hashMap.put("321go", videoDir + File.separator
                                + "female_321go.wav");
                        trainVoicePrompt = new TrainVoicePrompt(this, "female",
                                "start", hashMap, mHandler, videoDir);
                        trainVoicePrompt.playVoice();
                    }

                    cureNum = 1;
                    train_actionname.setText(lJsonInfos.get(currentVideoIndex)
                            .getActionName());
                    if (currentVideoIndex == lJsonInfos.size() - 1) {
                        train_action_nextname.setText(lJsonInfos.get(
                                currentVideoIndex).getActionName());
                    } else {
                        train_action_nextname.setText(lJsonInfos.get(
                                currentVideoIndex + 1).getActionName());
                    }

                    setPlayJinDu(cureNum + "/"
                                    + lJsonInfos.get(currentVideoIndex).getActionNum(),
                            cureNum + "");

                    if (currentVideoIndex == 0) {
                        progress = 0;
                        mCuNum = 0;
                        progress_viewid.setProgress(progress);
                        // mFowLastCount = -recLen;
                    } else {
                        progress = 0;
                        for (int i = 0; i < currentVideoIndex; i++) {
                            progress += lsList.get(i);
                        }
                        mCuNum = progress;
                        progress_viewid.setProgress(progress);
                    }
                    main_videoview.setVideoPath(VideoListQueue
                            .get(currentVideoIndex));
                    main_videoview.requestFocus();
                    // main_videoview.start();
                }
                break;
            case R.id.next_btn:
                if ((currentVideoIndex == lJsonInfos.size() - 1)) {
                    Toast.makeText(VideoViewMainActivity.this, "视频播放已经是最后一个..",
                            Toast.LENGTH_SHORT).show();
                    if (cureNum <= 1) {
                        if (trainVoicePrompt != null) {
                            trainVoicePrompt.stopPlay();
                            trainVoicePrompt = null;
                        }
                        hashMap.put("actionFirst", videoDir + "/" + "female_last.wav");
                        hashMap.put("actionName", videoDir
                                + "/"
                                + getVoiceName(lJsonInfos.get(currentVideoIndex)
                                .getPlayname()) + ".wav");
                        if (lJsonInfos.get(currentVideoIndex).getActionFlag() == 1) {
                            // 表示是次数
                            hashMap.put("actionNum", lJsonInfos.get(currentVideoIndex)
                                    .getActionNum() + "");
                        } else {
                            // 表示是秒数
                            hashMap.put("actionNum", lJsonInfos.get(currentVideoIndex)
                                    .getActionNum()
                                    * lJsonInfos.get(currentVideoIndex).getTime() + "");
                        }

                        hashMap.put("actionFlag", lJsonInfos.get(currentVideoIndex)
                                .getActionFlag() + "");
                        hashMap.put("321go", videoDir + "/" + "female_321go.wav");
                        trainVoicePrompt = new TrainVoicePrompt(
                                VideoViewMainActivity.this, "female", "start", hashMap,
                                mHandler, videoDir);
                        trainVoicePrompt.playVoice();

                        isYuPlay = false;
                        isPause = true;
                        mTempCount = recLen;
                        main_videoview.setVideoPath(VideoListQueue
                                .get(currentVideoIndex));
                        // main_videoview.start();
                    }

                } else {
                    if (trainVoicePrompt != null) {
                        trainVoicePrompt.stopPlay();
                        trainVoicePrompt = null;
                    }
                    runNum = 0;
                    currentVideoIndex++;
                    isYuPlay = false;
                    isPause = true;
                    if (currentVideoIndex > 0
                            && currentVideoIndex < lJsonInfos.size() - 1) {
                        hashMap.put("actionFirst", videoDir + "/"
                                + "female_next.wav");
                        hashMap.put("actionName", videoDir
                                + "/"
                                + getVoiceName(lJsonInfos.get(currentVideoIndex)
                                .getPlayname()) + ".wav");
                        if (lJsonInfos.get(currentVideoIndex).getActionFlag() == 1) {
                            // 表示是次数
                            hashMap.put("actionNum",
                                    lJsonInfos.get(currentVideoIndex)
                                            .getActionNum() + "");
                        } else {
                            // 表示是秒数
                            hashMap.put(
                                    "actionNum",
                                    lJsonInfos.get(currentVideoIndex)
                                            .getActionNum()
                                            * lJsonInfos.get(currentVideoIndex)
                                            .getTime() + "");
                        }

                        hashMap.put("actionFlag", lJsonInfos.get(currentVideoIndex)
                                .getActionFlag() + "");
                        hashMap.put("321go", videoDir + "/" + "female_321go.wav");
                        trainVoicePrompt = new TrainVoicePrompt(
                                VideoViewMainActivity.this, "female", "start",
                                hashMap, mHandler, videoDir);
                        trainVoicePrompt.playVoice();
                    } else if (currentVideoIndex == lJsonInfos.size() - 1) {
                        hashMap.put("actionFirst", videoDir + "/"
                                + "female_last.wav");
                        hashMap.put("actionName", videoDir
                                + "/"
                                + getVoiceName(lJsonInfos.get(currentVideoIndex)
                                .getPlayname()) + ".wav");
                        if (lJsonInfos.get(currentVideoIndex).getActionFlag() == 1) {
                            // 表示是次数
                            hashMap.put("actionNum",
                                    lJsonInfos.get(currentVideoIndex)
                                            .getActionNum() + "");
                        } else {
                            // 表示是秒数
                            hashMap.put(
                                    "actionNum",
                                    lJsonInfos.get(currentVideoIndex)
                                            .getActionNum()
                                            * lJsonInfos.get(currentVideoIndex)
                                            .getTime() + "");
                        }

                        hashMap.put("actionFlag", lJsonInfos.get(currentVideoIndex)
                                .getActionFlag() + "");
                        hashMap.put("321go", videoDir + "/" + "female_321go.wav");
                        trainVoicePrompt = new TrainVoicePrompt(
                                VideoViewMainActivity.this, "female", "start",
                                hashMap, mHandler, videoDir);
                        trainVoicePrompt.playVoice();
                    }

                    cureNum = 1;
                    train_actionname.setText(lJsonInfos.get(currentVideoIndex)
                            .getActionName());
                    if (currentVideoIndex == lJsonInfos.size() - 1) {
                        train_action_nextname.setText(lJsonInfos.get(
                                currentVideoIndex).getActionName());
                    } else {
                        train_action_nextname.setText(lJsonInfos.get(
                                currentVideoIndex + 1).getActionName());
                    }

                    setPlayJinDu(cureNum + "/"
                                    + lJsonInfos.get(currentVideoIndex).getActionNum(),
                            cureNum + "");

                    progress = 0;
                    for (int i = 0; i < currentVideoIndex; i++) {
                        progress += lsList.get(i);
                    }
                    mCuNum = progress;
                    progress_viewid.setProgress(progress);
                    mTempCount = recLen;
                    main_videoview.setVideoPath(VideoListQueue
                            .get(currentVideoIndex));
                    main_videoview.requestFocus();
                    // main_videoview.start();
                }
                break;
            case R.id.pause_btn:
                if (main_videoview.isPlaying()) {
                    main_videoview.pause();
                    if (trainVoiceBgPrompt != null) {
                        trainVoiceBgPrompt.stopPlay();
                    }
                    isPause = true;
                    isScreen = true;
                    startTimeSeconds = System.currentTimeMillis();
                    mTempCount = recLen;
                    pause_btn.setImageResource(R.drawable.train_playstart);
                    pause_menu_background.setVisibility(View.VISIBLE);
                    background_train_action_cruname.setText(lJsonInfos.get(
                            currentVideoIndex).getActionName());
                    last_btn.setClickable(false);
                    next_btn.setClickable(false);
                    pause_btn.setClickable(false);
                    back_btn.setClickable(false);
                    music_btn.setClickable(false);
                }
                if (trainVoicePrompt != null) {
                    trainVoicePrompt.stopOtherPlay();
                }
                // else {
                // main_videoview.start();
                // isPause = false;
                // pause_btn.setImageResource(R.drawable.train_playpause);
                // startTimeSeconds = System.currentTimeMillis();
                // mTempCount = recLen;
                // }
                break;

            case R.id.trainback_btn:
                backPopWindow();
                break;
            case R.id.bt_ok:
                if (myWindow != null) {
                    myWindow.dismiss();
                }
                break;
            case R.id.bt_cancel:
                if (myWindow != null) {
                    myWindow.dismiss();
                }
                comPlePopWindow();
                break;
            case R.id.train_bt_ok:
                if (comPleteWindow != null) {
                    comPleteWindow.dismiss();
                }
                if (mSportsApp.isOpenNetwork()) {
                    if (train_actionList.size() > 0) {
                        if (mUploadDialog != null && !mUploadDialog.isShowing())
                            mUploadDialog.show();
                        Date startDate = new Date(System.currentTimeMillis());
                        SimpleDateFormat formatter = new SimpleDateFormat(
                                "yyyy-MM-dd HH:mm:ss");
                        endTime = formatter.format(startDate);
                        uploadTrainTask();
                    } else {
                        finish();
                    }

                } else {
                    finish();
                }

                break;
            case R.id.music_btn:
                set_music_background.setVisibility(View.VISIBLE);
                last_btn.setClickable(false);
                next_btn.setClickable(false);
                pause_btn.setClickable(false);
                back_btn.setClickable(false);
                break;
            case R.id.close_set_music:
                set_music_background.setVisibility(View.GONE);
                last_btn.setClickable(true);
                next_btn.setClickable(true);
                pause_btn.setClickable(true);
                back_btn.setClickable(true);
                break;
            case R.id.start_play_layout:
                pause_menu_background.setVisibility(View.GONE);

                main_videoview.start();
                if (trainVoiceBgPrompt != null) {
                    trainVoiceBgPrompt.startPlay();
                }
                if (trainVoicePrompt != null) {
                    trainVoicePrompt.startPlay();
                }
                isPause = false;
                isScreen = false;
                pause_btn.setImageResource(R.drawable.train_playpause);
                startTimeSeconds = System.currentTimeMillis();
                mTempCount = recLen;
                last_btn.setClickable(true);
                next_btn.setClickable(true);
                pause_btn.setClickable(true);
                back_btn.setClickable(true);
                music_btn.setClickable(true);
                break;
            case R.id.train_sleep_layout:
                if (trainVoicePrompt != null) {
                    trainVoicePrompt.stopPlay();
                }
                isSleep = false;
                curSleepNum = 0;
                set_menu_background.setVisibility(View.GONE);
                last_btn.setClickable(true);
                next_btn.setClickable(true);
                pause_btn.setClickable(true);
                back_btn.setClickable(true);
                pause_btn.setImageResource(R.drawable.train_playpause);
                if (currentVideoIndex > 0
                        && currentVideoIndex < lJsonInfos.size() - 1) {
                    hashMap.put("actionFirst", videoDir + "/" + "female_next.wav");
                    hashMap.put("actionName", videoDir
                            + "/"
                            + getVoiceName(lJsonInfos.get(currentVideoIndex)
                            .getPlayname()) + ".wav");
                    if (lJsonInfos.get(currentVideoIndex).getActionFlag() == 1) {
                        // 表示是次数
                        hashMap.put("actionNum", lJsonInfos.get(currentVideoIndex)
                                .getActionNum() + "");
                    } else {
                        // 表示是秒数
                        hashMap.put("actionNum", lJsonInfos.get(currentVideoIndex)
                                .getActionNum()
                                * lJsonInfos.get(currentVideoIndex).getTime() + "");
                    }

                    hashMap.put("actionFlag", lJsonInfos.get(currentVideoIndex)
                            .getActionFlag() + "");
                    hashMap.put("321go", videoDir + "/" + "female_321go.wav");
                    trainVoicePrompt = new TrainVoicePrompt(
                            VideoViewMainActivity.this, "female", "start", hashMap,
                            mHandler, videoDir);
                    trainVoicePrompt.playVoice();

                    isYuPlay = false;
                    isPause = true;
                    mTempCount = recLen;
                    main_videoview.setVideoPath(VideoListQueue
                            .get(currentVideoIndex));
                    // main_videoview.start();

                } else if (currentVideoIndex == lJsonInfos.size() - 1) {
                    hashMap.put("actionFirst", videoDir + "/" + "female_last.wav");
                    hashMap.put("actionName", videoDir
                            + "/"
                            + getVoiceName(lJsonInfos.get(currentVideoIndex)
                            .getPlayname()) + ".wav");
                    if (lJsonInfos.get(currentVideoIndex).getActionFlag() == 1) {
                        // 表示是次数
                        hashMap.put("actionNum", lJsonInfos.get(currentVideoIndex)
                                .getActionNum() + "");
                    } else {
                        // 表示是秒数
                        hashMap.put("actionNum", lJsonInfos.get(currentVideoIndex)
                                .getActionNum()
                                * lJsonInfos.get(currentVideoIndex).getTime() + "");
                    }

                    hashMap.put("actionFlag", lJsonInfos.get(currentVideoIndex)
                            .getActionFlag() + "");
                    hashMap.put("321go", videoDir + "/" + "female_321go.wav");
                    trainVoicePrompt = new TrainVoicePrompt(
                            VideoViewMainActivity.this, "female", "start", hashMap,
                            mHandler, videoDir);
                    trainVoicePrompt.playVoice();

                    isYuPlay = false;
                    isPause = true;
                    mTempCount = recLen;
                    main_videoview.setVideoPath(VideoListQueue
                            .get(currentVideoIndex));
                    // main_videoview.start();
                }

                break;
            default:
                break;
        }
    }

    private Timer timer;
    private TimerTask task;
    private long startTimeSeconds;
    private String startTime, endTime;
    private int recLen = 0;
    private float progress = 0;// 当前进度
    private int mTempCount = 0;
    private float mCuNum = 0;// 进度条的值
    private int curSleepNum = 0;// 睡眠时长的累加
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 200:
                    recLen = (int) (mTempCount + (System.currentTimeMillis() - startTimeSeconds) / 1000L);
                    if (recLen % (1 * 10) == 0) {
                        if (train_actionList.size() > 0) {
                            Date startDate = new Date(System.currentTimeMillis());
                            SimpleDateFormat formatter = new SimpleDateFormat(
                                    "yyyy-MM-dd HH:mm:ss");
                            endTime = formatter.format(startDate);
                            save(false);
                        }
                    }

                    float mm = 0;
                    for (int i = 0; i <= currentVideoIndex; i++) {
                        mm += lsList.get(i);
                    }
                    mCuNum++;
                    if ((mCuNum) <= mm) {
                        progress_viewid.setProgress(mCuNum);
                    } else {
                        mCuNum--;
                    }
                    train_time.setText(SportTaskUtil.showMsCount(recLen));
                    runNum += 1;
                    int ss = (int) (lsList.get(currentVideoIndex) - (num1 + num2-1));
                    if (runNum == ss) {
                        if (lJsonInfos.get(currentVideoIndex).getActionFlag() == 0) {
                            hashMap.put("54321go", videoDir + "/"
                                    + "female_54321go.wav");
                            hashMap.put("54321", videoDir + "/"
                                    + "female_54321.wav");

                            trainVoicePrompt = new TrainVoicePrompt(
                                    VideoViewMainActivity.this, "female",
                                    "jianchi", hashMap, mHandler, videoDir);
                            trainVoicePrompt.playVoice();
                        }
                    }
                    if (lsList.get(currentVideoIndex) >= (num1 + num2 + num3)) {
                        if (lJsonInfos.get(currentVideoIndex).getOtherFlag() == 1) {
                            if (runNum == 2) {
                                hashMap.put("female_other", videoDir + "/"
                                        + "female_other.wav");

                                trainVoicePrompt = new TrainVoicePrompt(
                                        VideoViewMainActivity.this, "female",
                                        "other", hashMap, mHandler, videoDir);
                                trainVoicePrompt.playVoice();
                            }
                        }
                    }
                    break;
                case VOICEPLAY_ONE:
                    isPause = false;
                    isYuPlay = true;
                    startTimeSeconds = System.currentTimeMillis();
                    mTempCount = recLen;
                    main_videoview.setVideoPath(VideoListQueue
                            .get(currentVideoIndex));
                    // main_videoview.requestFocus();
                    // main_videoview.start();
                    break;
                case VOICEPLAY_TWO:
                    isPause = false;
                    isYuPlay = true;
                    runNum = 0;
                    if (!isStart) {
                        isStart = true;
                        isPause = false;
                        // 延时1000ms后执行，1000ms执行一次
                        timer.schedule(task, 0, 1000);
                        startTimeSeconds = System.currentTimeMillis();
                        Date startDate = new Date(startTimeSeconds);
                        SimpleDateFormat formatter = new SimpleDateFormat(
                                "yyyy-MM-dd HH:mm:ss");
                        startTime = formatter.format(startDate);
                        markCode = SportsApp.getInstance().getSportUser().getUid()
                                + UUIDGenerator.getUUID();

                        // 启动激活进程 防止被踢下线
                        new AsyncTask<Void, Void, Boolean>() {
                            @Override
                            protected Boolean doInBackground(Void... params) {
                                // TODO Auto-generated method stub
                                Api.addUserLoginTime(mSportsApp.getSessionId());
                                return true;
                            }

                            @Override
                            protected void onPostExecute(Boolean result) {
                                // TODO Auto-generated method stub

                            }
                        }.execute();

                    } else {
                        startTimeSeconds = System.currentTimeMillis();
                    }
                    main_videoview.setVideoPath(VideoListQueue
                            .get(currentVideoIndex));
                    // main_videoview.requestFocus();
                    // main_videoview.start();
                    if (lJsonInfos.get(currentVideoIndex).getActionFlag() == 1) {
                        hashMap.put("cishu", cureNum + "");
                        trainVoicePrompt = new TrainVoicePrompt(
                                VideoViewMainActivity.this, "female", "cishu",
                                hashMap, mHandler, videoDir);
                        trainVoicePrompt.playVoice();
                    }
                    break;
                case VOICEPLAY_THREE:
                    Intent intent = new Intent(VideoViewMainActivity.this,
                            TrainCompleteActivity.class);
                    TrainResultsInfo resultsInfo = new TrainResultsInfo();
                    resultsInfo.setSessionId(mSportsApp.getSessionId());
                    resultsInfo.setTrain_id(train_id);
                    resultsInfo.setTraintime(recLen);
                    resultsInfo.setTrain_calorie(train_calorie);
                    final StringBuffer train_action = new StringBuffer();
                    if (train_actionList.size() != 0) {
                        if (train_actionList.size() == 1) {
                            train_action.append(train_actionList.get(0));
                        } else {
                            for (int j = 0; j < train_actionList.size(); j++) {
                                if (j == 0) {
                                    train_action.append(train_actionList.get(0));
                                } else {
                                    train_action.append(","
                                            + train_actionList.get(j));
                                }
                            }
                        }
                    }
                    resultsInfo.setTrain_action(train_action.toString());
                    resultsInfo.setTrain_completion(train_actionList.size() * 100
                            / lJsonInfos.size());
                    resultsInfo.setTrain_position(train_name);
                    resultsInfo.setTrain_starttime(startTime);
                    resultsInfo.setTrain_endtime(endTime);
                    resultsInfo.setIs_total(1);
                    resultsInfo.setUnique_id(markCode);
                    resultsInfo.setDatasource("Android");
                    intent.putExtra("TrainResultsInfo", resultsInfo);
                    intent.putExtra("train_actionList", train_actionList.size());
                    startActivity(intent);
                    if (timer != null) {
                        timer.cancel();
                    }
                    finish();
                    break;
                case 300:
                    curSleepNum -= 1;
                    background_sleeptime.setText(curSleepNum + "s");
                    if (curSleepNum == num4) {
                        hashMap.put("rest_ok", videoDir + "/"
                                + "female_rest_ok.wav");
                        trainVoicePrompt = new TrainVoicePrompt(
                                VideoViewMainActivity.this, "female", "rest_ok",
                                hashMap, mHandler, videoDir);
                        trainVoicePrompt.playVoice();
                    }
                    if (curSleepNum == 0) {
                        isSleep = false;
                        curSleepNum = 0;
                        set_menu_background.setVisibility(View.GONE);
                        last_btn.setClickable(true);
                        next_btn.setClickable(true);
                        pause_btn.setClickable(true);
                        back_btn.setClickable(true);
                        pause_btn.setImageResource(R.drawable.train_playpause);
                        if (currentVideoIndex > 0
                                && currentVideoIndex < lJsonInfos.size() - 1) {
                            hashMap.put("actionFirst", videoDir + "/"
                                    + "female_next.wav");
                            hashMap.put("actionName", videoDir
                                    + "/"
                                    + getVoiceName(lJsonInfos
                                    .get(currentVideoIndex).getPlayname())
                                    + ".wav");
                            if (lJsonInfos.get(currentVideoIndex).getActionFlag() == 1) {
                                // 表示是次数
                                hashMap.put("actionNum",
                                        lJsonInfos.get(currentVideoIndex)
                                                .getActionNum() + "");
                            } else {
                                // 表示是秒数
                                hashMap.put(
                                        "actionNum",
                                        lJsonInfos.get(currentVideoIndex)
                                                .getActionNum()
                                                * lJsonInfos.get(currentVideoIndex)
                                                .getTime() + "");
                            }

                            hashMap.put("actionFlag",
                                    lJsonInfos.get(currentVideoIndex)
                                            .getActionFlag() + "");
                            hashMap.put("321go", videoDir + "/"
                                    + "female_321go.wav");
                            trainVoicePrompt = new TrainVoicePrompt(
                                    VideoViewMainActivity.this, "female", "start",
                                    hashMap, mHandler, videoDir);
                            trainVoicePrompt.playVoice();

                            isYuPlay = false;
                            isPause = true;
                            mTempCount = recLen;
                            main_videoview.setVideoPath(VideoListQueue
                                    .get(currentVideoIndex));
                            // main_videoview.start();

                        } else if (currentVideoIndex == lJsonInfos.size() - 1) {
                            hashMap.put("actionFirst", videoDir + "/"
                                    + "female_last.wav");
                            hashMap.put("actionName", videoDir
                                    + "/"
                                    + getVoiceName(lJsonInfos
                                    .get(currentVideoIndex).getPlayname())
                                    + ".wav");
                            if (lJsonInfos.get(currentVideoIndex).getActionFlag() == 1) {
                                // 表示是次数
                                hashMap.put("actionNum",
                                        lJsonInfos.get(currentVideoIndex)
                                                .getActionNum() + "");
                            } else {
                                // 表示是秒数
                                hashMap.put(
                                        "actionNum",
                                        lJsonInfos.get(currentVideoIndex)
                                                .getActionNum()
                                                * lJsonInfos.get(currentVideoIndex)
                                                .getTime() + "");
                            }

                            hashMap.put("actionFlag",
                                    lJsonInfos.get(currentVideoIndex)
                                            .getActionFlag() + "");
                            hashMap.put("321go", videoDir + "/"
                                    + "female_321go.wav");
                            trainVoicePrompt = new TrainVoicePrompt(
                                    VideoViewMainActivity.this, "female", "start",
                                    hashMap, mHandler, videoDir);
                            trainVoicePrompt.playVoice();

                            isYuPlay = false;
                            isPause = true;
                            mTempCount = recLen;
                            main_videoview.setVideoPath(VideoListQueue
                                    .get(currentVideoIndex));
                            // main_videoview.start();
                        }

                    }
                    break;
                case VOICEPLAY_FOUR:
                    isSleep = true;
                    break;
                case UPLOAD_RETURN:
                    if (mUploadDialog != null && mUploadDialog.isShowing()) {
                        mUploadDialog.dismiss();
                    }
                    boolean sta = (Boolean) msg.obj;
                    if (sta) {
                        isUpload = 1;
                        Toast.makeText(VideoViewMainActivity.this, "上传成功",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        isUpload = 0;
                        Toast.makeText(VideoViewMainActivity.this, "上传失败",
                                Toast.LENGTH_SHORT).show();
                    }
                    save(false);
                    finish();
                    break;
                default:
                    break;
            }

        }

        ;
    };

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if(readJsonFile!=null&&!"".equals(readJsonFile)){
            if (!isScreen) {
                last_btn.setClickable(true);
                next_btn.setClickable(true);
                pause_btn.setClickable(true);
                back_btn.setClickable(true);
                if (main_videoview != null && !main_videoview.isPlaying()
                        && isPause) {
                    main_videoview.start();
                    isPause = false;
                    startTimeSeconds = System.currentTimeMillis();
                    mTempCount = recLen;
                }
                if (trainVoiceBgPrompt != null) {
                    trainVoiceBgPrompt.startPlay();
                } else {
                    File f3 = new File(videoDir + "/" + "female_bjy.mp3");
                    if (f3.exists()) {
                        trainVoiceBgPrompt = new TrainVoiceBgPrompt(this, "female",
                                "train_bg", videoDir);
                        trainVoiceBgPrompt.playVoice(curVolume);
                    }
                }
            }
        }else{
            Toast.makeText(this,"文件解析错误",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
        if (trainVoicePrompt != null) {
            trainVoicePrompt.stopPlay();
        }
        if (myWindow != null) {
            myWindow.dismiss();
            myWindow = null;
        }
        if (comPleteWindow != null) {
            comPleteWindow.dismiss();
            comPleteWindow = null;
        }
        if (trainVoiceBgPrompt != null) {
            trainVoiceBgPrompt.destroyPlay();
        }
        if (db != null) {
            db.close();
        }
        if (screenListener != null) {
            screenListener.unregisterListener();
        }

        if(callReceiver!=null){
            unregisterReceiver(callReceiver);
            callReceiver=null;
        }
        mHandler.removeCallbacksAndMessages(null);
        VideoListQueue=null;
        lJsonInfos=null;
        train_actionList=null;
        hashMap=null;
        lsList=null;
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (main_videoview != null && main_videoview.isPlaying()) {
            main_videoview.pause();
            isPause = true;
            startTimeSeconds = System.currentTimeMillis();
            mTempCount = recLen;
        } else {
            isScreen = true;
        }
        if (trainVoiceBgPrompt != null) {
            trainVoiceBgPrompt.stopPlay();
        }
        if (trainVoicePrompt != null) {
            trainVoicePrompt.stopOtherPlay();
        }

    }

    //
    private void setPlayJinDu(String all, String position) {
        SpannableString styledText = new SpannableString(all);
        styledText.setSpan(new TextAppearanceSpan(this, R.style.index_style3),
                0, position.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        train_position.setText(styledText, TextView.BufferType.SPANNABLE);
    }

    // 截取视频文件名字
    private String getVoiceName(String playName) {
        String[] split = playName.split("\\.");
        if (split != null) {
            return split[0];
        } else {
            return "";
        }
    }

    private void backPopWindow() {
        if (myWindow != null && myWindow.isShowing())
            return;
        LayoutInflater inflater = LayoutInflater.from(this);
        myView = (LinearLayout) inflater.inflate(R.layout.sports_dialog3, null);

        myView.findViewById(R.id.bt_ok).setOnClickListener(this);
        myView.findViewById(R.id.bt_cancel).setOnClickListener(this);
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        // int height = wm.getDefaultDisplay().getHeight();
        myWindow = new PopupWindow(myView, width / 2,
                RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        myWindow.setAnimationStyle(R.style.AnimationPopup);
        myWindow.setOutsideTouchable(true);
        myWindow.setBackgroundDrawable(new BitmapDrawable());
        myWindow.showAtLocation(back_btn, Gravity.CENTER, 0, 0);
        // myWindow.setOnDismissListener(this);
        // final Animation animation = (Animation) AnimationUtils.loadAnimation(
        // this, R.anim.slide_in_from_bottom);
        // myView.startAnimation(animation);
        // mPopMenuBack.setVisibility(View.VISIBLE);
    }

    private void comPlePopWindow() {
        if (comPleteWindow != null && comPleteWindow.isShowing())
            return;
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout view = (LinearLayout) inflater.inflate(
                R.layout.sports_dialog4, null);
        TextView train_time = (TextView) view.findViewById(R.id.train_time);
        TextView train_actionnum = (TextView) view
                .findViewById(R.id.train_actionnum);
        TextView train_cal = (TextView) view.findViewById(R.id.train_cal);
        train_time.setText((recLen / 60) + "分钟");
        train_actionnum.setText(train_actionList.size() + "个动作");
        train_cal.setText(SportTaskUtil.getDoubleOneNum(train_calorie) + "Cal");
        view.findViewById(R.id.train_bt_ok).setOnClickListener(this);
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        // int height = wm.getDefaultDisplay().getHeight();
        comPleteWindow = new PopupWindow(view, width / 2,
                RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        comPleteWindow.setAnimationStyle(R.style.AnimationPopup);
        comPleteWindow.setOutsideTouchable(true);
        comPleteWindow.setBackgroundDrawable(new BitmapDrawable());
        comPleteWindow.showAtLocation(back_btn, Gravity.CENTER, 0, 0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK: {
                backPopWindow();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    // 保存本地数据库
    private int save(boolean isShow) {
        db = TrainPlanDataBase.getInstance(mContext.getApplicationContext());

        StringBuffer train_action = new StringBuffer();
        if (train_actionList.size() != 0) {
            if (train_actionList.size() == 1) {
                train_action.append(train_actionList.get(0));
            } else {
                for (int j = 0; j < train_actionList.size(); j++) {
                    if (j == 0) {
                        train_action.append(train_actionList.get(0));
                    } else {
                        train_action.append("," + train_actionList.get(j));
                    }
                }
            }
        }

        Date startDate = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        endTime = formatter.format(startDate);

        ContentValues values = new ContentValues();
        values.put(TrainPlanDataBase.UID_I, mSportsApp.getSportUser().getUid());
        values.put(TrainPlanDataBase.TRAIN_ID_I, train_id);
        values.put(TrainPlanDataBase.TRAIN_TIME_I, recLen);
        values.put(TrainPlanDataBase.TRAIN_CALORIE_D, train_calorie);
        values.put(TrainPlanDataBase.TRAIN_ACTION_S, train_action.toString());
        values.put(TrainPlanDataBase.TRAIN_POSITION_S, train_name);
        values.put(TrainPlanDataBase.TRAIN_COMPLETION_I,
                train_actionList.size() * 100 / lJsonInfos.size());
        values.put(TrainPlanDataBase.TRAIN_STARTTIME_S, startTime);
        values.put(TrainPlanDataBase.TRAIN_ENDTIME_S, endTime);
        values.put(TrainPlanDataBase.IS_TOTAL_I, 1);
        values.put(TrainPlanDataBase.IS_UPLOAD_I, isUpload);
        values.put(TrainPlanDataBase.TRAIN_MARKCODE, markCode);

        int result = 0;
        if (isFirstSave) {
            result = db.insert(values, false);
            if (result > 0) {
                isFirstSave = false;
            }
        } else {
            result = db.update(values, mSportsApp.getSportUser().getUid(),
                    startTime, false, markCode);
        }

        return result;

    }

    public Dialog onCreateDialog(int id, Bundle bundle, final int taskid) {
        String message = bundle.getString("message");
        switch (id) {
            case 1:
                Dialog loginPregressDialog = new Dialog(mContext,
                        R.style.sports_dialog);
                LayoutInflater mInflater = mContext.getLayoutInflater();
                View v1 = mInflater.inflate(R.layout.sports_progressdialog, null);
                TextView msg = (TextView) v1.findViewById(R.id.message);
                msg.setText(message);
                v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
                loginPregressDialog.setContentView(v1);
                loginPregressDialog.setCancelable(false);
                loginPregressDialog.setCanceledOnTouchOutside(false);
                loginPregressDialog.setOnKeyListener(new OnKeyListener() {

                    @Override
                    public boolean onKey(DialogInterface arg0, int keyCode,
                                         KeyEvent event) {
                        // TODO Auto-generated method stub
                        if (keyCode == KeyEvent.KEYCODE_BACK
                                && event.getRepeatCount() == 0) {
                            return true;
                        }
                        return false;
                    }
                });
                return loginPregressDialog;
        }
        return null;
    }

    private void uploadTrainTask() {
        new Thread() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                StringBuffer train_action = new StringBuffer();
                if (train_actionList.size() != 0) {
                    if (train_actionList.size() == 1) {
                        train_action.append(train_actionList.get(0));
                    } else {
                        for (int j = 0; j < train_actionList.size(); j++) {
                            if (j == 0) {
                                train_action.append(train_actionList.get(0));
                            } else {
                                train_action.append(","
                                        + train_actionList.get(j));
                            }
                        }
                    }
                }
                ApiMessage apiMessage = ApiJsonParser.addTrainRecord(
                        mSportsApp.getSessionId(), train_id, recLen,
                        train_calorie, train_action.toString(), train_name,
                        train_actionList.size() * 100 / lJsonInfos.size(),
                        startTime, endTime, 1, markCode);
                msg.what = UPLOAD_RETURN;
                msg.obj = apiMessage.isFlag();
                mHandler.sendMessage(msg);
            }
        }.start();
    }


    //监听来电
    public  class CallReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //呼入电话
            if(action.equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)){
                doReceivePhone(context);
            }
        }
    }


    /**
     * 处理电话广播.
     * @param context
     *
     */
    public void doReceivePhone(Context context) {
        //来电暂停
        if (main_videoview.isPlaying()) {
            main_videoview.pause();
            if (trainVoiceBgPrompt != null) {
                trainVoiceBgPrompt.stopPlay();
            }
            isPause = true;
            isScreen = true;
            startTimeSeconds = System.currentTimeMillis();
            mTempCount = recLen;
            pause_btn.setImageResource(R.drawable.train_playstart);
            pause_menu_background.setVisibility(View.VISIBLE);
            background_train_action_cruname.setText(lJsonInfos.get(
                    currentVideoIndex).getActionName());
            last_btn.setClickable(false);
            next_btn.setClickable(false);
            pause_btn.setClickable(false);
            back_btn.setClickable(false);
            music_btn.setClickable(false);
        }
        if (trainVoicePrompt != null) {
            trainVoicePrompt.stopOtherPlay();
        }


        TelephonyManager telephony =
                (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        int state = telephony.getCallState();
        switch(state){
            case TelephonyManager.CALL_STATE_RINGING:
                //等待接电话
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                //电话挂断
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                //通话中
                break;
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
