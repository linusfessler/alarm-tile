package linusfessler.alarmtiles.shared;

import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class VibrationManager {

    private static final int DEFAULT_DURATION = 250;
    private static final int DEFAULT_AMPLITUDE = -1;

    private final Vibrator vibrator;

    @Inject
    public VibrationManager(final Vibrator vibrator) {
        this.vibrator = vibrator;
    }

    public void vibrate() {
        vibrate(DEFAULT_DURATION);
    }

    public void vibrate(final long milliseconds) {
        vibrate(milliseconds, DEFAULT_AMPLITUDE);
    }

    @SuppressWarnings("squid:CallToDeprecatedMethod")
    public void vibrate(final long milliseconds, final int amplitude) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(milliseconds, amplitude));
        } else {
            vibrator.vibrate(milliseconds);
        }
    }
}
