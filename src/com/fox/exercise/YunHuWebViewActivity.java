package com.fox.exercise;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fox.exercise.api.ApiConstant;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.api.entity.UserDetail;
import com.fox.exercise.newversion.entity.ShopPayDingDan;
import com.fox.exercise.newversion.entity.WXPaylist;
import com.fox.exercise.newversion.entity.WXxdback;
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

public class YunHuWebViewActivity extends AbstractBaseActivity {
    private static final String TAG = "YunHuWebViewActivity";
    private static final String URL = ApiConstant.URL
            + "/Beauty/kupao.php?m=Webapp&a=url_jump&id=3&session_id=";
    private static final String APP_CACAHE_DIRNAME = "/webcache";

    private WebView webView;
    private LinearLayout lodingLayout;
    private SportsExceptionHandler mExceptionHandler = null;
    private RelativeLayout pay_rl;
    private TextView money_rule, pay_text;
    private ImageView backimg;
    private static final int IVEW_ID = 99;
    public static int biaoshi = 2;
    private Button pay_btn;
    int id1, mymoney;
    private WXxdback wb = null, wbpay = null;
    private WXPaylist list = null, Dlist = null;
    IWXAPI msgApi;
    PayReq req;
    ShopPayDingDan sd;// 支付订单model
    private String text = "", id, sjzf, name, dingdan, ip, type = "APP",
            entity, did;
    public static String sessionid, Numid;
    private String wxurl = ApiConstant.URL + "/Beauty/notify_url.php";
    private String qiandaourl = ApiConstant.DATA_URL + ApiConstant.Qiandao;
    private Dialog mLoadProgressDialog = null;
    private int returnFlg;
    int create_time, endtime;
    String uus;
    private int web_id = -1;

    @Override
    public void initIntentParam(Intent intent) {
        // TODO Auto-generated method stub
        title = getResources().getString(R.string.sports_coolmall);
        if (intent != null) {
            web_id = intent.getIntExtra("web_id", 0);
        }
    }

    class imgBackclicklister implements OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            webView.loadUrl(URL + ((SportsApp) getApplication()).getSessionId());
        }

    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        showContentView(R.layout.yunhu_webview_shop);
        mSportsApp = (SportsApp) getApplication();
        mExceptionHandler = mSportsApp.getmExceptionHandler();
        lodingLayout = (LinearLayout) findViewById(R.id.loading_layout);
        lodingLayout.setVisibility(View.VISIBLE);
//        Log.d(TAG, "WebViewActivity inited");
        webView = (WebView) findViewById(R.id.web);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        loadWebView();
        leftButton.setOnClickListener(new clickListener());
        left_ayout.setOnClickListener(new clickListener());
        pay_rl = (RelativeLayout) findViewById(R.id.re_payview);
        pay_btn = (Button) findViewById(R.id.pay_btn);
        pay_text = (TextView) findViewById(R.id.pay_text);
        pay_btn.setOnClickListener(new Payclicklister());
        req = new PayReq();
        sjzf = genNonceStr();
        ip = getLocalHostIp();
        sessionid = mSportsApp.getSessionId();
        msgApi = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        msgApi.registerApp(Constants.APP_ID);
        money_rule = new TextView(this);
        money_rule.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        money_rule.setId(+IVEW_ID);
        money_rule.setOnClickListener(new RightClickLiser());
        money_rule.setText(getResources().getString(R.string.qiandao));
        money_rule.setTextColor(getResources().getColor(R.color.white));
        money_rule.setTextSize(14);
        money_rule.setGravity(Gravity.CENTER);
        showRightBtn(money_rule);
        right_btn.setPadding(0, 0, mSportsApp.dip2px(17), 0);
        backimg = new ImageView(this);
        backimg.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        backimg.setImageResource(R.drawable.back_home);
        showBaxkbtn(backimg);
        mSportsApp.setmShopHandler(mHandler);// 设置金币商城的handler
        img_backhome.setOnClickListener(new imgBackclicklister());
    }

    class clickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (webView.canGoBack()) {
                webView.goBack(); // 后退
                pay_rl.setVisibility(View.GONE);
                if (webView.getUrl().equals(
                        ApiConstant.DATA_URL + "m=Webshopnew&a=goods_list")) {
                    finish();
                }

                if (webView.getUrl().contains("m=Webshopnew&a=useraddress")) {
                    if (webView.getUrl().contains("user_name")) {

                        if (list.getGoods_xjprice() <= 0) {
                            pay_rl.setVisibility(View.GONE);
                        } else {
                            pay_rl.setVisibility(View.VISIBLE);
                        }
                    } else if (webView.getUrl().contains("orderid")) {
                        if (list.getGoods_xjprice() <= 0) {
                            pay_rl.setVisibility(View.GONE);
                        } else {
                            pay_rl.setVisibility(View.VISIBLE);
                        }
                    } else {
                        pay_rl.setVisibility(View.GONE);
                    }

                }
            } else {
                GetUserNameThread thread = new GetUserNameThread();
                thread.start();
                finish();
            }
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
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (mLoadProgressDialog != null) {
            mLoadProgressDialog.dismiss();
        }
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
        webView.setVisibility(View.GONE);
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
            if (mLoadProgressDialog != null && mLoadProgressDialog.isShowing()) {
                mLoadProgressDialog.dismiss();
            }
            webView.goBack();
            pay_rl.setVisibility(View.GONE);
