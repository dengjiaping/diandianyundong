package com.fox.exercise.map;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.ingenic.indroidsync.SportsApp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.util.Log;

public class VoicePrompt {
    int n = 0;
    private Context context;
    private String sex;
    public String flag;
    private MediaPlayer mp;
    private AssetManager assetManager;
    private AssetFileDescriptor fileDescriptor;
    private List<String> list;
    private Map<String, String> hashMap;
    private int hour, minute, second;
    private SharedPreferences voiceSportSetting;

    public VoicePrompt(Context context, String sex, String flag,
                       Map<String, String> hashMap) {
        this.sex = sex;
        this.flag = flag;
        this.hashMap = hashMap;
        this.context = context;
        list = new ArrayList<String>();
        mp = new MediaPlayer();
        assetManager = context.getAssets();
        voiceSportSetting = context.getSharedPreferences("voice_sports", 0);
    }

    /* 设置男女声音.mp3源文件 ，female男 */
    private void setMusicResource(String voiceName) {
        try {
            if (sex.equals("male")) {
                fileDescriptor = assetManager.openFd(sex + "_" + voiceName
                        + ".wav");
            } else {
                fileDescriptor = assetManager.openFd(sex + "_" + voiceName
                        + ".wav");
            }
            // Log.e("fgw", sex + "_" + voiceName + ".mp3");
            mp.reset();
            mp.setDataSource(fileDescriptor.getFileDescriptor(),
                    fileDescriptor.getStartOffset(), fileDescriptor.getLength());
            mp.prepareAsync();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO: handle exception
            e.getStackTrace();
        }
    }

    // 毫秒转换时/分/秒
    private void dealTime(String milliSecond) {
        int temp = Integer.parseInt(milliSecond);
        // Log.e("millSecond", temp + "");
//		hour = temp / (60 * 60 * 1000);
//		minute = (temp - hour * 60 * 60 * 1000) / (60 * 1000);
        minute = (temp) / (60 * 1000);
//		second = (temp - hour * 60 * 60 * 1000 - minute * 60 * 1000) / 1000;
        second = (temp - minute * 60 * 1000) / 1000;
        // Log.e("time", hour + ":" + minute + ":" + second);
    }

    /* 多位数读取处理 */
    public void readNum(String num) {
        char[] temp = num.toCharArray();
        int length = temp.length;
        if (Integer.parseInt(num) % Math.pow(10, length - 1) == 0
                || length == 1) {
            list.add(num);
        } else {// 201 210 23 213 231
            for (int i = 0; i < length; i++) {
                if ((length > 2)
                        && (Integer.parseInt(String.valueOf(temp[i])) == 1)
                        && (i == (length - 2))) {// 读取213
                    list.add("10-2");
                } else if ((length > 2)
                        && Integer.parseInt(String.valueOf(temp[i])) == 0
                        && Integer.parseInt(num) % 10 == 0) {// 读取210
                } else if ((length > 2)
                        && Integer.parseInt(String.valueOf(temp[i])) == 0 && Integer.parseInt(num) % 10 != 0) {//读取201
                    list.add("0");
                } else {
                    list.add(String.valueOf(Integer.parseInt(String
                            .valueOf(temp[i]))
                            * (int) Math.pow(10, length - i - 1)));
                }
            }

        }

    }

