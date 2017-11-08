package com.fox.exercise.publish;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.fox.exercise.AbstractBaseActivity;
import com.fox.exercise.FindFriendsSendMsg;
import com.fox.exercise.R;
import com.fox.exercise.publish.ImageGridAdapter.TextCallback;
import com.umeng.analytics.MobclickAgent;

public class ImageGridActivity extends AbstractBaseActivity {
    public static final String EXTRA_IMAGE_LIST = "imagelist";

    private List<ImageItem> dataList;
    private GridView gridView;
    private ImageGridAdapter adapter;
    private AlbumHelper helper;
    TextView tv_send;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
//				Toast.makeText(ImageGridActivity.this,
//						getResources().getString(R.string.select_images_nine),
//						400).show();
                    Toast.makeText(ImageGridActivity.this,
                            getResources().getString(R.string.select_images_four),
                            400).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void initIntentParam(Intent intent) {
        title = getResources().getString(R.string.sports_photo);
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
        left_ayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intentFromGallery = new Intent(ImageGridActivity.this,
                        TestPicActivity.class);
                startActivity(intentFromGallery);
                finish();
            }

        });

        tv_send = new TextView(this);
        tv_send.setText(getResources().getString(R.string.sports_upload_finish));
        tv_send.setTextColor(getResources().getColor(
                R.color.sports_popular_title_normal));
        tv_send.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        showRightBtn(tv_send);
        tv_send.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ArrayList<String> list = new ArrayList<String>();
                Collection<String> c = adapter.map.values();
                Iterator<String> it = c.iterator();
                for (; it.hasNext(); ) {
                    list.add(it.next());
                }

                if (Bimp.act_bool) {
                    Bimp.act_bool = false;
                }
                for (int i = 0; i < list.size(); i++) {
                    if (Bimp.drr.size() < 9) {
                        Bimp.drr.add(list.get(i));
                    }
                }
                finish();
            }

        });

        showContentView(R.layout.activity_image_grid);

        helper = AlbumHelper.getHelper();
        helper.init(getApplicationContext());

        dataList = (List<ImageItem>) getIntent().getSerializableExtra(
                EXTRA_IMAGE_LIST);

        initViews();
    }

    @Override
    public void setViewStatus() {
    }

    private void initViews() {
        gridView = (GridView) findViewById(R.id.gridview);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new ImageGridAdapter(ImageGridActivity.this, dataList,
                mHandler);
        gridView.setAdapter(adapter);
        adapter.setTextCallback(new TextCallback() {
            public void onListen(int count) {
                if (tv_send != null) {
                    tv_send.setText(getResources().getString(
                            R.string.sports_upload_finish)
                            + "(" + count + ")");
                }
            }
        });

        gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                adapter.notifyDataSetChanged();
            }

        });

    }

    @Override
    public void onPageResume() {
        MobclickAgent.onPageStart("ImageGridActivity");
    }

    @Override
    public void onPagePause() {
        MobclickAgent.onPageEnd("ImageGridActivity");
    }

    @Override
    public void onPageDestroy() {
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Intent intentFromGallery = new Intent(ImageGridActivity.this,
                        TestPicActivity.class);
                startActivity(intentFromGallery);
                finish();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
