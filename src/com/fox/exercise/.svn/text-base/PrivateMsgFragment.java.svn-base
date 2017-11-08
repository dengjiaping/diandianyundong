package com.fox.exercise;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.fox.exercise.api.ApiConstant;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.api.WatchService;
import com.fox.exercise.api.entity.NewCommentInfo;
import com.fox.exercise.api.entity.PrivateMsgStatus;
import com.fox.exercise.api.entity.UserDetail;
import com.fox.exercise.api.entity.UserPrimsgAll;
import com.fox.exercise.db.PrimsgDeleteDb;
import com.fox.exercise.db.SportsContent.PrivateMessageAllTable;
import com.fox.exercise.db.SportsContent.PrivateMsgTable;
import com.fox.exercise.http.FunctionStatic;
import com.fox.exercise.newversion.newact.NewCommentsActivity;
import com.fox.exercise.util.AsyncTask;
import com.fox.exercise.view.PullToRefreshBase.OnRefreshListener;
import com.fox.exercise.view.PullToRefreshListView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import cn.ingenic.indroidsync.SportsApp;

public class PrivateMsgFragment extends Fragment implements OnClickListener {

    private static final String TAG = "PrivateMsgFragment";
    private PullToRefreshListView mPullListView = null;
    private ListView mListView = null;
    private PrivateMessageMyAdapter mAdapter = null;
    private Dialog loadProgressDialog = null;

    private Dialog alertDialog;
    private HashSet<UserPrimsgAll> mSet = null;
    private ArrayList<PrivateMsgStatus> mList = null;
    private SportsExceptionHandler mExceptionHandler = null;
    private TextView title_name;
    private GiftsHandler mHandler = null;
    //	private int mTimes = 0;
    private boolean isRefresh = true;
    public static boolean isFlag;
    // private boolean mIsRefresh=false;

    private static final int FRESH_LIST = 0x0001;
    private static final int VISITOR = 2;
    private static final int SPORTSSHOW = 3;
    private static final int SYSMSG = 6;

    private boolean flag = false;
    private int userID = 0;
    // private int total;
    private SportsApp mSportsApp;
    private int currentPosition = 0;
    private boolean needUpdateMsgStatus = false;

    private String mSendName = "";
    private long preTime = 0;
    private PrimsgDeleteDb db;

    private FoxSportsState mFoxSportsState;
    private View view;
    private UserDetail ud = SportsApp.getInstance().getSportUser();
    private Button btn_focusText, btn_focusText1, btn_focusText2;
    private NewCommentInfo commentInfo = new NewCommentInfo();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mSportsApp = (SportsApp) getActivity().getApplication();
        mExceptionHandler = mSportsApp.getmExceptionHandler();
        if (SportsApp.getInstance().getSportUser().getMsgCounts() != null) {
            userID = SportsApp.getInstance().getSportUser().getUid();
        }
        if (mSportsApp.config == 1) {
            db = PrimsgDeleteDb.getInstance(getActivity());
            getActivity().bindService(new Intent(getActivity(), WatchService.class), wConnection,
                    Context.BIND_AUTO_CREATE);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return inflater.inflate(R.layout.privatemsg_fragment, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        mFoxSportsState = (FoxSportsState) getActivity();
        initViews();
        initData();
        //异步加载运动秀未读消息
        SportsShow show = new SportsShow();
        show.execute("");
    }

    private void initData() {
        mList = new ArrayList<PrivateMsgStatus>();
        mHandler = new GiftsHandler();
        GetGiftsThread thread = new GetGiftsThread(userID);
        thread.start();
    }

    private WatchService wService;
    private ServiceConnection wConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            wService = ((WatchService.WBinder) service).getService();
        }

