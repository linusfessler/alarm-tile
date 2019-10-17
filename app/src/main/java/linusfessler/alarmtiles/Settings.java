package linusfessler.alarmtiles;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(tableName = "settings")
public class Settings {

    @PrimaryKey
    private Integer id;

    @ColumnInfo(name = "vibrate")
    private Boolean vibrate;

    public Settings() {

    }
}
