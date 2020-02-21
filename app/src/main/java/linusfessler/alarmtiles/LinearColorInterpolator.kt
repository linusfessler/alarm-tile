package linusfessler.alarmtiles

import android.animation.ArgbEvaluator

class LinearColorInterpolator(private vararg val colors: Int) {
    private val argbEvaluator = ArgbEvaluator()

    fun getColor(position: Float): Int {
        if (position < 0) {
            return colors[0]
        }

        if (position >= colors.size - 1) {
            return colors[colors.size - 1]
        }

        val index = position.toInt()
        return argbEvaluator.evaluate(position, colors[index], colors[index + 1]) as Int
    }
}