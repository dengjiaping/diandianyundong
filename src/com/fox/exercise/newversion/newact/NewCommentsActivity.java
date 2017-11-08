package com.fox.exercise.newversion.newact;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fox.exercise.CommentListAdapter;
import com.fox.exercise.CommentsDetailActivity;
import com.fox.exercise.FansAndNear;
import com.fox.exercise.R;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.entity.ExpressionItems;
import com.fox.exercise.api.entity.FindMore2;
import com.fox.exercise.db.SportShowDb;
import com.fox.exercise.newversion.act.UserActivityMainActivity;
import com.fox.exercise.newversion.entity.FindGroup;
import com.fox.exercise.view.PullToRefreshBase;
import com.fox.exercise.view.PullToRefreshListView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cn.ingenic.indroidsync.SportsApp;

/**
 * @author loujungang  评论消息界面
 */
public class NewCommentsActivity extends Activity implements PopupWindow.OnDismissListener {

    //	private static final String TAG = "commentsActivity";
    private SportsApp mSportsApp;
    private Button btBack, btn_clear;
    private ListView mListView = null;
    private List<FindMore2> listFM = null;
    FindMore2 findMore2 = new FindMore2();
    private FindGroup fm = null;
    private String findId, inputtime;
    private CommentListAdapter mAdapter = null;
    private PullToRefreshListView mPullSearchListView = null;
    SportShowDb db;
    Intent intent;
    int bs;
    NewCommebtHandler handler = null;
    private PopupWindow myWindow = null;
    private LinearLayout myView;
    private RelativeLayout mPopMenuBack, mainLL;
    private Dialog mLoadProgressDialog = null;
    TextView text_ts;
    //初始化一个表情包
    public static List<ExpressionItems> imgItems;
    private String[] imgStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_comments);
        intent = getIntent();
        bs = intent.getIntExtra("bs", -1);
        mSportsApp = (SportsApp) this.getApplication();
        db = SportShowDb.getmInstance(NewCommentsActivity.this);
        handler = new NewCommebtHandler();
        mPullSearchListView = (PullToRefreshListView) this
                .findViewById(R.id.lv_comments_show);
        mainLL = (RelativeLayout) findViewById(R.id.re_sport);
        text_ts = (TextView) findViewById(R.id.text_ts);
        mListView = mPullSearchListView.getRefreshableView();
        // 设置列表中的某一项的点击事件
        OnItemClickListener itemClickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (listFM == null)
                    return;
                // 查询到当前的findID
                findId = listFM.get(position).findId;
                Log.e("yushuijing", "findId = " + findId);
                //异步任务
