package com.example.xinlv;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fox.exercise.R;
import com.fox.exercise.api.ApiBack;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.db.SportContentDB;
import com.fox.exercise.newversion.entity.SleepEffect;
import com.jwetherell.heart_rate_monitor.ImageProcessing;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import cn.ingenic.indroidsync.SportsApp;

//import android.view.View;

/**
 * @author zhonghuibin 心率测试主activity
 *
 */
public class XinlvActivity extends Activity {
	// 曲线
	private Timer timer = new Timer();
	private TimerTask task;
	private static int gx;
	private static int j;

	// 健康心率模块引进的成员变量
	private SportContentDB contentDB;
	protected static final int UPDATE_TEXT = 0;
	//	private List<String> lits_xinlv;
	private boolean flagxinlv = true;
	private boolean flagxinlv2 = true;
	private List<Integer> lits_xinlv2;
	private Timer mTimer;
	private TimerTask mTimerTask;
	private Handler mHandler;
	private SportsApp msApp;
	private int count_xinlv;
	int[] count1 = new int[10];
	private String unique_id;// 唯一标示
	private long startTimeSeconds;// 开始计时时间 结束计时时间
	private String startTime1, endtime1;
	private static final int UPLOAD_PARAM_SUCCESS = 10001;
	private static final int UPLOAD_PARAM_ERROR = 10002;
	private Dialog mLoadProgressDialog = null;

	private static double flag = 1;
	private Handler handler;
	private String title = "pulse";
	private XYSeries series;
	private XYMultipleSeriesDataset mDataset;
	private GraphicalView chart;
	private XYMultipleSeriesRenderer renderer;
	private Context context;
	private int addX = -1;
	double addY;
	int[] xv = new int[300];
	int[] yv = new int[300];
	int[] hua = new int[] { 9, 10, 11, 12, 13, 14, 13, 12, 11, 10, 9, 8, 7, 6,
			7, 8, 9, 10, 11, 10, 10 };

	// private static final String TAG = "HeartRateMonitor";
	private static final AtomicBoolean processing = new AtomicBoolean(false);
	private static SurfaceView preview = null;
	private static SurfaceHolder previewHolder = null;
	private static Camera camera = null;
	// private static View image = null;
	private static TextView text = null;
	// 新增的项目
	private static Camera.Parameters parameters;
	private static AnimationDrawable animationDrawable;
	private ImageView animationTopRightView;
	private ImageView iv_back_xinlv, iv_tongji_xinlv, iv_startest_xinlView;
	private static TextView tv_zuijia, tv_xiegang, tv_wuyang;
	private ActionBar actionBar = null;
	//	private static TextView tv_hint;
	private TextView text_frame;
	private LinearLayout ll1;
	//	private ImageView iv_shoushi;
//	private SurfaceView sf;
	private int count = 1;

	//	private static TextView text1 = null;
//	private static TextView text2 = null;
	private static WakeLock wakeLock = null;
	private static int averageIndex = 0;
	private static final int averageArraySize = 4;
	private static final int[] averageArray = new int[averageArraySize];
	private static long lastClickTime;

	public static enum TYPE {
		GREEN, RED
	};

	private static TYPE currentType = TYPE.GREEN;

	public static TYPE getCurrent() {
		return currentType;
	}

