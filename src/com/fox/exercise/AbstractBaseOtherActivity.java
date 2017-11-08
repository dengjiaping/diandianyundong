package com.fox.exercise;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.ingenic.indroidsync.SportsApp;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import cn.ingenic.indroidsync.SportsApp;

/**
 * 基础Activity类
 */
public abstract class AbstractBaseOtherActivity extends FragmentActivity {
    protected final String TAG = getClass().getSimpleName();
    protected ViewGroup m_vContentView = null;
    protected String title = "";
    protected LinearLayout right_btn;
    protected ImageButton leftButton;
    protected LinearLayout left_ayout;
    protected LinearLayout top_title_layout;
    protected TextView title_tv;
    protected SportsApp mSportsApp;

    /**
     * 初始化activity传递的参数
     */
    public abstract void initIntentParam(Intent intent);

    /**
     * 定义页面控件
     */
    public abstract void initView();

    /**
     * 设置页面控件事件和状态
     */
    public abstract void setViewStatus();

    /**
     * == onResume()
     */
    public abstract void onPageResume();

    /**
     * == onPause()
     */
    public abstract void onPagePause();

    /**
     * == onDestroy()
     */
    public abstract void onPageDestroy();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean findMethod = SmartBarUtils.findActionBarTabsShowAtBottom();
        if (!findMethod) {
            // 取消ActionBar拆分，换用TabHost
            getWindow().setUiOptions(0);
            getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        initIntentParam(getIntent());
        setContentView(R.layout.base_fragment);
        if (findMethod) {
            getActionBar().setDisplayShowHomeEnabled(false);
            getActionBar().setDisplayShowTitleEnabled(false);
            SmartBarUtils.setActionModeHeaderHidden(getActionBar(), true);
            SmartBarUtils.setActionBarViewCollapsable(getActionBar(), true);
        }
        initView();
        setViewStatus();
        mSportsApp = (SportsApp) getApplication();
        mSportsApp.addActivity(this);
        //友盟推送
        PushAgent.getInstance(this).onAppStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        onPagePause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        nullViewDrawablesRecursive(m_vContentView);
        leftButton = null;
        m_vContentView = null;
        mSportsApp.removeActivity(this);
        onPageDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        onPageResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SportsApp app = (SportsApp) getApplication();
        Log.v(TAG, "app.getSessionId() is " + app.getSessionId());
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

    public void showRightBtn(View right_view) {
        right_btn = (LinearLayout) findViewById(R.id.title_right_btn);
        if (right_btn != null) {
            right_btn.removeAllViews();
            right_btn.addView(right_view);
        }
    }

    @Override
    public void setContentView(int layout) {
        ViewGroup mainView = (ViewGroup) LayoutInflater.from(this).inflate(
                layout, null);
        setContentView(mainView);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        m_vContentView = (ViewGroup) view;
        top_title_layout = (LinearLayout) m_vContentView
                .findViewById(R.id.top_title_layout);
        title_tv = (TextView) findViewById(R.id.top_title);
        title_tv.setText(title);
        left_ayout = (LinearLayout) m_vContentView.findViewById(R.id.title_left_layout);
        left_ayout.setOnClickListener(
                new clickListener());
        leftButton = new ImageButton(this);
        leftButton.setBackgroundResource(R.drawable.sport_title_back_selector);
        left_ayout.addView(leftButton);
        leftButton.setOnClickListener(new clickListener());
    }

    public void showContentView(int layout) {
        View view = LayoutInflater.from(this).inflate(layout, null);
        LinearLayout m_llContent = (LinearLayout) findViewById(R.id.main_content);
        if (m_llContent != null) {
            m_llContent.removeAllViews();
            m_llContent.addView(view, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        }
    }

    class clickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            finish();
        }
    }

    protected void nullViewDrawablesRecursive(View view) {
        if (view != null) {
            try {
                ViewGroup viewGroup = (ViewGroup) view;
                int childCount = viewGroup.getChildCount();
                for (int index = 0; index < childCount; index++) {
                    View child = viewGroup.getChildAt(index);
                    nullViewDrawablesRecursive(child);
                }
            } catch (Exception e) {
            }
            nullViewDrawable(view);
        }
    }

    protected void nullViewDrawable(View view) {
        try {
            view.setBackgroundDrawable(null);
            if (view instanceof ImageView) {
                ImageView imageView = (ImageView) view;
                imageView.setImageDrawable(null);
                imageView.setBackgroundDrawable(null);
            }
        } catch (Exception e) {
        }
    }

}