        public void onServiceDisconnected(ComponentName className) {
            wService = null;
        }
    };
    public int page;

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPageEnd("PrivateMsgFragment");
        FunctionStatic.onPause(getActivity(), FunctionStatic.FUNCTION_PRIVATE_MSG_LIST, preTime);
        // mAdapter=null;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        Log.d(TAG, "onResume invoked");
        super.onResume();
        MobclickAgent.onPageStart("PrivateMsgFragment");
        preTime = FunctionStatic.onResume();
        if (needUpdateMsgStatus) {
            updateMessageStatus();
            needUpdateMsgStatus = false;
        }
    }

    private void updateMessageStatus() {
        PrivateMsgStatus status = mSportsApp.getSportsDAO().queryPrivateMsgByUidAndName(
                PrivateMessageAllTable.TABLE_NAME, userID, mSendName);
        Log.v(TAG, "mSendName :" + mSendName);
        Log.v(TAG, "status :" + status);
        mList.get(currentPosition).setMsgStatus(status.getMsgStatus());
        mAdapter.notifyDataSetInvalidated();

    }

    private void initViews() {
        loadProgressDialog = new Dialog(getActivity(), R.style.sports_dialog);
        LayoutInflater mInflater = getActivity().getLayoutInflater();
        view = mInflater.inflate(R.layout.sports_progressdialog, null);
        TextView message = (TextView) view.findViewById(R.id.message);
        message.setText(R.string.sports_wait);
        view.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
        loadProgressDialog.setContentView(view);
        loadProgressDialog.setCanceledOnTouchOutside(false);
        loadProgressDialog.show();

        mPullListView = (PullToRefreshListView) getActivity().findViewById(R.id.gifts_pull_refresh_list);
        mPullListView.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh(int pullDirection) {
                switch (pullDirection) {
                    case FansAndNear.MODE_PULL_DOWN_TO_REFRESH:
                        flag = false;
//					mTimes = 0;
                        isRefresh = true;
                        isFlag = true;
                        GetGiftsThread newGiftsThread = new GetGiftsThread(userID);
                        newGiftsThread.start();
                        break;
                    case FansAndNear.MODE_DEFAULT_LOAD:
                        flag = true;
                        isRefresh = false;
                        isFlag = true;
                        GetGiftsThread loadGiftsThread = new GetGiftsThread(userID);
                        loadGiftsThread.start();
                        break;
                }

            }
        });
        mListView = mPullListView.getRefreshableView();
        Drawable drawable = getResources().getDrawable(R.drawable.sports_bg_line);
        mListView.setDivider(drawable);
        mListView.setDividerHeight(1);
        //设置布局
        view = LayoutInflater.from(getActivity()).inflate(R.layout.message_buju, null);
        mListView.addFooterView(view);
        view.findViewById(R.id.re_sport_show).setOnClickListener(this);
        view.findViewById(R.id.re_fangkemessage).setOnClickListener(this);
        view.findViewById(R.id.re_xitongmessage).setOnClickListener(this);
        btn_focusText = (Button) view.findViewById(R.id.focusText);
        btn_focusText1 = (Button) view.findViewById(R.id.tishi1);
        btn_focusText2 = (Button) view.findViewById(R.id.tishi2);
        XTmessage();
        FKmessage();

    }


    //设置系统消息条数提示
    public void XTmessage() {

        if (ud.getMsgCounts().getSysmsgsports() != 0) {
            btn_focusText1.setVisibility(View.VISIBLE);
            btn_focusText1.setText(ud.getMsgCounts().getSysmsgsports() >= 100 ? "99+" : ud.getMsgCounts().getSysmsgsports() + "");
        }

    }

    //设置访客提示
    public void FKmessage() {
        if (ud.getMsgCounts().getSportVisitor() != 0) {
            btn_focusText2.setVisibility(View.VISIBLE);
            btn_focusText2.setText(ud.getMsgCounts().getSportVisitor() >= 100 ? "99+" : ud.getMsgCounts().getSportVisitor() + "");
        }
    }

    //设置运动秀条数提示
    class SportsShow extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            commentInfo = ApiJsonParser.getNewCommentCount(mSportsApp
                    .getSessionId(), mSportsApp.getSportUser().getUid());
            if (commentInfo == null)
                return false;
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                if (commentInfo.commentCount > 0) {
                    btn_focusText.setVisibility(View.VISIBLE);
                    btn_focusText.setText(commentInfo.commentCount >= 100 ? "99+" : commentInfo.commentCount + "");
                } else {
                    btn_focusText.setVisibility(View.GONE);
                }
            } else {

                Log.e(TAG, "更新未读评论信息失败");
            }
        }
        //        if () {
