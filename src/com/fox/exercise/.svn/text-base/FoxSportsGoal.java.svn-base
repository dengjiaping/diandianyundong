package com.fox.exercise;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import cn.ingenic.indroidsync.SportsApp;

public class FoxSportsGoal extends Activity implements OnClickListener {
    private SportsApp mSportsApp;
    private EditText editStep;
    private EditText editCalories;
    private int numberStep;
    private int numberCalories;
    private int num = 0;
    private SharedPreferences sp;
//	private TextView calories;

    // private String s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean findMethod = SmartBarUtils.findActionBarTabsShowAtBottom();
        if (!findMethod) {
            // 取消ActionBar拆分，换用TabHost
            getWindow().setUiOptions(0);
            getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        setContentView(R.layout.fox_sports_goal);
        mSportsApp = (SportsApp) getApplication();
        init();

        if (findMethod) {
            getActionBar().setDisplayShowHomeEnabled(false);
            getActionBar().setDisplayShowTitleEnabled(false);
            SmartBarUtils.setActionModeHeaderHidden(getActionBar(), true);
            SmartBarUtils.setActionBarViewCollapsable(getActionBar(), true);
        }
        // System.out.println(s.equals("any string"));
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("FoxSportsGoal");
        MobclickAgent.onResume(FoxSportsGoal.this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("FoxSportsGoal");
        MobclickAgent.onPause(FoxSportsGoal.this);
    }

    private int kllMultiple;

    private void init() {
        findViewById(R.id.sports_goal_back).setOnClickListener(this);
        findViewById(R.id.sports_goal_ok).setOnClickListener(this);
        editStep = (EditText) findViewById(R.id.edit_step);
        editCalories = (EditText) findViewById(R.id.edit_calories);

        SharedPreferences getDate = getSharedPreferences("sports"
                + mSportsApp.getSportUser().getUid(), 0);
        editStep.setText(String.valueOf(getDate.getInt("editDistance", 0)));
        editCalories.setText(String.valueOf(getDate.getInt("editCalories", 0)));
        editStep.addTextChangedListener(editSteplistener);
        editCalories.addTextChangedListener(editCaloriesListener);
//		calories = (TextView) findViewById(R.id.text_calories);
        int num;

        sp = getSharedPreferences(
                "sports" + mSportsApp.getSportUser().getUid(), 0);
        int type = sp.getInt("type", 1);
        // Log.e("222",
        // "mSportsApp.getSportUser().getUid()---"+mSportsApp.getSportUser().getUid());
        // Log.e("222", "mSportsApp.getSportUser().getUid()type---"+type);
        kllMultiple = (int) KALULIMultiple(type);
        // Log.e("222",
        // "mSportsApp.getSportUser().getUid()kllMultiple---"+kllMultiple);
        switch (type) {
            case 0:
            case 1:
            case 6:
            case 7:
            case 8:
            case 9:
            case 12:
            case 14:
                num = kllMultiple * 200;
//			calories.setText("燃烧脂肪(0~" + num + ")卡路里");
                break;
            default:
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sports_goal_back:

                finish();
                break;
            case R.id.sports_goal_ok:
                if (editStep.getText().toString().trim().equals("")
                        || editCalories.getText().toString().trim().equals("")) {
                    Toast.makeText(FoxSportsGoal.this, "距离或者卡路里未输入!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    numberStep = Integer.parseInt(editStep.getText().toString());
                    numberCalories = Integer.parseInt(editCalories.getText()
                            .toString());
                    SharedPreferences sharedPreferences = getSharedPreferences(
                            "sports" + mSportsApp.getSportUser().getUid(), 0);
                    Editor editor = sharedPreferences.edit();
                    // editor.putInt("editStep", numberStep);
                    editor.putInt("editDistance", numberStep);
                    editor.putInt("editCalories", numberCalories);
                    // editor.putInt("sportStyle", 0);
                    editor.commit();
                    finish();
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK: {
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    TextWatcher editSteplistener = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                  int arg3) {
            // TODO Auto-generated method stub

        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable arg0) {
            // TODO Auto-generated method stub
            sp = getSharedPreferences("sports"
                    + mSportsApp.getSportUser().getUid(), 0);
            int type = sp.getInt("type", 1);
            int leng = editStep.getText().toString().length();
            if ("".equals(editStep.getText().toString())) {
                return;
            }
            int nameStep = Integer.parseInt(editStep.getText().toString());
            Log.i("", "lulu公里" + nameStep + "type" + type);

            num++;
            if (num == 2) {
                num = 0;
                return;
            }
            kllMultiple = (int) KALULIMultiple(type);
            if (!editStep.getText().toString().equals("")
                    && editStep.getText().toString() != null) {
                switch (type) {
                    case 0:
                    case 1:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 12:
                    case 14:
                        if (nameStep > 200) {
                            editStep.setText(200 + "");
                            Toast.makeText(FoxSportsGoal.this, "距离不能超过200",
                                    Toast.LENGTH_SHORT).show();
                            editCalories.setText((200 * kllMultiple) + "");
                        } else {
                            editCalories.setText((nameStep * kllMultiple) + "");
                        }
                        break;
                    default:
                        break;
                }
            }

        }
    };

    TextWatcher editCaloriesListener = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                  int arg3) {
            // TODO Auto-generated method stub

        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable arg0) {
            // TODO Auto-generated method stub
            sp = getSharedPreferences("sports"
                    + mSportsApp.getSportUser().getUid(), 0);
            int type = sp.getInt("type", 1);
            if ("".equals(editCalories.getText().toString())) {
                return;
            }
            editCalories.setFilters(new InputFilter[]{

            });
            int nameCalories = Integer.parseInt(editCalories.getText()
                    .toString());
            Log.i("", "lulu公里" + nameCalories + "type" + type);

            num++;
            if (num == 2) {
                num = 0;
                return;
            }
            kllMultiple = (int) KALULIMultiple(type);
            if (kllMultiple != 0 && !editCalories.getText().toString().equals("")) {
                switch (type) {
                    case 0:
                    case 1:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 12:
                    case 14:
                        if (nameCalories / kllMultiple > 200) {
                            editCalories.setText(200 * kllMultiple + "");
                            Toast.makeText(FoxSportsGoal.this, "卡路里消耗太大",
                                    Toast.LENGTH_SHORT).show();
                            editStep.setText(200 + "");
                        } else {
                            editStep.setText((nameCalories / kllMultiple) + "");
                        }
                        break;
                    default:
                        break;
                }
            } else {
                editStep.setText("");
            }
        }
    };

    public double KALULIMultiple(int type) {
        int weight = SportsApp.getInstance().getSportUser().getWeight();
        double KLL = 1;
        switch (type) {
            case 0:
                KLL = weight * 3.335 / 4;
                break;
            case 1:
                KLL = weight * 9.375 / 10;
                break;
            case 6:
                KLL = weight * 8.0375 / 6;
                break;
            case 7:
                KLL = weight * 4.025 / 3;
                break;
            case 8:
                KLL = weight * 9.375 / 9;
                break;
            case 9:
                KLL = weight * 9.375 / 20;
                break;
            case 12:
                KLL = weight * 9.375 / 10;
                break;
            case 14:
                KLL = weight * 2.6675 / 5;
                break;

            default:
                break;
        }
        return KLL;
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
}
