package linusfessler.alarmtiles.sleeptimer;

import android.content.Context;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.view.ContextThemeWrapper;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.core.App;
import linusfessler.alarmtiles.shared.TileServiceCompat;

public class SleepTimerTileService extends TileService {

    @Inject
    SleepTimerViewModel viewModel;

    @Inject
    InputMethodManager inputMethodManager;

    private SleepTimerStartDialog startDialog;
    private String tileLabel;

    private final CompositeDisposable disposable = new CompositeDisposable();

    @Override
    public void onCreate() {
        super.onCreate();

        ((App) getApplicationContext())
                .getSleepTimerComponent()
                .inject(this);

        // Wrap context for compatibility between material components and tile service
        final Context context = new ContextThemeWrapper(this, R.style.AppTheme);
        startDialog = new SleepTimerStartDialog(context, inputMethodManager, viewModel);
        tileLabel = getString(R.string.sleep_timer);
    }

    @Override
    public void onClick() {
        super.onClick();
        disposable.add(viewModel.getSleepTimer()
                .firstElement()
                .subscribe(sleepTimer -> {
                    if (sleepTimer.isEnabled()) {
                        viewModel.dispatch(new SleepTimerEvent.Cancel());
                    } else {
                        showDialog(startDialog);
                    }
                }));
    }

    @Override
    public void onStartListening() {
        super.onStartListening();

        disposable.add(viewModel.getSleepTimer()
                .subscribe(sleepTimer -> {
                    final int state;
                    if (sleepTimer.isEnabled()) {
                        state = Tile.STATE_ACTIVE;
                    } else {
                        state = Tile.STATE_INACTIVE;
                    }

                    final Tile tile = getQsTile();
                    tile.setState(state);
                    tile.updateTile();
                }));

        disposable.add(viewModel.getTimeLeft()
                .subscribe(timeLeft -> {
                    final Tile tile = getQsTile();
                    TileServiceCompat.setSubtitle(tile, tileLabel, timeLeft);
                    tile.updateTile();
                }));
    }

    @Override
    public void onStopListening() {
        super.onStopListening();
        disposable.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
        startDialog.dismiss();
    }
}
