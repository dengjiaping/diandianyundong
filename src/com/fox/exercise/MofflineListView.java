package com.fox.exercise;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import cn.ingenic.indroidsync.SportsApp;

//import com.baidu.platform.comapi.map.v;

public class MofflineListView extends Fragment {

    private SportsApp mSportsApp;

    private MKOfflineMap mofflineMap = new MKOfflineMap();
    private ArrayList<MKOLUpdateElement> localMapList = null;
    private LocalMapAdapter lAdapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mSportsApp = (SportsApp) getActivity().getApplication();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return inflater.inflate(R.layout.moffline_mlistview, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("MofflineListView");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MofflineListView");
    }

    public void init() {
        mofflineMap = new MKOfflineMap();
        mofflineMap.init(new MKOfflineMapListener() {
            @Override
            public void onGetOfflineMapState(int arg0, int arg1) {
                // TODO Auto-generated method stub
                MKOLUpdateElement update = mofflineMap.getUpdateInfo(arg1);
                // 处理下载进度更新提示
                if (update != null) {
                    updateView();
                                     /*if(update.ratio==100){
                                          Toast.makeText(getActivity(), "下载成功",
		    				 						Toast.LENGTH_SHORT).show();
		    			              }*/
                }
            }
        });
        localMapList = mofflineMap.getAllUpdateInfo();
        if (localMapList == null) {
            localMapList = new ArrayList<MKOLUpdateElement>();
        }
        ListView localMapListView = (ListView) getActivity().findViewById(R.id.localmaplist);
        lAdapter = new LocalMapAdapter();
        localMapListView.setAdapter(lAdapter);
    }

    public class LocalMapAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return localMapList.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return localMapList.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            // TODO Auto-generated method stub
            final MKOLUpdateElement e = (MKOLUpdateElement) getItem(arg0);
				/* if(e.ratio>100){
					 mofflineMap.start(arg0);
				 }*/
            View view = View.inflate(getActivity(), R.layout.load_offline, null);
            TextView ratio = (TextView) view.findViewById(R.id.ratio);
            TextView title = (TextView) view.findViewById(R.id.load_title);
            title.setText(e.cityName);
            ratio.setText(e.ratio + "%");
            Button remove = (Button) view.findViewById(R.id.remove);
            remove.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // MKOLUpdateElement currentE = (MKOLUpdateElement) getV(arg0.getp);
                    mofflineMap.remove(e.cityID);
                    updateView();
                }
            });
            return view;
        }

    }

    public void updateView() {
        localMapList = mofflineMap.getAllUpdateInfo();
        if (localMapList == null) {
            localMapList = new ArrayList<MKOLUpdateElement>();
        }
        if (lAdapter != null) {
            lAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        localMapList = mofflineMap.getAllUpdateInfo();
        if (localMapList != null) {
            for (int i = 0; i < localMapList.size(); i++) {
                final MKOLUpdateElement e = (MKOLUpdateElement) localMapList.get(i);
                if (e.ratio != 100) {
                    mofflineMap.remove(e.cityID);
                }
            }
        }
        if (mofflineMap != null)
            mofflineMap.destroy();
    }
}
