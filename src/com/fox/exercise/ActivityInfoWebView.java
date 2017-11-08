package com.fox.exercise;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Message;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fox.exercise.ActivityInfoWebView.activityinfoAdapter.ViewHoder;
import com.fox.exercise.api.ApiBack;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.api.entity.ActionList;
import com.fox.exercise.api.entity.ActivityInfo;
import com.fox.exercise.api.entity.ExpressionItems;
import com.fox.exercise.api.entity.UserDetail;
import com.fox.exercise.bitmap.util.ImageResizer;
import com.fox.exercise.newversion.act.PersonalPageMainActivity;
import com.fox.exercise.newversion.act.ShareActivity;
import com.fox.exercise.newversion.entity.FindGroup;
import com.fox.exercise.publish.SendMsgDetail;
import com.fox.exercise.util.RoundedImage;
import com.fox.exercise.view.PullToRefreshBase.OnRefreshListener;
import com.fox.exercise.view.PullToRefreshListView;
import com.fox.exercise.widght.expandablelayout.ExpandableLayout;
import com.umeng.analytics.MobclickAgent;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.ingenic.indroidsync.SportsApp;

public class ActivityInfoWebView extends AbstractBaseActivity {

    private FindOtherMoreAdapter sportsfindmoreAdapter = null;
    private List<ExpressionItems> imgItems;
    private List<ExpressionItems> imgItems2;
    private String[] imgStr;
    private String[] imgStr2;
    private ArrayList<FindGroup> mList2 = new ArrayList<FindGroup>();
    private int isshow = 1;
    private LinearLayout mContentLayout;
    private SportsApp mSportsApp;
    // private ImageButton iView;
    private PopupWindow popupWindow;
    private TextView apply_bt;
    private String URL;
    private String thrURL = "";
    private String th;
    private String onclicktime;
    private long startTimeSeconds;
    private int mItemHeight = 0;
    private SportsApp msApp;
    private SendMsgDetail self = null;
    private UserDetail detail;
    ViewHoder holder;
    private List<ImageView> imgList2 = new ArrayList<ImageView>();
    private List<ImageView> imgList3 = new ArrayList<ImageView>();
    private List<ImageView> imgList4 = new ArrayList<ImageView>();
    private List<ImageView> imgList5 = new ArrayList<ImageView>();
    private List<ImageView> imgList6 = new ArrayList<ImageView>();
    private List<ImageView> imgList7 = new ArrayList<ImageView>();
    private List<ImageView> imgList8 = new ArrayList<ImageView>();
    private List<ImageView> imgList9 = new ArrayList<ImageView>();
    private LayoutInflater mInflater = null;
    private ImageDownloader mDownloader = null;
    private ImageDownloader mDownloader2 = null;
    int type;
    private List<String> imgurList = new ArrayList<String>();
    private String activitytime;
    private int activityId;
    private ActivityInfo activityDetailInfo = null;
    private ImageDownloader mDownload = ImageDownloader.getInstance();
    private List<activityinfo2> mList = new ArrayList<activityinfo2>();
    private TextView content, content2;
    private TextView contentDetail, contentDetail2;
    private View contentView5;
    private String activityTime_2;
    private TextView contentWay, contentWay2;
    // private TextView contentTotal;
    // private TextView contentTotal2;
    // private ImageView iv_shouqi,iv_zhankai;
    private ImageResizer mImageWorker;
    private TextView description, description2;
    private TextView action_url;
    private ImageView activityDetail_pic;
    private Date curDate, activityDate;
    private SimpleDateFormat formatter;
    private ImageView reward_pic;
    private PullToRefreshListView actiondetailListView = null;
    private ListView listView = null;
    private FrameLayout fl_view, fl_view2;
    private int times = 0;
    private View head_view, weiView;
    private List<ActionList> actionLists = new ArrayList<ActionList>();
    private List<String> activityNameList = new ArrayList<String>();
    private List<String> activityTimeList = new ArrayList<String>();
    private List<Integer> activityIdList = new ArrayList<Integer>();
    private List<Integer> activitySendId = new ArrayList<Integer>();
    private List<Integer> activityNameSize = new ArrayList<Integer>();
    private List<activityinfo2> list = new ArrayList<activityinfo2>();
    private View contentView1;
    private View contentViewWay;
    // private View contentView4;
    // private View contentView3;
    private View contentViewDescription;
    private ExpandableLayout itemReward;
    private RelativeLayout rl_joinactivity;
    private ImageView img_share;
    private String title_name;
    private Dialog mLoadProgressDialog;
    private TextView mDialogMessage;

    @Override
    public void initIntentParam(Intent intent) {
        // TODO Auto-generated method stub
        title = getIntent().getStringExtra("title_name");
        title_name = getIntent().getStringExtra("title_name");
//		if (title.length() >= 15) {
//			title = "        "+title;
//		}
        URL = getIntent().getStringExtra("action_url");
        th = getIntent().getStringExtra("thrurl");
        if (th != null && th.length() != 0) {
            thrURL = th;
        }
        activityId = getIntent().getIntExtra("activity_id", 0);
        activitytime = getIntent().getStringExtra("activitytime");
        // Log.i("pppaaa", "接收到的----------------------888" +
        // activitytime.toString());
    }

