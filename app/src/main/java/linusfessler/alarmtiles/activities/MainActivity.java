package linusfessler.alarmtiles.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.service.quicksettings.TileService;
import android.widget.TimePicker;

import androidx.fragment.app.FragmentActivity;

import linusfessler.alarmtiles.DigitalTimePickerDialog;
import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.sleeptimer.SleepTimerTileService;

public class MainActivity extends FragmentActivity {

    public static final String EXTRA_COMPONENT_NAME = "android.intent.extra.COMPONENT_NAME";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        this.handleIntent(this.getIntent());
    }

    @Override
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);
        this.handleIntent(intent);
    }

    private void handleIntent(final Intent intent) {
        if (!TileService.ACTION_QS_TILE_PREFERENCES.equals(intent.getAction())) {
            return;
        }

        final ComponentName componentName = intent.getParcelableExtra(MainActivity.EXTRA_COMPONENT_NAME);
        if (componentName == null) {
            return;
        }

        if (componentName.getClassName().equals(SleepTimerTileService.class.getName())) {
            this.showSleepTimerDialog();
        }
    }

    private void showSleepTimerDialog() {
        // TODO: Create sleep timer dialog class to encapsulate logic
        // FIXME: Dismiss dialog to avoid leak
        final TimePicker.OnTimeChangedListener listener = (view, newHours, newMinutes) -> {
        };
        final DigitalTimePickerDialog timePickerDialog = new DigitalTimePickerDialog(this, listener, 0, 0, true);
        timePickerDialog.show();
    }
}
