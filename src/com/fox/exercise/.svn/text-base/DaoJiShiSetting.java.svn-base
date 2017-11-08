package com.fox.exercise;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.umeng.analytics.MobclickAgent;

import cn.ingenic.indroidsync.SportsApp;

public class DaoJiShiSetting extends AbstractBaseActivity {
    private RadioGroup mRadioGroup;
    private RadioButton djs_close;
    private RadioButton djs_3;
    private RadioButton djs_5;
    private RadioButton djs_10;
    private CheckBox djs_remind;
    private SharedPreferences sp;
    private int bt_num = 0;
    private boolean cb_sel = true;
    private ImageButton iButton;

    private void init() {
        iButton = new ImageButton(this);
        iButton.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        iButton.setBackgroundResource(R.drawable.sport_title_ok_selector);
        showRightBtn(iButton);
        right_btn.setPadding(0, 0, SportsApp.dip2px(17), 0);
//		findViewById(R.id.djs_back).setOnClickListener(this);
//		findViewById(R.id.djs_finish).setOnClickListener(this);
        mRadioGroup = (RadioGroup) findViewById(R.id.mRadioGroup);
        djs_close = (RadioButton) findViewById(R.id.djs_close);
        djs_3 = (RadioButton) findViewById(R.id.djs_3);
        djs_5 = (RadioButton) findViewById(R.id.djs_5);
        djs_10 = (RadioButton) findViewById(R.id.djs_10);
        djs_remind = (CheckBox) findViewById(R.id.djs_remind);
        sp = getSharedPreferences("save_djs", 0);
        bt_num = sp.getInt("djs_time", 3);
        switch (bt_num) {
            case 0:
                djs_close.setChecked(true);
                break;
            case 3:
                djs_3.setChecked(true);
                break;
            case 5:
                djs_5.setChecked(true);
                break;
            case 10:
                djs_10.setChecked(true);
                break;
        }
        cb_sel = sp.getBoolean("djs_remind", true);
        djs_remind.setChecked(cb_sel);
    }

//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		// TODO Auto-generated method stub
//		switch (keyCode) {
//		case KeyEvent.KEYCODE_BACK:
//			finish();
//			break;
//		}
//		return super.onKeyDown(keyCode, event);
//	}

    @Override
    public void initIntentParam(Intent intent) {
        title = getResources().getString(R.string.dis_title);

    }

    @Override
    public void initView() {
        showContentView(R.layout.djs_setting);
        init();
    }

    @Override
    public void setViewStatus() {
        iButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Editor ed = sp.edit();
                ed.putInt("djs_time", bt_num);
                ed.putBoolean("djs_remind", cb_sel);
                ed.commit();
                finish();
            }

        });
        right_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Editor ed = sp.edit();
                ed.putInt("djs_time", bt_num);
                ed.putBoolean("djs_remind", cb_sel);
                ed.commit();
                finish();
            }

        });

        mRadioGroup
                .setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(RadioGroup radiogroup, int id) {
                        // TODO Auto-generated method stub
                        switch (id) {
                            case R.id.djs_close:
                                bt_num = 0;
                                break;
                            case R.id.djs_3:
                                bt_num = 3;
                                break;
                            case R.id.djs_5:
                                bt_num = 5;
                                break;
                            case R.id.djs_10:
                                bt_num = 10;
                                break;
                        }
                    }
                });

        djs_remind
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton cb,
                                                 boolean check) {

                        if (check) {
                            cb_sel = true;
                        } else {
                            cb_sel = false;
                        }
                    }
                });

    }

    @Override
    public void onPageResume() {
        MobclickAgent.onPageStart("DaoJiShiSetting");
    }

    @Override
    public void onPagePause() {
        MobclickAgent.onPageEnd("DaoJiShiSetting");
    }

    @Override
    public void onPageDestroy() {
        // TODO Auto-generated method stub

    }

}
