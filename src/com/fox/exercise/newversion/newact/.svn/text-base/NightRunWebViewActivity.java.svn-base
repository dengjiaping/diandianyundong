package com.fox.exercise.newversion.newact;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fox.exercise.AbstractBaseActivity;
import com.fox.exercise.CoolCurrencyRulesActivity;
import com.fox.exercise.R;
import com.fox.exercise.SportsExceptionHandler;
import com.fox.exercise.api.ApiConstant;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.api.entity.UserDetail;
import com.fox.exercise.newversion.entity.ExternalActivi;
import com.fox.exercise.newversion.entity.WXxdback;
import com.fox.exercise.newversion.entity.YePaoInfo;
import com.fox.exercise.wxutil.Constants;
import com.fox.exercise.wxutil.MD5;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.NameValuePair;
import org.apache.http.conn.util.InetAddressUtils;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cn.ingenic.indroidsync.SportsApp;

public class NightRunWebViewActivity extends AbstractBaseActivity {
    private static final String TAG = "NightRunWebViewActivity";
    private static final String URL1 = ApiConstant.URL
            + "/Beauty/kupao.php?m=Webapp&a=url_jump&id=1&session_id=";
    private static final String APP_CACAHE_DIRNAME = "/webcache";
    // private SportsApp mSportsApp;
    private WXxdback wb = null, wbpay = null;
    private WebView webView;
    private LinearLayout lodingLayout;
    private SportsExceptionHandler mExceptionHandler = null;
    private TextView money_rule;
    private static final int IVEW_ID = 99;
    public static int biaoshi = 1;
    private ExternalActivi exActivi;// 活动对象
    private String uus, texturi;
    private LinearLayout mls_fl;
    private Button mls_btnpay;
    private YePaoInfo yp;// 夜跑对象
    IWXAPI msgApi;
    PayReq req;
    private String ip, entity, type = "APP", sjzf, numno;
    String wxurl = ApiConstant.URL + "/Beauty/yepaonotify_url.php";
    public static String sessionid, numid;

    private int returnFlg;
    private Dialog mLoadProgressDialog = null;

    @Override
    public void initIntentParam(Intent intent) {
        // TODO Auto-generated method stub
        mSportsApp = (SportsApp) getApplication();
        if (intent != null) {
            int isfrom=intent.getIntExtra("isfrom",-1);//1表示从弹出框跳转过来的
            exActivi = (ExternalActivi) intent
                    .getSerializableExtra("ExternalActivi");
            title = exActivi.getTitle();
            if(isfrom==1){
                uus = exActivi.getUrl() + "&session_id="
                        + mSportsApp.getSessionId() + "&id=" + exActivi.getWeb_id();
            }else{
                uus = exActivi.getUrl() + "&session_id="
                        + mSportsApp.getSessionId() + "&id=" + exActivi.getId();
            }
        }

    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        showContentView(R.layout.yunhu_webview_only);
        top_title_layout.setVisibility(View.VISIBLE);
        /*
         * boolean findMethod = SmartBarUtils.findActionBarTabsShowAtBottom();
		 * if (!findMethod) { // 取消ActionBar拆分，换用TabHost
		 * getWindow().setUiOptions(0);
		 * getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE); }
		 */
        mExceptionHandler = mSportsApp.getmExceptionHandler();
        lodingLayout = (LinearLayout) findViewById(R.id.loading_layout);
        lodingLayout.setVisibility(View.VISIBLE);
        mls_fl = (LinearLayout) findViewById(R.id.mls_pay);
        mls_btnpay = (Button) findViewById(R.id.mls_pay_btn);
        mls_btnpay.setOnClickListener(new payListener());
        // mls_fl.setOnClickListener(new payListener());
        Log.d(TAG, "WebViewActivity inited");
        webView = (WebView) findViewById(R.id.web);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        loadWebView();
		/*
		 * if(findMethod){ getActionBar().setDisplayShowHomeEnabled(false);
		 * getActionBar().setDisplayShowTitleEnabled(false);
		 * SmartBarUtils.setActionModeHeaderHidden(getActionBar(), true);
		 * SmartBarUtils.setActionBarViewCollapsable(getActionBar(), true); }
		 */
        leftButton.setOnClickListener(new clickListener());
        left_ayout.setOnClickListener(new clickListener());
        money_rule = new TextView(this);
        req = new PayReq();
        sjzf = genNonceStr();
        ip = getLocalHostIp();
        money_rule.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        money_rule.setId(+IVEW_ID);
        money_rule.setOnClickListener(new RightClickLiser());
        money_rule.setText(getResources().getString(R.string.cool_coins_rules));
        money_rule.setTextColor(getResources().getColor(R.color.white));
        money_rule.setTextSize(14);
        money_rule.setGravity(Gravity.CENTER);
//        showRightBtn(money_rule);
//        right_btn.setPadding(0, 0, mSportsApp.dip2px(17), 0);
        /**
         * order by jpf 在Fragment中使用以下方法监听返回键，原因是Fragment不能复写onKeyDown
         */
		/*
		 * webView.setOnKeyListener(new OnKeyListener() {
		 *
		 * @Override public boolean onKey(View v, int keyCode, KeyEvent event) {
		 * // TODO Auto-generated method stub if (event.getAction() ==
		 * KeyEvent.ACTION_DOWN) { if (keyCode == KeyEvent.KEYCODE_BACK &&
		 * webView.canGoBack()) { //表示按返回键 时的操作 webView.goBack(); //后退 return
		 * true; //已处理 } } return false; } });
		 */
        mSportsApp.setmYePaoHandler(mHandler);// 设置夜跑的handler
    }

