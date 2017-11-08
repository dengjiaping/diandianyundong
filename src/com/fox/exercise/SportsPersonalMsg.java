package com.fox.exercise;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fox.exercise.api.ApiBack;
import com.fox.exercise.api.ApiConstant;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.api.entity.ExpressionItems;
import com.fox.exercise.api.entity.ImageDetail;
import com.fox.exercise.api.entity.PicsAndIds;
import com.fox.exercise.api.entity.UserPrimsgAll;
import com.fox.exercise.api.entity.UserPrimsgOne;
import com.fox.exercise.bitmap.util.ImageResizer;
import com.fox.exercise.db.SportsContent.PrivateMessageAllTable;
import com.fox.exercise.db.SportsContent.PrivateMsgTable;
import com.fox.exercise.http.FunctionStatic;
import com.fox.exercise.util.RoundedImage;
import com.fox.exercise.util.ScrollLayout;
import com.fox.exercise.util.ScrollLayout.OnViewChangeListener;
import com.umeng.analytics.MobclickAgent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.ingenic.indroidsync.SportsApp;

public class SportsPersonalMsg extends AbstractBaseActivity implements OnViewChangeListener {
    /**
     * Called when the activity is first created.
     */
    private static final String TAG = "SportsPersonalMsg";
    //	private TextView mTitleText;
//	private ImageButton mMoreText;
    private ImageButton mCancelBtn;
    //	private PopupWindow mPopWindow = null;
    // long press for comment
    private Button sk2PressBtn;
    //	private Dialog alertDialog;
    // change comments type
    private ImageButton sk2TextBtn;
    // is comment voice or word
    private boolean upTypeText;
    private LinearLayout upcommentText;
    private EditText upcommentEdittext;
    private Button upcommentSend;
    private RecordHelper mRecorder;
    FinishHandler h = new FinishHandler();

    private static final int UPLOAD_FINISH = 1;
    private static final int NOT_LOGIN = 2;
    private static final int UPLOAD_FAILED = 3;
    private static final int UPLOAD_START = 4;
    private static final int SAVE_TO_DB = 5;
    private static final int RECORD_LOADING = 6;
    private static final int RECORD_PREPARED = 7;
    private static final int RECORD_FINISH = 8;
    private static final int RECORD_PAUSE = 9;
    private static final int RECORD_ERROR = 10;
    private static final int FLAG_RUNWAV = 11;
    private static final int RESULT_ERROR = 12;
    private static final int ADD_ITEM = 13;
    private static final int GETDETAIL_FAIL = 14;
    ImageView mWavBegin;
    private int currentDuration;
    private boolean isStart = true;
    //	private int currentDuration1;
//	private int curRecordPos;
//	private boolean isPrepared;
    boolean isPause = false;
    private Dialog uploadProgressDialog;
    private Dialog dialog;
    MediaPlayer mPlayer = null;
    private LinearLayout layoutVoice;
    private ListView mListView;
    private ChatMsgViewAdapter mAdapter;
    //	private List<UserPrimsgOne> mDataArrays = new ArrayList<UserPrimsgOne>();
    private SportsApp mSportsApp;
    private Context mContext;
    private SportsExceptionHandler mExceptionHandler = null;
    private Message eMsg = null;
    private int uid;// the one who send msg to me
    private int touid;// the target user
    private String senderName;// the sender name
    private String senderIcon;// the sender icon
    private String receiverIcon;// the target user icon
    private String birthday;
    private String sex;
    private String defaultMesg;
    private ImageResizer mImageWorker;
    //	private boolean isPopupShowing = false;
    private Dialog mProgressDialog;
    private LinearLayout verifyPassedLayput;
    private LinearLayout verifyFailedLayput;

    // press for emoji
    private ImageButton expressBtn;
    private RelativeLayout rScrollLayout;
    private ScrollLayout scrollLayout;
    private List<ExpressionItems> imgItems;
    private LinearLayout imgLayout;
    private static final float APP_PAGE_SIZE = 21.0f;
    private int mViewCount;
    private int mCurSel;
    private Boolean isShow;
    private String[] imgStr;
    private long preTime = 0;
    private String copyString;

    public static interface IMsgViewType {
        int IMVT_COM_MSG = 0;
        int IMVT_TO_MSG = 1;
    }

    @Override
    public void initIntentParam(Intent intent) {
//		setContentView(R.layout.sports_private_msg);
        Log.d(TAG, "onCreate INVOKED");
        mContext = this;
        mSportsApp = (SportsApp) getApplication();
        mExceptionHandler = mSportsApp.getmExceptionHandler();
        Bundle bundle = intent.getExtras();
        uid = bundle.getInt("uid", 0);
        touid = bundle.getInt("touid", mSportsApp.getSportUser().getUid());
        Log.d(TAG, "uid:" + uid);
        Log.d(TAG, "touid:" + touid);
        Log.d(TAG, "self:" + mSportsApp.getSportUser().getUid());
        senderName = bundle.getString("senderName");
        birthday = bundle.getString("birthday");
        sex = bundle.getString("sex");
        Log.d(TAG, "sex:" + sex);
        Log.d(TAG, "birthday:" + birthday);
        senderIcon = bundle.getString("senderIcon");
        //receiverIcon = bundle.getString("receiverIcon");
        //if ("".equals(receiverIcon) || receiverIcon == null) {
        receiverIcon = mSportsApp.getSportUser().getUimg();
        //}
        Log.d(TAG, "senderIcon" + senderIcon);
        Log.d(TAG, "receiverIcon" + receiverIcon);
        defaultMesg = bundle.getString("message");

//		if (uid == mSportsApp.managerId) {
//			title=getResources().getString(R.string.sports_admin_name);
//		} else {
        title = senderName;
//		}
    }

    @Override
    public void onPagePause() {
        FunctionStatic.onPause(this, FunctionStatic.FUNCTION_PRIVATE_MSG_CONTENT, preTime);
        MobclickAgent.onPageEnd("SportsPersonalMsg");
    }

    @Override
    public void onPageResume() {
        preTime = FunctionStatic.onResume();
        MobclickAgent.onPageStart("SportsPersonalMsg");
    }

    @Override
    public void initView() {
        showContentView(R.layout.sports_private_msg);

        mImageWorker = mSportsApp.getImageWorker(mContext, 200, 200);
        //mImageWorker.setLoadingImage(R.drawable.sports_user_edit_portrait);
//		mMoreText = (ImageButton) findViewById(R.id.button_more);

//		mTitleText = (TextView) findViewById(R.id.titleText);
//		if (uid == mSportsApp.managerId) {
//			mTitleText.setText(getString(R.string.sports_admin_name));

//			mMoreText.setVisibility(View.GONE);
//		} else {
        // mMoreText.setVisibility(View.VISIBLE);
//			mTitleText.setText(senderName);
//		}

//		mCancelBtn = (ImageButton) findViewById(R.id.button_cancel);
//
//		mCancelBtn.setOnClickListener(new Sk2ClickListener());

        layoutVoice = (LinearLayout) findViewById(R.id.layoutVoice);

        upTypeText = true;
        mRecorder = new RecordHelper();
        sk2PressBtn = (Button) findViewById(R.id.sk2_press_btn);
        sk2PressBtn.setOnLongClickListener(new Sk2ClickListener());
        sk2PressBtn.setOnTouchListener(new Sk2ClickListener());
        sk2PressBtn.setVisibility(View.GONE);
        sk2TextBtn = (ImageButton) findViewById(R.id.sk2_text_btn);
        sk2TextBtn.setBackgroundResource(R.drawable.sk2voice);
        sk2TextBtn.setOnClickListener(new Sk2ClickListener());

        expressBtn = (ImageButton) findViewById(R.id.expression_text_btn);
        expressBtn.setOnClickListener(new Sk2ClickListener());
        rScrollLayout = (RelativeLayout) findViewById(R.id.rScrollLayout);
        scrollLayout = (ScrollLayout) findViewById(R.id.ScrollLayoutTest);
        scrollLayout.SetOnViewChangeListener(this);
        imgLayout = (LinearLayout) findViewById(R.id.imageLayot);

        initViews();
        // 显示当前第一个
        isShow = false;
        mCurSel = 0;
        ImageView img = (ImageView) imgLayout.getChildAt(mCurSel);
        img.setBackgroundResource(R.drawable.qita_biaoqing_04);

        upcommentText = (LinearLayout) findViewById(R.id.upcomment_text);
        upcommentText.setVisibility(View.VISIBLE);
        upcommentSend = (Button) findViewById(R.id.upcomment_send);
        upcommentSend.setOnClickListener(new Sk2ClickListener());
        upcommentEdittext = (EditText) findViewById(R.id.upcomment_edittext);
        upcommentEdittext.addTextChangedListener(mTextWatcher);
        upcommentEdittext.setOnClickListener(new Sk2ClickListener());
        upcommentEdittext.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                // TODO Auto-generated method stub
                if (isShow == true) {
                    isShow = false;
                    rScrollLayout.setVisibility(View.GONE);
                }
            }
        });
        mListView = (ListView) findViewById(R.id.listview);
        if (defaultMesg != null && defaultMesg.length() > 0) {
            upTypeText = true;
            sk2PressBtn.setVisibility(View.GONE);
            upcommentText.setVisibility(View.VISIBLE);
            sk2TextBtn.setBackgroundResource(R.drawable.sk2voice);
            upcommentEdittext.setText(defaultMesg);
        }
        // init popup window
