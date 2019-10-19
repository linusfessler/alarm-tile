package linusfessler.alarmtiles.viewmodel;

import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import java.util.Locale;

import linusfessler.alarmtiles.model.FallAsleepSettings;
import lombok.Getter;

@Getter
public class FallAsleepSettingsViewModel extends ObservableViewModel {

    private static final int DEFAULT_HOURS = 0;
    private static final int DEFAULT_MINUTES = 30;

    @Bindable
    private boolean timerEnabled;

    @Bindable
    private int hours;

    @Bindable
    private int minutes;

    @Bindable
    private boolean slowlyFadingMusicOut;

    public void setTimerEnabled(final boolean timerEnabled) {
        this.timerEnabled = timerEnabled;
        notifyPropertyChanged(BR.timerEnabled);
    }

    public void setHours(final int hours) {
        this.hours = hours;
        notifyPropertyChanged(BR.hours);
    }

    public void setMinutes(final int minutes) {
        this.minutes = minutes;
        notifyPropertyChanged(BR.minutes);
    }

    public void setSlowlyFadingMusicOut(final boolean slowlyFadingMusicOut) {
        this.slowlyFadingMusicOut = slowlyFadingMusicOut;
        notifyPropertyChanged(BR.slowlyFadingMusicOut);
    }

    @Bindable({"hours", "minutes"})
    public String getTime() {
        return String.format(Locale.getDefault(), "%02d:%02d", hours, minutes);
    }

    public void reset() {
        setTimerEnabled(false);
        setHours(DEFAULT_HOURS);
        setMinutes(DEFAULT_MINUTES);
        setSlowlyFadingMusicOut(false);
    }

    public void init(final FallAsleepSettings fallAsleepSettings) {
        setTimerEnabled(fallAsleepSettings.isTimerEnabled());
        setHours(fallAsleepSettings.getHours());
        setMinutes(fallAsleepSettings.getMinutes());
        setSlowlyFadingMusicOut(fallAsleepSettings.isSlowlyFadingMusicOut());
    }

}
