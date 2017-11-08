package com.fox.exercise.newversion.act;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fox.exercise.AbstractBaseFragment;
import com.fox.exercise.ActivityInfoWebView;
import com.fox.exercise.ImageDownloader;
import com.fox.exercise.R;
import com.fox.exercise.YunHuWebViewActivity;
import com.fox.exercise.api.ApiBack;
import com.fox.exercise.api.ApiConstant;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.api.entity.ActionInfos;
import com.fox.exercise.api.entity.ActivityInfo;
import com.fox.exercise.api.entity.SquareInfo;
import com.fox.exercise.newversion.entity.ExternalActivi;
import com.fox.exercise.newversion.view.MyGridView;
import com.fox.exercise.newversion.view.MyViewPager;
import com.fox.exercise.newversion.view.PullDownElasticImp;
import com.fox.exercise.newversion.view.PullDownScrollView;
import com.fox.exercise.newversion.view.PullDownScrollView.RefreshListener;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cn.ingenic.indroidsync.SportsApp;

/**
 * 广场推荐
 *
 * @author zhaozhenbang
 */
public class RecomMend extends AbstractBaseFragment implements
        OnPageChangeListener, OnClickListener, RefreshListener {

    private View view;
    private SportsApp mSportsApp;
    private TextView mMore, mMore_two, mMore_three, zzs, ddfl, ssj, jcdt; // 点击更多
    private ImageDownloader mDownloader2 = null;
    private final int FRESH_LIST = 111;
    private final int FRESH_LIST_NULL = 112;
    List<SquareInfo> mSquareInfo = new ArrayList<SquareInfo>();
    private List<ExternalActivi> mList = new ArrayList<ExternalActivi>(); // banner图片

    public static String IMAGE_CACHE_PATH = "imageloader/Cache"; // 图片缓存路径
    private MyViewPager adViewPager;
    private List<ImageView> imageViews;// 滑动的图片集合

    private List<View> dots; // 图片标题正文的那些点
    private List<View> dotList;

    private int currentItem = 0; // 当前图片的索引号
    // 定义的五个指示点
    private View dot0;
    private View dot1;
    private View dot2;
    private View dot3;
    private View dot4;

    private ScheduledExecutorService scheduledExecutorService;

    // 异步加载图片
    private ImageLoader mImageLoader;
    private DisplayImageOptions options;

    private MyGridView tuijian_gridview1, tuijian_gridview2, tuijian_gridview3,
            tuijian_gridview4;

    private Dialog mLoadProgressDialog = null;

    private PullDownScrollView mPullDownScrollView;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    adViewPager.setCurrentItem(currentItem);
                    break;
                case FRESH_LIST:
                    setDate();
                    if(isAdded()){
                        mMore.setText(getResources().getString(R.string.guangchang_more));
                        mMore_two.setText(getResources().getString(R.string.guangchang_more));
                        mMore_three.setText(getResources().getString(R.string.guangchang_more));
                    }
                    break;
                case FRESH_LIST_NULL:
                    // Toast.makeText(getActivity(), "暂无数据", 10).show();
                    break;
                default:
                    break;
            }

        }

        ;
    };

    // private Intent uri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mSportsApp = (SportsApp) getActivity().getApplication();
        mSportsApp.addActivity(getActivity());
        this.mDownloader2 = new ImageDownloader(getActivity());
        mDownloader2.setType(ImageDownloader.OnlyOne);
        view = View.inflate(getActivity(), R.layout.activity_recommend1, null);


        mPullDownScrollView = (PullDownScrollView) view
                .findViewById(R.id.refresh_root);
        mPullDownScrollView.setRefreshListener(this);
        mPullDownScrollView.setPullDownElastic(new PullDownElasticImp(
                getActivity()));
        // 更多
        mMore = (TextView) view.findViewById(R.id.tv_more_one);
        mMore.setOnClickListener(this);
        mMore_two = (TextView) view.findViewById(R.id.tv_more_two);
        mMore_two.setOnClickListener(this);
        mMore_three = (TextView) view.findViewById(R.id.tv_more_three);
        mMore_three.setOnClickListener(this);
        if (!mSportsApp.LoginOption) {
            if(isAdded()){
                mSportsApp.TyrLoginAction(getActivity(),
                        getString(R.string.sports_love_title),
                        getString(R.string.try_to_login));
            }

        } else {

            initImageLoader();
            // 获取图片加载实例
            mImageLoader = ImageLoader.getInstance();
            options = new DisplayImageOptions.Builder()
                    .showStubImage(R.drawable.burn_loading)
                    .showImageForEmptyUri(R.drawable.burn_loading)
                    .showImageOnFail(R.drawable.burn_loading)
                    .cacheInMemory(true).cacheOnDisc(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .imageScaleType(ImageScaleType.EXACTLY).build();
            initAdData();
            tuijian_gridview1 = (MyGridView) view
                    .findViewById(R.id.tuijian_gridview1);
            tuijian_gridview2 = (MyGridView) view
                    .findViewById(R.id.tuijian_gridview2);
            tuijian_gridview3 = (MyGridView) view
                    .findViewById(R.id.tuijian_gridview3);
            tuijian_gridview4 = (MyGridView) view
                    .findViewById(R.id.tuijian_gridview4);
            tuijian_gridview1.setFocusable(false);
            tuijian_gridview2.setFocusable(false);
            tuijian_gridview3.setFocusable(false);
            tuijian_gridview4.setFocusable(false);
            ddfl = (TextView) view.findViewById(R.id.diandianfuli);// 点点福利
            zzs = (TextView) view.findViewById(R.id.zhangzisi);// 涨姿势
            ssj = (TextView) view.findViewById(R.id.shengshunjian);// 神瞬间
            jcdt = (TextView) view.findViewById(R.id.dongtai);// 精彩动态


            getAllDate();
        }
        return view;

    }

    private void getListData() {
        new AsyncTask<Void, Void, List<ExternalActivi>>() {

            @Override
            protected List<ExternalActivi> doInBackground(Void... params) {
                // TODO Auto-generated method stub
                List<ExternalActivi> list = new ArrayList<ExternalActivi>();
                try {
                    if(isAdded()){
                        list = ApiJsonParser.getExternalActives(
                                mSportsApp.getSessionId(),
                                2,
                                "z"
                                        + getResources().getString(
                                        R.string.config_game_id));
                    }
                } catch (ApiNetException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return list;
            }

            protected void onPostExecute(List<ExternalActivi> result) {
                if (result != null) {
                    try {
                        if (result != null && result.size() != 0) {
                            mList.clear();
                            for (int i = 0; i < result.size(); i++) {
                                mList.add(result.get(i));
                            }
                            addDynamicView();
                            adViewPager.setAdapter(new MyAdapter());// 设置填充ViewPager页面的适配器
//                            startAd();
                            currentItem = 0;
                        }
                    } catch (Exception e) {
                    }

                } else {
                }
            }

            ;
        }.execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 更多
            case R.id.tv_more_two:
                Intent intent2 = new Intent(getActivity(), DoubiActivity.class);
                intent2.putExtra("typeid", mSquareInfo.get(1).getId());
                startActivity(intent2);
                break;
            // 涨姿势
            case R.id.tv_more_one:
                if(isAdded()){
                    Intent intent = new Intent(getActivity(),
                            ZhangzisiWebViewActivity.class);
                    String url = ApiConstant.DATA_URL + ApiConstant.getZhangzisGengduo
                            + "z" + getResources().getString(R.string.config_game_id)
                            + "&catid=" + mSquareInfo.get(2).getId();
                    intent.putExtra("gengduourl", url);
                    intent.putExtra("bs", 1);
                    intent.putExtra("tbs", 2);
                    startActivity(intent);
                }
                break;
            // 点点福利
            case R.id.tv_more_three:
                Intent intent3 = new Intent(getActivity(),
                        YunHuWebViewActivity.class);
                startActivity(intent3);
                break;
            default:
                break;
        }
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void setViewStatus() {

    }

    @Override
    public void onPageResume() {
    }

    @Override
    public void onPagePause() {
    }

    @Override
    public void onPageDestroy() {
    }

    class SquareListThread extends Thread {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            super.run();
            Message msg = null;
            List<SquareInfo> list2 = new ArrayList<SquareInfo>();
            try {
                if(isAdded()){
                    list2 = ApiJsonParser
                            .getSquareInfo(
                                    mSportsApp.getSessionId(),
                                    "z"
                                            + getResources().getString(
                                            R.string.config_game_id));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // if (list != null) {
            // LogUtils.e(TAG, "length-------------" + list.size());
            // if (list.size() == 0) {
            // msg = Message.obtain(msportsFindMoreHandler, FRESH_NULL);
            // msg.sendToTarget();
            // } else {
            // for (FindGroup e : list) {
            // mList.add(e);
            // }
            // LogUtils.e(TAG, "mList------------" + mList.size());
            // LogUtils.i("zhonghuibin", "mList的长度："+mList.size());
            // msg = Message.obtain(msportsFindMoreHandler, FRESH_LIST);
            // msg.sendToTarget();
            // }
            // } else {
            // if (list == null) {
            // LogUtils.d(TAG, "*******检z4********");
            // msg = Message.obtain(msportsFindMoreHandler, FRESH_FAILED);
            // msg.sendToTarget();
            // }
            if (list2 != null) {
                if (list2.size() > 0) {
                    mSquareInfo.clear();
                    for (SquareInfo squareInfo : list2) {
                        mSquareInfo.add(squareInfo);
                    }
                    msg = Message.obtain(mHandler, FRESH_LIST);
                    msg.sendToTarget();
                } else {
                    msg = Message.obtain(mHandler, FRESH_LIST_NULL);
                    msg.sendToTarget();
                }
            }
        }
    }

    /**
     * 轮播图使用
     */
    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageSelected(int arg0) {
        // TODO Auto-generated method stub

    }

    private void initImageLoader() {
        File cacheDir = com.nostra13.universalimageloader.utils.StorageUtils
                .getOwnCacheDirectory(getActivity(), IMAGE_CACHE_PATH);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisc(true).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getActivity()).defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new LruMemoryCache(12 * 1024 * 1024))
                .memoryCacheSize(12 * 1024 * 1024)
                .discCacheSize(32 * 1024 * 1024).discCacheFileCount(100)
                // .discCache(new UnlimitedDiscCache(cacheDir))
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();

        ImageLoader.getInstance().init(config);
    }

    private void initAdData() {
        // 广告数据
        imageViews = new ArrayList<ImageView>();
        // 点
        dots = new ArrayList<View>();
        dotList = new ArrayList<View>();
        dot0 = view.findViewById(R.id.v_dot0);
        dot1 = view.findViewById(R.id.v_dot1);
        dot2 = view.findViewById(R.id.v_dot2);
        dot3 = view.findViewById(R.id.v_dot3);
        dot4 = view.findViewById(R.id.v_dot4);
        dots.add(dot0);
        dots.add(dot1);
        dots.add(dot2);
        dots.add(dot3);
        dots.add(dot4);

        adViewPager = (MyViewPager) view.findViewById(R.id.vp);
        WindowManager wm = (WindowManager) getActivity().getSystemService(
                Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, width * 7 / 18);
        adViewPager.setLayoutParams(params);
        // 设置一个监听器，当ViewPager中的页面改变时调用
        adViewPager.setOnPageChangeListener(new MyPageChangeListener());

    }

    private void addDynamicView() {
        if (dots != null) {
            for (int i = 0; i < dots.size(); i++) {
                dots.get(i).setVisibility(View.GONE);
            }
        }
        if (imageViews != null) {
            imageViews.clear();
        }
        // 动态添加图片和下面指示的圆点
        // 初始化图片资源
        WindowManager wm = (WindowManager) getActivity().getSystemService(
                Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        for (int i = 0; i < mList.size(); i++) {
            ImageView imageView = new ImageView(getActivity());
            imageView.setMaxWidth(width);
            imageView.setMinimumWidth(width);
            imageView.setMaxHeight(width * 7 / 18); //这里其实可以根据需求而定，我这里测试为最大宽度的5倍
            imageView.setMinimumHeight(width * 7 / 18);
            imageView.setScaleType(ScaleType.FIT_XY);
            // 异步加载图片
            mImageLoader.displayImage(mList.get(i).getThumb(), imageView,
                    options);
            imageViews.add(imageView);
            dots.get(i).setVisibility(View.VISIBLE);
            dotList.add(dots.get(i));
        }
    }

    private class MyPageChangeListener implements OnPageChangeListener {

        private int oldPosition = 0;

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int position) {
            currentItem = position;
            // AdDomain adDomain = adList.get(position);
            dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
            dots.get(position).setBackgroundResource(R.drawable.dot_focused);
            oldPosition = position;
        }
    }

    private class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView iv = imageViews.get(position);
            ((MyViewPager) container).addView(iv);
            // 在这个方法里面设置图片的点击事件
            iv.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // 处理跳转逻辑
                    if (mList.get(position) != null) {
                        if (mList.get(position).getHref_id() == 1) {
                            if (mSportsApp.isOpenNetwork()) {
                                tanOutCount(mList.get(position).getId());// 统计点击量
                            }
                            // 第三方外部链接
                            if (mList.get(position).getUrl() != null
                                    && !"".equals(mList.get(position).getUrl())) {
                                Uri uri = Uri.parse(mList.get(position)
                                        .getUrl());
                                Intent intent = new Intent(Intent.ACTION_VIEW,
                                        uri);
                                getActivity().startActivity(intent);
                            }

                        } else if (mList.get(position).getHref_id() == 2) {
                            if (mSportsApp.isOpenNetwork()) {
                                tanOutCount(mList.get(position).getId());// 统计点击量
                                waitShowDialog();

                                // 活动详情跳转
                                new AsyncTask<Void, Void, ActivityInfo>() {

                                    @Override
                                    protected ActivityInfo doInBackground(
                                            Void... params) {
                                        // TODO Auto-generated
                                        // method stub
                                        ActivityInfo activityInfos = null;
                                        try {
                                            activityInfos = ApiJsonParser.getActivityDetailInfo(
                                                    mSportsApp.getSessionId(),
                                                    mList.get(position)
                                                            .getWeb_id(), 0);
                                        } catch (ApiNetException e) {
                                            // TODO Auto-generated
                                            // catch block
                                            e.printStackTrace();
                                        } catch (ApiSessionOutException e) {
                                            // TODO Auto-generated
                                            // catch block
                                            e.printStackTrace();
                                        }
                                        return activityInfos;
                                    }

                                    protected void onPostExecute(
                                            ActivityInfo result) {
                                        if (mLoadProgressDialog != null)
                                            if (mLoadProgressDialog.isShowing())
                                                mLoadProgressDialog.dismiss();
                                        if (result != null) {
                                            // 活动详情跳转
                                            Intent activityIntent = new Intent(
                                                    getActivity(),
                                                    ActivityInfoWebView.class);
                                            activityIntent.putExtra(
                                                    "title_name",
                                                    result.getTitle());
                                            activityIntent.putExtra(
                                                    "action_url",
                                                    result.getActivity_URl());
                                            activityIntent.putExtra(
                                                    "activity_id",
                                                    result.getActionId());
                                            activityIntent.putExtra("thrurl",
                                                    result.getActivity_URl());
                                            SimpleDateFormat formatter1 = new SimpleDateFormat(
                                                    "yyyy-MM-dd");
                                            Date endDate = new Date(result
                                                    .getEnd_time() * 1000);
                                            String endTime = formatter1
                                                    .format(endDate);
                                            endTime = endTime.replace("-", "");
                                            int b = Integer.valueOf(endTime).intValue() + 1;
                                            endTime = b + "";
                                            String year = endTime.substring(0, 4);
                                            String month = endTime.substring(4, 6);
                                            String day = endTime.substring(6, 8);
                                            endTime = year + "-" + month + "-" + day;
                                            activityIntent.putExtra("activitytime", endTime);
                                            startActivity(activityIntent);

                                        }

                                    }

                                    ;

                                }.execute();
                            }

                        } else if (mList.get(position).getHref_id() == 4) {
                            if (mSportsApp.isOpenNetwork()) {
                                tanOutCount(mList.get(position).getId());// 统计点击量
                            }
                            // 商城商品详情
                            Intent yunIntent = new Intent(getActivity(),
                                    YunHuWebViewActivity.class);
                            yunIntent.putExtra("web_id", mList.get(position)
                                    .getWeb_id());
                            startActivity(yunIntent);

                        } else if (mList.get(position).getHref_id() == 3) {
                            if (mSportsApp.isOpenNetwork()) {
                                tanOutCount(mList.get(position).getId());// 统计点击量
                            }
                            // 商城商品详情
                            Intent yunIntent = new Intent(getActivity(),
                                    UserActivityMainActivity.class);
                            yunIntent.putExtra("findId", mList.get(position)
                                    .getWeb_id());
                            startActivity(yunIntent);
                        }
                    }

                }
            });
            return iv;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView((View) arg2);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {

        }

        @Override
        public void finishUpdate(View arg0) {

        }

    }

    private void startAd() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        // 当Activity显示出来后，每两秒切换一次图片显示
        scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 2,
                TimeUnit.SECONDS);
    }

    private class ScrollTask implements Runnable {

        @Override
        public void run() {
            synchronized (adViewPager) {
                currentItem = (currentItem + 1) % imageViews.size();
                // handler.obtainMessage().sendToTarget();
                mHandler.obtainMessage(0).sendToTarget();
            }
        }
    }

    private void setDate() {
        if (mSquareInfo.size() > 0) {
            for (int i = 0; i < mSquareInfo.size(); i++) {
                if (i == 0) {
                    if (mSquareInfo.get(0).getActinfoList() != null) {
                        MyGridAdapter adapter = new MyGridAdapter(mSquareInfo
                                .get(0).getActinfoList(), 0);
                        ddfl.setText(mSquareInfo.get(0).getCat_name());
                        tuijian_gridview4.setAdapter(adapter);
                    }

                } else if (i == 1) {
                    if (mSquareInfo.get(1).getActinfoList() != null) {
                        MyGridAdapter adapter = new MyGridAdapter(mSquareInfo
                                .get(1).getActinfoList(), 1);
                        tuijian_gridview3.setAdapter(adapter);
                        ssj.setText(mSquareInfo.get(1).getCat_name());
                    }

                } else if (i == 2) {
                    if (mSquareInfo.get(2).getActinfoList() != null) {
                        MyGridAdapter adapter = new MyGridAdapter(mSquareInfo
                                .get(2).getActinfoList(), 2);
                        tuijian_gridview2.setAdapter(adapter);
                        zzs.setText(mSquareInfo.get(2).getCat_name());
                    }
                } else if (i == 3) {
                    if (mSquareInfo.get(3).getActinfoList() != null) {
                        MyGridAdapter adapter = new MyGridAdapter(mSquareInfo
                                .get(3).getActinfoList(), 3);
                        tuijian_gridview1.setAdapter(adapter);
                        jcdt.setText(mSquareInfo.get(3).getCat_name());
                    }

                }
            }
        }
    }

    class MyGridAdapter extends BaseAdapter {
        private ArrayList<ActionInfos> actinfoList;
        private LayoutInflater mInflater;
        private ImageDownloader mDownloader = null;
        private int pos;
        private Drawable drawable1, drawable2;

        public MyGridAdapter(ArrayList<ActionInfos> actinfoList, int pos) {
            this.actinfoList = actinfoList;
            mInflater = LayoutInflater.from(getActivity());
            this.mDownloader = new ImageDownloader(getActivity());
            mDownloader.setType(ImageDownloader.OnlyOne);
            this.pos = pos;
            if(isAdded()){
                drawable1 = getResources().getDrawable(R.drawable.tuijian_time);
                drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
                drawable2 = getResources().getDrawable(R.drawable.remond_pinglun);
                drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight());
            }
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return actinfoList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return actinfoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.tuijian_grid_item,
                        null);
                holder = new ViewHolder();
                holder.tuijian_txt = (TextView) convertView
                        .findViewById(R.id.tuijian_txt);
                holder.tuijian_time = (TextView) convertView
                        .findViewById(R.id.tuijian_time);
                holder.tuijian_zan = (TextView) convertView
                        .findViewById(R.id.tuijian_zan);
                holder.tuijian_img = (ImageView) convertView
                        .findViewById(R.id.tuijian_img);
                holder.time_layout = (RelativeLayout) convertView.findViewById(R.id.time_layout);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (actinfoList.get(position).getThumb() != null
                    && !"".equals(actinfoList.get(position).getThumb())) {
                mDownloader.download(actinfoList.get(position).getThumb(),
                        holder.tuijian_img, null);
            }

            holder.tuijian_img.setOnClickListener(new GridItemClick(pos,
                    position));
            holder.tuijian_time.setText(actinfoList.get(position)
                    .getInputtime());
            holder.tuijian_txt.setText(actinfoList.get(position)
                    .getDescription());
            // holder.trainplan_time.setText(
            // mlist.get(position).getTrain_time() + "分钟");
            // holder.traincount.setText(mlist.get(position).getTraincount() +
            // "人参与");
            if (mSquareInfo.get(pos).getTypeid() == 3) {
                holder.tuijian_zan.setVisibility(View.VISIBLE);
                holder.tuijian_zan.setText(actinfoList.get(position)
                        .getLike_num() + "");
                holder.tuijian_time.setText(actinfoList.get(position)
                        .getComment_num() + "");
                if(drawable2!=null){
                    holder.tuijian_time.setCompoundDrawables(drawable2, null, null, null);
                }
            } else {
                holder.tuijian_zan.setVisibility(View.GONE);
                holder.tuijian_time.setText(actinfoList.get(position)
                        .getInputtime());
                if(drawable1!=null){
                    holder.tuijian_time.setCompoundDrawables(drawable1, null, null, null);
                }
            }
            if (mSquareInfo.get(pos).getTypeid() == 4) {
                holder.time_layout.setVisibility(View.GONE);
            } else {
                holder.time_layout.setVisibility(View.VISIBLE);
            }

            return convertView;
        }

        class ViewHolder {
            public ImageView tuijian_img;
            public TextView tuijian_txt, tuijian_time, tuijian_zan;
            public RelativeLayout time_layout;
        }

    }

    // 弹出框点击的次数
    private void tanOutCount(final int activity_id) {
        new AsyncTask<Void, Void, ApiBack>() {
            @Override
            protected ApiBack doInBackground(Void... params) {
                // TODO Auto-generated method stub
                ApiBack back = null;
                try {
                    Date startDate = new Date(System.currentTimeMillis());
                    SimpleDateFormat formatter = new SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss");
                    String startTime = formatter.format(startDate);
                    back = ApiJsonParser.healthdatacount(
                            mSportsApp.getSessionId(), 11, startTime,
                            activity_id);
                    startTime = null;
                    formatter = null;
                    startDate = null;
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

    private void waitShowDialog() {
        // TODO Auto-generated method stub
        if (mLoadProgressDialog == null) {
            mLoadProgressDialog = new Dialog(getActivity(),
                    R.style.sports_dialog);
            LayoutInflater mInflater = getActivity().getLayoutInflater();
            View v1 = mInflater.inflate(R.layout.bestgirl_progressdialog, null);
            TextView mDialogMessage = (TextView) v1.findViewById(R.id.message);
            mDialogMessage.setText(R.string.bestgirl_wait);
            v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
            mLoadProgressDialog.setContentView(v1);
        }
        if (mLoadProgressDialog != null)
            if (!mLoadProgressDialog.isShowing()
                    && !getActivity().isFinishing())
                mLoadProgressDialog.show();
    }

    class GridItemClick implements OnClickListener {
        private int pos, position;

        public GridItemClick(int pos, int position) {
            this.pos = pos;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (pos == 0) {
                Intent fulis1 = new Intent(getActivity(),
                        YunHuWebViewActivity.class);
                fulis1.putExtra("web_id", mSquareInfo.get(0).getActinfoList()
                        .get(position).getWeb_id());
                startActivity(fulis1);
            } else if (pos == 1) {
                Intent doubi1 = new Intent(getActivity(),
                        ZhangzisiWebViewActivity.class);
                doubi1.putExtra("infoid", mSquareInfo.get(1).getActinfoList()
                        .get(position).getId());
                doubi1.putExtra("bs", 2);
                doubi1.putExtra("tbs", 1);
                startActivity(doubi1);
            } else if (pos == 2) {
                Intent zhangzishi1 = new Intent(getActivity(),
                        ZhangzisiWebViewActivity.class);
                zhangzishi1.putExtra("infoid", mSquareInfo.get(2)
                        .getActinfoList().get(position).getId());
                zhangzishi1.putExtra("bs", 2);
                zhangzishi1.putExtra("tbs", 2);
                startActivity(zhangzishi1);
            } else if (pos == 3) {
                Intent mIntent = new Intent(getActivity(),
                        UserActivityMainActivity.class);
                int id1 = mSquareInfo.get(3).getActinfoList().get(position)
                        .getWeb_id();
                mIntent.putExtra("tp", 101);
                mIntent.putExtra("findId", id1);
                startActivity(mIntent);
            }

        }

    }

    @Override
    public void onRefresh(PullDownScrollView view) {
        // TODO Auto-generated method stub
        if (mSportsApp.isOpenNetwork()) {
            getListData();
            SquareListThread thread = new SquareListThread();
            thread.start();
            mPullDownScrollView.finishRefresh("松开刷新");
        } else {
            mPullDownScrollView.finishRefresh("松开刷新");
            if(isAdded()){
                Toast.makeText(getActivity(),
                        getResources().getString(R.string.network_not_avaliable),
                        Toast.LENGTH_SHORT).show();
            }
        }

    }

    //获取推荐的数据 先读取缓存 如果没有则读取网络
    private void getAllDate() {
        // 缓存
        SharedPreferences preferences = getActivity()
                .getSharedPreferences("ExternalActiviList", 0);
        String content = preferences.getString(
                "ExternalActiviList_info", "");
        if (content != null && !"".equals(content)) {
            try {
                JSONArray jsonArray = new JSONObject(content)
                        .getJSONArray("data");
                ExternalActivi externalActivis = null;
                if (mList != null) {
                    mList.clear();
                }
                if (jsonArray != null&& jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);

                        externalActivis = new ExternalActivi();
                        if (obj.has("id")) {
                            externalActivis.setId(obj.getInt("id"));
                        }
                        if (obj.has("title")) {
                            externalActivis
                                    .setTitle(obj.getString("title"));
                        }
                        if (obj.has("thumb")) {
                            externalActivis
                                    .setThumb(obj.getString("thumb"));
                        }
                        if (obj.has("start_time")) {
                            externalActivis.setStart_time(obj
                                    .getString("start_time"));
                        }
                        if (obj.has("end_time")) {
                            externalActivis.setEnd_time(obj
                                    .getString("end_time"));
                        }
                        if (obj.has("url")) {
                            externalActivis.setUrl(obj.getString("url"));
                        }
                        if (obj.has("price")) {
                            externalActivis.setPrice(obj.getInt("price"));
                        }
                        if (obj.has("href_id")) {
                            externalActivis.setHref_id(obj
                                    .getInt("href_id"));
                        }
                        if (obj.has("web_id")) {
                            externalActivis.setWeb_id(obj.getInt("web_id"));
                        }
                        mList.add(externalActivis);
                    }
                }
                addDynamicView();
                adViewPager.setAdapter(new MyAdapter());// 设置填充ViewPager页面的适配器
                startAd();
                if (mSportsApp.isOpenNetwork()) {
                    getListData();
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            if (mSportsApp.isOpenNetwork()) {
                getListData();
            }
        }


        // 缓存
        SharedPreferences preferences1 = getActivity()
                .getSharedPreferences("SquareInfoList", 0);
        String content1 = preferences1.getString("SquareInfoList_info",
                "");
        if (content1 != null && !"".equals(content1)) {
            SquareInfo squareInfo = null;
            if (mSquareInfo != null) {
                mSquareInfo.clear();
            }
            try {
                JSONArray jsonArray = new JSONObject(content1)
                        .getJSONArray("data");
                if (jsonArray != null && jsonArray.length() > 0) {
                    for (int i = 0, j = jsonArray.length(); i < j; i++) {
                        squareInfo = new SquareInfo();
                        JSONObject obj = jsonArray.getJSONObject(i);
                        if (obj.has("id")) {
                            squareInfo.setId(obj.getInt("id"));
                        }
                        if (obj.has("cat_name")) {
                            squareInfo.setCat_name(obj
                                    .getString("cat_name"));
                        }
                        if (obj.has("typeid")) {
                            squareInfo.setTypeid(obj.getInt("typeid"));
                        }
                        if (obj.has("cat_order")) {
                            squareInfo.setCat_order(obj
                                    .getInt("cat_order"));
                        }
                        if (obj.has("url")) {
                            squareInfo.setUrl(obj.getString("url"));
                        }

                        if (obj.has("actnum")) {
                            squareInfo.setActnum(obj.getInt("actnum"));
                            if (squareInfo.getActnum() != 0) {
                                ArrayList<ActionInfos> actinfoList = null;
                                if (obj.has("actinfo")) {
                                    actinfoList = new ArrayList<ActionInfos>();
                                    JSONArray infoJsonArray = obj
                                            .getJSONArray("actinfo");
                                    for (int k = 0; k < infoJsonArray
                                            .length(); k++) {
                                        ActionInfos actionInfos = new ActionInfos();
                                        JSONObject infoObj = infoJsonArray
                                                .getJSONObject(k);
                                        if (infoObj.has("id")) {
                                            actionInfos.setId(infoObj
                                                    .getInt("id"));
                                        }
                                        if (infoObj.has("title")) {
                                            actionInfos
                                                    .setTitle(infoObj
                                                            .getString("title"));
                                        }
                                        if (infoObj.has("description")) {
                                            actionInfos
                                                    .setDescription(infoObj
                                                            .getString("description"));
                                        }
                                        if (infoObj.has("start_time")) {
                                            actionInfos
                                                    .setStart_time(infoObj
                                                            .getString("start_time"));
                                        }
                                        if (infoObj.has("end_time")) {
                                            actionInfos
                                                    .setEnd_time(infoObj
                                                            .getString("end_time"));
                                        }
                                        if (infoObj.has("inputtime")) {
                                            actionInfos
                                                    .setInputtime(infoObj
                                                            .getString("inputtime"));
                                        }
                                        if (infoObj.has("listorder")) {
                                            actionInfos
                                                    .setListorder(infoObj
                                                            .getInt("listorder"));
                                        }
                                        if (infoObj.has("content")) {
                                            actionInfos
                                                    .setContent(infoObj
                                                            .getString("content"));
                                        }
                                        if (infoObj.has("web_id")) {
                                            actionInfos
                                                    .setWeb_id(infoObj
                                                            .getInt("web_id"));
                                        }
                                        if (infoObj.has("thumb")) {
                                            actionInfos
                                                    .setThumb(infoObj
                                                            .getString("thumb"));
                                        }
                                        if (infoObj.has("url")) {
                                            actionInfos.setUrl(infoObj
                                                    .getString("url"));
                                        }
                                        if (infoObj.has("like_num")) {
                                            actionInfos
                                                    .setLike_num(infoObj
                                                            .getInt("like_num"));
                                        }

                                        if (infoObj.has("comment_num")) {
                                            actionInfos.setComment_num(infoObj
                                                    .getInt("comment_num"));
                                        }
                                        actinfoList.add(actionInfos);
                                    }
                                    squareInfo
                                            .setActinfoList(actinfoList);
                                }
                            }
                        }
                        mSquareInfo.add(squareInfo);
                    }
                }

                setDate();
                if(isAdded()){
                    mMore.setText(getResources().getString(R.string.guangchang_more));
                    mMore_two.setText(getResources().getString(R.string.guangchang_more));
                    mMore_three.setText(getResources().getString(R.string.guangchang_more));
                }
                if (mSportsApp.isOpenNetwork()) {
                    SquareListThread thread = new SquareListThread();
                    thread.start();
                }


            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            if (mSportsApp.isOpenNetwork()) {
                SquareListThread thread = new SquareListThread();
                thread.start();
            }
        }


    }

}