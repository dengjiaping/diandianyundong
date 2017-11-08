package com.fox.exercise.newversion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.ingenic.indroidsync.SportsApp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.util.Log;

public class TrainVoiceBgPrompt {
    int n = 0;
    private String sex;
    public String flag;
    private MediaPlayer mp;
    private AssetManager assetManager;
    private AssetFileDescriptor fileDescriptor;
    private List<String> list;
    // private int hour, minute, second;
    private String videoDir;//播放录音本地路径
    private Context context;
    private SharedPreferences foxSportSetting;

    public TrainVoiceBgPrompt(Context context, String sex, String flag,
                              String videoDir) {
        this.sex = sex;
        this.flag = flag;
        list = new ArrayList<String>();
        mp = new MediaPlayer();
        this.context = context;
        assetManager = context.getAssets();
        this.videoDir = videoDir;
        foxSportSetting = context.getSharedPreferences("sports" + SportsApp.getInstance().getSportUser().getUid(), 0);
    }

    /* 设置男女声音.mp3源文件 ，female男 */
    private void setMusicResource(String voiceName, float num) {
        try {
            if (voiceName.contains(videoDir)) {
                //表示从本地文件读取音频文件
                mp.reset();
                mp.setLooping(true);
                mp.setDataSource(voiceName);
                mp.setVolume(num, num);
                mp.prepareAsync();
            }
//			else {
//				//表示从asses文件里读取音频文件
//				if (sex.equals("male")) {
//					fileDescriptor = assetManager.openFd(sex + "_" + voiceName
//							+ ".wav");
//				} else {
//					fileDescriptor = assetManager.openFd(sex + "_" + voiceName
//							+ ".wav");
//				}
//				// Log.e("fgw", sex + "_" + voiceName + ".mp3");
//				mp.reset();
//				mp.setDataSource(fileDescriptor.getFileDescriptor(),
//						fileDescriptor.getStartOffset(), fileDescriptor.getLength());
//				mp.prepareAsync();
//			}

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO: handle exception
            e.getStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // // 毫秒转换时/分/秒
    // private void dealTime(String milliSecond) {
    // int temp = Integer.parseInt(milliSecond);
    // // Log.e("millSecond", temp + "");
    // hour = temp / (60 * 60 * 1000);
    // minute = (temp - hour * 60 * 60 * 1000) / (60 * 1000);
    // second = (temp - hour * 60 * 60 * 1000 - minute * 60 * 1000) / 1000;
    // // Log.e("time", hour + ":" + minute + ":" + second);
    // }

	/* 多位数读取处理 */
//	public void readNum(String num) {
//		char[] temp = num.toCharArray();
//		int length = temp.length;
//		if (Integer.parseInt(num) % Math.pow(10, length - 1) == 0
//				|| length == 1) {
//			list.add(num);
//		} else {// 201 210 23 213 231
//			for (int i = 0; i < length; i++) {
//				if ((length > 2)
//						&& (Integer.parseInt(String.valueOf(temp[i])) == 1)
//						&& (i == (length - 2))) {// 读取213
//					list.add("10-2");
//				} else if ((length > 2)
//						&& Integer.parseInt(String.valueOf(temp[i])) == 0
//						&& Integer.parseInt(num) % 10 == 0) {// 读取210
//				} else if ((length > 2)
//						&& Integer.parseInt(String.valueOf(temp[i])) == 0
//						&& Integer.parseInt(num) % 10 != 0) {// 读取201
//					list.add("0");
//				} else {
//					list.add(String.valueOf(Integer.parseInt(String
//							.valueOf(temp[i]))
//							* (int) Math.pow(10, length - i - 1)));
//				}
//			}
//
//		}
//
//	}

    private void setVoiceNameList() {
        if (flag.equals("train_bg")) {
            list.add(videoDir + "/" + "female_bjy.mp3");
        }
    }

    // private void readTime(String time) {
    // dealTime(time);
    // // 时
    // if (hour != 0) {
    // readNum(String.valueOf(hour));
    // list.add("xiaoshi");
    // }
    // // 分
    // if (hour != 0 || (hour == 0 && minute != 0)) {
    // readNum(String.valueOf(minute));
    // list.add("fen");
    // }
    // // 秒
    // if (hour != 0 || minute != 0
    // || (hour == 0 || (minute == 0 && second != 0))) {
    // readNum(String.valueOf(second));
    // list.add("miao");
    // }
    // }

    public void playVoice(final float num) {
//		if(!foxSportSetting.getBoolean("voiceon", true)){
//			return;
//		}
        if (mp == null) {
            mp = new MediaPlayer();
        }
        setVoiceNameList();
        n = 0;
        setMusicResource(list.get(0).toString(), num);
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
                    setMusicResource(list.get(n).toString(), num);
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
                playVoice(num);
                Log.e("fgw", "setOnErrorListener");
                return true;
            }
        });
    }


    public void destroyPlay() {
        if (mp != null) {
            if (mp.isPlaying()) {
                mp.stop();

                mp.release();
                mp = null;
            }
        }
    }

    public void stopPlay() {
        if (mp != null) {
            if (mp.isPlaying()) {
                mp.pause();
            }
        }
    }

    public void startPlay() {
        if (mp != null) {
            mp.start();
        }
    }

    public int getCurrNum() {
        int num = 0;
        if (mp != null) {
            num = mp.getCurrentPosition();
        }
        return num;
    }

    public void setPlay(float num) {
        startPlay();
        stopPlay();
        try {
            mp.setDataSource(list.get(0));
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mp.seekTo(getCurrNum());
        mp.setVolume(num, num);
        startPlay();
    }

}
