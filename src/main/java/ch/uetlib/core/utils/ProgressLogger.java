package ch.uetlib.core.utils;

import static java.lang.Math.max;
import static java.lang.Math.round;

import org.jspecify.annotations.Nullable;
import org.slf4j.MDC;

import java.util.Map;

public final class ProgressLogger extends AbstractProgressLogger {

    private final long total;

    public ProgressLogger(final long total) {
        this(null, total);
    }

    public ProgressLogger(final @Nullable String prefix, final long total) {
        this(prefix, total, MDC.getCopyOfContextMap());
    }

    public ProgressLogger(
            final @Nullable String prefix,
            final long total,
            final Map<String, String> mdcContextMap) {
        this(prefix, total, mdcContextMap, 100);
    }

    public ProgressLogger(
            @Nullable final String prefix,
            final long total,
            final Map<String, String> mdcContextMap,
            final long numSteps) {
        super(prefix, max(1, round((double) total / numSteps)), mdcContextMap);
        this.total = total;
    }

    @Override
    protected void logProgress(
            final long currentCount,
            final double elementsPerSecond,
            final double overallElementsPerSecond) {
        final var percentage = (long) (100 * (double) currentCount / total);
        if (logger.isInfoEnabled()) {
            logger.info(
                    "{}Processed {}% (current {} elements per second, overall {} elements per"
                            + " second, {} of {})",
                    prefix,
                    percentage,
                    elementsPerSecond,
                    overallElementsPerSecond,
                    currentCount,
                    total);
        }
    }

    @Override
    protected void onCount(final long currentCount) {
        if (logger.isInfoEnabled() && currentCount == total) {
            logger.info("{}Done. Processed all {} elements.", prefix, total);
        }
    }
}
