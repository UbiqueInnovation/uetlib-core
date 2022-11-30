package ch.ubique.backend.core.utils;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Timer {

    private Timer() {}

    private static final Logger logger = LoggerFactory.getLogger(Timer.class);

    public static void measureAndLog(final Runnable runnable) {
        measureAndLog(asNullSupplier(runnable));
    }

    public static <R> R measureAndLog(final Supplier<R> supplier) {
        return measureAndLog(asFunction(supplier), null);
    }

    public static <T> void measureAndLog(final Consumer<T> consumer, final T parameter) {
        measureAndLog(asFunction(consumer), parameter);
    }

    public static <T, R> R measureAndLog(final Function<T, R> function, final T parameter) {
        final var result = measure(function, parameter);
        logger.debug("Duration: {} ms", result.duration().toMillis());
        return result.value();
    }

    public static Duration measure(final Runnable runnable) {
        return measure(asNullSupplier(runnable)).duration();
    }

    public static <R> TimerResult<R> measure(final Supplier<R> supplier) {
        return measure(asFunction(supplier), null);
    }

    public static <T> Duration measure(final Consumer<T> consumer, final T parameter) {
        return measure(asFunction(consumer), parameter).duration();
    }

    public static <T, R> TimerResult<R> measure(final Function<T, R> function, final T parameter) {
        final var startTime = System.nanoTime();
        final var result = function.apply(parameter);
        final var endTime = System.nanoTime();

        final var duration = Duration.ofNanos(endTime - startTime);
        return new TimerResult<>(result, duration);
    }

    private static <R> Supplier<R> asNullSupplier(final Runnable runnable) {
        return () -> {
            runnable.run();
            return null;
        };
    }

    private static <T> Function<T, ObjectUtils.Null> asFunction(final Consumer<T> consumer) {
        return t -> {
            consumer.accept(t);
            return null;
        };
    }

    private static <T> Function<Object, T> asFunction(final Supplier<T> consumer) {
        return t -> consumer.get();
    }
}
