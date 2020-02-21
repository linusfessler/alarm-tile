package linusfessler.alarmtiles.sleeptimer

import com.spotify.mobius.MobiusLoop
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import linusfessler.alarmtiles.shared.TimeFormatter
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.ceil

/**
 * Wraps the Mobius loop and prepares the model for the view
 */
@Singleton
class SleepTimerViewModel @Inject constructor(
        private val loop: MobiusLoop<SleepTimer, SleepTimerEvent, SleepTimerEffect>,
        private val timeFormatter: TimeFormatter
) {
    val sleepTimer: Observable<SleepTimer> = Observable.create { emitter: ObservableEmitter<SleepTimer> ->
        val mobiusDisposable = loop.observe { value: SleepTimer ->
            emitter.onNext(value)
        }
        emitter.setCancellable {
            mobiusDisposable.dispose()
        }
    }.observeOn(AndroidSchedulers.mainThread())

    val timeLeft: Observable<String> = sleepTimer
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
            }.observeOn(AndroidSchedulers.mainThread())

    fun dispatch(event: SleepTimerEvent) {
        loop.dispatchEvent(event)
    }
}