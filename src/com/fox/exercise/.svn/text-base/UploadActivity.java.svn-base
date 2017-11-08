package com.fox.exercise;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fox.exercise.api.ApiBack;
import com.fox.exercise.api.ApiConstant;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.api.entity.UserDetail;
import com.fox.exercise.login.LoginActivity;
import com.fox.exercise.login.SportMain;
import com.fox.exercise.weibo._FakeX509TrustManager;
import com.fox.exercise.weibo.renren.RenRenUpload;
import com.fox.exercise.weibo.sina.SinaUpload;
import com.fox.exercise.weibo.tencent.TXUpload;
import com.umeng.analytics.MobclickAgent;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.ingenic.indroidsync.SportsApp;

public class UploadActivity extends AbstractBaseActivity implements OnClickListener {

    private String picAddr = "";
    private EditText picTitleEditText = null;
    //	private String picTitle = "";
    private String shareTitle = "";
//	private ImageView photoImgView = null;

    // private RelativeLayout navLayout = null;

    private String imgUrl;
    private Button finishBtn = null;

    private SportsApp mSportsApp;

    FinishHandler h = new FinishHandler();

    private static final int UPLOAD_FINISH = 1;
    private static final int NOT_LOGIN = 2;
    private static final int UPLOAD_FAILED = 3;
    private static final int UPLOAD_START = 4;
    // private static final int NET_ERROR = 0;
    private Dialog uploadProgressDialog;

    private Toast toast = null;

    private boolean noSinaWeibo = false;
    private boolean noTxWeibo = false;
    private boolean noRenRen = false;
    private boolean noBaidu = false;
    private boolean noQQzone = false;

//	private final static String mbRootPath = "/apps/美美相机";

    private RecordHelper mRecorder;
    private ImageButton voiceStart;
    private TextView upvoiceTime;
    private ImageButton voiceDelete;
    private ProgressBar progress;
    private boolean isStart;
    private int currentDuration;
    private RelativeLayout upvoiceLinear;

    private final static int START = 0;
    public final static int UPDATE = 1;
    private final static int FINISH = 2;

    MediaPlayer mPlayer = null;
    //	private Dialog dialog;
    private boolean canSingleClick;
    private RelativeLayout reVoice;

    private Bitmap mPhoto = null;
    private SportExceptionHandler mExceptionHandler = null;

//	private TextView mCurrent_ka;

    private Context mContext = null;

    private static final String TAG = "UploadActivity";

    String pointString;
    int taskID;
    int mediaTypeID;
    private ImageButton iButton;

    @Override
    protected void onStart() {
        super.onStart();
        init_finish_btn();
    }

    private void init() {
        currentDuration = 0;
        isStart = true;
        canSingleClick = false;
        mRecorder = new RecordHelper();
        mRecorder.delete();
        voiceStart = (ImageButton) findViewById(R.id.voice_start);
        upvoiceTime = (TextView) findViewById(R.id.upvoice_time);
        voiceDelete = (ImageButton) findViewById(R.id.voice_delete);
        progress = (ProgressBar) findViewById(R.id.progress);

        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        upvoiceLinear = (RelativeLayout) findViewById(R.id.upvoice_linear);
        // upvoiceLinear.setLayoutParams(new RelativeLayout.LayoutParams(w * 9 /
        // 16, LayoutParams.WRAP_CONTENT));

        voiceStart.setOnClickListener(new AudioListener());
        voiceDelete.setOnClickListener(new AudioListener());
        reVoice = (RelativeLayout) findViewById(R.id.relative_voice);
        iButton = new ImageButton(this);
        iButton.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        iButton.setBackgroundResource(R.drawable.sport_title_ok_selector);
        showRightBtn(iButton);
        right_btn.setPadding(0, 0, SportsApp.getInstance().dip2px(17), 0);

    }

    class AudioListener implements OnClickListener, OnTouchListener, OnLongClickListener {

