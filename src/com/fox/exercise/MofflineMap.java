package com.fox.exercise;

import java.util.ArrayList;
import java.util.List;

import cn.ingenic.indroidsync.SportsApp;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
//import com.baidu.platform.comapi.map.v;
import com.fox.exercise.http.FunctionStatic;
import com.fox.exercise.view.MoffMapSwitchView;
import com.fox.exercise.view.MoffMapSwitchView.OnCheckedChangeListener;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MofflineMap extends AbstractBaseActivity {
    //private Button localButton,cityButton;
    private LinearLayout cityLinear;
    private LinearLayout DownloadLinear;
    private ListView mapList;
    private List<List<String>> city;
    private MKOfflineMap mofflineMap;
    private MapView mapView = null;
    private ArrayList<MKOLUpdateElement> localMapList = null;
    private LocalMapAdapter lAdapter = null;
    private SharedPreferences spf;
    public LocationClient mLocationClient = null;
    private String TheCurrentCity;
    private MKOLUpdateElement currentE = null;
    private SportsApp mSportsApp;
    MoffMapSwitchView moff_MAIN;

    private Activity mContext;
    private long preTime = 0;

    public void init() {
        cityLinear = (LinearLayout) findViewById(R.id.city_linear);
        DownloadLinear = (LinearLayout) findViewById(R.id.Download_linear);
        //cityButton = (Button) findViewById(R.id.cityButton);
        //localButton = (Button) findViewById(R.id.localButton);
        cityLinear.setVisibility(View.VISIBLE);
        DownloadLinear.setVisibility(View.GONE);
        moff_MAIN = (MoffMapSwitchView) findViewById(R.id.switch_moff);
        moff_MAIN.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    DownloadLinear.setVisibility(View.GONE);
                    cityLinear.setVisibility(View.VISIBLE);

                } else {
                    cityLinear.setVisibility(View.GONE);
                    DownloadLinear.setVisibility(View.VISIBLE);
                }
            }
        });
               /*cityButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				DownloadLinear.setVisibility(View.GONE);
				cityLinear.setVisibility(View.VISIBLE);	
				cityButton.setBackgroundResource(R.drawable.sports_moffline_click);
				localButton.setBackgroundResource(R.drawable.sports_moffline);
			}
		});
   		localButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				cityLinear.setVisibility(View.GONE);
	 	   		DownloadLinear.setVisibility(View.VISIBLE);
	 	   		localButton.setBackgroundResource(R.drawable.sports_moffline_click);
	 	   		cityButton.setBackgroundResource(R.drawable.sports_moffline);
			}
		});*/
        localMapList = mofflineMap.getAllUpdateInfo();
        if (localMapList == null) {
            localMapList = new ArrayList<MKOLUpdateElement>();
        }
        ListView localMapListView = (ListView) findViewById(R.id.localmaplist);
        lAdapter = new LocalMapAdapter();
        localMapListView.setAdapter(lAdapter);
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

    class MyExpandableListAdapter extends BaseExpandableListAdapter {
        private LayoutInflater listContainer;
        private Context context;
        private List<List<String>> city;
        private List<String> Province;

        public MyExpandableListAdapter(Context context, List<List<String>> city, List<String> province) {
            listContainer = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.city = city;
            this.Province = province;
            this.context = context;
        }

        public MyExpandableListAdapter(Context context, List<String> province) {
            listContainer = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.city = city;
            this.Province = province;
        }

        @Override
        public int getGroupCount() {
            // TODO Auto-generated method stub
            return Province.size();

        }

        @Override
        public Object getGroup(int groupPosition) {
            // TODO Auto-generated method stub
            return Province.get(groupPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            // TODO Auto-generated method stub
            return groupPosition;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            // TODO Auto-generated method stub
            if (city.equals("") || city == null) {
                return 0;
            }
            return city.get(groupPosition).size();
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            return city.get(groupPosition).get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            if (city.equals("") || city == null) {
                return 0;
            }
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            // TODO Auto-generated method stub
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            LinearLayout ll = (LinearLayout) (LinearLayout) listContainer.inflate(R.layout.expand_item, null);
            ll.setOrientation(0);
            TextView textView = (TextView) ll.findViewById(R.id.expand_txt);
            String cityName = Province.get(groupPosition);
            String str = cityName.split("-")[0];
            SpannableStringBuilder style = new SpannableStringBuilder(cityName);
            int color = context.getResources().getColor(R.color.text_step);
            style.setSpan(new ForegroundColorSpan(color), str.length(), cityName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(style);
            //  textView.setText(getGroup(groupPosition).toString());
            ImageView imageView = (ImageView) ll.findViewById(R.id.expand_img);
            if (isExpanded)
                imageView.setImageResource(R.drawable.moff_down_arrow);
            else
                imageView.setImageResource(R.drawable.moff_top_arrow);

            return ll;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            LinearLayout ll = (LinearLayout) (LinearLayout) listContainer.inflate(R.layout.expand_item, null);
            ll.setOrientation(0);
            TextView textView = (TextView) ll.findViewById(R.id.expand_txt);
            String cityName = city.get(groupPosition).get(childPosition);
            String strTxt = cityName.split("-")[0];
            SpannableStringBuilder style = new SpannableStringBuilder(cityName);
            int color = context.getResources().getColor(R.color.text_moffmap);
            style.setSpan(new ForegroundColorSpan(color), strTxt.length(), cityName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(style);
            ImageView imageView = (ImageView) ll.findViewById(R.id.expand_img);
            imageView.setImageResource(R.drawable.moffline_download);
            return ll;
        }

        @Override
        public boolean isChildSelectable(int groupPosition,
                                         int childPosition) {
            // TODO Auto-generated method stub
            return true;
        }

        @Override
        public void onGroupCollapsed(int groupPosition) {
            // TODO Auto-generated method stub
            super.onGroupCollapsed(groupPosition);
            String name = Province.get(groupPosition);

        }

        @Override
        public void onGroupExpanded(int groupPosition) {
            // TODO Auto-generated method stub
            super.onGroupExpanded(groupPosition);

            String cityName = Province.get(groupPosition);
            String str = cityName.split("-")[0];
            String cityStr = str.trim();
            ArrayList<MKOLSearchRecord> records = mofflineMap.searchCity(cityStr);
            //
            if (city.get(groupPosition).size() == 0) {
                mofflineMap.start(records.get(0).cityID);
                MKOLUpdateElement update = mofflineMap.getUpdateInfo(records.get(0).cityID);
                if (records == null || records.size() != 1) {
                    Toast.makeText(getApplicationContext(), "没有发现",
                            Toast.LENGTH_SHORT).show();
                }
                if (update.ratio == 100) {
                    Toast.makeText(getApplicationContext(), "已经下载 ",
                            Toast.LENGTH_SHORT).show();
                }
                if (!mSportsApp.isOpenNetwork()) {
                    Toast.makeText(mContext, "网络未连接",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "开始下载离线地图 ",
                            Toast.LENGTH_SHORT).show();
                    moff_MAIN.onCheckPosition(2);
                    cityLinear.setVisibility(View.GONE);
                    DownloadLinear.setVisibility(View.VISIBLE);
                    //localButton.setBackgroundResource(R.drawable.sports_moffline_click);
                    //cityButton.setBackgroundResource(R.drawable.sports_moffline);
                }
            }
        }
    }

    ;

    public String formatDataSize(int size) {
        String ret = "";
        if (size < (1024 * 1024)) {
            ret = String.format("%dK", size / 1024);
        } else {
            ret = String.format("%.1fM", size / (1024 * 1024.0));
        }
        return ret;
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
            View view = View.inflate(MofflineMap.this, R.layout.load_offline, null);
            TextView ratio = (TextView) view.findViewById(R.id.ratio);
            TextView title = (TextView) view.findViewById(R.id.load_title);
            title.setText(e.cityName);
            ratio.setText(e.ratio + "%");
            Button remove = (Button) view.findViewById(R.id.remove);
            remove.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Log.i("", "lulu " + arg0);
                    // MKOLUpdateElement currentE = (MKOLUpdateElement) getV(arg0.getp);
                    mofflineMap.remove(e.cityID);
                    updateView();
                }
            });

            return view;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public void initIntentParam(Intent intent) {
        title = getResources().getString(R.string.sports_map_baidu);

    }

    @Override
    public void initView() {
        showContentView(R.layout.moffline_map);
        mSportsApp = (SportsApp) getApplication();

        mofflineMap = new MKOfflineMap();
        mContext = this;
        mofflineMap.init(new MKOfflineMapListener() {
            @Override
            public void onGetOfflineMapState(int arg0, int arg1) {
                // TODO Auto-generated method stub
                MKOLUpdateElement update = mofflineMap.getUpdateInfo(arg1);
                // Log.i("", "lulu arg1"+arg1);
                // 处理下载进度更新提示
                if (update != null) {
                    updateView();
                    if (update.ratio == 100) {
                        Toast.makeText(getApplicationContext(), "下载成功",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        init();
        final ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        //List<List<String>> city= new ArrayList<List<String>>();
        //  expandableListView.setOnChildClickListener(this );

        final List<String> Province = new ArrayList<String>();
        city = new ArrayList<List<String>>();
        spf = this.getSharedPreferences("sports", 0);
        String currentCity = spf.getString("cityname", "");
        List<String> currentCityList = new ArrayList<String>();
        ArrayList<MKOLSearchRecord> Records = mofflineMap.getOfflineCityList();
        Province.add("当前城市");
        Log.i("", "currentCity" + "nnn" + spf.getString("cityname", "") + "nnn");
        Log.i("", "currentCity" + "nnn" + "" + "nnn");

        if (!(spf.getString("cityname", "").equals("")) || spf.getString("cityname", "") != "") {
            for (MKOLSearchRecord r : Records) {
                if (r.cityName.contains(currentCity)) {
                    currentCityList.add("  " + r.cityName + "   " + "-" + "   "
                            + this.formatDataSize(r.size));
                }
                if (r.childCities != null) {
                    for (MKOLSearchRecord s : r.childCities) {
                        if (s.cityName.contains(currentCity)) {
                            currentCityList.add("  " + currentCity + "   " + "-" + "   "
                                    + this.formatDataSize(s.size));
                        }
                    }
                }
            }
        } else {
            currentCityList.add("...");
        }
        city.add(currentCityList);

        Province.add("热门城市");
        List<String> hosList = new ArrayList<String>();
        ArrayList<MKOLSearchRecord> records1 = mofflineMap.getHotCityList();
        if (records1 != null) {
            for (MKOLSearchRecord r : records1) {
                hosList.add("  " + r.cityName + "   " + "-" + "   "
                        + this.formatDataSize(r.size));
            }
            city.add(hosList);
        }

        List<String> zhixiaCity = new ArrayList<String>();
        Province.add("直辖市");
        for (MKOLSearchRecord allCity : Records) {
            if (allCity.childCities == null) {
                if (allCity.cityName.contains("市")) {
                    zhixiaCity.add("  " + allCity.cityName + "   " + "-" + "   "
                            + this.formatDataSize(allCity.size));
                }
            }
        }
        city.add(zhixiaCity);

        List<String> Country = new ArrayList<String>();
        Province.add("全国概略图");
        for (MKOLSearchRecord allCity : Records) {
            if (allCity.childCities == null) {
                if (allCity.cityName.contains("全国")) {
                    Country.add("  " + allCity.cityName + "   " + "-" + "   "
                            + this.formatDataSize(allCity.size));
                }
            }
        }
        city.add(Country);
        //所有省
        for (MKOLSearchRecord r : Records) {
            List<String> usualCity = new ArrayList<String>();
            if (r.childCities != null) {
                Province.add("" + r.cityName);
                for (MKOLSearchRecord s : r.childCities) {
                    usualCity.add("  " + s.cityName + "   " + "-" + "   "
                            + this.formatDataSize(s.size));
                }
                city.add(usualCity);
            }
        }

        List<String> gangaoCity = new ArrayList<String>();
        Province.add("港澳");
        for (MKOLSearchRecord allCity : Records) {
            if (allCity.childCities == null) {
                if (allCity.cityName.contains("香港") || allCity.cityName.contains("澳门")) {
                    gangaoCity.add("  " + allCity.cityName + "   " + "-" + "   "
                            + this.formatDataSize(allCity.size));
                }
            }
        }
        city.add(gangaoCity);
        expandableListView.setGroupIndicator(null);
        expandableListView.setOnGroupExpandListener(new OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                // TODO Auto-generated method stub
                for (int i = 0; i < Province.size(); i++) {
                    if (groupPosition != i) {
                        expandableListView.collapseGroup(i);
                    }
                }
            }
        });
        expandableListView.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView arg0, View arg1,
                                        int groupPosition, int childPosition, long arg4) {
                // TODO Auto-generated method stub
                String cityName = city.get(groupPosition).get(childPosition);
                String str = cityName.split("-")[0];
                String cityStr = str.trim();

                ArrayList<MKOLSearchRecord> records = mofflineMap.searchCity(cityStr);
                if (records == null || records.size() != 1) {
                    Toast.makeText(getApplicationContext(), "没有发现",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
                mofflineMap.start(records.get(0).cityID);
                MKOLUpdateElement update = mofflineMap.getUpdateInfo(records.get(0).cityID);
                if (update.ratio == 100) {
                    Toast.makeText(getApplicationContext(), "已经下载 ",
                            Toast.LENGTH_SHORT).show();
                } else if (!mSportsApp.isOpenNetwork()) {
                    Toast.makeText(mContext, "网络未连接",
                            Toast.LENGTH_SHORT).show();
                } else {
                    MobclickAgent.onEvent(MofflineMap.this, "downloadbaidu");
                    Toast.makeText(getApplicationContext(), "开始下载离线地图",
                            Toast.LENGTH_SHORT).show();
                    moff_MAIN.onCheckPosition(2);
                    cityLinear.setVisibility(View.GONE);
                    DownloadLinear.setVisibility(View.VISIBLE);
                    //localButton.setBackgroundResource(R.drawable.sports_moffline_click);
                    //cityButton.setBackgroundResource(R.drawable.sports_moffline);
                }
                return true;
            }
        });

        expandableListView.setAdapter(new MyExpandableListAdapter(this, city, Province));

    }

    @Override
    public void setViewStatus() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageResume() {
        preTime = FunctionStatic.onResume();
        MobclickAgent.onPageStart("MofflineMap");

    }

    @Override
    public void onPagePause() {
        FunctionStatic.onPause(this, FunctionStatic.FUNCTION_DOWNLOAD_MAP_BAIDU, preTime);
        MobclickAgent.onPageEnd("MofflineMap");
    }

    @Override
    public void onPageDestroy() {
        localMapList = mofflineMap.getAllUpdateInfo();
        if (localMapList != null) {
            for (int i = 0; i < localMapList.size(); i++) {
                final MKOLUpdateElement e = (MKOLUpdateElement) localMapList.get(i);
                if (e.ratio != 100) {
                    mofflineMap.remove(e.cityID);
                }
            }
        }
        mofflineMap.destroy();

    }

}
