package com.fox.exercise;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Handler;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
/*import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;*/



import java.util.ArrayList;
import java.util.List;

import cn.ingenic.indroidsync.SportsApp;

import com.fox.exercise.api.entity.UserDetail;
import com.fox.exercise.bitmap.util.ImageResizer;
import com.fox.exercise.pedometer.ImageWorkManager;
import com.fox.exercise.util.RoundedImage;


public class ResideMenu extends FrameLayout {

    public static final int DIRECTION_LEFT = 0;
    //public  static final int DIRECTION_RIGHT = 1;
    private static final int PRESSED_MOVE_HORIZANTAL = 2;
    private static final int PRESSED_DOWN = 3;
    private static final int PRESSED_DONE = 4;
    private static final int PRESSED_MOVE_VERTICAL = 5;

    // private ImageView imageViewShadow;
    private ImageView imageViewBackground;
    //左侧菜单列表
    private LinearLayout layoutLeftMenu;
    //菜单列表被设置可伸缩
    private ScrollView scrollLayout;

    private LinearLayout linearLeftMenu;
    private LinearLayout scrollViewMenu;
    /**
     * the activity that view attach to
     */
    private Activity activity;
    /**
     * the decorview of the activity
     */
    private ViewGroup viewDecor;
    /**
     * the viewgroup of the activity
     */
    private TouchDisableView viewActivity;
    /**
     * the flag of menu open status
     */
    private boolean isOpened;
    private float shadowAdjustScaleX;
    private float shadowAdjustScaleY;
    /**
     * the view which don't want to intercept touch event
     */
    private List<View> ignoredViews;
    private List<ResideMenuItem> leftMenuItems;
    private DisplayMetrics displayMetrics = new DisplayMetrics();
    private float lastRawX;
    private boolean isInIgnoredView = false;
    private int scaleDirection = DIRECTION_LEFT;
    private int pressedState = PRESSED_DOWN;
    private List<Integer> disabledSwipeDirection = new ArrayList<Integer>();
    private float mScaleValue = 0.5f;
    private SportsApp mSportsApp;
    private RoundedImage userImage;
    private TextView nameText;
    private TextView money_rule;
    private TextView sports_money;
    private TextView photo_msg_count;
    private ImageView user_sex_icon;

    public ResideMenu(Context context) {
        super(context);
        initViews(context);
    }

    LinearLayout ssLayout;
    LinearLayout ruleLayout;
    private MainFragmentActivity mainFragmentActivity = null;

    private void initViews(Context context) {
        mainFragmentActivity = (MainFragmentActivity) context;
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.residemenu, this);
        mSportsApp = (SportsApp) context.getApplicationContext();
        UserDetail detail = SportsApp.getInstance().getSportUser();
        linearLeftMenu = (LinearLayout) findViewById(R.id.sv_left_menu);
        //imageViewShadow = (ImageView) findViewById(R.id.iv_shadow);
        layoutLeftMenu = (LinearLayout) findViewById(R.id.layout_left_menu);
        imageViewBackground = (ImageView) findViewById(R.id.iv_background);
        scrollLayout = (ScrollView) findViewById(R.id.sv_left_menu1);
        //设置身份信息
        ssLayout = (LinearLayout) findViewById(R.id.slim_settings_one);
        photo_msg_count = (TextView) findViewById(R.id.photo_msg_count);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) ssLayout.getLayoutParams();
        lp.width = (int) (SportsApp.ScreenWidth * 0.8);
        ssLayout.setLayoutParams(lp);
        nameText = (TextView) findViewById(R.id.user_name2);
        user_sex_icon = (ImageView) findViewById(R.id.user_sex_icon);
        sports_money = (TextView) findViewById(R.id.sports_money);
        userImage = (RoundedImage) findViewById(
                R.id.cover_user_photo2);
        money_rule = (TextView) findViewById(R.id.sports_money_rule);
