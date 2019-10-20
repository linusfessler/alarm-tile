package linusfessler.alarmtiles;

import android.graphics.drawable.Drawable;
import android.os.Handler;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DrawableStateController {

    @NonNull
    private final Drawable drawable;

    private final Handler handler = new Handler();

    public void press() {
        drawable.setState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled});
    }

    public void release() {
        drawable.setState(new int[]{});
    }

    public void pressAndRelease(final long pressAfterMilliseconds, final long releaseAfterMilliseconds, final boolean repeat) {
        final long releaseAfterMillisecondsFromStart = pressAfterMilliseconds + releaseAfterMilliseconds;

        handler.postDelayed(this::press, pressAfterMilliseconds);
        handler.postDelayed(this::release, releaseAfterMillisecondsFromStart);
        if (repeat) {
            handler.postDelayed(() ->
                    pressAndRelease(pressAfterMilliseconds, releaseAfterMilliseconds, true), releaseAfterMillisecondsFromStart);
        }
    }

    public void stop() {
        handler.removeCallbacksAndMessages(null);
    }

}
