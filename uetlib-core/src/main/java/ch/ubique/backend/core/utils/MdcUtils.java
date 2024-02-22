package ch.ubique.backend.core.utils;

import com.pivovarit.function.*;

import jakarta.annotation.Nullable;

import org.slf4j.MDC;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class MdcUtils {

    private MdcUtils() {}

    public static void runWithContext(final Runnable runnable, final Map<String, String> context) {
        final var previous = startMdcSection(context);
        runnable.run();
        endMdcSection(previous);
    }

    public static <T> Consumer<T> runWithContext(
            final Consumer<T> consumer, final Map<String, String> context) {
        return input -> {
            final var previous = startMdcSection(context);
            consumer.accept(input);
            endMdcSection(previous);
        };
    }

    public static <T, U> BiConsumer<T, U> runWithContext(
            final BiConsumer<T, U> consumer, final Map<String, String> context) {
        return (input1, input2) -> {
            final var previous = startMdcSection(context);
            consumer.accept(input1, input2);
            endMdcSection(previous);
        };
    }

    public static <T> Supplier<T> runWithContext(
            final Supplier<T> supplier, final Map<String, String> context) {
        return () -> {
            final var previous = startMdcSection(context);
            final var result = supplier.get();
            endMdcSection(previous);
            return result;
        };
    }

    public static <T, R> Function<T, R> runWithContext(
            final Function<T, R> function, final Map<String, String> context) {
        return input -> {
            final var previous = startMdcSection(context);
            final var result = function.apply(input);
            endMdcSection(previous);
            return result;
        };
    }

    public static <E extends Exception> ThrowingRunnable<E> runThrowingWithContext(
            final ThrowingRunnable<E> runnable, final Map<String, String> context) {
        return () -> {
            final var previous = startMdcSection(context);
            runnable.run();
            endMdcSection(previous);
        };
    }

    public static <T, E extends Exception> ThrowingConsumer<T, E> runThrowingWithContext(
            final ThrowingConsumer<T, E> consumer, final Map<String, String> context) {
        return (t) -> {
            final var previous = startMdcSection(context);
            consumer.accept(t);
            endMdcSection(previous);
        };
    }

    public static <T, U, E extends Exception> ThrowingBiConsumer<T, U, E> runThrowingWithContext(
            final ThrowingBiConsumer<T, U, E> biConsumer, final Map<String, String> context) {
        return (t, u) -> {
            final var previous = startMdcSection(context);
            biConsumer.accept(t, u);
            endMdcSection(previous);
        };
    }

    public static <T, R, E extends Exception> ThrowingFunction<T, R, E> runThrowingWithContext(
            final ThrowingFunction<T, R, E> function, final Map<String, String> context) {
        return input -> {
            final var previous = startMdcSection(context);
            final var result = function.apply(input);
            endMdcSection(previous);
            return result;
        };
    }

    public static <T, U, R, E extends Exception>
            ThrowingBiFunction<T, U, R, E> runThrowingWithContext(
                    final ThrowingBiFunction<T, U, R, E> function,
                    final Map<String, String> context) {
        return (t, u) -> {
            final var previous = startMdcSection(context);
            final var result = function.apply(t, u);
            endMdcSection(previous);
            return result;
        };
    }

    private static Map<String, String> startMdcSection(final Map<String, String> context) {
        final var previous = MDC.getCopyOfContextMap();
        if (context != null) {
            MDC.setContextMap(context);
        }
        return previous;
    }

    private static void endMdcSection(@Nullable final Map<String, String> previous) {
        MDC.clear();
        if (previous != null) {
            MDC.setContextMap(previous);
        }
    }
}
