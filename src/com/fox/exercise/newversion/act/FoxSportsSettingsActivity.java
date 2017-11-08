package com.fox.exercise.newversion.act;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.ingenic.indroidsync.SportsApp;

import com.fox.exercise.About;
import com.fox.exercise.AbstractBaseOtherActivity;
import com.fox.exercise.BindingDevice;
import com.fox.exercise.DaoJiShiSetting;
import com.fox.exercise.HistoryAllActivity;
import com.fox.exercise.MainFragmentActivity;
import com.fox.exercise.MapGaodeActivity;
import com.fox.exercise.MyDevice;
import com.fox.exercise.R;
import com.fox.exercise.SportsType;
import com.fox.exercise.SportsUtilities;
import com.fox.exercise.api.ApiBack;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.api.MessageService;
import com.fox.exercise.db.SportSubTaskDB;
import com.fox.exercise.http.FunctionStatic;
import com.fox.exercise.login.UpdataApplication;
import com.fox.exercise.login.UserEditActivity;
import com.fox.exercise.pedometer.SportContionTaskDetail;
import com.fox.exercise.util.SportTaskUtil;
import com.umeng.analytics.MobclickAgent;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;

/**
 * @author loujungang 运动设置页面
 */
public class FoxSportsSettingsActivity extends AbstractBaseOtherActivity
        implements OnClickListener, OnDismissListener {
    private static final String TAG = "FoxSportsSettingsActivity";
    public static final int UPDATE_HEADER = 1;
    public static final int UPDATE_TRYTOLOGIN = 0x9;
    private SportsApp mSportsApp;
    private SharedPreferences foxSportSetting, download_map_SharedPre,voiceSportSetting;
    private Boolean isOpen;
    private Boolean isVoiceON;
    private int mMap;
    private ImageView go_lockscreen;
    private ImageView go_readsound;
    private RelativeLayout mPopMenuBack;
    private TextView download_default_map;
    private long preTime = 0;
    private TextView defaultMap;
    private RadioGroup radioGroup_goal;
    private RadioButton bushu,gongli;
    private TextView tv_bushuorgongli;
    private int selectgoal;
    private boolean ischange_goalType;
    ;
    //	private Dialog alertDialog;
    private int tempMap;
    private Context mContext;

    private PopupWindow myWindow = null;
    private RelativeLayout myView;
    private EditText edit_step;
    private LinearLayout slim_settings_two;

    private RelativeLayout sports_target_settings;
    private TextView sport_target_num;
    private ImageView go_sport_target;
    private static final int IVEW_ID = 100;// 按钮ID

    private PopupWindow window;
    private LinearLayout mview;
    private RelativeLayout relativelayout_six;

    private TextView now_version;// 当前版本号

    private Dialog mLoadProgressDialog = null;
    private TextView mDialogMessage;
    private Dialog dialog;
    private String videoPath = SportsUtilities.DOWNLOAD_BASE_PATH + "/video/";
    public static final String action = "com.fox.exercise.newversion.act.FoxSportsSettingsActivity.action";

    @Override
    public void initIntentParam(Intent intent) {
        // TODO Auto-generated method stub
        mSportsApp = (SportsApp) getApplication();
        mSportsApp.setfHandler(fHandler);
        title = getResources().getString(R.string.menu_setting);
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        // 主体
        showContentView(R.layout.startsports_slim_setting);
        mContext = this;
        init();
    }

    @Override
    public void setViewStatus() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageResume() {
        // TODO Auto-generated method stub
        preTime = FunctionStatic.onResume();
        MobclickAgent.onPageStart("FoxSportsSettingsActivity");
        selectDownloadMap();
    }

    @Override
    public void onPagePause() {
        // TODO Auto-generated method stub
        FunctionStatic.onPause(mContext, FunctionStatic.FUNCTION_ME, preTime);
        MobclickAgent.onPageEnd("FoxSportsSettingsActivity");
    }

    @Override
    public void onPageDestroy() {
        // TODO Auto-generated method stub
        mSportsApp.setfHandler(null);
//		mSportsApp=null;
//		mContext=null;
    }

    private void init() {
        /*
         * mImageWorkerMan_Icon = new ImageWorkManager(getActivity(), 100, 100);
		 * mImageWorker_Icon = mImageWorkerMan_Icon.getImageWorker(); UserDetail
		 * detail = SportsApp.getInstance().getSportUser(); nameText =
		 * (TextView) getActivity().findViewById(R.id.user_name);
		 * nameText.setOnClickListener(this);
		 *
		 * if (mSportsApp.LoginOption) { initPortrait();
		 * nameText.setText(detail.getUname()); } else {
		 * nameText.setText("匿名用户"); }
		 */

        // userImage = (RoundedImage) getActivity().findViewById(
        // R.id.cover_user_photo);
        slim_settings_two = (LinearLayout) findViewById(R.id.slim_settings_two);
        go_lockscreen = (ImageView) findViewById(R.id.go_lockscreen);
        go_readsound = (ImageView) findViewById(R.id.go_read_sound);
        // userImage.setOnClickListener(this);
        findViewById(R.id.go_read_sound).setOnClickListener(this);
        findViewById(R.id.read_sound_text).setOnClickListener(this);
        findViewById(R.id.read_sound_layout).setOnClickListener(this);

        findViewById(R.id.ccuntdown_settings).setOnClickListener(this);
        findViewById(R.id.rl_train_video_cache).setOnClickListener(this);
        findViewById(R.id.go_selectmap).setOnClickListener(this);
        findViewById(R.id.go_downloadmap).setOnClickListener(this);
        findViewById(R.id.go_mydevice).setOnClickListener(this);
        findViewById(R.id.go_history).setOnClickListener(this);
        // getActivity().findViewById(R.id.go_yunhumall).setOnClickListener(this);
        findViewById(R.id.go_lockscreen).setOnClickListener(this);
        findViewById(R.id.relativelayout_download_map).setOnClickListener(this);
        findViewById(R.id.download_map).setOnClickListener(this);
        findViewById(R.id.relativelayout_select_map).setOnClickListener(this);
        findViewById(R.id.select_map).setOnClickListener(this);
        findViewById(R.id.relativelayout_one).setOnClickListener(this);
        findViewById(R.id.my_device).setOnClickListener(this);
        // getActivity().findViewById(R.id.relativelayout_mall)
        // .setOnClickListener(this);
        // getActivity().findViewById(R.id.yunhu_mall).setOnClickListener(this);
        findViewById(R.id.relativelayout_two).setOnClickListener(this);
        findViewById(R.id.lock_screen).setOnClickListener(this);
        findViewById(R.id.relativelayout_three).setOnClickListener(this);
        findViewById(R.id.history).setOnClickListener(this);
        download_default_map = (TextView) findViewById(R.id.download_default_map);

        mPopMenuBack = (RelativeLayout) findViewById(R.id.set_menu_background);

        foxSportSetting = getSharedPreferences("sports"
                + mSportsApp.getSportUser().getUid(), 0);
        voiceSportSetting = getSharedPreferences("voice_sports", 0);

        sports_target_settings = (RelativeLayout) findViewById(R.id.sports_target_settings);
        sports_target_settings.setOnClickListener(this);
        sport_target_num = (TextView) findViewById(R.id.sport_target_num);
        int goalType = foxSportSetting.getInt("select_goalType",0);
        if (goalType ==1){
            sport_target_num.setText(String.valueOf(foxSportSetting.getInt(
                    "editDistance", 0) + getResources().getString(R.string.km)));
            sport_target_num.setOnClickListener(this);
        }else{
            sport_target_num.setText(String.valueOf(foxSportSetting.getInt(
                    "editDistance", 0) + getResources().getString(R.string.steps)));
            sport_target_num.setOnClickListener(this);
        }

        go_sport_target = (ImageView) findViewById(R.id.go_sport_target);
        go_sport_target.setOnClickListener(this);
        findViewById(R.id.sports_parts_settings).setOnClickListener(this);
        findViewById(R.id.go_sports_parts).setOnClickListener(this);

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

        defaultMap = (TextView) findViewById(R.id.default_map);
        mMap = foxSportSetting.getInt("map", 0);
        mSportsApp.mCurMapType = mMap;
        tempMap = mMap;
        if (mMap == SportsApp.MAP_TYPE_BAIDU) {
            defaultMap.setText(R.string.sports_map_baidu);
        } else {
            defaultMap.setText(R.string.sports_map_gaode);
        }

        leftButton.setId(IVEW_ID);
        leftButton.setOnClickListener(this);

        relativelayout_six = (RelativeLayout) findViewById(R.id.relativelayout_six);
        relativelayout_six.setOnClickListener(this);

        findViewById(R.id.relativelayout_eight).setOnClickListener(this);
        findViewById(R.id.relativelayout_about).setOnClickListener(this);

        now_version = (TextView) findViewById(R.id.now_version);
        now_version.setText(getResources().getString(R.string.now_version)
                + ":" + getVersion());

        findViewById(R.id.sports_personal_target_settings).setOnClickListener(
                this);
        findViewById(R.id.go_personal_sport_target).setOnClickListener(this);
    }

    private long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = new FileInputStream(file);
            size = fis.available();
        } else {
            //file.createNewFile();
            Log.e("develop_debug", "文件不存在!");
        }
        return size;
    }

    private long getFileSizes(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();

        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSizes(flist[i]);
            } else {
                size = size + getFileSize(flist[i]);
            }
        }
        return size;
    }

    private static final int SIZETYPE_B = 1;//获取文件大小单位为B的double值
    private static final int SIZETYPE_KB = 2;//获取文件大小单位为KB的double值
    private static final int SIZETYPE_MB = 3;//获取文件大小单位为MB的double值
    private static final int SIZETYPE_GB = 4;//获取文件大小单位为GB的double值

    private static double FormetFileSize(long fileS, int sizeType) {
        DecimalFormat df = new DecimalFormat("#.0");
        double fileSizeLong = 0;

        switch (sizeType) {
            case SIZETYPE_B:
                fileSizeLong = Double.valueOf(df.format((double) fileS));
                break;
            case SIZETYPE_KB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
                break;
            case SIZETYPE_MB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
                break;
            case SIZETYPE_GB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1073741824));
                break;
            default:
                break;
        }
        return fileSizeLong;
    }

    private double getFileOrFilesSize(String filePath, int sizeType) {
        File file = new File(filePath);
        long blockSize = 0;

        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("develop_debug", "Exception : " + e.toString());
        }
        return FormetFileSize(blockSize, sizeType);
    }

    public void DeleteFile(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }

        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                //file.delete();
                return;
            }

            for (File f : childFile) {
                DeleteFile(f);
            }
            //file.delete();
        }
    }

    public void DeleteVideoFile(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }

        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                //file.delete();
                return;
            }

            for (File f : childFile) {
                DeleteVideoFile(f);
            }
            //file.delete();
        }
    }

    private void popCacheWindow() {
        LayoutInflater inflater = (LayoutInflater) FoxSportsSettingsActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dialog = new Dialog(FoxSportsSettingsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View layout = inflater.inflate(R.layout.activity_clear_train_video_cache,
                null);
        dialog.addContentView(layout, new LayoutParams(
                LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

        final TextView tv_pic_clear_cache = (TextView) layout.findViewById(R.id.tv_pic_clear_cache);
        tv_pic_clear_cache.setText(Double.toString(getFileOrFilesSize(SportsUtilities.DOWNLOAD_SAVE_PATH, SIZETYPE_MB)) + "M");

        final TextView tv_video_clear_cache = (TextView) layout.findViewById(R.id.tv_video_clear_cache);
        tv_video_clear_cache.setText(Double.toString(getFileOrFilesSize(videoPath, SIZETYPE_MB)) + "M");

        final CheckBox cb_pic_clear = (CheckBox) layout.findViewById(R.id.cb_pic_clear);
        final CheckBox cb_video_clear = (CheckBox) layout.findViewById(R.id.cb_video_clear);

        ((TextView) layout.findViewById(R.id.bt_ok))
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if ((!cb_pic_clear.isChecked()) && (!cb_video_clear.isChecked())) {
                            Toast.makeText(FoxSportsSettingsActivity.this, "请选择清除项目", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (cb_pic_clear.isChecked()) {
                            //if (!tv_pic_clear_cache.getText().toString().equalsIgnoreCase("0.0M")) {
                            File file = new File(SportsUtilities.DOWNLOAD_SAVE_PATH);
                            DeleteFile(file);
                            //}
                        }

                        if (cb_video_clear.isChecked()) {
                            //if (!tv_video_clear_cache.getText().toString().equalsIgnoreCase("0.0M")) {
                            File file = new File(videoPath);
                            DeleteVideoFile(file);
                            //}
                        }
                        dialog.cancel();
                        dialog = null;
                    }
                });
        ((TextView) layout.findViewById(R.id.bt_cancel))
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        dialog.cancel();
                        dialog = null;
                    }
                });
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_train_video_cache:
                popCacheWindow();
                break;
            case R.id.ccuntdown_settings:
                startActivity(new Intent(FoxSportsSettingsActivity.this,
                        DaoJiShiSetting.class));
                break;
            case R.id.personal_settings:
                if (!mSportsApp.LoginOption) {
                    mSportsApp.TyrLoginAction(mContext,
                            getString(R.string.sports_love_title),
                            getString(R.string.try_to_login));
                } else if (!mSportsApp.LoginNet) {
                    mSportsApp.NoNetLogin(mContext);

                } else {
                    startActivityForResult(
                            new Intent(FoxSportsSettingsActivity.this,
                                    UserEditActivity.class), 10);
                }
                break;

            case R.id.relativelayout_download_map:
            case R.id.download_map:
            case R.id.go_downloadmap:
