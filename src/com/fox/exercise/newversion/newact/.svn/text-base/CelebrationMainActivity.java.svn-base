package com.fox.exercise.newversion.newact;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.ingenic.indroidsync.SportsApp;

import com.fox.exercise.AbstractBaseActivity;
import com.fox.exercise.ImageDownloader;
import com.fox.exercise.R;
import com.fox.exercise.api.ApiBack;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.api.entity.AddFindItem;
import com.fox.exercise.publish.SendMsgDetail;
import com.fox.exercise.util.ImageFileUtil;
import com.fox.exercise.wxapi.WXEntryActivity;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.RenderPriority;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author loujungang 系统消息活动页面
 */
public class CelebrationMainActivity extends AbstractBaseActivity {

    private String APP_CACAHE_DIRNAME = "/webcache";

    private WebView webView;
    private LinearLayout lodingLayout;
    //	private SportsExceptionHandler mExceptionHandler = null;
    private static final int IVEW_ID = 99;
    private String Url, shareurl;

    private ImageView share_icon;
    private PopupWindow mSportWindow = null;
    private IWXAPI api;
    public final int GETIMAGE_SUCCESS = 1000;
    public final int DLFINISH = 1001;
    public final int DLFAIL = 1002;
    private SharedPreferences spf;
    private ImageFileUtil imageFileUtil;
    private String imagename;

    private ImageDownloader mDownloader = null;

