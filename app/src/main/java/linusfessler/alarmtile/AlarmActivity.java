package linusfessler.alarmtile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.Calendar;

public class AlarmActivity extends Activity {

    private static final long[] VIBRATION_PATTERN = { 500, 500 };

    private Ringtone alarmTone;
    private Vibrator vibrator;

    private boolean snoozed, dismissed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.activity_alarm);

        Calendar calendar = Calendar.getInstance();
        TextView time = findViewById(R.id.time);
        time.setText(TimeFormatter.format(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));

        startAlarm();
    }

    @Override
    protected  void onStop() {
        super.onStop();
        if (!snoozed && !dismissed) {
            dismiss();
        }
    }

    private void startAlarm() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String uriPath = preferences.getString("alarm_sound", "");
        if (!uriPath.equals("")) {
            Uri uri = Uri.parse(uriPath);
            alarmTone = RingtoneManager.getRingtone(this, uri);
            alarmTone.setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build());
            alarmTone.play();
        }

        boolean vibrate = preferences.getBoolean("alarm_vibrate", true);
        if (vibrate) {
            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            if (vibrator != null) {
                if (Build.VERSION.SDK_INT >= 26) {
                    vibrator.vibrate(VibrationEffect.createWaveform(VIBRATION_PATTERN, 0));
                } else {
                    vibrator.vibrate(VIBRATION_PATTERN, 0);
                }
            }
        }
    }

    private void stopAlarm() {
        if (alarmTone != null) {
            alarmTone.stop();
        }
        if (vibrator != null) {
            vibrator.cancel();
        }
    }

    private void snooze() {
        snoozed = true;
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(BroadcastActions.SNOOZED));
        stopAlarm();
        finish();
    }

    private void dismiss() {
        dismissed = true;
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(BroadcastActions.DISMISSED));
        stopAlarm();
        finish();
    }

    public void onSnooze(View view) {
        snooze();
    }

    public void onDismiss(View view) {
        dismiss();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
