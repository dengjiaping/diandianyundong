package com.fox.exercise.newversion.act;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.example.xinlv.XinlvActivity;
import com.fox.exercise.AbstractBaseFragment;
import com.fox.exercise.R;
import com.fox.exercise.YunHuWebViewActivity;
import com.umeng.analytics.MobclickAgent;

import cn.ingenic.indroidsync.SportsApp;

/**
 * @author loujungang 健康模块
 */
public class IndexHealthFrg extends AbstractBaseFragment implements OnClickListener {
    //	private SportsApp mSportsApp;
    private Context mContext;
    private View view;
    private SportsApp mSportsApp;


    @Override
    public void beforeInitView() {
        // TODO Auto-generated method stub
        title = getActivity().getResources().getString(R.string.sports_health);
    }

    @Override
    public void setViewStatus() {
        // TODO Auto-generated method stub
        view = LayoutInflater.from(getActivity()).inflate(
                R.layout.sports_health_layout, null);
        setContentViews(view);
        mSportsApp = (SportsApp) getActivity().getApplication();
        mSportsApp.addActivity(getActivity());
//		setContentView(R.layout.sports_health_layout);
        mContext = getActivity();
//		mSportsApp = (SportsApp) getActivity().getApplication();
        view.findViewById(R.id.sleep_layout).setOnClickListener(this);
        view.findViewById(R.id.xinlv_layout).setOnClickListener(this);

//        left_ayout.setOnClickListener(new clickListener());
//        iButton = new ImageButton(getActivity());
//        iButton.setBackgroundDrawable(getResources().getDrawable(
//                R.drawable.fristpage_jinbimall));
//        iButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT));
//        left_ayout.setPadding(0, 0, mSportsApp.dip2px(17), 0);
//        iButton.setOnClickListener(new clickListener());
//        if (left_ayout != null) {
//            left_ayout.removeAllViews();
//            left_ayout.addView(iButton);
//        }
    }

    @Override
    public void onPageResume() {
        // TODO Auto-generated method stub
        MobclickAgent.onPageStart("IndexHealthFrg");
    }

    @Override
    public void onPagePause() {
        // TODO Auto-generated method stub
        MobclickAgent.onPageEnd("IndexHealthFrg");
    }

    @Override
    public void onPageDestroy() {
        // TODO Auto-generated method stub
        mContext = null;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.sleep_layout:
                Intent intent = new Intent(mContext, SleepMainActivity.class);
                startActivity(intent);
                break;
            case R.id.xinlv_layout:
                //心率测试
                Intent intent2 = new Intent(mContext, XinlvActivity.class);
                startActivity(intent2);
                break;

            default:
                break;
        }

    }

    class clickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            // 金币商城
            if (mSportsApp.LoginNet) {
                if (mSportsApp.isOpenNetwork()) {
                    startActivity(new Intent(getActivity(), YunHuWebViewActivity.class));
                } else {
                    Toast.makeText(
                            getActivity(),
                            getResources().getString(
                                    R.string.error_cannot_access_net),
                            Toast.LENGTH_SHORT);
                }
            } else {
                mSportsApp.NoNetLogin(getActivity());
            }
        }
    }


}
