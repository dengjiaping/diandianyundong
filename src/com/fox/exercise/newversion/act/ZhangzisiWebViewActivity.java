package com.fox.exercise.newversion.act;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fox.exercise.AbstractBaseActivity;
import com.fox.exercise.R;
import com.fox.exercise.api.ApiConstant;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.entity.ActListInfo;
import com.umeng.analytics.MobclickAgent;

import java.io.File;

import cn.ingenic.indroidsync.SportsApp;

public class ZhangzisiWebViewActivity extends AbstractBaseActivity {

    private WebView webView;
    private String webUrl;
    private String APP_CACAHE_DIRNAME = "/webcache";
    private LinearLayout loading_layout;
    private ImageView img_share;
    private String Xiangqinurl = ApiConstant.DATA_URL
            + ApiConstant.getZhangzisInfo;
    Intent intent;
    int bs, infoid, tbs, xid;
    String info_title, img, jianjie;
    ActListInfo list;
    private SportsApp mSportsApp;

    @Override
    public void initIntentParam(Intent intent) {
        intent = getIntent();
        title = getResources().getString(R.string.doubi);
        tbs = intent.getIntExtra("tbs", -1);
         if (tbs == 2) {
         title = getResources().getString(R.string.zhangzisi);
         } else if (tbs == 1) {
         title = getResources().getString(R.string.doubi);
         }
        bs = intent.getIntExtra("bs", -1);
        infoid = intent.getIntExtra("infoid", -1);
        // info_title = intent.getStringExtra("title");
        // img = intent.getStringExtra("img_url");
        if (bs == 1) {
            // Toast.makeText(getApplicationContext(), "bs==1", 10).show();
            webUrl = intent.getStringExtra("gengduourl");
        } else if (bs == 2) {
            // Toast.makeText(getApplicationContext(), "bs==2", 10).show();
            webUrl = Xiangqinurl + intent.getIntExtra("infoid", -1);
        }
        // Toast.makeText(getApplicationContext(), webUrl, 10).show();
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        showContentView(R.layout.tran_webview);
        mSportsApp = (SportsApp) getApplication();
        // Log.d(TAG, "WebViewActivity inited");
        webView = (WebView) findViewById(R.id.web_train);
        loading_layout = (LinearLayout) findViewById(R.id.loading_layout);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        list = new ActListInfo();
        img_share = new ImageView(this);
        img_share.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        img_share.setImageResource(R.drawable.bushutongji_fenxiang);
        left_ayout.setOnClickListener(new mClickListen());
        leftButton.setOnClickListener(new mClickListen());
        if (webUrl.contains("a=activityinfo")) {
//			Log.e("UUUU", webUrl);
            String[] str = webUrl.split("&");
            String conn = str[2];
            String[] ss = conn.split("=");
            infoid = Integer.parseInt(ss[1]);
            getActivioTitle();
            showRightBtn(img_share);
            right_btn.setOnClickListener(new ZhangzisiShareLister());
        }
        loadWebView();
    }

    @Override
    public void setViewStatus() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageResume() {
        // TODO Auto-generated method stub
        MobclickAgent.onPageStart("Zhang_webViewClass");
    }

    @Override
    public void onPagePause() {
        // TODO Auto-generated method stub
        MobclickAgent.onPageEnd("Zhang_webViewClass");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            // 返回键退回
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onPageDestroy() {
        // TODO Auto-generated method stub
        clearWebViewCache();
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        webView.removeAllViews();
        webView.destroy();
        webView = null;
    }

    class mClickListen implements OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                finish();
            }

        }
    }

    private void loadWebView() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setRenderPriority(RenderPriority.HIGH);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT); // 设置 缓存模式
        // 开启 DOM storage API 功能
        webSettings.setDomStorageEnabled(true);
        // 开启 database storage API 功能
        webSettings.setDatabaseEnabled(true);
        String cacheDirPath = getFilesDir().getAbsolutePath()
                + APP_CACAHE_DIRNAME;
        Log.i(TAG, "cacheDirPath=" + cacheDirPath);
        // 设置数据库缓存路径
        webSettings.setDatabasePath(cacheDirPath);
        // 设置 Application Caches 缓存目录
        webSettings.setAppCachePath(cacheDirPath);
        // 开启 Application Caches 功能
        webSettings.setAppCacheEnabled(true);
        webSettings.setUserAgentString("mfox");
        webView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//				Log.e("wUU", url);
                if (url.contains("a=activityinfo")) {
                    showRightBtn(img_share);
                    right_btn.setOnClickListener(new ZhangzisiShareLister());
                    String[] str = url.split("&");
                    String conn = str[2];
                    String[] ss = conn.split("=");
                    infoid = Integer.parseInt(ss[1]);
                    getActivioTitle();

                }
                Log.e("AURL", url);

                view.loadUrl(url);
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

    public void getActivioTitle() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                list = ApiJsonParser.getInfoTitle(mSportsApp.getSessionId(),
                        infoid + "");
                mHandler.sendEmptyMessage(121);
            }
        }).start();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 121:
                    // Toast.makeText(
                    // getApplication(),
                    // "infoid:" + infoid + "....titel:" + list.getTitle()
                    // + "....img:" + list.getThumb(), 10).show();
                    if (list!=null){
                        img = list.getThumb();
                        info_title = list.getTitle();
                        jianjie = list.getDescription();
                    }
                    break;

                default:
                    break;
            }
        }

        ;
    };

    /**
     * 清除WebView缓存
     */
    public void clearWebViewCache() {

        // 清理Webview缓存数据库
        try {
            deleteDatabase("webview.db");
            deleteDatabase("webviewCache.db");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // WebView 缓存文件
        File appCacheDir = new File(getFilesDir().getAbsolutePath()
                + APP_CACAHE_DIRNAME);
        Log.e(TAG, "appCacheDir path=" + appCacheDir.getAbsolutePath());

        File webviewCacheDir = new File(getCacheDir().getAbsolutePath()
                + "/webviewCache");
        Log.e(TAG, "webviewCacheDir path=" + webviewCacheDir.getAbsolutePath());

        // 删除webview 缓存目录
        if (webviewCacheDir.exists()) {
            deleteFile(webviewCacheDir);
        }
        // 删除webview 缓存 缓存目录
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

        Log.i(TAG, "delete file path=" + file.getAbsolutePath());

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
            Log.e(TAG, "delete file no exists " + file.getAbsolutePath());
        }
    }

    /**
     * 分享单击事件
     */
    class ZhangzisiShareLister implements OnClickListener {

        @Override
        public void onClick(View v) {
            Intent mIntent = new Intent(getApplication(), ShareActivity.class);
            mIntent.putExtra("infoid", infoid);
            mIntent.putExtra("title", info_title);
            mIntent.putExtra("img_url", img);
            mIntent.putExtra("bs", 1);
            mIntent.putExtra("jianjie", jianjie);
            startActivity(mIntent);
        }
    }

}
