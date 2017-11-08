package com.fox.exercise;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.ingenic.indroidsync.SportsApp;

import com.fox.exercise.api.ApiBack;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.api.entity.ActionInfo;

public class ActivityListDetail extends FragmentActivity implements OnClickListener {
    private Context mcontext = this;
    private ImageView mimg;
    private TextView introduction, registration, costs, payment, top_title;
    private ViewPager activity_viewpager;
    private ArrayList<Fragment> fragmentList;
    private TextView[] txViews;
    private int crrentposition = 0;
    private ImageButton title_left_btn;
    private Button sign_up;
    private ImageDownloader mDownloader = null;
    private int activityId;
    private ActionInfo myactionInfo;
    private Fragment introductFragment;
    private static final String TAG = "ActivityListDetail";
    private ApiBack back;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        boolean findMethod = SmartBarUtils.findActionBarTabsShowAtBottom();
        if (!findMethod) {
            // 取消ActionBar拆分，换用TabHost
            getWindow().setUiOptions(0);
            getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        setContentView(R.layout.activity_list_detail);

//		String activityPic=(String) getIntent().getCharSequenceExtra("activityPic");
//		String activityName=(String) getIntent().getCharSequenceExtra("activityName");
        activityId = getIntent().getIntExtra("actionId", 0);
        mDownloader = new ImageDownloader(mcontext);
        Log.e(TAG, "wowowowowoowowoowosessionId" + SportsApp.getInstance().getSessionId());
        Log.e(TAG, "=============================actionId" + activityId);
        init();
        InitViewPager();

        new GetActionDetail_DataTask().execute();
        if (findMethod) {
            getActionBar().setDisplayShowHomeEnabled(false);
            getActionBar().setDisplayShowTitleEnabled(false);
            SmartBarUtils.setActionModeHeaderHidden(getActionBar(), true);
            SmartBarUtils.setActionBarViewCollapsable(getActionBar(), true);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void InitViewPager() {
        activity_viewpager = (ViewPager) findViewById(R.id.activity_viewpager);
        fragmentList = new ArrayList<Fragment>();
        introductFragment = new IntroductFragment();

//        Fragment registratFragment = new RegistratFragment(activityId);
        RegistratFragment registratFragment = new RegistratFragment();
        registratFragment.setActionId(activityId);

        ActivityRankingFragment activityRankFragment = new ActivityRankingFragment();
        activityRankFragment.setActivityId(activityId);
        PaymentFragment paymentFragment = new PaymentFragment();
        paymentFragment.setActivityId(activityId);


        fragmentList.add(introductFragment);
        fragmentList.add(registratFragment);
        fragmentList.add(activityRankFragment);
        fragmentList.add(paymentFragment);
        //fragmentList.add(findFragment);
        activity_viewpager.setAdapter(new myFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));

        activity_viewpager.setCurrentItem(0);
        activity_viewpager.setOffscreenPageLimit(4);
        activity_viewpager.setOnPageChangeListener(new MyOnPageChangeListener());
    }


    class myFragmentPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> myFragments;

        public myFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> myFragments) {
            super(fm);
            this.myFragments = myFragments;
        }

        @Override
        public Fragment getItem(int arg0) {
            return myFragments.get(arg0);
        }

