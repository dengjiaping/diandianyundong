package com.fox.exercise.newversion.act;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

import com.fox.exercise.AbstractBaseActivity;
import com.fox.exercise.FansAndNear;
import com.fox.exercise.ImageDownloader;
import com.fox.exercise.R;
import com.fox.exercise.api.ApiBack;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.bitmap.util.ImageResizer;
import com.fox.exercise.newversion.entity.CircleFindContent;
import com.fox.exercise.newversion.entity.CircleFindDetailContent;
import com.fox.exercise.newversion.entity.FindGroup;
import com.fox.exercise.newversion.entity.PointsSay;
import com.fox.exercise.newversion.view.CollapsibleTextView;
import com.fox.exercise.newversion.view.GridViewWithHeaderAndFooter;
import com.fox.exercise.view.PullToRefreshBase.OnRefreshListener;
import com.fox.exercise.view.PullToRefreshGridHeadView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import cn.ingenic.indroidsync.SportsApp;

/**
 * @author loujungang 运动圈发现里的趣事页面
 */
public class SportCircleFindEventActivity extends AbstractBaseActivity
        implements OnClickListener {

    private TextView iView;
    private RelativeLayout qushi_tuijian_layout, qushi_all_layout;
    private View qushi_tuijian__line, qushi_all_line;
    private int id;
    private String titles;
    private int times = 0;
    private int add = 0;
    private Dialog mLoadProgressDialog = null;
    private TextView mDialogMessage;
    private int allOrTuijian = 0;

    private SportsApp mSportsApp;
    private Context mContext;
    // 图片正方形
    private ImageResizer mImageWorker;
    private ImageDownloader mDownloader2 = null;
    int width, height;

    private PullToRefreshGridHeadView pullToRefreshGridView;
    private GridViewWithHeaderAndFooter mGridView;
    private MyAdapter myAdapter;
    private View head_view;
    private ImageView find_qushi_icon;
    private CollapsibleTextView collaps_txtview;

    private PointsSay pointsSay;
    private List<CircleFindContent> alllist = new ArrayList<CircleFindContent>();// 是全部列表的说说
    private List<CircleFindContent> recommendlist = new ArrayList<CircleFindContent>();// 是个大数组，是推荐列表的说说

    private final int FRESH_LIST = 111;// 更新成功
    private final int FRESH_FAILED = 112;// 更新失败
    private final int FRESH_DONE = 113;
    private final int REQUESTCODE = 1;
    private SportsFindMoreHandler mFindMoreHandler;
    private Animation animation;

    @Override
    public void initIntentParam(Intent intent) {
        // TODO Auto-generated method stub
        if (intent != null) {
            id = intent.getIntExtra("id", 0);
            titles = intent.getStringExtra("title");
            title = titles;
        }
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        // 主体
        showContentView(R.layout.activity_sport_circle_find_event);
        mSportsApp = (SportsApp) getApplication();
        mContext = this;
        mDownloader2 = new ImageDownloader(mContext);
        mDownloader2.setType(ImageDownloader.OnlyOne);
        // 点击减1加1效果测试
        animation = AnimationUtils.loadAnimation(mContext, R.anim.add_one);
        WindowManager wm = getWindowManager();
        width = wm.getDefaultDisplay().getWidth();
        mImageWorker = mSportsApp.getImageWorker(mContext, width, 0);
        iView = new TextView(this);
        iView.setText("分享");
        iView.setTextColor(getResources().getColor(R.color.black));
        iView.setBackgroundResource(R.color.white);
        iView.setLayoutParams(new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        iView.setPadding(15, 15, 15, 15);
        showRightBtn(iView);
        pullToRefreshGridView = (PullToRefreshGridHeadView) findViewById(R.id.sportscircle_find_refresh_gridview);
        mGridView = pullToRefreshGridView.getRefreshableView();
        head_view = LayoutInflater.from(this).inflate(
                R.layout.circle_findevent_grid_headview, null);
        qushi_tuijian_layout = (RelativeLayout) head_view
                .findViewById(R.id.qushi_tuijian_layout);
        qushi_all_layout = (RelativeLayout) head_view
                .findViewById(R.id.qushi_all_layout);
        qushi_tuijian_layout.setOnClickListener(this);
        qushi_all_layout.setOnClickListener(this);
        qushi_tuijian__line = head_view.findViewById(R.id.qushi_tuijian__line);
        qushi_all_line = head_view.findViewById(R.id.qushi_all_line);
        qushi_tuijian__line.setVisibility(View.VISIBLE);
        qushi_all_line.setVisibility(View.GONE);
        allOrTuijian = 2;
        find_qushi_icon = (ImageView) head_view
                .findViewById(R.id.find_qushi_icon);
        collaps_txtview = (CollapsibleTextView) head_view
                .findViewById(R.id.collaps_txtview);
        mFindMoreHandler = new SportsFindMoreHandler();
        waitShowDialog();
        SportsFindMoreThread sportsFindMoreThread = new SportsFindMoreThread();
        sportsFindMoreThread.start();
        // myAdapter = new MyAdapter(this);

        pullToRefreshGridView.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh(int pullDirection) {
                if (mSportsApp.isOpenNetwork()) {
                    switch (pullDirection) {
                        case FansAndNear.MODE_DEFAULT_LOAD:
                            times++;
                            SportsFindMoreThread loadThread = new SportsFindMoreThread();
                            loadThread.start();
                            break;
                        case FansAndNear.MODE_PULL_DOWN_TO_REFRESH:
                            times = 0;
                            SportsFindMoreThread refreshThread = new SportsFindMoreThread();
                            refreshThread.start();
                            break;
                    }
                } else {
                    Toast.makeText(
                            mContext,
                            getResources().getString(
                                    R.string.acess_server_error),
                            Toast.LENGTH_SHORT).show();
                    pullToRefreshGridView.onRefreshComplete();
                    if (mLoadProgressDialog != null)
                        if (mLoadProgressDialog.isShowing())
                            mLoadProgressDialog.dismiss();
                }

            }
        });
    }

    @Override
    public void setViewStatus() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageResume() {
        // TODO Auto-generated method stub
        MobclickAgent.onPageStart("SportCircleFindEventActivity");
    }

    @Override
    public void onPagePause() {
        // TODO Auto-generated method stub
        MobclickAgent.onPageEnd("SportCircleFindEventActivity");
    }

    @Override
    public void onPageDestroy() {
        // TODO Auto-generated method stub
        if (mLoadProgressDialog != null)
            if (mLoadProgressDialog.isShowing())
                mLoadProgressDialog.dismiss();
        alllist = null;
        recommendlist = null;
        animation = null;
//		mSportsApp=null;
    }

    // 自定义适配器
    class MyAdapter extends BaseAdapter {
        // 上下文对象
        private Context context;
        private LayoutInflater inflater;
        Drawable drawable1 = null;
        Drawable drawable2 = null;

        MyAdapter(Context context) {
            this.context = context;
            drawable1 = mContext.getResources().getDrawable(
                    R.drawable.user_dongtai_zan);
            drawable1.setBounds(0, 0, drawable1.getMinimumWidth(),
                    drawable1.getMinimumHeight());
            drawable2 = mContext.getResources().getDrawable(
                    R.drawable.already_zan_icon);
            drawable2.setBounds(0, 0, drawable2.getMinimumWidth(),
                    drawable2.getMinimumHeight());
            inflater = LayoutInflater.from(context);
        }

        public int getCount() {
            if (allOrTuijian == 1) {
                return alllist.size();
            } else {
                return recommendlist.size();
            }

        }

        public Object getItem(int item) {
            if (allOrTuijian == 1) {
                return alllist.get(item);
            } else {
                return recommendlist.get(item);
            }
        }

        public long getItemId(int id) {
            return id;
        }

        // 创建View方法
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.find_gridview_item,
                        null);
                holder = new ViewHolder();
                holder.all_tuijian_icon = (ImageView) convertView
                        .findViewById(R.id.all_tuijian_icon);
                holder.all_tuijian_txt = (TextView) convertView
                        .findViewById(R.id.all_tuijian_txt);
                holder.list_user_zans = (TextView) convertView
                        .findViewById(R.id.list_user_zans);
                holder.list_user_zan_addones = (TextView) convertView
                        .findViewById(R.id.list_user_zan_addones);
                holder.list_user_pingluns = (TextView) convertView
                        .findViewById(R.id.list_user_pingluns);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            CircleFindContent circleFindContent = null;
            if (allOrTuijian == 1) {
                circleFindContent = alllist.get(position);
                if (circleFindContent.getImgs() != null
                        && circleFindContent.getImgs().length != 0) {
                    setImageVoid(circleFindContent.getImgs()[0],
                            holder.all_tuijian_icon);
                }
                if (circleFindContent.getContent() != null
                        && !"".equals(circleFindContent.getContent())) {
                    holder.all_tuijian_txt.setText(circleFindContent
                            .getContent());
                }
                // else {
                // holder.all_tuijian_txt.setVisibility(View.GONE);
                // }
                if (circleFindContent.getLikenum() > 0) {
                    if (circleFindContent.getIlike() != 0) {
                        holder.list_user_zans.setCompoundDrawables(drawable2,
                                null, null, null);
                    } else {
                        holder.list_user_zans.setCompoundDrawables(drawable1,
                                null, null, null);
                    }
                    holder.list_user_zans.setText("赞 ("
                            + circleFindContent.getLikenum() + ")");
                } else {
                    holder.list_user_zans.setCompoundDrawables(drawable1, null,
                            null, null);
                    holder.list_user_zans.setText("赞 (0)");
                }
                if (circleFindContent.getCommentnum() > 0) {
                    holder.list_user_pingluns.setText("回复 ("
                            + circleFindContent.getCommentnum() + ")");
                } else {
                    holder.list_user_pingluns.setText("回复 (0)");
                }
                holder.list_user_zans.setOnClickListener(new AddAnimian(
                        holder.list_user_zan_addones, circleFindContent,
                        position));
                holder.list_user_pingluns
                        .setOnClickListener(new ToUserActivityListner(
                                circleFindContent));

            } else {
                circleFindContent = recommendlist.get(position);
                if (circleFindContent.getImgs() != null
                        && circleFindContent.getImgs().length != 0) {
                    setImageVoid(circleFindContent.getImgs()[0],
                            holder.all_tuijian_icon);
                }
                if (circleFindContent.getContent() != null
                        && !"".equals(circleFindContent.getContent())) {
                    holder.all_tuijian_txt.setText(circleFindContent
                            .getContent());
                } else {
                    holder.all_tuijian_txt.setVisibility(View.GONE);
                }
                if (circleFindContent.getLikenum() > 0) {
                    if (circleFindContent.getIlike() != 0) {
                        holder.list_user_zans.setCompoundDrawables(drawable2,
                                null, null, null);
                        holder.list_user_zans.setText("赞 ("
                                + circleFindContent.getLikenum() + ")");
                    } else {
                        holder.list_user_zans.setCompoundDrawables(drawable1,
                                null, null, null);
                        holder.list_user_zans.setText("赞 ("
                                + circleFindContent.getLikenum() + ")");
                    }

                } else {
                    holder.list_user_zans.setCompoundDrawables(drawable1, null,
                            null, null);
                    holder.list_user_zans.setText("赞 (0)");
                }
                if (circleFindContent.getCommentnum() > 0) {
                    holder.list_user_pingluns.setText("回复 ("
                            + circleFindContent.getCommentnum() + ")");
                } else {
                    holder.list_user_pingluns.setText("回复 (0)");
                }
                holder.list_user_zans.setOnClickListener(new AddAnimian(
                        holder.list_user_zan_addones, circleFindContent,
                        position));
                holder.list_user_pingluns
                        .setOnClickListener(new ToUserActivityListner(
                                circleFindContent));

            }
            return convertView;
        }
    }

    private void setImageVoid(String url, ImageView imageView) {
        // mImageWorker.loadImage(url, imageView, null, null, false);
        mDownloader2.download(url, imageView, null);
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.qushi_tuijian_layout:
                allOrTuijian = 2;
                qushi_tuijian__line.setVisibility(View.VISIBLE);
                qushi_all_line.setVisibility(View.GONE);
                break;
            case R.id.qushi_all_layout:
                allOrTuijian = 1;
                qushi_tuijian__line.setVisibility(View.GONE);
                qushi_all_line.setVisibility(View.VISIBLE);
                break;
        }
        // waitShowDialog();
        // SportsFindMoreThread sportsFindMoreThread = new
        // SportsFindMoreThread();
        // sportsFindMoreThread.start();
        myAdapter.notifyDataSetChanged();

    }

    class SportsFindMoreThread extends Thread {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            super.run();
            add++;
            Message msg = null;
            CircleFindDetailContent circleFindDetailContent = null;

            try {
                circleFindDetailContent = ApiJsonParser
                        .getCircleFindListsContent(mSportsApp.getSessionId(),
                                times, id, 1, allOrTuijian);
            } catch (ApiNetException e) {
                e.printStackTrace();
            } catch (ApiSessionOutException e) {
                e.printStackTrace();
            }
            if (circleFindDetailContent != null) {
                if (times == 0) {
                    msg = Message.obtain(mFindMoreHandler, FRESH_LIST);
                    msg.obj = circleFindDetailContent;
                    msg.sendToTarget();
                } else {
                    msg = Message.obtain(mFindMoreHandler, FRESH_DONE);
                    msg.obj = circleFindDetailContent;
                    msg.sendToTarget();
                }
            } else {
                msg = Message.obtain(mFindMoreHandler, FRESH_FAILED);
                msg.sendToTarget();
            }
        }

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
    }

    class SportsFindMoreHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case FRESH_LIST:
                    CircleFindDetailContent circleFindDetailContent = (CircleFindDetailContent) msg.obj;
                    if (myAdapter == null) {
                        myAdapter = new MyAdapter(mContext);
                    }
                    if (add == 1) {
                        pointsSay = new PointsSay();
                        pointsSay = circleFindDetailContent.getPointsSay();
                        mImageWorker.loadImage(circleFindDetailContent
                                        .getPointsSay().getImg(), find_qushi_icon, null,
                                null, false);
                        mDownloader2.download(circleFindDetailContent
                                .getPointsSay().getImg(), find_qushi_icon, null);
                        collaps_txtview.setDesc(pointsSay.getConnent(),
                                BufferType.NORMAL);
                        mGridView.addHeaderView(head_view);
                        mGridView.setAdapter(myAdapter);
                    }
                    if (circleFindDetailContent.getAlllist() != null
                            && circleFindDetailContent.getAlllist().size() > 0) {
                        alllist = circleFindDetailContent.getAlllist();
                    }
                    if (circleFindDetailContent.getRecommendlist() != null
                            && circleFindDetailContent.getRecommendlist().size() > 0) {
                        recommendlist = circleFindDetailContent.getRecommendlist();
                    }
                    // if (allOrTuijian == 1) {
                    // if (circleFindDetailContent.getAlllist() != null
                    // && circleFindDetailContent.getAlllist().size() > 0) {
                    // alllist = circleFindDetailContent.getAlllist();
                    // } else {
                    // Toast.makeText(
                    // mContext,
                    // getResources().getString(
                    // R.string.sports_data_load_more_null),
                    // Toast.LENGTH_SHORT).show();
                    // }
                    //
                    // } else if (allOrTuijian == 2) {
                    // if (circleFindDetailContent.getRecommendlist() != null
                    // && circleFindDetailContent.getRecommendlist()
                    // .size() > 0) {
                    // recommendlist = circleFindDetailContent
                    // .getRecommendlist();
                    // } else {
                    // Toast.makeText(
                    // mContext,
                    // getResources().getString(
                    // R.string.sports_data_load_more_null),
                    // Toast.LENGTH_SHORT).show();
                    // }
                    //
                    // }

                    myAdapter.notifyDataSetChanged();
                    pullToRefreshGridView.onRefreshComplete();
                    if (mLoadProgressDialog != null)
                        if (mLoadProgressDialog.isShowing())
                            mLoadProgressDialog.dismiss();
                    break;

                case FRESH_FAILED:
                    if (!mSportsApp.isOpenNetwork()) {
                        Toast.makeText(
                                mContext,
                                getResources().getString(
                                        R.string.acess_server_error),
                                Toast.LENGTH_SHORT).show();
                    } else if (this != null) {
                        Toast.makeText(
                                mContext,
                                getResources().getString(
                                        R.string.sports_get_list_failed),
                                Toast.LENGTH_SHORT).show();
                    }
                    if (mLoadProgressDialog != null)
                        if (mLoadProgressDialog.isShowing())
                            mLoadProgressDialog.dismiss();
                    // sportsfindmoreAdapter.notifyDataSetChanged();
                    pullToRefreshGridView.onRefreshComplete();
                    break;
                case FRESH_DONE:
                    if (this != null) {
                        CircleFindDetailContent circleFindDetailContents = (CircleFindDetailContent) msg.obj;
                        if (myAdapter == null) {
                            myAdapter = new MyAdapter(mContext);
                        }
                        if (allOrTuijian == 1) {
                            if (circleFindDetailContents.getAlllist() != null
                                    && circleFindDetailContents.getAlllist().size() > 0) {
                                alllist.addAll(circleFindDetailContents
                                        .getAlllist());
                            } else {
                                Toast.makeText(
                                        mContext,
                                        getResources()
                                                .getString(
                                                        R.string.sports_data_load_more_null),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else if (allOrTuijian == 2) {
                            if (circleFindDetailContents.getRecommendlist() != null
                                    && circleFindDetailContents.getRecommendlist()
                                    .size() > 0) {
                                alllist.addAll(circleFindDetailContents
                                        .getRecommendlist());
                            } else {
                                Toast.makeText(
                                        mContext,
                                        getResources()
                                                .getString(
                                                        R.string.sports_data_load_more_null),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                        myAdapter.notifyDataSetChanged();
                    }
                    if (mLoadProgressDialog != null)
                        if (mLoadProgressDialog.isShowing())
                            mLoadProgressDialog.dismiss();
                    pullToRefreshGridView.onRefreshComplete();
                    break;
                default:
                    break;
            }
        }

    }

    class ViewHolder {
        private ImageView all_tuijian_icon;
        private TextView all_tuijian_txt, list_user_zans,
                list_user_zan_addones, list_user_pingluns;
    }

    /**
     * @author loujungang 加1点赞效果
     */
    class AddAnimian implements View.OnClickListener {
        private View view;
        private CircleFindContent circleFindContent;
        private int position;

        public AddAnimian(View view, CircleFindContent circleFindContent,
                          int position) {
            this.view = view;
            this.circleFindContent = circleFindContent;
            this.position = position;
        }

        @Override
        public void onClick(final View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.list_user_zans:
                    final int theBeforeGoodPeople = circleFindContent.getLikenum();
                    view.setVisibility(View.VISIBLE);
                    final TextView textView = (TextView) view;
                    if (mSportsApp.isOpenNetwork()) {
                        new AsyncTask<Void, Void, ApiBack>() {
                            @Override
                            protected void onPreExecute() {
                                // TODO Auto-generated method stub
                                if (circleFindContent.getIlike() != 0) {
                                    // showWaitDialog(mContext.getResources()
                                    // .getString(R.string.cancel_wait));
                                    textView.setText("-1");
                                } else {
                                    // showWaitDialog(mContext.getResources()
                                    // .getString(R.string.praise_wait));
                                    textView.setText("+1");
                                }
                            }

                            @Override
                            protected ApiBack doInBackground(Void... params) {
                                // TODO Auto-generated method stub
                                ApiBack back = null;
                                try {
                                    back = (ApiBack) ApiJsonParser.likeFind(
                                            mSportsApp.getSessionId(),
                                            circleFindContent.getId() + "");
                                } catch (ApiNetException e) {
                                    e.printStackTrace();
                                }
                                return back;
                            }

                            @Override
                            protected void onPostExecute(ApiBack result) {
                                // TODO Auto-generated method stub
                                super.onPostExecute(result);
                                if (result == null || result.getFlag() != 0) {
                                    // Log.e("--", "result-------"+result);
                                    // waitProgressDialog.dismiss();
                                    Toast.makeText(
                                            mContext,
                                            mContext.getResources().getString(
                                                    R.string.sports_findgood_error),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    // 成功
                                    // waitProgressDialog.dismiss();
                                    if (circleFindContent.getIlike() != 0) {
                                        if (allOrTuijian == 1) {
                                            alllist.get(position).setIlike(0);
                                            alllist.get(position).setLikenum(
                                                    theBeforeGoodPeople - 1);
                                        } else {
                                            recommendlist.get(position).setIlike(0);
                                            recommendlist.get(position).setLikenum(
                                                    theBeforeGoodPeople - 1);
                                        }
                                        // Toast.makeText(
                                        // mContext,
                                        // mContext.getResources()
                                        // .getString(
                                        // R.string.praise_cancel_success),
                                        // Toast.LENGTH_SHORT).show();

                                    } else {
                                        if (allOrTuijian == 1) {
                                            alllist.get(position).setIlike(1);
                                            alllist.get(position).setLikenum(
                                                    theBeforeGoodPeople + 1);
                                        } else {
                                            recommendlist.get(position).setIlike(1);
                                            recommendlist.get(position).setLikenum(
                                                    theBeforeGoodPeople + 1);
                                        }
                                        // Toast.makeText(
                                        // mContext,
                                        // mContext.getResources().getString(
                                        // R.string.praise_successed),
                                        // Toast.LENGTH_SHORT).show();
                                    }
                                    view.startAnimation(animation);
                                    new Handler().postDelayed(new Runnable() {
                                        public void run() {
                                            view.setVisibility(View.GONE);
                                            myAdapter.notifyDataSetChanged();
                                        }
                                    }, 1000);

                                }
                            }

                        }.execute();
                    } else {
                        Toast.makeText(
                                mContext,
                                mContext.getResources().getString(
                                        R.string.error_cannot_access_net),
                                Toast.LENGTH_SHORT).show();

                    }

                    break;
            }

        }

    }

    // 跳转到动态详情页面
    class ToUserActivityListner implements OnClickListener {
        private CircleFindContent circleFindContent;

        public ToUserActivityListner(CircleFindContent circleFindContent) {
            this.circleFindContent = circleFindContent;
        }

        @Override
        public void onClick(View view) {
            // TODO Auto-generated method stub
            Intent mIntent = new Intent(mContext,
                    UserActivityMainActivity.class);
            mIntent.putExtra("findId", circleFindContent.getId());
            startActivityForResult(mIntent, REQUESTCODE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUESTCODE:
                FindGroup mFindMore = (FindGroup) data
                        .getSerializableExtra("mFindMore");
                if (mFindMore != null) {
                    if (allOrTuijian == 1) {
                        for (int i = 0; i < alllist.size(); i++) {
                            if (mFindMore.getFindId().equals(
                                    alllist.get(i).getId() + "")) {
                                alllist.get(i)
                                        .setLikenum(mFindMore.getGoodpeople());
                                alllist.get(i).setCommentnum(mFindMore.getcCount());
                                if (myAdapter != null) {
                                    new MyAdapter(this);
                                }

                                myAdapter.notifyDataSetChanged();

                            }
                        }
                    } else if (allOrTuijian == 2) {
                        for (int i = 0; i < recommendlist.size(); i++) {
                            if (mFindMore.getFindId().equals(
                                    recommendlist.get(i).getId() + "")) {
                                recommendlist.get(i).setLikenum(
                                        mFindMore.getGoodpeople());
                                recommendlist.get(i).setCommentnum(
                                        mFindMore.getcCount());
                                if (myAdapter != null) {
                                    new MyAdapter(this);
                                }
                                myAdapter.notifyDataSetChanged();

                            }
                        }

                    }
                }
                break;

            default:
                break;
        }
    }

}
