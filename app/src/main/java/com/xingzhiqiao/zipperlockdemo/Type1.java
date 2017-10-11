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
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;


public class Type1 extends ZipperLock {
    //背景图片
    private Bitmap bmpBg;
    //拉环
    private Bitmap bmpMask;
    //拉链
    private Bitmap bmpPendant;
    //后面的bitmap
    private Bitmap bmpRezBack;
    //前面的bitmap
    private Bitmap bmpRezFront;
    //完整的拉链
    private Bitmap bmpZipper;
    private Bitmap bmpZipperHalf;
    //后面的canvas
    private Canvas canvasBack;
    //前面的canvas
    private Canvas canvasFront;
    private ImageView imgFront;
    private int offset = 0;
    private Paint f3102p;

    public Type1(int width, int height, Context context) {
        super(width, height, context);
    }

    public void Init(ImageView imgZip, ImageView imgFront, LockScreenUtils lockScreenUtils) {
        this.imgZipper = imgZip;
        this.imgFront = imgFront;
        this.bmpRezBack = Bitmap.createBitmap(this.width, this.height, Config.ARGB_8888);
        this.bmpRezFront = Bitmap.createBitmap(this.width, this.height, Config.ARGB_8888);
        this.bmpZipper = BitmapFactory.decodeResource(this.context.getResources(), this.context.getResources().getIdentifier("zipper_v_0", "drawable", this.context.getPackageName()));
        this.bmpMask = BitmapFactory.decodeResource(this.context.getResources(), this.context.getResources().getIdentifier("mask_vertical", "drawable", this.context.getPackageName()));
        this.bmpPendant = BitmapFactory.decodeResource(this.context.getResources(), this.context.getResources().getIdentifier("pendant_v_0", "drawable", this.context.getPackageName()));

        this.step = (int) (((double) this.bmpZipper.getHeight()) * 0.3535d);
        if (this.bmpZipper.getHeight() < this.step + this.height) {
            int nS = (int) (((float) this.step) * (((float) this.height) / ((float) (this.bmpZipper.getHeight() - this.step))));
            int bmpWidth = (int) (((float) this.bmpZipper.getWidth()) * (((float) (this.height + nS)) / ((float) this.bmpZipper.getHeight())));
            if (this.width <= bmpWidth) {
                this.bmpZipper = Bitmap.createScaledBitmap(this.bmpZipper, bmpWidth, this.height + nS, true);
                this.offset = (this.bmpZipper.getWidth() - this.width) / 2;
            } else {
                this.bmpZipper = Bitmap.createScaledBitmap(this.bmpZipper, this.width, (int) (((float) (this.height + nS)) * (((float) this.width) / ((float) bmpWidth))), true);
            }
            this.step = (int) (((double) this.bmpZipper.getHeight()) * 0.3535d);
            this.bmpMask = Bitmap.createScaledBitmap(this.bmpMask, this.bmpZipper.getWidth(), this.bmpZipper.getHeight(), true);
            this.bmpPendant = Bitmap.createScaledBitmap(this.bmpPendant, this.bmpZipper.getWidth(), this.bmpZipper.getHeight(), true);
        } else if (this.bmpZipper.getWidth() - this.width > 0) {
            this.offset = (this.bmpZipper.getWidth() - this.width) / 2;
        }
        this.bmpZipperHalf = Bitmap.createBitmap(this.bmpZipper, 0, this.step, this.bmpZipper.getWidth(), this.bmpZipper.getHeight() - this.step);
        this.bmpZipperHalf = Bitmap.createScaledBitmap(this.bmpZipperHalf, this.bmpZipperHalf.getWidth(), this.height, true);
        this.bmpZipper = Bitmap.createBitmap(this.bmpZipper, 0, 0, this.bmpZipper.getWidth(), this.step);
        this.bmpBg = BitmapFactory.decodeResource(this.context.getResources(), this.context.getResources().getIdentifier("bg_zipper_0", "drawable", this.context.getPackageName()));
        float a = CheckDimensions(this.bmpBg.getWidth(), this.bmpBg.getHeight(), this.width, this.height);
        this.bmpBg = Bitmap.createScaledBitmap(this.bmpBg, (int) (((float) this.bmpBg.getWidth()) * a), (int) (((float) this.bmpBg.getHeight()) * a), true);
        this.pendantWidth = ((int) (((double) this.bmpMask.getWidth()) * 0.16d)) / 2;
        this.pendantLength = (int) (((double) this.bmpMask.getHeight()) * 0.1162d);
        this.limit = 0.8d * ((double) this.height);
        this.f3102p = new Paint(1);
        this.f3102p.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        this.f3102p.setAntiAlias(true);
        this.f3102p.setDither(true);
        this.canvasBack = new Canvas(this.bmpRezBack);
        this.canvasBack.drawBitmap(this.bmpBg, 0.0f, 0.0f, null);
        this.canvasBack.drawBitmap(this.bmpZipperHalf, (float) (-this.offset), 0.0f, null);
        this.bmpBg = Bitmap.createBitmap(this.bmpRezBack, 0, 0, this.bmpRezBack.getWidth(), this.bmpRezBack.getHeight());
        this.imgZipper.setImageBitmap(this.bmpBg);
        this.bmpRezBack = Bitmap.createBitmap(this.width, this.height, Config.ARGB_8888);
        this.canvasBack = new Canvas(this.bmpRezBack);
        this.canvasFront = new Canvas(this.bmpRezFront);
        SetFront(0.0f);
    }

