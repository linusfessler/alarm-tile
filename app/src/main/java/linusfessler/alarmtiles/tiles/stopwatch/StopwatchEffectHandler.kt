package linusfessler.alarmtiles.tiles.stopwatch

import com.spotify.mobius.Connectable
import com.spotify.mobius.Connection
import com.spotify.mobius.functions.Consumer
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StopwatchEffectHandler @Inject constructor(
        private val repository: StopwatchRepository
) : Connectable<StopwatchEffect, StopwatchEvent> {
    override fun connect(eventConsumer: Consumer<StopwatchEvent>): Connection<StopwatchEffect> {
        return object : Connection<StopwatchEffect> {
            private val disposable = CompositeDisposable()

            override fun accept(effect: StopwatchEffect) {
                when (effect) {
                    is StopwatchEffect.LoadFromDatabase -> disposable.add(repository.stopwatch
                            .take(1)
                            .subscribe {
                                eventConsumer.accept(StopwatchEvent.Initialized(it))
                            })

                    is StopwatchEffect.SaveToDatabase -> repository.update(effect.stopwatch)

                    is StopwatchEffect.Start -> {
                        val startTimestamp = System.currentTimeMillis()
                        eventConsumer.accept(StopwatchEvent.Start(startTimestamp))
                    }

                    is StopwatchEffect.Stop -> {
                        val stopTimestamp = System.currentTimeMillis()
                        eventConsumer.accept(StopwatchEvent.Stop(stopTimestamp))
                    }
                }
            }

            override fun dispose() {
                disposable.dispose()
            }
        }
    }
}
