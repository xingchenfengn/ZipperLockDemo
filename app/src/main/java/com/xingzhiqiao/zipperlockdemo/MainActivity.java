package com.xingzhiqiao.zipperlockdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends Activity implements View.OnTouchListener {

    private ImageView imgZipper;
    private ZipperLock mZipperLock;
    private ImageView imgFront;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pin_lock);
        createZipperView();
    }


    private void createZipperView() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        this.mZipperLock = new Type1(metrics.widthPixels, metrics.heightPixels, this);
        this.imgZipper = (ImageView) findViewById(R.id.imgZipper);
        this.imgZipper.setOnTouchListener(this);
        this.imgFront = (ImageView) findViewById(R.id.imgFront);
        this.imgFront.setOnTouchListener(this);
        this.mZipperLock.Init(this.imgZipper, this.imgFront, null);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mZipperLock.CheckMotionEvent(event);
        return true;
    }
}
