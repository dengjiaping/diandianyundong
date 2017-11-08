package com.fox.exercise.login;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fox.exercise.R;
import com.fox.exercise.SmartBarUtils;
import com.umeng.analytics.MobclickAgent;

import cn.ingenic.indroidsync.SportsApp;

public class JDAuthActivity extends Activity {
    private Dialog JDYPregressDialog = null;
    private WebView webView;
    private Bundle params = new Bundle();
    private String url = "";
    //private ProgressDialog progressDialog;
    private String JDOptionAppKey = "";
    private String JDOptionAppSecret = "";
    private String JDOptionAppRedirectUri = "";
    //	private JdListener jdListener;
    private TextView tvLeft;
    private RelativeLayout rlNavigation;
    private int color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        boolean findMethod = SmartBarUtils.findActionBarTabsShowAtBottom();
        if (!findMethod) {
            // 取消ActionBar拆分，换用TabHost
            getWindow().setUiOptions(0);
            getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        setContentView(R.layout.activity_jd_auth);
        tvLeft = (TextView) findViewById(R.id.tv_left);
        rlNavigation = (RelativeLayout) findViewById(R.id.navigation);
        tvLeft.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        Bundle bundle = getIntent().getBundleExtra("JDAuth");
        if (null != bundle) {
            JDOptionAppKey = bundle.getString("JDOptionAppKey");
            JDOptionAppSecret = bundle.getString("JDOptionAppSecret");
            JDOptionAppRedirectUri = bundle.getString("JDOptionAppRedirectUri");
            color = bundle.getInt("NavaigationColor", R.color.red);
        }
        rlNavigation.setBackgroundColor(getResources().getColor(color));
        /*progressDialog=new ProgressDialog(this);
		progressDialog.setMessage(R.string.sports_wait);*/
        initJDYProgressDialog();
        webView = (WebView) findViewById(R.id.wv_auth);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setWebViewClient(new JdWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSavePassword(true);
        webView.getSettings().setSaveFormData(true);
        params.putString("response_type", "code");
        params.putString("client_id", JDOptionAppKey);
        params.putString("redirect_uri", JDOptionAppRedirectUri);
        params.putString("state", "GET_AUTH_KEY");
        params.putString("view", "wap");
        url = JDConfigs.URL_AUTH + UrlUtil.encodeUrl(params);
        webView.loadUrl(url);
        if (findMethod) {
            getActionBar().setDisplayShowHomeEnabled(false);
            getActionBar().setDisplayShowTitleEnabled(false);
            SmartBarUtils.setActionModeHeaderHidden(getActionBar(), true);
            SmartBarUtils.setActionBarViewCollapsable(getActionBar(), true);
        }
    }

    private void initJDYProgressDialog() {
        JDYPregressDialog = new Dialog(JDAuthActivity.this,
                R.style.sports_dialog);
        LayoutInflater mInflater = getLayoutInflater();
        View v1 = mInflater.inflate(R.layout.sports_progressdialog, null);
        TextView message = (TextView) v1.findViewById(R.id.message);
        message.setText(R.string.sports_wait);
        v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
        JDYPregressDialog.setContentView(v1);
        JDYPregressDialog.setCancelable(true);
    }


    private class JdWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String murl, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
            if (JDYPregressDialog != null) {
                JDYPregressDialog.show();
            }

        }

        @Override
        public void onPageFinished(WebView view, String murl) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, murl);

            System.out.println(murl);
            if (murl.startsWith(JDOptionAppRedirectUri)) {
                Bundle values = UrlUtil.parseUrl(murl);
                String error = values.getString("error");
                if (error == null) {
                    error = values.getString("error_type");
                }
                if (error == null) {
                    String authKey = values.getString(JDConfigs.AUTH_KEY);
                    new GetAccessTokenTask().execute(authKey);
                }

            } else if (murl.startsWith(JDConfigs.URL_REGISTER)) {
                webView.loadUrl(url);
            } else {
                if (JDYPregressDialog != null) {
                    JDYPregressDialog.dismiss();
                }
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            Log.e("url", url);
            webView.loadUrl(url);
            return false;
        }


    }


    private class GetAccessTokenTask extends AsyncTask<String, Integer, Object> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(String... params) {
            // TODO Auto-generated method stub
            String authKey = params[0];
            Bundle parameters = new Bundle();
            parameters.putString("grant_type", "authorization_code");
            parameters.putString("client_id", JDOptionAppKey);
            parameters.putString("redirect_uri", JDOptionAppRedirectUri);
            parameters.putString("client_secret", JDOptionAppSecret);
            parameters.putString("state", "GET_TOKEN");
            parameters.putString("code", authKey);
            String url = JDConfigs.URL_TOKEN + UrlUtil.encodeUrl(parameters);
            try {
                String sReponse = UrlUtil.openUrl(url, "POST", parameters);
                System.out.println(sReponse);
                return sReponse;
            } catch (Exception e) {
                Log.e("Jingdong-WebView", "can not get access code: ", e);
                return e;
            }
        }

        @Override
        protected void onPostExecute(Object result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            JDYPregressDialog.dismiss();
            if (result instanceof Exception) {
                ((Exception) result).printStackTrace();
            } else if (result instanceof String) {
                Intent intent = new Intent();
                intent.putExtra("result", (String) result);
                setResult(1000, intent);
                JDAuthActivity.this.finish();
            } else {
//                jdListener.onError(new Exception("Impossible path!"));
            }
        }


    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPause(this);
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

 