package com.fox.exercise.newversion.act;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fox.exercise.AbstractBaseFragment;
import com.fox.exercise.FindFriendsSendMsg;
import com.fox.exercise.FindOtherFragment;
import com.fox.exercise.R;
import com.fox.exercise.YunHuWebViewActivity;
import com.fox.exercise.bitmap.util.ImageResizer;
import com.fox.exercise.pedometer.ImageWorkManager;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import cn.ingenic.indroidsync.MainActivity;
import cn.ingenic.indroidsync.SportsApp;

/**
 * @author loujungang 运动圈主页面
 */
public class SportCircleMainFragment extends AbstractBaseFragment implements OnTouchListener {
    private ImageButton iView;
    private final int IVEW_ID = 100;
    private SportsApp mSportsApp;
    private ViewPager mPager;
    public ArrayList<Fragment> fragmentList;
    private TextView[] tViews;
    private View[] Views;
    private FindOtherFragment findOtherFragment;
    private FindFriendsFragment findFriendsFragment;
    //	private SportCircleFindFragment sportCircleFindFragment;
    private RecomMend recomMend;
    private NewHuoDongListFrg newHuoDongListFrg;
    MyFragmentPagerAdapter adapter;
    public static int indexFlag = 0;
    public static final int REFRESHPHOTO = 100;

    public static SportsCircleMainhandler msCircleMainhandler = null;

    private View view;
    private LinearLayout title_newsorguanzhu_layout;

    @Override
    public void beforeInitView() {
        // TODO Auto-generated method stub
        if(isAdded()){
            title = getResources().getString(R.string.guangchang);
        }

    }

    @Override
    public void setViewStatus() {
        // 主体
        view = LayoutInflater.from(getActivity()).inflate(
                R.layout.sports_circle_main_frg, null);
//		setContentView(R.layout.sports_circle_main_frg);
        setContentViews(view);
        mSportsApp = (SportsApp) getActivity().getApplication();
        iView = new ImageButton(getActivity());
        iView.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.sports_title_photo));
        iView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        right_btn.setPadding(0, 0, mSportsApp.dip2px(17), 0);
        iView.setId(IVEW_ID);
        iView.setOnClickListener(new rightOnClickListener());
        showRightBtn(iView);
        msCircleMainhandler = new SportsCircleMainhandler();
        mSportsApp.setmSportsCircleMainhandler(msCircleMainhandler);
