package com.fox.exercise.newversion.trainingplan;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import cn.ingenic.indroidsync.SportsApp;

import com.fox.exercise.AbstractBaseActivity;
import com.fox.exercise.R;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiMessage;
import com.fox.exercise.newversion.entity.TrainAction;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class TrainingActionActivity extends AbstractBaseActivity {

    private VideoView vv_video;
    private TextView tv_action_name, tv_action_info, tv_current_action, tv_total_action;
    private String trainActionUrl, filePath, picPath, downLoadPath, fileName;
    private int fileSize;
    private String dir = Environment.getExternalStorageDirectory().toString() + "/android/data/"
            + SportsApp.getContext().getPackageName() + "/cache/video/trainAction/";
    private Button btn_train_task_list, btn_before, btn_next;
    private ArrayList<TrainAction> actionList;
    private int current_action;
    private ImageButton iv_star1, iv_star2, iv_star3, iv_star4, iv_star5;
    private boolean downloading;

    private class GetTrainActionInfo extends AsyncTask<Void, Void, ApiMessage> {

        @Override
        protected ApiMessage doInBackground(Void... Params) {
            if (!SportsApp.getInstance().isOpenNetwork()) {
                Toast.makeText(TrainingActionActivity.this, getString(R.string.network_not_avaliable),
                        Toast.LENGTH_SHORT).show();
                finish();
                return null;
            }

            downloading = true;

            return ApiJsonParser.getTrainActionInfo(SportsApp.getInstance().getSessionId(),
                    actionList.get(current_action).getId());
        }

        @Override
        protected void onPostExecute(ApiMessage result) {
            super.onPostExecute(result);

            if ((result == null) || (!result.isFlag())) {
                if (!SportsApp.getInstance().isOpenNetwork()) {
                    Toast.makeText(TrainingActionActivity.this, getString(R.string.network_not_avaliable),
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }

            try {
                if(result.getMsg()!=null&&!"".equals(result.getMsg())){
                    JSONObject obj = new JSONObject(result.getMsg()).getJSONObject("data");
                    tv_action_name.setText(obj.getString("action_name"));
                    picPath = obj.getString("thumb");
                    setStarSel(obj.getInt("grade"));
                    tv_action_info.setText(Html.fromHtml(obj.getString("train_info")));
                    trainActionUrl = obj.getString("train_fileurl");
                    filePath = dir + trainActionUrl.substring(trainActionUrl.lastIndexOf("/"), trainActionUrl.length());
                    File f = new File(filePath);
                    if (f.exists()) {
                        ViewGroup.LayoutParams params = vv_video.getLayoutParams();
                        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        vv_video.setLayoutParams(params);
                        vv_video.setVideoPath(filePath);
                        vv_video.start();
                        downloading = false;
                    } else {
                        if (SportsApp.getInstance().isOpenNetwork()) {
                            downLoadPath = "video/trainAction/";
                            fileName = trainActionUrl.substring(trainActionUrl.lastIndexOf("/") + 1, trainActionUrl.length());

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    //另起线程执行下载，安卓最新sdk规范，网络操作不能再主线程。
                                    FileDownload l = new FileDownload(trainActionUrl);
                                    fileSize = l.getLength();

                                    /**
                                     * 下载文件到sd卡，虚拟设备必须要开始设置sd卡容量
                                     * downhandler是Download的内部类，作为回调接口实时显示下载数据
                                     */
                                    int status = l.down2sd(downLoadPath, fileName, l.new downhandler() {
                                        @Override
                                        public void setSize(int size) {
                                            Message msg = downloadHandler.obtainMessage();
                                            msg.arg1 = size;
                                            msg.sendToTarget();
                                        }
                                    });
                                }
                            }).start();
                        } else {
                            Toast.makeText(TrainingActionActivity.this, getString(R.string.network_not_avaliable),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    final Handler downloadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            fileSize -= msg.arg1;

            if (fileSize <= 0) {
                ViewGroup.LayoutParams params = vv_video.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                vv_video.setLayoutParams(params);
                vv_video.setVideoPath(filePath);
                vv_video.start();

                downloading = false;
            }
        }
    };

    @Override
    public void initIntentParam(Intent intent) {
        // TODO Auto-generated method stub
        if (intent != null) {
            actionList = (ArrayList<TrainAction>) intent.getSerializableExtra("action_list");
            current_action = intent.getIntExtra("arg2", 0);
            title = "动作示范";
        }
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        showContentView(R.layout.activity_training_action);
        tv_current_action = (TextView) this.findViewById(R.id.tv_current_action);
        tv_current_action.setText((current_action + 1) + "");

        tv_total_action = (TextView) this.findViewById(R.id.tv_total_action);
        tv_total_action.setText(actionList.size() + "");

        btn_before = (Button) this.findViewById(R.id.btn_before);
        btn_before.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (downloading) {
                    return;
                }

                vv_video.stopPlayback();

                if (current_action == 0) {
                    return;
                } else {
                    current_action--;
                    tv_current_action.setText((current_action + 1) + "");

                    if (SportsApp.getInstance().isOpenNetwork()) {
                        new GetTrainActionInfo().execute();
                    } else {
                        Toast.makeText(TrainingActionActivity.this, getString(R.string.network_not_avaliable),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        btn_next = (Button) this.findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (downloading) {
                    return;
                }

                vv_video.stopPlayback();

                if (current_action == actionList.size() - 1) {
                    return;
                } else {
                    current_action++;
                    tv_current_action.setText((current_action + 1) + "");

                    if (SportsApp.getInstance().isOpenNetwork()) {
                        new GetTrainActionInfo().execute();
                    } else {
                        Toast.makeText(TrainingActionActivity.this, getString(R.string.network_not_avaliable),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btn_train_task_list = (Button) this.findViewById(R.id.btn_train_task_list);
        btn_train_task_list.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(TrainingActionActivity.this, TrainTaskListActivity.class);
                startActivity(intent);
            }
        });

        vv_video = (VideoView) this.findViewById(R.id.vv_video);
        vv_video.setOnCompletionListener(new OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                // TODO Auto-generated method stub
                vv_video.setVideoPath(filePath);
                vv_video.start();
            }
        });
        tv_action_name = (TextView) this.findViewById(R.id.tv_action_name);
        tv_action_info = (TextView) this.findViewById(R.id.tv_action_info);
        iv_star1 = (ImageButton) this.findViewById(R.id.iv_star1);
        iv_star2 = (ImageButton) this.findViewById(R.id.iv_star2);
        iv_star3 = (ImageButton) this.findViewById(R.id.iv_star3);
        iv_star4 = (ImageButton) this.findViewById(R.id.iv_star4);
        iv_star5 = (ImageButton) this.findViewById(R.id.iv_star5);

        if (SportsApp.getInstance().isOpenNetwork()) {
            new GetTrainActionInfo().execute();
        } else {
            Toast.makeText(TrainingActionActivity.this, getString(R.string.network_not_avaliable),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void setStarSel(int grade) {
        switch (grade) {
            case 1:
                iv_star1.setBackgroundResource(R.drawable.star_sel);
                iv_star2.setBackgroundResource(R.drawable.star_unsel);
                iv_star3.setBackgroundResource(R.drawable.star_unsel);
                iv_star4.setBackgroundResource(R.drawable.star_unsel);
                iv_star5.setBackgroundResource(R.drawable.star_unsel);
                break;
            case 2:
                iv_star1.setBackgroundResource(R.drawable.star_sel);
                iv_star2.setBackgroundResource(R.drawable.star_sel);
                iv_star3.setBackgroundResource(R.drawable.star_unsel);
                iv_star4.setBackgroundResource(R.drawable.star_unsel);
                iv_star5.setBackgroundResource(R.drawable.star_unsel);
                break;
            case 3:
                iv_star1.setBackgroundResource(R.drawable.star_sel);
                iv_star2.setBackgroundResource(R.drawable.star_sel);
                iv_star3.setBackgroundResource(R.drawable.star_sel);
                iv_star4.setBackgroundResource(R.drawable.star_unsel);
                iv_star5.setBackgroundResource(R.drawable.star_unsel);
                break;
            case 4:
                iv_star1.setBackgroundResource(R.drawable.star_sel);
                iv_star2.setBackgroundResource(R.drawable.star_sel);
                iv_star3.setBackgroundResource(R.drawable.star_sel);
                iv_star4.setBackgroundResource(R.drawable.star_sel);
                iv_star5.setBackgroundResource(R.drawable.star_unsel);
                break;
            case 5:
                iv_star1.setBackgroundResource(R.drawable.star_sel);
                iv_star2.setBackgroundResource(R.drawable.star_sel);
                iv_star3.setBackgroundResource(R.drawable.star_sel);
                iv_star4.setBackgroundResource(R.drawable.star_sel);
                iv_star5.setBackgroundResource(R.drawable.star_sel);
                break;
            default:
                iv_star1.setBackgroundResource(R.drawable.star_unsel);
                iv_star2.setBackgroundResource(R.drawable.star_unsel);
                iv_star3.setBackgroundResource(R.drawable.star_unsel);
                iv_star4.setBackgroundResource(R.drawable.star_unsel);
                iv_star5.setBackgroundResource(R.drawable.star_unsel);
                break;
        }
    }

    @Override
    public void setViewStatus() {
        // TODO Auto-generated method stub
        Log.e("develop_debug", "setViewStatus");
    }

    @Override
    public void onPageResume() {
        // TODO Auto-generated method stub
        Log.e("develop_debug", "onPageResume");
        vv_video.start();
    }

    @Override
    public void onPagePause() {
        // TODO Auto-generated method stub
        Log.e("develop_debug", "onPagePause");
        vv_video.pause();
    }

    @Override
    public void onPageDestroy() {
        // TODO Auto-generated method stub
        Log.e("develop_debug", "onPageDestroy");
        vv_video.stopPlayback();
        if (actionList != null) {
            actionList.clear();
            actionList = null;
        }

    }
}