//            Log.i("webView.getUrl()", "web的当前网址：" + webView.getUrl());
            if (webView.getUrl().equals(
                    ApiConstant.DATA_URL + "m=Lottery&a=prize")) {
                webView.loadUrl(ApiConstant.DATA_URL
                        + "m=Webapp&a=url_jump&id=3&session_id=" + sessionid);
            }
            if (webView.getUrl().equals(
                    ApiConstant.DATA_URL + "m=Lottery&a=prize_list")) {
                webView.loadUrl(ApiConstant.DATA_URL + "m=Lottery&a=prize");
            }
            if (webView.getUrl().equals(
                    ApiConstant.DATA_URL + "m=Webshopnew&a=goods_list")) {
                finish();
            }
            if (webView.getUrl().contains("m=Webshopnew&a=useraddress")) {
                if (webView.getUrl().contains("user_name")) {

                    if (list.getGoods_xjprice() <= 0) {
                        pay_rl.setVisibility(View.GONE);
                    } else {
                        pay_rl.setVisibility(View.VISIBLE);
                    }
                } else if (webView.getUrl().contains("orderid")) {
                    if (list.getGoods_xjprice() <= 0) {
                        pay_rl.setVisibility(View.GONE);
                    } else {
                        pay_rl.setVisibility(View.VISIBLE);
                    }
                } else {
                    pay_rl.setVisibility(View.GONE);
                }

            }
            if (returnFlg == 2) {
                finish();
            }
            return true;
        }
//        Log.i("webView.getUrl()", "web处理后网址：" + webView.getUrl());
        return super.onKeyDown(keyCode, event);
    }

    private void loadWebView() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);// 设置支持javascript脚本
        webSettings.setRenderPriority(RenderPriority.HIGH);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT); // 设置 缓存模式
        // 开启 DOM storage API 功能
        webSettings.setDomStorageEnabled(true);
        // 开启 database storage API 功能
        webSettings.setDatabaseEnabled(true);
        String cacheDirPath = getFilesDir().getAbsolutePath()
                + APP_CACAHE_DIRNAME;
