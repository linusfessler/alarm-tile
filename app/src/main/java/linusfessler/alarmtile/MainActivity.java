package linusfessler.alarmtile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

// TODO: Feature requests / donate activity content
// TODO: Fix dismiss/snooze
// TODO: Don't return to main activity
// TODO: Option to hide launcher icon
// TODO: If snoozing, also listen for snooze_delay changes
// TODO: Active tile?

public class MainActivity extends AppCompatActivity {

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

        private Preference.OnPreferenceChangeListener alarmChangeListener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                Log.d(MainActivity.class.getSimpleName(), "asldkfjl");
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent(BroadcastActions.ALARM_CHANGED));
                return true;
            }
        };

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            findPreference("dnd_enter").setOnPreferenceChangeListener(dndChangeListener);
            findPreference("alarm_delay").setOnPreferenceChangeListener(alarmChangeListener);
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
            case R.id.feature_requests:
                startActivity(new Intent(this, FeatureRequestsActivity.class));
                return true;
            case R.id.donate:
                startActivity(new Intent(this, DonateActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
