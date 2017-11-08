package com.fox.exercise.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.fox.exercise.CurrentTimeList;
import com.fox.exercise.OnlineAction;
import com.fox.exercise.R;
import com.fox.exercise.SportsType;
import com.fox.exercise.activityinfo2;
import com.fox.exercise.api.entity.ActListInfo;
import com.fox.exercise.api.entity.ActionInfo;
import com.fox.exercise.api.entity.ActionInfos;
import com.fox.exercise.api.entity.ActionList;
import com.fox.exercise.api.entity.ActionRankList;
import com.fox.exercise.api.entity.ActivityInfo;
import com.fox.exercise.api.entity.AdContent;
import com.fox.exercise.api.entity.AddFindItem;
import com.fox.exercise.api.entity.Ads;
import com.fox.exercise.api.entity.ApplyList;
import com.fox.exercise.api.entity.FindComment;
import com.fox.exercise.api.entity.FindMore;
import com.fox.exercise.api.entity.FindMore2;
import com.fox.exercise.api.entity.GetPeisu;
import com.fox.exercise.api.entity.Gifts;
import com.fox.exercise.api.entity.Help;
import com.fox.exercise.api.entity.ImageDetail;
import com.fox.exercise.api.entity.ImgSearch;
import com.fox.exercise.api.entity.InviteUser;
import com.fox.exercise.api.entity.ItemsCate;
import com.fox.exercise.api.entity.LikesAndWavtimes;
import com.fox.exercise.api.entity.MadelInfo;
import com.fox.exercise.api.entity.MsgCounts;
import com.fox.exercise.api.entity.NewCommentInfo;
import com.fox.exercise.api.entity.Notification;
import com.fox.exercise.api.entity.PeisuInfo;
import com.fox.exercise.api.entity.PhotoFrames;
import com.fox.exercise.api.entity.PicsAndIds;
import com.fox.exercise.api.entity.SportMediaFile;
import com.fox.exercise.api.entity.SportTask;
import com.fox.exercise.api.entity.SquareInfo;
import com.fox.exercise.api.entity.SysMsg;
import com.fox.exercise.api.entity.UserBlackList;
import com.fox.exercise.api.entity.UserComment;
import com.fox.exercise.api.entity.UserDetail;
import com.fox.exercise.api.entity.UserFollowMsg;
import com.fox.exercise.api.entity.UserGift;
import com.fox.exercise.api.entity.UserMeComments;
import com.fox.exercise.api.entity.UserMsg;
import com.fox.exercise.api.entity.UserMyLikes;
import com.fox.exercise.api.entity.UserNearby;
import com.fox.exercise.api.entity.UserPrimsgAll;
import com.fox.exercise.api.entity.UserPrimsgOne;
import com.fox.exercise.api.entity.UserRank;
import com.fox.exercise.api.entity.UserSearch;
import com.fox.exercise.api.entity.UserSports;
import com.fox.exercise.newversion.UUIDGenerator;
import com.fox.exercise.newversion.bushutongji.BuShuTongJiDetail;
import com.fox.exercise.newversion.entity.CircleFindContent;
import com.fox.exercise.newversion.entity.CircleFindDetailContent;
import com.fox.exercise.newversion.entity.CircleFinds;
import com.fox.exercise.newversion.entity.CircleFindsAd;
import com.fox.exercise.newversion.entity.CircleFindsCat;
import com.fox.exercise.newversion.entity.CircleFindsLists;
import com.fox.exercise.newversion.entity.ExternalActivi;
import com.fox.exercise.newversion.entity.FindGroup;
import com.fox.exercise.newversion.entity.PointsSay;
import com.fox.exercise.newversion.entity.PraiseUsers;
import com.fox.exercise.newversion.entity.ShopPayDingDan;
import com.fox.exercise.newversion.entity.SleepEffect;
import com.fox.exercise.newversion.entity.SportCircleComments;
import com.fox.exercise.newversion.entity.SportRecord;
import com.fox.exercise.newversion.entity.SportsLike;
import com.fox.exercise.newversion.entity.SysSportCircleComments;
import com.fox.exercise.newversion.entity.TodayStepBean;
import com.fox.exercise.newversion.entity.TopicContent;
import com.fox.exercise.newversion.entity.TrainAction;
import com.fox.exercise.newversion.entity.TrainInfo;
import com.fox.exercise.newversion.entity.TrainPlanList;
import com.fox.exercise.newversion.entity.TrainUserInfo;
import com.fox.exercise.newversion.entity.WXPaylist;
import com.fox.exercise.newversion.entity.WXxdback;
import com.fox.exercise.newversion.entity.YeDindanInfo;
import com.fox.exercise.newversion.entity.YePaoInfo;
import com.fox.exercise.pedometer.SportConditionDetail;
import com.fox.exercise.pedometer.SportContionTaskDetail;
import com.fox.exercise.util.SportTaskUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.ingenic.indroidsync.SportsApp;

public class ApiJsonParser {
    private static final String TAG = "ApiJsonParser";

    public static ApiBack registerPhone(String username, String pwd,
                                        String phoneno, String sex, int regfromtypeId)
            throws ApiNetException {
        return register(username, pwd, phoneno, "", sex, regfromtypeId);
    }

