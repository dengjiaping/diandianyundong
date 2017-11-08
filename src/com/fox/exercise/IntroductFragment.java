package com.fox.exercise;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.ingenic.indroidsync.SportsApp;

import com.fox.exercise.api.entity.ActionInfo;

public class IntroductFragment extends Fragment {
    private TextView action_time, action_rules;
    private ActionInfo introductActionInfo;
    private static final String TAG = "IntroductFragment";
    public Dialog mLoadProgressDialog = null;
    private ImageView activity_detail_content;
    private ImageDownloader mDownloader = null;
    Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            introductActionInfo = (ActionInfo) msg.obj;
            System.out.print("*************time" + introductActionInfo.getEnd_time());
            action_time.setText(introductActionInfo.getActivity_time());
            action_rules.setText(introductActionInfo.getContent());
            mDownloader.setType(ImageDownloader.ACTIVITYICON);
            mDownloader.download(introductActionInfo.getContent(), activity_detail_content, null);
            waitCloset();
        }

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SportsApp.getInstance().setmyHandler(myHandler);
        mDownloader = new ImageDownloader(getActivity());
        waitShow();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.introductfragment, null);
        action_time = (TextView) view.findViewById(R.id.action_time);
        action_rules = (TextView) view.findViewById(R.id.action_rules);
        activity_detail_content = (ImageView) view.findViewById(R.id.activity_detail_content);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        //introductActionInfo=(ActionInfo) introductBundle.getSerializable("IntroductBundle");


    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        SportsApp.getInstance().setmyHandler(null);
    }

    /**
     * 开启对话框
     */
    public void waitShow() {
        if (mLoadProgressDialog == null) {
            mLoadProgressDialog = new Dialog(getActivity(), R.style.sports_dialog);
            LayoutInflater mInflater = getActivity().getLayoutInflater();
            View v1 = mInflater.inflate(R.layout.bestgirl_progressdialog, null);
            TextView message = (TextView) v1.findViewById(R.id.message);
            message.setText(R.string.bestgirl_wait);
            v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
            mLoadProgressDialog.setContentView(v1);
            mLoadProgressDialog.setCanceledOnTouchOutside(false);
        }
        if (mLoadProgressDialog != null)
            if (!mLoadProgressDialog.isShowing() && !getActivity().isFinishing())
                mLoadProgressDialog.show();
    }


    public void waitCloset() {
        if (mLoadProgressDialog != null)
            if (mLoadProgressDialog.isShowing())
                mLoadProgressDialog.dismiss();
    }

}
