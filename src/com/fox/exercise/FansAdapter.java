package com.fox.exercise;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
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
import cn.ingenic.indroidsync.SportsApp;

import com.fox.exercise.api.ApiBack;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.api.entity.UserSearch;
import com.fox.exercise.login.LoginActivity;
import com.fox.exercise.util.RoundedImage;

public class FansAdapter extends BaseAdapter {

	private Context mContext = null;
	private ArrayList<UserSearch> mFanList = null;
	private LayoutInflater mInflater = null;
	ViewHolder holder = null;
	private ImageDownloader mDownloader = null;

	private static final String TAG = "FansAdapter";
	
	private final int DELETES=0;
	private final int ADDS=1;
//	private Button followBtn = null;

	private ChangeHandler mHandler = null;

	private Dialog loadProgressDialog = null;
	private TextView message = null;
	private SportsApp mSportsApp;
	private View  addView;
	private int addStatus;
	
	private class ViewHolder {
		private RoundedImage iconImg;
		private TextView nameTxt;
		private Button followBtn;
	}
	
	public FansAdapter(Context context, ArrayList<UserSearch> fanList,
			SportsApp sportsApp) {
		this.mContext = context;
		mSportsApp = sportsApp;
		this.mFanList = fanList;
		this.mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mDownloader = new ImageDownloader(context);
		mDownloader.setType(ImageDownloader.ICON);
		this.mHandler = new ChangeHandler();

		loadProgressDialog = new Dialog(context, R.style.sports_dialog);
		View v1 = mInflater.inflate(R.layout.sports_progressdialog, null);
		message = (TextView) v1.findViewById(R.id.message);
		v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
		loadProgressDialog.setContentView(v1);
		loadProgressDialog.setCanceledOnTouchOutside(false);
	}

	@Override
	public int getCount() {
		return mFanList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = (LinearLayout) mInflater.inflate(
					R.layout.sports_fans_list_item, null);
			holder.nameTxt = (TextView) convertView
					.findViewById(R.id.tx_name);
			holder.iconImg = (RoundedImage) convertView
					.findViewById(R.id.image_icon);
			holder.followBtn = (Button)convertView.findViewById(R.id.bt_follow);
			convertView.setTag(holder);
		}

		else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.iconImg.setImageDrawable(null);
		if (mFanList.get(position).getImg() != null && !"".equals(mFanList.get(position).getImg())) {
			if ("man".equals(mFanList.get(position).getSex())) {
				holder.iconImg.setBackgroundResource(R.drawable.sports_user_edit_portrait_male);
			} else if ("woman".equals(mFanList.get(position).getSex())) {
				holder.iconImg.setBackgroundResource(R.drawable.sports_user_edit_portrait);
			}
			if (!SportsApp.DEFAULT_ICON.equals(mFanList.get(position).getImg())) {
				mDownloader.download(mFanList.get(position).getImg(), holder.iconImg, null);
			}
		}
		holder.nameTxt.setText(mFanList.get(position).getName());

		FollowAndFun f = null;
		if (mFanList.get(position).getFollowStatus() == 1) {
			holder.followBtn.setText(R.string.sports_added);
			holder.followBtn.setBackgroundResource(R.drawable.addfriend_bg);
			f = new FollowAndFun(mFanList.get(position).getId(), 1, mContext
					.getResources().getString(R.string.sports_unadded),
					position);
			holder.followBtn.setTag(f);
			holder.followBtn.setEnabled(true);
		} else {
			holder.followBtn.setText(R.string.sports_unadded);
			holder.followBtn.setBackgroundResource(R.drawable.sports_smallbt_selector);
			f = new FollowAndFun(mFanList.get(position).getId(), 2, mContext
					.getResources().getString(R.string.sports_added),
					position);
			holder.followBtn.setTag(f);
			holder.followBtn.setEnabled(true);
		}
		f.position = position;
		holder.followBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mSportsApp.isLogin() == false
						&& (mSportsApp.getSessionId() == null || ""
								.equals(mSportsApp.getSessionId()))) {
					Intent intent = new Intent(mContext, LoginActivity.class);
					mContext.startActivity(intent);
					return;
				}
				String s = ((Button) v).getText().toString();
				FollowAndFun f = (FollowAndFun) v.getTag();
				addView = v;
				addStatus = f.status;
				ChangeThread thread = new ChangeThread(f);
				thread.start();
//				((Button) v).setText(f.oppoFollow);
//				((Button) v).setBackgroundResource(R.drawable.sports_button_small_disable);
				f.oppoFollow = s;
				if (f.status == 1) {
					message.setText(R.string.sports_deleting);
					if (loadProgressDialog != null)
						loadProgressDialog.show();
					f.status = 2;
					mFanList.get(f.position).setFollowStatus(2);
					SportsApp
							.getInstance()
							.getSportUser()
							.setFollowCounts(
									SportsApp.getInstance().getSportUser()
											.getFollowCounts() - 1);
					f.position = position;
					holder.followBtn.setEnabled(true);
				} else {
					message.setText(R.string.sports_adding);
					if (loadProgressDialog != null)
						loadProgressDialog.show();
					f.status = 1;
					mFanList.get(f.position).setFollowStatus(1);
					SportsApp
							.getInstance()
							.getSportUser()
							.setFollowCounts(
									SportsApp.getInstance().getSportUser()
											.getFollowCounts() + 1);
					v.setEnabled(true);
					f.position = position;
				}
				((Button) v).setTag(f);
			}
		});
		return convertView;
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
				ApiBack back = ApiJsonParser.follow(mSportsApp.getSessionId(),
						uid, status == 2 ? 1 : 2, 1);
				Log.d(TAG, back.getMsg());
				if(addStatus==1){
					Message msg = Message.obtain(mHandler,DELETES);
					msg.sendToTarget();
				}
				else {
					Message msg = Message.obtain(mHandler,ADDS);
					msg.sendToTarget();
				}
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
			if (loadProgressDialog != null){
				if (loadProgressDialog.isShowing()){
					loadProgressDialog.dismiss();
				}
			}
			switch (msg.what) {
			   case DELETES:				
				    ((Button) addView).setText(R.string.sports_unadded);
				    addView.setBackgroundResource(R.drawable.sports_smallbt_selector);
				    Toast.makeText(mContext, mContext.getString(R.string.sports_delete_successed), Toast.LENGTH_LONG).show();
				   break;
               case ADDS:            	   
					((Button) addView).setText(R.string.sports_added);
					addView.setBackgroundResource(R.drawable.addfriend_bg);
					Toast.makeText(mContext, mContext.getString(R.string.sports_follow_successed),  Toast.LENGTH_LONG).show();
				   break;
			   default:
					break;
			}
		}

	}

}
