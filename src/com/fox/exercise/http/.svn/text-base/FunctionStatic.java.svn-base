package com.fox.exercise.http;

import com.fox.exercise.R;

import android.content.Context;

public class FunctionStatic {

    public static final int FUNCTION_MYSPORTS = 0;
    public static final int FUNCTION_RANKING = 1;
    public static final int FUNCTION_START_SPORTS = 2;
    public static final int FUNCTION_INVITE = 3;
    public static final int FUNCTION_ME = 4;
    public static final int FUNCTION_OTHER_USER = 5;
    public static final int FUNCTION_INVITEME_LIST = 6;
    public static final int FUNCTION_NEW_FRIEND = 7;
    public static final int FUNCTION_HISTORY = 8;
    public static final int FUNCTION_SPORTS_DETAIL = 9;
    public static final int FUNCTION_MEDIA_VIEW = 10;
    public static final int FUNCTION_MSGBOX = 11;
    public static final int FUNCTION_PRIVATE_MSG_LIST = 12;
    public static final int FUNCTION_PRIVATE_MSG_CONTENT = 13;
    public static final int FUNCTION_FRIEND_ACTION = 14;
    public static final int FUNCTION_VISITER = 15;
    public static final int FUNCTION_DOWNLOAD_MAP_GAODE = 16;
    public static final int FUNCTION_DOWNLOAD_MAP_BAIDU = 17;

    public static String[] fun_name = {
            "我的运动",//0
            "排行",//1
            "运动中",//2
            "约跑",//3
            "我",//4
            "其他用户",//5
            "约我的人",//6
            "好友请求",//7
            "历史记录",//8
            "记录详情",//9
            "浏览多媒体",//10
            "消息盒子",//11
            "私信列表",//12
            "私信内容",//13
            "好友动态",//14
            "访客",//15
            "高德地图下载",//16
            "百度地图下载"//17
    };

    public static String[][] game_id = {
            {"5001", "普通"},
            {"50011", "360"},
            {"50012", "豌豆荚"},
            {"50013", "米赚"},
            {"50014", "联通"},
            {"50015", "百度"},
            {"50016", "安智"},
            {"50017", "机锋"},
            {"50018", "安卓"},
            {"50019", "华为"},
            {"500110", "搜狗"},
            {"500111", "当乐"},
            {"500112", "OPPO"},
            {"500113", "酷派"},
            {"500114", "小米"},
            {"500115", "Htc"},
            {"500116", "应用汇"},
            {"500117", "索爱"},
            {"500118", "飞流"},
            {"500119", "泡椒"},
            {"500120", "内置"},
            {"500121", "联想"},
            {"500122", "木蚂蚁"},
            {"500123", "应用宝"},
            {"500124", "官网"},
            {"500125", "淘宝"},
            {"500126", "美美相机推荐"},
            {"500127", "魅族"},
            {"500128", "TV大厅"},
            {"500129", "京东"},
            {"500130", "金立"},
            {"500131", "PP助手"},
            {"500132", "集团内置"},
            {"500133", "MM商店"},
    };

    public static long onResume() {
        return System.currentTimeMillis();
    }

    public static String getGameId(Context context) {
        String id = context.getString(R.string.config_game_id);
        String name = "";
        for (int i = 0; i < game_id.length; i++) {
            if (game_id[i][0].equals(id)) {
                name = game_id[i][1];
            }
        }
        return id + "-" + name;
    }

    public static String getGameNameById(Context context) {
        String id = context.getString(R.string.config_game_id);
        String name = "";
        for (int i = 0; i < game_id.length; i++) {
            if (game_id[i][0].equals(id)) {
                name = game_id[i][1];
            }
        }
        return name;
    }

    public static void onPause(Context context, int funcId, long preTime) {
        long time = (System.currentTimeMillis() - preTime) / 1000;

        StatCounts.ReportFunctionIdCount(context,
                getGameId(context),
                Integer.toString(funcId),
                fun_name[funcId],
                Long.toString(time));
    }
}