//        Log.i(TAG, "cacheDirPath=" + cacheDirPath);
        // 设置数据库缓存路径
        webSettings.setDatabasePath(cacheDirPath);
        // 设置 Application Caches 缓存目录
        webSettings.setAppCachePath(cacheDirPath);
        // 开启 Application Caches 功能
        webSettings.setAppCacheEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                Log.i("URL", "### getParasByUrl url:" + url);
                String[] uu = url.split(":");
                String content = uu[0];
                if (!url.contains(ApiConstant.DATA_URL)) {

                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } else if (url.contains(ApiConstant.DATA_URL)) {

                    if (content.equals("http")) {
                        view.loadUrl(url);
                        int pp = url.indexOf("&");
                        if (pp > 0) {
                            String array[] = url.split("&");
                            if (array.length > 0) {
                                text = array[1];
                            }

                            if (url.contains("m=Webshopnew&a=order_info")) {
                                id = array[2];
                                String[] str = id.split("=");
                                did = str[1];
//                                Log.d("TAGid", "id是:" + id);
                            }
                            if (text.equals("a=goods_info")) {
                                id = array[2];
                                String[] str = id.split("=");
                                id = str[1];
//                                Log.d("TAGid", "id是:" + id);
                                getListData(1);
                            }
                            if (text.equals("a=order_info")) {
                                getListData(2);

                            }
                            if (text.equals("a=create_order")) {

                                if (list.getGoods_xjprice() <= 0) {
                                    pay_rl.setVisibility(View.GONE);
                                } else {
                                    pay_rl.setVisibility(View.VISIBLE);
                                }

                            }
                            if (url.contains(
                                    "m=Webshopnew&a=useraddress")) {
                                pay_rl.setVisibility(View.GONE);
                            }

                        }
                    } else if (content.equals("https")) {
                        Uri uri = Uri.parse(url);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }

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
        if (web_id == -1) {
            webView.loadUrl(URL + ((SportsApp) getApplication()).getSessionId());
        } else {
            webView.loadUrl(URL + ((SportsApp) getApplication()).getSessionId() + "&gid=" + web_id);
        }
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

        File webviewCacheDir = new File(getCacheDir().getAbsolutePath()
                + "/webviewCache");

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
            webView.loadUrl(qiandaourl);

        }

    }

    /**
     * 支付按钮单击事件
     */
    class Payclicklister implements OnClickListener {

        @Override
        public void onClick(View v) {

            // TODO Auto-generated method stub
            waitShowDialog();
            GetData(1);
        }
    }

    /**
     * 订单记录的支付
     */
    class TwoPayclicklister implements OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            waitShowDialog();
            GetData(2);
        }

    }

    public void getListData(int bs) {
        if (mSportsApp.isOpenNetwork()) {
            // bs等于1的时候是商品详情页面
            if (bs == 1) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        try {
                            // 获取商品信息
                            list = ApiJsonParser.getGoodsinfo(
                                    mSportsApp.getSessionId(), Integer.parseInt(id));
                            mHandler.sendEmptyMessage(9);
                        } catch (ApiNetException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (ApiSessionOutException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }).start();
            } else if (bs == 2) {
                // bs等于2的时候是订单页面
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        try {
                            // 2是标识代表获取金币商城订单信息
                            sd = ApiJsonParser.getShopDingDanInfo(
                                    mSportsApp.getSessionId(), did, 2);
                            endtime = ApiJsonParser.getservertime();
                            mHandler.sendEmptyMessage(10);
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
        } else {
            Toast.makeText(
                    YunHuWebViewActivity.this,
                    getResources().getString(
                            R.string.error_cannot_access_net),
                    Toast.LENGTH_SHORT).show();
        }

    }

    // 获取订单数据
    public void GetData(int tt) {
        // tt1/2 1表示商品直接支付,2表示商品订单记录支付
        if (mSportsApp.isOpenNetwork()) {
            dingdan = "";
            if (tt == 1) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub

                        try {
                            Dlist = ApiJsonParser.getGoodspayinfo(sessionid,
                                    Integer.parseInt(id), 2);
                            name = Dlist.getGoods_name();
                            dingdan = Dlist.getOrder_nu();
                            mymoney = Dlist.getGoods_xjprice();
                            wb = ApiJsonParser.gettongyiInfo(Constants.APP_ID,
                                    name, Constants.MCH_ID, sjzf, wxurl, dingdan,
                                    ip, mymoney, type);
                            Numid = Dlist.getOrder_nu();
                            // 预支付id
                            entity = genProductArgs();
                            wbpay = ApiJsonParser.getPayid(entity, 2);
                            mHandler.sendEmptyMessage(200);
                        } catch (ApiNetException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (NumberFormatException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (ApiSessionOutException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }).start();
            } else if (tt == 2) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        try {
                            name = sd.getGoods_name();
                            dingdan = sd.getOrder_nu();
                            mymoney = sd.getGoods_xjprice();
                            wb = ApiJsonParser.gettongyiInfo(Constants.APP_ID,
                                    name, Constants.MCH_ID, sjzf, wxurl, dingdan,
                                    ip, mymoney, type);
                            Numid = sd.getOrder_nu();
                            // 预支付id
                            entity = genProductArgs();
                            wbpay = ApiJsonParser.getPayid(entity, 2);
                            mHandler.sendEmptyMessage(200);
                        } catch (ApiNetException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        } else {
            Toast.makeText(
                    YunHuWebViewActivity.this,
                    getResources().getString(
                            R.string.error_cannot_access_net),
                    Toast.LENGTH_SHORT).show();
        }

    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 200:
                    genPayReq();
                    break;
                case 9:
                    if (list.getGoods_xjprice() <= 0) {
                        pay_rl.setVisibility(View.GONE);
                    }
                    pay_text.setText((double) list.getGoods_xjprice() / 100 + "元 +"
                            + list.getGoods_price() + "金币");
                    break;
                case 10:
                    if (sd.getCreate_time() != "") {
                        create_time = Integer.parseInt(sd.getCreate_time());
                        create_time = create_time + 48 * 3600;
                        if (endtime > create_time || sd.getPay_status() == 2) {
                            pay_rl.setVisibility(View.GONE);
                        } else {
                            if (sd.getGoods_xjprice() <= 0) {
                                pay_rl.setVisibility(View.GONE);
                            } else if (sd.getPay_status() == 1) {
                                pay_rl.setVisibility(View.VISIBLE);
                                pay_text.setVisibility(View.GONE);
                                pay_btn.setOnClickListener(new TwoPayclicklister());
                            }
                        }
                    }
                    break;
                case 101:
                    if (mLoadProgressDialog != null
                            && mLoadProgressDialog.isShowing()) {
                        mLoadProgressDialog.dismiss();
                    }
                    break;
                case 102:
                    returnFlg = 2;
                    break;
                case 103:
                    returnFlg = 2;
                    break;

                default:
                    break;
            }
        }

        ;
    };

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
            packageParams.add(new BasicNameValuePair("out_trade_no", dingdan));
            packageParams.add(new BasicNameValuePair("spbill_create_ip", ip));
            packageParams.add(new BasicNameValuePair("total_fee", String
                    .valueOf(mymoney)));
            packageParams.add(new BasicNameValuePair("trade_type", type));
            packageParams.add(new BasicNameValuePair("sign", wb.getSign()));
            String xx = toXml(packageParams);
            // 改变拼接之后xml字符串格式
            return new String(xx.toString().getBytes(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }


    }


    private void genPayReq() {
        if (!"".equals(wbpay.getAppid()) && !"".equals(wbpay.getPartnerid()) && !"".equals(wbpay.getPrepayid()) && !"".equals(wbpay.getPackageVaue())
                && !"".equals(wbpay.getNoncestr()) && !"".equals(wbpay.getTimestamp()) && !"".equals(wbpay.getSign())) {

            req.appId = wbpay.getAppid();
            req.partnerId = wbpay.getPartnerid();
            req.prepayId = wbpay.getPrepayid();
            req.packageValue = wbpay.getPackageVaue();
            req.nonceStr = wbpay.getNoncestr();
            req.timeStamp = wbpay.getTimestamp();
            req.sign = wbpay.getSign();
            sendPayReq();
        } else {
            Toast.makeText(YunHuWebViewActivity.this, "亲 信息有误哦,检查一下吧!", Toast.LENGTH_SHORT).show();
        }

    }

    private void sendPayReq() {
        msgApi = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        msgApi.registerApp(Constants.APP_ID);
        msgApi.sendReq(req);
    }

    // 得到随机字符
    private String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
                .getBytes());
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
            e.printStackTrace();
        }
        return ipaddress;

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
    }

}