package ch.ubique.backend.core.utils;

import org.slf4j.MDC;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

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

    public static <T> Consumer<T> runWithContext(
            final Consumer<T> consumer, final Map<String, String> context) {
        return input -> {
            if (context == null) {
                consumer.accept(input);
                return;
            }

            final var previous = MDC.getCopyOfContextMap();
            MDC.setContextMap(context);
            consumer.accept(input);
            MDC.clear();
            if (previous != null) {
                MDC.setContextMap(previous);
            }
        };
    }

    public static <T> Supplier<T> runWithContext(
            final Supplier<T> supplier, final Map<String, String> context) {
        return () -> {
            if (context == null) {
                return supplier.get();
            }

            final var previous = MDC.getCopyOfContextMap();
            MDC.setContextMap(context);
            final var result = supplier.get();
            MDC.clear();
            if (previous != null) {
                MDC.setContextMap(previous);
            }
            return result;
        };
    }

    public static <T, R> Function<T, R> runWithContext(
            final Function<T, R> function, final Map<String, String> context) {
        return input -> {
            if (context == null) {
                return function.apply(input);
            }

            final var previous = MDC.getCopyOfContextMap();
            MDC.setContextMap(context);
            final var result = function.apply(input);
            MDC.clear();
            if (previous != null) {
                MDC.setContextMap(previous);
            }
            return result;
        };
    }
}
