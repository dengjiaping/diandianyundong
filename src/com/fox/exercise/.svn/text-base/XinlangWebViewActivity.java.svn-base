package com.fox.exercise;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.umeng.analytics.MobclickAgent;

public class XinlangWebViewActivity extends BaseActivity {
    private static final String TAG = "WebViewActivity";
    private WebView webView;
    private Intent intent = null;
    public static XinlangWebViewActivity webInstance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_PROGRESS);
        boolean findMethod = SmartBarUtils.findActionBarTabsShowAtBottom();
        if (!findMethod) {
            // 取消ActionBar拆分，换用TabHost
            getWindow().setUiOptions(0);
            getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        setContentView(R.layout.xinlang_webview);
        setTitle(getString(R.string.app_name));


        webInstance = this;
        Log.d(TAG, "WebViewActivity inited");
        mContext = getApplicationContext();
        webView = (WebView) findViewById(R.id.web);
        webView.setInitialScale(10);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSaveFormData(true);
        webSettings.setSavePassword(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                webView.requestFocus();
                return false;
            }
        });

        intent = this.getIntent();
        Log.d(TAG, "webview intent :" + intent);
        if (!intent.equals(null)) {
            Bundle b = intent.getExtras();
            if (b != null && b.containsKey("url")) {
                Log.d(TAG, "url from shareImage :" + b.getString("url"));
//		    	String url=b.getString("url");
                webView.loadUrl(b.getString("url"));
                Log.d(TAG, "url loaded :" + b.getString("url"));
                webView.setWebChromeClient(new WebChromeClient() {
                    public void onProgressChanged(WebView view, int progress) {
                        setTitle(getString(R.string.share_web_loading) + progress + "%");
                        setProgress(progress * 100);

                        if (progress == 100) setTitle(R.string.app_name);
                        Log.d(TAG, "title set to :" + R.string.app_name);
                    }
                });
            }
        }
        if (findMethod) {
            getActionBar().setDisplayShowHomeEnabled(false);
            getActionBar().setDisplayShowTitleEnabled(false);
            SmartBarUtils.setActionModeHeaderHidden(getActionBar(), true);
            SmartBarUtils.setActionBarViewCollapsable(getActionBar(), true);
        }
//		webView.addJavascriptInterface(new JavaScriptInterface(),"Methods");
//		WebViewClient wvc=new WebViewClient();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("XinlangWebViewActivity");
        MobclickAgent.onPause(XinlangWebViewActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("XinlangWebViewActivity");
        MobclickAgent.onResume(XinlangWebViewActivity.this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        webView.removeAllViews();
        webView = null;
        Log.v("webview", "onDestroy");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}