package cn.ingenic.indroidsync.updater;

import java.io.File;

import cn.ingenic.indroidsync.DefaultSyncManager;
import cn.ingenic.indroidsync.Module;
import cn.ingenic.indroidsync.RemoteBinderImpl;
import cn.ingenic.indroidsync.DefaultSyncManager.OnFileChannelCallBack;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
/**
 * for update sync, to get watch's name & version info
 * @author dfdun*/
public class UpdaterModule extends Module {

    public static final String TAG = "M-UPDATER";
    public static final boolean V = true;

    static final String UPDATER = "UPDATER";
    private Context mContext;
    
    public UpdaterModule() {
        super(UPDATER);
    }

    protected void onCreate(Context context) {
        mContext = context;
        if (V) {
            Log.d(TAG, "UpdaterModule created.");
        }

            registRemoteService(IUpdaterRemoteService.DESPRITOR,
                    new RemoteBinderImpl(UPDATER,
                            IUpdaterRemoteService.DESPRITOR));
        
    }
    
    private OnFileChannelCallBack mFileChannelCallBack = new OnFileChannelCallBack() {

        @Override
        public void onRetriveComplete(String name, boolean success) {
            Log.i(TAG, "onRetriveComplete] file " + name + ", " + success);
            Toast.makeText(mContext, name + ", " + success, Toast.LENGTH_SHORT)
                    .show();

            Intent intent = new Intent(mContext, NoticesActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("msg", "onRetriveComplete] file " + name + ", " + success+" Install update! ! !");
            intent.putExtra("update", "yes");
            intent.putExtra("filename", name);
            if (success)
                mContext.startActivity(intent);
        }

		@Override
		public void onSendComplete(String name, boolean success) {
			Log.i(TAG, "onSendComplete]OTAfile " + name + ", " + success);
			if (!success)
				return;
			((NotificationManager) mContext.getSystemService("notification"))
					.cancel(UpdateInstallActivity.NOTIFICATION_TAG,
							UpdateInstallActivity.NOTIFICATION_ID);
			File ota = new File(
					UpdateUtils.getStringFromSP(mContext, "file_pa"));
			if (ota != null && ota.exists())
				ota.delete();
		}
        
    };

    @Override
    public OnFileChannelCallBack getFileChannelCallBack() {
        // TODO Auto-generated method stub
        return mFileChannelCallBack;
    }

}