	private static int beatsIndex = 0;
	private static final int beatsArraySize = 3;
	private static final int[] beatsArray = new int[beatsArraySize];
	private static double beats = 0;
	private static long startTime = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xinlvactivity);
		initVIew();
		iv_btnEvent();

		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock = pm
				.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");
	}

	// 新增加的点击事件
	private void iv_btnEvent() {
		// TODO Auto-generated method stub
		iv_back_xinlv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (camera != null) {
					try {
						camera.setPreviewCallback(null);
						camera.stopPreview();
						camera.release();
						camera = null;
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				if (flagxinlv2) {
					finish();
				} else {
					Toast.makeText(XinlvActivity.this, "测试中，请先结束测试",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		iv_startest_xinlView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isFastClick2(3000)){
					return;
				}
				flagxinlv2 = false;
				count++;
				if (count % 2 == 0) {
					if (parameters != null) {
						try {
							parameters
									.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
							camera.setParameters(parameters);
							camera.startPreview();
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
					// TODO Auto-generated method stub
					iv_startest_xinlView
							.setImageResource(R.drawable.stoptest_xinlv);
					ll1.setVisibility(LinearLayout.VISIBLE);
					text.setVisibility(TextView.VISIBLE);
					text_frame.setVisibility(TextView.INVISIBLE);
					preview.setVisibility(SurfaceView.VISIBLE);
					startTimeSeconds = System.currentTimeMillis();
					Date startDate = new Date(startTimeSeconds);
					SimpleDateFormat formatter = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					startTime1 = formatter.format(startDate);

					// 后台统计心率点击率
					if (msApp != null && msApp.getSessionId() != null
							&& msApp.isOpenNetwork()) {
						healthCount();
					}

					unique_id = msApp.getSportUser().getUid() + ""
							+ startTimeSeconds;

					animationTopRightView = (ImageView) findViewById(R.id.animation_top_right);
					animationDrawable = (AnimationDrawable) animationTopRightView
							.getBackground();
					animationTopRightView.setVisibility(ImageView.VISIBLE);
					if ( Integer.parseInt((String) text.getText()) != 0) {
						animationDrawable.start();
					}

					// 曲线
					context = getApplicationContext();

					// 这里获得main界面上的布局，下面会把图表画在这个布局里面
					LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout1);

					// 这个类用来放置曲线上的所有点，是一个点的集合，根据这些点画出曲线
					series = new XYSeries(title);

					// 创建一个数据集的实例，这个数据集将被用来创建图表
					mDataset = new XYMultipleSeriesDataset();

					// 将点集添加到这个数据集中
					mDataset.addSeries(series);

					// 以下都是曲线的样式和属性等等的设置，renderer相当于一个用来给图表做渲染的句柄
					int color = Color.GREEN;
					PointStyle style = PointStyle.CIRCLE;
					renderer = buildRenderer(color, style, true);

					// 设置好图表的样式

					// 生成图表
					chart = ChartFactory.getLineChartView(context, mDataset,
							renderer);

					// 将图表添加到布局中去
					layout.addView(chart, new LayoutParams(
							LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
					// -----------------------------------------------------
					// 计时器、每隔固定时间取得心率、最终的平均值返回到服务器。
					mTimer = new Timer();

					mHandler = new Handler() {
						@Override
						public void handleMessage(Message msg) {
							switch (msg.what) {
								case UPDATE_TEXT:
									String a = text.getText().toString();
									int d = Integer.parseInt(a);
									lits_xinlv2.add(d);
									Time t = new Time();
									// or Time t=new Time("GMT+8"); 加上Time Zone资料。
									t.setToNow();
									// 取得系统时间。
//								final int year = t.year;
//								final int month = t.month;
//								final int date = t.monthDay;
//								final int hour = t.hour; // 0-23
//								final int minute = t.minute;
//								final int second = t.second;
									if (flagxinlv) {
										if (lits_xinlv2.size() == 20) {
											// 定时取得的心率和取得心率平均值最后和取得最后一个心律的时间组合成的字符串、返回给服务器。
											// Toast.makeText(XinlvActivity.this,
											// "测试心律完成！", 1).show();
											flagxinlv = false;
											count_xinlv = lits_xinlv2.get(19);
											// startActivity(new
											// Intent(XinlvActivity.this,
											// Xinlvcount.class));

											if (count_xinlv >= 50
													&& count_xinlv <= 130) {
												onDestroy();
												finish();
//											endTimeSeconds = System
//													.currentTimeMillis();
												Date endDate = new Date(
														startTimeSeconds);
												SimpleDateFormat formatter = new SimpleDateFormat(
														"yyyy-MM-dd HH:mm:ss");
												endtime1 = formatter
														.format(endDate);
												timer.cancel();
												SleepEffect sleepEffect = new SleepEffect();
												sleepEffect.setUid(msApp
														.getSportUser().getUid()
														+ "");
												sleepEffect.setUnique_id(unique_id);
												sleepEffect
														.setHart_rate(count_xinlv
																+ "");
												sleepEffect.setEndtime(endtime1);
												sleepEffect
														.setStarttime(startTime1);
												uploadSleep(sleepEffect);
												contentDB.insertXinlv(true,
														sleepEffect);
												startActivity(new Intent(
														XinlvActivity.this,
														Xinlvcount.class));
											} else {
												Toast.makeText(XinlvActivity.this,
														"测试失败，请按要求重新测试",
														Toast.LENGTH_SHORT).show();
												startActivity(new Intent(
														XinlvActivity.this,
														XinlvActivity.class));
											}
										}
									}
									break;
							}
						}
					};
					mTimerTask = new TimerTask() {

						@Override
						public void run() {
							Log.d("AndroidTimerDemo", "timer");
							mHandler.sendEmptyMessage(UPDATE_TEXT);
						}
					};
					// 每隔一秒取一次心率。
					mTimer.schedule(mTimerTask, 1000, 1000);

					// 这里的Handler实例将配合下面的Timer实例，完成定时更新图表的功能
					handler = new Handler() {
						@Override
						public void handleMessage(Message msg) {
							// 刷新图表
							updateChart();
							super.handleMessage(msg);
						}
					};

					task = new TimerTask() {
						@Override
						public void run() {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
						}
					};

					timer.schedule(task, 1, 20); // 曲线

					iv_startest_xinlView
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									if(isFastClick2(3000)){
										return;
									}
									// TODO Auto-generated method stub
									List<SleepEffect> queryUidSleep = contentDB
											.queryUidSleep(msApp.getSportUser()
													.getUid() + "");
									if (camera != null) {
										try {
											camera.setPreviewCallback(null);
											camera.stopPreview();
											camera.release();
											camera = null;
										} catch (Exception e) {
											// TODO: handle exception
										}
									}

									if (lits_xinlv2.size() == 19) {
										count_xinlv = (lits_xinlv2.get(15)
												+ lits_xinlv2.get(14)
												+ lits_xinlv2.get(13)
												+ lits_xinlv2.get(12)
												+ lits_xinlv2.get(11)
												+ lits_xinlv2.get(10)
												+ lits_xinlv2.get(9)
												+ lits_xinlv2.get(8)
												+ lits_xinlv2.get(7)
												+ lits_xinlv2.get(6)
												+ lits_xinlv2.get(5)
												+ lits_xinlv2.get(4) + lits_xinlv2
												.get(3)) / 13;
										flagxinlv = false;
										if (count_xinlv >= 50
												&& count_xinlv <= 130) {
											onDestroy();
											finish();
//											endTimeSeconds = System
//													.currentTimeMillis();
											Date endDate = new Date(
													startTimeSeconds);
											SimpleDateFormat formatter = new SimpleDateFormat(
													"yyyy-MM-dd HH:mm:ss");
											endtime1 = formatter
													.format(endDate);
											timer.cancel();
											SleepEffect sleepEffect = new SleepEffect();
											sleepEffect.setUid(msApp
													.getSportUser().getUid()
													+ "");
											sleepEffect.setUnique_id(unique_id);
											sleepEffect
													.setHart_rate(count_xinlv
															+ "");
											sleepEffect.setEndtime(endtime1);
											sleepEffect
													.setStarttime(startTime1);
											uploadSleep(sleepEffect);
											contentDB.insertXinlv(true,
													sleepEffect);
											startActivity(new Intent(
													XinlvActivity.this,
													Xinlvcount.class));
										} else {
											Toast.makeText(XinlvActivity.this,
													"测试失败，请按要求重新测试",
													Toast.LENGTH_SHORT).show();
											onDestroy();
											finish();
											startActivity(new Intent(
													XinlvActivity.this,
													XinlvActivity.class));
										}
									}
									if (lits_xinlv2.size() == 18) {
										count_xinlv = (lits_xinlv2.get(14)
												+ lits_xinlv2.get(13)
												+ lits_xinlv2.get(12)
												+ lits_xinlv2.get(11)
												+ lits_xinlv2.get(10)
												+ lits_xinlv2.get(9)
												+ lits_xinlv2.get(8)
												+ lits_xinlv2.get(7)
												+ lits_xinlv2.get(6)
												+ lits_xinlv2.get(5)
												+ lits_xinlv2.get(4) + lits_xinlv2
												.get(3)) / 12;
										flagxinlv = false;
										if (count_xinlv >= 50
												&& count_xinlv <= 130) {
											onDestroy();
											finish();
//											endTimeSeconds = System
//													.currentTimeMillis();
											Date endDate = new Date(
													startTimeSeconds);
											SimpleDateFormat formatter = new SimpleDateFormat(
													"yyyy-MM-dd HH:mm:ss");
											endtime1 = formatter
													.format(endDate);
											timer.cancel();
											SleepEffect sleepEffect = new SleepEffect();
											sleepEffect.setUid(msApp
													.getSportUser().getUid()
													+ "");
											sleepEffect.setUnique_id(unique_id);
											sleepEffect
													.setHart_rate(count_xinlv
															+ "");
											sleepEffect.setEndtime(endtime1);
											sleepEffect
													.setStarttime(startTime1);
											uploadSleep(sleepEffect);// 没网的时候这边提示上传失败。
											contentDB.insertXinlv(true,// 插入到本地数据库
													sleepEffect);
											startActivity(new Intent(
													XinlvActivity.this,
													Xinlvcount.class));
										} else {
											Toast.makeText(XinlvActivity.this,
													"测试失败，请按要求重新测试",
													Toast.LENGTH_SHORT).show();
											onDestroy();
											finish();
											startActivity(new Intent(
													XinlvActivity.this,
													XinlvActivity.class));
										}
									}
									if (lits_xinlv2.size() == 17) {
										count_xinlv = (lits_xinlv2.get(13)
												+ lits_xinlv2.get(12)
												+ lits_xinlv2.get(11)
												+ lits_xinlv2.get(10)
												+ lits_xinlv2.get(9)
												+ lits_xinlv2.get(8)
												+ lits_xinlv2.get(7)
												+ lits_xinlv2.get(6)
												+ lits_xinlv2.get(5)
												+ lits_xinlv2.get(4) + lits_xinlv2
												.get(3)) / 11;
										flagxinlv = false;
										if (count_xinlv >= 50
												&& count_xinlv <= 130) {
											onDestroy();
											finish();
//											endTimeSeconds = System
//													.currentTimeMillis();
											Date endDate = new Date(
													startTimeSeconds);
											SimpleDateFormat formatter = new SimpleDateFormat(
													"yyyy-MM-dd HH:mm:ss");
											endtime1 = formatter
													.format(endDate);
											timer.cancel();
											SleepEffect sleepEffect = new SleepEffect();
											sleepEffect.setUid(msApp
													.getSportUser().getUid()
													+ "");
											sleepEffect.setUnique_id(unique_id);
											sleepEffect
													.setHart_rate(count_xinlv
															+ "");
											sleepEffect.setEndtime(endtime1);
											sleepEffect
													.setStarttime(startTime1);
											uploadSleep(sleepEffect);
											contentDB.insertXinlv(true,
													sleepEffect);
											startActivity(new Intent(
													XinlvActivity.this,
													Xinlvcount.class));
										} else {
											Toast.makeText(XinlvActivity.this,
													"测试失败，请按要求重新测试",
													Toast.LENGTH_SHORT).show();
											onDestroy();
											finish();
											startActivity(new Intent(
													XinlvActivity.this,
													XinlvActivity.class));
										}
									}
									if (lits_xinlv2.size() == 16) {
										count_xinlv = (lits_xinlv2.get(12)
												+ lits_xinlv2.get(11)
												+ lits_xinlv2.get(10)
												+ lits_xinlv2.get(9)
												+ lits_xinlv2.get(8)
												+ lits_xinlv2.get(7)
												+ lits_xinlv2.get(6)
												+ lits_xinlv2.get(5)
												+ lits_xinlv2.get(4) + lits_xinlv2
												.get(3)) / 10;
										flagxinlv = false;
										if (count_xinlv >= 50
												&& count_xinlv <= 130) {
											onDestroy();
											finish();
//											endTimeSeconds = System
//													.currentTimeMillis();
											Date endDate = new Date(
													startTimeSeconds);
											SimpleDateFormat formatter = new SimpleDateFormat(
													"yyyy-MM-dd HH:mm:ss");
											endtime1 = formatter
													.format(endDate);
											timer.cancel();
											SleepEffect sleepEffect = new SleepEffect();
											sleepEffect.setUid(msApp
													.getSportUser().getUid()
													+ "");
											sleepEffect.setUnique_id(unique_id);
											sleepEffect
													.setHart_rate(count_xinlv
															+ "");
											sleepEffect.setEndtime(endtime1);
											sleepEffect
													.setStarttime(startTime1);
											uploadSleep(sleepEffect);
											contentDB.insertXinlv(true,
													sleepEffect);
											startActivity(new Intent(
													XinlvActivity.this,
													Xinlvcount.class));
										} else {
											Toast.makeText(XinlvActivity.this,
													"测试失败，请按要求重新测试",
													Toast.LENGTH_SHORT).show();
											onDestroy();
											finish();
											startActivity(new Intent(
													XinlvActivity.this,
													XinlvActivity.class));
										}
									}
									if (lits_xinlv2.size() == 15) {
										count_xinlv = (lits_xinlv2.get(11)
												+ lits_xinlv2.get(10)
												+ lits_xinlv2.get(9)
												+ lits_xinlv2.get(8)
												+ lits_xinlv2.get(7)
												+ lits_xinlv2.get(6)
												+ lits_xinlv2.get(5)
												+ lits_xinlv2.get(4) + lits_xinlv2
												.get(3)) / 9;
										flagxinlv = false;
										if (count_xinlv >= 50
												&& count_xinlv <= 130) {
											onDestroy();
											finish();
//											endTimeSeconds = System
//													.currentTimeMillis();
											Date endDate = new Date(
													startTimeSeconds);
											SimpleDateFormat formatter = new SimpleDateFormat(
													"yyyy-MM-dd HH:mm:ss");
											endtime1 = formatter
													.format(endDate);
											timer.cancel();
											SleepEffect sleepEffect = new SleepEffect();
											sleepEffect.setUid(msApp
													.getSportUser().getUid()
													+ "");
											sleepEffect.setUnique_id(unique_id);
											sleepEffect
													.setHart_rate(count_xinlv
															+ "");
											sleepEffect.setEndtime(endtime1);
											sleepEffect
													.setStarttime(startTime1);
											uploadSleep(sleepEffect);
											contentDB.insertXinlv(true,
													sleepEffect);
											startActivity(new Intent(
													XinlvActivity.this,
													Xinlvcount.class));
										} else {
											Toast.makeText(XinlvActivity.this,
													"测试失败，请按要求重新测试",
													Toast.LENGTH_SHORT).show();
											onDestroy();
											finish();
											startActivity(new Intent(
													XinlvActivity.this,
													XinlvActivity.class));
										}
									}
									if (lits_xinlv2.size() == 14) {
										count_xinlv = (lits_xinlv2.get(10)
												+ lits_xinlv2.get(9)
												+ lits_xinlv2.get(8)
												+ lits_xinlv2.get(7)
												+ lits_xinlv2.get(6)
												+ lits_xinlv2.get(5)
												+ lits_xinlv2.get(4) + lits_xinlv2
												.get(3)) / 8;
										flagxinlv = false;
										if (count_xinlv >= 50
												&& count_xinlv <= 130) {
											onDestroy();
											finish();
//											endTimeSeconds = System
//													.currentTimeMillis();
											Date endDate = new Date(
													startTimeSeconds);
											SimpleDateFormat formatter = new SimpleDateFormat(
													"yyyy-MM-dd HH:mm:ss");
											endtime1 = formatter
													.format(endDate);
											timer.cancel();
											SleepEffect sleepEffect = new SleepEffect();
											sleepEffect.setUid(msApp
													.getSportUser().getUid()
													+ "");
											sleepEffect.setUnique_id(unique_id);
											sleepEffect
													.setHart_rate(count_xinlv
															+ "");
											sleepEffect.setEndtime(endtime1);
											sleepEffect
													.setStarttime(startTime1);
											uploadSleep(sleepEffect);
											contentDB.insertXinlv(true,
													sleepEffect);
											startActivity(new Intent(
													XinlvActivity.this,
													Xinlvcount.class));
										} else {
											Toast.makeText(XinlvActivity.this,
													"测试失败，请按要求重新测试",
													Toast.LENGTH_SHORT).show();
											onDestroy();
											finish();
											startActivity(new Intent(
													XinlvActivity.this,
													XinlvActivity.class));
										}
									}
									if (lits_xinlv2.size() == 13) {
										count_xinlv = (lits_xinlv2.get(9)
												+ lits_xinlv2.get(8)
												+ lits_xinlv2.get(7)
												+ lits_xinlv2.get(6)
												+ lits_xinlv2.get(5)
												+ lits_xinlv2.get(4) + lits_xinlv2
												.get(3)) / 7;
										flagxinlv = false;
										if (count_xinlv >= 50
												&& count_xinlv <= 130) {
											onDestroy();
											finish();
//											endTimeSeconds = System
//													.currentTimeMillis();
											Date endDate = new Date(
													startTimeSeconds);
											SimpleDateFormat formatter = new SimpleDateFormat(
													"yyyy-MM-dd HH:mm:ss");
											endtime1 = formatter
													.format(endDate);
											timer.cancel();
											SleepEffect sleepEffect = new SleepEffect();
											sleepEffect.setUid(msApp
													.getSportUser().getUid()
													+ "");
											sleepEffect.setUnique_id(unique_id);
											sleepEffect
													.setHart_rate(count_xinlv
															+ "");
											sleepEffect.setEndtime(endtime1);
											sleepEffect
													.setStarttime(startTime1);
											uploadSleep(sleepEffect);
											contentDB.insertXinlv(true,
													sleepEffect);
											startActivity(new Intent(
													XinlvActivity.this,
													Xinlvcount.class));
										} else {
											Toast.makeText(XinlvActivity.this,
													"测试失败，请按要求重新测试",
													Toast.LENGTH_SHORT).show();
											onDestroy();
											finish();
											startActivity(new Intent(
													XinlvActivity.this,
													XinlvActivity.class));
										}
									}
									if (lits_xinlv2.size() == 12) {
										count_xinlv = (lits_xinlv2.get(10)
												+ lits_xinlv2.get(9)
												+ lits_xinlv2.get(8)
												+ lits_xinlv2.get(7)
												+ lits_xinlv2.get(6)
												+ lits_xinlv2.get(5)
												+ lits_xinlv2.get(4) + lits_xinlv2
												.get(3)) / 8;
										flagxinlv = false;
										if (count_xinlv >= 50
												&& count_xinlv <= 130) {
											onDestroy();
											finish();
//											endTimeSeconds = System
//													.currentTimeMillis();
											Date endDate = new Date(
													startTimeSeconds);
											SimpleDateFormat formatter = new SimpleDateFormat(
													"yyyy-MM-dd HH:mm:ss");
											endtime1 = formatter
													.format(endDate);
											timer.cancel();
											SleepEffect sleepEffect = new SleepEffect();
											sleepEffect.setUid(msApp
													.getSportUser().getUid()
													+ "");
											sleepEffect.setUnique_id(unique_id);
											sleepEffect
													.setHart_rate(count_xinlv
															+ "");
											sleepEffect.setEndtime(endtime1);
											sleepEffect
													.setStarttime(startTime1);
											uploadSleep(sleepEffect);
											contentDB.insertXinlv(true,
													sleepEffect);
											startActivity(new Intent(
													XinlvActivity.this,
													Xinlvcount.class));
										} else {
											Toast.makeText(XinlvActivity.this,
													"测试失败，请按要求重新测试",
													Toast.LENGTH_SHORT).show();
											onDestroy();
											finish();
											startActivity(new Intent(
													XinlvActivity.this,
													XinlvActivity.class));
										}
									}
									if (lits_xinlv2.size() == 11) {
										count_xinlv = (lits_xinlv2.get(9)
												+ lits_xinlv2.get(8)
												+ lits_xinlv2.get(7)
												+ lits_xinlv2.get(6)
												+ lits_xinlv2.get(5)
												+ lits_xinlv2.get(4) + lits_xinlv2
												.get(3)) / 7;
										flagxinlv = false;
										if (count_xinlv >= 50
												&& count_xinlv <= 130) {
											onDestroy();
											finish();
//											endTimeSeconds = System
//													.currentTimeMillis();
											Date endDate = new Date(
													startTimeSeconds);
											SimpleDateFormat formatter = new SimpleDateFormat(
													"yyyy-MM-dd HH:mm:ss");
											endtime1 = formatter
													.format(endDate);
											timer.cancel();
											SleepEffect sleepEffect = new SleepEffect();
											sleepEffect.setUid(msApp
													.getSportUser().getUid()
													+ "");
											sleepEffect.setUnique_id(unique_id);
											sleepEffect
													.setHart_rate(count_xinlv
															+ "");
											sleepEffect.setEndtime(endtime1);
											sleepEffect
													.setStarttime(startTime1);
											uploadSleep(sleepEffect);
											contentDB.insertXinlv(true,
													sleepEffect);
											startActivity(new Intent(
													XinlvActivity.this,
													Xinlvcount.class));
										} else {
											Toast.makeText(XinlvActivity.this,
													"测试失败，请按要求重新测试",
													Toast.LENGTH_SHORT).show();
											onDestroy();
											finish();
											startActivity(new Intent(
													XinlvActivity.this,
													XinlvActivity.class));
										}
									}
									if (lits_xinlv2.size() == 10) {
										count_xinlv = (lits_xinlv2.get(8)
												+ lits_xinlv2.get(7)
												+ lits_xinlv2.get(6)
												+ lits_xinlv2.get(5)
												+ lits_xinlv2.get(4) + lits_xinlv2
												.get(3)) / 6;
										flagxinlv = false;
										if (count_xinlv >= 50
												&& count_xinlv <= 130) {
											onDestroy();
											finish();
//											endTimeSeconds = System
//													.currentTimeMillis();
											Date endDate = new Date(
													startTimeSeconds);
											SimpleDateFormat formatter = new SimpleDateFormat(
													"yyyy-MM-dd HH:mm:ss");
											endtime1 = formatter
													.format(endDate);
											timer.cancel();
											SleepEffect sleepEffect = new SleepEffect();
											sleepEffect.setUid(msApp
													.getSportUser().getUid()
													+ "");
											sleepEffect.setUnique_id(unique_id);
											sleepEffect
													.setHart_rate(count_xinlv
															+ "");
											sleepEffect.setEndtime(endtime1);
											sleepEffect
													.setStarttime(startTime1);
											uploadSleep(sleepEffect);
											contentDB.insertXinlv(true,
													sleepEffect);
											startActivity(new Intent(
													XinlvActivity.this,
													Xinlvcount.class));
										} else {
											Toast.makeText(XinlvActivity.this,
													"测试失败，请按要求重新测试",
													Toast.LENGTH_SHORT).show();
											onDestroy();
											finish();
											startActivity(new Intent(
													XinlvActivity.this,
													XinlvActivity.class));
										}
									}
									if (lits_xinlv2.size() == 9) {
										count_xinlv = (lits_xinlv2.get(8)
												+ lits_xinlv2.get(7)
												+ lits_xinlv2.get(6)
												+ lits_xinlv2.get(5)
												+ lits_xinlv2.get(4) + lits_xinlv2
												.get(3)) / 6;
										flagxinlv = false;
										if (count_xinlv >= 50
												&& count_xinlv <= 130) {
											onDestroy();
											finish();
//											endTimeSeconds = System
//													.currentTimeMillis();
											Date endDate = new Date(
													startTimeSeconds);
											SimpleDateFormat formatter = new SimpleDateFormat(
													"yyyy-MM-dd HH:mm:ss");
											endtime1 = formatter
													.format(endDate);
											timer.cancel();
											SleepEffect sleepEffect = new SleepEffect();
											sleepEffect.setUid(msApp
													.getSportUser().getUid()
													+ "");
											sleepEffect.setUnique_id(unique_id);
											sleepEffect
													.setHart_rate(count_xinlv
															+ "");
											sleepEffect.setEndtime(endtime1);
											sleepEffect
													.setStarttime(startTime1);
											uploadSleep(sleepEffect);
											contentDB.insertXinlv(true,
													sleepEffect);
											startActivity(new Intent(
													XinlvActivity.this,
													Xinlvcount.class));
										} else {
											Toast.makeText(XinlvActivity.this,
													"测试失败，请按要求重新测试",
													Toast.LENGTH_SHORT).show();
											onDestroy();
											finish();
											startActivity(new Intent(
													XinlvActivity.this,
													XinlvActivity.class));
										}
									}
									if (lits_xinlv2.size() == 8) {
										count_xinlv = (lits_xinlv2.get(7)
												+ lits_xinlv2.get(6)
												+ lits_xinlv2.get(5)
												+ lits_xinlv2.get(4) + lits_xinlv2
												.get(3)) / 5;
										flagxinlv = false;
										if (count_xinlv >= 50
												&& count_xinlv <= 130) {
											onDestroy();
											finish();
//											endTimeSeconds = System
//													.currentTimeMillis();
											Date endDate = new Date(
													startTimeSeconds);
											SimpleDateFormat formatter = new SimpleDateFormat(
													"yyyy-MM-dd HH:mm:ss");
											endtime1 = formatter
													.format(endDate);
											timer.cancel();
											SleepEffect sleepEffect = new SleepEffect();
											sleepEffect.setUid(msApp
													.getSportUser().getUid()
													+ "");
											sleepEffect.setUnique_id(unique_id);
											sleepEffect
													.setHart_rate(count_xinlv
															+ "");
											sleepEffect.setEndtime(endtime1);
											sleepEffect
													.setStarttime(startTime1);
											uploadSleep(sleepEffect);
											contentDB.insertXinlv(true,
													sleepEffect);
											startActivity(new Intent(
													XinlvActivity.this,
													Xinlvcount.class));
										} else {
											Toast.makeText(XinlvActivity.this,
													"测试失败，请按要求重新测试",
													Toast.LENGTH_SHORT).show();
											onDestroy();
											finish();
											startActivity(new Intent(
													XinlvActivity.this,
													XinlvActivity.class));
										}
									}
									if (lits_xinlv2.size() == 7) {
										count_xinlv = (lits_xinlv2.get(6)
												+ lits_xinlv2.get(5)
												+ lits_xinlv2.get(4) + lits_xinlv2
												.get(3)) / 4;
										flagxinlv = false;
										if (count_xinlv >= 50
												&& count_xinlv <= 130) {
											onDestroy();
											finish();
//											endTimeSeconds = System
//													.currentTimeMillis();
											Date endDate = new Date(
													startTimeSeconds);
											SimpleDateFormat formatter = new SimpleDateFormat(
													"yyyy-MM-dd HH:mm:ss");
											endtime1 = formatter
													.format(endDate);
											timer.cancel();
											SleepEffect sleepEffect = new SleepEffect();
											sleepEffect.setUid(msApp
													.getSportUser().getUid()
													+ "");
											sleepEffect.setUnique_id(unique_id);
											sleepEffect
													.setHart_rate(count_xinlv
															+ "");
											sleepEffect.setEndtime(endtime1);
											sleepEffect
													.setStarttime(startTime1);
											uploadSleep(sleepEffect);
											contentDB.insertXinlv(true,
													sleepEffect);
											startActivity(new Intent(
													XinlvActivity.this,
													Xinlvcount.class));
										} else {
											Toast.makeText(XinlvActivity.this,
													"测试失败，请按要求重新测试",
													Toast.LENGTH_SHORT).show();
											onDestroy();
											finish();
											startActivity(new Intent(
													XinlvActivity.this,
													XinlvActivity.class));
										}
									}
									if (lits_xinlv2.size() == 6) {
										count_xinlv = (lits_xinlv2.get(5)
												+ lits_xinlv2.get(4) + lits_xinlv2
												.get(3)) / 3;
										flagxinlv = false;
										if (count_xinlv >= 50
												&& count_xinlv <= 130) {
											onDestroy();
											finish();
//											endTimeSeconds = System
//													.currentTimeMillis();
											Date endDate = new Date(
													startTimeSeconds);
											SimpleDateFormat formatter = new SimpleDateFormat(
													"yyyy-MM-dd HH:mm:ss");
											endtime1 = formatter
													.format(endDate);
											timer.cancel();
											SleepEffect sleepEffect = new SleepEffect();
											sleepEffect.setUid(msApp
													.getSportUser().getUid()
													+ "");
											sleepEffect.setUnique_id(unique_id);
											sleepEffect
													.setHart_rate(count_xinlv
															+ "");
											sleepEffect.setEndtime(endtime1);
											sleepEffect
													.setStarttime(startTime1);
											uploadSleep(sleepEffect);
											contentDB.insertXinlv(true,
													sleepEffect);
											startActivity(new Intent(
													XinlvActivity.this,
													Xinlvcount.class));
										} else {
											Toast.makeText(XinlvActivity.this,
													"测试失败，请按要求重新测试",
													Toast.LENGTH_SHORT).show();
											onDestroy();
											finish();
											startActivity(new Intent(
													XinlvActivity.this,
													XinlvActivity.class));
										}
									}
									if (lits_xinlv2.size() == 5) {
										count_xinlv = (lits_xinlv2.get(4)
												+ lits_xinlv2.get(3) + lits_xinlv2
												.get(2)) / 3;
										flagxinlv = false;
										if (count_xinlv >= 50
												&& count_xinlv <= 130) {
											onDestroy();
											finish();
//											endTimeSeconds = System
//													.currentTimeMillis();
											Date endDate = new Date(
													startTimeSeconds);
											SimpleDateFormat formatter = new SimpleDateFormat(
													"yyyy-MM-dd HH:mm:ss");
											endtime1 = formatter
													.format(endDate);
											timer.cancel();
											SleepEffect sleepEffect = new SleepEffect();
											sleepEffect.setUid(msApp
													.getSportUser().getUid()
													+ "");
											sleepEffect.setUnique_id(unique_id);
											sleepEffect
													.setHart_rate(count_xinlv
															+ "");
											sleepEffect.setEndtime(endtime1);
											sleepEffect
													.setStarttime(startTime1);
											uploadSleep(sleepEffect);
											contentDB.insertXinlv(true,
													sleepEffect);
											startActivity(new Intent(
													XinlvActivity.this,
													Xinlvcount.class));
										} else {
											Toast.makeText(XinlvActivity.this,
													"测试失败，请按要求重新测试",
													Toast.LENGTH_SHORT).show();
											onDestroy();
											finish();
											startActivity(new Intent(
													XinlvActivity.this,
													XinlvActivity.class));
										}
									}
									if (lits_xinlv2.size() == 4) {
										count_xinlv = (lits_xinlv2.get(3) + lits_xinlv2
												.get(2)) / 2;
										flagxinlv = false;
										if (count_xinlv >= 50
												&& count_xinlv <= 130) {
											onDestroy();
											finish();
//											endTimeSeconds = System
//													.currentTimeMillis();
											Date endDate = new Date(
													startTimeSeconds);
											SimpleDateFormat formatter = new SimpleDateFormat(
													"yyyy-MM-dd HH:mm:ss");
											endtime1 = formatter
													.format(endDate);
											timer.cancel();
											SleepEffect sleepEffect = new SleepEffect();
											sleepEffect.setUid(msApp
													.getSportUser().getUid()
													+ "");
											sleepEffect.setUnique_id(unique_id);
											sleepEffect
													.setHart_rate(count_xinlv
															+ "");
											sleepEffect.setEndtime(endtime1);
											sleepEffect
													.setStarttime(startTime1);
											uploadSleep(sleepEffect);
											contentDB.insertXinlv(true,
													sleepEffect);
											startActivity(new Intent(
													XinlvActivity.this,
													Xinlvcount.class));
										} else {
											Toast.makeText(XinlvActivity.this,
													"测试失败，请按要求重新测试",
													Toast.LENGTH_SHORT).show();
											onDestroy();
											finish();
											startActivity(new Intent(
													XinlvActivity.this,
													XinlvActivity.class));
										}
									}
									if (lits_xinlv2.size() == 3) {
										count_xinlv = lits_xinlv2.get(2);
										flagxinlv = false;
										if (count_xinlv >= 50
												&& count_xinlv <= 130) {
											onDestroy();
											finish();
//											endTimeSeconds = System
//													.currentTimeMillis();
											Date endDate = new Date(
													startTimeSeconds);
											SimpleDateFormat formatter = new SimpleDateFormat(
													"yyyy-MM-dd HH:mm:ss");
											endtime1 = formatter
													.format(endDate);
											timer.cancel();
											SleepEffect sleepEffect = new SleepEffect();
											sleepEffect.setUid(msApp
													.getSportUser().getUid()
													+ "");
											sleepEffect.setUnique_id(unique_id);
											sleepEffect
													.setHart_rate(count_xinlv
															+ "");
											sleepEffect.setEndtime(endtime1);
											sleepEffect
													.setStarttime(startTime1);
											uploadSleep(sleepEffect);
											contentDB.insertXinlv(true,
													sleepEffect);
											startActivity(new Intent(
													XinlvActivity.this,
													Xinlvcount.class));
										} else {
											Toast.makeText(XinlvActivity.this,
													"测试失败，请按要求重新测试",
													Toast.LENGTH_SHORT).show();
											onDestroy();
											finish();
											startActivity(new Intent(
													XinlvActivity.this,
													XinlvActivity.class));
										}
									}
								}
							});
				} else {
					iv_startest_xinlView
							.setImageResource(R.drawable.startest_xinlv);
					animationTopRightView.setVisibility(ImageView.INVISIBLE);
					if (animationDrawable.isRunning()) {
						animationDrawable.stop();
					}
					ll1.setVisibility(LinearLayout.VISIBLE);
					preview.setVisibility(SurfaceView.INVISIBLE);
				}
			}
		});

		iv_tongji_xinlv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (flagxinlv2) {
					startActivity(new Intent(XinlvActivity.this,
							Xinlvcount.class));
					onDestroy();
					finish();
				} else {
					Toast.makeText(XinlvActivity.this, "测试中，请先结束测试",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		// if (flagxinlv) {
		// if (lits_xinlv2.size()==20){
		// flagxinlv=false;
		// count_xinlv=lits_xinlv2.get(19);
		// if (count_xinlv>=50) {
		// onDestroy();
		// finish();
		// endTimeSeconds = System.currentTimeMillis();
		// Date endDate = new Date(startTimeSeconds);
		// SimpleDateFormat formatter = new
		// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// endtime1 = formatter.format(endDate);
		// timer.cancel();
		// SleepEffect sleepEffect=new SleepEffect();
		// sleepEffect.setUid(msApp.getSportUser().getUid() + "");
		// sleepEffect.setUnique_id(unique_id);
		// sleepEffect.setHart_rate(count_xinlv+"");
		// sleepEffect.setEndtime(endtime1);
		// sleepEffect.setStarttime(startTime1);
		// uploadSleep(sleepEffect);
		// contentDB.insertXinlv(true, sleepEffect);
		// startActivity(new Intent(XinlvActivity.this,Xinlvcount.class));
		// }else {
		// Toast.makeText(XinlvActivity.this, "测试失败，请按要求重新测试", 1).show();
		// startActivity(new Intent(XinlvActivity.this,XinlvActivity.class));
		// }
		// }
		// }
	}

	// 新增加的方法
	private void initVIew() {
		// TODO Auto-generated method stub

		contentDB = SportContentDB.getInstance(XinlvActivity.this);
//		lits_xinlv = new ArrayList<String>();
		lits_xinlv2 = new ArrayList<Integer>();
		tv_zuijia = (TextView) findViewById(R.id.tv_zuijia);
		tv_xiegang = (TextView) findViewById(R.id.tv_xiegang);
		tv_wuyang = (TextView) findViewById(R.id.tv_wuyang);
		msApp = (SportsApp) getApplication();
		actionBar = getActionBar();
		actionBar.hide();
		preview = (SurfaceView) findViewById(R.id.preview);
		previewHolder = preview.getHolder();
		previewHolder.addCallback(surfaceCallback);
		previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		text = (TextView) findViewById(R.id.text);
//		tv_hint = (TextView) findViewById(R.id.tv_hint);
		ll1 = (LinearLayout) findViewById(R.id.linearLayout1);
		text_frame = (TextView) findViewById(R.id.text_frame);
//		iv_shoushi = (ImageView) findViewById(R.id.iv_shoushi_xinlv);
//		sf = (SurfaceView) findViewById(R.id.preview);
		iv_back_xinlv = (ImageView) findViewById(R.id.iv_back_xinlv);
		iv_tongji_xinlv = (ImageView) findViewById(R.id.iv_tongji_xinlv);
		iv_startest_xinlView = (ImageView) findViewById(R.id.iv_startest_xinlv);

	}

	// 曲线
	@Override
	public void onDestroy() {
		// 当结束程序时关掉Timer
		timer.cancel();
//		preview = null;
//		previewHolder = null;
//		camera = null;
//		text = null;
//		parameters=null;
//		animationDrawable=null;
//		msApp=null;
//		context=null;
		super.onDestroy();
	};

	protected XYMultipleSeriesRenderer buildRenderer(int color,
													 PointStyle style, boolean fill) {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();

		// 设置图表中曲线本身的样式，包括颜色、点的大小以及线的粗细等
		XYSeriesRenderer r = new XYSeriesRenderer();
		r.setColor(Color.RED);
		// r.setPointStyle(null);
		// r.setFillPoints(fill);
		r.setLineWidth(1);
		renderer.addSeriesRenderer(r);
		return renderer;
	}

	protected void setChartSettings(XYMultipleSeriesRenderer renderer,
									String xTitle, String yTitle, double xMin, double xMax,
									double yMin, double yMax, int axesColor, int labelsColor) {
		// 有关对图表的渲染可参看api文档
		renderer.setChartTitle(title);
		renderer.setXTitle(xTitle);
		renderer.setYTitle(yTitle);
		renderer.setXAxisMin(xMin);
		renderer.setXAxisMax(xMax);
		renderer.setYAxisMin(yMin);
		renderer.setYAxisMax(yMax);
		renderer.setAxesColor(axesColor);
		renderer.setLabelsColor(labelsColor);
		renderer.setShowGrid(true);
		renderer.setGridColor(Color.parseColor("#151B1E"));
		renderer.setXLabels(20);
		renderer.setYLabels(10);
		renderer.setXTitle("Time");
		renderer.setYTitle("mmHg");
		renderer.setYLabelsAlign(Align.RIGHT);
		renderer.setPointSize((float) 3);
		renderer.setShowLegend(false);
	}

	private void updateChart() {

		if (flag == 1)
			addY = 10;
		else {
			flag = 1;
			if (gx < 150) {
				if (hua[20] > 1) {
					text.setText("00");
					if (animationDrawable.isRunning()) {
						animationDrawable.stop();
					}
					animationTopRightView.setVisibility(ImageView.INVISIBLE);
					hua[20] = 0;
				}
				hua[20]++;
				return;
			} else
				animationTopRightView.setVisibility(ImageView.VISIBLE);
			if (Integer.parseInt((String) text.getText()) != 0) {
				animationDrawable.start();
			}
			hua[20] = 10;
			j = 0;

		}
		if (j < 20) {
			addY = hua[j];
			j++;
		}

		// 移除数据集中旧的点集
		mDataset.removeSeries(series);

		// 判断当前点集中到底有多少点，因为屏幕总共只能容纳100个，所以当点数超过100时，长度永远是100
		int length = series.getItemCount();
		int bz = 0;
		if (length > 300) {
			length = 300;
			bz = 1;
		}
		addX = length;
		// 将旧的点集中x和y的数值取出来放入backup中，并且将x的值加1，造成曲线向右平移的效果
		for (int i = 0; i < length; i++) {
			xv[i] = (int) series.getX(i) - bz;
			yv[i] = (int) series.getY(i);
		}

		// 点集先清空，为了做成新的点集而准备
		series.clear();
		mDataset.addSeries(series);
		// 将新产生的点首先加入到点集中，然后在循环体中将坐标变换后的一系列点都重新加入到点集中
		// 这里可以试验一下把顺序颠倒过来是什么效果，即先运行循环体，再添加新产生的点
		series.add(addX, addY);
		for (int k = 0; k < length; k++) {
			series.add(xv[k], yv[k]);
		}

		// 视图更新，没有这一步，曲线不会呈现动态
		// 如果在非UI主线程中，需要调用postInvalidate()，具体参考api
		chart.invalidate();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (wakeLock != null) {
			wakeLock.acquire();
		}
		try {
			camera = Camera.open();
			startTime = System.currentTimeMillis();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		wakeLock.release();
		if (camera != null) {
			try {
				camera.setPreviewCallback(null);
				camera.stopPreview();
				camera.release();
				camera = null;
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	}

	private static PreviewCallback previewCallback = new PreviewCallback() {

		public void onPreviewFrame(byte[] data, Camera cam) {
			if (data == null)
				throw new NullPointerException();
			Camera.Size size = cam.getParameters().getPreviewSize();
			if (size == null)
				throw new NullPointerException();
			if (!processing.compareAndSet(false, true))
				return;
			int width = size.width;
			int height = size.height;
			// 图像处理
			int imgAvg = ImageProcessing.decodeYUV420SPtoRedAvg(data.clone(),
					height, width);
			gx = imgAvg;
			if (imgAvg == 0 || imgAvg == 255) {
				processing.set(false);
				return;
			}

			int averageArrayAvg = 0;
			int averageArrayCnt = 0;
			for (int i = 0; i < averageArray.length; i++) {
				if (averageArray[i] > 0) {
					averageArrayAvg += averageArray[i];
					averageArrayCnt++;
				}
			}

			int rollingAverage = (averageArrayCnt > 0) ? (averageArrayAvg / averageArrayCnt)
					: 0;
			TYPE newType = currentType;
			if (imgAvg < rollingAverage) {
				newType = TYPE.RED;
				if (newType != currentType) {
					beats++;
					flag = 0;
				}
			} else if (imgAvg > rollingAverage) {
				newType = TYPE.GREEN;
			}

			if (averageIndex == averageArraySize)
				averageIndex = 0;
			averageArray[averageIndex] = imgAvg;
			averageIndex++;

			if (newType != currentType) {
				currentType = newType;
			}
			// 获取系统结束时间（ms）
			long endTime = System.currentTimeMillis();
			double totalTimeInSecs = (endTime - startTime) / 1000d;
			if (totalTimeInSecs >= 2) {
				double bps = (beats / totalTimeInSecs);
				int dpm = (int) (bps * 60d);
				if (dpm < 30 || dpm > 180 || imgAvg < 150) {
					// 获取系统开始时间（ms）
					startTime = System.currentTimeMillis();
					// beats心跳总数
					beats = 0;
					processing.set(false);
					return;
				}
				if (beatsIndex == beatsArraySize)
					beatsIndex = 0;
				beatsArray[beatsIndex] = dpm;
				beatsIndex++;
				int beatsArrayAvg = 0;
				int beatsArrayCnt = 0;
				for (int i = 0; i < beatsArray.length; i++) {
					if (beatsArray[i] > 0) {
						beatsArrayAvg += beatsArray[i];
						beatsArrayCnt++;
					}
				}
				int beatsAvg = (beatsArrayAvg / beatsArrayCnt);
				if (beatsAvg >= 90 && beatsAvg <= 140) {
					tv_zuijia.setTextColor(Color.parseColor("#00ffc6"));
					tv_xiegang.setTextColor(Color.parseColor("#00ffc6"));
					tv_wuyang.setTextColor(Color.parseColor("#028269"));
				} else if (beatsAvg > 140) {
					tv_wuyang.setTextColor(Color.parseColor("#00ffc6"));
					tv_xiegang.setTextColor(Color.parseColor("#00ffc6"));
					tv_zuijia.setTextColor(Color.parseColor("#028269"));
				} else {
					tv_zuijia.setTextColor(Color.parseColor("#028269"));
					tv_wuyang.setTextColor(Color.parseColor("#028269"));
					tv_xiegang.setTextColor(Color.parseColor("#028269"));
				}
				text.setText(String.valueOf(beatsAvg));
				startTime = System.currentTimeMillis();
				beats = 0;
			}
			processing.set(false);
		}
	};

	private static SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {

		public void surfaceCreated(SurfaceHolder holder) {
			try {
				camera.setPreviewDisplay(previewHolder);
				camera.setPreviewCallback(previewCallback);
			} catch (Throwable t) {
			}
		}

		public void surfaceChanged(SurfaceHolder holder, int format, int width,
								   int height) {
			try {
				parameters = camera.getParameters();
				Camera.Size size = getSmallestPreviewSize(width, height,
						parameters);
				if (size != null) {
					parameters.setPreviewSize(size.width, size.height);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
		}
	};

	private static Camera.Size getSmallestPreviewSize(int width, int height,
													  Camera.Parameters parameters) {
		Camera.Size result = null;
		for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
			if (size.width <= width && size.height <= height) {
				if (result == null) {
					result = size;
				} else {
					int resultArea = result.width * result.height;
					int newArea = size.width * size.height;
					if (newArea < resultArea)
						result = size;
				}
			}
		}
		return result;
	}

	private void uploadSleep(final SleepEffect sleepEffect) {
		waitShowDialog();
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message msg = Message.obtain();
				ApiBack apiBack = null;
				try {
					apiBack = ApiJsonParser.uploadSleep(msApp.getSessionId(),
							sleepEffect, false);
					if (apiBack != null && apiBack.getFlag() == 1) {
						msg.what = UPLOAD_PARAM_SUCCESS;
						msg.obj = apiBack;
						sleepHandler.sendMessage(msg);
					} else {
						msg.what = UPLOAD_PARAM_ERROR;
						msg.obj = apiBack;
						sleepHandler.sendMessage(msg);
					}
				} catch (Exception e) {
					// TODO: handle exception
					msg.what = UPLOAD_PARAM_ERROR;
					msg.obj = apiBack;
					sleepHandler.sendMessage(msg);
				}
			}
		}.start();
	}

	private Handler sleepHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case UPLOAD_PARAM_SUCCESS:
					if (mLoadProgressDialog != null)
						if (mLoadProgressDialog.isShowing())
							mLoadProgressDialog.dismiss();
					Toast.makeText(XinlvActivity.this,
							getResources().getString(R.string.upload_success),
							Toast.LENGTH_SHORT).show();
					break;
				case UPLOAD_PARAM_ERROR:
					if (mLoadProgressDialog != null)
						if (mLoadProgressDialog.isShowing())
							mLoadProgressDialog.dismiss();
					Toast.makeText(XinlvActivity.this,
							getResources().getString(R.string.upload_failed_nonet),
							Toast.LENGTH_SHORT).show();
					break;

				default:
					break;
			}
		};
	};

	private void waitShowDialog() {
		// TODO Auto-generated method stub
		if (mLoadProgressDialog == null) {
			mLoadProgressDialog = new Dialog(XinlvActivity.this,
					R.style.sports_dialog);
			LayoutInflater mInflater = getLayoutInflater();
			View v1 = mInflater.inflate(R.layout.bestgirl_progressdialog, null);
			TextView mDialogMessage = (TextView) v1.findViewById(R.id.message);
			mDialogMessage.setText(R.string.uploading);
			v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
			mLoadProgressDialog.setContentView(v1);
		}
		if (mLoadProgressDialog != null)
			if (!mLoadProgressDialog.isShowing() && !this.isFinishing())
				mLoadProgressDialog.show();
	}

	// 后台统计心率点击的次数
	private void healthCount() {
		new AsyncTask<Void, Void, ApiBack>() {
			@Override
			protected ApiBack doInBackground(Void... params) {
				// TODO Auto-generated method stub
				ApiBack back = null;
				try {
					back = ApiJsonParser.healthdatacount(msApp.getSessionId(),
							2, startTime1,0);
				} catch (ApiNetException e) {
					e.printStackTrace();
				} catch (ApiSessionOutException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return back;
			}

			@Override
			protected void onPostExecute(ApiBack result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (result != null) {

				} else {
				}

			}
		}.execute();
	}

	public synchronized static boolean isFastClick2(int betweenTime) {
		long time = System.currentTimeMillis();
		if ( time - lastClickTime < betweenTime) {
			return true;
		}
		lastClickTime = time;
		return false;
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
}