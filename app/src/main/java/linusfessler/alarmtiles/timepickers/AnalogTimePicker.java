package linusfessler.alarmtiles.timepickers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Locale;

import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.schedulers.Scheduler;

public abstract class AnalogTimePicker extends TimePicker implements
        TimePicker.OnTimeChangedListener {

    private SharedPreferences preferences;
    private TextView hours;

    public AnalogTimePicker(Context context, AttributeSet attributes) {
        super(context, attributes);

        LinearLayout root = (LinearLayout) getChildAt(0);
        RelativeLayout timeHeader = (RelativeLayout) root.getChildAt(0);
        hours = (TextView) timeHeader.getChildAt(0);
        View radialTimePicker = root.getChildAt(1);

        radialTimePicker.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        setOnTimeChangedListener(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(context);

        if (isClock()) {
            boolean use24hFormat = preferences.getBoolean(context.getString(R.string.pref_use_24h_format_key), true);
            setIs24HourView(use24hFormat);
        } else {
            setIs24HourView(true);
        }

        int milliseconds = preferences.getInt(context.getString(getPreferenceKeyId()), 0);
        int minutes = milliseconds / 60000;
        setHour(minutes / 60);
        setMinute(minutes % 60);
        Log.d(getClass().getSimpleName(), "asldkfjaöslkefjöalskdjf");
    }

    @Override
    public void onTimeChanged(TimePicker timePicker, int hours, int minutes) {
        preferences.edit().putInt(getContext().getString(getPreferenceKeyId()), 60000 * (60 * hours + minutes)).commit();
        if (getScheduler().isScheduled(getContext())) {
            getScheduler().schedule(getContext());
        }

        if (isClock()) {
            boolean use24hFormat = preferences.getBoolean(getContext().getString(R.string.pref_use_24h_format_key), true);
            if (!use24hFormat) {
                hours = hours % 12;
            }
        }

        if (this.hours != null) {
            this.hours.setText(String.format(Locale.getDefault(), "%02d", hours));
        }
    }

    protected abstract boolean isClock();
    protected abstract int getPreferenceKeyId();
    protected abstract Scheduler getScheduler();
}
