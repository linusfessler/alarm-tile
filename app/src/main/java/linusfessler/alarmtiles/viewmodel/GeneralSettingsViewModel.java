package linusfessler.alarmtiles.viewmodel;

import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.model.GeneralSettings;
import lombok.Getter;

@Getter
public class GeneralSettingsViewModel extends ObservableViewModel {

    public static final int MAX_NAME_LENGTH = 20;
    public static final String NAME_ERROR_TEXT = "Please enter a name";
    public static final int DEFAULT_ICON_RESOURCE_ID = R.drawable.ic_alarm_24px;

    @Bindable
    private String name;

    @Bindable
    private Integer iconResourceId;

    @Bindable
    private boolean showingNotification;

    @Bindable
    private boolean graduallyIncreasingVolume;

    @Bindable
    private boolean vibrating;

    @Bindable
    private boolean turningOnFlashlight;

    public void setName(final String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    public void setIconResourceId(final int iconResourceId) {
        this.iconResourceId = iconResourceId;
        notifyPropertyChanged(BR.iconResourceId);
    }

    public void setShowingNotification(final boolean showingNotification) {
        this.showingNotification = showingNotification;
        notifyPropertyChanged(BR.showingNotification);
    }

    public void setGraduallyIncreasingVolume(final boolean graduallyIncreasingVolume) {
        this.graduallyIncreasingVolume = graduallyIncreasingVolume;
        notifyPropertyChanged(BR.graduallyIncreasingVolume);
    }

    public void setVibrating(final boolean vibrating) {
        this.vibrating = vibrating;
        notifyPropertyChanged(BR.vibrating);
    }

    public void setTurningOnFlashlight(final boolean turningOnFlashlight) {
        this.turningOnFlashlight = turningOnFlashlight;
        notifyPropertyChanged(BR.turningOnFlashlight);
    }

    @Bindable("name")
    public boolean isNameValid() {
        return name != null && !name.isEmpty();
    }

    @Bindable("name")
    public boolean isNameErrorEnabled() {
        return name != null && name.isEmpty();
    }

    @Bindable("name")
    public String getNameErrorText() {
        return isNameErrorEnabled() ? NAME_ERROR_TEXT : null;
    }

    @Bindable("iconResourceId")
    public boolean isIconResourceIdValid() {
        return iconResourceId != null;
    }

    @Bindable({"name", "iconResourceId"})
    public boolean isValid() {
        return isNameValid() && isIconResourceIdValid();
    }

    @Bindable
    public int getMaxNameLength() {
        return MAX_NAME_LENGTH;
    }

    public void reset() {
        setName(null);
        setIconResourceId(DEFAULT_ICON_RESOURCE_ID);
        setShowingNotification(true);
        setGraduallyIncreasingVolume(false);
        setVibrating(false);
        setTurningOnFlashlight(false);
    }

    public void init(final GeneralSettings generalSettings) {
        setName(generalSettings.getName());
        setIconResourceId(generalSettings.getIconResourceId());
        setShowingNotification(generalSettings.isShowingNotification());
        setGraduallyIncreasingVolume(generalSettings.isGraduallyIncreasingVolume());
        setVibrating(generalSettings.isVibrating());
        setTurningOnFlashlight(generalSettings.isTurningOnFlashlight());
    }

    public GeneralSettings getGeneralSettings() {
        return GeneralSettings.builder()
                .name(getName())
                .iconResourceId(getIconResourceId())
                .showingNotification(isShowingNotification())
                .graduallyIncreasingVolume(isGraduallyIncreasingVolume())
                .vibrating(isVibrating())
                .turningOnFlashlight(isTurningOnFlashlight())
                .build();
    }

}
