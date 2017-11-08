package com.fox.exercise;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import cn.ingenic.indroidsync.SportsApp;

import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.api.entity.ApplyList;
import com.fox.exercise.view.PullToRefreshGridView;
import com.fox.exercise.view.PullToRefreshBase.OnRefreshListener;

/**
 * @author dong.qu
 */
public class RegistratFragment extends Fragment {

    private PullToRefreshGridView registratRefreshGrid;
    private GridView registratGridView;
    private int actionId;
    private static final String TAG = "RegistratFragment";
    private boolean isFirst = true;
    private List<ApplyList> allApplyLists = new ArrayList<ApplyList>();
    private int times = 0;
    RegistratFragmentAdpter registratAdapter = null;
    public Dialog mLoadProgressDialog = null;

    public RegistratFragment() {
        super();
    }

    public void setActionId(int actionId){
        this.actionId = actionId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allApplyLists.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.registratfragment, null);
        registratRefreshGrid = (PullToRefreshGridView) view.findViewById(R.id.registrat_refresh_grid);
        registratGridView = registratRefreshGrid.getRefreshableView();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && isFirst) {
            waitShow();
            GetApplyListData();
            registratRefreshGrid.setOnRefreshListener(new OnRefreshListener() {

                @Override
                public void onRefresh(int pullDirection) {
                    switch (pullDirection) {
                        case FansAndNear.MODE_DEFAULT_LOAD:
                            times++;
                            GetApplyListData();
                            break;
                        case FansAndNear.MODE_PULL_DOWN_TO_REFRESH:
                            allApplyLists.clear();
                            times = 0;
                            GetApplyListData();
                            break;
                    }
                }
            });
            isFirst = false;
        }


    }

    @Override
    public void onStart() {
        super.onStart();
        registratGridView.setOnItemClickListener(new myGridviewClickListenter());
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    class myGridviewClickListenter implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

            //arg0.getItemAtPosition(position);
            //Toast.makeText(getActivity(), "点击"+position, 1).show();
        }
    }

    /**
     * 获取报名列表
     */
    private void GetApplyListData() {

        if (SportsUtilities.checkConnection(getActivity())) {
            waitShow();
            new GetApplyListTask().execute();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.acess_server_error), Toast.LENGTH_SHORT).show();
        }

    }

    private class GetApplyListTask extends AsyncTask<Void, Void, List<ApplyList>> {

        @Override
        protected List<ApplyList> doInBackground(Void... param) {

            List<ApplyList> applyLists = null;
            try {
                applyLists = ApiJsonParser.getApplyList(SportsApp.getInstance().getSessionId(), actionId, times);
            } catch (ApiNetException e) {
                e.printStackTrace();
            } catch (ApiSessionOutException e) {
                e.printStackTrace();
            }
            return applyLists;
        }

        @Override
        protected void onPostExecute(List<ApplyList> result) {
            super.onPostExecute(result);
            if (result == null)
                return;
            if (result.size() > 0) {
                for (ApplyList applyList : result) {
                    allApplyLists.add(applyList);
                }
                if (registratAdapter == null) {
                    registratAdapter = new RegistratFragmentAdpter(allApplyLists, getActivity());
                    registratGridView.setAdapter(registratAdapter);
                } else {
                    registratAdapter.notifyDataSetChanged();
                    registratRefreshGrid.onRefreshComplete();
                }
            } else if (result.size() == 0) {
                registratRefreshGrid.onRefreshComplete();
            }
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
