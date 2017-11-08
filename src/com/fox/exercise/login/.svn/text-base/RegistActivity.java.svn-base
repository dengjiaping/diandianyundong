package com.fox.exercise.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fox.exercise.R;
import com.fox.exercise.SmartBarUtils;
import com.fox.exercise.SportsExceptionHandler;
import com.fox.exercise.SportsUtilities;
import com.fox.exercise.api.ApiBack;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.api.entity.UserDetail;
import com.fox.exercise.http.FunctionStatic;
import com.fox.exercise.util.RoundedImage;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import cn.ingenic.indroidsync.SportsApp;

public class RegistActivity extends Activity implements View.OnClickListener,PopupWindow.OnDismissListener {
    private static final String TAG = "RegistActivity";
    private static final String FACE_PATH = SportsUtilities.DOWNLOAD_SAVE_PATH
            + "faceImage.jpg";
    // Environment.getExternalStorageDirectory().toString()+"/zuimei/.download/faceImage.jpg";
    private EditText mail;
    private EditText nickname;
    private EditText password,password_sure;
    private RoundedImage faceImage;
    // private Button bt_yz;
//	private EditText edityz;
    private String[] items;
    private static final String IMAGE_FILE_NAME = "faceImage.jpg";
    /* REQUEST_CODE */
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESULT_REQUEST_CODE = 2;
    private ProgressDialog registProgressDialog = null;
    private Toast toastnet = null;
    //	private Dialog alertDialog;
//	private String isim = null;
//	private String result = "";
    // 如果已经设置头像的话，此值为1，反之为0
    private int faceCount = 0;
    String SENT_SMS_ACTION = "SENT_SMS_ACTION";
    String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";

    //	private String user_name = null;
    private Dialog loginPregressDialog = null;
    //	private String qqzonenickName;
    private static SportsApp mSportsApp;

