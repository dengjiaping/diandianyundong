package com.fox.exercise;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.entity.MadelInfo;
import com.fox.exercise.login.Tools;
import com.fox.exercise.newversion.act.ShareActivity2;
import com.fox.exercise.publish.Bimp;
import com.umeng.analytics.MobclickAgent;

import java.io.File;

import cn.ingenic.indroidsync.SportsApp;

/**
 * Created by zhonghuibin on 2016/8/1.  个人主页我的勋章
 */
public class SportMe_MedalWebView extends AbstractBaseActivity{
    private static final String TAG = "YunHuWebViewActivity";
    private String APP_CACAHE_DIRNAME = "/webcache";

    private String webUrl;
    private WebView webView;
    private LinearLayout loading_layout;
    private int index,infoid,medaltype;
    private ImageView img_share;
    private MadelInfo madelInfo_me;
    private String medal_name,medal_pic;
    private int medalid;
    public  static boolean saveFile;
    private String nowUrl;
    private SportsApp mSportsApp;
    public static final String SHARE_PATH = SportsUtilities.DOWNLOAD_SAVE_PATH
            + "shareImage.jpg";

    @Override
    public void initIntentParam(Intent intent) {
        // TODO Auto-generated method stub
        title = getResources().getString(R.string.sports_mymedal_me);
        mSportsApp = (SportsApp) getApplication();
        if (intent != null) {
            webUrl = intent.getStringExtra("webUrl");
            index = intent.getIntExtra("index",0);
        }
        webUrl = getWebUrl(webUrl);
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        showContentView(R.layout.tran_webview);
        mSportsApp = (SportsApp) getApplication();
        left_ayout.setOnClickListener(new clickListener1());
        leftButton.setOnClickListener(new clickListener1());
        madelInfo_me = new MadelInfo();
        webView = (WebView) findViewById(R.id.web_train);
        loading_layout = (LinearLayout) findViewById(R.id.loading_layout);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        img_share = new ImageView(this);
        img_share.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        img_share.setImageResource(R.drawable.sportdetail_share);
        if (webUrl != null && webUrl.contains("a=MyMedallistinfo")) {
            String[] str = webUrl.split("&");
            String conn = str[2];
            String[] ss = conn.split("=");
            infoid = Integer.parseInt(ss[1]);
            showRightBtn(img_share);
            right_btn.setOnClickListener(new MymedalShareLister());
            right_btn.setVisibility(View.VISIBLE);
            getActivioTitle();
        }
        loadWebView();
    }

    @Override
    public void setViewStatus() {
        // TODO Auto-generated method stub
    }

    @Override
    public void onPageResume() {
        MobclickAgent.onPageStart("Train_webViewClass");
    }

    @Override
    public void onPagePause() {
        MobclickAgent.onPageEnd("Train_webViewClass");
    }

    @Override
    public void onPageDestroy() {
        // TODO Auto-generated method stub
        mHandler.removeCallbacksAndMessages(null);
        clearWebViewCache();
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        webView.removeAllViews();
        webView.destroy();
        webView = null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            // 返回键退回
            webView.goBack();
            right_btn.setVisibility(View.GONE);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    class clickListener1 implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (webView.canGoBack()) {
                webView.goBack();
                right_btn.setVisibility(View.GONE);
            }else{
                finish();
            }
        }
    }

    private void loadWebView() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        String cacheDirPath = getFilesDir().getAbsolutePath()
                + APP_CACAHE_DIRNAME;