//			startActivity(new Intent(FoxSportsSettingsActivity.this,
//					SportsDownloadMapTypeActivity.class));
                //只显示高德
                SharedPreferences mSharedPreferences = getSharedPreferences("sports_download", Context.MODE_PRIVATE);
                Editor editorss = mSharedPreferences.edit();
                startActivity(new Intent(this, MapGaodeActivity.class));
                editorss.putString("downloadMapType", "gaode");
                editorss.commit();
                break;
            case R.id.relativelayout_select_map:
            case R.id.select_map:
            case R.id.go_selectmap:
//			SelectMapDialog(mContext);
                break;
            case R.id.relativelayout_one:
            case R.id.my_device:
            case R.id.go_mydevice:
                startActivity(new Intent(FoxSportsSettingsActivity.this,
                        MyDevice.class));
                break;
            // case R.id.go_yunhumall:
            // case R.id.relativelayout_mall:
            // case R.id.yunhu_mall:
            // startActivity(new Intent(getActivity(), YunHuWebViewActivity.class));
            // break;
            case R.id.relativelayout_two:
            case R.id.lock_screen:
            case R.id.go_lockscreen:
                // startActivity(new Intent(getActivity(),
                // LockscreenActivity.class));
                if (isOpen) {
                    isOpen = false;
                    go_lockscreen.setBackgroundResource(R.drawable.close);
                    // 提示自动锁屏关闭
                    Toast.makeText(
                            mContext,
                            getResources().getString(R.string.lock_screen_shutdown),
                            Toast.LENGTH_SHORT).show();
                } else {
                    isOpen = true;
                    go_lockscreen.setBackgroundResource(R.drawable.open);
                    // 提示自动锁屏打开
                    Toast.makeText(mContext,
                            getResources().getString(R.string.lock_screen_open),
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
                    // 提示自动锁屏关闭
                    Toast.makeText(
                            mContext,
                            getResources()
                                    .getString(R.string.voice_prompt_shutdown),
                            Toast.LENGTH_SHORT).show();
                } else {
                    isVoiceON = true;
                    go_readsound.setBackgroundResource(R.drawable.open);
                    // 提示自动锁屏打开
                    Toast.makeText(mContext,
                            getResources().getString(R.string.voice_prompt_open),
                            Toast.LENGTH_SHORT).show();
                }
                Editor editor1 = voiceSportSetting.edit();
                editor1.putBoolean("voiceon", isVoiceON);
                editor1.commit();
                break;
            case R.id.relativelayout_three:
            case R.id.history:
            case R.id.go_history:
                // if (mSportsApp.LoginOption) {
                Intent intent = new Intent(FoxSportsSettingsActivity.this,
                        HistoryAllActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("ID", mSportsApp.getSportUser().getUid());
                intent.putExtras(bundle);
                startActivity(intent);
                // } else {
                // mSportsApp.TyrLoginAction(getActivity(),
                // getString(R.string.sports_love_title),
                // getString(R.string.try_to_login));
                // }

                break;

            case R.id.sports_goal_back:
                myWindow.dismiss();
                myView.setVisibility(View.GONE);
                mPopMenuBack.setVisibility(View.GONE);
                break;
            case R.id.sports_goal_ok:
                if (edit_step.getText().toString().trim().equals("")) {
                    Toast.makeText(mContext,
                            getResources().getString(R.string.juli_noshuru),
                            Toast.LENGTH_SHORT).show();
                } else if (selectgoal == 1 && Integer.parseInt(edit_step.getText().toString()) > 700 ){
                    Toast.makeText(mContext,
                            getResources().getString(R.string.belowto_700km),
                            Toast.LENGTH_SHORT).show();
                }  else {
                    int myNumberStep = Integer.parseInt(edit_step.getText()
                            .toString());
                    // myNumberCalories = Integer.parseInt(myEditCalories.getText()
                    // .toString());
                    SharedPreferences sharedPreferences = getSharedPreferences(
                            "sports" + mSportsApp.getSportUser().getUid(), 0);
                    Editor editors = sharedPreferences.edit();
                    editors.putInt("editDistance", myNumberStep);
                    // editor.putInt("editCalories", myNumberCalories);
                    if (ischange_goalType) {
                        editors.putInt("select_goalType", selectgoal);
                    }
                    editors.commit();
                    if (ischange_goalType){
                        if (selectgoal == 1){
                            sport_target_num.setText(Integer.parseInt(edit_step.getText().toString()) + getResources().getString(R.string.km));
                        }else{
                            sport_target_num.setText(Integer.parseInt(edit_step.getText().toString()) + getResources().getString(R.string.steps));
                        }
                    }else{
                        if (getSharedPreferences("sports"+ mSportsApp.getSportUser().getUid(), 0).getInt("select_goalType",0)==1){
                            sport_target_num.setText(Integer.parseInt(edit_step.getText().toString()) + getResources().getString(R.string.km));
                        }else{
                            sport_target_num.setText(Integer.parseInt(edit_step.getText().toString()) + getResources().getString(R.string.steps));
                        }
                    }


                    myWindow.dismiss();
                    myView.setVisibility(View.GONE);
                    mPopMenuBack.setVisibility(View.GONE);
                    Handler mainHandler = mSportsApp.getMainHandler();
                    if (mainHandler != null) {
                        mainHandler.sendMessage(mainHandler.obtainMessage(
                                MainFragmentActivity.UPDATE_SPORTSGOAL, 0, 0));
                    }
                    // setSportsgoal();
                }
                break;
            case R.id.sports_target_settings:
            case R.id.sport_target_num:
            case R.id.go_sport_target:
                shotSportsGoal();
                break;
            case R.id.sports_parts_settings:
            case R.id.go_sports_parts:
                startActivity(new Intent(FoxSportsSettingsActivity.this,
                        BindingDevice.class));
                break;
            case IVEW_ID:
                if (tempMap != mMap) {
                    // setResult(2, null);
                    // Handler mainHandler = mSportsApp.getMainHandler();
                    // if (mainHandler != null) {
                    // mainHandler
                    // .sendEmptyMessage(MainFragmentActivity.FOXSPORTSETTINGRESULT);
                    // }
                    setResult(1, null);
                }
                finish();
                break;

            case R.id.relativelayout_six:
                try {
                    Uri uri = Uri.parse("market://details?id=" + getPackageName());
                    Intent i = new Intent(Intent.ACTION_VIEW, uri);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(FoxSportsSettingsActivity.this,
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

            case R.id.sports_personal_target_settings:
            case R.id.go_personal_sport_target:
                if (!mSportsApp.LoginOption) {
                    mSportsApp.TyrLoginAction(this,
                            getString(R.string.sports_love_title),
                            getString(R.string.try_to_login));
                } else if (!mSportsApp.LoginNet) {
                    mSportsApp.NoNetLogin(this);
                } else {
                    startActivityForResult(
                            new Intent(this, UserEditActivity.class), 10);
                }
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
                case UPDATE_TRYTOLOGIN:
                    init();
                    break;
            }
        }
    };

    @Override
    public void onDismiss() {
        mPopMenuBack.setVisibility(View.GONE);
    }

//	private void SelectMapDialog(Context context) {
//		alertDialog = new Dialog(mContext, R.style.sports_dialog);
//		LayoutInflater mInflater = getLayoutInflater();
//		View v = mInflater.inflate(R.layout.sports_radio_dialog, null);
//		final RadioButton baiduButton = (RadioButton) v
//				.findViewById(R.id.map_baidu);
//		final RadioButton gaodeButton = (RadioButton) v
//				.findViewById(R.id.map_gaode);
//		if (mMap == SportsApp.MAP_TYPE_BAIDU) {
//			baiduButton.setChecked(true);
//			gaodeButton.setChecked(false);
//		} else {
//			baiduButton.setChecked(false);
//			gaodeButton.setChecked(true);
//		}
//		baiduButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			@Override
//			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
//				if (baiduButton.isChecked()) {
//					baiduButton.setChecked(true);
//					gaodeButton.setChecked(false);
//					mMap = SportsApp.MAP_TYPE_BAIDU;
//					mSportsApp.mCurMapType = SportsApp.MAP_TYPE_BAIDU;
//					defaultMap.setText(R.string.sports_map_baidu);
//
//				} else {
//					baiduButton.setChecked(false);
//					gaodeButton.setChecked(true);
//					mMap = SportsApp.MAP_TYPE_GAODE;
//					mSportsApp.mCurMapType = SportsApp.MAP_TYPE_GAODE;
//					defaultMap.setText(R.string.sports_map_gaode);
//
//				}
//				Handler mainHandler = mSportsApp.getMainHandler();
//				if (mainHandler != null) {
//					mainHandler.sendMessage(mainHandler.obtainMessage(
//							ApiConstant.UPDATE_DEFAULTMAP_MSG, mMap, 0));
//				}
//				Editor editor = foxSportSetting.edit();
//				editor.putInt("map", mMap);
//				editor.commit();
//				alertDialog.dismiss();
//			}
//		});
//
//		// gaodeButton.setOnCheckedChangeListener(new OnCheckedChangeListener()
//		// {
//		// @Override
//		// public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
//		// if(gaodeButton.isChecked()==false){
//		// gaodeButton.setChecked(true);
//		// }
//		// mMap = SportsApp.MAP_TYPE_GAODE;
//		// mSportsApp.mCurMapType = SportsApp.MAP_TYPE_GAODE;
//		// defaultMap.setText(R.string.sports_map_gaode);
//		// Editor editor = foxSportSetting.edit();
//		// editor.putInt("map", 1);
//		// editor.commit();
//		// // alertDialog.dismiss();
//		// }
//		// });
//
//		v.findViewById(R.id.bt_cancel).setOnClickListener(
//				new View.OnClickListener() {
//					@Override
//					public void onClick(View arg0) {
//						alertDialog.dismiss();
//					}
//				});
//		v.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
//		alertDialog.setCancelable(true);
//		alertDialog.setCanceledOnTouchOutside(false);
//		alertDialog.setContentView(v);
//		alertDialog.show();
//	}

    // 选择离线地图
    private void selectDownloadMap() {
        download_map_SharedPre = getSharedPreferences("sports_download",
                Context.MODE_PRIVATE);
        String downloadMapName = download_map_SharedPre.getString(
                "downloadMapType", null);
        if (downloadMapName != null) {
            if (downloadMapName.equals("baidu")) {
                download_default_map.setText(R.string.sports_map_baidu);
            } else if (downloadMapName.equals("gaode")) {
                download_default_map.setText(R.string.sports_map_gaode);
            }
        }
    }

    public void shotSportsGoal() {
        if (myWindow != null && myWindow.isShowing())
            return;
        LayoutInflater inflater = LayoutInflater.from(this);
//		myView = (RelativeLayout) inflater.inflate(R.layout.fox_sports_newgoal,
//				null);
        myView = (RelativeLayout) inflater.inflate(R.layout.sports_goal_newlayout,
                null);
        tv_bushuorgongli = (TextView) myView.findViewById(R.id.tv_bushuorgongli);
        bushu = (RadioButton) myView.findViewById(R.id.bushu);
        gongli = (RadioButton) myView.findViewById(R.id.gongli);
        radioGroup_goal = (RadioGroup) myView.findViewById(R.id.radioGroup_goal);
        edit_step = (EditText) myView.findViewById(R.id.edit_step);

        final SharedPreferences getDate = getSharedPreferences("sports"
                + mSportsApp.getSportUser().getUid(), 0);
        edit_step.setText(String.valueOf(getDate.getInt("editDistance", 0)));
        if ("0".equals(edit_step.getText().toString())) {
            edit_step.setText("0");
            edit_step.setSelection(1);
        } else {
            edit_step.setSelection(String.valueOf(getDate.getInt("editDistance", 0)).length());
        }

        int goalType = getDate.getInt("select_goalType", 0);
        if (goalType == 1){
            radioGroup_goal.check(gongli.getId());
            tv_bushuorgongli.setText(getResources().getString(R.string.km));
        }else{
            radioGroup_goal.check(bushu.getId());
            tv_bushuorgongli.setText(getResources().getString(R.string.steps));
        }
        if (getDate.getInt("select_goalType",0) == 1){
            selectgoal = 1;
        }else{
            selectgoal = 0;
        }
        radioGroup_goal.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == bushu.getId()){
                    tv_bushuorgongli.setText(getResources().getString(R.string.steps));
                    selectgoal = 0;
                    ischange_goalType = true;
                    if (getDate.getInt("select_goalType",0) == 0 && getDate.getInt("editDistance", 0) != 0 ){
                        edit_step.setText(String.valueOf(getDate.getInt("editDistance", 0)));
                        edit_step.setSelection(String.valueOf(getDate.getInt("editDistance", 0)).length());
                    }else{
                        edit_step.setText("0");
                        edit_step.setSelection(1);
                    }
                }else if(i == gongli.getId()){
                    tv_bushuorgongli.setText(getResources().getString(R.string.km));
                    ischange_goalType = true;
                    selectgoal = 1;
                    if (getDate.getInt("select_goalType",0) == 1 && getDate.getInt("editDistance", 0) != 0){
                        edit_step.setText(String.valueOf(getDate.getInt("editDistance", 0)));
                        edit_step.setSelection(String.valueOf(getDate.getInt("editDistance", 0)).length());
                    }else{
                        edit_step.setText("0");
                        edit_step.setSelection(1);
                    }
                }
            }
        });

        myView.findViewById(R.id.sports_goal_back).setOnClickListener(this);
        myView.findViewById(R.id.sports_goal_ok).setOnClickListener(this);
        // myEditCalories
        // .setText(String.valueOf(getDate.getInt("editCalories", 0)));
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        myWindow = new PopupWindow(myView,
                width - SportsApp.dip2px(40),
                RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        myWindow.setAnimationStyle(R.style.AnimationPopup);
        myWindow.setOutsideTouchable(true);
        myWindow.setBackgroundDrawable(new BitmapDrawable());
        myWindow.showAtLocation(slim_settings_two, Gravity.CENTER, 0, 0);
        myWindow.setOnDismissListener(this);
//		final Animation animation = (Animation) AnimationUtils.loadAnimation(
//				this, R.anim.slide_in_from_bottom);
//		myView.startAnimation(animation);
        mPopMenuBack.setVisibility(View.VISIBLE);
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
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        // TODO Auto-generated method stub
        super.onActivityResult(arg0, arg1, arg2);
    }

    private void waitShowDialog() {
        // TODO Auto-generated method stub
        if (mLoadProgressDialog == null) {
            mLoadProgressDialog = new Dialog(mContext, R.style.sports_dialog);
            LayoutInflater mInflater = getLayoutInflater();
            View v1 = mInflater.inflate(R.layout.bestgirl_progressdialog, null);
            mDialogMessage = (TextView) v1.findViewById(R.id.message);
            mDialogMessage.setText(R.string.bestgirl_wait);
            v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
            mLoadProgressDialog.setContentView(v1);
        }
        if (mLoadProgressDialog != null)
            if (!mLoadProgressDialog.isShowing() && !this.isFinishing())
                mLoadProgressDialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK: {
                if (tempMap != mMap) {
                    setResult(1, null);
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
