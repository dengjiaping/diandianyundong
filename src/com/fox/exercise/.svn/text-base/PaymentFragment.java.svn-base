package com.fox.exercise;

import cn.ingenic.indroidsync.SportsApp;

import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class PaymentFragment extends Fragment {

    private int activityId;
    private String backString;
    private Boolean isFirst = true;
    public Dialog mLoadProgressDialog = null;

    @SuppressLint("ValidFragment")
    private TextView pay;

    public PaymentFragment() {
        super();
    }

    public void setActivityId(int activityId){
        this.activityId = activityId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.paymentfragment, null);
        pay = (TextView) view.findViewById(R.id.pay);

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && isFirst) {
            waitShow();
            GetPayInfoData();
            isFirst = false;
        }
    }

    /**
     * 获取支付详情接口
     */
    private void GetPayInfoData() {

        if (SportsUtilities.checkConnection(getActivity())) {
            waitShow();
            new GetPayInfoTask().execute();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.acess_server_error), 1).show();
        }

    }

    private class GetPayInfoTask extends AsyncTask<Void, Void, String> {

        String payInfoString = null;

        @Override
        protected String doInBackground(Void... arg0) {
            try {
                payInfoString = ApiJsonParser.getPayInfo(SportsApp.getInstance().getSessionId(), activityId);
            } catch (ApiNetException e) {
                e.printStackTrace();
            } catch (ApiSessionOutException e) {
                e.printStackTrace();
            }
            return payInfoString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pay.setText(payInfoString);
            waitCloset();
        }
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
