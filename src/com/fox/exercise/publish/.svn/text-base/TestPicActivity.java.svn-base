package com.fox.exercise.publish;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.fox.exercise.AbstractBaseActivity;
import com.fox.exercise.R;
import com.umeng.analytics.MobclickAgent;

import java.io.Serializable;
import java.util.List;

public class TestPicActivity extends AbstractBaseActivity {
    private GridView gridView;

    private List<ImageBucket> dataList;
    private ImageBucketAdapter adapter;
    private AlbumHelper helper;
    public static final String EXTRA_IMAGE_LIST = "imagelist";

    public static Bitmap bimap;

    @Override
    public void initIntentParam(Intent intent) {
        title = getResources().getString(R.string.sports_photo) + "";
    }

    @Override
    public void initView() {
        left_ayout.removeAllViews();
        TextView tv_cancle = new TextView(this);
        tv_cancle.setText(getResources().getString(R.string.button_cancel));
        tv_cancle.setTextColor(getResources().getColor(
                R.color.sports_popular_title_normal));
        tv_cancle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        left_ayout.addView(tv_cancle);

        showContentView(R.layout.activity_image_bucket);

        helper = AlbumHelper.getHelper();
        helper.init(getApplicationContext());

        initData();
        initViews();
    }

    @Override
    public void setViewStatus() {
    }

    private void initData() {
        dataList = helper.getImagesBucketList(false);
        bimap = BitmapFactory.decodeResource(getResources(),
                R.drawable.icon_addpic_unfocused);
    }

    private void initViews() {
        gridView = (GridView) findViewById(R.id.gridview);
        adapter = new ImageBucketAdapter(TestPicActivity.this, dataList);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(TestPicActivity.this,
                        ImageGridActivity.class);
                intent.putExtra(TestPicActivity.EXTRA_IMAGE_LIST,
                        (Serializable) dataList.get(position).imageList);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onPageResume() {
        MobclickAgent.onPageStart("TestPicActivity");
    }

    @Override
    public void onPagePause() {
        MobclickAgent.onPageEnd("TestPicActivity");
    }

    @Override
    public void onPageDestroy() {
    }
}