    // 支付按钮单击事件
    class payListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            waitShowDialog();
            Data();

        }
    }

    public void Data() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub

                try {
                    yp = ApiJsonParser.getYePaoInfo(mSportsApp.getSessionId(),
                            exActivi.getId(), 1);
                    wb = ApiJsonParser.gettongyiInfo(Constants.APP_ID,
                            yp.getMls_name(), Constants.MCH_ID, sjzf, wxurl,
                            yp.getOrder_nu(), ip, yp.getMls_price(), type);
                    // 预支付id
                    entity = genProductArgs();
                    wbpay = ApiJsonParser.getPayid(entity, 1);
                    sessionid = mSportsApp.getSessionId();
                    numid = yp.getOrder_nu();
                    mHandler.sendEmptyMessage(200);
                } catch (ApiNetException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (ApiSessionOutException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 200:
                    genPayReq();
                    break;
                case 300:
                    mls_fl.setVisibility(View.GONE);
                    clearWebViewCache();
                    webView.loadUrl(uus);
                    returnFlg = 2;
                    break;
                case 400:
                    if (mLoadProgressDialog != null
                            && mLoadProgressDialog.isShowing()) {
                        mLoadProgressDialog.dismiss();
                    }
                case 500:
                    mls_fl.setVisibility(View.GONE);
                    clearWebViewCache();
                    webView.loadUrl(uus);
                    returnFlg = 2;
                    break;

                default:
                    break;
            }
        }

        ;
    };

    class clickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (webView.canGoBack()) {
                webView.goBack(); // 后退
                if (webView
                        .getUrl()
                        .equals(ApiConstant.DATA_URL+"m=Webshop&a=goods_list")) {
                    webView.loadUrl(ApiConstant.DATA_URL+"m=Webshop&a=goods_list");
                }
                //左上角返回图标支付按钮隐藏和显示
                if (webView.getUrl().contains("a=usersignedit")) {
                    mls_fl.setVisibility(View.VISIBLE);
                } else {
                    mls_fl.setVisibility(View.GONE);
                }
            } else {
                GetUserNameThread thread = new GetUserNameThread();
                thread.start();
                finish();
            }
			/* webView.reload(); */
        }
    }

    class GetUserNameThread extends Thread {
        @Override
        public void run() {
            if (mSportsApp.getSessionId() == null
                    || "".equals(mSportsApp.getSessionId()))
                return;
            try {
                UserDetail detail = ApiJsonParser.refreshRank(mSportsApp
                        .getSessionId());
                if (detail != null) {
                    mSportsApp.setSportUser(detail);
                    Handler mainHandler = SportsApp.getInstance()
                            .getMainHandler();
                    if (mainHandler != null) {
                        mainHandler.sendMessage(mainHandler
                                .obtainMessage(ApiConstant.UPDATE_COINS_NOW));
                    }
                }
            } catch (ApiNetException e) {
                e.printStackTrace();
                SportsApp.eMsg = Message.obtain(mExceptionHandler,
                        SportsExceptionHandler.NET_ERROR);
                SportsApp.eMsg.sendToTarget();
            } catch (ApiSessionOutException e) {
                e.printStackTrace();
                SportsApp.eMsg = Message.obtain(mExceptionHandler,
                        SportsExceptionHandler.SESSION_OUT);
                SportsApp.eMsg.sendToTarget();

            }
        }
    }

    @Override
    public void setViewStatus() {
        // TODO Auto-generated method stub
    }

    @Override
    public void onPageResume() {
        MobclickAgent.onPageStart("YunHuWebViewActivity");
    }

    @Override
    public void onPagePause() {
        MobclickAgent.onPageEnd("YunHuWebViewActivity");
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
        Log.v("webview", "onDestroy");
    }

    /**
     * 按键响应，在WebView中查看网页时，按返回键的时候按浏览历史退回,如果不做此项处理则整个WebView返回退出
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            // 返回键退回
            webView.goBack();
            if (webView.getUrl().contains("a=weixinpay")) {
                mls_fl.setVisibility(View.VISIBLE);
            } else {
                mls_fl.setVisibility(View.GONE);
            }
            if (webView.getUrl().contains("a=usersignedit")) {
                mls_fl.setVisibility(View.VISIBLE);
            } else {
                mls_fl.setVisibility(View.GONE);
            }
//            mls_fl.setVisibility(View.GONE);
            if (returnFlg == 2) {
                finish();
            }
            Log.i("webView.getUrl()", "web的当前网址：" + webView.getUrl());
            return true;
        }
        Log.i("webView.getUrl()", "web处理后网址：" + webView.getUrl());
        // If it wasn't the Back key or there's no web page history, bubble up
        // to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
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
                // Log.i("", "### getParasByUrl url:" + url);
                view.loadUrl(url);
                // 分割字符串
//                int pp = url.indexOf("&");
//                if (pp > 0) {
//                    String array[] = url.split("&");
//                    if (array.length > 0) {
//                        texturi = array[1];
//                    }
                if (url.contains("a=usersign")) {
                    mls_fl.setVisibility(View.GONE);
                }
                if(url.contains("a=usersignedit")){
                    mls_fl.setVisibility(View.GONE);
                }
                if (url.contains("a=weixinpay")) {
                    mls_fl.setVisibility(View.VISIBLE);
                }
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
                    lodingLayout.setVisibility(View.GONE);
            }
        });

        webView.loadUrl(uus);
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

    class RightClickLiser implements OnClickListener {

        @Override
        public void onClick(View view) {
            // TODO Auto-generated method stub
            startActivity(new Intent(NightRunWebViewActivity.this,
                    CoolCurrencyRulesActivity.class));
        }

    }

    private String toXml(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (int i = 0; i < params.size(); i++) {
            sb.append("<" + params.get(i).getName() + ">");

            sb.append(params.get(i).getValue());
            sb.append("</" + params.get(i).getName() + ">");
        }
        sb.append("</xml>");
        Log.e("orion", sb.toString());
        return sb.toString();
    }

    StringBuffer xml = new StringBuffer();

    private String genProductArgs() {

        try {
            xml.append("</xml>");
            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
            packageParams
                    .add(new BasicNameValuePair("appid", Constants.APP_ID));
            packageParams.add(new BasicNameValuePair("body", yp.getMls_name()));
            packageParams
                    .add(new BasicNameValuePair("mch_id", Constants.MCH_ID));
            packageParams.add(new BasicNameValuePair("nonce_str", sjzf));
            packageParams.add(new BasicNameValuePair("notify_url", wxurl));
            packageParams.add(new BasicNameValuePair("out_trade_no", yp
                    .getOrder_nu()));
            packageParams.add(new BasicNameValuePair("spbill_create_ip", ip));
            packageParams.add(new BasicNameValuePair("total_fee", yp.getMls_price() + ""));
            packageParams.add(new BasicNameValuePair("trade_type", type));
            packageParams.add(new BasicNameValuePair("sign", wb.getSign()));
            String xx = toXml(packageParams);
            // 改变拼接之后xml字符串格式
            Log.e("Data", xx + "交费金额:" + exActivi.getPrice() + "");
            return new String(xx.toString().getBytes(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

    }

    public Map<String, String> decodeXml(String content) {

        try {
            Map<String, String> xml = new HashMap<String, String>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {

                String nodeName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:

                        if ("xml".equals(nodeName) == false) {
                            // 实例化student对象
                            xml.put(nodeName, parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }

            return xml;
        } catch (Exception e) {
            Log.e("orion", e.toString());
        }
        return null;

    }

    private void waitShowDialog() {
        // TODO Auto-generated method stub
        if (mLoadProgressDialog == null) {
            mLoadProgressDialog = new Dialog(this, R.style.sports_dialog);
            LayoutInflater mInflater = this.getLayoutInflater();
            View v1 = mInflater.inflate(R.layout.bestgirl_progressdialog, null);
            TextView mDialogMessage = (TextView) v1.findViewById(R.id.message);
            mDialogMessage.setText(R.string.PayInfo);
            v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
            mLoadProgressDialog.setCancelable(false);
            mLoadProgressDialog.setContentView(v1);
        }
        if (mLoadProgressDialog != null)
            if (!mLoadProgressDialog.isShowing() && !this.isFinishing())
                mLoadProgressDialog.show();
        Log.i(TAG, "isFirstshow----");
    }

    // 得到随机字符
    private String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
                .getBytes());
    }

    private void genPayReq() {
        req.appId = wbpay.getAppid();
        req.partnerId = wbpay.getPartnerid();
        req.prepayId = wbpay.getPrepayid();
        req.packageValue = wbpay.getPackageVaue();
        req.nonceStr = wbpay.getNoncestr();
        req.timeStamp = wbpay.getTimestamp();
        req.sign = wbpay.getSign();

        sendPayReq();

    }

    private void sendPayReq() {
        msgApi = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        msgApi.registerApp(Constants.APP_ID);
        msgApi.sendReq(req);
        // Toast.makeText(NightRunWebViewActivity.this, "获取支付信息中,请稍等...",
        // Toast.LENGTH_SHORT);

    }

    // 得到本机ip地址
    public String getLocalHostIp() {
        String ipaddress = "";
        try {
            Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces();
            // 遍历所用的网络接口
            while (en.hasMoreElements()) {
                NetworkInterface nif = en.nextElement();// 得到每一个网络接口绑定的所有ip
                Enumeration<InetAddress> inet = nif.getInetAddresses();
                // 遍历每一个接口绑定的所有ip
                while (inet.hasMoreElements()) {
                    InetAddress ip = inet.nextElement();
                    if (!ip.isLoopbackAddress()
                            && InetAddressUtils.isIPv4Address(ip
                            .getHostAddress())) {
                        return ipaddress = ip.getHostAddress();
                    }
                }

            }
        } catch (SocketException e) {
            Log.e("feige", "获取本地ip地址失败");
            e.printStackTrace();
        }
        return ipaddress;

    }

}