package com.fox.exercise.newversion.newact;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.fox.exercise.AbstractBaseActivity;
import com.fox.exercise.R;
import com.fox.exercise.map.SportingMapActivityGaode;

import cn.ingenic.indroidsync.SportsApp;

/**
 * 运动中设置页面 loujungang
 */
public class SportstingSettingActivity extends AbstractBaseActivity implements View.OnClickListener{
    private SportsApp mSportsApp;
    private SharedPreferences foxSportSetting,voiceSportSetting;
    private boolean isVoiceON,isOpen;
    private ImageView is_close_light,is_close_yuyin;
    private LinearLayout playdis_layout;
    private RadioGroup playdis_group;
    private int playdis=0;//得到语音播报是按照公里
    private int playtime=0;//得到语音播报是按照时间
    private RadioButton playdis_radiobutton1,playdis_radiobutton2,playdis_radiobutton3,
            playtime_radiobutton1,playtime_radiobutton2,playtime_radiobutton3;

    @Override
    public void initIntentParam(Intent intent) {
        mSportsApp = (SportsApp) getApplication();
        title=getString(R.string.set);
    }

    @Override
    public void initView() {
        showContentView(R.layout.activity_sportsting_setting);
        foxSportSetting = getSharedPreferences("sports"
                + mSportsApp.getSportUser().getUid(), 0);
        voiceSportSetting = getSharedPreferences("voice_sports", 0);
        playdis=voiceSportSetting.getInt("playdis",0);
        playtime=voiceSportSetting.getInt("playtime",0);

        playdis_layout= (LinearLayout) findViewById(R.id.playdis_layout);
        playdis_group= (RadioGroup) findViewById(R.id.playdis_group);
        playdis_radiobutton1= (RadioButton) findViewById(R.id.playdis_radiobutton1);
        playdis_radiobutton2= (RadioButton) findViewById(R.id.playdis_radiobutton2);
        playdis_radiobutton3= (RadioButton) findViewById(R.id.playdis_radiobutton3);
        playtime_radiobutton1= (RadioButton) findViewById(R.id.playtime_radiobutton1);
        playtime_radiobutton2= (RadioButton) findViewById(R.id.playtime_radiobutton2);
        playtime_radiobutton3= (RadioButton) findViewById(R.id.playtime_radiobutton3);
        playdis_group
                .setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(RadioGroup radiogroup, int id) {
                        // TODO Auto-generated method stub
                        switch (id) {
                            case R.id.playdis_radiobutton1:
                                playdis=1;
                                playtime=0;
                                setPlayRoundNum();
                                playtime_radiobutton1.setChecked(false);
                                playtime_radiobutton2.setChecked(false);
                                playtime_radiobutton3.setChecked(false);
                                break;
                            case R.id.playdis_radiobutton2:
                                playdis=2;
                                playtime=0;
                                setPlayRoundNum();
                                playtime_radiobutton1.setChecked(false);
                                playtime_radiobutton2.setChecked(false);
                                playtime_radiobutton3.setChecked(false);
                                break;
                            case R.id.playdis_radiobutton3:
                                playdis=5;
                                playtime=0;
                                setPlayRoundNum();
                                playtime_radiobutton1.setChecked(false);
                                playtime_radiobutton2.setChecked(false);
                                playtime_radiobutton3.setChecked(false);
                                break;
                            case R.id.playtime_radiobutton1:
                                playdis=0;
                                playtime=5;
                                setPlayRoundNum();
                                playdis_radiobutton1.setChecked(false);
                                playdis_radiobutton2.setChecked(false);
                                playdis_radiobutton3.setChecked(false);
                                break;
                            case R.id.playtime_radiobutton2:
                                playdis=0;
                                playtime=10;
                                setPlayRoundNum();
                                playdis_radiobutton1.setChecked(false);
                                playdis_radiobutton2.setChecked(false);
                                playdis_radiobutton3.setChecked(false);
                                break;
                            case R.id.playtime_radiobutton3:
                                playdis=0;
                                playtime=15;
                                setPlayRoundNum();
                                playdis_radiobutton1.setChecked(false);
                                playdis_radiobutton2.setChecked(false);
                                playdis_radiobutton3.setChecked(false);
                                break;
                        }
                    }
                });
        is_close_light=(ImageView) findViewById(R.id.is_close_light);
        isOpen = foxSportSetting.getBoolean("lockScreen", false);
        if (isOpen) {
            is_close_light.setBackgroundResource(R.drawable.sportting_setting_openicon);
        } else {
            is_close_light.setBackgroundResource(R.drawable.sportting_setting_closeicon);
        }
        is_close_light.setOnClickListener(this);

        is_close_yuyin=(ImageView)findViewById(R.id.is_close_yuyin);
        isVoiceON = voiceSportSetting.getBoolean("voiceon", true);
        if (isVoiceON) {
            is_close_yuyin.setBackgroundResource(R.drawable.sportting_setting_openicon);
            playdis_layout.setVisibility(View.VISIBLE);
            if(playdis==0&&playtime==0){
                playdis_radiobutton1.setChecked(true);
                playdis=1;
                SharedPreferences.Editor editor = voiceSportSetting.edit();
                editor.putInt("playdis",1);
                editor.commit();
            }else{
                if(playdis>0){
                    if(playdis==1){
                        playdis_radiobutton1.setChecked(true);
                    }else if(playdis==2){
                        playdis_radiobutton2.setChecked(true);
                    }else if(playdis==5){
                        playdis_radiobutton3.setChecked(true);
                    }
                }else if(playtime>0){
                    if(playtime==5){
                        playtime_radiobutton1.setChecked(true);
                    }else if(playtime==10){
                        playtime_radiobutton2.setChecked(true);
                    }else if(playtime==15){
                        playtime_radiobutton3.setChecked(true);
                    }
                }
            }
        } else {
            is_close_yuyin.setBackgroundResource(R.drawable.sportting_setting_closeicon);
            playdis_layout.setVisibility(View.GONE);
        }
        is_close_yuyin.setOnClickListener(this);
    }

    @Override
    public void setViewStatus() {

    }

    @Override
    public void onPageResume() {

    }

    @Override
    public void onPagePause() {

    }

    @Override
    public void onPageDestroy() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.is_close_light:
                if (isOpen) {
                    isOpen = false;
                    is_close_light.setBackgroundResource(R.drawable.sportting_setting_closeicon);
                } else {
                    isOpen = true;
                    is_close_light.setBackgroundResource(R.drawable.sportting_setting_openicon);
                }
                SharedPreferences.Editor editor = foxSportSetting.edit();
                editor.putBoolean("lockScreen", isOpen);
                editor.commit();
                break;
            case R.id.is_close_yuyin:
                if (isVoiceON) {
                    isVoiceON = false;
                    is_close_yuyin.setBackgroundResource(R.drawable.sportting_setting_closeicon);
                    playdis_layout.setVisibility(View.GONE);
                } else {
                    isVoiceON = true;
                    is_close_yuyin.setBackgroundResource(R.drawable.sportting_setting_openicon);
                    playdis_layout.setVisibility(View.VISIBLE);
                    if(playdis==0&&playtime==0){
                        playdis_radiobutton1.setChecked(true);
                        playdis=1;
                        SharedPreferences.Editor editor1 = voiceSportSetting.edit();
                        editor1.putInt("playdis",1);
                        editor1.commit();
                    }else{
                        if(playdis>0){
                            if(playdis==1){
                                playdis_radiobutton1.setChecked(true);
                            }else if(playdis==2){
                                playdis_radiobutton2.setChecked(true);
                            }else if(playdis==5){
                                playdis_radiobutton3.setChecked(true);
                            }
                        }else if(playtime>0){
                            if(playtime==5){
                                playtime_radiobutton1.setChecked(true);
                            }else if(playtime==10){
                                playtime_radiobutton2.setChecked(true);
                            }else if(playtime==15){
                                playtime_radiobutton3.setChecked(true);
                            }
                        }
                    }
                }
                SharedPreferences.Editor editor1 = voiceSportSetting.edit();
                editor1.putBoolean("voiceon", isVoiceON);
                editor1.commit();
                break;
        }

    }

    private void setPlayRoundNum(){
        SharedPreferences.Editor editor = voiceSportSetting.edit();
        editor.putInt("playdis",playdis);
        editor.putInt("playtime",playtime);
        editor.commit();
        if(mSportsApp!=null){
            if(mSportsApp.getmSettingHandler()!=null){
                mSportsApp.getmSettingHandler().sendEmptyMessage(6);
            }
        }

    }
}
