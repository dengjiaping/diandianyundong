package com.fox.exercise;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.api.entity.InviteUser;
import com.fox.exercise.api.entity.UserDetail;
import com.fox.exercise.http.FunctionStatic;
import com.fox.exercise.login.LoginActivity;
import com.fox.exercise.newversion.act.PersonalPageMainActivity;
import com.fox.exercise.util.RoundedImage;
import com.fox.exercise.view.PullToRefreshBase.OnRefreshListener;
import com.fox.exercise.view.PullToRefreshListView;
import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.ingenic.indroidsync.SportsApp;

/**
 * @author veyu
 *         <p/>
 *         约跑界面
 */
public class InviteSportsActivity extends AbstractBaseActivity {

    private Context context;
    private PullToRefreshListView mPullListView = null;
    private InviteSportsAdapter mAdapter = null;
    private ListView mListView = null;
    private ArrayList<InviteUser> mList = null;
    private Dialog loadProgressDialog = null;

    //private ImageButton backBtn;

    private SportsApp mSportsApp;
    private SportsExceptionHandler mExceptionHandler = null;

    private static final int INIT_LIST = 0x0002;
    private static final int FRESH_LIST = 0x0001;

    private boolean isRefresh = false;

    private int times = 0;

    private long preTime = 0;

    @Override
    public void initIntentParam(Intent intent) {
        // TODO Auto-generated method stub
        title = getResources().getString(R.string.who_about_you);
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        showContentView(R.layout.invitesports_layout);
        context = this;
        mSportsApp = (SportsApp) getApplication();
        mExceptionHandler = mSportsApp.getmExceptionHandler();
    }

    @Override
    public void setViewStatus() {
        // TODO Auto-generated method stub
        initViews();
    }

    @Override
    public void onPageResume() {
        // TODO Auto-generated method stub
        preTime = FunctionStatic.onResume();
        MobclickAgent.onPageStart("InviteSportsActivity");
    }

    @Override
    public void onPagePause() {
        // TODO Auto-generated method stub
        FunctionStatic.onPause(this, FunctionStatic.FUNCTION_INVITEME_LIST,
                preTime);
        MobclickAgent.onPageEnd("InviteSportsActivity");
    }

    @Override
    public void onPageDestroy() {
        // TODO Auto-generated method stub

    }


    private void initViews() {
        switchDialog(true);
        mList = new ArrayList<InviteUser>();
        Thread getInviteSports = new Thread(new GetInviteSportThread());
        getInviteSports.start();

		/*backBtn = (ImageButton) findViewById(R.id.sport_map_back);
        backBtn.setOnClickListener(this);*/

        mPullListView = (PullToRefreshListView) findViewById(R.id.timeList);
        mPullListView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(int pullDirection) {
                switch (pullDirection) {
                    case INIT_LIST:
                        switchDialog(true);
                        isRefresh = false;
                        Thread thread = new Thread(new GetInviteSportThread());
                        thread.start();
                        Log.e("setOnRefreshListener", "连续加载！");
                        break;
                    case FRESH_LIST:
                        switchDialog(true);
                        isRefresh = true;
                        Thread thread1 = new Thread(new GetInviteSportThread());
                        thread1.start();
                        Log.e("setOnRefreshListener", "重新刷新！");
                        break;
                }

            }
        });
        mListView = mPullListView.getRefreshableView();
        Drawable drawable = getResources().getDrawable(R.drawable.sports_bg_line);
        mListView.setDivider(drawable);
        mListView.setDividerHeight(0);
        //mListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (!mSportsApp.isOpenNetwork()) {
                    Toast.makeText(context, getString(R.string.newwork_not_connected), Toast.LENGTH_LONG)
                            .show();
                    return;
                }