    @Override
    public void initView() {
        msApp = (SportsApp) getApplication();
        mSportsApp = SportsApp.getInstance();
        imgStr = getResources().getStringArray(R.array.imageStr_array);
        imgStr2 = getResources().getStringArray(R.array.daka_array);
        imgItems = new ArrayList<ExpressionItems>();
        imgItems2 = new ArrayList<ExpressionItems>();
        int j = 0,k = 0;
        Field[] files1 = R.drawable.class.getDeclaredFields();//打卡的图片列表
        for (Field file : files1) {
            if (file.getName().startsWith("daka")) {
                ApplicationInfo appInfo = getApplicationInfo();
                int resID = getResources().getIdentifier(file.getName(),
                        "drawable", appInfo.packageName);
                ExpressionItems item = new ExpressionItems();
                item.setId(resID);
                item.setName(imgStr2[k].toString());
                item.setIsCanel(false);
                imgItems2.add(item);
                k++;
                // System.out.println(file.getName());
            }
        }
        Field[] files = R.drawable.class.getDeclaredFields();
        for (Field file : files) {
            if (file.getName().startsWith("biaoqing_")) {
                if (((imgItems.size() + 1) % 21) == 0) {
                    ExpressionItems item = new ExpressionItems();
                    item.setId(R.drawable.qita_biaoqing_01);
                    item.setName("itemCanel");
                    item.setIsCanel(true);
                    imgItems.add(item);
                }
                ApplicationInfo appInfo = ActivityInfoWebView.this
                        .getApplicationInfo();
                int resID = getResources().getIdentifier(file.getName(),
                        "drawable", appInfo.packageName);
                ExpressionItems item = new ExpressionItems();
                item.setId(resID);
                item.setName(imgStr[j].toString());
                item.setIsCanel(false);
                imgItems.add(item);
                j++;
                // System.out.println(file.getName());
            }
        }

        mInflater = (LayoutInflater) ActivityInfoWebView.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // 头像
        this.mDownloader = new ImageDownloader(ActivityInfoWebView.this);
        mDownloader.setType(ImageDownloader.ICON);
        // 一张图像
        this.mDownloader2 = new ImageDownloader(ActivityInfoWebView.this);
        mDownloader2.setType(ImageDownloader.OnlyOne);
        showContentView(R.layout.activity_listview);
        rl_joinactivity = (RelativeLayout) findViewById(R.id.rl_infowebview);
        rl_joinactivity.setAlpha(+255);
        mItemHeight = Util.getRealPixel_W(ActivityInfoWebView.this,
                (int) (SportsApp.ScreenWidth * 0.625) / 3);
        mImageWorker = mSportsApp.getImageWorker(ActivityInfoWebView.this,
                mItemHeight, mItemHeight);
        actiondetailListView = (PullToRefreshListView) findViewById(R.id.activity_list);
        head_view = LayoutInflater.from(ActivityInfoWebView.this).inflate(
                R.layout.activity_infowebview, null);
        weiView = LayoutInflater.from(ActivityInfoWebView.this).inflate(
                R.layout.activity_wei, null);
        curDate = new Date(System.currentTimeMillis());
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        // 申请报名点击实现
        // apply_bt = (Button)head_view.findViewById(R.id.detail_bt);
        apply_bt = (TextView) findViewById(R.id.tv_join);
        fl_view = (FrameLayout) head_view.findViewById(R.id.fl_view);
        fl_view2 = (FrameLayout) findViewById(R.id.fl_view2);
        apply_bt.setVisibility(View.VISIBLE);
        fl_view.setVisibility(View.VISIBLE);
        img_share = new ImageView(getApplication());
        img_share.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        img_share.setImageResource(R.drawable.bushutongji_fenxiang);

        showRightBtn(img_share);
        right_btn.setOnClickListener(new ActionInfoShareLister());
        // if (thrURL !=null && "".equals(thrURL)) {
        // apply_bt.setVisibility(View.GONE);
        // fl_view.setVisibility(View.GONE);
        // }else {
        // apply_bt.setVisibility(View.VISIBLE);
        // fl_view.setVisibility(View.VISIBLE);
        // }
        try {
            if (activitytime != null) {
                activityDate = formatter.parse(activitytime);
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (curDate.after(activityDate)) {
            apply_bt.setText("已结束");
            apply_bt.setTextColor(Color.parseColor("#DCDCDC"));
        } else {
            apply_bt.setText("立即报名");
            apply_bt.setTextColor(Color.parseColor("#FFFFFF"));
        }
        apply_bt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (mSportsApp.isOpenNetwork()) {
                    if (curDate.after(activityDate)) {
                        Toast.makeText(ActivityInfoWebView.this,
                                getString(R.string.activity_end),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Intent activityIntent = new Intent(
                            ActivityInfoWebView.this, FindFriendsSendMsg.class);
                    activityIntent.putExtra("title_name", title);
                    activityIntent.putExtra("activity_id", activityId);
                    // Toast.makeText(ActivityInfoWebView.this,
                    // "这是"+title+"活动！", 1).show();
                    startActivity(activityIntent);
                } else {
                    Toast.makeText(ActivityInfoWebView.this,
                            getString(R.string.network_not_avaliable),
                            Toast.LENGTH_SHORT).show();
                }

                // // TODO Auto-generated method stub
                // new AsyncTask<Void, Void, ApiBack>() {
                //
                // @Override
                // protected ApiBack doInBackground(Void... arg0) {
                // // TODO Auto-generated method stub
                // ApiBack back = null;
                // try {
                // back = (ApiBack) ApiJsonParser.signupAction(
                // mSportsApp.getSessionId(),
                // mSportsApp.getSportUser().getUid(),activityId );
                // } catch (ApiNetException e) {
                // e.printStackTrace();
                // }
                // return back;
                //
                // }
                // @Override
                // protected void onPostExecute(ApiBack result) {
                // if (result == null || result.getFlag() != 0) {
                // // 报名活动失败
                // Toast.makeText(ActivityInfoWebView.this,
                // result.getMsg(),
                // Toast.LENGTH_SHORT).show();
                // } else {
                // Toast.makeText(ActivityInfoWebView.this,
                // "该活动报名成功",
                // Toast.LENGTH_SHORT).show();
                // }
                // }
                //
                // }.execute();
            }
        });

		/*
         * //活动详情界面右上角分享Button iView=new ImageButton(this);
		 * iView.setBackgroundResource(R.drawable.add);
		 * iView.setLayoutParams(new
		 * LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		 * showRightBtn(iView); //right_btn.setPadding(0, 0, 0, 0);
		 * iView.setOnClickListener(new OnClickListener() {
		 *
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub getPopupWindow(); popupWindow.showAsDropDown(v); } });
		 */

        // 活动详情界面显示
        GetActivityInfoTask activityInfoTask = new GetActivityInfoTask();
        activityInfoTask.execute("");
        mContentLayout = (LinearLayout) head_view
                .findViewById(R.id.linearlayout_content);
        activityDetail_pic = (ImageView) head_view
                .findViewById(R.id.detail_pic);

        leftButton.setBackgroundResource(R.drawable.sport_title_back_selector);

        // add expand layout
        // final ExpandableLayout itemTime = new ExpandableLayout(this);
        contentView1 = View.inflate(this, R.layout.content_activity, null);
        content = (TextView) contentView1.findViewById(R.id.tv_actiondetail2);
        content2 = (TextView) contentView1.findViewById(R.id.tv_actiondetail1);
        // itemTime.initView("活动时间:", contentView1);
        contentView1.setVisibility(View.VISIBLE);
        mContentLayout.addView(contentView1);

        // ExpandableLayout itemContent= new ExpandableLayout(this);
        View contentView2 = View.inflate(this, R.layout.content_activity, null);
        contentDetail = (TextView) contentView2
                .findViewById(R.id.tv_actiondetail2);
        contentDetail2 = (TextView) contentView2
                .findViewById(R.id.tv_actiondetail1);
        // itemContent.initView("活动内容:", contentView2);
        // itemContent.setVisibility(View.GONE);
        mContentLayout.addView(contentView2);

        // 第三方活动的参加网址放置地点
        apply_bt.setVisibility(View.VISIBLE);
        fl_view.setVisibility(View.VISIBLE);
        if (thrURL.length() != 0) {
            // if (apply_bt!=null) {
            // apply_bt.setVisibility(View.INVISIBLE);
            // }
            // 新加的显示全部点击按钮
            final View contentView_huodong = View.inflate(this,
                    R.layout.huodongurl, null);
            action_url = (TextView) contentView_huodong
                    .findViewById(R.id.tv_huodongurl);
            mContentLayout.addView(contentView_huodong);
            SpannableString ss = new SpannableString("点击查看：" + thrURL);
            if (ss.length() >= 5) {
                ss.setSpan(new URLSpan(thrURL), 5, thrURL.length() + 5,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                action_url.setText(ss);
                action_url.setMovementMethod(LinkMovementMethod.getInstance());
                action_url.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        startTimeSeconds = System.currentTimeMillis();
                        Date startDate2 = new Date(startTimeSeconds);
                        SimpleDateFormat formatter2 = new SimpleDateFormat(
                                "yyyy-MM-dd HH:mm:ss");
                        onclicktime = formatter2.format(startDate2);
                        healthCount();
                    }
                });
            }
        }

        // //新加的显示全部点击按钮
        // contentView3 = View.inflate(this, R.layout.xianshiquanbu,null);
        // iv_zhankai = (ImageView)
        // contentView3.findViewById(R.id.tv_xianshiquanbu);
        // mContentLayout.addView(contentView3);

        // final ExpandableLayout itemWay = new ExpandableLayout(this);
        contentViewWay = View.inflate(this, R.layout.content_activity, null);
        contentWay = (TextView) contentViewWay
                .findViewById(R.id.tv_actiondetail2);
        contentWay2 = (TextView) contentViewWay
                .findViewById(R.id.tv_actiondetail1);
        // itemWay.initView("参与方式:", contentViewWay);
        contentViewWay.setVisibility(View.VISIBLE);
        mContentLayout.addView(contentViewWay);

        itemReward = new ExpandableLayout(this);
        View contentViewReward = View.inflate(this,
                R.layout.view_content_picture, null);
        reward_pic = (ImageView) contentViewReward
                .findViewById(R.id.reward_img);
        itemReward.initView("活动奖励:", contentViewReward);
        itemReward.setVisibility(View.VISIBLE);
        mContentLayout.addView(itemReward);

        final ExpandableLayout itemDescription = new ExpandableLayout(this);
        contentViewDescription = View.inflate(this, R.layout.content_activity,
                null);
        description = (TextView) contentViewDescription
                .findViewById(R.id.tv_actiondetail2);
        description2 = (TextView) contentViewDescription
                .findViewById(R.id.tv_actiondetail1);
        // itemDescription.initView("活动说明:", contentViewDescription);
        contentViewDescription.setVisibility(View.VISIBLE);
        mContentLayout.addView(contentViewDescription);

        // 收起全部内容
        // contentView4 = View.inflate(this, R.layout.xianshiquanbu,null);
        // iv_shouqi = (ImageView)
        // contentView4.findViewById(R.id.tv_xianshiquanbu);
        // contentView4.setVisibility(View.GONE);
        // mContentLayout.addView(contentView4);

