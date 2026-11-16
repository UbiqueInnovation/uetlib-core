package ch.uetlib.core.utils;

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

    public static <T extends @Nullable Object> Callable<T> callWithContext(
            final Callable<T> callable, final Map<String, String> mdcContext) {
        return () -> {
            final var previous = startMdcSection(mdcContext);
            final var result = callable.call();
            endMdcSection(previous);
            return result;
        };
    }

    public static <T extends @Nullable Object> Supplier<T> supplyWithContext(
            final Supplier<T> supplier, final Map<String, String> mdcContext) {
        return () -> {
            final var previous = startMdcSection(mdcContext);
            final var result = supplier.get();
            endMdcSection(previous);
            return result;
        };
    }

    public static <T extends @Nullable Object> Consumer<T> consumeWithContext(
            final Consumer<T> consumer, final Map<String, String> mdcContext) {
        return input -> {
            final var previous = startMdcSection(mdcContext);
            consumer.accept(input);
            endMdcSection(previous);
        };
    }

    public static <T extends @Nullable Object, U extends @Nullable Object>
            BiConsumer<T, U> biConsumeWithContext(
                    final BiConsumer<T, U> consumer, final Map<String, String> mdcContext) {
        return (input1, input2) -> {
            final var previous = startMdcSection(mdcContext);
            consumer.accept(input1, input2);
            endMdcSection(previous);
        };
    }

    public static <T extends @Nullable Object, R extends @Nullable Object>
            Function<T, R> applyWithContext(
                    final Function<T, R> function, final Map<String, String> mdcContext) {
        return input -> {
            final var previous = startMdcSection(mdcContext);
            final var result = function.apply(input);
            endMdcSection(previous);
            return result;
        };
    }

    public static <
                    T extends @Nullable Object,
                    U extends @Nullable Object,
                    R extends @Nullable Object>
            BiFunction<T, U, R> biApplyWithContext(
                    final BiFunction<T, U, R> function, final Map<String, String> mdcContext) {
        return (t, u) -> {
            final var previous = startMdcSection(mdcContext);
            final var result = function.apply(t, u);
            endMdcSection(previous);
            return result;
        };
    }

    public static <T extends @Nullable Object, E extends Exception>
            ThrowingConsumer<T, E> consumeThrowingWithContext(
                    final ThrowingConsumer<T, E> consumer, final Map<String, String> mdcContext) {
        return t -> {
            final var previous = startMdcSection(mdcContext);
            consumer.accept(t);
            endMdcSection(previous);
        };
    }

    public static <T extends @Nullable Object, U extends @Nullable Object, E extends Exception>
            ThrowingBiConsumer<T, U, E> biConsumeThrowingWithContext(
                    final ThrowingBiConsumer<T, U, E> biConsumer,
                    final Map<String, String> mdcContext) {
        return (t, u) -> {
            final var previous = startMdcSection(mdcContext);
            biConsumer.accept(t, u);
            endMdcSection(previous);
        };
    }

    public static <T extends @Nullable Object, R extends @Nullable Object, E extends Exception>
            ThrowingFunction<T, R, E> applyThrowingWithContext(
                    final ThrowingFunction<T, R, E> function,
                    final Map<String, String> mdcContext) {
        return input -> {
            final var previous = startMdcSection(mdcContext);
            final var result = function.apply(input);
            endMdcSection(previous);
            return result;
        };
    }

    public static <
                    T extends @Nullable Object,
                    U extends @Nullable Object,
                    R extends @Nullable Object,
                    E extends Exception>
            ThrowingBiFunction<T, U, R, E> biApplyThrowingWithContext(
                    final ThrowingBiFunction<T, U, R, E> function,
                    final Map<String, String> mdcContext) {
        return (t, u) -> {
            final var previous = startMdcSection(mdcContext);
            final var result = function.apply(t, u);
            endMdcSection(previous);
            return result;
        };
    }

    public static <T extends @Nullable Object> IntFunction<T> intApplyWithContext(
            final IntFunction<T> function, final Map<String, String> mdcContext) {
        return i -> {
            final var previous = startMdcSection(mdcContext);
            final var result = function.apply(i);
            endMdcSection(previous);
            return result;
        };
    }

    public static <T extends @Nullable Object> LongFunction<T> longApplyWithContext(
            final LongFunction<T> function, final Map<String, String> mdcContext) {
        return i -> {
            final var previous = startMdcSection(mdcContext);
            final var result = function.apply(i);
            endMdcSection(previous);
            return result;
        };
    }

    public static <T extends @Nullable Object> DoubleFunction<T> doubleApplyWithContext(
            final DoubleFunction<T> function, final Map<String, String> mdcContext) {
        return i -> {
            final var previous = startMdcSection(mdcContext);
            final var result = function.apply(i);
            endMdcSection(previous);
            return result;
        };
    }

    private static Map<String, String> startMdcSection(
            final @Nullable Map<String, String> mdcContext) {
        final var previous = MDC.getCopyOfContextMap();
        if (mdcContext != null) {
            MDC.setContextMap(mdcContext);
        }
        return previous;
    }

    private static void endMdcSection(final @Nullable Map<String, String> previousMdcContext) {
        MDC.clear();
        if (previousMdcContext != null) {
            MDC.setContextMap(previousMdcContext);
        }
    }
}
