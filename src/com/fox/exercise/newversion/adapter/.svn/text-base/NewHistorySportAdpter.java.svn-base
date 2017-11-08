package com.fox.exercise.newversion.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.ingenic.indroidsync.SportsApp;

import com.fox.exercise.ImageDownloader;
import com.fox.exercise.R;
import com.fox.exercise.pedometer.SportContionTaskDetail;
import com.fox.exercise.util.SportTaskUtil;

public class NewHistorySportAdpter extends BaseAdapter {
    private static final String TAG = "HistorySportAdpter";

    private ArrayList<SportContionTaskDetail> mList = null;
    private Context mContext = null;
    private LayoutInflater mInflater = null;
    private int curUid;
    private int uid;
    private int time;
    private double speed;
    private SportsApp mSportsApp = SportsApp.getInstance();
    private ImageDownloader mDownloader = null;
    private ImageDownloader mIconDownloader = null;
    private int num = 1;
    private TextView sport_distance, sports_time, burn_calories, average_speed,
            title_time, step_counter, title_sport_num, average_history, Combustion_history,
            time_history, Distance_history;
    String cutten_data = "", next_data = "";
    ArrayList<String> sport_nums = new ArrayList<String>();
    HashMap<String, String> sportnum = new HashMap<String, String>();
    private int[] sportTypeId = {R.drawable.mysports_walk,
            R.drawable.mysports_running, R.drawable.mysports_swim,
            R.drawable.mysports_climbing, R.drawable.mysports_golf,
            R.drawable.mysports_walk_race, R.drawable.mysports_cycling,
            R.drawable.mysports_tennis, R.drawable.mysports_badminton,
            R.drawable.mysports_football, R.drawable.mysports_table_tennis,
            R.drawable.mysports_rowing, R.drawable.mysports_skating,
            R.drawable.mysports_roller_skating};
    private int[] swimTypeId = {R.drawable.mysports_ziyouyong,
            R.drawable.mysports_dieyong, R.drawable.mysports_wayong,
            R.drawable.mysports_yangyong};

    private int j = 0;

    public NewHistorySportAdpter(ArrayList<SportContionTaskDetail> list,
                                 Context context) {
        this.mContext = context;
        this.mList = list;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mDownloader = new ImageDownloader(mContext);
        mDownloader.setType(ImageDownloader.GIFT);
        mIconDownloader = new ImageDownloader(mContext);
        mIconDownloader.setSize(90, 90);
        mIconDownloader.setType(ImageDownloader.ICON);

        // Log.e(TAG, "检测构造函数中集合大小:"+ mList.size());
        prepareTitles();
    }

