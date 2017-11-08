package com.fox.exercise;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fox.exercise.api.entity.ActionRankList;
import com.fox.exercise.util.RoundedImage;

public class ActivityRankFragmentAdaptre extends BaseAdapter {


    private Context mcontext;
    private LayoutInflater mInflater;
    private List<ActionRankList> mActionRankLists;
    private ImageDownloader mDownloader = null;


    public ActivityRankFragmentAdaptre(Context mcontext, List<ActionRankList> mActionRankLists) {

        super();
        this.mcontext = mcontext;
        this.mActionRankLists = mActionRankLists;
        this.mInflater = LayoutInflater.from(mcontext);
        this.mDownloader = new ImageDownloader(mcontext);
    }

    @Override
    public int getCount() {
        return mActionRankLists.size();
    }

    @Override
    public Object getItem(int arg0) {

        return mActionRankLists.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {

        return arg0;
    }

    public class ViewHodler {
        RoundedImage cover_user_photo;
        TextView username, usercal;
        ImageView ranking;

    }

    @Override
    public View getView(int position, View contentView, ViewGroup arg2) {
        ViewHodler vhHodler = null;
        if (vhHodler == null) {
            vhHodler = new ViewHodler();
            contentView = mInflater.inflate(R.layout.activityrankdetail, null);
            RoundedImage mRoundedImage = (RoundedImage) contentView.findViewById(R.id.cover_user_photo);
            TextView musername = (TextView) contentView.findViewById(R.id.username);
            TextView musercal = (TextView) contentView.findViewById(R.id.usercal);
            ImageView mranking = (ImageView) contentView.findViewById(R.id.ranking);
            vhHodler.cover_user_photo = mRoundedImage;
            vhHodler.username = musername;
            vhHodler.usercal = musercal;
            vhHodler.ranking = mranking;
            contentView.setTag(vhHodler);
        } else {
            vhHodler = (ViewHodler) contentView.getTag();
        }
        ActionRankList actionRankList = mActionRankLists.get(position);
        mDownloader.setType(ImageDownloader.ICON);
        mDownloader.download(actionRankList.getUserIcon(), vhHodler.cover_user_photo, null);
        vhHodler.username.setText(actionRankList.getUserName());
        //截取字符串
        int endIndex = actionRankList.getCalories().indexOf(".");
        String newCloriesString = actionRankList.getCalories().substring(0, endIndex);
        vhHodler.usercal.setText(newCloriesString);
        if (position == 0) {
            vhHodler.ranking.setBackgroundResource(R.drawable.rank_one);
        } else if (position == 1) {
            vhHodler.ranking.setBackgroundResource(R.drawable.rank_two);
        } else if (position == 2) {
            vhHodler.ranking.setBackgroundResource(R.drawable.rank_three);
        } else {
            vhHodler.ranking.setBackgroundColor(mcontext.getResources().getColor(R.color.white));
        }

        return contentView;
    }

}
