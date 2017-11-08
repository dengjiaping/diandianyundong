package com.fox.exercise.map;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.fox.exercise.R;
import com.fox.exercise.SmartBarUtils;
import com.fox.exercise.SportsUtilities;
import com.fox.exercise.api.Util;
import com.fox.exercise.wxapi.WXEntryActivity;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

public class SportsShared extends SWeiboBaseActivity implements OnClickListener {
    private long mClickTime;
    ImageView displayImage;
    private Context mContext;
    private IWXAPI api;
    private static final int THUMB_SIZE = 150;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean findMethod = SmartBarUtils.findActionBarTabsShowAtBottom();
        if (!findMethod) {
            // 取消ActionBar拆分，换用TabHost
            getWindow().setUiOptions(0);
            getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        setContentView(R.layout.sports_share_image);
        mContext = this;
        displayImage = (ImageView) findViewById(R.id.share_image);
        findViewById(R.id.backButton).setOnClickListener(this);
        findViewById(R.id.shareto_tenxun_button).setOnClickListener(this);
        findViewById(R.id.shareto_xinlang_button).setOnClickListener(this);
//		findViewById(R.id.qq_zone_button).setOnClickListener(this);
        findViewById(R.id.shareto_weixin_button).setOnClickListener(this);
        Bundle bundle = getIntent().getExtras();
        String filePath = "";
        String userName = "";
        String url = "";
        String message = "";
        if (bundle != null) {
            filePath = bundle.containsKey("filePath") ? bundle.getString("filePath") : "";
            Log.e("SportsShared", filePath);
            userName = bundle.containsKey("userName") ? bundle.getString("userName") : "";
            url = bundle.containsKey("url") ? bundle.getString("url") : "";
            message = bundle.containsKey("message") ? bundle.getString("message") : "";
        }
        thisUrl = url;
        thisLarge = filePath;
        Log.d("SportsShared", "thisLarge:" + thisLarge);
        thisMessage = message;
        Bitmap bitmap = SportsUtilities.decodeFile(filePath);
        if (bitmap == null) {
            Log.d("SportsShared", "bitmap:" + (bitmap == null));
            thisLarge = null;
//			displayImage.setImageResource(R.drawable.sports_default_picture);
        }
//		Log.e("SportsShared", bitmap.toString());
        displayImage.setImageBitmap(bitmap);
        if (findMethod) {
            getActionBar().setDisplayShowHomeEnabled(false);
            getActionBar().setDisplayShowTitleEnabled(false);
            SmartBarUtils.setActionModeHeaderHidden(getActionBar(), true);
            SmartBarUtils.setActionBarViewCollapsable(getActionBar(), true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("SportsShared");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("SportsShared");
        MobclickAgent.onPause(this);
    }

    @Override
    public void onClick(View v) {
        long nowTime = System.currentTimeMillis();
        if (mClickTime > nowTime - 1000) {
            return;
        }
        mClickTime = nowTime;
        switch (v.getId()) {
            case R.id.backButton:
                finish();
                return;
            case R.id.shareto_xinlang_button:
                shareToXinlangWeibo();
                return;
            case R.id.shareto_tenxun_button:
                shareToTenxunWeibo();
                return;
//		case R.id.qq_zone_button:
//			// shareToQQzone();
//			shareToQQzoneNEWSDK();
//			return;
            case R.id.shareto_weixin_button:
                shareToWeixin();
                return;
        }
    }

    private void shareToWeixin() {
        api = WXAPIFactory.createWXAPI(SportsShared.this, WXEntryActivity.APP_ID, true);
        api.registerApp(WXEntryActivity.APP_ID);
        if (!api.isWXAppInstalled()) {
            Toast.makeText(getApplicationContext(), "亲，你还没有安装微信呢", Toast.LENGTH_SHORT).show();
            return;
        } else if (api.getWXAppSupportAPI() < 0x21020001) {
            Toast.makeText(getApplicationContext(), "亲，你的微信版本不支持发送朋友圈", Toast.LENGTH_SHORT).show();
            return;
        }

        Bitmap bmp = BitmapFactory.decodeFile(thisLarge);
        WXImageObject imgObj = new WXImageObject(bmp);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true); // 设置缩略图

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        if (!SportsUtilities.checkConnection(SportsShared.this)) {
            Toast.makeText(SportsShared.this, getString(R.string.sports_comment_neterror), Toast.LENGTH_LONG)
                    .show();
        } else {
            api.sendReq(req);
            MobclickAgent.onEvent(mContext, "shareto", "weixin");
        }
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy invoked");
        if (displayImage != null) {
            displayImage.setImageBitmap(null);
            displayImage = null;
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
