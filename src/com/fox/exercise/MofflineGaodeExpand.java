package com.fox.exercise;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.MapView;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.offlinemap.OfflineMapCity;
import com.amap.api.maps.offlinemap.OfflineMapManager;
import com.amap.api.maps.offlinemap.OfflineMapManager.OfflineMapDownloadListener;
import com.amap.api.maps.offlinemap.OfflineMapProvince;
import com.fox.exercise.pedometer.MofflistGaodeUtil;
import com.fox.exercise.util.OffLineMapUtils;
import com.umeng.analytics.MobclickAgent;
import com.fox.exercise.util.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.ingenic.indroidsync.SportsApp;

public class MofflineGaodeExpand extends Fragment {
    //private Button localButton,cityButton;
    private SportsApp mSportsApp;

    private List<OfflineMapProvince> provinceList = new ArrayList<OfflineMapProvince>();// 保存一级目录的省直辖市
    private HashMap<Object, List<OfflineMapCity>> cityMap = new HashMap<Object, List<OfflineMapCity>>();// 保存二级目录的市
    private List<OfflineMapCity> xiazaiList = new ArrayList<OfflineMapCity>();//已下载城市列表
    private MapView mapView;
    private OfflineMapManager amapManager = null;
    private boolean isStart = false;
    private boolean[] isOpen;// 记录一级目录是否打开
    private String File_Path ="";
    private String fileList [];

    private int completeCode;
    private int groupPosition;// 记录一级目录的position
    private int childPosition;

