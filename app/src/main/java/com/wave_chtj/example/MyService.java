package com.wave_chtj.example;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Create on 2020/3/23
 * author chtj
 * desc
 */
public class MyService extends Service {
    public static final String TAG="MyService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate: ");
        sleepTest();
    }
    public void sleepTest() {
        Log.e(TAG, "sleepTest: start");
        SystemClock.sleep(20000);
        Log.e(TAG, "sleepTest: end");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: ");
    }
}
