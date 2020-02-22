package linusfessler.alarmtiles.stopwatch

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import io.reactivex.disposables.CompositeDisposable
import linusfessler.alarmtiles.R
import linusfessler.alarmtiles.core.App
import linusfessler.alarmtiles.shared.TileServiceCompat.Companion.setSubtitle
import linusfessler.alarmtiles.stopwatch.StopwatchEvent.Toggle
import javax.inject.Inject

class StopwatchTileService : TileService() {
    @Inject
    lateinit var viewModel: StopwatchViewModel

    private lateinit var tileLabel: String

    private val disposable = CompositeDisposable()

    override fun onCreate() {
        super.onCreate()

        (applicationContext as App)
                .stopwatchComponent
                .inject(this)

        tileLabel = getString(R.string.stopwatch)
    }

    override fun onClick() {
        super.onClick()
        viewModel.dispatch(Toggle())
    }

    override fun onStartListening() {
        super.onStartListening()

        disposable.add(viewModel.stopwatch
                .subscribe {
                    qsTile.state =
                            if (it.isEnabled) {
                                Tile.STATE_ACTIVE
                            } else {
                                Tile.STATE_INACTIVE
                            }
                    qsTile.updateTile()
                })

        disposable.add(viewModel.elapsedTime
                .subscribe {
                    setSubtitle(qsTile, tileLabel, it)
                    qsTile.updateTile()
                })
    }

    override fun onStopListening() {
        disposable.clear()
        super.onStopListening()
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }
}