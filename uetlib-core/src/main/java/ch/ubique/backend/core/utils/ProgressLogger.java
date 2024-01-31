package ch.ubique.backend.core.utils;

import static java.lang.Math.max;
import static java.lang.Math.round;

import org.slf4j.MDC;

import java.util.Map;

public final class ProgressLogger extends AbstractProgressLogger {

    private final long total;

    public ProgressLogger(final long total) {
        this(null, total);
    }

    public ProgressLogger(final String prefix, final long total) {
        this(prefix, total, MDC.getCopyOfContextMap());
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
        super(prefix, max(1, round((double) total / numSteps)), mdcContextMap);
        this.total = total;
    }

    @Override
    protected void logProgress(final long currentCount, final double elementsPerSecond) {
        final var percent = 100 * currentCount / total;
        logger.info(
                "{}Processed {}% ({} elements per second, {} of {})",
                prefix, percent, elementsPerSecond, currentCount, total);
    }

    @Override
    protected void onCount(final long currentCount) {
        if (currentCount == total) {
            logger.info("{}Done. Processed all {} elements.", prefix, total);
        }
    }
}
