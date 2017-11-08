package com.fox.exercise.newversion.act;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fox.exercise.AbstractBaseFragment;
import com.fox.exercise.BindingDevice;
import com.fox.exercise.HistoryAllActivity;
import com.fox.exercise.NormalProble;
import com.fox.exercise.R;
import com.fox.exercise.SportMeWebView;
import com.fox.exercise.SportMe_MedalWebView;
import com.fox.exercise.SportMe_MyOrderWebView;
import com.fox.exercise.SportsType;
import com.fox.exercise.YunHuWebViewActivity;
import com.fox.exercise.api.ApiBack;
import com.fox.exercise.api.ApiConstant;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.api.MessageService;
import com.fox.exercise.api.WatchService;
import com.fox.exercise.api.entity.UserDetail;
import com.fox.exercise.bitmap.util.ImageResizer;
import com.fox.exercise.db.SportSubTaskDB;
import com.fox.exercise.login.LoginActivity;
import com.fox.exercise.newversion.newact.NewFansActivity;
import com.fox.exercise.newversion.newact.NewGuanZhuActivity;
import com.fox.exercise.newversion.newact.NewRanksActivity;
import com.fox.exercise.newversion.newact.NewYuePaoGaoDeActivity;
import com.fox.exercise.pedometer.ImageWorkManager;
import com.fox.exercise.pedometer.SportContionTaskDetail;
import com.fox.exercise.util.RoundedImage;
import com.fox.exercise.util.SportTaskUtil;
import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.ingenic.indroidsync.SportsApp;

/**
 * Created by zhonghuibin on 2016/7/29.
 */

public class IndexMeFragment extends AbstractBaseFragment implements OnClickListener,PopupWindow.OnDismissListener {

    private View view;
    private SportsApp mSportsApp;
    private Context mContext;
    private UserDetail mUserDetail = null;

    private RelativeLayout sports_history_me;//运动记录
    private RelativeLayout sports_mymatch_me;//我的赛事
    private RelativeLayout sports_myorder_me;//我的订单
    private RelativeLayout sports_medal_me;//我的勋章
    private RelativeLayout sports_jinbimall_me;//金币商城
    private RelativeLayout sports_device_me;//智能设备
    private RelativeLayout sports_weixinrank_me;//微信排行
    private RelativeLayout sports_diandianrank_me;//点点排行
    private RelativeLayout sports_normalproble_me;//常见问题
    private RelativeLayout sports_qqhealth_me;//QQ健康
    private RelativeLayout sports_setting_me;//设置
    private RelativeLayout sports_nearby_me;//附近的人

    private RoundedImage personal_icon;//头像
    private TextView sport_username_me;//名字
    //我的动态、我的关注、我的粉丝
    private LinearLayout ll_mydongtai;
    private LinearLayout ll_myguanzhu;
    private LinearLayout ll_myfensi;
    private TextView tv_mydongtai;
    private TextView tv_myguanzhu;
    private TextView tv_myfensi;
    private TextView bianji;//编辑
    private ImageView sport_ivgotosetting_me;
    private RelativeLayout rl_personalhead_me;
    private Button exit_me;
    private PopupWindow window;
    private LinearLayout mview;
    private RelativeLayout mPopMenuBack;
    private TextView mDialogMessage;
    public Dialog mLoadProgressDialog2 = null;

    private int dongtai;
    private int guanzhu;
    private int fensi;
    private int kaluli;

    //金币商城的地址先用来测试
    private static final String URL = ApiConstant.URL + "/Beauty/kupao.php?m=Webapp&a=url_jump&id=3&session_id=";
    private static final String URL_Myorder = ApiConstant.URL + "/Beauty/kupao.php?m=Webapp&a=url_jump&id=3&act=order_list&session_id=";
    private static final String URL_Mymedal = ApiConstant.URL +"/Beauty/kupao.php?m=medal&a=MyMedallist&uid=";
    private static final String URL_Mymatch = ApiConstant.URL +"/Beauty/kupao.php?m=Webactivity&a=index&url=list&session_id=";
    @Override
    public void beforeInitView() {
        title = getActivity().getResources().getString(R.string.personal_center);
    }

