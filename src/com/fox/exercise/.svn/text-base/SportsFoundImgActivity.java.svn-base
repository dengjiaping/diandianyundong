package com.fox.exercise;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fox.exercise.widght.MutilTouchImageView;
import com.fox.exercise.widght.MutilTouchScrollLayout;
import com.fox.exercise.widght.MutilTouchScrollLayout.OnViewChangeListener;
import com.umeng.analytics.MobclickAgent;

import cn.ingenic.indroidsync.SportsApp;

public class SportsFoundImgActivity extends AbstractBaseActivity implements OnViewChangeListener {

    private SportsApp mSportsApp;

    private MutilTouchScrollLayout scrollLayout;
    private ImageDownloader mDownloader;
    private LayoutInflater mInflater;
    private String[] urlString;
    private int index;
    private Dialog mLoadProgressDialog;
    private ProgressBar progressBar;
    private TextView mDialogMessage;
    private ImageButton rButton;
    private int urlNum;

    @Override
    public void initIntentParam(Intent intent) {
        // TODO Auto-generated method stub
        urlString = intent.getExtras().getStringArray("urlString");
        index = intent.getExtras().getInt("index");
        urlNum = index;
        title = index + 1 + "/" + urlString.length;
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        showContentView(R.layout.sports_friend_found_img);
        mSportsApp = (SportsApp) getApplication();
        scrollLayout = (MutilTouchScrollLayout) findViewById(R.id.ScrollLayouFound);
        rButton = new ImageButton(this);
        rButton.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        rButton.setBackgroundResource(R.drawable.title_bownload);
        showRightBtn(rButton);
        mDownloader = new ImageDownloader(this);
        mDownloader.setType(ImageDownloader.ICON);
        Intent intent = getIntent();
        Log.i("", "urlString[index]" + urlString[index]);
        for (int i = 0; i < urlString.length; i++) {
            mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.app_item_img, null);
            progressBar = (ProgressBar) view.findViewById(R.id.progressId);
            MutilTouchImageView imageView = (MutilTouchImageView) view.findViewById(R.id.item_img);
            if (i == index) {
                if (mSportsApp.isOpenNetwork()) {
                    mDownloader.download(urlString[i], imageView, progressBar);
                } else {
                    Toast.makeText(SportsFoundImgActivity.this,
                            getResources().getString(R.string.acess_server_error), Toast.LENGTH_SHORT).show();
                }
            }
            scrollLayout.addView(view, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        }
        bool = new boolean[urlString.length];
        for (int i = 0, j = urlString.length; i < j; i++)
            bool[i] = true;
        bool[index] = false;
        scrollLayout.setToScreen(index);
        scrollLayout.SetOnViewChangeListener(this);
    }

    @Override
    public void setViewStatus() {
        // TODO Auto-generated method stub
        rButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                //progressBar.setVisibility(View.GONE);
                DownloadBigPicTask bigPicTask = new DownloadBigPicTask(SportsFoundImgActivity.this, urlString[urlNum]);
                bigPicTask.execute();
                Log.i("", "来了" + urlString[urlNum]);
            }
        });
    }

    @Override
    public void onPageResume() {
        MobclickAgent.onPageStart("SportsFoundImgActivity");
    }

    @Override
    public void onPagePause() {
        MobclickAgent.onPageEnd("SportsFoundImgActivity");
    }

    @Override
    public void onPageDestroy() {
        // TODO Auto-generated method stub
        bool = null;
    }
    //private Button localButton,cityButton;

    private boolean[] bool;

    @Override
    public void OnViewChange(int view, MutilTouchImageView childView) {
        // TODO Auto-generated method stub
        if (bool[view]) {
            mDownloader.download(urlString[view], childView, (ProgressBar) scrollLayout.getChildAt(view).findViewById(R.id.progressId));
            bool[view] = false;
        }
        if (urlNum != view) {
            childView.zoomTo(1f);
            Log.i("", "进来了 露露");
        }
        title = view + 1 + "/" + urlString.length;
        title_tv.setText(title);
        urlNum = view;
    }

}

