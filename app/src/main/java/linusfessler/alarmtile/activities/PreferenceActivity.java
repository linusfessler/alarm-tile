package linusfessler.alarmtile.activities;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import linusfessler.alarmtile.constants.BroadcastActions;
import linusfessler.alarmtile.utility.Permissions;
import linusfessler.alarmtile.constants.PreferenceKeys;
import linusfessler.alarmtile.R;

public class PreferenceActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    private Menu menu;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainFragment()).commit();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public static class MainFragment extends PreferenceFragment {

        private SharedPreferences preferences;

        private Preference.OnPreferenceChangeListener alarmDelayListener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(BroadcastActions.ALARM_DELAY_CHANGED));
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

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            PreferenceManager.setDefaultValues(getActivity(), R.xml.preferences, false);

            preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

            findPreference(PreferenceKeys.ALARM_DELAY).setOnPreferenceChangeListener(alarmDelayListener);
            findPreference(PreferenceKeys.SNOOZE_DELAY).setOnPreferenceChangeListener(snoozeDelayListener);
            findPreference(PreferenceKeys.DND_ENTER).setOnPreferenceChangeListener(dndEnterListener);
            findPreference(PreferenceKeys.VIBRATION_DURATION).setOnPreferenceChangeListener(vibrationDurationListener);

            // only give option to hide launcher icon for devices running Android versions < N
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                PreferenceScreen preferenceScreen = (PreferenceScreen) findPreference(PreferenceKeys.SCREEN_MAIN);
                PreferenceCategory launcherCategory = (PreferenceCategory) findPreference(PreferenceKeys.CATEGORY_LAUNCHER);
                preferenceScreen.removePreference(launcherCategory);
            } else {
                findPreference(PreferenceKeys.HIDE_LAUNCHER_ICON).setOnPreferenceChangeListener(hideLauncherIconListener);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_preference, menu);
        this.menu = menu;
        boolean alarmSet = preferences.getBoolean("alarm_set", false);
        boolean snoozeSet = preferences.getBoolean("snooze_set", false);
        setMenuItemsVisible(alarmSet || snoozeSet);
        return true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            boolean alarmSet = preferences.getBoolean("alarm_set", false);
            boolean snoozeSet = preferences.getBoolean("snooze_set", false);
            setMenuItemsVisible(alarmSet || snoozeSet);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        setMenuItemsVisible(item.getGroupId() == R.id.group_alarm);

        switch (item.getItemId()) {
            case R.id.menu_alarm:
                preferences.edit().putBoolean(PreferenceKeys.ALARM_SET, true).apply();
                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(BroadcastActions.ALARM_SET));
                return true;
            case R.id.menu_snooze:
                preferences.edit().putBoolean(PreferenceKeys.SNOOZE_SET, true).apply();
                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(BroadcastActions.SNOOZE_SET));
                return true;
            case R.id.menu_dismiss:
                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(BroadcastActions.ALARM_DISMISS));
                return true;
            case R.id.menu_about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            default:
                Log.d(getClass().getSimpleName(), "default...");
                return super.onOptionsItemSelected(item);
        }
    }

    private void setMenuItemsVisible(boolean visible) {
        menu.setGroupVisible(R.id.group_alarm, !visible);
        menu.findItem(R.id.menu_dismiss).setVisible(visible);
    }
}
