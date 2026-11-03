package ch.ubique.uetlib.core.utils;

import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Timer {

    private static final Logger logger = LoggerFactory.getLogger(Timer.class);

    private Timer() {}

    public static void measureAndLog(final Runnable runnable) {
        measureAndLog(asNullSupplier(runnable));
    }

    @SuppressWarnings("UnusedReturnValue")
    public static <R extends @Nullable Object> R measureAndLog(final Supplier<R> supplier) {
        return Timer.<@Nullable Void, R>measureAndLog(t -> supplier.get(), null);
    }

    public static <T extends @Nullable Object> void measureAndLog(
            final Consumer<T> consumer, final T parameter) {
        measureAndLog(asFunction(consumer), parameter);
    }

    public static <T extends @Nullable Object, R extends @Nullable Object> R measureAndLog(
            final Function<T, R> function, final T parameter) {
        final var result = measure(function, parameter);
        logger.debug("Duration: {} ms", result.duration().toMillis());
        return result.value();
    }

    public static Duration measure(final Runnable runnable) {
        return measure(asNullSupplier(runnable)).duration();
    }

    public static <R extends @Nullable Object> TimerResult<R> measure(final Supplier<R> supplier) {
        return Timer.<@Nullable Void, R>measure(t -> supplier.get(), null);
    }

    public static <T extends @Nullable Object> Duration measure(
            final Consumer<T> consumer, final T parameter) {
        return measure(asFunction(consumer), parameter).duration();
    }

    public static <T extends @Nullable Object, R extends @Nullable Object> TimerResult<R> measure(
            final Function<T, R> function, final T parameter) {
        final var startTime = System.nanoTime();
        final var result = function.apply(parameter);
        final var endTime = System.nanoTime();

        final var duration = Duration.ofNanos(endTime - startTime);
        return new TimerResult<>(result, duration);
    }

    private static <R extends @Nullable Object> Supplier<R> asNullSupplier(
            final Runnable runnable) {
        return () -> {
            runnable.run();
            return null;
        };
    }

    private static <T extends @Nullable Object> Function<T, @Nullable Void> asFunction(
            final Consumer<T> consumer) {
        return t -> {
            consumer.accept(t);
            return null;
        };
    }
}
