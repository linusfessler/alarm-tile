package linusfessler.alarmtiles;

public class Assert {

    private Assert() {
        throw new UnsupportedOperationException("Do not initialize this static class.");
    }

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }
}
