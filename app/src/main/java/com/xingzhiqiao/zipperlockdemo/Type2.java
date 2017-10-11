package com.xingzhiqiao.zipperlockdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.widget.ImageView;

public class Type2 extends ZipperLock {
    private Bitmap bmpBg;
    private Bitmap bmpMask;
    private Bitmap bmpPendant;
    private Bitmap bmpRezBack;
    private Bitmap bmpRezFront;
    private Bitmap bmpZipper;
    private Bitmap bmpZipperHalf;
    private Canvas canvasBack;
    private Canvas canvasFront;
    private int center;
    private ImageView imgFront;
    private Paint f3103p;

    public Type2(int width, int height, Context context) {
        super(width, height, context);
    }

    public void Init(ImageView imgZip, ImageView imgFront, LockScreenUtils lockScreenUtils) {
        this.imgZipper = imgZip;
        this.imgFront = imgFront;
        this.mLockScreenUtils = lockScreenUtils;
        this.bmpRezBack = Bitmap.createBitmap(this.width, this.height, Config.ARGB_8888);
        this.bmpRezFront = Bitmap.createBitmap(this.width, this.height, Config.ARGB_8888);
        this.bmpZipperHalf = Bitmap.createBitmap(this.width, this.height, Config.ARGB_8888);
        int zipper = this.context.getSharedPreferences(String.valueOf(this.context.getPackageName()), 0).getInt(this.context.getString(R.string.ZIPPER_SELECTED_PREF_KEY), 0);
        int pendant = this.context.getSharedPreferences(String.valueOf(this.context.getPackageName()), 0).getInt(this.context.getString(R.string.PENDANT_SELECTED_PREF_KEY), 0);
        int bg = this.context.getSharedPreferences(String.valueOf(this.context.getPackageName()), 0).getInt(this.context.getString(R.string.BG_SELECTED_PREF_KEY), 0);
        this.bmpMask = BitmapFactory.decodeResource(this.context.getResources(), this.context.getResources().getIdentifier("mask_horizontal", "drawable", this.context.getPackageName()));
        this.bmpMask = Bitmap.createScaledBitmap(this.bmpMask, this.width * 2, this.height, true);
        this.pendantWidth = (int) (((double) this.bmpMask.getHeight()) * 0.1015d);
        this.pendantLength = (int) (((double) this.bmpMask.getWidth()) * 0.16d);
        this.center = (int) (((double) this.bmpMask.getHeight()) * 0.7226d);
        this.limit = ((double) this.width) * 0.8d;
        this.bmpZipper = BitmapFactory.decodeResource(this.context.getResources(), this.context.getResources().getIdentifier("zipper_h_" + zipper, "drawable", this.context.getPackageName()));
        this.bmpZipper = Bitmap.createScaledBitmap(this.bmpZipper, this.width * 2, this.height, true);
        this.bmpZipperHalf = Bitmap.createBitmap(this.bmpZipper, this.width, 0, this.width, this.height);
        this.bmpZipper = Bitmap.createBitmap(this.bmpZipper, 0, 0, this.width, this.height);
        this.bmpPendant = BitmapFactory.decodeResource(this.context.getResources(), this.context.getResources().getIdentifier("pendant_h_" + pendant, "drawable", this.context.getPackageName()));
        this.bmpPendant = Bitmap.createScaledBitmap(this.bmpPendant, this.width * 2, this.height, true);
        this.bmpBg = BitmapFactory.decodeResource(this.context.getResources(), this.context.getResources().getIdentifier("bg_zipper_" + bg, "drawable", this.context.getPackageName()));
        float a = CheckDimensions(this.bmpBg.getWidth(), this.bmpBg.getHeight(), this.width, this.height);
        this.bmpBg = Bitmap.createScaledBitmap(this.bmpBg, (int) (((float) this.bmpBg.getWidth()) * a), (int) (((float) this.bmpBg.getHeight()) * a), true);
        this.f3103p = new Paint(1);
        this.f3103p.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        this.f3103p.setAntiAlias(true);
        this.f3103p.setDither(true);
        this.canvasBack = new Canvas(this.bmpRezBack);
        this.canvasBack.drawBitmap(this.bmpBg, 0.0f, 0.0f, null);
        this.canvasBack.drawBitmap(this.bmpZipperHalf, 0.0f, 0.0f, null);
        this.bmpBg = Bitmap.createBitmap(this.bmpRezBack, 0, 0, this.bmpRezBack.getWidth(), this.bmpRezBack.getHeight());
        this.bmpRezBack = Bitmap.createBitmap(this.width, this.height, Config.ARGB_8888);
        this.canvasFront = new Canvas(this.bmpRezFront);
        this.canvasBack = new Canvas(this.bmpRezBack);
        this.imgZipper.setImageBitmap(this.bmpBg);
        SetFront(0.0f);
    }

