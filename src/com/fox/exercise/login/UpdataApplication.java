package com.fox.exercise.login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.fox.exercise.R;

import java.io.File;

public class UpdataApplication {

    Context mContext;
    ProgressDialog m_dialog;
    private String download_url;
    FileDownloader fd;
    private String savePathString;


    public UpdataApplication() {
        super();
        // TODO Auto-generated constructor stub
    }

    public UpdataApplication(Context context) {
        super();
        // TODO Auto-generated constructor stub
        mContext = context;
        savePathString = mContext.getFilesDir().getPath() + "/tmp.apk";
        fd = new FileDownloader(context, handler, savePathString);

    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (!Thread.currentThread().isInterrupted()) {
                switch (msg.what) {
                    case 1:
                        int x = fd.getDownedFileLength() * 100 / fd.getFileLength();
                        m_dialog.setProgress(x);
                        break;
                    case 2:
                        m_dialog.dismiss();

                        File file = new File(savePathString);
                        MessageBox(Toast.LENGTH_SHORT, mContext.getString(R.string.download_finished));
                        Intent intent = new Intent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setAction(android.content.Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                        mContext.startActivity(intent);
                        if (message.getForce_update().equals("1")) {
                            System.exit(0);
                        }
                        break;
                    case 3: {
                        m_dialog.dismiss();
                        MessageBox(Toast.LENGTH_SHORT,
                                mContext.getString(R.string.download_apk_failed));
                        if (message.getForce_update().equals("1")) {
                            System.exit(0);
                        }
                        break;
                    }
                    default:
                        break;
                }
            }
        }
    };
    private final int UPDATE_MESSAGE_NETWORKERR = 1;
    private final int UPDATE_MESSAGE_SERVERERR = 2;
    private final int UPDATE_MESSAGE_CANCEL = 3;
    private final int UPDATE_MESSAGE_UPDATE = 4;
    private Handler messageHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (misCanceled) return;
            if (!Thread.currentThread().isInterrupted()) {
                switch (msg.what) {
                    case UPDATE_MESSAGE_NETWORKERR:
                        if (!misOnResume) {
                            MessageBox(Toast.LENGTH_LONG, mContext.getString(R.string.error_cannot_access_net));
                            showProgressDialog(false);
                        }
                        break;
                    case UPDATE_MESSAGE_SERVERERR:
                        if (!misOnResume) {
                            MessageBox(Toast.LENGTH_LONG, mContext.getString(R.string.acess_server_error));
                            showProgressDialog(false);
                        }
                        break;
                    case UPDATE_MESSAGE_CANCEL:
                        misCanceled = true;
                        break;
                    case UPDATE_MESSAGE_UPDATE:
                        try {
                            if (misOnResume) {
                                InitUpdateInfo();
                            } else {
                                showProgressDialog(false);
                                UpdateApp();
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    };

    private void MessageBox(int lengthShrort, String string) {
        // TODO Auto-generated method 
        Toast toast = Toast.makeText(mContext, string, lengthShrort);
        toast.show();
    }

    private UpdateMessage message;

    private void InitUpdateInfo() throws Exception {
        if (misCanceled) {
            return;
        }
        String s = getVersionName(mContext);
        int n = getVersionNum(mContext);
        if (message == null)
            return;

        /** have new version update */
        if (!s.equals(message.getVersionName()) && (n < message.getVersionCode()) && message.getVersionName() != null) {
            String ss = message.getForce_update().equals("1") ? mContext.getString(R.string.yes) : mContext.getString(R.string.no);
            AlertDialog dlg = new AlertDialog.Builder(mContext)
                    .setTitle(mContext.getString(R.string.update_info))
                    .setMessage(
                            mContext.getString(R.string.version_info) + ":" + message.getVersionName() + "\n"
                                    + mContext.getString(R.string.update_info) + ":" + message.getUpdate_info() + "\n"
                                    + mContext.getString(R.string.file_size) + ":" + message.getFilesize() + "\n"
                                    + mContext.getString(R.string.update_date) + ":" + message.getUpdate_date() + "\n"
                                    + mContext.getString(R.string.is_force_update) + ":" + ss)
                    .setPositiveButton(mContext.getString(R.string.update), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            download_url = message.getDownload_url();
                            m_dialog = new ProgressDialog(mContext);
                            m_dialog.setTitle(mContext.getString(R.string.downloading_new_version));
                            m_dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            m_dialog.setButton(DialogInterface.BUTTON_POSITIVE, mContext.getString(R.string.button_cancel),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            fd.setCancel(true);
                                            m_dialog.dismiss();
                                            if (message.getForce_update().equals("1")) {
                                                System.exit(0);
                                            }
                                        }
                                    });
                            m_dialog.setCanceledOnTouchOutside(false);
                            m_dialog.show();
                            Thread thread = new Thread() {
                                public void run() {
                                    try {
                                        fd.DownFile(download_url);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            thread.start();
                        }
                    }).setNegativeButton(mContext.getString(R.string.button_cancel), new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            if (message.getForce_update().equals("1")) {
                                System.exit(0);
                            }
                        }
                    }).setOnKeyListener(new OnKeyListener() {
                        @Override
                        public boolean onKey(DialogInterface dialog, int keyCode,
                                             KeyEvent event) {
                            if (keyCode == KeyEvent.KEYCODE_BACK
                                    && event.getRepeatCount() == 0) {
                                if (message.getForce_update().equals("1")) {
                                    System.exit(0);
                                }
                            }
                            return false;
                        }

                    }).create();
            dlg.setCanceledOnTouchOutside(false);
            dlg.show();

        }
    }

