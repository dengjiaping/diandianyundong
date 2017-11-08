package com.fox.exercise.login;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.ingenic.indroidsync.SportsApp;

import com.fox.exercise.AbstractBaseActivity;
import com.fox.exercise.AbstractBaseOtherFragment;
import com.fox.exercise.AllWeiboInfo;
import com.fox.exercise.FindOtherFragment;
import com.fox.exercise.FoxSportsSetting;
import com.fox.exercise.MainFragmentActivity;
import com.fox.exercise.R;
import com.fox.exercise.SportsExceptionHandler;
import com.fox.exercise.SportsUtilities;
import com.fox.exercise.StateActivity;
import com.fox.exercise.TextUtil;
import com.fox.exercise.api.ApiBack;
import com.fox.exercise.api.ApiConstant;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.api.WatchService;
import com.fox.exercise.api.ApiConstant.WeiboType;
import com.fox.exercise.api.entity.UserDetail;
import com.fox.exercise.newversion.act.MyFirstSportFragment;
import com.fox.exercise.newversion.act.PersonalPageMainActivity;
import com.fox.exercise.newversion.act.SportCircleMainFragment;
import com.fox.exercise.newversion.city.CityPicker;
import com.fox.exercise.newversion.city.CityPicker.OnSelectingListener;
import com.fox.exercise.util.RoundedImage;
import com.fox.exercise.widght.NumericWheelAdapter;
import com.fox.exercise.widght.OnWheelScrollListener;
import com.fox.exercise.widght.WheelView;
import com.umeng.analytics.MobclickAgent;

