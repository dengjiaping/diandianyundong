package com.fox.exercise;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.ingenic.indroidsync.SportsApp;

import com.fox.exercise.api.entity.UserFollowMsg;
import com.fox.exercise.map.SportTaskDetailActivityGaode;
import com.fox.exercise.newversion.act.PersonalPageMainActivity;
import com.fox.exercise.util.RoundedImage;

public class SlimFrendAdapter extends BaseAdapter {

    private ArrayList<UserFollowMsg> mList = null;
    private Context mContext = null;
    private LayoutInflater mInflater = null;

    private SportsApp mSportsApp = SportsApp.getInstance();
    private ImageDownloader mDownloader = null;
    private ImageDownloader mIconDownloader = null;

    public SlimFrendAdapter(ArrayList<UserFollowMsg> list, Context context) {
        this.mContext = context;
        this.mList = list;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDownloader = new ImageDownloader(mContext);
        mDownloader.setType(ImageDownloader.GIFT);
        mIconDownloader = new ImageDownloader(mContext);
        mIconDownloader.setSize(90, 90);
        mIconDownloader.setType(ImageDownloader.ICON);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    public void clearList() {
        mList.clear();
    }

    public void addItem(UserFollowMsg um) {
        mList.add(um);
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        LinearLayout layout = null;

        if (convertView == null) {
            layout = (LinearLayout) mInflater.inflate(
                    R.layout.slim_frend_adapter, null);
        } else {
            layout = (LinearLayout) convertView;
        }

        RoundedImage icon = (RoundedImage) layout
                .findViewById(R.id.focus_image_icon);
        icon.setImageDrawable(null);
        if ("man".equals(mList.get(position).getSex())) {
            icon.setBackgroundResource(R.drawable.sports_user_edit_portrait_male);
        } else {
            icon.setBackgroundResource(R.drawable.sports_user_edit_portrait);
        }
        mIconDownloader.download(mList.get(position).getImg(), icon, null);

        icon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (mList != null && position < mList.size()) {
                    if (!mSportsApp.isOpenNetwork()) {
                        // 网络未连接，请检查网络!
                        Toast.makeText(
                                mContext,
                                mContext.getResources().getString(
                                        R.string.newwork_not_connected),
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    Intent intent = new Intent(mContext,
                            PersonalPageMainActivity.class);
                    intent.putExtra("ID", mList.get(position).getId());
                    mContext.startActivity(intent);
                }
            }
        });

        TextView nameTxt = (TextView) layout.findViewById(R.id.focus_name_txt);
        nameTxt.setText(mList.get(position).getName());

        TextView timeTxt = (TextView) layout.findViewById(R.id.tx_time);
        long time = System.currentTimeMillis()
                - mList.get(position).getAddTime() * 1000;
        if (time <= 60 * 1000)
            timeTxt.setText(mContext.getResources().getString(
                    R.string.sports_time_justnow));
        else if (time <= 60 * 60 * 1000) {
            int h = (int) (time / 1000 / 60);
            timeTxt.setText(""
                    + h
                    + mContext.getResources().getString(
                    R.string.sports_time_mins_ago));
        } else {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            timeTxt.setText(format
                    .format(mList.get(position).getAddTime() * 1000));
        }

        RoundedImage uploadIcon = (RoundedImage) layout
                .findViewById(R.id.upload_image_icon);
        uploadIcon.setBackgroundResource(SportsUtilities
                .getStateImgIdsMysports(mList.get(position).getType(), mList
                        .get(position).getDetailType()));

        uploadIcon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // if (mList != null && position < mList.size() && !mTaskRun) {
                // GetImageDetailTask task = new
                // GetImageDetailTask(mList.get(position).getPid());
                // task.execute();
                // }
                Intent intent;
                if (mSportsApp.mCurMapType == SportsApp.MAP_TYPE_GAODE) {
                    intent = new Intent(mContext,
                            SportTaskDetailActivityGaode.class);

                    // else{
                    // intent = new Intent(mContext,
                    // SportTaskDetailActivity.class);
                    // }
                    intent.putExtra("uid", mList.get(position).getId());
                    intent.putExtra("taskid", mList.get(position).getTaskid());
                    mContext.startActivity(intent);
                }
            }
        });
        return layout;
    }

    // boolean mTaskRun = false;
    // class GetImageDetailTask extends AsyncTask<Integer, Integer, ImageDetail>
    // {
    //
    // private int picId = 0;
    //
    // public GetImageDetailTask(int picId) {
    // this.picId = picId;
    // }
    //
    // @Override
    // protected ImageDetail doInBackground(Integer... params) {
    // mTaskRun = true;
    // Message msg = null;
    // try {
    // ImageDetail back =
    // ApiJsonParser.getSportsImgDetail(mSportsApp.getSessionId(), picId);
    // return back;
    // } catch (ApiNetException e) {
    // msg = Message.obtain(mSportsApp.getmExceptionHandler(),
    // SportsExceptionHandler.NET_ERROR);
    // msg.sendToTarget();
    // e.printStackTrace();
    // } catch (ApiSessionOutException e) {
    // msg = Message.obtain(mSportsApp.getmExceptionHandler(),
    // SportsExceptionHandler.SESSION_OUT);
    // msg.sendToTarget();
    // e.printStackTrace();
    // }
    // return null;
    // }
    //
    // @Override
    // protected void onPostExecute(ImageDetail result) {
    // mTaskRun = false;
    // if(result == null){
    // return ;
    // }
    // List<PicsAndIds> list = new ArrayList<PicsAndIds>();
    // PicsAndIds p = new PicsAndIds();
    // p.setId(result.getPid());
    // p.setImgUrl(result.getPimg());
    // p.setLikes(result.getLikes());
    // p.setImgDetail(result);
    // list.add(p);
    // SlimDetails.startSelf(mContext, list, 0, -1);
    // }
    //
    // }

}
