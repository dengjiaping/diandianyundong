package com.fox.exercise.newversion.act;

import cn.ingenic.indroidsync.SportsApp;

import com.fox.exercise.About;
import com.fox.exercise.AbstractBaseActivity;
import com.fox.exercise.R;
import com.fox.exercise.api.MessageService;
import com.fox.exercise.login.LoginActivity;
import com.fox.exercise.login.UpdataApplication;

import android.net.Uri;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author loujungang 用户设置退出界面
 */
public class UserSetMainActivity extends AbstractBaseActivity implements
        OnClickListener, OnDismissListener {
    //	private static final String TAG = "UserSetMainActivity";
    private SportsApp mSportsApp;
    private PopupWindow window;
    private LinearLayout mview;
    private RelativeLayout relativelayout_six;
    private RelativeLayout mPopMenuBack;

    private TextView now_version;// 当前版本号

    @Override
    public void initIntentParam(Intent intent) {
        // TODO Auto-generated method stub

    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        showContentView(R.layout.activity_user_set_main);
        mSportsApp = (SportsApp) getApplication();
        init();
    }

    @Override
    public void setViewStatus() {
        // TODO Auto-generated method stub

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

    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return this.getString(R.string.can_not_find_version_name);
        }
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.relativelayout_six:
                try {
                    Uri uri = Uri.parse("market://details?id=" + getPackageName());
                    Intent i = new Intent(Intent.ACTION_VIEW, uri);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(UserSetMainActivity.this,
                            getResources().getString(R.string.no_android_market),
                            Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                break;

            case R.id.relativelayout_eight:
                UpdataApplication mUpdataApplication = new UpdataApplication(this);
                try {
                    mUpdataApplication.UpdateAppBackground(false);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.relativelayout_about:
                Intent aboutIntent = new Intent(this, About.class);
                startActivity(aboutIntent);
                break;

            case R.id.exit:
                stopService(new Intent(this, MessageService.class));
                shot();
                break;

            case R.id.exit_login:
                if (window != null && window.isShowing()) {
                    window.dismiss();
                }

                SharedPreferences sp = getSharedPreferences("user_login_info",
                        Context.MODE_PRIVATE);
                Editor edit = sp.edit();
                // edit.clear();
                // edit.commit();
                edit.remove("account").commit();
                startActivity(new Intent(this, LoginActivity.class));
                System.exit(0);
                break;
            case R.id.exit_cancel:
                if (window != null && window.isShowing()) {
                    window.dismiss();
                }
                break;
        }

    }

    private void init() {
        mPopMenuBack = (RelativeLayout) findViewById(R.id.set_menu_background);
        relativelayout_six = (RelativeLayout) findViewById(R.id.relativelayout_six);
        relativelayout_six.setOnClickListener(this);
        findViewById(R.id.relativelayout_eight).setOnClickListener(this);
        findViewById(R.id.relativelayout_about).setOnClickListener(this);
        findViewById(R.id.exit).setOnClickListener(this);
        if (mSportsApp.LoginOption) {
            findViewById(R.id.exit).setVisibility(View.VISIBLE);
            findViewById(R.id.exit).setOnClickListener(this);
        }

        now_version = (TextView) findViewById(R.id.now_version);
        now_version.setText(getResources().getString(R.string.now_version) + ":" + getVersion());

    }

    /**
     * 弹出退出框PopupWindow
     */
    public void shot() {
        LayoutInflater inflater = LayoutInflater.from(this);
        mview = (LinearLayout) inflater.inflate(R.layout.exit_login, null);
        mview.findViewById(R.id.exit_login).setOnClickListener(this);
        mview.findViewById(R.id.exit_cancel).setOnClickListener(this);
        window = new PopupWindow(mview, LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        // 设置弹出框大小
        // window.setHeight(300);
        // 设置出场动画：
        window.setAnimationStyle(R.style.AnimationPopup);
        window.setOutsideTouchable(true);// 设置PopupWindow外部区域是否可触摸
        window.setBackgroundDrawable(new BitmapDrawable());
        // relativelayout_four personal_settings userImage
        window.showAtLocation(relativelayout_six, Gravity.BOTTOM, 0, 0);
        // window.showAtLocation(, Gravity.BOTTOM, 0, 0);
        // window.showAsDropDown(relativelayout_four);
        window.setOnDismissListener(this);
        // 设置进场动画
        final Animation animation = (Animation) AnimationUtils.loadAnimation(
                this, R.anim.slide_in_from_bottom);
        mview.startAnimation(animation);
        // 点击退出设置阴影
        mPopMenuBack.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDismiss() {
        // TODO Auto-generated method stub
        mPopMenuBack.setVisibility(View.GONE);
    }

}
