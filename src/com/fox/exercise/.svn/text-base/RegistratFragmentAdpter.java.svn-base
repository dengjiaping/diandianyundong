package com.fox.exercise;

import java.util.List;

import com.fox.exercise.api.entity.ApplyList;
import com.fox.exercise.util.RoundedImage;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RegistratFragmentAdpter extends BaseAdapter {

    private List<ApplyList> activityApplyLists;
    private Context mContext;
    private LayoutInflater mInflater;
    private ImageDownloader mDownloader = null;


    public RegistratFragmentAdpter(List<ApplyList> activityApplyLists, Context mContext) {
        super();
        this.activityApplyLists = activityApplyLists;
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);

    }

    @Override
    public int getCount() {
        return activityApplyLists.size();
    }

    @Override
    public Object getItem(int arg0) {
        return activityApplyLists.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }


    private class ViewHodler {

        RoundedImage avatar;
        TextView name;
    }

    @Override
    public View getView(int positiion, View contentview, ViewGroup arg2) {

        ViewHodler vh = null;
        if (vh == null) {
            vh = new ViewHodler();
            contentview = mInflater.inflate(R.layout.registrat_fragment_item, null);
            RoundedImage mImageView = (RoundedImage) contentview.findViewById(R.id.avatar);
            TextView mTextView = (TextView) contentview.findViewById(R.id.name);
            vh.avatar = mImageView;
            vh.name = mTextView;
            contentview.setTag(vh);
        } else {
            vh = (ViewHodler) contentview.getTag();
        }
        ApplyList applyPerson = activityApplyLists.get(positiion);
        mDownloader = new ImageDownloader(mContext);
        mDownloader.setType(ImageDownloader.ICON);
        Log.e("hehhe", "********************applyPerson.getUserIcon()=" + applyPerson.getUserIcon());

        mDownloader.download(applyPerson.getUserIcon(), vh.avatar, null);
        vh.name.setText(applyPerson.getUserName());

        return contentview;
    }

}
