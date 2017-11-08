package com.fox.exercise;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fox.exercise.bluetooth.DeviceListActivity;
import com.umeng.analytics.MobclickAgent;

import cn.ingenic.indroidsync.MSettings;
import cn.ingenic.indroidsync.MainActivity;
import cn.ingenic.indroidsync.SportsApp;

public class BindingDevice extends AbstractBaseActivity implements OnClickListener {

    // private TextView device_name;
    // private Button btnDetail;
    private Dialog alertDialog;
    private TextView lanyan_lian;
    private RelativeLayout binding_devices;
    private LinearLayout have_binded;
    private View mLine;

    @Override
    public void initIntentParam(Intent intent) {
        // TODO Auto-generated method stub
        title = getResources().getString(R.string.bind_device);

    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        showContentView(R.layout.bind_device);
    }

    @Override
    public void setViewStatus() {
        // TODO Auto-generated method stub
        init();
        updateUi();
    }

    @Override
    public void onPageResume() {
        MobclickAgent.onPageStart("BindingDevice");
    }

    @Override
    public void onPagePause() {
        MobclickAgent.onPageEnd("BindingDevice");
    }

    @Override
    public void onPageDestroy() {
        // TODO Auto-generated method stub

    }


    private void init() {
        /*lanyan_lian = (TextView) findViewById(R.id.lanyan_lian);
        String newMessageInfo = "<font color=\"#ffffff\">追踪活动和睡眠,</font><font color=\"#33c9ca\">蓝牙4.0</font><font color=\"#ffffff\">无线连接</font>";
		lanyan_lian.setText(Html.fromHtml(newMessageInfo));*/

        binding_devices = (RelativeLayout) findViewById(R.id.binding_devices);
        binding_devices.setOnClickListener(this);
        mLine = (View) findViewById(R.id.line1);
        findViewById(R.id.image_xing).setOnClickListener(this);
        findViewById(R.id.layout_device).setOnClickListener(this);
        findViewById(R.id.watch_2).setOnClickListener(this);
        findViewById(R.id.introduce).setOnClickListener(this);
        findViewById(R.id.lanyan_lian).setOnClickListener(this);
        findViewById(R.id.add).setOnClickListener(this);

		/*findViewById(R.id.yunhu_mall).setOnClickListener(this);
		findViewById(R.id.image_mall).setOnClickListener(this);
		findViewById(R.id.layout_mall).setOnClickListener(this);
		findViewById(R.id.gotomall).setOnClickListener(this);*/

        have_binded = (LinearLayout) findViewById(R.id.have_binded);
        findViewById(R.id.sports_history_detail).setOnClickListener(this);
        findViewById(R.id.history_detail_logo).setOnClickListener(this);
        findViewById(R.id.history_detail_content).setOnClickListener(this);
        findViewById(R.id.history_detail_go).setOnClickListener(this);

        findViewById(R.id.sports_sync_set).setOnClickListener(this);
        findViewById(R.id.sync_set_logo).setOnClickListener(this);
        findViewById(R.id.sync_set_content).setOnClickListener(this);
        findViewById(R.id.sync_set_content_go).setOnClickListener(this);

        // findViewById(R.id.device_bind).setOnClickListener(this);
        //findViewById(R.id.bt_back).setOnClickListener(this);

        // device_name = (TextView) findViewById(R.id.device_name);
        // device_name.setOnClickListener(this);
        // btnDetail = (Button) findViewById(R.id.devices);
        // btnDetail.setOnClickListener(this);

    }

    private void showDialog() {
        alertDialog = new Dialog(this, R.style.sports_dialog);
        LayoutInflater mInflater = getLayoutInflater();
        View v = mInflater.inflate(R.layout.sports_dialog, null);
        v.findViewById(R.id.bt_ok).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent it = new Intent(BindingDevice.this, MainActivity.class);
                startActivityForResult(it, 22);
                alertDialog.dismiss();
            }
        });
        v.findViewById(R.id.bt_cancel).setOnClickListener(this);
        TextView message = (TextView) v.findViewById(R.id.message);
        message.setText(R.string.connect_to_watch);
        v.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setContentView(v);
        alertDialog.show();
    }

    private void updateUi() {
        SharedPreferences sps = getSharedPreferences("sprots_uid", 0);
        int sportUid = sps.getInt("sportsUid", 0);
        SharedPreferences sharedPreferences = getSharedPreferences("sports"
                + sportUid, 0);
        String name = sharedPreferences.getString(
                DeviceListActivity.EXTRA_DEVICE_NAME, "");
        if (TextUtils.isEmpty(name)) {
            // device_name.setVisibility(View.GONE);
            // btnDetail.setVisibility(View.GONE);
            binding_devices.setVisibility(View.VISIBLE);
            mLine.setVisibility(View.VISIBLE);
            have_binded.setVisibility(View.GONE);
        } else {
            binding_devices.setVisibility(View.GONE);
            mLine.setVisibility(View.GONE);
            have_binded.setVisibility(View.VISIBLE);
            // device_name.setVisibility(View.VISIBLE);
            // btnDetail.setVisibility(View.VISIBLE);
            // device_name.setText(name);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 22:
                updateUi();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
		/*case R.id.bt_back:
			finish();
			break;*/
            case R.id.binding_devices:
            case R.id.image_xing:
            case R.id.layout_device:
            case R.id.watch_2:
            case R.id.introduce:
            case R.id.lanyan_lian:
            case R.id.add:
                showDialog();
                break;
		/*case R.id.yunhu_mall:
		case R.id.image_mall:
		case R.id.layout_mall:
		case R.id.gotomall:
			startActivity(new Intent(this, YunHuWebViewActivity.class));
			break;*/
            case R.id.sports_history_detail:
            case R.id.history_detail_logo:
            case R.id.history_detail_content:
            case R.id.history_detail_go:
                startActivity(new Intent(BindingDevice.this, SportsDetail.class));
                finish();
                break;
            case R.id.sports_sync_set:
            case R.id.sync_set_logo:
            case R.id.sync_set_content:
            case R.id.sync_set_content_go:
                startActivity(new Intent(BindingDevice.this, MSettings.class));
                break;
            case R.id.bt_cancel:
                alertDialog.dismiss();
                break;
        }
    }


}
