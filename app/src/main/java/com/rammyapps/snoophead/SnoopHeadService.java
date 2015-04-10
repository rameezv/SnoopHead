package com.rammyapps.snoophead;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.IOException;

public class SnoopHeadService extends Service {

    private WindowManager windowManager;
    private ImageView chatHead;
    private ImageView cancelHead;
    WindowManager.LayoutParams params;
    WindowManager.LayoutParams paramsCancel;
    SharedPreferences sharedpreferences;
    public static final String PREFS = "com.rammyapps.snoophead.prefs";
    public static final String cHEAD = "com.rammyapps.snoophead.prefs.head";
    public static final String cSOUND = "com.rammyapps.snoophead.prefs.sound";
    public static final String cENABLED = "com.rammyapps.snoophead.prefs.enabled";
    public static final String cTIME = "com.rammyapps.snoophead.prefs.time";
    MediaPlayer mp = new MediaPlayer();

    @Override public IBinder onBind(Intent intent) {
        // Not used
        return null;
    }

    @Override public void onCreate() {
        super.onCreate();

        sharedpreferences = getSharedPreferences(PREFS, Context.MODE_PRIVATE);

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        mp = new MediaPlayer();

        chatHead = new ImageView(this);
        chatHead.setImageResource(sharedpreferences.getInt(cHEAD, R.drawable.head_snoop_default));

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        params.x = 0;
        params.y = 100;

        windowManager.addView(chatHead, params);

        //

        chatHead.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();

                        cancelHead = new ImageView(SnoopHeadService.this);
                        cancelHead.setImageResource(R.drawable.button_close_normal);

                        paramsCancel = new WindowManager.LayoutParams(
                                WindowManager.LayoutParams.WRAP_CONTENT,
                                WindowManager.LayoutParams.WRAP_CONTENT,
                                WindowManager.LayoutParams.TYPE_PHONE,
                                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                                PixelFormat.TRANSLUCENT);

                        paramsCancel.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                        paramsCancel.x = 0;
                        paramsCancel.y = 100;

                        windowManager.addView(cancelHead, paramsCancel);

                        return true;
                    case MotionEvent.ACTION_UP:
                        // swed
                        int canLoc[] = new int[2];
                        cancelHead.getLocationOnScreen(canLoc);
                        if ((params.x > (cancelHead.getLeft()- 250)) && (params.x < (cancelHead.getLeft() + 250))) {
                            if ((params.y > (canLoc[1] - 250)) && (params.y < (canLoc[1] + 250))) {
                                if (sharedpreferences.getString(cTIME, "1620").equals("NULL")) {
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putBoolean(cENABLED, false);
                                    editor.apply();
                                }
                                SnoopHeadService.this.stopSelf();
                            }
                        }
                        windowManager.removeView(cancelHead);
                        return playMedia();
                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        int canLoc2[] = new int[2];
                        cancelHead.getLocationOnScreen(canLoc2);
                        if ((params.x > (cancelHead.getLeft()- 250)) && (params.x < (cancelHead.getLeft() + 250))) {
                            if ((params.y > (canLoc2[1] - 250)) && (params.y < (canLoc2[1] + 250))) {
                                cancelHead.setImageResource(R.drawable.button_close_active);
                            } else {
                                cancelHead.setImageResource(R.drawable.button_close_normal);
                            }
                        } else {
                            cancelHead.setImageResource(R.drawable.button_close_normal);
                        }
                        windowManager.updateViewLayout(cancelHead, paramsCancel);
                        windowManager.updateViewLayout(chatHead, params);
                        return true;
                }
                return false;
            }
        });

        if (!sharedpreferences.getString(cTIME, "1620").equals("NULL")) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    SnoopHeadService.this.stopSelf();
                }
            }, 60000);
        }

        if(playMedia()) {
            // dance
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (chatHead != null) windowManager.removeView(chatHead);
    }

    private boolean playMedia() {
        if(mp.isPlaying()) {
            mp.stop();
        }

        try {
            mp.reset();
            AssetFileDescriptor afd;
            afd = getAssets().openFd(sharedpreferences.getString(cSOUND, "swed_default.mp3"));
            mp.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            mp.prepare();
            mp.start();
            return true;
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}