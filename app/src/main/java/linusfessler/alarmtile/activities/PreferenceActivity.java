package linusfessler.alarmtile.activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import linusfessler.alarmtile.constants.BroadcastActions;
import linusfessler.alarmtile.utility.Permissions;
import linusfessler.alarmtile.constants.PreferenceKeys;
import linusfessler.alarmtile.R;

public class PreferenceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainFragment()).commit();
    }

    public static class MainFragment extends PreferenceFragment {

        private SwitchPreference alarmSwitch;
        private SwitchPreference snoozeSwitch;

        private SharedPreferences preferences;

        private Preference.OnPreferenceChangeListener setAlarmListener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(BroadcastActions.ALARM_SET));
                return true;
            }
        };

        private Preference.OnPreferenceChangeListener alarmDelayListener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(BroadcastActions.ALARM_DELAY_CHANGED));
                return true;
            }
        };

        private Preference.OnPreferenceChangeListener setSnoozeListener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(BroadcastActions.SNOOZE_SET));
                return true;
            }
        };

        private Preference.OnPreferenceChangeListener snoozeDelayListener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                if (preferences.getBoolean(PreferenceKeys.SNOOZE_SET, false)) {
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(BroadcastActions.SNOOZE_DELAY_CHANGED));
                }
                return true;
            }
        };

        private Preference.OnPreferenceChangeListener dndEnterListener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                boolean value = (boolean) o;
                if (value) {
                    if (!Permissions.isNotificationPolicyAccessGranted(getActivity())) {
                        new AlertDialog.Builder(getActivity())
                                .setTitle(getString(R.string.dialog_dnd_title))
                                .setMessage(getString(R.string.dialog_dnd_message))
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                                        getActivity().startActivity(intent);
                                    }
                                })
                                .create()
                                .show();
                        return false;
                    }
                }
                return true;
            }
        };

        private Preference.OnPreferenceChangeListener vibrationDurationListener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                long millis = Long.parseLong((String) o);
                return millis > 0;
            }
        };

        private Preference.OnPreferenceChangeListener hideLauncherIconListener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                boolean enabled = (boolean) o;
                PackageManager packageManager = getActivity().getPackageManager();
                packageManager.setComponentEnabledSetting(
                        new ComponentName(getActivity(), LauncherActivity.class),
                        enabled ? PackageManager.COMPONENT_ENABLED_STATE_DISABLED : PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP
                );
                if (enabled) {
                    startActivity(new Intent(getActivity(), PreferenceActivity.class));
                }
                return true;
            }
        };

        private BroadcastReceiver updateSwitchesReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                alarmSwitch.setChecked(intent.getBooleanExtra(PreferenceKeys.ALARM_SET, false));
                snoozeSwitch.setChecked(intent.getBooleanExtra(PreferenceKeys.SNOOZE_SET, false));
            }
        };

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            PreferenceManager.setDefaultValues(getActivity(), R.xml.preferences, false);

            alarmSwitch = (SwitchPreference) findPreference(PreferenceKeys.ALARM_SET);
            snoozeSwitch = (SwitchPreference) findPreference(PreferenceKeys.SNOOZE_SET);

            preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

            findPreference(PreferenceKeys.ALARM_SET).setOnPreferenceChangeListener(setAlarmListener);
            findPreference(PreferenceKeys.ALARM_DELAY).setOnPreferenceChangeListener(alarmDelayListener);
            findPreference(PreferenceKeys.SNOOZE_SET).setOnPreferenceChangeListener(setSnoozeListener);
            findPreference(PreferenceKeys.SNOOZE_DELAY).setOnPreferenceChangeListener(snoozeDelayListener);
            findPreference(PreferenceKeys.DND_ENTER).setOnPreferenceChangeListener(dndEnterListener);
            findPreference(PreferenceKeys.VIBRATION_DURATION).setOnPreferenceChangeListener(vibrationDurationListener);
            findPreference(PreferenceKeys.HIDE_LAUNCHER_ICON).setOnPreferenceChangeListener(hideLauncherIconListener);

            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updateSwitchesReceiver, new IntentFilter(BroadcastActions.UPDATE_SWITCHES));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