        // 动态
        contentView5 = View.inflate(this, R.layout.dongtai_activity, null);
        mContentLayout.addView(contentView5);

        // ExpandableLayout itemActionDetial = new ExpandableLayout(this);
        // View contentViewItem = View.inflate(this,
        // R.layout.activity_listview,null);
        // actiondetailListView =
        // (OtPullToRefreshListView)contentViewItem.findViewById(R.id.activity_list);
        // itemActionDetial.initView("参与详情:", contentViewItem);
        // mContentLayout.addView(itemActionDetial);

        // 为显示全部设置监听
        // contentView3.setOnClickListener(new OnClickListener() {
        //
        // @Override
        // public void onClick(View v) {
        // // TODO Auto-generated method stub
        // contentView1.setVisibility(View.VISIBLE);
        // contentViewWay.setVisibility(View.VISIBLE);
        // itemReward.setVisibility(View.VISIBLE);
        // contentViewDescription.setVisibility(View.VISIBLE);
        // contentView3.setVisibility(View.GONE);
        // contentView4.setVisibility(View.VISIBLE);
        // }
        // });
        // contentView4.setOnClickListener(new OnClickListener() {
        //
        // @Override
        // public void onClick(View v) {
        // // TODO Auto-generated method stub
        // contentView1.setVisibility(View.GONE);
        // contentViewWay.setVisibility(View.GONE);
        // itemReward.setVisibility(View.GONE);
        // contentViewDescription.setVisibility(View.GONE);
        // contentView4.setVisibility(View.GONE);
        // contentView3.setVisibility(View.VISIBLE);
        // }
        // });
        new GetActionDataTask().execute();
        new GetActionDataTask2().execute();
        new GetActionDataDetailTask().execute();
    }

    private void waitShowDialog() {
        // TODO Auto-generated method stub
        if (mLoadProgressDialog == null) {
            mLoadProgressDialog = new Dialog(ActivityInfoWebView.this,
                    R.style.sports_dialog);
            LayoutInflater mInflater = ActivityInfoWebView.this.getLayoutInflater();
            View v1 = mInflater.inflate(R.layout.bestgirl_progressdialog, null);
            mDialogMessage = (TextView) v1.findViewById(R.id.message);
            mDialogMessage.setText(R.string.bestgirl_wait);
            v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
            mLoadProgressDialog.setContentView(v1);
        }
        if (mLoadProgressDialog != null)
            if (!mLoadProgressDialog.isShowing()
                    && !ActivityInfoWebView.this.isFinishing())
                mLoadProgressDialog.show();
        Log.i(TAG, "isFirstshow----");
    }

    @Override
    public void setViewStatus() {
        // TODO Auto-generated method stub
        // actiondetailListView.addHeaderView(head_view);
        listView = actiondetailListView.getRefreshableView();
        listView.addHeaderView(head_view);
        listView.addFooterView(weiView);
        listView.setDivider(null);

        actiondetailListView.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh(int pullDirection) {
                if (mSportsApp.isOpenNetwork()) {
                    switch (pullDirection) {
                        // case FansAndNear.MODE_DEFAULT_LOAD:
                        case FansAndNear.MODE_DEFAULT_LOAD:
                            times++;
                            // SportsFindMoreThread loadThread = new
                            // SportsFindMoreThread();
                            // loadThread.start();
                            GetActionData();
                            break;
                        // case FansAndNear.MODE_PULL_DOWN_TO_REFRESH:
                        case FansAndNear.MODE_PULL_DOWN_TO_REFRESH:
                            times = 0;
                            // SportsFindMoreThread refreshThread = new
                            // SportsFindMoreThread();
                            // refreshThread.start();
                            GetActionData();
                            break;
                    }
                } else {
                    Toast.makeText(
                            ActivityInfoWebView.this,
                            getResources().getString(
                                    R.string.acess_server_error),
                            Toast.LENGTH_SHORT).show();
                    actiondetailListView.onRefreshComplete();
                    if (mLoadProgressDialog != null)
                        if (mLoadProgressDialog.isShowing())
                            mLoadProgressDialog.dismiss();
                    fl_view2.setVisibility(View.VISIBLE);
                }
            }
        });
        waitShowDialog();
        fl_view2.setVisibility(View.INVISIBLE);
        sportsfindmoreAdapter = new FindOtherMoreAdapter(
                ActivityInfoWebView.this, actionLists, mList2, mSportsApp,
                imgItems,imgItems2, 0);
        listView.setAdapter(sportsfindmoreAdapter);
        // activityinfoAdapter adapter = new
        // activityinfoAdapter(ActivityInfoWebView.this, mList);
        // listView.setAdapter(adapter);
    }

    @Override
    public void onPageResume() {
        // TODO Auto-generated method stub

        // TODO Auto-generated method stub
        self = mSportsApp.getmSendMsgDetail();
        detail = SportsApp.getInstance().getSportUser();
        // Log.e(TAG, "self-------------"+self);
        if (self != null) {
            int position = listView.getSelectedItemPosition();
            FindGroup f2 = new FindGroup();
            f2.setOtheruid(detail.getUid());
            f2.setSex(detail.getSex());
            f2.setOthername(detail.getUname());
            f2.setOtherimg(detail.getUimg());
            f2.setDetils(self.getMethod_str());
            String[] imgs = self.getUrls();
            f2.setImgs(imgs);
            f2.setBiggerImgs(self.getBigurls());
            f2.setTimes(self.getTimes() / 1000);
            f2.setFindId(self.getFindId());
            f2.setIsFriends(2);
            if (imgs != null) {
                if (imgs.length == 1) {
                    f2.setWidth(self.getWidth());
                    f2.setHeight(self.getHeight());
                }
            }
            f2.setTopicList(self.getTopicList());
            f2.setComefrom(self.getComeFrom());
            if (isshow != 2) {
                mList2.add(0, f2);
            }
            listView.setSelection(position + 1);
            if (sportsfindmoreAdapter != null) {
                sportsfindmoreAdapter.notifyDataSetChanged();
            }
            actiondetailListView.onRefreshComplete();
            mSportsApp.setmSendMsgDetail(null);
        }
        MobclickAgent.onPageStart("ActivityInfoWebView");
    }

    @Override
    public void onPagePause() {
        // TODO Auto-generated method stub
        MobclickAgent.onPageEnd("ActivityInfoWebView");
    }

    @Override
    public void onPageDestroy() {
        // TODO Auto-generated method stub
        if (mList2 != null) {
            mList2.clear();
            mList2 = null;
        }
        if (mList != null) {
            mList.clear();
            mList = null;
        }
        if (imgList2 != null) {
            imgList2.clear();
            imgList2 = null;
        }
        if (imgList3 != null) {
            imgList3.clear();
            imgList3 = null;
        }
        if (imgList4 != null) {
            imgList4.clear();
            imgList4 = null;
        }
        if (imgList5 != null) {
            imgList5.clear();
            imgList5 = null;
        }
        if (imgList6 != null) {
            imgList6.clear();
            imgList6 = null;
        }
        if (imgList7 != null) {
            imgList7.clear();
            imgList7 = null;
        }
        if (imgList8 != null) {
            imgList8.clear();
            imgList8 = null;
        }
        if (imgList9 != null) {
            imgList9.clear();
            imgList9 = null;
        }
        if (imgurList != null) {
            imgurList.clear();
            imgurList = null;
        }
        if (actionLists != null) {
            actionLists.clear();
            actionLists = null;
        }
        if (activityNameList != null) {
            activityNameList.clear();
            activityNameList = null;
        }
        if (activityTimeList != null) {
            activityTimeList.clear();
            activityTimeList = null;
        }
        if (activityIdList != null) {
            activityIdList.clear();
            activityIdList = null;
        }
        if (activitySendId != null) {
            activitySendId.clear();
            activitySendId = null;
        }
        if (activityNameSize != null) {
            activityNameSize.clear();
            activityNameSize = null;
        }
        if (list != null) {
            list.clear();
            list = null;
        }
        if (mLoadProgressDialog != null)
            if (mLoadProgressDialog.isShowing())
                mLoadProgressDialog.dismiss();
        fl_view2.setVisibility(View.VISIBLE);
        mSportsApp = null;

    }

    /**
     * 创建PopupWindow
     */
    protected void initPopuptWindow() {
        // TODO Auto-generated method stub
        // 获取自定义布局文件activity_popupwindow_left.xml的视图
        View popupWindow_view = getLayoutInflater().inflate(
                R.layout.activity_share_select, null, false);
        Button share_weibo_btn = (Button) popupWindow_view
                .findViewById(R.id.share_weibo_btn);
        share_weibo_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    popupWindow = null;
                }
                Toast.makeText(ActivityInfoWebView.this, "点击分享微博",
                        Toast.LENGTH_LONG).show();
            }
        });

        Button share_tengxun_btn = (Button) popupWindow_view
                .findViewById(R.id.share_tengxun_btn);
        share_tengxun_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    popupWindow = null;
                }
                Toast.makeText(ActivityInfoWebView.this, "点击分享腾讯",
                        Toast.LENGTH_LONG).show();
            }
        });

        Button share_weixin_btn = (Button) popupWindow_view
                .findViewById(R.id.share_weixin_btn);
        share_weixin_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    popupWindow = null;
                }
                Toast.makeText(ActivityInfoWebView.this, "点击微信",
                        Toast.LENGTH_LONG).show();
            }
        });

        Button share_yundong_btn = (Button) popupWindow_view
                .findViewById(R.id.share_yundong_btn);
        share_yundong_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    popupWindow = null;
                }
                Toast.makeText(ActivityInfoWebView.this, "点击运动",
                        Toast.LENGTH_LONG).show();
            }
        });
        popupWindow = new PopupWindow(popupWindow_view, mSportsApp.dip2px(150),
                mSportsApp.dip2px(170), true);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.about));
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
    }

    // private void getPopupWindow() {
    // if (null != popupWindow) {
    // popupWindow.dismiss();
    // return;
    // } else {
    // initPopuptWindow();
    // }
    // }

    class GetActivityInfoTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {

            try {
                activityDetailInfo = ApiJsonParser.getActivityDetailInfo(
                        mSportsApp.getSessionId(), activityId, times);
            } catch (ApiNetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ApiSessionOutException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (activityDetailInfo == null)
                return false;
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                if (!TextUtils.isEmpty(activityDetailInfo.getBiaoti_img()))
                    // iv_shouqi.setImageResource(R.drawable.shouqi_iv);
                    // iv_zhankai.setImageResource(R.drawable.shankai_iv);
                if (activityDetailInfo.getBiaoti_img() != null && activityDetailInfo.getBiaoti_img().length() != 0) {
                    mDownload.download(activityDetailInfo.getBiaoti_img(),
                            activityDetail_pic, null);
                }
                content.setText(activityDetailInfo.getActivity_time());
                content2.setText("活动时间：");
                contentDetail.setText(activityDetailInfo.getContent());
                contentDetail2.setText("活动内容：");
                contentWay.setText(activityDetailInfo.getCanjia_type());
                contentWay2.setText("参与方式：");
                mDownload.download(activityDetailInfo.getJiangli_img(),
                        reward_pic, null);
                description.setText(activityDetailInfo.getAct_explain());
                description2.setText("活动说明：");
                List<activityinfo2> list_2 = null;
                isshow = activityDetailInfo.getIsshow();
                list_2 = activityDetailInfo.getList_catInfo();
                contentView5.setVisibility(View.VISIBLE);
                if (isshow == 2) {
                    // // listView.setVisibility(View.GONE);
                    // contentView5.setVisibility(View.GONE);
                    // // iv_zhankai.setVisibility(View.INVISIBLE);
                    // contentView1.setVisibility(View.VISIBLE);
                    // contentViewWay.setVisibility(View.VISIBLE);
                    // itemReward.setVisibility(View.VISIBLE);
                    // contentViewDescription.setVisibility(View.VISIBLE);
                    // contentView3.setVisibility(View.INVISIBLE);
                    // contentView4.setVisibility(View.INVISIBLE);
                    listView.setVisibility(View.VISIBLE);
                    contentView5.setVisibility(View.GONE);
                    // return;
                } else {
                    listView.setVisibility(View.VISIBLE);
                    contentView5.setVisibility(View.VISIBLE);
                }
                for (activityinfo2 activityinfo2 : list_2) {
                    mList.add(activityinfo2);
                }
                Log.i("ababab", "下载下来传入适配器的数组长度：" + mList.size());
                // activityinfoAdapter adapter = new
                // activityinfoAdapter(ActivityInfoWebView.this, mList);
                // listView.setAdapter(adapter);
                // sportsfindmoreAdapter = new
                // FindOtherMoreAdapter(ActivityInfoWebView.this,actionLists,
                // mList2, mSportsApp, imgItems);
                // listView.setAdapter(sportsfindmoreAdapter);
                sportsfindmoreAdapter.notifyDataSetChanged();
                actiondetailListView.onRefreshComplete();
                if (mLoadProgressDialog != null)
                    if (mLoadProgressDialog.isShowing())
                        mLoadProgressDialog.dismiss();
                fl_view2.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(ActivityInfoWebView.this, "获取活动详情信息失败",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    class activityinfoAdapter extends android.widget.BaseAdapter {
        private Context context;
        private List<activityinfo2> list;

        public activityinfoAdapter(Context context, List<activityinfo2> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return list.get(position).getInputtime();
        }

        @Override
        public int getItemViewType(int position) {
            if (list.get(position).getList() != null) {
                type = list.get(position).getList().size();
            } else {
                type = 0;
            }
            return type;
        }

        @Override
        public int getViewTypeCount() {
            // TODO Auto-generated method stub
            return 10;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            imgurList = list.get(position).getList();
            getItemViewType(position);

            if (convertView == null) {
                holder = new ViewHoder();

                switch (type) {
                    case 0:
                        // convertView = (LinearLayout) mInflater.inflate(
                        // R.layout.sports_find_other_list_item, null);
                        convertView = (LinearLayout) mInflater.inflate(
                                R.layout.base_activity_details_zero_layout, null);
                        // holder.img1 = (ImageView) convertView
                        // .findViewById(R.id.detils_img_one);
                        break;
                    case 1:
                        convertView = (LinearLayout) mInflater.inflate(
                                R.layout.base_activity_details_one_layout, null);
                        holder.iv1 = (ImageView) convertView
                                .findViewById(R.id.detils_img_one);
                        break;
                    case 2:
                        convertView = (LinearLayout) mInflater.inflate(
                                R.layout.base_activity_details_two_layout, null);
                        holder.iv1 = (ImageView) convertView
                                .findViewById(R.id.detils_img_one);
                        holder.iv2 = (ImageView) convertView
                                .findViewById(R.id.detils_img_two);
                        break;
                    case 3:
                        convertView = (LinearLayout) mInflater
                                .inflate(
                                        R.layout.base_activity_details_three_1_layout,
                                        null);
                        holder.iv1 = (ImageView) convertView
                                .findViewById(R.id.detils_img_one);
                        holder.iv2 = (ImageView) convertView
                                .findViewById(R.id.detils_img_two);
                        holder.iv3 = (ImageView) convertView
                                .findViewById(R.id.detils_img_three);
                        break;
                    case 4:
                        convertView = (LinearLayout) mInflater.inflate(
                                R.layout.base_activity_details_four_layout, null);
                        holder.iv1 = (ImageView) convertView
                                .findViewById(R.id.detils_img_one);
                        holder.iv2 = (ImageView) convertView
                                .findViewById(R.id.detils_img_two);
                        holder.iv3 = (ImageView) convertView
                                .findViewById(R.id.detils_img_three);
                        holder.iv4 = (ImageView) convertView
                                .findViewById(R.id.detils_img_four);
                        break;
                    case 5:
                        convertView = (LinearLayout) mInflater.inflate(
                                R.layout.base_activity_details_four_layout, null);
                        holder.iv1 = (ImageView) convertView
                                .findViewById(R.id.detils_img_one);
                        holder.iv2 = (ImageView) convertView
                                .findViewById(R.id.detils_img_two);
                        holder.iv3 = (ImageView) convertView
                                .findViewById(R.id.detils_img_three);
                        holder.iv4 = (ImageView) convertView
                                .findViewById(R.id.detils_img_four);
                        // holder.img5 = (ImageView) convertView
                        // .findViewById(R.id.detils_img_five);
                        break;
                    case 6:
                        convertView = (LinearLayout) mInflater.inflate(
                                R.layout.base_activity_details_four_layout, null);
                        holder.iv1 = (ImageView) convertView
                                .findViewById(R.id.detils_img_one);
                        holder.iv2 = (ImageView) convertView
                                .findViewById(R.id.detils_img_two);
                        holder.iv3 = (ImageView) convertView
                                .findViewById(R.id.detils_img_three);
                        holder.iv4 = (ImageView) convertView
                                .findViewById(R.id.detils_img_four);
                        break;
                    case 7:
                        convertView = (LinearLayout) mInflater.inflate(
                                R.layout.base_activity_details_four_layout, null);
                        holder.iv1 = (ImageView) convertView
                                .findViewById(R.id.detils_img_one);
                        holder.iv2 = (ImageView) convertView
                                .findViewById(R.id.detils_img_two);
                        holder.iv3 = (ImageView) convertView
                                .findViewById(R.id.detils_img_three);
                        holder.iv4 = (ImageView) convertView
                                .findViewById(R.id.detils_img_four);
                        break;
                    case 8:
                        convertView = (LinearLayout) mInflater.inflate(
                                R.layout.base_activity_details_four_layout, null);
                        holder.iv1 = (ImageView) convertView
                                .findViewById(R.id.detils_img_one);
                        holder.iv2 = (ImageView) convertView
                                .findViewById(R.id.detils_img_two);
                        holder.iv3 = (ImageView) convertView
                                .findViewById(R.id.detils_img_three);
                        holder.iv4 = (ImageView) convertView
                                .findViewById(R.id.detils_img_four);
                        break;
                    case 9:
                        convertView = (LinearLayout) mInflater.inflate(
                                R.layout.base_activity_details_four_layout, null);
                        // 判断图片的是水平还是竖直从而设定此ImageView的长宽
                        holder.iv1 = (ImageView) convertView
                                .findViewById(R.id.detils_img_one);
                        holder.iv2 = (ImageView) convertView
                                .findViewById(R.id.detils_img_two);
                        holder.iv3 = (ImageView) convertView
                                .findViewById(R.id.detils_img_three);
                        holder.iv4 = (ImageView) convertView
                                .findViewById(R.id.detils_img_four);
                        break;
                    default:
                        break;
                }
                holder.iv_head = (RoundedImage) convertView
                        .findViewById(R.id.image_icon1);// 头像图片
                holder.tv1 = (TextView) convertView
                        .findViewById(R.id.tx_start_times);// 时间
                holder.tv2 = (TextView) convertView
                        .findViewById(R.id.sports_find_othername1);// 名字
                holder.tv3 = (TextView) convertView
                        .findViewById(R.id.tx_detils1);// 发表的内容
                holder.tv4 = (TextView) convertView
                        .findViewById(R.id.tx_sport_address);// 发表的地址
                holder.iv_ismanorwoman = (ImageView) convertView
                        .findViewById(R.id.is_manorwomen_icon);// 性别判断
                convertView.setTag(holder);
            } else {
                holder = (ViewHoder) convertView.getTag();
            }
            switch (type) {
                // case 0:
                // imgList2.clear();
                // setImageVoid(type, position, imgList2);
                // mDownloader2.download(imgurList.get(0), holder.iv1, null);
                // break;
                case 1:
                    int height = list.get(position).getHeight();
                    int width = list.get(position).getWidth();
                    // int height = 150;
                    // int width = 200;
                    if (height != 0 && width != 0) {
                        if (width >= height) {
                            Log.e("---", "此图形是横着的---------");
                            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.iv1
                                    .getLayoutParams();
                            int imgWidth = (int) (SportsApp.ScreenWidth * 0.5);
                            lp.height = (int) ((imgWidth * height) / width);
                            lp.width = imgWidth;
                            holder.iv1.setLayoutParams(lp);

                        } else if (height > width) {
                            Log.e("---", "此图形是竖直的---------");
                            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.iv1
                                    .getLayoutParams();
                            int imgHeight = (int) (SportsApp.ScreenHeight * 0.33);
                            lp.height = imgHeight;
                            lp.width = (int) ((imgHeight * width) / height);
                            holder.iv1.setLayoutParams(lp);
                        }

                        mDownloader2.download(imgurList.get(0), holder.iv1, null);
                    } else {
                        height = 150;
                        width = 250;
                        if (width >= height) {
                            Log.e("---", "此图形是横着的---------");
                            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.iv1
                                    .getLayoutParams();
                            int imgWidth = (int) (SportsApp.ScreenWidth * 0.5);
                            lp.height = (int) ((imgWidth * height) / width);
                            lp.width = imgWidth;
                            holder.iv1.setLayoutParams(lp);

                        } else if (height > width) {
                            Log.e("---", "此图形是竖直的---------");
                            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.iv1
                                    .getLayoutParams();
                            int imgHeight = (int) (SportsApp.ScreenHeight * 0.33);
                            lp.height = imgHeight;
                            lp.width = (int) ((imgHeight * width) / height);
                            holder.iv1.setLayoutParams(lp);
                        }

                        mDownloader2.download(imgurList.get(0), holder.iv1, null);
                    }
                    break;
                case 2:
                    imgList2.clear();
                    imgList2.add(holder.iv1);
                    imgList2.add(holder.iv2);
                /*
                 * for (int i = 0; i < type; i++) { LinearLayout.LayoutParams
				 * lps
				 * =(LinearLayout.LayoutParams)imgList2.get(i).getLayoutParams
				 * (); lps.width=(int)(SportsApp.ScreenWidth * 0.625)/3;
				 * lps.height=(int)(SportsApp.ScreenWidth * 0.625)/3;
				 * imgList2.get(i).setLayoutParams(lps);
				 * mImageWorker.loadImage(urls[i], imgList2.get(i), null, null,
				 * false); }
				 */
                    setImageVoid(type, position, imgList2);
                    break;
                case 3:
                    imgList3.clear();
                    imgList3.add(holder.iv1);
                    imgList3.add(holder.iv2);
                    imgList3.add(holder.iv3);
                    setImageVoid(type, position, imgList3);
                /*
                 * for (int i = 0; i < type; i++) { LinearLayout.LayoutParams
				 * lps
				 * =(LinearLayout.LayoutParams)imgList3.get(i).getLayoutParams
				 * (); lps.width=(int)(SportsApp.ScreenWidth * 0.625)/3;
				 * lps.height=(int)(SportsApp.ScreenWidth * 0.625)/3;
				 * imgList3.get(i).setLayoutParams(lps);
				 * mImageWorker.loadImage(urls[i], imgList3.get(i), null, null,
				 * false); }
				 */
                    break;
                case 4:
                    imgList4.clear();
                    imgList4.add(holder.iv1);
                    imgList4.add(holder.iv2);
                    imgList4.add(holder.iv3);
                    imgList4.add(holder.iv4);
                    setImageVoid(type, position, imgList4);
                /*
				 * for (int i = 0; i < type; i++) { LinearLayout.LayoutParams
				 * lps
				 * =(LinearLayout.LayoutParams)imgList4.get(i).getLayoutParams
				 * (); lps.width=(int)(SportsApp.ScreenWidth * 0.625)/3;
				 * lps.height=(int)(SportsApp.ScreenWidth * 0.625)/3;
				 * imgList4.get(i).setLayoutParams(lps);
				 * mImageWorker.loadImage(urls[i], imgList4.get(i), null, null,
				 * false); }
				 */
                    break;
                case 5:
                    imgList5.clear();
                    imgList5.add(holder.iv1);
                    imgList5.add(holder.iv2);
                    imgList5.add(holder.iv3);
                    imgList5.add(holder.iv4);
                    // imgList5.add(holder.img5);
                    // setImageVoid(type, position, imgList5);
                    setImageVoid(4, position, imgList5);
                    break;
                case 6:
                    imgList6.clear();
                    imgList6.add(holder.iv1);
                    imgList6.add(holder.iv2);
                    imgList6.add(holder.iv3);
                    imgList6.add(holder.iv4);
                    // imgList6.add(holder.img5);
                    // imgList6.add(holder.img6);
                    // setImageVoid(type, position, imgList6);
                    setImageVoid(4, position, imgList6);
				/*
				 * for (int i = 0; i < type; i++) { LinearLayout.LayoutParams
				 * lps
				 * =(LinearLayout.LayoutParams)imgList6.get(i).getLayoutParams
				 * (); lps.width=(int)(SportsApp.ScreenWidth * 0.625)/3;
				 * lps.height=(int)(SportsApp.ScreenWidth * 0.625)/3;
				 * imgList6.get(i).setLayoutParams(lps);
				 * mImageWorker.loadImage(urls[i], imgList6.get(i), null, null,
				 * false); }
				 */
                    break;
                case 7:
                    imgList7.clear();
                    imgList7.add(holder.iv1);
                    imgList7.add(holder.iv2);
                    imgList7.add(holder.iv3);
                    imgList7.add(holder.iv4);
                    // imgList7.add(holder.img5);
                    // imgList7.add(holder.img6);
                    // imgList7.add(holder.img7);
                    // setImageVoid(type, position, imgList7);
                    setImageVoid(4, position, imgList7);
				/*
				 * for (int i = 0; i < type; i++) { LinearLayout.LayoutParams
				 * lps
				 * =(LinearLayout.LayoutParams)imgList7.get(i).getLayoutParams
				 * (); lps.width=(int)(SportsApp.ScreenWidth * 0.625)/3;
				 * lps.height=(int)(SportsApp.ScreenWidth * 0.625)/3;
				 * imgList7.get(i).setLayoutParams(lps);
				 * mImageWorker.loadImage(urls[i], imgList7.get(i), null, null,
				 * false); }
				 */
                    break;
                case 8:
                    imgList8.clear();
                    imgList8.add(holder.iv1);
                    imgList8.add(holder.iv2);
                    imgList8.add(holder.iv3);
                    imgList8.add(holder.iv4);
                    // imgList8.add(holder.img5);
                    // imgList8.add(holder.img6);
                    // imgList8.add(holder.img7);
                    // imgList8.add(holder.img8);
                    // setImageVoid(type, position, imgList8);
                    setImageVoid(4, position, imgList8);
				/*
				 * for (int i = 0; i < type; i++) { LinearLayout.LayoutParams
				 * lps
				 * =(LinearLayout.LayoutParams)imgList8.get(i).getLayoutParams
				 * (); lps.width=(int)(SportsApp.ScreenWidth * 0.625)/3;
				 * lps.height=(int)(SportsApp.ScreenWidth * 0.625)/3;
				 * imgList8.get(i).setLayoutParams(lps);
				 * mImageWorker.loadImage(urls[i], imgList8.get(i), null, null,
				 * false); }
				 */
                    break;
                case 9:
                    imgList9.clear();
                    imgList9.add(holder.iv1);
                    imgList9.add(holder.iv2);
                    imgList9.add(holder.iv3);
                    imgList9.add(holder.iv4);
                    // imgList9.add(holder.img5);
                    // imgList9.add(holder.img6);
                    // imgList9.add(holder.img7);
                    // imgList9.add(holder.img8);
                    // imgList9.add(holder.img9);
                    // setImageVoid(type, position, imgList9);
                    setImageVoid(4, position, imgList9);
				/*
				 * for (int i = 0; i < type; i++) { LinearLayout.LayoutParams
				 * lps
				 * =(LinearLayout.LayoutParams)imgList9.get(i).getLayoutParams
				 * (); lps.width=(int)(SportsApp.ScreenWidth * 0.625)/3;
				 * lps.height=(int)(SportsApp.ScreenWidth * 0.625)/3;
				 * imgList9.get(i).setLayoutParams(lps);
				 * imgList9.get(i).setBackgroundDrawable(null);
				 * mImageWorker.loadImage(urls[i], imgList9.get(i), null, null,
				 * false); }
				 */
                    break;
			/*
			 * for (int i = 0; i < type; i++) { LinearLayout.LayoutParams
			 * lps=(LinearLayout
			 * .LayoutParams)imgList.get(type-2).get(i).getLayoutParams();
			 * //lps.width=mItemHeight; //lps.height=mItemHeight;
			 * lps.width=(int)(SportsApp.ScreenWidth * 0.625)/3;
			 * lps.height=(int)(SportsApp.ScreenWidth * 0.625)/3;
			 * imgList.get(type-2).get(i).setLayoutParams(lps);
			 * mImageWorker.loadImage(urls[i], imgList.get(type-2).get(i), null,
			 * null, false); //imgList2.get(i).setOnClickListener(new
			 * imgOnClickListener(i+1,mList.get(position).getId())); } break;
			 */

                default:
                    break;
            }

            activityinfo2 activityinfo2 = list.get(position);

            mDownload.download(activityinfo2.getImg_head(), holder.iv_head,
                    null);
            // holder.iv_head.setOnClickListener(new
            // personalInformationOnClickListener(
            // list.get(position).getUid()));
            // if (activityinfo2.getList().size() !=0) {
            // mDownload.download(activityinfo2.getList().get(0), holder.iv2,
            // null);
            // }

            // 这是判断什么时候发的
            long time2 = list.get(position).getInputtime() * 1000L;
            long time = System.currentTimeMillis() - time2;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Log.i("ppp", "系统时间和发表时间差：" + time);
            if (time <= 60 * 1000)
                // 一分钟内显示刚刚
                holder.tv1.setText(context.getResources().getString(
                        R.string.sports_time_justnow));
            else if (time <= 60 * 60 * 1000) {
                int h = (int) (time / 1000 / 60);
                // 一小时内显示多少分钟前
                holder.tv1.setText(""
                        + h
                        + context.getResources().getString(
                        R.string.sports_time_mins_ago));
            } else {
                holder.tv1.setText(formatDisplayTime(format.format(list.get(
                        position).getInputtime() * 1000L), "yyyy-MM-dd HH:mm"));
                Log.i("ppp",
                        "发表出来的时间："
                                + formatDisplayTime(format.format(list.get(
                                position).getInputtime() * 1000L),
                                "yyyy-MM-dd HH:mm"));
            }

            if (list.get(position).getSex() == 1) {
                holder.iv_ismanorwoman
                        .setImageResource(R.drawable.friends_group_sexman);
            } else if (list.get(position).getSex() == 2) {
                holder.iv_ismanorwoman
                        .setImageResource(R.drawable.friends_group_sexwomen);
            }
            holder.tv4.setText(activityinfo2.getComefrom());
            holder.tv2.setText(activityinfo2.getName());
            // holder.tv2.setOnClickListener(new
            // personalInformationOnClickListener(
            // list.get(position).getUid()));
            // viewHoder.tv3.setText(activityinfo2.getContent());

            if (list.get(position).getContent() != null
                    && !"".equals(list.get(position).getContent())) {

                // 为了点击字段跳转到各自的活动页面详情页面：
                if (list.get(position).getContent().lastIndexOf(" ") != 0
                        && list != null) {
                    // boolean flog =
                    // list.get(position).getContent().contains(" ");
                    boolean flog2 = list.get(position).getContent()
                            .contains("#");
                    if (flog2) {
                        String likeUsers = list
                                .get(position)
                                .getContent()
                                .substring(
                                        0,
                                        list.get(position).getContent()
                                                .lastIndexOf("#") + 1)
                                .toString();

                        String listRemove = list.get(position).getContent()
                                .replace(likeUsers, "");
                        // final String likeUsers =
                        // mList.get(position).getDetils().toString();
                        String[] ary = likeUsers.split("#");
                        for (int i = 0; i < ary.length; i++) {
                            int a = getActivityId(ary[i]);
                            if (a != -1) {
                                activitySendId.add(a);
                            }
                        }

                        SpannableStringBuilder ss = new SpannableStringBuilder(
                                likeUsers);
                        String[] ary1 = likeUsers.split("#");
                        for (int i = 0; i < ary1.length; i++) {
                            int b = ary1[i].length();
                            activityNameSize.add(b);
                        }
                        if (ary1.length > 0) {
                            // 最后一个
                            for (int i = 0; i < ary1.length; i++) {
                                final String name = ary1[i];
                                final int start = likeUsers.indexOf(name);
                                ss.setSpan(new ClickableSpan() {

                                    @Override
                                    public void onClick(View widget) {
                                        // Toast.makeText(mContext, name,
                                        // Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(context,
                                                ActivityInfoWebView.class);
                                        intent.putExtra("title_name", name
                                                .toString().replace("#", ""));
                                        intent.putExtra("action_url", "");
                                        intent.putExtra("thrurl", "");
                                        if (activityNameList.indexOf(name
                                                .toString().replace("#", "")) != -1) {
                                            intent.putExtra(
                                                    "activity_id",
                                                    activityIdList
                                                            .get(activityNameList
                                                                    .indexOf(name
                                                                            .toString()
                                                                            .replace(
                                                                                    "#",
                                                                                    ""))));
                                            ActionList action = actionLists
                                                    .get(activityNameList
                                                            .indexOf(name
                                                                    .toString()
                                                                    .replace(
                                                                            "#",
                                                                            "")));
                                            activityTime_2 = action
                                                    .getActionTime()
                                                    .substring(
                                                            action.getActionTime()
                                                                    .indexOf(
                                                                            "-") + 1,
                                                            action.getActionTime()
                                                                    .length())
                                                    .replace(".", "-");
                                            Log.e("ActivityList", "活动时间："
                                                    + activityTime_2);
                                            activityTime_2 = activityTime_2
                                                    .replace("-", "");
                                            int b = Integer.valueOf(
                                                    activityTime_2).intValue() + 1;
                                            activityTime_2 = b + "";
                                            String year = activityTime_2
                                                    .substring(0, 4);
                                            String month = activityTime_2
                                                    .substring(4, 6);
                                            String day = activityTime_2
                                                    .substring(6, 8);
                                            activityTime_2 = year + "-" + month
                                                    + "-" + day;
                                            Log.i("aaaaaaa", "本页面跳转："
                                                    + activityTime_2);
                                            intent.putExtra("activitytime",
                                                    activityTime_2);
                                            context.startActivity(intent);
                                        }
                                    }

                                    @Override
                                    public void updateDrawState(TextPaint ds) {
                                        super.updateDrawState(ds);
                                        // ds.setColor(Color.RED); // 设置文本颜色
                                        // 去掉连接字体下划线
                                        ds.setUnderlineText(false);
                                    }

                                }, start, start + name.length(), 0);
                            }
                        }
                        SpannableStringBuilder aaaa = ss.append(listRemove);

                        // ss.setSpan(new ClickableSpan() {
                        // @Override
                        // public void onClick(View view) {
                        // Intent intent=new
                        // Intent(mContext,ActivityInfoWebView.class);
                        // intent.putExtra("title_name",
                        // activityNameList.get(0));
                        // intent.putExtra("action_url", "");
                        // intent.putExtra("activity_id",
                        // activitySendId.get(0));
                        // mContext.startActivity(intent);
                        // }
                        // }, 0, activityNameSize.get(0),
                        // Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        holder.tv3.setText(aaaa);
                        holder.tv3.setMovementMethod(LinkMovementMethod
                                .getInstance());
                    } else {
                        holder.tv3.setText(list.get(position).getContent());
                    }
                }

                // holder.detils.setOnClickListener(new OnClickListener() {
                //
                // @Override
                // public void onClick(View v) {
                // // TODO Auto-generated method stub
                // Intent intent=new Intent(mContext,ActivityInfoWebView.class);
                // intent.putExtra("title_name",
                // actionLists.get(position).getTitle());
                // intent.putExtra("action_url", "");
                // intent.putExtra("activity_id", activitySendId.get(1));
                // mContext.startActivity(intent);
                // }
                // });

                // holder.detils.setText(mList.get(position).getDetils());
                holder.tv3.setVisibility(View.VISIBLE);
            }

            return convertView;
        }

        class ViewHoder {
            TextView tv1;
            TextView tv2;
            TextView tv3;
            TextView tv4;
            ImageView iv_ismanorwoman;
            RoundedImage iv_head;
            ImageView iv1;
            ImageView iv2;
            ImageView iv3;
            ImageView iv4;
            ImageView iv5;
            ImageView iv6;
            ImageView iv7;
            ImageView iv8;
            ImageView iv9;
            ImageView iv10;

        }

    }

    public static String formatDisplayTime(String time, String pattern) {
        String display = "";
        int tMin = 60 * 1000;
        int tHour = 60 * tMin;
        int tDay = 24 * tHour;

        if (time != null) {
            try {
                Date tDate = new SimpleDateFormat(pattern).parse(time);
                Date today = new Date();
                SimpleDateFormat thisYearDf = new SimpleDateFormat("yyyy");
                SimpleDateFormat todayDf = new SimpleDateFormat("yyyy-MM-dd");
                Date thisYear = new Date(thisYearDf.parse(
                        thisYearDf.format(today)).getTime());
                Date yesterday = new Date(todayDf.parse(todayDf.format(today))
                        .getTime());
                Date beforeYes = new Date(yesterday.getTime() - tDay);
                if (tDate != null) {
                    SimpleDateFormat halfDf = new SimpleDateFormat(
                            "MM-dd HH:mm");
                    long dTime = today.getTime() - tDate.getTime();
                    if (tDate.before(thisYear)) {
                        display = new SimpleDateFormat("yyyy年MM月dd日")
                                .format(tDate);
                    } else {

                        if (dTime < tMin) {
                            display = "刚刚";
                        } else if (dTime < tHour) {
                            display = (int) (dTime / tMin) + "分钟前";
                        } else if (dTime < tDay && tDate.after(yesterday)) {
                            display = (int) (dTime / tHour) + "小时前";
                        } else if (tDate.after(beforeYes)
                                && tDate.before(yesterday)) {
                            display = "昨天";
                        } else {
                            display = halfDf.format(tDate);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return display;
    }

    // 通过传入一个活动的名字得到活动的ID。若传入的活动名字错误、返回-1；
    public int getActivityId(String activityName) {
        if (activityName != null && !" ".equals(activityName)) {
            if (actionLists != null) {
                for (int i = 0; i < actionLists.size(); i++) {
                    activityNameList.add(actionLists.get(i).getTitle());
                    activityIdList.add(actionLists.get(i).getActionId());
                    activityTimeList.add(actionLists.get(i).getActionTime());
                }
                boolean flog = activityNameList.contains(activityName);
                if (flog) {
                    int index = activityNameList.indexOf(activityName);
                    int activityId = activityIdList.get(index);
                    return activityId;
                } else {
                    return -1;
                }
            }
            return -1;
        }
        return -1;
    }

    // 下载活动列表
    private class GetActionDataTask extends
            AsyncTask<Void, Void, List<ActionList>> {

        @Override
        protected List<ActionList> doInBackground(Void... sessionid) {

            List<ActionList> actionLists = null;
            try {
                actionLists = ApiJsonParser
                        .getNewActionList(
                                mSportsApp.getSessionId(),
                                "z"
                                        + getResources().getString(
                                        R.string.config_game_id), 0);
            } catch (ApiNetException e) {
                e.printStackTrace();
            } catch (ApiSessionOutException e) {
                e.printStackTrace();
            }
            return actionLists;
        }

        @Override
        protected void onPostExecute(List<ActionList> result) {
            super.onPostExecute(result);
            // waitCloset();
            if (result == null)
                return;
            if (result.size() > 0) {
                for (ActionList actionList : result) {
                    actionLists.add(actionList);
                }
            }
        }

    }

    private class GetActionDataTask2 extends
            AsyncTask<Void, Void, List<ActionList>> {

        @Override
        protected List<ActionList> doInBackground(Void... sessionid) {

            List<ActionList> actionLists = null;
            try {
                actionLists = ApiJsonParser
                        .getNewActionList(
                                mSportsApp.getSessionId(),
                                "z"
                                        + getResources().getString(
                                        R.string.config_game_id), 1);
            } catch (ApiNetException e) {
                e.printStackTrace();
            } catch (ApiSessionOutException e) {
                e.printStackTrace();
            }
            return actionLists;
        }

        @Override
        protected void onPostExecute(List<ActionList> result) {
            super.onPostExecute(result);
            // waitCloset();
            if (result == null)
                return;
            if (result.size() > 0) {
                for (ActionList actionList : result) {
                    actionLists.add(actionList);
                }
            }
        }

    }

    // 计算ListView的高度；
    // private void setListViewHeightBasedOnChildren(ListView listView) {
    //
    // ListAdapter listAdapter = listView.getAdapter();
    // if (listAdapter == null) {
    // return;
    // }
    //
    // int totalHeight = 0;
    // for (int i = 0; i < listAdapter.getCount(); i++) {
    // View listItem = listAdapter.getView(i, null, listView);
    // listItem.measure(0, 0);
    // totalHeight += listItem.getMeasuredHeight();
    // }
    //
    // ViewGroup.LayoutParams params = listView.getLayoutParams();
    // params.height = totalHeight
    // + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
    // listView.setLayoutParams(params);
    // }

    private void GetActionData() {

        if (mSportsApp.isOpenNetwork()) {
            new GetActionDataDetailTask().execute();
        } else {
            Toast.makeText(ActivityInfoWebView.this,
                    getResources().getString(R.string.acess_server_error), Toast.LENGTH_SHORT)
                    .show();
        }

    }

    private void setImageVoid(int type, int position, List<ImageView> imgList) {
        for (int i = 0; i < type; i++) {
            LinearLayout.LayoutParams lps = (LinearLayout.LayoutParams) imgList
                    .get(i).getLayoutParams();
            // lps.width = (int) (SportsApp.ScreenWidth * 0.625) / 3; old
            // lps.height = (int) (SportsApp.ScreenWidth * 0.625) / 3;
            lps.width = (int) (SportsApp.ScreenWidth * 0.635) / 2;
            lps.height = (int) (SportsApp.ScreenWidth * 0.635) / 2;
            imgList.get(i).setLayoutParams(lps);
            mImageWorker.loadImage(imgurList.get(i), imgList.get(i), null,
                    null, false);
        }

    }

    // 点击进入个人信息页面
    class personalInformationOnClickListener implements OnClickListener {
        private int userId;

        public personalInformationOnClickListener(int id) {
            this.userId = id;
        }

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            if (!mSportsApp.isOpenNetwork()) {
                Toast.makeText(ActivityInfoWebView.this, "网络未连接，请检查网络！",
                        Toast.LENGTH_LONG).show();
                return;
            }
            // Intent intent = new Intent(mContext, PedometerActivity.class);
            Intent intent = new Intent(ActivityInfoWebView.this,
                    PersonalPageMainActivity.class);
            intent.putExtra("ID", userId);
            ActivityInfoWebView.this.startActivity(intent);

        }
    }

    // 后台统计第三方的活动参加人数
    private void healthCount() {
        new AsyncTask<Void, Void, ApiBack>() {
            @Override
            protected ApiBack doInBackground(Void... params) {
                // TODO Auto-generated method stub
                ApiBack back = null;
                try {
                    back = ApiJsonParser.healthdatacount(msApp.getSessionId(),
                            9, onclicktime, 0);
                } catch (ApiNetException e) {
                    e.printStackTrace();
                } catch (ApiSessionOutException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return back;
            }

            @Override
            protected void onPostExecute(ApiBack result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                if (result != null) {

                } else {
                }

            }
        }.execute();
    }

    public class ExpressionImgAdapter extends BaseAdapter {
        private List<ExpressionItems> mList;
        private Context mContext;
        public static final int APP_PAGE_SIZE = 21;

        public ExpressionImgAdapter(Context context,
                                    List<ExpressionItems> list, int page) {
            mContext = context;

            mList = new ArrayList<ExpressionItems>();
            int i = page * APP_PAGE_SIZE;
            int iEnd = i + APP_PAGE_SIZE;
            while ((i < list.size()) && (i < iEnd)) {
                mList.add(list.get(i));
                i++;
            }
        }

        public int getCount() {
            return mList.size();
        }

        public Object getItem(int position) {
            return mList.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ExpressionItems map = mList.get(position);
            ImgItem appItem;
            if (convertView == null) {
                View v = LayoutInflater.from(mContext).inflate(
                        R.layout.app_item, null);

                appItem = new ImgItem();
                appItem.mAppIcon = (ImageView) v.findViewById(R.id.ivAppIcon);

                v.setTag(appItem);
                convertView = v;
            } else {
                appItem = (ImgItem) convertView.getTag();
            }
            appItem.mAppIcon.setBackgroundResource((Integer) map.getId());
            return convertView;
        }

        class ImgItem {
            ImageView mAppIcon;
        }
    }

    // 下载活动详情列表的数据源
    class SportsFindMoreThread extends Thread {
        // private long startTime = System.currentTimeMillis();

        @Override
        public void run() {
            // TODO Auto-generated method stub
            super.run();
            Message msg = null;
            // ArrayList<FindMore> list = new ArrayList<FindMore>();
            ArrayList<FindGroup> list = new ArrayList<FindGroup>();
            try {
                // list = (ArrayList<FindMore>) ApiJsonParser.getFindList(
                // mSportsApp.getSessionId(), times,false);
                list = (ArrayList<FindGroup>) ApiJsonParser.getNewFindList(
                        mSportsApp.getSessionId(), times, mSportsApp
                                .getSportUser().getUid() + "", false,
                        activityId);
            } catch (ApiNetException e) {
                e.printStackTrace();
            } catch (ApiSessionOutException e) {
                e.printStackTrace();
            }
            if (times == 0) {
                mList.clear();
            }
            if (list != null) {
                Log.e(TAG, "length-------------" + list.size());
                if (list.size() == 0) {
                    // msg = Message.obtain(msportsFindMoreHandler, FRESH_NULL);
                    msg.sendToTarget();
                } else {
                    for (FindGroup e : list) {
                        if (isshow != 2) {
                            // if (thrURL.length() == 0) {
                            mList2.add(e);
                            // }
                        }
                    }
                    Log.e(TAG, "mList------------" + mList.size());
                    // msg = Message.obtain(msportsFindMoreHandler, FRESH_LIST);
                    msg.sendToTarget();
                }
            } else {
                if (list == null) {
                    Log.d(TAG, "*******检z4********");
                    // msg = Message.obtain(msportsFindMoreHandler,
                    // FRESH_FAILED);
                    msg.sendToTarget();
                }
            }
        }
    }

    // 下载活动列表
    private class GetActionDataDetailTask extends
            AsyncTask<Void, Void, List<FindGroup>> {

        @Override
        protected List<FindGroup> doInBackground(Void... sessionid) {

            ArrayList<FindGroup> list = new ArrayList<FindGroup>();
            try {
                Log.i("acitivityID", "活动ID:" + mSportsApp.getSessionId());
                Log.i("acitivityID", "活动ID:" + activityId);
                Log.i("acitivityID", "活动ID:"
                        + mSportsApp.getSportUser().getUid());
                Log.i("acitivityID", "活动页数:" + times);

                list = (ArrayList<FindGroup>) ApiJsonParser.getNewFindList(
                        mSportsApp.getSessionId(), times, mSportsApp
                                .getSportUser().getUid() + "", false,
                        activityId);

            } catch (ApiNetException e) {
                e.printStackTrace();
            } catch (ApiSessionOutException e) {
                e.printStackTrace();
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<FindGroup> result) {
            super.onPostExecute(result);
            // waitCloset();
            if (result == null)
                return;
            if (times == 0) {
                mList2.clear();
            }
            if (result.size() > 0) {
                for (FindGroup actionList : result) {
                    if (isshow != 2) {
                        // if (thrURL.length() == 0) {
                        mList2.add(actionList);
                        // }
                    }
                }
                sportsfindmoreAdapter.notifyDataSetChanged();
                actiondetailListView.onRefreshComplete();
            } else {
                Toast.makeText(ActivityInfoWebView.this, "信息已全部加载",
                        Toast.LENGTH_SHORT).show();
                actiondetailListView.onRefreshComplete();
            }
        }

    }

    class ActionInfoShareLister implements OnClickListener {

        @Override
        public void onClick(View v) {
            if (activityDetailInfo != null && activityDetailInfo.getBiaoti_img() != null) {
                Intent mIntent = new Intent(getApplication(),
                        ShareActivity.class);
                mIntent.putExtra("infoid", activityId);
                mIntent.putExtra("title", title_name);
                mIntent.putExtra("img_url", activityDetailInfo.getBiaoti_img());
                mIntent.putExtra("content", activityDetailInfo.getContent());
                mIntent.putExtra("bs", 2);
                startActivity(mIntent);
            } else if (activityDetailInfo != null && activityDetailInfo.getBiaoti_img() == null) {
                Toast.makeText(getApplication(), "数据加载失败...", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplication(), "数据正在加载中,请稍后...", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
