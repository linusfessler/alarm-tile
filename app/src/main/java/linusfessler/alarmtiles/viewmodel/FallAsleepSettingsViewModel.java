package linusfessler.alarmtiles.viewmodel;

import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import linusfessler.alarmtiles.model.FallAsleepSettings;
import lombok.Getter;

@Getter
public class FallAsleepSettingsViewModel extends ObservableViewModel {

    @Bindable
    private boolean timerEnabled;

    @Bindable
    private boolean slowlyFadingMusicOut;

    public void setTimerEnabled(final boolean timerEnabled) {
        this.timerEnabled = timerEnabled;
        notifyPropertyChanged(BR.timerEnabled);
    }

    public void setSlowlyFadingMusicOut(final boolean slowlyFadingMusicOut) {
        this.slowlyFadingMusicOut = slowlyFadingMusicOut;
        notifyPropertyChanged(BR.slowlyFadingMusicOut);
    }

    /*@Bindable({"timerEnabled", "slowlyFadingMusicOut"})
    public boolean isValid() {
        return true;
    }*/

    public void reset() {
        setTimerEnabled(false);
        setSlowlyFadingMusicOut(false);
    }

    public void init(final FallAsleepSettings fallAsleepSettings) {
        setTimerEnabled(fallAsleepSettings.isTimerEnabled());
        setSlowlyFadingMusicOut(fallAsleepSettings.isSlowlyFadingMusicOut());
    }

}
