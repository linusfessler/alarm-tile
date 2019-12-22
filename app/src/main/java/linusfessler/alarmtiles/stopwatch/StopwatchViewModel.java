package linusfessler.alarmtiles.stopwatch;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import linusfessler.alarmtiles.TimeFormatter;

public class StopwatchViewModel extends ViewModel {

    private final StopwatchRepository repository;
    private final String tileLabel;
    private final TimeFormatter timeFormatter;

    private final LiveData<Stopwatch> stopwatchLiveData;
    private final LiveData<String> tileLabelLiveData;

    private Timer timer;

    public StopwatchViewModel(final StopwatchRepository repository, final String tileLabel, final TimeFormatter timeFormatter) {
        this.repository = repository;
        this.tileLabel = tileLabel;
        this.timeFormatter = timeFormatter;

        this.stopwatchLiveData = this.repository.getStopwatch();

        this.tileLabelLiveData = Transformations.switchMap(this.stopwatchLiveData, stopwatch -> {
            final MutableLiveData<String> tileLabelMutableLiveData = new MutableLiveData<>();

            if (stopwatch == null) {
                return tileLabelMutableLiveData;
            }

            if (!stopwatch.isEnabled()) {
                if (this.timer != null) {
                    this.timer.cancel();
                }
                return tileLabelMutableLiveData;
            }

            this.timer = new Timer(false);
            this.timer.scheduleAtFixedRate(new TimerTask() {
                long elapsedMillis = System.currentTimeMillis() - stopwatch.getStartTimeStamp();

                @Override
                public void run() {
                    final String elapsedTime = timeFormatter.format(this.elapsedMillis, TimeUnit.MILLISECONDS);
                    tileLabelMutableLiveData.postValue(tileLabel + "\n" + elapsedTime);
                    this.elapsedMillis += 10;
                }
            }, 0, 10);

            return tileLabelMutableLiveData;
        });
    }

    public LiveData<Stopwatch> getStopwatch() {
        return this.stopwatchLiveData;
    }

    public LiveData<String> getTileLabel() {
        return this.tileLabelLiveData;
    }

    public void toggle(final Stopwatch stopwatch) {
        stopwatch.toggle();
        this.repository.updateStopwatch(stopwatch);
    }
}
