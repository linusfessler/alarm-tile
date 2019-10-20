package linusfessler.alarmtiles.viewmodel;

import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import linusfessler.alarmtiles.TimeFormatter;
import linusfessler.alarmtiles.model.SleepSettings;
import lombok.Getter;

@Getter
public class SleepSettingsViewModel extends ObservableViewModel {

    private static final int DEFAULT_TIMER_HOURS = 8;
    private static final int DEFAULT_TIMER_MINUTES = 0;

    private final TimeFormatter timeFormatter = new TimeFormatter();

    @Bindable
    private boolean timerEnabled;

    @Bindable
    private int timerHours;

    @Bindable
    private int timerMinutes;

    @Bindable
    private boolean enteringDoNotDisturb;

    @Bindable
    private boolean allowingPriorityNotifications;

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

    public void setEnteringDoNotDisturb(final boolean enteringDoNotDisturb) {
        this.enteringDoNotDisturb = enteringDoNotDisturb;
        notifyPropertyChanged(BR.enteringDoNotDisturb);
    }

    public void setAllowingPriorityNotifications(final boolean allowingPriorityNotifications) {
        this.allowingPriorityNotifications = allowingPriorityNotifications;
        notifyPropertyChanged(BR.allowingPriorityNotifications);
    }

    @Bindable({"timerHours", "timerMinutes"})
    public String getTimerDuration() {
        return timeFormatter.format(timerHours, timerMinutes);
    }

    public void reset() {
        setTimerEnabled(false);
        setTimerHours(DEFAULT_TIMER_HOURS);
        setTimerMinutes(DEFAULT_TIMER_MINUTES);
        setEnteringDoNotDisturb(false);
        setAllowingPriorityNotifications(false);
    }

    public void init(final SleepSettings sleepSettings) {
        setTimerEnabled(sleepSettings.isTimerEnabled());
        setTimerHours(sleepSettings.getTimerHours());
        setTimerMinutes(sleepSettings.getTimerMinutes());
        setEnteringDoNotDisturb(sleepSettings.isEnteringDoNotDisturb());
        setAllowingPriorityNotifications(sleepSettings.isAllowingPriorityNotifications());
    }

}
