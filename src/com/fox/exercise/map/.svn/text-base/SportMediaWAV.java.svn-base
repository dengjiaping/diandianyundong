package com.fox.exercise.map;

import java.io.IOException;

import cn.ingenic.indroidsync.SportsApp;

import com.fox.exercise.R;
import com.fox.exercise.api.ApiConstant;
import com.fox.exercise.util.ImageFileUtil;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


public class SportMediaWAV implements View.OnClickListener {

    private static final String TAG = "SportMediaFileDetailActivity";

    Activity mContext;

    MediaPlayer mPlayer = null;
    //音频下载路径
    public static String DOWNLOAD_SAVE_PATH = "Android/data/" + SportsApp.getContext().getPackageName() + "/cache/.download/video";
    //语音本地路径
    private String mediaPath = null;
    //语音辅助类
    private RecordHelper mRecorder;
    //音频播放持续时长
    private int currentDuration = 0;
    //音频播放的触发按钮
    private LinearLayout recodButton;
    //音频播放的时间长度
    private TextView wavDurations;
    //音频播放的状态图标
    private ImageView wavBegin;
    //声音开始的状态图片
    private ImageView mWavBegin;
    //播放是否被暂停
    private boolean isPause;
    //是否在播放
    private boolean isWaving;
    //音频默认没有开始读取
    private boolean mIsLoading;
    //音频默认没有开始下载
    private boolean mIsNotLoadVoice;
    //是否准备开始
    private boolean isStart;
    //进度条
    private ProgressBar progress;
    //media各种状态的主线程
    private MyHandler mHandler = null;

    private int type;

    private static final int RECORD_LOADING = 1;
    private static final int RECORD_PREPARED = 2;
    private static final int RECORD_PAUSE = 3;
    private static final int RECORD_FINISH = 4;
    private static final int RECORD_ERROR = 5;
    private final static int PROGRESS_START = 6;
    public final static int PROGRESS_UPDATE = 7;
    private final static int PROGRESS_FINISH = 8;
    private final static int START_PLAY = 9;

    final Object lock = new Object();

    SportMediaWAV(Activity context) {
        mContext = context;
    }

    void setCurentView(View view, int durations, String path) {
        release();
        mRecorder = new RecordHelper();
        mHandler = new MyHandler();
        mPlayer = new MediaPlayer();
        recodButton = (LinearLayout) view.findViewById(R.id.recoding_click);
        wavDurations = (TextView) view.findViewById(R.id.wav_durations);
        wavBegin = (ImageView) view.findViewById(R.id.wav_begin);
        progress = (ProgressBar) view.findViewById(R.id.progress_wav);
        recodButton.setOnClickListener(this);

        progress.setProgress(0);

        mediaPath = path;
        wavDurations.setText("" + durations + "″");
        if (durations < 1) {
//			recodButton.setVisibility(View.GONE);
        }
    }

    //	public int calculationduration(int durations)
//	{
//		int time = durations/1000;
//		if(durations%1000 > 0)
//		{
//			time++;
//		}
//		return time;
//	}
//
    public void release() {
        isPause = false;
        isWaving = false;
        mIsLoading = false;
        isStart = true;
        mIsNotLoadVoice = true;

        if (mRecorder != null) {
            mRecorder.stopPlayback();
            mRecorder = null;
        }
//		if (mPlayer != null) {
//			try {
//				mPlayer.stop();
//			} catch (IllegalStateException e) {
//				e.printStackTrace();
//			}
//		}
//		if (mPlayer != null) {
//			mPlayer.release();
//			mPlayer = null;
//		}
    }

    public void resetView(View v) {
        ProgressBar progresss = (ProgressBar) v.findViewById(R.id.progress_wav);
        progresss.setMax(100);
        progresss.setProgress(0);
        recodButton.setOnClickListener(null);
        mediaPath = "";
        wavDurations.setText("");
        mHandler.sendMessage(mHandler.obtainMessage(RECORD_FINISH, wavBegin));
    }

