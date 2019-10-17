package linusfessler.alarmtiles;

import android.animation.ArgbEvaluator;

public class ColorLinearInterpolator {

    private Integer[] colors;
    private ArgbEvaluator argbEvaluator;

    public ColorLinearInterpolator(Integer... colors) {
        this.colors = colors;
        argbEvaluator = new ArgbEvaluator();
    }

    public Integer getColor(Float position) {
        if (position < 0) {
            return colors[0];
        }

        if (position >= colors.length - 1) {
            return colors[colors.length - 1];
        }

        Integer index = position.intValue();
        return (Integer) argbEvaluator.evaluate(position, colors[index], colors[index + 1]);
    }
}
