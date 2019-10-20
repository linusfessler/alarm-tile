package linusfessler.alarmtiles.viewmodel;

import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import linusfessler.alarmtiles.TimeFormatter;
import linusfessler.alarmtiles.model.SleepSettings;
import lombok.Getter;

@Getter
public class SleepSettingsViewModel extends ObservableViewModel {

    private static final int DEFAULT_HOURS = 8;
    private static final int DEFAULT_MINUTES = 0;

    private final TimeFormatter timeFormatter = new TimeFormatter();

    @Bindable
    private boolean timerEnabled;

    @Bindable
    private int hours;

    @Bindable
    private int minutes;

    @Bindable
    private boolean enteringDoNotDisturb;

    @Bindable
    private boolean allowingPriorityNotifications;

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

    public void setEnteringDoNotDisturb(final boolean enteringDoNotDisturb) {
        this.enteringDoNotDisturb = enteringDoNotDisturb;
        notifyPropertyChanged(BR.enteringDoNotDisturb);
    }

    public void setAllowingPriorityNotifications(final boolean allowingPriorityNotifications) {
        this.allowingPriorityNotifications = allowingPriorityNotifications;
        notifyPropertyChanged(BR.allowingPriorityNotifications);
    }

    @Bindable({"hours", "minutes"})
    public String getDuration() {
        return timeFormatter.format(hours, minutes);
    }

    public void reset() {
        setTimerEnabled(false);
        setHours(DEFAULT_HOURS);
        setMinutes(DEFAULT_MINUTES);
        setEnteringDoNotDisturb(false);
        setAllowingPriorityNotifications(false);
    }

    public void init(final SleepSettings sleepSettings) {
        setTimerEnabled(sleepSettings.isTimerEnabled());
        setHours(sleepSettings.getHours());
        setMinutes(sleepSettings.getMinutes());
        setEnteringDoNotDisturb(sleepSettings.isEnteringDoNotDisturb());
        setAllowingPriorityNotifications(sleepSettings.isAllowingPriorityNotifications());
    }

}
