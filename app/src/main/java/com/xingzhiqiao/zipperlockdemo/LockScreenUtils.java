package com.xingzhiqiao.zipperlockdemo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.provider.CallLog.Calls;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import java.util.Calendar;
import java.util.Date;

public class LockScreenUtils implements OnClickListener, OnTouchListener {
    public static String PIN_FOR_UNLOCK;
    static final IntentFilter sIntentFilter = new IntentFilter();
    String LOCK_COMBINATION;
    boolean batteryEnabled;
    private boolean batteryGone = false;
    ImageView btn0;
    ImageView btn1;
    ImageView btn2;
    ImageView btn3;
    ImageView btn4;
    ImageView btn5;
    ImageView btn6;
    ImageView btn7;
    ImageView btn8;
    ImageView btn9;
    ImageView btnBackSpace;
    ImageView btnCancel;
    private boolean callPermission = false;
    Context context;
    boolean dateEnabled;
    String dateFormat;
    private boolean dateGone = false;
    TextView enterPasswordText;
    final Handler handler = new Handler();
    private int height;
    private ImageView imgFront;
    ImageView imgMissedCall = null;
    ImageView imgMissedSms = null;
    private ImageView imgZipper;
    ZipperLock mZipperLock;
    Activity masterActivity;
    boolean missedCallEnabled;
    ImageView[] pinImages;
    RelativeLayout pinLockHolder;
    public View pinView;
    LinearLayout pinsHolder;
    SharedPreferences prefs;
    int selectedBgNumber;
    boolean shouldUnlockScreen = true;
    boolean smsEnabled;
    private boolean smsPermission = false;
    public RelativeLayout tdbHolder;
    private final BroadcastReceiver timeChangedReceiver = new C11483();
    boolean timeEnabled;
    int timeFormat;
    TextView txtBattery;
    TextView txtDate;
    TextView txtTime;
    Typeface typeface;
    UnlockListener unlockListener;
    private int width;
    Animation wiggleAnim;

    class C11472 implements Runnable {
        C11472() {
        }

        public void run() {
            LockScreenUtils.this.GetNumOfSMS();
//            LockScreenUtils.this.GetNumOfMissedCalls();
        }
    }

    class C11483 extends BroadcastReceiver {
        C11483() {
        }

        public void onReceive(Context context, Intent intent) {
            LockScreenUtils.this.updateTimeTextView();
            LockScreenUtils.this.updateDateTextView();
            LockScreenUtils.this.refreshBatteryPercentage();
        }
    }

    public interface UnlockListener {
        void onUnlockCommand();

        void setCallAndSMSVisible();
    }

    class C18161 implements UnlockListener {
        C18161() {
        }

        public void onUnlockCommand() {
            LockScreenUtils.this.shouldUnlockScreen = true;
            LockScreenUtils.this.unlockScreen();
        }

        public void setCallAndSMSVisible() {
            if (LockScreenUtils.this.imgMissedCall != null && LockScreenUtils.this.imgMissedSms != null) {
//                LockScreenUtils.this.GetNumOfMissedCalls();
                LockScreenUtils.this.GetNumOfSMS();
            }
        }
    }

    static {
        sIntentFilter.addAction("android.intent.action.TIME_TICK");
        sIntentFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
        sIntentFilter.addAction("android.intent.action.TIME_SET");
        sIntentFilter.addAction("android.intent.action.DATE_CHANGED");
        sIntentFilter.addAction("android.intent.action.BATTERY_CHANGED");
    }

    public LockScreenUtils(Context context, boolean callPermission, boolean smsPermission) {
        this.masterActivity = (Activity) context;
        this.context = context;
        this.callPermission = callPermission;
        this.smsPermission = smsPermission;
        this.prefs = context.getSharedPreferences(String.valueOf(context.getPackageName()), 0);
    }

    void getSavedPin() {
        PIN_FOR_UNLOCK = this.prefs.getString(this.context.getString(R.string.PIN_PREF_KEY), "0000");
        Log.v("LOCKSCREEN_UTILS_TEST", "password: " + PIN_FOR_UNLOCK);
    }

    void getVisibilityUtils() {
//        this.timeEnabled = this.prefs.getBoolean(this.context.getString(R.string.TIME_ON_PREF_KEY), true);
//        this.dateEnabled = this.prefs.getBoolean(this.context.getString(R.string.DATE_ON_PREF_KEY), true);
//        this.batteryEnabled = this.prefs.getBoolean(this.context.getString(R.string.BATTERY_ON_PREF_KEY), true);
//        this.missedCallEnabled = this.prefs.getBoolean(this.context.getString(R.string.MISSED_CALL_ON_PREF_KEY), true);
//        this.smsEnabled = this.prefs.getBoolean(this.context.getString(R.string.SMS_ON_PREF_KEY), true);
//        this.timeFormat = this.prefs.getInt(this.context.getString(R.string.TIME_FORMAT_PREF_KEY), 1);
//        Log.e("TimeFormat", String.valueOf(this.timeFormat));
//        this.dateFormat = this.context.getResources().getStringArray(R.array.date_formats)[this.prefs.getInt(this.context.getString(R.string.DATE_FORMAT_PREF_KEY), 0)];
    }

