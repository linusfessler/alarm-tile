package linusfessler.alarmtiles.stopwatch;

import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;
import linusfessler.alarmtiles.App;
import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.TileServiceCompat;

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
        ((App) this.getApplicationContext()).getAppComponent().inject(this);

        this.viewModel = this.viewModelFactory.create(StopwatchViewModel.class);
        this.tileLabel = this.getString(R.string.stopwatch);
    }

    @Override
    public void onClick() {
        super.onClick();
        this.clickSubject.onNext(true);
    }

    @Override
    public void onStartListening() {
        super.onStartListening();

        this.disposable.add(this.viewModel.getStopwatch().subscribe(stopwatch -> {
            final int state;
            if (stopwatch.isEnabled()) {
                state = Tile.STATE_ACTIVE;
            } else {
                state = Tile.STATE_INACTIVE;
            }

            final Tile tile = this.getQsTile();
            tile.setState(state);
            tile.updateTile();
        }));

        this.disposable.add(this.viewModel.getElapsedTime().subscribe(elapsedTime -> {
            final Tile tile = this.getQsTile();
            TileServiceCompat.setSubtitle(tile, this.tileLabel, elapsedTime);
            tile.updateTile();
        }));

        this.disposable.add(this.clickSubject
                .subscribe(click -> this.viewModel.onClick()));
    }

    @Override
    public void onStopListening() {
        this.disposable.clear();
        super.onStopListening();
    }

    @Override
    public void onDestroy() {
        this.disposable.dispose();
        super.onDestroy();
    }
}
