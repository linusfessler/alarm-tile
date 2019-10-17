package linusfessler.alarmtiles.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
public class BasicSettings implements Serializable {

    public static final int MAX_NAME_LENGTH = 20;

    @Setter
    private String name;

    @Setter
    private Integer iconResourceId;

    public boolean isNameValid() {
        return name != null && !name.isEmpty();
    }

    public boolean isIconResourceIdValid() {
        return iconResourceId != null;
    }

    public boolean isValid() {
        return isNameValid() && isIconResourceIdValid();
    }

    public int getMaxNameLength() {
        return MAX_NAME_LENGTH;
    }

}
