package com.fox.exercise.baseadapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
/**
 *
 *@author suhu
 *@time 2016/9/28 11:16
 *
 *
*/
public class ViewHolder {
    private View mConvertView;//用于返回给 listview adapter getview 方法的view
    private SparseArray<View> viewSparseArray = new SparseArray<View>();//推荐使用这个来替代HashMap<integer,E>
    public View getmConvertView() {
        return mConvertView;
    }

    public ViewHolder(Context Context, int resId) {
        mConvertView = LayoutInflater.from(Context).inflate(resId, null);
        mConvertView.setTag(this);//给 view 设置 tag
    }

    public static ViewHolder getHolder(View convertView, Context context, int resId) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder(context,resId);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        return viewHolder;
    }

    public View findViewById(int id) {
        View view = viewSparseArray.get(id);
        if (view != null) {

        }else{
            view=mConvertView.findViewById(id);
            viewSparseArray.append(id, view);
        }
        return view;
    }
}
