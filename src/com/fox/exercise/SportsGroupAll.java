package com.fox.exercise;

import java.util.ArrayList;

import android.app.Dialog;
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
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import cn.ingenic.indroidsync.SportsApp;

import com.fox.exercise.view.PullToRefreshBase.OnRefreshListener;
import com.fox.exercise.view.PullToRefreshGridView;

public class SportsGroupAll extends Fragment {
    private SportsApp mSportsApp;
    //	private boolean isFirst = true;
    private PullToRefreshGridView pullToRefreshGridView = null;
    private GridView mGridView = null;
    private Dialog mLoadProgressDialog = null;
    private static final String TAG = "SportsGroupAll";
    private TextView mDialogMessage;
    private EditText search_group_edittext;
    private final int FRESH_LIST = 1;// 更新成功
    private final int FRESH_FAILED = 2;// 更新失败
    private int times = 0;
    private SportsGroupAllAdapter sportsGroupAllAdapter = null;
    private ArrayList<GroupAll> mList = new ArrayList<GroupAll>();
    //搜索框输入的内容
    private String editGroupName;
    //记录上次搜索的长度
    private int edittextCnt = 0;
    //判断搜索框现在的搜索方式
//	private int mSearchGroupALL = 0;
    //搜索框无内容
    private final int SEARCH_BASIC = 0;
    //搜索框有内容
    private final int SEARCH_NAME = 3;
    private SportsGroupAllHandler msportsGroupAllHandler = null;

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
        View view = inflater.inflate(R.layout.sports_group_all, null);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        init();
        mGridView = pullToRefreshGridView.getRefreshableView();
        waitShowDialog();
        //mGridView.addHeaderView(search_view);
        msportsGroupAllHandler = new SportsGroupAllHandler();// 更新
        //首次开启线程
        SportsGroupAllThread sportsGroupAllThread = new SportsGroupAllThread();
        sportsGroupAllThread.start();
        //给搜索框增加监听事件
        search_group_edittext = (EditText) getActivity().findViewById(R.id.sports_groupall_edittext);
        search_group_edittext
                .addTextChangedListener(sports_group_all_editchangelistener);
        //给GrideView增加下拉刷新监听
        pullToRefreshGridView.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh(int pullDirection) {
                switch (pullDirection) {
                    case FansAndNear.MODE_DEFAULT_LOAD:
                        times++;
                        SportsGroupAllThread loadThread = new SportsGroupAllThread();
                        loadThread.start();
                        break;
                    case FansAndNear.MODE_PULL_DOWN_TO_REFRESH:
                        times = 0;
                        SportsGroupAllThread refreshThread = new SportsGroupAllThread();
                        refreshThread.start();
                        break;
                }
            }
        });
    }

    private void init() {
        pullToRefreshGridView = (PullToRefreshGridView) getActivity().findViewById(R.id.sports_groupall_pull_refresh_list);
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

    class SportsGroupAllHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case FRESH_LIST:
                    if (times == 0) {
                        Log.d(TAG, "handleMessage mImgTimes == 0");
                        mGridView.setAdapter(null);
                        sportsGroupAllAdapter = new SportsGroupAllAdapter(
                                getActivity(), mList, mSportsApp);
                        mGridView.setAdapter(sportsGroupAllAdapter);
                        pullToRefreshGridView.onRefreshComplete();
                    } else {
                        if (sportsGroupAllAdapter == null) {
                            sportsGroupAllAdapter = new SportsGroupAllAdapter(
                                    getActivity(), mList, mSportsApp);
                        }
                        sportsGroupAllAdapter.notifyDataSetChanged();
                        pullToRefreshGridView.onRefreshComplete();
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

    class SportsGroupAllThread extends Thread {
//		private long startTime = System.currentTimeMillis();

        @Override
        public void run() {
            // TODO Auto-generated method stub
            super.run();
            Message msg = null;
            ArrayList<GroupAll> list = new ArrayList<GroupAll>();
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
            GroupAll g1 = new GroupAll();
            g1.setId(0);
            g1.setTitlename("慢跑之路1");
            g1.setDetils("跑步,散步1");
            g1.setGoal(0.2f);
            g1.setImg("http://pic15.nipic.com/20110808/1369025_183230168000_2.jpg");
            GroupAll g2 = new GroupAll();
            g2.setId(1);
            g2.setTitlename("慢跑之路2");
            g2.setDetils("跑步,散步2");
            g2.setGoal(0.21f);
            g2.setImg("http://pic15.nipic.com/20110808/1369025_183230168000_2.jpg");
            GroupAll g3 = new GroupAll();
            g3.setId(2);
            g3.setTitlename("慢跑之路3");
            g3.setDetils("跑步,散步3");
            g3.setGoal(0.22f);
            g3.setImg("http://pic15.nipic.com/20110808/1369025_183230168000_2.jpg");
            GroupAll g4 = new GroupAll();
            g4.setId(3);
            g4.setTitlename("慢跑之路4");
            g4.setDetils("跑步,散步4");
            g4.setGoal(0.23f);
            g4.setImg("http://pic15.nipic.com/20110808/1369025_183230168000_2.jpg");
            GroupAll g5 = new GroupAll();
            g5.setId(4);
            g5.setTitlename("慢跑之路5");
            g5.setDetils("跑步,散步5");
            g5.setGoal(0.25f);
            g5.setImg("http://pic15.nipic.com/20110808/1369025_183230168000_2.jpg");
            GroupAll g6 = new GroupAll();
            g1.setId(5);
            g6.setTitlename("慢跑之路6");
            g6.setDetils("跑步,散步6");
            g6.setGoal(0.26f);
            g6.setImg("http://pic15.nipic.com/20110808/1369025_183230168000_2.jpg");
            GroupAll g7 = new GroupAll();
            g7.setId(6);
            g7.setTitlename("慢跑之路7");
            g7.setDetils("跑步,散步7");
            g7.setGoal(0.27f);
            g7.setImg("http://pic15.nipic.com/20110808/1369025_183230168000_2.jpg");
            GroupAll g8 = new GroupAll();
            g8.setId(7);
            g8.setTitlename("慢跑之路8");
            g8.setDetils("跑步,散步8");
            g8.setGoal(0.28f);
            g8.setImg("http://pic15.nipic.com/20110808/1369025_183230168000_2.jpg");
            GroupAll g9 = new GroupAll();
            g9.setId(8);
            g9.setTitlename("慢跑之路9");
            g9.setDetils("跑步,散步9");
            g9.setGoal(0.29f);
            g9.setImg("http://pic15.nipic.com/20110808/1369025_183230168000_2.jpg");
            GroupAll g10 = new GroupAll();
            g10.setId(9);
            g10.setTitlename("慢跑之路10");
            g10.setDetils("跑步,散步10");
            g10.setGoal(0.3f);
            g10.setImg("http://pic15.nipic.com/20110808/1369025_183230168000_2.jpg");
            list.add(g1);
            list.add(g2);
            list.add(g3);
            list.add(g4);
            list.add(g5);
            list.add(g6);
            list.add(g7);
            list.add(g8);
            list.add(g9);
            list.add(g10);
            if (times == 0)
                mList.clear();
            if (list != null) {
                for (GroupAll e : list) {
                    mList.add(e);
                }
                msg = Message.obtain(msportsGroupAllHandler, FRESH_LIST);
                msg.sendToTarget();
            } else {
                if (list == null) {
                    Log.d(TAG, "*******检z4********");
                    msg = Message.obtain(msportsGroupAllHandler, FRESH_FAILED);
                    msg.sendToTarget();
                }
            }
        }

    }

    // 监听edit
    private TextWatcher sports_group_all_editchangelistener = new TextWatcher() {

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
            editGroupName = search_group_edittext.getText().toString();
            int txtCnt = editGroupName.length();
            if (txtCnt != edittextCnt && txtCnt != 0) {
                times = 0;
                edittextCnt = txtCnt;
//				mSearchGroupALL = SEARCH_NAME;
                SportsGroupAllThread search_name_Thread = new SportsGroupAllThread();
                search_name_Thread.start();
            } else if (txtCnt != edittextCnt && txtCnt == 0) {
                times = 0;
                edittextCnt = txtCnt;
//				mSearchGroupALL = SEARCH_BASIC;
                SportsGroupAllThread search_all_Thread = new SportsGroupAllThread();
                search_all_Thread.start();
            }

        }
    };

    //假数据类
    class GroupAll {
        private int id;
        private String img;
        private String titlename;
        private String detils;
        private float goal;

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

        public float getGoal() {
            return goal;
        }

        public void setGoal(float goal) {
            this.goal = goal;
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

}