    @Override
    public void setViewStatus() {
        view = LayoutInflater.from(getActivity()).inflate(
                R.layout.sports_aboutme_main_frg, null);
        setContentViews(view);

        mSportsApp = (SportsApp) getActivity().getApplication();
        mSportsApp.addActivity(getActivity());
        mContext = getActivity();
        mUserDetail = mSportsApp.getSportUser();

        sports_history_me = (RelativeLayout) view.findViewById(R.id.sports_history_me);
        sports_history_me.setOnClickListener(this);
        sports_mymatch_me = (RelativeLayout) view.findViewById(R.id.sports_mymatch_me);
        sports_mymatch_me.setOnClickListener(this);
        sports_myorder_me = (RelativeLayout) view.findViewById(R.id.sports_myorder_me);
        sports_myorder_me.setOnClickListener(this);
        sports_medal_me = (RelativeLayout) view.findViewById(R.id.sports_medal_me);
        sports_medal_me.setOnClickListener(this);
        sports_jinbimall_me = (RelativeLayout) view.findViewById(R.id.sports_jinbimall_me);
        sports_jinbimall_me.setOnClickListener(this);
        sports_device_me = (RelativeLayout) view.findViewById(R.id.sports_device_me);
        sports_device_me.setOnClickListener(this);
        sports_weixinrank_me = (RelativeLayout) view.findViewById(R.id.sports_weixinrank_me);
        sports_weixinrank_me.setOnClickListener(this);
        sports_diandianrank_me = (RelativeLayout) view.findViewById(R.id.sports_diandianrank_me);
        sports_diandianrank_me.setOnClickListener(this);
        sports_normalproble_me = (RelativeLayout) view.findViewById(R.id.sports_normalproble_me);
        sports_normalproble_me.setOnClickListener(this);
        sports_qqhealth_me = (RelativeLayout) view.findViewById(R.id.sports_qqhealth_me);
        sports_qqhealth_me.setOnClickListener(this);
        sports_setting_me = (RelativeLayout) view.findViewById(R.id.sports_setting_me);
        sports_setting_me.setOnClickListener(this);
        sports_nearby_me = (RelativeLayout) view.findViewById(R.id.sports_nearby_me);
        sports_nearby_me.setOnClickListener(this);

        //我的动态、关注的人、我的粉丝
        ll_mydongtai = (LinearLayout) view.findViewById(R.id.ll_mydongtai);
        ll_mydongtai.setOnClickListener(this);
        ll_myguanzhu = (LinearLayout) view.findViewById(R.id.ll_myguanzhu);
        ll_myguanzhu.setOnClickListener(this);
        ll_myfensi = (LinearLayout) view.findViewById(R.id.ll_myfensi);
        ll_myfensi.setOnClickListener(this);

        tv_mydongtai = (TextView) view.findViewById(R.id.tv_mydongtai);
        tv_myguanzhu = (TextView) view.findViewById(R.id.tv_myguanzhu);
        tv_myfensi = (TextView) view.findViewById(R.id.tv_myfensi);

        exit_me = (Button) view.findViewById(R.id.exit_me);
        exit_me.setOnClickListener(this);
        if (mSportsApp.LoginOption) {
            exit_me.setVisibility(View.VISIBLE);
        }else{
            exit_me.setVisibility(View.GONE);
        }
        mPopMenuBack = (RelativeLayout) view.findViewById(R.id.set_menu_background);

//        if (mUserDetail.getFind_count_num() == 0) {
//            setContent(tv_mydongtai, "动态 : " + "0", 4, 0);
//        } else {
//            setContent(tv_mydongtai, "动态 : " + mUserDetail.getFind_count_num(),4, 0);
//        }
//
//        if (mUserDetail.getFollowCounts() == 0) {
//            setContent(tv_myguanzhu, "关注 : " + "0", 4, 0);
//        } else {
//            setContent(tv_myguanzhu, "关注 : " + mUserDetail.getFollowCounts(),4, 0);
//        }
//
//        if (mUserDetail.getFanCounts() == 0) {
//            setContent(tv_myfensi, "粉丝 : " + "0", 4, 0);
//        } else {
//            setContent(tv_myfensi, "粉丝 : " + mUserDetail.getFanCounts(), 4, 0);
//        }
        sport_username_me = (TextView) view.findViewById(R.id.sport_username_me);
        if (mUserDetail != null){
            sport_username_me.setVisibility(View.VISIBLE);
            sport_username_me.setText(mUserDetail.getUname());
            sport_username_me.setOnClickListener(new clickListener_bianji());
        }else{
            sport_username_me.setVisibility(View.INVISIBLE);
        }


//        bianji = new TextView(getActivity());
//        bianji.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT));
//        bianji.setText(getResources().getString(R.string.edit_save));
//        bianji.setTextSize(18);
//        bianji.setTextColor(getResources().getColor(R.color.white));
//        bianji.setOnClickListener(new clickListener_bianji());
//        showRightBtn(bianji);
//        right_btn.setPadding(0, 0, SportsApp.getInstance().dip2px(17), 0);
        sport_ivgotosetting_me = (ImageView) view.findViewById(R.id.sport_ivgotosetting_me);
        sport_ivgotosetting_me.setOnClickListener(new clickListener_bianji());

        rl_personalhead_me = (RelativeLayout) view.findViewById(R.id.rl_personalhead_me);
        rl_personalhead_me.setOnClickListener(new clickListener_bianji());




//        left_ayout.setOnClickListener(new clickListener());
//        iButton = new ImageButton(getActivity());
//        iButton.setBackgroundDrawable(getResources().getDrawable(
//                R.drawable.fristpage_jinbimall));
//        iButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT));
//        left_ayout.setPadding(0, 0, mSportsApp.dip2px(17), 0);
//        iButton.setOnClickListener(new clickListener());
//        if (left_ayout != null) {
//            left_ayout.removeAllViews();
//            left_ayout.addView(iButton);
//        }
    }

