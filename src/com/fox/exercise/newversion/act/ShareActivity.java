package com.fox.exercise.newversion.act;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fox.exercise.ImageDownloader;
import com.fox.exercise.R;
import com.fox.exercise.api.AddCoinsThread;
import com.fox.exercise.api.ApiConstant;
import com.fox.exercise.map.SWeiboBaseActivity;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ShareActivity extends SWeiboBaseActivity implements IUiListener {
    private GridView mGridView;
    private int[] myDjsImage = new int[]{R.drawable.fenxiang_weixin,
            R.drawable.fenxiang_weixin_friends, R.drawable.fenxiang_qq};
    private TextView share_cacle_txt;
    private Tencent mTencent;
    private IWXAPI api;
    private String Xiangqinurl = ApiConstant.DATA_URL
            + ApiConstant.getZhangzisInfo;
    private String ActionUrl = ApiConstant.DATA_URL
            + ApiConstant.getShareAction;
    String info_title, img = "", jianjie, actInfoContent;
    Intent intent;
    int infoid, bs;
    private Activity mContext;
    Bitmap bt;
    private ImageDownloader mDownloader = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        this.getWindow().getAttributes().gravity = Gravity.CENTER;
        setFinishOnTouchOutside(true);
        setContentView(R.layout.share_grid_layout);
        intent = getIntent();
        if(intent!=null){
            bs = intent.getIntExtra("bs", 1);
            infoid = intent.getIntExtra("infoid", -1);
            info_title = intent.getStringExtra("title");
            img = intent.getStringExtra("img_url");
            jianjie = intent.getStringExtra("jianjie");
            actInfoContent = intent.getStringExtra("content");
        }
        mContext = this;
        mGridView = (GridView) findViewById(R.id.share_dialog_gridview);
        MyAdapter adapter = new MyAdapter(this);
        mGridView.setAdapter(adapter);
        mDownloader = new ImageDownloader(this);

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
        if (!"".equals(img) && img != null) {

            getBit();
        } else {
            bt = BitmapFactory.decodeResource(getResources(),
                    R.drawable.share_icon);

        }
    }

    public void getBit() {
        new AsyncTask<Void, Void, Bitmap>() {

            @Override
            protected Bitmap doInBackground(Void... params) {
                // TODO Auto-generated method stub
                Bitmap bt2;
                bt2 = mDownloader.downloadBitmap(img);
                return bt2;
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                // TODO Auto-generated method stub
                if (result != null) {
                    bt = compressImage(result);
                }
            }
        }.execute();

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
                ShareWxFriend(0, infoid);
                finish();
            } else if (position == 1) {
                // shareToWeixin();
                ShareWxFriend(1, infoid);
                finish();
            }
        }

    }

    /**
     * 分享到微信
     */
    private void ShareWxFriend(int flag, int id) {

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
        share2weixin(flag, id);
    }

    private void share2weixin(int flag, int id) {
        if (api == null) {
            api = WXAPIFactory.createWXAPI(getApplication(),
                    WXEntryActivity.APP_ID, true);
            api.registerApp(WXEntryActivity.APP_ID);
        }

        WXWebpageObject webpage = new WXWebpageObject();
        if (bs == 1) {
            webpage.webpageUrl = Xiangqinurl + id + "&type=2";
        } else if (bs == 2) {
            webpage.webpageUrl = ActionUrl + id;
        }
        // Log.i("UU", Xiangqinurl + id + "&type=2");
        WXMediaMessage msg = new WXMediaMessage(webpage);
        // 自己
        if (flag == 1 && bs == 1) {
            msg.title = jianjie;
        } else if (flag == 1 && bs == 2) {
//            msg.title = actInfoContent;// title
            msg.title = info_title;// title
        } else {
            msg.title = info_title;
        }
        if (bt != null) {

            msg.setThumbImage(bt);
        } else {

        }
        if (bs == 2) {
            msg.description = actInfoContent;
        } else {
            msg.description = jianjie;
        }
        // msg.setThumbImage(bt);
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
        image.compress(Bitmap.CompressFormat.JPEG, 20, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 15) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    private void onClickShare() {
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,
                QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, info_title);// 要分享的标题
        // params.putString(QQShare.SHARE_TO_QQ_SUMMARY,
        // findGroup.getDetils());// 要分享的摘要
        if (bs == 1) {

            params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, Xiangqinurl
                    + infoid + "&type=2");
        } else if (bs == 2) {
            params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, ActionUrl + infoid);
        }
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, img);// ??????
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "1101732794");
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, 0);
        mTencent.shareToQQ(mContext, params, this);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null != mTencent)
            mTencent.onActivityResultData(requestCode, resultCode, data, this);
    }

    @Override
    public void onCancel() {
        // TODO Auto-generated method stub
//        Toast.makeText(mContext, "分享取消", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onComplete(Object arg0) {
        // TODO Auto-generated method stub
        finish();
        Toast.makeText(this, "QQ分享成功", Toast.LENGTH_SHORT).show();
        new AddCoinsThread(10, 4, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method
                // stub
                switch (msg.what) {
                    case ApiConstant.COINS_SUCCESS:
                        SportTaskUtil.jump2CoinsDialog(ShareActivity.this,
                                getString(R.string.shared_success_add_coins));
                        break;
                    case ApiConstant.COINS_LIMIT:
                        Toast.makeText(ShareActivity.this,
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
    public void onError(UiError arg0) {
        // TODO Auto-generated method stub
        Toast.makeText(mContext, "分享失败", Toast.LENGTH_SHORT).show();
        finish();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bt!=null){
            bt.recycle();
            bt=null;
        }
        if(mDownloader!=null){
            mDownloader=null;
        }
    }
}
