package linusfessler.alarmtile.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

public class AlarmService extends Service {

    private static final long[] DEFAULT_VIBRATION_PATTERN = { 500, 500 };

    private Ringtone alarmTone;
    private Vibrator vibrator;

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String uriPath = preferences.getString("alarm_sound", "");
        if (!uriPath.equals("")) {
            Uri uri = Uri.parse(uriPath);
            alarmTone = RingtoneManager.getRingtone(this, uri);
            alarmTone.setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build());
            alarmTone.play();
        }

        boolean vibrate = preferences.getBoolean("vibrate", true);
        if (vibrate) {
            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            if (vibrator != null) {
                long vibrationPause = Long.parseLong(preferences.getString("pause_duration", String.valueOf(DEFAULT_VIBRATION_PATTERN[0])));
                long vibrationDuration = Long.parseLong(preferences.getString("vibration_duration", String.valueOf(DEFAULT_VIBRATION_PATTERN[1])));
                long[] vibrationPattern = new long[]{ vibrationPause, vibrationDuration };
                if (Build.VERSION.SDK_INT >= 26) {
                    vibrator.vibrate(VibrationEffect.createWaveform(vibrationPattern, 0));
                } else {
                    vibrator.vibrate(vibrationPattern, 0);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (alarmTone != null) {
            alarmTone.stop();
        }
        if (vibrator != null) {
            vibrator.cancel();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
