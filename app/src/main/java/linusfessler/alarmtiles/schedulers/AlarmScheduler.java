package linusfessler.alarmtiles.schedulers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Calendar;
import java.util.GregorianCalendar;

import linusfessler.alarmtiles.R;

public class AlarmScheduler extends Scheduler {

    private static AlarmScheduler instance;
    public static AlarmScheduler getInstance() {
        if (instance == null) {
            instance = new AlarmScheduler();
        }
        return instance;
    }

    @Override
    protected int getActivityRequestCode() {
        return 0;
    }

    @Override
    protected int getShowRequestCode() {
        return 1;
    }

    @Override
    protected int getIsScheduledKey() {
        return R.string.pref_key_alarm_scheduled;
    }

    @Override
    protected String getDurationLeftKey() {
        return "alarm_duration_left";
    }

    @Override
    protected String getTimestampKey() {
        return "alarm_timestamp";
    }

    @Override
    public void schedule(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int milliseconds = preferences.getInt(context.getString(R.string.pref_alarm_time_key), 0);
        int minutes = milliseconds / 60000;
        Calendar calendar = new GregorianCalendar();
        int currentMinutes = 60 * calendar.get(Calendar.HOUR_OF_DAY) + calendar.get(Calendar.MINUTE);
        minutes = (minutes - currentMinutes + 1440) % 1440;
        super.schedule(context, 60000 * minutes);
    }
}