    private int getVersionNum(Context context) {
        int versionCode = 0;
        try {
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;

    }

    public String getVersionName(Context context) {
        String versionName = "";
        try {
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;

    }

    private boolean UpdateApp() {
        if (misCanceled) {
            return false;
        }
        if (message == null)
            return false;

//        try {
//            message = new UpdateMessage();
//        } catch (Exception e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//            MessageBox(Toast.LENGTH_LONG, mContext.getString(R.string.error_cannot_access_net));
//            return false;
//        }

        String s = getVersionName(mContext);
        int n = getVersionNum(mContext);
        if (message == null || (message.getVersionName() == null)) {
            MessageBox(Toast.LENGTH_LONG, mContext.getString(R.string.error_cannot_access_net));
        } else if (!s.equals(message.getVersionName()) && (n < message.getVersionCode()) && message.getVersionName() != null) {

            String ss = message.getForce_update().equals("1") ? mContext.getString(R.string.yes) : mContext.getString(R.string.no);
            AlertDialog dlg = new AlertDialog.Builder(mContext)
                    .setTitle(mContext.getString(R.string.update_info))
                    .setMessage(
                            mContext.getString(R.string.version_info) + ":" + message.getVersionName() + "\n"
                                    + mContext.getString(R.string.update_info) + ":" + message.getUpdate_info() + "\n"
                                    + mContext.getString(R.string.file_size) + ":" + message.getFilesize() + "\n"
                                    + mContext.getString(R.string.update_date) + ":" + message.getUpdate_date() + "\n"
                                    + mContext.getString(R.string.is_force_update) + ":" + ss)
                    .setPositiveButton(mContext.getString(R.string.update), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            download_url = message.getDownload_url();
                            m_dialog = new ProgressDialog(mContext);
                            m_dialog.setTitle(mContext.getString(R.string.downloading_new_version));
                            m_dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            m_dialog.setButton(DialogInterface.BUTTON_NEGATIVE, mContext.getString(R.string.button_cancel),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            fd.setCancel(true);
                                            dialog.dismiss();
                                        }
                                    });

                            m_dialog.show();
                            Thread thread = new Thread() {
                                public void run() {
                                    try {
                                        fd.DownFile(download_url);
                                    } catch (Exception e) {

                                    }
                                }
                            };
                            thread.start();
                        }
                    }).setNegativeButton(mContext.getString(R.string.button_cancel), new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create();
            dlg.setCanceledOnTouchOutside(false);
            dlg.show();
        } else {
            MessageBox(Toast.LENGTH_LONG, mContext.getString(R.string.already_newest));
        }
        return false;

    }

    private ProgressDialog mypDialog;

    private void showProgressDialog(boolean bshow) {
        if (mypDialog == null) {
            mypDialog = new ProgressDialog(mContext);
            mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mypDialog.setMessage(mContext.getResources().getString(R.string.getting_updateinfo));
            mypDialog.setIndeterminate(false);
            mypDialog.setCancelable(true);
            mypDialog.setOnCancelListener(new OnCancelListener() {

                @Override
                public void onCancel(DialogInterface arg0) {
                    messageHandler.sendEmptyMessage(UPDATE_MESSAGE_CANCEL);
                    mypDialog.dismiss();
                }
            });
        }
        if (bshow) {
            mypDialog.show();
        } else {
            mypDialog.dismiss();
        }

    }

    private boolean misOnResume;
    private boolean misCanceled = false;

    public void UpdateAppBackground(boolean isOnResume) {
        misOnResume = isOnResume;
        if (!misOnResume) {
            showProgressDialog(true);
        }
        UpdataAppBackgroundThread updataAppBackgroundThread =
                new UpdataAppBackgroundThread();
        updataAppBackgroundThread.start();
    }

    public class UpdataAppBackgroundThread extends Thread {


        public UpdataAppBackgroundThread() {
            super();
        }

        @Override
        public void run() {
            try {
                message = null;
                message = new UpdateMessage(mContext);
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                Log.d("yuyang", "UPDATE_MESSAGE_NETWORKERR");
                messageHandler.sendEmptyMessage(UPDATE_MESSAGE_NETWORKERR);
                return;
            }
            if (message == null || (message.getVersionName() == null)) {
                Log.d("yuyang", "UPDATE_MESSAGE_SERVERERR");
                messageHandler.sendEmptyMessage(UPDATE_MESSAGE_SERVERERR);
            } else {
                Log.d("yuyang", "UPDATE_MESSAGE_UPDATE");
                messageHandler.sendEmptyMessage(UPDATE_MESSAGE_UPDATE);

            }
            return;
        }

    }

}
