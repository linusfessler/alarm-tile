package linusfessler.alarmtiles;

public class SystemServiceNotAvailableException extends RuntimeException {

    public SystemServiceNotAvailableException(final Class c) {
        super(String.format("System service %s is not available.", c.getSimpleName()));
    }
}
