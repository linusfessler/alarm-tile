package linusfessler.alarmtiles;

import android.graphics.drawable.Drawable;
import android.os.Handler;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DrawableStateController {

    @NonNull
    private final Drawable drawable;

    public void press(final long pressAfterMilliseconds, final long releaseAfterMilliseconds) {
        press(pressAfterMilliseconds, releaseAfterMilliseconds, false);
    }

    public void pressRepeatedly(final long pressAfterMilliseconds, final long releaseAfterMilliseconds) {
        press(pressAfterMilliseconds, releaseAfterMilliseconds, true);
    }

    private void press(final long pressAfterMilliseconds, final long releaseAfterMilliseconds, final boolean repeat) {
        final long releaseAfterMillisecondsFromStart = pressAfterMilliseconds + releaseAfterMilliseconds;

        final Handler handler = new Handler();
        handler.postDelayed(this::press, pressAfterMilliseconds);
        handler.postDelayed(this::release, releaseAfterMillisecondsFromStart);
        if (repeat) {
            handler.postDelayed(() ->
                    press(pressAfterMilliseconds, releaseAfterMilliseconds, true), releaseAfterMillisecondsFromStart);
        }
    }

    private void press() {
        drawable.setState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled});
    }

    private void release() {
        drawable.setState(new int[]{});
    }

}
