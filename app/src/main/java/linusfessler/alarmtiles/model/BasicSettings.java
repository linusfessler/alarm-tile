package linusfessler.alarmtiles.model;

import java.io.Serializable;

import linusfessler.alarmtiles.Assert;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder
public class BasicSettings implements Serializable {

    @NonNull
    private final String name;

    private final int iconResourceId;

    private BasicSettings(final String name, final int iconResourceId) {
        Assert.isNotEmpty(name, "Name must not be null.");
        this.name = name;
        this.iconResourceId = iconResourceId;
    }

}