package com.fox.exercise.newversion.newact;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fox.exercise.AbstractBaseActivity;
import com.fox.exercise.FindFriendsSendMsg;
import com.fox.exercise.R;
import com.fox.exercise.SportsUtilities;
import com.fox.exercise.api.AddCoinsThread;
import com.fox.exercise.api.ApiConstant;
import com.fox.exercise.login.Tools;
import com.fox.exercise.map.SportTaskDetailActivityGaode;
import com.fox.exercise.publish.Bimp;
import com.fox.exercise.util.SportTaskUtil;
import com.fox.exercise.wxapi.WXEntryActivity;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.umeng.analytics.MobclickAgent;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

import cn.ingenic.indroidsync.SportsApp;

/**
 *  运动勋章详情页面  loujungang
 */
public class SportsHonorActivity extends AbstractBaseActivity implements View.OnClickListener,IUiListener {
    private static final String TAG = "SportsHonorActivity";
    private String APP_CACAHE_DIRNAME = "/webcache";
    private static final String SHARE_PATH = SportsUtilities.DOWNLOAD_SAVE_PATH
            + "shareImage.jpg";

    private String webUrl;
    private WebView webView;
    private LinearLayout loading_layout;
	private static final int LEFT_VIEW_ID = 99;
    private static final int RIGHT_VIEW_ID = 100;
    private ImageButton rightBtn;
    private Dialog shareDialog;
    private Tencent mTencent;
    private IWXAPI api;
    private int fromPage=-1;//表示从哪个页面跳转而来  1表示从运动页面跳转  2表示从运动页面跳转
    private String grouptype;
    private String shareUrl;

    private int taskID = 0;
    private int uid;
    private String startTime,markCode;
    @Override
    public void initIntentParam(Intent intent) {
        // TODO Auto-generated method stub
        if (intent != null) {
            fromPage=intent.getIntExtra("fromPage",-1);
            if(fromPage==1){
                taskID=intent.getIntExtra("taskid", 0);
                uid=intent.getIntExtra("uid", 0);
                startTime=intent.getStringExtra("startTime");
                markCode= intent.getStringExtra("mark_code");
            }
            grouptype=intent.getStringExtra("grouptype");
            webUrl=ApiConstant.DATA_URL+"m=medal&a=Medalinfo&grouptype="+grouptype;//运动完的勋章页面m=medal&a= Medalinfo&grouptype=123456
            shareUrl=ApiConstant.DATA_URL+"m=medal&a=Medalinfoshare&grouptype="+grouptype;//勋章分享m=medal&a= Medalinfoshare&grouptype=123456
        }
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        showContentView(R.layout.sports_hornor_layout);
        top_title_layout.setBackgroundResource(R.color.sports_hornor_color);
        leftButton.setBackgroundResource(R.drawable.sports_hornor_close);
        leftButton.setLayoutParams(new LinearLayout.LayoutParams(SportsApp.dip2px(22), SportsApp.dip2px(22)));
        leftButton.setId(LEFT_VIEW_ID);
        rightBtn = new ImageButton(this);
        rightBtn.setBackgroundResource(R.drawable.sportdetail_share);
        rightBtn.setLayoutParams(new LinearLayout.LayoutParams(SportsApp.dip2px(26), SportsApp.dip2px(27)));
        showRightBtn(rightBtn);
        rightBtn.setId(RIGHT_VIEW_ID);
        mSportsApp = (SportsApp) getApplication();
        webView = (WebView) findViewById(R.id.web);
        loading_layout = (LinearLayout) findViewById(R.id.loading_layout);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        if(grouptype!=null&&!"".equals(grouptype)){
            loadWebView();
        }else{
            rightBtn.setVisibility(View.GONE);
            loading_layout.setVisibility(View.GONE);
        }
        mTencent = Tencent.createInstance("1101732794",
                this.getApplicationContext());
        leftButton.setOnClickListener(this);
        rightBtn.setOnClickListener(this);
    }

    @Override
    public void setViewStatus() {
        // TODO Auto-generated method stub
    }

    @Override
    public void onPageResume() {
        MobclickAgent.onPageStart("SportsHonorActivity");
    }

