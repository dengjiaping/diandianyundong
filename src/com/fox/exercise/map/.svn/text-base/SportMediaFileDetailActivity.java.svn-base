package com.fox.exercise.map;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.ingenic.indroidsync.SportsApp;

import com.fox.exercise.R;
import com.fox.exercise.SmartBarUtils;
import com.fox.exercise.api.ApiBack;
import com.fox.exercise.api.ApiConstant;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.api.entity.SportMediaFile;
import com.fox.exercise.http.FunctionStatic;
import com.fox.exercise.util.ImageFileUtil;
import com.fox.exercise.util.ScrollLayout;
import com.fox.exercise.util.ScrollLayout.OnViewChangeListener;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

public class SportMediaFileDetailActivity extends Activity implements OnViewChangeListener, OnClickListener {
    private ArrayList<SportMediaFile> mediaFilesList;
    private int mCurrentIndex;
    private VideoHelper mVideoHelper;
    private SportMediaWAV mSportMediaWAV;
    private ScrollLayout scrollLayout;
    private LayoutInflater inflater;
    private ImageButton mback, positionBtn;
    private ImageButton mdel;
    LinearLayout layout;
    private Dialog alertDialog;
    SportsApp mSportsApp;

    private TextView textView;
    private static final int DELETE = 101;
    private static final int REFRESH_PHOTO = 2;

