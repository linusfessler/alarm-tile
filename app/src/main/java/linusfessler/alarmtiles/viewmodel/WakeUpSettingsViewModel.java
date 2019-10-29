package linusfessler.alarmtiles.viewmodel;

import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import linusfessler.alarmtiles.TimeFormatter;
import linusfessler.alarmtiles.TimeOfDayFormatter;
import linusfessler.alarmtiles.model.AlarmTile;
import lombok.Getter;
import lombok.Setter;

@Getter
public class WakeUpSettingsViewModel extends ObservableViewModel {

    private final TimeOfDayFormatter timeOfDayFormatter = new TimeOfDayFormatter();
    private final TimeFormatter timeFormatter = new TimeFormatter();

    @Setter
    private AlarmTile alarmTile;

    @Setter
    private boolean is24Hours;

    @Bindable
    public boolean isAlarmEnabled() {
        return alarmTile.getWakeUpSettings().isAlarmEnabled();
    }

    public void setAlarmEnabled(final boolean alarmEnabled) {
        alarmTile.getWakeUpSettings().setAlarmEnabled(alarmEnabled);
        notifyPropertyChanged(BR.alarmEnabled);
    }

    @Bindable
    public int getAlarmHour() {
        return alarmTile.getWakeUpSettings().getAlarmHour();
    }

    public void setAlarmHour(final int alarmHour) {
        alarmTile.getWakeUpSettings().setAlarmHour(alarmHour);
        notifyPropertyChanged(BR.alarmHour);
    }

    @Bindable
    public int getAlarmMinute() {
        return alarmTile.getWakeUpSettings().getAlarmMinute();
    }

    public void setAlarmMinute(final int alarmMinute) {
        alarmTile.getWakeUpSettings().setAlarmMinute(alarmMinute);
        notifyPropertyChanged(BR.alarmMinute);
    }

    @Bindable({"alarmHour", "alarmMinute"})
    public String getAlarmTime() {
        return timeOfDayFormatter.format(getAlarmHour(), getAlarmMinute(), is24Hours);
    }

    @Bindable
    public boolean isTimerEnabled() {
        return alarmTile.getWakeUpSettings().isTimerEnabled();
    }

    public void setTimerEnabled(final boolean timerEnabled) {
        alarmTile.getWakeUpSettings().setTimerEnabled(timerEnabled);
        notifyPropertyChanged(BR.timerEnabled);
    }

    @Bindable
    public int getTimerHours() {
        return alarmTile.getWakeUpSettings().getTimerHours();
    }

    public void setTimerHours(final int timerHours) {
        alarmTile.getWakeUpSettings().setTimerHours(timerHours);
        notifyPropertyChanged(BR.timerHours);
    }

    @Bindable
    public int getTimerMinutes() {
        return alarmTile.getWakeUpSettings().getTimerMinutes();
    }

    public void setTimerMinutes(final int timerMinutes) {
        alarmTile.getWakeUpSettings().setTimerMinutes(timerMinutes);
        notifyPropertyChanged(BR.timerMinutes);
    }

    @Bindable({"timerHours", "timerMinutes"})
    public String getTimerDuration() {
        return timeFormatter.format(getTimerHours(), getTimerMinutes());
    }

    @Bindable({"alarmEnabled", "timerEnabled"})
    public boolean isWakingUp() {
        return isAlarmEnabled() || isTimerEnabled();
    }

    @Bindable
    public boolean isSnoozeEnabled() {
        return alarmTile.getWakeUpSettings().isSnoozeEnabled();
    }

    public void setSnoozeEnabled(final boolean snoozeEnabled) {
        alarmTile.getWakeUpSettings().setSnoozeEnabled(snoozeEnabled);
        notifyPropertyChanged(BR.snoozeEnabled);
    }

    @Bindable({"snoozeEnabled", "wakingUp"})
    public boolean isSnoozeEnabledAndWakingUp() {
        return isSnoozeEnabled() && isWakingUp();
    }

    @Bindable
    public int getSnoozeHours() {
        return alarmTile.getWakeUpSettings().getSnoozeHours();
    }

    public void setSnoozeHours(final int snoozeHours) {
        alarmTile.getWakeUpSettings().setSnoozeHours(snoozeHours);
        notifyPropertyChanged(BR.snoozeHours);
    }

    @Bindable
    public int getSnoozeMinutes() {
        return alarmTile.getWakeUpSettings().getSnoozeMinutes();
    }

