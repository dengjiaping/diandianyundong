package com.fox.exercise;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.entity.Help;

import java.util.ArrayList;
import java.util.List;
//import com.umeng.analytics.MobclickAgent;

public class SportsHelpInfo extends Activity implements OnClickListener {
    private static final String TAG = "SportsHelpInfo";
    //	private SportsApp mSportsApp;
    private List<Help> helpInfoList = null;
    private InfoListAdapter mInfoAdapter;
    private Context mContext;
    private ListView helpInfoListView;
//	private Dialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean findMethod = SmartBarUtils.findActionBarTabsShowAtBottom();
        if (!findMethod) {
            // 取消ActionBar拆分，换用TabHost
            getWindow().setUiOptions(0);
            getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        setContentView(R.layout.sports_help_info);
        mContext = this;
//		mSportsApp = (SportsApp) getApplication();
        helpInfoListView = (ListView) findViewById(R.id.list_help_info);
        mInfoAdapter = new InfoListAdapter(mContext);

        if (findMethod) {
            getActionBar().setDisplayShowHomeEnabled(false);
            getActionBar().setDisplayShowTitleEnabled(false);
            SmartBarUtils.setActionModeHeaderHidden(getActionBar(), true);
            SmartBarUtils.setActionBarViewCollapsable(getActionBar(), true);
        }
        helpInfoListView.setAdapter(mInfoAdapter);
        helpInfoListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                mInfoAdapter.toggle(position);
            }

        });
        findViewById(R.id.bt_back).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
//		MobclickAgent.onResume(mContext);
        initData();
    }

    @Override
    protected void onPause() {
        super.onPause();
//		MobclickAgent.onPause(mContext);
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy invoked");
        super.onDestroy();
    }

    private void initData() {
        if (helpInfoList != null) {
            helpInfoList.clear();
            mInfoAdapter.clear();
        }

        new AsyncTask<Void, Void, List<Help>>() {

            @Override
            protected List<Help> doInBackground(Void... params) {
                try {
                    helpInfoList = ApiJsonParser.getHelp();
                } catch (ApiNetException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return helpInfoList;
            }

            @Override
            protected void onPostExecute(List<Help> helpInfoList) {
//				Log.d(TAG, "helpInfoList:"+helpInfoList.size());
                if (helpInfoList != null) {
                    for (Help help : helpInfoList) {
                        mInfoAdapter.addItem(help);
                    }
                    mInfoAdapter.updateStatus();
                    mInfoAdapter.notifyDataSetChanged();
                }
            }
        }.execute();

    }

    static class HelpInfoListHolder {
        public TextView infoTextView;
        public TextView contentTextView;
    }


    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.bt_back:
                finish();
                break;
        }
    }

    private class InfoListAdapter extends BaseAdapter {
        private List<Help> mHelpInfoList = new ArrayList<Help>();
        private boolean[] mExpanded;
        private Context mContext;

        public InfoListAdapter(Context context) {
            mContext = context;
        }

        public int getCount() {
            return mHelpInfoList.size();
        }

        public void clear() {
            this.mHelpInfoList.clear();
        }

        public void addItem(Help helpInfo) {
            this.mHelpInfoList.add(helpInfo);
        }

        public void updateStatus() {
            mExpanded = new boolean[getCount()];
            for (int i = 0; i < getCount(); i++) {
                mExpanded[i] = false;
            }
        }

        public Help getItem(int position) {
            return mHelpInfoList.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            Help helpInfoItem = mHelpInfoList.get(position);
            InfoView sv;
            if (convertView == null) {
                sv = new InfoView(mContext, helpInfoItem.getTitle(), helpInfoItem.getMsg(), mExpanded[position]);
            } else {
                sv = (InfoView) convertView;
                sv.setTitle(helpInfoItem.getTitle());
                sv.setDialogue(helpInfoItem.getMsg());
                sv.setExpanded(mExpanded[position]);
            }
            return sv;
        }

        public void toggle(int position) {
            mExpanded[position] = !mExpanded[position];
            notifyDataSetChanged();
        }

    }

    private class InfoView extends LinearLayout {
        public InfoView(Context context, String title, String dialogue, boolean expanded) {
            super(context);

            this.setOrientation(VERTICAL);
            this.setMinimumHeight(60);
            this.setGravity(Gravity.CENTER_VERTICAL);
            this.setPadding(15, 10, 15, 10);

            // Here we build the child views in code. They could also have
            // been specified in an XML file.

            mTitle = new TextView(context);
            mTitle.setTextSize(16);
            mTitle.setTextColor(context.getResources().getColor(R.color.sports_help_info));
            mTitle.setText(title);
            mTitle.setPadding(0, 0, 0, 10);
            addView(mTitle, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

            mContent = new TextView(context);
            mContent.setText(dialogue);
            mContent.setTextSize(15);
            mContent.setTextColor(Color.BLACK);
            addView(mContent, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

            mContent.setVisibility(expanded ? VISIBLE : GONE);
        }

        /**
         * Convenience method to set the title of a SpeechView
         */
        public void setTitle(String title) {
            mTitle.setText(title);
        }

        /**
         * Convenience method to set the dialogue of a SpeechView
         */
        public void setDialogue(String words) {
            mContent.setText(words);
        }

        /**
         * Convenience method to expand or hide the dialogue
         */
        public void setExpanded(boolean expanded) {
            mContent.setVisibility(expanded ? VISIBLE : GONE);
        }

        private TextView mTitle;
        private TextView mContent;
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