    private Dialog dialog;
    private ProgressBar progressBar;
    private ImageView img;
    private Bitmap bmp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean findMethod = SmartBarUtils.findActionBarTabsShowAtBottom();
        if (!findMethod) {
            // 取消ActionBar拆分，换用TabHost
            getWindow().setUiOptions(0);
            getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        setContentView(R.layout.sporttask_mediadetail);
        textView = (TextView) findViewById(R.id.listSize);
        mVideoHelper = new VideoHelper(this);
        mSportMediaWAV = new SportMediaWAV(this);
        mSportsApp = (SportsApp) getApplication();
        mSportsApp.addActivity(this);

        //友盟推送
        PushAgent.getInstance(this).onAppStart();

        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        scrollLayout = (ScrollLayout) findViewById(R.id.scrollLayout);
        scrollLayout.SetOnViewChangeListener(this);

        Bundle bundle = getIntent().getExtras();
        if (mSportsApp.mCurMapType == SportsApp.MAP_TYPE_GAODE) {
            mediaFilesList = SportTaskDetailActivityGaode.mediaFilesList;
//		}else{
//			mediaFilesList = SportTaskDetailActivity.mediaFilesList;
//		}
        }
        mCurrentIndex = (Integer) bundle.get("index");

        Bundle diaBundle = new Bundle();
        diaBundle.putString("message", getResources().getString(R.string.sports_wait));
        dialog = onCreateDialog(1, diaBundle);
        dialog.show();
        if (findMethod) {
            getActionBar().setDisplayShowHomeEnabled(false);
            getActionBar().setDisplayShowTitleEnabled(false);
            SmartBarUtils.setActionModeHeaderHidden(getActionBar(), true);
            SmartBarUtils.setActionBarViewCollapsable(getActionBar(), true);
        }
        mback = (ImageButton) findViewById(R.id.sport_media_back);
        mback.setOnClickListener(this);

        positionBtn = (ImageButton) findViewById(R.id.positionBtn);
        positionBtn.setOnClickListener(this);

        mdel = (ImageButton) findViewById(R.id.sport_media_clear);
        mdel.setOnClickListener(this);
        SportMediaFile file = mediaFilesList.get(mCurrentIndex);
        int currentUid = file.getUid();
        int selfUid = SportsApp.getInstance().getSportUser().getUid();
        Log.i("uid", "currentUid= " + currentUid + ",selfUid = " + selfUid);
        if (currentUid == selfUid) {
            mdel.setVisibility(View.VISIBLE);
        } else {
            mdel.setVisibility(View.GONE);
        }
        getMediaFile(mCurrentIndex);
        updateListSize(mCurrentIndex + 1, mediaFilesList.size());
        Log.i("mCurrentIndex", "当前页面为：" + mCurrentIndex + ",总页数:" + mediaFilesList.size());
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

    private void showDialog(Context con) {
        alertDialog = new Dialog(con, R.style.sports_dialog);
        LayoutInflater mInflater = getLayoutInflater();
        View v = mInflater.inflate(R.layout.sports_dialog, null);
        v.findViewById(R.id.bt_ok).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog.show();
                deleMedia();
                alertDialog.dismiss();
            }
        });
        v.findViewById(R.id.bt_cancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                alertDialog.dismiss();
            }
        });
        TextView message = (TextView) v.findViewById(R.id.message);
        message.setText("确定要删除本条记录？");
        v.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setContentView(v);
        alertDialog.show();
    }

    public Dialog onCreateDialog(int id, Bundle bundle) {
        String message = bundle.getString("message");
        switch (id) {
            case 1:
                Dialog loginPregressDialog = new Dialog(this, R.style.sports_dialog);
                LayoutInflater mInflater = getLayoutInflater();
                View v1 = mInflater.inflate(R.layout.sports_progressdialog, null);
                TextView msg = (TextView) v1.findViewById(R.id.message);
                msg.setText(message);
                v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
                loginPregressDialog.setContentView(v1);
                loginPregressDialog.setCancelable(true);
                loginPregressDialog.setCanceledOnTouchOutside(false);
                return loginPregressDialog;
        }
        return null;
    }

    private void deleMedia() {
        new Thread() {
            @Override
            public void run() {
                ApiBack back = null;
                try {
                    back = ApiJsonParser.deleteSportMediaById(mSportsApp.getSessionId(), mSportsApp.getSportUser()
                            .getUid(), mediaFilesList.get(mCurrentIndex).getMediaId());

                    Log.e("SportMediaFileDetailActivity", mSportsApp.getSessionId() + "----"
                            + mSportsApp.getSportUser().getUid() + "----"
                            + mediaFilesList.get(mCurrentIndex).getMediaId() + "----" + mCurrentIndex);
                } catch (ApiNetException e) {
                    e.printStackTrace();
                } catch (ApiSessionOutException e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.what = DELETE;
                msg.obj = back;
                handler.sendMessage(msg);
            }
        }.start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DELETE:
                    ApiBack back = (ApiBack) msg.obj;
                    if (back.getFlag() == 1) {
                        int deleteIndex = mCurrentIndex;
                        int moveTo = 0;
                        if (mCurrentIndex > 0) {
                            moveTo = deleteIndex - 1;
                            if (mediaFilesList.size() > 1) {
                                OnViewChange(moveTo);
                            }
                            mediaFilesList.remove(deleteIndex);
                            scrollLayout.removeViewAt(deleteIndex);
                            Log.i("remove", "deleteIndex = " + deleteIndex + ",moveTo=" + moveTo);

                            updateListSize(deleteIndex, mediaFilesList.size());
                            scrollLayout.snapToScreen(mCurrentIndex);
                        } else {
                            moveTo = deleteIndex + 1;
                            if (mediaFilesList.size() > 1) {
                                OnViewChange(moveTo);

                                mediaFilesList.remove(deleteIndex);
                                scrollLayout.removeViewAt(deleteIndex);
                                Log.i("remove", "deleteIndex = " + deleteIndex + ",moveTo=" + moveTo);

                                scrollLayout.snapToScreen(deleteIndex);
                                mCurrentIndex = 0;
                            } else {
                                mediaFilesList.remove(deleteIndex);
                                scrollLayout.removeViewAt(deleteIndex);
                            }
                        }
                        if (mediaFilesList.size() < 1) {
                            finish();
                        }
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        Log.i("snapToScreen", "移动屏幕到" + mCurrentIndex + ",deleteIndex=" + deleteIndex);
                        Log.i("删除操作", "删除成功！");
                    } else {
                        Log.i("删除操作", "删除失败！");
                    }
                    break;
                case REFRESH_PHOTO:
                    View v = (View) msg.obj;
                    if (v != null) {
                        ProgressBar prog = (ProgressBar) v.findViewById(R.id.progressId);
                        prog.setVisibility(View.GONE);
                        Bitmap map = getBitmap(msg.arg1);
                        if (map != null) {
                            Log.v("", "map!=null");
                            img = (ImageView) v.findViewById(R.id.ImgView);
                            img.setImageBitmap(map);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };
    private long preTime = 0;

    private void updateListSize(int curID, int size) {
        String str = curID + "/" + size;
        textView.setText(str);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sport_media_back:
                finish();
                break;
            case R.id.sport_media_clear:
                showDialog(SportMediaFileDetailActivity.this);
                break;
            case R.id.positionBtn:
                Intent intent;
                if (mSportsApp.mCurMapType == SportsApp.MAP_TYPE_GAODE) {
                    intent = new Intent(this, MediaPointInMapGaode.class);

//			}else{
////				intent = new Intent(this,MediaPointInMap.class);
//			}
                    SportMediaFile currentFile = mediaFilesList.get(mCurrentIndex);
                    intent.putExtra("point", currentFile.getPointStr());
                    intent.putExtra("mediaType", currentFile.getMediaTypeID());
                    intent.putExtra("mapType", currentFile.getMapType());
                    startActivity(intent);
                    break;
                }
        }
    }

    private void getMediaFile(int mCurrentIndex) {
        if (mediaFilesList.size() > 0) {
            int id = 0;
            for (SportMediaFile file : mediaFilesList) {
                int typeID = file.getMediaTypeID();
                if (typeID == 1) {
                    getPhotoView(id);
                } else if (typeID == 2) {
                    getVoiceView(id);
                } else if (typeID == 3) {
                    getVideoView(id);
                }
                id++;
            }
        }
        SportMediaFile currentFile = mediaFilesList.get(mCurrentIndex);
        if (currentFile.getMediaTypeID() == 3) {
            mVideoHelper.setCurentView(scrollLayout.getChildAt(mCurrentIndex));
        } else if (currentFile.getMediaTypeID() == 2) {
            SportMediaFile file = mediaFilesList.get(mCurrentIndex);
            String path = file.getMediaFilePath();
            int durations = file.getDurations();
            mSportMediaWAV.setCurentView(scrollLayout.getChildAt(mCurrentIndex), durations, path);
        }
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        OnViewChange(mCurrentIndex);
        scrollLayout.setToScreen(mCurrentIndex);
    }

    private void getPhotoView(int Id) {
        final View view = inflater.inflate(R.layout.sporttask_mediadetail_adapter, null);
        progressBar = (ProgressBar) view.findViewById(R.id.progressId);
        progressBar.setVisibility(View.VISIBLE);
        Bitmap map = getBitmap(Id);
        if (map != null) {
            progressBar.setVisibility(View.GONE);
            img = (ImageView) view.findViewById(R.id.ImgView);
            img.setImageBitmap(map);
        } else {
            final int id = Id;
            new Thread() {
                @Override
                public void run() {
                    try {
                        final SportMediaFile file = mediaFilesList.get(id);
                        View v = view;
//						ImageView image = (ImageView) v.findViewById(R.id.ImgView);
                        ProgressBar prog = (ProgressBar) v.findViewById(R.id.progressId);
                        prog.setVisibility(View.VISIBLE);
                        String fileName = file.getMediaFileName();
                        String urlStr = ApiConstant.URL + file.getMediaFilePath();
                        String rootStr = "Android/data/" + getPackageName() + "/cache/.download/img";
                        ImageFileUtil imaUtil = new ImageFileUtil(SportMediaFileDetailActivity.this, rootStr, null);
                        Log.i("bitmap", "网络获取！");
                        imaUtil.writeToSDFromPhoto(urlStr, fileName);
                        Message msg = new Message();
                        msg.obj = v;
                        msg.arg1 = id;
                        msg.what = REFRESH_PHOTO;
                        handler.sendMessage(msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
        scrollLayout.addView(view);
    }

    private void getVoiceView(int Id) {
        View view = (RelativeLayout) inflater.inflate(R.layout.sporttask_mediadetail_adapter_wav, null);
        SportMediaFile file = mediaFilesList.get(Id);
        String path = file.getMediaFilePath();
        int durations = file.getDurations();
        mSportMediaWAV.initView(view, durations, path);
        scrollLayout.addView(view);
    }

    private void getVideoView(int index) {
        View view = (RelativeLayout) inflater.inflate(R.layout.media_player_video, null);
        view.setTag(index);
        SportMediaFile fileTemp = mediaFilesList.get(index);
        mVideoHelper.initView(view, fileTemp.getDurations(), fileTemp.getWidth(), fileTemp.getHeight());
        scrollLayout.addView(view);
        Log.i("getVideoView", "视频文件！");
    }

    @Override
    public void OnViewChange(int index) {
        if (mCurrentIndex != index) {

            SportMediaFile currentFile = mediaFilesList.get(mCurrentIndex);
            if (currentFile.getMediaTypeID() == 3) {
                mVideoHelper.resetView(scrollLayout.getChildAt(mCurrentIndex));
                mVideoHelper.release();
            } else if (currentFile.getMediaTypeID() == 2) {
                mSportMediaWAV.resetView(scrollLayout.getChildAt(mCurrentIndex));
                mSportMediaWAV.release();
            }
            mCurrentIndex = index;
            currentFile = mediaFilesList.get(mCurrentIndex);
            if (currentFile.getMediaTypeID() == 3) {
                mVideoHelper.setCurentView(scrollLayout.getChildAt(mCurrentIndex));
            } else if (currentFile.getMediaTypeID() == 2) {
                SportMediaFile file = mediaFilesList.get(mCurrentIndex);
                String path = file.getMediaFilePath();
                int durations = file.getDurations();
                mSportMediaWAV.setCurentView(scrollLayout.getChildAt(mCurrentIndex), durations, path);
            }
            updateListSize(mCurrentIndex + 1, mediaFilesList.size());
        }
        Log.i("OnViewChange", "这里是" + index);
    }


    private Bitmap getBitmap(int curID) {
        SportMediaFile file = mediaFilesList.get(curID);
        String fileName = file.getMediaFileName();
        String rootStr = "Android/data/" + getPackageName() + "/cache/.download/img";
        ImageFileUtil imaUtil = new ImageFileUtil(this, rootStr, null);
        Bitmap bitmap = null;
        try {
            bitmap = imaUtil.getImg(fileName);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        if (bitmap != null) {
//			progressBar.setVisibility(View.GONE);
            Log.i("bitmap", "本地获取！");
        }
        Log.i("getPhotoView", "图片文件！");
        return bitmap;
    }

    @Override
    protected void onDestroy() {
        mVideoHelper.onDestroy();
        mSportMediaWAV.release();
        if (img != null) {
            BitmapDrawable bd = (BitmapDrawable) img.getDrawable();
            Bitmap map = bd.getBitmap();
            if (map != null) {
                map.recycle();
            }
        }
        mSportsApp.removeActivity(this);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        mVideoHelper.onPause();
        MobclickAgent.onPageEnd("SportMediaFileDetailActivity");
        MobclickAgent.onPause(SportMediaFileDetailActivity.this);
        FunctionStatic.onPause(this, FunctionStatic.FUNCTION_MEDIA_VIEW, preTime);
        super.onPause();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        mVideoHelper.onResume();
        MobclickAgent.onPageStart("SportMediaFileDetailActivity");
        MobclickAgent.onResume(SportMediaFileDetailActivity.this);
        preTime = FunctionStatic.onResume();
        super.onResume();
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
