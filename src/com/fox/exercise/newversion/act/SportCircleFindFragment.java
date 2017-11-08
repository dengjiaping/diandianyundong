package com.fox.exercise.newversion.act;

import java.util.ArrayList;
import java.util.List;

import cn.ingenic.indroidsync.SportsApp;

import com.fox.exercise.FansAndNear;
import com.fox.exercise.ImageDownloader;
import com.fox.exercise.MsgboxAdapter;
import com.fox.exercise.R;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.bitmap.util.ImageResizer;
import com.fox.exercise.newversion.entity.CircleFinds;
import com.fox.exercise.newversion.entity.CircleFindsAd;
import com.fox.exercise.newversion.entity.CircleFindsCat;
import com.fox.exercise.newversion.entity.CircleFindsLists;
import com.fox.exercise.view.PullToRefreshListView;
import com.fox.exercise.view.PullToRefreshBase.OnRefreshListener;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author loujungang 运动圈发现页面
 */
public class SportCircleFindFragment extends Fragment {
    private static final String TAG = "SportCircleFindFragment";
    private View view;
    private GridView mGridView;
    int width, height;
    private ArrayList<Integer> al;
    private MsgboxAdapter ma;
    private Dialog mLoadProgressDialog = null;
    private TextView mDialogMessage;
    private SportsApp mSportsApp;
    private int times = 0;
    private PullToRefreshListView mPullSearchListView = null;
    private ListView mListView = null;
    private View head_view;
    private ImageView find_head_icon;
    public boolean isFirst = false;
    private final int FRESH_LIST = 111;// 更新成功
    private final int FRESH_FAILED = 112;// 更新失败
    private final int FRESH_DONE = 113;
    private final int FRESH_NULL = 114;

    private List<CircleFindsAd> adList = new ArrayList<CircleFindsAd>();
    private List<CircleFindsCat> calist = new ArrayList<CircleFindsCat>();
    private List<CircleFindsLists> lists = new ArrayList<CircleFindsLists>();

