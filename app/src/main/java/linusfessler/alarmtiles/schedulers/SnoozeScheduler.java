package linusfessler.alarmtiles.schedulers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import linusfessler.alarmtiles.R;

public class SnoozeScheduler extends Scheduler {

    private static SnoozeScheduler instance;
    public static SnoozeScheduler getInstance(Context context) {
        if (instance == null) {
            instance = new SnoozeScheduler(context);
        }
        return instance;
    }

    private SnoozeScheduler(Context context) {
        super(context);
    }

    @Override
    protected int getActivityRequestCode() {
        return 4;
    }

    @Override
    protected int getShowRequestCode() {
        return 5;
    }

    @Override
    protected int getIsScheduledKey() {
        return R.string.pref_snooze_scheduled_key;
    }

    @Override
    protected String getDurationLeftKey() {
        return "snooze_duration_left";
    }

    @Override
    protected String getTimestampKey() {
        return "snooze_timestamp";
    }

    public void schedule() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int snoozeDuration = preferences.getInt(context.getString(R.string.pref_snooze_duration_key), 0);
        schedule(context, snoozeDuration);
    }
}
