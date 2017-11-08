package com.fox.exercise.map;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fox.exercise.AbstractBaseActivity;
import com.fox.exercise.FindFriendsSendMsg;
import com.fox.exercise.R;
import com.fox.exercise.SportsUtilities;
import com.fox.exercise.api.AddCoinsThread;
import com.fox.exercise.api.ApiConstant;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.entity.GetPeisu;
import com.fox.exercise.api.entity.PeisuInfo;
import com.fox.exercise.db.PeisuDB;
import com.fox.exercise.login.Tools;
import com.fox.exercise.publish.Bimp;
import com.fox.exercise.util.SportTaskUtil;
import com.fox.exercise.util.SportTrajectoryUtilGaode;
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
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import cn.ingenic.indroidsync.SportsApp;

/**
 * Created by zhangjian on 2016/8/1.
 */
public class PeiSuActivity extends AbstractBaseActivity implements View.OnClickListener, IUiListener {
    ListView listView;
    private String bsid, imgurl;
    private int taskId, typeid;
    private PeisuDB db;
    private List<GetPeisu> ps;
    private PeisuInfo peisuInfo;
    private GetPeisu pp = new GetPeisu();
    private PeisuAdapter peisuAdapter;
    private TextView textView_pj, textView_zk;
    private int temp = 0, tm, g, pj;
    private SportsApp mSportsApp;
    private Dialog shareDialog;
    private Tencent mTencent;
    private IWXAPI api;
    private String shareurl = ApiConstant.DATA_URL + "m=sports&a=sportstakedata&id=";
    public static final String SHARE_PATH = SportsUtilities.DOWNLOAD_SAVE_PATH
            + "shareImage.jpg";
    private ArrayList<String> peiSuList1, peiSuList2, peiSuList3;//配速的三个集合
    private Bitmap mBitmap;
    private RelativeLayout peisu_toplayout;


    @Override
    public void initIntentParam(Intent intent) {
        title = "配速";
        if(intent==null){
            intent=getIntent();
        }
        bsid = intent.getStringExtra("bs");
        taskId = intent.getIntExtra("taskid", -1);
        typeid = intent.getIntExtra("typeId", -1);
        imgurl = intent.getStringExtra("imgurl");
    }