    @Override
    public void onPageResume() {
        dongtai = mSportsApp.getDongtai_personalceter();
        if (dongtai != 0){
        }
        if (mSportsApp.getUserName() != null){
            sport_username_me.setText(mSportsApp.getUserName());
        }else{
            sport_username_me.setText(mSportsApp.getSportUser().getUname());
        }
        ImageWorkManager mImageWorkerMan_Icon = new ImageWorkManager(getActivity(), 100, 100);
        ImageResizer mImageWorker_Icon = mImageWorkerMan_Icon.getImageWorker();

        personal_icon = (RoundedImage) view.findViewById(R.id.personal_icon);
        personal_icon.setOnClickListener(new clickListener_bianji());
        if (!mSportsApp.LoginOption){
            personal_icon.setImageResource(R.drawable.sports_residemenu_man);
        }else{
            if (mSportsApp.getSportUser().getUimg() != null && !"http://dev-kupao.mobifox.cn".equals(mSportsApp.getSportUser().getUimg())
                    &&!"http://kupao.mobifox.cn".equals(mSportsApp.getSportUser().getUimg())) {
                mImageWorker_Icon.loadImage(mSportsApp.getSportUser().getUimg(), personal_icon,
                        null, null, false);
            } else {
                personal_icon.setImageResource("woman".equals(mSportsApp.getSportUser().getSex()) ? R.drawable.sports_user_edit_portrait
                        : R.drawable.sports_user_edit_portrait_male);
            }
        }

        //为了每次进去页面得到最新的数据、将数据赋值写在OnResume
        mUserDetail = mSportsApp.getSportUser();
        if (mUserDetail.getFind_count_num() == 0) {
            if(mSportsApp.getDongtai_personalceter() != 1) {
                setContent(tv_mydongtai, "动态 : " + "0", 4, 0);
            }else{
                setContent(tv_mydongtai, "动态 : " + "1", 4, 0);
            }
        } else {
            if (mSportsApp.getDongtai_personalceter() == 1) {
                setContent(tv_mydongtai, "动态 : " + (mUserDetail.getFind_count_num()+1), 4, 0);
                mUserDetail.setFind_count_num(mUserDetail.getFind_count_num()+1);
            }else if(mSportsApp.getDongtai_personalceter() == 3){
                setContent(tv_mydongtai, "动态 : " + (mUserDetail.getFind_count_num()-1), 4, 0);
                mUserDetail.setFind_count_num(mUserDetail.getFind_count_num()-1);
            }else{
                setContent(tv_mydongtai, "动态 : " + mUserDetail.getFind_count_num(), 4, 0);
            }
            mSportsApp.setDongtai_personalceter(0);
        }

        if (mUserDetail.getFollowCounts() == 0) {
            setContent(tv_myguanzhu, "关注 : " + "0", 4, 0);
        } else {
            setContent(tv_myguanzhu, "关注 : " + mUserDetail.getFollowCounts(),4, 0);
        }

        if (mUserDetail.getFanCounts() == 0) {
            setContent(tv_myfensi, "粉丝 : " + "0", 4, 0);
        } else {
            setContent(tv_myfensi, "粉丝 : " + mUserDetail.getFanCounts(), 4, 0);
        }

        MobclickAgent.onPageStart("IndexMeFragment");
    }

