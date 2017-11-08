package com.fox.exercise;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMapException;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.offlinemap.OfflineMapCity;
import com.amap.api.maps.offlinemap.OfflineMapManager;
import com.amap.api.maps.offlinemap.OfflineMapManager.OfflineMapDownloadListener;
import com.amap.api.maps.offlinemap.OfflineMapProvince;
import com.amap.api.maps.offlinemap.OfflineMapStatus;
import com.baidu.mapapi.map.MapView;
import com.fox.exercise.pedometer.MofflistGaodeUtil;
import com.fox.exercise.util.*;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.ingenic.indroidsync.SportsApp;



/**
 * amapsdk/afflineMap/data/vmap
 *
 * */

public class MofflineListGaodeView extends Fragment {

    private SportsApp mSportsApp;
    protected static final int VALUELISTGAODE = 0;
    protected static final int DELETECITY = 1;
    private MapView mapView;
    private OfflineMapManager amapManager = null;
    private LocalMapAdapter lAdapter = null;
    private ArrayList<OfflineMapCity> localMapList;
    private ArrayList<OfflineMapCity> arrayList;
    private ArrayList<OfflineMapProvince> localMapListpProvinces;
    private List<MofflistGaodeUtil> mList;
    private int completeCode;
    public boolean bool = true;
    private List<OfflineMapCity> xiazaiList = new ArrayList<OfflineMapCity>();//已下载城市列表
    private Handler refreshHandler;
    private String fileList [];
    private String File_Path ="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mSportsApp = (SportsApp) getActivity().getApplication();
        MapsInitializer.sdcardDir = OffLineMapUtils.getSdCacheDir(getActivity());
        File_Path = MapsInitializer.sdcardDir;
        getFileList();
        mSportsApp.setmofHandler(handler);
        mSportsApp.setDownloading(true);
        refreshHandler = mSportsApp.getNotifiRefreshHandler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return inflater.inflate(R.layout.moffline_gaode_mlistview, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        mapView = new MapView(getActivity());
        amapManager = new OfflineMapManager(getActivity(), new OfflineMapDownloadListener() {
            @Override
            public void onDownload(int arg0, int completeCode, String arg2) {
                // TODO Auto-generated method stub
                switch (arg0) {
                    case OfflineMapStatus.LOADING:
                        Log.i("", "来了来了completeCode" + completeCode);
                        for (int i = 0; i < mList.size(); i++) {
                            Log.i("", "completeCode gaga" + mList.get(i).getCityName() + arg2);
                            if (mList.get(i).getCityName().equals(arg2) && completeCode != 100) {
                                mList.get(i).setCompleteCodeStr("下载");
                                mList.get(i).setCompleteCode(completeCode);
                                updateView();
                            }
                        }
                        break;
                    case OfflineMapStatus.UNZIP:
                        for (int i = 0; i < mList.size(); i++) {
                            Log.i("", "completeCode gaga" + mList.get(i).getCityName() + arg2);
                            if (mList.get(i).getCityName().equals(arg2)) {
                                mList.get(i).setCompleteCodeStr("解压");
                                mList.get(i).setCompleteCode(completeCode);
                                updateView();
                            }
                        }
                        break;
                    case OfflineMapStatus.SUCCESS:
                        bool = true;
                        mSportsApp.setDownloading(true);
                        for (int i = 0; i < mList.size(); i++) {
                            if (mList.get(i).getCityName().equals(arg2)) {
                                mList.get(i).setCompleteCode(completeCode);
                                mList.get(i).setCompleteCodeStr("");
                                updateView();
                            }
                            Log.i("", "mList.get(i).getCompleteCode()" + mList.get(i).getCompleteCode() + mList.get(i).getState());
                            if (mList.get(i).getCompleteCode() == 0 && mList.get(i).getState() == 1) {

                                try {
                                    amapManager.downloadByProvinceName(mList.get(i).getCityName());
                                    break;
                                } catch (AMapException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            } else if (mList.get(i).getCompleteCode() == 0 && mList.get(i).getState() == 2) {
                                try {
                                    amapManager.downloadByCityName(mList.get(i).getCityName());
                                    break;
                                } catch (AMapException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onCheckUpdate(boolean arg0, String arg1) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onRemove(boolean arg0, String arg1, String arg2) {
                // TODO Auto-generated method stub

            }
        });
        init();
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
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

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case VALUELISTGAODE:
                    getFileList();
                    MofflistGaodeUtil moffUtil = (MofflistGaodeUtil) msg.obj;
                    String value = moffUtil.getCityName();
                    int id = moffUtil.getCompleteCode();
                    boolean b = false;
                    mSportsApp.setDownloading(false);
                    for (int i = 0; i < mList.size(); i++) {
                        if (mList.get(i).getCityName().equals(value)) {
                            b = true;
                            break;
                        }
                    }
                    if(compareFile(com.fox.exercise.util.Utils.converterToSpell(value))){
                        if (b==true){
                            mSportsApp.setDownloading(true);
                        }else {
                            MofflistGaodeUtil gaodeUtil = new MofflistGaodeUtil();
                            gaodeUtil.setCityName(value);
                            gaodeUtil.setCompleteCode(100);
                            gaodeUtil.setState(id);
                            gaodeUtil.setCompleteCodeStr("");
                            mList.add(gaodeUtil);
                            mSportsApp.setDownloading(true);
                            updateView();
                        }
                    }else {
                        if (bool == false) {
                            Toast.makeText(getActivity(), R.string.downloading,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            if (id == 2) {
                                try {
                                    amapManager.downloadByCityName(value);
                                } catch (AMapException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                    Log.e("离线地图下载", "离线地图下载抛出异常" + e.getErrorMessage());
                                    bool = true;
                                    mSportsApp.setDownloading(true);
                                }
                            } else {
                                try {
                                    amapManager.downloadByProvinceName(value);
                                } catch (AMapException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                    Log.e("离线地图下载", "离线地图下载抛出异常" + e.getErrorMessage());
                                    bool = true;
                                    mSportsApp.setDownloading(true);
                                }
                            }

                            if (b == false) {
                                MofflistGaodeUtil gaodeUtil1 = new MofflistGaodeUtil();
                                gaodeUtil1.setCityName(value);
                                gaodeUtil1.setCompleteCode(completeCode);
                                gaodeUtil1.setState(id);
                                gaodeUtil1.setCompleteCodeStr("");
                                mList.add(gaodeUtil1);
                                bool = false;
                                mSportsApp.setDownloading(false);
                            }
                            updateView();
                        }
                    }
                    break;
            }
        }
    };

    public void init() {
        if (localMapListpProvinces == null)
            localMapListpProvinces = new ArrayList<OfflineMapProvince>();
        localMapListpProvinces = amapManager.getDownloadOfflineMapProvinceList();
        localMapList = (ArrayList<OfflineMapCity>) amapManager.getDownloadOfflineMapCityList();
        if (localMapList == null) {
            localMapList = new ArrayList<OfflineMapCity>();
        }

        mList = new ArrayList<MofflistGaodeUtil>();
        for (int i = 0; i < localMapListpProvinces.size(); i++) {
            MofflistGaodeUtil gaodeUtil = new MofflistGaodeUtil();
            gaodeUtil.setCityName(localMapListpProvinces.get(i).getProvinceName());
            gaodeUtil.setCompleteCode(localMapListpProvinces.get(i).getcompleteCode());
            gaodeUtil.setCities(localMapListpProvinces.get(i).getCityList());
            gaodeUtil.setCompleteCodeStr("");
            gaodeUtil.setState(1);
            mList.add(gaodeUtil);
        }
        for (int i = 0; i < localMapList.size(); i++) {
            MofflistGaodeUtil gaodeUtil = new MofflistGaodeUtil();
            gaodeUtil.setCityName(localMapList.get(i).getCity());
            gaodeUtil.setCompleteCode(localMapList.get(i).getcompleteCode());
            gaodeUtil.setCompleteCodeStr("");
            gaodeUtil.setState(2);
            mList.add(gaodeUtil);
        }
        for (int j = 0;j < 4; j++) {
            for (int i = 1; i < mList.size(); i++) {
                if (mList.get(i).getCityName().equals(mList.get(0).getCityName())) {
                    mList.remove(0);
                }
            }
        }
        ListView localMapListView = (ListView) getActivity().findViewById(R.id.localmaplist);
        lAdapter = new LocalMapAdapter();
        localMapListView.setAdapter(lAdapter);
    }

    public class LocalMapAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mList.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return mList.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public View getView(final int item, View arg1, ViewGroup arg2) {
            // TODO Auto-generated method stub
            Log.i("", "lulu" + localMapListpProvinces.size() + item);
            View view = View.inflate(getActivity(), R.layout.load_offline, null);
            TextView ratio = (TextView) view.findViewById(R.id.ratio);
            TextView title = (TextView) view.findViewById(R.id.load_title);
            title.setText(mList.get(item).getCityName());
            ratio.setText(mList.get(item).getCompleteCodeStr() + mList.get(item).getCompleteCode() + "%");

            Button remove = (Button) view.findViewById(R.id.remove);
            remove.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    amapManager.remove(mList.get(item).getCityName());
                    mList.remove(item);
                    if (mList.size() == item)
                        bool = true;
                    mSportsApp.setDownloading(true);
                    updateView();
                }
            });
            return view;
        }

    }

    public void updateView() {
        Log.i("", "lalalalalala");
        if (lAdapter != null) {
            lAdapter.notifyDataSetChanged();
        }
        refreshHandler.sendMessage(new Message());
    }

    //		 private String getSdCacheDir(Context context) {
//				if (Environment.getExternalStorageState().equals(
//						Environment.MEDIA_MOUNTED)) {
//					java.io.File fExternalStorageDirectory = Environment
//							.getExternalStorageDirectory();
//					java.io.File autonaviDir = new java.io.File(
//							fExternalStorageDirectory, "amapsdk");
//					boolean result = false;
//					if (!autonaviDir.exists()) {
//						result = autonaviDir.mkdir();
//					}
//					java.io.File minimapDir = new java.io.File(autonaviDir,
//							"offlineMap");
//					if (!minimapDir.exists()) {
//						result = minimapDir.mkdir();
//					}
//					return minimapDir.toString() + "/";
//				} else {
//					return "";
//				}
//			}
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        MoffGaodeThred gaodeThred = new MoffGaodeThred();
        gaodeThred.start();
        if (mapView != null) {
            mapView.onDestroy();
        }
        if (amapManager != null) {
            amapManager.stop();
        }
        mSportsApp.setfHandler(null);
    }

