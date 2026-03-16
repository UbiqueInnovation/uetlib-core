package ch.ubique.backend.core.utils;

import org.jspecify.annotations.Nullable;
import jakarta.validation.constraints.Positive;

import org.slf4j.MDC;

import java.util.Map;

public final class IndeterminateProgressLogger extends AbstractProgressLogger {

    public IndeterminateProgressLogger(@Positive final long stepSize) {
        this(null, stepSize, MDC.getCopyOfContextMap());
    }

    public IndeterminateProgressLogger(
            @Nullable final String prefix,
            @Positive final long stepSize,
            @Nullable final Map<String, String> mdcContextMap) {
        super(prefix, stepSize, mdcContextMap);
    }

    @Override
    protected void logProgress(
            final long currentCount,
            final double elementsPerSecond,
            final double overallElementsPerSecond) {
        if (logger.isInfoEnabled()) {
            logger.info(
                    "{}Processed {} elements (current {} elements per second, overall {} elements"
                            + " per second)",
                    prefix,
                    currentCount,
                    elementsPerSecond,
                    overallElementsPerSecond);
        }
    }
}
