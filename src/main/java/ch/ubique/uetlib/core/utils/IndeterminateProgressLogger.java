package ch.ubique.uetlib.core.utils;

import jakarta.validation.constraints.Positive;
import java.util.Map;
import org.jspecify.annotations.Nullable;
import org.slf4j.MDC;

public final class IndeterminateProgressLogger extends AbstractProgressLogger {

    public IndeterminateProgressLogger(@Positive final long stepSize) {
        this(null, stepSize, MDC.getCopyOfContextMap());
    }

    public IndeterminateProgressLogger(
            final @Nullable String prefix,
            @Positive final long stepSize,
            final @Nullable Map<String, String> mdcContextMap) {
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
