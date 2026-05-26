package ch.ubique.backend.core.utils;

import com.pivovarit.function.*;

import org.jspecify.annotations.Nullable;
import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.*;

public final class MdcUtils {

    private MdcUtils() {}

    public static void runWithContext(
            final Runnable runnable, final Map<String, String> mdcContext) {
        final var previous = startMdcSection(mdcContext);
        runnable.run();
        endMdcSection(previous);
    }

    public static Runnable runnableWithContext(
            final Runnable runnable, final Map<String, String> mdcContext) {
        return () -> runWithContext(runnable, mdcContext);
    }

    public static <E extends Exception> ThrowingRunnable<E> runThrowingWithContext(
            final ThrowingRunnable<E> runnable, final Map<String, String> mdcContext) {
        return () -> {
            final var previous = startMdcSection(mdcContext);
            runnable.run();
            endMdcSection(previous);
        };
    }

    public static <T> Callable<@Nullable T> callWithContext(
            final Callable<@Nullable T> callable, final Map<String, String> mdcContext) {
        return () -> {
            final var previous = startMdcSection(mdcContext);
            final var result = callable.call();
            endMdcSection(previous);
            return result;
        };
    }

    public static <T> Supplier<@Nullable T> supplyWithContext(
            final Supplier<@Nullable T> supplier, final Map<String, String> mdcContext) {
        return () -> {
            final var previous = startMdcSection(mdcContext);
            final var result = supplier.get();
            endMdcSection(previous);
            return result;
        };
    }

    public static <T> Consumer<@Nullable T> consumeWithContext(
            final Consumer<@Nullable T> consumer, final Map<String, String> mdcContext) {
        return input -> {
            final var previous = startMdcSection(mdcContext);
            consumer.accept(input);
            endMdcSection(previous);
        };
    }

    public static <T, U> BiConsumer<@Nullable T, @Nullable U> biConsumeWithContext(
            final BiConsumer<@Nullable T, @Nullable U> consumer,
            final Map<String, String> mdcContext) {
        return (input1, input2) -> {
            final var previous = startMdcSection(mdcContext);
            consumer.accept(input1, input2);
            endMdcSection(previous);
        };
    }

    public static <T, R> Function<@Nullable T, @Nullable R> applyWithContext(
            final Function<@Nullable T, @Nullable R> function,
            final Map<String, String> mdcContext) {
        return input -> {
            final var previous = startMdcSection(mdcContext);
            final var result = function.apply(input);
            endMdcSection(previous);
            return result;
        };
    }

    public static <T, U, R> BiFunction<@Nullable T, @Nullable U, R> biApplyWithContext(
            final BiFunction<@Nullable T, @Nullable U, R> function,
            final Map<String, String> mdcContext) {
        return (t, u) -> {
            final var previous = startMdcSection(mdcContext);
            final var result = function.apply(t, u);
            endMdcSection(previous);
            return result;
        };
    }

    public static <T, E extends Exception>
            ThrowingConsumer<@Nullable T, E> consumeThrowingWithContext(
                    final ThrowingConsumer<@Nullable T, E> consumer,
                    final Map<String, String> mdcContext) {
        return t -> {
            final var previous = startMdcSection(mdcContext);
            consumer.accept(t);
            endMdcSection(previous);
        };
    }

    public static <T, U, E extends Exception>
            ThrowingBiConsumer<@Nullable T, @Nullable U, E> biConsumeThrowingWithContext(
                    final ThrowingBiConsumer<@Nullable T, @Nullable U, E> biConsumer,
                    final Map<String, String> mdcContext) {
        return (t, u) -> {
            final var previous = startMdcSection(mdcContext);
            biConsumer.accept(t, u);
            endMdcSection(previous);
        };
    }

    public static <T, R, E extends Exception>
            ThrowingFunction<@Nullable T, @Nullable R, E> applyThrowingWithContext(
                    final ThrowingFunction<@Nullable T, @Nullable R, E> function,
                    final Map<String, String> mdcContext) {
        return input -> {
            final var previous = startMdcSection(mdcContext);
            final var result = function.apply(input);
            endMdcSection(previous);
            return result;
        };
    }

    public static <T, U, R, E extends Exception>
            ThrowingBiFunction<@Nullable T, @Nullable U, @Nullable R, E> biApplyThrowingWithContext(
                    final ThrowingBiFunction<@Nullable T, @Nullable U, @Nullable R, E> function,
                    final Map<String, String> mdcContext) {
        return (t, u) -> {
            final var previous = startMdcSection(mdcContext);
            final var result = function.apply(t, u);
            endMdcSection(previous);
            return result;
        };
    }

    public static <T> IntFunction<@Nullable T> intApplyWithContext(
            final IntFunction<@Nullable T> function, final Map<String, String> mdcContext) {
        return i -> {
            final var previous = startMdcSection(mdcContext);
            final var result = function.apply(i);
            endMdcSection(previous);
            return result;
        };
    }

    public static <T> LongFunction<@Nullable T> longApplyWithContext(
            final LongFunction<@Nullable T> function, final Map<String, String> mdcContext) {
        return i -> {
            final var previous = startMdcSection(mdcContext);
            final var result = function.apply(i);
            endMdcSection(previous);
            return result;
        };
    }

    public static <T> DoubleFunction<@Nullable T> doubleApplyWithContext(
            final DoubleFunction<@Nullable T> function, final Map<String, String> mdcContext) {
        return i -> {
            final var previous = startMdcSection(mdcContext);
            final var result = function.apply(i);
            endMdcSection(previous);
            return result;
        };
    }

    private static Map<String, String> startMdcSection(
             final @Nullable  Map<String, String> mdcContext) {
        final var previous = MDC.getCopyOfContextMap();
        if (mdcContext != null) {
            MDC.setContextMap(mdcContext);
        }
        return previous;
    }

    private static void endMdcSection( final @Nullable  Map<String, String> previousMdcContext) {
        MDC.clear();
        if (previousMdcContext != null) {
            MDC.setContextMap(previousMdcContext);
        }
    }
}