        @Override
        public void onClick(View v) {
            if (v == voiceStart) {
                if (RecordHelper.mSampleFile == null)
                    return;
                if (isStart) {
                    isStart = false;
                    voiceStart.setImageResource(R.drawable.voice_stop);
                    mPlayer = mRecorder.startPlayback(mContext);
                    if (currentDuration > 0) {
                        mPlayer.seekTo(currentDuration);
                        currentDuration = 0;
                    }
                    Message msg = Message.obtain(handler, START, mPlayer.getDuration());
                    // msg.what = START;
                    // msg.arg1 = mPlayer.getDuration();
                    // handler.sendMessage(msg);
                    msg.sendToTarget();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (mPlayer != null && RecordHelper.flag) {
                                try {
                                    Thread.sleep(50);
                                    Message msg = new Message();
                                    msg.what = UPDATE;
                                    try {
                                        msg.arg1 = mPlayer.getCurrentPosition();
                                    } catch (Exception e) {
                                        Message msg1 = new Message();
                                        msg1.what = FINISH;
                                        handler.sendMessage(msg1);
                                    }
                                    handler.sendMessage(msg);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (!RecordHelper.flag) {
                                Message msg = new Message();
                                msg.what = FINISH;
                                handler.sendMessage(msg);
                            }
                        }
                    }).start();
                } else if (!isStart) {
                    isStart = true;
                    voiceStart.setImageResource(R.drawable.voice_begin);
                    mPlayer.pause();
                    currentDuration = mPlayer.getCurrentPosition();
                }
            }
            if (v == voiceDelete) {
                mRecorder.delete();
                upvoiceLinear.setVisibility(View.GONE);
                currentDuration = 0;
                isStart = true;
                canSingleClick = false;
                finishBtn.setText(getString(R.string.sports_upload_pressrecoding));
            }

        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP: {
                    if (v == finishBtn) {
                        // stopRecord();
                        mRecorder.stop();
                        // show.setVisibility(View.VISIBLE);
                        // show.setText(Integer.toString(RecordHelper.mSampleLength));
                        if (canSingleClick) {
                            if (RecordHelper.mSampleLength >= 1) {
                                upvoiceLinear.setVisibility(View.VISIBLE);
                                finishBtn.setText(getString(R.string.sports_upload_delete));
                                upvoiceTime.setText(Integer.toString(RecordHelper.mSampleLength) + "″");
                            } else {
                                mRecorder.delete();
                                upvoiceLinear.setVisibility(View.GONE);
                                currentDuration = 0;
                                isStart = true;
                                canSingleClick = false;
                                Toast.makeText(mContext, "录音时间不能低于一秒钟", Toast.LENGTH_SHORT).show();
                            }
                            reVoice.setVisibility(View.GONE);
                        }
                        break;
                    }
                }
            }
            return false;
        }

        @Override
        public boolean onLongClick(View v) {
            if (v == finishBtn) {
                if (canSingleClick)
                    return true;
                // creatAudioRecord();
                // startRecord();
                mRecorder.startRecording(MediaRecorder.OutputFormat.DEFAULT, ".mp3", mContext);
                // dialog = new
                // Dialog(mContext,R.style.share_dialog2);
                // dialog.show();
                reVoice.setVisibility(View.VISIBLE);
                canSingleClick = true;
            }
            return true;
        }

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (!Thread.currentThread().isInterrupted()) {
                switch (msg.what) {
                    case START:
                        progress.setMax((Integer) msg.obj);
                        progress.setProgress(0);
                    case UPDATE:
                        progress.setProgress(msg.arg1);
                        break;
                    case FINISH:
                        progress.setProgress(0);
                        isStart = true;
                        voiceStart.setImageResource(R.drawable.voice_begin);
                        // Toast.makeText(main.this, "文件下载完成", 1).show();
                        break;

                    case -1:
                        // String error = msg.getData().getString("error");
                        // Toast.makeText(main.this, error, 1).show();
                        break;
                }
            }
            super.handleMessage(msg);
        }
    };


    @Override
    protected void onStop() {
        super.onStop();
        Log.e("upload", "onStop");
//		Intent intent = new Intent(this, SportServices.class);
//		stopService(intent);
        if (mPhoto != null)
            mPhoto.recycle();
    }

    class FinishHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            Intent i = null;
            switch (msg.what) {
                case UPLOAD_FINISH:
                    if (uploadProgressDialog != null && uploadProgressDialog.isShowing())
                        uploadProgressDialog.dismiss();
                    int flag = msg.arg1;
                    boolean isFinish = false;
                    if (flag == 1) {
                        isFinish = true;
                    } else {
                        isFinish = false;
                    }
//				Intent intent = new Intent(UploadActivity.this, SportingMapActivity.class);
//				intent.putExtra("isFinish", isFinish);
//				UploadActivity.this.setResult(SportingMapActivity.VOICE,intent);
//				UploadActivity.this.finish();
                    break;
                case NOT_LOGIN:
                    i = new Intent(mContext, LoginActivity.class);
                    startActivity(i);
                    break;
                case UPLOAD_FAILED:
                    if (uploadProgressDialog != null && uploadProgressDialog.isShowing())
                        uploadProgressDialog.dismiss();
                    if (msg.arg1 == 1) {
                        Toast.makeText(mContext, mContext.getResources().getString(R.string.acess_server_error),
                                Toast.LENGTH_SHORT).show();
                    } else if (msg.arg1 == 2) {
                        Toast.makeText(mContext, mContext.getResources().getString(R.string.exception_session_out),
                                Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(mContext, LoginActivity.class));
                    }
                    break;
                case UPLOAD_START:
                    if (uploadProgressDialog == null) {
                        uploadProgressDialog = new Dialog(mContext, R.style.sports_dialog);
                        LayoutInflater mInflater = getLayoutInflater();
                        View v = mInflater.inflate(R.layout.sports_progressdialog, null);
                        TextView message = (TextView) v.findViewById(R.id.message);
                        message.setText(R.string.uploading);
                        v.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
                        uploadProgressDialog.setContentView(v);
                        uploadProgressDialog.setCancelable(true);
                        uploadProgressDialog.setCanceledOnTouchOutside(false);
                    }
                    if (uploadProgressDialog != null && !uploadProgressDialog.isShowing())
                        uploadProgressDialog.show();
                    break;
                // case GET_DETAIL_FINISH:
                // if (noBaidu && noQQzone && noRenRen && noSinaWeibo && noTxWeibo)
                // {
                // Log.d(TAG, "no share");
                // break;
                // } else {
                // Log.d(TAG, "start to share");
                // ShareImgThread thread = new ShareImgThread();
                // thread.start();
                // break;
                // }
                // case NET_ERROR:
                // break;
            }
        }

    }

    private void init_finish_btn() {
        Log.d("upload", "session_id" + mSportsApp.getSessionId());
        picTitleEditText = (EditText) findViewById(R.id.sports_upload_heading_edittext);
        picTitleEditText.setMaxLines(1);
        picTitleEditText.addTextChangedListener(new EditTextMaxLengthWatcher(18, picTitleEditText));
//		picTitle = picTitleEditText.getText().toString();
        finishBtn = (Button) findViewById(R.id.sports_upload_finish_button);
        finishBtn.setOnLongClickListener(new AudioListener());
        finishBtn.setOnTouchListener(new AudioListener());
        Log.d("upload", "find finish button");
        finishBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mRecorder.delete();
                upvoiceLinear.setVisibility(View.GONE);
                currentDuration = 0;
                isStart = true;
                canSingleClick = false;
                finishBtn.setText(getString(R.string.sports_upload_pressrecoding));
            }
        });
    }

    public class EditTextMaxLengthWatcher implements TextWatcher {
        private int maxLen;
        private EditText editText;

        public EditTextMaxLengthWatcher(int maxLen, EditText editText) {
            this.maxLen = maxLen;
            this.editText = editText;
        }

        @Override
        public void onTextChanged(CharSequence ss, int start, int before, int count) {
            Editable editable = editText.getText();
            int len = editable.length();
            if (len > maxLen) {
                int selEndIndex = Selection.getSelectionEnd(editable);
                String str = editable.toString();
                String newStr = str.substring(0, maxLen);
                editText.setText(newStr);
                editable = editText.getText();
                int newLen = editable.length();
                if (selEndIndex > newLen) {
                    selEndIndex = editable.length();
                }
                Selection.setSelection(editable, selEndIndex);
                if (toast == null) {
                    toast = Toast.makeText(mContext, "输入字符过长", Toast.LENGTH_SHORT);
                } else {
                    toast.cancel();
                    toast.setText("输入字符过长");
                }
                toast.show();

            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }
    }

    class UploadThread extends Thread {

        @Override
        public void run() {
            ApiBack apiBack = new ApiBack();

            try {
                if ("".equals(mSportsApp.getSessionId()) || mSportsApp.getSessionId() == null) {

                    Message msg = Message.obtain(h, NOT_LOGIN);
                    msg.sendToTarget();
                } else {
                    Message msg = Message.obtain(h, UPLOAD_START);
                    msg.sendToTarget();

                    pointString = getIntent().getExtras().getString("pointStr");
                    mediaTypeID = getIntent().getExtras().getInt("mediaTypeID");
                    SharedPreferences sp = getSharedPreferences("sport_state_" + mSportsApp.getSportUser()
                            .getUid(), MODE_PRIVATE);
                    taskID = sp.getInt("taskID", 0);
                    Log.i("pointString", pointString + "---" + taskID + "---" + mediaTypeID);

                    String wavAddr = "/sdcard/Recording/" + (RecordHelper.mSampleFile).getName();
                    String wavName = wavAddr.substring(wavAddr.lastIndexOf(".") + 1);

                    apiBack = ApiJsonParser.uploadSportTaskMedia(mSportsApp.getSessionId(), mSportsApp.getSportUser()
                            .getUid(), taskID, mediaTypeID, String.valueOf(RecordHelper.mSampleLength), wavAddr, wavName, pointString, 0, 0, mSportsApp.mCurMapType);

                    Log.i("uploadSportTaskMedia", "sessionId = " + mSportsApp.getSessionId() + ",uid = "
                            + mSportsApp.getSportUser().getUid() + ",taskID = " + taskID + ",mediaTypeID = "
                            + mediaTypeID + ",durations = " + RecordHelper.mSampleLength + ",mediaFilePath = " + wavAddr + ",mediaFileName = " + wavName
                            + ",pointString = " + pointString + "---" + String.valueOf(1));

                    Log.d("upload", "" + apiBack.getFlag() + ":" + apiBack.getMsg());

                    if (apiBack.getMsg() != null) {
                        String img = apiBack.getMsg();
                        imgUrl = ApiConstant.URL + img.substring(img.indexOf(":") + 1);
                    }
                    msg = Message.obtain(h, UPLOAD_FINISH);
                    msg.arg1 = apiBack.getFlag();
                    msg.sendToTarget();
                }
            } catch (ApiNetException e) {
                e.printStackTrace();
                Message mgs = Message.obtain(h, UPLOAD_FAILED);
                mgs.arg1 = 1;
                mgs.sendToTarget();
            } catch (ApiSessionOutException e) {
                e.printStackTrace();
                Message mgs = Message.obtain(h, UPLOAD_FAILED);
                mgs.arg1 = 2;
                mgs.sendToTarget();
            }
        }
    }

    class ShareImgThread extends Thread {

        @Override
        public void run() {
            Log.d(TAG, "ShareImgThread");
            // if (SportMain.user_name == null ||
            // SportMain.user_name.equals("")) {
            // shareTitle =
            // "快来最美女孩为我加油，下载地址：http://cdn.17vee.com/lmstation/Beauty/17veeBeauty.apk";
            // } else {
            // shareTitle = "快来最美女孩为我（" + SportMain.user_name
            // +
            // "）加油，下载地址：http://cdn.17vee.com/lmstation/Beauty/17veeBeauty.apk";
            // }
            shareTitle = "我正在使用云狐运动记录每天的瘦身成果，大家一起来比一比吧！"
                    + "下载地址：http://cdn.17vee.com/lmstation/Beauty/17veeBeauty.apk";
            if (imgUrl != null) {
                if (!noSinaWeibo) {
                    ShareToSinaThread t = new ShareToSinaThread();
                    t.start();
                }
                if (!noTxWeibo) {
                    ShareToTXWeiboThread t2 = new ShareToTXWeiboThread();
                    t2.start();
                }
                if (!noBaidu) {
//					ShareToBaiduThread t3 = new ShareToBaiduThread();
//					t3.start();
                }
                if (!noRenRen) {
                    ShareToRenRenThread t3 = new ShareToRenRenThread();
                    t3.start();
                }
                if (!noQQzone) {
                    ShareToQQzoneThread t5 = new ShareToQQzoneThread();
                    t5.start();
                }
            }
        }

    }

    class GetUserNameThread extends Thread {

        @Override
        public void run() {
            if (mSportsApp.getSessionId() == null || "".equals(mSportsApp.getSessionId()))
                return;
            try {
                UserDetail detail = ApiJsonParser.refreshRank(mSportsApp.getSessionId());
                SportMain.user_name = detail.getUname();
                // msg = Message.obtain(handler, GET_DETAIL_FINISH);
                // msg.sendToTarget();
                ShareImgThread thread = new ShareImgThread();
                thread.start();
            } catch (ApiNetException e) {
                Message msg2 = Message.obtain(mExceptionHandler, SportsExceptionHandler.NET_ERROR);
                msg2.sendToTarget();
                e.printStackTrace();
            } catch (ApiSessionOutException e) {
                Message msg2 = Message.obtain(mExceptionHandler, SportsExceptionHandler.SESSION_OUT);
                msg2.sendToTarget();
                startActivity(new Intent(mContext, LoginActivity.class));
                e.printStackTrace();
            }
        }
    }

    class ShareToRenRenThread extends Thread {

        @Override
        public void run() {
            RenRenUpload upload = new RenRenUpload(mContext);
            upload.upload(picAddr, shareTitle);
        }
    }

    class ShareToSinaThread extends Thread {

        @Override
        public void run() {
            SinaUpload upload = new SinaUpload(mContext);
            SimpleDateFormat formate = new SimpleDateFormat("HH:mm:ss");
            upload.uploadWeibo(picAddr, shareTitle + " " + formate.format(new Date(System.currentTimeMillis())));
        }

    }

    class ShareToTXWeiboThread extends Thread {

        @Override
        public void run() {
            TXUpload upload = new TXUpload();
            upload.upload(mContext, shareTitle, picAddr);
        }

    }

//	class ShareToBaiduThread extends Thread {
//
//		@Override
//		public void run() {
//			String mbOauth = PreferenceManager.getDefaultSharedPreferences(mContext).getString(Settings.BAIDU_TOKEN,
//					null);
//			BaiduPCSClient api = new BaiduPCSClient();
//			api.setAccessToken(mbOauth);
//			final String fileName = picAddr.substring(picAddr.lastIndexOf("/"));
//
//			api.uploadFile(picAddr, mbRootPath + fileName, new BaiduPCSStatusListener() {
//				@Override
//				public void onProgress(long bytes, long total) {
//
//				}
//
//				@Override
//				public long progressInterval() {
//					return 1000;
//				}
//			});
//		}
//
//	}

    class ShareToQQzoneThread extends Thread {

        @Override
        public void run() {
            try {

                _FakeX509TrustManager.allowAllSSL();// 调用
                URL postUrl = new URL("https://graph.qq.com/share/add_share");
                HttpURLConnection conn = (HttpURLConnection) postUrl.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");

                StringBuffer params = new StringBuffer();
                SharedPreferences sp = getSharedPreferences(AllWeiboInfo.TENCENT_QQZONE_TOKEN_SP, MODE_PRIVATE);
                AllWeiboInfo.TENCENT_QQZONE_TOKEN = sp.getString(AllWeiboInfo.TENCENT_QQZONE_TOKEN_KEY, "");
                AllWeiboInfo.TENCENT_QQZONE_OPEN_ID = sp.getString(AllWeiboInfo.TENCENT_QQZONE_OPEN_ID_KEY, "");

                params.append(URLEncoder.encode("access_token", "utf-8") + "="
                        + URLEncoder.encode(AllWeiboInfo.TENCENT_QQZONE_TOKEN, "utf-8"));
                params.append("&" + URLEncoder.encode("oauth_consumer_key", "utf-8") + "="
                        + URLEncoder.encode(AllWeiboInfo.TENCENT_APPID, "utf-8"));
                params.append("&" + URLEncoder.encode("openid", "utf-8") + "="
                        + URLEncoder.encode(AllWeiboInfo.TENCENT_QQZONE_OPEN_ID, "utf-8"));
                // String title =
                // "亲，快来“最美女孩”为我加油吧！下载链接>>>http://cdn.17vee.com/lmstation/Beauty/17veeBeauty.apk";
                params.append("&" + URLEncoder.encode("title", "utf-8") + "=" + URLEncoder.encode(shareTitle, "utf-8"));
                params.append("&" + URLEncoder.encode("url", "utf-8") + "="
                        + URLEncoder.encode("http://cdn.17vee.com/lmstation/Beauty/17veeBeauty.apk", "utf-8"));
                params.append("&" + URLEncoder.encode("images", "utf-8") + "=" + URLEncoder.encode(imgUrl, "utf-8"));
                params.append("&" + URLEncoder.encode("site", "utf-8") + "=" + URLEncoder.encode("云狐运动", "utf-8"));
                params.append("&" + URLEncoder.encode("fromurl", "utf-8") + "="
                        + URLEncoder.encode("http://cdn.17vee.com/lmstation/Beauty/17veeBeauty.apk", "utf-8"));

                DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                out.write(params.toString().getBytes());

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = "";
                StringBuffer res = new StringBuffer();
                while ((line = reader.readLine()) != null) {
                    res.append(line);
                }
                Log.d(TAG, res.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.title_right_btn:
//			if (RecordHelper.mSampleLength >= 1) {
//				UploadThread t = new UploadThread();
//				t.start();
//			} else {
//				Toast.makeText(mContext, "录音时间低于一秒钟,不能上传！", Toast.LENGTH_SHORT).show();
//			}
                break;
        }
    }

    static class Settings {

        protected static final String BAIDU_TOKEN = null;
        protected static final String BAIDU_NICK = null;

    }

    @Override
    public void initIntentParam(Intent intent) {
        title = getResources().getString(R.string.sports_upload_title);

    }

    @Override
    public void initView() {
        showContentView(R.layout.sport_upload);
        mContext = this;

        mSportsApp = (SportsApp) getApplication();
        mExceptionHandler = new SportExceptionHandler(this);
//		photoImgView = (ImageView) findViewById(R.id.sports_upload_photo_imageview);
        init();
    }

    @Override
    public void setViewStatus() {
        iButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (RecordHelper.mSampleLength >= 1) {
                    UploadThread t = new UploadThread();
                    t.start();
                } else {
                    Toast.makeText(mContext, "录音时间低于一秒钟,不能上传！", Toast.LENGTH_SHORT).show();
                }
            }

        });
        right_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (RecordHelper.mSampleLength >= 1) {
                    UploadThread t = new UploadThread();
                    t.start();
                } else {
                    Toast.makeText(mContext, "录音时间低于一秒钟,不能上传！", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    @Override
    public void onPageResume() {
        MobclickAgent.onPageStart("UploadActivity");
    }

    @Override
    public void onPagePause() {
        MobclickAgent.onPageEnd("UploadActivity");
    }

    @Override
    public void onPageDestroy() {
        // TODO Auto-generated method stub
        mSportsApp = null;

    }
}