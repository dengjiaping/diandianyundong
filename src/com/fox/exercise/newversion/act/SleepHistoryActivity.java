package com.fox.exercise.newversion.act;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.fox.exercise.AbstractBaseActivity;
import com.fox.exercise.R;
import com.fox.exercise.db.SportContentDB;
import com.fox.exercise.newversion.entity.SleepEffect;
import com.fox.exercise.newversion.view.RoundProgressBar;
import com.umeng.analytics.MobclickAgent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ingenic.indroidsync.SportsApp;

/**
 * @author loujungang 睡眠历史记录
 */
public class SleepHistoryActivity extends AbstractBaseActivity implements
        OnClickListener {
    private RoundProgressBar roundProgressBar;
    private SportContentDB contentDB;
    private SportsApp msApp;
    private Context mContext;
    private Map<String, List<SleepEffect>> effectMap;
    private RadioGroup sleep_day_radiogroup;
    private RadioButton sleep_day_btn, sleep_night_btn;
    private LinearLayout mSleep_time_layout;// 柱状图layout
    private LinearLayout mSleep_time1, mSleep_time2, mSleep_time3,
            mSleep_time4, mSleep_time5, mSleep_time6, mSleep_time7;// 柱状图layout
    private ImageView cal_1, km_1, cal_2, km_2, cal_3, km_3, cal_4, km_4,
            cal_5, km_5, cal_6, km_6, cal_7, km_7;
    private TextView day1, day2, day3, day4, day5, day6, day7;
//	private final int FRESH_LIST = 110;// 更新成功

    private SleepEffect radioEffect1, radioEffect2, fromSleepEffect;
    private int cruZhu = 0;

    @Override
    public void initIntentParam(Intent intent) {
        // TODO Auto-generated method stub
        title = getResources().getString(R.string.sleep_record);
        if (intent != null) {
            fromSleepEffect = (SleepEffect) intent
                    .getSerializableExtra("sleepEffect");
        }
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        showContentView(R.layout.activity_sleep_history);
        msApp = (SportsApp) getApplication();
        mContext = this;
        roundProgressBar = (RoundProgressBar) findViewById(R.id.roundProgressBar1);
        // roundProgressBar.setProgress(85, "2015/11/25", "睡眠状态佳");
        contentDB = SportContentDB.getInstance(getApplicationContext());
        effectMap = new HashMap<String, List<SleepEffect>>();
        init();
        if (msApp != null && msApp.LoginNet && msApp.getSportUser() != null) {
            getSleepDayDate(msApp.getSportUser().getUid() + "");
            setDate();
            if (fromSleepEffect != null) {
                double ss = Double.valueOf(fromSleepEffect.getHart_rate());
                int num = (int) (ss * 100);
                if (num >= 90) {
                    roundProgressBar.setProgress(num,
                            getStringDate(cruZhu, "yyyy/MM/dd"), "好极了");
                } else if (num >= 80 && num < 90) {
                    roundProgressBar.setProgress(num,
                            getStringDate(cruZhu, "yyyy/MM/dd"), "不错");
                } else if (num >= 60 && num < 80) {
                    roundProgressBar.setProgress(num,
                            getStringDate(cruZhu, "yyyy/MM/dd"), "一般");
                } else {
                    if (num < 0) {
                        num = 0;
                    }
                    roundProgressBar.setProgress(num,
                            getStringDate(cruZhu, "yyyy/MM/dd"), "太差了");
                }
                List<SleepEffect> list = new ArrayList<SleepEffect>();
                list.add(fromSleepEffect);
                checkDayOrNight(list);

            } else {
                SleepEffect sfeEffect = getAllLastSleep();
                if (sfeEffect != null) {
                    double ss = Double.valueOf(sfeEffect.getHart_rate());
                    int num = (int) (ss * 100);
                    if (num >= 90) {
                        roundProgressBar.setProgress(num, "最近一次", "好极了");
                    } else if (num >= 80 && num < 90) {
                        roundProgressBar.setProgress(num, "最近一次", "不错");
                    } else if (num >= 60 && num < 80) {
                        roundProgressBar.setProgress(num, "最近一次", "一般");
                    } else {
                        if (num < 0) {
                            num = 0;
                        }
                        roundProgressBar.setProgress(num, "最近一次", "太差了");
                    }
                    List<SleepEffect> list = new ArrayList<SleepEffect>();
                    list.add(sfeEffect);
                    checkDayOrNight(list);
                    for (int i = 0; i < 7; i++) {
                        if (getStringDate(i, "yyyy/MM/dd").equals(
                                timeToMillons(sfeEffect.getStarttime()))) {
                            cruZhu = i;
                        }
                    }
                } else {
                    setprogressDate(0);
                    checkDayOrNight(effectMap.get(0 + "day"));
                }

            }

            if (effectMap != null) {
                if (effectMap.containsKey(6 + "day")) {
                    if (effectMap.get(6 + "day").size() == 0) {
                        mSleep_time1.setClickable(false);
                    } else {
                        mSleep_time1.setClickable(true);
                    }

                }
                if (effectMap.containsKey(5 + "day")) {
                    if (effectMap.get(5 + "day").size() == 0) {
                        mSleep_time2.setClickable(false);
                    } else {
                        mSleep_time2.setClickable(true);
                    }

                }
                if (effectMap.containsKey(4 + "day")) {
                    if (effectMap.get(4 + "day").size() == 0) {
                        mSleep_time3.setClickable(false);
                    } else {
                        mSleep_time3.setClickable(true);
                    }

                }
                if (effectMap.containsKey(3 + "day")) {
                    if (effectMap.get(3 + "day").size() == 0) {
                        mSleep_time4.setClickable(false);
                    } else {
                        mSleep_time4.setClickable(true);
                    }

                }
                if (effectMap.containsKey(2 + "day")) {
                    if (effectMap.get(2 + "day").size() == 0) {
                        mSleep_time5.setClickable(false);
                    } else {
                        mSleep_time5.setClickable(true);
                    }

                }
                if (effectMap.containsKey(1 + "day")) {
                    if (effectMap.get(1 + "day").size() == 0) {
                        mSleep_time6.setClickable(false);
                    } else {
                        mSleep_time6.setClickable(true);
                    }

                }
                if (effectMap.containsKey(0 + "day")) {
                    if (effectMap.get(0 + "day").size() == 0) {
                        mSleep_time7.setClickable(false);
                    } else {
                        mSleep_time7.setClickable(true);
                    }

                }

            }
        } else {
            roundProgressBar.setProgress(0, getStringDate(0, "yyyy/MM/dd"), "");
            mSleep_time1.setClickable(false);
            mSleep_time2.setClickable(false);
            mSleep_time3.setClickable(false);
            mSleep_time4.setClickable(false);
            mSleep_time5.setClickable(false);
            mSleep_time6.setClickable(false);
            mSleep_time7.setClickable(false);
        }

    }

    @Override
    public void setViewStatus() {
        // TODO Auto-generated method stub

    }

//	private Handler mHandler = new Handler() {
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//			case FRESH_LIST:
//				break;
//
//			default:
//				break;
//			}
//		};
//	};

    private void init() {
        sleep_day_radiogroup = (RadioGroup) findViewById(R.id.sleep_day_radiogroup);
        sleep_day_btn = (RadioButton) findViewById(R.id.sleep_day_btn);
        sleep_night_btn = (RadioButton) findViewById(R.id.sleep_night_btn);
        mSleep_time_layout = (LinearLayout) findViewById(R.id.mSleep_time_layout);
        mSleep_time1 = (LinearLayout) findViewById(R.id.mSleep_time1);
        mSleep_time2 = (LinearLayout) findViewById(R.id.mSleep_time2);
        mSleep_time3 = (LinearLayout) findViewById(R.id.mSleep_time3);
        mSleep_time4 = (LinearLayout) findViewById(R.id.mSleep_time4);
        mSleep_time5 = (LinearLayout) findViewById(R.id.mSleep_time5);
        mSleep_time6 = (LinearLayout) findViewById(R.id.mSleep_time6);
        mSleep_time7 = (LinearLayout) findViewById(R.id.mSleep_time7);
        mSleep_time1.setOnClickListener(this);
        mSleep_time2.setOnClickListener(this);
        mSleep_time3.setOnClickListener(this);
        mSleep_time4.setOnClickListener(this);
        mSleep_time5.setOnClickListener(this);
        mSleep_time6.setOnClickListener(this);
        mSleep_time7.setOnClickListener(this);
        cal_1 = (ImageView) findViewById(R.id.cal_1);
        km_1 = (ImageView) findViewById(R.id.km_1);
        cal_2 = (ImageView) findViewById(R.id.cal_2);
        km_2 = (ImageView) findViewById(R.id.km_2);
        cal_3 = (ImageView) findViewById(R.id.cal_3);
        km_3 = (ImageView) findViewById(R.id.km_3);
        cal_4 = (ImageView) findViewById(R.id.cal_4);
        km_4 = (ImageView) findViewById(R.id.km_4);
        cal_5 = (ImageView) findViewById(R.id.cal_5);
        km_5 = (ImageView) findViewById(R.id.km_5);
        cal_6 = (ImageView) findViewById(R.id.cal_6);
        km_6 = (ImageView) findViewById(R.id.km_6);
        cal_7 = (ImageView) findViewById(R.id.cal_7);
        km_7 = (ImageView) findViewById(R.id.km_7);
        day1 = (TextView) findViewById(R.id.day1);
        day2 = (TextView) findViewById(R.id.day2);
        day3 = (TextView) findViewById(R.id.day3);
        day4 = (TextView) findViewById(R.id.day4);
        day5 = (TextView) findViewById(R.id.day5);
        day6 = (TextView) findViewById(R.id.day6);
        day7 = (TextView) findViewById(R.id.day7);
        day1.setText(getStringDate(6, "MM/dd"));
        day2.setText(getStringDate(5, "MM/dd"));
        day3.setText(getStringDate(4, "MM/dd"));
        day4.setText(getStringDate(3, "MM/dd"));
        day5.setText(getStringDate(2, "MM/dd"));
        day6.setText(getStringDate(1, "MM/dd"));
        day7.setText(getStringDate(0, "MM/dd"));
        sleep_day_radiogroup
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(RadioGroup arg0, int Checkid) {
                        // TODO Auto-generated method stub
                        switch (Checkid) {
                            case R.id.sleep_day_btn:
                                if (radioEffect1 == null) {
                                    roundProgressBar.setProgress(0,
                                            getStringDate(cruZhu, "yyyy/MM/dd"),
                                            " ");
                                } else {
                                    double ss = Double.valueOf(radioEffect1
                                            .getHart_rate());
                                    int num = (int) (ss * 100);
                                    if (num >= 90) {
                                        roundProgressBar
                                                .setProgress(
                                                        num,
                                                        getStringDate(cruZhu,
                                                                "yyyy/MM/dd"),
                                                        "好极了");
                                    } else if (num >= 80 && num < 90) {
                                        roundProgressBar
                                                .setProgress(
                                                        num,
                                                        getStringDate(cruZhu,
                                                                "yyyy/MM/dd"), "不错");
                                    } else if (num >= 60 && num < 80) {
                                        roundProgressBar
                                                .setProgress(
                                                        num,
                                                        getStringDate(cruZhu,
                                                                "yyyy/MM/dd"), "一般");
                                    } else {
                                        if (num <= 0) {
                                            num = 0;
                                        }
                                        roundProgressBar
                                                .setProgress(
                                                        num,
                                                        getStringDate(cruZhu,
                                                                "yyyy/MM/dd"),
                                                        "太差了");
                                    }

                                }
                                break;
                            case R.id.sleep_night_btn:
                                if (radioEffect2 == null) {
                                    roundProgressBar.setProgress(0,
                                            getStringDate(cruZhu, "yyyy/MM/dd"),
                                            " ");
                                } else {
                                    double ss = Double.valueOf(radioEffect2
                                            .getHart_rate());
                                    int num = (int) (ss * 100);
                                    if (num >= 90) {
                                        roundProgressBar
                                                .setProgress(
                                                        num,
                                                        getStringDate(cruZhu,
                                                                "yyyy/MM/dd"),
                                                        "好极了");
                                    } else if (num >= 80 && num < 90) {
                                        roundProgressBar
                                                .setProgress(
                                                        num,
                                                        getStringDate(cruZhu,
                                                                "yyyy/MM/dd"), "不错");
                                    } else if (num >= 60 && num < 80) {
                                        roundProgressBar
                                                .setProgress(
                                                        num,
                                                        getStringDate(cruZhu,
                                                                "yyyy/MM/dd"), "一般");
                                    } else {
                                        if (num <= 0) {
                                            num = 0;
                                        }
                                        roundProgressBar
                                                .setProgress(
                                                        num,
                                                        getStringDate(cruZhu,
                                                                "yyyy/MM/dd"),
                                                        "太差了");
                                    }

                                }
                                break;

                            default:
                                break;
                        }

                    }
                });
    }

    List<Double> sleepTimeList = new ArrayList<Double>();// 睡眠时常
    List<Double> sleepYouTimeList = new ArrayList<Double>();// 有效睡眠时常

    // 获取最近七天的睡眠数据
    private void getSleepDayDate(String mUid) {

        for (int day = 0; day < 7; day++) {
            List<SleepEffect> list = null;
            list = contentDB.queryUidTimeSleep(mUid,
                    getStringDate(day, "yyyy-MM-dd"));
            effectMap.put(day + "day", list);
            int count = 0;
            count = list.size();
            double allTime = 0;
            double youXiaoTime = 0;
            while (count > 0) {
                allTime += subtractTime(list.get(count - 1).getStarttime(),
                        list.get(count - 1).getEndtime());
                if (list.get(count - 1).getHart_rate() != null
                        && !"".equals(list.get(count - 1).getHart_rate())) {
                    if (Double.valueOf(list.get(count - 1).getHart_rate()) > 0) {
                        youXiaoTime += subtractTime(list.get(count - 1)
                                .getStarttime(), list.get(count - 1)
                                .getEndtime())
                                * Double.valueOf(list.get(count - 1)
                                .getHart_rate());
                    }

                }
                count--;
            }
            sleepTimeList.add(allTime);
            sleepYouTimeList.add(youXiaoTime);
        }

    }

    private void setDate() {
        int mtime_height = mSleep_time_layout.getLayoutParams().height;
        int mXiaolv_height = mtime_height * 5 / 6;
        double m12 = 12 * 60 * 60;
        LayoutParams para1 = cal_1.getLayoutParams();
        LayoutParams kpara1 = km_1.getLayoutParams();
        if (sleepTimeList.get(6) >= m12) {
            para1.height = mtime_height;
            cal_1.setLayoutParams(para1);
        } else {
            para1.height = (int) (mtime_height * 1.0 * (sleepTimeList.get(6) / m12));
            if (sleepTimeList.get(6) <= 0) {
                para1.height = 2;
            }
            cal_1.setLayoutParams(para1);
        }
        if (sleepYouTimeList.get(6) <= 0) {
            kpara1.height = 2;
            km_1.setLayoutParams(kpara1);
        } else {
            if (sleepTimeList.get(6) > 0) {
                kpara1.height = (int) (mXiaolv_height * 1.0 * (sleepYouTimeList
                        .get(6) / sleepTimeList.get(6)));
                km_1.setLayoutParams(kpara1);
            } else {
                kpara1.height = 2;
                km_1.setLayoutParams(kpara1);
            }
        }

        LayoutParams para2 = cal_2.getLayoutParams();
        LayoutParams kpara2 = km_2.getLayoutParams();
        if (sleepTimeList.get(5) >= m12) {
            para2.height = mtime_height;
            cal_2.setLayoutParams(para1);
        } else {
            para2.height = (int) (mtime_height * 1.0 * (sleepTimeList.get(5) / m12));
            if (sleepTimeList.get(5) <= 0) {
                para2.height = 2;
            }
            cal_2.setLayoutParams(para2);
        }
        if (sleepYouTimeList.get(5) <= 0) {
            kpara2.height = 2;
            km_2.setLayoutParams(kpara2);
        } else {
            if (sleepTimeList.get(5) > 0) {
                kpara2.height = (int) (mXiaolv_height * 1.0 * (sleepYouTimeList
                        .get(5) / sleepTimeList.get(5)));
                km_2.setLayoutParams(kpara2);
            } else {
                kpara2.height = 2;
                km_2.setLayoutParams(kpara2);
            }
        }

        LayoutParams para3 = cal_3.getLayoutParams();
        LayoutParams kpara3 = km_3.getLayoutParams();
        if (sleepTimeList.get(4) >= m12) {
            para3.height = mtime_height;
            cal_3.setLayoutParams(para3);
        } else {
            para3.height = (int) (mtime_height * 1.0 * (sleepTimeList.get(4) / m12));
            if (sleepTimeList.get(6) <= 0) {
                para3.height = 2;
            }
            cal_3.setLayoutParams(para3);
        }
        if (sleepYouTimeList.get(4) <= 0) {
            kpara3.height = 2;
            km_3.setLayoutParams(kpara3);
        } else {
            if (sleepTimeList.get(4) > 0) {
                kpara3.height = (int) (mXiaolv_height * 1.0 * (sleepYouTimeList
                        .get(4) / sleepTimeList.get(4)));
                km_3.setLayoutParams(kpara1);
            } else {
                kpara3.height = 2;
                km_3.setLayoutParams(kpara3);
            }
        }

        LayoutParams para4 = cal_4.getLayoutParams();
        LayoutParams kpara4 = km_4.getLayoutParams();
        if (sleepTimeList.get(3) >= m12) {
            para4.height = mtime_height;
            cal_4.setLayoutParams(para4);
        } else {
            para4.height = (int) (mtime_height * 1.0 * (sleepTimeList.get(3) / m12));
            if (sleepTimeList.get(3) <= 0) {
                para4.height = 2;
            }
            cal_4.setLayoutParams(para4);
        }
        if (sleepYouTimeList.get(3) <= 0) {
            kpara4.height = 2;
            km_4.setLayoutParams(kpara4);
        } else {
            if (sleepTimeList.get(3) > 0) {
                kpara4.height = (int) (mXiaolv_height * 1.0 * (sleepYouTimeList
                        .get(3) / sleepTimeList.get(3)));
                km_4.setLayoutParams(kpara4);
            } else {
                kpara4.height = 2;
                km_4.setLayoutParams(kpara4);
            }
        }

        LayoutParams para5 = cal_5.getLayoutParams();
        LayoutParams kpara5 = km_5.getLayoutParams();
        if (sleepTimeList.get(2) >= m12) {
            para5.height = mtime_height;
            cal_5.setLayoutParams(para5);
        } else {
            para5.height = (int) (mtime_height * 1.0 * (sleepTimeList.get(2) / m12));
            if (sleepTimeList.get(2) <= 0) {
                para5.height = 2;
            }
            cal_5.setLayoutParams(para5);
        }
        if (sleepYouTimeList.get(2) <= 0) {
            kpara5.height = 2;
            km_5.setLayoutParams(kpara5);
        } else {
            if (sleepTimeList.get(2) > 0) {
                kpara5.height = (int) (mXiaolv_height * 1.0 * (sleepYouTimeList
                        .get(2) / sleepTimeList.get(2)));
                km_5.setLayoutParams(kpara5);
            } else {
                kpara5.height = 2;
                km_5.setLayoutParams(kpara5);
            }
        }

        LayoutParams para6 = cal_6.getLayoutParams();
        LayoutParams kpara6 = km_6.getLayoutParams();
        if (sleepTimeList.get(1) >= m12) {
            para6.height = mtime_height;
            cal_6.setLayoutParams(para6);
        } else {
            para6.height = (int) (mtime_height * 1.0 * (sleepTimeList.get(1) / m12));
            if (sleepTimeList.get(1) <= 0) {
                para6.height = 2;
            }
            cal_6.setLayoutParams(para6);
        }
        if (sleepYouTimeList.get(1) <= 0) {
            kpara6.height = 2;
            km_6.setLayoutParams(kpara6);
        } else {
            if (sleepTimeList.get(1) > 0) {
                kpara6.height = (int) (mXiaolv_height * 1.0 * (sleepYouTimeList
                        .get(1) / sleepTimeList.get(1)));
                km_6.setLayoutParams(kpara6);
            } else {
                kpara6.height = 2;
                km_6.setLayoutParams(kpara6);
            }
        }

        LayoutParams para7 = cal_7.getLayoutParams();
        LayoutParams kpara7 = km_7.getLayoutParams();
        if (sleepTimeList.get(0) >= m12) {
            para7.height = mtime_height;
            cal_7.setLayoutParams(para7);
        } else {
            para7.height = (int) (mtime_height * 1.0 * (sleepTimeList.get(0) / m12));
            if (sleepTimeList.get(0) <= 0) {
                para7.height = 2;
            }
            cal_7.setLayoutParams(para7);
        }
        if (sleepYouTimeList.get(0) <= 0) {
            kpara7.height = 2;
            km_7.setLayoutParams(kpara7);
        } else {
            if (sleepTimeList.get(0) > 0) {
                kpara7.height = (int) (mXiaolv_height * 1.0 * (sleepYouTimeList
                        .get(0) / sleepTimeList.get(0)));
                km_7.setLayoutParams(kpara7);
            } else {
                kpara7.height = 2;
                km_7.setLayoutParams(kpara7);
            }
        }
    }

    private String getStringDate(int day, String format) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DATE, -day);
        SimpleDateFormat sdfd = new SimpleDateFormat(format);
        String str_date = sdfd.format(now.getTime());
        return str_date;
    }

    @Override
    public void onPageResume() {
        // TODO Auto-generated method stub
        MobclickAgent.onPageStart("SleepHistoryActivity");
        msApp = (SportsApp) getApplication();
        mContext = this;
        if (contentDB == null) {
            contentDB = SportContentDB.getInstance(mContext);
        }
    }

    @Override
    public void onPagePause() {
        // TODO Auto-generated method stub
        MobclickAgent.onPageEnd("SleepHistoryActivity");
    }

    @Override
    public void onPageDestroy() {
        // TODO Auto-generated method stub
        effectMap = null;
        radioEffect1 = null;
        radioEffect2 = null;
        fromSleepEffect = null;
        sleepTimeList = null;
        sleepYouTimeList = null;
        mContext = null;
        msApp = null;
    }

    // 两个时间相减得到的时间秒
    private double subtractTime(String startTime, String endTime) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        double subtractTime = 0;
        try {
            Date d1 = df.parse(endTime);
            Date d2 = df.parse(startTime);
            long diff = d1.getTime() - d2.getTime();// 这样得到的差值是微秒级别
            subtractTime = diff / 1000;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return subtractTime;
    }

    // 设置那个圈的值和radiobutton的值
    private void setprogressDate(int day) {
        List<SleepEffect> list = effectMap.get(day + "day");
        if (list != null && list.size() > 0) {
            double long1 = sleepTimeList.get(day);
            double long2 = sleepYouTimeList.get(day);
            if (long2 <= 0 || long1 <= 0) {
                roundProgressBar.setProgress(0,
                        getStringDate(day, "yyyy/MM/dd"), " ");
            } else {
                int num = (int) ((long2 / long1) * 100);
                if (num >= 90) {
                    roundProgressBar.setProgress(num,
                            getStringDate(day, "yyyy/MM/dd"), "好极了");
                } else if (num >= 80 && num < 90) {
                    roundProgressBar.setProgress(num,
                            getStringDate(day, "yyyy/MM/dd"), "不错");
                } else if (num >= 60 && num < 80) {
                    roundProgressBar.setProgress(num,
                            getStringDate(day, "yyyy/MM/dd"), "一般");
                } else {
                    if (num <= 0) {
                        num = 0;
                    }
                    roundProgressBar.setProgress(num,
                            getStringDate(day, "yyyy/MM/dd"), "太差了");
                }

            }
        } else {
            roundProgressBar.setProgress(0, getStringDate(day, "yyyy/MM/dd"),
                    "睡眠状态无");
            sleep_day_btn.setText("暂无数据");
            sleep_night_btn.setText("暂无数据");
        }
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        sleep_day_btn.setChecked(false);
        sleep_night_btn.setChecked(false);
        radioEffect1 = null;
        radioEffect2 = null;
        switch (view.getId()) {
            case R.id.mSleep_time1:
                setprogressDate(6);
                cruZhu = 6;
                checkDayOrNight(effectMap.get(6 + "day"));
                break;
            case R.id.mSleep_time2:
                setprogressDate(5);
                cruZhu = 5;
                checkDayOrNight(effectMap.get(5 + "day"));
                break;
            case R.id.mSleep_time3:
                setprogressDate(4);
                cruZhu = 4;
                checkDayOrNight(effectMap.get(4 + "day"));
                break;
            case R.id.mSleep_time4:
                setprogressDate(3);
                cruZhu = 3;
                checkDayOrNight(effectMap.get(3 + "day"));
                break;
            case R.id.mSleep_time5:
                setprogressDate(2);
                cruZhu = 2;
                checkDayOrNight(effectMap.get(2 + "day"));
                break;
            case R.id.mSleep_time6:
                setprogressDate(1);
                cruZhu = 1;
                checkDayOrNight(effectMap.get(1 + "day"));
                break;
            case R.id.mSleep_time7:
                setprogressDate(0);
                cruZhu = 0;
                checkDayOrNight(effectMap.get(0 + "day"));
                break;

            default:
                break;
        }

    }

    private void checkDayOrNight(List<SleepEffect> list) {
        if (list == null) {
            sleep_day_btn.setText("暂无数据");
            sleep_night_btn.setText("暂无数据");
        } else {
            if (list.size() == 1) {
                radioEffect1 = list.get(0);
                if (getHours(list.get(0).getStarttime()) >= 7
                        && getHours(list.get(0).getStarttime()) <= 21) {
                    // 白天
                    sleep_day_btn
                            .setBackgroundResource(R.drawable.sleep_record_day_btn_click);
                    sleep_day_btn.setText(getOnTime(list.get(0).getStarttime(),
                            list.get(0).getEndtime()));
                    sleep_night_btn.setText("暂无数据");
                } else {
                    // 夜晚
                    sleep_day_btn
                            .setBackgroundResource(R.drawable.sleep_record_night_btn_click);
                    sleep_day_btn.setText(getOnTime(list.get(0).getStarttime(),
                            list.get(0).getEndtime()));
                    sleep_night_btn.setText("暂无数据");
                }
            } else if (list.size() == 2) {
                radioEffect1 = list.get(0);
                radioEffect2 = list.get(1);
                if (getHours(list.get(0).getStarttime()) >= 7
                        && getHours(list.get(0).getStarttime()) <= 21) {
                    // 白天
                    sleep_day_btn
                            .setBackgroundResource(R.drawable.sleep_record_day_btn_click);
                    sleep_day_btn.setText(getOnTime(list.get(0).getStarttime(),
                            list.get(0).getEndtime()));
                } else {
                    // 夜晚
                    sleep_day_btn
                            .setBackgroundResource(R.drawable.sleep_record_night_btn_click);
                    sleep_day_btn.setText(getOnTime(list.get(0).getStarttime(),
                            list.get(0).getEndtime()));
                }
                if (getHours(list.get(1).getStarttime()) >= 7
                        && getHours(list.get(1).getStarttime()) <= 21) {
                    // 白天
                    sleep_night_btn
                            .setBackgroundResource(R.drawable.sleep_record_day_btn_click);
                    sleep_night_btn.setText(getOnTime(list.get(1)
                            .getStarttime(), list.get(1).getEndtime()));
                } else {
                    // 夜晚
                    sleep_night_btn
                            .setBackgroundResource(R.drawable.sleep_record_night_btn_click);
                    sleep_night_btn.setText(getOnTime(list.get(1)
                            .getStarttime(), list.get(1).getEndtime()));
                }

            } else {
                radioEffect1 = getLongSleep(list);
                radioEffect2 = getLastSleep(list);
                if (radioEffect1 != null && radioEffect2 != null) {
                    if (radioEffect1.getStarttime().equals(
                            radioEffect2.getStarttime())) {
                        List<SleepEffect> alist = new ArrayList<SleepEffect>();
                        alist.addAll(list);
                        for (int i = 0; i < alist.size(); i++) {
                            if (radioEffect1.getStarttime().equals(
                                    alist.get(i).getStarttime())) {
                                alist.remove(i);
                            }

                        }
                        radioEffect1 = getLongSleep(alist);
                        alist = null;
                    }
                }
                if (radioEffect1 == null) {
                    sleep_day_btn.setText("暂无数据");
                } else {
                    if (getHours(radioEffect1.getStarttime()) >= 7
                            && getHours(radioEffect1.getStarttime()) <= 21) {
                        // 白天
                        sleep_day_btn
                                .setBackgroundResource(R.drawable.sleep_record_day_btn_click);
                        sleep_day_btn.setText(getOnTime(
                                radioEffect1.getStarttime(),
                                radioEffect1.getEndtime()));
                    } else {
                        // 夜晚
                        sleep_day_btn
                                .setBackgroundResource(R.drawable.sleep_record_night_btn_click);
                        sleep_day_btn.setText(getOnTime(
                                radioEffect1.getStarttime(),
                                radioEffect1.getEndtime()));
                    }

                }
                if (radioEffect2 == null) {
                    sleep_night_btn.setText("暂无数据");
                } else {
                    if (getHours(radioEffect2.getStarttime()) >= 7
                            && getHours(radioEffect2.getStarttime()) <= 21) {
                        // 白天
                        sleep_night_btn
                                .setBackgroundResource(R.drawable.sleep_record_day_btn_click);
                        sleep_night_btn.setText(getOnTime(
                                radioEffect2.getStarttime(),
                                radioEffect2.getEndtime()));
                    } else {
                        // 夜晚
                        sleep_night_btn
                                .setBackgroundResource(R.drawable.sleep_record_night_btn_click);
                        sleep_night_btn.setText(getOnTime(
                                radioEffect2.getStarttime(),
                                radioEffect2.getEndtime()));
                    }
                }

            }
        }

    }

    // private Dialog mLoadProgressDialog = null;
    //
    // private void waitShowDialog() {
    // // TODO Auto-generated method stub
    // if (mLoadProgressDialog == null) {
    // mLoadProgressDialog = new Dialog(mContext, R.style.sports_dialog);
    // LayoutInflater mInflater = getLayoutInflater();
    // View v1 = mInflater.inflate(R.layout.bestgirl_progressdialog, null);
    // TextView mDialogMessage = (TextView) v1.findViewById(R.id.message);
    // mDialogMessage.setText(R.string.bestgirl_wait);
    // v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
    // mLoadProgressDialog.setContentView(v1);
    // }
    // if (mLoadProgressDialog != null)
    // if (!mLoadProgressDialog.isShowing() && !this.isFinishing())
    // mLoadProgressDialog.show();
    // Log.i(TAG, "isFirstshow----");
    // }

    // 得到小时
    private int getHours(String startTime) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int subtractTime = 0;
        try {
            Date d2 = df.parse(startTime);
            subtractTime = d2.getHours();
        } catch (Exception e) {
            // TODO: handle exception
        }
        return subtractTime;
    }

    // 两个时间段
    private String getOnTime(String startTime, String endTime) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat df1 = new SimpleDateFormat("HH:mm");
        String subtractTime = "";
        try {
            Date d1 = df.parse(endTime);
            Date d2 = df.parse(startTime);
            df1.format(d2);
            subtractTime = df1.format(d2) + "~" + df1.format(d1);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return subtractTime;
    }

    // 得到最近睡眠时段
    private SleepEffect getLastSleep(List<SleepEffect> list) {
        SleepEffect sleepEffect = null;
        try {
            long min = 0;
            long cru = System.currentTimeMillis();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long mm = df.parse(list.get(0).getStarttime()).getTime();
            min = cru - mm;
            sleepEffect = list.get(0);
            for (int i = 1; i < list.size(); i++) {
                mm = df.parse(list.get(i).getStarttime()).getTime();
                if ((cru - mm) < min) {
                    min = cru - mm;
                    sleepEffect = list.get(i);
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        return sleepEffect;

    }

    // 得到所有数据最近睡眠时段
    private SleepEffect getAllLastSleep() {
        List<SleepEffect> list = new ArrayList<SleepEffect>();
        if (effectMap != null) {
            for (int i = 0; i < 7; i++) {
                if (effectMap.containsKey(i + "day")) {
                    list.addAll(effectMap.get(i + "day"));
                }
            }
        }
        SleepEffect sleepEffect = null;
        try {
            long min = 0;
            long cru = System.currentTimeMillis();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long mm = df.parse(list.get(0).getStarttime()).getTime();
            min = cru - mm;
            sleepEffect = list.get(0);
            for (int i = 1; i < list.size(); i++) {
                mm = df.parse(list.get(i).getStarttime()).getTime();
                if ((cru - mm) < min) {
                    min = cru - mm;
                    sleepEffect = list.get(i);
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        return sleepEffect;

    }

    // 得到最长睡眠时段
    private SleepEffect getLongSleep(List<SleepEffect> list) {
        SleepEffect sleepEffect = null;
        try {
            double max = 0;
            max = subtractTime(list.get(0).getStarttime(), list.get(0)
                    .getEndtime());
            sleepEffect = list.get(0);
            for (int i = 1; i < list.size(); i++) {
                if ((subtractTime(list.get(i).getStarttime(), list.get(i)
                        .getEndtime())) > max) {
                    max = subtractTime(list.get(i).getStarttime(), list.get(i)
                            .getEndtime());
                    sleepEffect = list.get(i);
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        return sleepEffect;

    }

    private String timeToMillons(String time) {
        String ss = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long timeStart = sdf.parse(time).getTime();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");
            Date date = new Date(timeStart);
            ss = sdf1.format(date);
        } catch (Exception e) {
            // TODO: handle exception
        }

        return ss;

    }
}
