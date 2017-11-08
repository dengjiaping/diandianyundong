package com.fox.exercise.newversion.trainingplan;

import java.util.ArrayList;

import cn.ingenic.indroidsync.SportsApp;

import com.fox.exercise.ImageDownloader;
import com.fox.exercise.R;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiMessage;
import com.fox.exercise.pedometer.SportContionTaskDetail;
import com.fox.exercise.util.SportTaskUtil;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TrainTaskListAdapter extends BaseAdapter {

    private ArrayList<TrainTaskListDetail> mList = null;
    private Context mContext = null;
    private LayoutInflater mInflater = null;
    private static final int SAVE_START = 1;
    private static final int SAVE_COMPLETE = 2;
    private int pos;

    public TrainTaskListAdapter(ArrayList<TrainTaskListDetail> list, Context context) {
        mContext = context;
        mList = list;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    public void clearList() {
        mList.clear();
    }

    public void addItem(TrainTaskListDetail um) {
        mList.add(um);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        RelativeLayout layout = null;
        if (convertView == null) {
            layout = (RelativeLayout) mInflater.inflate(R.layout.adapter_get_train_action_list, null);
        } else {
            layout = (RelativeLayout) convertView;
        }

        TextView tv_list_date = (TextView) layout.findViewById(R.id.tv_list_date);
        TextView tv_list_time = (TextView) layout.findViewById(R.id.tv_list_time);
        TextView tv_list_name = (TextView) layout.findViewById(R.id.tv_list_name);
        TextView tv_list_shichang_value = (TextView) layout.findViewById(R.id.tv_list_shichang_value);
        TextView tv_list_xiaohao_value = (TextView) layout.findViewById(R.id.tv_list_xiaohao_value);
        final TextView tv_list_tip = (TextView) layout.findViewById(R.id.tv_list_tip);

        final ImageButton tv_list_tipicon = (ImageButton) layout.findViewById(R.id.tv_list_tipicon);
        tv_list_tipicon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (SportsApp.getInstance().isOpenNetwork()) {
                    WindowManager.LayoutParams lp = ((TrainTaskListActivity)mContext).getWindow().getAttributes();
                    lp.alpha = 0.3f;
                    ((TrainTaskListActivity)mContext).getWindow().setAttributes(lp);

                    final Dialog dialog = new Dialog(mContext, R.style.sports_dialog1);
                    LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                    View v1 = inflater.inflate(R.layout.sport_dialog_for_newtask, null);
                    v1.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
                    TextView msg = (TextView) v1.findViewById(R.id.message);
                    msg.setText("是否要上传训练记录?");
                    Button button = (Button) v1.findViewById(R.id.bt_ok);
                    button.setText("确定");
                    dialog.setContentView(v1);
                    button.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            dialog.dismiss();
                            tv_list_tip.setVisibility(View.GONE);
                            tv_list_tipicon.setVisibility(View.GONE);
                            uploadTrainTask(mList.get(position));
                            pos = position;
                        }
                    });
                    v1.findViewById(R.id.bt_cancel).setOnClickListener(
                            new OnClickListener() {
                                @Override
                                public void onClick(View arg0) {
                                    dialog.dismiss();
                                }
                            });
                    dialog.setCancelable(true);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            // TODO Auto-generated method stub
                            WindowManager.LayoutParams lp = ((TrainTaskListActivity)mContext).getWindow().getAttributes();
                            lp.alpha = 1f;
                            ((TrainTaskListActivity)mContext).getWindow().setAttributes(lp);
                        }
                    });
                    dialog.show();
                } else {
                    Toast.makeText(mContext, "无网络，请检查网络连接后重试", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (mList.get(position).getIs_upload() == 0) {
            tv_list_tip.setVisibility(View.VISIBLE);
            tv_list_tipicon.setVisibility(View.VISIBLE);
        } else {
            tv_list_tip.setVisibility(View.GONE);
            tv_list_tipicon.setVisibility(View.GONE);
        }

        tv_list_time.setText(mList.get(position).getTrain_starttime().subSequence(11, 16));
        tv_list_name.setText(mList.get(position).getTrain_position());
        if (mList.get(position).getTraintime() / 60 == 0) {
            tv_list_shichang_value.setText(mList.get(position).getTraintime() + "秒");
        } else {
            tv_list_shichang_value.setText(Integer.toString(mList.get(position).getTraintime() / 60) + "分钟");
        }
        tv_list_xiaohao_value.setText(SportTaskUtil.getDoubleOneNum(mList.get(position).getTrain_calorie()) + "Cal");
        int month = Integer.valueOf(mList.get(position).getTrain_starttime().substring(5, 7));
        int day = Integer.valueOf(mList.get(position).getTrain_starttime().substring(8, 10));
        tv_list_date.setText(month + "/" + day);
        return layout;
    }

    private void uploadTrainTask(final TrainTaskListDetail detail) {

        new Thread() {
            @Override
            public void run() {
                if (detail==null||(detail.getTrain_endtime() == null) || (detail.getTrain_endtime().equalsIgnoreCase(""))) {
                    return;
                }
                ApiMessage msg = ApiJsonParser.addTrainRecord(SportsApp.getInstance().getSessionId(),
                        detail.getTrain_id(), detail.getTraintime(),
                        detail.getTrain_calorie(), detail.getTrain_action(),
                        detail.getTrain_position(), detail.getTrain_completion(),
                        detail.getTrain_starttime(), detail.getTrain_endtime(),
                        detail.getIs_total(), detail.getUnique_id());

                Message msgfail = Message.obtain();
                msgfail.what = SAVE_START;
                msgfail.obj = msg.isFlag();
                sportHandler.sendMessage(msgfail);
            }
        }.start();
    }

    private void saveTrainTask(final TrainTaskListDetail detail) {
        new Thread() {
            @Override
            public void run() {
                int saveResult = 0;

                TrainPlanDataBase db = TrainPlanDataBase.getInstance(mContext);

                ContentValues values = new ContentValues();
                values.put(TrainPlanDataBase.UID_I, detail.getUid());
                values.put(TrainPlanDataBase.TRAIN_ID_I, detail.getTrain_id());
                values.put(TrainPlanDataBase.TRAIN_TIME_I, detail.getTraintime());
                values.put(TrainPlanDataBase.TRAIN_CALORIE_D, detail.getTrain_calorie());
                values.put(TrainPlanDataBase.TRAIN_ACTION_S, detail.getTrain_action());
                values.put(TrainPlanDataBase.TRAIN_POSITION_S, detail.getTrain_position());
                values.put(TrainPlanDataBase.TRAIN_COMPLETION_I, detail.getTrain_completion());
                values.put(TrainPlanDataBase.TRAIN_STARTTIME_S, detail.getTrain_starttime());
                values.put(TrainPlanDataBase.TRAIN_ENDTIME_S, detail.getTrain_endtime());
                values.put(TrainPlanDataBase.IS_TOTAL_I, detail.getIs_total());
                values.put(TrainPlanDataBase.IS_UPLOAD_I, 1);
                values.put(TrainPlanDataBase.TRAIN_MARKCODE, detail.getUnique_id());
                saveResult = db.update(values, detail.getUid(), detail.getTrain_starttime(),
                        false, detail.getUnique_id());
                db.close();

                Message saveMsg = Message.obtain();
                saveMsg.what = SAVE_COMPLETE;
                saveMsg.obj = saveResult;
                sportHandler.sendMessage(saveMsg);
            }
        }.start();
    }

    private Handler sportHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SAVE_START:
                    boolean sta = (Boolean) msg.obj;
                    if (sta) {
                        Toast.makeText(mContext, "上传成功", Toast.LENGTH_SHORT).show();
                        mList.get(pos).setIs_upload(1);
                        saveTrainTask(mList.get(pos));
                    } else {
                        Toast.makeText(mContext, "上传失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case SAVE_COMPLETE:
                    int com = (Integer) msg.obj;
                    if (com != -1) {
                        Log.e("develop_debug", "本地保存成功");
                    } else {
                        Log.e("develop_debug", "本地保存失败");
                    }
                    break;
            }
        }

        ;
    };
}