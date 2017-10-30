package linusfessler.alarmtile.activity;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import linusfessler.alarmtile.AlarmScheduler;
import linusfessler.alarmtile.BroadcastActions;
import linusfessler.alarmtile.LauncherActivity;
import linusfessler.alarmtile.PermissionUtility;
import linusfessler.alarmtile.R;

public class PreferenceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainFragment()).commit();
    }

    public static class MainFragment extends PreferenceFragment {

        private Preference.OnPreferenceChangeListener dndChangeListener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                boolean value = (boolean) o;
                if (value) {
                    if (!PermissionUtility.isNotificationPolicyAccessGranted(getContext())) {
                        new AlertDialog.Builder(getContext())
                                .setTitle(getString(R.string.dialog_dnd_title))
                                .setMessage(getString(R.string.dialog_dnd_message))
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                                        getContext().startActivity(intent);
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

        private Preference.OnPreferenceChangeListener alarmDelayChangeListener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent(BroadcastActions.ALARM_CHANGED));
                return true;
            }
        };

        private Preference.OnPreferenceChangeListener snoozeDelayChangeListener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                if (AlarmScheduler.snoozing) {
                    LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent(BroadcastActions.SNOOZED));
                }
                return true;
            }
        };

        private Preference.OnPreferenceChangeListener vibrationDurationChangeListener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                long millis = Long.parseLong((String) o);
                return millis > 0;
            }
        };

        private Preference.OnPreferenceChangeListener hideLauncherIconChangeListener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                boolean enabled = (boolean) o;
                PackageManager packageManager = getContext().getPackageManager();
                packageManager.setComponentEnabledSetting(
                        new ComponentName(getContext(), LauncherActivity.class),
                        enabled ? PackageManager.COMPONENT_ENABLED_STATE_DISABLED : PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP
                );
                if (enabled) {
                    startActivity(new Intent(getContext(), PreferenceActivity.class));
                }
                return true;
            }
        };

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            PreferenceManager.setDefaultValues(getContext(), R.xml.preferences, false);

            findPreference("dnd_enter").setOnPreferenceChangeListener(dndChangeListener);
            findPreference("alarm_delay").setOnPreferenceChangeListener(alarmDelayChangeListener);
            findPreference("snooze_delay").setOnPreferenceChangeListener(snoozeDelayChangeListener);
            findPreference("vibration_duration").setOnPreferenceChangeListener(vibrationDurationChangeListener);
            findPreference("hide_launcher_icon").setOnPreferenceChangeListener(hideLauncherIconChangeListener);
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
