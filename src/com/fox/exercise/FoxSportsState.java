package com.fox.exercise;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.fox.exercise.api.entity.UserDetail;
import com.fox.exercise.http.FunctionStatic;
import com.fox.exercise.login.SportsLocalBroadcastReceiver;
import com.fox.exercise.newversion.adapter.MsgboxNewAdapter;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import cn.ingenic.indroidsync.SportsApp;

public class FoxSportsState extends AbstractBaseOtherActivity {
    private static final int FOUCS = 0;
    private static final int FANS = 1;
    private static final int VISITOR = 2;
    private static final int COMMENTS = 3;
    private static final int CALLME = 4;
    private static final int SYSMESSAGE = 5;
    private static final int SYSMSG = 6;
    public int foucsTotals = 0;
    //    private ListView msgbox_lv;
    private ArrayList<Integer> al;
    private MsgboxNewAdapter ma;
    private UserDetail ud;
    private static SharedPreferences spf;
    private long preTime = 0;
//    private View message_top_view, message_bottom_view;

    private SportsApp mSportsApp;

    @Override
    public void initIntentParam(Intent intent) {
        // TODO Auto-generated method stub
        title = getResources().getString(R.string.sports_message_box);
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        showContentView(R.layout.sports_message_box);
        PrivateMsgFragment fragment = new PrivateMsgFragment();
        setFragment(fragment, null);
//        msgbox_lv = (ListView) findViewById(R.id.msgbox_lv);
//        message_top_view = findViewById(R.id.message_top_view);
//        message_bottom_view = findViewById(R.id.message_bottom_view);
        mSportsApp = SportsApp.getInstance();
//		List<PrivateMsgStatus> localList = mSportsApp.getSportsDAO().queryPrivateMsgAll(
//				PrivateMessageAllTable.TABLE_NAME, mSportsApp.getSportUser().getUid());
//		if (localList!=null&&localList.size()>0) {
//			message_top_view.setVisibility(View.VISIBLE);
//			message_bottom_view.setVisibility(View.VISIBLE);
//		}else if(mSportsApp.getSportUser().getMsgCounts() != null&&mSportsApp.getSportUser().getMsgCounts().getPrimsg()>0){
//			message_top_view.setVisibility(View.VISIBLE);
//			message_bottom_view.setVisibility(View.VISIBLE);
//		}else {
//			message_top_view.setVisibility(View.GONE);
//			message_bottom_view.setVisibility(View.GONE);
//		}

    }

    @Override
    public void setViewStatus() {
        // TODO Auto-generated method stub
        al = new ArrayList<Integer>();
        ud = SportsApp.getInstance().getSportUser();
//		al.add(ud.getActmsgs());
        al.add(ud.getMsgCounts().getFans());
        al.add(ud.getMsgCounts().getSportVisitor());
//		al.add(ud.getMsgCounts().getInvitesports());
        al.add(ud.getMsgCounts().getSysmsgsports());
//        ma = new MsgboxNewAdapter(this, al);
//        msgbox_lv.setAdapter(ma);
//        msgbox_lv.setOnItemClickListener(new mListener());
        foucsTotals = ud.getFcount() + ud.getMsgCounts().getfFollows();
        spf = getSharedPreferences("sports", 0);
        SportsLocalBroadcastReceiver sbr = SportsLocalBroadcastReceiver.getInstance();
        sbr.setList(al);
        sbr.setMsgAdapter(ma);
    }

    @Override
    public void onPageResume() {
        // TODO Auto-generated method stub
        preTime = FunctionStatic.onResume();
        MobclickAgent.onPageStart("FoxSportsState");
    }

    @Override
    public void onPagePause() {
        // TODO Auto-generated method stub
        FunctionStatic.onPause(this, FunctionStatic.FUNCTION_MSGBOX, preTime);
        MobclickAgent.onPageEnd("FoxSportsState");
    }

    @Override
    public void onPageDestroy() {
        // TODO Auto-generated method stub

    }

    class mListener implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
            // TODO Auto-generated method stub
//			if (position == 0) {
//				gotoFocus();
//			} else if (position == 1) {
//				gotoFans();
//			} else if (position == 2) {
//				gotoVisitor();
//			} else if (position == 4) {
//				gotoSysmsg();
//			}else if (position==3) {
//				gotoCallMe();
//			}/*else if (position == 3) {
//				gotoPrivateMessage();
//			} */
            if (position == 0) {
                gotoFans();
            } else if (position == 1) {
                gotoVisitor();
            } else if (position == 2) {
//				gotoCallMe();
                gotoSysmsg();
            }

        }

    }

//	private void gotoFocus() {
//		Intent foucsIntent = new Intent(this, FoxSportsFocus.class);
//		startActivityForResult(foucsIntent, FOUCS);
//	}

    private void gotoFans() {
        Intent intent = new Intent();
        intent.setClass(this, FansListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("number", ud.getFanCounts());
        bundle.putInt("type", FansAndNear.SELF_FANS);
        bundle.putInt("uid", 0);
        intent.putExtras(bundle);
        startActivityForResult(intent, FANS);
    }

    private void gotoVisitor() {
        startActivityForResult((new Intent(this, VisitorMyActivity.class)), VISITOR);
    }

    private void gotoCallMe() {
        Intent iinvite = new Intent(this,
                InviteSportsActivity.class);
        startActivityForResult(iinvite, CALLME);
    }


    private void gotoSysmsg() {
        Intent privateIntent = new Intent(this, SysMessageMyActivity.class);
        startActivityForResult(privateIntent, SYSMSG);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
//		case FOUCS:
//			al.set(0, 0);
//			ma.notifyDataSetChanged();
//			ud.setActmsgs(0);
//			break;
            case FANS:
                al.set(0, 0);
                ma.notifyDataSetChanged();
                ud.getMsgCounts().setFans(0);
                break;
            case VISITOR:
                al.set(1, 0);
                ma.notifyDataSetChanged();
                ud.getMsgCounts().setSportVisitor(0);
                break;
        /*case PRIVATE:
            al.set(3, 0);
			ma.notifyDataSetChanged();
			Editor editor = spf.edit();
			editor.putInt("primsgcount", 0);
			editor.commit();
			ma.notifyDataSetChanged();
			ud.getMsgCounts().setPrimsg(0);
			break;*/
            case CALLME:
                al.set(2, 0);
                ma.notifyDataSetChanged();
                ud.getMsgCounts().setInvitesports(0);
                break;
            case SYSMSG:
                al.set(2, 0);
                ma.notifyDataSetChanged();
                ud.getMsgCounts().setSysmsgsports(0);
                break;
        }
    }

    public void setFragment(Fragment fragment, Bundle bundle) {
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

//    public void setGone() {
//        message_top_view.setVisibility(View.GONE);
//       message_bottom_view.setVisibility(View.GONE);
//    }

//    public void setViewVisible() {
//        message_top_view.setVisibility(View.VISIBLE);
//        message_bottom_view.setVisibility(View.VISIBLE);
//    }

}
