package com.fox.exercise;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.fox.exercise.api.entity.ActionList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ActivityListAdapter extends BaseAdapter {
    //	 private Context mContext;
    private LayoutInflater mlfInflater;
    private List<ActionList> activityEntitys;
    private ImageDownloader mDownloader = null;
    private static final String TAG = "ActivityListAdapter";
    private String activityTime;
    private SimpleDateFormat formatter;
    private TextView activiti_station;
    private Date curDate, activityDate;
    private ColorMatrix matrix;
//     private ColorMatrixColorFilter filter;

    public ActivityListAdapter(Context mContext, List<ActionList> activityEntitys) {
        super();
//		this.mContext = mContext;
        this.mlfInflater = LayoutInflater.from(mContext);
        this.activityEntitys = activityEntitys;
        this.mDownloader = new ImageDownloader(mContext);

        formatter = new SimpleDateFormat("yyyy-MM-dd");
        curDate = new Date(System.currentTimeMillis());
        matrix = new ColorMatrix();
        matrix.setSaturation(0);
//        filter = new ColorMatrixColorFilter(matrix);
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return activityEntitys.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return activityEntitys.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    public class ViewHolder {
        ImageView mimg;
        TextView mactivity_name, mactivity_time, mactivity_time_context;

    }

    @SuppressWarnings("unused")
    @Override
    public View getView(int position, View contentview, ViewGroup arg2) {

        ViewHolder vh = null;
        if (vh == null) {
            vh = new ViewHolder();
            contentview = mlfInflater.inflate(R.layout.activity_list_item, null);
            TextView activity_name = (TextView) contentview.findViewById(R.id.activity_name);
            TextView activity_time = (TextView) contentview.findViewById(R.id.activity_time);
            activiti_station = (TextView) contentview.findViewById(R.id.activity_station);
            TextView activity_time_context = (TextView) contentview.findViewById(R.id.activity_time_context);
            ImageView activity_pic = (ImageView) contentview.findViewById(R.id.activity_pic);
            vh.mactivity_name = activity_name;
            vh.mactivity_time = activity_time;
            vh.mactivity_time_context = activity_time_context;
            vh.mimg = activity_pic;
            contentview.setTag(vh);
        } else {
            vh = (ViewHolder) contentview.getTag();
        }
        ActionList activity = activityEntitys.get(position);
        vh.mactivity_name.setText(activity.getTitle());
        //vh.mactivity_time.setText();
        vh.mactivity_time_context.setText(activity.getActionTime());
        activityTime = activity.getActionTime().substring(activity.getActionTime().indexOf("-") + 1,
                activity.getActionTime().length()).replace(".", "-");
        Log.e("ActivityListAdapter", "活动时间：" + activityTime);

        try {
            activityDate = formatter.parse(activityTime);
            activityDate.setHours(23);
            activityDate.setMinutes(59);
            activityDate.setSeconds(59);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.e("ActivityListAdapter", "系统时间：" + curDate.toString());
        Log.e("ActivityListAdapter", "活动时间：" + activityDate.toString());

        if (curDate.after(activityDate)) {
            activiti_station.setText("");
            activiti_station.setBackgroundResource(R.drawable.finish);
            activiti_station.setTextColor(Color.parseColor("#FFFFFF"));
            //vh.mimg.setColorFilter(filter);
        }
        //////////
        /*DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;
		Log.e(TAG, "*************screenW="+screenW);*/
        //SportsApp.ScreenWidth;
	/*	WindowManager wm = (WindowManager) mContext .getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		int height = wm.getDefaultDisplay().getHeight();*/
        if (activity.getUrl() != null && !"".equals(activity.getUrl())) {
            mDownloader.setType(ImageDownloader.ACTIVITYICON);
            mDownloader.download(activity.getUrl(), vh.mimg, null);
        }
        Log.e(TAG, "*****************title=" + activity.getTitle());
        Log.e(TAG, "*****************pic=" + activity.getUrl());
        System.out.print("***********picpicpicpicpic*********" + activity.getUrl());

        return contentview;
    }
}