    void checkCombination(int s) {
        this.LOCK_COMBINATION += String.valueOf(s);
        this.pinImages[this.LOCK_COMBINATION.length() - 1].setImageResource(R.drawable.pin_full);
        this.btnBackSpace.setVisibility(0);
        this.btnCancel.setVisibility(8);
        if (this.LOCK_COMBINATION.length() != 4) {
            return;
        }
        if (this.LOCK_COMBINATION.equalsIgnoreCase(PIN_FOR_UNLOCK)) {
            unlockScreen();
            return;
        }
        resetPins();
        this.pinsHolder.startAnimation(this.wiggleAnim);
    }

    void deleteNumberFromCombination() {
        if (this.LOCK_COMBINATION.length() > 0) {
            this.pinImages[this.LOCK_COMBINATION.length() - 1].setImageResource(R.drawable.pin_empty);
            this.LOCK_COMBINATION = this.LOCK_COMBINATION.substring(0, this.LOCK_COMBINATION.length() - 1);
            Log.v("PIN_CHECK_TEST", this.LOCK_COMBINATION);
            if (this.LOCK_COMBINATION.length() == 0) {
                this.btnBackSpace.setVisibility(8);
                this.btnCancel.setVisibility(0);
                return;
            }
            return;
        }
        this.btnBackSpace.setVisibility(8);
        this.btnCancel.setVisibility(0);
        Log.v("PIN_CHECK_TEST", "LOCK_COMBINATION is empty");
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pin_btn_1:
                Log.v("PIN_CLICK_TEST", "clicked #1");
                checkCombination(1);
                return;
            case R.id.pin_btn_2:
                Log.v("PIN_CLICK_TEST", "clicked #2");
                checkCombination(2);
                return;
            case R.id.pin_btn_3:
                Log.v("PIN_CLICK_TEST", "clicked #3");
                checkCombination(3);
                return;
            case R.id.pin_btn_4:
                Log.v("PIN_CLICK_TEST", "clicked #4");
                checkCombination(4);
                return;
            case R.id.pin_btn_5:
                Log.v("PIN_CLICK_TEST", "clicked #5");
                checkCombination(5);
                return;
            case R.id.pin_btn_6:
                Log.v("PIN_CLICK_TEST", "clicked #6");
                checkCombination(6);
                return;
            case R.id.pin_btn_7:
                Log.v("PIN_CLICK_TEST", "clicked #7");
                checkCombination(7);
                return;
            case R.id.pin_btn_8:
                Log.v("PIN_CLICK_TEST", "clicked #8");
                checkCombination(8);
                return;
            case R.id.pin_btn_9:
                Log.v("PIN_CLICK_TEST", "clicked #9");
                checkCombination(9);
                return;
            case R.id.pin_btn_0:
                Log.v("PIN_CLICK_TEST", "clicked #0");
                checkCombination(0);
                return;
            case R.id.pin_btn_backspace:
                Log.v("PIN_CLICK_TEST", "clicked backspace");
                deleteNumberFromCombination();
                return;
            case R.id.pin_btn_cancel:
                resetPins();
                return;
            default:
                return;
        }
    }

    public boolean onTouch(View v, MotionEvent event) {
        this.mZipperLock.CheckMotionEvent(event);
        return true;
    }

    public void createLock() {
        this.selectedBgNumber = this.prefs.getInt(this.context.getString(R.string.BG_SELECTED_PREF_KEY), 0);
        this.pinView = View.inflate(this.context, R.layout.pin_lock, null);
        ((RelativeLayout) this.pinView.findViewById(R.id.pinLockBG)).setBackgroundResource(this.context.getResources().getIdentifier("bg_pin", "drawable", this.context.getPackageName()));
        this.imgZipper = (ImageView) this.pinView.findViewById(R.id.imgZipper);
        this.imgZipper.setOnTouchListener(this);
        this.imgFront = (ImageView) this.pinView.findViewById(R.id.imgFront);
        this.imgFront.setOnTouchListener(this);
//        LayoutParams layoutParams;
//        switch (this.context.getSharedPreferences("MY_PREF", 0).getInt("zipperPos", Integer.parseInt(this.context.getString(R.string.zipper_position)))) {
//            case 1:
                this.mZipperLock = new Type1(this.width, this.height, this.context);
//                if (!this.batteryGone || !this.dateGone) {
//                    this.tdbHolder.setVisibility(0);
//                    layoutParams = new LayoutParams((int) (0.244375d * ((double) this.height)), (int) (0.225d * ((double) this.height)));
//                    layoutParams.addRule(9, -1);
//                    layoutParams.addRule(12, -1);
//                    this.tdbHolder.setLayoutParams(layoutParams);
//                    break;
//                }
//                this.tdbHolder.setVisibility(8);
//                break;
//            case 2:
//                this.mZipperLock = new Type2(this.width, this.height, this.context);
//                if (!this.batteryGone || !this.dateGone) {
//                    this.tdbHolder.setVisibility(0);
//                    layoutParams = new LayoutParams((int) (0.261d * ((double) this.height)), (int) (0.225d * ((double) this.height)));
//                    layoutParams.addRule(14, -1);
//                    layoutParams.setMargins(0, this.width / 5, 0, 0);
//                    this.tdbHolder.setLayoutParams(layoutParams);
//                    break;
//                }
//                this.tdbHolder.setVisibility(8);
//                break;
//        }
        this.mZipperLock.Init(this.imgZipper, this.imgFront, this);
    }

