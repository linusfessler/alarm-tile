package linusfessler.alarmtiles;

import android.os.Build;
import android.service.quicksettings.Tile;

public class TileServiceCompat {

    private TileServiceCompat() {
    }

    public static void setSubtitle(final Tile tile, final String label, final String subtitle) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            tile.setSubtitle(subtitle);
            return;
        }

        if (subtitle == null || subtitle.equals("")) {
            tile.setLabel(label);
        } else {
            tile.setLabel(label + "\n" + subtitle);
        }
    }
}
