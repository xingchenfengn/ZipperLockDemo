package com.xingzhiqiao.zipperlockdemo;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.ImageView;

public abstract class ZipperLock {
    public Context context;
    float delta = 0.0f;
    int height;
    ImageView imgZipper;
    double limit;
    LockScreenUtils mLockScreenUtils;
    int pendantLength;
    int pendantWidth;
    boolean shouldDrag = false;
    int step;
    boolean unlock = false;
    int width;

    public abstract void ChangeImages(float f);

    public abstract void CheckMotionEvent(MotionEvent motionEvent);

    public abstract void DestroyBitmaps();

    public abstract void Init(ImageView imageView, ImageView imageView2, LockScreenUtils lockScreenUtils);

    public abstract void ResetImage();

    ZipperLock(int width, int height, Context context) {
        this.width = width;
        this.height = height;
        this.context = context;
    }

    float CheckDimensions(int bmpWidth, int bmpHeight, int screenWidth, int screenHeight) {
        if (bmpWidth >= screenWidth && bmpHeight >= screenHeight) {
            return Math.max(((float) screenWidth) / ((float) bmpWidth), ((float) screenHeight) / ((float) bmpHeight));
        }
        if (bmpWidth <= screenWidth && bmpHeight <= screenHeight) {
            return Math.max(((float) screenWidth) / ((float) bmpWidth), ((float) screenHeight) / ((float) bmpHeight));
        }
        if (bmpWidth > screenWidth && bmpHeight <= screenHeight) {
            return ((float) screenHeight) / ((float) bmpHeight);
        }
        if (bmpHeight <= screenHeight || bmpWidth > screenWidth) {
            return 1.0f;
        }
        return ((float) screenWidth) / ((float) bmpWidth);
    }
}