//				Intent intent = new Intent(context, PedometerActivity.class);
                Intent intent = new Intent(context, PersonalPageMainActivity.class);
                intent.putExtra("ID", mList.get(position).getUid());
                startActivity(intent);
            }
        });
    }


    private class GetInviteSportThread implements Runnable {
        @Override
        public void run() {
            List<InviteUser> inviteList = new ArrayList<InviteUser>();
            try {
                if (isRefresh) {
                    mList.clear();
                    times = 0;
                    inviteList = (ArrayList<InviteUser>) ApiJsonParser
                            .getInviteSports(mSportsApp.getSessionId(), times);
                } else {
                    inviteList = (ArrayList<InviteUser>) ApiJsonParser
                            .getInviteSports(mSportsApp.getSessionId(), times);
                }
                times++;
                for (InviteUser user : inviteList) {
                    mList.add(user);
                }
            } catch (ApiNetException e) {
                e.printStackTrace();
                SportsApp.eMsg = Message.obtain(mExceptionHandler,
                        SportsExceptionHandler.NET_ERROR);
                SportsApp.eMsg.sendToTarget();
            } catch (ApiSessionOutException e) {
                e.printStackTrace();
                SportsApp.eMsg = Message.obtain(mExceptionHandler,
                        SportsExceptionHandler.SESSION_OUT);
                SportsApp.eMsg.sendToTarget();
                startActivity(new Intent(context, LoginActivity.class));
            }

            Message msg = null;
            msg = Message.obtain(handler, FRESH_LIST);
            msg.sendToTarget();
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // case INIT_LIST:
                // switchDialog(false);
                // break;
                case FRESH_LIST:
                    switchDialog(false);
                    Log.i("FRESH_LIST", "FRESH_LIST");
                    if (mAdapter == null) {
                        Log.i("FRESH_LIST", "mAdapter == null");
                        mAdapter = new InviteSportsAdapter(mList, context);
                        mListView.setAdapter(mAdapter);
                        mListView.setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView,
                                                    View view, int position, long id) {
                                InviteUser user = (InviteUser) adapterView
                                        .getItemAtPosition(position);
                                int uid = user.getUid();
                                Log.i("setOnItemClickListener", "uid = " + uid);
                            }
                        });
                    } else {
                        mAdapter.notifyDataSetChanged();
                    }
                    if (isRefresh == true) {
                        mAdapter.notifyDataSetChanged();
                    }
                    mPullListView.onRefreshComplete();
                    break;
            }
        }

        ;
    };

    private class InviteSportsAdapter extends BaseAdapter {

        private ArrayList<InviteUser> mList = null;
        private Context mContext = null;
        private LayoutInflater mInflater = null;
        private int indexGL = -1;
        private int indexRZ = -1;

        private ImageDownloader mIconDownloader = null;

        public InviteSportsAdapter(ArrayList<InviteUser> list, Context context) {
            this.mContext = context;
            this.mList = list;
            this.mInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mIconDownloader = new ImageDownloader(mContext);
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public InviteUser getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            if (mList != null && position < mList.size()) {
                return mList.get(position).getUid();
            }
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup arg2) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();

                convertView = (RelativeLayout) mInflater.inflate(
                        R.layout.invitesports_item, null);
                holder.icon = (RoundedImage) convertView
                        .findViewById(R.id.image_icon);
                holder.timeTxt = (TextView) convertView.findViewById(R.id.time_txt);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            UserDetail detail = SportsApp.getInstance().getSportUser();

            final InviteUser inviteUser = mList.get(position);
            holder.icon.setImageDrawable(null);

            Log.i("inviteUser.getSex()", "" + inviteUser.getSex());

            holder.icon.setBackgroundResource(inviteUser.getSex() == 1 ? R.drawable.sports_user_edit_portrait_male
                    : R.drawable.sports_user_edit_portrait);
            mIconDownloader.download(inviteUser.getImg(), holder.icon, null);

            // TextView nameTxt = (TextView) layout.findViewById(R.id.name_txt);
            // nameTxt.setText(inviteUser.getName());


            long time = System.currentTimeMillis() - inviteUser.getAddTime()
                    * 1000;
            if (time <= 60 * 1000)
                holder.timeTxt.setText(getString(R.string.sports_time_justnow));
            else if (time <= 60 * 60 * 1000) {
                int h = (int) (time / 1000 / 60);
                holder.timeTxt.setText("" + h + getString(R.string.sports_time_mins_ago));
            } else {
                SimpleDateFormat format = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm");
                holder.timeTxt.setText(format.format(inviteUser.getAddTime() * 1000));
            }
            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (!mSportsApp.isOpenNetwork()) {
                        Toast.makeText(context, getString(R.string.newwork_not_connected), Toast.LENGTH_LONG)
                                .show();
                        return;
                    }
//					Intent intent = new Intent(context, PedometerActivity.class);
                    Intent intent = new Intent(context, PersonalPageMainActivity.class);
                    intent.putExtra("ID", inviteUser.getUid());
                    startActivity(intent);
                }
            });
            return convertView;
        }

        private class ViewHolder {
            private RoundedImage icon;
            private TextView timeTxt;
        }
    }

    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getHeight(),
                bitmap.getWidth(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);

        canvas.drawOval(rectF, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rectF, paint);
        return output;
    }

    // 对话框显示开关
    public void switchDialog(boolean isShow) {
        if (isShow) {
            if (loadProgressDialog != null) {
                loadProgressDialog.show();
            } else {
                loadProgressDialog = new Dialog(context, R.style.sports_dialog);
                LayoutInflater mInflater = getLayoutInflater();
                View v1 = mInflater.inflate(R.layout.sports_progressdialog,
                        null);
                TextView message = (TextView) v1.findViewById(R.id.message);
                message.setText(R.string.sports_wait);
                v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
                loadProgressDialog.setContentView(v1);
                loadProgressDialog.setCanceledOnTouchOutside(false);
                loadProgressDialog.show();
            }
        } else {
            loadProgressDialog.dismiss();
        }
    }


}
