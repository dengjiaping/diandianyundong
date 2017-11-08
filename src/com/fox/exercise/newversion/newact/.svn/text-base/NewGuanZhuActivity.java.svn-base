package com.fox.exercise.newversion.newact;

import java.util.ArrayList;

import cn.ingenic.indroidsync.SportsApp;

import com.fox.exercise.AbstractBaseOtherActivity;
import com.fox.exercise.AddFriendActivity;
import com.fox.exercise.FriendsFragment;
import com.fox.exercise.MainFragmentActivity;
import com.fox.exercise.R;
import com.fox.exercise.SportsExceptionHandler;
import com.fox.exercise.api.ApiConstant;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.api.entity.UserDetail;
import com.umeng.analytics.MobclickAgent;

import android.os.Handler;
import android.os.Message;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author loujungang 新关注的activity
 */
public class NewGuanZhuActivity extends AbstractBaseOtherActivity implements OnClickListener {
    private ImageView image;
    private int mMoveSize;
    private ViewPager mPager;
    private ArrayList<Fragment> fragmentList;
    public int currIndex;
    private int mSelectionPosition = 1;
    private TextView[] tViews;
    private ImageButton iView;
    private Drawable[] iDrawables;
    private Button sinxin_num;
    private SportsApp fSportsApp;
    private static final int LEFT_TITLE_VIEW = 1;
    private static final int LEFT_TITLE_BUTTON = 2;
    private SportsExceptionHandler mExceptionHandler = null;
    private int fromwhere;

    @Override
    public void initIntentParam(Intent intent) {
        // TODO Auto-generated method stub
//		title = getResources().getString(R.string.rankboard_friend);
        title = getResources().getString(R.string.guanzhu_people);
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub

    }

    @Override
    public void setViewStatus() {
        // TODO Auto-generated method stub
//		top_title_layout.addView(LayoutInflater.from(this).inflate(R.layout.title_under_toptitle,null));
        //主体
        showContentView(R.layout.sports_page_friends);
        if (getIntent() != null){
            fromwhere = getIntent().getIntExtra("fraomMe",0);
        }
        fSportsApp = SportsApp.getInstance();
        mExceptionHandler = fSportsApp.getmExceptionHandler();
        iDrawables = new Drawable[]{null, getResources().getDrawable(
                R.drawable.title_add_friends)};
        iView = new ImageButton(this);
        showRightBtn(iView);
        iView.setBackgroundDrawable(iDrawables[1]);
        iView.setOnClickListener(new rightOnClickListener(1));
        right_btn.setOnClickListener(new rightOnClickListener(1));
        iView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        right_btn.setPadding(0, 0, SportsApp.dip2px(17), 0);
//		InitTextView();
//		InitImage();
        InitViewPager();
        left_ayout.setId(LEFT_TITLE_VIEW);
        left_ayout.setOnClickListener(this);
        leftButton.setId(LEFT_TITLE_BUTTON);
        leftButton.setOnClickListener(this);
        leftButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (fromwhere == 1) {
                    startActivity(new Intent(NewGuanZhuActivity.this, MainFragmentActivity.class));
                    finish();
                } else {
                    finish();
                }
            }
        });
    }

    @Override
    public void onPageResume() {
        // TODO Auto-generated method stub
        MobclickAgent.onPageStart("NewGuanZhuActivity");

    }

    @Override
    public void onPagePause() {
        // TODO Auto-generated method stub
        MobclickAgent.onPageEnd("NewGuanZhuActivity");
    }

    @Override
    public void onPageDestroy() {
        // TODO Auto-generated method stub
        fSportsApp.setFriendsHandler(null);
//		fSportsApp=null;
        fragmentList = null;

        if (iDrawables != null) {
            iDrawables = null;
        }
        tViews = null;

    }

    class rightOnClickListener implements OnClickListener {

        private int index;

        public rightOnClickListener(int index) {
            this.index = index;
        }

        @Override
        public void onClick(View v) {
            switch (index) {
                case 1:
                    startActivity(new Intent(NewGuanZhuActivity.this, AddFriendActivity.class));
                    finish();
                    break;

                default:
                    break;
            }
        }
    }

    private void InitViewPager() {
        // TODO Auto-generated method stub
        mPager = (ViewPager) findViewById(R.id.viewpager);
        fragmentList = new ArrayList<Fragment>();
//		Fragment priFragment = new PrivateMsgFragment();
        Fragment friendFragment = new FriendsFragment();
        //Fragment findFragment = new FindFragment();
//		fragmentList.add(priFragment);
        fragmentList.add(friendFragment);
        //fragmentList.add(findFragment);
        mPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));

        mPager.setCurrentItem(1);
//		mPager.setOffscreenPageLimit(2);
//		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

