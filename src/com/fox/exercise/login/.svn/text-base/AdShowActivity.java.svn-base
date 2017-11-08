package com.fox.exercise.login;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.fox.exercise.FileCache;
import com.fox.exercise.ImageDownloader;
import com.fox.exercise.R;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.entity.AdContent;
import com.iflytek.voiceads.AdError;
import com.iflytek.voiceads.AdKeys;
import com.iflytek.voiceads.IFLYAdListener;
import com.iflytek.voiceads.IFLYAdSize;
import com.iflytek.voiceads.IFLYFullScreenAd;
import com.iflytek.voiceads.IFLYNativeAd;
import com.iflytek.voiceads.IFLYNativeListener;
import com.iflytek.voiceads.NativeADDataRef;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import cn.ingenic.indroidsync.SportsApp;

public class AdShowActivity extends Activity implements IFLYNativeListener {
    private ImageView imageView;
    private static final int GETCODE_FAIL_NONET = 0x0012;
    private int play_time;
    private ImageDownloader mDownloader = null;
    private TimeCount timeCount;
    private TimeCount timeCountThree;
    private String jumpurl;
    //	private LinearLayout mLinearLayout;
    private ImageButton jumpButton;

    private SportsApp mSportsApp;
    private int isLocalad;
    private String aid;//讯飞广告id

    private IFLYNativeAd nativeAd;
    private NativeADDataRef adItem;
//    private FileCache fileCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.adshow_play);
        mSportsApp = (SportsApp) getApplication();
        jumpButton = (ImageButton) findViewById(R.id.img_bt);
        imageView = (ImageView) findViewById(R.id.ad_img);
        jumpButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                LoginActivity.instance.resetStartState();
                AdShowActivity.this.finish();
            }
        });
        this.mDownloader = new ImageDownloader(this);
        mDownloader.setType(ImageDownloader.OnlyOne);
