package linusfessler.alarmtile.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Calendar;
import java.util.concurrent.Executors;

import linusfessler.alarmtile.Flashlight;
import linusfessler.alarmtile.constants.PreferenceKeys;
import linusfessler.alarmtile.services.AlarmService;
import linusfessler.alarmtile.constants.BroadcastActions;
import linusfessler.alarmtile.R;
import linusfessler.alarmtile.utility.TimeFormatter;

public class AlarmActivity extends Activity implements SeekBar.OnSeekBarChangeListener {

    private ImageView snoozeImage;
    private ImageView alarmImage;
    private ImageView dismissImage;
    private Drawable rippleDrawable;

    private boolean volumeButtons;
    private boolean useFlashlight;
    private Flashlight flashlight;

    private boolean firstTouch = true;
    private boolean isTrackingTouch;
    private boolean finished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        Calendar calendar = Calendar.getInstance();
        TextView time = findViewById(R.id.time);
        time.setText(TimeFormatter.format(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));

        snoozeImage = findViewById(R.id.image_snooze);
        alarmImage = findViewById(R.id.image_alarm);
        dismissImage = findViewById(R.id.image_dismiss);

        SeekBar seekBar = findViewById(R.id.seek_bar);
        seekBar.setOnSeekBarChangeListener(this);
        onProgressChanged(seekBar, seekBar.getProgress(), false);

        rippleDrawable = (findViewById(R.id.ripple)).getBackground();

        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(BroadcastActions.ALARM_START));
        startService(new Intent(this, AlarmService.class));

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        volumeButtons = preferences.getBoolean(PreferenceKeys.VOLUME_BUTTONS, false);
        useFlashlight = preferences.getBoolean(PreferenceKeys.FLASHLIGHT, false);
        if (useFlashlight) {
            flashlight = new Flashlight(this);
            flashlight.turnOn();
        }

        final long vibrationPause = Long.parseLong(preferences.getString(PreferenceKeys.PAUSE_DURATION, String.valueOf(AlarmService.DEFAULT_VIBRATION_PATTERN[0])));
        final long vibrationDuration = Long.parseLong(preferences.getString(PreferenceKeys.VIBRATION_DURATION, String.valueOf(AlarmService.DEFAULT_VIBRATION_PATTERN[1])));

        Executors.newFixedThreadPool(1).submit(new Runnable() {
            @Override
            public void run() {
                while (!finished) {
                    try {
                        Thread.sleep(vibrationPause);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            rippleDrawable.setState(new int[] { android.R.attr.state_pressed, android.R.attr.state_enabled });
                        }
                    });
                    try {
                        Thread.sleep(vibrationDuration);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            rippleDrawable.setState(new int[] {});
                        }
                    });
                }
            }
        });
    }

    private void snooze() {
        if (useFlashlight) {
            flashlight.turnOff();
        }
        finished = true;
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(BroadcastActions.ALARM_SNOOZE));
        stopService(new Intent(this, AlarmService.class));
        finishAndRemoveTask();
    }

    private void dismiss() {
        if (useFlashlight) {
            flashlight.turnOff();
        }
        finished = true;
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(BroadcastActions.ALARM_DISMISS));
        stopService(new Intent(this, AlarmService.class));
        finishAndRemoveTask();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        dismiss();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (firstTouch) {
            firstTouch = false;
            isTrackingTouch = 25 < progress && progress < 75;
        }

        if (!isTrackingTouch) {
            seekBar.setProgress(50);
            return;
        }

        float center = seekBar.getMax() / 2f;
        progress -= center;
        float scale = Math.abs(progress) / center;
        float scaleInv = 1 - scale;
        float largeScale = 0.5f * scale + 1;

        setScale(alarmImage, scaleInv);
        if (progress > 0) {
            setScale(snoozeImage, largeScale);
            setScale(dismissImage, 1);
            setBackgroundAlpha(snoozeImage, 255 * scale);
            setBackgroundAlpha(dismissImage, 0);
        } else if (progress < 0) {
            setScale(snoozeImage, 1);
            setScale(dismissImage, largeScale);
            setBackgroundAlpha(snoozeImage, 0);
            setBackgroundAlpha(dismissImage, 255 * scale);
        } else {
            setScale(snoozeImage, 1);
            setScale(dismissImage, 1);
            setBackgroundAlpha(snoozeImage, 0);
            setBackgroundAlpha(dismissImage, 0);
        }
    }

    private void setBackgroundAlpha(View view, float alpha) {
        view.getBackground().setAlpha((int) alpha);
    }

    private void setScale(View view, float scale) {
        view.setScaleX(scale);
        view.setScaleY(scale);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        firstTouch = true;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (!isTrackingTouch) {
            return;
        }

        int progress = seekBar.getProgress();
        float center = seekBar.getMax() / 2f;
        progress -= center;
        float threshold = 0.8f * center;

        if (progress < -threshold) {
            dismiss();
        } else if (threshold < progress) {
            snooze();
        } else {
            seekBar.setProgress(50);
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (!volumeButtons) {
            return super.dispatchKeyEvent(event);
        }

        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                snooze();
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                dismiss();
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }
}
