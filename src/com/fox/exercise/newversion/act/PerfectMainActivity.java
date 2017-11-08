package com.fox.exercise.newversion.act;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fox.exercise.AllWeiboInfo;
import com.fox.exercise.R;
import com.fox.exercise.SmartBarUtils;
import com.fox.exercise.SportsExceptionHandler;
import com.fox.exercise.SportsUtilities;
import com.fox.exercise.api.ApiBack;
import com.fox.exercise.api.ApiConstant.WeiboType;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.api.entity.UserDetail;
import com.fox.exercise.login.LoginActivity;
import com.fox.exercise.login.LoginByOtherThread;
import com.fox.exercise.login.Tools;
import com.fox.exercise.util.RoundedImage;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;

import cn.ingenic.indroidsync.SportsApp;



/**
 * @author loujungang 完善客户信息页面 第三方登录重名的问题
 */
public class PerfectMainActivity extends Activity implements OnClickListener,
        OnDismissListener {
    private final String TAG = "PerfectMainActivity";
    private RoundedImage perfect_image_icon;
    private EditText perfect_edit_name;
    private TextView perfect_bt_ok, top_title;
    private PopupWindow photoPopWindow;
    private RelativeLayout pop_menu_background;
    private final String IMAGE_FILE_NAME = "faceImage.jpg";
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int RESULT_REQUEST_CODE = 2;
    private BitmapDrawable drawable;
    private final String FACE_PATH = SportsUtilities.DOWNLOAD_SAVE_PATH
            + "/faceImage.jpg";
    private final String FACE_PATH_TWO = SportsUtilities.DOWNLOAD_SAVE_PATH
            + "/qqfaceImage.jpg";
    private String url_photo ;
    SportsApp mSportsApp;
    // 线程SESSION_OUT和NET_ERROR的解决所调用的接口申明
    private SportsExceptionHandler mExceptionHandler = null;
    private String comeFrom;// 表示从哪个页面跳转的
    private String sex ="";
    //	private boolean isfacechanged = false;
    private String weiBo_photo = "facePhoto";
    private SharedPreferences weiBo_sp;
    private String weiBo_type;
    private UserDetail mUserDetail;

    private String weiboType, weiboName, token;
    private Intent intent;
    private String argname;
    private Dialog mLoadProgressDialog = null;
    private TextView mDialogMessage;
    private UserDetail userdetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        boolean findMethod = SmartBarUtils.findActionBarTabsShowAtBottom();
        if (!findMethod) {
            // 取消ActionBar拆分，换用TabHost
            getWindow().setUiOptions(0);
            getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        setContentView(R.layout.activity_perfect_main);
        mSportsApp = (SportsApp) getApplication();
        top_title = (TextView) (findViewById(R.id.top_title));
        top_title.setText(getResources().getString(R.string.complete_info));
        intent = getIntent();
        if (intent != null) {
            comeFrom = intent.getStringExtra("comeFrom");
            weiboType = intent.getStringExtra("weiboType");
            weiboName = intent.getStringExtra("weiboName");
            token = intent.getStringExtra("token");
        }
        if (mSportsApp != null) {
            mExceptionHandler = mSportsApp.getmExceptionHandler();
        }
        // Toast.makeText(PerfectMainActivity.this, "PerfectMainActivity进入",
        // 10).show();
        weiBo_sp = getSharedPreferences("user_login_info", Context.MODE_PRIVATE);
        weiBo_type = weiBo_sp.getString("weibotype", "");
        perfect_image_icon = (RoundedImage) findViewById(R.id.perfect_image_icon);
        perfect_edit_name = (EditText) findViewById(R.id.perfect_edit_name);
        perfect_bt_ok = (TextView) findViewById(R.id.perfect_bt_ok);
        pop_menu_background = (RelativeLayout) findViewById(R.id.send_menu_background);

        perfect_bt_ok.setOnClickListener(this);




        if ("SportMain".equals(comeFrom)) {
            // 表示从SportMain页面来的
            if (mSportsApp.isOpenNetwork()) {
                waitShowDialog();
                (new LoginFinishTask()).execute();
                perfect_image_icon.setOnClickListener(this);
            }


        } else {
            perfect_edit_name.setText(weiboName);
        }

        if (("LoginActivity".equals(comeFrom))){
            LoadImage(AllWeiboInfo.TENCENT_QQZONE_PHOTO,perfect_image_icon);
        }


        if (findMethod) {
            getActionBar().setDisplayShowHomeEnabled(false);
            getActionBar().setDisplayShowTitleEnabled(false);
            SmartBarUtils.setActionModeHeaderHidden(getActionBar(), true);
            SmartBarUtils.setActionBarViewCollapsable(getActionBar(), true);
        }
    }

    // 选中头像
    public void shotSelectImages() {
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout myView = (LinearLayout) inflater.inflate(
                R.layout.select_images_from_local1, null);

        myView.findViewById(R.id.btn_paizhao).setOnClickListener(this);
        myView.findViewById(R.id.btn_select_pic).setOnClickListener(this);
        // myView.findViewById(R.id.btn_cancle).setOnClickListener(this);

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        photoPopWindow = new PopupWindow(myView, width - SportsApp.dip2px(40),
                RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        // photoPopWindow.setAnimationStyle(R.style.AnimationPopup);
        photoPopWindow.setOutsideTouchable(true);
        photoPopWindow.setBackgroundDrawable(new BitmapDrawable());
        photoPopWindow.showAtLocation(top_title, Gravity.CENTER, 0, 0);
        photoPopWindow.setOnDismissListener(this);
        // final Animation animation = (Animation) AnimationUtils.loadAnimation(
        // this, R.anim.slide_in_from_bottom);
        // myView.startAnimation(animation);
        pop_menu_background.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.btn_paizhao:
                Intent intentFromCapture = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                // 判断存储卡是否可以用，可用进行存储
                if (Tools.hasSdcard()) {

                    intentFromCapture
                            .putExtra(MediaStore.EXTRA_OUTPUT, Uri
                                    .fromFile(new File(Environment
                                            .getExternalStorageDirectory(),
                                            IMAGE_FILE_NAME)));
                }

                startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);

                pop_menu_background.setVisibility(View.GONE);
                photoPopWindow.dismiss();
                break;
            case R.id.btn_select_pic:
                Intent intentFromGallery = new Intent(Intent.ACTION_PICK, null);
                intentFromGallery.setDataAndType(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);
                pop_menu_background.setVisibility(View.GONE);
                photoPopWindow.dismiss();
                break;

            case R.id.perfect_image_icon:
                shotSelectImages();
                break;
            case R.id.perfect_bt_ok:
                if ("SportMain".equals(comeFrom)) {
                    File f = new File(FACE_PATH);
                    argname = perfect_edit_name.getText().toString().trim();
                    if (argname != null && !"".equals(argname)) {
                        if (judgeNum(argname)) {
                            Toast.makeText(
                                    PerfectMainActivity.this,
                                    getResources().getString(
                                            R.string.sports_nickname_toolong), Toast.LENGTH_SHORT)
                                    .show();
                        } else if (argname.contains("_")) {
                            Toast.makeText(
                                    PerfectMainActivity.this,
                                    getResources()
                                            .getString(
                                                    R.string.sports_toast_illegal_character),
                                    Toast.LENGTH_LONG).show();
                        } else if (!f.exists()) {
                            Toast.makeText(
                                    PerfectMainActivity.this,
                                    "请添加头像",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            if (mSportsApp.getSessionId() != null
                                    && !"".equals(mSportsApp.getSessionId())) {
                                waitShowDialog();
                                new AsyncTask<Void, Void, ApiBack>() {
                                    @Override
                                    protected ApiBack doInBackground(Void... arg0) {
                                        // TODO Auto-generated method stub
                                        String argpicAddess = FACE_PATH;
                                        Log.i("UserEdiActivity","QQ——:"+argpicAddess);
                                        String argsex = "";
                                        String argbirthday = "";
                                        String argheight = mUserDetail.getHeight()
                                                + "";
                                        String argweight = mUserDetail.getWeight()
                                                + "";
                                        String province = "";
                                        String city = "";
                                        String conuy = "";
                                        String singure = "";
                                        String bmi = mUserDetail.getBmi() + "";
                                        if (mUserDetail.getSignature() != null
                                                && !"".equals(mUserDetail
                                                .getSignature())) {
                                            singure = mUserDetail.getSignature();
                                        }
                                        if (mUserDetail.getArea() != null
                                                && !"".equals(mUserDetail.getArea())) {
                                            conuy = mUserDetail.getArea();
                                        }
                                        if (mUserDetail.getCity() != null
                                                && !"".equals(mUserDetail.getCity())) {
                                            city = mUserDetail.getCity();
                                        }
                                        if (mUserDetail.getProvince() != null
                                                && !"".equals(mUserDetail
                                                .getProvince())) {
                                            province = mUserDetail.getProvince();
                                        }
                                        if (mUserDetail.getBirthday() != null
                                                && !"".equals(mUserDetail
                                                .getBirthday())) {
                                            argbirthday = mUserDetail.getBirthday();
                                        }
                                        if (mUserDetail.getSex() != null
                                                && !"".equals(mUserDetail.getSex())) {
                                            argsex = mUserDetail.getSex();
                                        }

                                        ApiBack back = null;
                                        try {
                                            back = ApiJsonParser.modifymsg(
                                                    mSportsApp.getSessionId(),
                                                    argname, "", "", argpicAddess,
                                                    argsex, argbirthday, "", "",
                                                    argheight, argweight, province,
                                                    city, conuy, singure, bmi);
                                            return back;
                                        } catch (ApiNetException e) {
                                            if (mLoadProgressDialog != null)
                                                if (mLoadProgressDialog.isShowing())
                                                    mLoadProgressDialog.dismiss();
                                            Log.e(TAG, "---网络错误");
                                            SportsApp.eMsg = Message
                                                    .obtain(mExceptionHandler,
                                                            SportsExceptionHandler.NET_ERROR);
                                            SportsApp.eMsg.sendToTarget();
                                            e.printStackTrace();
                                        } catch (ApiSessionOutException e) {
                                            if (mLoadProgressDialog != null)
                                                if (mLoadProgressDialog.isShowing())
                                                    mLoadProgressDialog.dismiss();
                                            e.printStackTrace();
                                            SportsApp.eMsg = Message
                                                    .obtain(mExceptionHandler,
                                                            SportsExceptionHandler.SESSION_OUT);
                                            SportsApp.eMsg.sendToTarget();
                                        }
                                        return back;
                                    }

                                    protected void onPostExecute(ApiBack result) {
                                        if (mLoadProgressDialog != null)
                                            if (mLoadProgressDialog.isShowing())
                                                mLoadProgressDialog.dismiss();
                                        if (result != null) {
                                            if (result.getFlag() == 0) {
                                                Toast.makeText(
                                                        PerfectMainActivity.this,
                                                        getResources()
                                                                .getString(
                                                                        R.string.sports_modify_successed),
                                                        Toast.LENGTH_SHORT).show();
                                                finish();
                                            } else {
                                                Toast.makeText(
                                                        PerfectMainActivity.this,
                                                        getResources()
                                                                .getString(
                                                                        R.string.modified_failed_message),
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(
                                                    PerfectMainActivity.this,
                                                    getResources()
                                                            .getString(
                                                                    R.string.sports_modify_failed),
                                                    Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    ;

                                }.execute();
                            } else {
                                Toast.makeText(
                                        PerfectMainActivity.this,
                                        getResources().getString(
                                                R.string.sports_get_data_fail), Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }

                    } else {
                        Toast.makeText(
                                PerfectMainActivity.this,
                                getResources().getString(
                                        R.string.nickname_ont_be_empty), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    String name = perfect_edit_name.getText().toString().trim();
                    if (name != null && !"".equals(name)) {
                        if (judgeNum(name)) {
                            Toast.makeText(
                                    PerfectMainActivity.this,
                                    getResources().getString(
                                            R.string.sports_nickname_toolong), Toast.LENGTH_SHORT)
                                    .show();
                        } else if (name.contains("_")) {
                            Toast.makeText(
                                    PerfectMainActivity.this,
                                    getResources()
                                            .getString(R.string.sports_toast_illegal_character),
                                    Toast.LENGTH_LONG).show();
                        } else {
                            waitShowDialog();
                            LoginByOtherThread thread = new LoginByOtherThread(
                                    mHandler, weiboType, name, token);
                            thread.start();
                        }
                    } else {
                        Toast.makeText(
                                PerfectMainActivity.this,
                                getResources().getString(
                                        R.string.nickname_ont_be_empty), Toast.LENGTH_SHORT).show();
                    }
                }

                break;
            default:
                break;
        }
    }

    // 判断用户名过长
    private boolean judgeNum(String name) {
        boolean isJudeg = false;

        if (name.length() > 15) {
            isJudeg = true;
            return isJudeg;
        }
        // try {
        // int length = name.getBytes("gbk").length;
        // if (length != 0 && length > 20) {
        // isJudeg = true;
        // return isJudeg;
        // }
        //
        // if (name.length() > 20) {
        // isJudeg = true;
        // return isJudeg;
        // }
        // } catch (UnsupportedEncodingException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }

        return isJudeg;
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (mLoadProgressDialog != null)
                if (mLoadProgressDialog.isShowing())
                    mLoadProgressDialog.dismiss();
            switch (msg.what) {
                case 2:
                    Toast.makeText(
                            PerfectMainActivity.this,
                            getResources().getString(
                                    R.string.sports_modify_successed), Toast.LENGTH_SHORT).show();
                    String sessionid = null;
                    if (msg != null && msg.getData().getString("msg") != null && !"".equals(msg.getData().getString("msg"))) {
                        sessionid = msg.getData().getString("msg").substring(7);
                    }
                    Log.e(TAG, "session_id" + sessionid);
                    if (mSportsApp != null && sessionid != null) {
                        mSportsApp.setSessionId(sessionid);
                        finish();
                    } else {
                        mSportsApp.setSessionId("");
                        Toast.makeText(
                                PerfectMainActivity.this,
                                getResources().getString(R.string.sports_modify_failed),
                                Toast.LENGTH_SHORT).show();
                    }

                    break;
                case 101:
                    Toast.makeText(
                            PerfectMainActivity.this,
                            getResources().getString(
                                    R.string.modified_failed_message), Toast.LENGTH_SHORT).show();
                    break;
                case LoginActivity.QQ_LOGIN_FIALED:
                    Toast.makeText(
                            PerfectMainActivity.this,
                            getResources().getString(R.string.sports_modify_failed),
                            Toast.LENGTH_SHORT).show();
                    break;
                case LoginActivity.LOGIN_FAIL_DEVICE_DISABLE:
                    Log.e("develop_debug", "PerfectMainActivity.java 458");
                    Toast.makeText(
                            PerfectMainActivity.this,
                            getResources().getString(R.string.login_fail_device_disable),
                            Toast.LENGTH_LONG).show();
                    break;
            }
        }

        ;
    };

    @Override
    public void onDismiss() {
        // TODO Auto-generated method stub
        pop_menu_background.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (resultCode != RESULT_CANCELED) {

            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    startPhotoZoom(data.getData());
                    break;
                case CAMERA_REQUEST_CODE:
                    if (Tools.hasSdcard()) {
                        File tempFile = new File(
                                Environment.getExternalStorageDirectory()
                                        + File.separator + IMAGE_FILE_NAME);
                        startPhotoZoom(Uri.fromFile(tempFile));
                    } else {
                        Toast.makeText(
                                PerfectMainActivity.this,
                                getResources().getString(
                                        R.string.sports_toast_nosdcard),
                                Toast.LENGTH_LONG).show();
                    }

                    break;
                case RESULT_REQUEST_CODE:
                    if (data != null) {
                        getImageToView(data);
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 2);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param data
     */
    private void getImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            drawable = new BitmapDrawable(photo);
            perfect_image_icon.setImageDrawable(drawable);
//			isfacechanged = true;
            Tools.SaveBitmapAsFile(FACE_PATH, photo);
            if (photo != null) {
                photo.recycle();
                photo = null;
            }
        }
    }

    class LoginFinishTask extends AsyncTask<Integer, Integer, UserDetail> {

        @Override
        protected UserDetail doInBackground(Integer... arg0) {
            UserDetail detail = null;
            // while (detail == null) {
            try {
                detail = ApiJsonParser.refreshRank(mSportsApp.getSessionId());
            } catch (ApiNetException e) {
                if (mLoadProgressDialog != null)
                    if (mLoadProgressDialog.isShowing())
                        mLoadProgressDialog.dismiss();
                e.printStackTrace();
                SportsApp.eMsg = Message.obtain(mExceptionHandler,
                        SportsExceptionHandler.NET_ERROR);
                SportsApp.eMsg.sendToTarget();
                return null;
            } catch (ApiSessionOutException e) {
                if (mLoadProgressDialog != null)
                    if (mLoadProgressDialog.isShowing())
                        mLoadProgressDialog.dismiss();
                e.printStackTrace();
                SportsApp.eMsg = Message.obtain(mExceptionHandler,
                        SportsExceptionHandler.SESSION_OUT);
                SportsApp.eMsg.sendToTarget();
                return null;
            }
            // }
            return detail;
        }

        @Override
        protected void onPostExecute(final UserDetail result) {
            if (mLoadProgressDialog != null)
                if (mLoadProgressDialog.isShowing())
                    mLoadProgressDialog.dismiss();
            if (result != null) {
                mUserDetail = result;
                if (result.getUname() != null && !"".equals(result.getUname())) {
                    perfect_edit_name.setText(result.getUname());
                }
                if (result.getUimg() != null && !"".equals(result.getUimg())) {

                    new AsyncTask<Void, Void, Bitmap>() {
                        @Override
                        protected Bitmap doInBackground(Void... params) {
                            Log.e(TAG, "doInBackground");
                            String url = result.getUimg();
                            String fileName = null;
                            if (!LoginActivity.mIsRegister) {
                                fileName = url.substring(
                                        url.lastIndexOf("/") + 1,
                                        url.lastIndexOf("."));
                            } else {
                                if (weiBo_type.equals(WeiboType.QQzone)) {
                                    fileName = weiBo_photo;
                                }
                            }
                            Log.e(TAG, "fileName-----------" + fileName);
                            return SportsUtilities.loadBitmap(url, fileName);
                        }

                        @Override
                        protected void onPostExecute(Bitmap bitmap) {
                            if (bitmap != null) {
                                perfect_image_icon.setImageBitmap(bitmap);
                                // if (LoginActivity.mIsRegister
                                // && !weiBo_type.equals("")) {
                                // Tools.SaveBitmapAsFile(FACE_PATH, bitmap);
                                // Tools.SaveBitmapAsFile(FACE_PATH_TWO,
                                // bitmap);
                                // isfacechanged = true;
                                // }
                                Log.i("UserEdiActivity","QQ——重名"+FACE_PATH);
                                Tools.SaveBitmapAsFile(FACE_PATH, bitmap);
                                Tools.SaveBitmapAsFile(FACE_PATH_TWO, bitmap);
//								isfacechanged = true;
                            } else {
                                Log.e(TAG, "bitmap=null");
                                if ("woman".equals(sex))
                                    perfect_image_icon
                                            .setImageResource(R.drawable.sports_user_edit_portrait);
                                else if ("man".equals(sex))
                                    perfect_image_icon
                                            .setImageResource(R.drawable.sports_user_edit_portrait_male);
                            }
                        }
                    }.execute();

                } else {
                    sex = result.getSex();
                    Log.i("QQ","修改QQ性别"+sex);
                    if ("woman".equals(sex))
                        perfect_image_icon
                                .setImageResource(R.drawable.sports_user_edit_portrait);
                    else if ("man".equals(sex))
                        perfect_image_icon
                                .setImageResource(R.drawable.sports_user_edit_portrait_male);
                }

            }
        }
    }

    ;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    private void waitShowDialog() {
        // TODO Auto-generated method stub
        if (mLoadProgressDialog == null) {
            mLoadProgressDialog = new Dialog(this, R.style.sports_dialog);
            LayoutInflater mInflater = getLayoutInflater();
            View v1 = mInflater.inflate(R.layout.bestgirl_progressdialog, null);
            mDialogMessage = (TextView) v1.findViewById(R.id.message);
            mDialogMessage.setText(R.string.bestgirl_wait);
            v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
            mLoadProgressDialog.setContentView(v1);
        }
        if (mLoadProgressDialog != null)
            if (!mLoadProgressDialog.isShowing() && !this.isFinishing())
                mLoadProgressDialog.show();
        Log.i(TAG, "isFirstshow----");
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (mLoadProgressDialog != null)
            if (mLoadProgressDialog.isShowing())
                mLoadProgressDialog.dismiss();
        mLoadProgressDialog = null;
        drawable = null;
        if (photoPopWindow != null) {
            photoPopWindow = null;
        }
        mSportsApp = null;
    }

    /**
     *@method 固定字体大小方法
     *@author suhu
     *@time 2016/10/11 13:23
     *@param
     *
     */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config=new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config,res.getDisplayMetrics() );
        return res;
    }

    /**
     *@method 加载图片
     *@author suhu
     *@time 2016/11/1 14:18
     *@param imageUrl
     *@param mImageView
     *
    */
    public void LoadImage(String imageUrl, ImageView mImageView){
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.sports_user_edit_portrait_male)  //加载中显示图片
                .showImageOnFail(R.drawable.sports_user_edit_portrait_male)    //加载失败显示图片
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoader.getInstance().displayImage(imageUrl, mImageView, options);
    }
}
