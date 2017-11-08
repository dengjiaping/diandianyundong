package com.fox.exercise;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fox.exercise.api.ApiConstant;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
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

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import cn.ingenic.indroidsync.SportsApp;

/**
 * Created by zhonghuibin on 2016/8/1. 个人主页我的赛事
 */
public class SportMeWebView extends AbstractBaseActivity {
    private static final String TAG = "YunHuWebViewActivity";
    private String APP_CACAHE_DIRNAME = "/webcache";

    private String webUrl, uus;//页面名字的标识
    private WebView webView;
    private int index;
    private LinearLayout mls_fl, lodingLayout;
    private Button mls_btnpay;
    private YePaoInfo yp;// 夜跑对象
    TextView text_pay;
    IWXAPI msgApi;
    PayReq req;
    private Dialog mLoadProgressDialog = null;
    private WXxdback wb = null, wbpay = null;
    private String ip, entity, type = "APP", sjzf, text, id, name;
    public static String sessionid, numid;
    private int returnFlg, price;
    private SportsExceptionHandler mExceptionHandler = null;
    public static final int biaoshi=3;
    String wxurl = ApiConstant.URL + "/Beauty/yepaonotify_url.php";
    String souyeurl = ApiConstant.DATA_URL + "m=Webactivity&a=introduce&id=";

    @Override
    public void initIntentParam(Intent intent) {
        // TODO Auto-generated method stub
//        title = getResources().getString(R.string.class_introduce);
        if (intent != null) {
            webUrl = intent.getStringExtra("webUrl");
            index = intent.getIntExtra("index", 0);
            switch (index) {
                case 1:
                    title = "我的赛事";
                    break;
                case 2:
                    title = "我的订单";
                    break;
                case 3:
                    title = "微信好友排行";
                    break;
                case 4:
                    title = "计步问题";
                    break;
                case 5:
                    title = "QQ健康";
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        showContentView(R.layout.tran_webview);
        mSportsApp = (SportsApp) getApplication();
        webView = (WebView) findViewById(R.id.web_train);
        text_pay = (TextView) findViewById(R.id.pay_text);
        lodingLayout = (LinearLayout) findViewById(R.id.loading_layout);
        lodingLayout.setVisibility(View.GONE);
        mls_fl = (LinearLayout) findViewById(R.id.mls_pay);
        mls_btnpay = (Button) findViewById(R.id.mls_pay_btn);
        mls_btnpay.setOnClickListener(new payListener());
        mExceptionHandler = mSportsApp.getmExceptionHandler();
        req = new PayReq();
        sjzf = genNonceStr();
        ip = getLocalHostIp();
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        loadWebView();
        mSportsApp.setmYePaoHandler(mHandler);// 设置夜跑的handler
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
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            //左上角返回图标支付按钮隐藏和显示
            if (webView.getUrl().contains("a=usersignedit")) {
                mls_fl.setVisibility(View.VISIBLE);
            } else {
                mls_fl.setVisibility(View.GONE);
            }
            if (returnFlg == 2) {
                finish();
            }
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
                if (url.contains("m=Webactivity&a=weixinpay")) {
                    int pp = url.indexOf("&");
                    if (pp > 0) {
                        String array[] = url.split("&");
                        if (array.length > 0) {
                            text = array[2];
                        }

                        mls_fl.setVisibility(View.VISIBLE);
                        String did = array[2];
                        String[] str = did.split("=");
                        id = str[1];

                    }
                    getData();
                }
                if (url.contains("a=usersignedit")) {
                    mls_fl.setVisibility(View.GONE);
                }
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
                    lodingLayout.setVisibility(View.GONE);
            }
        });
        webView.loadUrl(webUrl);
    }


    // 支付按钮单击事件
    class payListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            waitShowDialog();
            Data();
        }
    }

    public void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    yp = ApiJsonParser.getYePaoInfo(mSportsApp.getSessionId(),
                            Integer.parseInt(id), 1);
                    mHandler.sendEmptyMessage(101);
                } catch (ApiNetException e) {
                    e.printStackTrace();
                } catch (ApiSessionOutException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void Data() {
        numid = "";
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    if (yp != null) {
                        numid = yp.getOrder_nu();
                        price = yp.getMls_price();
                        name = yp.getMls_name();
                        wb = ApiJsonParser.gettongyiInfo(Constants.APP_ID,
                                name, Constants.MCH_ID, sjzf, wxurl,
                                numid, ip, price, type);
                        // 预支付id
                        entity = genProductArgs();
                        wbpay = ApiJsonParser.getPayid(entity, 1);
                        sessionid = mSportsApp.getSessionId();
                        mHandler.sendEmptyMessage(200);
                    } else {
                        mHandler.sendEmptyMessage(1111);
                    }
                } catch (ApiNetException e) {
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
                    webView.loadUrl(souyeurl + id);
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
                    webView.loadUrl(souyeurl + id);
                    returnFlg = 2;
                    break;
                case 101:
                    text_pay.setText("合计:¥" + (double) yp.getMls_price() / 100 + "元");
                    break;
                case 111:
                    Toast.makeText(SportMeWebView.this, "支付数据为空", Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        }

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
            packageParams.add(new BasicNameValuePair("body", name));
            packageParams
                    .add(new BasicNameValuePair("mch_id", Constants.MCH_ID));
            packageParams.add(new BasicNameValuePair("nonce_str", sjzf));
            packageParams.add(new BasicNameValuePair("notify_url", wxurl));
            packageParams.add(new BasicNameValuePair("out_trade_no", numid));
            packageParams.add(new BasicNameValuePair("spbill_create_ip", ip));
            packageParams.add(new BasicNameValuePair("total_fee", price + ""));
            packageParams.add(new BasicNameValuePair("trade_type", type));
            packageParams.add(new BasicNameValuePair("sign", wb.getSign()));
            String xx = toXml(packageParams);
            // 改变拼接之后xml字符串格式
            Log.e("Data", xx + "交费金额:" + yp.getMls_price() + "");
            return new String(xx.toString().getBytes(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

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
