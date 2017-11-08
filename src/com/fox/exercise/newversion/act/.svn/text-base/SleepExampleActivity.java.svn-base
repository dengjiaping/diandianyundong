package com.fox.exercise.newversion.act;

import android.content.Intent;

import com.fox.exercise.AbstractBaseActivity;
import com.fox.exercise.R;
import com.umeng.analytics.MobclickAgent;

public class SleepExampleActivity extends AbstractBaseActivity {

    @Override
    public void initIntentParam(Intent intent) {
        // TODO Auto-generated method stub

    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub

    }

    @Override
    public void setViewStatus() {
        // TODO Auto-generated method stub
        showContentView(R.layout.sleepexample);
        title_tv.setText("睡眠说明");
    }

    @Override
    public void onPageResume() {
        // TODO Auto-generated method stub
        MobclickAgent.onPageStart("SleepExampleActivity");
    }

    @Override
    public void onPagePause() {
        // TODO Auto-generated method stub
        MobclickAgent.onPageEnd("SleepExampleActivity");
    }

    @Override
    public void onPageDestroy() {
        // TODO Auto-generated method stub

    }

}