//		showNewsOrGuanzu();

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

        InitTextView();
        InitViewPager();
    }

    @Override
    public void onPageResume() {
        // TODO Auto-generated method stub
        MobclickAgent.onPageStart("SportCircleMainFragment");
    }

    @Override
    public void onPagePause() {
        // TODO Auto-generated method stub
        MobclickAgent.onPageEnd("SportCircleMainFragment");
    }

    @Override
    public void onPageDestroy() {
        // TODO Auto-generated method stub
//		mSportsApp=null;
    }

    // 跳转拍照发微博
    class rightOnClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case IVEW_ID:
                    Intent intent = new Intent(getActivity(),
                            FindFriendsSendMsg.class);
                    getActivity().startActivity(intent);

                    break;
            }
        }
    }

    private void InitTextView() {
        // TODO Auto-generated method stub
        title_newsorguanzhu_layout = (LinearLayout) view.findViewById(R.id.title_newsorguanzhu_layout);
        RelativeLayout title_news = (RelativeLayout) title_newsorguanzhu_layout
                .findViewById(R.id.title_news);
        RelativeLayout title_guanzhu_news = (RelativeLayout) title_newsorguanzhu_layout
                .findViewById(R.id.title_guanzhu_news);
        RelativeLayout title_tuijian = (RelativeLayout) title_newsorguanzhu_layout
                .findViewById(R.id.title_tuijian);
        RelativeLayout title_huodong_news = (RelativeLayout) title_newsorguanzhu_layout
                .findViewById(R.id.title_huodong_news);


        TextView title_news_msg = (TextView) title_newsorguanzhu_layout
                .findViewById(R.id.title_news_msg);
        TextView title_news_guanzhu_msg = (TextView) title_newsorguanzhu_layout
                .findViewById(R.id.title_news_guanzhu_msg);
        TextView title_tuijian_msg = (TextView) title_newsorguanzhu_layout
                .findViewById(R.id.title_tuijian_msg);
        TextView title_huodong_msg = (TextView) title_newsorguanzhu_layout
                .findViewById(R.id.title_huodong_msg);


        View title_news_line = (View) title_newsorguanzhu_layout
                .findViewById(R.id.title_news_line);
        View title_news_guanzhu_line = (View) title_newsorguanzhu_layout
                .findViewById(R.id.title_news_guanzhu_line);
        View title_tuijian_line = (View) title_newsorguanzhu_layout
                .findViewById(R.id.title_tuijian_line);
        View title_huodong_line = (View) title_newsorguanzhu_layout
                .findViewById(R.id.title_huodong_line);


        tViews = new TextView[]{title_tuijian_msg, title_news_msg, title_huodong_msg, title_news_guanzhu_msg};
        Views = new View[]{title_tuijian_line, title_news_line, title_huodong_line, title_news_guanzhu_line};
        title_news_msg.setOnClickListener(new txListener(1));
        title_news.setOnClickListener(new txListener(1));
        title_news_guanzhu_msg.setOnClickListener(new txListener(3));
        title_guanzhu_news.setOnClickListener(new txListener(3));
        title_tuijian.setOnClickListener(new txListener(0));
        title_tuijian_msg.setOnClickListener(new txListener(0));
        title_huodong_news.setOnClickListener(new txListener(2));
        title_huodong_msg.setOnClickListener(new txListener(2));
    }

    class txListener implements View.OnClickListener {
        private int index = 0;

        public txListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            // iView.setBackgroundDrawable(iDrawables[index]);
            // iView.setOnClickListener(new rightOnClickListener(index));
            // right_btn.setOnClickListener(new rightOnClickListener(index));
            mPager.setCurrentItem(index);
            indexFlag = index;
        }
    }

    private void InitViewPager() {
        // TODO Auto-generated method stub
        mPager = (ViewPager) view.findViewById(
                R.id.sport_circle_main_viewpager);
        fragmentList = new ArrayList<Fragment>();
        findOtherFragment = new FindOtherFragment();
        findFriendsFragment = new FindFriendsFragment();
//		sportCircleFindFragment = new SportCircleFindFragment();
        recomMend = new RecomMend();
        newHuoDongListFrg = new NewHuoDongListFrg();
        fragmentList.add(recomMend);
        fragmentList.add(findOtherFragment);
        fragmentList.add(newHuoDongListFrg);
        fragmentList.add(findFriendsFragment);

        adapter = new MyFragmentPagerAdapter(getChildFragmentManager(),
                fragmentList);
        mPager.setAdapter(adapter);

        Views[1].setVisibility(View.GONE);
        Views[2].setVisibility(View.GONE);
        Views[3].setVisibility(View.GONE);

//		Views[2].setVisibility(View.GONE);
        Views[0].setVisibility(View.VISIBLE);
//		tViews[0].setTextSize(17);
//		tViews[1].setTextSize(16);
//		tViews[2].setTextSize(16);
        mPager.setOffscreenPageLimit(4);
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
        mPager.setCurrentItem(0);
    }

    class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {
        ArrayList<Fragment> list;

        public MyFragmentPagerAdapter(FragmentManager fm,
                                      ArrayList<Fragment> list) {

            super(fm);
            this.list = list;

        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Fragment getItem(int arg0) {
            return list.get(arg0);
        }

    }

    class MyOnPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onPageSelected(int position) {
            // TODO Auto-generated method stub
            if (position == 0) {
                View view = getActivity().getWindow().peekDecorView();
                if (view!=null){
                    InputMethodManager inputmanger = (InputMethodManager) getActivity()
                            .getSystemService(MainActivity.INPUT_METHOD_SERVICE);
                    inputmanger.hideSoftInputFromWindow(view.getWindowToken(),
                            0);
                }
                getActivity().findViewById(R.id.sportw_ll_belowmain).setVisibility(View.VISIBLE);
                Views[1].setVisibility(View.GONE);
                Views[2].setVisibility(View.GONE);
                Views[3].setVisibility(View.GONE);
                Views[0].setVisibility(View.VISIBLE);
//				tViews[0].setTextSize(17);
//				tViews[1].setTextSize(16);
//				tViews[2].setTextSize(16);
                if (!mSportsApp.LoginOption) {
                    mSportsApp.TyrLoginAction(getActivity(),
                            getString(R.string.sports_love_title),
                            getString(R.string.try_to_login));

                }
            } else if (position == 1) {
                if (mSportsApp.getSport_below_main() == 1){
                    getActivity().findViewById(R.id.sportw_ll_belowmain).setVisibility(View.GONE);
                }else{
                    getActivity().findViewById(R.id.sportw_ll_belowmain).setVisibility(View.VISIBLE);
                }
                Views[0].setVisibility(View.GONE);
                Views[2].setVisibility(View.GONE);
                Views[3].setVisibility(View.GONE);
                Views[1].setVisibility(View.VISIBLE);
//				tViews[1].setTextSize(17);
//				tViews[2].setTextSize(16);
//				tViews[0].setTextSize(16);
                if (!mSportsApp.LoginOption) {
                    mSportsApp.TyrLoginAction(getActivity(),
                            getString(R.string.sports_love_title),
                            getString(R.string.try_to_login));

                }else {
                   if(findOtherFragment!=null&&!findOtherFragment.isFirst){
                       findOtherFragment.newLocalDate();
                   }
                    adapter.notifyDataSetChanged();
                }
            } else if (position == 2) {
                View view = getActivity().getWindow().peekDecorView();
                if (view!=null){
                    InputMethodManager inputmanger = (InputMethodManager) getActivity()
                            .getSystemService(MainActivity.INPUT_METHOD_SERVICE);
                    inputmanger.hideSoftInputFromWindow(view.getWindowToken(),
                            0);
                }
                getActivity().findViewById(R.id.sportw_ll_belowmain).setVisibility(View.VISIBLE);
                Views[1].setVisibility(View.GONE);
                Views[0].setVisibility(View.GONE);
                Views[3].setVisibility(View.GONE);
                Views[2].setVisibility(View.VISIBLE);
//				tViews[2].setTextSize(17);
//				tViews[1].setTextSize(16);
//				tViews[0].setTextSize(16);
                if (!mSportsApp.LoginOption) {
                    mSportsApp.TyrLoginAction(getActivity(),
                            getString(R.string.sports_love_title),
                            getString(R.string.try_to_login));

                }else{
                    if (newHuoDongListFrg!=null&&!newHuoDongListFrg.isFirst){
                        newHuoDongListFrg.getFirstData();
                    }
                    adapter.notifyDataSetChanged();
                }
            } else if (position == 3) {
                if (mSportsApp.getSport_below_main_guanzhu() == 1){
                    getActivity().findViewById(R.id.sportw_ll_belowmain).setVisibility(View.GONE);
                }else{
                    getActivity().findViewById(R.id.sportw_ll_belowmain).setVisibility(View.VISIBLE);
                }
                if (!mSportsApp.LoginOption) {
                    mSportsApp.TyrLoginAction(getActivity(),
                            getString(R.string.sports_love_title),
                            getString(R.string.try_to_login));

                } else {
					if (!findFriendsFragment.isFirst) {
						findFriendsFragment.loadDate();
					}
					adapter.notifyDataSetChanged();
                }
                Views[0].setVisibility(View.GONE);
                Views[2].setVisibility(View.GONE);
                Views[1].setVisibility(View.GONE);
                Views[3].setVisibility(View.VISIBLE);
//				tViews[1].setTextSize(17);
//				tViews[2].setTextSize(16);
//				tViews[0].setTextSize(16);

            }
            // tViews[position].setTextColor(Color.parseColor("#f49000"));
            // tViews[mSelectionPosition].setTextColor(Color.parseColor("#ffffffff"));
            // TranslateAnimation anim = new TranslateAnimation(mMoveSize
            // * position, mMoveSize * mSelectionPosition, 0, 0);
            // anim.setFillAfter(true);
            // anim.setDuration(200);
            // image.startAnimation(anim);
            // mSelectionPosition = position;
        }
    }

    class SportsCircleMainhandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case REFRESHPHOTO:
                    boolean isfacechange = (Boolean) msg.obj;
                    if (isfacechange) {
                        ImageWorkManager mImageWorkerMan_Icon = new ImageWorkManager(
                                getActivity(), 0, 0);
                        ImageResizer mImageWorker_Icon = mImageWorkerMan_Icon
                                .getImageWorker();
                        if (!SportsApp.getInstance().LoginOption) {
                            userPhoto
                                    .setImageResource(R.drawable.sports_user_edit_portrait_male);
                        } else {
                            if (SportsApp.getInstance().getSportUser().getSex()
                                    .equals("man")) {
                                userPhoto
                                        .setImageResource(R.drawable.sports_user_edit_portrait_male);
                            } else {
                                userPhoto
                                        .setImageResource(R.drawable.sports_user_edit_portrait);
                            }

                            mImageWorker_Icon.loadImage(SportsApp.getInstance()
                                            .getSportUser().getUimg(), userPhoto, null,
                                    null, false);
                        }
                    }
                    break;

                default:
                    break;
            }
        }
    }

    @Override
    public boolean onTouch(View arg0, MotionEvent arg1) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view.setOnTouchListener(this);
        super.onViewCreated(view, savedInstanceState);
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
