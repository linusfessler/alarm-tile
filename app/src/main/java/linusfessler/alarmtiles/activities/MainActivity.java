package linusfessler.alarmtiles.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.service.quicksettings.TileService;

import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.sleeptimer.SleepTimerTileService;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class MainActivity extends FragmentActivity {

    public static final String EXTRA_COMPONENT_NAME = "android.intent.extra.COMPONENT_NAME";

    private NavController navController;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        this.navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        this.handleIntent(this.getIntent());
    }

    @Override
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);
        this.handleIntent(intent);
    }

    private void handleIntent(final Intent intent) {
        if (TileService.ACTION_QS_TILE_PREFERENCES.equals(intent.getAction())) {
            this.handleTileServiceIntent(intent);
        }
    }

    private void handleTileServiceIntent(final Intent intent) {
        final ComponentName componentName = intent.getParcelableExtra(MainActivity.EXTRA_COMPONENT_NAME);

        if (componentName == null || this.navController.getCurrentDestination() == null) {
            return;
        }

        if (componentName.getClassName().equals(SleepTimerTileService.class.getName())) {
            if (this.navController.getCurrentDestination().getId() != R.id.sleepTimerConfigFragment) {
                this.navController.navigate(R.id.sleepTimerConfigFragment);
            }
        }
    }
}
