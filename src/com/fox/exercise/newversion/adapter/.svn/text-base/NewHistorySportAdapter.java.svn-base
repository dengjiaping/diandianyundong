package com.fox.exercise.newversion.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fox.exercise.R;
import com.fox.exercise.SportsType;
import com.fox.exercise.baseadapter.CommonBaseAdapter;
import com.fox.exercise.baseadapter.ViewHolder;
import com.fox.exercise.pedometer.SportContionTaskDetail;
import com.fox.exercise.util.SportTaskUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * Created by suhu on 2016/11/11.
 */

public class NewHistorySportAdapter extends CommonBaseAdapter {
    private HashMap<String, Double> distanceMap = new HashMap<String, Double>();
    private List<SportContionTaskDetail> mlist;
    private double speed;
    private int time,typ;
    private int[] sportTypeId = {R.drawable.zoulu_new,
            R.drawable.paobu_new,
            R.drawable.dengshan_new,
            R.drawable.qixing_new,};

    public NewHistorySportAdapter(ArrayList<SportContionTaskDetail> list, Context context, int id,int typ) {
        super(list, context, id);
        this.mlist = list;
        this.typ = typ;
        distanceSum();
    }


    @Override
    public void setData(ViewHolder viewHolder, int position) {
        RelativeLayout rl_title = (RelativeLayout) viewHolder.findViewById(R.id.rl_title);
        TextView title_time = (TextView) viewHolder.findViewById(R.id.title_time);
        TextView title_distance = (TextView) viewHolder.findViewById(R.id.title_distance);
        ImageView image_typ = (ImageView) viewHolder.findViewById(R.id.image_typ);
        ImageView speed_limit = (ImageView) viewHolder.findViewById(R.id.image_speed_limit);
        TextView tv_distance = (TextView) viewHolder.findViewById(R.id.tv_distance);
        TextView sport_time = (TextView) viewHolder.findViewById(R.id.sport_time);
        TextView sport_date = (TextView) viewHolder.findViewById(R.id.sport_date);
        //标题栏
        switch (needShowTitle(mlist, position,typ)) {
            case -1:
                rl_title.setVisibility(View.GONE);
                break;
            case 0:
                rl_title.setVisibility(View.VISIBLE);
                title_time.setText(Integer.parseInt(mlist.get(position).getSportDate().split("-")[1]) + "月");
                //title_distance.setText(SportTaskUtil.getDoubleNumber(getDistance(mlist, distanceMap, position)) + "km");
                break;
            case 1:
                rl_title.setVisibility(View.VISIBLE);
                title_time.setText(mlist.get(position).getSportDate().split("-")[0] + "年" + mlist.get(position).getSportDate().split("-")[1] + "月");
                //title_distance.setText(SportTaskUtil.getDoubleNumber(getDistance(mlist, distanceMap, position)) + "km");
                break;
        }
        //运动类型
        switch (mlist.get(position).getSports_type()) {
            case SportsType.TYPE_WALK:
                image_typ.setImageResource(sportTypeId[0]);
                break;
            case SportsType.TYPE_RUN:
                image_typ.setImageResource(sportTypeId[1]);
                break;
            case SportsType.TYPE_CLIMBING:
                image_typ.setImageResource(sportTypeId[2]);
                break;
            case SportsType.TYPE_CYCLING:
                image_typ.setImageResource(sportTypeId[3]);
                break;
        }
        //判断是否超速
        speed = mlist.get(position).getSportVelocity();
        time = Integer.parseInt(mlist.get(position).getSportTime());
        if (!SportTaskUtil.getNormalRange(mlist.get(position).getSports_type(), speed, time) || mlist.get(position).getSport_isupload() == 2) {
            speed_limit.setVisibility(View.VISIBLE);
        } else {
            speed_limit.setVisibility(View.GONE);
        }
        tv_distance.setText(SportTaskUtil.getDoubleNumber(mlist.get(position).getSportDistance()) + "");
        sport_time.setText(SportTaskUtil.showTimeCount(Integer.parseInt(mlist.get(position).getSportTime())));
        sport_date.setText(getTime(mlist.get(position).getStartTime()));
    }


    /**
     * @param position
     * @return :返回int状态值
     * -1:表示不显示
     * 0:表示显示月份（只显示月份，不显示年数）
     * 1:表示显示年份（显示年和月份）
     * @method 判断时间是否显示
     * @author suhu
     * @time 2016/11/11 9:55
     */
    public static int needShowTitle(List<SportContionTaskDetail> list, int position,int typ) {
        //当前年
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String nowDate = format.format(new Date());
        String nowYears = nowDate.split("-")[0];
        //当前数据时间
        String date = list.get(position).getSportDate();
        String times[] = date.split("-");
        String thisYears = "", thismonth = "";
        if (times != null && (times.length == 3)) {
            thisYears = times[0];
            thismonth = times[1];
        }
        if (position == 0) {
            return typ;
        }
        if (position < 0) {
            return -1;
        }
        //上一条数据时间
        String beforeDate = list.get(position - 1).getSportDate();
        String beforeTimes[] = beforeDate.split("-");
        String beforeMonth = "";
        if (beforeTimes != null && (beforeTimes.length == 3)) {
            beforeMonth = beforeTimes[1];
        }
        //判断月份是否为空
        if (TextUtils.isEmpty(thismonth) || TextUtils.isEmpty(beforeMonth)) {
            return -1;
        }
        //判断月份是否相同
        if (thismonth.equals(beforeMonth)) {
            return -1;
        }
        //判断年份是否相同
        if (nowYears.equals(thisYears)) {
            return 0;
        }
        return 1;
    }

    /**
     * @param
     * @return null
     * @method 获取每月公里数集合
     * @author suhu
     * @time 2016/11/11 10:57
     */
    public void distanceSum() {
        distanceMap.clear();
        if (mlist != null) {
            for (int i = 0; i < mlist.size(); i++) {
                String str[] = mlist.get(i).getSportDate().split("-");
                String date = "";
                if (str != null && str.length >= 2) {
                    date = str[0] + "-" + str[1];
                }
                if (i == 0) {
                    distanceMap.put(date, mlist.get(i).getSportDistance());
                    continue;
                }
                if (distanceMap.get(date) != null) {
                    distanceMap.put(date, distanceMap.get(date) + mlist.get(i).getSportDistance());
                } else {
                    distanceMap.put(date, mlist.get(i).getSportDistance());
                }
            }
        }

    }


    /**
     * @param list
     * @param map
     * @param position
     * @return double(如果返回null表述输入有问题)
     * @method 根据月份获得当月公里数
     * @author suhu
     * @time 2016/11/11 11:11
     */
    public static Double getDistance(List<SportContionTaskDetail> list, HashMap<String, Double> map, int position) {
        if (list==null){
            return null;
        }
        if (position < 0 || position >= list.size()) {
            return null;
        }
        String str[] = list.get(position).getSportDate().split("-");
        String date = "";
        if (str != null && str.length >= 2) {
            date = str[0] + "-" + str[1];
        }
        return map.get(date);
    }

    /**
     * @param startTime
     * @return 例如：2016-11-05 10:52:36变为5日10:52
     * @method 截取字符串
     * @author suhu
     * @time 2016/11/15 10:52
     */
    public String getTime(String startTime) {
        startTime.trim();
        try {
            String str1 = startTime.substring(8);
            String str2 = str1.substring(0, str1.length() - 3);
            String str3[] = str2.split(" ");
            return Integer.parseInt(str3[0]) + "日 " + str3[1];
        } catch (Exception e) {
            return "";
        }
    }

}
