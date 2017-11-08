package com.fox.exercise.login;


import java.util.logging.Handler;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import cn.ingenic.indroidsync.SportsApp;

import com.fox.exercise.AbstractBaseActivity;
import com.fox.exercise.R;
import com.fox.exercise.api.ApiBack;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.umeng.analytics.MobclickAgent;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class ModifyPwdActivity extends AbstractBaseActivity implements View.OnClickListener {
    private static final String TAG = "ModifyPwdActivity";
    private EditText currentpwd;
    private EditText newpwd;
    private EditText confirmpwd;
    private Dialog modifypwdProgressDialog;
    private SportsApp mSportsApp;
    private static final int okButton = 111;
//    private TextView okTxt;
    private LinearLayout layout_password, layout_newpwd, layout_confirmpwd;

//    private android.os.Handler mHandler = new android.os.Handler() {
//        public void handleMessage(android.os.Message msg) {
//            switch (msg.what) {
//                case 0:
//                    break;
//                case 1:
//                    break;
//            }
//
//        }
//
//        ;
//    };

    @Override
    public void initIntentParam(Intent intent) {
        // TODO Auto-generated method stub
        title = getResources().getString(R.string.sports_edit);
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        showContentView(R.layout.sports_pwd_edit);
        mSportsApp = (SportsApp) getApplication();
        currentpwd = (EditText) findViewById(R.id.ed_passwordss);
        newpwd = (EditText) findViewById(R.id.ed_newpwd);
        confirmpwd = (EditText) findViewById(R.id.ed_confirmpwd);
//        okTxt = new TextView(this);
//        okTxt.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT));
//        okTxt.setText(getResources().getString(R.string.button_ok));
//        okTxt.setTextSize(19);
//        okTxt.setTextColor(getResources().getColor(R.color.white));
//        showRightBtn(okTxt);
//        right_btn.setPadding(0, 0, SportsApp.getInstance().dip2px(17), 0);
//        okTxt.setId(okButton);
//        okTxt.setOnClickListener(this);
//        right_btn.setOnClickListener(this);
    }

    @Override
    public void setViewStatus() {
        // TODO Auto-generated method stub
        findViewById(R.id.bt_ok).setOnClickListener(this);
        layout_password = (LinearLayout) findViewById(R.id.layout_password);
        layout_newpwd = (LinearLayout) findViewById(R.id.layout_newpwd);
        layout_confirmpwd = (LinearLayout) findViewById(R.id.layout_confirmpwd);
        currentpwd.addTextChangedListener(new PassWord(currentpwd));
        newpwd.addTextChangedListener(new PassWord(newpwd));
        confirmpwd.addTextChangedListener(new PassWord(confirmpwd));

        currentpwd.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                // 此处为得到焦点时的处理内容
                    layout_password.setBackgroundResource(R.drawable.stroke_linecicle_yellow_background);
                } else {
                // 此处为失去焦点时的处理内容
                    layout_password.setBackgroundResource(R.drawable.stroke_nocicle_gray_background);
                }
            }
        });
        newpwd.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
           // 此处为得到焦点时的处理内容
                    layout_newpwd.setBackgroundResource(R.drawable.stroke_linecicle_yellow_background);
                } else {
                // 此处为失去焦点时的处理内容
                    layout_newpwd.setBackgroundResource(R.drawable.stroke_nocicle_gray_background);
                }
            }
        });
        confirmpwd.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                // 此处为得到焦点时的处理内容
                    layout_confirmpwd.setBackgroundResource(R.drawable.stroke_linecicle_yellow_background);
                } else {
              // 此处为失去焦点时的处理内容
                    layout_confirmpwd.setBackgroundResource(R.drawable.stroke_nocicle_gray_background);
                }
            }
        });

    }

    @Override
    public void onPageResume() {
        MobclickAgent.onPageStart("ModifyPwdActivity");
    }

    @Override
    public void onPagePause() {
        MobclickAgent.onPageEnd("ModifyPwdActivity");
    }

    @Override
    public void onPageDestroy() {
        // TODO Auto-generated method stub

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
                Toast.makeText(ModifyPwdActivity.this, "您输入的字符不符合要求，请重新输入!", Toast.LENGTH_SHORT).show();
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
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.bt_back:
                finish();
            /*startActivity(new Intent(ModifyPwdActivity.this,UserEditActivity.class));
            ModifyPwdActivity.this.finish();*/
                break;
            case R.id.bt_ok:
                if (currentpwd.getEditableText().toString().equals("")) {
                    Toast.makeText(ModifyPwdActivity.this, getResources().getString(R.string.sports_toast_please_inputpwd), Toast.LENGTH_SHORT).show();
                } else if (currentpwd.getEditableText().toString().equals(newpwd.getEditableText().toString())) {
                    Toast.makeText(ModifyPwdActivity.this, getResources().getString(R.string.sports_toast_newpwd_nodifference), Toast.LENGTH_SHORT).show();
                } else if (newpwd.getEditableText().toString().equals("")) {
                    Toast.makeText(ModifyPwdActivity.this, getResources().getString(R.string.sports_toast_please_inputnewpwd), Toast.LENGTH_SHORT).show();
                } else if (newpwd.getEditableText().toString().length() < 6) {
                    Toast.makeText(ModifyPwdActivity.this, getResources().getString(R.string.sports_toast_pwd_length), Toast.LENGTH_SHORT).show();
                } else if (confirmpwd.getEditableText().toString().equals("")) {
                    Toast.makeText(ModifyPwdActivity.this, getResources().getString(R.string.sports_toast_please_confirmpwd), Toast.LENGTH_SHORT).show();
                } else if (!newpwd.getEditableText().toString().equals(confirmpwd.getEditableText().toString())) {
                    Toast.makeText(ModifyPwdActivity.this, getResources().getString(R.string.sports_toast_not_fit), Toast.LENGTH_SHORT).show();
                } else {
//				modifypwdProgressDialog = ProgressDialog.show(ModifyPwdActivity.this, "", getString(R.string.sports_dialog_modifying), true, true);
                    modifypwdProgressDialog = new Dialog(ModifyPwdActivity.this, R.style.sports_dialog);
                    LayoutInflater mInflater = getLayoutInflater();
                    View v1 = mInflater.inflate(R.layout.sports_progressdialog, null);
                    TextView message = (TextView) v1.findViewById(R.id.message);
                    message.setText(R.string.sports_dialog_modifying);
                    v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
                    modifypwdProgressDialog.setContentView(v1);
                    modifypwdProgressDialog.setCanceledOnTouchOutside(false);
                    modifypwdProgressDialog.show();
                    new AsyncTask<Void, Void, ApiBack>() {
                        @Override
                        protected ApiBack doInBackground(Void... params) {
                            // TODO Auto-generated method stub
                            try {
                                Log.e(TAG, "currentpwd-->" + currentpwd.getText().toString() + "newpwd-->" + newpwd.getEditableText().toString());
                                ApiBack back = ApiJsonParser.modifymsg(mSportsApp.getSessionId(), "", currentpwd.getEditableText().toString(), newpwd.getEditableText().toString(), "", "", "", "", "", "", "", "", "", "", "", "");
                                return back;
                            } catch (ApiNetException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (ApiSessionOutException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(ApiBack back) {
                            // TODO Auto-generated method stub
                            Log.e(TAG, back.getFlag() + "555");
                            Log.e(TAG, back.getMsg() + "555");
                            if (back != null) {
                                modifypwdProgressDialog.dismiss();
                                if (back.getFlag() == 0) {
                                    SharedPreferences sp = getSharedPreferences("user_login_info", Context.MODE_PRIVATE);
                                    Editor ed = sp.edit();
                                    ed.putString("pwd", newpwd.getEditableText().toString());
                                    //保存用户身份标识
                                    ed.commit();
                                    Toast.makeText(ModifyPwdActivity.this, getString(R.string.sports_modify_successed), Toast.LENGTH_SHORT).show();
                                    ModifyPwdActivity.this.finish();
                                }
                                if (back.getFlag() == -1) {
                                    if (back.getMsg().equals("error_current_pwd")) {
                                        Toast.makeText(ModifyPwdActivity.this, getString(R.string.sports_modify_failed_for_error_psd), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ModifyPwdActivity.this, getString(R.string.sports_modify_failed), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    }.execute();
                }
                break;

            default:
                break;
        }
    }


}

	