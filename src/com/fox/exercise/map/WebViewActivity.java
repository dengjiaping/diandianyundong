package com.fox.exercise.map;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;


public abstract class WebViewActivity extends Activity implements IHMConstant {
    private static final String TAG = "WebViewActivity";
    private Handler handler;
    private static final String UNDEFINED = "undefined";
    public static final int MAP_TYPE_1 = 1; // 有兴趣点显示
    public static final int MAP_TYPE_0 = 0; // 没有兴趣点显示

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        handler = provideHandler();
    }

    public void log(String str) {
        Log.v("debug", "-js-" + str);
    }

    public abstract Handler provideHandler();

    public void setProvideHandler(Handler handler) {
        this.handler = handler;
    }

    /**
     * 显示兴趣点信息
     *
     * @param params
     */
    public void showPoiInfo(String title, String address,
                            String city, String phoneNumber, String postcode,
                            String provice) {
        if (handler == null) {
            Log.v("WebViewActivity", "-js-" + "provideHandler is null");
            return;
        }
        Message msg = handler.obtainMessage();
        msg.what = HM_MTA_POI_INFO;
        msg.obj = concatPoiInfo(title, address,
                city, phoneNumber, postcode,
                provice);
        msg.sendToTarget();
    }

    private String infoTitle;

    protected String getPoiInfoTitle() {
        if (infoTitle == null) {
            return "";
        }
        return infoTitle;
    }

    public void getAddress(String tel, String addr, String time) {
        Log.v(TAG, "getMyAddress:" + addr);
        if (handler == null) {
            Log.v(TAG, "-js-" + "provideHandler is null");
            return;
        }
        Log.v(TAG, "-js-" + "tel:" + tel + " addr:" + addr);
        if (tel == null || UNDEFINED.equals(tel) || "".equals(tel) ||
                addr == null || UNDEFINED.equals(addr) || "".equals(addr)) {
            return;
        }
        Message msg = handler.obtainMessage();
        msg.what = HMT_WV_GET_ADDR;
        msg.obj = new ParamObj(tel, addr, time, null, null);
        handler.sendMessage(msg);
    }

    private String concatPoiInfo(String title, String infoAddress,
                                 String infoCity, String infoPhoneNumber, String infoPostcode,
                                 String infoProvice) {
        infoTitle = title;

        Resources rs = getResources();

	/*	StringBuilder sb = new StringBuilder();
        if (infoCity != null && !infoCity.equals(UNDEFINED)) {
			sb.append(rs.getString(R.string.map_ad_city) + infoCity + "<br>");
		}
		if (infoAddress != null && !infoAddress.equals(UNDEFINED)) {
			sb.append(rs.getString(R.string.map_ad_address) + infoAddress + "<br>");
		}
		if (infoPhoneNumber != null && !infoPhoneNumber.equals(UNDEFINED)) {
			String[] nums = infoPhoneNumber.split(",");
			sb.append(rs.getString(R.string.map_ad_phonenum));
			boolean flag = false;
			for (String num : nums) {
				if (flag == true) {
					sb.append(" , ");
				}
				flag = true;
				sb.append("<a href=\"tel:" + num + "\">" + num + "</a>");
			}
			sb.append("<br>");
		}
		if (infoPostcode != null && !infoPostcode.equals(UNDEFINED)) {
			sb.append(rs.getString(R.string.map_ad_postcode) + infoPostcode
					+ "<br>");
		}
		if (infoProvice != null && !infoProvice.equals(UNDEFINED)) {
			sb.append(rs.getString(R.string.map_ad_provice) + infoProvice
					+ "<br>");
		}
		return sb.toString();*/
        return "";
    }

    /**
     * 显示操作面板
     *
     * @param tel
     * @param address
     */
    public void showUserOptPanel(String tel, String lat, String lng) {
        if (handler == null) {
            Log.v("WebViewActivity", "-js-" + "provideHandler is null");
            return;
        }

        Message msg = handler.obtainMessage();
        msg.what = HM_WV_SHOWOPPANEL;
        msg.obj = new ParamObj(tel, null, null, lat, lng);
        msg.sendToTarget();
    }

    public void updateAddress(String tel, String address, String time) {
        ParamObj obj = new ParamObj(tel, address, time, null, null);

        Message msg = handler.obtainMessage();
        msg.what = HMT_WV_UPDATE_USER_ADDR;
        msg.obj = obj;
        msg.sendToTarget();
    }

    protected class ParamObj {
        public ParamObj(String tel, String addr, String time, String lat, String lng) {
            this.optAddr = addr;
            this.index = tel;
            this.optTime = time;
            this.lat = lat;
            this.lng = lng;
        }

        public String index;
        public String optAddr;
        public String optTime;
        public String lat;
        public String lng;
    }

    public void postAddress(String index, String address, String time) {
        if (handler == null) {
            Log.v("WebViewActivity", "-js-" + "provideHandler is null");
            return;
        }

        ParamObj obj = new ParamObj(index, address, time, null, null);

        Message msg = handler.obtainMessage();
        msg.what = HMT_W_POST_ADDR;
        msg.obj = obj;
        msg.sendToTarget();
    }

    public void postIndexAddress(String time, String index, String address) {
        if (handler == null) {
            Log.v("WebViewActivity", "-js-" + "provideHandler is null");
            return;
        }

        ParamObj obj = new ParamObj(index, address, time, null, null);

        Message msg = handler.obtainMessage();
        msg.what = HMT_W_POST_ADDR;
        msg.obj = obj;
        msg.sendToTarget();
    }

    public void showPosInfo(int index) {
        if (handler == null) {
            Log.v("WebViewActivity", "-js-" + "provideHandler is null");
            return;
        }

        Message msg = handler.obtainMessage();
        msg.what = HM_HA_SHOW_POS_INFO;
        msg.arg1 = index;
        msg.sendToTarget();
    }

    /**
     * 处理Web信息
     * 如页面加载完成，地图加载完成，地图最大，地图最小
     *
     * @param msgType
     */
    public void handleMessage(int msgType) {
        if (handler == null) {
            Log.v("WebViewActivity", "-js-" + "provideHandler is null");
            return;
        }

        Log.v("WebViewActivity", "-js-" + "handleMessage " + msgType);
        Message msg = handler.obtainMessage();
        msg.what = msgType;
        msg.sendToTarget();
    }
}
