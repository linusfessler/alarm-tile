package linusfessler.alarmtiles.timer;

import android.os.CountDownTimer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import linusfessler.alarmtiles.TimeFormatter;

@Singleton
public class TimerViewModel extends ViewModel {

    private final TimerRepository repository;
    private final String tileLabel;
    private final TimeFormatter timeFormatter;

    private final LiveData<Timer> timerLiveData;
    private final LiveData<String> tileLabelLiveData;

    private CountDownTimer countdown;

    public TimerViewModel(final TimerRepository repository, final String tileLabel, final TimeFormatter timeFormatter) {
        this.repository = repository;
        this.tileLabel = tileLabel;
        this.timeFormatter = timeFormatter;

        this.timerLiveData = this.repository.getTimer();

        this.tileLabelLiveData = Transformations.switchMap(this.timerLiveData, timer -> {
            final MutableLiveData<String> tileLabelMutableLiveData = new MutableLiveData<>();

            if (timer == null) {
                return tileLabelMutableLiveData;
            }

            if (!timer.isEnabled()) {
                if (this.countdown != null) {
                    this.countdown.cancel();
                    tileLabelMutableLiveData.postValue(tileLabel);
                }
                return tileLabelMutableLiveData;
            }

            final long millisElapsed = System.currentTimeMillis() - timer.getStartTimeStamp();
            final long millisLeft = timer.getDuration() - millisElapsed;
            this.countdown = new CountDownTimer(millisLeft, 1000) {
                @Override
                public void onTick(final long millisUntilFinished) {
                    final String formattedTimeLeft = timeFormatter.format(millisUntilFinished, TimeUnit.SECONDS);
                    tileLabelMutableLiveData.postValue(tileLabel + "\n" + formattedTimeLeft);
                }

                @Override
                public void onFinish() {
                    tileLabelMutableLiveData.postValue(tileLabel);

                    timer.setEnabled(false);
                    TimerViewModel.this.repository.updateTimer(timer);

                    // TODO: Trigger alarm
                }
            };
            this.countdown.start();

            return tileLabelMutableLiveData;
        });
    }

    public LiveData<Timer> getTimer() {
        return this.timerLiveData;
    }

    public LiveData<String> getTileLabel() {
        return this.tileLabelLiveData;
    }

    public void toggle(final Timer timer) {
        timer.toggle();
        this.repository.updateTimer(timer);
    }
}