    @Override
    public void onPagePause() {
        MobclickAgent.onPageEnd("IndexMeFragment");
    }

    private WatchService wService;
    private ServiceConnection wConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            wService = ((WatchService.WBinder) service).getService();
            if (wService != null)
                wService.appOnForeground();
        }

        public void onServiceDisconnected(ComponentName className) {
            wService = null;
        }
    };
    @Override
    public void onPageDestroy() {
        mContext = null;
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            //跳转运动记录
            case R.id.sports_history_me:
                if (mSportsApp.LoginOption) {
                    Intent kintent = new Intent(getActivity(), HistoryAllActivity.class);
                    Bundle kbundle = new Bundle();
                    kbundle.putInt("ID", SportsApp.getInstance().getSportUser().getUid());
                    kintent.putExtra("ID", SportsApp.getInstance().getSportUser().getUid());
                    String ss1 = mSportsApp.getSportUser().getCount_num() + "";
                    int aaa = (int) mSportsApp.getSportUser().getSprots_Calorie();
                    String ss2 = aaa + "";
                    String ss3 = mSportsApp.getSportUser().getTime() + "";

                    kbundle.putString("yundong_cishu", ss1);
                    kbundle.putString("yundong_laluli", ss2);
                    kbundle.putString("yundong_di_day", ss3);
                    kintent.putExtra("yundong_cishu", ss1);
                    kintent.putExtra("yundong_laluli", ss2);
                    kintent.putExtra("yundong_di_day", ss3);
                    kintent.putExtras(kbundle);
                    startActivity(kintent);
                }else{
                    //是否立即登录弹出框
                    mSportsApp.TyrLoginAction(mContext,
                            getString(R.string.sports_love_title),
                            getString(R.string.try_to_login));
                }
                break;

            //跳转我的赛事
            case R.id.sports_mymatch_me:
                //Toast.makeText(mContext,"跳转我的赛事界面",Toast.LENGTH_LONG).show();
                if (mSportsApp.LoginOption){
                    Intent intentNo1 = new Intent(mContext, SportMeWebView.class);
                    intentNo1.putExtra("webUrl",URL_Mymatch + mSportsApp.getSessionId());
                    intentNo1.putExtra("index",1);
                    startActivity(intentNo1);
                }else {
                    mSportsApp.TyrLoginAction(mContext,
                            getString(R.string.sports_love_title),
                            getString(R.string.try_to_login));
                }
                break;

            //跳转我的订单
            case R.id.sports_myorder_me:
                if (mSportsApp.LoginOption){
                    //Toast.makeText(mContext,"跳转我的订单界面",Toast.LENGTH_LONG).show();
                    Intent intentNo2 = new Intent(mContext, SportMe_MyOrderWebView.class);
                    intentNo2.putExtra("webUrl",URL_Myorder+mSportsApp.getSessionId());
                    intentNo2.putExtra("index",2);
                    startActivity(intentNo2);
                }else{
                    mSportsApp.TyrLoginAction(mContext,
                            getString(R.string.sports_love_title),
                            getString(R.string.try_to_login));
                }

                break;
            //跳转我的勋章
            case R.id.sports_medal_me:
                if (mSportsApp.LoginOption){
                    Intent intent_medal = new Intent(mContext, SportMe_MedalWebView.class);
                    intent_medal.putExtra("webUrl",URL_Mymedal+mSportsApp.getSportUser().getUid());
                    Log.i("sport_xunzhang",URL_Mymedal+mSportsApp.getSportUser().getUid());
                    startActivity(intent_medal);
                }else{
                    mSportsApp.TyrLoginAction(mContext,
                            getString(R.string.sports_love_title),
                            getString(R.string.try_to_login));
                }

                break;

            //跳转金币商城
            case R.id.sports_jinbimall_me:
                if (mSportsApp.LoginOption) {
                    if (mSportsApp.isOpenNetwork()) {
                        startActivity(new Intent(getActivity(), YunHuWebViewActivity.class));
                    } else {
                        Toast.makeText(
                                getActivity(),
                                getResources().getString(
                                        R.string.error_cannot_access_net),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mSportsApp.TyrLoginAction(mContext,
                            getString(R.string.sports_love_title),
                            getString(R.string.try_to_login));
                }
                break;

            //跳转智能设备
            case R.id.sports_device_me:
                if (!mSportsApp.LoginOption) {
                    mSportsApp.TyrLoginAction(mContext,
                            getString(R.string.sports_love_title),
                            getString(R.string.try_to_login));
                } else if (!mSportsApp.LoginNet) {

                    Intent intentmap = new Intent(mContext, BindingDevice.class);
                    startActivity(intentmap);
                    if (mSportsApp.config == 1 && wService != null)
                        wService.setMe(true);
                } else {
                    Intent intentmap = new Intent(mContext, BindingDevice.class);
                    startActivity(intentmap);
                    if (mSportsApp.config == 1 && wService != null)
                        wService.setMe(true);
                }
                break;

            //跳转微信好友排行
            case R.id.sports_weixinrank_me:
                //Toast.makeText(mContext,"跳转微信好友排行界面",Toast.LENGTH_LONG).show();
                if (mSportsApp.isOpenNetwork()) {
                    if (mSportsApp.LoginOption) {
                        Intent intentNo3 = new Intent(mContext, SportMeWebView.class);
                        intentNo3.putExtra("webUrl", URL + mSportsApp.getSessionId());
                        intentNo3.putExtra("index", 3);
                        startActivity(intentNo3);
                    } else {
                        mSportsApp.TyrLoginAction(mContext,
                                getString(R.string.sports_love_title),
                                getString(R.string.try_to_login));
                    }
                }else{
                    Toast.makeText(
                            getActivity(),
                            getResources().getString(
                                    R.string.error_cannot_access_net),
                            Toast.LENGTH_SHORT).show();
                }
                break;

            //跳转点点排行
            case R.id.sports_diandianrank_me:
                if (!mSportsApp.LoginOption) {
                    mSportsApp.TyrLoginAction(mContext,
                            getString(R.string.sports_love_title),
                            getString(R.string.try_to_login));
                } else if (!mSportsApp.LoginNet) {
                    mSportsApp.NoNetLogin(mContext);

                } else {
                    Intent intentmap = new Intent(mContext, NewRanksActivity.class);
                    startActivity(intentmap);
                    if (mSportsApp.config == 1 && wService != null)
                        wService.setMe(true);
                }
                break;

            //跳转计步问题
            case R.id.sports_normalproble_me:
                //Toast.makeText(mContext,"跳转计步问题界面",Toast.LENGTH_LONG).show();
                Intent intentNo4 = new Intent(mContext, NormalProble.class);
//                intentNo4.putExtra("webUrl",URL+mSportsApp.getSessionId());
//                intentNo4.putExtra("index",4);
                startActivity(intentNo4);
                break;

            //跳转qq健康
            case R.id.sports_qqhealth_me:
                //Toast.makeText(mContext,"跳转QQ健康界面",Toast.LENGTH_LONG).show();
                if (mSportsApp.LoginOption){
                    Intent intentNo5 = new Intent(mContext, SportMeWebView.class);
                    intentNo5.putExtra("webUrl",URL+mSportsApp.getSessionId());
                    intentNo5.putExtra("index",5);
                    startActivity(intentNo5);
                }else {
                    mSportsApp.TyrLoginAction(mContext,
                            getString(R.string.sports_love_title),
                            getString(R.string.try_to_login));
                }
                break;

            //跳转设置
            case R.id.sports_setting_me:
                Intent userIntent = new Intent(mContext,
                        FoxSportsSettingsActivity.class);
                startActivityForResult(userIntent, 2);
                break;
            //跳转附近的人
            case R.id.sports_nearby_me:
                if (!mSportsApp.LoginOption) {
                    mSportsApp.TyrLoginAction(mContext,
                            getString(R.string.sports_love_title),
                            getString(R.string.try_to_login));
                } else if (!mSportsApp.LoginNet) {
                    mSportsApp.NoNetLogin(mContext);

                } else {
                    Intent intentmap = new Intent(mContext,
                            NewYuePaoGaoDeActivity.class);
                    startActivity(intentmap);
                }
                break;

            //我的动态
            case R.id.ll_mydongtai:
                if (!mSportsApp.isOpenNetwork()) {
                    Toast.makeText(
                            getActivity(),
                            getResources().getString(
                                    R.string.error_cannot_access_net),
                            Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(mContext, MyConditionActivity.class);
                    intent.putExtra("ID", mSportsApp.getSportUser().getUid());
                    intent.putExtra("isMyDongtai",2);
                    mContext.startActivity(intent);
                }
                break;

            //我的关注页面
            case R.id.ll_myguanzhu:
                if (!mSportsApp.LoginOption) {
                    mSportsApp.TyrLoginAction(getActivity(),
                            getString(R.string.sports_love_title),
                            getString(R.string.try_to_login));
                } else if (!mSportsApp.LoginNet) {
                    mSportsApp.NoNetLogin(getActivity());
                } else {
                    // setSelection(5);
                    Intent intentmap = new Intent(mContext,
                            NewGuanZhuActivity.class);
                    intentmap.putExtra("fraomMe",1);
                    startActivity(intentmap);
                    if (mSportsApp.config == 1 && wService != null)
                        wService.setMe(true);
                }

                break;

            //我的粉丝页面
            case R.id.ll_myfensi:
                // Toast.makeText(mContext,"跳转我的粉丝页面",Toast.LENGTH_SHORT).show();
                if (!mSportsApp.LoginOption) {
                    mSportsApp.TyrLoginAction(getActivity(),
                            getString(R.string.sports_love_title),
                            getString(R.string.try_to_login));
                } else if (!mSportsApp.LoginNet) {
                    mSportsApp.NoNetLogin(getActivity());

                } else {
                    Intent intentmap =  new Intent(mContext,
                            NewFansActivity.class);
                    intentmap.putExtra("fromMe",1);
                    startActivity(intentmap);
                    if (mSportsApp.config == 1 && wService != null)
                        wService.setMe(true);
                }
                break;

            //退出登录
            case R.id.exit_me:
                getActivity().stopService(new Intent(getActivity(), MessageService.class));
                shot();
                break;

//            case R.id.tv_mydongtai:
//
//
//                break;
//            case R.id.tv_myguanzhu:
//
//
//                break;
//            case R.id.tv_myfensi:
//
//
//                break;
            case R.id.bt_ok:
                if (window != null && window.isShowing()) {
                    window.dismiss();
                }
                SharedPreferences sp = getActivity().getSharedPreferences("user_login_info",
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = sp.edit();
                edit.remove("account").commit();
//                if (mSportsApp.isOpenNetwork()) {
//                    waitShowDialog();
//                    (new UpLoadTempTask()).execute();
//                } else {
                    startActivity(new Intent(getActivity(),
                        LoginActivity.class));
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置
                    startActivity(intent);

                    System.exit(0);
//                }
                break;
            case R.id.bt_cancel:
                if (window != null && window.isShowing()) {
                    window.dismiss();
                }
                break;

            default:
                break;
        }
    }

    public void shot() {
        if (window != null && window.isShowing())
            return;
        LayoutInflater inflater = LayoutInflater.from(getActivity());
//		mview = (LinearLayout) inflater.inflate(R.layout.exit_login, null);
        mview = (LinearLayout) inflater.inflate(R.layout.sports_dialog1, null);
        mview.findViewById(R.id.bt_ok).setOnClickListener(this);
        mview.findViewById(R.id.bt_cancel).setOnClickListener(this);
        ((TextView) mview.findViewById(R.id.message)).setText(getString(R.string.confirm_exit1));
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        window = new PopupWindow(mview, width - SportsApp.dip2px(20),
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        // 设置弹出框大小
        // window.setHeight(300);
        // 设置出场动画：
        window.setAnimationStyle(R.style.AnimationPopup);
        window.setOutsideTouchable(true);// 设置PopupWindow外部区域是否可触摸
        window.setBackgroundDrawable(new BitmapDrawable());
        window.showAtLocation(top_title_layout, Gravity.CENTER, 0, 0);
        window.setOnDismissListener(this);
        mPopMenuBack.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDismiss() {
        mPopMenuBack.setVisibility(View.GONE);
    }

    // 发送今日步数到服务器
    class UpLoadTempTask extends AsyncTask<Integer, Integer, ApiBack> {

        @Override
        protected ApiBack doInBackground(Integer... arg0) {
            // TODO Auto-generated method stub
            ApiBack apiBack = null;
            try {
                if (!"".equals(mSportsApp.getSessionId())
                        && mSportsApp.getSessionId() != null) {
//					SharedPreferences mState = getSharedPreferences(
//							"UID"
//									+ Integer.toString(mSportsApp
//											.getSportUser().getUid()), 0);
//					int mint = mState.getInt("steps", 0);
                    apiBack = ApiJsonParser.uploadSportTemp(
                            mSportsApp.getSessionId(), CalcStepCounter());
                }

            } catch (ApiNetException e) {
                e.printStackTrace();
            } catch (ApiSessionOutException e) {
                e.printStackTrace();
            }
            return apiBack;
        }

        @Override
        protected void onPostExecute(ApiBack result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (result != null) {
            }
            if (mLoadProgressDialog != null)
                if (mLoadProgressDialog.isShowing())
                    mLoadProgressDialog.dismiss();
			startActivity(new Intent(getActivity(),
					LoginActivity.class));
			Intent intent = new Intent();
			intent.setClass(getActivity(), LoginActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置
			startActivity(intent);
            System.exit(0);
        }
    }

    // 从数据库查询到今日运动公里数再得到今日步数
    private int CalcStepCounter() {
        double sports_juli = 0;
        int sports_cishu = 0;

        List<SportContionTaskDetail> list = null;
        SportSubTaskDB taskDBhelper = SportSubTaskDB.getInstance(getActivity());

        // list = taskDBhelper.getTasksByDate(mUid, getStringDate(0));
        list = taskDBhelper.getTasksByDate(mSportsApp.getSportUser().getUid(), getTodayDate());
        sports_cishu = list.size();

        if (sports_cishu > 0) {
            for (int i = 0; i < sports_cishu; i++) {
                //只有登山 走路 跑步才计算步数
                if (list.get(i).getSports_type() == SportsType.TYPE_WALK
                        || list.get(i).getSports_type() == SportsType.TYPE_RUN
                        || list.get(i).getSports_type() == SportsType.TYPE_CLIMBING) {
                    if (SportTaskUtil.getNormalRange(list.get(i)
                                    .getSports_type(), list.get(i).getSportVelocity(),
                            Integer.parseInt(list.get(i).getSportTime()))) {
                        sports_juli += list.get(i).getSportDistance();
                    }
                }
            }
        } else {
            sports_juli = 0;
        }

        sports_cishu = (int) (sports_juli * 10000 / 6);
        return sports_cishu;
    }

    private String getTodayDate() {
        String todayDate = null;
        Long todayTime = System.currentTimeMillis();
        Date date = new Date(todayTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        todayDate = sdf.format(date);
        Log.i("sport_sub", "todayDate:" + todayDate);
        return todayDate;
    }

    //左上角跳转金币商城点击事件
    class clickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            // 金币商城
            if (mSportsApp.LoginOption) {
                if (mSportsApp.isOpenNetwork()) {
                    startActivity(new Intent(getActivity(), YunHuWebViewActivity.class));
                } else {
                    Toast.makeText(
                            getActivity(),
                            getResources().getString(
                                    R.string.error_cannot_access_net),
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                mSportsApp.TyrLoginAction(mContext,
                        getString(R.string.sports_love_title),
                        getString(R.string.try_to_login));
            }
        }
    }

    private void waitShowDialog() {
        // TODO Auto-generated method stub
        if (mLoadProgressDialog2 == null) {
            mLoadProgressDialog2 = new Dialog(mContext, R.style.sports_dialog);
            LayoutInflater mInflater = getActivity().getLayoutInflater();
            View v1 = mInflater.inflate(R.layout.bestgirl_progressdialog, null);
            mDialogMessage = (TextView) v1.findViewById(R.id.message);
            mDialogMessage.setText(R.string.bestgirl_wait);
            v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
            mLoadProgressDialog2.setContentView(v1);
        }
        if (mLoadProgressDialog2 != null)
            if (!mLoadProgressDialog2.isShowing() && !getActivity().isFinishing())
                mLoadProgressDialog2.show();
    }

    //点击右上角跳转个人设置
    class clickListener_bianji implements OnClickListener {

        @Override
        public void onClick(View v) {
            if (!mSportsApp.isOpenNetwork()) {
                Toast.makeText(
                        getActivity(),
                        getResources().getString(
                                R.string.error_cannot_access_net),
                        Toast.LENGTH_SHORT).show();
            }else{
                Intent intent = new Intent(mContext, PersonalPageMainActivity.class);
                intent.putExtra("ID", mSportsApp.getSportUser().getUid());
                intent.putExtra("isMyDongtai",2);
                mContext.startActivity(intent);
            }
        }
    }

    //对动态数、关注数、和粉丝赋值改变颜色
    private void setContent(TextView textView, String content, int num,
                            int nextNum) {
        int tempLength = content.length();
        SpannableString ss = new SpannableString(content);
        ss.setSpan(new ForegroundColorSpan(Color.parseColor("#333333")), 0,
                num, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (nextNum == 0) {
            ss.setSpan(new ForegroundColorSpan(Color.parseColor("#333333")),
                    num, tempLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            ss.setSpan(new ForegroundColorSpan(Color.parseColor("#333333")),
                    num, nextNum, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new ForegroundColorSpan(Color.parseColor("#333333")),
                    nextNum, tempLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        textView.setText(ss);
    }

}






