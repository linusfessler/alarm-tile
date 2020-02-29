package linusfessler.alarmtiles.tiles.stopwatch

import com.spotify.mobius.MobiusLoop
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import linusfessler.alarmtiles.shared.formatters.TimeFormatter
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Wraps the Mobius loop and prepares the model for the view
 */
@Singleton
class StopwatchViewModel @Inject constructor(
        private val loop: MobiusLoop<Stopwatch, StopwatchEvent, StopwatchEffect>,
        private val timeFormatter: TimeFormatter
) {
    val stopwatch: Observable<Stopwatch> = Observable
            .create { emitter: ObservableEmitter<Stopwatch> ->
                val mobiusDisposable = loop.observe {
                    emitter.onNext(it)
                }
                emitter.setCancellable {
                    mobiusDisposable.dispose()
                }
            }
            .observeOn(AndroidSchedulers.mainThread())

    val elapsedTime: Observable<String> = stopwatch
            .switchMap {
                if (!it.isEnabled) {
                    if (it.stopTimestamp == null) {
                        // In this case the stopwatch has never run yet, so it is disabled and does not yet have a stop timestamp
                        return@switchMap Observable.just("")
                    }

                    val elapsedMillis = it.stopTimestamp - it.startTimestamp!!
                    return@switchMap Observable.just(timeFormatter.format(elapsedMillis, TimeUnit.MILLISECONDS))
                }

                val elapsedMillisBase = System.currentTimeMillis() - it.startTimestamp!!
                Observable.interval(10, TimeUnit.MILLISECONDS)
                        .map { elapsedHundredthsOfASecond: Long -> elapsedMillisBase + 10 * elapsedHundredthsOfASecond }
                        .map { elapsedMillis: Long -> timeFormatter.format(elapsedMillis, TimeUnit.MILLISECONDS) }
            }
            .observeOn(AndroidSchedulers.mainThread())

    fun dispatch(event: StopwatchEvent) {
        loop.dispatchEvent(event)
    }
}