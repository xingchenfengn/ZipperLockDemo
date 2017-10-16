package com.xingzhiqiao.zipperlockdemo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

public class LockScreenUtils implements OnClickListener, OnTouchListener {
    Context context;
    ZipperLock mZipperLock;
    Activity masterActivity;
    SharedPreferences prefs;
    @Override
    public void onClick(View v) {

    }

    public LockScreenUtils(Context context, boolean callPermission, boolean smsPermission) {
        this.masterActivity = (Activity) context;
        this.context = context;
        this.prefs = context.getSharedPreferences(String.valueOf(context.getPackageName()), 0);
    }

    public boolean onTouch(View v, MotionEvent event) {
        this.mZipperLock.CheckMotionEvent(event);
        return true;
    }

    public void unlockScreen() {
        Log.v("LOCK_TEST", "unlockScreen()");
        Log.v("LOCK_TEST", "unlockScreen() " + this.masterActivity.getLocalClassName());
        this.mZipperLock.DestroyBitmaps();
        this.masterActivity.finish();
    }

    public void destroyLock() {
//        this.masterActivity.unregisterReceiver(this.timeChangedReceiver);
    }
}