    @Override
    public void initIntentParam(Intent intent) {
        // TODO Auto-generated method stub
        title = "年终盘点";
        mSportsApp = (SportsApp) getApplication();
        if (intent != null) {
            Url = intent.getStringExtra("Url") + mSportsApp.getSportUser().getUid();
            shareurl = intent.getStringExtra("shareurl") + mSportsApp.getSportUser().getUid();
        }
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        showContentView(R.layout.activity_celebration_main);
//		mExceptionHandler = mSportsApp.getmExceptionHandler();
        lodingLayout = (LinearLayout) findViewById(R.id.loading_layout);
        lodingLayout.setVisibility(View.VISIBLE);
        Log.d(TAG, "WebViewActivity inited");
        webView = (WebView) findViewById(R.id.web);
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        loadWebView();
        String rootStr = "Android/data/" + getPackageName()
                + "/cache/.download/img";
        imageFileUtil = new ImageFileUtil(this, rootStr, mHandler);
        Date startDate = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        imagename = formatter.format(startDate) + "conclusion.jpg";
        if (imageFileUtil.isFileExist(imagename)) {
            lodingLayout.setVisibility(View.GONE);
        } else {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    try {
                        ApiBack panDianGetImage = ApiJsonParser
                                .panDianGetImage(mSportsApp.getSportUser()
                                        .getUid() + "");
                        Message msg = new Message();
                        msg.what = GETIMAGE_SUCCESS;
                        msg.obj = panDianGetImage;
                        mHandler.sendMessage(msg);
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
        share_icon = new ImageView(this);
        share_icon.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        share_icon.setId(IVEW_ID);
        share_icon.setBackgroundResource(R.drawable.sport_detail_share_icon);
        share_icon.setOnClickListener(new RightClickLiser());
        showRightBtn(share_icon);
        right_btn.setPadding(0, 0, mSportsApp.dip2px(17), 0);
        top_title_layout.setBackgroundResource(R.color.celebration_color);
        mDownloader = new ImageDownloader(this);
    }

    @Override
    public void setViewStatus() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageResume() {
        // TODO Auto-generated method stub
        MobclickAgent.onPageStart("CelebrationMainActivity");
    }

    @Override
    public void onPagePause() {
        // TODO Auto-generated method stub
        MobclickAgent.onPageEnd("CelebrationMainActivity");
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
        mSportsApp = null;
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case DLFINISH:
                    String path = (String) msg.obj;
                    final ArrayList<String> list_bitmap_path_upload = new ArrayList<String>();
                    list_bitmap_path_upload.add(path);
                    spf = getSharedPreferences("sports", 0);

                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                AddFindItem back = ApiJsonParser.addFind(
                                        mSportsApp.getSessionId(), "新年新征程  为运动喝彩！",
                                        spf.getString("cityname", ""),
                                        list_bitmap_path_upload, "", "", "", "");

                                Message msg = new Message();
                                msg.what = 20141112;
                                msg.obj = back;
                                mHandler.sendMessage(msg);
                            } catch (ApiNetException e) {
                                Message msg = new Message();
                                msg.what = 20141113;
                                msg.obj = e.exceMsg();
                                mHandler.sendMessage(msg);
                            }
                        }
                    }).start();

                    break;
                case DLFAIL:
                    lodingLayout.setVisibility(View.GONE);
                    break;
                case 20141112:
                    lodingLayout.setVisibility(View.GONE);

                    AddFindItem back = (AddFindItem) msg.obj;
                    if (back != null) {
                        // && back.bigurls.length != 0
                        if (mSportsApp.getFindHandler() != null) {
                            SendMsgDetail mSendMsgDetail = new SendMsgDetail();
                            mSendMsgDetail.setMethod_str("新年新征程  为运动喝彩！");
                            mSendMsgDetail.setTimes(System.currentTimeMillis());
                            mSendMsgDetail.setFindId(back.findId);
                            mSendMsgDetail.setUrls(back.urls);
                            mSendMsgDetail.setBigurls(back.bigurls);
                            if (back.urls != null) {
                                if (back.urls.length == 1) {
                                    mSendMsgDetail.setWidth(back.width);
                                    mSendMsgDetail.setHeight(back.height);
                                }

                            }
                            mSendMsgDetail.setComeFrom(spf
                                    .getString("cityname", ""));
                            mSportsApp.setmSendMsgDetail(mSendMsgDetail);
                        }

                    }
                    break;
                case 20141113:
                    lodingLayout.setVisibility(View.GONE);
                    break;
                case GETIMAGE_SUCCESS:
                    final ApiBack apiBack = (ApiBack) msg.obj;
                    if (apiBack != null) {
                        if (apiBack.getMsg() != null
                                && !"".equals(apiBack.getMsg())) {

                            new Thread(new Runnable() {

                                @Override
                                public void run() {
                                    // TODO Auto-generated method stub
                                    try {
                                        imageFileUtil.writeToSDFromNet(
                                                apiBack.getMsg(), imagename);
                                    } catch (IOException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                }
                            }).start();

                        } else {
                            lodingLayout.setVisibility(View.GONE);
                        }

                    } else {
                        lodingLayout.setVisibility(View.GONE);
                    }
                    break;
            }
        }

        ;
    };

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
        webSettings.setUseWideViewPort(true);
        webView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i("", "### getParasByUrl url:" + url);
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
                // if (progress == 100)
                // lodingLayout.setVisibility(View.GONE);
            }
        });

        webView.loadUrl(Url);
    }

    class clickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (webView.canGoBack()) {
                webView.goBack(); // 后退
            }
        }
    }

    class RightClickLiser implements OnClickListener {

        @Override
        public void onClick(View view) {
            // TODO Auto-generated method stub
            showSportWindow();
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
     * 按键响应，在WebView中查看网页时，按返回键的时候按浏览历史退回,如果不做此项处理则整个WebView返回退出
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            // 返回键退回
            webView.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up
        // to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }

    private void showSportWindow() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int popWith = width / 2;
        if (mSportWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.pop_sport_listitem,
                    null);
            ImageView sport_type_icon = (ImageView) view
                    .findViewById(R.id.sport_type_icon);
            sport_type_icon
                    .setBackgroundResource(R.drawable.fenxiang_weixin_friends);
            TextView sport_type_txt = (TextView) view
                    .findViewById(R.id.sport_type_txt);
            sport_type_txt.setText(getResources().getString(
                    R.string.toshare_friends));
            view.setBackgroundResource(R.color.celebration_popbg_color);
            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    mSportWindow.dismiss();
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            shareToWeixin(1);
                        }
                    }).start();

                }
            });
            mSportWindow = new PopupWindow(view, popWith,
                    LayoutParams.WRAP_CONTENT);
            mSportWindow.setTouchable(true);
            mSportWindow.setOutsideTouchable(true);
            mSportWindow.update();
            mSportWindow.setTouchInterceptor(new OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    Log.i("mengdd", "onTouch : ");

                    return false;
                    // 这里如果返回true的话，touch事件将被拦截
                    // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                }
            });
        }
        // mSportWindow.setFocusable(true);
        // mSportWindow.getContentView().setOnTouchListener(new
        // OnTouchListener() {
        // @Override
        // public boolean onTouch(View v, MotionEvent event) {
        // // TODO Auto-generated method stub
        // mSportWindow.setFocusable(false);
        // mSportWindow.dismiss();
        // return true;
        // }
        //
        // });
        mSportWindow.setBackgroundDrawable(new BitmapDrawable());
        // mSportWindow.showAtLocation(share_icon, Gravity.TOP, 0, 0);
        mSportWindow.showAsDropDown(top_title_layout, popWith, 0);

    }

    // 分享到微信里边的内容，其中flag 0是朋友圈，1是好友
    private void shareToWeixin(int flag) {
        if (api == null) {
            api = WXAPIFactory.createWXAPI(this, WXEntryActivity.APP_ID, true);
            api.registerApp(WXEntryActivity.APP_ID);
        }
        if (!api.isWXAppInstalled()) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.sports_shareto_weixin_no_weixin),
                    Toast.LENGTH_SHORT).show();
            return;
        } else if (api.getWXAppSupportAPI() < 0x21020001) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.sports_shareto_weixin_wrong_version),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        share2weixin(flag);
        // new saveDataToServer().execute();
    }

    private void share2weixin(int flag) {
        if (api == null) {
            api = WXAPIFactory.createWXAPI(this, WXEntryActivity.APP_ID, true);
            api.registerApp(WXEntryActivity.APP_ID);
        }
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = shareurl;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        // 自己
        msg.title = "这是我在云狐运动的战果，小伙伴们，速来围观";// title
        msg.description = "";
        // Bitmap thumb = getBitmap(findGroup.getImgs()[0]);
        // BitmapDrawable draw = (BitmapDrawable) getResources().getDrawable(
        // R.drawable.indexpage_sport_step_icon);
        // Bitmap m = draw.getBitmap();
        // if (back.urls != null) {
        // msg.setThumbImage(mDownloader.downloadBitmap(back.urls[0]));
        // }

        if (SportsApp.getInstance().getSportUser().getUimg() != null
                && !"".equals(SportsApp.getInstance().getSportUser().getUimg())) {
            msg.setThumbImage(mDownloader.downloadBitmap(SportsApp
                    .getInstance().getSportUser().getUimg()));

        } else {
            if (("man").equals(SportsApp.getInstance().getSportUser().getSex())) {
                msg.setThumbImage(BitmapFactory.decodeResource(getResources(),
                        R.drawable.sports_user_edit_portrait_male));
            } else {
                msg.setThumbImage(BitmapFactory.decodeResource(getResources(),
                        R.drawable.sports_user_edit_portrait));
            }
        }

        // if (imageFileUtil.isFileExist(imagename)) {
        // try {
        // msg.setThumbImage(imageFileUtil.getImg(imagename));
        // } catch (IOException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // } else {
        //
        // }

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag;
        api.sendReq(req);
        Date startDate = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SharedPreferences sp = getSharedPreferences("CelebrationSp", 0);
        Editor edit = sp.edit();
        edit.putString("weixindatacount", formatter.format(startDate));
        edit.commit();
    }
}
