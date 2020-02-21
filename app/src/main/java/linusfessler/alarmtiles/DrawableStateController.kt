package linusfessler.alarmtiles

import android.graphics.drawable.Drawable
import android.os.Handler

class DrawableStateController(val drawable: Drawable) {
    private val handler = Handler()

    fun press() {
        drawable.state = intArrayOf(android.R.attr.state_pressed, android.R.attr.state_enabled)
    }

    fun release() {
        drawable.state = intArrayOf()
    }

    fun pressAndRelease(pressAfterMilliseconds: Long, releaseAfterMilliseconds: Long, repeat: Boolean) {
        val releaseAfterMillisecondsFromStart = pressAfterMilliseconds + releaseAfterMilliseconds
        handler.postDelayed({ press() }, pressAfterMilliseconds)
        handler.postDelayed({ release() }, releaseAfterMillisecondsFromStart)
        if (repeat) {
            handler.postDelayed({ pressAndRelease(pressAfterMilliseconds, releaseAfterMilliseconds, true) }, releaseAfterMillisecondsFromStart)
        }
    }

    fun stop() {
        handler.removeCallbacksAndMessages(null)
    }
}