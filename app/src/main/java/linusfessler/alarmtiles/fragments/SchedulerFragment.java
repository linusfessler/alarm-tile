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
    private boolean useAnalogClocks;
    private boolean use24hFormat;
    private int hours;
    private int minutes;
    private TimePickerDialog timePickerDialog;
    private DialogInterface.OnClickListener positiveOnClickListener;
    private DialogInterface.OnDismissListener onDismissListener;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(), container, false);
    }

    public void onPageSelected(FloatingActionButton fab) {
        if (fab == null) {
            return;
        }

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
        preferences.edit().putBoolean(fab.getContext().getString(getScheduledKeyId()), isEnabled).apply();
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

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        useAnalogClocks = preferences.getBoolean(getString(R.string.pref_use_analog_clocks_key), false);
        use24hFormat = preferences.getBoolean(getString(R.string.pref_use_24h_format_key), false);

        int milliseconds = preferences.getInt(getString(getTimeKeyId()), 0);
        minutes = milliseconds / 60000;
        hours = minutes / 60;
        minutes = minutes % 60;

        View button = root.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog.show();
            }
        });

        final TextView time = button.findViewById(R.id.time);
        time.setText(TimeFormatter.format(hours, minutes));

        positiveOnClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                hours = timePickerDialog.getHour();
                minutes = timePickerDialog.getMinute();

                time.setText(TimeFormatter.format(hours, minutes));
                int milliseconds = 60000 * (60 * hours + minutes);
                preferences.edit().putInt(getContext().getString(getTimeKeyId()), milliseconds).apply();
                boolean isEnabled = preferences.getBoolean(getContext().getString(getScheduledKeyId()), false);
                if (isEnabled) {
                    getScheduler().schedule();
                }
            }
        };
        onDismissListener = new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                resetTimePickerDialog();
            }
        };

        resetTimePickerDialog();
    }

    private void resetTimePickerDialog() {
        if (timePickerDialog != null && !useAnalogClocks) {
            // no need to reset timePickerAnalog for digital clocks
            return;
        }
        timePickerDialog = new TimePickerDialog(getContext(), useAnalogClocks, use24hFormat, hours, minutes);
        // work-around for positive button not working, probably a bug
        // neutral -> negative
        timePickerDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "CANCEL", (DialogInterface.OnClickListener) null);
        // negative -> positive
        timePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "OK", positiveOnClickListener);
        // positive -> hide
        timePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "", (DialogInterface.OnClickListener) null);

        timePickerDialog.setOnDismissListener(onDismissListener);
    }

    protected abstract int getLayoutId();
    protected abstract int getPositiveIconId();
    protected abstract int getNegativeIconId();
    protected abstract int getTimeKeyId();
    protected abstract int getScheduledKeyId();
    protected abstract Scheduler getScheduler();
}