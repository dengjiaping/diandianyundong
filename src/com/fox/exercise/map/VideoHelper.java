package com.fox.exercise.map;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cn.ingenic.indroidsync.SportsApp;

import com.fox.exercise.R;
import com.fox.exercise.api.entity.SportMediaFile;
import com.fox.exercise.util.MediaFileDownloader;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class VideoHelper implements OnClickListener {
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private MediaPlayer mediaPlayer;
    private ImageView mDefaultBG;
    private ImageView mPlayImage;
    private ProgressBar mLoadingProgressBar;
    private ProgressBar mPlayProgressBar;
    private TextView mPastTimeText;
    private TextView mTotalTimeText;
    private int onPausePosition;
    private int mIndex;
    private String mFilePath;
    private Context mContext;
    private MediaFileDownloader mDownLoader;
    private ScheduledExecutorService executorService;
    private Handler handler;

    private static final int STATE_IDLE = 0;
    private static final int STATE_READY = 1;
    private static final int STATE_LOADING = 2;
    private static final int STATE_PLAYING = 3;
    private static final int STATE_PAUSE = 4;
    private static final int STATE_ONPAUSE = 5;
    private int mState = STATE_IDLE;

    private static final int MSG_FRESH = 0;
    private static final String TAG = "videoHelper";

    VideoHelper(Context context) {
        mContext = context;
    }

    private String stateToString(int state) {
        switch (state) {
            case STATE_IDLE:
                return "STATE_IDLE";
            case STATE_READY:
                return "STATE_READY";
            case STATE_LOADING:
                return "STATE_LOADING";
            case STATE_PLAYING:
                return "STATE_PLAYING";
            case STATE_PAUSE:
                return "STATE_PAUSE";
            case STATE_ONPAUSE:
                return "STATE_ONPAUSE";
            default:
                return "STATE_ERROR";
        }
    }

    void setCurentView(View v) {
        mIndex = (Integer) v.getTag();
        Log.i(TAG, "## setCurentView index:" + mIndex);
        surfaceView = (SurfaceView) v.findViewById(R.id.surfaceview);

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mDefaultBG = (ImageView) v.findViewById(R.id.default_bg);
        mPlayImage = (ImageView) v.findViewById(R.id.playImage);
        mLoadingProgressBar = (ProgressBar) v
                .findViewById(R.id.loadingProgressBar);
        mPlayProgressBar = (ProgressBar) v.findViewById(R.id.progress);
        mPastTimeText = (TextView) v.findViewById(R.id.pastTimeText);
        mTotalTimeText = (TextView) v.findViewById(R.id.totalTimeText);
        mPlayImage.setOnClickListener(this);
        surfaceView.setOnClickListener(this);

        release();
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                initMedia();
                return false;
            }
        });
        executorService = Executors.newScheduledThreadPool(1);
        executorService.schedule(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        }, 10000, TimeUnit.MICROSECONDS);

        mFilePath = null;
        if (mDownLoader != null) {
            mDownLoader.cancel();
        }
        SportMediaFile item;
        if (SportsApp.getInstance().mCurMapType == SportsApp.MAP_TYPE_GAODE) {
            item = SportTaskDetailActivityGaode.mediaFilesList
                    .get(mIndex);

//		else{
//			item = SportTaskDetailActivity.mediaFilesList
//					.get(mIndex);
//		}
            mDownLoader = new MediaFileDownloader(mContext, mHandler,
                    item.getMediaFilePath(), 3, mIndex);
            updateState(STATE_IDLE);
        }
    }

    void initView(View v, int durations, int width, int height) {
        SurfaceView sv = (SurfaceView) v.findViewById(R.id.surfaceview);
        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) sv
                .getLayoutParams();
        int maxWidth = SportsApp.ScreenWidth;
        int maxHeight = SportsApp.ScreenWidth * 280 / 480;
        if (width > 0 && height > 0) {
            if (width * 280 / 480 > height) {
                params.width = maxWidth;
                params.height = maxWidth * height / width;
            } else {
                params.width = maxHeight * width / height;
                params.height = maxHeight;
            }
        } else {
            params.width = maxWidth;
            params.height = maxHeight;
        }
        sv.setLayoutParams(params);

        ImageView bg = (ImageView) v.findViewById(R.id.default_bg);
        params = bg.getLayoutParams();
        params.height = maxHeight;
        bg.setLayoutParams(params);
        // TO fix other page video can not show
