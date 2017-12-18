package linusfessler.alarmtiles.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import linusfessler.alarmtiles.schedulers.Scheduler;

public abstract class SchedulerFragment extends Fragment {

    protected Context context;
    private FloatingActionButton fab;
    private SharedPreferences preferences;
    private Icon positiveIcon;
    private Icon negativeIcon;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(), container, false);
    }

    public void updateFab(Context context, FloatingActionButton fab) {
        this.context = context;
        this.fab = fab;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        positiveIcon = Icon.createWithResource(context, getPositiveIconId());
        negativeIcon = Icon.createWithResource(context, getNegativeIconId());

        boolean isEnabled = preferences.getBoolean(context.getString(getPreferenceKeyId()), false);
        updateFab(isEnabled);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFabClick();
            }
        });
    }

    protected void onFabClick() {
        boolean isEnabled = preferences.getBoolean(context.getString(getPreferenceKeyId()), false);
        isEnabled = !isEnabled;
        preferences.edit().putBoolean(context.getString(getPreferenceKeyId()), isEnabled).apply();
        updateFab(isEnabled);
        if (isEnabled) {
            getScheduler().schedule(context);
        } else {
            getScheduler().dismiss(context);
        }
    }

    private void updateFab(boolean enabled) {
        fab.setImageIcon(enabled ? negativeIcon : positiveIcon);
    }

    protected abstract int getLayoutId();
    protected abstract int getPositiveIconId();
    protected abstract int getNegativeIconId();
    protected abstract int getPreferenceKeyId();
    protected abstract Scheduler getScheduler();
}