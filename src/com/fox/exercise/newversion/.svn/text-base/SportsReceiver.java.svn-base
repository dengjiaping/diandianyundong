package com.fox.exercise.newversion;

import com.fox.exercise.map.SportingMapActivityGaode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SportsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        if ("com.fox.exercise.newversion.SportsReceiver".equals(intent.getAction())) {
            context.startActivity(new Intent(context, SportingMapActivityGaode.class));
        }
    }

}
