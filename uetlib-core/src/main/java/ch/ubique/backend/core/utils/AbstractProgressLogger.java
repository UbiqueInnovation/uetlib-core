package ch.ubique.backend.core.utils;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Positive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;
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
    private long lastTime;

    protected AbstractProgressLogger(
            final String prefix,
            @Positive final long stepSize,
            final Map<String, String> mdcContextMap) {

        this.prefix = Objects.requireNonNullElse(prefix, "");
        this.stepSize = stepSize;
        this.mdcContextMap = mdcContextMap;

        this.count = new AtomicLong();
        this.lastTime = System.currentTimeMillis();
    }

    public void step() {
        final var currentCount = count.incrementAndGet();
        if (currentCount % stepSize == 0) {
            final var currentTime = System.currentTimeMillis();
            final var deltaMillis = currentTime - lastTime;
            final var elementsPerSecond = 1000.0 * stepSize / deltaMillis;
            lastTime = currentTime;

            logProgress(currentCount, elementsPerSecond);
        }
        onCount(currentCount);
    }

    protected void onCount(final long currentCount) {}

    protected abstract void logProgress(final long currentCount, double elementsPerSecond);

    public void watch(final Runnable runnable) {
        MdcUtils.runWithContext(
                () -> {
                    runnable.run();
                    step();
                },
                mdcContextMap);
    }

    public <T> Consumer<T> watch(final Consumer<T> consumer) {
        return MdcUtils.runWithContext(
                t -> {
                    consumer.accept(t);
                    step();
                },
                mdcContextMap);
    }

    public <T, U> BiConsumer<T, U> watch(final BiConsumer<T, U> consumer) {
        return MdcUtils.runWithContext(
                (t, u) -> {
                    consumer.accept(t, u);
                    step();
                },
                mdcContextMap);
    }

    public <T, R> Function<T, R> watch(final Function<T, R> function) {
        return MdcUtils.runWithContext(
                t -> {
                    final var result = function.apply(t);
                    step();
                    return result;
                },
                mdcContextMap);
    }
}
