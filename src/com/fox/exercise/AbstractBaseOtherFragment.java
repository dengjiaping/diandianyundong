package com.fox.exercise;

import com.fox.exercise.bitmap.util.ImageResizer;
import com.fox.exercise.pedometer.ImageWorkManager;
import com.fox.exercise.util.RoundedImage;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.ingenic.indroidsync.SportsApp;

/**
 * 基础Fragement类
 */
public abstract class AbstractBaseOtherFragment extends Fragment {
    protected final String TAG = getClass().getSimpleName();
    public LinearLayout m_vContentView = null;
    public String title = "";
    private LinearLayout l_content;
    protected LinearLayout right_btn;
    protected LinearLayout top_title_layout;
    public View parentView;
    protected ImageButton iButton;
    public Dialog mLoadProgressDialog = null;
    protected LinearLayout left_ayout, title_newsorguanzhu_layout;
    public RoundedImage userPhoto;

    /**
     * 设置页面控件事件和状态
     */
    public abstract void beforeInitView(Bundle bundles);

    /**
     * 设置页面控件事件和状态
     */
    public abstract void setViewStatus(Bundle bundles);

    /**
     * == onResume()
     */
    public abstract void onPageResume();

    /**
     * == onPause()
     */
    public abstract void onPagePause();

    /**
     * == onDestroy()
     */
    public abstract void onPageDestroy();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeInitView(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // m_vContentView = (LinearLayout) inflater.inflate(
        // R.layout.base_fragment, null);
        m_vContentView = (LinearLayout) inflater.inflate(
                R.layout.base_fragment1, null);
        top_title_layout = (LinearLayout) m_vContentView
                .findViewById(R.id.top_title_layout);
        left_ayout = (LinearLayout) m_vContentView
                .findViewById(R.id.title_left_layout);
        OnClickListener cListener = new clickListener();
        left_ayout.setOnClickListener(cListener);
        RelativeLayout leftRelativeLayout = new RelativeLayout(getActivity());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
                android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
        leftRelativeLayout.setLayoutParams(params);
        left_ayout.addView(leftRelativeLayout);
        iButton = new ImageButton(getActivity());
        iButton.setBackgroundResource(R.drawable.title_left_new_icon);
        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(
                SportsApp.getInstance().dip2px(8),
                android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
        // RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(
        // android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
        // android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        lp1.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        iButton.setLayoutParams(lp1);
        userPhoto = new RoundedImage(getActivity());
        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(
                SportsApp.getInstance().dip2px(30), SportsApp.getInstance()
                .dip2px(30));
        lp2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        lp2.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        lp2.setMargins(SportsApp.getInstance().dip2px(10), 0, 0, 0);
        userPhoto.setLayoutParams(lp2);
        ImageWorkManager mImageWorkerMan_Icon = new ImageWorkManager(
                getActivity(), 0, 0);
        ImageResizer mImageWorker_Icon = mImageWorkerMan_Icon.getImageWorker();
        if (("man").equals(SportsApp.getInstance().getSportUser().getSex())) {
            userPhoto.setImageResource(R.drawable.sports_user_edit_portrait_male);
        } else {
            userPhoto.setImageResource(R.drawable.sports_user_edit_portrait);
        }

        mImageWorker_Icon.loadImage(SportsApp.getInstance().getSportUser()
                .getUimg(), userPhoto, null, null, false);

        leftRelativeLayout.addView(iButton);
        leftRelativeLayout.addView(userPhoto);
        // left_ayout.addView(iButton);
        iButton.setOnClickListener(cListener);
        TextView title_tv = (TextView) m_vContentView
                .findViewById(R.id.top_title);
        title_tv.setText(title);
        right_btn = (LinearLayout) m_vContentView
                .findViewById(R.id.title_right_btn);
        l_content = (LinearLayout) m_vContentView
                .findViewById(R.id.main_content);

        title_newsorguanzhu_layout = (LinearLayout) m_vContentView
                .findViewById(R.id.title_newsorguanzhu_layout);
        SportsApp.getInstance().setmBaseFragmentHandler(mBaseHandler);
        return m_vContentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setViewStatus(savedInstanceState);
    }

    class clickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            ResideMenu rm = ((MainFragmentActivity) getActivity())
                    .getResideMenu();
            if (rm.isOpened())
                rm.closeMenu();
            else
                rm.openMenu();
        }
    }

    public void setContentView(int layout) {
        parentView = LayoutInflater.from(getActivity()).inflate(layout, null);
        if (l_content != null) {
            l_content.removeAllViews();
            l_content.addView(parentView, new LayoutParams(
                    LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        }
    }

    // 设置进去view
    public void setContentViews(View view) {
        parentView = view;
        if (l_content != null) {
            l_content.removeAllViews();
            l_content.addView(parentView, new LayoutParams(
                    LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        }
    }

    public void showRightBtn(View right_view) {
        if (right_btn != null) {
            right_btn.removeAllViews();
            right_btn.addView(right_view);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        onPagePause();
        // MobclickAgent.onPause(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        nullViewDrawablesRecursive(m_vContentView);
        iButton = null;
        m_vContentView = null;
        onPageDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        onPageResume();
        // MobclickAgent.onResume(getActivity());
    }

    protected void nullViewDrawablesRecursive(View view) {
        if (view != null) {
            try {
                ViewGroup viewGroup = (ViewGroup) view;
                int childCount = viewGroup.getChildCount();
                for (int index = 0; index < childCount; index++) {
                    View child = viewGroup.getChildAt(index);
                    nullViewDrawablesRecursive(child);
                }
            } catch (Exception e) {
            }
            nullViewDrawable(view);
        }
    }

    protected void nullViewDrawable(View view) {
        try {
            view.setBackgroundDrawable(null);
            if (view instanceof ImageView) {
                ImageView imageView = (ImageView) view;
                imageView.setImageDrawable(null);
                imageView.setBackgroundDrawable(null);
            }
        } catch (Exception e) {
        }
    }

    /**
     * 开启对话框
     */
    public void waitShow() {
        if (mLoadProgressDialog == null) {
            mLoadProgressDialog = new Dialog(getActivity(),
                    R.style.sports_dialog);
            LayoutInflater mInflater = getActivity().getLayoutInflater();
            View v1 = mInflater.inflate(R.layout.bestgirl_progressdialog, null);
            TextView message = (TextView) v1.findViewById(R.id.message);
            message.setText(R.string.bestgirl_wait);
            v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
            mLoadProgressDialog.setContentView(v1);
            mLoadProgressDialog.setCanceledOnTouchOutside(false);
        }
        if (mLoadProgressDialog != null)
            if (!mLoadProgressDialog.isShowing()
                    && !getActivity().isFinishing())
                mLoadProgressDialog.show();
    }

    public void waitCloset() {
        if (mLoadProgressDialog != null)
            if (mLoadProgressDialog.isShowing())
                mLoadProgressDialog.dismiss();
    }

    protected Dialog onCreateDialog(int id, Bundle args) {
        // TODO Auto-generated method stub
        return null;
    }

    public void showNewsOrGuanzu() {
        if (title_newsorguanzhu_layout != null) {
            title_newsorguanzhu_layout.setVisibility(View.VISIBLE);
        }
    }

    public static final int REFRESHPHOTO = 121;
    private Handler mBaseHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESHPHOTO:
                    if (userPhoto != null) {
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
                                if (("man").equals(SportsApp.getInstance()
                                        .getSportUser().getSex())) {
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
                    }
                    break;

                default:
                    break;
            }
        }

        ;
    };

}
