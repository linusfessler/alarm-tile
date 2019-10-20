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

    public void setName(final String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    public void setIconResourceId(final int iconResourceId) {
        this.iconResourceId = iconResourceId;
        notifyPropertyChanged(BR.iconResourceId);
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
    }

    public void init(final GeneralSettings generalSettings) {
        setName(generalSettings.getName());
        setIconResourceId(generalSettings.getIconResourceId());
    }

}
