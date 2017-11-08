package com.fox.exercise.api;

import android.os.Handler;

import cn.ingenic.indroidsync.SportsApp;

public class AddCoinsThread extends Thread {

    private int coins;
    private Handler handler;
    private int actionStyle;//是哪一种金币奖励：1登陆奖励，2随机奖励，3按每公里奖励，4分享奖励，5 渠道活动奖励，6 应用活动奖励
    private int sportsType; //运动类型大类：0 走路;1 跑步;2 游泳;3 登山;4 高尔夫;5 竞走;6 骑行;7 网球;8 羽毛球; 9 足球;10 乒乓球;11 划船;12 溜冰;13 轮滑
    private int flag=0;//0表示走默认的上传金币的方法  1表示走和运动秀一起上传的方法
    private String findContent=null;//表示发表运动秀的内容

    public AddCoinsThread(int coins, int actionStyle, Handler handler, int sportsType) {
        // TODO Auto-generated constructor stub
        this.coins = coins;
        this.handler = handler;
        this.actionStyle = actionStyle;
        this.sportsType = sportsType;
    }

    public AddCoinsThread(int coins, int actionStyle, Handler handler, int sportsType,int flag,String findContent) {
        // TODO Auto-generated constructor stub
        this.coins = coins;
        this.handler = handler;
        this.actionStyle = actionStyle;
        this.sportsType = sportsType;
        this.flag=flag;
        this.findContent=findContent;
    }

    @Override
    public void run() {
        ApiBack ab = null;
        try {
            if (flag==0){
                ab = ApiJsonParser.coingoldChange(SportsApp.getInstance()
                        .getSessionId(), 1, coins, 0, 0, actionStyle, sportsType);
            }else{
                if(findContent==null){
                    findContent="";
                }
                ab = ApiJsonParser.uploadShowAndRewardInfo(SportsApp.getInstance()
                        .getSessionId(), 1, coins, actionStyle, findContent);
            }

        } catch (ApiNetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ApiSessionOutException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (ab != null && ab.getFlag() == 0) {
            Handler mainHandler = SportsApp.getInstance().getMainHandler();
            if (mainHandler != null) {
                mainHandler.sendMessage(mainHandler.obtainMessage(
                        ApiConstant.UPDATE_COINS_MSG, Integer.parseInt(ab.getMsg()), 0));
            }
            if (handler != null) {
                handler.sendMessage(handler.obtainMessage(ApiConstant.COINS_SUCCESS, ab));
            }
        } else if (ab != null && ab.getFlag() == -1) {
            if (ab.getMsg().equals("error")) {
                if (handler != null) {
                    handler.sendMessage(handler.obtainMessage(ApiConstant.COINS_FAIL, ab));
                }
            } else if (ab.getMsg().equals("limit")) {
                if (handler != null) {
                    handler.sendMessage(handler.obtainMessage(ApiConstant.COINS_LIMIT, ab));
                }
            }else{
                if (handler != null) {
                    handler.sendMessage(handler.obtainMessage(ApiConstant.COINS_FAIL, ab));
                }
            }
        } else {
            if(flag==1){
                if(ab != null){
                    if(ab.getFlag()==1060){
                        Handler mainHandler = SportsApp.getInstance().getMainHandler();
                        if (mainHandler != null) {
                            mainHandler.sendMessage(mainHandler.obtainMessage(
                                    ApiConstant.UPDATE_COINS_MSG, Integer.parseInt(ab.getMsg()), 0));
                        }
                        if (handler != null) {
                            handler.sendMessage(handler.obtainMessage(ApiConstant.COINS_SUCCESS, ab));
                        }
                    }else{
                        if (ab.getMsg().equals("error")) {
                            if (handler != null) {
                                handler.sendMessage(handler.obtainMessage(ApiConstant.COINS_FAIL, ab));
                            }
                        } else if (ab.getMsg().equals("limit")) {
                            if (handler != null) {
                                handler.sendMessage(handler.obtainMessage(ApiConstant.COINS_LIMIT, ab));
                            }
                        }else{
                            if (handler != null) {
                                handler.sendMessage(handler.obtainMessage(ApiConstant.COINS_FAIL, ab));
                            }
                        }
                    }

                }else{
                    if (handler != null) {
                        handler.sendMessage(handler.obtainMessage(ApiConstant.COINS_FAIL, ab));
                    }
                }
            }else{
                if (handler != null) {
                    handler.sendMessage(handler.obtainMessage(ApiConstant.COINS_FAIL, ab));
                }
            }

        }
    }

}