    private RadioButton mMaleButton = null;
    private SportsExceptionHandler mExceptionHandler = null;
    private ImageView isshow_password,isshow_password_sure;
    private boolean passwordisshow = true;
    private boolean passwordisshow2 = true;
    private TextView rigist_agreement;

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
        setContentView(R.layout.sports_regist);
        initView();
        setViewStatus();
        if (findMethod) {
            getActionBar().setDisplayShowHomeEnabled(false);
            getActionBar().setDisplayShowTitleEnabled(false);
            SmartBarUtils.setActionModeHeaderHidden(getActionBar(), true);
            SmartBarUtils.setActionBarViewCollapsable(getActionBar(), true);
        }
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
        MobclickAgent.onPageStart("RegistActivity");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPageEnd("RegistActivity");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        mExceptionHandler = null;
    }

    public void initView() {
        // TODO Auto-generated method stub
        mSportsApp = (SportsApp) getApplication();
        mail = (EditText) findViewById(R.id.ed_mail);
        nickname = (EditText) findViewById(R.id.ed_nickname);
        nickname.addTextChangedListener(mTextWatcher);
        password = (EditText) findViewById(R.id.ed_password);
        password_sure = (EditText) findViewById(R.id.ed_password_sure);
        password.addTextChangedListener(new PassWord(password));
        faceImage = (RoundedImage) findViewById(R.id.image_headphoto2);
        faceImage.setTag(R.drawable.sports_user_edit_portrait_male);
//		edityz = (EditText) findViewById(R.id.ed_yz);
        mMaleButton = (RadioButton) findViewById(R.id.male_radio);
        isshow_password = (ImageView) findViewById(R.id.isshow_password);
        isshow_password.setOnClickListener(this);
        isshow_password_sure = (ImageView) findViewById(R.id.isshow_password_sure);
        isshow_password_sure.setOnClickListener(this);

        mainLL = (RelativeLayout) findViewById(R.id.rl);
        mPopMenuBack = (RelativeLayout) findViewById(R.id.set_menu_background);
    }

    public void setViewStatus() {
        // TODO Auto-generated method stub
        findViewById(R.id.bt_back).setOnClickListener(this);
        findViewById(R.id.tx_shezhiheadphoto).setOnClickListener(this);
        findViewById(R.id.image_headphoto2).setOnClickListener(this);
        findViewById(R.id.bt_ok).setOnClickListener(this);
        findViewById(R.id.bt_xinlang).setOnClickListener(this);
        findViewById(R.id.bt_QQ).setOnClickListener(this);
        findViewById(R.id.bt_txweibo).setOnClickListener(this);
        findViewById(R.id.gologin_regist).setOnClickListener(this);
        // 注册协议
        rigist_agreement = (TextView) findViewById(R.id.rigist_agreement);
        // 文本内容
        SpannableString ss = new SpannableString(
                "点击“完成”即表示你同意《云狐运动APP服务协议》");
        // 设置9-11的字符为网络链接，点击时打开页面
        ss.setSpan(
                new URLSpan("http://kupao.mobifox.cn/Beauty/xieyi/zhuce.htm"),
                12, 25, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        rigist_agreement.setText(ss);
        rigist_agreement.setMovementMethod(LinkMovementMethod.getInstance());

        items = new String[]{
                getResources().getString(R.string.sports_camera),
                getResources().getString(R.string.sports_fromphotos),
                getResources().getString(R.string.sports_cancel)};
        mMaleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (mMaleButton.isChecked()) {
                    if (faceCount == 0) {
                        faceImage
                                .setImageResource(R.drawable.sports_user_edit_portrait_male);
                        faceImage
                                .setTag(R.drawable.sports_user_edit_portrait_male);
                    }
                    mMaleButton.setChecked(true);
                } else {
                    if (faceCount == 0) {
                        faceImage
                                .setImageResource(R.drawable.sports_user_edit_portrait);
                        faceImage.setTag(R.drawable.sports_user_edit_portrait);
                    }
                    mMaleButton.setChecked(false);
                }
            }
        });
        mMaleButton.setChecked(true);
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
            String str = stringFilter2(editable.toString());
            if (!editable.equals(str)) {
                nickname.setText(str);
                // 设置新的光标所在位置 www.2cto.com
                nickname.setSelection(str.length());
                Toast.makeText(
                        RegistActivity.this,
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
                        RegistActivity.this,
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

    public static String stringFilter2(String str)
            throws PatternSyntaxException {
        // 只允许字母和数字
        String regEx = "[\\s*]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    class PassWord implements TextWatcher {

        // 监听改变的文本框
        private EditText editText;

        /**
         * 构造函数
         */
        public PassWord(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void onTextChanged(CharSequence ss, int start, int before,
                                  int count) {
            String editable = editText.getText().toString();
            String str = stringFilter(editable.toString());
            if (!editable.equals(str)) {
                editText.setText(str);
                // 设置新的光标所在位置 www.2cto.com
                editText.setSelection(str.length());
                Toast.makeText(RegistActivity.this,
                        getResources().getString(R.string.words_no_access),
                        Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

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
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.bt_back:
                // startActivity(new
                // Intent(RegistActivity.this,LoginActivity.class));
                finish();
                break;
            case R.id.gologin_regist:
                finish();
                break;
            case R.id.image_headphoto2:
            case R.id.tx_shezhiheadphoto:
                Log.e(TAG, "shezhitouxiang");
                showDialog();
                break;
            case R.id.bt_ok:
                int length = 0;
                if (nickname.getText().toString().trim() != null
                        && !"".equals(nickname.getText().toString().trim())) {
                    length = nickname.getText().toString().trim().length();
                }
                // try {
                // length = nickname.getText().toString().getBytes("gbk").length;
                // } catch (UnsupportedEncodingException e1) {
                // // TODO Auto-generated catch block
                // e1.printStackTrace();
                // }
                if ("".equals(mail.getText().toString().trim()) || mail.getText().toString().trim() == null) {
//                    Toast.makeText(
//                            RegistActivity.this,
//                            getResources().getString(
//                                    R.string.phone_number_not_empty),
//                            Toast.LENGTH_SHORT).show();
                    exitPopWindow(getResources().getString(R.string.phone_number_not_empty),1);
                } else if (!mail.getText().toString().trim()
                        .matches("^(1(3|5|7|8)[0-9])\\d{8}$")
                        && !mail.getText().toString().matches("^(0033)\\d{9}$")) {
//                    Toast.makeText(
//                            RegistActivity.this,
//                            getResources().getString(
//                                    R.string.phone_number_format_wrong),
//                            Toast.LENGTH_SHORT).show();
                    exitPopWindow(getResources().getString(R.string.phone_number_format_wrong),1);
                }/*
             * else if("".equals(edityz.getText().toString())){
			 * Toast.makeText(RegistActivity.this, "请输入验证码!",
			 * Toast.LENGTH_SHORT).show(); }else if
			 * (!result.equals(edityz.getText().toString())) {
			 * bt_yz.setClickable(true); mail.setFocusableInTouchMode(true);
			 * Toast.makeText(RegistActivity.this, "验证码不匹配!",
			 * Toast.LENGTH_SHORT).show(); }
			 */ else if (nickname.getText().toString().trim().equals("")
                        || nickname.getText().toString().trim() == null) {
//                    Toast.makeText(
//                            RegistActivity.this,
//                            getResources().getString(
//                                    R.string.sports_toast_input_nickname),
//                            Toast.LENGTH_SHORT).show();
                    exitPopWindow(getResources().getString(R.string.sports_toast_input_nickname),1);
                } else if (length != 0 && length > 15) {
//                    Toast.makeText(
//                            RegistActivity.this,
//                            getResources().getString(
//                                    R.string.sports_nickname_toolong),
//                            Toast.LENGTH_SHORT).show();
                    exitPopWindow(getResources().getString(R.string.sports_nickname_toolong),1);
                } else if (nickname.getText().toString().trim().length() > 15) {
//                    Toast.makeText(
//                            RegistActivity.this,
//                            getResources().getString(
//                                    R.string.sports_nickname_toolong),
//                            Toast.LENGTH_SHORT).show();
                    exitPopWindow(getResources().getString(R.string.sports_nickname_toolong),1);
                } else if (nickname.getText().toString().trim().startsWith("txwb_")
                        || nickname.getText().toString().trim().startsWith("xlwb_")) {
//                    Toast.makeText(
//                            RegistActivity.this,
//                            getResources().getString(
//                                    R.string.sports_toast_illegal_character),
//                            Toast.LENGTH_SHORT).show();
                    exitPopWindow(getResources().getString(R.string.sports_toast_illegal_character),1);
                } else if (password.getText().toString().trim().equals("")) {
//                    Toast.makeText(
//                            RegistActivity.this,
//                            getResources().getString(
//                                    R.string.sports_toast_input_pwd),
//                            Toast.LENGTH_SHORT).show();
                    exitPopWindow(getResources().getString(R.string.sports_toast_input_pwd),1);
                } else if (password.getText().toString().trim().length() < 6) {
//                    Toast.makeText(
//                            RegistActivity.this,
//                            getResources().getString(
//                                    R.string.sports_toast_pwd_length),
//                            Toast.LENGTH_SHORT).show();
                    exitPopWindow(getResources().getString(R.string.sports_toast_pwd_length),1);
                } else if (!password.getText().toString().equals(password_sure.getText().toString())) {
//                    Toast.makeText(
//                            RegistActivity.this,
//                            getResources().getString(
//                                    R.string.sports_toast_not_fit),
//                            Toast.LENGTH_SHORT).show();
                    exitPopWindow(getResources().getString(R.string.sports_toast_not_fit),1);
                } else if (nickname.getText().toString().contains("_")) {
//                    Toast.makeText(
//                            RegistActivity.this,
//                            getResources().getString(
//                                    R.string.sports_toast_illegal_character2),
//                            Toast.LENGTH_SHORT).show();
                    exitPopWindow(getResources().getString(R.string.sports_toast_illegal_character2),1);
                } else {
                    loginPregressDialog = new Dialog(RegistActivity.this,
                            R.style.sports_dialog);
                    LayoutInflater mInflater = getLayoutInflater();
                    View v1 = mInflater.inflate(R.layout.sports_progressdialog,
                            null);
                    TextView message = (TextView) v1.findViewById(R.id.message);
                    message.setText(R.string.sports_registing);
                    v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
                    loginPregressDialog.setContentView(v1);
                    loginPregressDialog.setCanceledOnTouchOutside(false);
                    loginPregressDialog.setCancelable(true);
                    loginPregressDialog.show();
                    if (mSportsApp.isOpenNetwork()) {
                        new AsyncTask<Void, Void, ApiBack>() {
                            @Override
                            protected ApiBack doInBackground(Void... params) {
                                ApiBack back = null;
                                try {
                                    if ((faceImage.getTag()
                                            .equals(R.drawable.sports_user_edit_portrait))
                                            || faceImage
                                            .getTag()
                                            .equals(R.drawable.sports_user_edit_portrait_male)) {
                                        back = ApiJsonParser.registerPhone(nickname
                                                .getText().toString(), password
                                                .getText().toString(), mail
                                                .getText().toString(), mMaleButton
                                                .isChecked() ? "man" : "woman", 1);
                                    } else {
                                        back = ApiJsonParser.registerPhone(nickname
                                                        .getText().toString(), password
                                                        .getText().toString(), mail
                                                        .getText().toString(), FACE_PATH,
                                                mMaleButton.isChecked() ? "man"
                                                        : "woman", 1);
                                    }
                                } catch (ApiNetException e) {
                                    e.printStackTrace();
                                }
                                return back;
                            }

                            @Override
                            protected void onPostExecute(ApiBack message) {
                                if (message == null) {
                                    Toast.makeText(
                                            RegistActivity.this,
                                            getResources().getString(
                                                    R.string.sports_get_list_failed2),
                                            Toast.LENGTH_LONG).show();
//                                    exitPopWindow(getResources().getString(R.string.sports_get_list_failed2),1);
                                    if (loginPregressDialog != null)
                                        loginPregressDialog.dismiss();
                                    // bt_yz.setClickable(true);
                                    mail.setFocusableInTouchMode(true);
                                    return;
                                } else if (message.getMsg() == null) {
                                    Toast.makeText(
                                            RegistActivity.this,
                                            getResources()
                                                    .getString(
                                                            R.string.sports_get_list_failed2),
                                            Toast.LENGTH_LONG).show();
//                                    exitPopWindow(getResources().getString(R.string.sports_get_list_failed2),1);
                                    if (loginPregressDialog != null)
                                        loginPregressDialog.dismiss();
                                    // bt_yz.setClickable(true);
                                    mail.setFocusableInTouchMode(true);
                                    return;
                                }else if(message.getFlag() == -100){
                                    Log.e(TAG, "WIFI未验证");
                                    Toast.makeText(RegistActivity.this,
                                            getResources().getString(R.string.sports_get_list_failed2),
                                            Toast.LENGTH_LONG).show();
                                } else if (message.getFlag() != 0) {
                                    if (-11 == message.getFlag()) {
                                        Log.e("develop_debug", "RegistActivity.java 476");
                                        Toast.makeText(RegistActivity.this,
                                                       getResources().getString(R.string.login_fail_device_disable),
                                                       Toast.LENGTH_LONG).show();
                                    } else if (message.getFlag() == -56){
                                        Log.e(TAG, "请求服务器超时");
                                        Toast.makeText(RegistActivity.this,R.string.socket_outof_time,Toast.LENGTH_SHORT).show();
                                    }  else {
//                                        Toast.makeText(RegistActivity.this,
//                                                message.getMsg(), Toast.LENGTH_LONG)
//                                                .show();
                                        exitPopWindow(message.getMsg(),1);
                                    }
                                    if (loginPregressDialog != null)
                                        loginPregressDialog.dismiss();
                                    mail.setFocusableInTouchMode(true);
                                    return;
                                } else {
                                    Log.e(TAG,
                                            "message.isFlag():" + message.getFlag());
                                    Log.e(TAG, message.getMsg());

                                    Intent intent = new Intent();
                                    intent.setAction(SportAction.LOGIN_ACTION);
                                    intent.putExtra(SportAction.SEESION_ID_KEY,
                                            mSportsApp.getSessionId());
                                    intent.putExtra(SportAction.IS_ADMIN_KEY,
                                            SportsApp.mIsAdmin);
                                    sendBroadcast(intent);

                                    if (message.getFlag() == 0) {
                                        Toast.makeText(
                                                RegistActivity.this,
                                                getResources()
                                                        .getString(
                                                                R.string.sports_toast_regrist_succes),
                                                Toast.LENGTH_SHORT).show();
                                        new AsyncTask<Integer, Integer, Boolean>() {

                                            @Override
                                            protected void onPostExecute(
                                                    Boolean flag) {
                                                super.onPostExecute(flag);
                                                if (flag) {

                                                    SportsLoginUtils loginUtil = new SportsLoginUtils();
                                                    loginUtil
                                                            .saveNormalToFile(
                                                                    nickname.getText()
                                                                            .toString(),
                                                                    LoginActivity.NORMAL_PATH1,
                                                                    LoginActivity.NORMAL_PATH2);

                                                    SharedPreferences sp = getSharedPreferences(
                                                            "user_login_info",
                                                            Context.MODE_PRIVATE);
                                                    Editor ed = sp.edit();
                                                    ed.clear();
                                                    ed.putString("account",
                                                            nickname.getText()
                                                                    .toString());
                                                    ed.putString("pwd", password
                                                            .getText().toString());
                                                    // 记录普通用户身份
                                                    ed.putInt("login_way", 0);
                                                    ed.commit();
                                                    Log.d(TAG, "Uid:"
                                                            + mSportsApp
                                                            .getSportUser()
                                                            .getUid());
                                                    MobclickAgent.onEvent(
                                                            RegistActivity.this,
                                                            "regist");
                                                    SharedPreferences sps = getSharedPreferences(
                                                            "sprots_uid", 0);
                                                    Editor editor = sps.edit();
                                                    editor.putInt("sportsUid",
                                                            mSportsApp
                                                                    .getSportUser()
                                                                    .getUid());
                                                    editor.commit();
                                                    Log.e(TAG,
                                                            "session:"
                                                                    + mSportsApp
                                                                    .getSessionId());
                                                    LoginActivity.mIsRegister = true;
                                                    Intent intents = new Intent(
                                                            RegistActivity.this,
                                                            UserEditActivity.class);
                                                    startActivity(intents);
                                                    mSportsApp.getLoginActivity()
                                                            .finish();
                                                    finish();
                                                }
                                            }

                                            @Override
                                            protected Boolean doInBackground(
                                                    Integer... arg0) {
                                                boolean flag = trytoLogin(nickname
                                                                .getText().toString(),
                                                        password.getText()
                                                                .toString(),
                                                        RegistActivity.this);
                                                if (flag) {
                                                    checkFirstLogin();
                                                    UserDetail detail = null;
                                                    while (detail == null) {
                                                        try {
                                                            if (!"".equals(mSportsApp
                                                                    .getSessionId())
                                                                    && mSportsApp
                                                                    .getSessionId() != null) {
                                                                detail = ApiJsonParser
                                                                        .refreshRank(mSportsApp
                                                                                .getSessionId());
                                                            }
                                                        } catch (ApiNetException e) {
                                                            e.printStackTrace();
                                                            SportsApp.eMsg = Message
                                                                    .obtain(mExceptionHandler,
                                                                            SportsExceptionHandler.NET_ERROR);
                                                            SportsApp.eMsg
                                                                    .sendToTarget();
                                                        } catch (ApiSessionOutException e) {
                                                            e.printStackTrace();
                                                            SportsApp.eMsg = Message
                                                                    .obtain(mExceptionHandler,
                                                                            SportsExceptionHandler.SESSION_OUT);
                                                            SportsApp.eMsg
                                                                    .sendToTarget();
                                                            startActivity(new Intent(
                                                                    RegistActivity.this,
                                                                    LoginActivity.class));
                                                        }
                                                    }
                                                    mSportsApp.setSportUser(detail);
                                                }
                                                return flag;
                                            }
                                        }.execute();

                                    } else {
                                        if (message.getMsg().equals("nameexist")
                                                || message.getMsg().equals(
                                                "centernameexist")) {
                                            Toast.makeText(
                                                    RegistActivity.this,
                                                    getResources()
                                                            .getString(
                                                                    R.string.sports_toast_nameexist),
                                                    Toast.LENGTH_SHORT).show();
                                        } else if (message.getMsg().equals(
                                                "emailexist")) {
                                            Toast.makeText(
                                                    RegistActivity.this,
                                                    getResources()
                                                            .getString(
                                                                    R.string.sports_toast_emailexist),
                                                    Toast.LENGTH_SHORT).show();
                                        } else if (message.getMsg().equals(
                                                "success")) {
                                            Toast.makeText(
                                                    RegistActivity.this,
                                                    getResources()
                                                            .getString(
                                                                    R.string.sports_toast_regrist_succes),
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                        }.execute();
                    } else {
                        if (registProgressDialog != null)
                            registProgressDialog.dismiss();
                        if (loginPregressDialog != null)
                            loginPregressDialog.dismiss();
                        if (toastnet != null) {
                            Log.v(TAG, "cancel");
                            toastnet.cancel();
                        } else {
                            Log.v(TAG, "creat");
                            toastnet = Toast.makeText(
                                    RegistActivity.this,
                                    getResources().getString(
                                            R.string.error_cannot_access_net),
                                    Toast.LENGTH_SHORT);
                        }
                        toastnet.show();
                    }
                }

                break;
            // case R.id.bt_xinlang:
            // Log.e(TAG, "xianglang");
            // if (Tools.isNetworkConnected(RegistActivity.this)) {
            // gotoXinlangWeibo();
            // } else {
            // Toast.makeText(RegistActivity.this,
            // getResources().getString(R.string.error_cannot_access_net),
            // Toast.LENGTH_SHORT).show();
            // }
            // break;
            // case R.id.bt_txweibo:
            // Log.e(TAG, "txweibo");
            // if (Tools.isNetworkConnected(RegistActivity.this)) {
            // gotoTenxunWeibo();
            // } else {
            // Toast.makeText(RegistActivity.this,
            // getResources().getString(R.string.error_cannot_access_net),
            // Toast.LENGTH_SHORT).show();
            // }
            // break;
        /*
         * case R.id.bt_yz: TelephonyManager manager =
		 * (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE); int
		 * absent = manager.getSimState(); // if (1 == absent){
		 * //Toast.makeText(RegistActivity.this, "请确认sim卡是否插入或者sim卡暂时不可用！",
		 * Toast.LENGTH_SHORT).show(); // }else{ if
		 * ("".equals(mail.getText().toString())) {
		 * Toast.makeText(RegistActivity.this, "手机号码不能为空!",
		 * Toast.LENGTH_SHORT).show(); } else if
		 * (!mail.getText().toString().matches("^(1(3|5|8)[0-9])\\d{8}$")) {
		 * Toast.makeText(RegistActivity.this, "手机号码格式不对!",
		 * Toast.LENGTH_SHORT).show(); } else {
		 * showDialogs(RegistActivity.this); } //} break;
		 */
            case R.id.isshow_password:
                if (passwordisshow) {
                    passwordisshow = false;
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isshow_password.setBackgroundResource(R.drawable.according_psw);
                    password_sure.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isshow_password_sure.setBackgroundResource(R.drawable.according_psw);
                }else{
                    passwordisshow = true;
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isshow_password.setBackgroundResource(R.drawable.hidden_psw);
                    password_sure.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isshow_password_sure.setBackgroundResource(R.drawable.hidden_psw);
                }
                break;
            case R.id.isshow_password_sure:
//                if (passwordisshow2) {
//                    passwordisshow2 = false;
//                    password_sure.setInputType(0x81);
//                    isshow_password_sure.setBackgroundResource(R.drawable.showpassword);
//                }else{
//                    passwordisshow2 = true;
//                    password_sure.setInputType(0x90);
//                    isshow_password_sure.setBackgroundResource(R.drawable.hidepassword);
//                }
                break;
            default:
                break;
        }
    }

	/*
     * private void showDialogs(Context con) { // alertDialog = new Dialog(con,
	 * R.style.sports_dialog); // LayoutInflater mInflater =
	 * getLayoutInflater(); // View v =
	 * mInflater.inflate(R.layout.sports_dialog, null); //
	 * v.findViewById(R.id.bt_ok).setOnClickListener(new OnClickListener() { //
	 *
	 * @Override // public void onClick(View arg0) {
	 * Toast.makeText(RegistActivity.this, "验证码获取中！",
	 * Toast.LENGTH_SHORT).show(); isim = mail.getText().toString(); //Intent
	 * sentIntent = new Intent(SENT_SMS_ACTION); //PendingIntent sentPI =
	 * PendingIntent.getBroadcast(RegistActivity.this, 0, sentIntent, 0);
	 * //Intent deliverIntent = new Intent(DELIVERED_SMS_ACTION);
	 * //PendingIntent deliverPI =
	 * PendingIntent.getBroadcast(RegistActivity.this, 0, deliverIntent, 0); if
	 * (!"".equals(isim) && isim.matches("^(1(3|5|8)[0-9])\\d{8}$")) { new
	 * AsyncTask<Void, Void, ApiBack>(){
	 *
	 * @Override protected ApiBack doInBackground(Void... params) { Log.e(TAG,
	 * "doInBackground"); ApiBack back = null; try { back =
	 * ApiJsonParser.sendPhoneMessage(isim,"regist"); } catch (ApiNetException
	 * e) { e.printStackTrace(); } return back; }
	 *
	 * @Override protected void onPostExecute(ApiBack back) { if (back == null)
	 * { Toast.makeText(RegistActivity.this, "验证码发送失败！", Toast.LENGTH_SHORT)
	 * .show(); } else { Log.e(TAG, back.getFlag() + ""); Log.e(TAG,
	 * back.getMsg()); result=back.getMsg(); //flag=0发送失败 flag=-1,发送失败 if
	 * (back.getFlag() == -1) { Toast.makeText(RegistActivity.this, result,
	 * Toast.LENGTH_SHORT).show(); }else if (back.getFlag() == 0) {
	 * Toast.makeText(RegistActivity.this, "验证码已经通过短信发送给您,请注意查收.",
	 * Toast.LENGTH_SHORT).show(); bt_yz.setClickable(false);
	 * mail.setFocusableInTouchMode(false);
	 *
	 * } } } }.execute();
	 *
	 *
	 *
	 * SmsManager smsManager = SmsManager.getDefault(); List<String>
	 * divideContents = smsManager.divideMessage("您的验证码是：" + result); for
	 * (String msg : divideContents) { smsManager.sendTextMessage(isim, null,
	 * msg, sentPI, deliverPI); }
	 *
	 *
	 *
	 * // alertDialog.dismiss(); } // } // }); //
	 * v.findViewById(R.id.bt_cancel).setOnClickListener(new OnClickListener() {
	 * // // @Override // public void onClick(View v) { //
	 * alertDialog.dismiss(); // } // }); // TextView message = (TextView)
	 * v.findViewById(R.id.message); // message.setText("发送验证码需要花费您一条短信费!"); //
	 * v.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8)); //
	 * alertDialog.setCancelable(true); // alertDialog.setContentView(v); //
	 * alertDialog.show(); }
	 */

    /**
     * 显示选择对话框
     */
    private void showDialog() {

        new AlertDialog.Builder(this)
                .setTitle(
                        getResources().getString(R.string.sports_set_faceimage))
                .setItems(items, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:

                                Intent intentFromCapture = new Intent(
                                        MediaStore.ACTION_IMAGE_CAPTURE);
                                // 判断存储卡是否可以用，可用进行存储
                                if (Tools.hasSdcard()) {

                                    intentFromCapture.putExtra(
                                            MediaStore.EXTRA_OUTPUT,
                                            Uri.fromFile(new File(Environment
                                                    .getExternalStorageDirectory(),
                                                    IMAGE_FILE_NAME)));
                                }

                                startActivityForResult(intentFromCapture,
                                        CAMERA_REQUEST_CODE);
                                break;
                            case 1:
                            /*
                             * Intent intentFromGallery = new Intent();
							 * intentFromGallery.setType("image/*"); // 设置文件类型
							 * intentFromGallery
							 * .setAction(Intent.ACTION_GET_CONTENT);
							 */
                                Intent intentFromGallery = new Intent(
                                        Intent.ACTION_PICK, null);
                                intentFromGallery
                                        .setDataAndType(
                                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                                "image/*");
                                startActivityForResult(intentFromGallery,
                                        IMAGE_REQUEST_CODE);
                                break;
                            case 2:
                                dialog.dismiss();
                                break;
                        }
                    }
                }).show();
    }

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
                                RegistActivity.this,
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
     * @param
     */
    private void getImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(photo);
            faceImage.setImageDrawable(drawable);
            faceImage.setTag(FACE_PATH);
            Tools.SaveBitmapAsFile(FACE_PATH, photo);
            faceCount = 1;

        }
    }

    public static boolean trytoLogin(String account, String pwd, Context con) {
        if (Tools.isNetworkConnected(con)) {
            if (account.equals("") || pwd.equals("")) {
                return false;
            }
            SharedPreferences sp_umeng = mSportsApp.getSharedPreferences("UmengDeviceToken", Activity.MODE_PRIVATE);
            try {
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

    // BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
    // @Override
    // public void onReceive(Context context, Intent intent) {
    // Log.e(TAG, "broadcastReceived");
    // if (intent.getAction().equals("com.weibo.techface.getTencent_verifier"))
    // {
    // weibo = WeiboContext.getInstance();
    // Bundle bundle = intent.getExtras();
    // final String veryfier = bundle.getString("veryfier");//
    // 获取从授权页面返回的veryfier
    // if (veryfier != null) {
    // new AsyncTask<Void, Void, Void>() {
    // String userInfo = null;
    //
    // @Override
    // protected Void doInBackground(Void... params) {
    // weibo.getAccessToken(weibo.getTokenKey(), weibo.getTokenSecrect(),
    // veryfier);//
    // 取得key和secret,这个key和secret非常重要，调腾讯的API全靠它了，神马新浪的，人人网的都一样的，不过还是有点区别，腾讯的OAuth认证是基于1.0的
    // userInfo = weibo.getUserInfo(weibo.getAccessTokenKey(),
    // weibo.getAccessTokenSecrect());
    // return null;
    // }
    //
    // @Override
    // protected void onPostExecute(Void void1) {
    // try {
    // JSONObject data = new JSONObject(userInfo).getJSONObject("data");
    // final String userId = data.getString("name");
    // Log.e(TAG, "userId = " + userId);
    // Log.e(TAG, "data88888 = " + data);
    // loginPregressDialog = new Dialog(RegistActivity.this,
    // R.style.sports_dialog);
    // LayoutInflater mInflater = getLayoutInflater();
    // View v1 = mInflater.inflate(R.layout.sports_progressdialog, null);
    // TextView message = (TextView) v1.findViewById(R.id.message);
    // message.setText(R.string.sports_logining);
    // v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
    // loginPregressDialog.setContentView(v1);
    // loginPregressDialog.setCancelable(true);
    // loginPregressDialog.show();
    // if (Tools.isNetworkConnected(RegistActivity.this)) {
    // new AsyncTask<Void, Void, ApiBack>() {
    // @Override
    // protected ApiBack doInBackground(Void... params) {
    // Log.e(TAG, "doInBackground");
    // ApiBack back = null;
    // try {
    // back = ApiJsonParser.combineWeibo(ApiConstant.WeiboType.TengxunWeiBo,
    // userId);
    // } catch (ApiNetException e) {
    // // TODO Auto-generated catch
    // e.printStackTrace();
    // }
    // return back;
    // }
    //
    // @Override
    // protected void onPostExecute(ApiBack message) {
    // if (message == null) {
    // loginPregressDialog.dismiss();
    // Toast.makeText(RegistActivity.this,
    // getResources().getString(R.string.sports_server_error),
    // Toast.LENGTH_SHORT).show();
    // } else {
    // loginPregressDialog.dismiss();
    // Log.e(TAG, "message.isFlag():" + message.getFlag());
    // Log.e(TAG, message.getMsg());
    // if (message.getFlag() == 0) {
    // Log.e(TAG, "loginsuccess");
    // String sessionid = message.getMsg().substring(7);
    // Log.e(TAG, "session_id" + sessionid);
    // mSportsApp.setSessionId(sessionid);
    // mSportsApp.setLogin(true);
    // SharedPreferences sp = getSharedPreferences("user_login_info",
    // Context.MODE_PRIVATE);
    // Editor ed = sp.edit();
    // ed.clear();
    // ed.putString("account", userId);
    // ed.putString("weibotype", ApiConstant.WeiboType.TengxunWeiBo);
    // ed.commit();
    // Toast.makeText(
    // RegistActivity.this,
    // getResources().getString(
    // R.string.sports_toast_login_succes),
    // Toast.LENGTH_SHORT).show();
    // setResultCode(1);
    // finish();
    // } else {
    // Log.e(TAG, "loginfailed");
    // if (message.getMsg().equals("accountOrPwdError")) {
    // Toast.makeText(
    // RegistActivity.this,
    // getResources().getString(
    // R.string.sports_toast_nameOrPwdError),
    // Toast.LENGTH_SHORT).show();
    // } else {
    // Toast.makeText(
    // RegistActivity.this,
    // getResources().getString(
    // R.string.sports_toast_login_failed),
    // Toast.LENGTH_SHORT).show();
    // }
    // }
    // }
    // }
    // }.execute();
    // } else {
    // loginPregressDialog.dismiss();
    // if (toastnet != null) {
    // Log.v(TAG, "cancel");
    // toastnet.cancel();
    // } else {
    // Log.v(TAG, "creat");
    // toastnet = Toast.makeText(RegistActivity.this,
    // getResources().getString(R.string.error_cannot_access_net),
    // Toast.LENGTH_SHORT);
    // }
    // toastnet.show();
    // }
    // } catch (JSONException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // Log.e(TAG, userInfo);
    // }
    // }.execute();
    // }
    // Log.e(TAG, veryfier);
    // }
    // }
    // };

    // private void initWeiboInfo() {
    // registerReceiver(broadcastReceiver, new
    // IntentFilter("com.weibo.techface.getTencent_verifier"));
    // // registerReceiver(broadcastReceiver, new
    // IntentFilter("com.weibo.techface.getRenren_access_token"));
    // }

    // private void gotoTenxunWeibo() {
    //
    // weibo = WeiboContext.getInstance();
    // new AsyncTask<Void, Void, Void>() {
    // @Override
    // protected Void doInBackground(Void... params) {
    // weibo.getRequestToken();
    // return null;
    // }
    //
    // @Override
    // protected void onPostExecute(Void void1) {
    // Intent intent = new Intent(RegistActivity.this,
    // TencentAuthorizeActivity.class);
    // Bundle bundle = new Bundle();
    // bundle.putString("url", weibo.getAuthorizeUrl());
    // intent.putExtras(bundle);
    // startActivity(intent);
    // }
    // }.execute();
    // }

    // private void gotoXinlangWeibo() {
    // // Oauth2.0
    // // 隐式授权认证方式
    // Weibo weibo = Weibo.getInstance();
    // weibo.setupConsumerConfig(WeiboConstParam.CONSUMER_KEY,
    // WeiboConstParam.CONSUMER_SECRET);
    //
    // // 此处回调页内容应该替换为与appkey对应的应用回调页
    // weibo.setRedirectUrl(WeiboConstParam.REDIRECT_URL);
    //
    // // 启动认证
    // weibo.authorize(RegistActivity.this, new AuthDialogListener());
    // }

    @Override
    protected void onStop() {
        super.onStop();
        if (loginPregressDialog != null)
            loginPregressDialog.dismiss();
    }

    // class AuthDialogListener implements WeiboDialogListener, RequestListener
    // {
    //
    // @Override
    // public void onComplete(Bundle values) {
    //
    // String token = values.getString("access_token");
    //
    // AuthoSharePreference.putToken(RegistActivity.this, token);
    //
    // String tokenString = AuthoSharePreference.getToken(RegistActivity.this);
    //
    // AccessToken accessToken = new AccessToken(tokenString,
    // WeiboConstParam.CONSUMER_SECRET);
    // Log.e(TAG, "accessToken:" + accessToken);
    // final Weibo weibo = Weibo.getInstance();
    // weibo.setAccessToken(accessToken);
    //
    // String expires_in = values.getString("expires_in");
    // AuthoSharePreference.putExpires(RegistActivity.this, expires_in);
    //
    // String remind_in = values.getString("remind_in");
    // AuthoSharePreference.putRemind(RegistActivity.this, remind_in);
    //
    // final String uid = values.getString("uid");
    // Log.e(TAG, uid);
    // AuthoSharePreference.putUid(RegistActivity.this, uid);
    // // update(weibo, Weibo.getAppKey(), msg, "", "");
    // // retrieveSinaUserInfo(weibo,Weibo.getAppKey(),
    // // weibo.getAccessToken(),uid);
    // new AsyncTask<Void, Void, Void>() {
    //
    // @Override
    // protected Void doInBackground(Void... params) {
    // retrieveSinaUserInfo(weibo, Weibo.getAppKey(), weibo.getAccessToken(),
    // uid);
    // return null;
    // }
    //
    // @Override
    // protected void onPostExecute(Void void1) {
    // }
    // }.execute();
    // }
    //
    // private void retrieveSinaUserInfo(Weibo weibo, String source, Token
    // token, String uid) {
    // WeiboParameters bundle = new WeiboParameters();
    // Log.d(TAG, "token.toString():" + token.getRefreshToken());
    // Log.d(TAG, "token.toString():" + token.getToken());
    // bundle.add("access_token", token.getToken());
    // bundle.add("uid", uid);
    //
    // String url = Weibo.SERVER + "users/show.json";
    // AsyncWeiboRunner weiboRunner = new AsyncWeiboRunner(weibo);
    // weiboRunner.request(getApplicationContext(), url, bundle,
    // Utility.HTTPMETHOD_GET, this);
    // }
    //
    // // @Override
    // // public void onComplete(String response) {
    // // Message msg = mHandler.obtainMessage(1, response);
    // // mHandler.sendMessage(msg);
    // // }
    //
    // @Override
    // public void onIOException(IOException e) {
    // // TODO Auto-generated method stub
    //
    // }
    //
    // @Override
    // public void onError(WeiboException e) {
    // // TODO Auto-generated method stub
    //
    // }
    //
    // @Override
    // public void onWeiboException(WeiboException e) {
    // // TODO Auto-generated method stub
    //
    // }
    //
    // @Override
    // public void onError(DialogError e) {
    // // TODO Auto-generated method stub
    //
    // }
    //
    // @Override
    // public void onCancel() {
    // // TODO Auto-generated method stub
    //
    // }
    //
    // }

    // 退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK: {
                // startActivity(new
                // Intent(RegistActivity.this,LoginActivity.class));
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    // 检查是否第一次登陆
    private void checkFirstLogin() {
        (new MarketTask()).execute();
    }

    class MarketTask extends AsyncTask<Integer, Integer, ApiBack> {
        @Override
        protected ApiBack doInBackground(Integer... arg0) {
            Log.e(TAG, "coinsGiftFromMarket");
            try {
                ApiBack back = ApiJsonParser.activities(
                        mSportsApp.getSessionId(),
                        Integer.parseInt(getResources().getString(
                                R.string.config_game_id)));
                return back;
            } catch (NotFoundException e) {
                e.printStackTrace();
            } catch (ApiNetException e) {
                SportsApp.eMsg = Message.obtain(mExceptionHandler,
                        SportsExceptionHandler.NET_ERROR);
                SportsApp.eMsg.sendToTarget();
                e.printStackTrace();
            } catch (ApiSessionOutException e) {
                e.printStackTrace();
                SportsApp.eMsg = Message.obtain(mExceptionHandler,
                        SportsExceptionHandler.SESSION_OUT);
                SportsApp.eMsg.sendToTarget();
                startActivity(new Intent(RegistActivity.this,
                        LoginActivity.class));
            }
            return null;
        }

        @Override
        protected void onPostExecute(ApiBack result) {

            // File file = new File(getConfigPath());

            if (result == null || result.getFlag() != 0) {
                // file.delete();
                return;
            }
            String format = getResources().getString(
                    R.string.sports_market_gift);
            String s = String.format(format,
                    FunctionStatic.getGameNameById(RegistActivity.this) + "渠道",
                    result.getMsg());
            Toast.makeText(SportsApp.getContext(), s, Toast.LENGTH_SHORT)
                    .show();
            // if (file.exists()) {
            // try {
            // JSONObject json = new JSONObject();
            // json.put("config_game_id",
            // getResources().getString(R.string.config_game_id));
            // json.put("coin_gift",
            // getResources().getInteger(R.integer.coin_gift));
            // json.put("has_gift", 1);
            // FileWriter writer = new FileWriter(file);
            // writer.write(json.toString());
            // writer.flush();
            // writer.close();
            // } catch (IOException e) {
            // e.printStackTrace();
            // } catch (NotFoundException e1) {
            // e1.printStackTrace();
            // } catch (JSONException e1) {
            // e1.printStackTrace();
            // }
            // } else {
            // return;
            // }
        }
    }


    /**
     *@method 固定字体大小方法
     *@author suhu
     *@time 2016/10/11 13:23
     *@param
     *
     */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config=new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config,res.getDisplayMetrics() );
        return res;
    }

    private void exitPopWindow(String message,final int position) {
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
                if(position == 1){
                    if (myWindow != null) {
                        myWindow.dismiss();
                    }
                    myView.setVisibility(View.GONE);
                    mPopMenuBack.setVisibility(View.GONE);
                }else{
                    Intent intent_outof_time = new Intent(RegistActivity.this, LoginActivity.class);
                    intent_outof_time.putExtra("outTime",1);
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
