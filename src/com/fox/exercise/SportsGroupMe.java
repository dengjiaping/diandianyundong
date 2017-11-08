package com.fox.exercise;


import java.util.ArrayList;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import cn.ingenic.indroidsync.SportsApp;

import com.fox.exercise.view.PullToRefreshBase.OnRefreshListener;
import com.fox.exercise.view.PullToRefreshListView;

public class SportsGroupMe extends Fragment {
    private SportsApp mSportsApp;
    private boolean isFirst = true;
    private PullToRefreshListView mPullSearchListView = null;
    private ListView mListView = null;
    private Dialog mLoadProgressDialog = null;
    private static final String TAG = "SportsGroupAll";
    private TextView mDialogMessage;
    private EditText search_mygroup_edittext;
    private final int FRESH_LIST = 1;// 更新成功
    private final int FRESH_FAILED = 2;// 更新失败
    private int times = 0;
    private SportsGroupMeAdapter sportsGroupMeAdapter = null;
    private ArrayList<GroupMe> mList = new ArrayList<GroupMe>();
    //搜索框输入的内容
    private String editGroupName;
    //记录上次搜索的长度
    private int edittextCnt = 0;
    //判断搜索框现在的搜索方式
//	private int mSearchGroupALL = 0;
    //搜索框无内容
    private final int SEARCH_BASIC = 0;
    //搜索框有内容
    private final int SEARCH_NAME = 1;
    private SportsGroupMeHandler msportsGroupMeHandler = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mSportsApp = (SportsApp) getActivity().getApplication();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return inflater.inflate(R.layout.sports_group_me, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // TODO Auto-generated method stub
        super.setUserVisibleHint(isVisibleToUser);
        // 每次切换fragment时调用的方法
        if (isVisibleToUser && isFirst) {
            init();
            mListView = mPullSearchListView.getRefreshableView();
            Drawable drawable = getResources().getDrawable(
                    R.drawable.sports_bg_line);
            waitShowDialog();
            mListView.setDivider(drawable);
            mListView.setDividerHeight(1);
            View search_view = LayoutInflater.from(getActivity()).inflate(
                    R.layout.friends_list_front_view, null);
            search_mygroup_edittext = (EditText) search_view.findViewById(
                    R.id.add_friend_edittext);
            mListView.addHeaderView(search_view);
            msportsGroupMeHandler = new SportsGroupMeHandler();// 更新
            // 起线程
            SportsGroupMeThread sportsGroupMeThread = new SportsGroupMeThread();
            sportsGroupMeThread.start();
            search_mygroup_edittext
                    .addTextChangedListener(sports_group_me_editchangelistener);

            mPullSearchListView.setOnRefreshListener(new OnRefreshListener() {

                @Override
                public void onRefresh(int pullDirection) {
                    switch (pullDirection) {
                        case FansAndNear.MODE_DEFAULT_LOAD:
                            times++;
                            SportsGroupMeThread loadThread = new SportsGroupMeThread();
                            loadThread.start();
                            break;
                        case FansAndNear.MODE_PULL_DOWN_TO_REFRESH:
                            times = 0;
                            SportsGroupMeThread refreshThread = new SportsGroupMeThread();
                            refreshThread.start();
                            break;
                    }
                }
            });
            isFirst = false;
        }
    }

    private void init() {
        // TODO Auto-generated method stub
        mPullSearchListView = (PullToRefreshListView) getActivity()
                .findViewById(R.id.sports_group_me_pull_refresh_list);
    }

