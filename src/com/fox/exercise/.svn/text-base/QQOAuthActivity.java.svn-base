package com.fox.exercise;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class QQOAuthActivity extends Activity {

    private WebView mLoginWebView = null;

    private static final String TAG = "QQOAuthActivity";

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
        setContentView(R.layout.qq_oauth_activity_layout);
        initWebView();
        if (findMethod) {
            getActionBar().setDisplayShowHomeEnabled(false);
            getActionBar().setDisplayShowTitleEnabled(false);
            SmartBarUtils.setActionModeHeaderHidden(getActionBar(), true);
            SmartBarUtils.setActionBarViewCollapsable(getActionBar(), true);
        }
        // Intent intent= new Intent();
        // intent.setAction("android.intent.action.VIEW");
        // Uri content_url = Uri.parse("http://www.cnblogs.com");
        // intent.setData(content_url);
        // startActivity(intent);
    }

    private void initWebView() {
        mLoginWebView = (WebView) findViewById(R.id.qq_oauth_webView);
        mLoginWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                QQOAuthActivity.this.setProgress(newProgress * 100);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                if (message.length() > 0) {
                    Log.d(TAG, "onJsAlert");
                    Builder builder = new Builder(QQOAuthActivity.this);
                    builder.setMessage(message);
                    builder.setPositiveButton(getResources().getString(R.string.button_ok), new OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            result.confirm();
                        }
                    });
                    builder.setNegativeButton(getResources().getString(R.string.button_cancel), new OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            result.cancel();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                if (message.length() > 0) {
                    Log.d(TAG, "onJsConfirm");
                    Builder builder = new Builder(QQOAuthActivity.this);
                    builder.setMessage(message);
                    builder.setPositiveButton(getResources().getString(R.string.button_ok), new OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            result.confirm();
                        }
                    });
                    builder.setNegativeButton(getResources().getString(R.string.button_cancel), new OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            result.cancel();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue,
                                      final JsPromptResult result) {
                if (message.length() > 0) {
                    Log.d(TAG, "onJsPrompt");
                    Builder builder = new Builder(QQOAuthActivity.this);
                    builder.setMessage(message);
                    builder.setPositiveButton(getResources().getString(R.string.button_ok), new OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            result.confirm();
                        }
                    });
                    builder.setNegativeButton(getResources().getString(R.string.button_cancel), new OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            result.cancel();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public boolean onJsTimeout() {
                // TODO Auto-generated method stub
                return super.onJsTimeout();
            }

        });
        mLoginWebView.requestFocus();
        WebSettings setting = mLoginWebView.getSettings();
        setting.setJavaScriptEnabled(true);
        setting.setUseWideViewPort(true);
        setting.setLoadWithOverviewMode(true);
        mLoginWebView.setWebViewClient(new LoginWebViewClient());
        String url = "https://openmobile.qq.com/oauth2.0/m_authorize" + "?response_type=token" + "&client_id="
                + AllWeiboInfo.TENCENT_APPID + "&scope=add_share,get_simple_userinfo"
                + "&redirect_uri=http://open.z.qq.com/moc2/success.jsp";
        mLoginWebView.loadUrl(url);
        mLoginWebView.requestFocus();
    }

    class LoginWebViewClient extends WebViewClient {

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//			super.onReceivedSslError(view, handler, error);
            handler.proceed();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (url.contains("access_token") && url.contains("expires_in")) {

                Log.d(TAG, "url:" + url);

                int start = url.indexOf("#");
                start += "access_token".length() + 2;
                int end = url.indexOf("expires_in") - 1;

                AllWeiboInfo.TENCENT_QQZONE_TOKEN = url.substring(start, end);
                Log.d(TAG, "AllWeiboInfo.TENCENT_QQZONE_TOKEN:" + AllWeiboInfo.TENCENT_QQZONE_TOKEN);

                SharedPreferences sp = QQOAuthActivity.this.getSharedPreferences(AllWeiboInfo.TENCENT_QQZONE_TOKEN_SP,
                        MODE_PRIVATE);
                Editor e = sp.edit();
                e.putString(AllWeiboInfo.TENCENT_QQZONE_TOKEN_KEY, AllWeiboInfo.TENCENT_QQZONE_TOKEN);
                e.commit();

                QQOAuthActivity.this.setResult(11);
                QQOAuthActivity.this.finish();
            }
        }
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