        @Override
        public int getCount() {
            return myFragments.size();
        }
    }

    private class MyOnPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int position) {
            txViews[position].setTextColor(Color.parseColor("#25a7f2"));
            txViews[crrentposition].setTextColor(Color.parseColor("#3a3f47"));
            txViews[position].setBackgroundResource(R.drawable.tab2_focus);
            txViews[crrentposition].setBackgroundResource(R.drawable.tab2_bg);
            crrentposition = position;
        }

    }

    class myTextviewListenter implements OnClickListener {

        private int index = 0;

        public myTextviewListenter(int index) {
            super();
            this.index = index;
        }

        @Override
        public void onClick(View arg0) {
            txViews[index].setTextColor(Color.parseColor("#25a7f2"));
            txViews[index].setBackgroundResource(R.drawable.tab2_focus);
            if (crrentposition != index) {
                txViews[crrentposition].setTextColor(Color.parseColor("#3a3f47"));
                txViews[crrentposition].setBackgroundResource(R.drawable.tab2_bg);
            }

            activity_viewpager.setCurrentItem(index);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_up:
                new GoApplyTask().execute();

                break;
            case R.id.title_left_btn:
                finish();
                break;

            default:
                break;
        }
    }

    private void init() {

        title_left_btn = (ImageButton) findViewById(R.id.title_left_btn);
        top_title = (TextView) findViewById(R.id.top_title);
        mimg = (ImageView) findViewById(R.id.activity_pic);
        introduction = (TextView) findViewById(R.id.introduction);
        registration = (TextView) findViewById(R.id.registration);
        costs = (TextView) findViewById(R.id.costs);
        payment = (TextView) findViewById(R.id.payment);
        sign_up = (Button) findViewById(R.id.sign_up);
        txViews = new TextView[]{introduction, registration, costs, payment};
        introduction.setOnClickListener(new myTextviewListenter(0));
        registration.setOnClickListener(new myTextviewListenter(1));
        costs.setOnClickListener(new myTextviewListenter(2));
        payment.setOnClickListener(new myTextviewListenter(3));
        sign_up.setOnClickListener(this);
        title_left_btn.setOnClickListener(this);
        ////

    }

    /**
     * 获取活动详情
     */
    private class GetActionDetail_DataTask extends AsyncTask<Void, Void, ActionInfo> {

        @Override
        protected ActionInfo doInBackground(Void... Params) {
            if (!SportsApp.getInstance().isOpenNetwork()) {
                return null;
            }
            Log.e(TAG, "************into***********");
            ActionInfo actionInfo = null;
            try {
                Log.e(TAG, "************into***********11111");
                actionInfo = ApiJsonParser.getActionInfo(SportsApp.getInstance().getSessionId(), activityId);
//				  Log.e(TAG, "************into***********titlle"+actionInfo.getTitle());
            } catch (ApiNetException e) {
                e.printStackTrace();
            } catch (ApiSessionOutException e) {
                e.printStackTrace();
            }
            return actionInfo;
        }

        @Override
        protected void onPostExecute(ActionInfo result) {
            super.onPostExecute(result);
            if (result == null) {
                if (!SportsApp.getInstance().isOpenNetwork()) {
                    Toast.makeText(ActivityListDetail.this,
                            getString(R.string.network_not_avaliable),
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }
            myactionInfo = result;

            System.out.print("***************getContent=" + myactionInfo.getContent());
            System.out.print("/n***************getStart_time=" + myactionInfo.getStart_time());
            top_title.setText(myactionInfo.getTitle());

            mDownloader.setType(ImageDownloader.ACTIVITYICON);
            mDownloader.download(myactionInfo.getUrl(), mimg, null);
            Handler mainHandler = SportsApp.getInstance().getmyHandler();
            if (mainHandler == null) {
                System.out.print("***********nullnullnullnullnull********");
            } else {
                System.out.print("**********fei  null*********");
                mainHandler.sendMessage(mainHandler.obtainMessage(1, myactionInfo));
            }

        }

    }

    /**
     * 获取报名接口
     */
    private class GoApplyTask extends AsyncTask<Void, Void, ApiBack> {

        @Override
        protected ApiBack doInBackground(Void... arg0) {
            ApiBack back = null;
            try {
                back = ApiJsonParser.goApply(SportsApp.getInstance().getSessionId(), activityId);
            } catch (ApiNetException e) {
                e.printStackTrace();
            }
            return back;
        }

        @Override
        protected void onPostExecute(ApiBack result) {
            super.onPostExecute(result);
            result.getFlag();
            back = result;

            Toast.makeText(mcontext, back.getMsg(), 1).show();
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