    private void waitShowDialog() {
        // TODO Auto-generated method stub
        if (mLoadProgressDialog == null) {
            mLoadProgressDialog = new Dialog(getActivity(),
                    R.style.sports_dialog);
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
        Log.i(TAG, "isFirstshow----");
    }

    class SportsGroupMeHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case FRESH_LIST:
                    if (times == 0) {
                        Log.d(TAG, "handleMessage mImgTimes == 0");
                        mListView.setAdapter(null);
                        sportsGroupMeAdapter = new SportsGroupMeAdapter(
                                getActivity(), mList, mSportsApp);
                        mListView.setAdapter(sportsGroupMeAdapter);
                        mPullSearchListView.onRefreshComplete();
                    } else {
                        if (sportsGroupMeAdapter == null) {
                            sportsGroupMeAdapter = new SportsGroupMeAdapter(
                                    getActivity(), mList, mSportsApp);
                        }
                        sportsGroupMeAdapter.notifyDataSetChanged();
                        mPullSearchListView.onRefreshComplete();
                    }
                    if (mLoadProgressDialog != null)
                        if (mLoadProgressDialog.isShowing())
                            mLoadProgressDialog.dismiss();
                    break;

                case FRESH_FAILED:
                    if (getActivity() != null)
                        Toast.makeText(getActivity(), "获取列表失败！", Toast.LENGTH_SHORT)
                                .show();
                    if (mLoadProgressDialog != null)
                        if (mLoadProgressDialog.isShowing())
                            mLoadProgressDialog.dismiss();
                    break;

                default:
                    break;
            }
        }
    }

    // 监听edit
    private TextWatcher sports_group_me_editchangelistener = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                  int arg3) {
            // TODO Auto-generated method stub
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
            // TODO Auto-generated method stub
        }

        @Override
        public void afterTextChanged(Editable arg0) {
            // TODO Auto-generated method stub
            editGroupName = search_mygroup_edittext.getText().toString();
            int txtCnt = editGroupName.length();
            if (txtCnt != edittextCnt && txtCnt != 0) {
                times = 0;
                edittextCnt = txtCnt;
//					mSearchGroupALL = SEARCH_NAME;
                SportsGroupMeThread search_name_Thread = new SportsGroupMeThread();
                search_name_Thread.start();
            } else if (txtCnt != edittextCnt && txtCnt == 0) {
                times = 0;
                edittextCnt = txtCnt;
//					mSearchGroupALL = SEARCH_BASIC;
                SportsGroupMeThread search_all_Thread = new SportsGroupMeThread();
                search_all_Thread.start();
            }

        }
    };

    class SportsGroupMeThread extends Thread {
//			private long startTime = System.currentTimeMillis();

        @Override
        public void run() {
            // TODO Auto-generated method stub
            super.run();
            Message msg = null;
            ArrayList<GroupMe> list = new ArrayList<GroupMe>();
                /*if (mSearchGroupALL == SEARCH_BASIC) {
					try {
						list = (ArrayList<UserNearby>) ApiJsonParser.getFriendList(
								times, mSportsApp.getSessionId());
					} catch (ApiNetException e) {
						e.printStackTrace();
					} catch (ApiSessionOutException e) {
						e.printStackTrace();
					}
				} else if (mSearchGroupALL == SEARCH_NAME) {
					try {
						list = (ArrayList<UserNearby>) ApiJsonParser
								.getFriendbyNameNew(times, editname,
										mSportsApp.getSessionId());
					} catch (ApiNetException e) {
						e.printStackTrace();
					} catch (ApiSessionOutException e) {
						e.printStackTrace();
					}
				}*/
            GroupMe g1 = new GroupMe();
            g1.setId(0);
            g1.setTitlename("慢跑之路");
            g1.setDetils("跑步,游泳");
            g1.setImg("http://pic10.nipic.com/20100929/2846024_134344005927_2.jpg");
            GroupMe g2 = new GroupMe();
            g2.setId(1);
            g2.setTitlename("慢跑之路2");
            g2.setDetils("跑步,游泳2");
            g2.setImg("http://pic10.nipic.com/20100929/2846024_134344005927_2.jpg");
            GroupMe g3 = new GroupMe();
            g3.setId(2);
            g3.setTitlename("慢跑之路3");
            g3.setDetils("跑步,游泳3");
            g3.setImg("http://pic10.nipic.com/20100929/2846024_134344005927_2.jpg");
            GroupMe g4 = new GroupMe();
            g4.setId(3);
            g4.setTitlename("慢跑之路4");
            g4.setDetils("跑步,游泳4");
            g4.setImg("http://pic10.nipic.com/20100929/2846024_134344005927_2.jpg");
            GroupMe g5 = new GroupMe();
            g5.setId(4);
            g5.setTitlename("慢跑之路5");
            g5.setDetils("跑步,游泳5");
            g5.setImg("http://pic10.nipic.com/20100929/2846024_134344005927_2.jpg");
            GroupMe g6 = new GroupMe();
            g6.setId(5);
            g6.setTitlename("慢跑之路6");
            g6.setDetils("跑步,游泳6");
            g6.setImg("http://pic10.nipic.com/20100929/2846024_134344005927_2.jpg");
            GroupMe g7 = new GroupMe();
            g7.setId(6);
            g7.setTitlename("慢跑之路7");
            g7.setDetils("跑步,游泳7");
            g7.setImg("http://pic10.nipic.com/20100929/2846024_134344005927_2.jpg");
            list.add(g1);
            list.add(g2);
            list.add(g3);
            list.add(g4);
            list.add(g5);
            list.add(g6);
            list.add(g7);
            if (times == 0)
                mList.clear();
            if (list != null) {
                for (GroupMe e : list) {
                    mList.add(e);
                }
                msg = Message.obtain(msportsGroupMeHandler, FRESH_LIST);
                msg.sendToTarget();
            } else {
                if (list == null) {
                    Log.d(TAG, "*******检z4********");
                    msg = Message.obtain(msportsGroupMeHandler, FRESH_FAILED);
                    msg.sendToTarget();
                }
            }
        }

    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (mList != null) {
            mList.clear();
            mList = null;
        }
    }

    //假数据类
    class GroupMe {
        private int id;
        private String img;
        private String titlename;
        private String detils;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getTitlename() {
            return titlename;
        }

        public void setTitlename(String titlename) {
            this.titlename = titlename;
        }

        public String getDetils() {
            return detils;
        }

        public void setDetils(String detils) {
            this.detils = detils;
        }
    }
}


