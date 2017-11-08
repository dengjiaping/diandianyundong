package com.fox.exercise.login;


import com.fox.exercise.R;
import com.fox.exercise.SmartBarUtils;
import com.fox.exercise.weibo.sina.oauth2.SslError;
import com.fox.exercise.weibo.tencent.proxy.QQWeiboProxy;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

public class TencentAuthorizeActivity extends Activity {
    private static final String TAG = "AuthorizeActivity";
    public static TencentAuthorizeActivity authInstance = null;
    private View progressBar;
    //	private Context mContext;
    private WebView mWebView;
    private WebViewClient mWebViewClient;
    private QQWeiboProxy mQqWeiboProxy;
    private FrameLayout tencent_layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean findMethod = SmartBarUtils.findActionBarTabsShowAtBottom();
        if (!findMethod) {
            // 取消ActionBar拆分，换用TabHost
            getWindow().setUiOptions(0);
            getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        setContentView(R.layout.tencent_webview);
//		mContext = this;
        authInstance = this;
        initView();
        initData();
        if (findMethod) {
            getActionBar().setDisplayShowHomeEnabled(false);
            getActionBar().setDisplayShowTitleEnabled(false);
            SmartBarUtils.setActionModeHeaderHidden(getActionBar(), true);
            SmartBarUtils.setActionBarViewCollapsable(getActionBar(), true);
        }
    }

    private void initView() {
        tencent_layout = (FrameLayout) findViewById(R.id.tencent_layout);
        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.requestFocus();
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        progressBar = findViewById(R.id.show_request_progress_bar);
    }

    private void initData() {
        mWebViewClient = new WeiboWebViewClient();
        mWebView.setWebViewClient(mWebViewClient);
        CookieSyncManager.createInstance(this);
        mQqWeiboProxy = QQWeiboProxy.getInstance();
        String urlStr = mQqWeiboProxy.getAuthoUrl();
        mWebView.requestFocus();
        mWebView.loadUrl(urlStr);
    }

    private void showProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    private void hideProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private class WeiboWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            showProgress();
            Log.e(TAG, "onPageStarted URL = " + url);
            if (url.indexOf("access_token=") != -1) {
                int start = url.indexOf("access_token=");
                String responseData = url.substring(start);
                boolean ret = mQqWeiboProxy.parseAccessTokenAndOpenId(responseData);
                if (ret) {
                    String access_token = mQqWeiboProxy.getAccessToken();
                    String expires_in = mQqWeiboProxy.getExpireIn();
                    String openID = mQqWeiboProxy.getOpenID();
                    String openKey = mQqWeiboProxy.getOpenKey();
                    Log.e(TAG, "access_token = " + access_token + "\nexpires_in = " + expires_in +
                            "\nopenID = " + openID + "\nopenKey = " + openKey);
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("access_token", access_token);
                    bundle.putString("expires_in", expires_in);
                    bundle.putString("openID", openID);
                    bundle.putString("openKey", openKey);
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(TencentAuthorizeActivity.this, "Autho Fail...", Toast.LENGTH_SHORT).show();
                }
                tencent_layout.removeView(view);
                view.destroyDrawingCache();
                view.removeAllViews();
                view.destroy();
            }
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            hideProgress();
            super.onPageFinished(view, url);
        }

        /*
         * TODO Android2.2及以上版本才能使用该方法
         * 目前https://open.t.qq.com中存在http资源会引起sslerror，待网站修正后可去掉该方法
         */
        @SuppressWarnings("unused")
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            if ((null != view.getUrl()) && (view.getUrl().startsWith("https://open.t.qq.com"))) {
                handler.proceed();// 接受证书
            } else {
                handler.cancel(); // 默认的处理方式，WebView变成空白页
            }
            // handleMessage(Message msg); 其他处理
        }
    }
}
