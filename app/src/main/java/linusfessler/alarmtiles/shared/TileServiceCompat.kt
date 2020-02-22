package linusfessler.alarmtiles.shared

import android.os.Build
import android.service.quicksettings.Tile

class TileServiceCompat {
    companion object {
        fun setSubtitle(tile: Tile, label: String, subtitle: String?) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                tile.subtitle = subtitle
                return
            }

            if (subtitle.isNullOrBlank()) {
                tile.label = label
            } else {
                tile.label = label + "\n" + subtitle
            }
        }
    }
}