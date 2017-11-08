package com.fox.exercise;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.fox.exercise.api.entity.SysMsg;
import com.fox.exercise.newversion.newact.CelebrationMainActivity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SysMessageMyAdapter extends BaseAdapter {

    private ArrayList<SysMsg> mList = null;
    private Context mContext = null;
    private LayoutInflater mInflater = null;

    private ImageDownloader mDownloader = null;

    public SysMessageMyAdapter(ArrayList<SysMsg> list, Context context) {
        this.mContext = context;
        this.mList = list;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDownloader = new ImageDownloader(mContext);
        mDownloader.setType(ImageDownloader.OnlyOne);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        RelativeLayout layout = null;

        if (convertView == null) {
            layout = (RelativeLayout) mInflater.inflate(
                    R.layout.sports_sys_message, null);
        } else {
            layout = (RelativeLayout) convertView;
        }

        TextView messageTxt = (TextView) layout.findViewById(R.id.tv_message);
        ImageView img_icon = (ImageView) layout.findViewById(R.id.iv_tupian);
        if (mList.get(position).getContent() != null
                && !"".equals(mList.get(position).getContent())) {
            messageTxt.setVisibility(View.VISIBLE);
            messageTxt.setText(mList.get(position).getContent());
        } else {
            messageTxt.setVisibility(View.GONE);
        }
        if (mList.get(position).getImg() != null
                && !"".equals(mList.get(position).getImg())) {
            img_icon.setVisibility(View.VISIBLE);
            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            ViewGroup.LayoutParams lp = img_icon.getLayoutParams();
            lp.height = (wm.getDefaultDisplay().getWidth() - 44) * 9 / 16;
            img_icon.setLayoutParams(lp);
            mDownloader.download(mList.get(position).getImg(), img_icon, null);
            img_icon.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(mContext,
                            CelebrationMainActivity.class);
                    intent.putExtra("Url", mList.get(position).getUrl());
                    intent.putExtra("shareurl", mList.get(position).getShareurl());
                    mContext.startActivity(intent);
                }
            });
        } else {
            img_icon.setVisibility(View.GONE);
        }
        messageTxt
                .setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

                    @Override
                    public void onCreateContextMenu(ContextMenu menu, View v,
                                                    ContextMenuInfo menuInfo) {
                        // TODO Auto-generated method stub
                        menu.add(0, 0, 0, R.string.copy);
                    }
                });

        TextView timeTxt = (TextView) layout.findViewById(R.id.tv_time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        timeTxt.setText(format.format(mList.get(position).getAddTime() * 1000));
        return layout;
    }
}