    private void setVoiceNameList() {
        if (flag.equals("start")) {
            list.add("yundongkaishi");
        }
        if (flag.equals("pause")) {
            list.add("yundongzanting");
        }
        if (flag.equals("continue")) {
            list.add("jixuyundong");
        }
        if (flag.equals("loseGPS")) {
            list.add("GPS-DS");
        }
        if (flag.equals("obtainGPS")) {
            list.add("GPS-BH");
        }
        if (flag.equals("moving")) {
            String type=hashMap.get("type");
            String tempdis=hashMap.get("gongli");
            double tempDoub=Double.parseDouble(tempdis);
            if (tempDoub>0) {
                list.add("yijingyundong");// 已经运动了
                if("1".equals(type)){
                    list.add((int)tempDoub+"");
                }else{
                    if(tempDoub<1){
                        list.add("0");
                    }else{
                        readNum(String.valueOf((int)tempDoub));
                    }
                    if(tempDoub>(int)tempDoub){
                        list.add("point");// 不是整公里播报后两位点数
                        char[] charTemp = tempdis.toCharArray();
                        list.add(charTemp[charTemp.length-2]+"");//后两位小数的值
                        if('0'!=charTemp[charTemp.length-1]){
                            list.add(charTemp[charTemp.length-1]+"");//后两位小数的值
                        }
                        charTemp=null;
                    }
                }
                list.add("gongli");// 公里
                list.add("yongshi");// 用时
                readTime(hashMap.get("yongshi"));
                // 配速
//				list.add("peisu");
//				if (!hashMap.get("peisu_fen").equals("0")) {
//					readNum(hashMap.get("peisu_fen"));
//					list.add("fen");
//				}
//				readNum(hashMap.get("peisu_miao"));
//				list.add("miao");
//				list.add("mei");
//				list.add("gongli");
            }else{
                //运动0公里
                list.add("yijingyundong");// 已经运动了
                list.add("0");
                list.add("gongli");// 公里
                list.add("yongshi");// 用时
                readTime(hashMap.get("yongshi"));
            }

            StringBuffer bf = new StringBuffer();
            for (int i = 0; i < list.size(); i++) {
                bf.append(list.get(i));
            }
        }
        if (flag.equals("end")) {
            list.add("benciyundongzonggonghuode");
            readNum(hashMap.get("coins"));
            list.add("kubi");
        }
    }

    private void readTime(String time) {
        dealTime(time);
        // 时
		if (hour != 0) {
			readNum(String.valueOf(hour));
			list.add("xiaoshi");
		}
        // 分
//		if (hour != 0 || (hour == 0 && minute != 0)) {
//			readNum(String.valueOf(minute));
//			list.add("fen");
//		}
        if(minute==0){
            list.add("0");
            list.add("fen");
        }else{
            readNum(String.valueOf(minute));
            list.add("fen");
        }
//        if (minute != 0) {
//            readNum(String.valueOf(minute));
//            list.add("fen");
//        }
        // 秒
//		if (hour != 0 || minute != 0
//				|| (hour == 0 || (minute == 0 && second != 0))) {
//			readNum(String.valueOf(second));
//			list.add("miao");
//		}
        if(second==0){
            list.add("0");
            list.add("miao");
        }else{
            readNum(String.valueOf(second));
            list.add("miao");
        }
//        if (minute != 0
//                || ((minute == 0 && second != 0))) {
//            readNum(String.valueOf(second));
//            list.add("miao");
//        }
    }

    public void playVoice() {

        if (!voiceSportSetting.getBoolean("voiceon", true)) {
            return;
        }

        if (mp == null) {
            mp = new MediaPlayer();
        }
        setVoiceNameList();
        n = 0;
        setMusicResource(list.get(0).toString());
        mp.setOnPreparedListener(new OnPreparedListener() {

            public void onPrepared(MediaPlayer arg0) {
                // TODO 自动生成的方法存根
                Log.e("fgw", "setOnPreparedListener");
                mp.start();
            }
        });
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            // 覆盖文件播出完毕事件
            public void onCompletion(MediaPlayer arg0) {
                ++n;
                if (n < list.size()) {
                    setMusicResource(list.get(n).toString());
                } else {
                    if (mp != null) {
                        mp.stop();
                        mp.release();
                        mp = null;
                        Log.e("fgw", "释放MediaPlayer资源");
                    }

                }
            }
        });
        mp.setOnErrorListener(new OnErrorListener() {
            public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
                // TODO 自动生成的方法存根
                // 播放异常处理
                // setMusicResource(list.get(0).toString());
                playVoice();
                Log.e("fgw", "setOnErrorListener");
                return true;
            }
        });
    }
}
