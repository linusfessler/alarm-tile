package linusfessler.alarmtiles.viewmodel;

import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import linusfessler.alarmtiles.TimeFormatter;
import linusfessler.alarmtiles.model.FallAsleepSettings;
import lombok.Getter;

@Getter
public class FallAsleepSettingsViewModel extends ObservableViewModel {

    private static final int DEFAULT_TIMER_HOURS = 0;
    private static final int DEFAULT_TIMER_MINUTES = 30;

    private final TimeFormatter timeFormatter = new TimeFormatter();

    @Bindable
    private boolean timerEnabled;

    @Bindable
    private int timerHours;

    @Bindable
    private int timerMinutes;

    @Bindable
    private boolean slowlyFadingMusicOut;

    public void setTimerEnabled(final boolean timerEnabled) {
        this.timerEnabled = timerEnabled;
        notifyPropertyChanged(BR.timerEnabled);
    }

    public void setTimerHours(final int timerHours) {
        this.timerHours = timerHours;
        notifyPropertyChanged(BR.timerHours);
    }

    public void setTimerMinutes(final int timerMinutes) {
        this.timerMinutes = timerMinutes;
        notifyPropertyChanged(BR.timerMinutes);
    }

    public void setSlowlyFadingMusicOut(final boolean slowlyFadingMusicOut) {
        this.slowlyFadingMusicOut = slowlyFadingMusicOut;
        notifyPropertyChanged(BR.slowlyFadingMusicOut);
    }

    @Bindable({"timerHours", "timerMinutes"})
    public String getTimerDuration() {
        return timeFormatter.format(timerHours, timerMinutes);
    }

    public void reset() {
        setTimerEnabled(false);
        setTimerHours(DEFAULT_TIMER_HOURS);
        setTimerMinutes(DEFAULT_TIMER_MINUTES);
        setSlowlyFadingMusicOut(false);
    }

    public void init(final FallAsleepSettings fallAsleepSettings) {
        setTimerEnabled(fallAsleepSettings.isTimerEnabled());
        setTimerHours(fallAsleepSettings.getTimerHours());
        setTimerMinutes(fallAsleepSettings.getTimerMinutes());
        setSlowlyFadingMusicOut(fallAsleepSettings.isSlowlyFadingMusicOut());
    }

    public FallAsleepSettings getFallAsleepSettings() {
        return FallAsleepSettings.builder()
                .timerEnabled(isTimerEnabled())
                .timerHours(getTimerHours())
                .timerMinutes(getTimerMinutes())
                .slowlyFadingMusicOut(isSlowlyFadingMusicOut())
                .build();
    }

}