//		SurfaceHolder sh = sv.getHolder();
//		sh.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//		mediaPlayer = new MediaPlayer();
//		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//		try {
//			mediaPlayer.setDisplay(sh);
//			mediaPlayer.release();
//		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
//		} catch (IllegalStateException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		mediaPlayer = null;

        TextView mTotalTimeTextTemp = (TextView) v
                .findViewById(R.id.totalTimeText);
        mTotalTimeTextTemp.setText(formatTime(durations));
    }

    void resetView(View v) {
        ImageView mDefaultBGTemp = (ImageView) v.findViewById(R.id.default_bg);
        ImageView mPlayImageTemp = (ImageView) v.findViewById(R.id.playImage);
        ProgressBar mLoadingProgressBarTemp = (ProgressBar) v
                .findViewById(R.id.loadingProgressBar);
        ProgressBar mPlayProgressBarTemp = (ProgressBar) v
                .findViewById(R.id.progress);
        TextView mPastTimeTextTemp = (TextView) v
                .findViewById(R.id.pastTimeText);
        SurfaceView surfaceViewTemp = (SurfaceView) v
                .findViewById(R.id.surfaceview);

        mLoadingProgressBarTemp.setVisibility(View.INVISIBLE);
        mDefaultBGTemp.setVisibility(View.VISIBLE);
        mPlayImageTemp.setVisibility(View.VISIBLE);
        mPlayProgressBarTemp.setMax(100);
        mPlayProgressBarTemp.setProgress(0);
        mPastTimeTextTemp.setText(formatTime(0));
        mPlayImageTemp.setOnClickListener(null);
        surfaceViewTemp.setOnClickListener(null);
    }

    String formatTime(int time) {
        time /= 1000;
        if (time >= 3600) {
            return "EE:EE";
        }
        int m = time / 60;
        int s = time % 60;
        String sM = m > 9 ? "" + m : "0" + m;
        String sS = s > 9 ? "" + s : "0" + s;
        return sM + ":" + sS;
    }

    void onResume() {
    }

    void onPause() {
        mHandler.removeMessages(MSG_FRESH);
        try {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
                updateState(STATE_ONPAUSE);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void onDestroy() {
        release();
    }

    void pause() {
        mHandler.removeMessages(MSG_FRESH);
        try {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                onPausePosition = mediaPlayer.getCurrentPosition();
                mediaPlayer.pause();
                updateState(STATE_PAUSE);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    void play() {
        try {

            Log.i(TAG, "## play  path:" + mFilePath);
            if (mFilePath == null) {
                loadFile();
                return;
            }
            initMedia();
            if (mState != STATE_PAUSE) {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(mFilePath);
                mediaPlayer.prepare();
            }
//			if (mState == STATE_ONPAUSE) {
//				mediaPlayer.seekTo(onPausePosition);
//			}
            mediaPlayer.start();
            mTotalTimeText.setText(formatTime(mediaPlayer.getDuration()));
            mPlayProgressBar.setMax(mediaPlayer.getDuration());
            mHandler.sendEmptyMessage(MSG_FRESH);
            updateState(STATE_PLAYING);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void initMedia() {
        if (mediaPlayer != null) {
            return;
        }
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setDisplay(surfaceHolder);
        mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer arg0) {
                // TODO Auto-generated method stub
                mPlayProgressBar.setProgress(0);
                mPastTimeText.setText(formatTime(0));
                updateState(STATE_READY);
            }

        });
        mediaPlayer.setOnErrorListener(new OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
                // TODO Auto-generated method stub
                Log.e(TAG, "## onError " + arg1 + " ," + arg2);
                return false;
            }

        });
    }

    void release() {
        mHandler.removeMessages(MSG_FRESH);
        try {
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()
                        || mState == STATE_ONPAUSE
                        || mState == STATE_PAUSE) {
                    mediaPlayer.stop();
                }
                mediaPlayer.release();
                mediaPlayer = null;
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    private void updateState(int state) {
        Log.i(TAG, "## updateState  " + stateToString(mState) + " -> "
                + stateToString(state));
        mState = state;
        switch (mState) {
            case STATE_READY:
            case STATE_IDLE:
            case STATE_ONPAUSE:
                mLoadingProgressBar.setVisibility(View.INVISIBLE);
                mDefaultBG.setVisibility(View.VISIBLE);
                mPlayImage.setVisibility(View.VISIBLE);
                break;
            case STATE_LOADING:
                mLoadingProgressBar.setVisibility(View.VISIBLE);
                mDefaultBG.setVisibility(View.VISIBLE);
                mPlayImage.setVisibility(View.INVISIBLE);
                break;
            case STATE_PAUSE:
                mLoadingProgressBar.setVisibility(View.INVISIBLE);
                mDefaultBG.setVisibility(View.INVISIBLE);
                mPlayImage.setVisibility(View.VISIBLE);
                break;
            case STATE_PLAYING:
                mLoadingProgressBar.setVisibility(View.INVISIBLE);
                mDefaultBG.setVisibility(View.INVISIBLE);
                mPlayImage.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_FRESH:
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        int currentPosition = mediaPlayer.getCurrentPosition();
                        mPlayProgressBar.setProgress(currentPosition);
                        mPastTimeText.setText(formatTime(currentPosition));
                        sendEmptyMessageDelayed(MSG_FRESH, 1000);
                    }
                    break;
                case MediaFileDownloader.MSG_START_DOWNLOAD:
                    if (mIndex == msg.arg1) {
                        updateState(STATE_LOADING);
                    }
                    break;
                case MediaFileDownloader.MSG_DOWNLOAD_CANCEL:
                    break;
                case MediaFileDownloader.MSG_DOWNLOAD_FAILED:
                    if (mIndex == msg.arg1) {
                        updateState(STATE_IDLE);
                        Toast.makeText(mContext, R.string.sports_detail_download_fail, Toast.LENGTH_SHORT)
                                .show();
                    }
                    break;

                case MediaFileDownloader.MSG_DOWNLOAD_FINISH:
                    if (mIndex == msg.arg1) {
                        mFilePath = (String) msg.obj;
                        updateState(STATE_READY);
                        play();
                    }
                    break;
            }

        }
    };

    void loadFile() {
        Thread thread = new Thread() {
            public void run() {
                mDownLoader.DownFile();
            }
        };
        thread.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.playImage:
                play();
                break;
            case R.id.surfaceview:
                pause();
                break;
        }
    }
}