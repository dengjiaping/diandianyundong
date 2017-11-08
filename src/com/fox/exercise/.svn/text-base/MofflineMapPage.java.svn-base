package com.fox.exercise;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.fox.exercise.MofflineExpand.onCheckedPageListener;
import com.fox.exercise.http.FunctionStatic;
import com.fox.exercise.view.MoffMapSwitchView;
import com.fox.exercise.view.MoffMapSwitchView.OnCheckedChangeListener;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.util.ArrayList;

import cn.ingenic.indroidsync.SportsApp;


public class MofflineMapPage extends FragmentActivity implements OnClickListener {
    //private Button localButton,cityButton;
    private ViewPager mPager;
    private ArrayList<Fragment> fragmentList;
    private MoffMapSwitchView mapSwitchView;
    private boolean isSwith = false;
    private long preTime = 0;
    private SportsApp mSportsApp;

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sports_moff_friends);
        mSportsApp = (SportsApp) getApplication();
        mSportsApp.addActivity(this);

        //友盟推送
        PushAgent.getInstance(this).onAppStart();

        InitViewPager();
    }

    private void InitViewPager() {
        findViewById(R.id.bt_back).setOnClickListener(this);
        mPager = (ViewPager) findViewById(R.id.moff_viewpager);
        mapSwitchView = (MoffMapSwitchView) findViewById(R.id.switch_moff);
        fragmentList = new ArrayList<Fragment>();
        MofflineExpand exPandFragment = new MofflineExpand();
        MofflineListView listViewFragment = new MofflineListView();
        fragmentList.add(exPandFragment);
        fragmentList.add(listViewFragment);
        mPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(),
                fragmentList));
        mPager.setCurrentItem(0);
        mPager.setOffscreenPageLimit(2);
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
        mapSwitchView.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(boolean isChecked) {
                // TODO Auto-generated method stub
                isSwith = true;
                if (isChecked) {
                    mPager.setCurrentItem(0);
                } else {
                    mPager.setCurrentItem(1);
                }

            }
        });
        exPandFragment.setPageListener(new onCheckedPageListener() {

            @Override
            public void onCheckedPage() {
                // TODO Auto-generated method stub
                mPager.setCurrentItem(1);
            }
        });
    }

    class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {
        ArrayList<Fragment> list;

        public MyFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> list) {

            super(fm);
            this.list = list;

        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Fragment getItem(final int arg0) {

            return list.get(arg0);
        }

    }

    class MyOnPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onPageSelected(int position) {
            // TODO Auto-generated method stub
            if (isSwith == true) {
                isSwith = false;
            } else {
                mapSwitchView.onCheckPosition(position + 1);
            }
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MobclickAgent.onResume(this);
        preTime = FunctionStatic.onResume();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPause(this);
        FunctionStatic.onPause(this, FunctionStatic.FUNCTION_DOWNLOAD_MAP_BAIDU, preTime);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SportsApp app = (SportsApp) getApplication();
        SharedPreferences sp = getSharedPreferences("user_login_info", Context.MODE_PRIVATE);
        if (!"".equals(sp.getString("account", "")) && app.LoginOption) {
            if (app.getSessionId() == null || app.getSessionId().equals("")) {
                finish();
            }
        } else if ("".equals(sp.getString("account", "")) && app.LoginOption) {
            if (app.getSessionId() == null || app.getSessionId().equals("")) {
                finish();
            }
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        fragmentList = null;
        mSportsApp.removeActivity(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_back:
                finish();
                break;
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
