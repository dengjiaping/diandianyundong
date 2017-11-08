package com.fox.exercise.newversion.act;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fox.exercise.AbstractBaseActivity;
import com.fox.exercise.FansAndNear;
import com.fox.exercise.ImageDownloader;
import com.fox.exercise.R;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.entity.ActListInfo;
import com.fox.exercise.view.PullToRefreshBase.OnRefreshListener;
import com.fox.exercise.view.PullToRefreshListView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import cn.ingenic.indroidsync.SportsApp;

public class DoubiActivity extends AbstractBaseActivity {
    private ListView mListView;
    ArrayList<ActListInfo> list = new ArrayList<ActListInfo>();
    int tid, page = 0;
    private SportsApp mSportsApp;
    private SportsApp msApp;
    private ImageDownloader mDownloader = null;
    LinearLayout dblin;
    private PullToRefreshListView doubiListView = null;
    private DoubiAdapter mAdapter;

    @Override
    public void initIntentParam(Intent intent) {
        // TODO Auto-generated method stub
        title = getResources().getString(R.string.doubi);
        Intent mIntent = getIntent();
        tid = mIntent.getIntExtra("typeid", -1);
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        showContentView(R.layout.doubi_activity);
        doubiListView = (PullToRefreshListView) findViewById(R.id.doubi_list);
        mListView = doubiListView.getRefreshableView();
        dblin = (LinearLayout) findViewById(R.id.doubi_lin);
        mSportsApp = (SportsApp) getApplication();
        msApp = SportsApp.getInstance();

        this.mDownloader = new ImageDownloader(getApplication());
        mDownloader.setType(ImageDownloader.OnlyOne);

    }

    @Override
    public void setViewStatus() {
        GetData();
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (msApp.isOpenNetwork()) {
                    int id = list.get(arg2).getId();
                    // Toast.makeText(getApplication(), "arg2是" + arg2,
                    // 10).show();
                    Intent mIntent = new Intent(getApplication(),
                            ZhangzisiWebViewActivity.class);
                    mIntent.putExtra("infoid", id);
                    mIntent.putExtra("bs", 2);
                    startActivity(mIntent);
                } else {
                    Toast.makeText(DoubiActivity.this,
                            getString(R.string.network_not_avaliable),
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        // TODO Auto-generated method stub
        doubiListView.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh(int pullDirection) {
                // TODO Auto-generated method stub
                if (msApp.isOpenNetwork()) {
                    switch (pullDirection) {
                        case FansAndNear.MODE_DEFAULT_LOAD:

                            // 上拉加载
                            page++;
                            GetData();
                            break;
                        case FansAndNear.MODE_PULL_DOWN_TO_REFRESH:
                            // 下拉刷新
                            page = 0;
                            list.clear();
                            GetData();
                            break;

                        default:
                            break;
                    }
                } else {
                    Toast.makeText(DoubiActivity.this,
                            getString(R.string.network_not_avaliable),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onPageResume() {
        // TODO Auto-generated method stub
        MobclickAgent.onPageStart("Shen_webViewClass");
    }

    @Override
    public void onPagePause() {
        // TODO Auto-generated method stub
        MobclickAgent.onPageEnd("Shen_webViewClass");
    }

    @Override
    public void onPageDestroy() {
        // TODO Auto-generated method stub
    }

    // 获取数据加载显示
    public void GetData() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                ArrayList<ActListInfo> mlist = new ArrayList<ActListInfo>();

                try {
                    mlist = (ArrayList<ActListInfo>) ApiJsonParser
                            .getActListInfos(
                                    mSportsApp.getSessionId(),

                                    "z"
                                            + getResources().getString(
                                            R.string.config_game_id),
                                    tid, page);
                    if (mlist.size() > 0) {
                        for (ActListInfo actListInfo : mlist) {
                            list.add(actListInfo);
                        }
                        mHandler.sendEmptyMessage(100);
                    }
                } catch (NotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
        // new AsyncTask<Void, Void, ArrayList<ActListInfo>>() {
        //
        // @Override
        // protected ArrayList<ActListInfo> doInBackground(Void... params) {
        // // TODO Auto-generated method stub
        // ArrayList<ActListInfo> list1;
        // list1 = new ArrayList<ActListInfo>();
        // try {
        // list1 = (ArrayList<ActListInfo>) ApiJsonParser
        // .getActListInfos(
        // mSportsApp.getSessionId(),
        //
        // "z"
        // + getResources().getString(
        // R.string.config_game_id),
        // tid, page);
        // } catch (NotFoundException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // return list1;
        // }
        //
        // @Override
        // protected void onPostExecute(ArrayList<ActListInfo> result) {
        // // TODO Auto-generated method stub
        // if (result.size() > 0) {
        // for (ActListInfo actListInfo : result) {
        // list.add(actListInfo);
        // }
        // mListView.setAdapter(new DoubiAdapter(list,
        // getApplication()));
        // page++;
        // dblin.setVisibility(View.GONE);
        // }
        //
        // }
        // }.execute();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 100:
                    // Toast.makeText(getApplication(), "第" + page + "页",
                    // 10).show();
                    dblin.setVisibility(View.GONE);
                    if (mAdapter != null) {
                        mAdapter.notifyDataSetChanged();
                    } else {
                        mAdapter = new DoubiAdapter(list, DoubiActivity.this);
                        mListView.setAdapter(mAdapter);
                    }
                    doubiListView.onRefreshComplete();
                    break;

                default:
                    break;
            }
        }
    };

    class DoubiAdapter extends BaseAdapter {
        Context mContext;
        ArrayList<ActListInfo> list;

        public DoubiAdapter(ArrayList<ActListInfo> list, Context mContext) {
            this.mContext = mContext;
            this.list = list;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list == null ? 0 : list.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return list == null ? 0 : list.get(position);
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
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.doubi_item, null);
                holder.img = (ImageView) convertView
                        .findViewById(R.id.doubi_img);
                holder.title = (TextView) convertView
                        .findViewById(R.id.doubi_title);
                holder.time = (TextView) convertView
                        .findViewById(R.id.doubi_time);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (!"".equals(list.get(position).getThumbgengduo())
                    && list.get(position).getThumbgengduo() != null) {
                mDownloader.download(list.get(position).getThumbgengduo(),
                        holder.img, null);
            } else {
                holder.img.setImageResource(R.drawable.ic_launcher);
            }
            holder.title.setText(list.get(position).getDescription());
            holder.time.setText(list.get(position).getInputtime());
            return convertView;
        }
    }

    public class ViewHolder {
        private ImageView img;
        private TextView title, time;
    }

}
