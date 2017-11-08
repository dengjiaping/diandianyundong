package com.fox.exercise.newversion.adapter;

import java.util.ArrayList;


import com.fox.exercise.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MsgboxNewAdapter extends BaseAdapter {
    String[] title;
    int[] imgid = {R.drawable.sports_focus_box, R.drawable.sports_fans_box, R.drawable.mysports_invite_btn2,
            R.drawable.sports_sys_message_box};
    private ArrayList<Integer> al;
    private LayoutInflater mInflater;
    private Context mContext;

    public MsgboxNewAdapter(Context context, ArrayList<Integer> al) {
        // TODO Auto-generated constructor stub
        this.al = al;
        title = new String[]{context.getString(R.string.sports_request),
                context.getString(R.string.sports_visitors),
                context.getString(R.string.sports_sys_message)};
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    public MsgboxNewAdapter(Context context, ArrayList<Integer> al,
                            boolean isMyself) {
        // TODO Auto-generated constructor stub
        this.al = al;
        title = new String[]{context.getString(R.string.title_activity),
                context.getString(R.string.slimgirl_sort),
                context.getString(R.string.rankboard_friend),
                context.getString(R.string.slimgirl_sort),
                context.getString(R.string.sports_coolmall)};
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return title.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return title[position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        // TODO Auto-generated method stub
        if (mContext.getResources().getString(R.string.sports_sys_message)
                .equals(title[position])) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public int getViewTypeCount() {
        // TODO Auto-generated method stub
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = new ViewHolder();
        int type = getItemViewType(position);
        try {
            if (convertView == null) {
                switch (type) {
                    case 0:
                        convertView = mInflater.inflate(R.layout.msgbox_new_item,
                                null);
                        break;
                    case 1:
                        convertView = mInflater.inflate(
                                R.layout.msgbox_new_other_item, null);
                        break;

                }
                holder.img = (ImageView) convertView.findViewById(R.id.img);
                holder.txt = (TextView) convertView.findViewById(R.id.boxtxt);
                holder.tiptxt = (Button) convertView
                        .findViewById(R.id.focusText);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.img.setImageResource(imgid[position]);
            holder.txt.setText(title[position]);
            if (al.get(position) != 0) {
                holder.tiptxt.setVisibility(View.VISIBLE);
                holder.tiptxt.setText(al.get(position) >= 100 ? "99+" : (al
                        .get(position) + ""));
            } else {
                holder.tiptxt.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            // Log.e("Error", e.getMessage());
        }

        return convertView;
    }

    public class ViewHolder {
        public Button tiptxt;
        public TextView txt;
        public ImageView img;

    }

}