    @Override
    public void onPagePause() {
        MobclickAgent.onPageEnd("SportsHonorActivity");
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
        }else if((keyCode == KeyEvent.KEYCODE_BACK) && !webView.canGoBack()){
            if(fromPage==1){
                Intent intent = new Intent(SportsHonorActivity.this,
                        SportTaskDetailActivityGaode.class);
                intent.putExtra("taskid", taskID);
                intent.putExtra("uid", uid);
                intent.putExtra("startTime", startTime);
                intent.putExtra("mark_code", markCode);
                startActivity(intent);
                finish();
            }
        }
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
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case LEFT_VIEW_ID:
                if(fromPage==-1){
                    finish();
                }else if(fromPage==1){
                   Intent intent = new Intent(SportsHonorActivity.this,
                            SportTaskDetailActivityGaode.class);
                    intent.putExtra("taskid", taskID);
                    intent.putExtra("uid", uid);
                    intent.putExtra("startTime", startTime);
                    intent.putExtra("mark_code", markCode);
                    startActivity(intent);
                    finish();
                }else if(fromPage==2){
                    finish();
                }
                break;
            case RIGHT_VIEW_ID:
                toShare();
                break;
            case R.id.faxian:
            case R.id.faxian_layout:
                if (shareDialog != null && shareDialog.isShowing()) {
                    shareDialog.dismiss();
                }
                Bitmap wbitmap = shot();
                boolean saveFile = Tools.SaveBitmapAsFile(SHARE_PATH, wbitmap);
                if(wbitmap!=null){
                    wbitmap.recycle();
                    wbitmap=null;
                }
                if (saveFile) {
                    if (Bimp.drr.size() < 9) {
                        Bimp.drr.add(SHARE_PATH);
                    }
                    Intent intent = new Intent(this, FindFriendsSendMsg.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(this,getResources().getString(R.string.toast_bitmap_decode_failed),Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.xinlang:
            case R.id.xinlang_layout:
                //qq分享
                if (shareDialog != null && shareDialog.isShowing()) {
                    shareDialog.dismiss();
                }
                     onClickShare();
                break;
            case R.id.tengxun:
            case R.id.tengxun_layout:
                if (shareDialog != null && shareDialog.isShowing()) {
                    shareDialog.dismiss();
                }
                     ShareWxFriend(1);
                break;
            case R.id.weixin:
            case R.id.weixin_layout:
                if (shareDialog != null && shareDialog.isShowing()) {
                    shareDialog.dismiss();
                }
                      ShareWxFriend(0);
                break;
        }

    }


    //去分享的dialog
    private void toShare() {
        shareDialog = new Dialog(SportsHonorActivity.this, R.style.sports_coins_dialog);
        LayoutInflater inflater1 = getLayoutInflater();
        View view = inflater1.inflate(R.layout.sports_dialog2, null);
        view.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.9));
        view.findViewById(R.id.faxian_layout).setOnClickListener(this);
        view.findViewById(R.id.weixin_layout).setOnClickListener(this);
        view.findViewById(R.id.tengxun_layout).setOnClickListener(this);
        view.findViewById(R.id.xinlang_layout).setOnClickListener(this);
        view.findViewById(R.id.share_cacle_txt).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        shareDialog.dismiss();
                    }
                });
        shareDialog.setContentView(view);
        shareDialog.setCancelable(true);
        shareDialog.setCanceledOnTouchOutside(false);
        shareDialog.show();
    }

    public Bitmap shot() {
        View view = getWindow().getDecorView();
        Display display = this.getWindowManager().getDefaultDisplay();
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusheight = frame.top;// 手机状态栏高度
        view.setDrawingCacheEnabled(true);// 允许当前窗口保存缓存信息，这样getDrawingCache()方法才会返回一个Bitmap
        Bitmap bmp = Bitmap
                .createBitmap(
                        view.getDrawingCache(),
                        0,
                        top_title_layout.getHeight() + statusheight
                                , display.getWidth(),
                        webView.getHeight()-70
                );
        view.destroyDrawingCache();
        return bmp;
    }

    /**
     * 分享到微信
     */
    private void ShareWxFriend(final int flag) {

        if (api == null) {
            api = WXAPIFactory.createWXAPI(getApplication(),
                    WXEntryActivity.APP_ID, true);
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
            new Thread(new Runnable() {
                @Override
                public void run() {
                    share2weixin(flag);
                }
            }).start();


    }

    private void share2weixin(int flag) {
        if (api == null) {
            api = WXAPIFactory.createWXAPI(getApplication(),
                    WXEntryActivity.APP_ID, true);
            api.registerApp(WXEntryActivity.APP_ID);
        }

        String text = getResources().getString(R.string.sports_model_sharedecrtion);
        WXWebpageObject webpage = new WXWebpageObject();
        //分享的链接
        webpage.webpageUrl = shareUrl;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        // 自己
        if (flag == 1) {
            msg.title = getResources().getString(R.string.sports_model_sharetitle);
        } else {
            msg.title = getResources().getString(R.string.sports_model_sharetitle);// 分享的标题
        }
        msg.description = text;//分享的内容
        Bitmap mBitmap=BitmapFactory.decodeResource(getResources(),R.drawable.sports_model_shareicon);
        Tools.SaveBitmapAsFile(SHARE_PATH, mBitmap);
        Bitmap mBitmap1=lessenUriImage(SHARE_PATH);
        Bitmap mBitmap2=compressImage(mBitmap1);
        msg.setThumbImage(mBitmap2);//分享的图片
        if(mBitmap!=null){
            mBitmap.recycle();
            mBitmap=null;
        }
        if(mBitmap1!=null){
            mBitmap1.recycle();
            mBitmap1=null;
        }
        if(mBitmap2!=null){
            mBitmap2.recycle();
            mBitmap2=null;
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag;
        api.sendReq(req);
    }

    /**
     * 图片压缩
     */
    private Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 50;
        while (baos.toByteArray().length / 1024 > 10&&options>5) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 5;// 每次都减少5
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        try{
            if(baos!=null){
                baos.close();
                baos=null;
            }
            if(isBm!=null){
                isBm.close();
                isBm=null;
            }
        }catch (Exception e){

        }
        return bitmap;
    }


    //把本地图片转为bitmap
    private  Bitmap lessenUriImage(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options); //此时返回 bm 为空
        options.inJustDecodeBounds = false; //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = (int) (options.outHeight / (float) 320);
        if (be <= 0)
            be = 1;
        options.inSampleSize = be; //重新读入图片，注意此时已经把 options.inJustDecodeBounds 设回 false 了
        bitmap = BitmapFactory.decodeFile(path, options);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        System.out.println(w + " " + h); //after zoom
        return bitmap;
    }
        //分享到qq
    private void onClickShare() {
        Bitmap mBitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.sports_model_shareicon);
        Tools.SaveBitmapAsFile(SHARE_PATH, mBitmap);
        if(mBitmap!=null){
            mBitmap.recycle();
            mBitmap=null;
        }
            Bundle params = new Bundle();
            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,
                    QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
            params.putString(QQShare.SHARE_TO_QQ_TITLE, getResources().getString(R.string.sports_model_sharetitle));// 要分享的标题
            params.putString(QQShare.SHARE_TO_QQ_SUMMARY,
                    getResources().getString(R.string.sports_model_sharedecrtion));// 要分享的摘要

            params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareUrl);//分享的链接
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, SHARE_PATH);// 分享的图片
//          params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, SHARE_PATH);// 分享的图片
            params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "1101732794");
            params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, 0);
            mTencent.shareToQQ(SportsHonorActivity.this, params, this);

    }

    @Override
    public void onComplete(Object o) {
        Toast.makeText(SportsHonorActivity.this, "QQ分享成功", Toast.LENGTH_SHORT).show();
        new AddCoinsThread(10, 4, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method
                switch (msg.what) {
                    case ApiConstant.COINS_SUCCESS:
                        SportTaskUtil.jump2CoinsDialog(SportsHonorActivity.this,
                                getString(R.string.shared_success_add_coins));
                        break;
                    case ApiConstant.COINS_LIMIT:
                        Toast.makeText(SportsHonorActivity.this,
                                getString(R.string.shared_beyond_10times),
                                Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        }, -1).start();
    }

    @Override
    public void onError(UiError uiError) {
        Toast.makeText(SportsHonorActivity.this, "分享失败", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCancel() {

    }
}