    @Override
    public void initView() {
        showContentView(R.layout.peisulayout);
        top_title_layout.setBackgroundResource(R.color.peisu_color);
        listView = (ListView) findViewById(R.id.peisu_list);
//        listView.setDivider(getResources().getDrawable(R.drawable.peisu_divier));
//        listView.setDividerHeight(8);
        Typeface typeFace = Typeface.createFromAsset(getAssets(),
                "DIN Medium.ttf");
        textView_pj = (TextView) findViewById(R.id.peisu_pingjun);
        textView_zk = (TextView) findViewById(R.id.peisu_zuikuai);
        textView_pj.setTypeface(typeFace);
        textView_zk.setTypeface(typeFace);
        peisu_toplayout=(RelativeLayout) findViewById(R.id.peisu_toplayout);
        mSportsApp = (SportsApp) getApplication();
        db = PeisuDB.getmInstance(this);
        ps = new ArrayList<GetPeisu>();
        mTencent = Tencent.createInstance("1101732794",
                this.getApplicationContext());

        ImageButton iView = new ImageButton(this);
        iView.setBackgroundResource(R.drawable.sportdetail_share);
        iView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        showRightBtn(iView);
        right_btn.setPadding(0, 0, SportsApp.dip2px(17), 0);

        if(taskId>0){
            if(mSportsApp.isOpenNetwork()){
                getPeisuData(0);//从服务器获取数据
            }
        }

        ps = db.selectAll(bsid);
        //分享的单击事件
            iView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(ps!=null&&ps.size()>0){
                        Toshare();
                    }

                }


            });
            right_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(ps!=null&&ps.size()>0){
                        Toshare();
                    }
                }


            });
        if (ps != null&&ps.size() != 0 ) {
            dataToView();
        } else {
            if (taskId > 0) {
                if(mSportsApp.isOpenNetwork()){
                    getPeisuData(1);//从服务器获取数据
                }
            } else {
                Toast.makeText(PeiSuActivity.this, "暂无配速数据", Toast.LENGTH_SHORT).show();

            }

        }
        peiSuList1 = new ArrayList<String>();
        peiSuList2 = new ArrayList<String>();
        peiSuList3 = new ArrayList<String>();
        if (!"".equals(imgurl) && imgurl != null) {
            mBitmap = lessenUriImage(imgurl);
        } else {
            mBitmap = BitmapFactory.decodeResource(getResources(),
                    R.drawable.share_icon);
        }

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

    private void dataToView() {
        if (ps != null&&ps.size()>0) {
            ArrayList<Integer> aa = new ArrayList<Integer>();
            for (int i = 0; i < ps.size(); i++) {
                aa.add(Integer.parseInt(ps.get(i).getSprots_time()));
            }
            if(ps.size()==1){
                if(ps.get(0).getSport_distance()!=null&&!"".equals(ps.get(0).getSport_distance())&&Integer.parseInt(ps.get(0).getSport_distance())==1){
                    textView_zk.setText(getPeisu(Integer.parseInt(ps.get(0).getSprots_velocity())));
                    textView_pj.setText(getPeisu(Integer.parseInt(ps.get(0).getSprots_velocity())));
                }

            }else {
                for (int j = 1; j < aa.size(); j++) {
                    ps.get(j).setSprots_velocity(aa.get(j) - aa.get(j - 1) + "");
                    if (j == 1) {
                        temp = Integer.parseInt(ps.get(j).getSprots_velocity());
                        tm = Integer.parseInt(ps.get(j).getSprots_time());
                        g = Integer.parseInt(ps.get(j).getSport_distance());

                }
                int ss = Integer.parseInt(ps.get(j).getSprots_velocity());
                int tt = Integer.parseInt(ps.get(j).getSprots_time());
                int gg = Integer.parseInt(ps.get(j).getSport_distance());
                //获取最小配速
                if (ss <= temp) {
                    temp = ss;
                }
                //获取最大时间
                if (tt >= tm) {
                    tm = tt;
                }
                //获取总公里数
                if (gg >= g) {
                    g = gg;
                }
                pj = tm / g;

                }
                textView_zk.setText(getPeisu(temp));
                textView_pj.setText(getPeisu(pj));
            }
            peisuAdapter = new PeisuAdapter(PeiSuActivity.this, ps);
            listView.setAdapter(peisuAdapter);
        } else {
            Toast.makeText(PeiSuActivity.this, "本地数据为空", Toast.LENGTH_SHORT).show();
        }

    }

    //从服务器获取配速数据 1表示从服务器端获取数据并赋值给ps 其他不用赋值
    private void getPeisuData(final  int flag) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                peisuInfo = ApiJsonParser.getPeisu(mSportsApp.getSessionId(), taskId, typeid);
                if(flag==1){
                    handler.sendEmptyMessage(1);
                }
            }
        }).start();
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 1:
                    if (peisuInfo != null) {
                        ps = peisuInfo.getListpeis();
                        if (ps != null && ps.size() != 0) {
                            ArrayList<Integer> aa = new ArrayList<Integer>();
                            for (int i = 0; i < ps.size(); i++) {
                                aa.add(Integer.parseInt(ps.get(i).getSprots_time()));
                            }
                            for (int j = 1; j < aa.size(); j++) {
                                ps.get(j).setSprots_velocity(aa.get(j) - aa.get(j - 1) + "");
                            }
                            //获取数据存本地数据库
                            savePeisu(ps, bsid);
                            textView_zk.setText(getPeisu(peisuInfo.getSports_minvelocity()));
                            textView_pj.setText(getPeisu(peisuInfo.getSprots_svgvelocity()));
                            peisuAdapter = new PeisuAdapter(PeiSuActivity.this, ps);
                            listView.setAdapter(peisuAdapter);
                        } else {
                            Toast.makeText(PeiSuActivity.this, "暂无配速数据", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
            }
        }
    };

    @Override
    public void setViewStatus() {

    }

    @Override
    public void onPageResume() {
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    public void onPagePause() {

    }

    @Override
    public void onPageDestroy() {
        peiSuList1 = null;
        peiSuList2 = null;
        peiSuList3 = null;
        ps=null;
        peisuInfo=null;
        pp=null;
        if(mBitmap!=null){
            mBitmap.recycle();
            mBitmap=null;
        }

    }

    //配速或登山存本地
    private int savePeisu(List<GetPeisu> listpeis, String bs) {
        int savainsert = 0;
        for (int i = 0; i < listpeis.size(); i++) {
            peiSuList1.add(listpeis.get(i).getSport_distance());
            peiSuList2.add(listpeis.get(i).getSprots_velocity());
            peiSuList3.add(listpeis.get(i).getSprots_time());
        }
        ContentValues c = new ContentValues();
        c.put(PeisuDB.GongLi, SportTrajectoryUtilGaode.peiListToString(peiSuList1));
        c.put(PeisuDB.Peisu, SportTrajectoryUtilGaode.peiListToString(peiSuList2));
        c.put(PeisuDB.Time, SportTrajectoryUtilGaode.peiListToString(peiSuList3));
        c.put(PeisuDB.SPORT_MARKCODE, bs);
        c.put(PeisuDB.SPORT_ISUPLOAD, 1 + "");
        savainsert = db.insert(c);

        return savainsert;
    }


    /**
     * 获取和保存当前屏幕的截图
     */
    private void GetandSaveCurrentImage() {
        // 获取窗口的顶层视图对象
        View v = getWindow().getDecorView();
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache();
        // 第一步:获取保存屏幕图像的Bitmap对象

        Bitmap srcBitmap = v.getDrawingCache();

        Rect frame = new Rect();
        // decorView是window中的最顶层view，可以从window中获取到decorView，然后decorView有个getWindowVisibleDisplayFrame方法可以获取到程序显示的区域，包括标题栏，但不包括状态栏。
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);

        // 第二步 得到状态栏的高度
        int statusHeight = frame.top;
        // 第三步 获取屏幕图像的高度
        Point outSize = new Point();

        getWindowManager().getDefaultDisplay().getSize(outSize);

        int width = outSize.x;

        int height = outSize.y;

        // 第四步 创建新的Bitmap对象 并截取除了状态栏的其他区域
        Bitmap bitmap = null;
                if(ps.size()>4){
                    bitmap = Bitmap.createBitmap(srcBitmap, 0, statusHeight + top_title_layout.getHeight(),
                width, height - statusHeight - top_title_layout.getHeight());
                }else{
                    bitmap = Bitmap.createBitmap(srcBitmap, 0, statusHeight + top_title_layout.getHeight(),
                            width, SportsApp.dip2px(75)*ps.size()+SportsApp.dip2px(15)+peisu_toplayout.getHeight());
                }

        v.destroyDrawingCache();
        try {
            String filepath = SHARE_PATH;
            File file = new File(filepath);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = null;
            fos = new FileOutputStream(file);
            if (null != fos) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                fos.flush();
                fos.close();
//                Toast.makeText(this, "截屏文件已保存", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //qq分享
            case R.id.xinlang:
            case R.id.xinlang_layout:
                if (shareDialog != null && shareDialog.isShowing()) {
                    shareDialog.dismiss();
                }
                if(taskId>0){
                    if(peisuInfo!=null&&peisuInfo.getPeisuid()>0){
                        onClickShare();
                    }else{
                        Toast.makeText(this,getResources().getString(R.string.sports_get_list_failed1),Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this,getResources().getString(R.string.please_upload_sportsrecorder),Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.tengxun:
            case R.id.tengxun_layout:
                if (shareDialog != null && shareDialog.isShowing()) {
                    shareDialog.dismiss();
                }
                if(taskId>0){
                    if(peisuInfo!=null&&peisuInfo.getPeisuid()>0){
                        ShareWxFriend(1, peisuInfo.getPeisuid());
                    }else{
                        Toast.makeText(this,getResources().getString(R.string.sports_get_list_failed1),Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this,getResources().getString(R.string.please_upload_sportsrecorder),Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.weixin:
            case R.id.weixin_layout:
                Log.e("weixinshare", "share to weixin");
                if (shareDialog != null && shareDialog.isShowing()) {
                    shareDialog.dismiss();
                }
                if(taskId>0){
                    if(peisuInfo!=null&&peisuInfo.getPeisuid()>0){
                        ShareWxFriend(0, peisuInfo.getPeisuid());
                    }else{
                        Toast.makeText(this,getResources().getString(R.string.sports_get_list_failed1),Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this,getResources().getString(R.string.please_upload_sportsrecorder),Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.faxian:
            case R.id.faxian_layout:
                if (shareDialog != null && shareDialog.isShowing()) {
                    shareDialog.dismiss();
                }
//                GetandSaveCurrentImage();
                Bitmap wbitmap = syntheticImagesBitmap(shot(), getbBitmap(listView,this));
                boolean saveFile = Tools.SaveBitmapAsFile(SHARE_PATH, wbitmap);
                if(wbitmap!=null){
                    wbitmap.recycle();
                    wbitmap=null;
                }
                if (saveFile) {
                    if (Bimp.drr.size() < 9) {
                        Bimp.drr.add(SHARE_PATH);
                        Log.e(TAG, "SHARE_PATH : " + SHARE_PATH);
                    }
                    Intent intent = new Intent(this, FindFriendsSendMsg.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(this,getResources().getString(R.string.toast_bitmap_decode_failed),Toast.LENGTH_SHORT).show();
                }


                break;
        }
    }

    //去分享的dialog
    private void Toshare() {
        shareDialog = new Dialog(PeiSuActivity.this, R.style.sports_coins_dialog);
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

    /**
     * 分享到微信
     */
    private void ShareWxFriend(final int flag, final int id) {

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
        if(mBitmap!=null){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    share2weixin(flag, id);
                }
            }).start();
        }else{
            Toast.makeText(getApplicationContext(),
                    getString(R.string.toast_bitmap_decode_failed),
                    Toast.LENGTH_SHORT).show();
        }


    }

    private void share2weixin(int flag, int id) {
        if (api == null) {
            api = WXAPIFactory.createWXAPI(getApplication(),
                    WXEntryActivity.APP_ID, true);
            api.registerApp(WXEntryActivity.APP_ID);
        }

        String text = "我刚用#云狐运动# 跑了" + peisuInfo.getSport_distance() + "公里，平均配速" + getPeisu(peisuInfo.getSprots_svgvelocity()) + "，快来围观吧";
        WXWebpageObject webpage = new WXWebpageObject();
        //分享的链接
        webpage.webpageUrl = shareurl + id;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        // 自己
        if (flag == 1) {
            msg.title = text;
        } else {
            msg.title = "云狐运动";// 分享的标题
        }
        msg.description = text;//分享的内容
        msg.setThumbImage(compressImage(mBitmap));//分享的图片
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag;
        api.sendReq(req);
    }

    //分享到qq
    private void onClickShare() {
        if(imgurl!=null&&!"".equals(imgurl)){
            final Bundle params = new Bundle();
            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,
                    QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
            params.putString(QQShare.SHARE_TO_QQ_TITLE, "云狐运动");// 要分享的标题
            params.putString(QQShare.SHARE_TO_QQ_SUMMARY,
                    "我刚用#云狐运动# 跑了" + peisuInfo.getSport_distance() + "公里，平均配速" + getPeisu(peisuInfo.getSprots_svgvelocity()) + "，快来围观吧\n");// 要分享的摘要

            params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareurl + peisuInfo.getPeisuid());//分享的链接
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imgurl);// 分享的图片
            params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "1101732794");
            params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, 0);
            mTencent.shareToQQ(PeiSuActivity.this, params, this);
        }else {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.toast_bitmap_decode_failed),
                    Toast.LENGTH_SHORT).show();
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null != mTencent)
            mTencent.onActivityResultData(requestCode, resultCode, data, this);
    }

    @Override
    public void onComplete(Object o) {
        Toast.makeText(this, "QQ分享成功", Toast.LENGTH_SHORT).show();
        new AddCoinsThread(10, 4, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method
                // stub
                switch (msg.what) {
                    case ApiConstant.COINS_SUCCESS:
                        SportTaskUtil.jump2CoinsDialog(PeiSuActivity.this,
                                getString(R.string.shared_success_add_coins));
                        break;
                    case ApiConstant.COINS_LIMIT:
                        Toast.makeText(PeiSuActivity.this,
                                getString(R.string.shared_beyond_10times),
                                Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        }, -1).start();
        finish();
    }

    @Override
    public void onError(UiError uiError) {
        Toast.makeText(PeiSuActivity.this, "分享失败", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCancel() {
//        Toast.makeText(PeiSuActivity.this, "分享取消", Toast.LENGTH_LONG).show();
    }

    //配速的adapter
    class PeisuAdapter extends BaseAdapter implements ListAdapter {
        private Context mContext = null;
        private List<GetPeisu> mListData = null;
        private TextView text_gongli, text_peisu, text_time;
        private View last_line,view1;
        private RelativeLayout bottom_layout;
        private int width;
        private double minSpeed,maxSpeed;

        public PeisuAdapter(Context context, List<GetPeisu> ps) {
            this.mContext = context;
            this.mListData = ps;
            WindowManager wm = (WindowManager)getSystemService(
                    Context.WINDOW_SERVICE);

            width = wm.getDefaultDisplay().getWidth()-SportsApp.dip2px(65);
            if(ps!=null&&ps.size()>0){
                try {
                    minSpeed=Double.parseDouble(ps.get(0).getSprots_velocity());
                    maxSpeed=Double.parseDouble(ps.get(0).getSprots_velocity());
                    for (int i=0;i<ps.size();i++){
                        if(minSpeed<=Double.parseDouble(ps.get(i).getSprots_velocity())){
                            minSpeed=Double.parseDouble(ps.get(i).getSprots_velocity());
                        }
                        if(maxSpeed>=Double.parseDouble(ps.get(i).getSprots_velocity())){
                            maxSpeed=Double.parseDouble(ps.get(i).getSprots_velocity());
                        }
                    }
                }catch (Exception e){

                }

            }

        }


        @Override
        public int getCount() {
            if (mListData != null) {
                return mListData.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (mListData != null) {
                return mListData.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            GetPeisu list_data = mListData.get(position);
            View view = inflater.inflate(R.layout.peisu_item, null);
            double time=0, time2=0;
            int pace, pace2;
            String paceStringValue="", paceStringVaule2="";
            try {
                time = Double.parseDouble(list_data.getSprots_time());
                time2 = Double.parseDouble(list_data.getSprots_velocity());
                //用时
                if (time != 0) {
                    pace = (int) time / 60000;// 配速：单位 分/公里
                    pace2 = (int) (time - pace * 60000) / 1000;

                } else {
                    pace = 0;
                    pace2 = 0;
                }
                paceStringVaule2 = getTwoPeisu(time2);
                paceStringValue = (int) (pace) + "'"
                        + (int) (pace2) + "\"";
            }catch (Exception e){

            }

            TextView speed_num=(TextView) view.findViewById(R.id.speed_num);
            speed_num.setText(list_data.getSport_distance());

            TextView speed_values=(TextView) view.findViewById(R.id.speed_values);

            if(minSpeed!=0){
                int txWidth=(int)(width*(time2/minSpeed));
                if(txWidth<180){
                    txWidth=180;
                }
                speed_values.getLayoutParams().width=txWidth;
                speed_values.setText(paceStringVaule2);
            }

            TextView fivecol_shouinfo=(TextView) view.findViewById(R.id.fivecol_shouinfo);
            if(list_data.getSport_distance()!=null&&!"".equals(list_data.getSport_distance())){
                if(Integer.parseInt(list_data.getSport_distance())%5==0){
                    fivecol_shouinfo.setVisibility(View.VISIBLE);
                    paceStringValue=SportTaskUtil.showTimeCount((int)(time/1000));
                    fivecol_shouinfo.setText(list_data.getSport_distance()+getResources().getString(R.string.peisu_timetx)+paceStringValue);
                }else{
                    fivecol_shouinfo.setVisibility(View.GONE);
                }
            }else{
                fivecol_shouinfo.setVisibility(View.GONE);
            }


            if(time2==maxSpeed){
                view.findViewById(R.id.peisu_fasticon).setVisibility(View.VISIBLE);
            }else{
                view.findViewById(R.id.peisu_fasticon).setVisibility(View.GONE);
            }

            return view;
        }
    }

    //配速的算法
    private String getPeisu(double time2) {
        int pace3;
        int pace4;
        String paceStringVaule2;//配速
        if (time2 != 0) {
            pace3 = (int) time2 / 60000;// 配速：单位 分/公里
            pace4 = (int) (time2 - pace3 * 60000) / 1000;

        } else {
            pace3 = 0;
            pace4 = 0;
        }
        paceStringVaule2 = (int) (pace3) + "'"
                + (int) (pace4) + "\"";
        return paceStringVaule2;
    }

    //配速的算法 分钟最少保留两位
    private String getTwoPeisu(double time2) {
        int pace3;
        int pace4;
        String paceStringVaule2;//配速
        if (time2 != 0) {
            pace3 = (int) time2 / 60000;// 配速：单位 分/公里
            pace4 = (int) (time2 - pace3 * 60000) / 1000;

        } else {
            pace3 = 0;
            pace4 = 0;
        }
        if(pace3<10){
            if(pace4<10){
                paceStringVaule2 = "0"+(int) (pace3) + "'"+"0"
                        + (int) (pace4) + "\"";
            }else{
                paceStringVaule2 = "0"+(int) (pace3) + "'"
                        + (int) (pace4) + "\"";
            }

        }else{
            if(pace4<10){
                paceStringVaule2 = (int) (pace3) + "'"+"0"
                        + (int) (pace4) + "\"";
            }else{
                paceStringVaule2 = (int) (pace3) + "'"
                        + (int) (pace4) + "\"";
            }

        }

        return paceStringVaule2;
    }

    /**
     * 图片压缩
     */
    private Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 20, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 20) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10

        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }


     //截取listview上半部分的图片
    public Bitmap shot() {
        View view = getWindow().getDecorView();
        Display display = this.getWindowManager().getDefaultDisplay();
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusheight = frame.top;// 手机状态栏高度
        view.setDrawingCacheEnabled(true);// 允许当前窗口保存缓存信息，这样getDrawingCache()方法才会返回一个Bitmap
        Bitmap  bitmap = Bitmap.createBitmap(view.getDrawingCache(), 0, statusheight + top_title_layout.getHeight(),
                display.getWidth(), peisu_toplayout.getHeight());
        view.destroyDrawingCache();
        return bitmap;
    }


    //合成图片,src在上面,mark在src的下面
    public Bitmap syntheticImagesBitmap(Bitmap src, Bitmap mark) {

        int w = src.getWidth();
        int h = src.getHeight();
        int wh = mark.getHeight();
        Bitmap newb = Bitmap.createBitmap(w, h + wh, Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(newb);
        cv.drawBitmap(src, 0, 0, null);
        cv.drawBitmap(mark, 0, h, null);
        cv.save(Canvas.ALL_SAVE_FLAG);
        cv.restore();
        if (src != null && !src.isRecycled()) {
            src.recycle();
            src = null;
        }
        if (mark != null && !mark.isRecycled()) {
            mark.recycle();
            mark = null;
        }
        return newb;
    }




    /**
     * ListView 截屏
     * @param listView
     * @param context
     * @return
     */
    public  Bitmap getbBitmap(ListView listView, Context context){
        int titleHeight,width, height, rootHeight=0;
        Bitmap bitmap;
        Canvas canvas;
        int yPos = 0;
        int listItemNum;
        List<View> childViews = null;

        width = SportsApp.ScreenWidth;//宽度等于屏幕宽

        ListAdapter listAdapter = listView.getAdapter();
        listItemNum = listAdapter.getCount();
        childViews = new ArrayList<View>(listItemNum);
        View itemView;
        //计算整体高度:
        for(int pos=0; pos < listItemNum; ++pos){
            itemView = listAdapter.getView(pos, null, listView);
            //measure过程
            itemView.measure(View.MeasureSpec.makeMeasureSpec(width, View. MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            childViews.add(itemView);
            rootHeight += itemView.getMeasuredHeight();
        }

        height = rootHeight;
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(listView.getWidth(), height,
                Bitmap.Config.ARGB_8888);
        //bitmap = BitmapUtil.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);

        Bitmap itemBitmap;
        int childHeight;
        //把每个ItemView生成图片，并画到背景画布上
        for(int pos=0; pos < childViews.size(); ++pos){
            itemView = childViews.get(pos);
            childHeight = itemView.getMeasuredHeight();
            itemBitmap = viewToBitmap(itemView,width,childHeight);
            if(itemBitmap!=null){
                canvas.drawBitmap(itemBitmap, 0, yPos, null);
            }
            yPos = childHeight +yPos;
        }

        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return bitmap;
    }


    public  Bitmap viewToBitmap(View view, int viewWidth, int viewHeight){
        view.layout(0, 0, viewWidth, viewHeight);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

}
