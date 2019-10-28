package linusfessler.alarmtiles;

import android.content.res.Configuration;

import lombok.Getter;

@Getter
public enum AlarmTilePageConfiguration {

    PORTRAIT(4, 3), LANDSCAPE(2, 4);

    private final int count;
    private final int rowCount;
    private final int columnCount;

    AlarmTilePageConfiguration(final int rowCount, final int columnCount) {
        this.count = rowCount * columnCount;
        this.rowCount = rowCount;
        this.columnCount = columnCount;
    }

    public static AlarmTilePageConfiguration fromOrientation(final int orientation) {
        return orientation == Configuration.ORIENTATION_PORTRAIT ? PORTRAIT : LANDSCAPE;
    }

}