//    private void GetNumOfMissedCalls() {
//        if (this.callPermission) {
//            int i;
//            String[] projection = new String[]{MediationMetaData.KEY_NAME, "numberlabel", "type"};
//            Cursor c = this.context.getContentResolver().query(Calls.CONTENT_URI, projection, "type=3 AND is_read=0", null, null);
//            Log.d("STATUS", "YES CALL PERMISSION");
//            if (c != null) {
//                i = 1;
//            } else {
//                i = 0;
//            }
//            if ((i & this.missedCallEnabled) != 0) {
//                c.moveToFirst();
//                Log.d("STATUS", "CALL: " + c.getCount());
//                if (c.getCount() == 0) {
//                    this.imgMissedCall.setVisibility(4);
//                    Log.d("STATUS", "CALL INVISIBLE");
//                    return;
//                }
//                this.imgMissedCall.setVisibility(0);
//                Log.d("STATUS", "CALL VISIBLE");
//                return;
//            }
//            this.imgMissedCall.setVisibility(4);
//            Log.d("STATUS", "NO CALL PERMISSION");
//            return;
//        }
//        this.imgMissedCall.setVisibility(4);
//    }

    private void GetNumOfSMS() {
        if (this.smsPermission) {
            Cursor c = this.context.getContentResolver().query(Uri.parse("content://sms"), null, "read = 0", null, null);
            Log.d("STATUS", "YES SMS PERMISSION");
            if (c == null || !this.smsEnabled) {
                this.imgMissedSms.setVisibility(4);
                Log.d("STATUS", "NO SMS PERMISSION");
                return;
            }
            Log.d("CALL AND SMS", "SMS: " + c.getCount());
            c.moveToFirst();
            Log.d("STATUS", "SMS: " + c.getCount());
            if (c.getCount() == 0) {
                this.imgMissedSms.setVisibility(4);
                Log.d("STATUS", "SMS INVISIBLE");
                return;
            }
            this.imgMissedSms.setVisibility(0);
            Log.d("STATUS", "SMS VISIBLE");
            return;
        }
        this.imgMissedSms.setVisibility(4);
    }

    private void resetPins() {
        this.LOCK_COMBINATION = "";
        for (ImageView i : this.pinImages) {
            i.setImageResource(R.drawable.pin_empty);
        }
        this.btnBackSpace.setVisibility(8);
        this.btnCancel.setVisibility(0);
    }

    public void resumeLock() {
        this.shouldUnlockScreen = false;
        this.mZipperLock.ResetImage();
        if (this.batteryGone && this.dateGone) {
            this.tdbHolder.setVisibility(8);
        } else {
            this.tdbHolder.setVisibility(0);
        }
        resetPins();
    }

    public void refreshBatteryPercentage() {
        if (this.batteryEnabled) {
            Intent batteryStatus = this.context.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
            this.txtBattery.setText(((int) Math.floor((double) (100.0f * (((float) batteryStatus.getIntExtra("level", -1)) / ((float) batteryStatus.getIntExtra("scale", -1)))))) + "%");
        }
    }

    public void updateTimeTextView() {
        if (this.dateEnabled) {
            StringBuilder timeBuilder = new StringBuilder(5);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            if (this.timeFormat == 1) {
                if (calendar.get(11) < 10) {
                    timeBuilder.append("0");
                }
                timeBuilder.append(String.valueOf(calendar.get(11)));
            } else if (calendar.get(10) == 0) {
                timeBuilder.append("12");
            } else {
                if (calendar.get(11) < 10) {
                    timeBuilder.append("0");
                }
                timeBuilder.append(String.valueOf(calendar.get(10)));
            }
            timeBuilder.append(":");
            if (calendar.get(12) < 10) {
                timeBuilder.append("0");
            }
            timeBuilder.append(String.valueOf(calendar.get(12)));
            if (this.timeFormat == 0) {
                if (calendar.get(9) == 0) {
                    timeBuilder.append(" am");
                } else {
                    timeBuilder.append(" pm");
                }
            }
            this.txtTime.setText(timeBuilder);
        }
    }

    public void updateDateTextView() {
        if (this.dateEnabled) {
            this.txtDate.setText(DateFormat.format(this.dateFormat, new Date(System.currentTimeMillis())));
        }
    }

    public void unlockScreen() {
        Log.v("LOCK_TEST", "unlockScreen()");
        Log.v("LOCK_TEST", "unlockScreen() " + this.masterActivity.getLocalClassName());
        this.pinView.setVisibility(8);
        this.mZipperLock.DestroyBitmaps();
        this.masterActivity.finish();
    }

    public void destroyLock() {
        this.masterActivity.unregisterReceiver(this.timeChangedReceiver);
    }
}
