package ch.ubique.backend.core.utils;

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
    public static <R extends @Nullable Object> @Nullable R measureAndLog(
            final Supplier<@Nullable R> supplier) {
        return measureAndLog(asFunction(supplier), null);
    }

    public static <T extends @Nullable Object> void measureAndLog(
            final Consumer<T> consumer, final @Nullable T parameter) {
        measureAndLog(asFunction(consumer), parameter);
    }

    public static <T extends @Nullable Object, R extends @Nullable Object>
            @Nullable R measureAndLog(
                    final Function<@Nullable T, @Nullable R> function,
                    final @Nullable T parameter) {
        final var result = measure(function, parameter);
        logger.debug("Duration: {} ms", result.duration().toMillis());
        return result.value();
    }

    public static Duration measure(final Runnable runnable) {
        return measure(asNullSupplier(runnable)).duration();
    }

    public static <R extends @Nullable Object> TimerResult<@Nullable R> measure(
            final Supplier<@Nullable R> supplier) {
        return measure(asFunction(supplier), null);
    }

    public static <T extends @Nullable Object> Duration measure(
            final Consumer<@Nullable T> consumer, final @Nullable T parameter) {
        return measure(asFunction(consumer), parameter).duration();
    }

    public static <T extends @Nullable Object, R extends @Nullable Object>
            TimerResult<@Nullable R> measure(
                    final Function<@Nullable T, @Nullable R> function,
                    final @Nullable T parameter) {
        final var startTime = System.nanoTime();
        final var result = function.apply(parameter);
        final var endTime = System.nanoTime();

        final var duration = Duration.ofNanos(endTime - startTime);
        return new TimerResult<>(result, duration);
    }

    private static <R extends @Nullable Object> Supplier<@Nullable R> asNullSupplier(
            final Runnable runnable) {
        return () -> {
            runnable.run();
            return null;
        };
    }

    private static <T extends @Nullable Object> Function<@Nullable T, @Nullable Void> asFunction(
            final Consumer<@Nullable T> consumer) {
        return t -> {
            consumer.accept(t);
            return null;
        };
    }

    private static <T extends @Nullable Object> Function<Object, @Nullable T> asFunction(
            final Supplier<@Nullable T> consumer) {
        return t -> consumer.get();
    }
}