//        fileCache = new FileCache(this);
        getAdShow();
    }


    private void initAdview(){
        if(mSportsApp.isOpenNetwork()){
            //有网状态下
            if(isLocalad!=1){
                imageView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        LoginActivity.instance.resetStartState();
                        //闪屏页跳转
                        Intent jumpintent = new Intent();
                        jumpintent.setAction("android.intent.action.VIEW");
                        if (!TextUtils.isEmpty(jumpurl)) {
                            Uri content_url = Uri.parse(jumpurl);
                            jumpintent.setData(content_url);
                            startActivity(jumpintent);
                        }
                        AdShowActivity.this.finish();
                    }
                });
                timeCountThree = new TimeCount(3000, 1000);
                timeCountThree.start();
            }else{
                //用的飞讯广告
                loadAD();
            }

        }else{
            //无网状态下
            timeCountThree = new TimeCount(3000, 1000);
            timeCountThree.start();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (LoginActivity.instance != null) {
                LoginActivity.instance.resetStartState();
            }
            AdShowActivity.this.finish();
        }
        return false;
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
//        getAdShow();
    }

    private void getAdShow() {
        if (Tools.isNetworkConnected(AdShowActivity.this)) {
            new AsyncTask<Void, Void, AdContent>() {
                @Override
                protected AdContent doInBackground(Void... params) {
                    AdContent back = null;
                    try {
                        back = ApiJsonParser.adShow("z" + getResources().getString(R.string.config_game_id));
                    } catch (ApiNetException e) {
                        e.printStackTrace();
                        Message.obtain(handler, GETCODE_FAIL_NONET).sendToTarget();
                    }
                    return back;
                }

                @Override
                protected void onPostExecute(AdContent back) {
                    if (back == null) {
                        if (timeCountThree != null) {
                            timeCountThree.cancel();
                        }
                        Message.obtain(handler, GETCODE_FAIL_NONET).sendToTarget();
//						Log.e("sda", "获取广告失败");
                    } else {
                        if (timeCountThree != null) {
                            timeCountThree.cancel();
                        }
                        if (back.getPlay_seconds() != null) {
                            play_time = Integer.parseInt(back.getPlay_seconds());
                            if (play_time == 0)
                                play_time = 5;
                        } else {
                            play_time = 5;
                        }
                        isLocalad=back.getIs_xunfei();
                        if(back.getIs_xunfei()==0){
                            if(mDownloader!=null){
                                mDownloader.download(back.getAdimg(), imageView, null);
                            }
                            jumpurl = back.getJumpurl();
                            timeCount = new TimeCount(play_time * 1000, 1000);
                            timeCount.start();
                        }else{
                            aid=back.getXfadid();
                        }
                        initAdview();


                    }

                }
            }.execute();
        } else {
            if (timeCountThree != null) {
                timeCountThree.cancel();
            }
            if (LoginActivity.instance != null) {
                LoginActivity.instance.resetStartState();
            }
            finish();
        }
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case GETCODE_FAIL_NONET:
                    if (LoginActivity.instance != null) {
                        LoginActivity.instance.resetStartState();
                    }
                    finish();
                    break;

                default:
                    break;
            }
        }

    };

    @Override
    public void onADLoaded(List<NativeADDataRef> list) {
        if (list.size() > 0) {
            adItem = list.get(0);
//            Toast.makeText(this, "原生广告加载成功", Toast.LENGTH_LONG).show();
//            try{
//                fileCache.clearUrlCache(adItem.getImage());
//            }catch (Exception e){
//                Log.i("adItem",e.toString());
//            }

//            if(mDownloader!=null){
//                mDownloader.download(adItem.getImage(), imageView, null);
//            }
            new BitmapDownloaderTask(imageView).execute(adItem.getImage());
            if(!NativeADDataRef.AD_DOWNLOAD.equals(adItem.getAdtype())){
                imageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        adItem.onClicked(view);
                    }
                });
            }
            //原生广告需上传点击位置
            imageView.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // TODO Auto-generated method stub
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            nativeAd.setParameter(AdKeys.CLICK_POS_DX, event.getX() + "");
                            nativeAd.setParameter(AdKeys.CLICK_POS_DY, event.getY() + "");
                            break;
                        case MotionEvent.ACTION_UP:
                            nativeAd.setParameter(AdKeys.CLICK_POS_UX, event.getX() + "");
                            nativeAd.setParameter(AdKeys.CLICK_POS_UY, event.getY() + "");
                            break;
                        default:
                            break;
                    }
                    return false;
                }
            });

            if (adItem.onExposured(imageView)){
                Log.i("adItem","曝光成功");
            }
            timeCountThree = new TimeCount(3000, 1000);
            timeCountThree.start();
        } else {
            Log.i("AD_DEMO", "NOADReturn");
            timeCountThree = new TimeCount(3000, 1000);
            timeCountThree.start();
        }

    }

    @Override
    public void onAdFailed(AdError adError) {
        Log.i("AD_DEMO", "NOADReturn"+adError.getErrorDescription()+"  "+adError.getErrorCode());
        timeCountThree = new TimeCount(3000, 1000);
        timeCountThree.start();
    }

    @Override
    public void onConfirm() {

    }

    @Override
    public void onCancel() {

    }

    /* 定义一个倒计时的内部类 */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            if (LoginActivity.instance != null)
                LoginActivity.instance.resetStartState();
            finish();
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MobclickAgent.onPageStart("AdShowActivity");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPageEnd("AdShowActivity");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        if (timeCount != null) {
            timeCount.cancel();
            timeCount = null;
        }
        if (timeCountThree != null) {
            timeCountThree.cancel();
            timeCountThree = null;
        }
        super.onDestroy();

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


    public void loadAD() {
        aid=getResources().getString(R.string.xunfei_advert_openpage_id);
        if (nativeAd == null) {
            nativeAd = new IFLYNativeAd(this,aid , this);
        }
        int count = 1; // 一次拉取的广告条数,当前仅支持一条
        nativeAd.loadAd(count);
    }


    /**
     * 根据一个网络连接(String)获取bitmap图像
     *
     * @param imageUri
     * @return
     * @throws
     */
    public  Bitmap getbitmap(String imageUri) {
        // 显示网络上的图片
        Bitmap bitmap = null;
        try {
            URL myFileUrl = new URL(imageUri);
            HttpURLConnection conn = (HttpURLConnection) myFileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();

        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            bitmap = null;
        } catch (IOException e) {
            e.printStackTrace();
            bitmap = null;
        }
        return bitmap;
    }

    class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageView;
        private String url;

        public BitmapDownloaderTask(ImageView imageView){
            this.imageView=imageView;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            url = params[0];
            return getbitmap(url);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                    int height = bitmap.getHeight();
                    int width = bitmap.getWidth();
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
                    int imgWidth = (int) (SportsApp.ScreenWidth);
                    lp.height = (int) ((imgWidth * height) / width);
                    lp.width = imgWidth;
                    imageView.setLayoutParams(lp);
                imageView.setImageBitmap(bitmap);
            }
        }
    }


}
