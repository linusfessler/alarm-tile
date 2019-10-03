package linusfessler.alarmtiles.activities;

import android.app.Activity;
import android.app.AlertDialog;
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

import linusfessler.alarmtiles.DoNotDisturb;
import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.schedulers.Schedulers;
import linusfessler.alarmtiles.utility.Components;
import linusfessler.alarmtiles.utility.Permissions;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
        Schedulers.getInstance(this).resume();
    }

    public static class SettingsFragment extends PreferenceFragment {

        private SharedPreferences preferences;

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

                if (Schedulers.getInstance(getContext()).isScheduled()) {
                    if (dndEnter) {
                        boolean dndPriority = preferences.getBoolean(getContext().getString(R.string.pref_dnd_priority_key), false);
                        DoNotDisturb.getInstance(getContext()).turnOn(dndPriority);
                    } else {
                        boolean dndExit = preferences.getBoolean(getContext().getString(R.string.pref_dnd_exit_key), false);
                        if (dndExit) {
                            DoNotDisturb.getInstance(getContext()).turnOff();
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

                if (Schedulers.getInstance(getContext()).isScheduled()) {
                    boolean dndEnter = preferences.getBoolean(getContext().getString(R.string.pref_dnd_enter_key), false);
                    if (dndEnter) {
                        DoNotDisturb.getInstance(getContext()).turnOn(dndPriority);
                    }
                }
                return true;
            }
        };

        private Preference.OnPreferenceChangeListener hideLauncherIconListener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                boolean enabled = (boolean) o;
                if (enabled) {
                    Components.disable(getContext(), LauncherActivity.class);
                } else {
                    Components.enable(getContext(), LauncherActivity.class);
                }
                return true;
            }
        };

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

            findPreference(getString(R.string.pref_vibration_duration_key)).setOnPreferenceChangeListener(vibrationDurationListener);
            findPreference(getString(R.string.pref_dnd_enter_key)).setOnPreferenceChangeListener(dndEnterListener);
            findPreference(getString(R.string.pref_dnd_priority_key)).setOnPreferenceChangeListener(dndPriorityListener);
            findPreference(getString(R.string.pref_hide_launcher_icon_key)).setOnPreferenceChangeListener(hideLauncherIconListener);

            // remove option for flashlight if camera flash not available
            if (!getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                PreferenceCategory flashlightCategory = (PreferenceCategory) findPreference(getString(R.string.pref_category_flashlight_key));
                getPreferenceScreen().removePreference(flashlightCategory);
            }

            // for devices running Android versions < N ...
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                // ... remove option to show notification of time left until music is going to stop
                PreferenceCategory music = (PreferenceCategory) findPreference(getString(R.string.pref_category_music_key));
                music.removePreference(findPreference(getString(R.string.pref_show_music_notification_key)));
                // ... remove option to hide launcher icon
                PreferenceCategory launcherCategory = (PreferenceCategory) findPreference(getString(R.string.pref_category_launcher_key));
                getPreferenceScreen().removePreference(launcherCategory);
            }
        }

        private boolean checkNotificationPolicyAccess() {
            if (Permissions.isNotificationPolicyAccessGranted(getContext())) {
                return true;
            }

            new AlertDialog.Builder(getContext())
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
}
