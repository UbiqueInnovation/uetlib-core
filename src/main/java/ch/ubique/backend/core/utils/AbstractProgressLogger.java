package ch.ubique.backend.core.utils;

import static java.util.Objects.requireNonNullElse;

import com.pivovarit.function.ThrowingBiConsumer;
import com.pivovarit.function.ThrowingConsumer;
import com.pivovarit.function.ThrowingFunction;
import com.pivovarit.function.ThrowingRunnable;

import org.jspecify.annotations.Nullable;
import jakarta.validation.constraints.Positive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract sealed class AbstractProgressLogger
        permits ProgressLogger, IndeterminateProgressLogger {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Nullable protected final String prefix;
    @Positive private final long stepSize;

    private final AtomicLong count;
    private final Map<String, String> mdcContextMap;
    private final long startTime;
    private long lastTime;

    protected AbstractProgressLogger(
            final String prefix,
            @Positive final long stepSize,
            final Map<String, String> mdcContextMap) {

        this.prefix = requireNonNullElse(prefix, "");
        this.stepSize = stepSize;
        this.mdcContextMap = mdcContextMap;

        this.count = new AtomicLong();
        this.startTime = System.currentTimeMillis();
        this.lastTime = startTime;
    }

    private void stepInternal() {
        final var currentCount = count.incrementAndGet();
        if (currentCount % stepSize == 0) {
            final var currentTime = System.currentTimeMillis();
            final var deltaMillis = currentTime - lastTime;
            final var elementsPerSecond = 1000.0 * stepSize / deltaMillis;
            lastTime = currentTime;

            final var overallDeltaMillis = currentTime - startTime;
            final var overallElementsPerSecond = 1000.0 * currentCount / overallDeltaMillis;

            logProgress(currentCount, elementsPerSecond, overallElementsPerSecond);
        }
        onCount(currentCount);
    }

    protected void onCount(final long currentCount) {}

    protected abstract void logProgress(
            final long currentCount, double elementsPerSecond, double overallElementsPerSecond);

    public void step() {
        MdcUtils.runWithContext(this::stepInternal, mdcContextMap);
    }

    public void watch(final Runnable runnable) {
        MdcUtils.runWithContext(
                () -> {
                    runnable.run();
                    stepInternal();
                },
                mdcContextMap);
    }

    public Runnable watchRunnable(final Runnable runnable) {
        return MdcUtils.runnableWithContext(
                () -> {
                    runnable.run();
                    stepInternal();
                },
                mdcContextMap);
    }

    public <T> Consumer<T> watch(final Consumer<T> consumer) {
        return MdcUtils.consumeWithContext(
                t -> {
                    consumer.accept(t);
                    stepInternal();
                },
                mdcContextMap);
    }

    public <T, U> BiConsumer<T, U> watch(final BiConsumer<T, U> consumer) {
        return MdcUtils.biConsumeWithContext(
                (t, u) -> {
                    consumer.accept(t, u);
                    stepInternal();
                },
                mdcContextMap);
    }

    public <T, R> Function<T, R> watch(final Function<T, R> function) {
        return MdcUtils.applyWithContext(
                t -> {
                    final var result = function.apply(t);
                    stepInternal();
                    return result;
                },
                mdcContextMap);
    }

    public <E extends Exception> ThrowingRunnable<E> watchThrowing(
            final ThrowingRunnable<E> runnable) {
        return MdcUtils.runThrowingWithContext(
                () -> {
                    runnable.run();
                    stepInternal();
                },
                mdcContextMap);
    }

    public <T, E extends Exception> ThrowingConsumer<T, E> watchThrowing(
            final ThrowingConsumer<T, E> consumer) {
        return MdcUtils.consumeThrowingWithContext(
                t -> {
                    consumer.accept(t);
                    stepInternal();
                },
                mdcContextMap);
    }

    public <T, U, E extends Exception> ThrowingBiConsumer<T, U, E> watchThrowing(
            final ThrowingBiConsumer<T, U, E> consumer) {
        return MdcUtils.biConsumeThrowingWithContext(
                (t, u) -> {
                    consumer.accept(t, u);
                    stepInternal();
                },
                mdcContextMap);
    }

    public <T, R, E extends Exception> ThrowingFunction<T, R, E> watchThrowing(
            final ThrowingFunction<T, R, E> function) {
        return MdcUtils.applyThrowingWithContext(
                t -> {
                    final var result = function.apply(t);
                    stepInternal();
                    return result;
                },
                mdcContextMap);
    }
}
