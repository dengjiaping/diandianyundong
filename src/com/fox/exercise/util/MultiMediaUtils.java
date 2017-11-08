package com.fox.exercise.util;


public class MultiMediaUtils {
    // private static MultiMediaUtils mediaUtil = null;
    // private SportingMapActivity activity;
    //
    // public static int PHOTOFromCAMERA = 0x01;
    // public static int PHOTOFromGallery = 0x02;
    // public static int VIDEO = 0x03;
    // public static int VOICE = 0x04;
    //
    // private MultiMediaUtils(SportingMapActivity activity) {
    // this.activity = activity;
    // }
    //
    // public static MultiMediaUtils getInstance(SportingMapActivity activity) {
    // if (mediaUtil == null) {
    // mediaUtil = new MultiMediaUtils(activity);
    // }
    // return mediaUtil;
    // }
    //
    // public void getPicFromGarry() {
    // Intent picture = new Intent(Intent.ACTION_PICK,
    // android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    // activity.startActivityForResult(picture, PHOTOFromGallery);
    // }
    //
    // public void getPicFromCamera() {
    // Log.i("getPicFromCamera", "跳转界面！");
    // Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    // activity.startActivityForResult(camera, PHOTOFromCAMERA);
    // }
    //
    // public void getVoice() {
    // Log.i("getVoice", "跳转界面！");
    // Intent intent = new Intent(Intent.ACTION_VOICE_COMMAND);
    // intent.setType("audio/amr");
    // intent.setClassName("com.android.soundrecorder",
    // "com.android.soundrecorder.SoundRecorder");
    // activity.startActivityForResult(intent, VOICE);
    // }
    //
    // public void getVideo() {
    // Log.i("getVideo", "跳转界面！");
    // Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
    // intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
    // intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30);
    // activity.startActivityForResult(intent, VIDEO);
    // }
}
