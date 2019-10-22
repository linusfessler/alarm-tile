package linusfessler.alarmtiles.viewmodel;

import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import linusfessler.alarmtiles.TimeFormatter;
import linusfessler.alarmtiles.model.AlarmTile;
import lombok.Getter;
import lombok.Setter;

@Getter
public class SleepSettingsViewModel extends ObservableViewModel {

    private final TimeFormatter timeFormatter = new TimeFormatter();

    @Setter
    private AlarmTile alarmTile;

    @Bindable
    public boolean isTimerEnabled() {
        return alarmTile.getSleepSettings().isTimerEnabled();
    }

    public void setTimerEnabled(final boolean timerEnabled) {
        alarmTile.getSleepSettings().setTimerEnabled(timerEnabled);
        notifyPropertyChanged(BR.timerEnabled);
    }

    @Bindable
    public int getTimerHours() {
        return alarmTile.getSleepSettings().getTimerHours();
    }

    public void setTimerHours(final int timerHours) {
        alarmTile.getSleepSettings().setTimerHours(timerHours);
        notifyPropertyChanged(BR.timerHours);
    }

    @Bindable
    public int getTimerMinutes() {
        return alarmTile.getSleepSettings().getTimerMinutes();
    }

    public void setTimerMinutes(final int timerMinutes) {
        alarmTile.getSleepSettings().setTimerMinutes(timerMinutes);
        notifyPropertyChanged(BR.timerMinutes);
    }

    @Bindable
    public boolean isEnteringDoNotDisturb() {
        return alarmTile.getSleepSettings().isEnteringDoNotDisturb();
    }

    public void setEnteringDoNotDisturb(final boolean enteringDoNotDisturb) {
        alarmTile.getSleepSettings().setEnteringDoNotDisturb(enteringDoNotDisturb);
        notifyPropertyChanged(BR.enteringDoNotDisturb);
    }

    @Bindable
    public boolean isAllowingPriorityNotifications() {
        return alarmTile.getSleepSettings().isAllowingPriorityNotifications();
    }

    public void setAllowingPriorityNotifications(final boolean allowingPriorityNotifications) {
        alarmTile.getSleepSettings().setAllowingPriorityNotifications(allowingPriorityNotifications);
        notifyPropertyChanged(BR.allowingPriorityNotifications);
    }

    @Bindable({"timerHours", "timerMinutes"})
    public String getTimerDuration() {
        return timeFormatter.format(getTimerHours(), getTimerMinutes());
    }

}
