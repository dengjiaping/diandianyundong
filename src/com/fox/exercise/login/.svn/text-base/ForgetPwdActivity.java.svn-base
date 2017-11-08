package com.fox.exercise.login;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fox.exercise.R;
import com.fox.exercise.SmartBarUtils;
import com.fox.exercise.api.ApiBack;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.util.SportsForgetPwdUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import cn.ingenic.indroidsync.SportsApp;

public class ForgetPwdActivity extends Activity implements
        View.OnClickListener, PopupWindow.OnDismissListener {
    private static final String TAG = "ForgetPwdActivity";
    private EditText phoneNum;
    private EditText yzNum;
    private EditText newPwd;
    private EditText newPwd_agagin;
    private Button bt_yz;
    private CheckBox checkBox_psw;


    //	private ProgressDialog forgetProgressDialog = null;
//	private Dialog alertDialog;
    private String phonenumberString = null;
    private String result = "";
    private RelativeLayout oneLayout;
    private TextView phoneTextView;
    private SportsApp fSportsApp;
    String SENT_SMS_ACTION = "SENT_SMS_ACTION";
    String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";
    private int getTheNumber;
    private String time;
    private TimeCount timeCount;
    private String newpwdString = "";
    private String newpwdagainString = "";
    private Dialog codeDialog;
    private Dialog pwdDialog;
    private static final int GETCODE_FAIL_NONET = 0x0012;


    private PopupWindow myWindow = null;
    private LinearLayout myView;
    private RelativeLayout mPopMenuBack, mainLL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        boolean findMethod = SmartBarUtils.findActionBarTabsShowAtBottom();
        if (!findMethod) {
            // 取消ActionBar拆分，换用TabHost
            getWindow().setUiOptions(0);
            getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        setContentView(R.layout.sports_forgetpwd);
        if (findMethod) {
            getActionBar().setDisplayShowHomeEnabled(false);
            getActionBar().setDisplayShowTitleEnabled(false);
            SmartBarUtils.setActionModeHeaderHidden(getActionBar(), true);
            SmartBarUtils.setActionBarViewCollapsable(getActionBar(), true);
        }
        fSportsApp = SportsApp.getInstance();
        initView();
        setViewStatus();
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MobclickAgent.onPageStart("ForgetPwdActivity");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPageEnd("ForgetPwdActivity");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    public void initView() {
        // TODO Auto-generated method stub
        mainLL = (RelativeLayout) findViewById(R.id.rl);
        mPopMenuBack = (RelativeLayout) findViewById(R.id.set_menu_background);
        phoneNum = (EditText) findViewById(R.id.regist_mail);
        yzNum = (EditText) findViewById(R.id.ed_yz);
        bt_yz = (Button) findViewById(R.id.bt_yz);
        phoneTextView = (TextView) findViewById(R.id.phoneText);
        oneLayout = (RelativeLayout) findViewById(R.id.layout_email);
        newPwd = (EditText) findViewById(R.id.edtxt_newpwd);
        newPwd_agagin = (EditText) findViewById(R.id.edtxt_check_newpwd);
        newPwd.addTextChangedListener(new PassWord(newPwd));
        newPwd_agagin.addTextChangedListener(new PassWord(newPwd_agagin));

        checkBox_psw = (CheckBox) findViewById(R.id.checkbox_psw);
        checkBox_psw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    newPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    newPwd_agagin.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    newPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    newPwd_agagin.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });


        newpwdString = newPwd.getText().toString();
        newpwdagainString = newPwd_agagin.getText().toString();
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        oneLayout.measure(w, h);
        phoneTextView.measure(w, h);
        bt_yz.measure(w, h);
        RelativeLayout.LayoutParams lps = (RelativeLayout.LayoutParams) phoneNum
                .getLayoutParams();
        lps.width = (int) (SportsApp.ScreenWidth - fSportsApp.dip2px(77)
                - phoneTextView.getMeasuredWidth() - bt_yz.getMeasuredWidth());
        phoneNum.setLayoutParams(lps);
        timeCount = new TimeCount(60000, 1000);//构造CountDownTimer对象
        initCodeDialog();
        initPwdDialog();
    }

    public void setViewStatus() {
        // TODO Auto-generated method stub

        findViewById(R.id.bt_submit).setOnClickListener(this);
        bt_yz.setOnClickListener(this);
        findViewById(R.id.bt_back).setOnClickListener(this);
    }

    class PassWord implements TextWatcher {

        //监听改变的文本框
        private EditText editText;

        /**
         * 构造函数
         */
        public PassWord(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void onTextChanged(CharSequence ss, int start, int before, int count) {
            String editable = editText.getText().toString();
            String str = stringFilter(editable.toString());
            if (!editable.equals(str)) {
                editText.setText(str);
                //设置新的光标所在位置 www.2cto.com
                editText.setSelection(str.length());
                Toast.makeText(ForgetPwdActivity.this, getResources().getString(R.string.words_no_access), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

    }

    public static String stringFilter(String str) throws PatternSyntaxException {
        // 只允许字母和数字
        String regEx = "[^a-zA-Z0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_back:
                // startActivity(new Intent(ForgetPwdActivity.this,
                // LoginActivity.class));
                finish();
                break;
            case R.id.bt_yz:
                if ("".equals(phoneNum.getText().toString())) {
                    //电话号码不能为空

//                    Toast.makeText(
//                            ForgetPwdActivity.this,
//                            getResources().getString(
//                                    R.string.phone_number_not_empty),
//                            Toast.LENGTH_SHORT).show();

                    exitPopWindow(getResources().getString(R.string.phone_number_not_empty_fgt), 1);


                } else if (!phoneNum.getText().toString()
                        .matches("^(1(3|5|7|8)[0-9])\\d{8}$")
                        && !phoneNum.getText().toString().matches("^(0033)\\d{9}$")) {
                    //电话号码格式不对
//                    Toast.makeText(
//                            ForgetPwdActivity.this,
//                            getResources().getString(
//                                    R.string.phone_number_format_wrong),
//                            Toast.LENGTH_SHORT).show();
                    exitPopWindow(getResources().getString(R.string.phone_number_not_empty_fgt), 1);
                } else if (fSportsApp.getForgetPwdTime() > 0) {
                    //获取验证码过于频繁
                    Toast.makeText(
                            ForgetPwdActivity.this,
                            getResources().getString(
                                    R.string.forgetpwd_confirm_newpwd_soquickly),
                            Toast.LENGTH_SHORT).show();
                } else {
                    phonenumberString = phoneNum.getText().toString();
                    time = fSportsApp.getCurrentDate();
                    String dateNumber = SportsForgetPwdUtil
                            .readTotalNumberFromFile(phonenumberString);
                    Log.e(TAG, "------------" + dateNumber);
                    if (TextUtils.isEmpty(dateNumber)) { // 第一次使用
                        getTheNumber = 0;
                    } else {
                        String[] sCoinString = dateNumber.split("#");
                        final String date = sCoinString[0];
                        if (date.equals(time)) {
                            getTheNumber = Integer.parseInt(sCoinString[1]);
                        } else {
                            getTheNumber = 0;
                        }
                    }
                    Log.e(TAG, "----------" + getTheNumber);
                    if (getTheNumber >= 3) {
                        Toast.makeText(
                                ForgetPwdActivity.this,
                                getResources().getString(
                                        R.string.forgetpwd_confirm_newpwd_somuch),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        getCheckCode();
                    }
                }
                break;
            case R.id.bt_submit:
                newpwdString = newPwd.getText().toString();
                newpwdagainString = newPwd_agagin.getText().toString();
                if ("".equals(phoneNum.getText().toString())) {
                    //手机号码不能为空
//                    Toast.makeText(
//                            ForgetPwdActivity.this,
//                            getResources().getString(
//                                    R.string.phone_number_not_empty),
//                            Toast.LENGTH_SHORT).show();

                    exitPopWindow(getResources().getString(R.string.phone_number_not_empty_fgt), 1);


                } else if (!phoneNum.getText().toString()
                        .matches("^(1(3|5|7|8)[0-9])\\d{8}$")
                        && !phoneNum.getText().toString().matches("^(0033)\\d{9}$")) {
                    //手机号码格式不对
//                    Toast.makeText(
//                            ForgetPwdActivity.this,
//                            getResources().getString(
//                                    R.string.phone_number_format_wrong),
//                            Toast.LENGTH_SHORT).show();
                    exitPopWindow(getResources().getString(R.string.phone_number_not_empty_fgt), 1);
                } else if ("".equals(yzNum.getText().toString())) {
                    //验证码不能为空
//                    Toast.makeText(
//                            ForgetPwdActivity.this,
//                            getResources().getString(
//                                    R.string.enter_verification_code),
//                            Toast.LENGTH_SHORT).show();

                    exitPopWindow(getResources().getString(R.string.enter_verification_code), 1);


                } else if (!result.equals(yzNum.getText().toString())) {
                    //验证码不匹配
                    bt_yz.setClickable(true);
                    phoneNum.setFocusableInTouchMode(true);
//                    Toast.makeText(
//                            ForgetPwdActivity.this,
//                            getResources().getString(
//                                    R.string.verification_code_not_match),
//                            Toast.LENGTH_SHORT).show();

                    exitPopWindow(getResources().getString(R.string.verification_code_not_match), 1);

                } else if (newpwdString.equals("")) {
                    //新密码不能为空


                    //Toast.makeText(ForgetPwdActivity.this, getResources().getString(R.string.sports_toast_please_inputnewpwd_fgt), Toast.LENGTH_SHORT).show();

                    exitPopWindow(getResources().getString(R.string.sports_toast_please_inputnewpwd_fgt), 1);
                } else if (newpwdString.length() < 6) {
                    //新密码位数不能小于6位
                    //Toast.makeText(ForgetPwdActivity.this, getResources().getString(R.string.sports_toast_pwd_length), Toast.LENGTH_SHORT).show();
                    exitPopWindow(getResources().getString(R.string.sports_toast_please_inputnewpwd_fgt), 1);
                } else if (newpwdagainString.equals("") || newpwdString.length() < 6) {
                    //Toast.makeText(ForgetPwdActivity.this, getResources().getString(R.string.sports_toast_please_confirmpwd), Toast.LENGTH_SHORT).show();
                    exitPopWindow(getResources().getString(R.string.sports_toast_please_inputnewpwd_fgt), 1);

                } else if (!newpwdString.equals(newpwdagainString)) {
                    //Toast.makeText(ForgetPwdActivity.this, getResources().getString(R.string.sports_toast_not_fit_fgt), Toast.LENGTH_SHORT).show();
                    exitPopWindow(getResources().getString(R.string.sports_toast_not_fit_fgt), 1);
                } else {
                    ChangePwd();
                }
                break;
            default:
                break;
        }
    }

    //	private boolean isTruePhoneNum(String num) {
//		return num.matches("^(1(3|5|7|8)[0-9])\\d{8}$");
//	}
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case GETCODE_FAIL_NONET:
                    if (codeDialog != null) {
                        codeDialog.dismiss();
                    }
                    if (pwdDialog != null) {
                        pwdDialog.dismiss();
                    }
                    Toast.makeText(ForgetPwdActivity.this,
                            getResources().getString(R.string.error_cannot_access_net),
                            Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        }

    };

    private void getCheckCode() {
        if (codeDialog != null) {
            Log.e(TAG, "---------" + codeDialog);
            codeDialog.show();
        }
        if (Tools.isNetworkConnected(ForgetPwdActivity.this)) {
            new AsyncTask<Void, Void, ApiBack>() {
                @Override
                protected ApiBack doInBackground(Void... params) {
                    Log.e(TAG, "doInBackground");
                    ApiBack back = null;
                    try {
                        back = ApiJsonParser.sendPhoneMessage(
                                phonenumberString, "forget");
                    } catch (ApiNetException e) {
                        e.printStackTrace();
                        Message.obtain(handler, GETCODE_FAIL_NONET).sendToTarget();
                    }
                    return back;
                }

                @Override
                protected void onPostExecute(ApiBack back) {
                    if (codeDialog != null) {
                        codeDialog.dismiss();
                    }
                    if (back == null) {
                        Toast.makeText(
                                ForgetPwdActivity.this,
                                getResources().getString(
                                        R.string.code_send_failed),
                                Toast.LENGTH_SHORT).show();

                    } else {
                        result = back.getMsg();
                        // flag=0发送失败 flag=-1,发送失败
                        if (back.getFlag() == -1) {
//                            Toast.makeText(ForgetPwdActivity.this, result,
//                                    Toast.LENGTH_SHORT).show();

                            exitPopWindow("该手机号未注册，请正确填写", 1);


                        } else if (back.getFlag() == 0) {
                            SportsForgetPwdUtil.saveToatlNumberToFile(time, getTheNumber + 1, phonenumberString);
                            timeCount.start();
                            Toast.makeText(
                                    ForgetPwdActivity.this,
                                    getResources().getString(
                                            R.string.code_send_successed),
                                    Toast.LENGTH_SHORT).show();
                            bt_yz.setClickable(false);
                            phoneNum.setFocusableInTouchMode(false);

                        }
                    }
                    getTheNumber = 0;
                }
            }.execute();
        } else {
            if (codeDialog != null) {
                codeDialog.dismiss();
            }
            Toast.makeText(ForgetPwdActivity.this,
                    getResources().getString(R.string.error_cannot_access_net),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void ChangePwd() {
        if (pwdDialog != null) {
            Log.e(TAG, "---------" + pwdDialog);
            pwdDialog.show();
        }
        if (Tools.isNetworkConnected(ForgetPwdActivity.this)) {
            new AsyncTask<Void, Void, ApiBack>() {
                @Override
                protected ApiBack doInBackground(Void... params) {
                    Log.e(TAG, "doInBackground");
                    ApiBack back = null;
                    try {
                        back = ApiJsonParser.updatePwNew(yzNum.getText().toString(),
                                phoneNum.getText().toString(), newPwd.getText().toString());
                    } catch (ApiNetException e) {
                        e.printStackTrace();
                        Message.obtain(handler, GETCODE_FAIL_NONET).sendToTarget();
                    }
                    return back;
                }

                @Override
                protected void onPostExecute(ApiBack back) {
                    if (pwdDialog != null) {
                        pwdDialog.dismiss();
                    }
                    if (back == null) {
                        Toast.makeText(
                                ForgetPwdActivity.this,
                                getResources().getString(
                                        R.string.sports_server_error),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e(TAG, back.getFlag() + "");
                        Log.e(TAG, back.getMsg());
                        if (back.getFlag() == -1) {
                            Toast.makeText(ForgetPwdActivity.this,
                                    back.getMsg(), Toast.LENGTH_SHORT)
                                    .show();
                        } else if (back.getFlag() == 0) {
                            Toast.makeText(ForgetPwdActivity.this,
                                    back.getMsg(), Toast.LENGTH_SHORT)
                                    .show();
                            finish();
                        }
                    }
                }
            }.execute();
        } else {
            if (pwdDialog != null) {
                pwdDialog.dismiss();
            }
            Toast.makeText(
                    ForgetPwdActivity.this,
                    getResources().getString(
                            R.string.error_cannot_access_net),
                    Toast.LENGTH_SHORT).show();
        }


    }

    // 退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK: {
                // startActivity(new
                // Intent(ForgetPwdActivity.this,LoginActivity.class));
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /* 定义一个倒计时的内部类 */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            fSportsApp.setForgetPwdTime(0);
            bt_yz.setText(getResources().getString(R.string.sports_get_yzs));
            bt_yz.setFocusable(true);
            bt_yz.setClickable(true);
            //login_button.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            fSportsApp.setForgetPwdTime((int) (millisUntilFinished / 1000));
            bt_yz.setText( millisUntilFinished / 1000 + "s  后重新获取");
            bt_yz.setFocusable(false);
            //login_button.setClickable(false);
        }
    }

    private void initCodeDialog() {
        codeDialog = new Dialog(ForgetPwdActivity.this,
                R.style.sports_dialog);
        LayoutInflater mInflater = getLayoutInflater();
        View v1 = mInflater.inflate(R.layout.sports_progressdialog, null);
        TextView message = (TextView) v1.findViewById(R.id.message);
        message.setText(R.string.forgetpwd_checknumber_get);
        v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
        codeDialog.setContentView(v1);
        codeDialog.setCancelable(true);
        codeDialog.setCanceledOnTouchOutside(false);
    }

    private void initPwdDialog() {
        pwdDialog = new Dialog(ForgetPwdActivity.this,
                R.style.sports_dialog);
        LayoutInflater mInflater = getLayoutInflater();
        View v1 = mInflater.inflate(R.layout.sports_progressdialog, null);
        TextView message = (TextView) v1.findViewById(R.id.message);
        message.setText(R.string.forgetpwd_changepwd_wait);
        v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
        pwdDialog.setContentView(v1);
        pwdDialog.setCancelable(true);
        pwdDialog.setCanceledOnTouchOutside(false);
    }

    /**
     * @param
     * @method 固定字体大小方法
     * @author suhu
     * @time 2016/10/11 13:23
     */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }


    private void exitPopWindow(String message, final int position) {
        WindowManager.LayoutParams lp = this.getWindow()
                .getAttributes();
        lp.alpha = 0.3f;
        this.getWindow().setAttributes(lp);

        if (myWindow != null && myWindow.isShowing())
            return;
        LayoutInflater inflater = LayoutInflater.from(this);
        myView = (LinearLayout) inflater.inflate(R.layout.sports_dialog5, null);
        ((TextView) myView.findViewById(R.id.message)).setText(message);

        myView.findViewById(R.id.bt_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (position == 1) {
                    if (myWindow != null) {
                        myWindow.dismiss();
                    }
                    myView.setVisibility(View.GONE);
                    mPopMenuBack.setVisibility(View.GONE);
                } else {
                    Intent intent_outof_time = new Intent(ForgetPwdActivity.this, LoginActivity.class);
                    intent_outof_time.putExtra("outTime", 1);
                    startActivity(intent_outof_time);
                    finish();
                }
            }
        });
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        myWindow = new PopupWindow(myView, width - SportsApp.dip2px(70),
                RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        ColorDrawable cd = new ColorDrawable(0x000000);
        myWindow.setBackgroundDrawable(cd);
        myWindow.setTouchable(true);
        myWindow.setOutsideTouchable(true);
        myWindow.setBackgroundDrawable(new BitmapDrawable());
        myWindow.update();
        myWindow.showAtLocation(mainLL, Gravity.CENTER, 0, 0);
        myWindow.setOnDismissListener(this);
        mPopMenuBack.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDismiss() {
        // TODO Auto-generated method stub
        mPopMenuBack.setVisibility(View.GONE);
        WindowManager.LayoutParams lp = this.getWindow()
                .getAttributes();
        lp.alpha = 1f;
        this.getWindow().setAttributes(lp);
    }
}
