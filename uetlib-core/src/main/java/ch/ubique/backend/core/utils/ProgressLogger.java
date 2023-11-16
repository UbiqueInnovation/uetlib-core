package ch.ubique.backend.core.utils;

import static java.lang.Math.max;
import static java.lang.Math.round;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Positive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Function;

public class ProgressLogger {

    private static final Logger logger = LoggerFactory.getLogger(ProgressLogger.class);

    private final String prefix;

    @Nullable private final Long total;
    @Positive private final long stepSize;

    private final AtomicLong count;
    private final long startTime;

    private final Map<String, String> mdcContextMap;

    public ProgressLogger(final long total) {
        this(null, total);
    }

    public ProgressLogger(final String prefix, final long total) {
        this(prefix, total, null);
    }

    public ProgressLogger(
            final String prefix, final long total, final Map<String, String> mdcContextMap) {
        this(prefix, total, mdcContextMap, 100);
    }

    public ProgressLogger(
            final String prefix,
            final long total,
            final Map<String, String> mdcContextMap,
            final long numSteps) {
        this(prefix, total, max(1, round((double) total / numSteps)), mdcContextMap);
    }

    private ProgressLogger(
            final String prefix,
            @Nullable final Long total,
            @Positive final long stepSize,
            final Map<String, String> mdcContextMap) {

        this.prefix = Objects.requireNonNullElse(prefix, "");

        this.total = total;
        this.stepSize = stepSize;

        this.mdcContextMap = mdcContextMap;

        this.count = new AtomicLong();
        this.startTime = System.currentTimeMillis();
    }

    public static ProgressLogger indeterminate() {
        return indeterminate(null);
    }

    public static ProgressLogger indeterminate(final long stepSize) {
        return indeterminate(stepSize, null);
    }

    public static ProgressLogger indeterminate(
            final long stepSize, final Map<String, String> mdcContextMap) {
        return indeterminate(null, stepSize, mdcContextMap);
    }

    public static ProgressLogger indeterminate(final String prefix) {
        return indeterminate(prefix, null);
    }

    public static ProgressLogger indeterminate(
            final String prefix, final Map<String, String> mdcContextMap) {
        return indeterminate(prefix, 100, mdcContextMap);
    }

    public static ProgressLogger indeterminate(
            final String prefix, final long stepSize, final Map<String, String> mdcContextMap) {
        return new ProgressLogger(prefix, null, stepSize, mdcContextMap);
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

    public void step() {
        final var currentCount = count.incrementAndGet();
        if (currentCount % stepSize == 0) {
            logProgress(currentCount);
        }
    }

    private void logProgress(final long currentCount) {
        if (total != null) {
            logDeterminateProgress(currentCount);
        } else {
            logIndeterminateProgress(currentCount);
        }
    }

    private void logDeterminateProgress(final long currentCount) {
        final var percent = 100 * currentCount / Objects.requireNonNull(total);
        final var currentTime = System.currentTimeMillis();
        final var elementsPerSecond = 1000.0 * currentCount / (currentTime - startTime);
        logger.info(
                "{}Processed {}% ({} elements per second, {} of {})",
                prefix, percent, elementsPerSecond, currentCount, total);
    }

    private void logIndeterminateProgress(final long currentCount) {
        final var currentTime = System.currentTimeMillis();
        final var elementsPerSecond = 1000.0 * currentCount / (currentTime - startTime);
        logger.info(
                "{}Processed {} elements ({} elements per second)",
                prefix,
                currentCount,
                elementsPerSecond);
    }

    public <T> Consumer<T> watch(final Consumer<T> consumer) {
        return MdcUtils.runWithContext(
                t -> {
                    consumer.accept(t);
                    step();
                },
                mdcContextMap);
    }
}
