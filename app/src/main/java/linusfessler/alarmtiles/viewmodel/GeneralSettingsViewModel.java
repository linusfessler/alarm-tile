package linusfessler.alarmtiles.viewmodel;

import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import linusfessler.alarmtiles.TimeFormatter;
import linusfessler.alarmtiles.model.AlarmTile;
import lombok.Getter;
import lombok.Setter;

@Getter
public class GeneralSettingsViewModel extends ObservableViewModel {

    private final TimeFormatter timeFormatter = new TimeFormatter();

    public static final String NAME_ERROR_TEXT = "Please enter a name";

    @Setter
    private AlarmTile alarmTile;

    @Bindable
    public String getName() {
        return alarmTile.getGeneralSettings().getName();
    }

    public void setName(final String name) {
        alarmTile.getGeneralSettings().setName(name);
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public int getIconResourceId() {
        return alarmTile.getGeneralSettings().getIconResourceId();
    }

    public void setIconResourceId(final int iconResourceId) {
        alarmTile.getGeneralSettings().setIconResourceId(iconResourceId);
        notifyPropertyChanged(BR.iconResourceId);
    }

    @Bindable
    public boolean isShowingNotification() {
        return alarmTile.getGeneralSettings().isShowingNotification();
    }

    public void setShowingNotification(final boolean showingNotification) {
        alarmTile.getGeneralSettings().setShowingNotification(showingNotification);
        notifyPropertyChanged(BR.showingNotification);
    }

    @Bindable
    public boolean isVolumeTimerEnabled() {
        return alarmTile.getGeneralSettings().isVolumeTimerEnabled();
    }

    public void setVolumeTimerEnabled(final boolean volumeTimerEnabled) {
        alarmTile.getGeneralSettings().setVolumeTimerEnabled(volumeTimerEnabled);
        notifyPropertyChanged(BR.volumeTimerEnabled);
    }

    @Bindable
    public int getVolumeTimerHours() {
        return alarmTile.getGeneralSettings().getVolumeTimerHours();
    }

    public void setVolumeTimerHours(final int volumeTimerHours) {
        alarmTile.getGeneralSettings().setVolumeTimerHours(volumeTimerHours);
        notifyPropertyChanged(BR.volumeTimerHours);
    }

    @Bindable
    public int getVolumeTimerMinutes() {
        return alarmTile.getGeneralSettings().getVolumeTimerMinutes();
    }

    public void setVolumeTimerMinutes(final int volumeTimerMinutes) {
        alarmTile.getGeneralSettings().setVolumeTimerMinutes(volumeTimerMinutes);
        notifyPropertyChanged(BR.volumeTimerMinutes);
    }

    @Bindable
    public boolean isDismissTimerEnabled() {
        return alarmTile.getGeneralSettings().isDismissTimerEnabled();
    }

    public void setDismissTimerEnabled(final boolean dismissTimerEnabled) {
        alarmTile.getGeneralSettings().setDismissTimerEnabled(dismissTimerEnabled);
        notifyPropertyChanged(BR.dismissTimerEnabled);
    }

    @Bindable
    public int getDismissTimerHours() {
        return alarmTile.getGeneralSettings().getDismissTimerHours();
    }

    public void setDismissTimerHours(final int dismissTimerHours) {
        alarmTile.getGeneralSettings().setDismissTimerHours(dismissTimerHours);
        notifyPropertyChanged(BR.dismissTimerHours);
    }

    @Bindable
    public int getDismissTimerMinutes() {
        return alarmTile.getGeneralSettings().getDismissTimerMinutes();
    }

    public void setDismissTimerMinutes(final int dismissTimerMinutes) {
        alarmTile.getGeneralSettings().setDismissTimerMinutes(dismissTimerMinutes);
        notifyPropertyChanged(BR.dismissTimerMinutes);
    }

    @Bindable
    public boolean isVibrating() {
        return alarmTile.getGeneralSettings().isVibrating();
    }

    public void setVibrating(final boolean vibrating) {
        alarmTile.getGeneralSettings().setVibrating(vibrating);
        notifyPropertyChanged(BR.vibrating);
    }

    @Bindable
    public boolean isTurningOnFlashlight() {
        return alarmTile.getGeneralSettings().isTurningOnFlashlight();
    }

    public void setTurningOnFlashlight(final boolean turningOnFlashlight) {
        alarmTile.getGeneralSettings().setTurningOnFlashlight(turningOnFlashlight);
        notifyPropertyChanged(BR.turningOnFlashlight);
    }

    @Bindable({"volumeTimerHours", "volumeTimerMinutes"})
    public String getVolumeTimerDuration() {
        return timeFormatter.format(getVolumeTimerHours(), getVolumeTimerMinutes());
    }

    @Bindable({"dismissTimerHours", "dismissTimerMinutes"})
    public String getDismissTimerDuration() {
        return timeFormatter.format(getDismissTimerHours(), getDismissTimerMinutes());
    }

    @Bindable("name")
    public boolean isNameValid() {
        return getName() != null && !getName().isEmpty();
    }

    @Bindable("name")
    public boolean isNameErrorEnabled() {
        return getName() != null && getName().isEmpty();
    }

    @Bindable("name")
    public String getNameErrorText() {
        return isNameErrorEnabled() ? NAME_ERROR_TEXT : null;
    }

    @Bindable("iconResourceId")
    public boolean isIconResourceIdValid() {
        return true;
    }

    @Bindable({"name", "iconResourceId"})
    public boolean isValid() {
        return isNameValid() && isIconResourceIdValid();
    }

}
