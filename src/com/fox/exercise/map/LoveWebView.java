package com.fox.exercise.map;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.webkit.ConsoleMessage;
import android.webkit.ConsoleMessage.MessageLevel;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class LoveWebView extends WebView {
    private static final String TAG = "LoveWebView";

    private static final int URL_INIT_MAP = 1; // 初始化地图
    private static final int URL_ZOOM_IN = 2; // 放大地图
    private static final int URL_ZOOM_OUT = 3; // 缩小地图
    private static final int URL_MAP_TYPE = 4; // 切换地图类型
    private static final int URL_NEED_GET_ADDR = 5; // 需要获取详细地址
    private static final int URL_ADD_POS = 6; // 添加历史位置标注
    private static final int URL_PAN_POS = 7; // 将某个历史位置标注移到地图中心
    private static final int URL_ADD_ME = 8; // 添加我到地图上
    private static final int URL_ADD_USR = 9; // 添加用户
    private static final int URL_FINISH_ADD = 10; // 添加完成
    private static final int URL_SHOW_USR_INFO = 11; // 显示用户(包括我)信息
    private static final int URL_UPD_USR_LOC = 12; // 更新用户位置
    private static final int URL_UPD_USR_ICO = 13; // 更新用户图标
    private static final int URL_DEL_USR = 14; // 删除用户
    private static final int URL_ADD_POI_TYPE = 15; // 设置兴趣点是否显示
    private static final int URL_REMOVE_USRS = 16; // 移除其他用户头像
    private static final int URL_SET_MAP_CENTER = 17; // 使地图居中
    private static final int URL_SET_POI = 18; // 设置兴趣点是否显示
    private static final int URL_UPD_POI = 19; // 更新兴趣点

    private String getJsUrl(int type, Object... strs) {
        StringBuffer js = new StringBuffer("javascript:");
        switch (type) {
            case URL_SET_MAP_CENTER: {
                js.append(String.format("setMapCenter(%s, %s)", strs));
                break;
            }
            case URL_INIT_MAP: {
                js.append(String.format("initMap(%s, %s, %s, %s, %s)", strs));
                break;
            }
            case URL_ZOOM_IN: {
                js.append(String.format("zoomIn()"));
                break;
            }
            case URL_ZOOM_OUT: {
                js.append(String.format("zoomOut()"));
                break;
            }
            case URL_MAP_TYPE: {
                js.append(String.format("changeMapType(%s)", strs));
                break;
            }
            case URL_NEED_GET_ADDR: {
                js.append(String.format("needGetAddress(%s, %s, %s, %s)", strs));
                break;
            }
            case URL_ADD_POS: {
                js.append(String.format("addPosToMap(%s, %s, %s, %s)", strs));
                break;
            }
            case URL_PAN_POS: {
                js.append(String.format("setLabelCenter(%s)", strs));
                break;
            }
            case URL_ADD_ME: {
                js.append(String.format("addMeToMap(%s, %s, %s, '%s')", strs));
                break;
            }
            case URL_ADD_USR: {
                js.append(String.format("addUserToMap(%s, %s, %s, '%s', %s)", strs));
                break;
            }
            case URL_FINISH_ADD: {
                js.append(String.format("finishAdd()"));
                break;
            }
            case URL_SHOW_USR_INFO: {
                js.append(String.format("showUserInfo(%s)", strs));
                break;
            }
            case URL_UPD_USR_LOC: {
                js.append(String.format("updateUserInfo(%s, %s, %s)", strs));
                break;
            }
            case URL_UPD_USR_ICO: {
                js.append(String.format("updateUserIcon(%s, '%s')", strs));
                break;
            }
            case URL_DEL_USR: {
                js.append(String.format("deleteUser(%s)", strs));
                break;
            }
            case URL_ADD_POI_TYPE: {
                js.append(String.format("addPoiType(%s)", strs));
                break;
            }
            case URL_SET_POI: {
                js.append(String.format("setPoiType(%s, %s)", strs));
                break;
            }
            case URL_REMOVE_USRS: {
                js.append(String.format("removeUsers()"));
                break;
            }
            case URL_UPD_POI: {
                js.append(String.format("refreshMapPoi()"));
                break;
            }
//			private static final int URL_GET_MY_ADDR = 13;
            default: {
                Log.e(TAG, "URL is error");
                break;
            }
        }

        Log.v("sss", "js:" + js.toString());

        return js.toString();
    }

    public void setMapCenter(String lat, String lng) {
        loadUrl(getJsUrl(URL_SET_MAP_CENTER, lng, lat));
    }

    public void setMapCenter(double lat, double lng) {
        loadUrl(getJsUrl(URL_SET_MAP_CENTER, lng, lat));
    }

    public void addMeToMap(String tel, String lat, String lng, String icon) {
        loadUrl(getJsUrl(URL_ADD_ME, tel, lng, lat, icon));
    }

    public LoveWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public LoveWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoveWebView(Context context) {
        super(context);
    }

    private Handler handler;

    public void init(WebViewActivity act, Handler handler) {
        Log.v(TAG, "LoveWebView开始加载页面");
        Log.v(TAG, "&&&&&&&&&&&&&&进入webview init函数 4");
        this.handler = handler;
        loadUrl(SysConfig.WEBVIEW_URI);
        getSettings().setJavaScriptEnabled(true);
        {
            try {
                WebSettings wWebSettings = getSettings();
                int screenDensity = getDensity(act);
                WebSettings.ZoomDensity zoomDensity = WebSettings.ZoomDensity.MEDIUM;

                switch (screenDensity) {
                    case DisplayMetrics.DENSITY_LOW:
                        zoomDensity = WebSettings.ZoomDensity.CLOSE;
                        break;
                    case DisplayMetrics.DENSITY_MEDIUM:
                        zoomDensity = WebSettings.ZoomDensity.MEDIUM;
                        break;
                    case DisplayMetrics.DENSITY_HIGH:
                        zoomDensity = WebSettings.ZoomDensity.FAR;
                        break;
                }
                wWebSettings.setDefaultZoom(zoomDensity);
            } catch (NullPointerException e) {

            }
        }
        setWebViewClient(mWebViewClient);
        setWebChromeClient(mWebChromeClient);
        addJavascriptInterface(act, "handler");
    }

    private int switchType = 0;

    private int getDensity(WebViewActivity act) {

        DisplayMetrics metrics = new DisplayMetrics();
        Display display = act.getWindowManager().getDefaultDisplay();

        display.getMetrics(metrics);
        return metrics.densityDpi;

    }

    private WebViewClient mWebViewClient = new WebViewClient() {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            Log.e(TAG, "...............error2:" + errorCode);
        }

        ;

        public void onReceivedSslError(WebView view,
                                       android.webkit.SslErrorHandler handler,
                                       android.net.http.SslError error) {
            Log.e(TAG, "...............error1:" + error);
        }

        ;

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (handler != null) {
                // Log.v(TAG, "LoveWebView加载页面完成");
                // handler.sendEmptyMessage(IHMConstant.HM_MTA_INITED_WEBVIEW);
            } else {
                Log.e(TAG, "...............handler == null");
            }
        }
    };
    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        public void onProgressChanged(WebView view, int newProgress) {
            Log.v(TAG, "LoveWebView加载页面进度：" + newProgress);
            if (newProgress == 100) {
                Log.v(TAG, "&&&&&&&&&&&&&&webview初始化完成 5");
                handler.sendEmptyMessage(IHMConstant.HM_MTA_INITED_WEBVIEW);

            }
        }

        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            Log.v(TAG, "???"
                    + (consoleMessage.messageLevel() == MessageLevel.ERROR));
            Log.v(TAG, "???" + consoleMessage.lineNumber());
            Log.v(TAG, "???" + consoleMessage.message());
            Log.v(TAG, "???" + consoleMessage.sourceId());

            if (consoleMessage.messageLevel() == MessageLevel.ERROR) {
                handler.sendEmptyMessage(IHMConstant.HMT_WV_ERROR);
            }

            return false;
        }

        ;
    };

    //js.append(String.format("initMap(%s, %s, %s)", strs));
    public void initMap(double lat, double lng, int mapPoiType, int mapType) {
        Log.v(TAG, "执行初始化地图js代码");
        Log.v(TAG, "&&&&&&&&&&&&&&执行js初始化地图 9");
        Log.v(TAG, "-------------- unregisterLocationReceiver 14");
        loadUrl(getJsUrl(URL_INIT_MAP, lng, lat, mapPoiType, false, mapType));
    }

    public void initMap(String lat, String lng, int mapPoiType, int mapType) {
        loadUrl(getJsUrl(URL_INIT_MAP, lng, lat, mapPoiType, false, mapType));
    }

    public void initMap(double lat, double lng, int mapPoiType, boolean isAddFlag, int mapType) {
        loadUrl(getJsUrl(URL_INIT_MAP, lng, lat, mapPoiType, isAddFlag, mapType));
    }

    //js.append(String.format("zoomIn()"));
    public void addZoom() {
        loadUrl(getJsUrl(URL_ZOOM_IN));
    }

    //js.append(String.format("zoomOut()"));
    public void subZoom() {
        loadUrl(getJsUrl(URL_ZOOM_OUT));
    }

    //js.append(String.format("changeMapType(%s)", strs));
    public void changedMapType() {
        loadUrl(getJsUrl(URL_MAP_TYPE, switchType++ % 3));
    }

    public void setMapType(int type) {
        loadUrl(getJsUrl(URL_MAP_TYPE, type));
    }

    //js.append(String.format("needGetAddress(%s, %s, %s)", strs));
    public void needGetAddress(String tel, double lat, double lng, String time) {
        loadUrl(getJsUrl(URL_NEED_GET_ADDR, tel, lng, lat, time));
    }

    //js.append(String.format("addPosToMap(%s, %s, %s)", strs));
    public void addPosToMap(int time, int index, double lat, double lng) {
        loadUrl(getJsUrl(URL_ADD_POS, time, index, lng, lat));
    }

    public void addPosToMap(int time, String index, String lat, String lng) {
        loadUrl(getJsUrl(URL_ADD_POS, time, index, lng, lat));
    }

    //js.append(String.format("setLabelCenter(%s)", strs));
    public void setLabelCenter(int index) {
        loadUrl(getJsUrl(URL_PAN_POS, index));
    }

    //js.append(String.format("addMeToMap(%s, %s, %s, '%s')", strs));
    public void addMeToMap(String tel, double lat, double lng, String icon) {
        loadUrl(getJsUrl(URL_ADD_ME, tel, lng, lat, icon));
    }

    //js.append(String.format("addUserToMap(%s, %s, %s, '%s')", strs));
    public void addUserToMap(String tel, double lat, double lng, String icon, String time) {
        loadUrl(getJsUrl(URL_ADD_USR, tel, lng, lat, icon, time));
    }

    public void addUserToMap(String tel, String lat, String lng, String icon, String time) {
        loadUrl(getJsUrl(URL_ADD_USR, tel, lng, lat, icon, time));
    }

    //js.append(String.format("finishAdd()"));
    public void addUserFinish() {
        loadUrl(getJsUrl(URL_FINISH_ADD));
    }

    //js.append(String.format("showUserInfo(%s)", strs));
    public void showUserInfo(String tel) {
        loadUrl(getJsUrl(URL_SHOW_USR_INFO, tel));
    }

    //js.append(String.format("updateUserInfo(%s, %s, %s)", strs));
    public void updateUserInfo(String tel, double lat, double lng) {
        loadUrl(getJsUrl(URL_UPD_USR_LOC, tel, lng, lat));
    }

    public void updateUserInfo(String tel, String lat, String lng) {
        loadUrl(getJsUrl(URL_UPD_USR_LOC, tel, lng, lat));
    }

    //js.append(String.format("updateUserIcon(%s, '%s')", strs));
    public void updateUserIcon(String tel, String icon) {
        loadUrl(getJsUrl(URL_UPD_USR_ICO, tel, icon));
    }

    //js.append(String.format("deleteUser(%s)", strs));
    public void deleteUser(String tel) {
        loadUrl(getJsUrl(URL_DEL_USR, tel));
    }

//	public void refreshPoi(){
//		loadUrl(getJsUrl(URL_REFRESH_POI));
//	}

    public void setPoiType(String index, int flag) {
        loadUrl(getJsUrl(URL_SET_POI, index, flag));
    }

    public void removeUsers() {
        loadUrl(getJsUrl(URL_REMOVE_USRS));
    }

    public void updatePoi() {
        loadUrl(getJsUrl(URL_UPD_POI));
    }
}
