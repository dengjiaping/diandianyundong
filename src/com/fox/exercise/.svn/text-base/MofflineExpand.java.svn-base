package com.fox.exercise;

import java.util.ArrayList;
import java.util.List;

import cn.ingenic.indroidsync.SportsApp;

import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.umeng.analytics.MobclickAgent;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

public class MofflineExpand extends Fragment {
    //private Button localButton,cityButton;
    private SportsApp mSportsApp;

    private ExpandableListView expandableListView;
    private MKOfflineMap mofflineMap;
    private List<List<String>> city;
    private SharedPreferences spf;
    private MofflineMapPage mapPage;
    private onCheckedPageListener onCheckedPageListener;
    private boolean isCheckPage;

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
        return inflater.inflate(R.layout.moffline_expand, null);
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
        MobclickAgent.onPageStart("MofflineExpand");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MofflineExpand");
    }


    private void init() {
        mapPage = new MofflineMapPage();
        expandableListView = (ExpandableListView) getActivity().findViewById(R.id.expandableListView);
        mofflineMap = new MKOfflineMap();
        mofflineMap.init(new MKOfflineMapListener() {
            @Override
            public void onGetOfflineMapState(int arg0, int arg1) {
                // TODO Auto-generated method stub
                MKOLUpdateElement update = mofflineMap.getUpdateInfo(arg1);
            }
        });

        final List<String> Province = new ArrayList<String>();
        city = new ArrayList<List<String>>();
        spf = getActivity().getSharedPreferences("sports", 0);
        String currentCity = spf.getString("cityname", "");
        List<String> currentCityList = new ArrayList<String>();
        ArrayList<MKOLSearchRecord> Records = mofflineMap.getOfflineCityList();
        Province.add("当前城市");

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
                    Toast.makeText(getActivity(), R.string.not_friends_tab_faxian,
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
                mofflineMap.start(records.get(0).cityID);
                MKOLUpdateElement update = mofflineMap.getUpdateInfo(records.get(0).cityID);
                if (update.ratio == 100) {
                    Toast.makeText(getActivity(), R.string.download_finished,
                            Toast.LENGTH_SHORT).show();
                } else if (!mSportsApp.isOpenNetwork()) {
                    Toast.makeText(getActivity(), R.string.sports_comment_not_connected,
                            Toast.LENGTH_SHORT).show();
                } else {
                    MobclickAgent.onEvent(getActivity(), "downloadbaidu");
                    Toast.makeText(getActivity(), R.string.startSport_moffline_map,
                            Toast.LENGTH_SHORT).show();
                    if (onCheckedPageListener != null) ;
                    onCheckedPageListener.onCheckedPage();
                    //localButton.setBackgroundResource(R.drawable.sports_moffline_click);
                    //cityButton.setBackgroundResource(R.drawable.sports_moffline);
                }
                return true;
            }
        });

        expandableListView.setAdapter(new MyExpandableListAdapter(getActivity(), city, Province));

    }

    public String formatDataSize(int size) {
        String ret = "";
        if (size < (1024 * 1024)) {
            ret = String.format("%dK", size / 1024);
        } else {
            ret = String.format("%.1fM", size / (1024 * 1024.0));
        }
        return ret;
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
//  		  this.city=city;
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

        }
    }

    ;

    public interface onCheckedPageListener {
        void onCheckedPage();
    }

    public void setPageListener(onCheckedPageListener onCheckedPageListener) {
        this.onCheckedPageListener = onCheckedPageListener;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

}

