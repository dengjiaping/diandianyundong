package com.fox.exercise.newversion.newact;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fox.exercise.ImageDownloader;
import com.fox.exercise.R;
import com.fox.exercise.api.AddCoinsThread;
import com.fox.exercise.api.ApiConstant;
import com.fox.exercise.login.Tools;
import com.fox.exercise.map.SWeiboBaseActivity;
import com.fox.exercise.map.SportTaskDetailActivityGaode;
import com.fox.exercise.newversion.entity.FindGroup;
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

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.ingenic.indroidsync.SportsApp;

public class ShareDialogMainActivity extends SWeiboBaseActivity implements
        IUiListener {
    private GridView mGridView;
    private int[] myDjsImage = new int[]{R.drawable.fenxiang_weixin,
            R.drawable.fenxiang_weixin_friends, R.drawable.fenxiang_qq};
    private SportsApp mSportsApp;
    private Activity mContext;
    private FindGroup findGroup;

    private Tencent mTencent;
    private IWXAPI api;
    private ImageDownloader mDownloader = null;

    private TextView share_cacle_txt;

    private static final String URL = ApiConstant.DATA_URL
            + "m=sports&a=FindRecordHtml&id=";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.getWindow().getAttributes().gravity = Gravity.CENTER;
        setFinishOnTouchOutside(true);
        setContentView(R.layout.share_grid_layout);
        Intent intent = getIntent();
        if (intent != null) {
            findGroup = (FindGroup) intent.getSerializableExtra("findGroup");
            if (findGroup.getImgs() != null) {
                thisLarge = findGroup.getImgs()[0];
            }
        }
        mContext = this;
        mSportsApp = (SportsApp) getApplication();

        mGridView = (GridView) findViewById(R.id.share_dialog_gridview);
        MyAdapter adapter = new MyAdapter(this);
        mGridView.setAdapter(adapter);
        share_cacle_txt = (TextView) findViewById(R.id.share_cacle_txt);
        share_cacle_txt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        mTencent = Tencent.createInstance("1101732794",
                this.getApplicationContext());
        mDownloader = new ImageDownloader(mContext);

    }

    class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater = null;
        String[] stringArray = null;

        public MyAdapter(Context context) {
            mInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            stringArray = context.getResources().getStringArray(
                    R.array.share_content_arrs);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return myDjsImage.length;
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.share_gride_list_item,
                        null);
                holder = new ViewHolder();
                holder.img = (ImageView) convertView
                        .findViewById(R.id.share_icon);
                holder.txt = (TextView) convertView
                        .findViewById(R.id.share_text);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.img.setBackgroundResource(myDjsImage[position]);
            holder.txt.setText(stringArray[position]);
            holder.img.setOnClickListener(new ShareClicklistner(position));
            return convertView;
        }

    }

    class ViewHolder {
        public TextView txt;
        public ImageView img;
    }

    class ShareClicklistner implements OnClickListener {
        private int position;

        public ShareClicklistner(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (position == 2) {
                // shareToTenxunWeibo();
                onClickShare();
            } else if (position == 0) {
                // shareToWeixin();
                shareToWeixin(0);
                finish();
            } else if (position == 1) {
                // shareToWeixin();
                shareToWeixin(1);
                finish();
            }
            // else if (position == 3) {
            // shareToXinlangWeibo();
            // finish();
            // }
        }

    }

    @Override
    public void onCancel() {
        // TODO Auto-generated method stub
//        Toast.makeText(mContext, "分享取消", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onComplete(Object arg0) {
        // TODO Auto-generated method stub
        // String ssString = arg0.toString();
        // Toast.makeText(mContext, "分享成功", Toast.LENGTH_LONG).show();
        // if(LoginActivity.isWeiXinLogin) {
        // LoginActivity.WX_CODE = ((SendAuth.Resp) arg0).code;
        // Log.e("WXEntryActivity", LoginActivity.WX_CODE);
        // } else {
        Toast.makeText(this, "QQ分享成功", Toast.LENGTH_SHORT).show();
        new AddCoinsThread(10, 4, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method
                // stub
                switch (msg.what) {
                    case ApiConstant.COINS_SUCCESS:
                        SportTaskUtil.jump2CoinsDialog(
                                ShareDialogMainActivity.this,
                                getString(R.string.shared_success_add_coins));
                        break;
                    case ApiConstant.COINS_LIMIT:
                        Toast.makeText(getApplicationContext(),
                                getString(R.string.shared_beyond_10times),
                                Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        }, -1).start();
        // }
    }

    @Override
    public void onError(UiError arg0) {
        // TODO Auto-generated method stub
        Toast.makeText(mContext, "分享失败", Toast.LENGTH_LONG).show();
    }

    private void onClickShare() {
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,
                QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        if (findGroup.getOtheruid() == mSportsApp.getSportUser().getUid()) {
            // 自己
            params.putString(QQShare.SHARE_TO_QQ_TITLE,
                    "这是我的运动秀 #云狐运动# 帅哥、美女最多的运动APP");// 要分享的标题
        } else {
            params.putString(QQShare.SHARE_TO_QQ_TITLE,
                    findGroup.getOthername() + " 在云狐运动中万众瞩目，来凑个热闹吧");// 要分享的标题
        }
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, findGroup.getDetils());// 要分享的摘要
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,
                URL + findGroup.getFindId());
        if (!"".equals(thisLarge) && thisLarge != null) {

            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, thisLarge);// 要分享的图片
        } else {
            Bitmap mBitmap = BitmapFactory.decodeResource(getResources(),
                    R.drawable.share_icon);
            boolean saveFile = Tools.SaveBitmapAsFile(
                    SportTaskDetailActivityGaode.SHARE_PATH, mBitmap, 90);
            if (saveFile) {

                params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,
                        SportTaskDetailActivityGaode.SHARE_PATH);
            }
        }
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "1101732794");
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, 0);
        mTencent.shareToQQ(mContext, params, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mTencent.onActivityResultData(requestCode, resultCode, data, this);
        finish();
    }

    // 分享到微信里边的内容，其中flag 0是朋友圈，1是好友
    private void shareToWeixin(int flag) {
        if (api == null) {
            api = WXAPIFactory.createWXAPI(mContext, WXEntryActivity.APP_ID,
                    true);
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
            api = WXAPIFactory.createWXAPI(mContext, WXEntryActivity.APP_ID,
                    true);
            api.registerApp(WXEntryActivity.APP_ID);
        }

        if (!api.isWXAppInstalled()) {
            Toast.makeText(mContext, "您还未安装微信客户端", Toast.LENGTH_SHORT).show();
            return;
        }

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = URL + findGroup.getFindId();
        WXMediaMessage msg = new WXMediaMessage(webpage);
        if (findGroup.getOtheruid() == mSportsApp.getSportUser().getUid()) {
            // 自己
            msg.title = "这是我的运动秀 #云狐运动# 帅哥、美女最多的运动APP";// title
        } else {
            msg.title = findGroup.getOthername() + " 在云狐运动中万众瞩目，来凑个热闹吧";// title
        }
        msg.description = findGroup.getDetils();
        // Bitmap thumb = getBitmap(findGroup.getImgs()[0]);
        // BitmapDrawable draw = (BitmapDrawable) getResources().getDrawable(
        // R.drawable.indexpage_sport_step_icon);
        // Bitmap m = draw.getBitmap();

        if (findGroup.getImgs() != null) {
            msg.setThumbImage(mDownloader.downloadBitmap(findGroup.getImgs()[0]));
        } else {
            BitmapDrawable bitmap = (BitmapDrawable) getResources()
                    .getDrawable(R.drawable.share_icon);
            Bitmap b = bitmap.getBitmap();
            msg.setThumbImage(b);
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag;
        api.sendReq(req);
    }

    // 根据图片的URL路径来获取网络图片，核心代码如下：
    public static Bitmap getBitmap(String path) {
        URL url;
        Bitmap bitmap = null;
        try {
            url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                InputStream inputStream = conn.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
            return null;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (mDownloader == null) {
            mDownloader = new ImageDownloader(mContext);
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        mDownloader = null;
        myDjsImage = null;
        mSportsApp = null;
    }

}
