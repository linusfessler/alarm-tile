package linusfessler.alarmtiles.tiles.alarmtimer

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.view.ContextThemeWrapper
import io.reactivex.disposables.CompositeDisposable
import linusfessler.alarmtiles.R
import linusfessler.alarmtiles.shared.App
import linusfessler.alarmtiles.shared.TileServiceCompat
import javax.inject.Inject

class AlarmTimerTileService : TileService() {
    @Inject
    lateinit var viewModel: AlarmTimerViewModel

    @Inject
    lateinit var inputMethodManager: InputMethodManager

    private lateinit var startDialog: AlarmTimerStartDialog
    private lateinit var tileLabel: String

    private val disposable = CompositeDisposable()

    override fun onCreate() {
        super.onCreate()

        (applicationContext as App)
                .alarmTimerComponent
                .inject(this)

        // Wrap context for compatibility between material components and tile service
        val context = ContextThemeWrapper(this, R.style.Theme_App)
        startDialog = AlarmTimerStartDialog(context, inputMethodManager, viewModel)
        tileLabel = getString(R.string.sleep_timer)
    }

    override fun onClick() {
        super.onClick()
        disposable.add(viewModel.alarmTimer
                .firstElement()
                .subscribe {
                    if (it.isEnabled) {
                        viewModel.dispatch(AlarmTimerEvent.Disable())
                    } else {
                        showDialog(startDialog)
                    }
                })
    }

    override fun onStartListening() {
        super.onStartListening()

        disposable.add(viewModel.alarmTimer
                .subscribe {
                    qsTile.state = if (it.isEnabled) {
                        Tile.STATE_ACTIVE
                    } else {
                        Tile.STATE_INACTIVE
                    }
                    qsTile.updateTile()
                })

        disposable.add(viewModel.timeLeft
                .subscribe {
                    TileServiceCompat.setSubtitle(qsTile, tileLabel, it)
                    qsTile.updateTile()
                })
    }

    override fun onStopListening() {
        super.onStopListening()
        disposable.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
        startDialog.dismiss()
    }
}