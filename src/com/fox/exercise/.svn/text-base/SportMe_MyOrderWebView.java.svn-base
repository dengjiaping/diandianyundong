package com.fox.exercise;

import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.umeng.analytics.MobclickAgent;

import java.io.File;

import cn.ingenic.indroidsync.SportsApp;

/**
 * Created by zhonghuibin on 2016/8/1.  个人主页我的订单
 */
public class SportMe_MyOrderWebView extends AbstractBaseActivity{
    private static final String TAG = "YunHuWebViewActivity";
    //	private static final String URL = ApiConstant.URL
//			+ "/Beauty/kupao.php?m=Webapp&a=url_jump&id=1&session_id=";;
    private String APP_CACAHE_DIRNAME = "/webcache";

    private String webUrl;
    private WebView webView;
    private LinearLayout loading_layout;
    private int index;
//	private static final int IVEW_ID = 99;
//	private int count = 1;

    @Override
    public void initIntentParam(Intent intent) {
        // TODO Auto-generated method stub
        title = getResources().getString(R.string.sports_myorder_me);
        if (intent != null) {
            webUrl = intent.getStringExtra("webUrl");
            index = intent.getIntExtra("index",0);
        }
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        showContentView(R.layout.tran_webview);
        mSportsApp = (SportsApp) getApplication();
        Log.d(TAG, "WebViewActivity inited");
        webView = (WebView) findViewById(R.id.web_train);
        loading_layout = (LinearLayout) findViewById(R.id.loading_layout);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
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
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void loadWebView() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
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
}
