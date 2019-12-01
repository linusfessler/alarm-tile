package linusfessler.alarmtiles.stopwatch;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.Timer;
import java.util.TimerTask;

import linusfessler.alarmtiles.R;

public class StopwatchViewModel extends AndroidViewModel {

    private static final long ONE_SECOND = 1000;

    private final StopwatchRepository repository;

    private final LiveData<Stopwatch> stopwatchLiveData;
    private final LiveData<String> tileLabelLiveData;

    private Timer timer;

    public StopwatchViewModel(@NonNull final Application application) {
        super(application);
        this.repository = new StopwatchRepository(application);

        this.stopwatchLiveData = this.repository.getStopwatch();

        final String tileLabel = application.getString(R.string.stopwatch);
        //final TimeFormatter timeFormatter = new TimeFormatter("h", "h", "min", "min");
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
                long secondsElapsed = (System.currentTimeMillis() - stopwatch.getStartTimeStamp()) / 1000;

                @Override
                public void run() {
                    tileLabelMutableLiveData.postValue(tileLabel + "\n" + this.secondsElapsed++);
                }
            }, 0, StopwatchViewModel.ONE_SECOND);

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
        if (stopwatch.isEnabled()) {
            this.disable(stopwatch);
        } else {
            this.enable(stopwatch);
        }
    }

    private void enable(final Stopwatch stopwatch) {
        stopwatch.setEnabled(true);
        stopwatch.setStartTimeStamp(System.currentTimeMillis());
        this.repository.updateStopwatch(stopwatch);
    }

    private void disable(final Stopwatch stopwatch) {
        stopwatch.setEnabled(false);
        stopwatch.setStartTimeStamp(null);
        this.repository.updateStopwatch(stopwatch);
    }
}
