package linusfessler.alarmtiles.tiles.alarm

import com.spotify.mobius.MobiusLoop
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import linusfessler.alarmtiles.shared.formatters.TimeOfDayFormatter
import javax.inject.Inject

/**
 * Wraps the Mobius loop and prepares the model for the view
 */
class AlarmViewModel @Inject constructor(
        private val loop: MobiusLoop<Alarm, AlarmEvent, AlarmEffect>,
        timeOfDayFormatter: TimeOfDayFormatter
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
                return@switchMap if (it.isEnabled) {
                    Observable.just(timeOfDayFormatter.format(it.timeOfDay.hourOfDay, it.timeOfDay.minuteOfHour))
                } else {
                    return@switchMap Observable.just("")
                }
            }
            .observeOn(AndroidSchedulers.mainThread())

    fun dispatch(event: AlarmEvent) {
        loop.dispatchEvent(event)
    }
}