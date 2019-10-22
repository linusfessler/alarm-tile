package linusfessler.alarmtiles.viewmodel;

import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import linusfessler.alarmtiles.TimeFormatter;
import linusfessler.alarmtiles.model.AlarmTile;
import lombok.Getter;
import lombok.Setter;

@Getter
public class FallAsleepSettingsViewModel extends ObservableViewModel {

    private final TimeFormatter timeFormatter = new TimeFormatter();

    @Setter
    private AlarmTile alarmTile;

    @Bindable
    public boolean isTimerEnabled() {
        return alarmTile.getFallAsleepSettings().isTimerEnabled();
    }

    public void setTimerEnabled(final boolean timerEnabled) {
        alarmTile.getFallAsleepSettings().setTimerEnabled(timerEnabled);
        notifyPropertyChanged(BR.timerEnabled);
    }

    @Bindable
    public int getTimerHours() {
        return alarmTile.getFallAsleepSettings().getTimerHours();
    }

    public void setTimerHours(final int timerHours) {
        alarmTile.getFallAsleepSettings().setTimerHours(timerHours);
        notifyPropertyChanged(BR.timerHours);
    }

    @Bindable
    public int getTimerMinutes() {
        return alarmTile.getFallAsleepSettings().getTimerMinutes();
    }

    public void setTimerMinutes(final int timerMinutes) {
        alarmTile.getFallAsleepSettings().setTimerMinutes(timerMinutes);
        notifyPropertyChanged(BR.timerMinutes);
    }

    @Bindable
    public boolean isSlowlyFadingMusicOut() {
        return alarmTile.getFallAsleepSettings().isSlowlyFadingMusicOut();
    }

    public void setSlowlyFadingMusicOut(final boolean slowlyFadingMusicOut) {
        alarmTile.getFallAsleepSettings().setSlowlyFadingMusicOut(slowlyFadingMusicOut);
        notifyPropertyChanged(BR.slowlyFadingMusicOut);
    }

    @Bindable({"timerHours", "timerMinutes"})
    public String getTimerDuration() {
        return timeFormatter.format(getTimerHours(), getTimerMinutes());
    }

}
