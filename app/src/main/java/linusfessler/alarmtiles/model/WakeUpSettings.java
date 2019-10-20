package linusfessler.alarmtiles.model;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WakeUpSettings implements Serializable {

    private final boolean alarmEnabled;
    private final int alarmHour;
    private final int alarmMinute;

    private WakeUpSettings(final boolean alarmEnabled, final int alarmHour, final int alarmMinute) {
        this.alarmEnabled = alarmEnabled;
        this.alarmHour = alarmHour;
        this.alarmMinute = alarmMinute;
    }

}
