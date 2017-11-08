package cn.ingenic.indroidsync;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.fox.exercise.R;
import com.fox.exercise.http.StatCounts;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class CrashHandler implements UncaughtExceptionHandler {
    public static final String TAG = "CrashHandler";
    StringBuffer sbs = new StringBuffer();
    //系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    //CrashHandler实例
    private static CrashHandler INSTANCE = new CrashHandler();
    //程序的Context对象
    private Context mContext;
    //用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<String, String>();
    public int config = 0;
    //用于格式化日期,作为日志文件名的一部分
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashHandler() {
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        mContext = context;
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    public void uncaughtException(Thread thread, Throwable ex) {
        // TODO Auto-generated method stub
        if (!handleException(ex) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        }/*else {
           try {
               Thread.sleep(5000);
             //  mDefaultHandler.uncaughtException(thread, ex);
           } catch (InterruptedException e) {
               Log.e(TAG, "error : ", e);
           }
           //退出程序
          android.os.Process.killProcess(android.os.Process.myPid());
           System.exit(1);
       } */


    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        config = mContext.getResources().getInteger(R.integer.config_uploaderror);
        if (config == 0) {
            return false;
        }
        if (ex == null) {
            return false;
        }
        final Throwable exsThrowable = ex;
        //使用Toast来显示异常信息
//        new Thread() {
//            @Override
//            public void run() {
//                Looper.prepare();
//                showDialog(mContext);
//                Looper.loop();
//            }
//        }.start();
      /* String versioninfo = getVersionInfo();

       // 2.获取手机的硬件信息.
       String mobileInfo  = getMobileInfo();

       // 3.把错误的堆栈信息 获取出来
*/
        String errorinfo = getErrorInfo(ex);
        //  String time = formatter.format(new Date());
      /* sbs.append(time);
       sbs.append("\n");
       sbs.append("软件版本:"+versioninfo);
       sbs.append("\n");
       sbs.append("手机型号"+mobileInfo);
       sbs.append("\n");  */
        sbs.append(errorinfo);
        StatCounts.ReportBug(mContext, sbs.toString());
        new Thread(new Runnable() {

            public void run() {
                // TODO Auto-generated method stub
                try {
                    Thread.sleep(3000);

                } catch (InterruptedException e) {
                    Log.e(TAG, "error : ", e);
                }
                //退出程序
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        }).start();
        return true;
    }

    private void showDialog(final Context context) {
        final Dialog dialog;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dialog = new Dialog(mContext,
                R.style.sports_dialog);
        View v = inflater.inflate(R.layout.sports_dialog, null);
        ((TextView) v.findViewById(R.id.message)).setText("由于发生了一个未知错误，云狐运动应用已关闭，我们对此引起的不便表示抱歉！" +
                "您可以将错误信息上传到我们的服务器，帮助我们尽快解决该问题，谢谢！");
        Log.i(TAG, sbs.toString());
        TextView tv = (TextView) v.findViewById(R.id.title);
        tv.setText("云狐运动温馨提示");
        v.findViewById(R.id.bt_ok).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View arg0) {
                        dialog.dismiss();
                        StatCounts.ReportBug(mContext, sbs.toString());
                        Toast.makeText(mContext, "感谢您对云狐运动的支持与帮助！", Toast.LENGTH_LONG).show();
                        new Thread(new Runnable() {

                            public void run() {
                                // TODO Auto-generated method stub
                                try {
                                    Thread.sleep(3000);

                                } catch (InterruptedException e) {
                                    Log.e(TAG, "error : ", e);
                                }
                                //退出程序
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
                            }
                        }).start();
                    }
                });
        v.findViewById(R.id.bt_cancel).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View arg0) {
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    }
                });
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.setCanceledOnTouchOutside(false);//设置点击屏幕其他地方，dialog不消失
        dialog.setCancelable(false);//设置点击返回键和HOme键，dialog不消失
        dialog.setContentView(v);
        dialog.show();

        Log.i("PLog", "2");
    }


    //打印错误信息
    private String getErrorInfo(Throwable arg1) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        arg1.printStackTrace(printWriter);
        Throwable cause = arg1.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String error = writer.toString();
        return error;
    }
    /**
     * 获取手机的硬件信息
     * @return
     *//*
   private String getMobileInfo() {
       StringBuffer sb = new StringBuffer();
       sb.append(""+android.os.Build.BRAND + android.os.Build.MODEL);
       sb.append("\n");
       return sb.toString();
   }

   *//**
     * 获取软件的版本信息
     * @return
     *//*
   private String getVersionInfo(){
       try {
           PackageManager pm = mContext.getPackageManager();
             PackageInfo info =pm.getPackageInfo(mContext.getPackageName(), 0);
             return  info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "版本号未知";
        }
    }  */

}
