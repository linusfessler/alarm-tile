package linusfessler.alarmtiles.model;

import androidx.room.Ignore;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WakeUpSettings {

    private boolean alarmEnabled;
    private int alarmHour;
    private int alarmMinute;

    @Ignore
    public WakeUpSettings() {
        setAlarmEnabled(false);
        setAlarmHour(8);
        setAlarmMinute(0);
    }

    public WakeUpSettings(final boolean alarmEnabled, final int alarmHour, final int alarmMinute) {
        this.alarmEnabled = alarmEnabled;
        this.alarmHour = alarmHour;
        this.alarmMinute = alarmMinute;
    }

}
