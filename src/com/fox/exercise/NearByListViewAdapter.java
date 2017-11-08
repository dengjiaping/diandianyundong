package com.fox.exercise;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import cn.ingenic.indroidsync.SportsApp;

import com.fox.exercise.api.entity.UserNearby;
import com.fox.exercise.login.SportMain;
import com.fox.exercise.newversion.act.PersonalPageMainActivity;
import com.fox.exercise.util.RoundedImage;
//import com.umeng.analytics.MobclickAgent;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class NearByListViewAdapter extends BaseAdapter {

    private Context mContext = null;
    private ArrayList<UserNearby> mList = null;

    private ImageDownloader mDownloader = null;

    private LayoutInflater mInflater = null;

    private ImageView followBtn = null;
    private int position_new;
    //private ChangeHandler mHandler = null;

    //private Dialog loadProgressDialog = null;
    //private TextView message = null;

    private static final String TAG = "NearByListViewAdapter";
    private SportsApp mSportsApp;

    public NearByListViewAdapter(Context context, ArrayList<UserNearby> list, SportsApp bestGirApp) {
        this.mContext = context;
        mSportsApp = bestGirApp;
        this.mList = list;
        // mList.add(0, self);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mDownloader = new ImageDownloader(context);
        mDownloader.setType(ImageDownloader.ICON);
        //this.mHandler = new ChangeHandler();

//		loadProgressDialog = new Dialog(context, R.style.sports_dialog);
//		// LayoutInflater mInflater = getLayoutInflater();
//		View v1 = mInflater.inflate(R.layout.sports_progressdialog, null);
//		message = (TextView) v1.findViewById(R.id.message);
//		message.setText("正在发送约跑私信给ta,请稍后!");
//		v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
//		loadProgressDialog.setContentView(v1);
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
    public View getView(int position, View convertView, ViewGroup arg2) {
        position_new = position;
        LinearLayout layout = null;
        if (convertView == null)
            layout = (LinearLayout) mInflater.inflate(R.layout.sports_nearby_list_item, null);
        else
            layout = (LinearLayout) convertView;
        RoundedImage iconImg = (RoundedImage) layout.findViewById(R.id.image_icon);
        iconImg.setImageDrawable(null);
        ImageView sexImageView = (ImageView) layout.findViewById(R.id.img_sex);
        if (mList.get(position).getImg() != null && !"".equals(mList.get(position).getImg())) {
            if ("man".equals(mList.get(position).getSex())) {
                iconImg.setBackgroundResource(R.drawable.sports_user_edit_portrait_male);
                sexImageView.setImageBitmap(BitmapFactory.decodeStream(mContext.getResources()
                        .openRawResource(+R.drawable.sex_boy)));
            } else if ("woman".equals(mList.get(position).getSex())) {
                iconImg.setBackgroundResource(R.drawable.sports_user_edit_portrait);
                sexImageView.setImageBitmap(BitmapFactory.decodeStream(mContext.getResources()
                        .openRawResource(+R.drawable.sex_girl)));
            }
            //传头像
            if (!SportsApp.DEFAULT_ICON.equals(mList.get(position).getImg())) {
                mDownloader.download(mList.get(position).getImg(), iconImg, null);
                iconImg.setImageBitmap(mDownloader.getLocalBitmap(mList.get(position).getImg()));
            }
        }
        Log.i("", "");
        TextView nameTxt = (TextView) layout.findViewById(R.id.tx_name);
        nameTxt.setText(mList.get(position).getName());

	/*	ImageView sexImg = (ImageView) layout.findViewById(R.id.nearby_sex_img);
        if (mList.get(position).getSex().equals("man")) {
			sexImg.setImageBitmap(BitmapFactory.decodeStream(mContext.getResources()
					.openRawResource(R.drawable.sex_boy)));
		} else {
			sexImg.setImageBitmap(BitmapFactory.decodeStream(mContext.getResources().openRawResource(
					R.drawable.sex_girl)));
		}*/

		/*TextView ageTxt = (TextView) layout.findViewById(R.id.tx_age);
        SportsUtilities.setAge(mList.get(position).getBirthday(), ageTxt);*/
//		SimpleDateFormat format = new SimpleDateFormat("yyyy");
        // int nowYear = Integer
        // .valueOf(format.format(System.currentTimeMillis()));
//		SimpleDateFormat formatParse = new SimpleDateFormat("yyyy-MM-dd");
//		int birthYear;
//		try {
//			birthYear = Integer.valueOf(format.format(formatParse.parse(mList.get(position).getBirthday())));
//			int age = Integer.valueOf(format.format(System.currentTimeMillis())) - birthYear;
//			if (age < 0 || age >= 100)
//				age = 0;
//			ageTxt.setText("" + age + "岁");
//		} catch (NumberFormatException e) {
//			e.printStackTrace();
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}

        TextView timeTxt = (TextView) layout.findViewById(R.id.tx_time);
        long time = System.currentTimeMillis() - mList.get(position).getTime() * 1000;
        if (time <= 60 * 1000)
            timeTxt.setText(R.string.sports_time_justnow);
        else if (time <= 60 * 60 * 1000) {
            int h = (int) (time / 1000 / 60);
            timeTxt.setText("" + h + mContext.getResources().getString(R.string.sports_time_mins_ago));
        } else {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            timeTxt.setText(format.format(mList.get(position).getTime() * 1000));
        }

        TextView distanceTxt = (TextView) layout.findViewById(R.id.tx_distance);
        int distance = mList.get(position).getDistance();
        if (distance > 1000) {
            float f = distance / 1000.0f;
            distanceTxt.setText(String.format("%.1f", f) + "千米");
        } else {
            distanceTxt.setText(distance + mContext.getResources().getString(R.string.sports_meters));
        }

        followBtn = (ImageView) layout.findViewById(R.id.bt_follow);
        followBtn.setVisibility(View.VISIBLE);
        if (mList.get(position).getName().equals(SportMain.user_name)) {
            followBtn.setVisibility(View.GONE);
        }
        //followBtn.setText("约ta");
        followBtn.setTag(mList.get(position));
        followBtn.setOnClickListener(clicklisten);
        layout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!mSportsApp.isOpenNetwork()) {
                    Toast.makeText(mContext, "网络未连接，请检查网络！", Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                // TODO Auto-generated method stub
                ImageView bn = (ImageView) arg0.findViewById(R.id.bt_follow);
                UserNearby unb = (UserNearby) bn.getTag();

//				Intent intent = new Intent(mContext, PedometerActivity.class);
                Intent intent = new Intent(mContext, PersonalPageMainActivity.class);
                intent.putExtra("ID", unb.getId());
                intent.putExtra("isFromInvite", true);
                mContext.startActivity(intent);
            }
        });
        return layout;
    }

    private OnClickListener clicklisten = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (!mSportsApp.isOpenNetwork()) {
                Toast.makeText(mContext, "网络未连接，请检查网络！", Toast.LENGTH_LONG)
                        .show();
                return;
            }
            ImageView bn = (ImageView) v.findViewById(R.id.bt_follow);
            UserNearby unb = (UserNearby) bn.getTag();

//			Intent intent = new Intent(mContext, PedometerActivity.class);
            Intent intent = new Intent(mContext, PersonalPageMainActivity.class);
            intent.putExtra("ID", unb.getId());
            intent.putExtra("isFromInvite", true);
            mContext.startActivity(intent);

        }
    };

	/*class SendMsgTask extends AsyncTask<Integer, Integer, ApiBack> {
		SendMsgTask(){
		}
		@Override
		protected void onPreExecute() {
//			loadProgressDialog.show();
		}

		@Override
		protected ApiBack doInBackground(Integer... arg0) {
			ApiBack back = null;
			try {
				back = ApiJsonParser.inviteSport(mSportsApp.getSessionId(),
						mSportsApp.getSportUser().getUid(), mList.get(position_new).getId());
			} catch (ApiNetException e) {
				e.printStackTrace();
			} catch (ApiSessionOutException e) {
				e.printStackTrace();
			}
			return back;
		}
		@Override
		protected void onPostExecute(ApiBack result) {
//			mHandler.sendEmptyMessage(0);
			if(result == null || result.getFlag()!=0){
				Toast.makeText(mContext, "约跑失败！", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(mContext, "约跑成功！", Toast.LENGTH_SHORT).show();
			}
			
		}
	}*/

}
