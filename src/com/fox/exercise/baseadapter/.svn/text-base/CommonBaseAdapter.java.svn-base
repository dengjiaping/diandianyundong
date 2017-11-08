package com.fox.exercise.baseadapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 *
 *@author suhu
 *@time 2016/9/28 11:16
 *封装该类的目的：
 * 1.减少重复代码
 * 2.item的复用
 * 3.开发时方便，再次用到就不用在外部创建ViewHolder类
 *
 *
*/
public abstract class CommonBaseAdapter<T> extends BaseAdapter {
    public List<T> list;//封装有数据的数据源
    private Context context;
    private int resId;//listview item 的资源 id

    public CommonBaseAdapter(List<T> list, Context context, int resId) {
        this.list = list;
        this.context = context;
        this.resId = resId;
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return list!=null?list.get(position):null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.getHolder(convertView, context, resId);
        //需要显示内容
        setData(holder,position);
        return holder.getmConvertView();
    }


    /**
     *@method 抽象方法中设置值
     *@author suhu
     *@time 2016/9/28 11:22
     *@param viewHolder
     *@param position
     *
    */
    public abstract void setData(ViewHolder viewHolder,int position);
}
