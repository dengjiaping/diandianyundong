package cn.ingenic.indroidsync.camera;

import cn.ingenic.indroidsync.DefaultSyncManager;
import cn.ingenic.indroidsync.DefaultSyncManager.OnChannelCallBack;
import cn.ingenic.indroidsync.Module;
import cn.ingenic.indroidsync.Transaction;

import android.content.Intent;
import android.util.Log;

import java.util.UUID;

public class CameraModule extends Module {

    public static final String TAG = "CameraModule";

    public static final String CAMERA = "CAMERA";
    private static final String PREVIEW_SETTING = "camera_window_settings";
    private static boolean mSettingHasWindow;
    private static CameraWindowService.PreviewWindow mServer;

    public static final int HIDE_PREVIEW_WINDOW = 101;

    private static OnChannelCallBack mCallback;

    public CameraModule() {
	super(CAMERA, new String[]{PREVIEW_SETTING});
	mSettingHasWindow = DefaultSyncManager.getDefault().isFeatureEnabled(PREVIEW_SETTING);
	setServer(null);
    }

    @Override
    protected Transaction createTransaction() {
	return new CameraTransaction();
    }

    public static void setServer(CameraWindowService.PreviewWindow server){
	mServer = server;
    }

    @Override
    public void onConnectivityStateChange(boolean connected) {
    	super.onConnectivityStateChange(connected);
	Log.i(TAG,"Phone: connetivity state change: connection is "+connected);
	if (!connected && mServer != null){
	    Log.i(TAG,"will onClosePreviewWindowNoNotify");
	    mServer.onClosePreviewWindowNoNotify();
	}
    }

    @Override
    public void onFeatureStateChange(String feature, boolean enabled) {
	if (feature.equals(PREVIEW_SETTING)){
	    mSettingHasWindow = enabled;
	    Log.i(TAG, "camera_window_settings---------------------------------"+mSettingHasWindow);
	    if (mServer != null){
		if (enabled){
		    mServer.displayWindow(mSettingHasWindow);
		}else{
		    Intent i = new Intent(mServer.getContext(),cn.ingenic.indroidsync.camera.CameraWindowService.class);
		    i.putExtra("requestId", HIDE_PREVIEW_WINDOW);
		    mServer.getContext().startService(i);
		}
	    }
	}
    }

    public static boolean getWindowSetting(){
	return mSettingHasWindow;
    }

    public static void setChannelCallBack(OnChannelCallBack cb) {
	mCallback = cb;
    }

    public OnChannelCallBack getChannelCallBack(UUID uuid) {
	return mCallback;
    }

}
