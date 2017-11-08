package com.fox.exercise;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class SportsThreadPool {

    private List<Thread> pool = null;
    private static final int MAX = 2;

    private static final String TAG = "SportsThreadPool";

    public SportsThreadPool() {
        pool = new ArrayList<Thread>();
    }

    public void tryToStart(Thread t) {
        Thread target = null;
        pool.add(0, t);
        while (pool.size() > MAX) {
            Log.d(TAG, "pool.size() > MAX");
            target = pool.get(pool.size() - 1);
            if (target.isAlive()) {
                target.interrupt();
            }
            target = null;
            pool.remove(pool.size() - 1);
        }
        target = pool.get(0);
        if (!target.isAlive())
            target.start();
    }

    public void recycle() {
        while (pool.size() > 0) {
            Thread target = pool.get(0);
            if (target != null) {
                if (target.isAlive()) {
                    target.interrupt();
                } else {
                    target = null;
                }
            }
        }
        pool.clear();
        pool = null;
    }
}