//		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		LinearLayout popLayout = (LinearLayout) inflater.inflate(R.layout.sports_private_msg_pop_layout, null);
//		mPopWindow = new PopupWindow(popLayout, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//		mPopWindow.setBackgroundDrawable(new BitmapDrawable());
//		mPopWindow.setFocusable(true);
//		mPopWindow.setOutsideTouchable(true);
//		LinearLayout userDetail = (LinearLayout) popLayout.findViewById(R.id.private_msg_userdetail);
        // userDetail.setOnClickListener(new OnClickListener() {
        //
        // @Override
        // public void onClick(View arg0) {
        //
        // Intent userDetailIntent = new Intent(SportsPersonalMsg.this,
        // SportsUserBrowse.class);
        // userDetailIntent.putExtra("ID", uid);
        // startActivity(userDetailIntent);
        // }
        // });

//		LinearLayout addToBlackListLayout = (LinearLayout) popLayout.findViewById(R.id.private_msg_addto_blacklist);
//		addToBlackListLayout.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				showDialog(SportsPersonalMsg.this);
//			}
//		});
//		verifyPassedLayput = (LinearLayout) popLayout.findViewById(R.id.private_msg_verify_passed);
//		verifyPassedLayput.setOnClickListener(new OnClickListener() {
//			ApiBack result = null;
//
//			@Override
//			public void onClick(View v) {
//				new AsyncTask<Void, Void, ApiBack>() {
//
//					@Override
//					protected ApiBack doInBackground(Void... arg0) {
//						try {
//							result = ApiJsonParser.verify(uid, 1);
//						} catch (ApiNetException e) {
//							e.printStackTrace();
//						}
//						return result;
//					}
//
//					@Override
//					protected void onPostExecute(ApiBack result) {
//						// TODO Auto-generated method stub
//						super.onPostExecute(result);
//						if (result.getFlag() == 0) {
//							Toast.makeText(mContext, R.string.sports_private_msg_verify_msg, Toast.LENGTH_LONG).show();
//							if (mPopWindow != null && mPopWindow.isShowing()) {
//								mPopWindow.dismiss();
//							}
//						}
//					}
//
//				}.execute();
//			}
//		});
//		verifyFailedLayput = (LinearLayout) popLayout.findViewById(R.id.private_msg_verify_failed);
//		verifyFailedLayput.setOnClickListener(new OnClickListener() {
//			ApiBack result = null;
//
//			@Override
//			public void onClick(View v) {
//				new AsyncTask<Void, Void, ApiBack>() {
//
//					@Override
//					protected ApiBack doInBackground(Void... arg0) {
//						try {
//							result = ApiJsonParser.verify(uid, 2);
//						} catch (ApiNetException e) {
//							e.printStackTrace();
//						}
//						return result;
//					}
//
//					@Override
//					protected void onPostExecute(ApiBack result) {
//						// TODO Auto-generated method stub
//						super.onPostExecute(result);
//						if (result.getFlag() == 0) {
//							Toast.makeText(mContext, R.string.sports_private_msg_verify_msg, Toast.LENGTH_LONG).show();
//							if (mPopWindow != null && mPopWindow.isShowing()) {
//								mPopWindow.dismiss();
//							}
//						}
//					}
//
//				}.execute();
//			}
//		});