    void initView(View view, int durations, String path) {
        recodButton = (LinearLayout) view.findViewById(R.id.recoding_click);
        wavDurations = (TextView) view.findViewById(R.id.wav_durations);
        wavBegin = (ImageView) view.findViewById(R.id.wav_begin);

        recodButton.setOnClickListener(this);

        mediaPath = path;

        //wavDurations.setText("" + durations + "″");
        if (durations < 1) {
//			recodButton.setVisibility(View.GONE);
        }

        isPause = false;
        isWaving = false;
        mIsLoading = false;
        isStart = true;
        mIsNotLoadVoice = true;
    }

    public static String getUrlFileName(String url) {
        int l = url.lastIndexOf("/") + 1;
        return url.substring(l);
    }

    //MEDIA主线程
    private class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            if (!Thread.currentThread().isInterrupted()) {
                mWavBegin = ((ImageView) msg.obj);
                switch (msg.what) {
                    case RECORD_LOADING:
                        mIsLoading = true;
                        wavBegin.setImageResource(+R.anim.record_loading);
                        AnimationDrawable ad1 = (AnimationDrawable) wavBegin.getDrawable();
                        ad1.start();
                        break;
                    case RECORD_PREPARED:
                        mWavBegin.setImageResource(+R.anim.record_run);
                        AnimationDrawable ad2 = (AnimationDrawable) mWavBegin.getDrawable();
                        ad2.start();
                        break;
                    case RECORD_PAUSE:
                        mWavBegin.setImageResource(R.drawable.audio_play);
                        mIsLoading = false;
                        isStart = true;
                        break;
                    case RECORD_FINISH:
                    case RECORD_ERROR:
                        mWavBegin.setImageResource(R.drawable.audio_play);
                        break;
                    case START_PLAY:
                        playVoice();
                        break;
                }
            }
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (!Thread.currentThread().isInterrupted()) {
                switch (msg.what) {
                    case PROGRESS_START:
                        progress.setMax((Integer) msg.obj);
                        progress.setProgress(0);
                    case PROGRESS_UPDATE:
                        progress.setProgress(msg.arg1);
                        break;
                    case PROGRESS_FINISH:
                        progress.setProgress(0);
                        break;
                }
            }
            super.handleMessage(msg);
        }
    };

    public void progressRun() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mPlayer != null && RecordHelper.flag && !isPause) {
                    try {
                        Thread.sleep(50);
                        Message msg = new Message();
                        msg.what = PROGRESS_UPDATE;
                        try {
                            msg.arg1 = mPlayer.getCurrentPosition();
                            if (msg.arg1 == type && !isWaving) {
                                Message.obtain(handler, PROGRESS_FINISH, 0).sendToTarget();
                                break;
                            }
                            type = msg.arg1;
                        } catch (Exception e) {
                            Message.obtain(handler, PROGRESS_FINISH, 0).sendToTarget();
                            break;
                        }
                        handler.sendMessage(msg);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Message.obtain(handler, PROGRESS_FINISH, 0).sendToTarget();
                        break;
                    }
                }
                if (!RecordHelper.flag || !isPause) {
                    Message.obtain(handler, PROGRESS_FINISH, 0).sendToTarget();
                }
            }
        }).start();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.recoding_click:
                if (mIsNotLoadVoice) {
                    loadFile();
                    mHandler.sendMessage(mHandler.obtainMessage(RECORD_LOADING, wavBegin));
                    return;
                }

                playVoice();
                break;
        }
    }

    private void loadFile() {
        final String urlStr = ApiConstant.URL + mediaPath;
        final ImageFileUtil util = new ImageFileUtil(mContext, DOWNLOAD_SAVE_PATH, new MyHandler());
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    mediaPath = util.writeToSDFromVoice(urlStr, getUrlFileName(mediaPath));
                    mIsNotLoadVoice = false;
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void void1) {
                mIsLoading = false;
                if (mediaPath.endsWith("null") || mediaPath == null) {
                    mHandler.sendEmptyMessage(RECORD_ERROR);
                } else {
                    mHandler.sendEmptyMessage(START_PLAY);
                }
            }
        }.execute();
    }

    private void playVoice() {
        synchronized (lock) {
            //语音是否已经读取了
            if (mIsLoading) {
                return;
            }
            //判断是否存在media路径
            if (mediaPath.endsWith("null") || mediaPath == null) {
                return;
            }
            new Thread() {
                @Override
                public void run() {
                    if (isStart && isPause) {
                        //表示isStart为true,isPause为true则表示暂停后继续播放
                        isPause = false;
                        isStart = false;
                        if (mPlayer != null) {
                            mHandler.sendMessage(mHandler.obtainMessage(RECORD_PREPARED, wavBegin));
                            try {
                                mPlayer.start();
                                mPlayer.seekTo(currentDuration);
                                isWaving = true;
                                progressRun();
                            } catch (IllegalStateException e) {
                                isWaving = false;
                                mPlayer = null;
                                isStart = true;
                                currentDuration = 0;
                                mHandler.sendMessage(mHandler.obtainMessage(RECORD_PAUSE, wavBegin));
                            }
                        } else {
                            return;
                        }
                        currentDuration = 0;
                    } else if (isStart) {
                        //表示isStart为true,isPause = false表示初次开始播放
                        isWaving = true;
                        mHandler.sendMessage(mHandler.obtainMessage(RECORD_LOADING, wavBegin));
                        isStart = false;
                        if (!isPause) {
                            mPlayer = mRecorder.startPlaybackNet(mContext, mediaPath);
                        }
                        if (mPlayer == null) {
                            mPlayer = mRecorder.startPlaybackNet(mContext, mediaPath);
                            mHandler.sendMessage(mHandler.obtainMessage(RECORD_LOADING, wavBegin));
                        }

                        mPlayer.setOnErrorListener(new OnErrorListener() {
                            @Override
                            public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
                                isWaving = false;
                                isStart = true;
                                currentDuration = 0;
                                mRecorder.stopPlayback();
                                mHandler.sendMessage(mHandler.obtainMessage(RECORD_FINISH, wavBegin));
                                return true;
                            }
                        });
                        mPlayer.setOnCompletionListener(new OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer arg0) {
                                isWaving = false;
                                mIsLoading = false;
                                isStart = true;
                                isPause = false;
                                mHandler.sendMessage(mHandler.obtainMessage(RECORD_FINISH, wavBegin));
                            }
                        });
                        mPlayer.setOnPreparedListener(new OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer arg0) {
                                mIsLoading = false;

                                if (mPlayer == null) {
                                    currentDuration = mPlayer.getCurrentPosition();
                                    mHandler.sendMessage(mHandler.obtainMessage(RECORD_PAUSE, wavBegin));
                                    return;
                                }
                                mHandler.sendMessage(mHandler.obtainMessage(RECORD_PREPARED, wavBegin));
                            }
                        });

                        try {
                            mPlayer.prepare();
                            mPlayer.start();
                            /*Message.obtain(handler, _START, mPlayer.getDuration()).sendToTarget();
                            Run();*/
                        } catch (IllegalStateException e) {
                            mPlayer = null;
                            isStart = true;
                            currentDuration = 0;
                            mHandler.sendMessage(mHandler.obtainMessage(RECORD_PAUSE, wavBegin));
                        } catch (Exception e) {
                            mPlayer = null;
                            isStart = true;
                            currentDuration = 0;
                            mHandler.sendMessage(mHandler.obtainMessage(RECORD_PAUSE, wavBegin));
                        }
                    } else if (!isStart) {
                        //isStart为false,当前状态为暂停
                        isStart = true;
                        if (mPlayer != null) {
                            try {
                                mPlayer.pause();
                                isStart = true;
                                isPause = true;
                                currentDuration = mPlayer.getCurrentPosition();
                                mHandler.sendMessage(mHandler.obtainMessage(RECORD_PAUSE, wavBegin));
                            } catch (Exception e) {
                                mPlayer = null;
                                isStart = true;
                                isPause = true;
                                currentDuration = mPlayer.getCurrentPosition();
                                mHandler.sendMessage(mHandler.obtainMessage(RECORD_PAUSE, wavBegin));
                            }
                        }
                    }
                }
            }.start();
        }
    }
}
