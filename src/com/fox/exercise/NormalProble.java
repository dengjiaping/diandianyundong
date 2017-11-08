package com.fox.exercise;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by zhonghuibin 常见问题.
 */
public class NormalProble extends AbstractBaseActivity implements View.OnClickListener {
    private RelativeLayout rl_personal_jibu;
    private RelativeLayout rl_personal_qq;
    private RelativeLayout rl_personal_jilu;
    @Override
    public void initIntentParam(Intent intent) {

    }

    @Override
    public void initView() {

    }

    @Override
    public void setViewStatus() {
        showContentView(R.layout.activity_normalproble_me);
        title_tv.setText(R.string.sports_normalproble_me);
        rl_personal_jibu = (RelativeLayout) findViewById(R.id.rl_personal_jibu);
        rl_personal_jibu.setOnClickListener(this);
        rl_personal_qq = (RelativeLayout) findViewById(R.id.rl_personal_qq);
        rl_personal_qq.setOnClickListener(this);
        rl_personal_jilu = (RelativeLayout) findViewById(R.id.rl_personal_jilu);
        rl_personal_jilu.setOnClickListener(this);
    }

    @Override
    public void onPageResume() {
        // TODO Auto-generated method stub
        MobclickAgent.onPageStart("NormalProble");
    }

    @Override
    public void onPagePause() {
        // TODO Auto-generated method stub
        MobclickAgent.onPageEnd("NormalProble");
    }

    @Override
    public void onPageDestroy() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_personal_jibu:
                startActivity(new Intent(NormalProble.this,Sport_JiBuproblemActivity.class));
                break;
            case R.id.rl_personal_qq:
                startActivity(new Intent(NormalProble.this,QQhealth_problemActivity.class));
                break;
            case R.id.rl_personal_jilu:
                startActivity(new Intent(NormalProble.this,problem3_me_Activity.class));
                break;

        }
    }
}