    private Bitmap deresouceFromBM(int resouceId, Context context) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), resouceId, options);
        options.inSampleSize = 2;
        options.inPreferredConfig = Config.RGB_565;
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        return BitmapFactory.decodeResource(context.getResources(), resouceId, options);
    }

    public void ChangeImages(float y) {
        this.unlock = ((double) ((this.delta / 2.0f) + y)) >= this.limit;
        if (y < ((float) (this.height - (this.pendantLength / 2)))) {
            SetBack(y);
            SetFront(y);
        }
    }

    public void CheckMotionEvent(MotionEvent event) {
        switch (MotionEventCompat.getActionMasked(event)) {
            case MotionEvent.ACTION_DOWN:
                this.unlock = false;
                if (event.getX() > ((float) ((this.width / 2) - this.pendantWidth)) && event.getX() < ((float) ((this.width / 2) + this.pendantWidth)) && event.getY() < ((float) this.pendantLength)) {
                    this.shouldDrag = true;
                    this.delta = event.getY();
                    return;
                }
                return;
            case MotionEvent.ACTION_UP:
                this.shouldDrag = false;
                if (!this.unlock) {
                    this.imgZipper.setImageBitmap(this.bmpBg);
                    SetFront(0.0f);
                    return;
                } else {
                    //TODO 解锁处理
//                    this.mLockScreenUtils.unlockScreen();
                    return;
                }
            case MotionEvent.ACTION_MOVE:
                if (this.shouldDrag && event.getY() - this.delta >= 0.0f) {
                    ChangeImages(event.getY() - this.delta);
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

    private void SetBack(float y) {
        if (this.bmpMask != null && this.bmpBg != null && this.bmpRezBack != null) {
            this.canvasBack.drawColor(0, Mode.CLEAR);
            this.canvasBack.drawBitmap(this.bmpMask, (float) (-this.offset), ((float) (-this.step)) + y, new Paint());
            this.canvasBack.drawBitmap(this.bmpBg, 0.0f, 0.0f, this.f3102p);
            this.imgZipper.setImageBitmap(this.bmpRezBack);
        }
    }

    private void SetFront(float y) {
        if (this.bmpZipper != null && this.bmpPendant != null && this.bmpRezFront != null) {
            this.canvasFront.drawColor(0, Mode.CLEAR);
            this.canvasFront.drawBitmap(this.bmpZipper, (float) (-this.offset), ((float) (-this.step)) + y, null);
            this.canvasFront.drawBitmap(this.bmpPendant, (float) (-this.offset), ((float) (-this.step)) + y, null);
            this.imgFront.setImageBitmap(this.bmpRezFront);
        }
    }

    public int getMaskWigth() {
        return this.bmpMask.getWidth();
    }
}
