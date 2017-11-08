package com.fox.exercise;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fox.exercise.api.ApiBack;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.api.entity.UserNearby;
import com.fox.exercise.login.LoginActivity;
import com.fox.exercise.newversion.act.PersonalPageMainActivity;
import com.fox.exercise.util.RoundedImage;

import java.util.ArrayList;

import cn.ingenic.indroidsync.SportsApp;

public class AddFriendListViewAdapter extends BaseAdapter {

    private Context mContext = null;
    private ArrayList<UserNearby> mList = null;

    private ImageDownloader mDownloader = null;
    ViewHolder holder = null;
    private LayoutInflater mInflater = null;

    //private Dialog loadProgressDialog = null;
    //private TextView message = null;
//    private SharedPreferences sp;
    private static final String TAG = "AddFriendActivity";
    private SportsApp mSportsApp;
    private Dialog loadProgressDialog = null;
    private TextView message = null;
    private ChangeHandler mHandler = null;
    private View addView;
    private final int DELETES = 0;
    private final int ADDS = 1;
    //	private int positions;
    private int addStatus;

    public AddFriendListViewAdapter(Context context, ArrayList<UserNearby> list, SportsApp sportApp) {
        this.mContext = context;
        mSportsApp = sportApp;
        this.mList = list;
        // mList.add(0, self);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mDownloader = new ImageDownloader(context);
        mDownloader.setType(ImageDownloader.ICON);
        this.mHandler = new ChangeHandler();
        loadProgressDialog = new Dialog(context, R.style.sports_dialog);
        // LayoutInflater mInflater = getLayoutInflater();
        View v1 = mInflater.inflate(R.layout.sports_progressdialog, null);
        message = (TextView) v1.findViewById(R.id.message);
        // message.setText(R.string.sports_wait);
        v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
        loadProgressDialog.setContentView(v1);
        loadProgressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = (LinearLayout) mInflater.inflate(
                    R.layout.sports_addfriends_item, null);
            holder.nameTxt = (TextView) convertView
                    .findViewById(R.id.tx_name);
            holder.iconImg = (RoundedImage) convertView
                    .findViewById(R.id.image_icon);
            holder.distance = (TextView) convertView
                    .findViewById(R.id.tx_distance);
            holder.followBtn = (Button) convertView.findViewById(R.id.bt_follow);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.iconImg.setImageDrawable(null);
        if (mList.get(position).getImg() != null && !"".equals(mList.get(position).getImg())) {
            if ("man".equals(mList.get(position).getSex())) {
                holder.iconImg.setBackgroundResource(R.drawable.sports_user_edit_portrait_male);
            } else if ("woman".equals(mList.get(position).getSex())) {
                holder.iconImg.setBackgroundResource(R.drawable.sports_user_edit_portrait);
            }
            if (!SportsApp.DEFAULT_ICON.equals(mList.get(position).getImg())) {
                mDownloader.download(mList.get(position).getImg(), holder.iconImg, null);
            }
        }
        holder.nameTxt.setText(mList.get(position).getName());
        int distance = mList.get(position).getDistance();
        String distance_txt;
        int mi;
        if (distance > 1000) {
            float f = distance / 1000.0f;
            distance_txt = mContext.getString(R.string.friends_away_me) + String.format("%.1f", f) + mContext.getString(R.string.sports_kilometers);
            mi = 2;
        } else {
            distance_txt = mContext.getString(R.string.friends_away_me) + distance + mContext.getString(R.string.sports_meters);
            mi = 1;
        }
        SpannableStringBuilder style = new SpannableStringBuilder(
                distance_txt);
        style.setSpan(new ForegroundColorSpan(Color.parseColor("#f49000")),
                mContext.getString(R.string.friends_away_me).length(), distance_txt.length() - mi,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.distance.setText(style);


        holder.followBtn.setVisibility(View.VISIBLE);
        if (mList.get(position).getName().toString().equals(SportsApp.getInstance().getSportUser().getUname())) {
            holder.followBtn.setVisibility(View.GONE);
        }
        /////////////////////////添加关注
        FollowAndFun f = null;
        if (mList.get(position).getFollowStatus() == 1) {
            holder.followBtn.setBackgroundResource(R.drawable.addfriend_bg);
            holder.followBtn.setText(R.string.sports_added);//已添加

            //holder.followBtn.setClickable(false);
            f = new FollowAndFun(mList.get(position).getId(), 1, mContext.getResources().getString(
                    R.string.sports_unadded), position);
            holder.followBtn.setTag(f);

            holder.followBtn.setEnabled(true);
        }else {
            holder.followBtn.setBackgroundResource(R.drawable.sports_smallbt_selector);
            holder.followBtn.setText(R.string.sports_unadded);//添加
            f = new FollowAndFun(mList.get(position).getId(), 2, mContext.getResources().getString(
                    R.string.sports_added), position);
            holder.followBtn.setTag(f);
            f.position = position;
            holder.followBtn.setEnabled(true);
        }
        holder.followBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mSportsApp.isLogin() == false
                        && (mSportsApp.getSessionId() == null || "".equals(mSportsApp.getSessionId()))) {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(intent);
                    return;
                }
                String s = ((Button) v).getText().toString();

                FollowAndFun f = (FollowAndFun) v.getTag();
                addView = v;
                addStatus = f.status;
//						positions = position;
                // v.setEnabled(false);
                ChangeThread thread = new ChangeThread(f);
                thread.start();
                //((Button) v).setText(f.oppoFollow);
                f.oppoFollow = s;
                if (f.status == 1) {
                    message.setText(R.string.sports_deleting);
                    if (loadProgressDialog != null)
                        loadProgressDialog.show();
                    f.status = 2;
                    mList.get(f.position).setFollowStatus(2);
                    SportsApp.getInstance().getSportUser().setFollowCounts(SportsApp.getInstance().getSportUser().getFollowCounts() - 1);

                    f.position = position;
                    holder.followBtn.setEnabled(true);
                } else {
                    message.setText(R.string.sports_adding);
                    if (loadProgressDialog != null)
                        loadProgressDialog.show();
                    f.status = 1;
                    //						Log.e(TAG, "f.position:"+f.position);
                    //Toast.makeText(mContext, "添加成功",  Toast.LENGTH_LONG).show();
                    mList.get(f.position).setFollowStatus(1);
                    v.setEnabled(true);
							/*f = new FollowAndFun(mList.get(position).getId(), 1, mContext.getResources().getString(
									R.string.sports_unadded),position);
							holder.followBtn.setTag(f);*/
                    f.position = position;
                    //	v.setBackgroundResource(R.drawable.addfriend_bg);
                    SportsApp.getInstance().getSportUser().setFollowCounts(SportsApp.getInstance().getSportUser().getFollowCounts() + 1);
                }
                //((Button) v).setTag(f);

            }
        });


        //////////////////////////////////////////////////
        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (!mSportsApp.isOpenNetwork()) {
                    Toast.makeText(mContext, "网络未连接，请检查网络！", Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                Button bn = (Button) arg0.findViewById(R.id.bt_follow);
                FollowAndFun unb = (FollowAndFun) bn.getTag();

//				Intent intent = new Intent(mContext, PedometerActivity.class);
                Intent intent = new Intent(mContext, PersonalPageMainActivity.class);
                intent.putExtra("fromactivity", 2);
                intent.putExtra("ID", unb.getUid());
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    private class ViewHolder {
        private TextView distance;
        private RoundedImage iconImg;
        private TextView nameTxt;
        private Button followBtn;
    }

    class ChangeThread extends Thread {

        private int uid = 0;
        private int status = 0;

        public ChangeThread(FollowAndFun tag) {
            this.uid = tag.uid;
            this.status = tag.status;
        }

        @Override
        public void run() {
            try {
                ApiBack back = ApiJsonParser.follow(mSportsApp.getSessionId(), uid, status == 2 ? 1 : 2, 1);
                if (addStatus == 1) {
                    Message msg = Message.obtain(mHandler, DELETES);
                    msg.sendToTarget();
                } else {
                    Message msg = Message.obtain(mHandler, ADDS);
                    msg.sendToTarget();
                }
                Log.d(TAG, back.getMsg());


            } catch (ApiNetException e) {
                e.printStackTrace();
            } catch (ApiSessionOutException e) {
                e.printStackTrace();
            }
        }
    }

    class ChangeHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            // holder.followBtn.setEnabled(true);
            if (loadProgressDialog != null)
                if (loadProgressDialog.isShowing())
                    loadProgressDialog.dismiss();

            FollowAndFun f = null;
            switch (msg.what) {
                case DELETES:
                    ((Button) addView).setText(R.string.sports_unadded);
                    addView.setBackgroundResource(R.drawable.sports_smallbt_selector);
                    Toast.makeText(mContext, mContext.getString(R.string.sports_delete_successed), Toast.LENGTH_LONG).show();
                    break;
                case ADDS:
                    ((Button) addView).setText(R.string.sports_added);
                    addView.setBackgroundResource(R.drawable.addfriend_bg);
                    Toast.makeText(mContext, mContext.getString(R.string.sports_follow_successed), Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    }
}
