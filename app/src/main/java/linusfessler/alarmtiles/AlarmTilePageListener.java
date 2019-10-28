package linusfessler.alarmtiles;

import linusfessler.alarmtiles.model.AlarmTile;

public interface AlarmTilePageListener {

    void onAlarmTileClicked(AlarmTile alarmTile);

    void onAlarmTileLongClicked(AlarmTile alarmTile);

}
