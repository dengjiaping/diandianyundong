package com.fox.exercise.newversion.newact;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ingenic.indroidsync.SportsApp;

import com.fox.exercise.AbstractBaseOtherActivity;
import com.fox.exercise.FriendFragment;
import com.fox.exercise.R;
import com.fox.exercise.SportExceptionHandler;
import com.fox.exercise.SportsExceptionHandler;
import com.fox.exercise.WholeFragment;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.api.entity.UserDetail;
import com.fox.exercise.api.entity.UserRank;
import com.fox.exercise.http.FunctionStatic;
import com.fox.exercise.view.PullToRefreshGridView;
import com.umeng.analytics.MobclickAgent;

import android.os.Message;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author loujungang 新排名 activity
 */
public class NewRanksActivity extends AbstractBaseOtherActivity implements OnClickListener {
    private final static String TAG = "RankActivity";

    private ViewPager mPager_sub;
    private int currIndex_sub = 1;
    int sex = 2;
    private ProgressDialog mypDialog;
    private TextView title_week;
    private TextView title_month;
    static int preIndex;
    private GridView listView;
    private PullToRefreshGridView mPullRefreshGridView;
    //	private SportsApp sportsApp;
    private Dialog mProgressDialog;
    private Map<Integer, List<UserRank>> persistUserRanks = new HashMap<Integer, List<UserRank>>();
    private Map<Integer, Integer> loadTimesIndex = new HashMap<Integer, Integer>();
    private SportExceptionHandler mExceptionHandler = null;

    private TextView mydate;
    private long preTime = 0;

    private ImageView image;
    private int mMoveSize;
    private ArrayList<Fragment> fragmentList;
    SportsApp mSportsApp;
    private Context mContext;
    private static final int LEFT_TITLE_VIEW = 111;
    private static final int LEFT_TITLE_BUTTON = 112;

    @Override
    public void initIntentParam(Intent intent) {
        // TODO Auto-generated method stub
        title = getString(R.string.slimgirl_sort);
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub

    }

    @Override
    public void setViewStatus() {
        // TODO Auto-generated method stub
        View view = LayoutInflater.from(this).inflate(
                R.layout.sport_rank_layout, null);
        top_title_layout.addView(view);
        // 主体
        showContentView(R.layout.sports_rank_content);
        left_ayout.setId(LEFT_TITLE_VIEW);
        left_ayout.setOnClickListener(this);
        leftButton.setId(LEFT_TITLE_BUTTON);
        leftButton.setOnClickListener(this);
        mContext = this;
        mSportsApp = (SportsApp) getApplication();
        mSportsApp.addActivity(this);
        InitImage();

        mExceptionHandler = new SportExceptionHandler(this);
        initProgressDialog();
        InitViewPager();
    }

    @Override
    public void onPageResume() {
        // TODO Auto-generated method stub
        preTime = FunctionStatic.onResume();
        MobclickAgent.onPageStart("RankActivity"); // 统计页面
    }

    @Override
    public void onPagePause() {
        // TODO Auto-generated method stub
        showProgressDialog(false);
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        if (mPullRefreshGridView != null) {
            mPullRefreshGridView = null;
        }
        if (listView != null) {
            listView.setAdapter(null);
            listView = null;
        }
        FunctionStatic.onPause(this, FunctionStatic.FUNCTION_RANKING, preTime);
        MobclickAgent.onPageEnd("RankActivity");
    }

    @Override
    public void onPageDestroy() {
        // TODO Auto-generated method stub
        if (persistUserRanks != null) {
            persistUserRanks.clear();
            persistUserRanks = null;
        }
        if (loadTimesIndex != null) {
            loadTimesIndex.clear();
            loadTimesIndex = null;
        }
        if (listView != null) {
            listView.setAdapter(null);
            listView = null;
        }
        mSportsApp.removeActivity(this);
//		mSportsApp=null;
//		sportsApp = null;

    }

    private void InitImage() {
        /*
         * image = (ImageView) getActivity().findViewById(R.id.my_cursor_bar);
		 * DisplayMetrics dm = new DisplayMetrics();
		 * getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		 * int screenW = dm.widthPixels; Log.e(TAG,
		 * "*************screenW="+screenW); mMoveSize = (screenW -
		 * SportsApp.getInstance().dip2px(85))/ 2; Log.e(TAG,
		 * "*************mMoveSize="+mMoveSize);
		 *
		 * LayoutParams para = image.getLayoutParams(); para.width = mMoveSize;
		 * //para.height = 53; image.setLayoutParams(para);
		 */

        image = (ImageView) findViewById(R.id.my_cursor_bar);
        BitmapDrawable bitmap = (BitmapDrawable) image.getDrawable();
        mMoveSize = bitmap.getBitmap().getWidth()
                + SportsApp.getInstance().dip2px(2);
    }

    private void initProgressDialog() {
        mProgressDialog = new Dialog(mContext, R.style.sports_dialog);
        LayoutInflater mInflater = getLayoutInflater();
        View v = mInflater.inflate(R.layout.sports_progressdialog, null);
        TextView message = (TextView) v.findViewById(R.id.message);
        message.setText(R.string.loading);
        v.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
        mProgressDialog.setContentView(v);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setCanceledOnTouchOutside(false);
    }

