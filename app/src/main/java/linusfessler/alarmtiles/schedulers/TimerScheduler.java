package linusfessler.alarmtiles.schedulers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import linusfessler.alarmtiles.R;

public class TimerScheduler extends Scheduler {

    private static TimerScheduler instance;
    public static TimerScheduler getInstance() {
        if (instance == null) {
            instance = new TimerScheduler();
        }
        return instance;
    }

    @Override
    protected int getActivityRequestCode() {
        return 2;
    }

    @Override
    protected int getShowRequestCode() {
        return 3;
    }

    @Override
    protected int getIsScheduledKey() {
        return R.string.pref_key_timer_scheduled;
    }

    @Override
    protected String getDurationLeftKey() {
        return "timer_duration_left";
    }

    @Override
    protected String getTimestampKey() {
        return "timer_timestamp";
    }

    public void schedule(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int timerDuration = preferences.getInt(context.getString(R.string.pref_timer_duration_key), 0);
        schedule(context, timerDuration);
    }
}