    public void ChangeImages(float y) {
        this.unlock = ((double) ((this.delta / 2.0f) + y)) >= this.limit;
        if (y < ((float) (this.width - (this.pendantLength / 2)))) {
            SetBack(y);
            SetFront(y);
        }
    }

    public void CheckMotionEvent(MotionEvent event) {
        switch (MotionEventCompat.getActionMasked(event)) {
            case 0:
                this.unlock = false;
                if (event.getY() > ((float) (this.center - (this.pendantWidth / 2))) && event.getY() < ((float) (this.center + (this.pendantWidth / 2))) && event.getX() < ((float) this.pendantLength)) {
                    this.shouldDrag = true;
                    this.delta = event.getX();
                    return;
                }
                return;
            case 1:
                this.shouldDrag = false;
                if (!this.unlock) {
                    this.imgZipper.setImageBitmap(this.bmpBg);
                    SetFront(0.0f);
                    return;
                } else if (this.context.getSharedPreferences("MY_PREF", 0).getBoolean("pinLock", false)) {
                    this.imgZipper.setVisibility(8);
                    this.imgFront.setVisibility(8);
                    return;
                } else {
//                    this.mLockScreenUtils.unlockScreen();
                    return;
                }
            case 2:
                if (this.shouldDrag && event.getX() - this.delta >= 0.0f) {
                    ChangeImages(event.getX() - this.delta);
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void ResetImage() {
        this.imgZipper.setVisibility(0);
        this.imgFront.setVisibility(0);
        this.imgZipper.setImageBitmap(this.bmpBg);
        SetFront(0.0f);
    }

    private void SetBack(float y) {
        if (this.bmpMask != null && this.bmpBg != null && this.bmpRezBack != null) {
            this.canvasBack.drawColor(0, Mode.CLEAR);
            this.canvasBack.drawBitmap(this.bmpMask, ((float) ((-this.bmpMask.getWidth()) / 2)) + y, 0.0f, new Paint());
            this.canvasBack.drawBitmap(this.bmpBg, 0.0f, 0.0f, this.f3103p);
            this.imgZipper.setImageBitmap(this.bmpRezBack);
        }
    }

    private void SetFront(float y) {
        if (this.bmpZipper != null && this.bmpPendant != null && this.bmpRezFront != null) {
            this.canvasFront.drawColor(0, Mode.CLEAR);
            this.canvasFront.drawBitmap(this.bmpZipper, ((float) ((-this.bmpMask.getWidth()) / 2)) + y, 0.0f, null);
            this.canvasFront.drawBitmap(this.bmpPendant, ((float) ((-this.bmpMask.getWidth()) / 2)) + y, 0.0f, null);
            this.imgFront.setImageBitmap(this.bmpRezFront);
        }
    }

    public void DestroyBitmaps() {
        if (this.bmpMask != null) {
            this.bmpMask.recycle();
            this.bmpMask = null;
        }
        if (this.bmpRezBack != null) {
            this.bmpRezBack.recycle();
            this.bmpRezBack = null;
        }
        if (this.bmpRezFront != null) {
            this.bmpRezFront.recycle();
            this.bmpRezFront = null;
        }
        if (this.bmpZipper != null) {
            this.bmpZipper.recycle();
            this.bmpZipper = null;
        }
        if (this.bmpPendant != null) {
            this.bmpPendant.recycle();
            this.bmpPendant = null;
        }
        if (this.bmpBg != null) {
            this.bmpBg.recycle();
            this.bmpBg = null;
        }
        if (this.bmpZipperHalf != null) {
            this.bmpZipperHalf.recycle();
            this.bmpZipperHalf = null;
        }
    }
}
