package com.fox.exercise.login;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.fox.exercise.R;
import com.fox.exercise.SmartBarUtils;
import com.fox.exercise.login.FirstScrollLayout.OnViewChangeListener;

public class FirstLoginView extends Activity implements OnViewChangeListener {

//	private static final String TAG = "FirstLoginView";

    private FirstScrollLayout scrollLayout;

    private int[] images = new int[]{R.drawable.start_1, R.drawable.start_2, R.drawable.start_3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean findMethod = SmartBarUtils.findActionBarTabsShowAtBottom();
        if (!findMethod) {
            // 取消ActionBar拆分，换用TabHost
            getWindow().setUiOptions(0);
            getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        setContentView(R.layout.firstloginlayout);

        scrollLayout = (FirstScrollLayout) findViewById(R.id.scrollLayout);
        scrollLayout.SetOnViewChangeListener(this);
        if (findMethod) {
            getActionBar().setDisplayShowHomeEnabled(false);
            getActionBar().setDisplayShowTitleEnabled(false);
            SmartBarUtils.setActionModeHeaderHidden(getActionBar(), true);
            SmartBarUtils.setActionBarViewCollapsable(getActionBar(), true);
        }
        for (int i : images) {
            addView(i);
        }
    }

    private void addView(int resID) {
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.first_imageview, null);
        ImageView img = (ImageView) v.findViewById(R.id.imgView);
        img.setBackgroundResource(resID);
        if (resID == R.drawable.start_3) {
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    finish();
                }
            });
        }
        scrollLayout.addView(v);
    }

    // OnViewChange是FirstScrollLayout内部接口OnViewChangeListener的方法
    @Override
    public void OnViewChange(int view) {
        // 滑动到右边最后一块时结束
        /*if (view == scrollLayout.getChildCount()) {
			LoginActivity.instance.resetStartState();
			finish();
		}*/
//		Log.i("view", "view=" + (view + 1));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        images = null;
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
