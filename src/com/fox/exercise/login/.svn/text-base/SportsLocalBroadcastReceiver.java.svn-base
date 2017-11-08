package com.fox.exercise.login;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import cn.ingenic.indroidsync.SportsApp;

import com.fox.exercise.MsgboxAdapter;
import com.fox.exercise.api.entity.UserDetail;
import com.fox.exercise.newversion.adapter.MsgboxNewAdapter;

public class SportsLocalBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "SportsLocalBroadcastReceiver";

    private static SportsLocalBroadcastReceiver mReceiver = new SportsLocalBroadcastReceiver();

    public static SportsLocalBroadcastReceiver getInstance() {
        if (mReceiver == null) {
            mReceiver = new SportsLocalBroadcastReceiver();
        }
        return mReceiver;
    }

    //	private TextView mTabMsgView = null;
    private SportsApp mSportsApp = SportsApp.getInstance();

//	private Context context;

    private int invitemsg;

//	private int foucsTotals;

    private int fans;

    private int visitors;

    private ArrayList<Integer> al;

    //	private MsgboxAdapter ma;
    private MsgboxNewAdapter ma;

    private int sysmsg;

//	public void setmTabMsgView(TextView mTabMsgView) {
//		this.mTabMsgView = mTabMsgView;
//	}

    private void update(UserDetail detail, boolean isAllUpdate) {
        if (detail == null)
            return;
        if (isAllUpdate) {
            int num = 0;
            if (detail != null && detail.getMsgCounts() != null) {
                num = detail.getMsgCounts().getTotal();
                Log.e(TAG, "num:" + num);
            }
//			if (detail != null && detail.getMsgCounts() != null
//					&& detail.getMsgCounts().getInvitesports() > 0) {
//					invitemsg = detail.getMsgCounts().getInvitesports();
//				al.set(3, invitemsg);
//			}
//			if (detail != null && detail.getActmsgs()> 0) {
//
//				foucsTotals = detail.getActmsgs();
//				al.set(0, foucsTotals);
//			}
//			if (detail != null && detail.getMsgCounts() != null
//					&& detail.getMsgCounts().getFans() > 0) {
//
//				fans = detail.getMsgCounts().getFans();
//				al.set(1, fans);
//			}
//			if (detail != null && detail.getMsgCounts() != null
//					&& detail.getMsgCounts().getSportVisitor() > 0) {
//
//				visitors = detail.getMsgCounts().getSportVisitor();
//
//				al.set(2, visitors);
//			}
//			if (detail != null && detail.getMsgCounts() != null
//					&& detail.getMsgCounts().getSysmsgsports() > 0) {
//
//				sysmsg = detail.getMsgCounts().getSysmsgsports();
//
//				al.set(4, sysmsg);
//			}
//			if (detail != null && detail.getMsgCounts() != null
//					&& detail.getMsgCounts().getInvitesports() > 0) {
//
//				sysmsg = detail.getMsgCounts().getInvitesports();
//
//				al.set(3, sysmsg);
//			}

            if (detail != null && detail.getMsgCounts() != null
                    && detail.getMsgCounts().getInvitesports() > 0) {
                invitemsg = detail.getMsgCounts().getInvitesports();
                al.set(2, invitemsg);
            }
            if (detail != null && detail.getMsgCounts() != null
                    && detail.getMsgCounts().getFans() > 0) {

                fans = detail.getMsgCounts().getFans();
                al.set(0, fans);
            }
            if (detail != null && detail.getMsgCounts() != null
                    && detail.getMsgCounts().getSportVisitor() > 0) {

                visitors = detail.getMsgCounts().getSportVisitor();

                al.set(1, visitors);
            }
            if (detail != null && detail.getMsgCounts() != null
                    && detail.getMsgCounts().getSysmsgsports() > 0) {

                sysmsg = detail.getMsgCounts().getSysmsgsports();

                al.set(3, sysmsg);
            }
            if (detail != null && detail.getMsgCounts() != null
                    && detail.getMsgCounts().getInvitesports() > 0) {

                sysmsg = detail.getMsgCounts().getInvitesports();

                al.set(2, sysmsg);
            }
            ma.notifyDataSetChanged();

        }

    }

    @Override
    public void onReceive(Context arg0, Intent intent) {
        if (mSportsApp == null) {
            if (SportsApp.getInstance() == null) {
                return;
            }
            mSportsApp = SportsApp.getInstance();
        }
//		this.context = arg0;
        if (SportAction.DETAIL_ACTION.equals(intent.getAction())) {
            UserDetail detail = (UserDetail) intent
                    .getSerializableExtra(SportAction.DETAIL_KEY);
            if (detail == null)
                return;
            if (detail != null) {
                if (al != null && ma != null)
                    update(detail, true);
            }
        } else if (SportAction.UPDATE_TAB_MSG_ACTION.equals(intent.getAction())) {
            if (al != null && ma != null)
                update(mSportsApp.getSportUser(), false);
        }
    }

    public void setList(ArrayList<Integer> al) {
        // TODO Auto-generated method stub
        this.al = al;
    }

    public void setMsgAdapter(MsgboxNewAdapter ma) {
        // TODO Auto-generated method stub
        this.ma = ma;
    }

}
