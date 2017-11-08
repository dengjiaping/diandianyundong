package com.fox.exercise;

import java.util.ArrayList;


import com.fox.exercise.SportsGroupAll.GroupAll;
import com.fox.exercise.SportsGroupAll.SportsGroupAllHandler;
import com.fox.exercise.SportsGroupAll.SportsGroupAllThread;
import com.fox.exercise.api.entity.UserNearby;
import com.fox.exercise.util.RoundedImage;

import android.R.integer;
import android.app.Dialog;
import android.content.Context;

import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.ingenic.indroidsync.SportsApp;

public class SportsGroupAllAdapter extends BaseAdapter {
    private Context mContext = null;
    private ArrayList<GroupAll> mList = null;
    //	private SportsGroupAllHandler editHandler;
//	private SportsApp mSportsApp;
    private LayoutInflater mInflater = null;
    private ImageDownloader mDownloader = null;
    private Dialog loadProgressDialog = null;
    //	private TextView message = null;
    private int itemWidth;
    private int itemHeight;

    public SportsGroupAllAdapter(Context context,
                                 ArrayList<GroupAll> list, SportsApp sportsApp) {
        // TODO Auto-generated constructor stub
        this.mContext = context;
//		mSportsApp = sportsApp;
        this.mList = list;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mDownloader = new ImageDownloader(context);
        mDownloader.setType(ImageDownloader.ICON);
        loadProgressDialog = new Dialog(context, R.style.sports_dialog);
        View v1 = mInflater.inflate(R.layout.sports_progressdialog, null);
//		message = (TextView) v1.findViewById(R.id.message);
        v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
        loadProgressDialog.setContentView(v1);
        itemWidth = (int) ((SportsApp.ScreenWidth - 2) / 2);
        itemHeight = (int) (SportsApp.ScreenHeight * 0.24);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    private class ViewHolder {
        private TextView title;
        private ImageView detilsImg;
        private TextView detils;
        private TextView goal;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = (LinearLayout) mInflater.inflate(
                    R.layout.sports_group_all_list_item, null);
           /* AbsListView.LayoutParams param = new AbsListView.LayoutParams(
	              android.view.ViewGroup.LayoutParams.FILL_PARENT,
	              android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	        convertView.setLayoutParams(param);*/
            holder.title = (TextView) convertView
                    .findViewById(R.id.title_name);
            holder.detilsImg = (ImageView) convertView
                    .findViewById(R.id.detils_img);
			/*FrameLayout.LayoutParams lp=(FrameLayout.LayoutParams)holder.detilsImg.getLayoutParams();
			lp.width=(int) (SportsApp.ScreenWidth * 0.5);
			holder.detilsImg.setLayoutParams(lp);*/
            holder.detils = (TextView) convertView
                    .findViewById(R.id.detils_text);
            holder.goal = (TextView) convertView
                    .findViewById(R.id.goal_text);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.detilsImg.setImageDrawable(null);
        Log.e("---", "position--------" + position
                + "----titlename----" + mList.get(position).getImg());
        if (mList.get(position).getImg() != null
                && !"".equals(mList.get(position).getImg())) {
				/*holder.iconImg
						.setBackgroundResource(R.drawable.sports_user_edit_portrait);
			if (!SportsApp.DEFAULT_ICON
					.equals(mList.get(position).getImg())) {
				mDownloader.download(mList.get(position).getImg(),
						holder.iconImg, null);*/
            mDownloader.download(mList.get(position).getImg(),
                    holder.detilsImg, null);
        }
        Log.e("---", "position--------" + position
                + "----titlename----" + mList.get(position).getTitlename());
        Log.e("---", "position--------" + position
                + "----detiles----" + mList.get(position).getDetils());
        Log.e("---", "position--------" + position
                + "----goal----" + mList.get(position).getGoal());
        holder.title.setText(mList.get(position).getTitlename());
        holder.detils.setText(mList.get(position).getDetils());
        float a = mList.get(position).getGoal();
        float b = (float) (Math.round(a * 100)) / 100;
        Log.e("---", "position--------" + position
                + "----b----" + b);
        holder.goal.setText("" + b + mContext.getResources().getString(R.string.sports_goal_text));
        AbsListView.LayoutParams param = new AbsListView.LayoutParams(
                itemWidth,
                itemHeight);
        convertView.setLayoutParams(param);

        return convertView;
    }


}
