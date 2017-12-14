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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import linusfessler.alarmtile.AlarmScheduler;
import linusfessler.alarmtile.DoNotDisturb;
import linusfessler.alarmtile.utility.Permissions;
import linusfessler.alarmtile.R;

public class MainActivity extends AppCompatActivity {

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

        private Preference.OnPreferenceChangeListener timerDurationListener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                if (AlarmScheduler.timerIsScheduled(getActivity())) {
                    AlarmScheduler.scheduleTimer(getActivity(), (int) o);
                }
                return true;
            }
        };

        private Preference.OnPreferenceChangeListener snoozeDurationListener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                if (AlarmScheduler.snoozeIsScheduled(getActivity())) {
                    AlarmScheduler.scheduleSnooze(getActivity(), (int) o);
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

        private Preference.OnPreferenceChangeListener dndEnterListener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                boolean dndEnter = (boolean) o;
                if (dndEnter && !checkNotificationPolicyAccess()) {
                    return false;
                }

                if (AlarmScheduler.anyIsScheduled(getActivity())) {
                    if (dndEnter) {
                        boolean dndPriority = preferences.getBoolean(getActivity().getString(R.string.key_dnd_priority), false);
                        DoNotDisturb.turnOn(getActivity(), dndPriority);
                    } else {
                        boolean dndExit = preferences.getBoolean(getActivity().getString(R.string.key_dnd_exit), false);
                        if (dndExit) {
                            DoNotDisturb.turnOff(getActivity());
                        }
                    }
                }
                return true;
            }
        };

        private Preference.OnPreferenceChangeListener dndPriorityListener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                boolean dndPriority = (boolean) o;
                if (dndPriority && !checkNotificationPolicyAccess()) {
                    return false;
                }

                if (AlarmScheduler.anyIsScheduled(getActivity())) {
                    boolean dndEnter = preferences.getBoolean(getActivity().getString(R.string.key_dnd_enter), false);
                    if (dndEnter) {
                        DoNotDisturb.turnOn(getActivity(), dndPriority);
                    }
                }
                return true;
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
                return true;
            }
        };

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            PreferenceManager.setDefaultValues(getActivity(), R.xml.preferences, false);

            preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

            findPreference(getString(R.string.key_timer_duration)).setOnPreferenceChangeListener(timerDurationListener);
            findPreference(getString(R.string.key_snooze_duration)).setOnPreferenceChangeListener(snoozeDurationListener);
            findPreference(getString(R.string.key_vibration_duration)).setOnPreferenceChangeListener(vibrationDurationListener);
            findPreference(getString(R.string.key_dnd_enter)).setOnPreferenceChangeListener(dndEnterListener);
            findPreference(getString(R.string.key_dnd_priority)).setOnPreferenceChangeListener(dndPriorityListener);
            findPreference(getString(R.string.key_hide_launcher_icon)).setOnPreferenceChangeListener(hideLauncherIconListener);

            // remove option for flashlight if camera flash not available
            if (!getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                PreferenceScreen preferenceScreen = (PreferenceScreen) findPreference(getString(R.string.key_screen_main));
                PreferenceCategory flashlightCategory = (PreferenceCategory) findPreference(getString(R.string.key_category_flashlight));
                preferenceScreen.removePreference(flashlightCategory);
            }

            // remove option to hide launcher icon for devices running Android versions < N
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                PreferenceScreen preferenceScreen = (PreferenceScreen) findPreference(getString(R.string.key_screen_main));
                PreferenceCategory launcherCategory = (PreferenceCategory) findPreference(getString(R.string.key_category_launcher));
                preferenceScreen.removePreference(launcherCategory);
            }
        }

        private boolean checkNotificationPolicyAccess() {
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
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;
        setMenuItemsVisible(AlarmScheduler.anyIsScheduled(this));
        return true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            setMenuItemsVisible(AlarmScheduler.anyIsScheduled(this));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        setMenuItemsVisible(item.getGroupId() == R.id.group_alarm);

        switch (item.getItemId()) {
            case R.id.menu_alarm:
                AlarmScheduler.scheduleTimer(this);
                return true;
            case R.id.menu_snooze:
                AlarmScheduler.scheduleSnooze(this);
                return true;
            case R.id.menu_dismiss:
                AlarmScheduler.dismiss(this);
                return true;
            case R.id.menu_about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setMenuItemsVisible(boolean visible) {
        menu.setGroupVisible(R.id.group_alarm, !visible);
        menu.findItem(R.id.menu_dismiss).setVisible(visible);
    }
}
