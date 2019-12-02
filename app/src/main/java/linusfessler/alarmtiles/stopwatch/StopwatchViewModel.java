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
import linusfessler.alarmtiles.TimeFormatter;

public class StopwatchViewModel extends AndroidViewModel {

    private final StopwatchRepository repository;

    private final LiveData<Stopwatch> stopwatchLiveData;
    private final LiveData<String> tileLabelLiveData;

    private Timer timer;

    public StopwatchViewModel(@NonNull final Application application) {
        super(application);
        this.repository = new StopwatchRepository(application);

        this.stopwatchLiveData = this.repository.getStopwatch();

        final String tileLabel = application.getString(R.string.stopwatch);
        final TimeFormatter timeFormatter = new TimeFormatter();
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
                    final String elapsedTime = timeFormatter.format(this.elapsedMillis, true);
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
