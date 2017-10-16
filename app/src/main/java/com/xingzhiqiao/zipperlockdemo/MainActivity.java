package com.xingzhiqiao.zipperlockdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity implements View.OnTouchListener {


    private Button mTypeBtn;

    private ImageView imgZipper;
    private ZipperLock mZipperLock;
    private ImageView imgFront;

    private static final int LOCKER_TYPE_VERTICAL = 0;
    private static final int LOCKER_TYPE_HORIZON = 1;

    private String[] types = new String[]{"Vertical", "Horizon"};

    private int currentType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pin_lock);
        createZipperView();
    }


    private void createZipperView() {
        initLocker(currentType);
        mTypeBtn = (Button) findViewById(R.id.type);
        mTypeBtn.setText(types[0]);
        mTypeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentType == LOCKER_TYPE_VERTICAL) {
                    currentType = LOCKER_TYPE_HORIZON;
                    mTypeBtn.setText(types[1]);

                } else {
                    currentType = LOCKER_TYPE_VERTICAL;
                    mTypeBtn.setText(types[0]);
                }
                initLocker(currentType);
            }
        });
    }

    private void initLocker(int type) {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        if (type == LOCKER_TYPE_VERTICAL) {
            this.mZipperLock = new VerticalLocker(metrics.widthPixels, metrics.heightPixels, this);
        } else {
            this.mZipperLock = new HorizontalLocker(metrics.widthPixels, metrics.heightPixels, this);
        }
        this.imgZipper = (ImageView) findViewById(R.id.imgZipper);
        this.imgZipper.setOnTouchListener(this);
        this.imgFront = (ImageView) findViewById(R.id.imgFront);
        this.imgFront.setOnTouchListener(this);
        this.mZipperLock.init(this.imgZipper, this.imgFront, null);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mZipperLock.CheckMotionEvent(event);
        return true;
    }
}
