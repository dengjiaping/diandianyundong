package com.fox.exercise.newversion.bushutongji;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fox.exercise.AbstractBaseActivity;
import com.fox.exercise.FindFriendsSendMsg;
import com.fox.exercise.ImageDownloader;
import com.fox.exercise.R;
import com.fox.exercise.SportsUtilities;
import com.fox.exercise.api.AddCoinsThread;
import com.fox.exercise.api.ApiConstant;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiMessage;
import com.fox.exercise.api.Util;
import com.fox.exercise.login.LoginActivity;
import com.fox.exercise.login.Tools;
import com.fox.exercise.publish.Bimp;
import com.fox.exercise.util.SportTaskUtil;
import com.fox.exercise.wxapi.WXEntryActivity;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import cn.ingenic.indroidsync.SportsApp;

public class BuShuTongJiBarChart extends AbstractBaseActivity implements IUiListener {

    private String TAG = "develop_debug";
    private String SHARE_PATH = SportsUtilities.DOWNLOAD_SAVE_PATH + "shareImage.jpg";
    private int page;
    private int listNum;
    private ArrayList<BuShuTongJiDetail> list;
    private final int GET_FROM_LOCAL = 1;
    private final int GET_FROM_NETWORK = 2;
    private final int UPDATE_DATA = 3;
    private final int OUTPUT_VIEW = 4;
    private final int SHARE_RESULT = 5;
    private final int SAVE_TO_LOCAL = 6;
    private final int SAVE_TO_NETWORK = 7;
    private final int OUTPUT_VIEW_TIMER = 8;
    private BuShuTongJiDB db;
    private LayoutInflater mInflater;
    private int screenHeight;
    private int selectPosition;
    private HorizontalListView listview;
    private TextView tv_steps, tv_juli, tv_reliang;
    private IWXAPI api;
    private ImageDownloader mDownloader;
    private Dialog shareDialog;
    private int shareId;
    private Tencent mTencent;
    private String today, yestoday;
    private Dialog alertDialog;
    private Dialog mLoadProgressDialog;
    private TextView mDialogMessage;
    private Boolean haveData;
    private Boolean fromClick;
    private Boolean fromOutView;
    private Boolean fromGetView;
    private TimerTask task;
    private Timer timer;

