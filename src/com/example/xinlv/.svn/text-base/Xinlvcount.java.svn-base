package com.example.xinlv;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fox.exercise.R;
import com.fox.exercise.api.ApiBack;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.db.SportContentDB;
import com.fox.exercise.newversion.entity.SleepEffect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.ingenic.indroidsync.SportsApp;

/**
 * @author zhonghuibin 心率统计的activity
 *
 */
public class Xinlvcount extends Activity implements PopupWindow.OnDismissListener {
	// private Adapter adapter;
	private XinlvAdapter adapter2;
	private List<String> list1;
	private ListView lv_xinlv;
	private SportsApp msApp;
	private String aa;
	private int count = 1;
	private ImageView deleteAll;
	private Dialog mLoadProgressDialog = null;
	private SportContentDB contentDB;
	private ActionBar actionBar;
	private ImageView back;
	private Dialog alertDialog;
	private Dialog dialog;
	private SleepEffectService sleepEffectService;
	private List<SleepEffect> list;

	private PopupWindow myWindow = null;
	private LinearLayout myView, mainLL;
	private RelativeLayout mPopMenuBack;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xinlvcount);
		actionBar = getActionBar();
		actionBar.hide();
		initVIew();
		iv_btnEvent();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// startActivity(new Intent(Xinlvcount.this,XinlvActivity.class));
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
			case KeyEvent.KEYCODE_BACK: {
				onDestroy();
				finish();
				startActivity(new Intent(Xinlvcount.this, XinlvActivity.class));
				return true;
			}

		}
		return super.onKeyDown(keyCode, event);
	}

	private void initVIew() {
		// TODO Auto-generated method stub
		contentDB = SportContentDB.getInstance(Xinlvcount.this);
		msApp = (SportsApp) getApplication();
		deleteAll = (ImageView) findViewById(R.id.delete_all);
		back = (ImageView) findViewById(R.id.iv_back_xinlv);
		lv_xinlv = (ListView) findViewById(R.id.lv_xinlv);
		sleepEffectService = new SleepEffectService(this);
		list = new ArrayList<SleepEffect>();
		mainLL = (LinearLayout) findViewById(R.id.ll);
		mPopMenuBack = (RelativeLayout) findViewById(R.id.set_menu_background);
//		有些手机获取不到服务器数据的时候直接没有数据
//		list = contentDB.queryUidXinLv(msApp.getSportUser()
//				.getUid() + "");
//		count++;
//		if (count % 2 == 0) {
//			Collections.reverse(list);
//		}
//		adapter2 = new XinlvAdapter(list, Xinlvcount.this);
//		lv_xinlv.setAdapter(adapter2);
//		 获取服务器上面的健康心率的数据
		if (msApp.isOpenNetwork()) {
			waitShowDialog(getResources().getString(R.string.loading));
			new AsyncTask<Void, Void, List<SleepEffect>>() {
				@Override
				protected List<SleepEffect> doInBackground(Void... params) {
					// TODO Auto-generated method stub
					List<SleepEffect> back_1 = null;
					try {
						back_1 = ApiJsonParser.getSleepDate(
								msApp.getSessionId(), 0, false);
					} catch (ApiNetException e) {
						e.printStackTrace();
					}
					return back_1;
				}

				@Override
				protected void onPostExecute(List<SleepEffect> result) {
					// TODO Auto-generated method stub
					super.onPostExecute(result);
					if (result != null) {
						if (result.size() <= 0) {
							if (mLoadProgressDialog != null)
								if (mLoadProgressDialog.isShowing())
									mLoadProgressDialog.dismiss();
							list = contentDB.queryUidXinLv(msApp.getSportUser()
									.getUid() + "");
							count++;
							if (count % 2 == 0) {
								Collections.reverse(list);
							}
							adapter2 = new XinlvAdapter(list, Xinlvcount.this);
							lv_xinlv.setAdapter(adapter2);
						} else {
							for (int i = 0; i < result.size(); i++) {
								contentDB.insertXinlv(true, result.get(i));
							}
							if (mLoadProgressDialog != null)
								if (mLoadProgressDialog.isShowing())
									mLoadProgressDialog.dismiss();
							list = contentDB.queryUidXinLv(msApp.getSportUser()
									.getUid() + "");
							count++;
							if (count % 2 == 0) {
								Collections.reverse(list);
							}
							adapter2 = new XinlvAdapter(list, Xinlvcount.this);
							lv_xinlv.setAdapter(adapter2);
						}
					} else {
						if (mLoadProgressDialog != null)
							if (mLoadProgressDialog.isShowing())
								mLoadProgressDialog.dismiss();
						list = contentDB.queryUidXinLv(msApp.getSportUser()
								.getUid() + "");
						count++;
						if (count % 2 == 0) {
							Collections.reverse(list);
						}
						adapter2 = new XinlvAdapter(list, Xinlvcount.this);
						lv_xinlv.setAdapter(adapter2);
					}
				}
			}.execute();
		}else {
			list = contentDB.queryUidXinLv(msApp.getSportUser()
					.getUid() + "");
			adapter2 = new XinlvAdapter(list, Xinlvcount.this);
			lv_xinlv.setAdapter(adapter2);
		}

		// 删除全部历史记录的点击
		deleteAll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog = new Dialog(Xinlvcount.this, R.style.sports_dialog1);
				LayoutInflater inflater = getLayoutInflater();
				// View v = inflater.inflate(R.layout.sports_dialog, null);
				View v1 = inflater.inflate(R.layout.sport_dialog_for_newtask,
						null);
				v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
				TextView msg = (TextView) v1.findViewById(R.id.message);
				msg.setText("确认全部删除吗？");
				Button button = (Button) v1.findViewById(R.id.bt_ok);
				Button button2 = (Button) v1.findViewById(R.id.bt_cancel);
				button2.setText("取消");
				button.setText(getResources().getString(R.string.button_ok));
				dialog.setContentView(v1);
				button2.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				button.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						contentDB.deleteAll(true, msApp);
						list.clear();
						// 删除之后重新刷新 listview、重新加载一下适配器。
						if (msApp.isOpenNetwork()) {
							deleteXinlvALL();
						} else {
							Toast.makeText(Xinlvcount.this,
									"网络未连接，本地删除成功服务器删除失败！", Toast.LENGTH_SHORT)
									.show();
						}

						lv_xinlv.setAdapter((ListAdapter) adapter2);
						dialog.dismiss();
					}
				});

				dialog.setCancelable(true);
				dialog.setCanceledOnTouchOutside(false);
				dialog.show();
			}
		});

	}

	private void waitShowDialog(String message) {
		// TODO Auto-generated method stub
		if (mLoadProgressDialog == null) {
			mLoadProgressDialog = new Dialog(this, R.style.sports_dialog);
			LayoutInflater mInflater = getLayoutInflater();
			View v1 = mInflater.inflate(R.layout.bestgirl_progressdialog, null);
			TextView mDialogMessage = (TextView) v1.findViewById(R.id.message);
			mDialogMessage.setText(message);
			v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
			mLoadProgressDialog.setContentView(v1);
		}
		if (mLoadProgressDialog != null)
			if (!mLoadProgressDialog.isShowing() && !this.isFinishing())
				mLoadProgressDialog.show();
		// Log.i(TAG, "isFirstshow----");
	}

	private void iv_btnEvent() {
		// TODO Auto-generated method stub
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onDestroy();
				finish();
				startActivity(new Intent(Xinlvcount.this, XinlvActivity.class));
			}
		});
	}

	@Override
	public void onDismiss() {
		// TODO Auto-generated method stub
		mPopMenuBack.setVisibility(View.GONE);
		WindowManager.LayoutParams lp = this.getWindow()
				.getAttributes();
		lp.alpha = 1f;
		this.getWindow().setAttributes(lp);
	}

	public class XinlvAdapter extends BaseAdapter {
		private List<SleepEffect> list;
		private Context context;

		public XinlvAdapter(List<SleepEffect> list, Context context) {
			super();
			this.list = list;
			this.context = context;
		}

		public XinlvAdapter(List<SleepEffect> list2,
							android.content.DialogInterface.OnClickListener onClickListener) {
			// TODO Auto-generated constructor stub
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
							ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHoder viewHoder;
			if (convertView == null) {
				viewHoder = new ViewHoder();
				convertView = LayoutInflater.from(context).inflate(
						R.layout.item_xinlvcount, parent, false);
				viewHoder.tv1 = (TextView) convertView
						.findViewById(R.id.tv_xinlv_average);
				viewHoder.tv2 = (TextView) convertView
						.findViewById(R.id.tv_time);
				viewHoder.btn1 = (ImageView) convertView
						.findViewById(R.id.btn_delete);
				convertView.setTag(viewHoder);
			} else {
				viewHoder = (ViewHoder) convertView.getTag();
			}
			SleepEffect sleepEffect = list.get(position);
			if (sleepEffect.getHart_rate() != null
					&& !"".equals(sleepEffect.getHart_rate())) {
				double md = Double.valueOf(sleepEffect.getHart_rate());
				int dd = (int) md;
				viewHoder.tv1.setText(dd + "");
			} else {
				viewHoder.tv1.setText("0");
			}
			viewHoder.tv2.setText(sleepEffect.getEndtime());
			viewHoder.btn1.setTag(position);
			// 给Button添加单击事件 添加Button之后ListView将失去焦点 需要的直接把Button的焦点去掉
			viewHoder.btn1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
//					showInfo(position);
					exitPopWindow("确认删除此条记录吗？",position);
				}
			});

			// holder.viewBtn.setOnClickListener(MyListener(position));
			return convertView;
		}

		public void showInfo(final int position) {
			dialog = new Dialog(Xinlvcount.this, R.style.sports_dialog1);
			LayoutInflater inflater = getLayoutInflater();
			// View v = inflater.inflate(R.layout.sports_dialog, null);
			View v1 = inflater.inflate(R.layout.sport_dialog_for_newtask, null);
			v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
			TextView msg = (TextView) v1.findViewById(R.id.message);
			msg.setText("确认删除此条记录吗？");
			Button button = (Button) v1.findViewById(R.id.bt_ok);
			Button button2 = (Button) v1.findViewById(R.id.bt_cancel);
			button2.setText("取消");
			button.setText(getResources().getString(R.string.button_ok));
			dialog.setContentView(v1);
			button2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					SleepEffect sleepEffect = new SleepEffect();
					if (list != null && list.size() != 0) {
						sleepEffect.setEndtime(list.get(position).getEndtime());
						sleepEffect.setHart_rate(list.get(position)
								.getHart_rate());
						sleepEffect.setStarttime(list.get(position)
								.getStarttime());
						sleepEffect.setUid(list.get(position).getUid());
						sleepEffect.setUnique_id(list.get(position)
								.getUnique_id());
						contentDB.deleteSleep(true, sleepEffect);

						aa = list.get(position).getUnique_id();
						list.remove(position);
						if (msApp.isOpenNetwork()) {
							deleteXinlv();
						} else {
							Toast.makeText(Xinlvcount.this,
									"网络未连接，本地删除成功服务器删除失败！", Toast.LENGTH_SHORT)
									.show();
						}
					}
					if (adapter2 != null) {
						adapter2.notifyDataSetChanged();
					}
					dialog.dismiss();
				}
			});

			dialog.setCancelable(true);
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		class ViewHoder {
			TextView tv1;
			TextView tv2;
			ImageView btn1;
		}
	}

	private void exitPopWindow(String message, final int position) {
		WindowManager.LayoutParams lp = this.getWindow()
				.getAttributes();
		lp.alpha = 0.3f;
		this.getWindow().setAttributes(lp);

		if (myWindow != null && myWindow.isShowing())
			return;
		LayoutInflater inflater = LayoutInflater.from(this);
		myView = (LinearLayout) inflater.inflate(R.layout.sports_dialog1, null);
		((TextView) myView.findViewById(R.id.message)).setText(message);

		myView.findViewById(R.id.bt_ok).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SleepEffect sleepEffect = new SleepEffect();
				if (list != null && list.size() != 0) {
					sleepEffect.setEndtime(list.get(position).getEndtime());
					sleepEffect.setHart_rate(list.get(position)
							.getHart_rate());
					sleepEffect.setStarttime(list.get(position)
							.getStarttime());
					sleepEffect.setUid(list.get(position).getUid());
					sleepEffect.setUnique_id(list.get(position)
							.getUnique_id());
					contentDB.deleteSleep(true, sleepEffect);

					aa = list.get(position).getUnique_id();
					list.remove(position);
					if (msApp.isOpenNetwork()) {
						deleteXinlv();
					} else {
						Toast.makeText(Xinlvcount.this,
								"网络未连接，本地删除成功服务器删除失败！", Toast.LENGTH_SHORT)
								.show();
					}
				}
				if (myWindow != null) {
					myWindow.dismiss();
				}
				if (adapter2 != null) {
					adapter2.notifyDataSetChanged();
				}
			}
		});
		myView.findViewById(R.id.bt_cancel).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (myWindow != null) {
					myWindow.dismiss();
				}
				myView.setVisibility(View.GONE);
				mPopMenuBack.setVisibility(View.GONE);
			}
		});
		// myEditCalories
		// .setText(String.valueOf(getDate.getInt("editCalories", 0)));
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		// int height = wm.getDefaultDisplay().getHeight();
		myWindow = new PopupWindow(myView, width - SportsApp.dip2px(70),
				RelativeLayout.LayoutParams.WRAP_CONTENT, true);
		ColorDrawable cd = new ColorDrawable(0x000000);
		myWindow.setBackgroundDrawable(cd);
		myWindow.setTouchable(true);
		myWindow.setOutsideTouchable(true);
		myWindow.setBackgroundDrawable(new BitmapDrawable());
		myWindow.update();
		myWindow.showAtLocation(mainLL, Gravity.CENTER, 0, 0);
		myWindow.setOnDismissListener(this);
		mPopMenuBack.setVisibility(View.VISIBLE);
	}

	// 通过唯一标识删除服务器里面的心律
	private void deleteXinlv() {
		new Thread() {
			@Override
			public void run() {
				Message msg = Message.obtain();
				ApiBack apiBack = null;
				try {
					apiBack = ApiJsonParser.deleteXinlv(msApp.getSessionId(),
							aa);
				} catch (ApiNetException e) {
					e.printStackTrace();
				} catch (ApiSessionOutException e) {
					e.printStackTrace();
				}
				msg.what = 2;
				msg.obj = apiBack;
				xinLvHandler.sendMessage(msg);
			}
		}.start();
	}

	// 个别删除是否成功的判别码
	private Handler xinLvHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 2) {
				ApiBack back3 = (ApiBack) msg.obj;
				if (back3 != null && back3.getFlag() == 1) {
					Toast.makeText(Xinlvcount.this, "删除成功", Toast.LENGTH_SHORT)
							.show();
				} else {
					Toast.makeText(Xinlvcount.this, "删除失败", Toast.LENGTH_SHORT)
							.show();
				}
			}
		};
	};

	// 通过用户名删除一个用户的全部历史记录：
	private void deleteXinlvALL() {
		new Thread() {
			@Override
			public void run() {
				Message msg2 = Message.obtain();
				ApiBack apiBack = null;
				try {
					apiBack = ApiJsonParser.deleteXinlv(msApp.getSessionId());
				} catch (ApiNetException e) {
					e.printStackTrace();
				} catch (ApiSessionOutException e) {
					e.printStackTrace();
				}
				msg2.what = 1;
				msg2.obj = apiBack;
				allXinlvHandler.sendMessage(msg2);
			}
		}.start();
	}

	// 整体删除是否成功的判别码
	private Handler allXinlvHandler = new Handler() {
		public void handleMessage(Message msg2) {
			if (msg2.what == 1) {
				ApiBack back3 = (ApiBack) msg2.obj;
				if (back3 != null && back3.getFlag() == 1) {
					Toast.makeText(Xinlvcount.this, "整体删除成功",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(Xinlvcount.this, "整体删除失败",
							Toast.LENGTH_SHORT).show();
				}
			}
		};
	};


	//集合通过时间排序
//	private  List<SleepEffect> getList(List<SleepEffect> list){
//		List<String> endTimeList = new ArrayList<String>();
//		for (int i = 0; i < list.size(); i++) {
//			endTimeList.add(list.get(i).getEndtime());
//		}
//		for (int i = 0; i < endTimeList.size(); i++) {
//			aa=timeToMillons(endTimeList.get(i));
//		}
//		
//		
//		
//		
//		
//		return null;
//	}

//	private String timeToMillons(String time) {
//		
//		String ss = "";
//		try {
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			long timeStart = sdf.parse(time).getTime();
//			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");
//			Date date = new Date(timeStart);
//			ss = sdf1.format(date);
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//
//		return ss;
//
//	}

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
}