    public void prepareTitles() {
        sportnum.clear();
        for (int i = 0; i < mList.size(); i++) {

            if (i == 0) {// 第一次进入为cutten_data进行初始化
                cutten_data = mList.get(i).getSportDate();
                num = 1;
                // Log.e(TAG, "测试适配器cutten_data:"+cutten_data);
                if (i == mList.size() - 1) {// i==当天运动次数总和,用于只有一条运动记录
                    sportnum.put(cutten_data, "" + num);
                }
                continue;
            }

            next_data = mList.get(i).getSportDate();
            Log.e(TAG, "测试适配器cutten_data:" + cutten_data);
            Log.e(TAG, "测试适配器next_data:" + next_data);
            if (cutten_data.equals(next_data)) {
                num++;
                if (i == mList.size() - 1) {// i==当天运动次数总和
                    String temp = sportnum.get(cutten_data);
                    if (temp != null && !temp.equals("")) {
                        int preValue = 0;
                        preValue = Integer.parseInt(temp);
                        int value = num + preValue;
                        sportnum.remove(cutten_data);
                        sportnum.put(cutten_data, "" + value);
                    } else {
                        sportnum.put(cutten_data, "" + num);
                    }
                }
            } else {
                String temp = sportnum.get(cutten_data);
                if (temp != null && !temp.equals("")) {
                    int preValue = 0;
                    preValue = Integer.parseInt(temp);
                    int value = num + preValue;
                    sportnum.remove(cutten_data);
                    sportnum.put(cutten_data, "" + value);
                } else {
                    sportnum.put(cutten_data, "" + num);
                }
                num = 1;
                cutten_data = next_data;
                if (i == mList.size() - 1) {// i==当天运动次数总和
                    sportnum.put(cutten_data, "" + num);
                }
            }
        }
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    public void clearList() {
        mList.clear();
    }

    public void addItem(SportContionTaskDetail um) {
        mList.add(um);
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        RelativeLayout layout = null;
        // Log.e(TAG, "测试适配器position:"+position);
        if (convertView == null) {
            layout = (RelativeLayout) mInflater.inflate(R.layout.sports_history_adpter_item, null);

        } else {
            layout = (RelativeLayout) convertView;
        }

        ImageView icon = (ImageView) layout.findViewById(R.id.focus_image_icon);
        int sportType = mList.get(position).getSports_type();
        if (sportType == 2) {
            int swimType = mList.get(position).getSwimType();
            icon.setBackgroundResource(swimTypeId[swimType]);
        } else {
            icon.setBackgroundResource(sportTypeId[sportType]);
        }
        icon.setImageDrawable(null);

        ImageView mywatch_icon = (ImageView) layout.findViewById(R.id.mywatch);
        int mywatch_index = mList.get(position).getMonitoringEquipment();
        if (mywatch_index == 1) {
            mywatch_icon.setVisibility(View.VISIBLE);
        } else {
            mywatch_icon.setVisibility(View.GONE);
        }

        TextView startTime = (TextView) layout.findViewById(R.id.start_time_txt);

        int index = mList.get(position).getStartTime().indexOf(" ");
        String start_time = mList.get(position).getStartTime().substring(index + 1);
        startTime.setText(start_time);
        // if ("man".equals(mList.get(position).)) {
        // icon.setBackgroundResource(R.drawable.sports_user_edit_portrait_male);
        // } else {
        // icon.setBackgroundResource(R.drawable.sports_user_edit_portrait);
        // }
        // mIconDownloader.download(mList.get(position).getImg(), icon, null);

        // icon.setOnClickListener(new OnClickListener() {
        //
        // @Override
        // public void onClick(View arg0) {
        // if (mList != null && position < mList.size()) {
        // Intent intent = new Intent(mContext, PedometerActivity.class);
        // intent.putExtra("ID", mList.get(position).getId());
        // mContext.startActivity(intent);
        // }
        // }
        // });

        sport_distance = (TextView) layout
                .findViewById(R.id.sport_distance);
        String disStr = SportTaskUtil.getDoubleNum(mList.get(position)
                .getSportDistance());
        sport_distance.setText(disStr);

        sports_time = (TextView) layout.findViewById(R.id.sports_time);
        time = Integer.parseInt(mList.get(position).getSportTime());
        String tiemStr = Integer.toString(time / 3600) + "h"
                + Integer.toString(time % 3600 / 60) + "'"
                + Integer.toString(time % 3600 % 60) + "\"";
        sports_time.setText(tiemStr);

        burn_calories = (TextView) layout
                .findViewById(R.id.burn_calories);
        burn_calories.setText(String.valueOf(mList.get(position)
                .getSprots_Calorie()));

        average_speed = (TextView) layout
                .findViewById(R.id.average_speed);
        String speedStr = SportTaskUtil.getDoubleNum(mList.get(position)
                .getSportVelocity());
        average_speed.setText(speedStr);
        // View line = (View) layout.findViewById(R.id.line);

        title_time = (TextView) layout.findViewById(R.id.title_time);
        step_counter = (TextView) layout.findViewById(R.id.step_counter);
        title_sport_num = (TextView) layout
                .findViewById(R.id.title_sport_num);
        RelativeLayout title_layout = (RelativeLayout) layout
                .findViewById(R.id.title_layout);

        average_history = (TextView) layout.findViewById(R.id.average_history);
        Combustion_history = (TextView) layout.findViewById(R.id.Combustion_history);
        time_history = (TextView) layout.findViewById(R.id.time_history);
        Distance_history = (TextView) layout.findViewById(R.id.Distance_history);
        speed = mList.get(position).getSportVelocity();
        if (!SportTaskUtil.getNormalRange(sportType, speed, time)) {
            historyColor();
        } else {
            sport_distance.setTextColor(mContext.getResources().getColor(R.color.new_black));
            average_history.setTextColor(mContext.getResources().getColor(R.color.gray_litter));
            Combustion_history.setTextColor(mContext.getResources().getColor(R.color.gray_litter));
            time_history.setTextColor(mContext.getResources().getColor(R.color.gray_litter));
            Distance_history.setTextColor(mContext.getResources().getColor(R.color.gray_litter));
            burn_calories.setTextColor(mContext.getResources().getColor(R.color.new_black));
            sports_time.setTextColor(mContext.getResources().getColor(R.color.new_black));
            average_speed.setTextColor(mContext.getResources().getColor(R.color.new_black));
        }

        ImageView isUplaod_icon = (ImageView) layout.findViewById(R.id.is_upload);
        int sport_isUpload = mList.get(position).getSport_isupload();
        int tempId = mList.get(position).getTaskid();
        curUid = SportsApp.getInstance().getSportUser().getUid();
        uid = mList.get(position).getUserId();
        Log.e("yushuijing========>", "curUid : " + curUid + "  uid :" + uid);
        if (curUid == uid) {
            if (sport_isUpload < 1 || (tempId < 0 && sport_isUpload == 1)) {
                if (!SportTaskUtil.getNormalRange(sportType, speed, time)) {
                    isUplaod_icon.setVisibility(View.GONE);
                } else {
                    isUplaod_icon.setVisibility(View.VISIBLE);
                }
            }
        }

        // 计算运动次数
        if (needTitle(position)) {
            // 显示标题并设置内容
            String data = mList.get(position).getSportDate();
            // Log.e(TAG, "******测试运动次数的日期:"+sportnum.get(data));
            title_sport_num.setText(sportnum.get(data));
            String title = data.replace("-", ".");
            title_time.setText(title);
            title_time.setVisibility(View.VISIBLE);
            step_counter.setText(CalcStepCounter(position, Integer.parseInt(sportnum.get(data))) + " 步");
            step_counter.setVisibility(View.VISIBLE);
            title_layout.setVisibility(View.VISIBLE);
            // line.setVisibility(View.VISIBLE);
        } else {
            // 内容项隐藏标题
            title_time.setVisibility(View.GONE);
            step_counter.setVisibility(View.GONE);
            title_layout.setVisibility(View.GONE);
            // line.setVisibility(View.GONE);
        }

		/*View line = (View) layout.findViewById(R.id.horizontal_line);
        if (((position < mList.size() - 1) && needTitle(position + 1))
				|| (position == mList.size() - 1)) {
			line.setVisibility(View.GONE);
		} else {
			line.setVisibility(View.VISIBLE);
		}*/

        // 运动类型点击
        // ImageView uploadIcon = (ImageView)
        // layout.findViewById(R.id.upload_image_icon);
        // uploadIcon.setBackgroundResource(PedometerActivity.getStateImgIds(mList.get(position).getType()));
        //
        // uploadIcon.setOnClickListener(new OnClickListener() {
        //
        // @Override
        // public void onClick(View arg0) {
        // // if (mList != null && position < mList.size() && !mTaskRun) {
        // // GetImageDetailTask task = new
        // GetImageDetailTask(mList.get(position).getPid());
        // // task.execute();
        // // }
        // Intent intent = new Intent(mContext, SportTaskDetailActivity.class);
        // intent.putExtra("uid", mList.get(position).getId());
        // intent.putExtra("taskid", mList.get(position).getTaskid());
        // mContext.startActivity(intent);
        // }
        // });
        return layout;
    }

    // 判断是否显示每一个item上的标题
    private int CalcStepCounter(int position, int count) {
        Double distance = 0.0;

        for (int i = position; i < (position + count); i++) {
            Log.e(TAG, "DIS : " + mList.get(i).getSportDistance());
            distance += mList.get(i).getSportDistance();
        }

        return (int) (distance * 10000 / 6);
    }

    // 判断是否显示每一个item上的标题
    private boolean needTitle(int position) {

        // 第一个肯定是分类
        if (position == 0) {
            return true;
        }

        // 边界处理
        if (position < 0) {
            return false;
        }

        // 当前 // 上一个
        String current_time = mList.get(position).getSportDate();
        String previous_time = mList.get(position - 1).getSportDate();

        if (null == current_time || null == previous_time) {
            return false;
        }

        // 当前item分类名和上一个item分类名不同，则表示两item属于不同分类
        if (current_time.equals(previous_time)) {
            return false;
        }

        return true;
    }

    public void historyColor() {
        sport_distance.setTextColor(Color.RED);
        average_history.setTextColor(Color.RED);
        Combustion_history.setTextColor(Color.RED);
        time_history.setTextColor(Color.RED);
        Distance_history.setTextColor(Color.RED);
        burn_calories.setTextColor(Color.RED);
        sports_time.setTextColor(Color.RED);
        average_speed.setTextColor(Color.RED);
    }

}