    private BaseAdapter mAdapter = new BaseAdapter() {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            Log.e("develop_debug", "position:"+position);
            if (haveData && !fromClick && !fromOutView) {
                if (position == 0) {
                    fromGetView = true;

                    Message msg = updateHandler.obtainMessage();
                    msg.what = GET_FROM_LOCAL;
                    msg.sendToTarget();
                }
            }

            fromClick = false;
            fromOutView = false;

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.activity_bushutongji_viewitem, parent, false);
                ViewHolder holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }

            if (convertView != null && convertView.getTag() instanceof ViewHolder) {
                final ViewHolder holder = (ViewHolder) convertView.getTag();
                holder.image.setTag(list.get(position));
                holder.image_bg.setTag(list.get(position));
                if ((selectPosition >= 0) && (selectPosition == position)) {
                    holder.image.setBackgroundColor(android.graphics.Color.parseColor("#ff7800"));
                    holder.title.setTextColor(android.graphics.Color.parseColor("#ff7800"));
                } else {
                    holder.image.setBackgroundColor(android.graphics.Color.parseColor("#ffad00"));
                    ;
                    holder.title.setTextColor(android.graphics.Color.parseColor("#999999"));
                }
                holder.image_bg.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if (((BuShuTongJiDetail) v.getTag()).getDay() == null) {
                            return;
                        }

                        fromClick = true;

                        holder.image.setBackgroundColor(android.graphics.Color.parseColor("#ff7800"));
                        holder.title.setTextColor(android.graphics.Color.parseColor("#ff7800"));
                        selectPosition = position;

                        Message msg1 = updateHandler.obtainMessage();
                        msg1.what = UPDATE_DATA;
                        msg1.obj = v.getTag();
                        msg1.sendToTarget();
                    }
                });
                holder.image.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if (((BuShuTongJiDetail) v.getTag()).getDay() == null) {
                            return;
                        }

                        fromClick = true;

                        holder.image.setBackgroundColor(android.graphics.Color.parseColor("#ff7800"));
                        holder.title.setTextColor(android.graphics.Color.parseColor("#ff7800"));
                        selectPosition = position;

                        Message msg1 = updateHandler.obtainMessage();
                        msg1.what = UPDATE_DATA;
                        msg1.obj = v.getTag();
                        msg1.sendToTarget();
                    }
                });

                LayoutParams para = holder.image.getLayoutParams();
                int stepNum = list.get(position).getStep_num();

                if (stepNum <= 7000) {
                    para.height = (screenHeight * 5 / 10) * stepNum / 7000;
                } else if ((7000 < stepNum) && (stepNum <= 14000)) {
                    para.height = (screenHeight * 5 / 10) + (screenHeight * 2 / 10) * (stepNum - 7000) / 7000;
                } else if ((14000 < stepNum) && (stepNum <= 21000)) {
                    para.height = (screenHeight * 7 / 10) + (screenHeight * 2 / 10) * (stepNum - 14000) / 7000;
                } else if ((21000 < stepNum) && (stepNum <= 28000)) {
                    para.height = (screenHeight * 9 / 10) + (screenHeight * 1 / 10) * (stepNum - 21000) / 7000;
                } else {
                    para.height = screenHeight;
                }

                holder.image.setLayoutParams(para);

                holder.title.setText(getFormatedDate(list.get(position).getDay()));
            }

            return convertView;
        }

        class ViewHolder {
            public TextView title;
            public ImageView image;
            public ImageView image_bg;

            public ViewHolder(View viewRoot) {
                title = (TextView) viewRoot.findViewById(R.id.title);
                image = (ImageView) viewRoot.findViewById(R.id.image);
                image_bg = (ImageView) viewRoot.findViewById(R.id.image_bg);
            }
        }
    };

    @Override
    public void initIntentParam(Intent intent) {
        // TODO Auto-generated method stub
        title = "步数统计";
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        showContentView(R.layout.activity_bushutongji_barchart);

        page = 0;
        listNum = 0;
        list = new ArrayList<BuShuTongJiDetail>();
        selectPosition = -1;
        haveData = false;
        fromClick = false;
        fromOutView = false;
        fromGetView = false;

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        screenHeight = metric.heightPixels / 2 - 50;

        mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        listview = (HorizontalListView) findViewById(R.id.listview);
        listview.setAdapter(mAdapter);
        tv_steps = (TextView) this.findViewById(R.id.tv_steps);
        tv_juli = (TextView) this.findViewById(R.id.tv_juli);
        tv_reliang = (TextView) this.findViewById(R.id.tv_reliang);

        ImageButton iView = new ImageButton(this);
        iView.setBackgroundResource(R.drawable.bushutongji_fenxiang);
        iView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        iView.setOnClickListener(new rightOnClickListener());
        showRightBtn(iView);
        right_btn.setPadding(0, 0, SportsApp.dip2px(17), 0);
        right_btn.setOnClickListener(new rightOnClickListener());

        alertDialog = null;
        mLoadProgressDialog = null;
    }

    @Override
    public void setViewStatus() {
        // TODO Auto-generated method stub
        Message msg = updateHandler.obtainMessage();
        msg.what = SAVE_TO_LOCAL;
        msg.sendToTarget();
    }

    public void TyrLoginAction(final Context context, String title,
                               String message) {
        if (alertDialog == null) {
            initTryLoginDialog(context, title, message);
        }
        if (alertDialog.isShowing()) {
            return;
        } else {
            alertDialog.show();
        }

    }

    private void initTryLoginDialog(final Context context, String title,
                                    String message) {
        // TODO Auto-generated method stub
        alertDialog = new Dialog(context, R.style.sports_dialog);
        alertDialog.setCanceledOnTouchOutside(false);
        LayoutInflater mInflater = ((Activity) context).getLayoutInflater();
        View v = mInflater.inflate(R.layout.sports_dialog, null);
        ((TextView) v.findViewById(R.id.message)).setText(message);
        ((TextView) v.findViewById(R.id.title)).setText(title);
        Button button = (Button) v.findViewById(R.id.bt_ok);
        button.setText(getResources().getString(R.string.login_now));
        v.findViewById(R.id.bt_ok).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        intent.putExtra("isfirst_try", false);
                        context.startActivity(intent);
                        alertDialog.dismiss();
                        finish();
                    }
                });
        v.findViewById(R.id.bt_cancel).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        alertDialog.dismiss();
                    }
                });
        v.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
        alertDialog.setCancelable(true);
        alertDialog.setContentView(v);
        alertDialog.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface arg0) {
                // TODO Auto-generated method stub
                alertDialog = null;
            }
        });
    }

    class rightOnClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.title_right_btn:
                    if (SportsApp.getInstance().getSportUser().getUid() <= 0) {
                        TyrLoginAction(BuShuTongJiBarChart.this,
                                getString(R.string.sports_love_title),
                                getString(R.string.try_to_login));
                    } else {
                        WindowManager.LayoutParams lp = BuShuTongJiBarChart.this.getWindow().getAttributes();
                        lp.alpha = 0.3f;
                        BuShuTongJiBarChart.this.getWindow().setAttributes(lp);

                        shareDialog = new Dialog(BuShuTongJiBarChart.this, R.style.sports_dialog1);
                        LayoutInflater inflater1 = getLayoutInflater();
                        View view = inflater1.inflate(R.layout.dialog_bushutongji_share, null);
                        view.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.9));
                        view.findViewById(R.id.faxian_layout).setOnClickListener(this);
                        view.findViewById(R.id.weixin_layout).setOnClickListener(this);
                        view.findViewById(R.id.weixin_pengyouquan_layout).setOnClickListener(this);
                        view.findViewById(R.id.qq_layout).setOnClickListener(this);
                        view.findViewById(R.id.share_cacle_txt).setOnClickListener(
                                new OnClickListener() {

                                    @Override
                                    public void onClick(View arg0) {
                                        // TODO Auto-generated method stub
                                        if (shareDialog != null) {
                                            shareDialog.dismiss();
                                            shareDialog = null;
                                        }
                                    }
                                });
                        shareDialog.setContentView(view);
                        shareDialog.setCancelable(true);
                        shareDialog.setCanceledOnTouchOutside(true);
                        shareDialog.setOnDismissListener(new OnDismissListener() {

                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                // TODO Auto-generated method stub
                                WindowManager.LayoutParams lp = BuShuTongJiBarChart.this.getWindow().getAttributes();
                                lp.alpha = 1f;
                                BuShuTongJiBarChart.this.getWindow().setAttributes(lp);
                            }
                        });
                        shareDialog.show();
                    }
                    break;
                case R.id.faxian_layout:
                case R.id.weixin_layout:
                case R.id.weixin_pengyouquan_layout:
                case R.id.qq_layout:
                    if (shareDialog != null) {
                        shareDialog.dismiss();
                        shareDialog = null;
                    }

                    shareId = v.getId();
                    Log.e(TAG, "select id : " + list.get(selectPosition).getId());

                    if (SportsApp.getInstance().isOpenNetwork()) {
                        if (selectPosition >= 0) {
                            if (list.get(selectPosition).getId() < 0) {
                                Toast.makeText(BuShuTongJiBarChart.this, "该记录还未上传，请先上传再分享", Toast.LENGTH_SHORT).show();
                            } else {
                                if (mLoadProgressDialog == null) {
                                    mLoadProgressDialog = new Dialog(BuShuTongJiBarChart.this, R.style.sports_dialog);
                                    LayoutInflater mInflater = BuShuTongJiBarChart.this.getLayoutInflater();
                                    View v1 = mInflater.inflate(R.layout.bestgirl_progressdialog, null);
                                    mDialogMessage = (TextView) v1.findViewById(R.id.message);
                                    mDialogMessage.setText(R.string.bestgirl_wait);
                                    v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
                                    mLoadProgressDialog.setContentView(v1);
                                }

                                mLoadProgressDialog.show();
                                new GetShareData().execute();
                            }
                        }
                    } else {
                        Toast.makeText(BuShuTongJiBarChart.this, getString(R.string.network_not_avaliable),
                                Toast.LENGTH_SHORT).show();
                    }

                    break;
            }
        }
    }

    private class GetShareData extends AsyncTask<Void, Void, ApiMessage> {

        @Override
        protected ApiMessage doInBackground(Void... params) {
            // TODO Auto-generated method stub

            return ApiJsonParser.GetShareData(list.get(selectPosition).getId());
        }

        @Override
        protected void onPostExecute(ApiMessage result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            if (result != null) {
                try {
                    JSONObject obj = new JSONObject(result.getMsg()).getJSONObject("data");
                    if (obj != null) {
                        Log.e(TAG, "flag : " + obj.getInt("flag"));

                        if (obj.getInt("flag") == 0) {
                            String imgurl = obj.getString("imgurl");
                            Log.e(TAG, "imgurl : " + imgurl);

                            Message msgLocal = updateHandler.obtainMessage();
                            msgLocal.what = SHARE_RESULT;
                            msgLocal.obj = imgurl;
                            msgLocal.sendToTarget();
                        }
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Log.e(TAG, "JSONException : " + e.toString());
                }
            }
        }
    }

    private class GetAllDayBuShuFromNetwork extends AsyncTask<Void, Void, ApiMessage> {

        @Override
        protected ApiMessage doInBackground(Void... params) {
            // TODO Auto-generated method stub
            return ApiJsonParser.GetAllDayBuShu(SportsApp.getInstance().getSessionId(), page);
        }

        @Override
        protected void onPostExecute(ApiMessage result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            if (result != null) {
                try {
                    JSONObject obj = new JSONObject(result.getMsg()).getJSONObject("data");
                    if (0 == obj.getInt("flag")) {
                        if (0 == listNum) {
                            listNum = obj.getInt("listnum");
                        }

                        JSONArray jsonArray = obj.getJSONArray("data");

                        Log.e(TAG, "listNum : " + listNum + ", jsonArray length : " + jsonArray.length());
                        listNum -= jsonArray.length();

                        BuShuTongJiDetail detail;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            obj = jsonArray.getJSONObject(i);
                            detail = new BuShuTongJiDetail(obj.getInt("uid"), obj.getInt("id"), obj.getInt("step_num"),
                                    obj.getDouble("distance"), obj.getDouble("step_Calorie"),
                                    obj.getString("day"), 1);

                            int saveResult = saveBuShuTongJiToLocal(detail);
                            Log.e(TAG, "saveResult : " + saveResult);
                        }
                    } else {
                        page = 0;

                        Message msg = updateHandler.obtainMessage();
                        msg.what = GET_FROM_LOCAL;
                        msg.sendToTarget();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Log.e(TAG, "JSONException : " + e.toString());
                }
            }

            if (listNum > 0) {
                page++;

                Message msg = updateHandler.obtainMessage();
                msg.what = GET_FROM_NETWORK;
                msg.sendToTarget();
            } else {
                page = 0;

                Message msg = updateHandler.obtainMessage();
                msg.what = GET_FROM_LOCAL;
                msg.sendToTarget();
            }
        }
    }

    private int saveBuShuTongJiToLocal(BuShuTongJiDetail detail) {

        int saveResult = 0;

        if (detail == null) {
            Log.e(TAG, "detail is null");
            return -1;
        }

        db = BuShuTongJiDB.getInstance(BuShuTongJiBarChart.this);

        Cursor cursor = db.query(detail.getUid(), detail.getDay());
        ContentValues values = new ContentValues();
        values.put(BuShuTongJiDB.UID_I, detail.getUid());
        values.put(BuShuTongJiDB.ID_I, detail.getId());
        values.put(BuShuTongJiDB.STEP_NUM_I, detail.getStep_num());
        values.put(BuShuTongJiDB.DISTANCE_D, detail.getDistance());
        values.put(BuShuTongJiDB.STEP_CALORIE_D, detail.getStep_Calorie());
        values.put(BuShuTongJiDB.DAY_S, detail.getDay());
        values.put(BuShuTongJiDB.IS_UPLOAD_I, detail.getIs_upload());

        if (cursor != null) {
            if (!cursor.moveToFirst()) {
                saveResult = db.insert(values, false);
            } else {
                if (detail.getStep_num() >= cursor.getInt(cursor.getColumnIndex(BuShuTongJiDB.STEP_NUM_I))) {
                    saveResult = db.update(values, detail.getUid(), detail.getDay(), false);
                }
            }
        } else {
            saveResult = db.insert(values, false);
        }

        if (cursor != null) {
            cursor.close();
            cursor = null;
        }

        Log.e(TAG, "save data to local : " + saveResult);
        return saveResult;
    }

    private Handler updateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case GET_FROM_LOCAL:
                    db = BuShuTongJiDB.getInstance(BuShuTongJiBarChart.this);
                    ArrayList<BuShuTongJiDetail> localList = db.getTasksList(SportsApp.getInstance().getSportUser().getUid(), page);
                    if ((localList != null) && (localList.size() > 0)) {
                        for (int i = 0; i < localList.size(); i++) {
                            list.add(0, localList.get(i));
                        }

                        page++;
                        haveData = true;
//                        Message msgLocal = updateHandler.obtainMessage();
//                        msgLocal.what = GET_FROM_LOCAL;
//                        msgLocal.sendToTarget();
                    } else {
                        haveData = false;
                    }

                    int size = list.size();
                    if (size == 0) {
                        Date dateTemp = new Date(System.currentTimeMillis());
                        SimpleDateFormat sdfTemp = new SimpleDateFormat("yyyy-MM-dd");

                        list.add(new BuShuTongJiDetail(SportsApp.getInstance().getSportUser().getUid(),
                                -1, 0, 0, 0, sdfTemp.format(dateTemp), 0));
                        size++;
                    }

                    if (size < 7) {
                        for (int i = size; i < 4; i++) {
                            list.add(0, new BuShuTongJiDetail(SportsApp.getInstance().getSportUser().getUid(),
                                    -1, 0, 0, 0, null, 0));
                        }
                    }

                    if (selectPosition == -1) {
                        for (int i = 0; i < 3; i++) {
                            list.add(new BuShuTongJiDetail(SportsApp.getInstance().getSportUser().getUid(),
                                    -1, 0, 0, 0, null, 0));
                        }

                        selectPosition = list.size() - 4;
                    }

                    Message msgLocal = updateHandler.obtainMessage();
                    msgLocal.what = OUTPUT_VIEW;
                    msgLocal.sendToTarget();

                    break;
                case GET_FROM_NETWORK:
                    if (SportsApp.getInstance().isOpenNetwork() && (SportsApp.getInstance().getSportUser().getUid() > 0)) {
                        new GetAllDayBuShuFromNetwork().execute();
                    } else {
                        Message msgN = updateHandler.obtainMessage();
                        msgN.what = GET_FROM_LOCAL;
                        msgN.sendToTarget();
                    }
                    break;
                case UPDATE_DATA:
                    mAdapter.notifyDataSetChanged();

                    tv_steps.setText("" + ((BuShuTongJiDetail) msg.obj).getStep_num());
                    tv_juli.setText("" + new DecimalFormat("#.##").format(((BuShuTongJiDetail) msg.obj).getDistance()) + "Km");
                    tv_reliang.setText("" + (int) ((BuShuTongJiDetail) msg.obj).getStep_Calorie() + "Cal");
                    break;
                case OUTPUT_VIEW:
                    mAdapter.notifyDataSetChanged();
                    if (fromGetView) {
                        task = new TimerTask() {
                            public void run() {
                                Log.e("develop_debug", "OUTPUT_VIEW run");

                                    Message msgLocal = updateHandler.obtainMessage();
                                    msgLocal.what = OUTPUT_VIEW_TIMER;
                                    msgLocal.sendToTarget();

                                    if (task != null) {
                                        task.cancel();
                                    }

                                    if (timer != null) {
                                        timer.cancel();
                                    }
                                }
                        };

                        timer = new Timer(true);
                        timer.schedule(task, 20, 3000);
                    }
                    fromOutView = true;
                    tv_steps.setText("" + list.get(selectPosition).getStep_num());
                    tv_juli.setText("" + new DecimalFormat("#.##").format(list.get(selectPosition).getDistance()) + "Km");
                    tv_reliang.setText("" + (int) list.get(selectPosition).getStep_Calorie() + "Cal");
                    break;
                case OUTPUT_VIEW_TIMER:
                    listview.scrollTo(listview.getWidth() * 11 / listview.getChildCount());
                    fromGetView = false;
                    break;
                case SHARE_RESULT:
                    String msgUrl = (String) msg.obj;
                    Log.e(TAG, "msgUrl : " + msgUrl);
                    new goShare().execute(msgUrl);

                    break;
                case SAVE_TO_LOCAL:
                    int uid = SportsApp.getInstance().getSportUser().getUid();
                    int height = SportsApp.getInstance().getSportUser().getHeight();
                    if (height <= 0) {
                        height = 170;
                    }
                    int steps = BuShuTongJiBarChart.this.getIntent().getIntExtra("steps", 0);
                    double gongli = height * 0.45 / 100000 * steps;
                    double qianka = BuShuTongJiBarChart.this.getIntent().getDoubleExtra("calories", 0);

                    Date dateSaveToLocal = new Date(System.currentTimeMillis());
                    SimpleDateFormat formatterSaveToLocal = new SimpleDateFormat("yyyy-MM-dd");
                    String formatDateSaveToLocal = formatterSaveToLocal.format(dateSaveToLocal);

                    today = formatDateSaveToLocal;
                    yestoday = formatterSaveToLocal.format(new Date(System.currentTimeMillis() - 24 * 3600 * 1000));
                    ;

                    BuShuTongJiDetail detail = new BuShuTongJiDetail(uid, -1, steps, gongli, qianka, formatDateSaveToLocal, 0);

                    saveBuShuTongJiToLocal(detail);

                    if (SportsApp.getInstance().isOpenNetwork() && (SportsApp.getInstance().getSportUser().getUid() > 0)) {
                        Message msgL = updateHandler.obtainMessage();
                        msgL.what = SAVE_TO_NETWORK;
                        msgL.sendToTarget();
                    } else {
                        Message msgL = updateHandler.obtainMessage();
                        msgL.what = GET_FROM_LOCAL;
                        msgL.sendToTarget();
                    }
                    break;
                case SAVE_TO_NETWORK:
                    if (SportsApp.getInstance().isOpenNetwork() && (SportsApp.getInstance().getSportUser().getUid() > 0)) {
                        new saveBuShuTongJiToNetWork().execute();
                    }
            }
        }
    };

    private class saveBuShuTongJiToNetWork extends AsyncTask<Void, Void, ApiMessage> {
        private BuShuTongJiDetail detail;

        @Override
        protected ApiMessage doInBackground(Void... params) {
            // TODO Auto-generated method stub
            db = BuShuTongJiDB.getInstance(BuShuTongJiBarChart.this);
            detail = db.getUnUploadTask(SportsApp.getInstance().getSportUser().getUid());
            if (detail != null) {
                return ApiJsonParser.saveBuShuTongJiToNetWork(SportsApp.getInstance().getSessionId(),
                        detail.getStep_num(), detail.getDay(),
                        "z" + getResources().getString(R.string.config_game_id));
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ApiMessage result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            if (result != null && result.getMsg() != null) {
                Log.e(TAG, "result : " + result.getMsg().toString());

                try {
                    JSONObject obj = new JSONObject(result.getMsg()).getJSONObject("data");
                    if ((obj != null) && (obj.getInt("flag") == 0)) {
                        Log.e(TAG, "save steps to network success");
                        detail.setIs_upload(1);
                        detail.setId(obj.getInt("id"));
                        saveBuShuTongJiToLocal(detail);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Log.e(TAG, "saveBuShuTongJiToNetWork JSONException : " + e.toString());
                }
            }

            Message msg = updateHandler.obtainMessage();
            msg.what = GET_FROM_NETWORK;
            msg.sendToTarget();
        }
    }

    private class goShare extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            boolean saveFile = false;

            if (mSportsApp.isOpenNetwork()) {
                Tools.delAllFile(SportsUtilities.DOWNLOAD_SAVE_PATH);

                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    Log.e(TAG, "get map screen shot");

                    mDownloader = ImageDownloader.getInstance();
                    mDownloader.setType(ImageDownloader.DEFAULT);
                    Bitmap bitmap = mDownloader.downloadBitmap(params[0]);
                    if (bitmap != null) {
                        saveFile = Tools.SaveBitmapAsFile(SHARE_PATH, bitmap, 90);
                    }
                } else {
                    Toast.makeText(BuShuTongJiBarChart.this, getString(R.string.sd_card_is_invalid),
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(BuShuTongJiBarChart.this, getString(R.string.network_not_avaliable),
                        Toast.LENGTH_SHORT).show();
            }

            return saveFile;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            if (mLoadProgressDialog != null) {
                if (mLoadProgressDialog.isShowing()) {
                    mLoadProgressDialog.dismiss();
                }
            }

            if (result) {
                switch (shareId) {
                    case R.id.faxian_layout:
                        if (Bimp.drr.size() < 9) {
                            Bimp.drr.add(SHARE_PATH);
                            Log.e(TAG, "SHARE_PATH : " + SHARE_PATH);
                        }
                        Intent intent = new Intent(BuShuTongJiBarChart.this, FindFriendsSendMsg.class);
                        startActivity(intent);
                        break;
                    case R.id.weixin_layout:
                        shareToWeixinHaoYou();
                        break;
                    case R.id.weixin_pengyouquan_layout:
                        shareToWeixin();
                        break;
                    case R.id.qq_layout:
                        shareToQQ();
                        break;
                }
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null != mTencent)
            mTencent.onActivityResultData(requestCode, resultCode, data, this);
    }

    private void shareToQQ() {
        mTencent = Tencent.createInstance("1101732794", this.getApplicationContext());
        Bundle params = new Bundle();
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, SHARE_PATH);
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
        mTencent.shareToQQ(this, params, this);
    }

    private void shareToWeixin() {
        api = WXAPIFactory.createWXAPI(BuShuTongJiBarChart.this,
                WXEntryActivity.APP_ID, true);
        api.registerApp(WXEntryActivity.APP_ID);

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

        WXImageObject imgObj = new WXImageObject();
        imgObj.setImagePath(SHARE_PATH);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;
        msg.description = "图片描述";

        Bitmap bmp = BitmapFactory.decodeFile(SHARE_PATH);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 144, 246, true);
        bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
        msg.title = "云狐运动";
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "img" + String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);
    }

    private void shareToWeixinHaoYou() {
        api = WXAPIFactory.createWXAPI(BuShuTongJiBarChart.this,
                WXEntryActivity.APP_ID, true);
        api.registerApp(WXEntryActivity.APP_ID);

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

        WXImageObject imgObj = new WXImageObject();
        imgObj.setImagePath(SHARE_PATH);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;
        msg.description = "图片描述";

        Bitmap bmp = BitmapFactory.decodeFile(SHARE_PATH);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 144, 246, true);
        bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
        msg.title = "云狐运动";
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "img" + String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);
    }

    private String getFormatedDate(String date) {
        if (date == null) {
            return "";
        } else if (date.equalsIgnoreCase(today)) {
            return "今天";
        } else if (date.equalsIgnoreCase(yestoday)) {
            return "昨天";
        } else {
            String temp = date.replace('-', '/');
            return temp.substring(temp.indexOf("/") + 1);
        }
    }

    @Override
    public void onPageResume() {
        // TODO Auto-generated method stub
    }

    @Override
    public void onPagePause() {
        // TODO Auto-generated method stub
    }

    @Override
    public void onPageDestroy() {
        // TODO Auto-generated method stub
        if (db != null) {
            db.close();
            db = null;
        }
    }

    @Override
    public void onCancel() {
        // TODO Auto-generated method stub
//        Toast.makeText(this, "分享取消", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onComplete(Object arg0) {
        // TODO Auto-generated method stub
        Toast.makeText(this, "QQ分享成功", Toast.LENGTH_SHORT).show();

        new AddCoinsThread(10, 4, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method
                // stub
                switch (msg.what) {
                    case ApiConstant.COINS_SUCCESS:
                        SportTaskUtil.jump2CoinsDialog(
                                BuShuTongJiBarChart.this,
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
    }

    @Override
    public void onError(UiError arg0) {
        // TODO Auto-generated method stub
        Toast.makeText(this, "分享失败", Toast.LENGTH_LONG).show();
    }
}