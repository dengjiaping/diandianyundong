package com.fox.exercise.newversion.adapter;

import java.util.ArrayList;

import com.fox.exercise.ImageDownloader;
import com.fox.exercise.R;
import com.fox.exercise.newversion.entity.TrainPlanList;
import com.fox.exercise.util.SportTaskUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author loujungang 训练计划列表适配器
 */
public class TrainPlanListAdapter extends BaseAdapter {
    private ArrayList<TrainPlanList> mlist;
    private LayoutInflater mInflater;
    private Context mContext;
    private ImageDownloader mDownloader = null;

    public TrainPlanListAdapter(ArrayList<TrainPlanList> mlist, Context mContext) {
        this.mlist = mlist;
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
        this.mDownloader = new ImageDownloader(mContext);
        mDownloader.setType(ImageDownloader.OnlyOne);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mlist.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return mlist.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.trainplan_list_item, null);
            holder = new ViewHolder();
            holder.trainplan_time = (TextView) convertView
                    .findViewById(R.id.trainplan_time);
            holder.train_name = (TextView) convertView
                    .findViewById(R.id.trainplan_name);
            holder.traincount = (TextView) convertView
                    .findViewById(R.id.traincount);
            holder.img = (ImageView) convertView
                    .findViewById(R.id.trainlist_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        mDownloader.download(mlist.get(position).getThumb(), holder.img, null);
        holder.train_name.setText(mlist.get(position).getTrain_name());
        holder.trainplan_time.setText(mlist.get(position).getTrain_time() + "分钟");
        if(mlist.get(position).getTraincount()>=10000){
            double c=mlist.get(position).getTraincount()*1.0/10000;
            String ss = SportTaskUtil.getDoubleOneNum(c)
                    + "万";
            holder.traincount.setText(ss + "人参与");
            ss=null;
        }else{
            holder.traincount.setText(mlist.get(position).getTraincount() + "人参与");
        }

        return convertView;
    }

    public class ViewHolder {
        public TextView train_name;
        public TextView trainplan_time;
        public TextView traincount;
        public ImageView img;

    }

}
