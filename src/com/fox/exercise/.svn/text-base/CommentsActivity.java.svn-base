package com.fox.exercise;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.api.entity.FindMore;
import com.fox.exercise.api.entity.FindMore2;

import java.util.List;

import cn.ingenic.indroidsync.SportsApp;

public class CommentsActivity extends Activity {

    private static final String TAG = "commentsActivity";
    private SportsApp mSportsApp;
    private Button btBack;
    private ListView mListView = null;
    private List<FindMore2> listFM = null;
    private FindMore fm = null;
    private String findId;
    private CommentListAdapter mAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_comments);
        mSportsApp = (SportsApp) this.getApplication();
        mListView = (ListView) this
                .findViewById(R.id.lv_comments_show);
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
                GetFindMoreTask FindMoreTask = new GetFindMoreTask();
                FindMoreTask.execute("");

            }
        };
        mListView.setOnItemClickListener(itemClickListener);
        btBack = (Button) this.findViewById(R.id.layout_letf);
        btBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        GetNewCommnetListTask listTask = new GetNewCommnetListTask();
        listTask.execute("");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 33:
                if (CommentsDetailActivity.act == 100) {
                    for (int i = listFM.size() - 1; i >= 0; i--) {
                        int size = listFM.size();
                        String id = listFM.get(i).findId;
                        if (listFM.get(i).findId
                                .equals(CommentsDetailActivity.mItemFindId)) {
                            listFM.remove(listFM.get(i));
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                    break;
                }

                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    class GetNewCommnetListTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {

            listFM = ApiJsonParser.getNewCommentLists(
                    mSportsApp.getSessionId(), mSportsApp.getSportUser().getUid());
            if (listFM == null)
                return false;
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                mAdapter = new CommentListAdapter(
                        CommentsActivity.this, listFM);
                mListView.setAdapter(mAdapter);
            } else {
                Toast.makeText(CommentsActivity.this, "获取评论信息失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class GetFindMoreTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {

            try {
                fm = ApiJsonParser.getGetFindMore(mSportsApp.getSessionId(), findId);
            } catch (ApiNetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ApiSessionOutException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (fm == null)
                return false;
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                mSportsApp.mFindMore = fm;
                Intent intent = new Intent(CommentsActivity.this, CommentsDetailActivity.class);
                startActivityForResult(intent, 33);
            } else {
                Toast.makeText(CommentsActivity.this, "获取评论详情失败", Toast.LENGTH_SHORT).show();
            }
        }
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