public class UserEditActivity extends AbstractBaseActivity implements
        View.OnClickListener, OnDismissListener {
    public static boolean mIsDoing = false;
    private static final String FACE_PATH = SportsUtilities.DOWNLOAD_SAVE_PATH
            + "/faceImage.jpg";
    private static final String FACE_PATH_TWO = SportsUtilities.DOWNLOAD_SAVE_PATH
            + "/qqfaceImage.jpg";
    private static final String TAG = "UserEditActivity";
    private SportsApp mSportsApp;
    private EditText nickname;
    // private EditText mEmailEdit = null;
    private TextView mheight;
    private TextView mweight;

    private EditText height, weight;
    // private EditText phone;
    private RoundedImage face;
    // private RadioButton female;
    // private RadioButton male;
    // private RadioGroup radiogroup;
    private TextView rButton;
    private String sex, sex2;
    //	private String mEmail = "";
//	private String[] items;
    private TextView birthday;
    private String birthday2;
    private String phone2;
    private int height2 = 170;
    private int weight2 = 65;
    private String nickname2;
    private String faceurl;
    private boolean isnicknamechanged = false;
    private boolean isbirthdaychanged = false;
    private boolean isfacechanged = false;
    // private boolean isphonechanged = false;
    private UserDetail userdetail;
    private static final String IMAGE_FILE_NAME = "faceImage.jpg";
    /* 请求码 */
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESULT_REQUEST_CODE = 2;
    private static final int okButton = 111;
    private static final int backButton = 112;
    // 如果已经设置头像的话，此值为1，反之为0
    private int faceCount = 0;
    private Dialog userEditProgressDialog;
    private Dialog waitProgressDialog;
    private Calendar cal = Calendar.getInstance();
    // 日期控件
    private WheelView yearWheel;
    private WheelView monthWheel;
    private WheelView dayWheel;
    // 当前真正的时间
    private int curYear, curMonth, curDate;
    // 设置好的时间
    private String newYearString, newMonthString, newDayString;
    private PopupWindow menuWindow, cityWindow, sexPopWindow, heightPopWindow,
            photoPopWindow, weightPopWindow;
    private RelativeLayout pop_menu_background;
    // 从文本获得的时间
    private int nowyear, nowmonth, nowday;
    private SharedPreferences weiBo_sp;
    private String weiBo_type;
    private String weiBo_photo = "facePhoto";

    private EditText ed_gexing_qianming;// 个性签名
    private TextView bmi_num, bmi_content;// bmi值

    private LinearLayout layout_address;
    private TextView tx_suozai_points, tx_suozai_city, tx_suozai_couny;

    private TextView tx_sexs;
    private String argname;
    private ImageButton bt_add1, bt_del1, bt_add2, bt_del2;

//	private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
//
//		@Override
//		public void onDateSet(DatePicker view, int year, int monthOfYear,
//				int dayOfMonth) {
//			cal.set(Calendar.YEAR, year);
//			cal.set(Calendar.MONTH, monthOfYear);
//			cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//			updateDate();
//		}
//	};

    private SportsExceptionHandler mExceptionHandler = null;

    private PopupWindow myWindow = null;
    private String province2,city2,area2,sing2;

    @Override
    public void initIntentParam(Intent intent) {
        // TODO Auto-generated method stub
        if (LoginActivity.mIsRegister) {
            title = getResources().getString(R.string.complete_info);
        } else {
            title = getResources().getString(R.string.complete_info_1);
        }

        mSportsApp = (SportsApp) getApplication();
        mExceptionHandler = mSportsApp.getmExceptionHandler();
        userdetail = mSportsApp.getSportUser();
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        showContentView(R.layout.sports_user_edit);
        weiBo_sp = getSharedPreferences("user_login_info", Context.MODE_PRIVATE);
        weiBo_type = weiBo_sp.getString("weibotype", "");
        if (!mSportsApp.LoginOption) {
            findViewById(R.id.layout_pwd).setVisibility(View.GONE);
        }
        nickname = (EditText) findViewById(R.id.ed_nickname);
        face = (RoundedImage) findViewById(R.id.image_headphoto);
        birthday = (TextView) findViewById(R.id.tx_birthday);
        mheight = (TextView) findViewById(R.id.ed_height);
        mweight = (TextView) findViewById(R.id.ed_weight);
        // female = (RadioButton) findViewById(R.id.female_radio);
        // male = (RadioButton) findViewById(R.id.male_radio);
        // radiogroup = (RadioGroup) findViewById(R.id.radioGroup1);

        bmi_num = (TextView) findViewById(R.id.bmi_num);
        bmi_content = (TextView) findViewById(R.id.bmi_content);
        ed_gexing_qianming = (EditText) findViewById(R.id.ed_gexing_qianming);
        layout_address = (LinearLayout) findViewById(R.id.layout_address);
        tx_suozai_points = (TextView) findViewById(R.id.tx_suozai_points);
        tx_suozai_city = (TextView) findViewById(R.id.tx_suozai_city);
        tx_suozai_couny = (TextView) findViewById(R.id.tx_suozai_couny);

        rButton = new TextView(this);
        rButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        rButton.setText(getResources().getString(R.string.save_goal));
        rButton.setTextSize(19);
        rButton.setTextColor(getResources().getColor(R.color.white));
        showRightBtn(rButton);
        right_btn.setPadding(0, 0, SportsApp.getInstance().dip2px(17), 0);
        SharedPreferences sp = getSharedPreferences("user_login_info",
                Context.MODE_PRIVATE);
        int log_ways = sp.getInt("login_way", -1);
        String type = sp.getString("weibotype", "");
        Log.e(TAG, "用户登录类型为----------1" + log_ways);
        Log.e(TAG, "用户登录类型为----------2" + type);
        findViewById(R.id.layout_nickname).setVisibility(View.VISIBLE);
        findViewById(R.id.layout_headphoto).setVisibility(View.VISIBLE);
        findViewById(R.id.layout_headphoto).setOnClickListener(this);
        if (type.equals("") && mSportsApp.LoginOption) {
            findViewById(R.id.bt_pwd_edit).setVisibility(View.VISIBLE);
            findViewById(R.id.layout_pwd).setVisibility(View.VISIBLE);
        }else{
            findViewById(R.id.bt_pwd_edit).setVisibility(View.GONE);
            findViewById(R.id.layout_pwd).setVisibility(View.GONE);
        }
        pop_menu_background = (RelativeLayout) findViewById(R.id.send_menu_background);
        findViewById(R.id.layout_sex).setOnClickListener(this);
        findViewById(R.id.layout_height).setOnClickListener(this);
        findViewById(R.id.layout_weight).setOnClickListener(this);
        tx_sexs = (TextView) findViewById(R.id.tx_sexs);
    }

    @Override
    public void setViewStatus() {
        // TODO Auto-generated method stub
        nickname.addTextChangedListener(mTextWatcher);
        // height.setOnFocusChangeListener(new OnFocusChangeListener() {
        // public void onFocusChange(View v, boolean hasFocus) {
        // adjustHeight();
        // setBmi();
        // }
        // });
        // // height.addTextChangedListener(mHeightWatcher);
        // weight.setOnFocusChangeListener(new OnFocusChangeListener() {
        // public void onFocusChange(View v, boolean hasFocus) {
        // adjustWeight();
        // setBmi();
        // }
        // });
        if (mSportsApp.config == 1) {
            bindService(new Intent(this, WatchService.class), wConnection,
                    Context.BIND_AUTO_CREATE);
        }
        birthday.setOnClickListener(this);

        layout_address.setOnClickListener(this);
        tx_suozai_points.setOnClickListener(this);
        tx_suozai_city.setOnClickListener(this);

        // findViewById(R.id.bt_add1).setOnClickListener(this);
        // findViewById(R.id.bt_del1).setOnClickListener(this);
        // findViewById(R.id.bt_add2).setOnClickListener(this);
        // findViewById(R.id.bt_del2).setOnClickListener(this);
        // findViewById(R.id.bt_back).setOnClickListener(this);
        left_ayout.setOnClickListener(this);
        leftButton.setId(backButton);
        leftButton.setOnClickListener(this);
        rButton.setId(okButton);
        rButton.setOnClickListener(this);
        right_btn.setOnClickListener(this);
        nickname2 = userdetail.getUname();
        if (!LoginActivity.mIsRegister) {
            faceurl = userdetail.getUimg();
        } else {
            if (weiBo_type.equals(WeiboType.QQzone)) {
                faceurl = AllWeiboInfo.TENCENT_QQZONE_PHOTO;
            } else {
                faceurl = userdetail.getUimg();
            }
        }


        sex = userdetail.getSex();
        Log.i(TAG, "faceurl---" + faceurl+"    "+sex);
        if ("woman".equals(sex)) {
            face.setImageResource(R.drawable.sports_user_edit_portrait);
        }else if ("man".equals(sex)) {
            face.setImageResource(R.drawable.sports_user_edit_portrait_male);
        }else{
            face.setImageResource(R.drawable.sports_user_edit_portrait_male);
        }
        if (faceurl != null) {
            if (!faceurl.equals("")) {
                new AsyncTask<Void, Void, Bitmap>() {
                    @Override
                    protected Bitmap doInBackground(Void... params) {
                        Log.e(TAG, "doInBackground");
                        String url = faceurl;
                        String fileName = null;
                        if (!LoginActivity.mIsRegister) {
                            fileName = url.substring(url.lastIndexOf("/") + 1,
                                    url.lastIndexOf("."));
                        } else {
                            if (weiBo_type.equals(WeiboType.QQzone)) {
//                                fileName = weiBo_photo;
                            }
                        }
                        Log.e(TAG, "fileName-----------" + fileName);
                        return SportsUtilities.loadBitmap(url, fileName);
                    }

                    @Override
                    protected void onPostExecute(Bitmap bitmap) {
                        if (bitmap != null) {
                            face.setImageBitmap(bitmap);
                            if (LoginActivity.mIsRegister
                                    && !weiBo_type.equals("")) {
                                Tools.SaveBitmapAsFile(FACE_PATH, bitmap);
                                Tools.SaveBitmapAsFile(FACE_PATH_TWO, bitmap);
                            }
                            faceCount = 1;
                        } else {
                            Log.e(TAG, "bitmap=null");
                            if ("woman".equals(sex)) {
                                face.setImageResource(R.drawable.sports_user_edit_portrait);
                            }else if ("man".equals(sex)) {
                                face.setImageResource(R.drawable.sports_user_edit_portrait_male);
                            }else{
                                face.setImageResource(R.drawable.sports_user_edit_portrait_male);
                            }
                        }
                    }
                }.execute();
            }
        }

        nickname.setText(nickname2);
        if (mSportsApp.getSportUser().getEmail() != null) {
//			mEmail = mSportsApp.getSportUser().getEmail();
            // mEmailEdit.setText(mSportsApp.getSportUser().getEmail());
        }
        findViewById(R.id.bt_pwd_edit).setOnClickListener(this);
        findViewById(R.id.layout_pwd).setOnClickListener(this);
        findViewById(R.id.image_headphoto).setOnClickListener(this);
        findViewById(R.id.tx_shezhiheadphoto).setOnClickListener(this);
        // }else{
        // title.setText(getResources().getString(R.string.complete_info_2));
        // }
        birthday2 = userdetail.getBirthday();
        Log.v(TAG, "birthday2:1" + birthday2);
        height2 = userdetail.getHeight();
        weight2 = userdetail.getWeight();
        if (height2 == 0) {
            if ("woman".equals(sex))
                height2 = 160;
            else if ("man".equals(sex))
                height2 = 170;
            else
                height2 = 170;
        }
        if (weight2 == 0) {
            if ("woman".equals(sex))
                weight2 = 50;
            else if ("man".equals(sex))
                weight2 = 60;
            else
                weight2 = 60;

        }
        mheight.setText("" + height2);
        mweight.setText("" + weight2);
        // 设置BMI
        setBmi();
        phone2 = userdetail.getPhoneno();
        if (birthday2 != null && !"0000-00-00".equals(birthday2)
                && !"".equals(birthday2)&& !"null".equals(birthday2)) {
            birthday.setText(birthday2);
            Log.v(TAG, "birthday2:2" + birthday2);
        } else {
            birthday2 = "1985-01-01";
            birthday.setText(birthday2);
            Log.v(TAG, "birthday2:3" + birthday2);
        }
//		items = new String[] {
//				getResources().getString(R.string.sports_camera),
//				getResources().getString(R.string.sports_fromphotos),
//				getResources().getString(R.string.sports_cancel) };

        if ("man".equals(sex)) {
            // male.setChecked(true);
            tx_sexs.setText(getResources().getString(R.string.male));
            sex2 = getResources().getString(R.string.male);
        } else if ("woman".equals(sex)) {
            // female.setChecked(true);
            tx_sexs.setText(getResources().getString(R.string.female));
            sex2 = getResources().getString(R.string.female);
        } else {
            sex2 = getResources().getString(R.string.male);
        }
        if (userdetail.getSignature()==null||"".equals(userdetail.getSignature())){
            sing2="";
            ed_gexing_qianming.setText(sing2);
        }else{
            ed_gexing_qianming.setText(userdetail.getSignature());
            sing2=userdetail.getSignature();
        }
        // bmi_num.setText(userdetail.getBmi() + "");
        if (userdetail.getProvince() != null
                && !"".equals(userdetail.getProvince())) {
            tx_suozai_points.setText(userdetail.getProvince());
            province2=userdetail.getProvince();
        } else {
            tx_suozai_points.setText("北京市");
            province2="北京市";
        }
        if (userdetail.getCity() != null && !"".equals(userdetail.getCity())) {
            tx_suozai_city.setText(userdetail.getCity());
            city2=userdetail.getCity();
        } else {
            tx_suozai_city.setText("县");
            city2="县";
        }
        if (userdetail.getArea() != null && !"".equals(userdetail.getArea())) {
            tx_suozai_couny.setText(userdetail.getArea());
            area2=userdetail.getArea();
        } else {
            tx_suozai_couny.setText("密云县");
            area2="密云县";
        }

        // radiogroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
        //
        // @Override
        // public void onCheckedChanged(RadioGroup group, int checkedId) {
        // if (faceCount == 0) {
        // switch (checkedId) {
        // case R.id.female_radio:
        // face.setImageResource(R.drawable.sports_user_edit_portrait);
        // break;
        // case R.id.male_radio:
        // face.setImageResource(R.drawable.sports_user_edit_portrait_male);
        // break;
        //
        // }
        // }
        // }
        // });
    }

    @Override
    public void onPageResume() {
        MobclickAgent.onPageStart("UserEditActivity");
    }

    @Override
    public void onPagePause() {
        MobclickAgent.onPageEnd("UserEditActivity");
    }

    @Override
    public void onPageDestroy() {
        // TODO Auto-generated method stub
        if (mSportsApp.config == 1) {
            unbindService(wConnection);
        }
        waitProgressDialog = null;
        menuWindow = null;
        cityWindow = null;
        sexPopWindow = null;
        heightPopWindow = null;

    }

    TextWatcher mTextWatcher = new TextWatcher() {
        private CharSequence temp;
        private int editStart;
        private int editEnd;
        private EditText editnameText;

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            // TODO Auto-generated method stub
            temp = s;
            String editable = nickname.getText().toString();
//			nickname.setSelection(editable.length());
            String str = stringFilter(editable.toString());
            if (!editable.equals(str)) {
                nickname.setText(str);
                // 设置新的光标所在位置 www.2cto.com
                nickname.setSelection(str.length());
                Toast.makeText(
                        UserEditActivity.this,
                        getResources().getString(R.string.don_not_enter_spaces),
                        Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub
            // mTextView.setText(s);//将输入的内容实时显示
        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            editStart = nickname.getSelectionStart();
            editEnd = nickname.getSelectionEnd();
            if (temp.length() > 15) {
                Toast.makeText(
                        UserEditActivity.this,
                        getResources()
                                .getString(R.string.enter_number_outindex),
                        Toast.LENGTH_SHORT).show();
                s.delete(editStart - 1, editEnd);
                int tempSelection = editStart;
                nickname.setText(s);
                nickname.setSelection(tempSelection);
            }
        }
    };

    public static String stringFilter(String str) throws PatternSyntaxException {
        // 只允许字母和数字
        String regEx = "[\\s*]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    private WatchService wService;
    private ServiceConnection wConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            wService = ((WatchService.WBinder) service).getService();
            wService.registerSettingCallback(wCallback);
        }

        public void onServiceDisconnected(ComponentName className) {
            wService = null;
        }
    };

    private void adjustHeight() {
        if (height.getText().toString().equals("")
                || height.getText().toString() == null
                || height.getText().toString().length() == 0) {
            height2 = 50;
            height.setText("" + 50);
        } else if (height.getText().toString().length() >= 4/*
                                                             * ||
															 * Integer.parseInt
															 * (height
															 * .getText().
															 * toString())>250
															 */) {
            height2 = 250;
            height.setText("" + 250);
        } else {
            height2 = Integer.parseInt(height.getText().toString());
        }
        if (height2 <= 50) {
            bt_del1.setClickable(false);
            bt_add1.setClickable(true);
            // if(height2 ==50){
            // Toast.makeText(getApplication(), "已经达到身高最低下限值哦！",
            // Toast.LENGTH_LONG).show();
            // }else{
            if (height2 != 50) {
                Toast.makeText(
                        getApplication(),
                        getResources().getString(
                                R.string.height_limit_warning_one),
                        Toast.LENGTH_LONG).show();
                height.setText("" + 50);
            }

        } else if (height2 >= 250) {
            bt_add1.setClickable(false);
            bt_del1.setClickable(true);
            // if(height2 == 250){
            // Toast.makeText(getApplication(), "已经达到身高最高上限值哦！",
            // Toast.LENGTH_LONG).show();
            // }else{
            if (height2 != 250) {
                Toast.makeText(
                        getApplication(),
                        getResources().getString(
                                R.string.height_limit_warning_two),
                        Toast.LENGTH_LONG).show();
                height.setText("" + 250);
            }

        }
    }

    private void adjustWeight() {
        if (weight.getText().toString().equals("")
                || weight.getText().toString() == null
                || weight.getText().toString().length() == 0) {
            weight2 = 20;
            weight.setText("" + 20);
        } else if (weight.getText().toString().length() >= 4 /*
                                                             * ||
															 * Integer.parseInt
															 * (weight
															 * .getText().
															 * toString())>200
															 */) {
            weight2 = 200;
            weight.setText("" + 200);
        } else {
            weight2 = Integer.parseInt(weight.getText().toString());
        }
        if (weight2 <= 20) {
            bt_del2.setClickable(false);
            bt_add2.setClickable(true);
            // if(weight2 ==20){
            // weight.setText(""+20);
            // Toast.makeText(getApplication(), "已经达到体重最低下限值哦！",
            // Toast.LENGTH_LONG).show();
            // }else{
            if (weight2 != 20) {
                Toast.makeText(
                        getApplication(),
                        getResources().getString(
                                R.string.weight_limit_waring_one),
                        Toast.LENGTH_LONG).show();
                weight.setText("" + 20);
            }

        } else if (weight2 >= 200) {
            bt_add2.setClickable(false);
            bt_del2.setClickable(true);
            // if(weight2 ==200){
            // Toast.makeText(getApplication(), "已经达到体重最高上限值哦！",
            // Toast.LENGTH_LONG).show();
            // weight.setText(""+200);
            // }else{
            if (weight2 != 200) {
                Toast.makeText(
                        getApplication(),
                        getResources().getString(
                                R.string.weight_limit_waring_two),
                        Toast.LENGTH_LONG).show();
                weight.setText("" + 200);
            }

        }
    }

    private WatchService.SCallback wCallback = new WatchService.SCallback() {

        @Override
        public void setUser(UserDetail user) {
            // TODO Auto-generated method stub
            sex = user.getSex();
            mheight.setText(user.getHeight() + "");
            mweight.setText(user.getWeight() + "");
        }

    };
    private BitmapDrawable drawable;

    private void showWaitDialog() {
        waitProgressDialog = new Dialog(UserEditActivity.this,
                R.style.sports_dialog);
        LayoutInflater mInflater = getLayoutInflater();
        View v1 = mInflater.inflate(R.layout.sports_progressdialog, null);
        TextView message = (TextView) v1.findViewById(R.id.message);
        message.setText(R.string.share_message);
        v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
        waitProgressDialog.setContentView(v1);
        waitProgressDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left_layout:
            case backButton:
                // if(!getIntent().getBooleanExtra("key", false))
                // {
                // startActivity(new Intent(UserEditActivity.this,
                // FoxSportsNavigation.class));
                // }
                if (LoginActivity.mIsRegister) {
                    if (nickname.getText().toString().trim() == null
                            || "".equals(nickname.getText().toString().trim())) {
                        Toast.makeText(
                                getApplication(),
                                getResources()
                                        .getString(R.string.nickname_ont_be_empty),
                                Toast.LENGTH_LONG).show();
                    } else {
                        //新加需求昵称大于15的话不能提交
                        if (judgeNum(nickname.getText().toString().trim())) {
                            Toast.makeText(
                                    getApplication(),
                                    getResources()
                                            .getString(R.string.sports_nickname_toolong),
                                    Toast.LENGTH_LONG).show();
                        } else if (nickname.getText().toString().trim().contains("_")) {
                            Toast.makeText(
                                    getApplication(),
                                    getResources()
                                            .getString(R.string.sports_toast_illegal_character),
                                    Toast.LENGTH_LONG).show();
                        } else {
                            showWaitDialog();
                            (new WeiBoRegistBackTask()).execute();
                        }
                    }

                } else {
                    isnicknamechanged = nickname.getText().toString().trim()
                            .equals(nickname2);
                    boolean isGeXingQianmingChanged = ed_gexing_qianming.getText().toString().trim().equals(sing2);
                    boolean isSexChanged = tx_sexs.getText().toString().trim().equals(sex2);
                    isbirthdaychanged = birthday2
                            .equals(birthday.getText().toString());
                    boolean isCityChanged = false;
                    if (!tx_suozai_points.getText().toString().trim().equals(province2)) {
                        isCityChanged = true;
                    }
                    if (!tx_suozai_city.getText().toString().trim().equals(city2)) {
                        isCityChanged = true;
                    }
                    if (!tx_suozai_couny.getText().toString().trim().equals(area2)) {
                        isCityChanged = true;
                    }
                    boolean isHeightChanged = mheight.getText().toString().trim().equals(height2 + "");
                    boolean isWeightChanged = mweight.getText().toString().trim().equals(weight2 + "");
                    if (isfacechanged || (isnicknamechanged == false) || (isGeXingQianmingChanged == false)
                            || (isSexChanged == false) || (isbirthdaychanged == false) || isCityChanged
                            || (isHeightChanged == false) || (isWeightChanged == false)) {
                      tipPopWindow(getResources().getString(R.string.info_tips));
                    } else {
                        finish();
                    }

                }
                break;
            case R.id.bt_add1:
                bt_del1.setClickable(true);
                height2 = Integer.parseInt(height.getText().toString());
                if (height2 >= 250) {
                    bt_add1.setClickable(false);
                    Toast.makeText(
                            getApplication(),
                            getResources().getString(
                                    R.string.soprts_limit_warning_one),
                            Toast.LENGTH_LONG).show();
                    return;
                }
                height2++;
                height.setText("" + height2);
                setBmi();
                break;
            case R.id.bt_add2:
                bt_del2.setClickable(true);
                weight2 = Integer.parseInt(weight.getText().toString());
                if (weight2 >= 200) {
                    bt_add2.setClickable(false);
                    Toast.makeText(
                            getApplication(),
                            getResources().getString(
                                    R.string.soprts_limit_warning_one),
                            Toast.LENGTH_LONG).show();
                    return;
                }
                weight2++;
                weight.setText("" + weight2);
                setBmi();
                break;
            case R.id.bt_del1:
                bt_add1.setClickable(true);
                height2 = Integer.parseInt(height.getText().toString());
                if (height2 <= 50) {
                    bt_del1.setClickable(false);
                    Toast.makeText(
                            getApplication(),
                            getResources().getString(
                                    R.string.soprts_limit_warning_two),
                            Toast.LENGTH_LONG).show();
                    return;
                }
                height2--;
                height.setText("" + height2);
                setBmi();
                break;
            case R.id.bt_del2:
                bt_add2.setClickable(true);
                weight2 = Integer.parseInt(weight.getText().toString());
                if (weight2 <= 20) {
                    bt_del2.setClickable(false);
                    Toast.makeText(
                            getApplication(),
                            getResources().getString(
                                    R.string.soprts_limit_warning_two),
                            Toast.LENGTH_LONG).show();
                    return;
                }
                weight2--;
                weight.setText("" + weight2);
                setBmi();
                break;
            case okButton:
            case R.id.title_right_btn:
                if (mheight.getText().toString().equals("")
                        || mheight.getText().toString() == null
                        || mheight.getText().toString().length() == 0) {
                    height2 = 170;
                } else {
                    height2 = Integer.parseInt(mheight.getText().toString());
                }
                if (mweight.getText().toString().equals("")
                        || mweight.getText().toString() == null
                        || mweight.getText().toString().length() == 0) {
                    weight2 = 65;
                } else {
                    weight2 = Integer.parseInt(mweight.getText().toString());
                }
                if (weight2 <= 20) {
                    if (weight2 != 20) {
                        Toast.makeText(
                                getApplication(),
                                getResources().getString(
                                        R.string.weight_limit_waring_one),
                                Toast.LENGTH_LONG).show();
                        mweight.setText("" + 20);
                        return;
                    }
                } else if (weight2 >= 200) {
                    if (weight2 != 200) {
                        Toast.makeText(
                                getApplication(),
                                getResources().getString(
                                        R.string.weight_limit_waring_two),
                                Toast.LENGTH_LONG).show();
                        mweight.setText("" + 200);
                        return;
                    }
                }
                if (height2 <= 50) {
                    if (height2 != 50) {
                        Toast.makeText(
                                getApplication(),
                                getResources().getString(
                                        R.string.height_limit_warning_one),
                                Toast.LENGTH_LONG).show();
                        mheight.setText("" + 50);
                        return;
                    }
                } else if (height2 >= 250) {
                    if (height2 != 250) {
                        Toast.makeText(
                                getApplication(),
                                getResources().getString(
                                        R.string.height_limit_warning_two),
                                Toast.LENGTH_LONG).show();
                        mheight.setText("" + 250);
                        return;
                    }
                }
                if (nickname.getText().toString().trim() == null
                        || "".equals(nickname.getText().toString().trim())) {
                    Toast.makeText(
                            getApplication(),
                            getResources()
                                    .getString(R.string.nickname_ont_be_empty),
                            Toast.LENGTH_LONG).show();
                    return;
                } else {
                    //新加需求昵称大于15的话不能提交
                    if (judgeNum(nickname.getText().toString().trim())) {
                        Toast.makeText(
                                getApplication(),
                                getResources()
                                        .getString(R.string.sports_nickname_toolong),
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (nickname.getText().toString().trim().contains("_")) {
                        Toast.makeText(
                                getApplication(),
                                getResources()
                                        .getString(R.string.sports_toast_illegal_character),
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                    //新加需求个性签名大于15的话不能提交
                    if (judgeNum(ed_gexing_qianming.getText().toString().trim())) {
                        Toast.makeText(
                                getApplication(),
                                getResources()
                                        .getString(R.string.sports_qianming_toolong),
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (ed_gexing_qianming.getText().toString().trim().contains("_")) {
                        Toast.makeText(
                                getApplication(),
                                getResources()
                                        .getString(R.string.sports_toast_qianming_character),
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                isnicknamechanged = !nickname.getText().toString().trim()
                        .equals(nickname2);
                isbirthdaychanged = !birthday2
                        .equals(birthday.getText().toString());

                userEditProgressDialog = new Dialog(UserEditActivity.this,
                        R.style.sports_dialog);
                LayoutInflater mInflater = getLayoutInflater();
                View v1 = mInflater.inflate(R.layout.sports_progressdialog, null);
                TextView message = (TextView) v1.findViewById(R.id.message);
                message.setText(R.string.sports_dialog_modifying);
                v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
                userEditProgressDialog.setContentView(v1);
                userEditProgressDialog.setCanceledOnTouchOutside(false);
                userEditProgressDialog.show();
                new AsyncTask<Void, Void, ApiBack>() {
                    @Override
                    protected ApiBack doInBackground(Void... params) {
                        argname = nickname.getText().toString().trim();
                        String argpicAddess = FACE_PATH;
                        String email = "";
                        String phone = "";
                        String argsex = "";
                        String argbirthday = birthday.getText().toString();
                        Log.v(TAG, "birthday2:4" + argbirthday);
                        // String argphone = phone.getText().toString();
                        String argheight = mheight.getText().toString();
                        String argweight = mweight.getText().toString();
                        if (argheight.length() == 0 || argheight.equals("")
                                || argheight == null || argheight.length() > 4
                                || Integer.parseInt(argheight) > 250) {
                            argheight = String.valueOf(height2);
                        }
                        if (argweight.length() == 0 || argweight.equals("")
                                || argweight == null || argweight.length() > 4
                                || Integer.parseInt(argweight) > 200) {
                            argweight = String.valueOf(weight2);
                        }
                        if (!isnicknamechanged) {
                            argname = "";
                        }
                        if (getResources().getString(R.string.female).equals(
                                tx_sexs.getText().toString())) {
                            argsex = "woman";
                        } else if (getResources().getString(R.string.male).equals(
                                tx_sexs.getText().toString())) {
                            argsex = "man";
                        }
                        // if (!"".equals(mEmailEdit.getText().toString())) {
                        // if (mEmail != null &&
                        // !mEmail.equals(mEmailEdit.getText().toString())) {
                        // email = mEmailEdit.getText().toString();
                        // }else{
                        // email = "";
                        // }
                        // }
                        if (!LoginActivity.mIsRegister){
                            if (!isfacechanged) {
                                argpicAddess = "";
                            }
                        }else {
                            if (!isfacechanged) {
                                if (ApiConstant.URL.equals(faceurl)){
                                    argpicAddess = "";
                                }
                            }
                        }


                        // if (isphonechanged && argphone.equals("")) {
                        // Log.e(TAG, "modify to original");
                        // argphone = "null";
                        // }
                        if (!isbirthdaychanged) {
                            Log.e(TAG, "isbirthdaychanged" + isbirthdaychanged);
                            argbirthday = "";
                        }
                        // if (!isphonechanged) {
                        // argphone = "";
                        // }
                        if (!sex.equals(argsex)) {
                            isfacechanged = true;
                        }
                        String province = tx_suozai_points.getText().toString();
                        String city = tx_suozai_city.getText().toString();
                        String conuy = tx_suozai_couny.getText().toString();
                        String singure = ed_gexing_qianming.getText().toString();
                        String bmi = bmi_num.getText().toString().trim();
                        try {
                            ApiBack back = null;
                            back = ApiJsonParser.modifymsg(
                                    mSportsApp.getSessionId(), argname, "", "",
                                    argpicAddess, argsex, argbirthday, phone,
                                    email, argheight, argweight, province, city,
                                    conuy, singure, bmi);
                            if (!"".equals(mSportsApp.getSessionId())
                                    && mSportsApp.getSessionId() != null) {
                                mSportsApp.setSportUser(ApiJsonParser
                                        .refreshRank(mSportsApp.getSessionId()));
                            }
                            return back;
                        } catch (ApiNetException e) {
                            if (userEditProgressDialog != null)
                                if (userEditProgressDialog.isShowing())
                                    userEditProgressDialog.dismiss();
                            Log.e(TAG, "---网络错误");
                            SportsApp.eMsg = Message.obtain(mExceptionHandler,
                                    SportsExceptionHandler.NET_ERROR);
                            SportsApp.eMsg.sendToTarget();
                            e.printStackTrace();
                        } catch (ApiSessionOutException e) {
                            if (userEditProgressDialog != null)
                                if (userEditProgressDialog.isShowing())
                                    userEditProgressDialog.dismiss();
                            e.printStackTrace();
                            SportsApp.eMsg = Message.obtain(mExceptionHandler,
                                    SportsExceptionHandler.SESSION_OUT);
                            SportsApp.eMsg.sendToTarget();
                            startActivity(new Intent(UserEditActivity.this,
                                    LoginActivity.class));
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(ApiBack back) {
                        if (userEditProgressDialog != null)
                            if (userEditProgressDialog.isShowing())
                                userEditProgressDialog.dismiss();
                        if (back == null) {
                            return;
                        }
                        Log.e(TAG, back.getFlag() + "555");
                        Log.e(TAG, back.getMsg() + "555");
                        if (back.getFlag() == 0) {
                            new AsyncTask<Void, Void, UserDetail>() {
                                @Override
                                protected UserDetail doInBackground(Void... params) {
                                    UserDetail detail = null;
                                    try {
                                        Log.v(TAG,
                                                "Session1:"
                                                        + mSportsApp.getSessionId());
                                        // Log.e(TAG,
                                        // "时间分割线----------------------第一次准备刷新");
                                        detail = ApiJsonParser
                                                .refreshRank(mSportsApp
                                                        .getSessionId());
                                        return detail;
                                    } catch (ApiNetException e) {
                                        e.printStackTrace();
                                        SportsApp.eMsg = Message.obtain(
                                                mExceptionHandler,
                                                SportsExceptionHandler.NET_ERROR);
                                        SportsApp.eMsg.sendToTarget();
                                    } catch (ApiSessionOutException e) {
                                        e.printStackTrace();
                                        SportsApp.eMsg = Message.obtain(
                                                mExceptionHandler,
                                                SportsExceptionHandler.SESSION_OUT);
                                        SportsApp.eMsg.sendToTarget();
                                        startActivity(new Intent(
                                                UserEditActivity.this,
                                                LoginActivity.class));
                                    }
                                    return detail;
                                }

                                @Override
                                protected void onPostExecute(UserDetail result) {
                                    if (result != null) {
                                        mSportsApp.setSportUser(result);
                                    }
                                    nickname2 = nickname.getText().toString().trim();
                                    birthday2 = birthday.getText().toString();
                                    SharedPreferences sp = getSharedPreferences(
                                            "user_login_info", Context.MODE_PRIVATE);
                                    Editor ed = sp.edit();
                                    ed.putString("account", nickname2);
                                    // 保存用户身份标识
                                    ed.commit();
                                    Toast.makeText(
                                            UserEditActivity.this,
                                            getString(R.string.sports_modify_successed),
                                            Toast.LENGTH_SHORT).show();
                                    Log.e(TAG, "edit finish");
                                    mSportsApp.setUserName(nickname.getText().toString().trim());
                                    // SimpleDateFormat sDateFormat = new
                                    // SimpleDateFormat("yyyy MM dd hh:mm:ss");
                                    Time localTime = new Time("Asia/Hong_Kong");
                                    localTime.setToNow();
                                    String date = localTime
                                            .format("%Y-%m-%d %H:%M:%S");
                                    SharedPreferences spf = getSharedPreferences(
                                            "sports", 0);
                                    Editor editor = spf.edit();
                                    editor.putString("date", date);
                                    editor.putString("sex", sex);
                                    editor.putInt("height", height2);
                                    editor.putInt("weight", weight2);
                                    editor.commit();
                                    if (mSportsApp.config == 1) {
                                        wService.sendUser();
                                    }
                                    // if(getIntent().getBooleanExtra("key", false)
                                    // {
                                    if (LoginActivity.mIsRegister) {
                                        LoginActivity.mIsRegister = false;
                                        if (mSportsApp.LoginOption) {
                                            startActivity(new Intent(
                                                    UserEditActivity.this,
                                                    MainFragmentActivity.class));
                                        } else {
                                            Handler mHandler = mSportsApp
                                                    .getmHandler();
                                            if (mHandler != null) {
                                                // mHandler.sendMessage(mHandler
                                                // .obtainMessage(StateActivity.UPDATE_TRYTOLOGIN));
                                                mHandler.sendMessage(mHandler
                                                        .obtainMessage(
                                                                MyFirstSportFragment.REFRESHPHOTO,
                                                                isfacechanged));
                                            }
                                            Handler fHandler = mSportsApp
                                                    .getfHandler();
                                            if (fHandler != null) {
                                                fHandler.sendMessage(fHandler
                                                        .obtainMessage(FoxSportsSetting.UPDATE_TRYTOLOGIN));
                                            }
                                            Handler mainHandler = mSportsApp
                                                    .getMainHandler();
                                            mainHandler
                                                    .sendMessage(mainHandler
                                                            .obtainMessage(MainFragmentActivity.UPDATE_TRYTOLOGIN));

                                            Handler personalFindHandler = mSportsApp
                                                    .getPersonalFindHandler();
                                            if (personalFindHandler != null) {
                                                Message msgMessage = personalFindHandler
                                                        .obtainMessage(PersonalPageMainActivity.USEREDITFRESH_VIEW);
                                                msgMessage.obj = isfacechanged;
                                                personalFindHandler
                                                        .sendMessage(msgMessage);
                                            }
                                            if (mSportsApp
                                                    .getmSportsCircleMainhandler() != null) {
                                                Handler mSportsCircleMainhandler = mSportsApp
                                                        .getmSportsCircleMainhandler();
                                                Message msgMessage = mSportsCircleMainhandler
                                                        .obtainMessage(SportCircleMainFragment.REFRESHPHOTO);
                                                msgMessage.obj = isfacechanged;
                                                mSportsCircleMainhandler
                                                        .sendMessage(msgMessage);
                                            }
                                            if (mSportsApp
                                                    .getmBaseFragmentHandler() != null) {
                                                Handler mBaseFragmentHandler = mSportsApp
                                                        .getmBaseFragmentHandler();
                                                Message msgMessage = mBaseFragmentHandler
                                                        .obtainMessage(AbstractBaseOtherFragment.REFRESHPHOTO);
                                                msgMessage.obj = isfacechanged;
                                                mBaseFragmentHandler
                                                        .sendMessage(msgMessage);
                                            }
                                            mSportsApp.LoginOption = true;
                                        }
                                        UserEditActivity.this.finish();
                                    } else {
                                        LoginActivity.mIsRegister = false;
                                        Intent userIntent = new Intent();
                                        Bundle bundle = new Bundle();
                                        bundle.putBoolean("isfacechanged",
                                                isfacechanged);
                                        bundle.putString("username", nickname2);
                                        userIntent.putExtra("useredit", bundle);
                                        Handler mHandler = mSportsApp.getmHandler();
                                        if (mHandler != null) {
                                            mHandler.sendMessage(mHandler
                                                    .obtainMessage(
                                                            MyFirstSportFragment.REFRESHPHOTO,
                                                            isfacechanged));
                                        }
                                        if (mSportsApp
                                                .getmSportsCircleMainhandler() != null) {
                                            Handler mSportsCircleMainhandler = mSportsApp
                                                    .getmSportsCircleMainhandler();
                                            Message msgMessage = mSportsCircleMainhandler
                                                    .obtainMessage(SportCircleMainFragment.REFRESHPHOTO);
                                            msgMessage.obj = isfacechanged;
                                            mSportsCircleMainhandler
                                                    .sendMessage(msgMessage);
                                        }
                                        if (mSportsApp.getmBaseFragmentHandler() != null) {
                                            Handler mBaseFragmentHandler = mSportsApp
                                                    .getmBaseFragmentHandler();
                                            Message msgMessage = mBaseFragmentHandler
                                                    .obtainMessage(AbstractBaseOtherFragment.REFRESHPHOTO);
                                            msgMessage.obj = isfacechanged;
                                            mBaseFragmentHandler
                                                    .sendMessage(msgMessage);
                                        }
                                        Handler fHandler = mSportsApp.getfHandler();
                                        if (fHandler != null) {
                                            fHandler.sendMessage(fHandler
                                                    .obtainMessage(
                                                            FoxSportsSetting.UPDATE_HEADER,
                                                            userIntent));
                                        }
                                        if (mSportsApp.getFindHandler() != null) {
                                            Handler findHandler = mSportsApp
                                                    .getFindHandler();
                                            findHandler
                                                    .sendMessage(findHandler
                                                            .obtainMessage(
                                                                    FindOtherFragment.FRESH_PHOTO,
                                                                    userIntent));
                                        }
                                        Handler mainHandler = mSportsApp
                                                .getMainHandler();
                                        if (mainHandler != null) {
                                            mainHandler
                                                    .sendMessage(mainHandler
                                                            .obtainMessage(
                                                                    MainFragmentActivity.UPDATE_HEADER,
                                                                    userIntent));
                                        }
                                        Handler personalFindHandler = mSportsApp
                                                .getPersonalFindHandler();
                                        if (personalFindHandler != null) {
                                            Message msgMessage = personalFindHandler
                                                    .obtainMessage(PersonalPageMainActivity.USEREDITFRESH_VIEW);
                                            msgMessage.obj = isfacechanged;
                                            personalFindHandler
                                                    .sendMessage(msgMessage);
                                        }
                                        isfacechanged = false;
                                        finish();
                                    }
                                }
                            }.execute();

                            // }
                            // else{
                            // startActivity(new Intent(UserEditActivity.this,
                            // FoxSportsNavigation.class));
                            // }

                        }
                        if (back.getFlag() == -1) {
                            isfacechanged = false;
                            if (back.getMsg().equals("nameexist")) {
                                Toast.makeText(
                                        UserEditActivity.this,
                                        getResources().getString(
                                                R.string.modified_failed_message),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(
                                        UserEditActivity.this,
                                        getResources().getString(
                                                R.string.sports_modify_failed)
                                                + back.getMsg(), Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    }
                }.execute();
                break;
            case R.id.layout_headphoto:
            case R.id.image_headphoto:
            case R.id.tx_shezhiheadphoto:
                Log.e(TAG, "shezhitouxiang");
                // showDialog();
                shotSelectImages();
                break;
            case R.id.bt_pwd_edit:
            case R.id.layout_pwd:
                startActivity(new Intent(UserEditActivity.this,
                        ModifyPwdActivity.class));
                // UserEditActivity.this.finish();
                break;
            case R.id.tx_birthday:
                // 隐藏输入框
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive() && getCurrentFocus() != null) {
                    if (getCurrentFocus().getWindowToken() != null) {
                        imm.hideSoftInputFromWindow(getCurrentFocus()
                                        .getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
                if (menuWindow == null) {
                    showPopwindow(getDataPick());
                } else {
                    if (!menuWindow.isShowing()) {
                        showPopwindow(getDataPick());
                    }
                }
                break;
            // case R.id.tx_suozai_points:
            // case R.id.tx_suozai_city:
            case R.id.layout_address:
                // 隐藏输入框
                InputMethodManager imm1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm1.isActive() && getCurrentFocus() != null) {
                    if (getCurrentFocus().getWindowToken() != null) {
                        imm1.hideSoftInputFromWindow(getCurrentFocus()
                                        .getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
                if (cityWindow == null) {
                    showCityPopwindow(getCityView());
                } else {
                    if (!cityWindow.isShowing()) {
                        showCityPopwindow(getCityView());
                    }
                }
                break;
            case R.id.layout_sex:
                showSexPop();
                break;
            case R.id.layout_height:
                showHeightPop();
                break;
            case R.id.layout_weight:
                showWeightPop();
                break;
            case R.id.btn_paizhao:
                Intent intentFromCapture = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                // 判断存储卡是否可以用，可用进行存储
                if (Tools.hasSdcard()) {

                    intentFromCapture
                            .putExtra(MediaStore.EXTRA_OUTPUT, Uri
                                    .fromFile(new File(Environment
                                            .getExternalStorageDirectory(),
                                            IMAGE_FILE_NAME)));
                }

                startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);

                pop_menu_background.setVisibility(View.GONE);
                photoPopWindow.dismiss();
                break;
            case R.id.btn_select_pic:
                Intent intentFromGallery = new Intent(Intent.ACTION_PICK, null);
                intentFromGallery.setDataAndType(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);
                pop_menu_background.setVisibility(View.GONE);
                photoPopWindow.dismiss();
                break;
            default:
                break;
        }
    }

    // ----------------时间控件--------------
    private void showPopwindow(View view) {
        menuWindow = new PopupWindow(view, LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT);
        menuWindow.setAnimationStyle(R.style.AnimationPopup);
        menuWindow.setFocusable(true);
        menuWindow.setBackgroundDrawable(new BitmapDrawable());
        // popwindow显示在屏幕的位置
        // menuWindow.showAtLocation(findViewById(R.id.layout), Gravity.CENTER,
        // 0, (int)(Yto*0.05));
        menuWindow.showAtLocation(right_btn, Gravity.BOTTOM, 0, 0);
        menuWindow.setOutsideTouchable(true);
        Animation animation = (Animation) AnimationUtils.loadAnimation(this,
                R.anim.slide_in_from_bottom);
        view.startAnimation(animation);
        pop_menu_background.setVisibility(View.VISIBLE);
        menuWindow.setOnDismissListener(new OnDismissListener() {
            public void onDismiss() {
                menuWindow = null;
                pop_menu_background.setVisibility(View.GONE);
            }
        });
    }

    // ----------------城市控件--------------
    private void showCityPopwindow(View view) {
        cityWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        cityWindow.setAnimationStyle(R.style.AnimationPopup);
        cityWindow.setFocusable(true);
        cityWindow.setBackgroundDrawable(new BitmapDrawable());
        // popwindow显示在屏幕的位置
        // menuWindow.showAtLocation(findViewById(R.id.layout), Gravity.CENTER,
        // 0, (int)(Yto*0.05));
        cityWindow.showAtLocation(right_btn, Gravity.BOTTOM, 0, 0);
        cityWindow.setOutsideTouchable(true);
        Animation animation = (Animation) AnimationUtils.loadAnimation(this,
                R.anim.slide_in_from_bottom);
        view.startAnimation(animation);
        pop_menu_background.setVisibility(View.VISIBLE);
        cityWindow.setOnDismissListener(new OnDismissListener() {
            public void onDismiss() {
                cityWindow = null;
                pop_menu_background.setVisibility(View.GONE);
            }
        });
    }

    private View getDataPick() {
        // 得到真正的时间
        Calendar c = Calendar.getInstance();
        curYear = c.get(Calendar.YEAR);
        curMonth = c.get(Calendar.MONTH) + 1;// 通过Calendar算出的月数要+1
        curDate = c.get(Calendar.DATE);
        String date = birthday.getText().toString();
        if (date.length() != 10) {
            Toast.makeText(this, R.string.date_error, Toast.LENGTH_SHORT)
                    .show();
        }
        if (date != null && !date.equals("")) {
            nowyear = Integer.parseInt(date.substring(0, 4));
            nowmonth = Integer.parseInt(date.substring(5, 7));
            nowday = Integer.parseInt(date.substring(8, 10));
        } else {
            nowyear = 1985;
            nowmonth = 1;
            nowday = 1;
        }
        LayoutInflater inflater = LayoutInflater.from(UserEditActivity.this);
        View view = inflater.inflate(R.layout.datepicker_dialog, null);
        yearWheel = (WheelView) view.findViewById(R.id.year_wheel);
        yearWheel.setAdapter(new NumericWheelAdapter(1920, curYear));
//        yearWheel.setLabel("年");
        yearWheel.setCyclic(true);
        yearWheel.addScrollingListener(scrollListener);

        monthWheel = (WheelView) view.findViewById(R.id.month_wheel);
        // monthWheel.setAdapter(new NumericWheelAdapter(1, curMonth-1,"%02d"));
        if (nowyear == curYear) {
            if (nowmonth >= curMonth) {
                monthWheel.setAdapter(new NumericWheelAdapter(1, curMonth,
                        "%02d"));
                monthWheel.setCurrentItem(curMonth - 1);
            }
        } else {
            monthWheel.setAdapter(new NumericWheelAdapter(1, 12, "%02d"));
        }
//        monthWheel.setLabel("月");
        monthWheel.setCyclic(true);
        monthWheel.addScrollingListener(scrollListener);

        dayWheel = (WheelView) view.findViewById(R.id.day_wheel);
        initDay(curYear, curMonth);
//        dayWheel.setLabel("日");
        dayWheel.setCyclic(true);

        yearWheel.setCurrentItem(nowyear - 1920);
        monthWheel.setCurrentItem(nowmonth - 1);
        dayWheel.setCurrentItem(nowday - 1);

        TextView bt = (TextView) view.findViewById(R.id.bt_ok);
        bt.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // String str = (year.getCurrentItem()+1920) + "-"+
                // (month.getCurrentItem()+1)+"-"+(day.getCurrentItem()+1);
                newYearString = yearWheel.getCurrentItem() + 1920 + "";
                if ((monthWheel.getCurrentItem() + 1) < 10) {
                    newMonthString = "0" + (monthWheel.getCurrentItem() + 1);
                } else {
                    newMonthString = (monthWheel.getCurrentItem() + 1) + "";
                }
                if ((dayWheel.getCurrentItem() + 1) < 10) {
                    newDayString = "0" + (dayWheel.getCurrentItem() + 1);
                } else {
                    newDayString = (dayWheel.getCurrentItem() + 1) + "";
                }
                Log.e(TAG, "zzzzz年----------" + newYearString);
                Log.e(TAG, "zzzzz月----------" + newMonthString);
                Log.e(TAG, "zzzzz日----------" + newDayString);
                String string = newYearString + "-" + newMonthString + "-"
                        + newDayString;
                birthday.setText(string);
                // Toast.makeText(MainActivity.this, str,
                // Toast.LENGTH_LONG).show();
                menuWindow.dismiss();
                pop_menu_background.setVisibility(View.GONE);
            }
        });
        TextView cancel = (TextView) view.findViewById(R.id.bt_cancel);
        cancel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                menuWindow.dismiss();
                pop_menu_background.setVisibility(View.GONE);
            }
        });
        return view;
    }

    String provinceContent = "";
    String cityContent = "";
    String conuyContent = "";
    boolean isSelect = false;

    // 显示城市view
    private View getCityView() {
        isSelect = false;
        LayoutInflater inflater = LayoutInflater.from(UserEditActivity.this);
        View view = inflater.inflate(R.layout.citypicker_dialog, null);
        final CityPicker cityPicker = (CityPicker) view
                .findViewById(R.id.citypicker);
        cityPicker.setOnSelectingListener(new OnSelectingListener() {

            @Override
            public void selected(boolean selected) {
                // TODO Auto-generated method stub
                isSelect = selected;
                provinceContent = cityPicker.getProvince_string();
                cityContent = cityPicker.getCity_string();
                conuyContent = cityPicker.getCouny_string();
            }
        });
        TextView cancel = (TextView) view.findViewById(R.id.cbt_cancel);
        cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                cityWindow.dismiss();
                pop_menu_background.setVisibility(View.GONE);
            }
        });
        TextView bt = (TextView) view.findViewById(R.id.cbt_ok);
        bt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (isSelect) {
                    tx_suozai_points.setText(provinceContent);
                    tx_suozai_city.setText(cityContent);
                    tx_suozai_couny.setText(conuyContent);
                } else {
                    tx_suozai_points.setText("北京市");
                    tx_suozai_city.setText("县");
                    tx_suozai_couny.setText("密云县");
                }
                cityWindow.dismiss();
                pop_menu_background.setVisibility(View.GONE);
            }
        });
        return view;
    }

    OnWheelScrollListener scrollListener = new OnWheelScrollListener() {

        public void onScrollingStarted(WheelView wheel) {

        }

        public void onScrollingFinished(WheelView wheel) {
            // TODO Auto-generated method stub
            int n_year = yearWheel.getCurrentItem() + 1920;// 设置年
            int n_month = monthWheel.getCurrentItem() + 1;// 设置月
            if (n_year == curYear) {
                if (n_month >= curMonth) {
                    monthWheel.setAdapter(new NumericWheelAdapter(1, curMonth,
                            "%02d"));
                    monthWheel.setCurrentItem(curMonth - 1);
                }
            } else {
                monthWheel.setAdapter(new NumericWheelAdapter(1, 12, "%02d"));
            }
            initDay(n_year, n_month);
        }
    };

    /**
     * @param year
     * @param month
     * @return
     */
    private int getDay(int year, int month) {
        int day = 30;
        boolean flag = false;
        switch (year % 4) {
            case 0:
                flag = true;
                break;
            default:
                flag = false;
                break;
        }
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day = 31;
                break;
            case 2:
                day = flag ? 29 : 28;
                break;
            default:
                day = 30;
                break;
        }
        return day;
    }

    /**
     */
    private void initDay(int syear, int smonth) {
        int d = getDay(syear, smonth);
        int n_year = yearWheel.getCurrentItem() + 1920;// 设置年
        int n_month = monthWheel.getCurrentItem() + 1;// 设置月
        if (n_year == curYear && n_month >= curMonth && d >= curDate) {
            d = curDate;
            dayWheel.setCurrentItem(curDate - 1);
        }
        dayWheel.setAdapter(new NumericWheelAdapter(1, d, "%02d"));
    }

    // -----------------------------------------------------------

    /**
     * 显示选择对话框
     */
//	private void showDialog() {
//
//		new AlertDialog.Builder(this)
//				.setTitle(
//						getResources().getString(R.string.sports_set_faceimage))
//				.setItems(items, new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						switch (which) {
//						case 0:
//
//							Intent intentFromCapture = new Intent(
//									MediaStore.ACTION_IMAGE_CAPTURE);
//							// 判断存储卡是否可以用，可用进行存储
//							if (Tools.hasSdcard()) {
//
//								intentFromCapture.putExtra(
//										MediaStore.EXTRA_OUTPUT,
//										Uri.fromFile(new File(Environment
//												.getExternalStorageDirectory(),
//												IMAGE_FILE_NAME)));
//							}
//
//							startActivityForResult(intentFromCapture,
//									CAMERA_REQUEST_CODE);
//							break;
//						case 1:
//							/*
//							 * Intent intentFromGallery = new Intent();
//							 * intentFromGallery.setType("image/*"); // 设置文件类型
//							 * intentFromGallery
//							 * .setAction(Intent.ACTION_GET_CONTENT);
//							 */
//							Intent intentFromGallery = new Intent(
//									Intent.ACTION_PICK, null);
//							intentFromGallery
//									.setDataAndType(
//											MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//											"image/*");
//							startActivityForResult(intentFromGallery,
//									IMAGE_REQUEST_CODE);
//							break;
//						case 2:
//							dialog.dismiss();
//							break;
//						}
//					}
//				}).show();
//	}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 结果码不等于取消时候
        if (resultCode != RESULT_CANCELED) {

            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    startPhotoZoom(data.getData());
                    break;
                case CAMERA_REQUEST_CODE:
                    if (Tools.hasSdcard()) {
                        File tempFile = new File(
                                Environment.getExternalStorageDirectory()
                                        + File.separator + IMAGE_FILE_NAME);
                        startPhotoZoom(Uri.fromFile(tempFile));
                    } else {
                        Toast.makeText(
                                UserEditActivity.this,
                                getResources().getString(
                                        R.string.sports_toast_nosdcard),
                                Toast.LENGTH_LONG).show();
                    }

                    break;
                case RESULT_REQUEST_CODE:
                    if (data != null) {
                        getImageToView(data);
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 2);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param picdata
     */
    private void getImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            drawable = new BitmapDrawable(photo);
            face.setImageDrawable(drawable);
            isfacechanged = true;
            Tools.SaveBitmapAsFile(FACE_PATH, photo);
            faceCount = 1;
        }
    }

    private void updateDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        birthday.setText(simpleDateFormat.format(cal.getTime()));
    }

    private boolean isPhoneNumber(String phoneNumber) {
        return (phoneNumber != null && phoneNumber
                .matches("^(1(3|5|7|8)[0-9])\\d{8}$"));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // if (keyCode == KeyEvent.KEYCODE_BACK && LoginActivity.mIsRegister &&
        // !mDialog.isShowing()) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.e(TAG, "onKeyDown KEYCODE_BACK");
            if (LoginActivity.mIsRegister) {
                if (nickname.getText().toString().trim() == null
                        || "".equals(nickname.getText().toString().trim())) {
                    Toast.makeText(
                            getApplication(),
                            getResources()
                                    .getString(R.string.nickname_ont_be_empty),
                            Toast.LENGTH_LONG).show();
                } else {
                    //新加需求昵称大于15的话不能提交
                    if (judgeNum(nickname.getText().toString().trim())) {
                        Toast.makeText(
                                getApplication(),
                                getResources()
                                        .getString(R.string.sports_nickname_toolong),
                                Toast.LENGTH_LONG).show();
                    } else if (nickname.getText().toString().trim().contains("_")) {
                        Toast.makeText(
                                getApplication(),
                                getResources()
                                        .getString(R.string.sports_toast_illegal_character),
                                Toast.LENGTH_LONG).show();
                    } else {
                        showWaitDialog();
                        (new WeiBoRegistBackTask()).execute();
                    }
                }

            } else {
                isnicknamechanged = nickname.getText().toString().trim()
                        .equals(nickname2);
                boolean isGeXingQianmingChanged = ed_gexing_qianming.getText().toString().trim().equals(sing2);
                boolean isSexChanged = tx_sexs.getText().toString().trim().equals(sex2);
                isbirthdaychanged = birthday2
                        .equals(birthday.getText().toString());
                boolean isCityChanged = false;
                if (!tx_suozai_points.getText().toString().trim().equals(province2)) {
                    isCityChanged = true;
                }
                if (!tx_suozai_city.getText().toString().trim().equals(city2)) {
                    isCityChanged = true;
                }
                if (!tx_suozai_couny.getText().toString().trim().equals(area2)) {
                    isCityChanged = true;
                }
                boolean isHeightChanged = mheight.getText().toString().trim().equals(height2 + "");
                boolean isWeightChanged = mweight.getText().toString().trim().equals(weight2 + "");
                if (isfacechanged || (isnicknamechanged == false) || (isGeXingQianmingChanged == false)
                        || (isSexChanged == false) || (isbirthdaychanged == false) || isCityChanged
                        || (isHeightChanged == false) || (isWeightChanged == false)) {
                    tipPopWindow(getResources().getString(R.string.info_tips));
                } else {
                    finish();
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    class WeiBoRegistBackTask extends AsyncTask<Void, Void, ApiBack> {

        @Override
        protected ApiBack doInBackground(Void... arg0) {
            try {
                ApiBack back = null;
                SharedPreferences sp = getSharedPreferences("user_login_info",
                        Context.MODE_PRIVATE);
                String type = sp.getString("weibotype", "");
                if (type.equals(WeiboType.QQzone)) {
                    back = ApiJsonParser.modifymsg(mSportsApp.getSessionId(),
                            "", "", "", FACE_PATH_TWO, "", birthday2, phone2,
                            "", "0", "0", "", "", "", "", "");
                } else {
                    back = ApiJsonParser.modifymsg(mSportsApp.getSessionId(),
                            "", "", "", "", "", birthday2, phone2, "", "0",
                            "0", "", "", "", "", "");
                }
                if (!"".equals(mSportsApp.getSessionId())
                        && mSportsApp.getSessionId() != null) {
                    mSportsApp.setSportUser(ApiJsonParser
                            .refreshRank(mSportsApp.getSessionId()));
                }
                return back;
            } catch (ApiNetException e) {
                if (waitProgressDialog != null)
                    if (waitProgressDialog.isShowing())
                        waitProgressDialog.dismiss();
                SportsApp.eMsg = Message.obtain(mExceptionHandler,
                        SportsExceptionHandler.NET_ERROR);
                SportsApp.eMsg.sendToTarget();
                e.printStackTrace();
            } catch (ApiSessionOutException e) {
                if (waitProgressDialog != null)
                    if (waitProgressDialog.isShowing())
                        waitProgressDialog.dismiss();
                e.printStackTrace();
                SportsApp.eMsg = Message.obtain(mExceptionHandler,
                        SportsExceptionHandler.SESSION_OUT);
                SportsApp.eMsg.sendToTarget();
                startActivity(new Intent(UserEditActivity.this,
                        LoginActivity.class));
            }
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        protected void onPostExecute(ApiBack back) {
            // TODO Auto-generated method stub
            if (back == null) {
                return;
            }
            /*
             * if(waitProgressDialog != null) if(waitProgressDialog.isShowing())
			 * waitProgressDialog.dismiss();
			 */
            LoginActivity.mIsRegister = false;
            if (mSportsApp.LoginOption) {
                startActivity(new Intent(UserEditActivity.this,
                        MainFragmentActivity.class));
            } else {
                if (mSportsApp.getmHandler() != null) {
                    Handler mHandler = mSportsApp.getmHandler();
                    mHandler.sendMessage(mHandler
                            .obtainMessage(StateActivity.UPDATE_TRYTOLOGIN));
                }
                if (mSportsApp.getfHandler() != null) {
                    Handler fHandler = mSportsApp.getfHandler();
                    fHandler.sendMessage(fHandler
                            .obtainMessage(FoxSportsSetting.UPDATE_TRYTOLOGIN));
                }
                Handler mainHandler = mSportsApp.getMainHandler();
                mainHandler.sendMessage(mainHandler
                        .obtainMessage(MainFragmentActivity.UPDATE_TRYTOLOGIN));
                Handler personalFindHandler = mSportsApp
                        .getPersonalFindHandler();
                if (personalFindHandler != null) {
                    Message msgMessage = personalFindHandler
                            .obtainMessage(PersonalPageMainActivity.USEREDITFRESH_VIEW);
                    msgMessage.obj = isfacechanged;
                    personalFindHandler.sendMessage(msgMessage);
                }
            }
            mSportsApp.LoginOption = true;
            UserEditActivity.this.finish();
            finish();
        }
    }

    ;

    public boolean trytoLogin(String account, String pwd, Context con) {
        if (Tools.isNetworkConnected(con)) {
            if (account.equals("") || pwd.equals("")) {
                return false;
            }
            try {
                SharedPreferences sp_umeng = getSharedPreferences("UmengDeviceToken", Activity.MODE_PRIVATE);
                ApiBack message = ApiJsonParser.login(account, pwd, 1, sp_umeng.getString("device_token", ""));
                if (message == null) {
                    Log.e(TAG, "login failed");
                    return false;
                } else if (message.getFlag() == 0 || message.getFlag() == 1) {
                    if (message.getFlag() == 1) {
                        SportsApp.mIsAdmin = true;
                    } else {
                        SportsApp.mIsAdmin = false;
                    }
                    Log.e(TAG, "loginsuccess");
                    String sessionid = message.getMsg().substring(7);
                    Log.e(TAG, "session_id" + sessionid);
                    if (mSportsApp == null) {
                        mSportsApp = SportsApp.getInstance();
                    }
                    Log.d(TAG, "mSportsApp:" + mSportsApp);
                    mSportsApp.setSessionId(sessionid);
                    mSportsApp.setLogin(true);
                    Log.e(TAG, "session_id in mSportsApp:" + sessionid);

                    return true;
                } else {
                    Log.e("develop_debug", "UserEditActivity.java 1941" + message.getMsg());
                    Log.e(TAG, message.getMsg());
                    return false;
                }
            } catch (ApiNetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mIsDoing = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mIsDoing = false;
    }

    private void setBmi() {
        // BMI＝体重（kg）÷（身高（m）×身高（m））
        // 　　例：体重55kg、身高160cm的情况　55kg÷（1.6m×1.6m）＝21.5
        // 　　不足19.8 　　　　　瘦
        // 　　不足19.8～24.2 　　普通
        // 　　不足24.2～25.0　　 轻度肥胖
        // 　　25.0以上 　　　　　肥胖
        // 　　 ※ BMI只适合成人、不适用于小孩子。
        double mHeight = 0.0;
        double mWeight = 0.0;
        if (mheight.getText().toString() != null
                && !"".equals(mheight.getText().toString())) {
            mHeight = Double.valueOf(mheight.getText().toString());
        }
        if (mweight.getText().toString() != null
                && !"".equals(mweight.getText().toString())) {
            mWeight = Double.valueOf(mweight.getText().toString());
        }
        if (mHeight != 0.0 && mWeight != 0.0) {
            DecimalFormat df = new DecimalFormat("###.0");
            double hwNum = mWeight / ((mHeight / 100) * (mHeight / 100));
            String format = df.format(hwNum);
            bmi_num.setText(format);
            if (Double.valueOf(format) < 19.8) {
                bmi_content
                        .setText(getResources().getString(R.string.bmi_shou));
            } else if (Double.valueOf(format) < 24.2
                    && Double.valueOf(format) >= 19.8) {
                bmi_content.setText(getResources().getString(
                        R.string.bmi_nomole));
            } else if (Double.valueOf(format) < 25.0
                    && Double.valueOf(format) >= 24.2) {
                bmi_content.setText(getResources().getString(
                        R.string.bmi_little_fat));
            } else if (Double.valueOf(format) >= 25.0) {
                bmi_content.setText(getResources().getString(R.string.bmi_fat));
            }
        } else {
            bmi_num.setText("20.0");
            bmi_content.setText(getResources().getString(R.string.bmi_nomole));
        }
    }

    // 设置性别
    private void showSexPop() {
        if (sexPopWindow != null && sexPopWindow.isShowing())
            return;
        LayoutInflater inflater = LayoutInflater.from(this);
        final LinearLayout myView = (LinearLayout) inflater.inflate(
                R.layout.setting_sex_layout1, null);
//        final LinearLayout myView = (LinearLayout) inflater.inflate(
//                R.layout.nomalpicker_dialog, null);
//        final WheelView  normal_wheel = (WheelView) myView.findViewById(R.id.normal_wheel);
//        normal_wheel.setAdapter(new NumericWheelAdapter(0, 1));
//        normal_wheel.setCyclic(true);
////        normal_wheel.setCurrentItem(Integer.parseInt(mheight.getText().toString())-120);
//        TextView bt_content=(TextView)myView.findViewById(R.id.bt_content);
//        bt_content.setText(getResources().getString(R.string.sex_setting));
        myView.findViewById(R.id.btn_male).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                tx_sexs.setText(getResources().getString(R.string.male));
//                face.setImageResource(R.drawable.sports_user_edit_portrait_male);
                sexPopWindow.dismiss();
                myView.setVisibility(View.GONE);
                pop_menu_background.setVisibility(View.GONE);
            }
        });
        myView.findViewById(R.id.btn_female).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                tx_sexs.setText(getResources().getString(
                        R.string.female));
//                face.setImageResource(R.drawable.sports_user_edit_portrait);
                sexPopWindow.dismiss();
                myView.setVisibility(View.GONE);
                pop_menu_background.setVisibility(View.GONE);
            }
        });

//        RadioGroup radioGroup = (RadioGroup) myView
//                .findViewById(R.id.radioGroup1);
//        final RadioButton female = (RadioButton) myView
//                .findViewById(R.id.female_radio);
//        final RadioButton male = (RadioButton) myView
//                .findViewById(R.id.male_radio);
//        if (getResources().getString(R.string.male).equals(
//                tx_sexs.getText().toString())) {
//            male.setChecked(true);
//        } else if (getResources().getString(R.string.female).equals(
//                tx_sexs.getText().toString())) {
//            female.setChecked(true);
//        }
//        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                if (faceCount == 0) {
//                    switch (checkedId) {
//                        case R.id.female_radio:
//                            face.setImageResource(R.drawable.sports_user_edit_portrait);
//                            tx_sexs.setText(getResources().getString(
//                                    R.string.female));
//                            break;
//                        case R.id.male_radio:
//                            face.setImageResource(R.drawable.sports_user_edit_portrait_male);
//                            tx_sexs.setText(getResources().getString(R.string.male));
//                            break;
//
//                    }
//                }
//            }
//        });
//
//        myView.findViewById(R.id.sports_goal_back).setOnClickListener(
//                new OnClickListener() {
//
//                    @Override
//                    public void onClick(View arg0) {
//                        // TODO Auto-generated method stub
//                        sexPopWindow.dismiss();
//                        myView.setVisibility(View.GONE);
//                        pop_menu_background.setVisibility(View.GONE);
//                    }
//                });
//        myView.findViewById(R.id.sports_goal_ok).setOnClickListener(
//                new OnClickListener() {
//
//                    @Override
//                    public void onClick(View arg0) {
//                        // TODO Auto-generated method stub
//                        if (male.isChecked()) {
//                            tx_sexs.setText(getResources().getString(
//                                    R.string.male));
//                        } else if (female.isChecked()) {
//                            tx_sexs.setText(getResources().getString(
//                                    R.string.female));
//                        }
//                        sexPopWindow.dismiss();
//                        myView.setVisibility(View.GONE);
//                        pop_menu_background.setVisibility(View.GONE);
//                    }
//                });

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        // int height = wm.getDefaultDisplay().getHeight();
        sexPopWindow = new PopupWindow(myView, width- SportsApp.dip2px(20),
                RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        sexPopWindow.setAnimationStyle(R.style.AnimationPopup);
        sexPopWindow.setOutsideTouchable(true);
        sexPopWindow.setBackgroundDrawable(new BitmapDrawable());
        sexPopWindow.showAtLocation(right_btn, Gravity.CENTER, 0, 0);
        sexPopWindow.setOnDismissListener(this);
        // final Animation animation = (Animation) AnimationUtils.loadAnimation(
        // this, R.anim.slide_in_from_bottom);
        // myView.startAnimation(animation);
        pop_menu_background.setVisibility(View.VISIBLE);
    }

    // 设置身高
    private void showHeightPop() {
        if (heightPopWindow != null && heightPopWindow.isShowing())
            return;
        LayoutInflater inflater = LayoutInflater.from(this);
        final LinearLayout myView = (LinearLayout) inflater.inflate(
                R.layout.nomalpicker_dialog, null);
        final WheelView normal_wheel = (WheelView) myView.findViewById(R.id.normal_wheel);
        normal_wheel.setAdapter(new NumericWheelAdapter(120, 210));
        normal_wheel.setCyclic(false);
        normal_wheel.setCurrentItem(Integer.parseInt(mheight.getText().toString()) - 120);
        TextView bt_content = (TextView) myView.findViewById(R.id.bt_content);
        bt_content.setText(getResources().getString(R.string.sports_height1) + "(cm)");
//        normal_wheel.addScrollingListener(scrollListener);
//        height = (EditText) myView.findViewById(R.id.ed_height1);
//        height.setText(mheight.getText().toString());
//        //设置光标位置
//        height.setSelection(mheight.getText().toString().length());
//        height.setOnFocusChangeListener(new OnFocusChangeListener() {
//            public void onFocusChange(View v, boolean hasFocus) {
//                adjustHeight();
//                setBmi();
//            }
//        });
//
//        bt_add1 = (ImageButton) myView.findViewById(R.id.bt_add1);
//        bt_del1 = (ImageButton) myView.findViewById(R.id.bt_del1);
//        bt_add1.setOnClickListener(this);
//        bt_del1.setOnClickListener(this);
        myView.findViewById(R.id.bt_cancel).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        heightPopWindow.dismiss();
                        myView.setVisibility(View.GONE);
                        pop_menu_background.setVisibility(View.GONE);
                    }
                });
        myView.findViewById(R.id.bt_ok).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        mheight.setText(normal_wheel.getCurrentItem() + 120 + "");
                        setBmi();
                        heightPopWindow.dismiss();
                        myView.setVisibility(View.GONE);
                        pop_menu_background.setVisibility(View.GONE);
                    }
                });

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        // int height = wm.getDefaultDisplay().getHeight();
        heightPopWindow = new PopupWindow(myView, width,
                RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        heightPopWindow.setAnimationStyle(R.style.AnimationPopup);
        heightPopWindow.setOutsideTouchable(true);
        heightPopWindow.setBackgroundDrawable(new BitmapDrawable());
        heightPopWindow.showAtLocation(right_btn, Gravity.BOTTOM, 0, 0);
        heightPopWindow.setOnDismissListener(this);
        // final Animation animation = (Animation) AnimationUtils.loadAnimation(
        // this, R.anim.slide_in_from_bottom);
        // myView.startAnimation(animation);
        pop_menu_background.setVisibility(View.VISIBLE);
    }

    // 设置体重
    private void showWeightPop() {
        if (weightPopWindow != null && weightPopWindow.isShowing())
            return;
        LayoutInflater inflater = LayoutInflater.from(this);
        final LinearLayout myView = (LinearLayout) inflater.inflate(
                R.layout.nomalpicker_dialog, null);
//        weight = (EditText) myView.findViewById(R.id.ed_weight);
//        weight.setText(mweight.getText().toString());
//        //设置光标位置
//        weight.setSelection(mweight.getText().toString().length());
//        weight.setOnFocusChangeListener(new OnFocusChangeListener() {
//            public void onFocusChange(View v, boolean hasFocus) {
//                adjustWeight();
//                setBmi();
//            }
//        });
//
//        bt_add2 = (ImageButton) myView.findViewById(R.id.bt_add2);
//        bt_del2 = (ImageButton) myView.findViewById(R.id.bt_del2);
//        bt_add2.setOnClickListener(this);
//        bt_del2.setOnClickListener(this);
        final WheelView normal_wheel = (WheelView) myView.findViewById(R.id.normal_wheel);
        normal_wheel.setAdapter(new NumericWheelAdapter(30, 150));
        normal_wheel.setCyclic(false);
        normal_wheel.setCurrentItem(Integer.parseInt(mweight.getText().toString()) - 30);
        TextView bt_content = (TextView) myView.findViewById(R.id.bt_content);
        bt_content.setText(getResources().getString(R.string.sports_weight1) + "(kg)");
        myView.findViewById(R.id.bt_cancel).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        weightPopWindow.dismiss();
                        myView.setVisibility(View.GONE);
                        pop_menu_background.setVisibility(View.GONE);
                    }
                });
        myView.findViewById(R.id.bt_ok).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        mweight.setText(normal_wheel.getCurrentItem() + 30 + "");
                        setBmi();
                        weightPopWindow.dismiss();
                        myView.setVisibility(View.GONE);
                        pop_menu_background.setVisibility(View.GONE);
                    }
                });

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        // int height = wm.getDefaultDisplay().getHeight();
        weightPopWindow = new PopupWindow(myView, width,
                RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        weightPopWindow.setAnimationStyle(R.style.AnimationPopup);
        weightPopWindow.setOutsideTouchable(true);
        weightPopWindow.setBackgroundDrawable(new BitmapDrawable());
        weightPopWindow.showAtLocation(right_btn, Gravity.BOTTOM, 0, 0);
        weightPopWindow.setOnDismissListener(this);
        // final Animation animation = (Animation) AnimationUtils.loadAnimation(
        // this, R.anim.slide_in_from_bottom);
        // myView.startAnimation(animation);
        pop_menu_background.setVisibility(View.VISIBLE);
    }

    // 选中头像
    public void shotSelectImages() {
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout myView = (LinearLayout) inflater.inflate(
                R.layout.select_images_from_local1, null);

        myView.findViewById(R.id.btn_paizhao).setOnClickListener(this);
        myView.findViewById(R.id.btn_select_pic).setOnClickListener(this);
        // myView.findViewById(R.id.btn_cancle).setOnClickListener(this);

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        photoPopWindow = new PopupWindow(myView, width - SportsApp.dip2px(40),
                RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        // photoPopWindow.setAnimationStyle(R.style.AnimationPopup);
        photoPopWindow.setOutsideTouchable(true);
        photoPopWindow.setBackgroundDrawable(new BitmapDrawable());
        photoPopWindow.showAtLocation(right_btn, Gravity.CENTER, 0, 0);
        photoPopWindow.setOnDismissListener(this);
        // final Animation animation = (Animation) AnimationUtils.loadAnimation(
        // this, R.anim.slide_in_from_bottom);
        // myView.startAnimation(animation);
        pop_menu_background.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDismiss() {
        // TODO Auto-generated method stub
        pop_menu_background.setVisibility(View.GONE);
    }

    // 判断用户名过长
    private boolean judgeNum(String name) {
        boolean isJudeg = false;

        if (name.length() > 15) {
            isJudeg = true;
            return isJudeg;
        }
        // try {
        // int length = name.getBytes("gbk").length;
        // if (length != 0 && length > 20) {
        // isJudeg = true;
        // return isJudeg;
        // }
        //
        // if (name.length() > 15) {
        // isJudeg = true;
        // return isJudeg;
        // }
        // } catch (UnsupportedEncodingException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }

        return isJudeg;
    }



    private void tipPopWindow(String message) {
        if (myWindow != null && myWindow.isShowing())
            return;
        LayoutInflater inflater = LayoutInflater.from(this);
       LinearLayout myView = (LinearLayout) inflater.inflate(R.layout.sports_dialog1, null);
        ((TextView) myView.findViewById(R.id.message)).setText(message);
        TextView bt_ok=(TextView) myView.findViewById(R.id.bt_ok);
        bt_ok.setText(getResources().getString(R.string.continue_edit));
        bt_ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myWindow != null) {
                    myWindow.dismiss();
                }
                pop_menu_background.setVisibility(View.GONE);
            }
        });
        TextView bt_cancel=(TextView) myView.findViewById(R.id.bt_cancel);
        bt_cancel.setText(getResources().getString(R.string.dialog_button_giveup));
        bt_cancel.setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        if (myWindow != null) {
                            myWindow.dismiss();
                        }
                        pop_menu_background.setVisibility(View.GONE);
                        finish();
                    }
                });
        // myEditCalories
        // .setText(String.valueOf(getDate.getInt("editCalories", 0)));
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        // int height = wm.getDefaultDisplay().getHeight();
        myWindow = new PopupWindow(myView, width - SportsApp.dip2px(20),
                RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        myWindow.setAnimationStyle(R.style.AnimationPopup);
        myWindow.setOutsideTouchable(true);
        myWindow.setBackgroundDrawable(new BitmapDrawable());
//		myWindow.setBackgroundDrawable(null);
        myWindow.showAtLocation(right_btn, Gravity.CENTER, 0, 0);
        myWindow.setOnDismissListener(this);
        // final Animation animation = (Animation) AnimationUtils.loadAnimation(
        // this, R.anim.slide_in_from_bottom);
        // myView.startAnimation(animation);
        pop_menu_background.setVisibility(View.VISIBLE);
    }

}
