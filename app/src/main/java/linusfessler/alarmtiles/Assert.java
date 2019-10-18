package linusfessler.alarmtiles;

public class Assert {

    private Assert() {
        throw new UnsupportedOperationException("Do not initialize this static class.");
    }

    public static void isTrue(final boolean expression, final String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isNotNull(final Object object, final String message) {
        Assert.isTrue(object != null, message);
    }

    public static void isNotEmpty(final String string, final String message) {
        Assert.isNotNull(string, message);
        Assert.isTrue(!string.equals(""), message);
    }
}