    private SportsFindMoreHandler msportsFindMoreHandler = null;
    private MyAdapter adapter;
    private Context mContext;
    // 图片正方形
    private ImageResizer mImageWorker;
    private ImageDownloader mDownloader2 = null;
    private MyListAdapter myListAdapter;
    private int bg_width;
    private int bg_height;
    private int screen_width;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view = inflater.inflate(R.layout.sport_circle_find_frg, null);
        initView();
        return view;
    }

    private void initView() {
        mSportsApp = (SportsApp) getActivity().getApplication();
        mContext = getActivity();
        bg_width = 480;
        bg_height = 200;
        mDownloader2 = new ImageDownloader(mContext);
        mDownloader2.setType(ImageDownloader.OnlyOne);
        mPullSearchListView = (PullToRefreshListView) view
                .findViewById(R.id.sportscircle_find_refresh_list);
        mListView = mPullSearchListView.getRefreshableView();
        myListAdapter = new MyListAdapter();
        head_view = LayoutInflater.from(getActivity()).inflate(
                R.layout.sport_circle_findheadview, null);
        screen_width = (int) (SportsApp.ScreenWidth);
        find_head_icon = (ImageView) head_view
                .findViewById(R.id.find_head_icon);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) find_head_icon
                .getLayoutParams();
        lp.width = screen_width;
        lp.height = (int) ((screen_width * bg_height) / bg_width);
        find_head_icon.setLayoutParams(lp);
        WindowManager wm = getActivity().getWindowManager();
        width = wm.getDefaultDisplay().getWidth();
        mImageWorker = mSportsApp.getImageWorker(mContext, width, 0);
        width = width / 2;
        height = wm.getDefaultDisplay().getHeight();
        al = new ArrayList<Integer>();
        mGridView = (GridView) head_view.findViewById(R.id.myfind_gridview);
        adapter = new MyAdapter(getActivity());
        mGridView.setAdapter(adapter);
        mListView.addHeaderView(head_view);
        mListView.setAdapter(myListAdapter);
        msportsFindMoreHandler = new SportsFindMoreHandler();
        mPullSearchListView.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh(int pullDirection) {
                if (mSportsApp.isOpenNetwork()) {
                    switch (pullDirection) {
                        case FansAndNear.MODE_DEFAULT_LOAD:
                            // times++;
                            times = 0;
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
                            getActivity(),
                            getResources().getString(
                                    R.string.acess_server_error),
                            Toast.LENGTH_SHORT).show();
                    mPullSearchListView.onRefreshComplete();
                    if (mLoadProgressDialog != null)
                        if (mLoadProgressDialog.isShowing())
                            mLoadProgressDialog.dismiss();
                }

            }
        });

        // mListView.setDivider(null);
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long id) {
                // TODO Auto-generated method stub
                if (id == -1) {
                    return;
                }
                int realPosition = (int) id;
                arg1.setSelected(true);
                Intent intent = new Intent(mContext,
                        FindPointsSayActivity.class);
                intent.putExtra("CircleFindsLists", lists.get(realPosition));
                startActivity(intent);
            }
        });

        // 初始化dialog
        if (mLoadProgressDialog == null) {
            mLoadProgressDialog = new Dialog(mContext, R.style.sports_dialog);
            LayoutInflater mInflater = getActivity().getLayoutInflater();
            View v1 = mInflater.inflate(R.layout.bestgirl_progressdialog, null);
            mDialogMessage = (TextView) v1.findViewById(R.id.message);
            mDialogMessage.setText(R.string.bestgirl_wait);
            v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
            mLoadProgressDialog.setContentView(v1);
        }

    }

    // 自定义适配器
    class MyAdapter extends BaseAdapter {
        // 上下文对象
        private Context context;

        MyAdapter(Context context) {
            this.context = context;
        }

        public int getCount() {
            return calist.size();
        }

        public Object getItem(int item) {
            return calist.get(item);
        }

        public long getItemId(int id) {
            return id;
        }

        // 创建View方法
        @SuppressLint("ResourceAsColor")
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            Button button;
            if (convertView == null) {
                button = new Button(context);
                button.setLayoutParams(new GridView.LayoutParams(width - 20,
                        android.widget.GridLayout.LayoutParams.WRAP_CONTENT));// 设置ImageView对象布局
                button.setTextColor(R.color.black);
                button.setBackgroundResource(R.drawable.stroke_nocicle_gray_background);
                button.setGravity(Gravity.CENTER);
                // button.setPadding(5, 5, 5, 5);// 设置间距
            } else {
                button = (Button) convertView;
            }
            button.setText(calist.get(position).getTitle());
            button.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    Intent activityIntent = new Intent(getActivity(),
                            SportCircleFindEventActivity.class);
                    activityIntent.putExtra("id", calist.get(position).getId());
                    activityIntent.putExtra("title", calist.get(position)
                            .getTitle());
                    startActivity(activityIntent);
                }
            });
            return button;
        }
    }

    private void waitShowDialog() {
        // TODO Auto-generated method stub
        if (mLoadProgressDialog == null) {
            mLoadProgressDialog = new Dialog(mContext, R.style.sports_dialog);
            LayoutInflater mInflater = getActivity().getLayoutInflater();
            View v1 = mInflater.inflate(R.layout.bestgirl_progressdialog, null);
            mDialogMessage = (TextView) v1.findViewById(R.id.message);
            mDialogMessage.setText(R.string.bestgirl_wait);
            v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
            mLoadProgressDialog.setContentView(v1);
        }
        if (mLoadProgressDialog != null)
            if (!mLoadProgressDialog.isShowing()
                    && !getActivity().isFinishing())
                mLoadProgressDialog.show();
    }

    class SportsFindMoreThread extends Thread {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            super.run();
            Message msg = null;
            CircleFinds circleFinds = null;

            try {
                circleFinds = ApiJsonParser.getCircleFindLists(mSportsApp
                        .getSessionId(), times, mSportsApp.getSportUser()
                        .getUid(), 1);
            } catch (ApiNetException e) {
                e.printStackTrace();
            } catch (ApiSessionOutException e) {
                e.printStackTrace();
            }
            // if (times == 0) {
            // adList.clear();
            // calist.clear();
            // lists.clear();
            // }
            if (circleFinds != null) {
                if (adList.size() == circleFinds.getAdList().size()
                        && lists.size() == circleFinds.getLists().size()) {
                    msg = Message.obtain(msportsFindMoreHandler, FRESH_NULL);
                    msg.sendToTarget();
                } else {
                    msg = Message.obtain(msportsFindMoreHandler, FRESH_LIST);
                    msg.obj = circleFinds;
                    msg.sendToTarget();
                }
            } else {
                Log.d(TAG, "*******检z4********");
                msg = Message.obtain(msportsFindMoreHandler, FRESH_FAILED);
                msg.sendToTarget();
            }
        }

    }

    /**
     * 开始加载数据
     */
    public void loadDate() {
        waitShowDialog();
        // 起线程
        SportsFindMoreThread sportsFindMoreThread = new SportsFindMoreThread();
        if (mSportsApp.isOpenNetwork()) {
            if (mSportsApp.getSessionId() != null
                    && !mSportsApp.getSessionId().equals("")) {
                sportsFindMoreThread.start();
            }
        } else {
            Toast.makeText(getActivity(),
                    getResources().getString(R.string.acess_server_error),
                    Toast.LENGTH_SHORT).show();

            mPullSearchListView.onRefreshComplete();
            if (mLoadProgressDialog != null)
                if (mLoadProgressDialog.isShowing())
                    mLoadProgressDialog.dismiss();
        }

        isFirst = true;
    }

    class SportsFindMoreHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case FRESH_LIST:
                    if (times == 0) {
                        CircleFinds circleFinds = (CircleFinds) msg.obj;
                        adList = circleFinds.getAdList();
                        calist = circleFinds.getCalist();
                        lists = circleFinds.getLists();
                        // mImageWorker.loadImage(adList.get(0).getImg(),
                        // find_head_icon, null, null, false);
                        mDownloader2.download(adList.get(0).getImg(),
                                find_head_icon, null);
                        adapter.notifyDataSetChanged();
                        myListAdapter.notifyDataSetChanged();
                        mPullSearchListView.onRefreshComplete();
                    } else {
                        mPullSearchListView.onRefreshComplete();
                    }
                    if (mLoadProgressDialog != null)
                        if (mLoadProgressDialog.isShowing())
                            mLoadProgressDialog.dismiss();
                    break;

                case FRESH_FAILED:
                    if (!mSportsApp.isOpenNetwork()) {
                        Toast.makeText(
                                getActivity(),
                                getResources().getString(
                                        R.string.acess_server_error),
                                Toast.LENGTH_SHORT).show();
                    } else if (getActivity() != null) {
                        Toast.makeText(
                                getActivity(),
                                getActivity().getResources().getString(
                                        R.string.sports_get_list_failed),
                                Toast.LENGTH_SHORT).show();
                    }
                    if (mLoadProgressDialog != null)
                        if (mLoadProgressDialog.isShowing())
                            mLoadProgressDialog.dismiss();
                    // sportsfindmoreAdapter.notifyDataSetChanged();
                    mPullSearchListView.onRefreshComplete();
                    break;
                case FRESH_NULL:
                    if (getActivity() != null) {
                        if (adList.size() != 0) {
                            Toast.makeText(
                                    getActivity(),
                                    getActivity().getResources().getString(
                                            R.string.sports_data_load_more_null),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(
                                    getActivity(),
                                    getActivity().getResources().getString(
                                            R.string.sports_upload_find_new),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (mLoadProgressDialog != null)
                        if (mLoadProgressDialog.isShowing())
                            mLoadProgressDialog.dismiss();
                    mPullSearchListView.onRefreshComplete();
                    break;
                default:
                    break;
            }
        }

    }

    class MyListAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        public MyListAdapter() {
            inflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return lists.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return lists.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.circle_find_listitem,
                        null);
                holder = new ViewHolder();
                holder.sport_type_icon = (ImageView) convertView
                        .findViewById(R.id.sport_type_icon);
                holder.sport_type_txt = (TextView) convertView
                        .findViewById(R.id.sport_type_txt);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            mImageWorker.loadImage(lists.get(position).getImg(),
                    holder.sport_type_icon, null, null, false);
            holder.sport_type_txt.setText(lists.get(position).getTitle());
            return convertView;
        }

    }

    class ViewHolder {
        private ImageView sport_type_icon;
        private TextView sport_type_txt;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        mSportsApp = null;
    }

}