//				GetFindMoreTask FindMoreTask = new GetFindMoreTask();
//				FindMoreTask.execute("");

                Intent activityIntent = new Intent(NewCommentsActivity.this,
                        UserActivityMainActivity.class);
                activityIntent.putExtra("findId", Integer.parseInt(findId));
                startActivity(activityIntent);

            }
        };
        mPullSearchListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh(int pullDirection) {
                switch (pullDirection) {
                    case FansAndNear.MODE_DEFAULT_LOAD:
                        page++;
                        List<FindMore2> list = new ArrayList<FindMore2>();
                        list = db.getContent(page);
                        if (list != null && list.size() > 0&&listFM!=null) {
                            for (FindMore2 findMore2 : list) {
                                listFM.add(findMore2);
                            }

                        }
                        handler.sendEmptyMessage(11);

                        break;
                    case FansAndNear.MODE_PULL_DOWN_TO_REFRESH:
                        page = 0;
                        GetNewCommnetListTask getNewCommnetListTask = new GetNewCommnetListTask();
                        getNewCommnetListTask.execute("");
//                        List<FindMore2> listFM1 = new ArrayList<FindMore2>();
//                        listFM1 = db.getContent(page);
//                        if (listFM1 != null && listFM1.size() > 0) {
//                            for (FindMore2 ff : listFM1) {
//                                listFM.add(ff);
//                            }
//                        }
//                        handler.sendEmptyMessage(12);
                        break;
                }
            }
        });
        mListView.setOnItemClickListener(itemClickListener);
        btBack = (Button) this.findViewById(R.id.layout_letf);
        btn_clear = (Button) findViewById(R.id.layout_right);
        btBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        //清空
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitPopWindow(getString(R.string.sport_clear));

            }
        });
        GetNewCommnetListTask listTask = new GetNewCommnetListTask();
        listTask.execute("");
        waitShowDialog();//加载框

        //初始化一个表情包，用于显示消息页面
        imgItems = new ArrayList<ExpressionItems>();
        imgStr = getResources().getStringArray(R.array.imageStr_array);
        int j = 0;
        Field[] files = R.drawable.class.getDeclaredFields();
        for (Field file : files) {
            if (file.getName().startsWith("biaoqing_")) {
                if (((imgItems.size() + 1) % 21) == 0) {
                    ExpressionItems item = new ExpressionItems();
                    item.setId(R.drawable.qita_biaoqing_01);
                    item.setName("itemCanel");
                    item.setIsCanel(true);
                    imgItems.add(item);
                }
                ApplicationInfo appInfo = getApplicationInfo();
                int resID = getResources().getIdentifier(file.getName(),
                        "drawable", appInfo.packageName);
                ExpressionItems item = new ExpressionItems();
                item.setId(resID);
                item.setName(imgStr[j].toString());
                item.setIsCanel(false);
                imgItems.add(item);
                j++;
            }
        }
        ExpressionItems item = new ExpressionItems();
        item.setId(R.drawable.qita_biaoqing_01);
        item.setName("itemCanel");
        item.setIsCanel(true);
        imgItems.add(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 33:
                if (CommentsDetailActivity.act == 100) {
                    if(listFM!=null){
                        for (int i = listFM.size() - 1; i >= 0; i--) {
                            int size = listFM.size();
                            String id = listFM.get(i).findId;
                            if (listFM.get(i).findId
                                    .equals(CommentsDetailActivity.mItemFindId)) {
                                listFM.remove(listFM.get(i));
                            }
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                    break;
                }

                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDismiss() {
//        mPopMenuBack.setVisibility(View.GONE);
    }

    class NewCommebtHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 12:
//                    if (mAdapter == null) {
//                        mAdapter = new CommentListAdapter(NewCommentsActivity.this, listFM);
//                        mListView.setAdapter(mAdapter);
//                    } else {
//                        mAdapter.notifyDataSetChanged();
//                    }
//                    mPullSearchListView.onRefreshComplete();
                    break;
                case 11:
                    if (listFM != null && listFM.size() > 0) {
                        text_ts.setVisibility(View.GONE);
                        if (mAdapter == null) {
                            mAdapter = new CommentListAdapter(
                                    NewCommentsActivity.this, listFM, imgItems);
                            mListView.setAdapter(mAdapter);
                        } else {
                            mAdapter.notifyDataSetChanged();
                        }
                    } else {
                        text_ts.setVisibility(View.VISIBLE);
                    }

                    mPullSearchListView.onRefreshComplete();
                    break;
                case 13:
                    if (listFM != null && listFM.size() > 0) {
                        text_ts.setVisibility(View.GONE);
                        mAdapter = new CommentListAdapter(
                                NewCommentsActivity.this, listFM, imgItems);
                        mListView.setAdapter(mAdapter);
                    } else {
                        text_ts.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
    }

    private int page = 0;

    class GetNewCommnetListTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {

            listFM = ApiJsonParser.getNewCommentLists(
                    mSportsApp.getSessionId(), mSportsApp.getSportUser().getUid());

            if (listFM != null && listFM.size() > 0)
                return true;
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if (result) {
                if (mLoadProgressDialog != null)
                    if (mLoadProgressDialog.isShowing())
                        mLoadProgressDialog.dismiss();
                if(listFM!=null){
                    for (FindMore2 ff : listFM) {

                        InsertData(ff);
                    }
                }

                if (bs == 111) {
                    List<FindMore2> listFM1 = new ArrayList<FindMore2>();
                    listFM1 = db.getContent(page);
                    if(listFM!=null){
                        if (page == 0) {
                            listFM.clear();
                        }
                        for (FindMore2 findMore2 : listFM1) {
                            listFM.add(findMore2);
                        }
                    }
                    handler.sendEmptyMessage(13);

                } else {
                    if(listFM==null){
                        listFM=new ArrayList<FindMore2>();
                    }
                    text_ts.setVisibility(View.GONE);
                    mAdapter = new CommentListAdapter(
                            NewCommentsActivity.this, listFM, imgItems);
                    mListView.setAdapter(mAdapter);
                }

            } else {
                if (mLoadProgressDialog != null)
                    if (mLoadProgressDialog.isShowing())
                        mLoadProgressDialog.dismiss();
                if (bs == 111) {
                    listFM = new ArrayList<FindMore2>();
                    listFM = db.getContent(page);
                    if (listFM != null && listFM.size() > 0) {
                        text_ts.setVisibility(View.GONE);
                        mAdapter = new CommentListAdapter(
                                NewCommentsActivity.this, listFM, imgItems);
                        mListView.setAdapter(mAdapter);
                    } else {
                        text_ts.setVisibility(View.VISIBLE);
                    }

                } else {
                    Toast.makeText(NewCommentsActivity.this, "获取评论信息失败", Toast.LENGTH_SHORT).show();
                }
            }
            mPullSearchListView.onRefreshComplete();
        }
    }

    /**
     * @param
     * @return 添加数据到数据库
     */
    private int InsertData(FindMore2 list) {
        int savarusult = 0;
        ContentValues values = new ContentValues();

        values.put(SportShowDb.TYPE, list.getType());
        values.put(SportShowDb.IMG, list.getPicPath());
        values.put(SportShowDb.FINDID, list.getFindId());
        values.put(SportShowDb.COMMENTID, list.getCommentId());
        values.put(SportShowDb.UID, list.getuId());
        values.put(SportShowDb.CONTENT, list.getContent());
        values.put(SportShowDb.ADDRESS, list.getWavAddress());
        values.put(SportShowDb.WAVTIME, list.getWavTime());
        values.put(SportShowDb.INPUTTIME, list.getInputTime());
        values.put(SportShowDb.NAME, list.getName());
        values.put(SportShowDb.USERIMG, list.getUserImg());
        values.put(SportShowDb.TIME, list.getTime());
        values.put(SportShowDb.IS_DELETE, list.getIs_delete());
        Cursor c;
        if (list.iszanOrPing) {
            c = db.query(list.getCommentId(), true);

        } else {
            c = db.query(list.getuId(), false);
        }
        if (c != null) {
            if (!c.moveToFirst()) {
                savarusult = db.insert(values, false);
            }
        }
        if (c != null) {
            c.close();
            c = null;
        }
        return savarusult;

    }

    private void exitPopWindow(String message) {
        if (myWindow != null && myWindow.isShowing())
            return;
        LayoutInflater inflater = LayoutInflater.from(this);
        myView = (LinearLayout) inflater.inflate(R.layout.sports_dialog1, null);
        ((TextView) myView.findViewById(R.id.message)).setText(message);

        myView.findViewById(R.id.bt_ok).setOnClickListener(new ExitClick());
        myView.findViewById(R.id.bt_cancel).setOnClickListener(new ExitClick());
        // myEditCalories
        // .setText(String.valueOf(getDate.getInt("editCalories", 0)));
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        // int height = wm.getDefaultDisplay().getHeight();
        myWindow = new PopupWindow(myView, width - SportsApp.dip2px(20),
                RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        myWindow.setAnimationStyle(R.style.AnimationPopup);
        myWindow.setOutsideTouchable(true);
        myWindow.setBackgroundDrawable(new BitmapDrawable());
//		myWindow.setBackgroundDrawable(null);
        myWindow.showAtLocation(mainLL, Gravity.CENTER, 0, 0);
        myWindow.setOnDismissListener(this);
        // final Animation animation = (Animation) AnimationUtils.loadAnimation(
        // this, R.anim.slide_in_from_bottom);
        // myView.startAnimation(animation);
//        mPopMenuBack.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        mSportsApp = null;
//        if (db != null) {
//            db.close();
//            db = null;
//        }
    }

    class ExitClick implements android.view.View.OnClickListener {

        @Override
        public void onClick(View view) {
            // TODO Auto-generated method stub
            switch (view.getId()) {
                case R.id.bt_ok:
                    myWindow.dismiss();
                    myView.setVisibility(View.GONE);
                    int i = db.delete();
                    if (i > 0) {
                        if (listFM != null) {
                            listFM.clear();
                            text_ts.setVisibility(View.VISIBLE);
                            if (mAdapter != null) {
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                    break;
                case R.id.bt_cancel:
                    if (myWindow != null) {
                        myWindow.dismiss();
                    }
                    myView.setVisibility(View.GONE);
//                    mPopMenuBack.setVisibility(View.GONE);

                    break;
                default:
                    break;
            }
        }

    }

    //加载框
    private void waitShowDialog() {
        // TODO Auto-generated method stub
        if (mLoadProgressDialog == null) {
            mLoadProgressDialog = new Dialog(this,
                    R.style.sports_dialog);
            LayoutInflater mInflater = this.getLayoutInflater();
            View v1 = mInflater.inflate(R.layout.bestgirl_progressdialog, null);
            TextView mDialogMessage = (TextView) v1.findViewById(R.id.message);
            mDialogMessage.setText(R.string.bestgirl_wait);
            v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
            mLoadProgressDialog.setContentView(v1);
        }
        if (mLoadProgressDialog != null)
            if (!mLoadProgressDialog.isShowing()
                    && !this.isFinishing())
                mLoadProgressDialog.show();
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
