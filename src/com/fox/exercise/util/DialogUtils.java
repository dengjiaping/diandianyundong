package com.fox.exercise.util;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.fox.exercise.R;

import cn.ingenic.indroidsync.SportsApp;

/**
 * Created by suhu on 2016/9/28.
 */
public class DialogUtils {


    /**
     *@method 加载数据弹出对话框
     *@author suhu
     *@time 2016/9/28 11:02
     *@param context
     *@param str 弹出对话框要显示的文字
     *return 返回Dialog对象，用于调用者管理
    */
    public static Dialog showWaitDialog (Context context,String str){
        Dialog waitProgressDialog = new Dialog(context, R.style.sports_dialog);
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v1 = mInflater.inflate(R.layout.sports_progressdialog, null);
        TextView message = (TextView) v1.findViewById(R.id.message);
        message.setText(str);
        v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
        waitProgressDialog.setContentView(v1);
        waitProgressDialog.show();
        return waitProgressDialog;
    }



}
