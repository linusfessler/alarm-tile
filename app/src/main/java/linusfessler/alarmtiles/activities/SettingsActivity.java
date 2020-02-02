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
        this.getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
        Schedulers.getInstance(this).resume();
    }

    public static class SettingsFragment extends PreferenceFragment {

        private SharedPreferences preferences;

        private final Preference.OnPreferenceChangeListener vibrationDurationListener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(final Preference preference, final Object o) {
                final long millis = Long.parseLong((String) o);
                return millis > 0;
            }
        };

        private final Preference.OnPreferenceChangeListener dndEnterListener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(final Preference preference, final Object o) {
                final boolean dndEnter = (boolean) o;
                if (dndEnter && !SettingsFragment.this.checkNotificationPolicyAccess()) {
                    return false;
                }

                if (Schedulers.getInstance(SettingsFragment.this.getContext()).isScheduled()) {
                    if (dndEnter) {
                        final boolean dndPriority = SettingsFragment.this.preferences.getBoolean(SettingsFragment.this.getContext().getString(R.string.pref_dnd_priority_key), false);
                        DoNotDisturb.getInstance(SettingsFragment.this.getContext()).turnOn(dndPriority);
                    } else {
                        final boolean dndExit = SettingsFragment.this.preferences.getBoolean(SettingsFragment.this.getContext().getString(R.string.pref_dnd_exit_key), false);
                        if (dndExit) {
                            DoNotDisturb.getInstance(SettingsFragment.this.getContext()).turnOff();
                        }
                    }
                }
                return true;
            }
        };

        private final Preference.OnPreferenceChangeListener dndPriorityListener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(final Preference preference, final Object o) {
                final boolean dndPriority = (boolean) o;
                if (dndPriority && !SettingsFragment.this.checkNotificationPolicyAccess()) {
                    return false;
                }

                if (Schedulers.getInstance(SettingsFragment.this.getContext()).isScheduled()) {
                    final boolean dndEnter = SettingsFragment.this.preferences.getBoolean(SettingsFragment.this.getContext().getString(R.string.pref_dnd_enter_key), false);
                    if (dndEnter) {
                        DoNotDisturb.getInstance(SettingsFragment.this.getContext()).turnOn(dndPriority);
                    }
                }
                return true;
            }
        };

        private final Preference.OnPreferenceChangeListener hideLauncherIconListener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(final Preference preference, final Object o) {
                final boolean enabled = (boolean) o;
                if (enabled) {
                    Components.disable(SettingsFragment.this.getContext(), LauncherActivity.class);
                } else {
                    Components.enable(SettingsFragment.this.getContext(), LauncherActivity.class);
                }
                return true;
            }
        };

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.addPreferencesFromResource(R.xml.preferences);

            this.preferences = PreferenceManager.getDefaultSharedPreferences(this.getContext());

            this.findPreference(this.getString(R.string.pref_vibration_duration_key)).setOnPreferenceChangeListener(this.vibrationDurationListener);
            this.findPreference(this.getString(R.string.pref_dnd_enter_key)).setOnPreferenceChangeListener(this.dndEnterListener);
            this.findPreference(this.getString(R.string.pref_dnd_priority_key)).setOnPreferenceChangeListener(this.dndPriorityListener);
            this.findPreference(this.getString(R.string.pref_hide_launcher_icon_key)).setOnPreferenceChangeListener(this.hideLauncherIconListener);

            // remove option for flashlight if camera flash not available
            if (!this.getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                final PreferenceCategory flashlightCategory = (PreferenceCategory) this.findPreference(this.getString(R.string.pref_category_flashlight_key));
                this.getPreferenceScreen().removePreference(flashlightCategory);
            }

            // for devices running Android versions < N ...
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                // ... remove option to show notification of time left until music is going to stop
                final PreferenceCategory music = (PreferenceCategory) this.findPreference(this.getString(R.string.pref_category_music_key));
                music.removePreference(this.findPreference(this.getString(R.string.pref_show_music_notification_key)));
                // ... remove option to hide launcher icon
                final PreferenceCategory launcherCategory = (PreferenceCategory) this.findPreference(this.getString(R.string.pref_category_launcher_key));
                this.getPreferenceScreen().removePreference(launcherCategory);
            }
        }

        private boolean checkNotificationPolicyAccess() {
            if (Permissions.isNotificationPolicyAccessGranted(this.getContext())) {
                return true;
            }

            new AlertDialog.Builder(this.getContext())
                    .setTitle(this.getString(R.string.dialog_dnd_title))
                    .setMessage(this.getString(R.string.dialog_dnd_message))
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialogInterface, final int i) {
                            final Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                            SettingsFragment.this.getActivity().startActivity(intent);
                        }
                    })
                    .create()
                    .show();
            return false;
        }
    }
}