//		money_rule.setText(Html
//				.fromHtml("<u>"
//						+ getResources().getString(R.string.sports_money_rule)
//						+ "</u>"));
        ruleLayout = (LinearLayout) findViewById(R.id.rule_layout);

    }

    public LinearLayout getruleLinearLayout() {
        return ruleLayout;
    }

    public TextView getphotomsgcount() {
        return photo_msg_count;
    }

    public TextView getNameTextView() {
        return nameText;

    }

    public ImageView getUserSexView() {
        return user_sex_icon;
    }

    public TextView getMoneyTextView() {
        return sports_money;
    }

    public TextView getRuleTextView() {
        return money_rule;
    }

    public RoundedImage getUserImage() {
        return userImage;
    }

    public LinearLayout getPhotoView() {
        return ssLayout;
    }

    /**
     * use the method to set up the activity which residemenu need to show;
     *
     * @param activity
     * @param fl
     */
    public void attachToActivity(Activity activity) {
        initValue(activity);
        setShadowAdjustScaleXByOrientation();
        viewDecor.addView(this, 0);
        setViewPadding();

    }

    private void initValue(Activity activity) {
        this.activity = activity;
        leftMenuItems = new ArrayList<ResideMenuItem>();
        //  rightMenuItems  = new ArrayList<ResideMenuItem>();
        ignoredViews = new ArrayList<View>();
        viewDecor = (ViewGroup) activity.getWindow().getDecorView();
        viewActivity = new TouchDisableView(this.activity);
        View mContent = viewDecor.getChildAt(0);
        viewDecor.removeViewAt(0);
        viewActivity.setContent(mContent);
        addView(viewActivity);

        ViewGroup parent = (ViewGroup) linearLeftMenu.getParent();
        // parent.removeView(scrollViewLeftMenu);
        // parent.removeView(scrollViewRightMenu);

    }

    private void setShadowAdjustScaleXByOrientation() {
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            shadowAdjustScaleX = 0.034f;
            shadowAdjustScaleY = 0.12f;
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            shadowAdjustScaleX = 0.06f;
            shadowAdjustScaleY = 0.07f;
        }
    }

    /**
     * set the menu background picture;
     *
     * @param imageResrouce
     */
    public void setBackground(int imageResrouce) {
        imageViewBackground.setImageResource(imageResrouce);
    }

    /**
     * the visiblity of shadow under the activity view;
     *
     * @param isVisible
     */
   /* public void setShadowVisible(boolean isVisible){
        if (isVisible)
            imageViewShadow.setImageResource(R.drawable.shadow);
        else
            imageViewShadow.setImageBitmap(null);
    }*/
    public void addMenuItem(ResideMenuItem menuItem) {
        this.leftMenuItems.add(menuItem);
        layoutLeftMenu.addView(menuItem);
    }


    public void setMenuItems(List<ResideMenuItem> menuItems) {
        this.leftMenuItems = menuItems;
        rebuildMenu();
    }

    private void rebuildMenu() {
        layoutLeftMenu.removeAllViews();
        // layoutRightMenu.removeAllViews();
        for (int i = 0; i < leftMenuItems.size(); i++)
            layoutLeftMenu.addView(leftMenuItems.get(i), i);
       /* for(int i = 0; i < rightMenuItems.size() ; i ++)
            layoutRightMenu.addView(rightMenuItems.get(i), i);*/
    }

    /**
     * get the left menu items;
     *
     * @return
     */


    public List<ResideMenuItem> getMenuItems() {
        return leftMenuItems;
    }

    /**
     * if you need to do something on the action of closing or opening
     * menu, set the listener here.
     *
     * @return
     */
    /*public void setMenuListener(OnMenuListener menuListener) {
        this.menuListener = menuListener;
    }


    public OnMenuListener getMenuListener() {
        return menuListener;
    }*/

    /**
     * we need the call the method before the menu show, because the
     * padding of activity can't get at the moment of onCreateView();
     */
    private void setViewPadding() {
        this.setPadding(viewActivity.getPaddingLeft(),
                viewActivity.getPaddingTop(),
                viewActivity.getPaddingRight(),
                viewActivity.getPaddingBottom());
    }

    /**
     * show the reside menu;
     */
    public void openMenu() {
        setScaleDirection();
        isOpened = true;
        AnimatorSet scaleDown_activity = buildScaleDownAnimation(viewActivity, 0.8f, 1.0f);      
       /* AnimatorSet scaleDown_shadow = buildScaleDownAnimation(imageViewShadow,
                mScaleValue + shadowAdjustScaleX,mScaleValue + shadowAdjustScaleY);*/
        AnimatorSet alpha_menu = buildMenuAnimation(scrollViewMenu, 1.0f, 1.5f - mScaleValue, 1.5f - mScaleValue);
        scaleDown_activity.addListener(animationListener);
        /*scaleDown_shadow.addListener(animationListener);
        scaleDown_activity.playTogether(scaleDown_shadow);*/
        scaleDown_activity.playTogether(alpha_menu);
        scaleDown_activity.start();
    }

    /**
     * close the reslide menu;
     */
    public void closeMenu() {

        isOpened = false;
        AnimatorSet scaleUp_activity = buildScaleUpAnimation(viewActivity, 1.0f, 1.0f);
        // AnimatorSet scaleUp_shadow = buildScaleUpAnimation(imageViewShadow, 1.0f, 1.0f);
        AnimatorSet alpha_menu = buildMenuAnimation(scrollViewMenu, 0.0f, 0.0f, 0.0f);
        scaleUp_activity.addListener(animationListener);
        //scaleUp_activity.playTogether(scaleUp_shadow);
        scaleUp_activity.playTogether(alpha_menu);
        if (scaleUp_activity != null) {
            scaleUp_activity.start();
        }
    }

   /* @Deprecated
    public void setDirectionDisable(int direction){
        disabledSwipeDirection.add(direction);
    }*/

   /* public void setSwipeDirectionDisable(int direction){
        disabledSwipeDirection.add(direction);
    }*/

    private boolean isInDisableDirection(int direction) {
        return disabledSwipeDirection.contains(direction);
    }

    private void setScaleDirection() {
        // private void setScaleDirection(int direction)
        int screenWidth = getScreenWidth();
        float pivotX;
        float pivotY = getScreenHeight() * 0.5f;
        scrollViewMenu = linearLeftMenu;
        pivotX = screenWidth * 3.7f;

        scrollLayout.setLayoutParams(new LinearLayout.LayoutParams((int) (screenWidth * 0.82), LayoutParams.WRAP_CONTENT));

        scrollViewMenu.setPivotX(0);
        scrollViewMenu.setPivotY(pivotY);
        viewActivity.setPivotX(pivotX);
        viewActivity.setPivotY(pivotY);
       /* imageViewShadow.setPivotX(pivotX);
        imageViewShadow.setPivotY(pivotY);*/

    }

    /**
     * return the flag of menu status;
     *
     * @return
     */
    public boolean isOpened() {
        return isOpened;
    }

    public void setIsopned(boolean isOpene) {
        this.isOpened = isOpene;
    }

    private OnClickListener viewActivityOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isOpened()) closeMenu();
        }
    };

    private Animator.AnimatorListener animationListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            if (isOpened()) {
                // showScrollViewMenu();
                /*if (menuListener != null)
                    menuListener.openMenu();*/
            }
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            // reset the view;
            if (isOpened()) {
                viewActivity.setTouchDisable(true);
                viewActivity.setOnClickListener(viewActivityOnClickListener);
            } else {
                viewActivity.setTouchDisable(false);
                viewActivity.setOnClickListener(null);
                // hideScrollViewMenu();
                /*if (menuListener != null)
                    menuListener.closeMenu();*/
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    /**
     * a helper method to build scale down animation;
     *
     * @param target
     * @param targetScaleX
     * @param targetScaleY
     * @return
     */
    private AnimatorSet buildScaleDownAnimation(View target, float targetScaleX, float targetScaleY) {

        AnimatorSet scaleDown = new AnimatorSet();
        scaleDown.playTogether(
                ObjectAnimator.ofFloat(target, "scaleX", targetScaleX),
                ObjectAnimator.ofFloat(target, "scaleY", targetScaleY)
        );

        scaleDown.setInterpolator(AnimationUtils.loadInterpolator(activity,
                android.R.anim.decelerate_interpolator));
        scaleDown.setDuration(250);
        return scaleDown;
    }

    /**
     * a helper method to build scale up animation;
     *
     * @param target
     * @param targetScaleX
     * @param targetScaleY
     * @return
     */
    private AnimatorSet buildScaleUpAnimation(View target, float targetScaleX, float targetScaleY) {

        AnimatorSet scaleUp = new AnimatorSet();
        scaleUp.playTogether(
                ObjectAnimator.ofFloat(target, "scaleX", targetScaleX),
                ObjectAnimator.ofFloat(target, "scaleY", targetScaleY)
        );

        scaleUp.setDuration(250);
        return scaleUp;
    }

    private AnimatorSet buildMenuAnimation(View target, float alpha, float targetScaleX, float targetScaleY) {

        AnimatorSet alphaAnimation = new AnimatorSet();
        alphaAnimation.playTogether(
                ObjectAnimator.ofFloat(target, "alpha", alpha),
                ObjectAnimator.ofFloat(target, "scaleX", targetScaleX),
                ObjectAnimator.ofFloat(target, "scaleY", targetScaleY)
        );

        alphaAnimation.setDuration(250);
        return alphaAnimation;
    }

    /**
     * if there ware some view you don't want reside menu
     * to intercept their touch event,you can use the method
     * to set.
     *
     * @param v
     */
    public void addIgnoredView(View v) {
        ignoredViews.add(v);
    }

    /**
     * remove the view from ignored view list;
     *
     * @param v
     */
    public void removeIgnoredView(View v) {
        ignoredViews.remove(v);
    }

    /**
     * clear the ignored view list;
     */
    public void clearIgnoredViewList() {
        ignoredViews.clear();
    }

    /**
     * if the motion evnent was relative to the view
     * which in ignored view list,return true;
     *
     * @param ev
     * @return
     */
//    private boolean isInIgnoredView(MotionEvent ev) {
//        Rect rect = new Rect();
//        for (View v : ignoredViews) {
//            v.getGlobalVisibleRect(rect);
//            if (rect.contains((int) ev.getX(), (int) ev.getY()))
//                return true;
//        }
//        return false;
//    }
    private void setScaleDirectionByRawX(float currentRawX) {
        if (currentRawX > lastRawX) {
            setScaleDirection();
        }
    }

    private float getTargetScale(float currentRawX) {
        float scaleFloatX = ((currentRawX - lastRawX) / getScreenWidth()) * 0.75f;
        float targetScale = viewActivity.getScaleX() - scaleFloatX;
        targetScale = targetScale > 1.0f ? 1.0f : targetScale;
        targetScale = targetScale < 0.8f ? 0.8f : targetScale;
        return targetScale;
    }

    private float lastActionDownX, lastActionDownY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float currentActivityScaleX = viewActivity.getScaleX();//ViewHelper.getScaleX(viewActivity);
        if (currentActivityScaleX == 1.0f)
            setScaleDirectionByRawX(ev.getRawX());
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastActionDownX = ev.getX();
                lastActionDownY = ev.getY();
//              isInIgnoredView = isInIgnoredView(ev) && !isOpened();//�Ƚ���Ҫ��һ���ж� 
                if (mainFragmentActivity.isIndexTouch == 1) {
//                    isInIgnoredView = false;//原来是可以划开侧边来的（首页）
                    isInIgnoredView = true;
                } else {
                    isInIgnoredView = true;
                }
                pressedState = PRESSED_DOWN;
                break;

            case MotionEvent.ACTION_MOVE:
                if (isInIgnoredView || isInDisableDirection(scaleDirection))
                    break;

                if (pressedState != PRESSED_DOWN &&
                        pressedState != PRESSED_MOVE_HORIZANTAL)
                    break;

                int xOffset = (int) (ev.getX() - lastActionDownX);
                int yOffset = (int) (ev.getY() - lastActionDownY);

                if (pressedState == PRESSED_DOWN) {
                    if (yOffset > 25 || yOffset < -25) {
                        pressedState = PRESSED_MOVE_VERTICAL;
                        break;
                    }
                    if (xOffset < -50 || xOffset > 50) {
                        pressedState = PRESSED_MOVE_HORIZANTAL;
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                    }
                } else if (pressedState == PRESSED_MOVE_HORIZANTAL) {
                    Handler mainHandler = SportsApp.getInstance().getMainHandler();
                    if (mainHandler != null) {
                        mainHandler.sendMessage(mainHandler.obtainMessage(
                                MainFragmentActivity.HIDE_VIEW));
                    }
                    float targetScale = getTargetScale(ev.getRawX());
                    viewActivity.setScaleX(targetScale);
//                    viewActivity.setScaleY(targetScale);
                    viewActivity.setScaleY(1);
              
                   /* imageViewShadow.setScaleX(targetScale + shadowAdjustScaleX);
                    imageViewShadow.setScaleY(targetScale + shadowAdjustScaleX);*/


                    scrollViewMenu.setAlpha((1 - targetScale) * 5.0f);
                    scrollViewMenu.setPivotX(0);
                    scrollViewMenu.setPivotY(getScreenHeight() * 0.5f);
                    scrollViewMenu.setScaleX(1.7f - targetScale);
                    scrollViewMenu.setScaleY(1.7f - targetScale);
                    lastRawX = ev.getRawX();
                    return true;
                }

                break;

            case MotionEvent.ACTION_UP:

                if (isInIgnoredView) break;
                if (pressedState != PRESSED_MOVE_HORIZANTAL) break;

                pressedState = PRESSED_DONE;
                if (isOpened()) {
                    if (currentActivityScaleX > 0.88f)
                        closeMenu();
                    else
                        //openMenu(scaleDirection);
                        openMenu();
                } else {
                    if (currentActivityScaleX < 0.88f) {
                        //openMenu(scaleDirection);
                        openMenu();
                    } else {
                        closeMenu();
                    }
                }

                break;

        }
        lastRawX = ev.getRawX();
        //
        return super.dispatchTouchEvent(ev);
    }

    public int getScreenHeight() {
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public int getScreenWidth() {
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public void setScaleValue(float scaleValue) {
        this.mScaleValue = scaleValue;
    }

   /* public interface OnMenuListener{

        *//**
     * the method will call on the finished time of opening menu's animation.
     *//*
        public void openMenu();

        *//**
     * the method will call on the finished time of closing menu's animation  .
     *//*
        public void closeMenu();
    }*/
   /* private void showScrollViewMenu(){
    	if (scrollViewMenu != null && scrollViewMenu.getParent() == null){
          // addView(scrollViewMenu);
        }
    }*/
  
   /* private void hideScrollViewMenu(){
        if (scrollViewMenu != null && scrollViewMenu.getParent() != null){
          //  removeView(scrollViewMenu);
        }
    }*/
}
