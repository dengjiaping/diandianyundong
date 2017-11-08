package cn.ingenic.indroidsync.camera;

import cn.ingenic.indroidsync.Config;
import cn.ingenic.indroidsync.DefaultSyncManager;
import cn.ingenic.indroidsync.LogTag.Client;
import cn.ingenic.indroidsync.Transaction;
import cn.ingenic.indroidsync.data.DefaultProjo;
import cn.ingenic.indroidsync.data.Projo;
import cn.ingenic.indroidsync.data.ProjoType;

import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.EnumSet;

public class CameraTransaction extends Transaction {
    public static final String TAG = "CameraTransaction >> phone";

    //the requests which will be send from watch to phone. 
    public static final int OPEN_CAMERA_REQUEST = 0;
    public static final int TAKE_PICTURE_REQUEST = 1;
    public static final int SWITCH_CAMERA_REQUEST = 2;
    public static final int EXIT_CAMERA_REQUEST = 3;

    //the responses from phone.
    public static final int OPEN_RESULT_RESPONSE = 0;
	public static final int TAKE_RESULT_RESPONSE = 1;
    public static final int EXIT_CAMERA_RESPONSE = 2;

	//the content of open result.
	public static final int OPEN_RESULT_SUCCESS = 0;
	public static final int OPEN_RESULT_FAILED = 1;
	public static final int OPEN_RESULT_FAILED_POWER = 2;
	public static final int OPEN_RESULT_FAILED_SENSOR = 3;
    public static final int OPEN_RESULT_FAILED_CHANNEL = 4;

    public void openFailedPower(){
		ArrayList<Projo> datas = new ArrayList<Projo>(2);
		Projo projoTitle = new DefaultProjo(EnumSet.of(CameraColumn.phoneResponseState), ProjoType.DATA);
		projoTitle.put(CameraColumn.phoneResponseState, CameraTransaction.OPEN_RESULT_RESPONSE);
		datas.add(projoTitle);

		Projo projoContent = new DefaultProjo(EnumSet.of(CameraColumn.openCameraResult), ProjoType.DATA);
		projoContent.put(CameraColumn.openCameraResult, OPEN_RESULT_FAILED_POWER);
		datas.add(projoContent);

		Config config = new Config(CameraModule.CAMERA);
        DefaultSyncManager.getDefault().request(config, datas);
    }

    @Override
    public void onStart(ArrayList<Projo> datas) {
        super.onStart(datas);
	PowerManager pm = (PowerManager)mContext.getSystemService(Context.POWER_SERVICE);
	if (!pm.isScreenOn()){
	    openFailedPower();
	    return;
	}

	Projo data = datas.get(0);
	if (data == null) {
	    Client.e("datas[0] is null.");
	    return;
	}

	Integer request = (Integer) data.get(CameraColumn.watchRequest);
	Log.i(TAG, "receive a request from watch ---------------------------------"+request.intValue());

	if (request == OPEN_CAMERA_REQUEST){
	    Integer max = (Integer) datas.get(1).get(CameraColumn.maxScreen);
	    sendRequestToService(request.intValue(), max.intValue());
	    return;
	}

	sendRequestToService(request.intValue());
    }

    //Don't use binder in the same process.
    private void sendRequestToService(int id){
	Intent i = new Intent(mContext,cn.ingenic.indroidsync.camera.CameraWindowService.class);
	i.putExtra("requestId", id);
	mContext.startService(i);
    }

    private void sendRequestToService(int id, int max){
	Intent i = new Intent(mContext,cn.ingenic.indroidsync.camera.CameraWindowService.class);
	i.putExtra("requestId", id);
	i.putExtra("maxBound", max);
	mContext.startService(i);
    }
}