    private onCheckedPageGaodeListener onCheckedPageListener;
    private MyExpandableListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mSportsApp = (SportsApp) getActivity().getApplication();
        MapsInitializer.sdcardDir = OffLineMapUtils.getSdCacheDir(getActivity());
        File_Path = MapsInitializer.sdcardDir;
        getFileList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        return inflater.inflate(R.layout.offlinemap_gaode, null);
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
        MobclickAgent.onPageStart("MofflineGaodeExpand");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MofflineGaodeExpand");
    }

    int i = 0;

    private void init() {
        // TODO Auto-generated method stub
        mapView = new MapView(getActivity());
        amapManager = new OfflineMapManager(getActivity(), new OfflineMapDownloadListener() {

            @Override
            public void onDownload(int status, int completeCode, String downName) {
                // TODO Auto-generated method stub

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
        final ExpandableListView expandableListView = (ExpandableListView) getActivity().findViewById(R.id.list);
        expandableListView.setGroupIndicator(null);
        provinceList = amapManager.getOfflineMapProvinceList();
        List<OfflineMapProvince> bigCityList = new ArrayList<OfflineMapProvince>();// 以省格式保存直辖市、港澳、全国概要图
        List<OfflineMapCity> cityList = new ArrayList<OfflineMapCity>();// 以市格式保存直辖市、港澳、全国概要图
        List<OfflineMapCity> gangaoList = new ArrayList<OfflineMapCity>();// 保存港澳城市
        List<OfflineMapCity> gaiyaotuList = new ArrayList<OfflineMapCity>();// 保存概要图
        List<OfflineMapCity> dangqianList = new ArrayList<OfflineMapCity>();// 保存当前城市

        xiazaiList = amapManager.getDownloadOfflineMapCityList();//获得已下载城市列表
        Log.i("gaoDeMap","我的下载列表："+xiazaiList.size()+"");

        for (int i = 0; i < provinceList.size(); i++) {
            OfflineMapProvince offlineMapProvince = provinceList.get(i);
            List<OfflineMapCity> city = new ArrayList<OfflineMapCity>();
            OfflineMapCity aMapCity = getCicy(offlineMapProvince);
            if (offlineMapProvince.getCityList().size() != 1) {
                for (OfflineMapCity city2 : offlineMapProvince.getCityList()) {
//					Log.v(TAG,"wmh city2.getCity is "+city2.getCity());
                    if (mSportsApp.curCity != null && city2.getCity().contains(mSportsApp.curCity)) {
                        dangqianList.add(aMapCity);
                    }
                }
                city.add(aMapCity);
                city.addAll(offlineMapProvince.getCityList());
            } else {
                cityList.add(aMapCity);
                bigCityList.add(offlineMapProvince);
            }
            cityMap.put(i + 4, city);
        }
        OfflineMapProvince title = new OfflineMapProvince();

        title.setProvinceName("当前城市");
        provinceList.add(0, title);
        title = new OfflineMapProvince();
        title.setProvinceName("概要图");
        provinceList.add(1, title);
        title = new OfflineMapProvince();
        title.setProvinceName("直辖市");
        provinceList.add(2, title);
        title = new OfflineMapProvince();
        title.setProvinceName("港澳");
        provinceList.add(3, title);
        provinceList.removeAll(bigCityList);

        for (OfflineMapProvince aMapProvince : bigCityList) {
            if (aMapProvince.getProvinceName().contains("香港")
                    || aMapProvince.getProvinceName().contains("澳门")) {
                gangaoList.add(getCicy(aMapProvince));
            } else if (aMapProvince.getProvinceName().contains("全国概要图")) {
                gaiyaotuList.add(getCicy(aMapProvince));
            } else if (mSportsApp.curCity != null
                    && aMapProvince.getProvinceName().contains(mSportsApp.curCity)) {
                dangqianList.add(getCicy(aMapProvince));
            }
        }
        try {
            cityList.remove(4);// 从List集合体中删除香港
            cityList.remove(4);// 从List集合体中删除澳门
            cityList.remove(4);// 从List集合体中删除全国概要图
        } catch (Throwable e) {
            e.printStackTrace();
        }
        cityMap.put(0, dangqianList);// 在HashMap中第0位置添加当前城市
        cityMap.put(1, gaiyaotuList);// 在HashMap中第0位置添加全国概要图
        cityMap.put(2, cityList);// 在HashMap中第1位置添加直辖市
        cityMap.put(3, gangaoList);// 在HashMap中第2位置添加港澳
        isOpen = new boolean[provinceList.size()];
        // 为列表绑定数据源
        adapter = new MyExpandableListAdapter();
        expandableListView.setAdapter(adapter);
        expandableListView.setGroupIndicator(null);
        expandableListView.setOnGroupExpandListener(new OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                isOpen[groupPosition] = true;
                for (int i = 0; i < provinceList.size(); i++) {
                    if (groupPosition != i) {
                        expandableListView.collapseGroup(i);
                    }
                }
            }
        });
        // 设置二级item点击的监听器
        expandableListView.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView arg0, View arg1, int groupPosition,
                                        int childPosition, long arg4) {
                // TODO Auto-generated method stub
                String nameValue;
                if (!mSportsApp.isOpenNetwork()) {
                    Toast.makeText(getActivity(), R.string.sports_comment_not_connected,
                            Toast.LENGTH_SHORT).show();
                } else if (groupPosition == 0 || groupPosition == 1
                        || groupPosition == 2 || groupPosition == 3) {
                    nameValue = cityMap.get(groupPosition).get(childPosition)
                            .getCity();
                    MobclickAgent.onEvent(getActivity(), "downloadgaode");
                    onCheckedPageListener.onCheckedPage();
                    Handler handler = mSportsApp.getmofHandler();
                    MofflistGaodeUtil gaodeUtil = new MofflistGaodeUtil();
                    gaodeUtil.setCityName(nameValue);
                    gaodeUtil.setCompleteCode(1);
                    handler.sendMessage(handler.obtainMessage(MofflineListGaodeView.VALUELISTGAODE, gaodeUtil));

                }
                // 下载各省的离线地图数据
                else {
                    // 下载各省列表中的省份离线地图数据
                    if (childPosition == 0) {
                        nameValue = provinceList.get(
                                groupPosition).getProvinceName();
                        MobclickAgent.onEvent(getActivity(), "downloadgaode");
                        onCheckedPageListener.onCheckedPage();
                        Handler handler = mSportsApp.getmofHandler();
                        MofflistGaodeUtil gaodeUtil = new MofflistGaodeUtil();
                        gaodeUtil.setCityName(nameValue);
                        gaodeUtil.setCompleteCode(1);
                        handler.sendMessage(handler.obtainMessage(MofflineListGaodeView.VALUELISTGAODE, gaodeUtil));
                    }
                    // 下载各省列表中的城市离线地图数据
                    else if (childPosition > 0) {
                        nameValue = cityMap.get(groupPosition).get(childPosition)
                                .getCity();
                        MobclickAgent.onEvent(getActivity(), "downloadgaode");
                        onCheckedPageListener.onCheckedPage();
                        Handler handler = mSportsApp.getmofHandler();
                        MofflistGaodeUtil gaodeUtil = new MofflistGaodeUtil();
                        gaodeUtil.setCityName(nameValue);
                        gaodeUtil.setCompleteCode(2);
                        handler.sendMessage(handler.obtainMessage(MofflineListGaodeView.VALUELISTGAODE, gaodeUtil));
                    }
                }
                return false;
            }
        });
    }

    public OfflineMapCity getCicy(OfflineMapProvince aMapProvince) {
        OfflineMapCity aMapCity = new OfflineMapCity();
        aMapCity.setCity(aMapProvince.getProvinceName());
        aMapCity.setSize(aMapProvince.getSize());
        aMapCity.setCompleteCode(aMapProvince.getcompleteCode());
        aMapCity.setState(aMapProvince.getState());
        aMapCity.setUrl(aMapProvince.getUrl());
        return aMapCity;
    }

    class MyExpandableListAdapter extends BaseExpandableListAdapter {
        Handler refreshHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                getFileList();
                notifyDataSetChanged();
                super.handleMessage(msg);
            }
        };

        public MyExpandableListAdapter(){
            mSportsApp.setNotifiRefreshHandler(refreshHandler);
        }

        @Override
        public int getGroupCount() {
            return provinceList.size();
        }

        /**
         * 获取一级标签内容
         */
        @Override
        public Object getGroup(int groupPosition) {
            return provinceList.get(groupPosition).getProvinceName();
        }

        /**
         * 获取一级标签的ID
         */
        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        /**
         * 获取一级标签下二级标签的总数
         */
        @Override
        public int getChildrenCount(int groupPosition) {
            return cityMap.get(groupPosition).size();
        }

        /**
         * 获取一级标签下二级标签的内容
         */
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return cityMap.get(groupPosition).get(childPosition).getCity();
        }

        /**
         * 获取二级标签的ID
         */
        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        /**
         * 指定位置相应的组视图
         */
        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup arg3) {
            // TODO Auto-generated method stub
            TextView group_text;
            ImageView group_image;
            if (convertView == null) {
                convertView = (RelativeLayout) RelativeLayout.inflate(
                        getActivity(), R.layout.offlinemap_gaode_group, null);
            }
            group_text = (TextView) convertView.findViewById(R.id.group_text);
            group_image = (ImageView) convertView
                    .findViewById(R.id.group_image);
            group_text.setText(provinceList.get(groupPosition)
                    .getProvinceName());
            if (isExpanded) {
                group_image.setImageDrawable(getResources().getDrawable(
                        R.drawable.moff_down_arrow));
            } else {
                group_image.setImageDrawable(getResources().getDrawable(
                        R.drawable.moff_top_arrow));
            }
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean arg2, View convertView,
                                 ViewGroup arg4) {
            // TODO Auto-generated method stub
            if (convertView == null) {
                convertView = (RelativeLayout) RelativeLayout.inflate(
                        getActivity(), R.layout.offlinemap_gaode_child, null);
            }
            ViewHolder holder = new ViewHolder(convertView);
            holder.cityName.setText(cityMap.get(groupPosition)
                    .get(childPosition).getCity() + " — ");
            holder.citySize.setText((int) ((cityMap.get(groupPosition).get(
                    childPosition).getSize()) * 100
                    / (1024 * 1024f)) / 100.0 + "MB");

            if (compareFile(Utils.converterToSpell(cityMap.get(groupPosition).get(childPosition).getCity()))){
                holder.imageView.setImageResource(R.drawable.sportting_setround_checked);
            }else {
                holder.imageView.setImageResource(R.drawable.moffline_download);
            }


            return convertView;
        }

        class ViewHolder {
            TextView cityName;
            TextView citySize;
            ImageView imageView;

            public ViewHolder(View view) {
                cityName = (TextView) view.findViewById(R.id.city_name);
                citySize = (TextView) view.findViewById(R.id.city_size);
                imageView = (ImageView) view.findViewById(R.id.expand_gaode_img);
            }
        }

        @Override
        public boolean isChildSelectable(int arg0, int arg1) {
            // TODO Auto-generated method stub
            return true;
        }

    };

    public interface onCheckedPageGaodeListener {
        void onCheckedPage();
    }

    public void setPageGaodeListener(onCheckedPageGaodeListener onCheckedPageListener) {
        this.onCheckedPageListener = onCheckedPageListener;
    }


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

