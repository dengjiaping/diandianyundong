package com.fox.exercise.widght;

import com.fox.exercise.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Scroller;

@SuppressLint("WrongCall")
public class MutilTouchScrollLayout extends ViewGroup {

    private static final String TAG = "MutilTouchScrollLayout";
    private Scroller mScroller;

    private int mCurScreen;
    private int mDefaultScreen = 0;
    private static final int TOUCH_STATE_REST = 0;
    private static final int TOUCH_STATE_SCROLLING = 1;
    private int mTouchState = TOUCH_STATE_REST;
    private int mTouchSlop;
    private float mLastMotionX;
    private float mLastMotionY;
    private static final int SNAP_VELOCITY = 600;
    private OnViewChangeListener mOnViewChangeListener;
    private GestureDetector mListener;
    private Context context;
    private MutilTouchImageView childView;
    public boolean isSingleTouch;
    private VelocityTracker mVelocityTracker;

    public MutilTouchScrollLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }

    public MutilTouchScrollLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        this.context = context;
        mScroller = new Scroller(context);
        mListener = new GestureDetector(new MyGestureListener());
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        mCurScreen = mDefaultScreen;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // TODO Auto-generated method stub
        Log.e(TAG, "onLayout");
        if (changed) {
            int childLeft = 0;
            final int childCount = getChildCount();

            for (int i = 0; i < childCount; i++) {
                final View childView = getChildAt(i);
                if (childView.getVisibility() != View.GONE) {
                    final int childWidth = childView.getMeasuredWidth();
                    childView.layout(childLeft, 0, childLeft + childWidth, childView.getMeasuredHeight());
                    childLeft += childWidth;
                }
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.e(TAG, "onMeasure");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int width = MeasureSpec.getSize(widthMeasureSpec);

        // The children are given the same width and height as the scrollLayout
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
        }
        // Log.e(TAG, "moving to screen "+mCurScreen);
        scrollTo(mCurScreen * width, 0);
    }

    /**
     * According to the position of current layout scroll to the destination
     * page.
     */
    public void snapToDestination() {
        final int screenWidth = getWidth();
        final int destScreen = (getScrollX() + screenWidth * 3 / 4) / screenWidth;
        snapToScreen(destScreen);
    }

    public void snapToScreen(int whichScreen) {
        // get the valid layout page
        whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
        if (getScrollX() != (whichScreen * getWidth())) {

            final int delta = whichScreen * getWidth() - getScrollX();
            mScroller.startScroll(getScrollX(), 0, delta, 0, Math.abs(delta) * 2);
            mCurScreen = whichScreen;
            View layout = (View) getChildAt(mCurScreen);
            childView = (MutilTouchImageView) layout.findViewById(R.id.item_img);
            invalidate(); // Redraw the layout
            if (mOnViewChangeListener != null) {
                mOnViewChangeListener.OnViewChange(mCurScreen, childView);
            }
        }
    }

    public void setToScreen(int whichScreen) {
        whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
        mCurScreen = whichScreen;
        View layout = (View) getChildAt(mCurScreen);
        childView = (MutilTouchImageView) layout.findViewById(R.id.item_img);
        scrollTo(whichScreen * getWidth(), 0);
    }

    public int getCurScreen() {
        return mCurScreen;
    }

    @Override
    public void computeScroll() {
        // TODO Auto-generated method stub
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    private class MyGestureListener extends
            GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            if (e2.getPointerCount() == 2) {// 2�㴥��
                childView.scaleWithFinger(e2);// ���ÿ���ͼƬ��С�ķ���
                isSingleTouch = false;
                return true;
            } else if (e2.getPointerCount() == 1) {// ���㴥��
                isSingleTouch = true;
                return false;
            }
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            ((Activity) context).finish();
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            // Switch between the original scale and 3x scale.
            if (childView.getScale() > 2F) {
                childView.zoomTo(1f);
            } else {
                childView.zoomToPoint(3f, e.getX(), e.getY());
            }
            return true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if (mListener.onTouchEvent(event)) {
            return true;
        }
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        final int action = event.getAction();
        final float x = event.getX();
        final float y = event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                mLastMotionX = x;
                mLastMotionY = y;
//			Log.i("ACTION_DOWN", "x:" + x);
                break;

            case MotionEvent.ACTION_MOVE:
                if (isSingleTouch) {
                    int deltaX = (int) (mLastMotionX - x);
                    int deltaY = (int) (mLastMotionY - y);
                    mLastMotionX = x;
                    mLastMotionY = y;
                    if (!childView.isScrollOver(deltaX)) {
                        childView.postTranslateCenter(-deltaX, -deltaY);
                    } else
                        scrollBy((int) deltaX, 0);
                }
                break;

            case MotionEvent.ACTION_UP:
                childView.mBeforeLenght = -1;
                if (isSingleTouch) {
                    final VelocityTracker velocityTracker = mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(1000);
                    int velocityX = (int) velocityTracker.getXVelocity();
//			Log.e(TAG, "velocityX:" + velocityX);
                    if (velocityX > SNAP_VELOCITY && mCurScreen > 0) {
                        // Fling enough to move left
                        Log.e(TAG, "snap left");
                        snapToScreen(mCurScreen - 1);
                    } else if (velocityX < -SNAP_VELOCITY && mCurScreen < getChildCount() - 1) {
                        // Fling enough to move right
                        Log.e(TAG, "snap right");
                        snapToScreen(mCurScreen + 1);
                    } else {
                        snapToDestination();
                    }
                }
                if (childView.getScale() < 1F)
                    childView.zoomTo(1f);
                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                // }
                mTouchState = TOUCH_STATE_REST;
                break;
            case MotionEvent.ACTION_CANCEL:
                mTouchState = TOUCH_STATE_REST;
                break;
        }
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        Log.e(TAG, "onInterceptTouchEvent-slop:" + mTouchSlop);
        final int action = ev.getAction();
        if ((action == MotionEvent.ACTION_MOVE) && (mTouchState != TOUCH_STATE_REST)) {
            return true;
        }
        final float x = ev.getX();
        final float y = ev.getY();

        switch (action) {
            case MotionEvent.ACTION_MOVE:
                final int xDiff = (int) Math.abs(mLastMotionX - x);
                if (xDiff > mTouchSlop) {
                    mTouchState = TOUCH_STATE_SCROLLING;
                }
                break;

            case MotionEvent.ACTION_DOWN:
                mLastMotionX = x;
                mLastMotionY = y;
                mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST : TOUCH_STATE_SCROLLING;
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mTouchState = TOUCH_STATE_REST;
                break;
        }
        return mTouchState != TOUCH_STATE_REST;
    }

    @Override
    public void removeViewAt(int index) {
        super.removeViewAt(index);
        onLayout(true, 0, 0, 0, 0);
    }

    public void SetOnViewChangeListener(OnViewChangeListener listener) {
        mOnViewChangeListener = listener;
    }

    public interface OnViewChangeListener {
        public void OnViewChange(int view, MutilTouchImageView childView);
    }
}