//	private void InitImage() {
//		// TODO Auto-generated method stub
//		image = (ImageView) findViewById(R.id.cursor_bar);	
//		BitmapDrawable bitmap = (BitmapDrawable) image.getDrawable();	
//		mMoveSize= bitmap.getBitmap().getWidth()+fSportsApp.dip2px(2);		
//		
//	}

    //	private void InitTextView() {
//		// TODO Auto-generated method stub
//		RelativeLayout view1 = (RelativeLayout) findViewById(R.id.tab_sixin);
//		TextView view2 = (TextView) findViewById(R.id.tab_haoyou);
//		//RelativeLayout view3 = (RelativeLayout) getActivity().findViewById(R.id.tab_faxian);
//		TextView tView1=(TextView)findViewById(R.id.tab_sixin_tv);
//		//TextView tView2=(TextView)getActivity().findViewById(R.id.tab_faxian_tv);
//		tViews=new TextView[]{tView1,view2};	
//		sinxin_num=(Button)findViewById(R.id.sinxin_focusText);
//		//faxian_num=(Button)getActivity().findViewById(R.id.faxian_focusText);
//		int friendsMsg=fSportsApp.getSportUser().getMsgCounts().getPrimsg();
//		if(friendsMsg>0){
//			sinxin_num.setVisibility(View.VISIBLE);
//			sinxin_num.setText(friendsMsg > 99 ? "99+" : friendsMsg + "");
//		}
//		fSportsApp.setFriendsHandler(fHandler);
//		OnClickListener sClickListener=new txListener(0);
//		view1.setOnClickListener(sClickListener);
//		tView1.setOnClickListener(sClickListener);
//		view2.setOnClickListener(new txListener(1));
//		//view3.setOnClickListener(new txListener(2));
//	}
    Handler fHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ApiConstant.UPDATE_FRIENDS_MSG:
                    int friendsMsg = msg.arg1;
                    if (sinxin_num != null) {
                        sinxin_num.setVisibility(View.VISIBLE);
                        sinxin_num.setText(friendsMsg > 99 ? "99+" : friendsMsg + "");
                    }
                    break;

                case ApiConstant.CLEAR_PRI_MSG:
                    if (sinxin_num != null)
                        sinxin_num.setVisibility(View.GONE);
                    Editor editor = getSharedPreferences("sports" + fSportsApp.getSportUser().getUid(), 0).edit();
                    editor.putInt("primsgcount", 0);
                    editor.commit();
                    break;
                default:

                    break;
            }
        }

    };

    class txListener implements View.OnClickListener {
        private int index = 0;

        public txListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            iView.setBackgroundDrawable(iDrawables[index]);
            iView.setOnClickListener(new rightOnClickListener(index));
            right_btn.setOnClickListener(new rightOnClickListener(index));
            mPager.setCurrentItem(index);
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
            tViews[position].setTextColor(Color.parseColor("#f49000"));
            tViews[mSelectionPosition].setTextColor(Color.parseColor("#ffffffff"));
            TranslateAnimation anim = new TranslateAnimation(mMoveSize
                    * position, mMoveSize * mSelectionPosition, 0, 0);
            anim.setFillAfter(true);
            anim.setDuration(200);
            image.startAnimation(anim);
            mSelectionPosition = position;
        }
    }

    class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {
        ArrayList<Fragment> list;

        public MyFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> list) {

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

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case LEFT_TITLE_VIEW:
            case LEFT_TITLE_BUTTON:
                GetUserNameThread thread = new GetUserNameThread();
                thread.start();
                finish();
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK: {
                if (fromwhere == 1) {
                    startActivity(new Intent(NewGuanZhuActivity.this, MainFragmentActivity.class));
                    finish();
                } else {
                    finish();
                }
            }

        }
        return super.onKeyDown(keyCode, event);
    }


    class GetUserNameThread extends Thread {
        @Override
        public void run() {
            if (fSportsApp.getSessionId() == null || "".equals(fSportsApp.getSessionId()))
                return;
            try {
                UserDetail detail = ApiJsonParser.refreshRank(fSportsApp
                        .getSessionId());
                if (detail != null) {
                    fSportsApp.setSportUser(detail);
                }
            } catch (ApiNetException e) {
                e.printStackTrace();
                SportsApp.eMsg = Message.obtain(mExceptionHandler,
                        SportsExceptionHandler.NET_ERROR);
                SportsApp.eMsg.sendToTarget();
            } catch (ApiSessionOutException e) {
                e.printStackTrace();
                SportsApp.eMsg = Message.obtain(mExceptionHandler,
                        SportsExceptionHandler.SESSION_OUT);
                SportsApp.eMsg.sendToTarget();

            }
        }
    }


}
