package linusfessler.alarmtiles.alarm

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import androidx.appcompat.view.ContextThemeWrapper
import io.reactivex.disposables.CompositeDisposable
import linusfessler.alarmtiles.R
import linusfessler.alarmtiles.core.App
import linusfessler.alarmtiles.shared.TileServiceCompat
import javax.inject.Inject
import javax.inject.Named

class AlarmTileService : TileService() {
    @Inject
    lateinit var viewModel: AlarmViewModel

    @JvmField
    @field:[Inject Named("is24Hours")]
    var is24Hours = false

    private lateinit var startDialog: AlarmStartDialog
    private lateinit var tileLabel: String

    private val disposable = CompositeDisposable()

    override fun onCreate() {
        super.onCreate()

        (applicationContext as App)
                .alarmComponent
                .inject(this)

        // Wrap context for compatibility between material components and tile service
        val context = ContextThemeWrapper(this, R.style.Theme_App)
        startDialog = AlarmStartDialog(context, viewModel, is24Hours)
        tileLabel = getString(R.string.sleep_timer)
    }

    override fun onClick() {
        super.onClick()
        disposable.add(viewModel.alarm
                .firstElement()
                .subscribe {
                    if (it.isEnabled) {
                        viewModel.dispatch(AlarmEvent.Disable())
                    } else {
                        showDialog(startDialog)
                    }
                })
    }

    override fun onStartListening() {
        super.onStartListening()

        disposable.add(viewModel.alarm
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