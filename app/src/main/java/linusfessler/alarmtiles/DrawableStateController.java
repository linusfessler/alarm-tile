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
        final Handler handler = new Handler();
        handler.postDelayed(this::press, pressAfterMilliseconds);
        handler.postDelayed(this::release, pressAfterMilliseconds + releaseAfterMilliseconds);
    }

    public void pressRepeatedly(final long pressAfterMilliseconds, final long releaseAfterMilliseconds) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    private void press() {
        drawable.setState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled});
    }

    private void release() {
        drawable.setState(new int[]{});
    }

}