    public void setSnoozeMinutes(final int snoozeMinutes) {
        alarmTile.getWakeUpSettings().setSnoozeMinutes(snoozeMinutes);
        notifyPropertyChanged(BR.snoozeMinutes);
    }

    @Bindable({"snoozeHours", "snoozeMinutes"})
    public String getSnoozeDuration() {
        return timeFormatter.format(getSnoozeHours(), getSnoozeMinutes());
    }

    @Bindable
    public boolean isVolumeTimerEnabled() {
        return alarmTile.getWakeUpSettings().isVolumeTimerEnabled();
    }

    public void setVolumeTimerEnabled(final boolean volumeTimerEnabled) {
        alarmTile.getWakeUpSettings().setVolumeTimerEnabled(volumeTimerEnabled);
        notifyPropertyChanged(BR.volumeTimerEnabled);
    }

    @Bindable({"volumeTimerEnabled", "wakingUp"})
    public boolean isVolumeTimerEnabledAndWakingUp() {
        return isVolumeTimerEnabled() && isWakingUp();
    }

    @Bindable
    public int getVolumeTimerHours() {
        return alarmTile.getWakeUpSettings().getVolumeTimerHours();
    }

    public void setVolumeTimerHours(final int volumeTimerHours) {
        alarmTile.getWakeUpSettings().setVolumeTimerHours(volumeTimerHours);
        notifyPropertyChanged(BR.volumeTimerHours);
    }

    @Bindable
    public int getVolumeTimerMinutes() {
        return alarmTile.getWakeUpSettings().getVolumeTimerMinutes();
    }

    public void setVolumeTimerMinutes(final int volumeTimerMinutes) {
        alarmTile.getWakeUpSettings().setVolumeTimerMinutes(volumeTimerMinutes);
        notifyPropertyChanged(BR.volumeTimerMinutes);
    }

    @Bindable({"volumeTimerHours", "volumeTimerMinutes"})
    public String getVolumeTimerDuration() {
        return timeFormatter.format(getVolumeTimerHours(), getVolumeTimerMinutes());
    }

    @Bindable
    public boolean isDismissTimerEnabled() {
        return alarmTile.getWakeUpSettings().isDismissTimerEnabled();
    }

    public void setDismissTimerEnabled(final boolean dismissTimerEnabled) {
        alarmTile.getWakeUpSettings().setDismissTimerEnabled(dismissTimerEnabled);
        notifyPropertyChanged(BR.dismissTimerEnabled);
    }

    @Bindable({"dismissTimerEnabled", "wakingUp"})
    public boolean isDismissTimerEnabledAndWakingUp() {
        return isDismissTimerEnabled() && isWakingUp();
    }

    @Bindable
    public int getDismissTimerHours() {
        return alarmTile.getWakeUpSettings().getDismissTimerHours();
    }

    public void setDismissTimerHours(final int dismissTimerHours) {
        alarmTile.getWakeUpSettings().setDismissTimerHours(dismissTimerHours);
        notifyPropertyChanged(BR.dismissTimerHours);
    }

    @Bindable
    public int getDismissTimerMinutes() {
        return alarmTile.getWakeUpSettings().getDismissTimerMinutes();
    }

    public void setDismissTimerMinutes(final int dismissTimerMinutes) {
        alarmTile.getWakeUpSettings().setDismissTimerMinutes(dismissTimerMinutes);
        notifyPropertyChanged(BR.dismissTimerMinutes);
    }

    @Bindable({"dismissTimerHours", "dismissTimerMinutes"})
    public String getDismissTimerDuration() {
        return timeFormatter.format(getDismissTimerHours(), getDismissTimerMinutes());
    }

    @Bindable
    public boolean isVibrating() {
        return alarmTile.getWakeUpSettings().isVibrating();
    }

    public void setVibrating(final boolean vibrating) {
        alarmTile.getWakeUpSettings().setVibrating(vibrating);
        notifyPropertyChanged(BR.vibrating);
    }

    @Bindable
    public boolean isTurningOnFlashlight() {
        return alarmTile.getWakeUpSettings().isTurningOnFlashlight();
    }

    public void setTurningOnFlashlight(final boolean turningOnFlashlight) {
        alarmTile.getWakeUpSettings().setTurningOnFlashlight(turningOnFlashlight);
        notifyPropertyChanged(BR.turningOnFlashlight);
    }

}
