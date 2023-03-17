package ch.ubique.backend.core.utils;

import org.slf4j.MDC;

import java.util.Map;

public final class MdcUtils {

    private MdcUtils() {}

    public static void runWithContext(final Runnable runnable, final Map<String, String> context) {
        if (context == null) {
            runnable.run();
            return;
        }

        final var previous = MDC.getCopyOfContextMap();
        MDC.setContextMap(context);
        runnable.run();
        MDC.clear();
        if (previous != null) {
            MDC.setContextMap(previous);
        }
    }
}
