package linusfessler.alarmtiles.fragments;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.TimePickerDialog;
import linusfessler.alarmtiles.schedulers.Scheduler;
import linusfessler.alarmtiles.utility.TimeFormatter;

public abstract class SchedulerFragment extends Fragment {

    private FloatingActionButton fab;
    private Icon positiveIcon;
    private Icon negativeIcon;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(), container, false);
    }

    public void onPageSelected(FloatingActionButton fab) {
        this.fab = fab;
        positiveIcon = Icon.createWithResource(fab.getContext(), getPositiveIconId());
        negativeIcon = Icon.createWithResource(fab.getContext(), getNegativeIconId());

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(fab.getContext());

        boolean isEnabled = preferences.getBoolean(fab.getContext().getString(getScheduledKeyId()), false);
        updateFab(isEnabled);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFabClick();
            }
        });
    }

    protected void onFabClick() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(fab.getContext());
        boolean isEnabled = preferences.getBoolean(fab.getContext().getString(getScheduledKeyId()), false);
        isEnabled = !isEnabled;
        updateFab(isEnabled);
        preferences.edit().putBoolean(fab.getContext().getString(getScheduledKeyId()), isEnabled).commit();
        if (isEnabled) {
            getScheduler().schedule();
        } else {
            getScheduler().dismiss();
        }
    }

    private void updateFab(boolean isEnabled) {
        fab.setImageIcon(isEnabled ? negativeIcon : positiveIcon);
    }

    @Override
    public void onResume() {
        super.onResume();

        View root = getView();
        if (root == null) {
            return;
        }

        View button = root.findViewById(R.id.button);
        final TextView time = button.findViewById(R.id.time);

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        boolean useAnalogClocks = preferences.getBoolean(getString(R.string.pref_use_analog_clocks_key), true);
        boolean use24hFormat = preferences.getBoolean(getString(R.string.pref_use_24h_format_key), true);

        int milliseconds = preferences.getInt(getString(getTimeKeyId()), 0);
        int minutes = milliseconds / 60000;
        int hours = minutes / 60;
        minutes = minutes % 60;

        final TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), useAnalogClocks, use24hFormat, hours, minutes);
        timePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int hours = timePickerDialog.getHour();
                int minutes = timePickerDialog.getMinute();
                time.setText(TimeFormatter.format(hours, minutes));
                int milliseconds = 60000 * (60 * hours + minutes);
                preferences.edit().putInt(getContext().getString(getTimeKeyId()), milliseconds).apply();
                boolean isEnabled = preferences.getBoolean(getContext().getString(getScheduledKeyId()), false);
                if (isEnabled) {
                    getScheduler().schedule();
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog.show();
            }
        });

        timePickerDialog.update(useAnalogClocks, use24hFormat, hours, minutes);
        time.setText(TimeFormatter.format(hours, minutes));
    }

    protected abstract int getLayoutId();
    protected abstract int getPositiveIconId();
    protected abstract int getNegativeIconId();
    protected abstract int getTimeKeyId();
    protected abstract int getScheduledKeyId();
    protected abstract Scheduler getScheduler();
}