//		mMoreText.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				if (mPopWindow.isShowing()) {
//
//					mPopWindow.dismiss();
//
//				} else {
//					mPopWindow.showAsDropDown(findViewById(R.id.button_more));
//				}
//			}
//		});
        // mBtnSend = (Button) findViewById(R.id.btn_send);
        // mBtnSend.setOnClickListener(this);
        // mBtnBack = (Button) findViewById(R.id.btn_back);
        // mBtnBack.setOnClickListener(this);
        //
        // mEditTextContent = (EditText) findViewById(R.id.et_sendmessage);

    }

    class Sk2ClickListener implements OnClickListener, OnLongClickListener, OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP: {
                    Log.d(TAG, "ACTION_UP INVOKED:sk2PressBtn clicked?" + (v == sk2PressBtn));
                    if (v == sk2PressBtn) {
                        if (mSportsApp.isOpenNetwork()) {
                            mRecorder.stop();
                            if (dialog != null) {
                                dialog.dismiss();
                                dialog = null;
                                layoutVoice.setVisibility(View.GONE);
                                if (mRecorder.sampleLength() < 1) {
                                    Toast.makeText(SportsPersonalMsg.this,
                                            getResources().getString(R.string.sports_record_fail), Toast.LENGTH_SHORT).show();
                                    break;
                                }
                                UploadThread t = new UploadThread("/sdcard/Recording/" + (RecordHelper.mSampleFile).getName(),
                                        "", RecordHelper.mSampleLength);
                                t.start();

                            }
                        } else {
                            layoutVoice.setVisibility(View.GONE);
                            Toast.makeText(SportsPersonalMsg.this,
                                    getResources().getString(R.string.acess_server_error), Toast.LENGTH_SHORT).show();
                            if (uploadProgressDialog != null)
                                if (uploadProgressDialog.isShowing())
                                    uploadProgressDialog.dismiss();
                        }
                    }
                }
            }
            return false;
        }

        @Override
        public boolean onLongClick(View v) {
            if (v == sk2PressBtn) {
                if (wavBeginOne != null && mPlayer != null) {
                    mHandler.sendMessage(mHandler.obtainMessage(RECORD_PAUSE, wavBeginOne));
                    isStart = true;
                    mPlayer.pause();
                    mPlayer = null;
                    isPause = false;
                    Log.i("", "进来了");
                }
                mRecorder.startRecording(MediaRecorder.OutputFormat.DEFAULT, ".mp3", SportsPersonalMsg.this);
                dialog = new Dialog(SportsPersonalMsg.this, R.style.share_dialog2);
                dialog.setCanceledOnTouchOutside(false);
                // dialog.show();
                layoutVoice.setVisibility(View.VISIBLE);
            }
            return true;
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick INVOKED:sk2PressBtn clicked?" + (v == sk2PressBtn));
            if (v == sk2TextBtn) {
                isShow = false;
                rScrollLayout.setVisibility(View.GONE);
                if (!upTypeText) {
                    upTypeText = true;
                    sk2PressBtn.setVisibility(View.GONE);
                    upcommentText.setVisibility(View.VISIBLE);
                    sk2TextBtn.setBackgroundResource(R.drawable.sk2voice);
                } else {
                    upTypeText = false;
                    sk2PressBtn.setVisibility(View.VISIBLE);
                    upcommentText.setVisibility(View.GONE);
                    sk2TextBtn.setBackgroundResource(R.drawable.sk2text);
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(upcommentEdittext.getWindowToken(), 0);
                }
            }
            if (v == upcommentSend) {
                if (mSportsApp.isOpenNetwork()) {
                    isShow = false;
                    rScrollLayout.setVisibility(View.GONE);
                    if (upcommentEdittext.length() == 0) {
                        Toast.makeText(SportsPersonalMsg.this, getString(R.string.sports_private_msg_not_empty),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    UploadThread t = new UploadThread("", upcommentEdittext.getText().toString(), 0);
                    upcommentEdittext.setText("");
                    t.start();
                } else {
                    Toast.makeText(SportsPersonalMsg.this,
                            getResources().getString(R.string.acess_server_error), Toast.LENGTH_SHORT).show();
                    if (uploadProgressDialog != null)
                        if (uploadProgressDialog.isShowing())
                            uploadProgressDialog.dismiss();
                }
            }
            if (v == expressBtn) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager.isActive())
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                sk2PressBtn.setVisibility(View.GONE);
                upcommentText.setVisibility(View.VISIBLE);
                if (isShow == false) {
                    isShow = true;
                    rScrollLayout.setVisibility(View.VISIBLE);
                    if (!upTypeText) {
                        upTypeText = true;
                        sk2TextBtn.setBackgroundResource(R.drawable.sk2voice);
                    }
                } else if (isShow == true) {
                    isShow = false;
                    rScrollLayout.setVisibility(View.GONE);
                }
            }
            Log.i("", "lalaalla12222" + v);
            if (v == upcommentEdittext) {
                Log.i("", "lalaalla12222");
                if (isShow == true) {
                    isShow = false;
                    rScrollLayout.setVisibility(View.GONE);
                    Log.i("", "lalaalla133333");
                }
            }

            // if (v==mMoreText) {
            //
            // if (!mPopWindow.isShowing()) {
            // mPopWindow.showAsDropDown(findViewById(R.id.button_more));
            // }
            // }

            if (v == mCancelBtn) {
                finish();
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            try {
                IBinder token = mListView.getWindowToken();
                if (inputMethodManager.isActive() && token != null)
                    inputMethodManager.hideSoftInputFromWindow(token,
                            InputMethodManager.HIDE_NOT_ALWAYS);
            } catch (Exception e) {
            }
            isShow = false;
            rScrollLayout.setVisibility(View.GONE);
        }
        return super.onTouchEvent(event);
    }

    class GetPicDetailThread extends Thread {
        private int picId = 0;

        public GetPicDetailThread(int picId) {
            this.picId = picId;
        }

        @Override
        public void run() {
            try {
                ImageDetail detail = ApiJsonParser.getImgDetail(mSportsApp.getSessionId(), picId);
                if (detail == null || ApiConstant.URL.equals(detail.getPimg())) {
                    Message.obtain(h, GETDETAIL_FAIL).sendToTarget();
                    return;
                }
                List<PicsAndIds> list = new ArrayList<PicsAndIds>();
                PicsAndIds p = new PicsAndIds();
                p.setId(picId);
                p.setImgUrl(detail.getPimg());
                p.setLikes(detail.getLikes());
                p.setImgDetail(detail);
                list.add(p);
                if (list.size() > 0) {
                    // SportsDetails.startSelf(SportsPersonalMsg.this, list, 0,
                    // -1);
                }
            } catch (ApiNetException e) {
                e.printStackTrace();
            } catch (ApiSessionOutException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
//		if (mPopWindow != null && mPopWindow.isShowing()) {
//			mPopWindow.dismiss();
//		} else {
        super.onBackPressed();
//		}
    }

    class UploadThread extends Thread {
        private String wav;
        private String comment;
        private int wavDurations;

        public UploadThread(String wav, String comment, int wavDurations) {
            this.wav = wav;
            this.comment = comment;
            this.wavDurations = wavDurations;

        }

        @Override
        public void run() {

            ApiBack apiBack = new ApiBack();
            Log.d(TAG, "mSportsApp.getSessionId():" + mSportsApp.getSessionId());

            try {
                if ("".equals(mSportsApp.getSessionId()) || mSportsApp.getSessionId() == null) {
                    Message msg = Message.obtain(h, NOT_LOGIN);
                    msg.sendToTarget();
                } else {
                    Message msg = Message.obtain(h, UPLOAD_START);
                    msg.sendToTarget();
                    apiBack = ApiJsonParser.sendprimsg(mSportsApp.getSessionId(), uid, comment, wav, wavDurations);
                    // apiBack =
                    // ApiJsonParser.upComments(mSportsApp.getSessionId(),
                    // picId, comment, wav, wavDurations,
                    // item.getImgDetail().getUid());
                    if (apiBack.getFlag() == 0) {
                        MobclickAgent.onEvent(mContext, "sendprimsg");
                        final UserPrimsgOne priMsg = new UserPrimsgOne();
                        priMsg.setAddTime(System.currentTimeMillis() / 1000);
                        priMsg.setCommentText(comment);
                        priMsg.setCommentWav(wav);

                        priMsg.setWavDuration(wavDurations);
                        Log.d(TAG, "msg in server " + "text" + comment);
                        priMsg.setUid(mSportsApp.getSportUser().getUid());
                        priMsg.setTouid(uid);
                        priMsg.setOwnerid(mSportsApp.getSportUser().getUid());
                        new Thread() {
                            @Override
                            public void run() {
                                long result = mSportsApp.getSportsDAO().savePrivateMsgInfo(priMsg);
                                Log.d(TAG, "result:" + result);
                                mHandler.post(new Runnable() {

                                    @Override
                                    public void run() {
                                        mAdapter.addItem(priMsg);
                                    }
                                });

                                h.sendEmptyMessage(SAVE_TO_DB);
                            }
                        }.start();

                        new Thread() {
                            List<UserPrimsgAll> primsgAllList = new ArrayList<UserPrimsgAll>();

                            @Override
                            public void run() {
                                UserPrimsgAll userPrimsgAll = new UserPrimsgAll();
                                userPrimsgAll.setAddTime(priMsg.getAddTime());
                                userPrimsgAll.setName(senderName);
                                userPrimsgAll.setUid(priMsg.getTouid());
                                userPrimsgAll.setTouid(mSportsApp.getSportUser().getUid());
                                userPrimsgAll.setUimg(senderIcon);
                                userPrimsgAll.setBirthday(birthday);
                                userPrimsgAll.setCounts(0);
                                userPrimsgAll.setSex(sex);
                                userPrimsgAll.setTouimg(mSportsApp.getSportUser().getUimg());
                                primsgAllList.add(userPrimsgAll);
                                mSportsApp.getSportsDAO().insertPrivateMsgAll(PrivateMessageAllTable.TABLE_NAME,
                                        primsgAllList, "read");
                                Log.d(TAG, "save msg to PrivateMsgAll :" + userPrimsgAll);
                            }
                        }.start();

                        msg = Message.obtain(h, UPLOAD_FINISH);
                        msg.sendToTarget();
                    } else {
                        msg = Message.obtain(h, UPLOAD_FAILED, apiBack.getMsg());
                        msg.sendToTarget();
                    }
                }
            } catch (ApiNetException e) {
                e.printStackTrace();
                // SportsApp.eMsg = Message.obtain(mExceptionHandler,
                // SportsExceptionHandler.NET_ERROR);
                // SportsApp.eMsg.sendToTarget();
            } catch (ApiSessionOutException e) {
                e.printStackTrace();
                // SportsApp.eMsg = Message.obtain(mExceptionHandler,
                // SportsExceptionHandler.SESSION_OUT);
                // SportsApp.eMsg.sendToTarget();
                // startActivity(new Intent(SportsPersonalMsg.this,
                // LoginActivity.class));
            }
        }
    }

    class FinishHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            Intent i = null;
            switch (msg.what) {

                case GETDETAIL_FAIL:
                    Toast.makeText(mContext, R.string.sports_msg_pic_not_exist, Toast.LENGTH_SHORT).show();
                    break;
                case UPLOAD_FINISH:
//				MobclickAgent.onEvent(mContext, "upcomment");
                    // i = new Intent(SportsUploadActivity.this,
                    // CameraApp.class);
                    // startActivity(i);
                    uploadProgressDialog.dismiss();
                    Toast.makeText(SportsPersonalMsg.this, getString(R.string.sports_private_msg_send_successfully),
                            Toast.LENGTH_LONG).show();
                    mListView.setSelection(mListView.getBottom());
                    break;
                case NOT_LOGIN:
                    // i = new Intent(SportsPersonalMsg.this, LoginActivity.class);
                    // startActivity(i);
                    break;
                case UPLOAD_FAILED:
                    uploadProgressDialog.dismiss();
                    Toast.makeText(SportsPersonalMsg.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case UPLOAD_START:

                    uploadProgressDialog = new Dialog(SportsPersonalMsg.this, R.style.sports_dialog);
                    LayoutInflater mInflater = getLayoutInflater();
                    View v = mInflater.inflate(R.layout.sports_progressdialog, null);
                    TextView message = (TextView) v.findViewById(R.id.message);
                    message.setText(getString(R.string.sports_private_msg_sending));
                    v.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
                    uploadProgressDialog.setContentView(v);
                    uploadProgressDialog.setCancelable(true);
                    uploadProgressDialog.setCanceledOnTouchOutside(false);
                    uploadProgressDialog.show();
                    break;
                case SAVE_TO_DB:

                    mAdapter.notifyDataSetChanged();
            }
        }

    }

//	private String[] msgArray = new String[] {};
//
//	private String[] dataArray = new String[] { "2012-09-01 18:00", "2012-09-01 18:10", "2012-09-01 18:11",
//			"2012-09-01 18:20", "2012-09-01 18:30", "2012-09-01 18:35", "2012-09-01 18:40", "2012-09-01 18:50" };

//	private final static int COUNT = 8;

    private class InitialDataTask extends AsyncTask<Void, Void, Void> {

        public InitialDataTask() {
        }

        @Override
        protected Void doInBackground(Void... sessionIds) {
            initData();
            return null;

        }

        @Override
        protected void onPostExecute(Void userDetail) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }

            mAdapter.notifyDataSetChanged();
            mListView.setSelection(mListView.getBottom());
            super.onPostExecute(userDetail);
        }

    }

    public void initData() {
        Log.d(TAG, "initData");
        // insert some test statistics
        // try {
        // ApiBack result1=ApiJsonParser.sendprimsg(mSportsApp.getSessionId(),
        // uid, "test1 from q.", "", 0);
        // ApiBack result2=ApiJsonParser.sendprimsg(mSportsApp.getSessionId(),
        // uid, "test2 from q.", "", 0);
        // ApiBack result3=ApiJsonParser.sendprimsg(mSportsApp.getSessionId(),
        // uid, "test3 from q.", "", 0);
        // Log.d(TAG, "result1:"+result1.getFlag());
        // Log.d(TAG, "result1:"+result1.getMsg());
        // Log.d(TAG, "result2:"+result2.getFlag());
        // Log.d(TAG, "result2:"+result2.getMsg());
        // Log.d(TAG, "result3:"+result3.getFlag());
        // Log.d(TAG, "result3:"+result3.getMsg());
        // } catch (ApiNetException e) {
        // e.printStackTrace();
        // eMsg = Message.obtain(mExceptionHandler,
        // SportsExceptionHandler.NET_ERROR);
        // eMsg.sendToTarget();
        // } catch (ApiSessionOutException e) {
        // e.printStackTrace();
        // eMsg = Message.obtain(mExceptionHandler,
        // SportsExceptionHandler.SESSION_OUT);
        // eMsg.sendToTarget();
        // startActivity(new Intent(mContext, LoginActivity.class));
        // }

        // get data from local db
        // TODO get the msg from each other
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("select * from ").append(PrivateMsgTable.TABLE_NAME).append(" where ((( ")
                .append(PrivateMsgTable.Columns.UID).append("=?").append(" AND ").append(PrivateMsgTable.Columns.TOUID)
                .append("=?").append(" ) ").append(" OR ").append("( ").append(PrivateMsgTable.Columns.UID)
                .append("=?").append(" AND ").append(PrivateMsgTable.Columns.TOUID).append("=?").append(" )) ")
                .append(" AND ( ").append(PrivateMsgTable.Columns.OWNERID).append("=?").append(" OR ")
                .append(PrivateMsgTable.Columns.OWNERID).append("=?").append(" )) ");
        // .append(" ORDER BY ").append(PrivateMsgTable.Columns.ADDTIME).append(" ASC ");
        // String sql = "select * from "+ PrivateMsgTable.TABLE_NAME +
        // " where (( "
        // +PrivateMsgTable.Columns.UID +"=?" + " AND "
        // +PrivateMsgTable.Columns.TOUID +"=?" +" ) "
        // +" OR "
        // +"( "
        // +PrivateMsgTable.Columns.UID +"=?"
        // +" AND "
        // +PrivateMsgTable.Columns.TOUID +"=?"
        // +" )) "
        // +" ORDER BY "
        // +PrivateMsgTable.Columns.ADDTIME + " ASC ";
        String[] selectionArgs = new String[]{Integer.toString(uid), Integer.toString(touid),
                Integer.toString(touid), Integer.toString(uid),
                Integer.toString(touid), Integer.toString(0)};

        List<UserPrimsgOne> localMsgList = mSportsApp.getSportsDAO().retrievePrivateMSG(sqlBuilder.toString(),
                selectionArgs);
        int size = localMsgList.size();
        Log.d(TAG, "localMsgList size:" + localMsgList.size());
        for (int i = 0; i < size; i++) {
            Log.d(TAG, "msg in local DB:" + localMsgList.get(i));
            Message message = Message.obtain(mHandler, ADD_ITEM);
            message.obj = localMsgList.get(i);
            message.sendToTarget();
            // mAdapter.addItem(localMsgList.get(i));
        }
        // mAdapter.notifyDataSetChanged();
        List<UserPrimsgOne> latestMsgList = null;
        // get the latest msg from server, then save to db
        try {
            latestMsgList = ApiJsonParser.getPrimsgOne(mSportsApp.getSessionId(), uid, 0);
        } catch (ApiNetException e) {
            e.printStackTrace();
            eMsg = Message.obtain(mExceptionHandler, SportsExceptionHandler.NET_ERROR);
            eMsg.sendToTarget();
        } catch (ApiSessionOutException e) {
            e.printStackTrace();
            eMsg = Message.obtain(mExceptionHandler, SportsExceptionHandler.SESSION_OUT);
            eMsg.sendToTarget();
            // startActivity(new Intent(mContext, LoginActivity.class));
        }

        if (latestMsgList != null && latestMsgList.size() > 0) {
            Log.d(TAG, "latestMsgList size:" + latestMsgList.size());
            int l_size = latestMsgList.size();
            for (int i = 0; i < l_size; i++) {
                Log.d(TAG, "msg in server" + latestMsgList.get(i).toString());
                Message message = Message.obtain(mHandler, ADD_ITEM);
                message.obj = latestMsgList.get(i);
                message.sendToTarget();
                // mAdapter.addItem(latestMsgList.get(i));
            }
            // mAdapter.notifyDataSetChanged();
            final List<UserPrimsgOne> saveToDBMsgList = latestMsgList;
            new Thread() {
                @Override
                public void run() {
                    int result = mSportsApp.getSportsDAO().savePrivateMsgInfo(saveToDBMsgList);
                    Log.d(TAG, "save latest msg to db :" + result);
                }
            }.start();

            new Thread() {
                List<UserPrimsgAll> primsgAllList = new ArrayList<UserPrimsgAll>();
                UserPrimsgAll userPrimsgAll = null;

                @Override
                public void run() {
                    for (UserPrimsgOne userPrimsgOne : saveToDBMsgList) {
                        userPrimsgAll = new UserPrimsgAll();
                        userPrimsgAll.setAddTime(userPrimsgOne.getAddTime());
                        userPrimsgAll.setName(senderName);
                        userPrimsgAll.setUid(userPrimsgOne.getUid());
                        userPrimsgAll.setTouid(userPrimsgOne.getTouid());
                        userPrimsgAll.setUimg(senderIcon);
                        userPrimsgAll.setTouid(userPrimsgOne.getTouid());
                        userPrimsgAll.setBirthday(birthday);
                        userPrimsgAll.setCounts(0);
                        userPrimsgAll.setSex(sex);
                        userPrimsgAll.setTouimg(mSportsApp.getSportUser().getUimg());
                        primsgAllList.add(userPrimsgAll);
                    }
                    mSportsApp.getSportsDAO().insertPrivateMsgAll(PrivateMessageAllTable.TABLE_NAME, primsgAllList,
                            "read");
                    Log.d(TAG, "save msg to PrivateMsgAll :" + userPrimsgAll);
                }
            }.start();
        }

        // mAdapter = new ChatMsgViewAdapter(this, mDataArrays);

    }

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ADD_ITEM:
                    UserPrimsgOne userPrimsgOne = (UserPrimsgOne) msg.obj;
                    mAdapter.addItem(userPrimsgOne);
                    break;
                case RECORD_LOADING:
                    ImageView wavBegin = (ImageView) msg.obj;
                    wavBegin.setImageResource(+R.anim.record_loading);
                    AnimationDrawable ad1 = (AnimationDrawable) wavBegin.getDrawable();
                    ad1.start();
                    break;
                case RECORD_PREPARED:
                    ((ImageView) msg.obj).setImageResource(+R.anim.record_run);
                    mWavBegin = ((ImageView) msg.obj);
                    AnimationDrawable ad2 = (AnimationDrawable) ((ImageView) msg.obj).getDrawable();
                    ad2.start();
                    break;
                case RECORD_PAUSE:
                case RECORD_ERROR:
                case RECORD_FINISH:
                    ((ImageView) msg.obj).setImageResource(R.drawable.audio_ani_1);
                    isStart = true;
                    // if (mPlayer != null && mPlayer.isPlaying())
                    // mPlayer.pause();
                    // mRecorder.destory();
                    break;
                case FLAG_RUNWAV:
                    TextView userWavTimes = (TextView) msg.obj;
                    String cont = userWavTimes.getText().toString();
                    if (!"".equals(cont) && cont != null) {
                        int number = Integer.parseInt(cont.split("次播放")[0]) + 1;
                        (userWavTimes).setText(Integer.toString(number) + "次播放");
                    }
                    break;

                case RESULT_ERROR:
                    Log.e(TAG, "msg_ERROR");
                    String message = (String) msg.obj;
                    Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
                    break;
            }
        }

    };
    ImageView wavBeginOne;

    /* (non-Javadoc)
     * @see android.app.Activity#onContextItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        ClipboardManager cmb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        cmb.setText(copyString);
        Toast.makeText(this, R.string.copy_success, Toast.LENGTH_SHORT).show();
        return super.onContextItemSelected(item);
    }

    class ChatMsgViewAdapter extends BaseAdapter {

        private List<UserPrimsgOne> msgList = new ArrayList<UserPrimsgOne>();

        private Context ctx;

        private LayoutInflater mInflater;
        private SportsApp mSportsApp;
        private int uid; // myself

        public ChatMsgViewAdapter(Context context) {
            ctx = context;
            mSportsApp = SportsApp.getInstance();
            uid = mSportsApp.getSportUser().getUid();

            Log.d(TAG, "uid:" + uid);
            // this.msgList = msgList;
            mInflater = LayoutInflater.from(context);
        }

        public int getCount() {
            return msgList.size();
        }

        public void addItem(UserPrimsgOne msg) {
            msgList.add(msg);
        }

        public Object getItem(int position) {
            return msgList.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public int getItemViewType(int position) {
            // TODO Auto-generated method stub
            UserPrimsgOne msg = msgList.get(position);

            if (getMsgType(msg)) {
                return IMsgViewType.IMVT_COM_MSG;
            } else {
                return IMsgViewType.IMVT_TO_MSG;
            }
        }

        public int getViewTypeCount() {
            // TODO Auto-generated method stub
            return 2;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            final UserPrimsgOne msg = msgList.get(position);
            Log.d(TAG, "msg.getUid():" + msg.getUid());
            Log.d(TAG, "msg.getTouid():" + msg.getTouid());
            boolean isComMsg = getMsgType(msg);
            Log.d(TAG, "isComMsg:" + isComMsg);
            ViewHolder viewHolder = null;
            if (convertView == null) {
                if (isComMsg) {
                    convertView = mInflater.inflate(R.layout.chatting_item_msg_text_left, null);
//                    convertView = mInflater.inflate(R.layout.chatting_item_msg_text_right, null);
                } else {
                    convertView = mInflater.inflate(R.layout.chatting_item_msg_text_right, null);
//                    convertView = mInflater.inflate(R.layout.chatting_item_msg_text_left, null);
                }

                viewHolder = new ViewHolder();
                viewHolder.rClick = (LinearLayout) convertView.findViewById(R.id.recoding_click);
                viewHolder.wavBegin = (ImageView) convertView.findViewById(R.id.wav_begin);
                viewHolder.wavDurations = (TextView) convertView.findViewById(R.id.wav_durations);
                viewHolder.userIcon = (RoundedImage) convertView.findViewById(R.id.iv_userhead);
                viewHolder.tvSendTime = (TextView) convertView.findViewById(R.id.tv_sendtime);
                viewHolder.commentText = (TextView) convertView.findViewById(R.id.tv_chatcontent);
                viewHolder.authImageView = (ImageView) convertView.findViewById(R.id.iv_authpic);

                viewHolder.isComMsg = isComMsg;

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            String commonText = msg.getCommentText();
            if (msg.getCommentWav() != null && msg.getWavDuration() > 0) {
                viewHolder.commentText.setVisibility(View.GONE);
                viewHolder.authImageView.setVisibility(View.GONE);
                viewHolder.rClick.setVisibility(View.VISIBLE);
                viewHolder.wavDurations.setText("" + msg.getWavDuration() + "″");

            } else if (commonText != null && commonText.startsWith("####")
                    && commonText.contains("http://kupao.mobifox.cn")) {
                mImageWorker.loadImage(commonText.substring(commonText.indexOf("####") + 4), viewHolder.authImageView,
                        null, null, false);
                mHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        if (verifyPassedLayput != null)
                            verifyPassedLayput.setVisibility(View.VISIBLE);
                        if (verifyFailedLayput != null)
                            verifyFailedLayput.setVisibility(View.VISIBLE);
                    }
                });
                viewHolder.authImageView.setVisibility(View.VISIBLE);
                viewHolder.commentText.setVisibility(View.GONE);
                viewHolder.rClick.setVisibility(View.GONE);
            } else {
                viewHolder.commentText.setVisibility(View.VISIBLE);
                viewHolder.rClick.setVisibility(View.GONE);
                viewHolder.authImageView.setVisibility(View.GONE);
            }
            Log.d(TAG, "senderIcon:" + senderIcon);
            Log.d(TAG, "receiverIcon:" + receiverIcon);
            if (isComMsg) {
                viewHolder.userIcon.setImageResource("man".equals(sex) ? R.drawable.sports_user_edit_portrait_male
                        : R.drawable.sports_user_edit_portrait);
                if (senderIcon != null && !senderIcon.endsWith("http://kupao.mobifox.cn") && !senderIcon.equals("")) {
                    mImageWorker.loadImage(senderIcon, viewHolder.userIcon, null, null, false);
                } else {
                    viewHolder.userIcon.setImageResource("man".equals(sex) ? R.drawable.sports_user_edit_portrait_male
                            : R.drawable.sports_user_edit_portrait);
                }

            } else {
                viewHolder.userIcon.setImageResource("man".equals(mSportsApp.getSportUser().getSex()) ? R.drawable.sports_user_edit_portrait_male
                        : R.drawable.sports_user_edit_portrait);
                if (receiverIcon != null && !receiverIcon.endsWith("http://kupao.mobifox.cn")
                        && !receiverIcon.equals("")) {
                    mImageWorker.loadImage(receiverIcon, viewHolder.userIcon, null, null, false);
                } else {

                    viewHolder.userIcon.setImageResource("man".equals(mSportsApp.getSportUser().getSex()) ? R.drawable.sports_user_edit_portrait_male
                            : R.drawable.sports_user_edit_portrait);
                }

            }
            final String mediaPath = msg.getCommentWav();
            final ImageView wavBegin = viewHolder.wavBegin;
            Log.d(TAG, "mediaPath:" + mediaPath);
            final Object lock = new Object();
            viewHolder.rClick.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    synchronized (lock) {

                        if (mediaPath.endsWith("null") || mediaPath == null) {
                            return;
                        }
//						currentDuration1 = 0;
                        new Thread() {
                            @Override
                            public void run() {
                                if (wavBeginOne != wavBegin && mPlayer != null) {
                                    mHandler.sendMessage(mHandler.obtainMessage(RECORD_PAUSE, wavBeginOne));
                                    isStart = true;
                                    mPlayer.pause();
                                    isPause = false;
                                    wavBeginOne = wavBegin;
                                    Log.i("", "进来了");
                                }
                                if (isStart) {
                                    Log.d(TAG, "if (isStart)");
                                    //mHandler.sendMessage(mHandler.obtainMessage(RECORD_LOADING, wavBegin));
                                    // wavBegin.setImageResource(R.drawable.record_loading1);
                                    isStart = false;
                                    wavBeginOne = wavBegin;
                                    if (!isPause) {
                                        Log.d(TAG, "if (!isPause)");
                                        mPlayer = mRecorder.startPlaybackNet(SportsPersonalMsg.this, mediaPath);
                                    }
                                    if (mPlayer == null) {
                                        Log.d(TAG, "if (mPlayer == null)");
                                        // mHandler.sendMessage(mHandler.obtainMessage(RECORD_PAUSE,
                                        // wavBegin));
                                        mPlayer = mRecorder.startPlaybackNet(SportsPersonalMsg.this, mediaPath);
                                        mHandler.sendMessage(mHandler.obtainMessage(RECORD_LOADING, wavBegin));
                                        // return;
                                    }
                                    mPlayer.setOnErrorListener(new OnErrorListener() {
                                        @Override
                                        public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
                                            isStart = true;
                                            currentDuration = 0;
                                            mRecorder.stopPlayback();
                                            mHandler.sendMessage(mHandler.obtainMessage(RECORD_ERROR, wavBegin));
                                            return true;
                                        }
                                    });
                                    mPlayer.setOnCompletionListener(new OnCompletionListener() {
                                        @Override
                                        public void onCompletion(MediaPlayer arg0) {
                                            isStart = true;
                                            isPause = false;
                                            Log.e("hjtest", "onCompletion");
                                            mHandler.sendMessage(mHandler.obtainMessage(RECORD_FINISH, wavBegin));
                                        }
                                    });
                                    mPlayer.setOnPreparedListener(new OnPreparedListener() {
                                        @Override
                                        public void onPrepared(MediaPlayer arg0) {
                                            Log.e("hjtest", "onPrepared");
                                            if (mPlayer == null) {
                                                mHandler.sendMessage(mHandler.obtainMessage(RECORD_PAUSE, wavBegin));
                                                return;
                                            }
                                            mHandler.sendMessage(mHandler.obtainMessage(RECORD_PREPARED, wavBegin));
                                        }
                                    });
                                    try {
                                        if (!isPause) {
                                            Log.d(TAG, "not isPause");
                                            mPlayer.prepare();
                                            mPlayer.start();
                                        } else {
                                            Log.d(TAG, "isPause");
                                            mPlayer.start();
                                            mHandler.sendMessage(mHandler.obtainMessage(RECORD_PREPARED, wavBegin));
                                        }
                                    } catch (IllegalStateException e) {
                                        mPlayer = null;
                                        isStart = true;
                                        currentDuration = 0;
                                        mHandler.sendMessage(mHandler.obtainMessage(RECORD_PAUSE, wavBegin));
                                    } catch (Exception e) {
                                        mPlayer = null;
                                        isStart = true;
                                        currentDuration = 0;
                                        mHandler.sendMessage(mHandler.obtainMessage(RECORD_PAUSE, wavBegin));
                                    }
                                    if (currentDuration > 0 || isPause) {
                                        isPause = false;
                                        if (mPlayer != null)
                                            mPlayer.start();
                                        else {
                                            mPlayer = mRecorder.startPlaybackNet(SportsPersonalMsg.this, mediaPath);
                                            isPause = false;
                                            mHandler.sendMessage(mHandler.obtainMessage(RECORD_FINISH, wavBegin));
                                        }
                                        currentDuration = 0;
                                    } else {
                                        // new Thread() {
                                        // @Override
                                        // public void run() {
                                        // // TODO Auto-generated
                                        // // method
                                        // // stub
                                        // try {
                                        // ApiJsonParser.runWav(mSportsApp.getSessionId(),
                                        // mItem.getId());
                                        // mHandler.sendMessage(mHandler.obtainMessage(FLAG_RUNWAV,
                                        // mUserWavTimes));
                                        // } catch (ApiNetException e) {
                                        // e.printStackTrace();
                                        // }
                                        // }
                                        //
                                        // }.start();
                                    }
                                } else if (!isStart) {
                                    Log.d(TAG, "else if (!isStart)");
                                    if (mPlayer != null) {
                                        Log.d(TAG, "if (mPlayer != null)");
                                        try {
                                            mPlayer.pause();
                                            isStart = true;
                                            isPause = true;
                                            // currentDuration1 =
                                            // mPlayer.getCurrentPosition();
                                        } catch (Exception e) {
                                            mPlayer = null;
                                            isStart = true;
                                            currentDuration = 0;
                                            mHandler.sendMessage(mHandler.obtainMessage(RECORD_PAUSE, wavBegin));
                                        }
                                    }
                                    mHandler.sendMessage(mHandler.obtainMessage(RECORD_PAUSE, wavBegin));

                                }
                            }

                        }.start();

                    }
                }
            });
            viewHolder.tvSendTime.setText(SportsUtilities.millionsToStringDate(msg.getAddTime()));
            String content = msg.getCommentText();
            List<SpannableString> list = getExpressionString(mContext, content);
            for (SpannableString span : list) {
                viewHolder.commentText.setText(span);
            }
            viewHolder.wavPath = msg.getCommentWav();
            viewHolder.commentText.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

                @Override
                public void onCreateContextMenu(ContextMenu menu, View v,
                                                ContextMenuInfo menuInfo) {
                    // TODO Auto-generated method stub
                    menu.add(0, 0, 0, R.string.copy);
                }
            });

            if (SportsApp.mIsAdmin) {
                viewHolder.commentText.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        Log.e(TAG, "msg.getCommentText():" + msg.getCommentText());
                        if (msg.getCommentText().startsWith("$$$$$$")) {
                            String buff = msg.getCommentText().split(" ")[1];
                            Log.e(TAG, "buff:" + buff);
                            try {
                                int userId = Integer.parseInt(buff);
                                Log.e(TAG, "id" + userId);
                                // Intent intent = new Intent(mContext,
                                // SportsUserBrowse.class);
                                // intent.putExtra("ID", userId);
                                // mContext.startActivity(intent);
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        } else if (msg.getCommentText().startsWith("$$$$")) {
                            String buff = msg.getCommentText().split(" ")[1];
                            Log.e(TAG, "buff:" + buff);
                            try {
                                int picId = Integer.parseInt(buff);
                                GetPicDetailThread t = new GetPicDetailThread(picId);
                                t.start();
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });
            }
            return convertView;
        }

        private boolean getMsgType(UserPrimsgOne msg) {
            return msg.getUid() == uid ? false : true;
        }

        private class ViewHolder {
            public TextView commentText;
            public TextView wavDurations;
            public String wavPath;
            public LinearLayout rClick;
            public TextView tvSendTime;
            public ImageView wavBegin;
            public RoundedImage userIcon;
            public boolean isComMsg = true;
            public ImageView authImageView;
        }

    }

    // @Override
    // public void onClick(View v) {
    // // TODO Auto-generated method stub
    // switch(v.getId())
    // {
    // case R.id.btn_send:
    // send();
    // break;
    // case R.id.btn_back:
    // finish();
    // break;
    // }
    // }

    // private void send()
    // {
    // String contString = mEditTextContent.getText().toString();
    // if (contString.length() > 0)
    // {
    // ChatMsgEntity entity = new ChatMsgEntity();
    // entity.setDate(getDate());
    // entity.setName("浜洪┈");
    // entity.setMsgType(false);
    // entity.setText(contString);
    //
    // mDataArrays.add(entity);
    // mAdapter.notifyDataSetChanged();
    //
    // mEditTextContent.setText("");
    //
    // mListView.setSelection(mListView.getCount() - 1);
    // }
    // }

//	private String getDate() {
//		Calendar c = Calendar.getInstance();
//
//		String year = String.valueOf(c.get(Calendar.YEAR));
//		String month = String.valueOf(c.get(Calendar.MONTH));
//		String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH) + 1);
//		String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
//		String mins = String.valueOf(c.get(Calendar.MINUTE));
//
//		StringBuffer sbBuffer = new StringBuffer();
//		sbBuffer.append(year + "-" + month + "-" + day + " " + hour + ":" + mins);
//
//		return sbBuffer.toString();
//	}

//	private void showDialog(Context con) {
//		alertDialog = new Dialog(con, R.style.sports_dialog);
//		LayoutInflater mInflater = getLayoutInflater();
//		View v = mInflater.inflate(R.layout.sports_dialog, null);
//		v.findViewById(R.id.bt_ok).setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				new AsyncTask<Void, Void, Void>() {
//					ApiBack apiBack = null;
//
//					@Override
//					protected Void doInBackground(Void... params) {
//						try {
//							apiBack = ApiJsonParser.blackpeople(mSportsApp.getSessionId(), uid, 1);
//						} catch (ApiNetException e) {
//							// SportsApp.eMsg =
//							// Message.obtain(mExceptionHandler,
//							// SportsExceptionHandler.NET_ERROR);
//							// SportsApp.eMsg.sendToTarget();
//						} catch (ApiSessionOutException e) {
//							// SportsApp.eMsg =
//							// Message.obtain(mExceptionHandler,
//							// SportsExceptionHandler.SESSION_OUT);
//							// SportsApp.eMsg.sendToTarget();
//							// startActivity(new Intent(SportsPersonalMsg.this,
//							// LoginActivity.class));
//						}
//						return null;
//					}
//
//					@Override
//					protected void onPostExecute(Void result) {
//						super.onPostExecute(result);
//						alertDialog.dismiss();
//						if (apiBack.getFlag() == 0) {
////							MobclickAgent.onEvent(SportsPersonalMsg.this, "blackpeople");
//							Toast.makeText(SportsPersonalMsg.this, "拉黑成功", Toast.LENGTH_SHORT).show();
//						} else if (apiBack.getFlag() == -1) {
//							Toast.makeText(SportsPersonalMsg.this, "拉黑失败", Toast.LENGTH_SHORT).show();
//						}
//					}
//				}.execute();
//			}
//		});
//		v.findViewById(R.id.bt_cancel).setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				alertDialog.dismiss();
//			}
//		});
//		TextView message = (TextView) v.findViewById(R.id.message);
//		message.setText("拉黑后对方的消息将被屏蔽,你可以到黑名单中取消拉黑,是否继续？");
//		v.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
//		alertDialog.setCancelable(true);
//		alertDialog.setCanceledOnTouchOutside(false);
//		alertDialog.setContentView(v);
//		alertDialog.show();
//	}

    @Override
    public void OnViewChange(int view) {
        setCurPoint(view);
    }

    private void setCurPoint(int index) {
        if (index < 0 || index > mViewCount - 1 || mCurSel == index) {
            return;
        }
        ImageView v1 = (ImageView) imgLayout.getChildAt(mCurSel);
        v1.setBackgroundResource(R.drawable.qita_biaoqing_03);
        ImageView v2 = (ImageView) imgLayout.getChildAt(index);
        v2.setBackgroundResource(R.drawable.qita_biaoqing_04);
        mCurSel = index;
    }

    public void initViews() {
        imgStr = getResources().getStringArray(R.array.imageStr_array);
        imgItems = new ArrayList<ExpressionItems>();
        Field[] files = R.drawable.class.getDeclaredFields();
        int j = 0;
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
                int resID = getResources().getIdentifier(file.getName(), "drawable", appInfo.packageName);
                ExpressionItems item = new ExpressionItems();
                item.setId(resID);
                item.setName(imgStr[j].toString());
                item.setIsCanel(false);
                imgItems.add(item);
                j++;
                System.out.println(file.getName());
            }
        }
        ExpressionItems item = new ExpressionItems();
        item.setId(R.drawable.qita_biaoqing_01);
        item.setName("itemCanel");
        item.setIsCanel(true);
        imgItems.add(item);
        final int Count = (int) Math.ceil(imgItems.size() / APP_PAGE_SIZE);
        Log.e(TAG, "size:" + imgItems.size() + " page:" + Count);
        mViewCount = Count;

        for (int i = 0; i < Count; i++) {
            GridView appPage = new GridView(this);
            appPage.setAdapter(new ExpressionImgAdapter(this, imgItems, i));
            appPage.setNumColumns(7);
            appPage.setHorizontalSpacing(10);
            appPage.setVerticalSpacing(10);
            appPage.setSelector(new ColorDrawable(Color.TRANSPARENT));
            appPage.setOnItemClickListener(listener);
            scrollLayout.addView(appPage);

            ImageView img = new ImageView(this);
            img.setBackgroundResource(R.drawable.qita_biaoqing_03);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 0, 10, 0);
            img.setLayoutParams(params);
            img.setEnabled(true);
            img.setTag(appPage.getId());
            imgLayout.addView(img);
        }
    }

    private void delStr(EditText edit) {
        int selectionStart = edit.getSelectionStart();// 获取光标的位置
        if (selectionStart > 0) {
            String body = edit.getText().toString();
            if (!(body.length() == 0)) {
                String tempStr = body.substring(0, selectionStart);
                String zhengze = "\\[[^\\]]+\\]";
                // 通过传入的正则表达式来生成一个pattern
                Pattern sinaPatten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);
                Matcher matcher = sinaPatten.matcher(tempStr);
                ArrayList<String> list = new ArrayList<String>();
                int t = 0;
                while (matcher.find()) {
                    list.add(matcher.group());
                    t++;
                }
                Log.i("t", t + "");
                if (t > 0) {
                    String key = list.get(t - 1);
                    Log.i("key", key);
                    // 获取最后一个表情的位置
                    int i = tempStr.lastIndexOf("[");
                    String temp = tempStr.substring(i, selectionStart);
                    Log.i("temp", temp);
                    if (key.equals(temp)) {
                        edit.getEditableText().delete(i, selectionStart);
                        t--;
                    } else {
                        edit.getEditableText().delete(tempStr.length() - 1, selectionStart);
                    }
                } else {
                    edit.getEditableText().delete(tempStr.length() - 1, selectionStart);
                }
                Log.i("删除表情", edit.getEditableText() + "");
            }
        }
    }

    public OnItemClickListener listener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ExpressionItems item = (ExpressionItems) parent.getItemAtPosition(position);
            if (item.getIsCanel()) {
                delStr(upcommentEdittext);
            } else {
                int d = item.getId();
                Drawable drawable = getResources().getDrawable(d);
                CharSequence cs = item.getName();
                SpannableString ss = new SpannableString(cs);
                if (drawable != null) {
                    Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), d);
                    DisplayMetrics mDisplayMetrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
                    int Screenwidth = mDisplayMetrics.widthPixels;
                    int width = 0;
                    if (Screenwidth > 1000) {
                        width = Screenwidth * 23 / 100;
                    } else {
                        width = Screenwidth * 13 / 100;
                    }
                    bitmap = Bitmap.createScaledBitmap(bitmap, width, width, true);
                    ImageSpan span = new ImageSpan(bitmap);
                    // drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    // drawable.getIntrinsicHeight());
                    // ImageSpan span = new ImageSpan(drawable,
                    // ImageSpan.ALIGN_BASELINE);
                    ss.setSpan(span, 0, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    int cursor = upcommentEdittext.getSelectionStart();
                    upcommentEdittext.getText().insert(cursor, ss);
                }
            }
        }
    };

    /**
     * 传入一个String对象，通过进行正则判断，返回一个SpannableString的集合
     *
     * @param context
     * @param str
     * @return
     */
    public List<SpannableString> getExpressionString(Context context, String str) {
        List<SpannableString> list = new ArrayList<SpannableString>();
        SpannableString spanString = new SpannableString(str);
        // 正则表达式比配字符串里是否含有表情，如： 我好[开心]啊
        String zhengze = "\\[[^\\]]+\\]";
        // 通过传入的正则表达式来生成一个pattern
        Pattern sinaPatten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);
        try {
            Matcher matcher = sinaPatten.matcher(str);
            int resId = 0;
            while (matcher.find()) {
                String key = matcher.group();
                if (matcher.start() < 0) {
                    continue;
                }
                for (ExpressionItems item : imgItems) {
                    if (item.getName().equals(key)) {
                        resId = item.getId();
                    }
                }
                if (resId == 0) {
                    continue;
                } else if (resId != 0) {
                    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
                    Display display = getWindowManager().getDefaultDisplay();
                    int Screenwidth = display.getWidth();
                    int width = 0;
                    if (Screenwidth > 1000) {
                        width = Screenwidth * 23 / 100;
                    } else {
                        width = Screenwidth * 13 / 100;
                    }
                    bitmap = Bitmap.createScaledBitmap(bitmap, width, width, true);
                    // 通过图片资源id来得到bitmap，用一个ImageSpan来包装
                    ImageSpan imageSpan = new ImageSpan(bitmap);
                    // 计算该图片名字的长度，也就是要替换的字符串的长度
                    int end = matcher.start() + key.length();
                    // 将该图片替换字符串中规定的位置中
                    spanString.setSpan(imageSpan, matcher.start(), end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }

            list.add(spanString);
        } catch (Exception e) {
            Log.e("dealExpression", e.getMessage());
        }
        return list;
    }

    public class ExpressionImgAdapter extends BaseAdapter {
        private List<ExpressionItems> mList;
        private Context mContext;
        public static final int APP_PAGE_SIZE = 21;

        public ExpressionImgAdapter(Context context, List<ExpressionItems> list, int page) {
            mContext = context;

            mList = new ArrayList<ExpressionItems>();
            int i = page * APP_PAGE_SIZE;
            int iEnd = i + APP_PAGE_SIZE;
            while ((i < list.size()) && (i < iEnd)) {
                mList.add(list.get(i));
                i++;
            }
        }

        public int getCount() {
            return mList.size();
        }

        public Object getItem(int position) {
            return mList.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ExpressionItems map = mList.get(position);
            ImgItem appItem;
            if (convertView == null) {
                View v = LayoutInflater.from(mContext).inflate(R.layout.app_item, null);

                appItem = new ImgItem();
                appItem.mAppIcon = (ImageView) v.findViewById(R.id.ivAppIcon);

                v.setTag(appItem);
                convertView = v;
            } else {
                appItem = (ImgItem) convertView.getTag();
            }
            appItem.mAppIcon.setBackgroundResource((Integer) map.getId());
            return convertView;
        }

        class ImgItem {
            ImageView mAppIcon;
        }
    }


    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            // TODO Auto-generated method stub
            String editable = upcommentEdittext.getText().toString();
            String str = filterEmoji(editable.toString());
            if (!editable.equals(str)) {
                upcommentEdittext.setText(str);
                //设置新的光标所在位置
                upcommentEdittext.setSelection(str.length());
                Toast.makeText(SportsPersonalMsg.this, "暂不支持此类符号输入!", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub
        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
        }
    };

    /**
     * 过滤emoji 或者 其他非文字类型的字符
     *
     * @param source
     * @return
     */
    public static String filterEmoji(String source) {

        if (!containsEmoji(source)) {
            return source;//如果不包含，直接返回
        }
        //到这里铁定包含
        StringBuilder buf = null;

        int len = source.length();

        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);

            if (!isEmojiCharacter(codePoint)) {
                if (buf == null) {
                    buf = new StringBuilder(source.length());
                }

                buf.append(codePoint);
            } else {
            }
        }

        if (buf == null) {
            return "";
        } else {
            if (buf.length() == len) {//这里的意义在于尽可能少的toString，因为会重新生成字符串
                buf = null;
                return source;
            } else {
                return buf.toString();
            }
        }

    }

    /**
     * 检测是否有emoji字符
     *
     * @param source
     * @return 一旦含有就抛出
     */
    public static boolean containsEmoji(String source) {
        if (source == null || source.equals("")) {
            return false;
        }

        int len = source.length();

        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);

            if (isEmojiCharacter(codePoint)) {
                //do nothing，判断到了这里表明，确认有表情字符
                return true;
            }
        }

        return false;
    }

    private static boolean isEmojiCharacter(char codePoint) {
        return !((codePoint == 0x0) ||
                (codePoint == 0x9) ||
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF)));
    }

    @Override
    public void setViewStatus() {
        mAdapter = new ChatMsgViewAdapter(this);

        mListView.setAdapter(mAdapter);
        mListView.setSelection(mListView.getBottom());
        mAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                mListView.setSelection(mAdapter.getCount() - 1);
            }
        });
        mListView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                // TODO Auto-generated method stub
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                IBinder token = arg0.getWindowToken();
                if (inputMethodManager.isActive() && token != null)
                    inputMethodManager.hideSoftInputFromWindow(token,
                            InputMethodManager.HIDE_NOT_ALWAYS);

                return false;
            }
        });

        mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                // TODO Auto-generated method stub
                copyString = ((UserPrimsgOne) parent.getItemAtPosition(position)).getCommentText();

                return false;
            }
        });

        mProgressDialog = new Dialog(mContext, R.style.sports_dialog);
        LayoutInflater mInflater = getLayoutInflater();
        View v = mInflater.inflate(R.layout.sports_progressdialog, null);
        TextView message = (TextView) v.findViewById(R.id.message);
        message.setText(R.string.loading);
        v.setMinimumWidth((int) (SportsApp.ScreenWidth * 0.8));
        mProgressDialog.setContentView(v);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        new InitialDataTask().execute();

    }

    @Override
    public void onPageDestroy() {
        // TODO Auto-generated method stub
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer = null;
        }
        mSportsApp = null;
        mContext = null;
    }

}