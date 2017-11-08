package com.fox.exercise.newversion.adapter;

import java.util.ArrayList;
import java.util.List;

import com.fox.exercise.ImageDownloader;
import com.fox.exercise.R;
import com.fox.exercise.newversion.entity.TrainAction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HorizontalScrollViewAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<TrainAction> mDatas;
    private ImageDownloader mDownloader = null;

    public HorizontalScrollViewAdapter(Context context, ArrayList<TrainAction> mDatas) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mDatas = mDatas;
        this.mDownloader = new ImageDownloader(mContext);
        mDownloader.setType(ImageDownloader.OnlyOne);
    }

    public int getCount() {
        return mDatas.size();
    }

    public Object getItem(int position) {
        return mDatas.get(position).getThumb();
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(
                    R.layout.activity_index_gallery_item, parent, false);
            viewHolder.mImg = (ImageView) convertView
                    .findViewById(R.id.id_index_gallery_item_image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        mDownloader.download(mDatas.get(position).getThumb(), viewHolder.mImg, null);

        return convertView;
    }

    private class ViewHolder {
        ImageView mImg;
    }

}
