package linusfessler.alarmtiles.core;

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
        setContentView(R.layout.activity_main);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(final Intent intent) {
        if (TileService.ACTION_QS_TILE_PREFERENCES.equals(intent.getAction())) {
            handleTileServiceIntent(intent);
        }
    }

    private void handleTileServiceIntent(final Intent intent) {
        final ComponentName componentName = intent.getParcelableExtra(EXTRA_COMPONENT_NAME);

        if (componentName == null || navController.getCurrentDestination() == null) {
            return;
        }

        if (componentName.getClassName().equals(SleepTimerTileService.class.getName())) {
            if (navController.getCurrentDestination().getId() != R.id.sleepTimerConfigFragment) {
                navController.navigate(R.id.sleepTimerConfigFragment);
            }
        }
    }
}
