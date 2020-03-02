package linusfessler.alarmtiles.tiles.alarmtimer

import com.spotify.mobius.MobiusLoop
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import linusfessler.alarmtiles.shared.alarm.Alarm
import linusfessler.alarmtiles.shared.alarm.AlarmEffect
import linusfessler.alarmtiles.shared.alarm.AlarmEvent
import linusfessler.alarmtiles.shared.formatters.TimeFormatter
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.ceil

/**
 * Wraps the Mobius loop and prepares the model for the view
 */
class AlarmTimerViewModel @Inject constructor(
        private val loop: MobiusLoop<Alarm, AlarmEvent, AlarmEffect>,
        timeFormatter: TimeFormatter
) {
    val alarm: Observable<Alarm> = Observable
            .create { emitter: ObservableEmitter<Alarm> ->
                val mobiusDisposable = loop.observe {
                    emitter.onNext(it)
                }
                emitter.setCancellable {
                    mobiusDisposable.dispose()
                }
            }
            .observeOn(AndroidSchedulers.mainThread())

    val timeLeft: Observable<String> = alarm
            .switchMap {
                if (!it.isEnabled) {
                    return@switchMap Observable.just("")
                }

                val millisLeft = it.millisLeft
                val secondsLeft = ceil(millisLeft / 1000.0).toLong()
                Observable
                        .intervalRange(0, secondsLeft, 0, 1, TimeUnit.SECONDS)
                        .map { zeroBasedSecondsPassed: Long ->
                            timeFormatter.format(1000 * (secondsLeft - zeroBasedSecondsPassed), TimeUnit.SECONDS)
                        }
            }
            .observeOn(AndroidSchedulers.mainThread())

    fun dispatch(event: AlarmEvent) {
        loop.dispatchEvent(event)
    }
}