    /**
     * 初始化viewpager中控件
     */
    public void InitViewPager() {

        mPager_sub = (ViewPager) findViewById(R.id.vPager);

        fragmentList = new ArrayList<Fragment>();
        FriendFragment friendFragment = new FriendFragment();
        WholeFragment wholeFragment = new WholeFragment();

        fragmentList.add(friendFragment);
        fragmentList.add(wholeFragment);
        mPager_sub.setAdapter(new MyPagerAdapter(getSupportFragmentManager(),
                fragmentList));

        mPager_sub.setCurrentItem(1);
        mPager_sub.setOffscreenPageLimit(2);
        mPager_sub.setOnPageChangeListener(new MyOnPageChangeListener_sub());
        initSubCoursor();

        title_week = (TextView) findViewById(R.id.title_week);
        title_month = (TextView) findViewById(R.id.title_month);

        title_week.setOnClickListener(new MyOnClickListener_sub(0));
        title_month.setOnClickListener(new MyOnClickListener_sub(1));

    }

    /**
     * 初始化日期
     */
    private void initSubCoursor() {
        mydate = (TextView) findViewById(R.id.myDate);
        mydate.setText(getDateOfYesterday().toString());
    }

    public String getDateOfYesterday() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        Calendar c = Calendar.getInstance();
        long t = c.getTimeInMillis();
        long l = t - 24 * 3600 * 1000;
        Date d = new Date(l);
        String s = sdf.format(d);
        return s;
    }

    /**
     * viepager适配器
     *
     * @author dong.qu
     */
    public class MyPagerAdapter extends FragmentStatePagerAdapter {

        public ArrayList<Fragment> list;

        public MyPagerAdapter(FragmentManager fm, ArrayList<Fragment> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public Fragment getItem(int arg0) {
            // TODO Auto-generated method stub
            return list.get(arg0);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
        }
    }

    public class MyOnPageChangeListener_sub implements OnPageChangeListener {

        public void onPageSelected(int arg0) {
            Log.i(TAG, "MyOnPageChangeListener onPageSelected : " + arg0
                    + "currIndex_sub : " + currIndex_sub);
            TranslateAnimation anim = new TranslateAnimation(mMoveSize * arg0,
                    mMoveSize * currIndex_sub, 0, 0);
            anim.setFillAfter(true);
            anim.setDuration(200);
            image.startAnimation(anim);

            switch (arg0) {

                case 0:
                    title_week.setTextColor(getResources().getColor(
                            R.color.sports_popular_title_selected));
                    title_month.setTextColor(getResources().getColor(
                            R.color.sports_popular_title_normal));

                    break;
                case 1:
                    title_week.setTextColor(getResources().getColor(
                            R.color.sports_popular_title_normal));
                    title_month.setTextColor(getResources().getColor(
                            R.color.sports_popular_title_selected));

                    break;

                case 2:
                    title_week.setTextColor(getResources().getColor(
                            R.color.sports_popular_title_normal));
                    title_month.setTextColor(getResources().getColor(
                            R.color.sports_popular_title_normal));

                    break;

            }
            currIndex_sub = arg0;
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        public void onPageScrollStateChanged(int arg0) {

        }
    }

    /**
     * 如果未点击选择，默认进入第一个，即 index = 0; 点击进入好友还是全部
     *
     * @author dong.qu
     */
    public class MyOnClickListener_sub implements View.OnClickListener {
        private int index = 1;

        public MyOnClickListener_sub(int i) {
            index = i;
            Log.i(TAG + ".MyOnClickListener_sub", "i:" + i);
        }

        public void onClick(View v) {
            if (index == 0 || index == 1 || index == 2) {
                sex = 2;
            } else {
                sex = 1;
            }
            mPager_sub.setCurrentItem(index);
        }
    }

    ;

    private void showProgressDialog(boolean bshow) {
        if (mypDialog == null) {
            mypDialog = new ProgressDialog(mContext);
            mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mypDialog.setMessage(getResources().getString(R.string.loading));
            mypDialog.setIndeterminate(false);
            mypDialog.setCancelable(true);
            mypDialog.setCanceledOnTouchOutside(false);
            mypDialog.setOnCancelListener(new OnCancelListener() {
                @Override
                public void onCancel(DialogInterface arg0) {
                    mypDialog.dismiss();
                }
            });
        }
        if (bshow) {
            mypDialog.show();
        } else {
            mypDialog.dismiss();
        }

    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case LEFT_TITLE_VIEW:
            case LEFT_TITLE_BUTTON:
                GetUserNameThread thread = new GetUserNameThread();
                thread.start();
                finish();
                break;

            default:
                break;
        }

    }


    class GetUserNameThread extends Thread {
        @Override
        public void run() {
            if (mSportsApp.getSessionId() == null || "".equals(mSportsApp.getSessionId()))
                return;
            try {
                UserDetail detail = ApiJsonParser.refreshRank(mSportsApp
                        .getSessionId());
                if (detail != null) {
                    mSportsApp.setSportUser(detail);
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

}
