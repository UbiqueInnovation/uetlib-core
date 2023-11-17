package ch.ubique.backend.core.utils;

import jakarta.annotation.Nullable;
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
    protected void logProgress(final long currentCount) {
        final var currentTime = System.currentTimeMillis();
        final var elementsPerSecond = 1000.0 * currentCount / (currentTime - startTime);
        logger.info(
                "{}Processed {} elements ({} elements per second)",
                prefix,
                currentCount,
                elementsPerSecond);
    }
}
