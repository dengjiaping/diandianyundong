package com.fox.exercise.weibo.tencent;

import java.util.List;

import com.fox.exercise.weibo.tencent.proxy.QQWeiboProxy;

import android.content.Context;
import android.util.Log;

public class TXUpload {

    public void upload(Context context, String content, String path) {
        QQWeiboProxy mQqWeiboProxy = QQWeiboProxy.getInstance();
        TencentDataHelper tencent_dataHelper = new TencentDataHelper(context);
        List<UserInfo> TuserInfoList = tencent_dataHelper.GetUserList(true);
        if (!TuserInfoList.isEmpty()) {
            mQqWeiboProxy.setAccesToakenString(TuserInfoList.get(0).getToken());
            mQqWeiboProxy.setExpireIn(TuserInfoList.get(0).getExpiresIn());
            mQqWeiboProxy.setOpenid(TuserInfoList.get(0).getOpenID());
            mQqWeiboProxy.setOpenKey(TuserInfoList.get(0).getOpenKey());
        }
        tencent_dataHelper.Close();

        if (path != null && !path.equals("")) {
            mQqWeiboProxy.sendPicWeibo(content, path);
        } else {
            mQqWeiboProxy.sendWeibo(content);
        }

    }
}
