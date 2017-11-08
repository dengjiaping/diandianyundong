package com.fox.exercise;

import java.util.ArrayList;

import com.fox.exercise.SportsGroupAll.GroupAll;

import com.fox.exercise.SportsGroupMe.GroupMe;
import com.fox.exercise.util.RoundedImage;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.ingenic.indroidsync.SportsApp;

public class SportsGroupMeAdapter extends BaseAdapter {
    //	private Context mContext = null;
    private ArrayList<GroupMe> mList = null;
    //	private SportsApp mSportsApp;
    private LayoutInflater mInflater = null;
    private ImageDownloader mDownloader = null;
    private Dialog loadProgressDialog = null;

    //	private TextView message = null;
    public SportsGroupMeAdapter(Context context,
                                ArrayList<GroupMe> list, SportsApp sportsApp) {
        // TODO Auto-generated constructor stub
//		this.mContext = context;
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
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if (convertView == null) {

            holder = new ViewHolder();
            convertView = (LinearLayout) mInflater.inflate(
                    R.layout.sports_group_me_item_list, null);
            holder.title = (TextView) convertView
                    .findViewById(R.id.sports_group_me_title);
            //给文字加粗
            TextPaint tp = holder.title.getPaint();
            tp.setFakeBoldText(true);

            holder.detilsImg = (ImageView) convertView
                    .findViewById(R.id.sports_group_me_img);
            holder.detils = (TextView) convertView
                    .findViewById(R.id.sports_group_me_detils);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.detilsImg.setImageDrawable(null);
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

        holder.title.setText(mList.get(position).getTitlename());
        holder.detils.setText(mList.get(position).getDetils());

        return convertView;
    }

}
