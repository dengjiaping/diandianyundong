package com.fox.exercise;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fox.exercise.api.ApiConstant;
import com.fox.exercise.api.MessageService;
import com.fox.exercise.api.entity.UserDetail;
import com.fox.exercise.bitmap.util.ImageResizer;
import com.fox.exercise.http.FunctionStatic;
import com.fox.exercise.login.LoginActivity;
import com.fox.exercise.login.UpdataApplication;
import com.fox.exercise.login.UserEditActivity;
import com.fox.exercise.pedometer.ImageWorkManager;
import com.umeng.analytics.MobclickAgent;

import cn.ingenic.indroidsync.SportsApp;


public class FoxSportsSetting extends AbstractBaseFragment implements OnClickListener,
        OnDismissListener {
    private static final String TAG = "FoxSportsSetting";
    public static final int UPDATE_HEADER = 1;
    public static final int UPDATE_TRYTOLOGIN = 0x9;
    private ImageWorkManager mImageWorkerMan_Icon;
    private ImageResizer mImageWorker_Icon;
    private SportsApp mSportsApp;
    private SharedPreferences foxSportSetting, download_map_SharedPre,voiceSportSetting ;
    private Boolean isOpen;
    private Boolean isVoiceON;
    private int mMap;
    private ImageView go_lockscreen;
    private ImageView go_readsound;
    //	private RoundedImage userImage;
    private static TextView tipNum;

    private PopupWindow window;
    private LinearLayout mview;
    private RelativeLayout mPopMenuBack;
    private TextView nameText, download_default_map;
    private long preTime = 0;
    private TextView now_version;
    private TextView defaultMap;
    ;
    private Dialog alertDialog;
    private RelativeLayout relativelayout_about, relativelayout_four, personal_settings;
    private int tempMap;

    @Override
    public void beforeInitView() {
        // TODO Auto-generated method stub
        mSportsApp = (SportsApp) getActivity().getApplication();
        mSportsApp.setfHandler(fHandler);
        Log.i(TAG, "setting is created");
        title = getActivity().getResources().getString(R.string.menu_setting);
    }

    @Override
    public void setViewStatus() {
        //主体
        setContentView(R.layout.slim_setting);
        init();
    }

    @Override
    public void onPageResume() {
        preTime = FunctionStatic.onResume();
        MobclickAgent.onPageStart("FoxSportsSetting");
        selectDownloadMap();
    }

    @Override
    public void onPagePause() {
        // TODO Auto-generated method stub
        FunctionStatic.onPause(getActivity(), FunctionStatic.FUNCTION_ME, preTime);
        MobclickAgent.onPageEnd("FoxSportsSetting");
    }

    @Override
    public void onPageDestroy() {
        // TODO Auto-generated method stub
        mSportsApp.setfHandler(null);
        mSportsApp = null;
    }

    private void init() {
        /*mImageWorkerMan_Icon = new ImageWorkManager(getActivity(), 100, 100);
        mImageWorker_Icon = mImageWorkerMan_Icon.getImageWorker();
		UserDetail detail = SportsApp.getInstance().getSportUser();
		nameText = (TextView) getActivity().findViewById(R.id.user_name);
		nameText.setOnClickListener(this);

		if (mSportsApp.LoginOption) {
			initPortrait();
			nameText.setText(detail.getUname());
		} else {
			nameText.setText("匿名用户");
		}*/

//		userImage = (RoundedImage) getActivity().findViewById(
//				R.id.cover_user_photo);
        go_lockscreen = (ImageView) getActivity().findViewById(
                R.id.go_lockscreen);
        go_readsound = (ImageView) getActivity().findViewById(
                R.id.go_read_sound);
        tipNum = (TextView) getActivity().findViewById(R.id.tip_num);
//		userImage.setOnClickListener(this);
        updateTipnum();
        getActivity().findViewById(R.id.go_read_sound).setOnClickListener(this);
        getActivity().findViewById(R.id.read_sound_text).setOnClickListener(this);
        getActivity().findViewById(R.id.read_sound_layout).setOnClickListener(this);

        getActivity().findViewById(R.id.ccuntdown_settings).setOnClickListener(this);

        personal_settings = (RelativeLayout) getActivity().findViewById(R.id.personal_settings);
        personal_settings.setOnClickListener(this);
        getActivity().findViewById(R.id.go_selectmap).setOnClickListener(this);
        getActivity().findViewById(R.id.go_downloadmap).setOnClickListener(this);
        getActivity().findViewById(R.id.go_mydevice).setOnClickListener(this);
//		getActivity().findViewById(R.id.go_yunhumall).setOnClickListener(this);
        getActivity().findViewById(R.id.go_lockscreen).setOnClickListener(this);
        getActivity().findViewById(R.id.go_history).setOnClickListener(this);
        getActivity().findViewById(R.id.go_messages).setOnClickListener(this);
        getActivity().findViewById(R.id.go_opinion).setOnClickListener(this);
        getActivity().findViewById(R.id.go_gride).setOnClickListener(this);
        getActivity().findViewById(R.id.relativelayout_download_map).setOnClickListener(
                this);
        getActivity().findViewById(R.id.download_map).setOnClickListener(this);
        getActivity().findViewById(R.id.relativelayout_select_map).setOnClickListener(
                this);
        getActivity().findViewById(R.id.select_map).setOnClickListener(this);
        getActivity().findViewById(R.id.relativelayout_one).setOnClickListener(
                this);
        getActivity().findViewById(R.id.my_device).setOnClickListener(this);
//		getActivity().findViewById(R.id.relativelayout_mall)
//				.setOnClickListener(this);
//		getActivity().findViewById(R.id.yunhu_mall).setOnClickListener(this);
        getActivity().findViewById(R.id.relativelayout_two).setOnClickListener(
                this);
        getActivity().findViewById(R.id.lock_screen).setOnClickListener(this);
        getActivity().findViewById(R.id.relativelayout_three)
                .setOnClickListener(this);
        getActivity().findViewById(R.id.history).setOnClickListener(this);
        relativelayout_four = (RelativeLayout) getActivity().findViewById(R.id.relativelayout_four);
        relativelayout_four.setOnClickListener(this);
        getActivity().findViewById(R.id.messages).setOnClickListener(this);
        getActivity().findViewById(R.id.relativelayout_five).setOnClickListener(this);

        getActivity().findViewById(R.id.opinion).setOnClickListener(this);
        getActivity().findViewById(R.id.relativelayout_six).setOnClickListener(
                this);
        getActivity().findViewById(R.id.gride).setOnClickListener(this);
        getActivity().findViewById(R.id.relativelayout_seven).setOnClickListener(this);
        relativelayout_about = (RelativeLayout) getActivity().findViewById(R.id.relativelayout_about);
        relativelayout_about.setOnClickListener(this);
        if (mSportsApp.LoginOption) {
            getActivity().findViewById(R.id.relativelayout_seven).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.exit).setOnClickListener(this);
        }
        getActivity().findViewById(R.id.relativelayout_eight).setOnClickListener(this);
        getActivity().findViewById(R.id.check_update_txt).setOnClickListener(this);
        getActivity().findViewById(R.id.check_update_img).setOnClickListener(this);

        getActivity().findViewById(R.id.now_version).setOnClickListener(this);
        download_default_map = (TextView) getActivity().findViewById(R.id.download_default_map);

        mPopMenuBack = (RelativeLayout) getActivity().findViewById(R.id.set_menu_background);

        foxSportSetting = getActivity().getSharedPreferences("sports" + mSportsApp.getSportUser().getUid(), 0);
        voiceSportSetting = getActivity().getSharedPreferences("voice_sports", 0);
        isOpen = foxSportSetting.getBoolean("lockScreen", false);
        if (isOpen) {
            go_lockscreen.setBackgroundResource(R.drawable.open);
        } else {
            go_lockscreen.setBackgroundResource(R.drawable.close);
        }
        isVoiceON = voiceSportSetting.getBoolean("voiceon", true);
        if (isVoiceON) {
            go_readsound.setBackgroundResource(R.drawable.open);
        } else {
            go_readsound.setBackgroundResource(R.drawable.close);
        }
        selectDownloadMap();


        defaultMap = (TextView) getActivity().findViewById(R.id.default_map);
        mMap = foxSportSetting.getInt("map", 0);
        mSportsApp.mCurMapType = mMap;
        tempMap = mMap;
        if (mMap == SportsApp.MAP_TYPE_BAIDU) {
            defaultMap.setText(R.string.sports_map_baidu);
        } else {
            defaultMap.setText(R.string.sports_map_gaode);
        }
        now_version = (TextView) getActivity().findViewById(R.id.now_version);
        PackageManager pm = getActivity().getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(getActivity().getPackageName(),
                    0);
            String versionName = pi.versionName;
            now_version.setText(getResources().getString(R.string.now_version)
                    + "：" + versionName);
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

//	private void initPortrait() {
//		mImageWorker_Icon
//				.setLoadingImage("man".equals(SportsApp.getInstance()
//						.getSportUser().getSex()) ? R.drawable.sports_user_edit_portrait_male
//						: R.drawable.sports_user_edit_portrait);
//		mImageWorker_Icon.loadImage(SportsApp.getInstance().getSportUser()
//				.getUimg(), userImage, null, null, false);
//	}

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ccuntdown_settings:
                startActivity(new Intent(getActivity(), DaoJiShiSetting.class));
                break;
            case R.id.personal_settings:
                if (!mSportsApp.LoginOption) {
                    mSportsApp.TyrLoginAction(getActivity(),
                            getString(R.string.sports_love_title),
                            getString(R.string.try_to_login));
                } else if (!mSportsApp.LoginNet) {
                    mSportsApp.NoNetLogin(getActivity());

                } else {
                    startActivityForResult(new Intent(getActivity(), UserEditActivity.class), 10);
                }
                break;

            case R.id.relativelayout_download_map:
            case R.id.download_map:
            case R.id.go_downloadmap:
                startActivity(new Intent(getActivity(), SportsDownloadMapTypeActivity.class));
                break;
            case R.id.relativelayout_select_map:
            case R.id.select_map:
            case R.id.go_selectmap:
                SelectMapDialog(getActivity());
                break;
            case R.id.relativelayout_one:
            case R.id.my_device:
            case R.id.go_mydevice:
                startActivity(new Intent(getActivity(), MyDevice.class));
                break;
//		case R.id.go_yunhumall:
//		case R.id.relativelayout_mall:
//		case R.id.yunhu_mall:
//			startActivity(new Intent(getActivity(), YunHuWebViewActivity.class));
//			break;
            case R.id.relativelayout_two:
            case R.id.lock_screen:
            case R.id.go_lockscreen:
                // startActivity(new Intent(getActivity(),
                // LockscreenActivity.class));
                if (isOpen) {
                    isOpen = false;
                    go_lockscreen.setBackgroundResource(R.drawable.close);
                    //提示自动锁屏关闭
                    Toast.makeText(getActivity(),
                            getActivity().getResources().getString(R.string.lock_screen_shutdown),
                            Toast.LENGTH_SHORT).show();
                } else {
                    isOpen = true;
                    go_lockscreen.setBackgroundResource(R.drawable.open);
                    //提示自动锁屏打开
                    Toast.makeText(getActivity(),
                            getActivity().getResources().getString(R.string.lock_screen_open),
                            Toast.LENGTH_SHORT).show();
                }
                Editor editor = foxSportSetting.edit();
                editor.putBoolean("lockScreen", isOpen);
                editor.commit();
                break;
            case R.id.read_sound_layout:
            case R.id.read_sound_text:
            case R.id.go_read_sound:
                if (isVoiceON) {
                    isVoiceON = false;
                    go_readsound.setBackgroundResource(R.drawable.close);
                    //提示自动锁屏关闭
                    Toast.makeText(getActivity(),
                            getActivity().getResources().getString(R.string.voice_prompt_shutdown),
                            Toast.LENGTH_SHORT).show();
                } else {
                    isVoiceON = true;
                    go_readsound.setBackgroundResource(R.drawable.open);
                    //提示自动锁屏打开
                    Toast.makeText(getActivity(),
                            getActivity().getResources().getString(R.string.voice_prompt_open),
                            Toast.LENGTH_SHORT).show();
                }
                Editor editor1 = voiceSportSetting.edit();
                editor1.putBoolean("voiceon", isVoiceON);
                editor1.commit();
                break;
            case R.id.relativelayout_three:
            case R.id.history:
            case R.id.go_history:
//			if (mSportsApp.LoginOption) {
                Intent intent = new Intent(getActivity(),
                        HistoryAllActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("ID", mSportsApp.getSportUser().getUid());
                intent.putExtras(bundle);
                startActivity(intent);
//			} else {
//				mSportsApp.TyrLoginAction(getActivity(),
//						getString(R.string.sports_love_title),
//						getString(R.string.try_to_login));
//			}

                break;
            case R.id.relativelayout_four:
            case R.id.messages:
            case R.id.go_messages:
                if (!mSportsApp.LoginOption) {
                    mSportsApp.TyrLoginAction(getActivity(),
                            getString(R.string.sports_love_title),
                            getString(R.string.try_to_login));
                } else if (!mSportsApp.LoginNet) {
                    mSportsApp.NoNetLogin(getActivity());

                } else {
                    startActivityForResult(new Intent(getActivity(), FoxSportsState.class), 10);
                }

                break;

            case R.id.relativelayout_five:
            case R.id.opinion:
            case R.id.go_opinion:
                startActivity(new Intent(getActivity(), AdviceFeedback.class));
                break;
            case R.id.relativelayout_six:
            case R.id.gride:
            case R.id.go_gride:
                try {
                    Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
                    Intent i = new Intent(Intent.ACTION_VIEW, uri);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.no_android_market),
                            Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                break;
            case R.id.relativelayout_seven:
            case R.id.exit:
                getActivity().stopService(new Intent(getActivity(), MessageService.class));
                shot();

                break;
            case R.id.exit_login:
                if (window != null && window.isShowing()) {
                    window.dismiss();
                }

                SharedPreferences sp = getActivity().getSharedPreferences(
                        "user_login_info", Context.MODE_PRIVATE);
                Editor edit = sp.edit();
                // edit.clear();
                // edit.commit();
                edit.remove("account").commit();


                startActivity(new Intent(getActivity(), LoginActivity.class));
                System.exit(0);
                break;
            case R.id.exit_cancel:
                if (window != null && window.isShowing()) {
                    window.dismiss();
                }
                break;
            case R.id.user_name:
//		case R.id.cover_user_photo:
//			if (mSportsApp.LoginOption) {
//				Intent uinvite = new Intent(getActivity(),UserEditActivity.class);
//				startActivity(uinvite);
//			} else {
//				mSportsApp.TyrLoginAction(getActivity(),
//						getString(R.string.sports_love_title),
//						getString(R.string.try_to_login));
//			}
//
//			break;
            case R.id.relativelayout_eight:
            case R.id.check_update_txt:
            case R.id.check_update_img:
            case R.id.now_version:
                UpdataApplication mUpdataApplication = new UpdataApplication(getActivity());
                try {
                    mUpdataApplication.UpdateAppBackground(false);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.relativelayout_about:

                Intent aboutIntent = new Intent(getActivity(), About.class);
                startActivity(aboutIntent);
                break;
            default:
                break;
        }
    }

    Handler fHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case ApiConstant.UPDATE_MSG:
                    int totalMsg = msg.arg1;
                    setMsgbox(totalMsg);
                    break;
                case UPDATE_TRYTOLOGIN:
                    init();
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            updateTipnum();
        }
    }

    private void updateTipnum() {
        // TODO Auto-generated method stub
        UserDetail detail = SportsApp.getInstance().getSportUser();
        setMsgbox(detail.getActmsgs() + detail.getMsgCounts().getFans()
                + detail.getMsgCounts().getSportVisitor()
                + detail.getMsgCounts().getInvitesports()
                + detail.getMsgCounts().getSysmsgsports());
    }

    private void setMsgbox(int msgNum) {
        if (tipNum != null) {
            if (msgNum > 0) {

                tipNum.setVisibility(View.VISIBLE);
                tipNum.setText(msgNum > 99 ? "99+" : msgNum + "");
            } else
                tipNum.setVisibility(View.GONE);
        }
    }

    /**
     * 弹出退出框PopupWindow
     */
    public void shot() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        mview = (LinearLayout) inflater.inflate(R.layout.exit_login, null);
        mview.findViewById(R.id.exit_login).setOnClickListener(this);
        mview.findViewById(R.id.exit_cancel).setOnClickListener(this);
        window = new PopupWindow(mview, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        //设置弹出框大小
        //window.setHeight(300);
        //设置出场动画：
        window.setAnimationStyle(R.style.AnimationPopup);
        window.setOutsideTouchable(true);//设置PopupWindow外部区域是否可触摸
        window.setBackgroundDrawable(new BitmapDrawable());
        //relativelayout_four     personal_settings   userImage
        window.showAtLocation(personal_settings, Gravity.BOTTOM, 0, 0);
        //window.showAtLocation(, Gravity.BOTTOM, 0, 0);
        //window.showAsDropDown(relativelayout_four);
        window.setOnDismissListener(this);
        //设置进场动画
        final Animation animation = (Animation) AnimationUtils.loadAnimation(
                getActivity(), R.anim.slide_in_from_bottom);
        mview.startAnimation(animation);
        //点击退出设置阴影
        mPopMenuBack.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDismiss() {
        mPopMenuBack.setVisibility(View.GONE);
		/*if(tempMap!=mMap){
			Handler mainHandler=mSportsApp.getMainHandler();
			if(mainHandler!=null){
				mainHandler.sendMessage(mainHandler.obtainMessage(
						ApiConstant.UPDATE_DEFAULTMAP_MSG, mMap, 0));
			}

		}*/
    }

    private void SelectMapDialog(Context context) {
        alertDialog = new Dialog(getActivity(), R.style.sports_dialog);
        LayoutInflater mInflater = getActivity().getLayoutInflater();
        View v = mInflater.inflate(R.layout.sports_radio_dialog, null);
        final RadioButton baiduButton = (RadioButton) v.findViewById(R.id.map_baidu);
        final RadioButton gaodeButton = (RadioButton) v.findViewById(R.id.map_gaode);
        if (mMap == SportsApp.MAP_TYPE_BAIDU) {
            baiduButton.setChecked(true);
            gaodeButton.setChecked(false);
        } else {
            baiduButton.setChecked(false);
            gaodeButton.setChecked(true);
        }
        baiduButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (baiduButton.isChecked()) {
                    baiduButton.setChecked(true);
                    gaodeButton.setChecked(false);
                    mMap = SportsApp.MAP_TYPE_BAIDU;
                    mSportsApp.mCurMapType = SportsApp.MAP_TYPE_BAIDU;
                    defaultMap.setText(R.string.sports_map_baidu);

                } else {
                    baiduButton.setChecked(false);
                    gaodeButton.setChecked(true);
                    mMap = SportsApp.MAP_TYPE_GAODE;
                    mSportsApp.mCurMapType = SportsApp.MAP_TYPE_GAODE;
                    defaultMap.setText(R.string.sports_map_gaode);

                }
                Handler mainHandler = mSportsApp.getMainHandler();
                if (mainHandler != null) {
                    mainHandler.sendMessage(mainHandler.obtainMessage(
                            ApiConstant.UPDATE_DEFAULTMAP_MSG, mMap, 0));
                }
                Editor editor = foxSportSetting.edit();
                editor.putInt("map", mMap);
                editor.commit();
                alertDialog.dismiss();
            }
        });

//		gaodeButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			@Override
//			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
//				if(gaodeButton.isChecked()==false){
//					gaodeButton.setChecked(true);
//				}
//				mMap = SportsApp.MAP_TYPE_GAODE;
//				mSportsApp.mCurMapType = SportsApp.MAP_TYPE_GAODE;
//				defaultMap.setText(R.string.sports_map_gaode);
//				Editor editor = foxSportSetting.edit();
//				editor.putInt("map", 1);
//				editor.commit();
////				alertDialog.dismiss();
//			}
//		});

        v.findViewById(R.id.bt_cancel).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        alertDialog.dismiss();
                    }
                });
        v.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setContentView(v);
        alertDialog.show();
    }

    //选择离线地图
    private void selectDownloadMap() {
        download_map_SharedPre = getActivity().getSharedPreferences("sports_download", Context.MODE_PRIVATE);
        String downloadMapName = download_map_SharedPre.getString("downloadMapType", null);
        if (downloadMapName != null) {
            if (downloadMapName.equals("baidu")) {
                download_default_map.setText(R.string.sports_map_baidu);
            } else if (downloadMapName.equals("gaode")) {
                download_default_map.setText(R.string.sports_map_gaode);
            }
        }
    }
}