//            btn_focusText.setVisibility(View.VISIBLE);
//        } else {
//            btn_focusText.setVisibility(View.GONE);
//        }
    }

    class GetGiftsThread extends Thread {
        private int uid = 0;

        public GetGiftsThread(int userID) {
            this.uid = userID;
        }

        @Override
        public void run() {
            List<PrivateMsgStatus> privateMsgStatusList = new ArrayList<PrivateMsgStatus>();
            List<UserPrimsgAll> privateMsgAllList = new ArrayList<UserPrimsgAll>();
            List<UserPrimsgAll> privateMsgAll = new ArrayList<UserPrimsgAll>();
            try {
                for (int i = 0; i <= page; i++) {
                    privateMsgAllList = (ArrayList<UserPrimsgAll>) ApiJsonParser.getPrimsgAll(mSportsApp.getSessionId(),
                            i);
                    Log.e("privateMsgAllListONE:", privateMsgAllList.toString());
                    privateMsgAll.addAll(privateMsgAllList);
                    if (privateMsgAllList.size() > 0) {
                        page++;
                    }
                }
                if (SportsApp.getInstance().getSportUser().getMsgCounts() != null) {
                    SportsApp.getInstance().getSportUser().getMsgCounts().setPrimsg(0);
                }
            } catch (ApiNetException e) {
                e.printStackTrace();
                SportsApp.eMsg = Message.obtain(mExceptionHandler, SportsExceptionHandler.NET_ERROR);
                SportsApp.eMsg.sendToTarget();
            } catch (ApiSessionOutException e) {
                e.printStackTrace();
                SportsApp.eMsg = Message.obtain(mExceptionHandler, SportsExceptionHandler.SESSION_OUT);
                SportsApp.eMsg.sendToTarget();
//				startActivity(new Intent(getActivity(), LoginActivity.class));
            }
            if (privateMsgAll.size() != 0) {
                PrivateMsgStatus privateMsgStatus = null;
                for (UserPrimsgAll userPrimsgAll : privateMsgAll) {
                    privateMsgStatus = new PrivateMsgStatus();
                    privateMsgStatus.setUserPrimsgAll(userPrimsgAll);
                    privateMsgStatus.setMsgStatus("unread");
                    privateMsgStatusList.add(privateMsgStatus);
                }
                mSportsApp.getSportsDAO().insertPrivateMsgAll(PrivateMessageAllTable.TABLE_NAME, privateMsgAll,
                        "unread");
            }
            Log.e("hdwid", userID + "");
            List<PrivateMsgStatus> localList = mSportsApp.getSportsDAO().queryPrivateMsgAll(
                    PrivateMessageAllTable.TABLE_NAME, userID);
            if (isRefresh == true) {
                if (mList != null) {
                    mList.clear();
                }
            }
            if (flag) {
//				for (PrivateMsgStatus privateMsgStatus : privateMsgStatusList) {
//					mList.add(privateMsgStatus);
//				}
            } else {
                for (PrivateMsgStatus privateMsgStatus : localList) {
                    Log.d(TAG, "localList:" + privateMsgStatus);
                    mList.add(privateMsgStatus);
                }
            }
            Message msg = null;
            msg = Message.obtain(mHandler, FRESH_LIST);
            msg.sendToTarget();
        }
    }

    class GiftsHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FRESH_LIST:
                    if (mAdapter == null) {
//                        if (mList != null && mList.size() > 0) {
//                            mFoxSportsState.setViewVisible();
//                        }
                        mAdapter = new PrivateMessageMyAdapter(mList, getActivity());
                        mListView.setAdapter(mAdapter);
                        mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

                            @Override
                            public boolean onItemLongClick(AdapterView<?> adapterView, View arg1, int position, long arg3) {
                                // TODO Auto-generated method stub
                                if (!isFlag) {
                                    if (mList != null && position < mList.size()) {
                                        UserPrimsgAll userPrimsgAll = ((PrivateMsgStatus) adapterView
                                                .getItemAtPosition(position)).getUserPrimsgAll();
                                        showDialogs(getActivity(), userPrimsgAll, position);
                                    }
                                }
                                return true;
                            }
                        });
                        mListView.setOnItemClickListener(new OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                // TODO Auto-generated method stub
                                needUpdateMsgStatus = true;
                                currentPosition = position;
                                if (!isFlag) {
                                    if (mList != null && position < mList.size()) {
                                        UserPrimsgAll userPrimsgAll = ((PrivateMsgStatus) adapterView
                                                .getItemAtPosition(position)).getUserPrimsgAll();
                                        userPrimsgAll.setCounts(0);
                                        mSportsApp.getSportsDAO().insertPrivateMsg(PrivateMessageAllTable.TABLE_NAME,
                                                userPrimsgAll, "read");
                                        mList.get(currentPosition).setMsgStatus("read");
                                        mList.get(currentPosition).getUserPrimsgAll().setCounts(0);
                                        Intent privateMsgIntent = new Intent(getActivity(),
                                                SportsPersonalMsg.class);
                                        privateMsgIntent.putExtra("uid", userPrimsgAll.getUid());
                                        privateMsgIntent.putExtra("touid", userPrimsgAll.getTouid());
                                        privateMsgIntent.putExtra("senderIcon", userPrimsgAll.getUimg());
                                        privateMsgIntent.putExtra("receiverIcon", userPrimsgAll.getTouimg());
                                        privateMsgIntent.putExtra("senderName", userPrimsgAll.getName());
                                        privateMsgIntent.putExtra("birthday", userPrimsgAll.getBirthday());
                                        privateMsgIntent.putExtra("sex", userPrimsgAll.getSex());
                                        mSendName = userPrimsgAll.getName();
                                        Log.d(TAG, "senderIcon:" + userPrimsgAll.getUimg());
                                        Log.d(TAG, "receiverIcon:" + userPrimsgAll.getTouimg());
                                        Log.d(TAG, "uid:" + userPrimsgAll.getUid());
                                        Log.d(TAG, "touid:" + userPrimsgAll.getTouid());
                                        Log.d(TAG, "senderName:" + userPrimsgAll.getName());
                                        Log.d(TAG, "sex:" + userPrimsgAll.getSex());
                                        startActivity(privateMsgIntent);
                                    }
                                }
                            }
                        });
                    } else {
                        mAdapter.notifyDataSetChanged();
                    }
                    if (isRefresh == true) {
                        mAdapter.notifyDataSetChanged();
                        if (mList != null && 0 < mList.size()) {
                            Handler pHandler = mSportsApp.getFriendsHandler();
                            if (pHandler != null) {
                                pHandler.sendMessage(pHandler.obtainMessage(
                                        ApiConstant.CLEAR_PRI_MSG));
                            }
                            Handler sHandler = mSportsApp.getMainHandler();
                            if (sHandler != null) {
                                sHandler.sendMessage(sHandler.obtainMessage(
                                        ApiConstant.CLEAR_PRI_MSG));
                            }
                        }
                    }
                    if (loadProgressDialog != null)
                        if (loadProgressDialog.isShowing()) {
                            loadProgressDialog.dismiss();
                        }
                    mPullListView.onRefreshComplete();
                    isFlag = false;
                    break;
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

		/*case R.id.bt_clear:
            showDialog(getActivity());
			break;*/
            case R.id.bt_cancel:
                alertDialog.dismiss();
                break;
            case R.id.re_sport_show:
                gotoSportsShow();
                break;
            case R.id.re_xitongmessage:

                gotoSysmsg();
                break;
            case R.id.re_fangkemessage:
                gotoVisitor();
                break;
        }
    }


    /**
     * 系统消息
     */
    private void gotoSysmsg() {
        Intent privateIntent = new Intent(getActivity(), SysMessageMyActivity.class);
        startActivityForResult(privateIntent, SYSMSG);
    }


    /**
     * 访客
     */
    private void gotoVisitor() {
        startActivityForResult((new Intent(getActivity(), VisitorMyActivity.class)), VISITOR);
    }

    /**
     * 运动秀
     */

    private void gotoSportsShow() {
        Intent intent = new Intent(getActivity(), NewCommentsActivity.class);
        intent.putExtra("bs", 111);
        startActivityForResult(intent, SPORTSSHOW);
    }

    private void showDialogs(Context con, final UserPrimsgAll userPrimsgAll, final int position) {
        alertDialog = new Dialog(con, R.style.sports_dialog);
        LayoutInflater mInflater = getActivity().getLayoutInflater();
        View v = mInflater.inflate(R.layout.sports_dialog, null);
        v.findViewById(R.id.bt_ok).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mSportsApp.getSportsDAO().deletePrivateMsgAllByUID(PrivateMessageAllTable.TABLE_NAME,
                        userPrimsgAll.getUid());
                mSportsApp.getSportsDAO().deletePrivateMsgByUID(PrivateMsgTable.TABLE_NAME,
                        mSportsApp.getSportUser().getUid(), userPrimsgAll.getUid());
                mList.remove(position);
                mAdapter.notifyDataSetChanged();
                alertDialog.dismiss();
                if (wService != null) {
                    if (wService.isDelete(userPrimsgAll.getName())) {
                        ContentValues cv = new ContentValues();
                        cv.put("name", userPrimsgAll.getName());
                        db.insert(cv);
                    }
                }

//                if (mList.size() == 0) {
//                    mFoxSportsState.setGone();
//                }
            }
        });
        v.findViewById(R.id.bt_cancel).setOnClickListener(this);
        TextView message = (TextView) v.findViewById(R.id.message);
        message.setText("确认要删除和" + userPrimsgAll.getName() + "私信记录?");
        v.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setContentView(v);
        alertDialog.show();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (mSportsApp.config == 1) {
            getActivity().unbindService(wConnection);
            db.close();
        }
        mList = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SPORTSSHOW:
                commentInfo.commentCount = 0;
                btn_focusText.setVisibility(View.GONE);
                break;
            case SYSMSG:
                ud.getMsgCounts().setSysmsgsports(0);
                btn_focusText1.setVisibility(View.GONE);
                break;
            case VISITOR:
                ud.getMsgCounts().setSportVisitor(0);
                btn_focusText2.setVisibility(View.GONE);
                break;
        }
    }
}
