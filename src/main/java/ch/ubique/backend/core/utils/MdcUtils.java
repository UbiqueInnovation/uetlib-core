package ch.ubique.backend.core.utils;

import com.pivovarit.function.*;

import jakarta.annotation.Nullable;

import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class MdcUtils {

    private MdcUtils() {}

    public static void runWithContext(
            final Runnable runnable, final Map<String, String> mdcContext) {
        final var previous = startMdcSection(mdcContext);
        runnable.run();
        endMdcSection(previous);
    }

    public static <T> Callable<T> runWithContext(
            final Callable<T> callable, final Map<String, String> mdcContext) {
        return () -> {
            final var previous = startMdcSection(mdcContext);
            final var result = callable.call();
            endMdcSection(previous);
            return result;
        };
    }

    public static <T> Consumer<T> runWithContext(
            final Consumer<T> consumer, final Map<String, String> mdcContext) {
        return input -> {
            final var previous = startMdcSection(mdcContext);
            consumer.accept(input);
            endMdcSection(previous);
        };
    }

    public static <T, U> BiConsumer<T, U> runWithContext(
            final BiConsumer<T, U> consumer, final Map<String, String> mdcContext) {
        return (input1, input2) -> {
            final var previous = startMdcSection(mdcContext);
            consumer.accept(input1, input2);
            endMdcSection(previous);
        };
    }

    public static <T> Supplier<T> runWithContext(
            final Supplier<T> supplier, final Map<String, String> mdcContext) {
        return () -> {
            final var previous = startMdcSection(mdcContext);
            final var result = supplier.get();
            endMdcSection(previous);
            return result;
        };
    }

    public static <T, R> Function<T, R> runWithContext(
            final Function<T, R> function, final Map<String, String> mdcContext) {
        return input -> {
            final var previous = startMdcSection(mdcContext);
            final var result = function.apply(input);
            endMdcSection(previous);
            return result;
        };
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

    public static <T, E extends Exception> ThrowingConsumer<T, E> runThrowingWithContext(
            final ThrowingConsumer<T, E> consumer, final Map<String, String> mdcContext) {
        return (t) -> {
            final var previous = startMdcSection(mdcContext);
            consumer.accept(t);
            endMdcSection(previous);
        };
    }

    public static <T, U, E extends Exception> ThrowingBiConsumer<T, U, E> runThrowingWithContext(
            final ThrowingBiConsumer<T, U, E> biConsumer, final Map<String, String> mdcContext) {
        return (t, u) -> {
            final var previous = startMdcSection(mdcContext);
            biConsumer.accept(t, u);
            endMdcSection(previous);
        };
    }

    public static <T, R, E extends Exception> ThrowingFunction<T, R, E> runThrowingWithContext(
            final ThrowingFunction<T, R, E> function, final Map<String, String> mdcContext) {
        return input -> {
            final var previous = startMdcSection(mdcContext);
            final var result = function.apply(input);
            endMdcSection(previous);
            return result;
        };
    }

    public static <T, U, R, E extends Exception>
            ThrowingBiFunction<T, U, R, E> runThrowingWithContext(
                    final ThrowingBiFunction<T, U, R, E> function,
                    final Map<String, String> mdcContext) {
        return (t, u) -> {
            final var previous = startMdcSection(mdcContext);
            final var result = function.apply(t, u);
            endMdcSection(previous);
            return result;
        };
    }

    private static Map<String, String> startMdcSection(final Map<String, String> mdcContext) {
        final var previous = MDC.getCopyOfContextMap();
        if (mdcContext != null) {
            MDC.setContextMap(mdcContext);
        }
        return previous;
    }

    private static void endMdcSection(@Nullable final Map<String, String> previousMdcContext) {
        MDC.clear();
        if (previousMdcContext != null) {
            MDC.setContextMap(previousMdcContext);
        }
    }
}
