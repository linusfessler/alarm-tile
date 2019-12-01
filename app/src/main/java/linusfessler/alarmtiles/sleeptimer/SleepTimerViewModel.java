package linusfessler.alarmtiles.sleeptimer;

import android.app.Application;
import android.os.CountDownTimer;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import linusfessler.alarmtiles.R;

public class SleepTimerViewModel extends AndroidViewModel {

    private static final long ONE_SECOND = 1000;

    private final SleepTimerRepository repository;

    private final LiveData<SleepTimer> sleepTimerLiveData;
    private final LiveData<String> tileLabelLiveData;

    private CountDownTimer countdown;

    public SleepTimerViewModel(@NonNull final Application application) {
        super(application);
        this.repository = new SleepTimerRepository(application);

        this.sleepTimerLiveData = this.repository.getSleepTimer();

        final String tileLabel = application.getString(R.string.sleep_timer);
        //final TimeFormatter timeFormatter = new TimeFormatter("h", "h", "min", "min");
        this.tileLabelLiveData = Transformations.switchMap(this.sleepTimerLiveData, sleepTimer -> {
            final MutableLiveData<String> tileLabelMutableLiveData = new MutableLiveData<>();

            if (sleepTimer == null) {
                return tileLabelMutableLiveData;
            }

            if (!sleepTimer.isEnabled()) {
                if (this.countdown != null) {
                    this.countdown.cancel();
                    tileLabelMutableLiveData.postValue(tileLabel);
                }
                return tileLabelMutableLiveData;
            }

            final long millisElapsed = System.currentTimeMillis() - sleepTimer.getStartTimeStamp();
            final long millisLeft = sleepTimer.getDuration() - millisElapsed;
            this.countdown = new CountDownTimer(millisLeft, SleepTimerViewModel.ONE_SECOND) {
                @Override
                public void onTick(final long millisUntilFinished) {
                    //tileLabelMutableLiveData.postValue(timeFormatter.format(millisUntilFinished));
                    tileLabelMutableLiveData.postValue("" + millisUntilFinished / 1000);
                }

                @Override
                public void onFinish() {
                    tileLabelMutableLiveData.postValue(tileLabel);

                    sleepTimer.setEnabled(false);
                    SleepTimerViewModel.this.repository.updateSleepTimer(sleepTimer);

                    // TODO: Trigger alarm
                }
            };
            this.countdown.start();

            return tileLabelMutableLiveData;
        });
    }

    public LiveData<SleepTimer> getSleepTimer() {
        return this.sleepTimerLiveData;
    }

    public LiveData<String> getTileLabel() {
        return this.tileLabelLiveData;
    }

    public void toggleSleepTimer(final SleepTimer sleepTimer) {
        sleepTimer.toggle();
        this.repository.updateSleepTimer(sleepTimer);
    }
}