    class MoffGaodeThred extends Thread {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            super.run();
            Message msg = null;
            localMapList = (ArrayList<OfflineMapCity>) amapManager.getDownloadingCityList();
            if (localMapList != null) {
                for (int i = 0; i < localMapList.size(); i++) {
                    final OfflineMapCity e = (OfflineMapCity) localMapList.get(i);
                    if (e.getcompleteCode() != 100) {
                        msg = Message.obtain(moffHandler, DELETECITY, e.getCity());
                        msg.sendToTarget();
                    }
                }
            }
            localMapListpProvinces = (ArrayList<OfflineMapProvince>) amapManager.getDownloadingProvinceList();
            if (localMapListpProvinces != null) {
                for (int i = 0; i < localMapListpProvinces.size(); i++) {
                    final OfflineMapProvince e = (OfflineMapProvince) localMapListpProvinces.get(i);
                    if (e.getcompleteCode() != 100) {
                        msg = Message.obtain(moffHandler, DELETECITY, e.getProvinceName());
                        msg.sendToTarget();
                    }
                }
            }
        }
    }

    Handler moffHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DELETECITY:
                    amapManager.remove(msg.toString());
                    break;

                default:
                    break;
            }
        }

        ;
    };



    /**
     *@method 比较文件是否存在
     *@author suhu
     *@time 2016/11/22 10:07
     *@param cityName
     *
     */
    private boolean compareFile(String cityName){
        for (String s : fileList) {
            if(cityName.startsWith(s.substring(0,s.length()-4))){
                return true;
            }
        }
        return false;
    }

    /**
     *@method 获得文件列表
     *@author suhu
     *@time 2016/11/22 10:08
     *@param
     *
     */
    private void getFileList(){
        File file = new File(File_Path,"/data/vmap/");
        if (!file.exists()){
            file.mkdir();
        }
        fileList = file.list();
    }





}
