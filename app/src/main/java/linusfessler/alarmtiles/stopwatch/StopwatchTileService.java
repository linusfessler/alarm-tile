package linusfessler.alarmtiles.stopwatch;

import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;
import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.core.App;
import linusfessler.alarmtiles.shared.TileServiceCompat;

public class StopwatchTileService extends TileService {

    @Inject
    StopwatchViewModelFactory viewModelFactory;

    private StopwatchViewModel viewModel;
    private String tileLabel;

    private final PublishSubject<Boolean> clickSubject = PublishSubject.create();
    private final CompositeDisposable disposable = new CompositeDisposable();

    @Override
    public void onCreate() {
        super.onCreate();

        ((App) getApplicationContext())
                .getStopwatchComponent()
                .inject(this);

        viewModel = viewModelFactory.create(StopwatchViewModel.class);
        tileLabel = getString(R.string.stopwatch);
    }

    @Override
    public void onClick() {
        super.onClick();
        clickSubject.onNext(true);
    }

    @Override
    public void onStartListening() {
        super.onStartListening();

        disposable.add(viewModel.isEnabled()
                .subscribe(enabled -> {
                    final int state;
                    if (enabled) {
                        state = Tile.STATE_ACTIVE;
                    } else {
                        state = Tile.STATE_INACTIVE;
                    }

                    final Tile tile = getQsTile();
                    tile.setState(state);
                    tile.updateTile();
                }));

        disposable.add(viewModel.getElapsedTime()
                .subscribe(elapsedTime -> {
                    final Tile tile = getQsTile();
                    TileServiceCompat.setSubtitle(tile, tileLabel, elapsedTime);
                    tile.updateTile();
                }));

        disposable.add(clickSubject
                .subscribe(click -> viewModel.onClick()));
    }

    @Override
    public void onStopListening() {
        disposable.clear();
        super.onStopListening();
    }

    @Override
    public void onDestroy() {
        disposable.dispose();
        super.onDestroy();
    }
}