    // regist with phone
    public static ApiBack registerPhone(String username, String pwd,
                                        String phoneno, String picAddress, String sex, int regfromtypeId)
            throws ApiNetException {
        ApiMessage msg = Api.register(username, pwd, phoneno, picAddress, sex,
                regfromtypeId);
        ApiBack back = null;
        if (msg.isFlag()) {
            back = new ApiBack();
            String content = msg.getMsg();
            if (content.contains(SportsApp.getInstance().getResources().getString(R.string.user_system))){
                back.setFlag(-100);
                back.setMsg("checkyour_WIFI_isconnect");
                return back;
            }else{ try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                back.setFlag(jsonObject.getInt("flag"));
                back.setMsg(jsonObject.getString("msg"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
                return back;
            }
        } else {
            if (msg != null && ("SocketTimeoutException").equals(msg.getMsg())){
                String content = msg.getMsg();
                back.setFlag(-56);
                back.setMsg(content);
                return back;
            }else{
                throw new ApiNetException("error:" + msg.getMsg());
            }
        }
    }

    // forget pwd with phone
    public static ApiBack forgetPwdPhone(String phone) throws ApiNetException {
        ApiMessage msg = Api.forgetPwdPhone(phone);
        ApiBack back = new ApiBack();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                back.setFlag(jsonObject.getInt("flag"));
                back.setMsg(jsonObject.getString("msg"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return back;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // //
    public static LikesAndWavtimes likesAndWav(int picId)
            throws ApiNetException {
        ApiMessage msg = Api.likesAndWav(picId);
        LikesAndWavtimes law = new LikesAndWavtimes(0, 0, 0);
        if (msg.isFlag()) {
            String content = msg.getMsg();
            Log.e("hjtest", content);
            try {
                JSONObject jsonObject = new JSONObject(content).getJSONObject(
                        "data").getJSONObject("msg");
                law.setLikes(jsonObject.getInt("likes"));
                law.setWavtimes(jsonObject.getInt("wavtimes"));
                law.setCommentCount(jsonObject.getInt("count"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return law;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    public static int getLikes(int picId) throws ApiNetException {
        ApiMessage msg = Api.getLikes(picId);
        int likes = 0;
        if (msg.isFlag()) {
            String content = msg.getMsg();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                likes = jsonObject.getInt("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return likes;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // true————————not liked
    // false————————liked or net error
    public static boolean likeStatus(String sessionId, int picId) {
        ApiMessage msg = Api.likeStatus(sessionId, picId);
        int flag = 0;
        if (msg.isFlag()) {
            String content = msg.getMsg();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                flag = jsonObject.getInt("flag");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (flag == 1) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static ApiBack sendDeviceId(String deviceId) throws ApiNetException {
        ApiMessage msg = Api.sendDeviceId(deviceId);
        ApiBack back = new ApiBack();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                back.setFlag(jsonObject.getInt("flag"));
                back.setMsg(jsonObject.getString("msg"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return back;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    public static String getHomeImg() throws ApiNetException {
        ApiMessage msg = Api.getHomeImg();
        String url = new String();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                ;
                url = ApiConstant.URL + jsonObject.getString("img");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return url;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // detail activity
    public static String getActDetail() throws ApiNetException {
        ApiMessage msg = Api.getActDetail();
        String url = new String();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                ;
                url = ApiConstant.URL + jsonObject.getString("img");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return url;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // 2
    public static List<ItemsCate> getLabelAndImgs() throws ApiNetException {
        ApiMessage msg = Api.getLabelAndImgs();
        List<ItemsCate> lists = new ArrayList<ItemsCate>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            try {
                JSONArray jsonArray = new JSONObject(content)
                        .getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                    ItemsCate itemsCate = new ItemsCate();
                    itemsCate.setId(jsonObject.getInt("id"));
                    itemsCate.setName(jsonObject.getString("name"));
                    itemsCate.setImgUrl(ApiConstant.URL
                            + jsonObject.getString("img"));
                    lists.add(itemsCate);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return lists;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // 3 ads
    public static List<Ads> getAds() throws ApiNetException {
        ApiMessage msg = Api.getAds();
        List<Ads> lists = new ArrayList<Ads>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            try {
                JSONArray jsonArray = new JSONObject(content)
                        .getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                    Ads ads = new Ads();
                    ads.setId(jsonObject.getInt("id"));
                    ads.setTitle(jsonObject.getString("title"));
                    ads.setImgUrl(ApiConstant.URL + jsonObject.getString("img"));
                    ads.setUrl(jsonObject.getString("url"));
                    ads.setStatus(jsonObject.getInt("status"));
                    ads.setUid(jsonObject.getInt("uid"));

                    lists.add(ads);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return lists;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // rank week
    public static List<PicsAndIds> rankWeek(String sessionId, int times)
            throws ApiNetException {
        ApiMessage msg = Api.rankWeek(sessionId, times);
        List<PicsAndIds> lists = new ArrayList<PicsAndIds>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            // String sessionOut = new String();
            // try{
            // JSONObject jsonObject = new
            // JSONObject(content).getJSONObject("data");
            // sessionOut = jsonObject.getString("msg");
            // }catch (JSONException e) {
            // e.printStackTrace();
            // }
            // if(ApiConstant.SESSION_OUT.equals(sessionOut)){
            // throw new ApiSessionOutException(sessionOut);
            // }else{
            try {
                JSONArray jsonArray = new JSONObject(content)
                        .getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.opt(i);

                    ImageDetail imageDetail = new ImageDetail();
                    List<String> labels = new ArrayList<String>();
                    PicsAndIds picsAndIds = new PicsAndIds();
                    picsAndIds.setId(jsonObject.getInt("id"));
                    picsAndIds.setImgUrl(ApiConstant.URL
                            + jsonObject.getString("img"));
                    picsAndIds.setLikes(jsonObject.getInt("likes"));

                    JSONArray jsons = new JSONObject(jsonObject.toString())
                            .getJSONArray("detail");
                    JSONObject jsonObject2 = (JSONObject) jsons.opt(0);
                    imageDetail.setUid(jsonObject2.getInt("uid"));
                    imageDetail.setSex(jsonObject2.getInt("sex") == 1 ? "man"
                            : "woman");
                    imageDetail.setLikes(jsonObject2.getInt("likes"));
                    imageDetail.setUname(jsonObject2.getString("uname"));
                    imageDetail.setUimg(ApiConstant.URL
                            + jsonObject2.getString("uimg"));
                    imageDetail.setPid(jsonObject2.getInt("pid"));
                    imageDetail.setPimg(ApiConstant.URL
                            + jsonObject2.getString("pimg"));
                    imageDetail.setPtitle(jsonObject2.getString("ptitle"));
                    imageDetail.setAddTime(jsonObject2.getLong("add_time"));
                    imageDetail
                            .setLiked(jsonObject2.getInt("likestatus") == 0 ? false
                                    : true);
                    imageDetail.setTotalRank(jsonObject2.getInt("totalrank"));
                    imageDetail.setWav(ApiConstant.URL
                            + jsonObject2.getString("wav"));
                    imageDetail.setWavDurations((jsonObject2
                            .getString("wavdurations") == null || "null"
                            .equals(jsonObject2.getString("wavdurations"))) ? 0
                            : Integer.parseInt(jsonObject2
                            .getString("wavdurations")));
                    imageDetail
                            .setWeiboName(jsonObject2.getString("weiboname"));
                    imageDetail.setBirthday(jsonObject2.getString("brithday"));
                    JSONArray jsonArray2 = new JSONObject(
                            jsonObject2.toString()).getJSONArray("labels");
                    for (int j = 0; j < jsonArray2.length(); j++) {
                        JSONObject jsonObject3 = (JSONObject) jsonArray2.opt(j);
                        labels.add(jsonObject3.getString("cid"));
                    }
                    imageDetail.setLabels(labels);
                    picsAndIds.setImgDetail(imageDetail);
                    lists.add(picsAndIds);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return lists;
            // }
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // rank total
    public static List<PicsAndIds> rankTotal(String sessionId, int times)
            throws ApiNetException {
        ApiMessage msg = Api.rankTotal(sessionId, times);
        List<PicsAndIds> lists = new ArrayList<PicsAndIds>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            // String sessionOut = new String();
            // try{
            // JSONObject jsonObject = new
            // JSONObject(content).getJSONObject("data");
            // sessionOut = jsonObject.getString("msg");
            // }catch (JSONException e) {
            // e.printStackTrace();
            // }
            // if(ApiConstant.SESSION_OUT.equals(sessionOut)){
            // throw new ApiSessionOutException(sessionOut);
            // }else{
            try {
                JSONArray jsonArray = new JSONObject(content)
                        .getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.opt(i);

                    ImageDetail imageDetail = new ImageDetail();
                    List<String> labels = new ArrayList<String>();
                    PicsAndIds picsAndIds = new PicsAndIds();
                    picsAndIds.setId(jsonObject.getInt("id"));
                    picsAndIds.setImgUrl(ApiConstant.URL
                            + jsonObject.getString("img"));
                    picsAndIds.setLikes(jsonObject.getInt("likes"));

                    JSONArray jsons = new JSONObject(jsonObject.toString())
                            .getJSONArray("detail");
                    JSONObject jsonObject2 = (JSONObject) jsons.opt(0);
                    imageDetail.setUid(jsonObject2.getInt("uid"));
                    imageDetail.setSex(jsonObject2.getInt("sex") == 1 ? "man"
                            : "woman");
                    imageDetail.setLikes(jsonObject2.getInt("likes"));
                    imageDetail.setUname(jsonObject2.getString("uname"));
                    imageDetail.setUimg(ApiConstant.URL
                            + jsonObject2.getString("uimg"));
                    imageDetail.setPid(jsonObject2.getInt("pid"));
                    imageDetail.setPimg(ApiConstant.URL
                            + jsonObject2.getString("pimg"));
                    imageDetail.setPtitle(jsonObject2.getString("ptitle"));
                    imageDetail.setAddTime(jsonObject2.getLong("add_time"));
                    imageDetail
                            .setLiked(jsonObject2.getInt("likestatus") == 0 ? false
                                    : true);
                    imageDetail.setTotalRank(jsonObject2.getInt("totalrank"));
                    imageDetail.setWav(ApiConstant.URL
                            + jsonObject2.getString("wav"));
                    imageDetail.setWavDurations((jsonObject2
                            .getString("wavdurations") == null || "null"
                            .equals(jsonObject2.getString("wavdurations"))) ? 0
                            : Integer.parseInt(jsonObject2
                            .getString("wavdurations")));
                    imageDetail
                            .setWeiboName(jsonObject2.getString("weiboname"));
                    imageDetail.setBirthday(jsonObject2.getString("brithday"));
                    JSONArray jsonArray2 = new JSONObject(
                            jsonObject2.toString()).getJSONArray("labels");
                    for (int j = 0; j < jsonArray2.length(); j++) {
                        JSONObject jsonObject3 = (JSONObject) jsonArray2.opt(j);
                        labels.add(jsonObject3.getString("cid"));
                    }
                    imageDetail.setLabels(labels);
                    picsAndIds.setImgDetail(imageDetail);
                    lists.add(picsAndIds);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return lists;
            // }
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // rank month
    public static List<PicsAndIds> rankMonth(String sessionId, int times)
            throws ApiNetException {
        ApiMessage msg = Api.rankMonth(sessionId, times);
        List<PicsAndIds> lists = new ArrayList<PicsAndIds>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            // String sessionOut = new String();
            // try{
            // JSONObject jsonObject = new
            // JSONObject(content).getJSONObject("data");
            // sessionOut = jsonObject.getString("msg");
            // }catch (JSONException e) {
            // e.printStackTrace();
            // }
            // if(ApiConstant.SESSION_OUT.equals(sessionOut)){
            // throw new ApiSessionOutException(sessionOut);
            // }else{
            try {
                JSONArray jsonArray = new JSONObject(content)
                        .getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.opt(i);

                    ImageDetail imageDetail = new ImageDetail();
                    List<String> labels = new ArrayList<String>();
                    PicsAndIds picsAndIds = new PicsAndIds();
                    picsAndIds.setId(jsonObject.getInt("id"));
                    picsAndIds.setImgUrl(ApiConstant.URL
                            + jsonObject.getString("img"));
                    picsAndIds.setLikes(jsonObject.getInt("likes"));

                    JSONArray jsons = new JSONObject(jsonObject.toString())
                            .getJSONArray("detail");
                    JSONObject jsonObject2 = (JSONObject) jsons.opt(0);
                    imageDetail.setUid(jsonObject2.getInt("uid"));
                    imageDetail.setSex(jsonObject2.getInt("sex") == 1 ? "man"
                            : "woman");
                    imageDetail.setLikes(jsonObject2.getInt("likes"));
                    imageDetail.setUname(jsonObject2.getString("uname"));
                    imageDetail.setUimg(ApiConstant.URL
                            + jsonObject2.getString("uimg"));
                    imageDetail.setPid(jsonObject2.getInt("pid"));
                    imageDetail.setPimg(ApiConstant.URL
                            + jsonObject2.getString("pimg"));
                    imageDetail.setPtitle(jsonObject2.getString("ptitle"));
                    imageDetail.setAddTime(jsonObject2.getLong("add_time"));
                    imageDetail
                            .setLiked(jsonObject2.getInt("likestatus") == 0 ? false
                                    : true);
                    imageDetail.setTotalRank(jsonObject2.getInt("totalrank"));
                    imageDetail.setWav(ApiConstant.URL
                            + jsonObject2.getString("wav"));
                    imageDetail.setWavDurations((jsonObject2
                            .getString("wavdurations") == null || "null"
                            .equals(jsonObject2.getString("wavdurations"))) ? 0
                            : Integer.parseInt(jsonObject2
                            .getString("wavdurations")));
                    imageDetail
                            .setWeiboName(jsonObject2.getString("weiboname"));
                    imageDetail.setBirthday(jsonObject2.getString("brithday"));
                    JSONArray jsonArray2 = new JSONObject(
                            jsonObject2.toString()).getJSONArray("labels");
                    for (int j = 0; j < jsonArray2.length(); j++) {
                        JSONObject jsonObject3 = (JSONObject) jsonArray2.opt(j);
                        labels.add(jsonObject3.getString("cid"));
                    }
                    imageDetail.setLabels(labels);
                    picsAndIds.setImgDetail(imageDetail);
                    lists.add(picsAndIds);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return lists;
            // }
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // rank quarter
    public static List<PicsAndIds> rankQuarter(String sessionId, int times)
            throws ApiNetException {
        ApiMessage msg = Api.rankQuarter(sessionId, times);
        List<PicsAndIds> lists = new ArrayList<PicsAndIds>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            // String sessionOut = new String();
            // try{
            // JSONObject jsonObject = new
            // JSONObject(content).getJSONObject("data");
            // sessionOut = jsonObject.getString("msg");
            // }catch (JSONException e) {
            // e.printStackTrace();
            // }
            // if(ApiConstant.SESSION_OUT.equals(sessionOut)){
            // throw new ApiSessionOutException(sessionOut);
            // }else{
            try {
                JSONArray jsonArray = new JSONObject(content)
                        .getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.opt(i);

                    ImageDetail imageDetail = new ImageDetail();
                    List<String> labels = new ArrayList<String>();
                    PicsAndIds picsAndIds = new PicsAndIds();
                    picsAndIds.setId(jsonObject.getInt("id"));
                    picsAndIds.setImgUrl(ApiConstant.URL
                            + jsonObject.getString("img"));
                    picsAndIds.setLikes(jsonObject.getInt("likes"));

                    JSONArray jsons = new JSONObject(jsonObject.toString())
                            .getJSONArray("detail");
                    JSONObject jsonObject2 = (JSONObject) jsons.opt(0);
                    imageDetail.setUid(jsonObject2.getInt("uid"));
                    imageDetail.setSex(jsonObject2.getInt("sex") == 1 ? "man"
                            : "woman");
                    imageDetail.setLikes(jsonObject2.getInt("likes"));
                    imageDetail.setUname(jsonObject2.getString("uname"));
                    imageDetail.setUimg(ApiConstant.URL
                            + jsonObject2.getString("uimg"));
                    imageDetail.setPid(jsonObject2.getInt("pid"));
                    imageDetail.setPimg(ApiConstant.URL
                            + jsonObject2.getString("pimg"));
                    imageDetail.setPtitle(jsonObject2.getString("ptitle"));
                    imageDetail.setAddTime(jsonObject2.getLong("add_time"));
                    imageDetail
                            .setLiked(jsonObject2.getInt("likestatus") == 0 ? false
                                    : true);
                    imageDetail.setTotalRank(jsonObject2.getInt("totalrank"));
                    imageDetail.setWav(ApiConstant.URL
                            + jsonObject2.getString("wav"));
                    imageDetail.setWavDurations((jsonObject2
                            .getString("wavdurations") == null || "null"
                            .equals(jsonObject2.getString("wavdurations"))) ? 0
                            : Integer.parseInt(jsonObject2
                            .getString("wavdurations")));
                    imageDetail
                            .setWeiboName(jsonObject2.getString("weiboname"));
                    JSONArray jsonArray2 = new JSONObject(
                            jsonObject2.toString()).getJSONArray("labels");
                    for (int j = 0; j < jsonArray2.length(); j++) {
                        JSONObject jsonObject3 = (JSONObject) jsonArray2.opt(j);
                        labels.add(jsonObject3.getString("cid"));
                    }
                    imageDetail.setLabels(labels);
                    picsAndIds.setImgDetail(imageDetail);
                    lists.add(picsAndIds);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return lists;
            // }
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // 3 hots
    public static List<PicsAndIds> getHots(String sessionId, int times)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.getHots(sessionId, times);
        List<PicsAndIds> lists = new ArrayList<PicsAndIds>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                try {
                    JSONArray jsonArray = new JSONObject(content)
                            .getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.opt(i);

                        ImageDetail imageDetail = new ImageDetail();
                        List<String> labels = new ArrayList<String>();
                        PicsAndIds picsAndIds = new PicsAndIds();
                        picsAndIds.setId(jsonObject.getInt("id"));
                        picsAndIds.setImgUrl(ApiConstant.URL
                                + jsonObject.getString("img"));
                        picsAndIds.setLikes(jsonObject.getInt("likes"));

                        JSONArray jsons = new JSONObject(jsonObject.toString())
                                .getJSONArray("detail");
                        JSONObject jsonObject2 = (JSONObject) jsons.opt(0);
                        imageDetail.setUid(jsonObject2.getInt("uid"));
                        imageDetail
                                .setSex(jsonObject2.getInt("sex") == 1 ? "man"
                                        : "woman");
                        imageDetail.setLikes(jsonObject2.getInt("likes"));
                        imageDetail.setUname(jsonObject2.getString("uname"));
                        imageDetail.setUimg(ApiConstant.URL
                                + jsonObject2.getString("uimg"));
                        imageDetail.setPid(jsonObject2.getInt("pid"));
                        imageDetail.setPimg(ApiConstant.URL
                                + jsonObject2.getString("pimg"));
                        imageDetail.setPtitle(jsonObject2.getString("ptitle"));
                        imageDetail.setAddTime(jsonObject2.getLong("add_time"));
                        imageDetail
                                .setLiked(jsonObject2.getInt("likestatus") == 0 ? false
                                        : true);
                        imageDetail.setTotalRank(jsonObject2
                                .getInt("totalrank"));
                        imageDetail.setWav(ApiConstant.URL
                                + jsonObject2.getString("wav"));
                        imageDetail
                                .setWavDurations((jsonObject2
                                        .getString("wavdurations") == null || "null"
                                        .equals(jsonObject2
                                                .getString("wavdurations"))) ? 0
                                        : Integer.parseInt(jsonObject2
                                        .getString("wavdurations")));
                        imageDetail.setWeiboName(jsonObject2
                                .getString("weiboname"));
                        imageDetail.setAuthStatus(jsonObject2
                                .getInt("authstatus"));
                        JSONArray jsonArray2 = new JSONObject(
                                jsonObject2.toString()).getJSONArray("labels");
                        for (int j = 0; j < jsonArray2.length(); j++) {
                            JSONObject jsonObject3 = (JSONObject) jsonArray2
                                    .opt(j);
                            labels.add(jsonObject3.getString("cid"));
                        }
                        imageDetail.setLabels(labels);
                        picsAndIds.setImgDetail(imageDetail);
                        lists.add(picsAndIds);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return lists;
            }
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // 3 news
    public static List<PicsAndIds> getNews(String sessionId, int times)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.getNews(sessionId, times);
        List<PicsAndIds> lists = new ArrayList<PicsAndIds>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                try {
                    JSONArray jsonArray = new JSONObject(content)
                            .getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.opt(i);

                        ImageDetail imageDetail = new ImageDetail();
                        List<String> labels = new ArrayList<String>();
                        PicsAndIds picsAndIds = new PicsAndIds();
                        picsAndIds.setId(jsonObject.getInt("id"));
                        picsAndIds.setImgUrl(ApiConstant.URL
                                + jsonObject.getString("img"));
                        picsAndIds.setLikes(jsonObject.getInt("likes"));

                        JSONArray jsons = new JSONObject(jsonObject.toString())
                                .getJSONArray("detail");
                        JSONObject jsonObject2 = (JSONObject) jsons.opt(0);
                        imageDetail.setUid(jsonObject2.getInt("uid"));
                        imageDetail
                                .setSex(jsonObject2.getInt("sex") == 1 ? "man"
                                        : "woman");
                        imageDetail.setLikes(jsonObject2.getInt("likes"));
                        imageDetail.setUname(jsonObject2.getString("uname"));
                        imageDetail.setUimg(ApiConstant.URL
                                + jsonObject2.getString("uimg"));
                        imageDetail.setPid(jsonObject2.getInt("pid"));
                        imageDetail.setPimg(ApiConstant.URL
                                + jsonObject2.getString("pimg"));
                        imageDetail.setPtitle(jsonObject2.getString("ptitle"));
                        imageDetail.setAddTime(jsonObject2.getLong("add_time"));
                        imageDetail
                                .setLiked(jsonObject2.getInt("likestatus") == 0 ? false
                                        : true);
                        imageDetail.setTotalRank(jsonObject2
                                .getInt("totalrank"));
                        imageDetail.setWav(ApiConstant.URL
                                + jsonObject2.getString("wav"));
                        imageDetail
                                .setWavDurations((jsonObject2
                                        .getString("wavdurations") == null || "null"
                                        .equals(jsonObject2
                                                .getString("wavdurations"))) ? 0
                                        : Integer.parseInt(jsonObject2
                                        .getString("wavdurations")));
                        imageDetail.setWeiboName(jsonObject2
                                .getString("weiboname"));
                        imageDetail.setAuthStatus(jsonObject2
                                .getInt("authstatus"));
                        JSONArray jsonArray2 = new JSONObject(
                                jsonObject2.toString()).getJSONArray("labels");
                        for (int j = 0; j < jsonArray2.length(); j++) {
                            JSONObject jsonObject3 = (JSONObject) jsonArray2
                                    .opt(j);
                            labels.add(jsonObject3.getString("cid"));
                        }
                        imageDetail.setLabels(labels);
                        picsAndIds.setImgDetail(imageDetail);
                        lists.add(picsAndIds);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return lists;
            }
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // 3 labels
    public static List<PicsAndIds> getLabelImgs(String sessionId, int times,
                                                int labelId) throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.getLabelImgs(sessionId, times, labelId);
        List<PicsAndIds> lists = new ArrayList<PicsAndIds>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                try {
                    JSONArray jsonArray = new JSONObject(content)
                            .getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.opt(i);

                        ImageDetail imageDetail = new ImageDetail();
                        List<String> labels = new ArrayList<String>();
                        PicsAndIds picsAndIds = new PicsAndIds();
                        picsAndIds.setId(jsonObject.getInt("id"));
                        picsAndIds.setImgUrl(ApiConstant.URL
                                + jsonObject.getString("img"));

                        JSONArray jsons = new JSONObject(jsonObject.toString())
                                .getJSONArray("detail");
                        JSONObject jsonObject2 = (JSONObject) jsons.opt(0);
                        imageDetail.setUid(jsonObject2.getInt("uid"));
                        imageDetail
                                .setSex(jsonObject2.getInt("sex") == 1 ? "man"
                                        : "woman");
                        imageDetail.setLikes(jsonObject2.getInt("likes"));
                        imageDetail.setUname(jsonObject2.getString("uname"));
                        imageDetail.setUimg(ApiConstant.URL
                                + jsonObject2.getString("uimg"));
                        imageDetail.setPid(jsonObject2.getInt("pid"));
                        imageDetail.setPimg(ApiConstant.URL
                                + jsonObject2.getString("pimg"));
                        imageDetail.setPtitle(jsonObject2.getString("ptitle"));
                        imageDetail.setAddTime(jsonObject2.getLong("add_time"));
                        imageDetail
                                .setLiked(jsonObject2.getInt("likestatus") == 0 ? false
                                        : true);
                        imageDetail.setTotalRank(jsonObject2
                                .getInt("totalrank"));
                        imageDetail.setWav(ApiConstant.URL
                                + jsonObject2.getString("wav"));
                        imageDetail
                                .setWavDurations((jsonObject2
                                        .getString("wavdurations") == null || "null"
                                        .equals(jsonObject2
                                                .getString("wavdurations"))) ? 0
                                        : Integer.parseInt(jsonObject2
                                        .getString("wavdurations")));
                        imageDetail.setWeiboName(jsonObject2
                                .getString("weiboname"));
                        imageDetail.setAuthStatus(jsonObject2
                                .getInt("authstatus"));
                        JSONArray jsonArray2 = new JSONObject(
                                jsonObject2.toString()).getJSONArray("labels");
                        for (int j = 0; j < jsonArray2.length(); j++) {
                            JSONObject jsonObject3 = (JSONObject) jsonArray2
                                    .opt(j);
                            labels.add(jsonObject3.getString("cid"));
                        }
                        imageDetail.setLabels(labels);
                        picsAndIds.setImgDetail(imageDetail);
                        lists.add(picsAndIds);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return lists;
            }
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // weibo register
    public static ApiBack combineWeibo(String weiboType, String weiboName)
            throws ApiNetException {
        ApiMessage msg = Api.combineWeibo(weiboType, weiboName);
        ApiBack back = new ApiBack();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                back.setFlag(jsonObject.getInt("flag"));
                back.setMsg(jsonObject.getString("msg"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return back;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // 4 register
    public static ApiBack register(String username, String pwd, String email,
                                   String sex, int regfromtypeId) throws ApiNetException {
        return register(username, pwd, email, "", sex, regfromtypeId);
    }

    public static ApiBack register(String username, String pwd, String email,
                                   String picAddress, String sex, int regfromtypeId)
            throws ApiNetException {
        ApiMessage msg = Api.register(username, pwd, email, picAddress, sex,
                regfromtypeId);
        Log.v("ApiJson", "register : " + username + "  " + pwd + "  " + email
                + "  " + picAddress + "  " + sex + "  " + regfromtypeId);
        ApiBack back = new ApiBack();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            Log.v("ApiJson", "content --> " + content);
            if (content.contains(SportsApp.getInstance().getResources().getString(R.string.user_system))){
                back.setFlag(-100);
                back.setMsg("checkyour_WIFI_isconnect");
                return back;
            }else{
                try {
                    JSONObject jsonObject = new JSONObject(content)
                            .getJSONObject("data");
                    back.setFlag(jsonObject.getInt("flag"));
                    back.setMsg(jsonObject.getString("msg"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return back;
            }
        } else {
            if (msg != null && ("SocketTimeoutException").equals(msg.getMsg())){
                String content = msg.getMsg();
                back.setFlag(-56);
                back.setMsg(content);
                return back;
            }else{
                throw new ApiNetException("error:" + msg.getMsg());
            }
        }
    }

    public static ApiBack forgetPwd(String email) throws ApiNetException {
        ApiMessage msg = Api.forgetPwd(email);
        ApiBack back = new ApiBack();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                back.setFlag(jsonObject.getInt("flag"));
                back.setMsg(jsonObject.getString("msg"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return back;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // 发送短信验证码
    // type 'regist'--for regist
    // type 'forget'--for forget password
    public static ApiBack sendPhoneMessage(String phone, String type)
            throws ApiNetException {
        ApiMessage msg = Api.sendPhoneMessage(phone, type);
        ApiBack back = new ApiBack();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                back.setFlag(jsonObject.getInt("flag"));
                back.setMsg(jsonObject.getString("msg"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return back;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // 4 login wrlogin(用来区分运动+和最美，1是运动，0是最美，最美没加)
    public static ApiBack login(String count, String pwd, int wrlogin, String umengDeviceToken)
            throws ApiNetException {
        ApiMessage msg = Api.login(count, pwd, wrlogin, umengDeviceToken);
        ApiBack back = new ApiBack();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            Log.v("login", content);
            if (content.contains(SportsApp.getInstance().getResources().getString(R.string.user_system))){
                back.setFlag(-100);
                back.setMsg("checkyour_WIFI_isconnect");
                return back;
            }else{
                try {
                    JSONObject jsonObject = new JSONObject(content)
                            .getJSONObject("data");
                    back.setFlag(jsonObject.getInt("flag"));
                    back.setMsg(jsonObject.getString("msg"));
                    back.setFirst(jsonObject.getInt("fstatus"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return back;
            }
        } else {
            if (msg != null && ("SocketTimeoutException").equals(msg.getMsg())){
                String content = msg.getMsg();
                back.setFlag(-56);
                back.setMsg(content);
                return back;
            }else{
                throw new ApiNetException("error:" + msg.getMsg());
            }

        }
    }

    // 5 image detail
    public static ImageDetail getImgDetail(String sessionId, int picId)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.getImgDetail(sessionId, picId);
        ImageDetail imageDetail = new ImageDetail();
        List<String> labels = new ArrayList<String>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                try {
                    JSONArray jsons = new JSONObject(content)
                            .getJSONArray("data");
                    JSONObject jsonObject = (JSONObject) jsons.opt(0);
                    imageDetail.setUid(jsonObject.getInt("uid"));
                    imageDetail.setSex(jsonObject.getInt("sex") == 1 ? "man"
                            : "woman");
                    imageDetail.setLikes(jsonObject.getInt("likes"));
                    imageDetail.setUname(jsonObject.getString("uname"));
                    imageDetail.setUimg(ApiConstant.URL
                            + jsonObject.getString("uimg"));
                    imageDetail.setPid(jsonObject.getInt("pid"));
                    imageDetail.setPimg(ApiConstant.URL
                            + jsonObject.getString("pimg"));
                    imageDetail.setPtitle(jsonObject.getString("ptitle"));
                    imageDetail.setAddTime(jsonObject.getLong("add_time"));
                    imageDetail
                            .setLiked(jsonObject.getInt("likestatus") == 0 ? false
                                    : true);
                    imageDetail.setTotalRank(jsonObject.getInt("totalrank"));
                    imageDetail.setWav(ApiConstant.URL
                            + jsonObject.getString("wav"));
                    imageDetail.setWavDurations((jsonObject
                            .getString("wavdurations") == null || "null"
                            .equals(jsonObject.getString("wavdurations"))) ? 0
                            : Integer.parseInt(jsonObject
                            .getString("wavdurations")));
                    imageDetail.setWeiboName(jsonObject.getString("weiboname"));
                    imageDetail.setAuthStatus(jsonObject.getInt("authstatus"));
                    imageDetail.setBirthday(jsonObject.getString("brithday"));
                    JSONArray jsonArray = new JSONObject(jsonObject.toString())
                            .getJSONArray("labels");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
                        labels.add(jsonObject2.getString("cid"));
                    }
                    imageDetail.setLabels(labels);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return imageDetail;
            }
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // 5 likeimg
    public static ApiBack likeImg(String sessionId, int picId)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.likeImg(sessionId, picId);
        ApiBack back = new ApiBack();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                back.setFlag(jsonObject.getInt("flag"));
                back.setMsg(jsonObject.getString("msg"));
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                return back;
            }
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // 5 runwav
    public static ApiBack runWav(String sessionId, int picId)
            throws ApiNetException {
        ApiMessage msg = Api.runWav(sessionId, picId);
        ApiBack back = new ApiBack();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            Log.e("hjtest", content);
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                back.setFlag(jsonObject.getInt("flag"));
                back.setMsg(jsonObject.getString("msg"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return back;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // 6 see user detail
    public static UserDetail seeUser(String sessionId, int uid)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.seeUser(sessionId, uid);
        UserDetail userDetail = new UserDetail();
        List<PicsAndIds> imgs = new ArrayList<PicsAndIds>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(content)
                            .getJSONObject("data");
                    userDetail.setUid(jsonObject.getInt("uid"));
                    userDetail.setUname(jsonObject.getString("uname"));
                    userDetail.setUimg(ApiConstant.URL
                            + jsonObject.getString("uimg"));
                    userDetail.setCounts(jsonObject.getInt("counts"));
                    userDetail.setTotalRank(jsonObject.getInt("totalrank"));
                    userDetail.setTotalLikes(jsonObject.getInt("totallikes"));
                    userDetail.setSex(jsonObject.getInt("sex") == 1 ? "man"
                            : "woman");
                    userDetail.setBirthday(jsonObject.getString("brithday"));
                    userDetail.setPhoneno(jsonObject.getString("phoneno"));
                    userDetail.setFanCounts(jsonObject.getInt("fancounts"));
                    userDetail.setFollowCounts(jsonObject
                            .getInt("followcounts"));
                    userDetail.setGiftCounts(jsonObject.getInt("giftcounts"));
                    userDetail.setFollowStatus(jsonObject.getInt("status"));

                    userDetail.setAuthPic(ApiConstant.URL
                            + jsonObject.getString("authpic"));
                    userDetail.setAuthStatus(jsonObject.getInt("authstatus"));

                    userDetail.setCharms(jsonObject.getInt("charms"));
                    userDetail.setUpcharms(jsonObject.getInt("upcharms"));
                    userDetail.setWealths(jsonObject.getInt("wealths"));
                    userDetail.setUpwealths(jsonObject.getInt("upwealths"));
                    userDetail.setKind(jsonObject.getInt("kind"));
                    userDetail.setUpkind(jsonObject.getInt("upkind"));
                    userDetail.setKindrank(jsonObject.getInt("kindrank"));
                    userDetail.setCharmsrank(jsonObject.getInt("charmsrank"));
                    userDetail.setWealthsrank(jsonObject.getInt("wealthsrank"));
                    userDetail.setOwner("me".equals(jsonObject
                            .getString("isowner")));

                    JSONArray jsonArray = new JSONObject(jsonObject.toString())
                            .getJSONArray("imgs");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
                        ImageDetail imageDetail = new ImageDetail();
                        List<String> labels = new ArrayList<String>();

                        PicsAndIds img = new PicsAndIds();
                        img.setId(jsonObject2.getInt("pid"));
                        img.setImgUrl(ApiConstant.URL
                                + jsonObject2.getString("pimg"));

                        JSONArray jsons = new JSONObject(jsonObject2.toString())
                                .getJSONArray("detail");
                        JSONObject jsonObject3 = (JSONObject) jsons.opt(0);
                        imageDetail.setUid(jsonObject3.getInt("uid"));
                        imageDetail.setLikes(jsonObject3.getInt("likes"));
                        imageDetail.setUname(jsonObject3.getString("uname"));
                        imageDetail.setUimg(ApiConstant.URL
                                + jsonObject3.getString("uimg"));
                        imageDetail.setPid(jsonObject3.getInt("pid"));
                        imageDetail.setPimg(ApiConstant.URL
                                + jsonObject3.getString("pimg"));
                        imageDetail.setPtitle(jsonObject3.getString("ptitle"));
                        imageDetail.setAddTime(jsonObject3.getLong("add_time"));
                        imageDetail
                                .setLiked(jsonObject3.getInt("likestatus") == 0 ? false
                                        : true);
                        imageDetail.setTotalRank(jsonObject3
                                .getInt("totalrank"));
                        imageDetail.setWav(ApiConstant.URL
                                + jsonObject3.getString("wav"));
                        imageDetail
                                .setWavDurations((jsonObject3
                                        .getString("wavdurations") == null || "null"
                                        .equals(jsonObject3
                                                .getString("wavdurations"))) ? 0
                                        : Integer.parseInt(jsonObject3
                                        .getString("wavdurations")));
                        imageDetail.setWeiboName(jsonObject3
                                .getString("weiboname"));
                        JSONArray jsonArray2 = new JSONObject(
                                jsonObject3.toString()).getJSONArray("labels");
                        for (int j = 0; j < jsonArray2.length(); j++) {
                            JSONObject jsonObject4 = (JSONObject) jsonArray2
                                    .opt(j);
                            labels.add(jsonObject4.getString("cid"));
                        }
                        imageDetail.setLabels(labels);
                        img.setImgDetail(imageDetail);

                        imgs.add(img);
                    }
                    userDetail.setImgs(imgs);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return userDetail;
            }
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // 8 search user
    public static List<UserSearch> getUsers(int times) throws ApiNetException {
        ApiMessage msg = Api.getUsers(times);
        List<UserSearch> lists = new ArrayList<UserSearch>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            try {
                JSONArray jsonArray = new JSONObject(content)
                        .getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                    Log.e("hjtest", jsonObject.toString());
                    UserSearch users = new UserSearch();
                    users.setId(jsonObject.getInt("id"));
                    users.setImg(ApiConstant.URL + jsonObject.getString("img"));
                    String totalRank = jsonObject.getString("totalrank");
                    String birthday = jsonObject.getString("birthday");
                    users.setBirthday(birthday);
                    if ("null".equals(totalRank)) {
                        totalRank = "0";
                    }
                    users.setTotalRank(Integer.parseInt(totalRank));
                    users.setName(jsonObject.getString("name"));
                    lists.add(users);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return lists;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // 8 search userbyname
    public static List<UserSearch> getUserByName(int times, String name, int uid)
            throws ApiNetException {
        ApiMessage msg = Api.getUserByName(times, name, uid);
        List<UserSearch> lists = new ArrayList<UserSearch>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            try {
                JSONArray jsonArray = new JSONObject(content)
                        .getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                    UserSearch users = new UserSearch();
                    users.setId(jsonObject.getInt("id"));
                    users.setSex(jsonObject.getInt("sex") == 1 ? "man"
                            : "woman");
                    users.setImg(ApiConstant.URL + jsonObject.getString("img"));
                    String totalRank = jsonObject.getString("totalrank");
                    String birthday = jsonObject.getString("brithday");
                    users.setBirthday(birthday);
                    if ("null".equals(totalRank)) {
                        totalRank = "0";
                    }
                    users.setTotalRank(Integer.parseInt(totalRank));
                    users.setName(jsonObject.getString("name"));
                    lists.add(users);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return lists;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // 8 search searchpic
    public static List<ImgSearch> getPics(int times) throws ApiNetException {
        ApiMessage msg = Api.getPics(times);
        List<ImgSearch> lists = new ArrayList<ImgSearch>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            try {
                JSONArray jsonArray = new JSONObject(content)
                        .getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                    ImgSearch img = new ImgSearch();
                    img.setId(jsonObject.getInt("id"));
                    img.setImg(ApiConstant.URL + jsonObject.getString("img"));
                    img.setAddTime(jsonObject.getLong("add_time"));
                    img.setLikes(jsonObject.getInt("likes"));
                    img.setUname(jsonObject.getString("name"));
                    img.setTitle(jsonObject.getString("title"));
                    lists.add(img);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return lists;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // 8 search searchpicbypic
    public static List<ImgSearch> getPicByPic(int times, String name)
            throws ApiNetException {
        ApiMessage msg = Api.getPicByPic(times, name);
        List<ImgSearch> lists = new ArrayList<ImgSearch>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            try {
                JSONArray jsonArray = new JSONObject(content)
                        .getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                    ImgSearch img = new ImgSearch();
                    img.setId(jsonObject.getInt("id"));
                    img.setImg(ApiConstant.URL + jsonObject.getString("img"));
                    img.setAddTime(jsonObject.getLong("add_time"));
                    img.setLikes(jsonObject.getInt("likes"));
                    img.setUname(jsonObject.getString("name"));
                    img.setTitle(jsonObject.getString("title"));
                    lists.add(img);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return lists;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    public static ApiBack uploadImg(String picTitle, String picAddress,
                                    String sessionId, List<String> labels) throws ApiNetException,
            ApiSessionOutException {
        return uploadImg(picTitle, picAddress, sessionId, labels, "", 0);
    }

    // 9 upload pic
    public static ApiBack uploadImg(String picTitle, String picAddress,
                                    String sessionId, List<String> labels, String wav, int wavDurations)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.uploadImg(picTitle, picAddress, sessionId, labels,
                wav, wavDurations);
        ApiBack back = new ApiBack();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                back.setFlag(jsonObject.getInt("flag"));
                back.setMsg(jsonObject.getString("msg"));
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                return back;
            }
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // 10 refresh rank
    public static UserDetail refreshRank(String sessionId)
            throws ApiNetException, ApiSessionOutException {
        Log.d("refreshRank", "refreshRank");
        ApiMessage msg = Api.refreshRank(sessionId);
        Log.v("ApiJsonParser", "sessionId:" + sessionId);
        UserDetail userDetail = new UserDetail();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(content)
                            .getJSONObject("data");
                    userDetail.setUid(jsonObject.getInt("uid"));
                    userDetail.setUname(jsonObject.getString("uname"));
                    userDetail.setUimg(ApiConstant.URL
                            + jsonObject.getString("uimg"));
                    userDetail.setCounts(jsonObject.getInt("counts"));
                    userDetail.setTotalRank(jsonObject.getInt("totalrank"));
                    userDetail.setSex(jsonObject.getInt("sex") == 1 ? "man"
                            : "woman");
                    if(jsonObject.getString("brithday")!=null){
                        userDetail.setBirthday(jsonObject.getString("brithday"));
                    }else{
                        userDetail.setBirthday("");
                    }
                    userDetail.setHeight(jsonObject.getInt("height"));
                    userDetail.setWeight(jsonObject.getInt("weight"));
                    userDetail.setFcount(jsonObject.getInt("actmsgs"));
                    userDetail.setTotalLikes(jsonObject.getInt("totallikes"));
                    userDetail.setPhoneno(jsonObject.getString("phoneno"));
                    userDetail.setFanCounts(jsonObject.getInt("fancounts"));
                    userDetail.setFollowCounts(jsonObject
                            .getInt("followcounts"));
                    userDetail.setEmail(jsonObject.getString("email"));
                    userDetail.setGiftCounts(jsonObject.getInt("giftcounts"));
                    userDetail.setGolds(jsonObject.getInt("golds"));
                    userDetail.setCoins(jsonObject.getInt("coins"));
                    userDetail.setAuthPic(ApiConstant.URL
                            + jsonObject.getString("authpic"));
                    userDetail.setAuthStatus(jsonObject.getInt("authstatus"));
                    userDetail.setKindrank(jsonObject.getInt("kindrank"));
                    userDetail.setCharmsrank(jsonObject.getInt("charmsrank"));
                    userDetail.setActmsgs(jsonObject.getInt("actmsgs"));
                    userDetail.setFindimg(jsonObject.getString("findimg"));
                    if (jsonObject.has("find_count_num")){
                        userDetail.setFind_count_num(jsonObject.getInt("find_count_num"));
                    }
                    MsgCounts msgcounts = new MsgCounts(0, 0, 0, 0, 0, 0, 0, 0,
                            0, 0, 0, 0, 0);
                    if (jsonObject.has("fans")) {
                        if (jsonObject.getInt("fans") >= 0) {
                            msgcounts.setFans(jsonObject.getInt("fans"));
                            msgcounts.setSportVisitor(jsonObject
                                    .getInt("sports_visitor"));
                            msgcounts.setSportComments(jsonObject
                                    .getInt("sport_comments"));
                            msgcounts.setLikes(jsonObject.getInt("likes"));
                            msgcounts.setGifts(jsonObject.getInt("gifts"));
                            msgcounts.setFollows(jsonObject.getInt("follows"));
                            msgcounts.setPrimsg(jsonObject.getInt("primsg"));
                            msgcounts.setSuns(jsonObject.getInt("suns"));
                            msgcounts.setSysmsgsports(jsonObject
                                    .getInt("sysmsg"));
                            msgcounts.setfUploads(jsonObject
                                    .getInt("f_uploads"));
                            msgcounts.setfFollows(jsonObject
                                    .getInt("f_follows"));
                            msgcounts.setfGifts(jsonObject.getInt("f_gifts"));
                            msgcounts.setTotal(jsonObject.getInt("total"));
                            msgcounts.setInvitesports(jsonObject
                                    .getInt("invite_sports"));
                        }
                    }
                    userDetail.setMsgCounts(msgcounts);

                    userDetail.setProvince(jsonObject.getString("province"));
                    userDetail.setCity(jsonObject.getString("city"));
                    userDetail.setArea(jsonObject.getString("area"));
                    double double1 = jsonObject.getDouble("sportsdata");
                    userDetail.setSportsdata(SportTaskUtil
                            .getDoubleNumber(double1));
                    if (jsonObject.has("count_num")) {
                        userDetail.setCount_num(jsonObject.getInt("count_num"));
                    }
                    if (jsonObject.has("sprots_Calorie")) {
                        userDetail.setSprots_Calorie(jsonObject
                                .getDouble("sprots_Calorie"));
                    }
                    if (jsonObject.has("time")) {
                        userDetail.setTime(jsonObject.getInt("time"));
                    }
                    userDetail.setSignature(jsonObject.getString("signature"));
                    userDetail.setBmi(jsonObject.getDouble("BMI"));
                    if (jsonObject.has("status")) {
                        userDetail.setFollowStatus(jsonObject.getInt("status"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return userDetail;
            }
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // 10 more modifymsg
    public static ApiBack modifymsg(String sessionId, String username,
                                    String oldPwd, String newPwd, String picAddress, String sex,
                                    String birthday, String phoneno, String email, String height,
                                    String weight, String province, String city, String area,
                                    String signature, String BMI) throws ApiNetException,
            ApiSessionOutException {
        ApiMessage msg = Api.modifymsg(sessionId, username, oldPwd, newPwd,
                picAddress, sex, birthday, phoneno, email, height, weight,
                province, city, area, signature, BMI);
        Log.e("APIJson", "msgssss" + msg);
        ApiBack back = new ApiBack();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                back.setFlag(jsonObject.getInt("flag"));
                Log.e("APIJSSSS", "flag" + jsonObject.getInt("flag"));
                back.setMsg(jsonObject.getString("msg"));
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                return back;
            }
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // 10 more userpics
    public static List<ImageDetail> getUserPics(String sessionId, int times)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.getUserPics(sessionId, times);
        List<ImageDetail> lists = new ArrayList<ImageDetail>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                try {
                    JSONArray jsonArray = new JSONObject(content)
                            .getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                        ImageDetail imgDetail = new ImageDetail();
                        imgDetail.setPid(jsonObject.getInt("pid"));
                        imgDetail.setPtitle(jsonObject.getString("ptitle"));
                        imgDetail.setPimg(ApiConstant.URL
                                + jsonObject.getString("pimg"));
                        imgDetail.setLikes(jsonObject.getInt("likes"));
                        imgDetail.setAddTime(jsonObject.getLong("add_time"));
                        lists.add(imgDetail);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return lists;
            }
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // 10 more deleteimg
    public static ApiBack deleteImg(String sessionId, List<Integer> picIds)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.deleteImg(sessionId, picIds);
        ApiBack back = new ApiBack();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                back.setFlag(jsonObject.getInt("flag"));
                back.setMsg(jsonObject.getString("msg"));
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                return back;
            }
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    //
    public static ApiBack upComments(String sessionId, int picId,
                                     String comments, String wav, int wavDurations, int touid)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.upComments(sessionId, picId, comments, wav,
                wavDurations, touid);
        ApiBack back = new ApiBack();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                back.setFlag(jsonObject.getInt("flag"));
                back.setMsg(jsonObject.getString("msg"));
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                return back;
            }
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    public static UserComment getNewComment(int picId) throws ApiNetException {
        ApiMessage msg = Api.getNewComment(picId);
        UserComment userComment = new UserComment();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            try {
                JSONArray jsonArray = new JSONObject(content)
                        .getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                    userComment.setUid(jsonObject.getInt("uid"));
                    userComment.setCommentText(jsonObject.getString("info"));
                    userComment.setCommentWav(ApiConstant.URL
                            + jsonObject.getString("wav"));
                    userComment.setAddTime(jsonObject.getLong("add_time"));
                    userComment.setUname(jsonObject.getString("uname"));
                    userComment.setUimg(ApiConstant.URL
                            + jsonObject.getString("uimg"));
                    userComment.setId(jsonObject.getInt("id"));
                    userComment.setSex(jsonObject.getInt("sex") == 1 ? "man"
                            : "woman");
                    userComment.setWavDuration("null".equals(jsonObject
                            .getString("wavdurations")) ? 0 : Integer
                            .parseInt(jsonObject.getString("wavdurations")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return userComment;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // get comment
    public static List<UserComment> getComments(int picId, int times, int id)
            throws ApiNetException {
        ApiMessage msg = Api.getComments(picId, times, id);
        List<UserComment> lists = new ArrayList<UserComment>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            try {
                JSONArray jsonArray = new JSONObject(content)
                        .getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                    UserComment userComment = new UserComment();
                    userComment.setUid(jsonObject.getInt("uid"));
                    userComment.setCommentText(jsonObject.getString("info"));
                    userComment.setCommentWav(ApiConstant.URL
                            + jsonObject.getString("wav"));
                    userComment.setAddTime(jsonObject.getLong("add_time"));
                    userComment.setSex(jsonObject.getInt("sex") == 1 ? "man"
                            : "woman");
                    userComment.setUname(jsonObject.getString("uname"));
                    userComment.setUimg(ApiConstant.URL
                            + jsonObject.getString("uimg"));
                    userComment.setId(jsonObject.getInt("id"));
                    userComment.setWavDuration("null".equals(jsonObject
                            .getString("wavdurations")) ? 0 : Integer
                            .parseInt(jsonObject.getString("wavdurations")));
                    userComment.setTouname(jsonObject.getString("touname"));
                    lists.add(userComment);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return lists;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    public static String ImgProcess(String imgUrl, int type) {
        String returnImgUrl = new String();
        switch (type) {
            case ApiConstant.ImgType.IMAGE_BIG: {
                if (imgUrl != null) {
                    String[] img = imgUrl.split("m_");
                    returnImgUrl = img[0] + "b_" + img[1];
                }
                break;
            }
            case ApiConstant.ImgType.IMAGE_MEDIUM: {
                returnImgUrl = imgUrl;
                break;
            }
            case ApiConstant.ImgType.IMAGE_ORI: {
                if (imgUrl != null) {
                    String[] img = imgUrl.split("m_");
                    returnImgUrl = img[0] + img[1];
                }
                break;
            }
            case ApiConstant.ImgType.IMAGE_SMALL: {
                if (imgUrl != null) {
                    String[] img = imgUrl.split("m_");
                    returnImgUrl = img[0] + "s_" + img[1];
                }
                break;
            }
        }
        return returnImgUrl;
    }

    // follow person
    // 1——关注 2——取消关注
    public static ApiBack follow(String sessionId, int uid, int status,
                                 int issports) throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.follow(sessionId, uid, status, issports);
        ApiBack back = new ApiBack();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                back.setFlag(jsonObject.getInt("flag"));
                back.setMsg(jsonObject.getString("msg"));
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                return back;
            }
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // giveGift
    public static ApiBack giveGift(String sessionId, int uid, int giftId,
                                   int golds) throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.giveGift(sessionId, uid, giftId, golds);
        ApiBack back = new ApiBack();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                back.setFlag(jsonObject.getInt("flag"));
                back.setMsg(jsonObject.getString("msg"));
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                return back;
            }
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // getGifts
    public static List<Gifts> getGifts() throws ApiNetException {
        ApiMessage msg = Api.getGifts();
        List<Gifts> lists = new ArrayList<Gifts>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            try {
                JSONArray jsonArray = new JSONObject(content)
                        .getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                    Gifts gifts = new Gifts();
                    gifts.setId(jsonObject.getInt("id"));
                    gifts.setGiftName(jsonObject.getString("gift_name"));
                    gifts.setGiftPic(ApiConstant.URL
                            + jsonObject.getString("gift_pic"));
                    gifts.setGiftPrice(jsonObject.getInt("gift_price"));
                    gifts.setCharmNo(jsonObject.getInt("charm_no"));
                    gifts.setWealthNo(jsonObject.getInt("wealth_no"));
                    lists.add(gifts);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return lists;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // gold coin change
    public static ApiBack coingoldChange(String sessionId, int coinStatus,
                                         int coins, int goldStatus, int golds, int actionStyle,
                                         int sportsType) throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.coingoldChange(sessionId, coinStatus, coins,
                goldStatus, golds, actionStyle, sportsType);
        ApiBack back = new ApiBack();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                back.setFlag(jsonObject.getInt("flag"));
                back.setMsg(jsonObject.getString("msg"));
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                return back;
            }
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // upload local
    public static ApiBack uploadLocal(String sessionId, String lat, String lng)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.uploadLocal(sessionId, lat, lng);
        ApiBack back = new ApiBack();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                back.setFlag(jsonObject.getInt("flag"));
                back.setMsg(jsonObject.getString("msg"));
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                return back;
            }
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // getNearby
    public static List<UserNearby> getNearby(int times, String sessionId)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.getNearby(times, sessionId);
        List<UserNearby> lists = new ArrayList<UserNearby>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            Log.i(TAG, "ncontent---" + content);
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                try {
                    JSONArray jsonArray = new JSONObject(content)
                            .getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                        // if(SportsMain.user_name.equals(jsonObject.get("name")))
                        // continue;
                        Log.e("hjtest", jsonObject.toString());
                        UserNearby users = new UserNearby();
                        users.setId(jsonObject.getInt("id"));
                        users.setImg(ApiConstant.URL
                                + jsonObject.getString("img"));
                        users.setName(jsonObject.getString("name"));
                        users.setDistance(jsonObject.getInt("distance"));
                        users.setFollowStatus((jsonObject.getString("status") == null || "null"
                                .equals(jsonObject.getString("status"))) ? 2
                                : Integer.parseInt(jsonObject
                                .getString("status")));
                        users.setSex(jsonObject.getInt("sex") == 1 ? "man"
                                : "woman");
                        users.setTime(jsonObject.getLong("local_time"));
                        users.setBirthday(jsonObject.getString("brithday"));
                        users.setLat(jsonObject.getDouble("lat"));
                        users.setLng(jsonObject.getDouble("lng"));
                        lists.add(users);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return lists;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // getNearbyNew
    public static List<UserNearby> getNearbyNew(String sessionId)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.getNearbyNew(sessionId);
        List<UserNearby> lists = new ArrayList<UserNearby>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                try {
                    JSONArray jsonArray = new JSONObject(content)
                            .getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                        // if(SportsMain.user_name.equals(jsonObject.get("name")))
                        // continue;
                        Log.e("hjtest", jsonObject.toString());
                        UserNearby users = new UserNearby();
                        users.setId(jsonObject.getInt("id"));
                        users.setImg(ApiConstant.URL
                                + jsonObject.getString("img"));
                        users.setName(jsonObject.getString("name"));
                        users.setDistance(jsonObject.getInt("distance"));
                        users.setFollowStatus((jsonObject.getString("status") == null || "null"
                                .equals(jsonObject.getString("status"))) ? 2
                                : Integer.parseInt(jsonObject
                                .getString("status")));
                        users.setSex(jsonObject.getInt("sex") == 1 ? "man"
                                : "woman");
                        users.setTime(jsonObject.getLong("local_time"));
                        users.setBirthday(jsonObject.getString("brithday"));
                        users.setLat(jsonObject.getDouble("lat"));
                        users.setLng(jsonObject.getDouble("lng"));
                        lists.add(users);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return lists;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // getNearbyBysex
    public static List<UserNearby> getNearbyBysex(int times, String sessionId,
                                                  int sex) throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.getNearbyBysex(times, sessionId, sex);
        List<UserNearby> lists = new ArrayList<UserNearby>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                try {
                    JSONArray jsonArray = new JSONObject(content)
                            .getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                        // if(SportsMain.user_name.equals(jsonObject.get("name")))
                        // continue;
                        Log.e("hjtest", jsonObject.toString());
                        UserNearby users = new UserNearby();
                        users.setId(jsonObject.getInt("id"));
                        users.setImg(ApiConstant.URL
                                + jsonObject.getString("img"));
                        users.setName(jsonObject.getString("name"));
                        users.setDistance(jsonObject.getInt("distance"));
                        users.setFollowStatus((jsonObject.getString("status") == null || "null"
                                .equals(jsonObject.getString("status"))) ? 2
                                : Integer.parseInt(jsonObject
                                .getString("status")));
                        users.setSex(jsonObject.getInt("sex") == 1 ? "man"
                                : "woman");
                        users.setTime(jsonObject.getLong("local_time"));
                        users.setBirthday(jsonObject.getString("brithday"));
                        users.setLat(jsonObject.getDouble("lat"));
                        users.setLng(jsonObject.getDouble("lng"));
                        lists.add(users);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return lists;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // getFriend
    public static List<UserNearby> getFriend(String sessionId)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.getFriend(sessionId);
        List<UserNearby> lists = new ArrayList<UserNearby>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                try {
                    JSONArray jsonArray = new JSONObject(content)
                            .getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                        // if(SportsMain.user_name.equals(jsonObject.get("name")))
                        // continue;
                        Log.e("hjtest", jsonObject.toString());
                        UserNearby users = new UserNearby();
                        users.setId(jsonObject.getInt("id"));
                        users.setImg(ApiConstant.URL
                                + jsonObject.getString("img"));
                        users.setName(jsonObject.getString("name"));
                        users.setDistance(jsonObject.getInt("distance"));
                        users.setFollowStatus((jsonObject.getString("status") == null || "null"
                                .equals(jsonObject.getString("status"))) ? 2
                                : Integer.parseInt(jsonObject
                                .getString("status")));
                        users.setSex(jsonObject.getInt("sex") == 1 ? "man"
                                : "woman");
                        users.setTime(jsonObject.getLong("local_time"));
                        users.setBirthday(jsonObject.getString("brithday"));
                        users.setLat(jsonObject.getDouble("lat"));
                        users.setLng(jsonObject.getDouble("lng"));
                        lists.add(users);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return lists;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // getFriend 获取好友列表解析
    public static List<UserNearby> getFriendList(int times, String sessionId,
                                                 Context mContext) throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.getFriendList(times, sessionId);
        List<UserNearby> lists = new ArrayList<UserNearby>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                try {
                    if (times == 0) {
                        // 保存缓存
                        SharedPreferences preferences = mContext
                                .getSharedPreferences("UserNearbylist", 0);
                        Editor edit = preferences.edit();
                        edit.putString("UserNearbylist_info", content);
                        edit.commit();
                    }

                    JSONArray jsonArray = new JSONObject(content)
                            .getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                        // if(SportsMain.user_name.equals(jsonObject.get("name")))
                        // continue;
                        Log.e("hjtest", jsonObject.toString());
                        UserNearby users = new UserNearby();
                        users.setId(jsonObject.getInt("id"));
                        users.setImg(ApiConstant.URL
                                + jsonObject.getString("img"));
                        users.setName(jsonObject.getString("name"));
                        users.setDistance(jsonObject.getInt("distance"));
                        users.setFollowStatus((jsonObject.getString("status") == null || "null"
                                .equals(jsonObject.getString("status"))) ? 2
                                : Integer.parseInt(jsonObject
                                .getString("status")));
                        users.setSex(jsonObject.getInt("sex") == 1 ? "man"
                                : "woman");
                        users.setTime(jsonObject.getLong("local_time"));
                        users.setBirthday(jsonObject.getString("brithday"));
                        lists.add(users);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return lists;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // getFriend 获取粉丝列表解析
    public static List<UserNearby> getFansList(int times, String sessionId,
                                                 Context mContext) throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.getFansList(times, sessionId);
        List<UserNearby> lists = new ArrayList<UserNearby>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                try {
                    if (times == 0) {
                        // 保存缓存
                        SharedPreferences preferences = mContext
                                .getSharedPreferences("UserNearbylist", 0);
                        Editor edit = preferences.edit();
                        edit.putString("UserNearbylist_info", content);
                        edit.commit();
                    }

                    JSONArray jsonArray = new JSONObject(content)
                            .getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                        // if(SportsMain.user_name.equals(jsonObject.get("name")))
                        // continue;
                        Log.e("hjtest", jsonObject.toString());
                        UserNearby users = new UserNearby();
                        users.setId(jsonObject.getInt("id"));
                        users.setImg(ApiConstant.URL
                                + jsonObject.getString("img"));
                        users.setName(jsonObject.getString("name"));
                        users.setDistance(jsonObject.getInt("distance"));
                        users.setFollowStatus((jsonObject.getString("status") == null || "null"
                                .equals(jsonObject.getString("status"))) ? 2
                                : Integer.parseInt(jsonObject
                                .getString("status")));
                        users.setSex(jsonObject.getInt("sex") == 1 ? "man"
                                : "woman");
                        users.setTime(jsonObject.getLong("local_time"));
                        users.setBirthday(jsonObject.getString("brithday"));
                        lists.add(users);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return lists;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // 搜索
    public static List<UserSearch> getSearchFriendsByName(int times, String name)
            throws ApiNetException {
        ApiMessage msg = Api.getSearchFriendsByName(times, name);
        List<UserSearch> lists = new ArrayList<UserSearch>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            try {
                JSONArray jsonArray = new JSONObject(content)
                        .getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                    UserSearch users = new UserSearch();
                    users.setId(jsonObject.getInt("id"));
                    users.setSex(jsonObject.getInt("sex") == 1 ? "man"
                            : "woman");
                    users.setImg(ApiConstant.URL + jsonObject.getString("img"));
                    String totalRank = jsonObject.getString("totalrank");
                    String birthday = jsonObject.getString("brithday");
                    users.setBirthday(birthday);
                    if ("null".equals(totalRank)) {
                        totalRank = "0";
                    }
                    users.setTotalRank(Integer.parseInt(totalRank));
                    users.setName(jsonObject.getString("name"));
                    lists.add(users);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return lists;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // getNearbyname
    public static List<UserNearby> getNearbyName(int times, String name, int uid)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.getUserByName(times, name, uid);
        List<UserNearby> lists = new ArrayList<UserNearby>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            // Log.d("NearbyActivity", "*********测试content******"+content);
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                sessionOut = jsonObject.getString("msg");
                // Log.d("NearbyActivity",
                // "*********测试111******"+sessionOut.toString());
            } catch (JSONException e) {
                e.printStackTrace();
                // Log.d("NearbyActivity", "*********测试2222******");
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);

            } else {
                try {
                    // Log.d("NearbyActivity", "*********测试333333******");
                    JSONArray jsonArray = new JSONObject(content)
                            .getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                        // if(SportsMain.user_name.equals(jsonObject.get("name")))
                        // continue;
                        // Log.d("NearbyActivity","****测试444444******"+
                        // jsonObject.toString());
                        UserNearby users = new UserNearby();
                        users.setId(jsonObject.getInt("id"));
                        users.setImg(ApiConstant.URL
                                + jsonObject.getString("img"));
                        users.setName(jsonObject.getString("name"));
                        users.setDistance(jsonObject.getInt("distance"));
                        users.setFollowStatus((jsonObject.getString("status") == null || "null"
                                .equals(jsonObject.getString("status"))) ? 2
                                : Integer.parseInt(jsonObject
                                .getString("status")));
                        users.setSex(jsonObject.getInt("sex") == 1 ? "man"
                                : "woman");
                        users.setTime(jsonObject.getLong("local_time"));
                        users.setBirthday(jsonObject.getString("brithday"));
                        lists.add(users);
                        // Log.d("NearbyActivity",
                        // "*********测试lists.******"+lists.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return lists;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // getFriendbyName
    public static List<UserNearby> getFriendbyName(int times, String name,
                                                   int uid) throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.getFriendByName(times, name, uid);
        List<UserNearby> lists = new ArrayList<UserNearby>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            Log.d("NearbyActivity", "content---" + name + "," + content);
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                sessionOut = jsonObject.getString("msg");
                // Log.d("NearbyActivity",
                // "*********测试111******"+sessionOut.toString());
            } catch (JSONException e) {
                e.printStackTrace();
                // Log.d("NearbyActivity", "*********测试2222******");
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);

            } else {
                try {
                    // Log.d("NearbyActivity", "*********测试333333******");
                    JSONArray jsonArray = new JSONObject(content)
                            .getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                        // if(SportsMain.user_name.equals(jsonObject.get("name")))
                        // continue;
                        // Log.d("NearbyActivity","****测试444444******"+
                        // jsonObject.toString());
                        UserNearby users = new UserNearby();
                        users.setId(jsonObject.getInt("id"));
                        users.setImg(ApiConstant.URL
                                + jsonObject.getString("img"));
                        users.setName(jsonObject.getString("name"));
                        users.setFollowStatus((jsonObject.getString("status") == null || "null"
                                .equals(jsonObject.getString("status"))) ? 2
                                : Integer.parseInt(jsonObject
                                .getString("status")));
                        users.setSex(jsonObject.getInt("sex") == 1 ? "man"
                                : "woman");
                        users.setTime(jsonObject.getLong("local_time"));
                        users.setBirthday(jsonObject.getString("brithday"));
                        users.setDistance(jsonObject.getInt("distance"));
                        lists.add(users);
                        // Log.d("NearbyActivity",
                        // "*********测试lists.******"+lists.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return lists;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // getFriendbyNameNew 搜索好友
    public static List<UserNearby> getFriendbyNameNew(int times, String name,
                                                      String sessionId) throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.getFriendByNameNew(times, name, sessionId);
        List<UserNearby> lists = new ArrayList<UserNearby>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            Log.d("NearbyActivity", "wmh*********测试content******" + content);
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                sessionOut = jsonObject.getString("msg");
                Log.d("NearbyActivity",
                        "*********测试111******" + sessionOut.toString());
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("NearbyActivity", "wmh*********测试2222******");
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);

            } else {
                try {
                    Log.d("NearbyActivity", "wmh*********测试333333******");
                    JSONArray jsonArray = new JSONObject(content)
                            .getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                        UserNearby users = new UserNearby();
                        users.setId(jsonObject.getInt("id"));
                        users.setImg(ApiConstant.URL
                                + jsonObject.getString("img"));
                        users.setName(jsonObject.getString("name"));
                        users.setFollowStatus((jsonObject.getString("status") == null || "null"
                                .equals(jsonObject.getString("status"))) ? 2
                                : Integer.parseInt(jsonObject
                                .getString("status")));
                        users.setSex(jsonObject.getInt("sex") == 1 ? "man"
                                : "woman");
                        users.setTime(jsonObject.getLong("local_time"));
                        users.setBirthday(jsonObject.getString("brithday"));
                        users.setDistance(jsonObject.getInt("distance"));
                        lists.add(users);
                        Log.d("NearbyActivity", "wmh*********测试lists.******"
                                + lists.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return lists;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // getFriendbyNameNew 搜索粉丝
    public static List<UserNearby> getFansbyNameNew(int times, String name,
                                                      String sessionId) throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.getFansByNameNew(times, name, sessionId);
        List<UserNearby> lists = new ArrayList<UserNearby>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            Log.d("NearbyActivity", "wmh*********测试content******" + content);
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                sessionOut = jsonObject.getString("msg");
                Log.d("NearbyActivity",
                        "*********测试111******" + sessionOut.toString());
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("NearbyActivity", "wmh*********测试2222******");
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);

            } else {
                try {
                    Log.d("NearbyActivity", "wmh*********测试333333******");
                    JSONArray jsonArray = new JSONObject(content)
                            .getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                        UserNearby users = new UserNearby();
                        users.setId(jsonObject.getInt("id"));
                        users.setImg(ApiConstant.URL
                                + jsonObject.getString("img"));
                        users.setName(jsonObject.getString("name"));
                        users.setFollowStatus((jsonObject.getString("status") == null || "null"
                                .equals(jsonObject.getString("status"))) ? 2
                                : Integer.parseInt(jsonObject
                                .getString("status")));
                        users.setSex(jsonObject.getInt("sex") == 1 ? "man"
                                : "woman");
                        users.setTime(jsonObject.getLong("local_time"));
                        users.setBirthday(jsonObject.getString("brithday"));
                        users.setDistance(jsonObject.getInt("distance"));
                        lists.add(users);
                        Log.d("NearbyActivity", "wmh*********测试lists.******"
                                + lists.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return lists;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // 约跑
    public static ApiBack inviteSport(String sessionID, int uid, int ouid)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.inviteSport(sessionID, uid, ouid);
        ApiBack back = new ApiBack();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                back.setFlag(jsonObject.getInt("flag"));
                back.setMsg(jsonObject.getString("msg"));
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                return back;
            }
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    public static List<UserSearch> userFollow(String sessionId, int uid,
                                              int times) throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.userFollow(sessionId, uid, times);
        List<UserSearch> lists = new ArrayList<UserSearch>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                try {
                    JSONArray jsonArray = new JSONObject(content)
                            .getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                        UserSearch users = new UserSearch();
                        users.setId(jsonObject.getInt("id"));
                        users.setImg(ApiConstant.URL
                                + jsonObject.getString("img"));
                        String totalRank = jsonObject.getString("totalrank");
                        if ("null".equals(totalRank)) {
                            totalRank = "0";
                        }
                        users.setBirthday(jsonObject.getString("brithday"));
                        users.setTotalRank(Integer.parseInt(totalRank));
                        users.setName(jsonObject.getString("name"));
                        users.setSex(jsonObject.getInt("sex") == 1 ? "man"
                                : "woman");
                        users.setFollowStatus(jsonObject.getInt("status"));
                        lists.add(users);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return lists;
            }
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    public static List<UserSearch> userFan(String sessionId, int uid, int times)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.userFan(sessionId, uid, times);
        List<UserSearch> lists = new ArrayList<UserSearch>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                try {
                    JSONArray jsonArray = new JSONObject(content)
                            .getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                        UserSearch users = new UserSearch();
                        users.setId(jsonObject.getInt("id"));
                        users.setImg(ApiConstant.URL
                                + jsonObject.getString("img"));
                        users.setBirthday(jsonObject.getString("brithday"));
                        users.setAddTime(jsonObject.getLong("add_time"));
                        users.setName(jsonObject.getString("name"));
                        users.setSex(jsonObject.getInt("sex") == 1 ? "man"
                                : "woman");
                        users.setFollowStatus(jsonObject.getInt("status"));
                        lists.add(users);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return lists;
            }
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    public static List<UserGift> userGift(String sessionId, int uid, int times)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.userGift(sessionId, uid, times);
        List<UserGift> lists = new ArrayList<UserGift>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            Log.d("ApiMessage", content);
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                try {
                    JSONArray jsonArray = new JSONObject(content)
                            .getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                        UserGift users = new UserGift();
                        users.setUid(jsonObject.getInt("uid"));
                        users.setGiftPic(ApiConstant.URL
                                + jsonObject.getString("gift_pic"));
                        users.setGiftName(jsonObject.getString("gift_name"));
                        users.setAddTime(jsonObject.getLong("add_time"));
                        users.setSendName(jsonObject.getString("name"));
                        users.setGiftCharm(jsonObject.getInt("charm_no"));
                        users.setGiftRich(jsonObject.getInt("wealth_no"));
                        lists.add(users);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return lists;
            }
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    public static ApiBack modifyLauncher(String sessionId, String username,
                                         String phoneno, String sex, String birthday)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.modifyLauncher(sessionId, username, phoneno, sex,
                birthday);
        ApiBack back = new ApiBack();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                back.setFlag(jsonObject.getInt("flag"));
                back.setMsg(jsonObject.getString("msg"));
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                return back;
            }
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    public static UserMsg getMeimeiMsg(String sessionId)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.getMeimeiMsg(sessionId);
        UserMsg userMsg = new UserMsg();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(content)
                            .getJSONObject("data");
                    userMsg.setAccount(jsonObject.getString("account_number"));
                    userMsg.setPasswd(jsonObject.getString("normalpwd"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return userMsg;
            }
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    public static UserMsg getLauncherMsg(String sessionId)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.getLauncherMsg(sessionId);
        UserMsg userMsg = new UserMsg();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(content)
                            .getJSONObject("data");
                    userMsg.setAccount(jsonObject.getString("account_number"));
                    userMsg.setPasswd(jsonObject.getString("normalpwd"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return userMsg;
            }
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    public static ApiBack combineLauncherWeibo(String weiboType,
                                               String weiboName) throws ApiNetException {
        ApiMessage msg = Api.combineLauncherWeibo(weiboType, weiboName);
        ApiBack back = new ApiBack();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                back.setFlag(jsonObject.getInt("flag"));
                back.setMsg(jsonObject.getString("msg"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return back;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    public static UserDetail refreshLauncherRank(String sessionId)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.refreshLauncherRank(sessionId);
        UserDetail userDetail = new UserDetail();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(content)
                            .getJSONObject("data");
                    userDetail.setUid(jsonObject.getInt("uid"));
                    userDetail.setUname(jsonObject.getString("uname"));
                    userDetail.setSex(jsonObject.getInt("sex") == 1 ? "man"
                            : "woman");
                    userDetail.setBirthday(jsonObject.getString("brithday"));
                    userDetail.setPhoneno(jsonObject.getString("phoneno"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return userDetail;
            }
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    public static ApiBack combineWeibo_New(String weiboType, String weiboName,
                                           String token, int wrlogin, String umengDeviceToken) throws ApiNetException {
        ApiMessage msg = Api.combineWeibo_New(weiboType, weiboName, token,
                wrlogin, umengDeviceToken);
        Log.d("ApiBack", "combineWeibo_New---weibo" + weiboType + ",weiboName"
                + weiboName + ",token" + token + "," + msg.msg);
        ApiBack back = new ApiBack();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            if (content.contains(SportsApp.getInstance().getResources().getString(R.string.user_system))){
                back.setFlag(-101);
                back.setMsg("checkyour_WIFI_isconnect");
                return back;
            }else {
                try {
                    JSONObject jsonObject = new JSONObject(content)
                            .getJSONObject("data");
                    back.setFlag(jsonObject.getInt("flag"));
                    back.setMsg(jsonObject.getString("msg"));
                    back.setFirst(jsonObject.getInt("fstatus"));
                    back.setReg(jsonObject.getInt("reg"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return back;
            }
        } else {
            if (msg != null && ("SocketTimeoutException").equals(msg.getMsg())){
                String content2 = msg.getMsg();
                back.setFlag(-56);
                back.setMsg(content2);
                return back;
            }else{
                throw new ApiNetException("error:" + msg.getMsg());
            }
        }
    }

    // every day golds
    public static ApiBack getdaygolds(String sessionId) throws ApiNetException,
            ApiSessionOutException {
        ApiMessage msg = Api.getdaygolds(sessionId);
        ApiBack back = new ApiBack();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                back.setFlag(jsonObject.getInt("flag"));
                back.setMsg(jsonObject.getString("msg"));
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                return back;
            }
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // back params
    // me or other
    public static ApiBack judgePic(String sessionId, int uid)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.judgePic(sessionId, uid);
        ApiBack back = new ApiBack();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                back.setFlag(jsonObject.getInt("flag"));
                back.setMsg(jsonObject.getString("msg"));
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                return back;
            }
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // charmrank
    // sex 1——boy 2——girl
    public static List<UserRank> charmrank(int type, int times, int sex)
            throws ApiNetException {
        ApiMessage msg = Api.charmrank(type, times, sex);
        List<UserRank> lists = new ArrayList<UserRank>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            try {
                JSONArray jsonArray = new JSONObject(content)
                        .getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                    Log.e("hjtest", jsonObject.toString());
                    UserRank users = new UserRank();
                    users.setId(jsonObject.getInt("id"));
                    users.setImg(ApiConstant.URL + jsonObject.getString("img"));
                    users.setRankNumber(jsonObject.getInt("ranks"));
                    users.setName(jsonObject.getString("name"));
                    users.setAuthStatus(jsonObject.getInt("authstatus"));
                    lists.add(users);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return lists;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // visitor
    public static List<UserNearby> userVisitor(String sessionId, int times)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.userVisitor(sessionId, times);
        List<UserNearby> lists = new ArrayList<UserNearby>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                try {
                    JSONArray jsonArray = new JSONObject(content)
                            .getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                        UserNearby users = new UserNearby();
                        users.setId(jsonObject.getInt("id"));
                        users.setImg(ApiConstant.URL
                                + jsonObject.getString("img"));
                        users.setName(jsonObject.getString("name"));
                        users.setSex(jsonObject.getInt("sex") == 1 ? "man"
                                : "woman");
                        users.setTime(jsonObject.getLong("add_time"));
                        users.setBirthday(jsonObject.getString("brithday"));
                        lists.add(users);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return lists;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // visitor person
    public static ApiBack visitor(String sessionId, int uid)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.visitor(sessionId, uid);
        ApiBack back = new ApiBack();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                back.setFlag(jsonObject.getInt("flag"));
                back.setMsg(jsonObject.getString("msg"));
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                return back;
            }
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // msg box comments
    public static List<UserMeComments> getCommentsMe(String sessionId, int times)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.getCommentsMe(sessionId, times);
        List<UserMeComments> lists = new ArrayList<UserMeComments>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                try {
                    JSONArray jsonArray = new JSONObject(content)
                            .getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                        UserMeComments userComment = new UserMeComments();
                        userComment.setUid(jsonObject.getInt("uid"));
                        userComment
                                .setCommentText(jsonObject.getString("info"));
                        userComment.setCommentWav(ApiConstant.URL
                                + jsonObject.getString("wav"));
                        userComment.setAddTime(jsonObject.getLong("add_time"));
                        userComment.setUname(jsonObject.getString("uname"));
                        userComment.setUimg(ApiConstant.URL
                                + jsonObject.getString("uimg"));
                        userComment.setPid(jsonObject.getInt("pid"));
                        userComment.setId(jsonObject.getInt("id"));
                        userComment
                                .setWavDuration("null".equals(jsonObject
                                        .getString("wavdurations")) ? 0
                                        : Integer.parseInt(jsonObject
                                        .getString("wavdurations")));
                        userComment.setPimg(ApiConstant.URL
                                + jsonObject.getString("img"));
                        userComment.setBirthday(jsonObject
                                .getString("brithday"));
                        userComment
                                .setSex((jsonObject.getInt("sex") == 1 ? "man"
                                        : "woman"));
                        lists.add(userComment);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return lists;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // msg box delcomments
    public static ApiBack delComments(String sessionId, int commentId)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.delComments(sessionId, commentId);
        ApiBack back = new ApiBack();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                back.setFlag(jsonObject.getInt("flag"));
                back.setMsg(jsonObject.getString("msg"));
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                return back;
            }
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // msg box getmylikes
    public static List<UserMyLikes> getMyLikes(String sessionId, int times)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.getMyLikes(sessionId, times);
        List<UserMyLikes> lists = new ArrayList<UserMyLikes>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                try {
                    JSONArray jsonArray = new JSONObject(content)
                            .getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                        UserMyLikes userComment = new UserMyLikes();
                        userComment.setUid(jsonObject.getInt("uid"));
                        userComment.setAddTime(jsonObject.getLong("add_time"));
                        userComment.setUname(jsonObject.getString("uname"));
                        userComment.setUimg(ApiConstant.URL
                                + jsonObject.getString("uimg"));
                        userComment.setPid(jsonObject.getInt("pid"));
                        userComment.setPimg(ApiConstant.URL
                                + jsonObject.getString("img"));
                        userComment.setBirthday(jsonObject
                                .getString("brithday"));
                        userComment
                                .setSex((jsonObject.getInt("sex") == 1 ? "man"
                                        : "woman"));
                        lists.add(userComment);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return lists;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    public static ApiBack sendprimsg(String sessionId, int uid,
                                     String comments, String wav, int wavDurations)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.sendprimsg(sessionId, uid, comments, wav,
                wavDurations);
        ApiBack back = new ApiBack();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                back.setFlag(jsonObject.getInt("flag"));
                back.setMsg(jsonObject.getString("msg"));
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                return back;
            }
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // msg box getprimsgall
    public static List<UserPrimsgAll> getPrimsgAll(String sessionId, int times)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.getPrimsgAll(sessionId, times);
        List<UserPrimsgAll> lists = new ArrayList<UserPrimsgAll>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            Log.i("priall", "get priall" + content);
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                try {
                    JSONArray jsonArray = new JSONObject(content)
                            .getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                        UserPrimsgAll umsgall = new UserPrimsgAll();
                        umsgall.setSex(jsonObject.getInt("sex") == 1 ? "man"
                                : "woman");
                        umsgall.setName(jsonObject.getString("name"));
                        umsgall.setBirthday(jsonObject.getString("brithday"));
                        umsgall.setUid(jsonObject.getInt("uid"));
                        umsgall.setCounts(jsonObject.getInt("counts"));
                        umsgall.setAddTime(jsonObject.getLong("add_time"));
                        umsgall.setTouimg(ApiConstant.URL
                                + jsonObject.getString("touimg"));
                        umsgall.setUimg(ApiConstant.URL
                                + jsonObject.getString("uimg"));
                        umsgall.setTouid(jsonObject.getInt("touid"));
                        lists.add(umsgall);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return lists;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // msg box getprimsgone
    public static List<UserPrimsgOne> getPrimsgOne(String sessionId, int uid,
                                                   int times) throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.getPrimsgOne(sessionId, uid, times);
        List<UserPrimsgOne> lists = new ArrayList<UserPrimsgOne>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            Log.i("primsg", "get primsg" + content);
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                try {
                    JSONArray jsonArray = new JSONObject(content)
                            .getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                        UserPrimsgOne umsgone = new UserPrimsgOne();
                        umsgone.setUid(jsonObject.getInt("uid"));
                        umsgone.setAddTime(jsonObject.getLong("add_time"));
                        umsgone.setTouid(jsonObject.getInt("touid"));
                        umsgone.setCommentText(jsonObject.getString("info"));
                        umsgone.setCommentWav(ApiConstant.URL
                                + jsonObject.getString("wav"));
                        umsgone.setWavDuration("null".equals(jsonObject
                                .getString("wavdurations")) ? 0 : Integer
                                .parseInt(jsonObject.getString("wavdurations")));
                        umsgone.setOwnerid(jsonObject.getInt("touid"));
                        Log.d(TAG, "msg in server" + umsgone.getOwnerid());
                        lists.add(umsgone);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return lists;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // msgbox blackpeople
    public static ApiBack blackpeople(String sessionId, int uid, int status)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.blackpeople(sessionId, uid, status);
        ApiBack back = new ApiBack();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                back.setFlag(jsonObject.getInt("flag"));
                back.setMsg(jsonObject.getString("msg"));
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                return back;
            }
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // msg box blacklist
    public static List<UserBlackList> getBlackList(String sessionId, int times)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.getBlackList(sessionId, times);
        List<UserBlackList> lists = new ArrayList<UserBlackList>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                try {
                    JSONArray jsonArray = new JSONObject(content)
                            .getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                        UserBlackList users = new UserBlackList();
                        users.setUid(jsonObject.getInt("buid"));
                        users.setImg(ApiConstant.URL
                                + jsonObject.getString("img"));
                        users.setName(jsonObject.getString("name"));
                        users.setSex(jsonObject.getInt("sex") == 1 ? "man"
                                : "woman");
                        users.setAddTime(jsonObject.getLong("add_time"));
                        users.setBirthday(jsonObject.getString("brithday"));
                        lists.add(users);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return lists;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // msg box sunshine
    public static ApiBack sunshine(String sessionId, int level)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.sunshine(sessionId, level);
        ApiBack back = new ApiBack();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                back.setFlag(jsonObject.getInt("flag"));
                back.setMsg(jsonObject.getString("msg"));
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                return back;
            }
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // msg box getsunshine
    public static List<UserBlackList> getSunshine(String sessionId, int times)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.getSunshine(sessionId, times);
        List<UserBlackList> lists = new ArrayList<UserBlackList>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                try {
                    JSONArray jsonArray = new JSONObject(content)
                            .getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                        UserBlackList users = new UserBlackList();
                        users.setUid(jsonObject.getInt("uid"));
                        users.setImg(ApiConstant.URL
                                + jsonObject.getString("img"));
                        users.setName(jsonObject.getString("name"));
                        users.setSex(jsonObject.getInt("sex") == 1 ? "man"
                                : "woman");
                        users.setAddTime(jsonObject.getLong("add_time"));
                        users.setBirthday(jsonObject.getString("brithday"));
                        lists.add(users);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return lists;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // msg box getsysmsg
    public static List<SysMsg> getSysmsg(String sessionId, int times)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.getSysmsg(sessionId, times);
        List<SysMsg> lists = new ArrayList<SysMsg>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                try {
                    JSONArray jsonArray = new JSONObject(content)
                            .getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                        SysMsg sysmsg = new SysMsg();
                        sysmsg.setAddTime(jsonObject.getLong("add_time"));
                        sysmsg.setContent(jsonObject.getString("msg"));
                        if (jsonObject.has("img")) {
                            sysmsg.setImg(jsonObject.getString("img"));
                        }
                        if (jsonObject.has("url")) {
                            sysmsg.setUrl(jsonObject.getString("url"));
                        }
                        if (jsonObject.has("shareurl")) {
                            sysmsg.setShareurl(jsonObject.getString("shareurl"));
                        }

                        lists.add(sysmsg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return lists;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // msg box follow upload
    public static List<UserFollowMsg> ffollow(String sessionId, int times)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.ffollow(sessionId, times);
        List<UserFollowMsg> lists = new ArrayList<UserFollowMsg>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                try {
                    JSONArray jsonArray = new JSONObject(content)
                            .getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                        UserFollowMsg users = new UserFollowMsg();
                        users.setId(jsonObject.getInt("id"));
                        users.setImg(ApiConstant.URL
                                + jsonObject.getString("img"));
                        users.setName(jsonObject.getString("name"));
                        users.setSex(jsonObject.getInt("sex") == 1 ? "man"
                                : "woman");
                        users.setAddTime(jsonObject.getLong("add_time"));
                        users.setOsex(jsonObject.getInt("osex") == 1 ? "man"
                                : "woman");
                        users.setBirthday(jsonObject.getString("brithday"));
                        users.setOid(jsonObject.getInt("uid"));
                        users.setOimg(ApiConstant.URL
                                + jsonObject.getString("oimg"));
                        lists.add(users);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return lists;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // msg box follow gift
    public static List<UserFollowMsg> fupload(String sessionId, int times)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.fupload(sessionId, times);
        List<UserFollowMsg> lists = new ArrayList<UserFollowMsg>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                try {
                    JSONArray jsonArray = new JSONObject(content)
                            .getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                        UserFollowMsg users = new UserFollowMsg();
                        users.setId(jsonObject.getInt("id"));
                        users.setImg(ApiConstant.URL
                                + jsonObject.getString("img"));
                        users.setName(jsonObject.getString("name"));
                        users.setSex(jsonObject.getInt("sex") == 1 ? "man"
                                : "woman");
                        users.setAddTime(jsonObject.getLong("add_time"));
                        users.setBirthday(jsonObject.getString("brithday"));
                        users.setPid(jsonObject.getInt("picid"));
                        users.setPimg(ApiConstant.URL
                                + jsonObject.getString("pimg"));
                        lists.add(users);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return lists;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // msg box follow follow
    public static List<UserFollowMsg> fgift(String sessionId, int times)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.fgift(sessionId, times);
        List<UserFollowMsg> lists = new ArrayList<UserFollowMsg>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                try {
                    JSONArray jsonArray = new JSONObject(content)
                            .getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                        UserFollowMsg users = new UserFollowMsg();
                        users.setId(jsonObject.getInt("id"));
                        users.setImg(ApiConstant.URL
                                + jsonObject.getString("img"));
                        users.setName(jsonObject.getString("name"));
                        users.setSex(jsonObject.getInt("sex") == 1 ? "man"
                                : "woman");
                        users.setOsex(jsonObject.getInt("osex") == 1 ? "man"
                                : "woman");
                        users.setAddTime(jsonObject.getLong("add_time"));
                        users.setBirthday(jsonObject.getString("brithday"));
                        users.setGiftPic(ApiConstant.URL
                                + jsonObject.getString("gift_pic"));
                        users.setOid(jsonObject.getInt("uid"));
                        users.setOimg(ApiConstant.URL
                                + jsonObject.getString("oimg"));
                        lists.add(users);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return lists;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // down img
    public static ApiBack downImg(String sessionId, int uid)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.downImg(sessionId, uid);
        ApiBack back = new ApiBack();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                back.setFlag(jsonObject.getInt("flag"));
                back.setMsg(jsonObject.getString("msg"));
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                return back;
            }
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // activities
    public static ApiBack activities(String sessionId, int channel)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.activities(sessionId, channel);
        ApiBack back = null;
        if (msg.isFlag()) {
            back = new ApiBack();
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                back.setFlag(jsonObject.getInt("flag"));
                back.setMsg(jsonObject.getString("msg"));
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                return back;
            }
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    public static MsgCounts getMsgCounts(String sessionId)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.getMsgCounts(sessionId);
        MsgCounts msgcounts = new MsgCounts(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0);
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(content)
                            .getJSONObject("data");
                    if (jsonObject.getInt("fans") >= 0) {
                        msgcounts.setFans(jsonObject.getInt("fans"));
                        msgcounts.setSportVisitor(jsonObject
                                .getInt("sports_visitor"));
                        msgcounts.setSportComments(jsonObject
                                .getInt("sport_comments"));
                        msgcounts.setLikes(jsonObject.getInt("likes"));
                        msgcounts.setGifts(jsonObject.getInt("gifts"));
                        msgcounts.setFollows(jsonObject.getInt("follows"));
                        msgcounts.setPrimsg(jsonObject.getInt("primsg"));
                        msgcounts.setSuns(jsonObject.getInt("suns"));
                        msgcounts.setSysmsgsports(jsonObject
                                .getInt("sysmsgsports"));
                        msgcounts.setfUploads(jsonObject.getInt("f_uploads"));
                        msgcounts.setfFollows(jsonObject.getInt("f_follows"));
                        msgcounts.setfGifts(jsonObject.getInt("f_gifts"));
                        msgcounts.setTotal(jsonObject.getInt("total"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return msgcounts;
            }
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // get tradeno
    public static ApiBack getTradeNo(String sessionId, int price)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.getTradeNo(sessionId, price);
        ApiBack back = new ApiBack();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                back.setFlag(jsonObject.getInt("flag"));
                back.setMsg(jsonObject.getString("msg"));
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                return back;
            }
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    public static List<ItemsCate> navigation() throws ApiNetException {
        ApiMessage msg = Api.navigation();
        List<ItemsCate> lists = new ArrayList<ItemsCate>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            try {
                JSONArray jsonArray = new JSONObject(content)
                        .getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                    ItemsCate itemsCate = new ItemsCate();
                    itemsCate.setId(jsonObject.getInt("id"));
                    itemsCate.setImgUrl(ApiConstant.URL
                            + jsonObject.getString("imgurl"));
                    lists.add(itemsCate);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return lists;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    public static ApiBack naviReplace(int id, String imgUrl)
            throws ApiNetException {
        ApiMessage msg = Api.naviReplace(id, imgUrl);
        ApiBack back = new ApiBack();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                back.setFlag(jsonObject.getInt("flag"));
                back.setMsg(jsonObject.getString("msg"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return back;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    public static ApiBack authentication(String sessionId, String pic)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.authentication(sessionId, pic);
        ApiBack back = new ApiBack();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                back.setFlag(jsonObject.getInt("flag"));
                back.setMsg(jsonObject.getString("msg"));
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                return back;
            }
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    public static ApiBack verify(int id, int status) throws ApiNetException {
        ApiMessage msg = Api.verify(id, status);
        ApiBack back = new ApiBack();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                back.setFlag(jsonObject.getInt("flag"));
                back.setMsg(jsonObject.getString("msg"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return back;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    public static ApiBack forbid(int uid) throws ApiNetException {
        ApiMessage msg = Api.forbid(uid);
        ApiBack back = new ApiBack();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                back.setFlag(jsonObject.getInt("flag"));
                back.setMsg(jsonObject.getString("msg"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return back;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    public static ApiBack examine(int picid, int status) throws ApiNetException {
        ApiMessage msg = Api.examine(picid, status);
        ApiBack back = new ApiBack();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                back.setFlag(jsonObject.getInt("flag"));
                back.setMsg(jsonObject.getString("msg"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return back;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    public static List<PicsAndIds> getExamine() throws ApiNetException {
        ApiMessage msg = Api.getExamine();
        List<PicsAndIds> lists = new ArrayList<PicsAndIds>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            try {
                JSONArray jsonArray = new JSONObject(content)
                        .getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                    PicsAndIds picsAndIds = new PicsAndIds();
                    picsAndIds.setId(jsonObject.getInt("id"));
                    picsAndIds.setImgUrl(ApiConstant.URL
                            + jsonObject.getString("img"));
                    lists.add(picsAndIds);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return lists;

        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    public static Notification getNotification() throws ApiNetException {
        ApiMessage msg = Api.getNotification();
        Notification notification = new Notification();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                notification.setAddTime(jsonObject.getLong("add_time"));
                notification.setId(jsonObject.getInt("id"));
                notification.setMsg(jsonObject.getString("msg"));
                notification.setMsgId(jsonObject.getInt("msg_id"));
                notification.setUid(jsonObject.getInt("uid"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return notification;

        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    public static List<Help> getHelp() throws ApiNetException {
        int id = 1;
        ApiMessage msg = Api.getHelp();
        List<Help> lists = new ArrayList<Help>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            try {
                JSONArray jsonArray = new JSONObject(content)
                        .getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                    Help help = new Help();
                    help.setId(id++);
                    help.setTitle(jsonObject.getString("title"));
                    help.setMsg(jsonObject.getString("msg"));
                    help.setAddTime(jsonObject.getLong("add_time"));
                    lists.add(help);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return lists;

        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // cmcc
    public static ApiBack cmcc(String sessionId, int price)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.cmcc(sessionId, price);
        ApiBack back = new ApiBack();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                back.setFlag(jsonObject.getInt("flag"));
                back.setMsg(jsonObject.getString("msg"));
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                return back;
            }
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    public static UserDetail seeUserSimple(String sessionId, int uid)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.seeUserSimple(sessionId, uid);
        UserDetail userDetail = new UserDetail();
        List<PicsAndIds> imgs = new ArrayList<PicsAndIds>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(content)
                            .getJSONObject("data");
                    userDetail.setUid(jsonObject.getInt("uid"));
                    userDetail.setUname(jsonObject.getString("uname"));
                    userDetail.setUimg(ApiConstant.URL
                            + jsonObject.getString("uimg"));
                    userDetail.setCounts(jsonObject.getInt("counts"));
                    userDetail.setTotalRank(jsonObject.getInt("totalrank"));
                    userDetail.setTotalLikes(jsonObject.getInt("totallikes"));
                    userDetail.setSex(jsonObject.getInt("sex") == 1 ? "man"
                            : "woman");
                    userDetail.setBirthday(jsonObject.getString("brithday"));
                    userDetail.setPhoneno(jsonObject.getString("phoneno"));
                    if (jsonObject.has("fancounts")) {
                        userDetail.setFanCounts((jsonObject.getString("fancounts") == null || "null"
                                .equals(jsonObject.getString("fancounts"))) ? 0
                                : Integer.parseInt(jsonObject
                                .getString("fancounts")));
                    }
                    if (jsonObject.has("followcounts")) {
                        userDetail.setFollowCounts((jsonObject.getString("status") == null || "null"
                                .equals(jsonObject.getString("status"))) ? 0
                                : Integer.parseInt(jsonObject
                                .getString("status")));
                    }
                    userDetail.setGiftCounts(jsonObject.getInt("giftcounts"));
//                    userDetail.setFollowStatus(jsonObject.getInt("status"));
                    userDetail.setFollowStatus((jsonObject.getString("status") == null || "null"
                            .equals(jsonObject.getString("status"))) ? 2
                            : Integer.parseInt(jsonObject
                            .getString("status")));

                    userDetail.setAuthPic(ApiConstant.URL
                            + jsonObject.getString("authpic"));
                    userDetail.setAuthStatus(jsonObject.getInt("authstatus"));

                    userDetail.setCharms(jsonObject.getInt("charms"));
                    userDetail.setUpcharms(jsonObject.getInt("upcharms"));
                    userDetail.setWealths(jsonObject.getInt("wealths"));
                    userDetail.setUpwealths(jsonObject.getInt("upwealths"));
                    userDetail.setKind(jsonObject.getInt("kind"));
                    userDetail.setUpkind(jsonObject.getInt("upkind"));
                    userDetail.setEqutype(jsonObject.getString("equtype"));
                    userDetail.setProvince(jsonObject.getString("province"));
                    userDetail.setCity(jsonObject.getString("city"));
                    userDetail.setArea(jsonObject.getString("area"));
                    double double2 = jsonObject.getDouble("sportsdata");
                    userDetail.setSportsdata(SportTaskUtil
                            .getDoubleNumber(double2));
                    if (jsonObject.has("count_num")) {
                        userDetail.setCount_num(jsonObject.getInt("count_num"));
                    }
                    if (jsonObject.has("sprots_Calorie")) {
                        userDetail.setSprots_Calorie(jsonObject
                                .getDouble("sprots_Calorie"));
                    }
                    if (jsonObject.has("time")) {
                        userDetail.setTime(jsonObject.getInt("time"));
                    }
                    userDetail.setSignature(jsonObject.getString("signature"));
                    userDetail.setCoins(jsonObject.getInt("coins"));
                    // userDetail.setKindrank(jsonObject.getInt("kindrank"));
                    // userDetail.setCharmsrank(jsonObject.getInt("charmsrank"));
                    // userDetail.setWealthsrank(jsonObject.getInt("wealthsrank"));
                    userDetail.setOwner("me".equals(jsonObject
                            .getString("isowner")));
                    if (jsonObject.has("find_count_num")){
                        userDetail.setFind_count_num(jsonObject.getInt("find_count_num"));
                    }
                   if (jsonObject.has("medalnum")){
                       userDetail.setMedal_num(jsonObject.getInt("medalnum"));
                   }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return userDetail;
            }
        } else {
            // throw new ApiNetException("error:" + msg.getMsg());
            return userDetail;
        }
    }

    public static UserDetail seeUserDetail(String sessionId, int uid)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.seeUserDetail(sessionId, uid);
        UserDetail userDetail = new UserDetail();
        List<PicsAndIds> imgs = new ArrayList<PicsAndIds>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(content)
                            .getJSONObject("data");
                    userDetail.setKindrank(jsonObject.getInt("kindrank"));
                    userDetail.setCharmsrank(jsonObject.getInt("charmsrank"));
                    userDetail.setWealthsrank(jsonObject.getInt("wealthsrank"));
                    JSONArray jsonArray = new JSONObject(jsonObject.toString())
                            .getJSONArray("imgs");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
                        ImageDetail imageDetail = new ImageDetail();
                        List<String> labels = new ArrayList<String>();

                        PicsAndIds img = new PicsAndIds();
                        img.setId(jsonObject2.getInt("pid"));
                        img.setImgUrl(ApiConstant.URL
                                + jsonObject2.getString("pimg"));

                        JSONArray jsons = new JSONObject(jsonObject2.toString())
                                .getJSONArray("detail");
                        JSONObject jsonObject3 = (JSONObject) jsons.opt(0);
                        imageDetail.setUid(jsonObject3.getInt("uid"));
                        imageDetail.setLikes(jsonObject3.getInt("likes"));
                        imageDetail.setUname(jsonObject3.getString("uname"));
                        imageDetail.setUimg(ApiConstant.URL
                                + jsonObject3.getString("uimg"));
                        imageDetail.setPid(jsonObject3.getInt("pid"));
                        imageDetail.setPimg(ApiConstant.URL
                                + jsonObject3.getString("pimg"));
                        imageDetail.setPtitle(jsonObject3.getString("ptitle"));
                        imageDetail.setAddTime(jsonObject3.getLong("add_time"));
                        imageDetail
                                .setLiked(jsonObject3.getInt("likestatus") == 0 ? false
                                        : true);
                        imageDetail.setTotalRank(jsonObject3
                                .getInt("totalrank"));
                        imageDetail.setWav(ApiConstant.URL
                                + jsonObject3.getString("wav"));
                        imageDetail
                                .setWavDurations((jsonObject3
                                        .getString("wavdurations") == null || "null"
                                        .equals(jsonObject3
                                                .getString("wavdurations"))) ? 0
                                        : Integer.parseInt(jsonObject3
                                        .getString("wavdurations")));
                        imageDetail.setWeiboName(jsonObject3
                                .getString("weiboname"));
                        JSONArray jsonArray2 = new JSONObject(
                                jsonObject3.toString()).getJSONArray("labels");
                        for (int j = 0; j < jsonArray2.length(); j++) {
                            JSONObject jsonObject4 = (JSONObject) jsonArray2
                                    .opt(j);
                            labels.add(jsonObject4.getString("cid"));
                        }
                        imageDetail.setLabels(labels);
                        img.setImgDetail(imageDetail);

                        imgs.add(img);
                    }
                    userDetail.setImgs(imgs);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return userDetail;
            }
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    public static List<PhotoFrames> getPhotoFrames() throws ApiNetException {
        ApiMessage msg = Api.getPhotoFrames();
        List<PhotoFrames> lists = new ArrayList<PhotoFrames>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            try {
                JSONArray jsonArray = new JSONObject(content)
                        .getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                    PhotoFrames itemsCate = new PhotoFrames();
                    itemsCate.setId(jsonObject.getInt("id"));
                    itemsCate.setAddTime(jsonObject.getLong("add_time"));
                    itemsCate.setFrameName(jsonObject.getString("photoname"));
                    itemsCate.setFrametype(jsonObject.getInt("frametype"));
                    itemsCate.setLevel(jsonObject.getInt("level"));
                    itemsCate.setTopLevel(jsonObject.getInt("toplevel"));
                    itemsCate.setPhotoFrame(ApiConstant.URL
                            + jsonObject.getString("photoframe"));
                    lists.add(itemsCate);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return lists;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    public static List<PhotoFrames> getFramesById(int id)
            throws ApiNetException {
        ApiMessage msg = Api.getFramesById(id);
        List<PhotoFrames> lists = new ArrayList<PhotoFrames>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            try {
                JSONArray jsonArray = new JSONObject(content)
                        .getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                    PhotoFrames itemsCate = new PhotoFrames();
                    itemsCate.setId(jsonObject.getInt("id"));
                    itemsCate.setFrametype(jsonObject.getInt("frametype"));
                    itemsCate.setLevel(jsonObject.getInt("level"));
                    itemsCate.setTopLevel(jsonObject.getInt("toplevel"));
                    itemsCate.setPhotoFrame(ApiConstant.URL
                            + jsonObject.getString("photoframe"));
                    lists.add(itemsCate);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return lists;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    public static List<UserRank> wmqrank(int type, int times)
            throws ApiNetException {
        ApiMessage msg = Api.wmqrank(type, times);
        List<UserRank> lists = new ArrayList<UserRank>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            try {
                JSONArray jsonArray = new JSONObject(content)
                        .getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                    UserRank users = new UserRank();
                    users.setId(jsonObject.getInt("id"));
                    users.setImg(ApiConstant.URL + jsonObject.getString("img"));
                    users.setRankNumber(jsonObject.getInt("ranks"));
                    users.setName(jsonObject.getString("name"));
                    users.setAuthStatus(jsonObject.getInt("authstatus"));
                    lists.add(users);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return lists;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // 修改身高体重
    public static ApiBack instWeightAndHeight(String sessionId, int weight,
                                              int height) throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.instWeightAndHeight(sessionId, weight, height);
        ApiBack back = new ApiBack();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                back.setFlag(jsonObject.getInt("flag"));
                back.setMsg(jsonObject.getString("msg"));
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                return back;
            }
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // 上传位置(不包含图片)
    public static ApiBack uploadSportsType(String sessionId, int sportsType,
                                           int scoreStep, int scoreCalories, int steps, int calories)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.uploadSportsType(sessionId, sportsType, scoreStep,
                scoreCalories, steps, calories);
        ApiBack back = new ApiBack();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                back.setFlag(jsonObject.getInt("flag"));
                back.setMsg(jsonObject.getString("msg"));
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                return back;
            }
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // 上传位置(包含图片)
    public static ApiBack uploadSportsHistoryWithImg(String picTitle,
                                                     String picAddress, String sessionId, String wav, int wavDurations,
                                                     String lat, String lng, int steps, int calories, int scoreStep,
                                                     int scoreCalories, int sportsType) throws ApiNetException,
            ApiSessionOutException {
        ApiMessage msg = Api.uploadSportsHistoryWithImg(picTitle, picAddress,
                sessionId, wav, wavDurations, lat, lng, steps, calories,
                scoreStep, scoreCalories, sportsType);
        ApiBack back = new ApiBack();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                back.setFlag(jsonObject.getInt("flag"));
                back.setMsg(jsonObject.getString("msg"));
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                return back;
            }
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // 位置图片审核
    public static ApiBack sportsExamine(int picid, int status)
            throws ApiNetException {
        ApiMessage msg = Api.sportsExamine(picid, status);
        ApiBack back = new ApiBack();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                back.setFlag(jsonObject.getInt("flag"));
                back.setMsg(jsonObject.getString("msg"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return back;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // 位置图片获取
    public static List<PicsAndIds> getSportsExamine() throws ApiNetException {
        ApiMessage msg = Api.getSportsExamine();
        List<PicsAndIds> lists = new ArrayList<PicsAndIds>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            try {
                JSONArray jsonArray = new JSONObject(content)
                        .getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                    PicsAndIds picsAndIds = new PicsAndIds();
                    picsAndIds.setId(jsonObject.getInt("id"));
                    picsAndIds.setImgUrl(ApiConstant.URL
                            + jsonObject.getString("img"));
                    lists.add(picsAndIds);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return lists;

        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    public static List<UserRank> sportsRank(int type, int times,
                                            String sessionId) throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.sportsRank(type, times, sessionId);
        List<UserRank> lists = new ArrayList<UserRank>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                try {
                    JSONArray jsonArray = new JSONObject(content)
                            .getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                        Log.e("hjtest", jsonObject.toString());
                        UserRank users = new UserRank();
                        users.setId(jsonObject.getInt("id"));
                        users.setImg(ApiConstant.URL
                                + jsonObject.getString("img"));
                        users.setRankNumber(jsonObject.getInt("ranks"));
                        users.setName(jsonObject.getString("name"));
                        users.setAuthStatus(jsonObject.getInt("authstatus"));
                        lists.add(users);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return lists;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    public static List<UserRank> sportsRankNew(int type, int times,
                                               String sessionId, Context mContext) throws ApiNetException,
            ApiSessionOutException {
        ApiMessage msg = Api.sportsRankNew(type, times, sessionId);
        List<UserRank> lists = new ArrayList<UserRank>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                try {
                    JSONArray jsonArray = new JSONObject(content)
                            .getJSONArray("data");
                    if (times == 0) {
                        // 保存用户排行榜
                        SharedPreferences preferences = mContext
                                .getSharedPreferences("UserRankList" + type, 0);
                        Editor edit = preferences.edit();
                        edit.putString("UserRankList_info", content);
                        edit.commit();
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                        Log.e("hjtest", jsonObject.toString());
                        UserRank users = new UserRank();
                        users.setId(jsonObject.getInt("id"));
                        users.setImg(ApiConstant.URL
                                + jsonObject.getString("img"));
                        users.setRankNumber(jsonObject.getInt("ranks"));
                        users.setName(jsonObject.getString("name"));
                        users.setAuthStatus(jsonObject.getInt("authstatus"));
                        users.setSex(jsonObject.getInt("sex"));
                        lists.add(users);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return lists;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // 获取运动好友动态
    public static int getSportsActs(String sessionId) throws ApiNetException,
            ApiSessionOutException {
        ApiMessage msg = Api.getSportsActs(sessionId);
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            int actmsgs = 0;
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(content)
                            .getJSONObject("data");
                    if (jsonObject.getInt("actmsgs") >= 0) {
                        actmsgs = jsonObject.getInt("actmsgs");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return actmsgs;
            }
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // msg box follow gift
    public static List<UserFollowMsg> sportsActs(String sessionId, int times)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.sportsActs(sessionId, times);
        List<UserFollowMsg> lists = new ArrayList<UserFollowMsg>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                try {
                    JSONArray jsonArray = new JSONObject(content)
                            .getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                        UserFollowMsg users = new UserFollowMsg();
                        users.setId(jsonObject.getInt("id"));
                        users.setImg(ApiConstant.URL
                                + jsonObject.getString("img"));
                        users.setName(jsonObject.getString("name"));
                        users.setSex(jsonObject.getInt("sex") == 1 ? "man"
                                : "woman");
                        users.setAddTime(jsonObject.getLong("add_time"));
                        users.setBirthday(jsonObject.getString("brithday"));
                        users.setPid(jsonObject.getInt("picid"));
                        users.setPimg(ApiConstant.URL
                                + jsonObject.getString("pimg"));
                        users.setGainCalories(jsonObject.getInt("gain_calorie"));
                        lists.add(users);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return lists;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    public static List<UserSports> getSportsDetails(String sessionId,
                                                    String startTime, String endTime, int uid) throws ApiNetException,
            ApiSessionOutException {
        ApiMessage msg = Api.getSportsDetails(sessionId, startTime, endTime,
                uid);
        List<UserSports> lists = new ArrayList<UserSports>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                try {
                    JSONArray jsonArray = new JSONObject(content)
                            .getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                        Log.e("hjtest", jsonObject.toString());
                        UserSports users = new UserSports();
                        users.setUid(jsonObject.getInt("uid"));
                        users.setCalories(jsonObject.getInt("calories"));
                        users.setStep(jsonObject.getInt("steps"));
                        users.setScoreCalorie(jsonObject
                                .getInt("score_calorie"));
                        users.setScoreStep(jsonObject.getInt("score_step"));
                        users.setSportsType(jsonObject.getInt("sports_type"));
                        lists.add(users);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return lists;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    public static List<ImageDetail> getdayimgs(String sessionId,
                                               String startTime, String endTime, int uid) throws ApiNetException,
            ApiSessionOutException {
        ApiMessage msg = Api.getdayimgs(sessionId, startTime, endTime, uid);
        List<ImageDetail> lists = new ArrayList<ImageDetail>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            Log.e("hjtest", "lllat : content " + content);
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                try {
                    JSONArray jsonArray = new JSONObject(content)
                            .getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
                        ImageDetail imageDetail = new ImageDetail();
                        imageDetail.setUid(jsonObject2.getInt("uid"));
                        imageDetail
                                .setSex(jsonObject2.getInt("sex") == 1 ? "man"
                                        : "woman");
                        imageDetail.setUname(jsonObject2.getString("uname"));
                        imageDetail.setUimg(ApiConstant.URL
                                + jsonObject2.getString("uimg"));
                        imageDetail.setPid(jsonObject2.getInt("pid"));
                        imageDetail.setPimg(ApiConstant.URL
                                + jsonObject2.getString("pimg"));
                        imageDetail.setPtitle(jsonObject2.getString("ptitle"));
                        imageDetail.setAddTime(jsonObject2.getLong("add_time"));
                        imageDetail.setWav(ApiConstant.URL
                                + jsonObject2.getString("wav"));
                        imageDetail
                                .setWavDurations((jsonObject2
                                        .getString("wavdurations") == null || "null"
                                        .equals(jsonObject2
                                                .getString("wavdurations"))) ? 0
                                        : Integer.parseInt(jsonObject2
                                        .getString("wavdurations")));
                        imageDetail.setWeiboName(jsonObject2
                                .getString("weiboname"));
                        imageDetail.setAuthStatus(jsonObject2
                                .getInt("authstatus"));
                        imageDetail.setGainCalories(jsonObject2
                                .getInt("gain_calorie"));
                        imageDetail.setLat(jsonObject2.getDouble("lat"));
                        imageDetail.setLng(jsonObject2.getDouble("lng"));
                        lists.add(imageDetail);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return lists;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    public static ImageDetail getSportsImgDetail(String sessionId, int pid)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.getSportsImgDetail(sessionId, pid);
        ImageDetail imageDetail = new ImageDetail();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                try {
                    JSONArray jsonArray = new JSONObject(content)
                            .getJSONArray("data");
                    JSONObject jsonObject2 = (JSONObject) jsonArray.opt(0);
                    Log.e("hjtest", jsonObject2.toString());
                    imageDetail.setUid(jsonObject2.getInt("uid"));
                    imageDetail.setSex(jsonObject2.getInt("sex") == 1 ? "man"
                            : "woman");
                    imageDetail.setUname(jsonObject2.getString("uname"));
                    imageDetail.setUimg(ApiConstant.URL
                            + jsonObject2.getString("uimg"));
                    imageDetail.setPid(jsonObject2.getInt("pid"));
                    imageDetail.setPimg(ApiConstant.URL
                            + jsonObject2.getString("pimg"));
                    imageDetail.setPtitle(jsonObject2.getString("ptitle"));
                    imageDetail.setAddTime(jsonObject2.getLong("add_time"));
                    imageDetail.setWav(ApiConstant.URL
                            + jsonObject2.getString("wav"));
                    imageDetail.setWavDurations((jsonObject2
                            .getString("wavdurations") == null || "null"
                            .equals(jsonObject2.getString("wavdurations"))) ? 0
                            : Integer.parseInt(jsonObject2
                            .getString("wavdurations")));
                    imageDetail
                            .setWeiboName(jsonObject2.getString("weiboname"));
                    imageDetail.setAuthStatus(jsonObject2.getInt("authstatus"));
                    imageDetail.setGainCalories(jsonObject2
                            .getInt("gain_calorie"));
                    imageDetail.setLat(jsonObject2.getDouble("lat"));
                    imageDetail.setLng(jsonObject2.getDouble("lng"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return imageDetail;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    public static long getTime() throws ApiNetException {
        ApiMessage msg = Api.getTime();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            long time = 0;
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                time = Long.parseLong(jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return time;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    public static ApiBack modifyTypeorName(String sessionId, String deviceName,
                                           int sportsType) throws ApiNetException {
        ApiMessage msg = Api
                .modifyTypeorName(sessionId, deviceName, sportsType);
        ApiBack back = new ApiBack();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            Log.v("ApiJson", "content --> " + content);
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                back.setFlag(jsonObject.getInt("flag"));
                back.setMsg(jsonObject.getString("msg"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return back;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    public static int uploadCount(String sessionId) throws ApiNetException {
        ApiMessage msg = Api.uploadCount(sessionId);
        int back = 0;
        if (msg.isFlag()) {
            String content = msg.getMsg();
            Log.v("ApiJsonUploadCount", "content --> " + content);
            try {
                JSONArray jsonArray = new JSONObject(content)
                        .getJSONArray("data");
                JSONObject jsonObject2 = (JSONObject) jsonArray.opt(0);
                back = jsonObject2.getInt("count");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return back;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    public static ApiBack runWavSports(String sessionId, int picId)
            throws ApiNetException {
        ApiMessage msg = Api.runWavSprots(sessionId, picId);
        ApiBack back = new ApiBack();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            Log.e("hjtest", content);
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                back.setFlag(jsonObject.getInt("flag"));
                back.setMsg(jsonObject.getString("msg"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return back;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    //上传配速记录
    public static ApiBack uploadPeisu(String sessionId, int sports_id, String datasource,
                                      String application_name, PeisuInfo array, int typeid) throws ApiSessionOutException, ApiNetException {
        ApiMessage msg = Api.uploadPeisu(sessionId, sports_id, datasource, application_name, array, typeid);
        ApiBack back = new ApiBack();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                if(content!=null&&!"".equals(content)){
                    JSONObject jsonObject = new JSONObject(content)
                            .getJSONObject("data");
                    if(jsonObject.has("id")){
                        int taskID = jsonObject.getInt("id");
                        if (taskID >= 0) {
                            back.setReg(taskID);
                        }
                    }

                    int flag=-1;
                    if(jsonObject.has("flag")){
                        flag = jsonObject.getInt("flag");
                        if (flag == 0) {
                            back.setFlag(1);
                        } else{
                            back.setFlag(flag);
                        }
                    }else{
                        back.setFlag(flag);
                    }
                    if(jsonObject.has("msg")){
                        back.setMsg(jsonObject.getString("msg"));
                        sessionOut = jsonObject.getString("msg");
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
                return back;
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                return back;
            }
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }

    }

    // 上传运动任务
    public static ApiBack uploadSportTask(int Limt, String sessionId,
                                          int Sports_type_task, int sports_swim_type,
                                          int monitoring_equipment, String start_time, int sprts_time,
                                          double sport_distance, double sprots_Calorie,
                                          double sprots_velocity, double Heart_rate,
                                          String longitude_latitude, int step_num, int maptype,
                                          String serial, String app_id,String velocity_list,String coordinate_list) throws ApiNetException,
            ApiSessionOutException {
        ApiMessage msg = Api.uploadSportTask(Limt, sessionId, Sports_type_task,
                sports_swim_type, monitoring_equipment, start_time, sprts_time,
                sport_distance, sprots_Calorie, sprots_velocity, Heart_rate,
                longitude_latitude, step_num, maptype, serial, app_id,velocity_list,coordinate_list);
        ApiBack back = new ApiBack();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            Log.i("content", content);
            String sessionOut = new String();
            if (content.contains(SportsApp.getInstance().getResources().getString(R.string.user_system))){
                back.setFlag(-100);
                back.setMsg("checkyour_WIFI_isconnect");
                return back;
            }else{
                try {
                    JSONObject jsonObject = new JSONObject(content)
                            .getJSONObject("data");
                    int flag=-1;//flag==0时表示成功，-1表示失败
                    if(jsonObject.has("flag")){
                        flag = jsonObject.getInt("flag");
                    }
                    if (flag == 0) {
                        back.setFlag(1);
                        if(jsonObject.has("taskid")){
                            int taskID = jsonObject.getInt("taskid");
                            if (taskID >= 0) {
                                back.setReg(taskID);
                            }
                        }
                    }else{
                        back.setFlag(flag);
                    }
                    if(jsonObject.has("msg")){
                        back.setMsg(jsonObject.getString("msg"));
                        sessionOut = jsonObject.getString("msg");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    return back;
                }
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                return back;
            }
        } else {
            if (msg != null && ("SocketTimeoutException").equals(msg.getMsg())){
                String content = msg.getMsg();
                back.setFlag(-56);
                back.setMsg(content);
                return back;
            }else{
                throw new ApiNetException(SportsApp.getInstance().getResources().getString(R.string.sports_get_list_failed2));
            }
        }
    }

    // 更新运动任务
    public static ApiBack updateSportTask(int id, int Limt, String sessionId,
                                          int Sports_type_task, int monitoring_equipment, String start_time,
                                          int sprts_time, double sport_distance, double sprots_Calorie,
                                          double sprots_velocity, double Heart_rate,
                                          String longitude_latitude, int step_num, int maptype)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.updateSportTask(id, Limt, sessionId,
                Sports_type_task, monitoring_equipment, start_time, sprts_time,
                sport_distance, sprots_Calorie, sprots_velocity, Heart_rate,
                longitude_latitude, step_num, maptype);
        ApiBack back = new ApiBack();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            Log.i("content", content);
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                int flag = jsonObject.getInt("flag");
                if (flag == 0) {
                    back.setFlag(1);
                    Log.i("flag", back.getFlag() + "---" + flag);
                }
                back.setMsg(jsonObject.getString("msg"));
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                return back;
            }
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // 上传运动任务中媒体文件
    public static ApiBack uploadSportTaskMedia(String sessionId, int uid,
                                               int task_id, int type, String durations, String path, String title,
                                               String location, int width, int hight, int maptype)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.uploadSportTaskMedia(sessionId, uid, task_id,
                type, durations, path, title, location, width, hight, maptype);
        ApiBack back = new ApiBack();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            Log.i("content", content);
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                int mediaID = jsonObject.getInt("medid");
                int flag = jsonObject.getInt("flag");
                if (flag == 0) {
                    back.setFlag(1);
                    back.setReg(mediaID);
                }
                back.setMsg(jsonObject.getString("msg"));
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                return back;
            }
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // visitor
    public static List<UserNearby> getSportsVisitor(String sessionId, int times)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.getSportsVisitor(sessionId, times);
        List<UserNearby> lists = new ArrayList<UserNearby>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                try {
                    JSONArray jsonArray = new JSONObject(content)
                            .getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                        UserNearby users = new UserNearby();
                        users.setId(jsonObject.getInt("id"));
                        users.setImg(ApiConstant.URL
                                + jsonObject.getString("img"));
                        users.setName(jsonObject.getString("name"));
                        users.setSex(jsonObject.getInt("sex") == 1 ? "man"
                                : "woman");
                        users.setTime(jsonObject.getLong("add_time"));
                        // Log.e(TAG,
                        // "时间是---------------"+jsonObject.getLong("add_time"));
                        users.setBirthday(jsonObject.getString("brithday"));
                        lists.add(users);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return lists;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    public static ApiBack visitorSports(String sessionId, int uid)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.visitorSports(sessionId, uid);
        ApiBack back = new ApiBack();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                back.setFlag(jsonObject.getInt("flag"));
                back.setMsg(jsonObject.getString("msg"));
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                return back;
            }
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    public static List<SportContionTaskDetail> getSportsTaskByDate(String uid,
                                                                   String date, int userid) throws ApiNetException,
            ApiSessionOutException {
        ApiMessage msg = Api.getSportsTaskByDate(uid, date, userid);
        List<SportContionTaskDetail> taskDetails = null;
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                try {
                    JSONObject jsonObject = new JSONObject(content)
                            .getJSONObject("data");
                    sessionOut = jsonObject.getString("msg");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                    throw new ApiSessionOutException(sessionOut);
                } else {
                    if (taskDetails == null) {
                        taskDetails = new ArrayList<SportContionTaskDetail>();
                    }
                    JSONArray array = new JSONObject(content)
                            .getJSONArray("data");
                    Log.e("llp", "array.length()=" + array.length());
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        SportContionTaskDetail taskDetail = new SportContionTaskDetail();
                        taskDetail.setSprots_Calorie(obj
                                .getInt("sprots_Calorie"));
                        taskDetail.setTaskid(obj.getInt("id"));
                        taskDetail.setTimes(obj.getString("start_time"));
                        taskDetail.setSportDate(obj.getString("current_times"));
                        taskDetail.setSports_type(obj
                                .getInt("Sports_type_task"));
                        taskDetail.setStartTime(obj.getString("current_times")
                                + " " + obj.getString("start_time"));
                        taskDetail.setHeartRate(obj.getDouble("Heart_rate"));
                        taskDetail.setSportDistance(obj
                                .getDouble("sport_distance"));
                        taskDetail.setLatlng(obj
                                .getString("longitude_latitude"));
                        taskDetail.setMonitoringEquipment(obj
                                .getInt("monitoring_equipment"));
                        taskDetail.setSportVelocity(obj
                                .getDouble("sprots_velocity"));
                        taskDetail.setStepNum(obj.getInt("step_num"));
                        taskDetail.setSportTime(obj.getString("sprts_time"));
                        taskDetail.setUserId(obj.getInt("uid"));
                        taskDetail.setSwimType(obj.getInt("sports_swim_type"));
                        taskDetails.add(taskDetail);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return taskDetails;
    }

    public static SportConditionDetail getSportsInfo(String uid, int limit,
                                                     int userid) throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.getSportsInfo(uid, limit, userid);
        SportConditionDetail detail = null;
        int calorieCount = 0;
        if (msg.isFlag()) {
            String content = msg.getMsg();
            Log.e("llp", "content:" + content.toString());
            JSONObject jsonObject = null;
            try {
                JSONArray jsonArray = new JSONObject(content)
                        .getJSONArray("data");
                if (jsonArray.length() > 0) {
                    detail = new SportConditionDetail();
                    jsonObject = jsonArray.getJSONObject(0);
                    detail.setCurrent_times(jsonObject
                            .getString("current_times"));
                    detail.setGain_calorie(jsonObject.getInt("gain_calorie"));
                    detail.setGain_step(jsonObject.getInt("gain_step"));
                    detail.setScore_calorie(jsonObject.getInt("score_calorie"));
                    detail.setScore_step(jsonObject.getInt("score_step"));
                    detail.setSports_type(jsonObject.getInt("sports_type"));
                    detail.setUid(jsonObject.getInt("uid"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                if (jsonObject != null) {
                    JSONArray array = jsonObject.getJSONArray("data");
                    List<SportContionTaskDetail> taskDetailsPhone = new ArrayList<SportContionTaskDetail>();
                    List<SportContionTaskDetail> taskDetailsWatch = new ArrayList<SportContionTaskDetail>();
                    SportContionTaskDetail taskDetail = null;
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        taskDetail = new SportContionTaskDetail();
                        taskDetail.setSprots_Calorie(obj
                                .getInt("sprots_Calorie"));
                        taskDetail.setTaskid(obj.getInt("id"));
                        taskDetail.setTimes(obj.getString("start_time"));
                        taskDetail.setSportDate(obj.getString("current_times"));
                        taskDetail.setSports_type(obj
                                .getInt("Sports_type_task"));
                        taskDetail.setStartTime(obj.getString("current_times")
                                + " " + obj.getString("start_time"));
                        taskDetail.setHeartRate(obj.getDouble("Heart_rate"));
                        taskDetail.setSportDistance(obj
                                .getDouble("sport_distance"));
                        taskDetail.setLatlng(obj
                                .getString("longitude_latitude"));
                        taskDetail.setMonitoringEquipment(obj
                                .getInt("monitoring_equipment"));
                        taskDetail.setSportVelocity(obj
                                .getDouble("sprots_velocity"));
                        taskDetail.setStepNum(obj.getInt("step_num"));
                        taskDetail.setSportTime(obj.getString("sprts_time"));
                        taskDetail.setUserId(obj.getInt("uid"));
                        taskDetail.setSwimType(obj.getInt("sports_swim_type"));
                        if (taskDetail.getMonitoringEquipment() == 1) {
                            taskDetailsWatch.add(taskDetail);
                        } else {
                            taskDetailsPhone.add(taskDetail);
                        }
                        calorieCount += obj.getInt("sprots_Calorie");
                    }
                    detail.setSportTasks(taskDetailsPhone);
                    detail.setSportTasksWatch(taskDetailsWatch);
                    detail.setCalorieCount(calorieCount);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return detail;
    }

    public static List<SportContionTaskDetail> getSportsTaskAll(String uid,
                                                                int times, int userid) throws ApiNetException,
            ApiSessionOutException {
        ApiMessage msg = Api.getSportsTaskAll(uid, times, userid);
        List<SportContionTaskDetail> taskDetails = new ArrayList<SportContionTaskDetail>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                try {
                    JSONObject jsonObject = new JSONObject(content)
                            .getJSONObject("data");
                    sessionOut = jsonObject.getString("msg");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                    throw new ApiSessionOutException(sessionOut);
                } else {
                    if(content!=null&&!"".equals(content)){
                        JSONArray array = new JSONObject(content)
                                .getJSONArray("data");
                        SportContionTaskDetail taskDetail = null;
                        int nums=1;
                        if(array!=null){
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = array.getJSONObject(i);
                                taskDetail = new SportContionTaskDetail();
                                if(obj.has("sprots_Calorie")){
                                    taskDetail.setSprots_Calorie(obj
                                            .getInt("sprots_Calorie"));
                                }
                                if(obj.has("id")){
                                    taskDetail.setTaskid(obj.getInt("id"));
                                }
                                if(obj.has("start_time")){
                                    taskDetail.setTimes(obj.getString("start_time"));
                                }
                                if(obj.has("current_times")){
                                    taskDetail.setSportDate(obj.getString("current_times"));
                                }
                                if(obj.has("Sports_type_task")){
                                    taskDetail.setSports_type(obj
                                            .getInt("Sports_type_task"));
                                }

                                if(obj.has("current_times")&&obj.has("start_time")){
                                    taskDetail.setStartTime(obj.getString("current_times")
                                            + " " + obj.getString("start_time"));
                                }
                                if(obj.has("Heart_rate")){
                                    taskDetail.setHeartRate(obj.getDouble("Heart_rate"));
                                }
                                if(obj.has("sport_distance")){
                                    taskDetail.setSportDistance(obj
                                            .getDouble("sport_distance"));
                                }
                                if(obj.has("longitude_latitude")){
                                    taskDetail.setLatlng(obj
                                            .getString("longitude_latitude"));
                                }
                                if(obj.has("monitoring_equipment")){
                                    taskDetail.setMonitoringEquipment(obj
                                            .getInt("monitoring_equipment"));
                                }
                                if(obj.has("sprots_velocity")){
                                    taskDetail.setSportVelocity(obj
                                            .getDouble("sprots_velocity"));
                                }
                                if(obj.has("step_num")){
                                    taskDetail.setStepNum(obj.getInt("step_num"));
                                }
                                if(obj.has("sprts_time")){
                                    taskDetail.setSportTime(obj.getString("sprts_time"));
                                }
                                if(obj.has("uid")){
                                    taskDetail.setUserId(obj.getInt("uid"));
                                }
                                if(obj.has("sports_swim_type")){
                                    taskDetail.setSwimType(obj.getInt("sports_swim_type"));
                                }
                                if(obj.has("maptype")){
                                    taskDetail.setMapType(obj.getInt("maptype"));
                                }

                                if(obj.has("app_id")){
                                    if(obj.getString("app_id")!=null&&!"".equals(obj.getString("app_id"))){
                                        taskDetail.setSport_markcode(obj.getString("app_id"));
                                    }else{
                                        taskDetail.setSport_markcode(UUIDGenerator.getUUID()+nums);
                                        nums++;
                                    }
                                }else{
                                    taskDetail.setSport_markcode(UUIDGenerator.getUUID()+nums);
                                    nums++;
                                }
                                if(obj.has("velocity_list")){
                                    taskDetail.setSport_speedList(obj.getString("velocity_list"));
                                }else{
                                    taskDetail.setSport_speedList("");
                                }
                                if(obj.has("coordinate_list")){
                                    taskDetail.setCoordinate_list(obj.getString("coordinate_list"));
                                }else{
                                    taskDetail.setCoordinate_list("");
                                }
                                if (obj.has("sportsdata")) {
                                    if (!"".equals(obj.getString("sportsdata"))) {
                                        JSONObject jsonObject = obj
                                                .getJSONObject("sportsdata");
                                        SportRecord sportRecord = new SportRecord();
                                        sportRecord.setTime(jsonObject
                                                .getString("time"));
                                        sportRecord.setSport_distance(jsonObject
                                                .getString("lasttime"));
                                        taskDetail.setSportRecord(sportRecord);
                                    }

                                }

                                taskDetails.add(taskDetail);
                            }
                        }

                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return taskDetails;
    }

    public static List<SportContionTaskDetail> getSportsTaskPhone(String uid,
                                                                  int times, int userid) throws ApiNetException,
            ApiSessionOutException {
        ApiMessage msg = Api.getSportsTaskPhone(uid, times, userid);
        List<SportContionTaskDetail> taskDetailsPhone = new ArrayList<SportContionTaskDetail>();
        SportConditionDetail detail = null;
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                try {
                    JSONObject jsonObject = new JSONObject(content)
                            .getJSONObject("data");
                    sessionOut = jsonObject.getString("msg");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                    throw new ApiSessionOutException(sessionOut);
                } else {
                    detail = new SportConditionDetail();
                    JSONArray array = new JSONObject(content)
                            .getJSONArray("data");
                    SportContionTaskDetail taskDetail = null;
                    Log.e("llp", "array.length()=" + array.length());
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        taskDetail = new SportContionTaskDetail();
                        taskDetail.setSprots_Calorie(obj
                                .getInt("sprots_Calorie"));
                        taskDetail.setTaskid(obj.getInt("id"));
                        taskDetail.setTimes(obj.getString("start_time"));
                        taskDetail.setSportDate(obj.getString("current_times"));
                        taskDetail.setSports_type(obj
                                .getInt("Sports_type_task"));
                        taskDetail.setStartTime(obj.getString("current_times")
                                + " " + obj.getString("start_time"));
                        taskDetail.setHeartRate(obj.getDouble("Heart_rate"));
                        taskDetail.setSportDistance(obj
                                .getDouble("sport_distance"));
                        taskDetail.setLatlng(obj
                                .getString("longitude_latitude"));
                        taskDetail.setMonitoringEquipment(obj
                                .getInt("monitoring_equipment"));
                        taskDetail.setSportVelocity(obj
                                .getDouble("sprots_velocity"));
                        taskDetail.setStepNum(obj.getInt("step_num"));
                        taskDetail.setSportTime(obj.getString("sprts_time"));
                        taskDetail.setUserId(obj.getInt("uid"));
                        taskDetail.setSwimType(obj.getInt("sports_swim_type"));
                        taskDetailsPhone.add(taskDetail);
                    }
                    detail.setSportTasks(taskDetailsPhone);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return taskDetailsPhone;
    }

    public static List<SportContionTaskDetail> getSportsTaskWatch(String uid,
                                                                  int times, int userid) throws ApiNetException,
            ApiSessionOutException {
        ApiMessage msg = Api.getSportsTaskWatch(uid, times, userid);
        List<SportContionTaskDetail> taskDetails = new ArrayList<SportContionTaskDetail>();
        SportConditionDetail detail = null;
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            try {
                try {
                    JSONObject jsonObject = new JSONObject(content)
                            .getJSONObject("data");
                    sessionOut = jsonObject.getString("msg");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                    throw new ApiSessionOutException(sessionOut);
                } else {
                    detail = new SportConditionDetail();
                    JSONArray array = new JSONObject(content)
                            .getJSONArray("data");
                    SportContionTaskDetail taskDetail = null;
                    Log.e("llp", "array.length()=" + array.length());
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        taskDetail = new SportContionTaskDetail();
                        taskDetail.setSprots_Calorie(obj
                                .getInt("sprots_Calorie"));
                        taskDetail.setTaskid(obj.getInt("id"));
                        taskDetail.setTimes(obj.getString("start_time"));
                        taskDetail.setSportDate(obj.getString("current_times"));
                        taskDetail.setSports_type(obj
                                .getInt("Sports_type_task"));
                        taskDetail.setStartTime(obj.getString("current_times")
                                + " " + obj.getString("start_time"));
                        taskDetail.setHeartRate(obj.getDouble("Heart_rate"));
                        taskDetail.setSportDistance(obj
                                .getDouble("sport_distance"));
                        taskDetail.setLatlng(obj
                                .getString("longitude_latitude"));
                        taskDetail.setMonitoringEquipment(obj
                                .getInt("monitoring_equipment"));
                        taskDetail.setSportVelocity(obj
                                .getDouble("sprots_velocity"));
                        taskDetail.setStepNum(obj.getInt("step_num"));
                        taskDetail.setSportTime(obj.getString("sprts_time"));
                        taskDetail.setUserId(obj.getInt("uid"));
                        taskDetail.setSwimType(obj.getInt("sports_swim_type"));
                        taskDetails.add(taskDetail);
                    }
                    detail.setSportTasksWatch(taskDetails);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return taskDetails;
    }

    public static SportTask getSportsTaskById(String sessionId, int uid,
                                              int taskid) throws ApiNetException, ApiSessionOutException {
        SportTask detail = null;
        ApiMessage msg = Api.getSportsTaskById(sessionId, uid, taskid);
        if (msg.isFlag()) {
            String content = msg.getMsg();
            JSONObject jsonObject = null;
            try {
                JSONArray jsonArray = new JSONObject(content)
                        .getJSONArray("data");
                detail = new SportTask();
                if (jsonArray.length() > 0) {
                    jsonObject = jsonArray.getJSONObject(0);
                    detail.setStart_time(jsonObject.getString("current_times")
                            + " " + jsonObject.getString("start_time"));
                    detail.setSport_type_task(jsonObject
                            .getInt("Sports_type_task"));
                    detail.setSport_device(jsonObject
                            .getInt("monitoring_equipment"));
                    detail.setSport_time(jsonObject.getInt("sprts_time"));
                    detail.setSport_distance(jsonObject
                            .getDouble("sport_distance"));
                    detail.setSport_calories(jsonObject
                            .getDouble("sprots_Calorie"));
                    detail.setSport_speed(jsonObject
                            .getDouble("sprots_velocity"));
                    detail.setSport_heart_rate(jsonObject
                            .getDouble("Heart_rate"));
                    detail.setSport_lat_lng(jsonObject
                            .getString("longitude_latitude"));
                    detail.setSport_step(jsonObject.getInt("step_num"));
                    detail.setSport_map_type(jsonObject.getInt("maptype"));
                    detail.setSport_isupload(1);
                    if(jsonObject.has("app_id")){
                        if(jsonObject.getString("app_id")!=null&&!"".equals(jsonObject.getString("app_id"))){
                            detail.setSport_mark_code(jsonObject.getString("app_id"));
                        }else{
                            detail.setSport_mark_code(UUIDGenerator.getUUID());
                        }
                    }else{
                        detail.setSport_mark_code(UUIDGenerator.getUUID());
                    }
                    if(jsonObject.has("velocity_list")){
                        detail.setSport_speedlsit(jsonObject.getString("velocity_list"));
                    }else{
                        detail.setSport_speedlsit("");
                    }
                    if(jsonObject.has("coordinate_list")){
                        detail.setCoordinate_list(jsonObject.getString("coordinate_list"));
                    }else{
                        detail.setCoordinate_list("");
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return detail;
    }

    public static List<UserFollowMsg> sportsUploadMsg(String sessionId,
                                                      int times) throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.sportsUploadMsg(sessionId, times);
        List<UserFollowMsg> lists = new ArrayList<UserFollowMsg>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            Log.e("sportsUploadMsg<<>>", content);
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                try {
                    JSONArray jsonArray = new JSONObject(content)
                            .getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                        UserFollowMsg users = new UserFollowMsg();
                        users.setId(jsonObject.getInt("id"));
                        users.setImg(ApiConstant.URL
                                + jsonObject.getString("img"));
                        users.setName(jsonObject.getString("name"));
                        users.setSex(jsonObject.getInt("sex") == 1 ? "man"
                                : "woman");
                        users.setAddTime(jsonObject.getLong("add_time"));
                        users.setType(jsonObject.getInt("type"));
                        users.setDetailType(jsonObject.getInt("detailtype"));
                        users.setTaskid(jsonObject.getInt("taskid"));
                        lists.add(users);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return lists;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    public static List<SportMediaFile> getMediaListByTaskid(String sessionId,
                                                            int taskid) throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.getMediaListByTaskid(sessionId, taskid);
        List<SportMediaFile> mediaFiles = null;
        if (msg.isFlag()) {
            String content = msg.getMsg();
            Log.i("content", content);
            mediaFiles = new ArrayList<SportMediaFile>();
            try {
                JSONArray jsonArray = new JSONObject(content)
                        .getJSONArray("data");
                SportMediaFile detail = null;
                if (jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        detail = new SportMediaFile();
                        JSONObject obj = jsonArray.getJSONObject(i);
                        detail.setMediaId(obj.getInt("id"));
                        detail.setUid(obj.getInt("uid"));
                        detail.setMediaTypeID(obj.getInt("type"));
                        detail.setDurations(obj.getInt("durations"));
                        detail.setPointStr(obj.getString("location"));
                        detail.setMediaFilePath(obj.getString("path"));
                        detail.setMediaFileName(obj.getString("title"));
                        detail.setWidth(obj.getInt("width"));
                        detail.setHeight(obj.getInt("height"));
                        detail.setMapType(obj.getInt("maptype"));
                        mediaFiles.add(detail);
                        Log.v("getMediaListByTaskid", "getMediaListByTaskid "
                                + detail.toString());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return mediaFiles;
    }

    public static SportMediaFile getSportsMediaById(String sessionId,
                                                    int mediaId) throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.getSportsMediaById(sessionId, mediaId);
        SportMediaFile detail = null;
        if (msg.isFlag()) {
            String content = msg.getMsg();
            try {
                detail = new SportMediaFile();
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                detail.setMediaFilePath(jsonObject.getString("path"));
                detail.setMediaTypeID(jsonObject.getInt("type"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("getSportsMediaById", content);
        }
        return detail;
    }

    public static ApiBack deleteSportsTaskById(String sessionid, int uid,
                                               int taskid) throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.deleteSportsTaskById(sessionid, uid, taskid);
        ApiBack back = null;
        if (msg.isFlag()) {
            String content = msg.getMsg();
            try {
                back = new ApiBack();
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                int flag = jsonObject.getInt("flag");
                if (flag == 0) {
                    back.setFlag(1);
                }
                back.setMsg(jsonObject.getString("msg"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("deleteSportsTaskById", content);
        }
        return back;
    }

    public static ApiBack deleteSportMediaById(String sessionid, int uid,
                                               int mediaID) throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.deleteSportMediaById(sessionid, uid, mediaID);
        ApiBack back = null;
        if (msg.isFlag()) {
            String content = msg.getMsg();
            try {
                back = new ApiBack();
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                int flag = jsonObject.getInt("flag");
                if (flag == 0) {
                    back.setFlag(1);
                }
                back.setMsg(jsonObject.getString("msg"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("deleteSportMediaById", content);
        }
        return back;
    }

    /**
     * @param
     * @return
     * @throws ApiNetException
     * @throws ApiSessionOutException
     */
//    public static SportWeatherInfo getSportsWeather(String url)
//            throws ApiNetException, ApiSessionOutException {
//        ApiMessage msg = Api.getSportsWeatherInfo(url);
//        SportWeatherInfo detail = null;
//        if (msg.isFlag()) {
//            String content = msg.getMsg();
//            try {
//                detail = new SportWeatherInfo();
//                JSONObject jsonObject = new JSONObject(content)
//                        .getJSONObject("weatherinfo");
//                detail.setDegree(jsonObject.getString("temp") + "℃");
//                // detail.setImgurl(imgurl)(jsonObject.getInt("type"));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            Log.i("getSportsMediaById", "sportsweather" + content);
//        }
//        return detail;
//    }
//
//    public static WeatherInfo getWeatherIconUrl(String url)
//            throws ApiNetException, ApiSessionOutException {
//        ApiMessage msg = Api.getSportsWeatherInfo(url);
//        WeatherInfo detail = null;
//        if (msg.isFlag()) {
//            detail = new WeatherInfo();
//            String content = msg.getMsg();
//            try {
//                JSONObject jsonObject = new JSONObject(content)
//                        .getJSONObject("weatherinfo");
//                String weather = jsonObject.getString("weather");
//                String temp_high = jsonObject.getString("temp1");
//                String temp_low = jsonObject.getString("temp2");
//                detail.weather = weather;
//                detail.temp_high = temp_high;
//                detail.temp_low = temp_low;
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            Log.i("getWeatherIconUrl", "weathericon" + content);
//        }
//        return detail;
//    }

    // 获取约跑你的人
    public static List<InviteUser> getInviteSports(String sessionId, int times)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.getInviteSports(sessionId, times);
        List<InviteUser> lists = new ArrayList<InviteUser>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            Log.i("getInviteSports", "get InviteUser:" + content);
            try {
                JSONArray jsonArray = new JSONObject(content)
                        .getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                    InviteUser user = new InviteUser();
                    user.setUid(jsonObject.getInt("id"));
                    user.setName(jsonObject.getString("name"));
                    user.setAddTime(jsonObject.getLong("add_time"));
                    user.setImg(ApiConstant.URL + jsonObject.getString("img"));
                    user.setSex(jsonObject.getInt("sex"));
                    lists.add(user);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return lists;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

//    public static WeatherForecast getWeatherForecast(String url)
//            throws ApiNetException, ApiSessionOutException {
//        ApiMessage msg = Api.getSportsWeatherInfo(url);
//        WeatherForecast detail = null;
//        if (msg.isFlag()) {
//            detail = new WeatherForecast();
//            String content = msg.getMsg();
//            try {
//                JSONObject jsonObject = new JSONObject(content)
//                        .getJSONObject("weatherinfo");
//                String weather_tomorrow = jsonObject.getString("weather2");
//                String weather_tomAftertom = jsonObject.getString("weather3");
//                String temp_tomorrow = jsonObject.getString("temp2");
//                String temp_tomAftertom = jsonObject.getString("temp3");
//                detail.weather_tomorrow = weather_tomorrow;
//                detail.weather_tomAftertom = weather_tomAftertom;
//                detail.temp_tomorrow = temp_tomorrow;
//                detail.temp_tomAftertom = temp_tomAftertom;
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            Log.i("getWeatherForecast", "weatherforecast" + content);
//        }
//        return detail;
//    }

//    public static SportPM getSportsPM(String url) throws ApiNetException,
//            ApiSessionOutException {
//        ApiMessage msg = Api.getSportsWeatherInfo(url);
//        SportPM detail = null;
//        if (msg.isFlag()) {
//            String content = msg.getMsg();
//            try {
//                detail = new SportPM();
//                JSONArray jsonArray = new JSONArray(content);
//                if (jsonArray != null && jsonArray.length() > 0) {
//                    JSONObject object = jsonArray.optJSONObject(0);
//                    detail.setPm("空气" + object.getString("quality"));
//                    String pm2d5 = object.getString("aqi");
//                    detail.setPm2d5(pm2d5);
//                }
//                // detail.setImgurl(imgurl)(jsonObject.getInt("type"));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            Log.i("getSportsMediaById", "sportspm" + content);
//        }
//        return detail;
//    }

    public static List<CurrentTimeList> getCurrentTimeById(String sessionId,
                                                           int uid) throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.getCurrentTimeById(sessionId, uid);
        List<CurrentTimeList> lst = null;
        if (msg.isFlag()) {
            lst = new ArrayList<CurrentTimeList>();
            String content = msg.getMsg();
            Log.e("llptest", "currenttime" + content);
            try {
                JSONArray jsonArray = new JSONObject(content)
                        .getJSONArray("data");
                CurrentTimeList lists = null;
                if (jsonArray != null && jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        lists = new CurrentTimeList();
                        JSONObject obj = jsonArray.getJSONObject(i);
                        lists.setCurrentTime(obj.getString("current_times"));
                        lst.add(lists);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("getSportsMediaById", content);
        }
        return lst;
    }

    public static List<CurrentTimeList> getTaskTimeById(String sessionId,
                                                        int uid) throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.getTaskTimeById(sessionId, uid);
        List<CurrentTimeList> lst = null;
        if (msg.isFlag()) {
            lst = new ArrayList<CurrentTimeList>();
            String content = msg.getMsg();
            Log.e("llptest", "currenttime" + content);
            try {
                JSONArray jsonArray = new JSONObject(content)
                        .getJSONArray("data");
                CurrentTimeList lists = null;
                if (jsonArray != null && jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        lists = new CurrentTimeList();
                        JSONObject obj = jsonArray.getJSONObject(i);
                        lists.setCurrentTime(obj.getString("current_times"));
                        lst.add(lists);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("getTaskTimeById", content);
        }
        return lst;
    }

    public static SportContionTaskDetail getLastTaskInfo(String sessionid,
                                                         int userid) throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.getLastTaskInfo(sessionid, userid);
        SportContionTaskDetail taskDetail = null;
        if (msg.isFlag()) {
            String content = msg.getMsg();
            JSONObject obj = null;
            try {
                JSONArray jsonArray = new JSONObject(content)
                        .getJSONArray("data");
                if (jsonArray.length() > 0) {
                    obj = jsonArray.getJSONObject(0);
                    taskDetail = new SportContionTaskDetail();
                    taskDetail.setSprots_Calorie(obj.getInt("sprots_Calorie"));
                    taskDetail.setTaskid(obj.getInt("id"));
                    taskDetail.setTimes(obj.getString("start_time"));
                    taskDetail.setSportDate(obj.getString("current_times"));
                    taskDetail.setSports_type(obj.getInt("Sports_type_task"));
                    taskDetail.setStartTime(obj.getString("current_times")
                            + " " + obj.getString("start_time"));
                    taskDetail.setHeartRate(obj.getDouble("Heart_rate"));
                    taskDetail
                            .setSportDistance(obj.getDouble("sport_distance"));
                    taskDetail.setLatlng(obj.getString("longitude_latitude"));
                    taskDetail.setMonitoringEquipment(obj
                            .getInt("monitoring_equipment"));
                    taskDetail.setSportVelocity(obj
                            .getDouble("sprots_velocity"));
                    taskDetail.setStepNum(obj.getInt("step_num"));
                    taskDetail.setSportTime(obj.getString("sprts_time"));
                    taskDetail.setUserId(obj.getInt("uid"));
                    taskDetail.setSwimType(obj.getInt("sports_swim_type"));
                }
                Log.v("getLastTaskInfo",
                        "getLastTaskInfo " + taskDetail.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return taskDetail;
    }

    // 获取活动列表接口
    public static List<ActionList> getActionList(String sessionId, int times)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.getActionList(sessionId, times);
        List<ActionList> lst = null;
        if (msg.isFlag()) {
            lst = new ArrayList<ActionList>();
            String content = msg.getMsg();
            try {
                JSONArray jsonArray = new JSONObject(content)
                        .getJSONArray("data");
                ActionList lists = null;
                if (jsonArray != null && jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        lists = new ActionList();
                        JSONObject obj = jsonArray.getJSONObject(i);
                        lists.actionId = obj.getInt("id");
                        lists.title = obj.getString("title");
                        lists.url = ApiConstant.URL + obj.getString("thumb");
                        lists.actionTime = obj.getString("activity_time");
                        lst.add(lists);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i(TAG, "action list=" + content);
        }
        return lst;
    }

    // 获取活动详情接口
    public static ActionInfo getActionInfo(String sessionId, int actionId)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.actionPost(sessionId, actionId,
                ApiConstant.getActionInfo);
        ActionInfo lists = null;
        if (msg.isFlag()) {
            String content = msg.getMsg();
            Log.i(TAG, "action info=" + content);
            try {
                JSONObject obj = new JSONObject(content).getJSONObject("data");
                lists = new ActionInfo();
                lists.actionId = Integer.parseInt(obj.getString("id"));
                lists.title = obj.getString("title");
                lists.url = ApiConstant.URL + obj.getString("thumb");
                lists.start_time = obj.getString("start_time");
                lists.end_time = obj.getString("end_time");
                lists.activity_time = obj.getString("activity_time");
                lists.content = ApiConstant.URL + obj.getString("content");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return lists;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // 获取每个活动详情的具体内容
    public static ActivityInfo getActivityDetailInfo(String sessionId,
                                                     int actionId, int times) throws ApiNetException,
            ApiSessionOutException {
        Log.i("当前的times", "-----" + times);
        ApiMessage msg = Api.activityPost(sessionId, actionId, times,
                ApiConstant.getActivityDetailInfo);
        ActivityInfo activityDetaiInfo = null;
        if (msg.isFlag()) {
            String content = msg.getMsg();
            Log.i("ActivityInfo", "action info=" + content + "---actionId:"
                    + actionId + "---sessionId:" + sessionId);
            try {
                JSONObject obj = new JSONObject(content).getJSONObject("data");
                activityDetaiInfo = new ActivityInfo();
                int actionID = Integer.parseInt(obj.getString("id"));
                activityDetaiInfo.title = obj.getString("title");
                if (obj.has("biaoti_img") && obj.getString("biaoti_img") != null) {
                    activityDetaiInfo.biaoti_img = obj.getString("biaoti_img");
                }
                if (obj.has("jiangli_img") && obj.getString("jiangli_img") != null) {
                    activityDetaiInfo.jiangli_img = obj.getString("jiangli_img");
                }
                activityDetaiInfo.activity_time = obj
                        .getString("activity_time");
                activityDetaiInfo.content = obj.getString("content");
                activityDetaiInfo.canjia_type = obj.getString("canjia_type");
                activityDetaiInfo.act_explain = obj.getString("act_explain");
                activityDetaiInfo.isshow = obj.getInt("events_show");
                activityDetaiInfo.activity_URl = obj.getString("thrurl");
                activityDetaiInfo.actionId = Integer.parseInt(obj
                        .getString("id"));
                if (obj.has("start_time")) {
                    activityDetaiInfo.setStart_time(obj.getLong("start_time"));
                }
                if (obj.has("end_time")) {
                    activityDetaiInfo.setEnd_time(obj.getLong("end_time"));
                }

                List<activityinfo2> actList = new ArrayList<activityinfo2>();
                JSONArray ary = obj.getJSONArray("findslist");
                for (int i = 0, j = ary.length(); i < j; i++) {
                    JSONObject aa = ary.getJSONObject(i);
                    activityinfo2 act = new activityinfo2();
                    act.setComefrom(aa.getString("comefrom"));
                    act.setContent(aa.getString("content"));
                    act.setUid(aa.getInt("uid"));
                    act.setImg_head(aa.getString("img"));
                    act.setInputtime(aa.getInt("inputtime"));
                    act.setSex(aa.getInt("sex"));
                    act.setName(aa.getString("name"));
                    if (aa.has("pic")) {
                        JSONArray bb = aa.getJSONArray("pic");
                        int m = bb.length();
                        List<String> str = new ArrayList<String>();
                        for (int k = 0, l = bb.length(); k < l; k++) {
                            str.add(bb.get(k).toString());
                        }
                        if (str != null) {
                            act.setList(str);
                        }
                        if (m == 1) {
                            if (aa.has("pic_width") && aa.has("pic_height")) {
                                act.setWidth(aa.getInt("pic_width"));
                                act.setHeight(aa.getInt("pic_height"));
                            }
                        }
                    }
                    actList.add(act);
                }
                activityDetaiInfo.setList_catInfo(actList);
                Log.i("ababab", "刚刚下载下来的集合长度：" + actList.size());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return activityDetaiInfo;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // 报名参加活动
    public static ApiBack signupAction(String sessionId, int uid, int actionId)
            throws ApiNetException {
        ApiMessage msg = Api.signupAction(sessionId, uid, actionId);
        ApiBack back = new ApiBack();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                back.setFlag(jsonObject.getInt("flag"));
                back.setMsg(jsonObject.getString("msg"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return back;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // 获取支付详情接口
    public static String getPayInfo(String sessionId, int actionId)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.actionPost(sessionId, actionId,
                ApiConstant.getPayInfo);
        String lists = null;
        if (msg.isFlag()) {
            String content = msg.getMsg();
            try {
                JSONObject obj = new JSONObject(content).getJSONObject("data");
                lists = obj.getString("pay");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i(TAG, "pay info=" + content);
        }
        return lists;
    }

    // 获取报名列表接口
    public static List<ApplyList> getApplyList(String sessionId, int actionId,
                                               int times) throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.getApplyList(sessionId, actionId, times);
        List<ApplyList> lst = null;
        if (msg.isFlag()) {
            lst = new ArrayList<ApplyList>();
            String content = msg.getMsg();
            try {
                JSONArray jsonArray = new JSONObject(content)
                        .getJSONArray("data");
                ApplyList lists = null;
                if (jsonArray != null && jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        lists = new ApplyList();
                        JSONObject obj = jsonArray.getJSONObject(i);
                        lists.userId = obj.getInt("id");
                        lists.userName = obj.getString("name");
                        lists.userIcon = ApiConstant.URL + obj.getString("img");
                        lst.add(lists);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i(TAG, "action list=" + content);
        }
        return lst;
    }

    // 获取活动排行接口
    public static List<ActionRankList> getActionRankList(String sessionId,
                                                         int actionId, int index) throws ApiNetException,
            ApiSessionOutException {
        ApiMessage msg = Api.getActionRankList(sessionId, actionId, index);

        List<ActionRankList> lst = null;
        if (msg.isFlag()) {
            lst = new ArrayList<ActionRankList>();
            String content = msg.getMsg();
            try {
                JSONArray jsonArray = new JSONObject(content)
                        .getJSONArray("data");
                ActionRankList lists = null;
                if (jsonArray != null && jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        lists = new ActionRankList();
                        JSONObject obj = jsonArray.getJSONObject(i);
                        lists.userId = obj.getInt("id");
                        lists.userName = obj.getString("name");
                        lists.userIcon = ApiConstant.URL + obj.getString("img");
                        lists.calories = obj.getString("calories");
                        lists.sports_time = obj.getString("times");
                        lst.add(lists);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i(TAG, "rank list=" + content);
        }
        return lst;
    }

    // 获取报名接口
    public static ApiBack goApply(String sessionId, int actionId)
            throws ApiNetException {
        ApiMessage msg = Api.actionPost(sessionId, actionId,
                ApiConstant.goApply);
        ApiBack back = null;
        if (msg.isFlag()) {
            back = new ApiBack();
            String content = msg.getMsg();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                back.setFlag(jsonObject.getInt("flag"));
                back.setMsg(jsonObject.getString("msg"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return back;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // 获取新活动列表接口
    public static List<ActionList> getNewActionList(String sessionId,
                                                    String channelnum, int times) throws ApiNetException,
            ApiSessionOutException {
        ApiMessage msg = Api.getNewActionList(sessionId, channelnum, times);
        List<ActionList> lst = null;
        if (msg.isFlag()) {
            lst = new ArrayList<ActionList>();
            String content = msg.getMsg();
            Log.e(TAG, "活动----" + content);
            try {
                JSONArray jsonArray = new JSONObject(content)
                        .getJSONArray("data");
                ActionList lists = null;
                if (jsonArray != null && jsonArray.length() > 0) {

                    // 缓存运动秀的数据
                    if (times == 0) {
                        SharedPreferences preferences = SportsApp.getInstance()
                                .getSharedPreferences("ActionList", 0);
                        Editor edit = preferences.edit();
                        edit.putString("ActionList_info", content);
                        edit.commit();
                    }

                    for (int i = 0; i < jsonArray.length(); i++) {
                        lists = new ActionList();
                        JSONObject obj = jsonArray.getJSONObject(i);
                        lists.actionId = obj.getInt("id");
                        lists.title = obj.getString("title");
                        lists.url = obj.getString("thumb");
                        lists.actionTime = obj.getString("activity_time");
                        lists.actionUrl = obj.getString("url");
                        lists.thuurl = obj.getString("thrurl");
                        if(obj.has("match_act")){
                            lists.match_act = obj.getInt("match_act");
                        }
                        if(obj.has("match_url")){
                            lists.match_url = obj.getString("match_url");
                        }

                        if (obj.has("match_info")) {
                            if (!"".equals(obj.getString("match_info"))) {
                                JSONObject jsonObject = obj.getJSONObject("match_info");
                                lists.setMatchId(jsonObject.getInt("id"));
                                lists.setMatchprice(jsonObject.getInt("price"));
                                lists.setMatchTitle(jsonObject.getString("title"));
                            }

                        }
                        lst.add(lists);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i(TAG, "action list=" + content);
        }
        return lst;
    }

    // 获取可供用户参加的活动列表
    public static List<OnlineAction> getOnlineActionList(String sessionId)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.getOnlineActionList(sessionId);
        List<OnlineAction> lst = null;
        if (msg.isFlag()) {
            lst = new ArrayList<OnlineAction>();
            String content = msg.getMsg();
            Log.e(TAG, "活动----" + content);
            try {
                JSONArray jsonArray = new JSONObject(content)
                        .getJSONArray("data");
                OnlineAction lists = null;
                if (jsonArray != null && jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        lists = new OnlineAction();
                        JSONObject obj = jsonArray.getJSONObject(i);
                        lists.setActionId(obj.getInt("id"));
                        lists.setTitle(obj.getString("title"));
                        lists.setActionTime(obj.getString("end_time"));
                        lst.add(lists);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i(TAG, "action list=" + content);
        }
        return lst;
    }

    // 获取未读评论数据
    public static NewCommentInfo getNewCommentCount(String sessionId, int uid) {
        NewCommentInfo commentInfo = new NewCommentInfo();
        ApiMessage msg = Api.getNewCommentCount(sessionId, uid);
        if (msg.isFlag()) {
            String content = msg.getMsg();
            try {
                JSONObject jsonRoot = new JSONObject(content)
                        .getJSONObject("data");
                JSONObject jsonComment = jsonRoot.getJSONObject("new_comment");
                commentInfo.setCommentCount(jsonRoot
                        .getInt("new_comment_count"));
                commentInfo.setHeadimg(jsonComment.getString("img"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i(TAG, "find list=" + content);
        }
        return commentInfo;
    }

    public static List<FindMore2> getNewCommentLists(String sessionId, int uid) {
        List<FindMore2> result = new ArrayList<FindMore2>();
        // 图片的路径
        String picPath = null;

        ApiMessage msg = Api.getNewCommentLists(sessionId, uid);
        if (msg.isFlag()) {
            String content = msg.getMsg();
            try {
                JSONArray jsonArray = new JSONObject(content)
                        .getJSONArray("data");
                if (jsonArray == null || jsonArray.length() == 0)
                    return null;

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject dataItem = jsonArray.getJSONObject(i);
                    // 获取图片信息，通过picPath是否等于null，来判断是否有图片需要显示
                    if (dataItem.has("pic")) {
                        JSONArray picArray = dataItem.getJSONArray("pic");
                        if (picArray != null || picPath.length() > 0) {
                            picPath = picArray.getString(0);

                        } else {
                            picPath = "";
                        }
                    } else {
                        picPath = "";
                    }
                    if (dataItem.has("comment")) {
                        // 获取Comment消息，有可能有多个
                        JSONArray commentArray = dataItem
                                .getJSONArray("comment");

                        for (int j = 0; j < commentArray.length(); j++) {
                            JSONObject obj = commentArray.getJSONObject(j);
                            FindMore2 fm2 = new FindMore2();
                            fm2.iszanOrPing = true;
                            fm2.setType(0);
                            fm2.setPicPath(picPath);
                            fm2.setCommentId(obj.getString("id"));
                            fm2.setFindId(obj.getString("find_id"));
                            fm2.setuId(obj.getString("uid"));
                            // fm2.toId = obj.getString("to_id");
                            // fm2.toUid = obj.getString("to_uid");
                            fm2.setContent(obj.getString("content"));
                            fm2.setWavAddress(obj.getString("wav"));
                            fm2.setWavTime(obj.getString("wavtime"));
                            fm2.setInputTime(obj.getString("inputtime"));
                            fm2.setName(obj.getString("name"));
                            // fm2.toName = obj.getString("to_name");
                            fm2.setUserImg(obj.getString("img"));
                            fm2.setTime(obj.getString("time"));
                            fm2.setIs_delete(obj.getInt("is_delete"));
                            result.add(fm2);
                        }
                    }
                    if (dataItem.has("like")) {
                        // 获取like消息
                        JSONArray likeArray = dataItem.getJSONArray("like");
                        for (int j = 0; j < likeArray.length(); j++) {
                            JSONObject obj = likeArray.getJSONObject(j);
                            FindMore2 fm2Like = new FindMore2();
                            fm2Like.iszanOrPing = false;
                            fm2Like.setType(1);
                            fm2Like.setPicPath(picPath);
                            fm2Like.setzId(obj.getString("id"));
                            fm2Like.setFindId(obj.getString("find_id"));
                            fm2Like.setuId(obj.getString("uid"));
                            fm2Like.setInputTime(obj.getString("inputtime"));
                            fm2Like.setName(obj.getString("name"));
                            fm2Like.setUserImg(obj.getString("img"));
                            result.add(fm2Like);
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        return result;
    }

    // 获取发现列表接口
    public static List<FindMore> getFindList(String sessionId, int times,
                                             boolean isFriends, int activity_id) throws ApiNetException,
            ApiSessionOutException {
        return getFindListByUid(sessionId, times, null, isFriends, activity_id);
    }

    // 获取好友发现列表接口
    public static List<FindMore> getGoodFriendsFindList(String sessionId,
                                                        int times, String uid, boolean isFriends, int activity_id)
            throws ApiNetException, ApiSessionOutException {
        return getFindListByUid(sessionId, times, uid, isFriends, activity_id);
    }

    // 获取发现列表接口--新
    public static List<FindGroup> getNewFindList(String sessionId)
            throws ApiNetException, ApiSessionOutException {
        return getFindGroupListByUid(sessionId);
    }

    // 获取发现列表接口--新
    public static List<FindGroup> getNewFindList(String sessionId, int times,
                                                 String uid, boolean isFriends, int activity_Id)
            throws ApiNetException, ApiSessionOutException {
        if (isFriends) {
            return getFindGroupListByUid(sessionId, times, uid, isFriends,
                    activity_Id);
        } else {
            return getFindGroupListByUid(sessionId, times, null, isFriends,
                    activity_Id);
        }
    }

    // 获取指定用户的发现列表接口
    public static List<FindMore> getFindListByUid(String sessionId, int times,
                                                  String uid, boolean isFriends, int activity_id)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.getFindList(sessionId, times, uid, isFriends,
                activity_id);
        List<FindMore> lst = null;
        if (msg.isFlag()) {
            lst = new ArrayList<FindMore>();
            String content = msg.getMsg();
            try {
                JSONArray jsonArray = new JSONObject(content)
                        .getJSONArray("data");
                FindMore lists = null;
                if (jsonArray != null && jsonArray.length() > 0) {
                    for (int i = 0, j = jsonArray.length(); i < j; i++) {
                        lists = new FindMore();
                        JSONObject obj = jsonArray.getJSONObject(i);
                        lists.setFindId(obj.getString("id"));
                        lists.setOtheruid(obj.getInt("uid"));
                        if (obj.getInt("sex") == 1) {
                            lists.setSex("man");
                        } else {
                            lists.setSex("woman");
                        }
                        lists.setDetils(obj.getString("content"));
                        lists.setOthername(obj.getString("name"));
                        lists.setOtherimg(obj.getString("img"));
                        lists.setTimes(obj.getLong("inputtime"));
                        if (obj.has("pic")) {
                            JSONArray pArray = obj.getJSONArray("pic");
                            int m = pArray.length();
                            String[] imgs = new String[m];
                            String[] biggerImgs = new String[m];
                            for (int k = 0; k < m; k++) {
                                String iurl = (String) pArray.get(k);
                                imgs[k] = iurl;
                                biggerImgs[k] = iurl.replace("s_", "b_");
                            }
                            lists.setImgs(imgs);
                            lists.setBiggerImgs(biggerImgs);
                            if (m == 1) {
                                lists.setWidth(obj.getInt("pic_width"));
                                lists.setHeight(obj.getInt("pic_height"));
                            }
                        }

                        if (obj.has("comment")) {
                            ArrayList<FindComment> fList = new ArrayList<FindComment>();
                            JSONArray cArray = obj.getJSONArray("comment");
                            for (int c = 0, b = cArray.length(); c < b; c++) {
                                JSONObject cObject = cArray.getJSONObject(c);
                                FindComment fc = new FindComment();
                                fc.setCommentId(cObject.getString("id"));
                                fc.setCommentText(cObject.getString("content"));
                                fc.setCommentWav(cObject.getString("wav"));
                                fc.setWavDuration(cObject.getString("wavtime"));
                                fc.setFirstName(cObject.getString("name"));
                                fc.setSecondName(cObject.getString("to_name"));
                                fList.add(fc);
                            }
                            lists.setTalkdetils(fList);
                            lists.setCommentCount(obj.getInt("comment_count"));
                        }
                        lists.setGoodpeople(obj.getInt("like_count"));
                        lists.setGood(obj.getInt("ilike") == 1 ? true : false);
                        if (obj.has("pic")) {
                            lst.add(lists);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e(TAG, "lst-------------" + lst.size());
            Log.i(TAG, "find list=" + content);
        }
        return lst;
    }

    // 获取指定用户的发现列表接口--新
    public static List<FindGroup> getFindGroupListByUid(String sessionId,
                                                        int times, String uid, boolean isFriends, int activity_Id)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.getFindList(sessionId, times, uid, isFriends,
                activity_Id);
        List<FindGroup> lst = null;
        if (msg.isFlag()) {
            lst = new ArrayList<FindGroup>();
            String content = msg.getMsg();
            try {
                JSONArray jsonArray = new JSONObject(content)
                        .getJSONArray("data");
                FindGroup lists = null;
                if (jsonArray != null && jsonArray.length() > 0) {
                    // 缓存运动秀的数据
                    if (times == 0) {
                        SharedPreferences preferences = SportsApp.getInstance()
                                .getSharedPreferences("FindGroupList", 0);
                        Editor edit = preferences.edit();
                        if (uid == null) {
                            edit.putString("FindGroupList_allinfo", content);
                        } else {
                            edit.putString("FindGroupList_info", content);
                        }
                        edit.commit();
                    }
                    for (int i = 0, j = jsonArray.length(); i < j; i++) {
                        lists = new FindGroup();
                        JSONObject obj = jsonArray.getJSONObject(i);
                        lists.setFindId(obj.getString("id"));
                        lists.setOtheruid(obj.getInt("uid"));
                        if (obj.getInt("sex") == 1) {
                            lists.setSex("man");
                        } else {
                            lists.setSex("woman");
                        }
                        if (obj.has("content")) {
                            lists.setDetils(obj.getString("content"));
                        }
                        lists.setOthername(obj.getString("name"));
                        lists.setOtherimg(obj.getString("img"));
                        if (obj.has("inputtime")) {
                            lists.setTimes(obj.getLong("inputtime"));
                        }
                        if (obj.has("if_fans")) {
                            lists.setIsFriends(obj.getInt("if_fans"));
                        }
                        if (obj.has("comefrom")) {
                            lists.setComefrom(obj.getString("comefrom"));
                        }
                        if (obj.has("pic")) {
                            JSONArray pArray = obj.getJSONArray("pic");
                            int m = pArray.length();
                            String[] imgs = new String[m];
                            String[] biggerImgs = new String[m];
                            for (int k = 0; k < m; k++) {
                                String iurl = (String) pArray.get(k);
                                imgs[k] = iurl;
                                biggerImgs[k] = iurl.replace("s_", "b_");
                            }
                            lists.setImgs(imgs);
                            lists.setBiggerImgs(biggerImgs);
                            if (m == 1) {
                                lists.setWidth(obj.getInt("pic_width"));
                                lists.setHeight(obj.getInt("pic_height"));
                            }
                        }

                        if (obj.has("topiccat")) {
                            ArrayList<TopicContent> topicList = new ArrayList<TopicContent>();
                            if (!"".equals(obj.getString("topiccat"))) {
                                JSONArray tArray = obj.getJSONArray("topiccat");
                                for (int c = 0, b = tArray.length(); c < b; c++) {
                                    JSONObject tObject = tArray
                                            .getJSONObject(c);
                                    TopicContent topicContent = new TopicContent();
                                    topicContent.setId(tObject.getString("id"));
                                    topicContent.setTitle(tObject
                                            .getString("title"));
                                    topicList.add(topicContent);
                                }
                            }
                            lists.setTopicList(topicList);
                        }

                        if (obj.has("sportsdata")) {
                            if (!"".equals(obj.getString("sportsdata"))) {
                                JSONObject jsonObject = obj
                                        .getJSONObject("sportsdata");
                                SportRecord sportRecord = new SportRecord();
                                sportRecord.setTime(jsonObject
                                        .getString("time"));
                                sportRecord.setSport_distance(jsonObject
                                        .getString("sport_distance"));
                                lists.setSportRecord(sportRecord);
                            }

                        }

                        if (obj.has("comment")) {
                            ArrayList<SportCircleComments> fList = new ArrayList<SportCircleComments>();
                            JSONArray cArray = obj.getJSONArray("comment");
                            for (int c = 0, b = cArray.length(); c < b; c++) {
                                JSONObject cObject = cArray.getJSONObject(c);
                                SportCircleComments fc = new SportCircleComments();
                                fc.setId(cObject.getString("id"));
                                fc.setUid(cObject.getString("uid"));
                                fc.setContent(cObject.getString("content"));
                                fc.setWav(cObject.getString("wav"));
                                fc.setWavtime(cObject.getString("wavtime"));
                                fc.setInputtime(cObject.getString("inputtime"));
                                fc.setName(cObject.getString("name"));
                                fc.setImg(cObject.getString("img"));
                                fc.setSex(cObject.getString("sex"));
                                fc.setTo_name(cObject.getString("to_name"));
                                fc.setTo_img(cObject.getString("to_img"));
                                fc.setTo_sex(cObject.getString("to_sex"));
                                fList.add(fc);
                            }
                            lists.setTalkdetils(fList);
                            lists.setcCount(obj.getInt("comment_count"));
                        }

                        if (obj.has("like")) {
                            ArrayList<PraiseUsers> pArrayList = new ArrayList<PraiseUsers>();
                            if (!"".equals(obj.getString("like"))) {
                                JSONArray sArray = obj.getJSONArray("like");
                                for (int c = 0, b = sArray.length(); c < b; c++) {
                                    JSONObject tObject = sArray
                                            .getJSONObject(c);
                                    PraiseUsers praiseUsers = new PraiseUsers();
                                    praiseUsers
                                            .setUid(tObject.getString("uid"));
                                    praiseUsers.setName(tObject
                                            .getString("name"));
                                    praiseUsers
                                            .setImg(tObject.getString("img"));
                                    praiseUsers
                                            .setSex(tObject.getString("sex"));
                                    pArrayList.add(praiseUsers);
                                }
                            }
                            lists.setpArrayList(pArrayList);
                        }
                        if (obj.has("like_count")) {
                            lists.setGoodpeople(obj.getInt("like_count"));
                        }
                        if (obj.has("ilike")) {
                            lists.setGood(obj.getInt("ilike") == 1 ? true
                                    : false);
                        }
                        // if (obj.has("pic")) {
                        lst.add(lists);
                        // }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e(TAG, "lst-------------" + lst.size());
            Log.i(TAG, "find list=" + content);
        }
        return lst;
    }

    // 获取指定用户的发现列表接口--新
    public static List<FindGroup> getFindGroupListByUid(String sessionId)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.getFindList(sessionId);
        List<FindGroup> lst = null;
        if (msg.isFlag()) {
            lst = new ArrayList<FindGroup>();
            String content = msg.getMsg();
            try {
                JSONArray jsonArray = new JSONObject(content)
                        .getJSONArray("data");
                FindGroup lists = null;
                if (jsonArray != null && jsonArray.length() > 0) {
                    for (int i = 0, j = jsonArray.length(); i < j; i++) {
                        lists = new FindGroup();
                        JSONObject obj = jsonArray.getJSONObject(i);
                        lists.setFindId(obj.getString("id"));
                        lists.setOtheruid(obj.getInt("uid"));
                        if (obj.getInt("sex") == 1) {
                            lists.setSex("man");
                        } else {
                            lists.setSex("woman");
                        }
                        lists.setDetils(obj.getString("content"));
                        lists.setOthername(obj.getString("name"));
                        lists.setOtherimg(obj.getString("img"));
                        lists.setTimes(obj.getLong("inputtime"));
                        if (obj.has("if_fans")) {
                            lists.setIsFriends(obj.getInt("if_fans"));
                        }
                        lists.setComefrom(obj.getString("comefrom"));
                        if (obj.has("pic")) {
                            JSONArray pArray = obj.getJSONArray("pic");
                            int m = pArray.length();
                            String[] imgs = new String[m];
                            String[] biggerImgs = new String[m];
                            for (int k = 0; k < m; k++) {
                                String iurl = (String) pArray.get(k);
                                imgs[k] = iurl;
                                biggerImgs[k] = iurl.replace("s_", "b_");
                            }
                            lists.setImgs(imgs);
                            lists.setBiggerImgs(biggerImgs);
                            if (m == 1) {
                                lists.setWidth(obj.getInt("pic_width"));
                                lists.setHeight(obj.getInt("pic_height"));
                            }
                        }

                        if (obj.has("topiccat")) {
                            ArrayList<TopicContent> topicList = new ArrayList<TopicContent>();
                            if (!"".equals(obj.getString("topiccat"))) {
                                JSONArray tArray = obj.getJSONArray("topiccat");
                                for (int c = 0, b = tArray.length(); c < b; c++) {
                                    JSONObject tObject = tArray
                                            .getJSONObject(c);
                                    TopicContent topicContent = new TopicContent();
                                    topicContent.setId(tObject.getString("id"));
                                    topicContent.setTitle(tObject
                                            .getString("title"));
                                    topicList.add(topicContent);
                                }
                            }
                            lists.setTopicList(topicList);
                        }

                        if (obj.has("sportsdata")) {
                            if (!"".equals(obj.getString("sportsdata"))) {
                                JSONObject jsonObject = obj
                                        .getJSONObject("sportsdata");
                                SportRecord sportRecord = new SportRecord();
                                sportRecord.setTime(jsonObject
                                        .getString("time"));
                                sportRecord.setSport_distance(jsonObject
                                        .getString("sport_distance"));
                                lists.setSportRecord(sportRecord);
                            }

                        }

                        if (obj.has("comment")) {
                            ArrayList<SportCircleComments> fList = new ArrayList<SportCircleComments>();
                            JSONArray cArray = obj.getJSONArray("comment");
                            for (int c = 0, b = cArray.length(); c < b; c++) {
                                JSONObject cObject = cArray.getJSONObject(c);
                                SportCircleComments fc = new SportCircleComments();
                                fc.setId(cObject.getString("id"));
                                fc.setUid(cObject.getString("uid"));
                                fc.setContent(cObject.getString("content"));
                                fc.setWav(cObject.getString("wav"));
                                fc.setWavtime(cObject.getString("wavtime"));
                                fc.setInputtime(cObject.getString("inputtime"));
                                fc.setName(cObject.getString("name"));
                                fc.setImg(cObject.getString("img"));
                                fc.setSex(cObject.getString("sex"));
                                fc.setTo_name(cObject.getString("to_name"));
                                fc.setTo_img(cObject.getString("to_img"));
                                fc.setTo_sex(cObject.getString("to_sex"));
                                fList.add(fc);
                            }
                            lists.setTalkdetils(fList);
                            lists.setcCount(obj.getInt("comment_count"));
                        }

                        if (obj.has("like")) {
                            ArrayList<PraiseUsers> pArrayList = new ArrayList<PraiseUsers>();
                            if (!"".equals(obj.getString("like"))) {
                                JSONArray sArray = obj.getJSONArray("like");
                                for (int c = 0, b = sArray.length(); c < b; c++) {
                                    JSONObject tObject = sArray
                                            .getJSONObject(c);
                                    PraiseUsers praiseUsers = new PraiseUsers();
                                    praiseUsers
                                            .setUid(tObject.getString("uid"));
                                    praiseUsers.setName(tObject
                                            .getString("name"));
                                    praiseUsers
                                            .setImg(tObject.getString("img"));
                                    praiseUsers
                                            .setSex(tObject.getString("sex"));
                                    pArrayList.add(praiseUsers);
                                }
                            }
                            lists.setpArrayList(pArrayList);
                        }

                        lists.setGoodpeople(obj.getInt("like_count"));
                        lists.setGood(obj.getInt("ilike") == 1 ? true : false);
                        // if (obj.has("pic")) {
                        lst.add(lists);
                        // }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e(TAG, "lst-------------" + lst.size());
            Log.i(TAG, "find list=" + content);
        }
        return lst;
    }

    // 获取发现列表接口--新
    public static List<FindGroup> getNewPersonalFindList(String sessionId,
                                                         int times, String uid) throws ApiNetException,
            ApiSessionOutException {
        return getFindPersonalListByUid(sessionId, times, uid);
    }

    // 获取个人主页动态发现列表接口
    public static List<FindGroup> getFindPersonalListByUid(String sessionId,
                                                           int times, String uid) throws ApiNetException,
            ApiSessionOutException {
        ApiMessage msg = Api.getPersonalFindList(sessionId, times, uid);
        List<FindGroup> lst = null;
        if (msg.isFlag()) {
            lst = new ArrayList<FindGroup>();
            String content = msg.getMsg();
            try {
                JSONArray jsonArray = new JSONObject(content)
                        .getJSONArray("data");
                FindGroup lists = null;
                if (jsonArray != null && jsonArray.length() > 0) {
                    for (int i = 0, j = jsonArray.length(); i < j; i++) {
                        lists = new FindGroup();
                        JSONObject obj = jsonArray.getJSONObject(i);
                        lists.setFindId(obj.getString("id"));
                        lists.setOtheruid(obj.getInt("uid"));
                        if (obj.getInt("sex") == 1) {
                            lists.setSex("man");
                        } else {
                            lists.setSex("woman");
                        }
                        lists.setDetils(obj.getString("content"));
                        lists.setOthername(obj.getString("name"));
                        lists.setOtherimg(obj.getString("img"));
                        lists.setTimes(obj.getLong("inputtime"));
                        if (obj.has("if_fans")) {
                            lists.setIsFriends(obj.getInt("if_fans"));
                        }
                        lists.setComefrom(obj.getString("comefrom"));
                        if (obj.has("pic")) {
                            JSONArray pArray = obj.getJSONArray("pic");
                            int m = pArray.length();
                            String[] imgs = new String[m];
                            String[] biggerImgs = new String[m];
                            for (int k = 0; k < m; k++) {
                                String iurl = (String) pArray.get(k);
                                imgs[k] = iurl;
                                biggerImgs[k] = iurl.replace("s_", "b_");
                            }
                            lists.setImgs(imgs);
                            lists.setBiggerImgs(biggerImgs);
                            if (m == 1) {
                                lists.setWidth(obj.getInt("pic_width"));
                                lists.setHeight(obj.getInt("pic_height"));
                            }
                        }

                        if (obj.has("topiccat")) {
                            ArrayList<TopicContent> topicList = new ArrayList<TopicContent>();
                            if (!"".equals(obj.getString("topiccat"))) {
                                JSONArray tArray = obj.getJSONArray("topiccat");
                                for (int c = 0, b = tArray.length(); c < b; c++) {
                                    JSONObject tObject = tArray
                                            .getJSONObject(c);
                                    TopicContent topicContent = new TopicContent();
                                    topicContent.setId(tObject.getString("id"));
                                    topicContent.setTitle(tObject
                                            .getString("title"));
                                    topicList.add(topicContent);
                                }
                            }
                            lists.setTopicList(topicList);
                        }

                        if (obj.has("sportsdata")) {
                            if (!"".equals(obj.getString("sportsdata"))) {
                                JSONObject jsonObject = obj
                                        .getJSONObject("sportsdata");
                                SportRecord sportRecord = new SportRecord();
                                sportRecord.setTime(jsonObject
                                        .getString("time"));
                                sportRecord.setSport_distance(jsonObject
                                        .getString("sport_distance"));
                                lists.setSportRecord(sportRecord);
                            }
                        }

                        if (obj.has("comment")) {
                            ArrayList<SportCircleComments> fList = new ArrayList<SportCircleComments>();
                            JSONArray cArray = obj.getJSONArray("comment");
                            for (int c = 0, b = cArray.length(); c < b; c++) {
                                JSONObject cObject = cArray.getJSONObject(c);
                                SportCircleComments fc = new SportCircleComments();
                                fc.setId(cObject.getString("id"));
                                fc.setUid(cObject.getString("uid"));
                                fc.setContent(cObject.getString("content"));
                                fc.setWav(cObject.getString("wav"));
                                fc.setWavtime(cObject.getString("wavtime"));
                                fc.setInputtime(cObject.getString("inputtime"));
                                fc.setName(cObject.getString("name"));
                                fc.setImg(cObject.getString("img"));
                                fc.setSex(cObject.getString("sex"));
                                fc.setTo_name(cObject.getString("to_name"));
                                fc.setTo_img(cObject.getString("to_img"));
                                fc.setTo_sex(cObject.getString("to_sex"));
                                fList.add(fc);
                            }
                            lists.setTalkdetils(fList);
                            lists.setcCount(obj.getInt("comment_count"));
                        }

                        if (obj.has("like")) {
                            ArrayList<PraiseUsers> pArrayList = new ArrayList<PraiseUsers>();
                            if (!"".equals(obj.getString("like"))) {
                                JSONArray sArray = obj.getJSONArray("like");
                                for (int c = 0, b = sArray.length(); c < b; c++) {
                                    JSONObject tObject = sArray
                                            .getJSONObject(c);
                                    PraiseUsers praiseUsers = new PraiseUsers();
                                    praiseUsers
                                            .setUid(tObject.getString("uid"));
                                    praiseUsers.setName(tObject
                                            .getString("name"));
                                    praiseUsers
                                            .setImg(tObject.getString("img"));
                                    praiseUsers
                                            .setSex(tObject.getString("sex"));
                                    pArrayList.add(praiseUsers);
                                }
                            }

                            lists.setpArrayList(pArrayList);
                        }

                        lists.setGoodpeople(obj.getInt("like_count"));
                        lists.setGood(obj.getInt("ilike") == 1 ? true : false);
                        // if (obj.has("pic")) {
                        lst.add(lists);
                        // }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e(TAG, "lst-------------" + lst.size());
            Log.i(TAG, "find list=" + content);
        }
        return lst;
    }

    // 通过find_id获取对应的发现接口
    public static FindMore getGetFindMore(String sessionId, String find_id)
            throws ApiNetException, ApiSessionOutException {
        return getFindMoreByUid(sessionId, find_id, null);
    }

    // 通过find_id获取对应的发现接口--new
    public static FindGroup getGetFindGroup(String sessionId, String find_id)
            throws ApiNetException, ApiSessionOutException {
        return getFindSingleGroupByUid(sessionId, find_id, null);
    }

    // 获取指定用户的发现列表接口
    public static FindMore getFindMoreByUid(String sessionId, String find_id,
                                            String uid) throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.getFindMore(sessionId, find_id, uid);
        FindMore findinfo = new FindMore();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            try {
                JSONArray jsonArray = new JSONObject(content)
                        .getJSONArray("data");
                FindMore lists = null;
                if (jsonArray != null && jsonArray.length() > 0) {
                    for (int i = 0, j = jsonArray.length(); i < j; i++) {
                        lists = new FindMore();
                        JSONObject obj = jsonArray.getJSONObject(i);
                        lists.setFindId(obj.getString("id"));
                        lists.setOtheruid(obj.getInt("uid"));
                        if (obj.getInt("sex") == 1) {
                            lists.setSex("man");
                        } else {
                            lists.setSex("woman");
                        }
                        lists.setDetils(obj.getString("content"));
                        lists.setOthername(obj.getString("name"));
                        lists.setOtherimg(obj.getString("img"));
                        lists.setTimes(obj.getLong("inputtime"));
                        if (obj.has("pic")) {
                            JSONArray pArray = obj.getJSONArray("pic");
                            int m = pArray.length();
                            String[] imgs = new String[m];
                            String[] biggerImgs = new String[m];
                            for (int k = 0; k < m; k++) {
                                String iurl = (String) pArray.get(k);
                                imgs[k] = iurl;
                                biggerImgs[k] = iurl.replace("s_", "b_");
                            }
                            lists.setImgs(imgs);
                            lists.setBiggerImgs(biggerImgs);
                            if (m == 1) {
                                lists.setWidth(obj.getInt("pic_width"));
                                lists.setHeight(obj.getInt("pic_height"));
                            }
                        }

                        if (obj.has("comment")) {
                            ArrayList<FindComment> fList = new ArrayList<FindComment>();
                            JSONArray cArray = obj.getJSONArray("comment");
                            for (int c = 0, b = cArray.length(); c < b; c++) {
                                JSONObject cObject = cArray.getJSONObject(c);
                                FindComment fc = new FindComment();
                                fc.setCommentId(cObject.getString("id"));
                                fc.setCommentText(cObject.getString("content"));
                                fc.setCommentWav(cObject.getString("wav"));
                                fc.setWavDuration(cObject.getString("wavtime"));
                                fc.setFirstName(cObject.getString("name"));
                                fc.setSecondName(cObject.getString("to_name"));
                                fList.add(fc);
                            }
                            lists.setTalkdetils(fList);
                            lists.setCommentCount(obj.getInt("comment_count"));
                        }
                        lists.setGoodpeople(obj.getInt("like_count"));
                        lists.setGood(obj.getInt("ilike") == 1 ? true : false);

                    }
                    findinfo = lists;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return findinfo;
    }

    // 获取指定用户的发现列表接口--new
    public static FindGroup getFindSingleGroupByUid(String sessionId,
                                                    String find_id, String uid) throws ApiNetException,
            ApiSessionOutException {
        ApiMessage msg = Api.getFindSingleMore(sessionId, find_id, uid);
        FindGroup findinfo = null;
        if (msg.isFlag()) {
            String content = msg.getMsg();
            try {
                JSONArray jsonArray = new JSONObject(content)
                        .getJSONArray("data");
                FindGroup lists = null;
                if (jsonArray != null && jsonArray.length() > 0) {
                    for (int i = 0, j = jsonArray.length(); i < j; i++) {
                        lists = new FindGroup();
                        JSONObject obj = jsonArray.getJSONObject(i);
                        lists.setFindId(obj.getString("id"));
                        lists.setOtheruid(obj.getInt("uid"));
                        if (obj.getInt("sex") == 1) {
                            lists.setSex("man");
                        } else {
                            lists.setSex("woman");
                        }
                        lists.setDetils(obj.getString("content"));
                        lists.setOthername(obj.getString("name"));
                        lists.setOtherimg(obj.getString("img"));
                        lists.setTimes(obj.getLong("inputtime"));
                        if (obj.has("if_fans")) {
                            lists.setIsFriends(obj.getInt("if_fans"));
                        }
                        lists.setComefrom(obj.getString("comefrom"));
                        if (obj.has("pic")) {
                            JSONArray pArray = obj.getJSONArray("pic");
                            int m = pArray.length();
                            String[] imgs = new String[m];
                            String[] biggerImgs = new String[m];
                            for (int k = 0; k < m; k++) {
                                String iurl = (String) pArray.get(k);
                                imgs[k] = iurl;
                                biggerImgs[k] = iurl.replace("s_", "b_");
                            }
                            lists.setImgs(imgs);
                            lists.setBiggerImgs(biggerImgs);
                            if (m == 1) {
                                lists.setWidth(obj.getInt("pic_width"));
                                lists.setHeight(obj.getInt("pic_height"));
                            }
                        }

                        if (obj.has("topiccat")) {
                            ArrayList<TopicContent> topicList = new ArrayList<TopicContent>();
                            if (!"".equals(obj.getString("topiccat"))) {
                                JSONArray tArray = obj.getJSONArray("topiccat");
                                for (int c = 0, b = tArray.length(); c < b; c++) {
                                    JSONObject tObject = tArray
                                            .getJSONObject(c);
                                    TopicContent topicContent = new TopicContent();
                                    topicContent.setId(tObject.getString("id"));
                                    topicContent.setTitle(tObject
                                            .getString("title"));
                                    topicList.add(topicContent);
                                }
                            }
                            lists.setTopicList(topicList);
                        }

                        if (obj.has("sportsdata")) {
                            SportRecord sportRecord = null;
                            if (!"".equals(obj.getString("sportsdata"))) {
                                JSONObject jsonObject = obj
                                        .getJSONObject("sportsdata");
                                sportRecord = new SportRecord();
                                sportRecord.setTime(jsonObject
                                        .getString("time"));
                                sportRecord.setSport_distance(jsonObject
                                        .getString("sport_distance"));
                            }
                            lists.setSportRecord(sportRecord);
                        }

                        if (obj.has("comment")) {
                            ArrayList<SportCircleComments> fList = new ArrayList<SportCircleComments>();
                            JSONArray cArray = obj.getJSONArray("comment");
                            for (int c = 0, b = cArray.length(); c < b; c++) {
                                JSONObject cObject = cArray.getJSONObject(c);
                                SportCircleComments fc = new SportCircleComments();
                                fc.setId(cObject.getString("id"));
                                fc.setUid(cObject.getString("uid"));
                                fc.setContent(cObject.getString("content"));
                                fc.setWav(cObject.getString("wav"));
                                fc.setWavtime(cObject.getString("wavtime"));
                                fc.setInputtime(cObject.getString("inputtime"));
                                fc.setName(cObject.getString("name"));
                                fc.setImg(cObject.getString("img"));
                                fc.setSex(cObject.getString("sex"));
                                fc.setTo_name(cObject.getString("to_name"));
                                fc.setTo_img(cObject.getString("to_img"));
                                fc.setTo_sex(cObject.getString("to_sex"));
                                fList.add(fc);
                            }
                            lists.setTalkdetils(fList);
                            lists.setcCount(obj.getInt("comment_count"));
                        }

                        if (obj.has("like")) {
                            ArrayList<PraiseUsers> pArrayList = new ArrayList<PraiseUsers>();
                            if (!"".equals(obj.getString("like"))) {
                                JSONArray sArray = obj.getJSONArray("like");
                                for (int c = 0, b = sArray.length(); c < b; c++) {
                                    JSONObject tObject = sArray
                                            .getJSONObject(c);
                                    PraiseUsers praiseUsers = new PraiseUsers();
                                    praiseUsers
                                            .setUid(tObject.getString("uid"));
                                    praiseUsers.setName(tObject
                                            .getString("name"));
                                    praiseUsers
                                            .setImg(tObject.getString("img"));
                                    praiseUsers
                                            .setSex(tObject.getString("sex"));
                                    pArrayList.add(praiseUsers);
                                }
                            }
                            lists.setpArrayList(pArrayList);
                        }

                        lists.setGoodpeople(obj.getInt("like_count"));
                        lists.setGood(obj.getInt("ilike") == 1 ? true : false);
                        // if (obj.has("pic")) {
                        // lst.add(lists);
                        // }
                    }
                    findinfo = new FindGroup();
                    findinfo = lists;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Log.e(TAG, "lst-------------" + lst.size());
            Log.i(TAG, "find list=" + content);
        }
        return findinfo;
    }

    // 添加发现接口
    public static AddFindItem addFind(String sessionId, String fcontent,
                                      String comefrom, ArrayList<String> pics, String topiccat,
                                      String datasource, String activity_id, String title_name)
            throws ApiNetException {
        ApiMessage msg = Api.addFind(sessionId, fcontent, comefrom, pics,
                topiccat, datasource, activity_id, title_name);
        AddFindItem findItem = null;
        if (msg.isFlag()) {
            String content = msg.getMsg();
            findItem = new AddFindItem();
            Log.v("ApiJson", "content --> " + content);
            try {
                if(content!=null&&!"".equals(content)){
                    JSONObject jsonObject = new JSONObject(content)
                            .getJSONObject("data");
                    if (jsonObject.has("flag")) {
                        findItem.flag = jsonObject.getInt("flag");
                    } else {
                        findItem.flag = -1;
                    }
                    if (jsonObject.has("find_id")) {
                        findItem.findId = jsonObject.getString("find_id");
                    }
                    if (jsonObject.has("pic")) {
                        if (jsonObject.getString("pic") != null
                                && !"".equals(jsonObject.getString("pic"))) {
                            JSONArray pArray = jsonObject.getJSONArray("pic");
                            if (pArray != null) {
                                int m = pArray.length();
                                String[] urls = new String[m];
                                String[] biggerImgs = new String[m];
                                for (int k = 0; k < m; k++) {
                                    String iurl = (String) pArray.get(k);
                                    urls[k] = iurl;
                                    biggerImgs[k] = iurl.replace("s_", "b_");
                                }
                                findItem.urls = urls;
                                findItem.bigurls = biggerImgs;
                                if (m == 1) {
                                    if(jsonObject.has("pic_width")){
                                        findItem.width = jsonObject.getInt("pic_width");
                                    }
                                    if(jsonObject.has("pic_height")){
                                        findItem.height = jsonObject
                                                .getInt("pic_height");
                                    }
                                }
                            }
                        }

                    }
                }else{
                    findItem.flag = -1;
                }

            } catch (JSONException e) {
                e.printStackTrace();
                return findItem;
            }
            return findItem;
        } else {
            if (msg != null && ("SocketTimeoutException").equals(msg.getMsg())){
                findItem = new AddFindItem();
                findItem.flag=-56;
                return findItem;
            }else{
                throw new ApiNetException(SportsApp.getInstance().getResources().getString(R.string.sports_get_list_failed2));
            }
        }
    }

    // 删除发现接口
    public static ApiBack delFind(String sessionId, String findId)
            throws ApiNetException {
        ApiMessage msg = Api.delFind(sessionId, findId);
        ApiBack back = null;
        if (msg.isFlag()) {
            back = new ApiBack();
            String content = msg.getMsg();
            Log.v("ApiJson", "content --> " + content);
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                back.setFlag(jsonObject.getInt("flag"));
                back.setMsg(jsonObject.getString("msg"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return back;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // 置顶发现接口
    public static ApiBack topFind(String sessionId, String findId,
                                  String toptime) throws ApiNetException {
        Log.i("toplist", "api置顶传入的sessionid和发现id：" + sessionId + ";" + findId);
        ApiMessage msg = Api.TopFind(sessionId, findId, toptime);
        ApiBack back = null;
        if (msg.isFlag()) {
            back = new ApiBack();
            String content = msg.getMsg();
            Log.v("ApiJson", "content --> " + content);
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                back.setFlag(jsonObject.getInt("flag"));
                back.setMsg(jsonObject.getString("msg"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return back;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // 取消置顶发现接口
    public static ApiBack deltopFind(String sessionId, String findId)
            throws ApiNetException {
        Log.i("toplist", "api置顶传入的sessionid和发现id：" + sessionId + ";" + findId);
        ApiMessage msg = Api.DelTopFind(sessionId, findId);
        ApiBack back = null;
        if (msg.isFlag()) {
            back = new ApiBack();
            String content = msg.getMsg();
            Log.v("ApiJson", "content --> " + content);
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                back.setFlag(jsonObject.getInt("flag"));
                back.setMsg(jsonObject.getString("msg"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return back;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // 赞发现接口
    public static ApiBack likeFind(String sessionId, String findId)
            throws ApiNetException {
        ApiMessage msg = Api.likeFind(sessionId, findId);
        ApiBack back = null;
        if (msg.isFlag()) {
            back = new ApiBack();
            String content = msg.getMsg();
            // Log.e("ApiJson", "content-----" + content);
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                back.setFlag(jsonObject.getInt("flag"));
                back.setMsg(jsonObject.getString("msg"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return back;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // 添加或是回复评论接口
    public static String addComment(String sessionId, String findId,
                                    String comment, String wav, String wavtime, String toId)
            throws ApiNetException {
        Log.e(TAG, "音频2-文字---------" + comment);
        Log.e(TAG, "音频2-文件---------" + wav);
        Log.e(TAG, "音频2-长度---------" + wavtime);
        ApiMessage msg = Api.addComment(sessionId, findId, comment, wav,
                wavtime, toId);
        String commentId = null;
        Log.e(TAG, "msg.isFlag()-----------" + msg.isFlag());
        if (msg.isFlag()) {
            String content = msg.getMsg();
            Log.v("ApiJson", "content --> " + content);
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                commentId = jsonObject.getString("comment_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e(TAG, "commentId-----------" + commentId);
            return commentId;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // 添加系统或是回复评论接口
    public static String addSysComment(String sessionId, String findId,
                                       String comment, String wav, String wavtime, String toId)
            throws ApiNetException {
        Log.e(TAG, "音频2-文字---------" + comment);
        Log.e(TAG, "音频2-文件---------" + wav);
        Log.e(TAG, "音频2-长度---------" + wavtime);
        ApiMessage msg = Api.addSysComment(sessionId, findId, comment, wav,
                wavtime, toId);
        String commentId = null;
        Log.e(TAG, "msg.isFlag()-----------" + msg.isFlag());
        if (msg.isFlag()) {
            String content = msg.getMsg();
            Log.v("ApiJson", "content --> " + content);
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                commentId = jsonObject.getString("comment_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e(TAG, "commentId-----------" + commentId);
            return commentId;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // 删除评论接口
    public static ApiBack delComment(String sessionId, String commentId)
            throws ApiNetException {
        ApiMessage msg = Api.delComment(sessionId, commentId);
        ApiBack back = null;
        if (msg.isFlag()) {
            back = new ApiBack();
            String content = msg.getMsg();
            Log.v("ApiJson", "content --> " + content);
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                back.setFlag(jsonObject.getInt("flag"));
                back.setMsg(jsonObject.getString("msg"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return back;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // 修改发现背景图接口
    public static ApiBack updateFindBg(String sessionId, String findimg)
            throws ApiNetException {
        ApiMessage msg = Api.updateFindBg(sessionId, findimg);
        ApiBack back = null;
        if (msg.isFlag()) {
            back = new ApiBack();
            String content = msg.getMsg();
            Log.v("ApiJson", "content --> " + content);
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                back.setFlag(jsonObject.getInt("flag"));
                back.setMsg(jsonObject.getString("msg"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return back;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // 新修改密码接口
    public static ApiBack updatePwNew(String verifyCode, String phoneno,
                                      String passwd) throws ApiNetException {
        ApiMessage msg = Api.updatePwNew(verifyCode, phoneno, passwd);
        ApiBack back = null;
        if (msg.isFlag()) {
            back = new ApiBack();
            String content = msg.getMsg();
            Log.v("ApiJson", "content --> " + content);
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                back.setFlag(jsonObject.getInt("flag"));
                back.setMsg(jsonObject.getString("msg"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return back;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // 上传运动数据到手机QQ健康平台
    public static ApiBack qqHealthTask(int typeId, double dis, int sportGoal,
                                       String[] params) throws ApiNetException {
        // TODO Auto-generated method stub
        ApiMessage msg = Api.qqHealthTask(typeId, dis, sportGoal, params);
        ApiBack back = null;
        if (msg.isFlag()) {
            back = new ApiBack();
            String content = msg.getMsg();
            Log.v("ApiJson", "content --> " + content);
            try {
                if(content!=null&&!"".equals(content)){
                    JSONObject jsonObject = new JSONObject(content);
                    if(jsonObject.has("ret")){
                        back.setFlag(jsonObject.getInt("ret"));
                    }
                    if(jsonObject.has("msg")){
                        back.setMsg(jsonObject.getString("msg"));
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return back;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // 广告
    public static AdContent adShow(String channelnum) throws ApiNetException {
        ApiMessage msg = Api.adShow(channelnum);
        AdContent detail = null;
        if (msg.isFlag()) {
            String content = msg.getMsg();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                if (jsonObject.has("thumb")) {
                    detail = new AdContent();
                    detail.setAdimg(jsonObject.getString("thumb"));
                    detail.setPlay_seconds(jsonObject.getString("play_time"));
                    detail.setJumpurl(jsonObject.getString("jumpurl"));
                    detail.setJumpurl(jsonObject.getString("linkurl"));
                    if(jsonObject.has("xfadid")){
                        detail.setXfadid(jsonObject.getString("xfadid"));
                    }
                    if(jsonObject.has("is_xunfei")){
                        detail.setIs_xunfei(jsonObject.getInt("is_xunfei"));
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("getWeatherForecast", "weatherforecast" + content);
            Log.e(TAG, "content--------" + content);
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
        return detail;
    }

    public static int saveDataToServer(String userName, String sessionId,
                                       String uId, String sportsDate, String sportsType,
                                       String sportsDistance, String sportsCalorie, String sportsVelocity,
                                       String sportsTime, String img, String map, String datasource)
            throws ApiNetException {
        ApiMessage msg = Api.getSportsRecordInfo(userName, sessionId, uId,
                sportsDate, sportsType, sportsDistance, sportsCalorie,
                sportsVelocity, sportsTime, img, map, datasource);
        Log.e("weixinshare", "flag : " + msg.isFlag());
        Log.e("weixinshare", "msg : " + msg.getMsg());

        int id = -1;

        if (msg.isFlag()) {
            String content = msg.getMsg();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                if (0 == jsonObject.getInt("flag")) {
                    id = jsonObject.getInt("id");
                } else {
                    id = -1;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return id;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    public static ApiBack openequipment(String serial, String model)
            throws ApiNetException {
        ApiMessage msg = Api.openequipment(serial, model);
        ApiBack back = new ApiBack();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            Log.v("login", content);
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                back.setFlag(jsonObject.getInt("flag"));
                back.setMsg(jsonObject.getString("msg"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return back;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // 获取点赞用户列表
    public static ArrayList<SportsLike> getSportsLikes(String session_id,
                                                       int find_id, int uid) throws ApiNetException,
            ApiSessionOutException {
        ApiMessage msg = Api.getLikeLists(session_id, find_id, uid);
        ArrayList<SportsLike> sportsLikes = null;
        if (msg.isFlag()) {
            String content = msg.getMsg();
            sportsLikes = new ArrayList<SportsLike>();
            try {
                JSONArray jsonArray = new JSONObject(content)
                        .getJSONArray("data");
                SportsLike sportsLike = null;
                if (jsonArray != null && jsonArray.length() > 0) {
                    for (int i = 0, j = jsonArray.length(); i < j; i++) {
                        sportsLike = new SportsLike();
                        JSONObject obj = jsonArray.getJSONObject(i);
                        sportsLike.setFind_id(obj.getInt("find_id"));
                        sportsLike.setIf_fans(obj.getInt("if_fans"));
                        sportsLike.setImg(obj.getString("img"));
                        sportsLike.setName(obj.getString("name"));
                        sportsLike.setSex(obj.getInt("sex"));
                        sportsLike.setTime(obj.getString("time"));
                        sportsLike.setUid(obj.getInt("uid"));
                        SportRecord record = null;
                        if (obj.has("sportsdata")) {
                            JSONObject jsonObject = obj
                                    .getJSONObject("sportsdata");
                            record = new SportRecord();
                            record.setSport_distance(jsonObject
                                    .getString("sport_distance"));
                            record.setTime(jsonObject.getString("time"));
                            sportsLike.setSportsdata(record);

                        }

                        sportsLikes.add(sportsLike);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return sportsLikes;
    }

    // 获取发现用户列表
    public static CircleFinds getCircleFindLists(String session_id, int times,
                                                 int uid, int type) throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.getCircleFindLists(session_id, times, uid, type);
        CircleFinds circleFinds = null;
        if (msg.isFlag()) {
            String content = msg.getMsg();
            try {
                JSONObject jsonObject2 = new JSONObject(content)
                        .getJSONObject("data");
                if (jsonObject2 != null && !"".equals(jsonObject2.toString())) {
                    circleFinds = new CircleFinds();
                    JSONArray jsonArray = jsonObject2.getJSONArray("ad");
                    if (jsonArray != null && jsonArray.length() != 0) {
                        List<CircleFindsAd> adList = new ArrayList<CircleFindsAd>();
                        CircleFindsAd circleFindsAd;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            circleFindsAd = new CircleFindsAd();
                            circleFindsAd.setId(jsonArray.getJSONObject(i)
                                    .getInt("id"));
                            circleFindsAd.setImg(jsonArray.getJSONObject(i)
                                    .getString("img"));
                            adList.add(circleFindsAd);
                        }
                        circleFinds.setAdList(adList);
                    }
                    JSONArray jsonArray2 = jsonObject2.getJSONArray("cat");
                    if (jsonArray2 != null && jsonArray2.length() != 0) {
                        List<CircleFindsCat> calist = new ArrayList<CircleFindsCat>();
                        CircleFindsCat circleFindsCat;
                        for (int i = 0; i < jsonArray2.length(); i++) {
                            circleFindsCat = new CircleFindsCat();
                            circleFindsCat.setId(jsonArray2.getJSONObject(i)
                                    .getInt("id"));
                            circleFindsCat.setTitle(jsonArray2.getJSONObject(i)
                                    .getString("title"));
                            calist.add(circleFindsCat);
                        }
                        circleFinds.setCalist(calist);
                    }
                    JSONArray jsonArray3 = jsonObject2.getJSONArray("lists");
                    if (jsonArray3 != null && jsonArray3.length() != 0) {
                        List<CircleFindsLists> lists = new ArrayList<CircleFindsLists>();
                        CircleFindsLists circleFindsLists;
                        for (int i = 0; i < jsonArray3.length(); i++) {
                            circleFindsLists = new CircleFindsLists();
                            circleFindsLists.setId(jsonArray3.getJSONObject(i)
                                    .getInt("id"));
                            circleFindsLists.setTitle(jsonArray3.getJSONObject(
                                    i).getString("title"));
                            circleFindsLists.setImg(jsonArray3.getJSONObject(i)
                                    .getString("img"));
                            lists.add(circleFindsLists);
                        }
                        circleFinds.setLists(lists);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return circleFinds;
    }

    // 获取标签列表
    public static List<CircleFindsCat> getBiaoQianLists(String session_id,
                                                        int times, int uid, int type) throws ApiNetException,
            ApiSessionOutException {
        ApiMessage msg = Api.getCircleFindLists(session_id, times, uid, type);
        List<CircleFindsCat> lCats = new ArrayList<CircleFindsCat>();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            try {
                JSONArray JSONArray2 = new JSONObject(content)
                        .getJSONArray("data");
                if (JSONArray2 != null && JSONArray2.length() != 0) {
                    CircleFindsCat circleFindsCat;
                    for (int i = 0; i < JSONArray2.length(); i++) {
                        circleFindsCat = new CircleFindsCat();
                        circleFindsCat.setId(JSONArray2.getJSONObject(i)
                                .getInt("id"));
                        circleFindsCat.setTitle(JSONArray2.getJSONObject(i)
                                .getString("title"));
                        lCats.add(circleFindsCat);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return lCats;
    }

    // 获取发现详情
    public static CircleFindDetailContent getCircleFindListsContent(
            String session_id, int times, int id, int type, int list)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.getCircleFindListsContent(session_id, times, id,
                type, list);
        CircleFindDetailContent circleFindDetailContent = null;
        if (msg.isFlag()) {
            String content = msg.getMsg();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                if (jsonObject != null && !"".equals(jsonObject.toString())) {
                    circleFindDetailContent = new CircleFindDetailContent();
                    JSONObject jsonObject2 = jsonObject.getJSONObject("info");
                    if (jsonObject2 != null
                            && !"".equals(jsonObject2.toString())) {
                        PointsSay pointsSay = new PointsSay();
                        pointsSay.setId(jsonObject2.getInt("id"));
                        pointsSay.setImg(jsonObject2.getString("img"));
                        pointsSay.setConnent(jsonObject2.getString("connent"));
                        pointsSay.setTitle(jsonObject2.getString("title"));
                        pointsSay.setStart_time(jsonObject2
                                .getString("start_time"));
                        pointsSay
                                .setEnd_time(jsonObject2.getString("end_time"));
                        circleFindDetailContent.setPointsSay(pointsSay);
                    }

                    JSONArray JSONArray2 = jsonObject.getJSONArray("alllist");
                    if (JSONArray2 != null && JSONArray2.length() != 0) {
                        List<CircleFindContent> alllist = new ArrayList<CircleFindContent>();
                        CircleFindContent circleFindContent;
                        for (int i = 0; i < JSONArray2.length(); i++) {
                            circleFindContent = new CircleFindContent();
                            circleFindContent.setId(JSONArray2.getJSONObject(i)
                                    .getInt("id"));
                            circleFindContent.setUid(JSONArray2
                                    .getJSONObject(i).getInt("uid"));
                            circleFindContent.setContent(JSONArray2
                                    .getJSONObject(i).getString("content"));
                            if (JSONArray2.getJSONObject(i).has("pic")) {
                                JSONArray pArray = JSONArray2.getJSONObject(i)
                                        .getJSONArray("pic");
                                int m = pArray.length();
                                String[] imgs = new String[m];
                                String[] biggerImgs = new String[m];
                                for (int k = 0; k < m; k++) {
                                    String iurl = (String) pArray.get(k);
                                    imgs[k] = iurl;
                                    biggerImgs[k] = iurl.replace("s_", "b_");
                                }
                                circleFindContent.setImgs(imgs);
                                circleFindContent.setBiggerImgs(biggerImgs);
                                if (m == 1) {
                                    circleFindContent.setPic_width(JSONArray2
                                            .getJSONObject(i).getInt(
                                                    "pic_width"));
                                    circleFindContent.setPic_height(JSONArray2
                                            .getJSONObject(i).getInt(
                                                    "pic_height"));
                                }
                            }
                            circleFindContent.setComefrom(JSONArray2
                                    .getJSONObject(i).getString("comefrom"));
                            circleFindContent.setLikenum(JSONArray2
                                    .getJSONObject(i).getInt("likenum"));
                            circleFindContent.setCommentnum(JSONArray2
                                    .getJSONObject(i).getInt("commentnum"));
                            circleFindContent.setIlike(JSONArray2
                                    .getJSONObject(i).getInt("ilikenum"));
                            alllist.add(circleFindContent);
                        }
                        circleFindDetailContent.setAlllist(alllist);

                    }

                    JSONArray JSONArray3 = jsonObject
                            .getJSONArray("recommendlist");
                    if (JSONArray3 != null && JSONArray3.length() != 0) {
                        List<CircleFindContent> recommendlist = new ArrayList<CircleFindContent>();
                        CircleFindContent circleFindContent;
                        for (int i = 0; i < JSONArray3.length(); i++) {
                            circleFindContent = new CircleFindContent();
                            circleFindContent.setId(JSONArray3.getJSONObject(i)
                                    .getInt("id"));
                            circleFindContent.setUid(JSONArray3
                                    .getJSONObject(i).getInt("uid"));
                            circleFindContent.setContent(JSONArray3
                                    .getJSONObject(i).getString("content"));
                            if (JSONArray3.getJSONObject(i).has("pic")) {
                                JSONArray pArray = JSONArray3.getJSONObject(i)
                                        .getJSONArray("pic");
                                int m = pArray.length();
                                String[] imgs = new String[m];
                                String[] biggerImgs = new String[m];
                                for (int k = 0; k < m; k++) {
                                    String iurl = (String) pArray.get(k);
                                    imgs[k] = iurl;
                                    biggerImgs[k] = iurl.replace("s_", "b_");
                                }
                                circleFindContent.setImgs(imgs);
                                circleFindContent.setBiggerImgs(biggerImgs);
                                if (m == 1) {
                                    circleFindContent.setPic_width(JSONArray3
                                            .getJSONObject(i).getInt(
                                                    "pic_width"));
                                    circleFindContent.setPic_height(JSONArray3
                                            .getJSONObject(i).getInt(
                                                    "pic_height"));
                                }
                            }
                            circleFindContent.setComefrom(JSONArray3
                                    .getJSONObject(i).getString("comefrom"));
                            circleFindContent.setLikenum(JSONArray3
                                    .getJSONObject(i).getInt("likenum"));
                            circleFindContent.setCommentnum(JSONArray3
                                    .getJSONObject(i).getInt("commentnum"));
                            circleFindContent.setIlike(JSONArray3
                                    .getJSONObject(i).getInt("ilikenum"));
                            recommendlist.add(circleFindContent);
                        }
                        circleFindDetailContent.setRecommendlist(recommendlist);

                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return circleFindDetailContent;
    }

    // 获取发现详情
    public static PointsSay getCirclePointsSayContent(String session_id,
                                                      int times, int id, int type, int list) throws ApiNetException,
            ApiSessionOutException {
        ApiMessage msg = Api.getCircleFindListsContent(session_id, times, id,
                type, list);
        PointsSay pointsSay = null;
        if (msg.isFlag()) {
            String content = msg.getMsg();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                if (jsonObject != null && !"".equals(jsonObject.toString())) {
                    pointsSay = new PointsSay();
                    JSONObject jsonObject2 = jsonObject.getJSONObject("info");
                    if (jsonObject2 != null
                            && !"".equals(jsonObject2.toString())) {
                        pointsSay = new PointsSay();
                        pointsSay.setId(jsonObject2.getInt("id"));
                        pointsSay.setImg(jsonObject2.getString("img"));
                        pointsSay.setConnent(jsonObject2.getString("connent"));
                        pointsSay.setTitle(jsonObject2.getString("title"));
                        pointsSay.setStart_time(jsonObject2
                                .getString("start_time"));
                        pointsSay
                                .setEnd_time(jsonObject2.getString("end_time"));

                        if (jsonObject.has("comments")) {
                            ArrayList<SysSportCircleComments> fList = new ArrayList<SysSportCircleComments>();
                            JSONArray cArray = jsonObject
                                    .getJSONArray("comments");
                            for (int c = 0, b = cArray.length(); c < b; c++) {
                                JSONObject cObject = cArray.getJSONObject(c);
                                SysSportCircleComments fc = new SysSportCircleComments();
                                fc.setId(cObject.getString("id"));
                                fc.setUid(cObject.getString("uid"));
                                fc.setContent(cObject.getString("content"));
                                fc.setWav(cObject.getString("wav"));
                                fc.setWavtime(cObject.getString("wavtime"));
                                fc.setInputtime(cObject.getString("inputtime"));
                                fc.setName(cObject.getString("name"));
                                fc.setImg(cObject.getString("img"));
                                fc.setSex(cObject.getString("sex"));
                                fc.setTo_name(cObject.getString("to_name"));
                                fc.setTo_img(cObject.getString("to_img"));
                                fc.setTo_sex(cObject.getString("to_sex"));
                                fc.setProvince(cObject.getString("province"));
                                fc.setCity(cObject.getString("city"));
                                fList.add(fc);
                            }
                            pointsSay.setfList(fList);

                        }

                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return pointsSay;
    }

    // 上传今日步数
    public static ApiBack uploadSportTemp(String sessionId, int step_num)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.uploadTepe(sessionId, step_num);
        ApiBack back = new ApiBack();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            Log.i("content", content);
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                int id = jsonObject.getInt("id");
                // Log.i("taskid", taskID + "---");
                // if (taskID >= 0) {
                back.setReg(id);
                // }
                int flag = jsonObject.getInt("flag");
                if (flag == 0) {
                    back.setFlag(1);
                    Log.i("flag", back.getFlag() + "---" + flag);
                } else {
                    back.setFlag(-1);
                }
                back.setMsg(jsonObject.getString("msg"));
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                return back;
            }
        } else {
            // throw new ApiNetException("error:" + msg.getMsg());
            return back;
        }
    }

    // 下载今日步数
    public static TodayStepBean getTodaySportsTemp(String sessionId)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.getTodaySportsTemp(sessionId);
        TodayStepBean todayStepBean = new TodayStepBean();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            Log.i("content", content);
            try {
                JSONArray jsonArray = new JSONObject(content)
                        .getJSONArray("data");
                if (jsonArray != null && jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        int flag = jsonArray.getJSONObject(i).getInt("flag");
                        if (flag != -3) {
                            int id = jsonArray.getJSONObject(i).getInt("id");
                            int uid = jsonArray.getJSONObject(i).getInt("uid");
                            int step_num = jsonArray.getJSONObject(i).getInt(
                                    "step_num");
                            String day = jsonArray.getJSONObject(i).getString(
                                    "day");
                            todayStepBean.setId(id);
                            todayStepBean.setUid(uid);
                            todayStepBean.setStep_num(step_num);
                            todayStepBean.setDay(day);
                        }

                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
                return todayStepBean;
            }
            // if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
            // throw new ApiSessionOutException(sessionOut);
            // } else {
            //
            // }
            return todayStepBean;
        } else {
            // throw new ApiNetException("error:" + msg.getMsg());
            return todayStepBean;
        }
    }

    // 上传心率或者睡眠
    public static ApiBack uploadSleep(String sessionId,
                                      SleepEffect sleepEffect, boolean isSleepOrXlv)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.uploadSleepData(sessionId, sleepEffect,
                isSleepOrXlv);
        ApiBack back = new ApiBack();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            Log.i("content", content);
            String sessionOut = new String();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                int id = jsonObject.getInt("id");
                back.setReg(id);
                int flag = jsonObject.getInt("flag");
                if (flag == 0) {
                    back.setFlag(1);
                    Log.i("flag", back.getFlag() + "---" + flag);
                } else {
                    back.setFlag(-1);
                }
                back.setMsg(jsonObject.getString("msg"));
                sessionOut = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                return back;
            }
        } else {
            // throw new ApiNetException("error:" + msg.getMsg());
            return back;
        }
    }

    // 获取运动记录总数
    public static ApiBack getSportscountNum(String sessionId, String userid)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.getSportscountNum(sessionId, userid);
        ApiBack back = new ApiBack();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            Log.i("content", content);
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                if (jsonObject.has("listcount")) {
                    int listcount = jsonObject.getInt("listcount");
                    back.setReg(listcount);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                return back;
            }
            return back;
        } else {
            // throw new ApiNetException("error:" + msg.getMsg());
            return back;
        }

    }

    // 获取健康睡眠数据 isXinlvOrsleep是true走心率是fasle接口
    public static List<SleepEffect> getSleepDate(String sessionId, int page,
                                                 boolean isXinlvOrsleep) throws ApiNetException {
        ApiMessage msg = Api.getSleepDate(sessionId, page, isXinlvOrsleep);
        SleepEffect sleepEffect = null;
        List<SleepEffect> list = null;
        if (msg.isFlag()) {
            String content = msg.getMsg();
            list = new ArrayList<SleepEffect>();
            Log.v("ApiJson", "content --> " + content);
            try {
                JSONArray jsonArray = new JSONObject(content)
                        .getJSONArray("data");
                if (jsonArray != null && jsonArray.length() > 0) {
                    for (int i = 0, j = jsonArray.length(); i < j; i++) {
                        sleepEffect = new SleepEffect();
                        JSONObject obj = jsonArray.getJSONObject(i);
                        sleepEffect.setUid(obj.getInt("uid") + "");
                        if (obj.has("starttime")) {
                            sleepEffect
                                    .setStarttime(obj.getString("starttime"));
                        }
                        if (obj.has("endtime")) {
                            sleepEffect.setEndtime(obj.getString("endtime"));
                        }
                        if (isXinlvOrsleep) {
                            if (obj.has("sleep_effect")) {
                                sleepEffect.setHart_rate(obj
                                        .getDouble("sleep_effect") + "");
                            }
                        } else {
                            if (obj.has("hart_rate")) {
                                sleepEffect.setHart_rate(obj
                                        .getDouble("hart_rate") + "");
                            }
                        }
                        if (obj.has("unique_id")) {
                            sleepEffect
                                    .setUnique_id(obj.getString("unique_id"));
                        }
                        list.add(sleepEffect);

                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
                return list;
            }
            return list;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // 健康心率的删除
    public static ApiBack deleteXinlv(String sessionid, String unique_id)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.deleteXinlv(sessionid, unique_id);
        ApiBack back = null;
        if (msg.isFlag()) {
            String content = msg.getMsg();
            try {
                back = new ApiBack();
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                int flag = jsonObject.getInt("flag");
                if (flag == 0) {
                    back.setFlag(1);
                }
                back.setMsg(jsonObject.getString("msg"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("deleteXinlv", content);
        }
        return back;
    }

    // 整体删除
    public static ApiBack deleteXinlv(String sessionid) throws ApiNetException,
            ApiSessionOutException {
        ApiMessage msg = Api.deleteXinlv(sessionid);
        ApiBack back = null;
        if (msg.isFlag()) {
            String content = msg.getMsg();
            try {
                back = new ApiBack();
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                int flag = jsonObject.getInt("flag");
                if (flag == 0) {
                    back.setFlag(1);
                }
                back.setMsg(jsonObject.getString("msg"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("deleteXinlv", content);
        }
        return back;
    }

    // 盘点最后一张图片生成接口
    public static ApiBack panDianGetImage(String userid)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.panDianGetImage(userid);
        ApiBack back = null;
        if (msg.isFlag()) {
            back = new ApiBack();
            String content = msg.getMsg();
            Log.i("content", content);
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                if (jsonObject.has("flag")) {
                    int flag = jsonObject.getInt("flag");
                    if (flag == 0) {
                        back.setFlag(1);
                        Log.i("flag", back.getFlag() + "---" + flag);
                    } else {
                        back.setFlag(-1);
                    }
                }
                if (jsonObject.has("imgurl")) {
                    String Imgurl = jsonObject.getString("imgurl");
                    back.setMsg(Imgurl);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                return back;
            }
            return back;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }

    }

    // 健康统计的添加 type_id 1表示睡眠，2表示心率
    public static ApiBack healthdatacount(String sessionid, int type_id,
                                          String click_time, int activity_id) throws ApiNetException,
            ApiSessionOutException {
        ApiMessage msg = Api.healthdatacount(sessionid, type_id, click_time,
                activity_id);
        ApiBack back = null;
        if (msg.isFlag()) {
            back = new ApiBack();
            String content = msg.getMsg();
            Log.i("content", content);
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                if (jsonObject.has("flag")) {
                    int flag = jsonObject.getInt("flag");
                    if (flag == 0) {
                        back.setFlag(1);
                        Log.i("flag", back.getFlag() + "---" + flag);
                    } else {
                        back.setFlag(-1);
                    }
                }
                if (jsonObject.has("id")) {
                    int id = jsonObject.getInt("id");
                    back.setReg(id);
                } else {
                    back = null;
                }

            } catch (JSONException e) {
                e.printStackTrace();
                return back;
            }
            return back;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // 获取每个活动详情的具体内容
    // public static ActivityInfo getActivityDetailInfo_copy(String sessionId,
    // int actionId) throws ApiNetException, ApiSessionOutException {
    // ApiMessage msg = Api.activityPost(sessionId, actionId,
    // ApiConstant.getActivityDetailInfo);
    // ActivityInfo activityDetaiInfo = null;
    // if (msg.isFlag()) {
    // String content = msg.getMsg();
    // Log.i(TAG, "action info=" + content);
    // try {
    // JSONObject obj = new JSONObject(content).getJSONObject("data");
    // activityDetaiInfo = new ActivityInfo();
    // int actionID = Integer.parseInt(obj.getString("id"));
    // activityDetaiInfo.title = obj.getString("title");
    // activityDetaiInfo.biaoti_img = obj.getString("biaoti_img");
    // activityDetaiInfo.jiangli_img = obj.getString("jiangli_img");
    // activityDetaiInfo.activity_time = obj
    // .getString("activity_time");
    // activityDetaiInfo.content = obj.getString("content");
    // activityDetaiInfo.canjia_type = obj.getString("canjia_type");
    // activityDetaiInfo.act_explain = obj.getString("act_explain");
    // activityDetaiInfo.actionId = Integer.parseInt(obj
    // .getString("id"));
    // } catch (JSONException e) {
    // e.printStackTrace();
    // }
    // return activityDetaiInfo;
    // } else {
    // throw new ApiNetException("error:" + msg.getMsg());
    // }
    // }
    // 获取训练计划的列表
    public static List<TrainPlanList> getTrainlist(String sessionId,
                                                   Context context) throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.getTrainlist(sessionId);
        List<TrainPlanList> mList = new ArrayList<TrainPlanList>();
        TrainPlanList trainPlanList = null;
        if (msg.isFlag()) {
            String content = msg.getMsg();
            Log.i("content", content);
            try {
                JSONArray jsonArray = new JSONObject(content)
                        .getJSONArray("data");
                if (jsonArray != null && jsonArray.length() > 0) {
                    // 保存训练首页缓存数据
                    SharedPreferences preferences = context
                            .getSharedPreferences("CacheIndexTrainlist", 0);
                    Editor edit = preferences.edit();
                    edit.putString("CacheTrainlist_info", content);
                    edit.commit();

                    for (int i = 0, j = jsonArray.length(); i < j; i++) {
                        trainPlanList = new TrainPlanList();
                        JSONObject obj = jsonArray.getJSONObject(i);
                        if (obj.has("id")) {
                            trainPlanList.setId(obj.getInt("id"));
                        }
                        if (obj.has("train_name")) {
                            trainPlanList.setTrain_name(obj
                                    .getString("train_name"));
                        }
                        if (obj.has("thumb")) {
                            trainPlanList.setThumb(obj.getString("thumb"));
                        }
                        if (obj.has("grade")) {
                            trainPlanList.setGrade(obj.getInt("grade"));
                        }
                        if (obj.has("position")) {
                            trainPlanList
                                    .setPosition(obj.getString("position"));
                        }
                        if (obj.has("train_time")) {
                            trainPlanList.setTrain_time(obj
                                    .getInt("train_time"));
                        }
                        if (obj.has("train_calorie")) {
                            trainPlanList.setTrain_calorie(obj
                                    .getDouble("train_calorie"));
                        }
                        if (obj.has("traincount")) {
                            trainPlanList.setTraincount(obj
                                    .getInt("traincount"));
                        }
                        mList.add(trainPlanList);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
                return mList;
            }
            return mList;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }

    }

    // 获取训练计划的列表
    public static TrainInfo getTraininfo(String sessionid, int id,
                                         Context context) throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.getTraininfo(sessionid, id);
        TrainInfo trainInfo = null;
        if (msg.isFlag()) {
            String content = msg.getMsg();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                if (jsonObject != null && !"".equals(jsonObject)) {
                    // 保存训练首页缓存数据
                    SharedPreferences preferences = context
                            .getSharedPreferences("CacheTrainInfo", 0);
                    Editor edit = preferences.edit();
                    edit.putString(id + "CacheTrainInfo_info", content);
                    edit.commit();

                    trainInfo = new TrainInfo();
                    if (jsonObject.has("id")) {
                        trainInfo.setId(jsonObject.getInt("id"));
                    }
                    if (jsonObject.has("train_name")) {
                        trainInfo.setTrain_name(jsonObject
                                .getString("train_name"));
                    }
                    if (jsonObject.has("thumb")) {
                        trainInfo.setThumb(jsonObject.getString("thumb"));
                    }
                    if (jsonObject.has("grade")) {
                        trainInfo.setGrade(jsonObject.getInt("grade"));
                    }
                    if (jsonObject.has("position")) {
                        trainInfo.setPosition(jsonObject.getString("position"));
                    }
                    if (jsonObject.has("train_time")) {
                        trainInfo
                                .setTrain_time(jsonObject.getInt("train_time"));
                    }
                    if (jsonObject.has("train_calorie")) {
                        trainInfo.setTrain_calorie(jsonObject
                                .getDouble("train_calorie"));
                    }
                    if (jsonObject.has("train_fileurl")) {
                        trainInfo.setTrain_fileurl(jsonObject
                                .getString("train_fileurl"));
                    }
                    if (jsonObject.has("action_num")) {
                        trainInfo
                                .setAction_num(jsonObject.getInt("action_num"));
                    }
                    if (jsonObject.has("traincount")) {
                        trainInfo
                                .setTraincount(jsonObject.getInt("traincount"));
                    }
                    if (jsonObject.has("userlist")) {

                        String str = jsonObject.getString("userlist");
                        if (str != null && !"null".equals(str)) {
                            JSONArray jsonArray = jsonObject
                                    .getJSONArray("userlist");
                            if (jsonArray != null && jsonArray.length() > 0) {
                                ArrayList<TrainUserInfo> tUserList = new ArrayList<TrainUserInfo>();
                                TrainUserInfo trainUserInfo = null;
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    trainUserInfo = new TrainUserInfo();
                                    if (obj.has("name")) {
                                        trainUserInfo.setName(obj
                                                .getString("name"));
                                    }
                                    if (obj.has("img")) {
                                        trainUserInfo.setImg(obj
                                                .getString("img"));
                                    }
                                    if (obj.has("id")) {
                                        trainUserInfo.setId(obj.getInt("id"));
                                    }
                                    if (obj.has("uid")) {
                                        trainUserInfo.setUid(obj.getInt("uid"));
                                    }
                                    if (obj.has("train_starttime")) {
                                        trainUserInfo.setTrain_starttime(obj
                                                .getString("train_starttime"));
                                    }
                                    if (obj.has("train_endtime")) {
                                        trainUserInfo.setTrain_endtime(obj
                                                .getString("train_endtime"));
                                    }
                                    if (obj.has("train_calorie")) {
                                        trainUserInfo.setTrain_calorie(obj
                                                .getDouble("train_calorie"));
                                    }
                                    tUserList.add(trainUserInfo);
                                }
                                trainInfo.settUserList(tUserList);
                            }
                        }

                    }
                    if (jsonObject.has("actionlist")) {
                        JSONArray jsonArray = jsonObject
                                .getJSONArray("actionlist");
                        if (jsonArray != null && jsonArray.length() > 0) {
                            ArrayList<TrainAction> actionlist = new ArrayList<TrainAction>();
                            TrainAction trainAction;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                trainAction = new TrainAction();
                                if (obj.has("id")) {
                                    trainAction.setId(obj.getInt("id"));
                                }
                                if (obj.has("action_name")) {
                                    trainAction.setName(obj
                                            .getString("action_name"));
                                }
                                if (obj.has("thumb")) {
                                    trainAction
                                            .setThumb(obj.getString("thumb"));
                                }
                                if (obj.has("listorder")) {
                                    trainAction.setListorder(obj
                                            .getInt("listorder"));
                                }
                                if (obj.has("resttime")) {
                                    trainAction.setResttime(obj
                                            .getInt("resttime"));
                                }
                                if (obj.has("actnum")) {
                                    trainAction.setActnum(obj.getInt("actnum"));
                                }
                                if (obj.has("videotime")) {
                                    trainAction.setVideotime(obj
                                            .getInt("videotime"));
                                }
                                actionlist.add(trainAction);
                            }

                            trainInfo.setActionlist(actionlist);
                        }

                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
                // return trainInfo;
            }
            return trainInfo;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }

    }

    public static ApiMessage getTrainActionInfo(String sessionId,
                                                int trainActionId) {
        return Api.getTrainActionInfo(sessionId, trainActionId);
    }

    public static ApiMessage getTrainTaskList(String sessionId, int page) {
        return Api.getTrainTaskList(sessionId, page);
    }

    public static ApiMessage getTotalTrainTask(String sessionId) {
        return Api.getTotalTrainTask(sessionId);
    }

    public static ApiMessage addTrainRecord(String sessionId, int train_id,
                                            int traintime, double train_calorie, String train_action,
                                            String train_position, int train_completion,
                                            String train_starttime, String train_endtime, int is_total,
                                            String unique_id) {
        return Api.addTrainRecord(sessionId, train_id, traintime,
                train_calorie, train_action, train_position, train_completion,
                train_starttime, train_endtime, is_total, unique_id);
    }

    public static ApiMessage GetCurrentDayBuShu(String sessionId, String day) {
        return Api.GetCurrentDayBuShu(sessionId, day);
    }

    public static ApiMessage GetAllDayBuShu(String sessionId, int page) {
        return Api.GetAllDayBuShu(sessionId, page);
    }

    public static ApiMessage saveBuShuTongJiToNetWork(String sessionId,
                                                      int step_num, String day, String appName) {
        return Api.saveBuShuTongJiToNetWork(sessionId, step_num, day, appName);
    }

    public static ApiMessage saveBuShuTongJiListToNetWork(String sessionId, ArrayList<BuShuTongJiDetail> list,
                                                           String datasource, String application_name) {
        return Api.saveBuShuTongJiListToNetWork(sessionId, list, datasource, application_name);
    }

    public static ApiMessage GetShareData(int id) {
        return Api.GetShareData(id);
    }

    // 外部活动最新的提取 typeid 1是弹窗2个广场轮播图
    public static ExternalActivi getExternalActive(String sessionid,
                                                   int typeid, String channelnum) throws ApiNetException,
            ApiSessionOutException {
        ApiMessage msg = Api.getExternalActive(sessionid, typeid, channelnum);
        ExternalActivi back = null;
        if (msg.isFlag()) {
            back = new ExternalActivi();
            String content = msg.getMsg();
            Log.i("content", content);
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                if (jsonObject.has("id")) {
                    back.setId(jsonObject.getInt("id"));
                }
                if (jsonObject.has("title")) {
                    back.setTitle(jsonObject.getString("title"));
                }
                if (jsonObject.has("thumb")) {
                    back.setThumb(jsonObject.getString("thumb"));
                }
                if (jsonObject.has("start_time")) {
                    back.setStart_time(jsonObject.getString("start_time"));
                }
                if (jsonObject.has("end_time")) {
                    back.setEnd_time(jsonObject.getString("end_time"));
                }
                if (jsonObject.has("url")) {
                    back.setUrl(jsonObject.getString("url"));
                }
                if (jsonObject.has("price")) {
                    back.setPrice(jsonObject.getInt("price"));
                }

                if (jsonObject.has("href_id")) {
                    back.setHref_id(jsonObject.getInt("href_id"));
                }
                if (jsonObject.has("web_id")) {
                    back.setWeb_id(jsonObject.getInt("web_id"));
                }

//                if (jsonObject.has("start_hour")) {
//                    back.setStart_hour(jsonObject.getInt("start_hour"));
//                }
//
//                if (jsonObject.has("end_hour")) {
//                    back.setEnd_hour(jsonObject.getInt("end_hour"));
//                }

                if (jsonObject.has("frequency")) {
                    back.setFrequency(jsonObject.getInt("frequency"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
                return back;
            }
            return back;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // // 获取微信统一下单签名
    public static WXxdback gettongyiInfo(String appid, String body,
                                         String mch_id, String nonce_str, String notify_url,
                                         String out_trade_no, String spbill_create_ip, int total_fee,
                                         String trade_type) throws ApiNetException {
        ApiMessage api = Api.getWXxiadanlist(appid, body, mch_id, nonce_str,
                notify_url, out_trade_no, spbill_create_ip, total_fee,
                trade_type);
        WXxdback wb = null;
        if (api.isFlag()) {
            String content = api.getMsg();
            Log.e("YePao", content);
            try {
                JSONObject json = new JSONObject(content).getJSONObject("data");
                if (json != null) {
                    wb = new WXxdback();
                    if (json.has("flag")) {
                        wb.setFlag(json.getInt("flag"));
                    }
                    if (json.has("msg")) {
                        wb.setMsg(json.getString("msg"));
                    }
                    if (json.has("sign")) {
                        wb.setSign(json.getString("sign"));
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return wb;
            }
            return wb;
        } else {
            throw new ApiNetException("error:" + api.getMsg());
        }

    }

    // 获取统一下单预支付id
    public static WXxdback getPayid(String data, int flg)
            throws ApiNetException {
        ApiMessage api = Api.getPaysing(data, flg);
        WXxdback wb = null;
        if (api.isFlag()) {
            String content = api.getMsg();
            Log.e("Payid", content);
            try {
                JSONObject json = new JSONObject(content).getJSONObject("data");
                if (json != null) {
                    wb = new WXxdback();
                    if (json.has("appid")) {
                        wb.setAppid(json.getString("appid"));
                    }
                    if (json.has("partnerid")) {
                        wb.setPartnerid(json.getString("partnerid"));
                    }
                    if (json.has("package")) {
                        wb.setPackageVaue(json.getString("package"));
                    }
                    if (json.has("prepayid")) {
                        wb.setPrepayid(json.getString("prepayid"));
                    }
                    if (json.has("noncestr")) {
                        wb.setNoncestr(json.getString("noncestr"));
                    }
                    if (json.has("timestamp")) {
                        wb.setTimestamp(json.getString("timestamp"));
                    }
                    if (json.has("sign")) {
                        wb.setSign(json.getString("sign"));
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return wb;
            }
            return wb;
        } else {
            throw new ApiNetException("error:" + api.getMsg());
        }

    }

    // 夜跑活动用户信息
    public static YePaoInfo getYePaoInfo(String sessionid, int id, int flg)
            throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.getYePao(sessionid, id, flg);
        YePaoInfo back = null;
        if (msg.isFlag()) {
            back = new YePaoInfo();
            String content = msg.getMsg();
            Log.i("content", content);
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                if (jsonObject.has("id")) {
                    back.setId(jsonObject.getInt("id"));
                }
                if (jsonObject.has("order_nu")) {
                    back.setOrder_nu(jsonObject.getString("order_nu"));
                }
                if (jsonObject.has("uid")) {
                    back.setUid(jsonObject.getInt("uid"));
                }
                if (jsonObject.has("mlsid")) {
                    back.setMlsid(jsonObject.getInt("mlsid"));
                }
                if (jsonObject.has("mls_name")) {
                    back.setMls_name(jsonObject.getString("mls_name"));
                }
                if (jsonObject.has("mls_price")) {
                    back.setMls_price(jsonObject.getInt("mls_price"));
                }
                if (jsonObject.has("user_name")) {
                    back.setUser_name(jsonObject.getString("user_name"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
                return back;
            }
            return back;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // 夜跑订单详情
    public static YeDindanInfo getDianDanInfo(String sessionid, String id,
                                              int flg) throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.getDindanId(sessionid, id, flg);
        YeDindanInfo back = null;
        if (msg.isFlag()) {
            back = new YeDindanInfo();
            String content = msg.getMsg();
            Log.e("GetData", content);
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                if (jsonObject.has("id")) {
                    back.setId(jsonObject.getInt("id"));
                }
                if (jsonObject.has("order_nu")) {
                    back.setOrder_nu(jsonObject.getString("order_nu"));
                }
                if (jsonObject.has("uid")) {
                    back.setUid(jsonObject.getInt("uid"));
                }
                if (jsonObject.has("mlsid")) {
                    back.setMlsid(jsonObject.getInt("mlsid"));
                }
                if (jsonObject.has("mls_name")) {
                    back.setMls_name(jsonObject.getString("mls_name"));
                }
                if (jsonObject.has("mls_price")) {
                    back.setMls_price(jsonObject.getInt("mls_price"));
                }
                if (jsonObject.has("create_time")) {
                    back.setCreate_time(jsonObject.getString("create_time"));
                }
                if (jsonObject.has("user_name")) {
                    back.setUser_name(jsonObject.getString("user_name"));
                }
                if (jsonObject.has("pay_status")) {
                    back.setPay_status(jsonObject.getInt("pay_status"));
                }
                if (jsonObject.has("transaction_id")) {
                    back.setTransaction_id(jsonObject
                            .getString("transaction_id"));
                }
                if (jsonObject.has("openid")) {
                    back.setOpened(jsonObject.getString("openid"));
                }
                if (jsonObject.has("pay_money")) {
                    back.setPay_money(jsonObject.getInt("pay_money"));
                }
                if (jsonObject.has("pay_time")) {
                    back.setPay_time(jsonObject.getString("pay_time"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
                return back;
            }
            return back;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // 获取商品信息
    public static WXPaylist getGoodsinfo(String sessionid, int gid)
            throws ApiNetException, ApiSessionOutException {
        Log.e("Flg", "打印 值sessionid:" + sessionid + "商品id:" + gid + "");
        ApiMessage apiMessage = Api.getGoodsInfo(sessionid, gid);
        WXPaylist wxPaylist = null;
        if (apiMessage.isFlag()) {
            String con = apiMessage.getMsg();
            Log.e("Conn", con);
            try {
                JSONObject obj = new JSONObject(con).getJSONObject("data");
                if (obj != null) {
                    wxPaylist = new WXPaylist();
                    if (obj.has("id")) {
                        wxPaylist.setId(obj.getInt("id"));
                    }
                    if (obj.has("goods_price")) {
                        wxPaylist.setGoods_price(obj.getInt("goods_price"));
                    }
                    if (obj.has("goods_xjprice")) {
                        wxPaylist.setGoods_xjprice(obj.getInt("goods_xjprice"));
                    }

                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return wxPaylist;
            }
            return wxPaylist;
        } else {
            throw new ApiNetException("error:" + apiMessage.getMsg());
        }

    }

    // 金币商城下单
    public static WXPaylist getGoodspayinfo(String sessionid, int gid, int flg)
            throws ApiNetException, ApiSessionOutException {
        Log.e("Flg", "打印" + flg + "值,sessionid:" + sessionid + "商品id:" + gid
                + "");
        ApiMessage apiMessage = Api.getYePao(sessionid, gid, flg);
        WXPaylist wxPaylist = null;
        if (apiMessage.isFlag()) {
            String con = apiMessage.getMsg();
            Log.e("GoodsInfo", con);
            wxPaylist = new WXPaylist();
            try {
                JSONObject obj = new JSONObject(con).getJSONObject("data");
                if (obj != null) {
                    if (obj.has("id")) {
                        wxPaylist.setId(obj.getInt("id"));
                    }
                    if (obj.has("uid")) {
                        wxPaylist.setUid(obj.getInt("uid"));
                    }
                    if (obj.has("gid")) {
                        wxPaylist.setGid(obj.getInt("gid"));
                    }
                    if (obj.has("order_nu")) {
                        wxPaylist.setOrder_nu(obj.getString("order_nu"));
                    }
                    if (obj.has("goods_name")) {
                        wxPaylist.setGoods_name(obj.getString("goods_name"));
                    }
                    if (obj.has("goods_price")) {
                        wxPaylist.setGoods_price(obj.getInt("goods_price"));
                    }
                    if (obj.has("goods_xjprice")) {
                        wxPaylist.setGoods_xjprice(obj.getInt("goods_xjprice"));
                    }
                    if (obj.has("goods_num")) {
                        wxPaylist.setGoods_num(obj.getInt("goods_num"));
                    }
                    if (obj.has("user_name")) {
                        wxPaylist.setUser_name(obj.getString("user_name"));
                    }

                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return wxPaylist;
            }
            return wxPaylist;
        } else {
            throw new ApiNetException("error:" + apiMessage.getMsg());
        }

    }

    public static int getservertime() {
        ApiMessage msg = Api.getServertime();
        int time = 0;
        if (msg.isFlag()) {
            String content = msg.getMsg();
            try {
                JSONObject jsonObject = new JSONObject(content);
                time = jsonObject.getInt("data");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return time;
    }

    // 金币商城支付订单详情
    public static ShopPayDingDan getShopDingDanInfo(String sessionid,
                                                    String id, int flg) throws ApiNetException, ApiSessionOutException {
        ApiMessage msg = Api.getDindanId(sessionid, id, flg);
        ShopPayDingDan back = null;
        if (msg.isFlag()) {
            back = new ShopPayDingDan();
            String content = msg.getMsg();
            Log.e("GetData", content);
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                if (jsonObject.has("id")) {
                    back.setId(jsonObject.getInt("id"));
                }
                if (jsonObject.has("order_nu")) {
                    back.setOrder_nu(jsonObject.getString("order_nu"));
                }
                if (jsonObject.has("uid")) {
                    back.setUid(jsonObject.getInt("uid"));
                }
                if (jsonObject.has("gid")) {
                    back.setGid(jsonObject.getInt("gid"));
                }
                if (jsonObject.has("goods_name")) {
                    back.setGoods_name(jsonObject.getString("goods_name"));
                }
                if (jsonObject.has("goods_xjprice")) {
                    back.setGoods_xjprice(jsonObject.getInt("goods_xjprice"));
                }
                if (jsonObject.has("create_time")) {
                    back.setCreate_time(jsonObject.getString("create_time"));
                }
                if (jsonObject.has("user_name")) {
                    back.setUser_name(jsonObject.getString("user_name"));
                }
                if (jsonObject.has("pay_status")) {
                    back.setPay_status(jsonObject.getInt("pay_status"));
                }
                if (jsonObject.has("transaction_id")) {
                    back.setTransaction_id(jsonObject
                            .getString("transaction_id"));
                }
                if (jsonObject.has("openid")) {
                    back.setOpenid(jsonObject.getString("openid"));
                }
                if (jsonObject.has("pay_money")) {
                    back.setPay_money(jsonObject.getInt("pay_money"));
                }
                if (jsonObject.has("pay_time")) {
                    back.setPay_time(jsonObject.getString("pay_time"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
                return back;
            }
            return back;
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
    }

    // 推荐 banner活动
    public static List<ExternalActivi> getExternalActives(String sessionid,
                                                          int typeid, String channelnum) throws ApiNetException {

        ApiMessage msg = Api.getExternalActive(sessionid, typeid, channelnum);
        ExternalActivi externalActivis = null;
        List<ExternalActivi> mList = new ArrayList<ExternalActivi>();
        if (msg.isFlag()) {
            String data = msg.getMsg();
            try {
                JSONArray jsonArray = new JSONObject(data).getJSONArray("data");

                SharedPreferences preferences = SportsApp.getInstance()
                        .getSharedPreferences("ExternalActiviList", 0);
                Editor edit = preferences.edit();
                edit.putString("ExternalActiviList_info", data);
                edit.commit();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);

                    externalActivis = new ExternalActivi();
                    if (obj.has("id")) {
                        externalActivis.setId(obj.getInt("id"));
                    }
                    if (obj.has("title")) {
                        externalActivis.setTitle(obj.getString("title"));
                    }
                    if (obj.has("thumb")) {
                        externalActivis.setThumb(obj.getString("thumb"));
                    }
                    if (obj.has("start_time")) {
                        externalActivis.setStart_time(obj
                                .getString("start_time"));
                    }
                    if (obj.has("end_time")) {
                        externalActivis.setEnd_time(obj.getString("end_time"));
                    }
                    if (obj.has("url")) {
                        externalActivis.setUrl(obj.getString("url"));
                    }
                    if (obj.has("price")) {
                        externalActivis.setPrice(obj.getInt("price"));
                    }
                    if (obj.has("href_id")) {
                        externalActivis.setHref_id(obj.getInt("href_id"));
                    }
                    if (obj.has("web_id")) {
                        externalActivis.setWeb_id(obj.getInt("web_id"));
                    }
                    mList.add(externalActivis);
                }
                // mList.add(externalActivis);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            throw new ApiNetException("error:" + msg.getMsg());
        }
        return mList;
    }

    // 广场列表信息
    public static List<SquareInfo> getSquareInfo(String sessionid,
                                                 String channelnum) {
        ApiMessage msg = Api.getActInfo(sessionid, channelnum);
        List<SquareInfo> mSquareInfo = new ArrayList<SquareInfo>();
        SquareInfo squareInfo = null;
        if (msg.isFlag()) {
            String content = msg.getMsg();
            Log.i("SquareInfo", content);
            try {
                JSONArray jsonArray = new JSONObject(content)
                        .getJSONArray("data");

                if (jsonArray != null && jsonArray.length() > 0) {
                    // 缓存
                    SharedPreferences preferences = SportsApp.getInstance()
                            .getSharedPreferences("SquareInfoList", 0);
                    Editor edit = preferences.edit();
                    edit.putString("SquareInfoList_info", content);
                    edit.commit();
                    Log.i("SquareInfoList", content);

                    for (int i = 0, j = jsonArray.length(); i < j; i++) {
                        squareInfo = new SquareInfo();
                        JSONObject obj = jsonArray.getJSONObject(i);
                        if (obj.has("id")) {
                            squareInfo.setId(obj.getInt("id"));
                        }
                        if (obj.has("cat_name")) {
                            squareInfo.setCat_name(obj.getString("cat_name"));
                        }
                        if (obj.has("typeid")) {
                            squareInfo.setTypeid(obj.getInt("typeid"));
                        }
                        if (obj.has("cat_order")) {
                            squareInfo.setCat_order(obj.getInt("cat_order"));
                        }
                        if (obj.has("url")) {
                            squareInfo.setUrl(obj.getString("url"));
                        }

                        if (obj.has("actnum")) {
                            squareInfo.setActnum(obj.getInt("actnum"));
                            if (squareInfo.getActnum() != 0) {
                                ArrayList<ActionInfos> actinfoList = null;
                                if (obj.has("actinfo")) {
                                    actinfoList = new ArrayList<ActionInfos>();
                                    JSONArray infoJsonArray = obj
                                            .getJSONArray("actinfo");
                                    Log.i("getActinfoList",
                                            infoJsonArray.toString());
                                    for (int k = 0; k < infoJsonArray.length(); k++) {
                                        ActionInfos actionInfos = new ActionInfos();
                                        JSONObject infoObj = infoJsonArray
                                                .getJSONObject(k);
                                        if (infoObj.has("id")) {
                                            actionInfos.setId(infoObj
                                                    .getInt("id"));
                                        }
                                        if (infoObj.has("title")) {
                                            actionInfos.setTitle(infoObj
                                                    .getString("title"));
                                        }
                                        if (infoObj.has("description")) {
                                            actionInfos.setDescription(infoObj
                                                    .getString("description"));
                                        }
                                        if (infoObj.has("start_time")) {
                                            actionInfos.setStart_time(infoObj
                                                    .getString("start_time"));
                                        }
                                        if (infoObj.has("end_time")) {
                                            actionInfos.setEnd_time(infoObj
                                                    .getString("end_time"));
                                        }
                                        if (infoObj.has("inputtime")) {
                                            actionInfos.setInputtime(infoObj
                                                    .getString("inputtime"));
                                        }
                                        if (infoObj.has("listorder")) {
                                            actionInfos.setListorder(infoObj
                                                    .getInt("listorder"));
                                        }
                                        if (infoObj.has("content")) {
                                            actionInfos.setContent(infoObj
                                                    .getString("content"));
                                        }
                                        if (infoObj.has("web_id")) {
                                            actionInfos.setWeb_id(infoObj
                                                    .getInt("web_id"));
                                        }
                                        if (infoObj.has("thumb")) {
                                            actionInfos.setThumb(infoObj
                                                    .getString("thumb"));
                                        }
                                        if (infoObj.has("url")) {
                                            actionInfos.setUrl(infoObj
                                                    .getString("url"));
                                        }

                                        if (infoObj.has("like_num")) {
                                            actionInfos.setLike_num(infoObj
                                                    .getInt("like_num"));
                                        }
                                        if (infoObj.has("comment_num")) {
                                            actionInfos.setComment_num(infoObj
                                                    .getInt("comment_num"));
                                        }

                                        actinfoList.add(actionInfos);
                                    }
                                    squareInfo.setActinfoList(actinfoList);
                                }
                            }
                        }
                        mSquareInfo.add(squareInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return mSquareInfo;
            }
        }
        return mSquareInfo;
    }

    // 广场列表信息2
    public static List<ActListInfo> getActListInfos(String sessionid,
                                                    String channelnum, int catid, int page) {
        ApiMessage msg = Api.getActInfo(sessionid, channelnum, catid, page);
        List<ActListInfo> mActListInfo = new ArrayList<ActListInfo>();
        ActListInfo mActListInfos = null;
        if (msg.isFlag()) {
            String content = msg.getMsg();
            Log.i("Doubi", content);
            try {
                JSONArray jsonArray = new JSONObject(content)
                        .getJSONArray("data");
                if (jsonArray != null && jsonArray.length() > 0) {

                    for (int i = 0, j = jsonArray.length(); i < j; i++) {
                        mActListInfos = new ActListInfo();
                        JSONObject obj = jsonArray.getJSONObject(i);
                        if (obj.has("id")) {
                            mActListInfos.setId(obj.getInt("id"));
                        }
                        if (obj.has("title")) {
                            mActListInfos.setTitle(obj.getString("title"));
                        }
                        if (obj.has("thumb")) {
                            mActListInfos.setThumb(obj.getString("thumb"));
                        }
                        if (obj.has("thumbgengduo")) {
                            mActListInfos.setThumbgengduo(obj
                                    .getString("thumbgengduo"));
                        }
                        if (obj.has("description")) {
                            mActListInfos.setDescription(obj
                                    .getString("description"));
                        }
                        if (obj.has("url")) {
                            mActListInfos.setUrl(obj.getString("url"));
                        }
                        if (obj.has("start_time")) {
                            mActListInfos.setStart_time(obj
                                    .getString("start_time"));
                        }
                        if (obj.has("end_time")) {
                            mActListInfos
                                    .setEnd_time(obj.getString("end_time"));
                        }
                        if (obj.has("inputtime")) {
                            mActListInfos.setInputtime(obj
                                    .getString("inputtime"));
                        }
                        if (obj.has("listorder")) {
                            mActListInfos.setListorder(obj.getInt("listorder"));
                        }
                        if (obj.has("content")) {
                            mActListInfos.setContent(obj.getString("content"));
                        }
                        mActListInfo.add(mActListInfos);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return mActListInfo;
            }
        }
        return mActListInfo;
    }

    public static String getShareIcon(int uid, int id) {
        ApiMessage msg = Api.getShareIcon(uid, id);
        Log.i("getShareIcon", msg.getMsg());
        String imgurl = null;
        if (msg.isFlag()) {
            String content = msg.getMsg();
            try {
                JSONObject jsonObject = new JSONObject(content)
                        .getJSONObject("data");
                ;
                imgurl = jsonObject.getString("imgurl");
                Log.i("getShareIcon", imgurl);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return imgurl;
    }

    public static ActListInfo getInfoTitle(String sid, String id) {
        ApiMessage msg = Api.getTitle(sid, id);
        ActListInfo mActListInfos = null;
        if (msg.isFlag()) {
            mActListInfos = new ActListInfo();
            String content = msg.getMsg();
            Log.e("AAA", content);
            try {
                JSONObject obj = new JSONObject(content).getJSONObject("data");

                mActListInfos = new ActListInfo();
                if (obj.has("title")) {
                    mActListInfos.setTitle(obj.getString("title"));
                }
                if (obj.has("thumb")) {
                    mActListInfos.setThumb(obj.getString("thumb"));
                }
                if (obj.has("thumbgengduo")) {
                    mActListInfos
                            .setThumbgengduo(obj.getString("thumbgengduo"));
                }
                if (obj.has("thumblist")) {
                    mActListInfos.setThumblist(obj.getString("thumblist"));
                }
                if (obj.has("description")) {
                    mActListInfos.setDescription(obj.getString("description"));
                }

                Log.e("title", mActListInfos.getTitle());
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return mActListInfos;
    }

    //获取配速的数据
    public static PeisuInfo getPeisu(String sessionid, int sports_id, int typeid) {
        ApiMessage api = Api.getPeisu(sessionid, sports_id);
        PeisuInfo peisuInfo = null;

        int flag;
        if (api.isFlag()) {
            String content = api.getMsg();
            try {
                if(content!=null&&!"".equals(content)){
                    peisuInfo = new PeisuInfo();
                    JSONObject obj = new JSONObject(content).getJSONObject("data");
                    if (obj.has("flag")) {
                        flag = obj.getInt("flag");
                        if (flag == 0) {
                            if (obj.has("averagevalue")) {
                                peisuInfo.setSprots_svgvelocity(obj.getDouble("averagevalue"));
                            }
                            if (obj.has("maxvalue")) {
                                peisuInfo.setSports_minvelocity(obj.getDouble("maxvalue"));
                            }
                            if (obj.has("id")) {
                                peisuInfo.setPeisuid(obj.getInt("id"));
                            }
                            if (obj.has("distance")) {
                                peisuInfo.setSport_distance(obj.getInt("distance"));
                            }
                            List<GetPeisu> listpeis = new ArrayList<GetPeisu>();
                            GetPeisu peisu;
                            if(obj.has("data")){
                                JSONArray array = obj.getJSONArray("data");
                                if(array!=null){
                                    for (int i = 0; i < array.length(); i++) {
                                        peisu = new GetPeisu();
                                        JSONObject jsonObject = array.getJSONObject(i);
                                        if(jsonObject.has("sport_distance")){
                                            peisu.setSport_distance(jsonObject.getString("sport_distance"));
                                        }
                                        if (typeid == SportsType.TYPE_CLIMBING) {
                                            if(jsonObject.has("sprots_height")){
                                                peisu.setSprots_velocity(jsonObject.getString("sprots_height"));
                                            }
                                            if(jsonObject.has("GPS_type")){
                                                String type=jsonObject.getString("GPS_type");
                                                if(type==null||"".equals(type)){
                                                    type="1";
                                                }
                                                peisu.setgPS_type(type);
                                                type=null;
                                            }else{
                                                peisu.setgPS_type("1");
                                            }

                                        } else {
                                            if(jsonObject.has("sprots_velocity")){
                                                peisu.setSprots_velocity(jsonObject.getString("sprots_velocity"));
                                            }
                                        }
                                        if(jsonObject.has("sprots_time")){
                                            peisu.setSprots_time(jsonObject.getString("sprots_time"));
                                        }
                                        listpeis.add(peisu);
                                    }
                                }
                            }
                            peisuInfo.setListpeis(listpeis);
                        } else if (flag == -3) {
                            peisuInfo.setIshave(flag);
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return peisuInfo;
    }




    // 上传运动记录和配速的总接口
    public static ApiBack uploadSportsTwoInfo(int Limt, String sessionId,
                                          int Sports_type_task, int sports_swim_type,
                                          int monitoring_equipment, String start_time, int sprts_time,
                                          double sport_distance, double sprots_Calorie,
                                          double sprots_velocity ,double Heart_rate,
                                          String longitude_latitude, int step_num, int maptype,
                                          String serial, String app_id,String velocity_list,String coordinate_list,String application_name, PeisuInfo array,int datatype) throws ApiNetException,
            ApiSessionOutException {
        ApiMessage msg = Api.uploadSportsTwoInfo(Limt, sessionId, Sports_type_task,
                sports_swim_type, monitoring_equipment, start_time, sprts_time,
                sport_distance, sprots_Calorie, sprots_velocity, Heart_rate,
                longitude_latitude, step_num, maptype, serial, app_id,velocity_list,coordinate_list,application_name, array,datatype);
        ApiBack back = new ApiBack();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            if(content!=null&&!"".equals(content)){
                if (content.contains(SportsApp.getInstance().getResources().getString(R.string.user_system))){
                    back.setFlag(-100);
                    back.setMsg("checkyour_WIFI_isconnect");
                    return back;
                }else{
                    try {
                        JSONObject jsonObjects=new JSONObject(content);
                        if(jsonObjects.has("data")){
                            JSONObject jsonObject = new JSONObject(content)
                                    .getJSONObject("data");
                            int flag=-1;//flag==0时表示成功，-1表示失败
                            if(jsonObject.has("flag")){
                                flag = jsonObject.getInt("flag");
                            }
                            if (flag == 0) {
                                back.setFlag(1);
                                if(jsonObject.has("taskid")){
                                    int taskID = jsonObject.getInt("taskid");
                                    if (taskID >= 0) {
                                        back.setReg(taskID);
                                    }
                                }
                            }else{
                                back.setFlag(flag);
                                if(flag==1010){
                                    if(jsonObject.has("taskid")){
                                        int taskID = jsonObject.getInt("taskid");
                                        if (taskID >= 0) {
                                            back.setReg(taskID);
                                        }
                                    }
                                }
                            }
                            if(jsonObject.has("grouptype")){
                                back.setGrouptype(jsonObject.getString("grouptype"));
                            }

                            if(jsonObject.has("msg")){
                                back.setMsg(jsonObject.getString("msg"));
                                sessionOut = jsonObject.getString("msg");
                            }
                        }else{
                            throw new ApiNetException(SportsApp.getInstance().getResources().getString(R.string.sports_get_list_failed2));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        return back;
                    }
                }
            }else{
                throw new ApiNetException(SportsApp.getInstance().getResources().getString(R.string.sports_get_list_failed2));
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                return back;
            }
        } else {
            if (msg != null && ("SocketTimeoutException").equals(msg.getMsg())){
                String content = msg.getMsg();
                back.setFlag(-56);
                back.setMsg(content);
                return back;
            }else{
                throw new ApiNetException(SportsApp.getInstance().getResources().getString(R.string.sports_get_list_failed2));
            }
        }
    }





    // 上传奖励和运动秀合并接口
    public static ApiBack uploadShowAndRewardInfo(String sessionId, int coinStatus,
                                                  int coins, int log,String dakaContent) throws ApiNetException,
            ApiSessionOutException {
        ApiMessage msg = Api.uploadShowAndRewardInfo(sessionId,coinStatus,coins,log,dakaContent);
        ApiBack back = new ApiBack();
        if (msg.isFlag()) {
            String content = msg.getMsg();
            String sessionOut = new String();
            if(content!=null&&!"".equals(content)){
                try {
                    JSONObject jsonObjects=new JSONObject(content);
                    if(jsonObjects.has("data")){
                        JSONObject jsonObject = new JSONObject(content)
                                .getJSONObject("data");
                        int flag=-1;//flag==0时表示成功，-1表示失败
                        if(jsonObject.has("flag")){
                            flag = jsonObject.getInt("flag");
                        }
                        back.setFlag(flag);
                        if(jsonObject.has("msg")){
                            back.setMsg(jsonObject.getString("msg"));
                            sessionOut = jsonObject.getString("msg");
                        }
                        if(jsonObject.has("find_id")){
                            back.setReg(jsonObject.getInt("find_id"));
                        }
                    }else{
                        throw new ApiNetException(SportsApp.getInstance().getResources().getString(R.string.sports_get_list_failed2));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    return back;
                }
            }else{
                throw new ApiNetException(SportsApp.getInstance().getResources().getString(R.string.sports_get_list_failed2));
            }
            if (ApiConstant.SESSION_OUT.equals(sessionOut)) {
                throw new ApiSessionOutException(sessionOut);
            } else {
                return back;
            }
        } else {
            throw new ApiNetException(SportsApp.getInstance().getResources().getString(R.string.sports_get_list_failed2));
        }
    }

    public static MadelInfo getMedalInfo(String sid, String id) {
        ApiMessage msg = Api.getMedalInfo(sid, id);
        MadelInfo madelInfo = null;
        if (msg.isFlag()) {
            madelInfo = new MadelInfo();
            String content = msg.getMsg();
            try {
                JSONObject obj = new JSONObject(content).getJSONObject("data");
                if (obj.has("medal_name")) {
                    madelInfo.setMedal_name(obj.getString("medal_name"));
                }
                if (obj.has("medal_pic")) {
                    madelInfo.setMedal_pic(obj.getString("medal_pic"));
                }
                if (obj.has("id")) {
                    madelInfo.setId(obj.getInt("id"));
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return madelInfo;
    }
}