//        String appCacheDir = this.getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath();
        webSettings.setDatabasePath(cacheDirPath);
        webSettings.setAppCachePath(getCacheDir().getPath());
        webSettings.setAppCacheEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setUserAgentString("mfox");
        if(SportsApp.getInstance().isOpenNetwork()){
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT); // 设置 缓存模式
        }else{
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); // 设置 缓存模式
        }
        webView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null && url.contains("a=MyMedallistinfo")) {
                    showRightBtn(img_share);
                    right_btn.setOnClickListener(new MymedalShareLister());

                    String[] str = url.split("&");
                    String conn = str[2];
                    String conn2 = str[3];
                    String[] ss = conn.split("=");
                    String[] ss2 = conn2.split("=");
                    infoid = Integer.parseInt(ss[1]);
                    medaltype = Integer.parseInt(ss2[1]);
                    infoid = Integer.parseInt(ss[1]);
                    if (medaltype == 2 && mSportsApp.isOpenNetwork()){
                        right_btn.setVisibility(View.VISIBLE);
                        getActivioTitle();
                    }else{
                        right_btn.setVisibility(View.GONE);
                    }
                }
                view.loadUrl(getWebUrl(url));
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100)
                    loading_layout.setVisibility(View.GONE);
            }
        });
        webView.loadUrl(webUrl);
    }

    public void clearWebViewCache() {
        try {
            deleteDatabase("webview.db");
            deleteDatabase("webviewCache.db");
        } catch (Exception e) {
            e.printStackTrace();
        }

        File appCacheDir = new File(getFilesDir().getAbsolutePath()
                + APP_CACAHE_DIRNAME);

        File webviewCacheDir = new File(getCacheDir().getAbsolutePath()
                + "/webviewCache");

        if (webviewCacheDir.exists()) {
            deleteFile(webviewCacheDir);
        }
        if (appCacheDir.exists()) {
            deleteFile(appCacheDir);
        }
    }

    /**
     * 递归删除 文件/文件夹
     *
     * @param file
     */
    public void deleteFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
            file.delete();
        } else {
        }
    }

    public void getActivioTitle() {
        if (medal_name != null){
            medal_name = null;
        }
        if (medal_pic != null){
            medal_pic = null;
        }
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                madelInfo_me = ApiJsonParser.getMedalInfo(mSportsApp.getSessionId(),
                        infoid + "");
                mHandler.sendEmptyMessage(1123);
            }
        }).start();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1123:
                    if (madelInfo_me!=null){
                        medal_name = madelInfo_me.getMedal_name();
                        medal_pic = madelInfo_me.getMedal_pic();
                        medalid = madelInfo_me.getId();
                    }
                    break;

                default:
                    break;
            }
        }

        ;
    };
    //分享运动秀屏幕截图
    public Bitmap shot() {
        View view = getWindow().getDecorView();
        Display display = this.getWindowManager().getDefaultDisplay();
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusheight = frame.top;// 手机状态栏高度
        view.setDrawingCacheEnabled(true);// 允许当前窗口保存缓存信息，这样getDrawingCache()方法才会返回一个Bitmap
        Bitmap  bitmap = Bitmap.createBitmap(view.getDrawingCache(), 0, statusheight + top_title_layout.getHeight(),
                display.getWidth(), webView.getHeight());
        view.destroyDrawingCache();
        return bitmap;
    }

    /**
     * 分享单击事件
     */
    class MymedalShareLister implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Bitmap bitmap = shot();
            saveFile = Tools.SaveBitmapAsFile(SHARE_PATH, bitmap);
            if(bitmap!=null){
                bitmap.recycle();
                bitmap=null;
            }
            if (Bimp.drr.size() > 0){
                Bimp.drr.clear();
            }
            Intent mIntent = new Intent(getApplication(), ShareActivity2.class);
            mIntent.putExtra("infoid", infoid);
            if(medal_name==null){
                medal_name="";
            }
            mIntent.putExtra("title", "我获得了一枚"+ medal_name +"勋章");
            mIntent.putExtra("img_url", medal_pic);
            mIntent.putExtra("jianjie", getResources().getString(R.string.sports_model_sharedecrtion));
            startActivity(mIntent);
        }
    }


    /**
     *@method 获取webUrl(没网加载本地html)
     *@author suhu
     *@time 2016/12/12 15:34
     *@param url
     *
    */
    private String getWebUrl(String url){
        if (mSportsApp.isOpenNetwork()){
            return url;
        }else {
            return "file:///android_asset/offline/offline.html";
        }
    }




}
