package com.fox.exercise;

import android.os.Binder;

public class SportBinder extends Binder {

    private SporterBundle bundle = null;

    public SportBinder() {
        bundle = SporterBundle.getInstance();
    }

}
