package ch.ubique.backend.core.utils;

import static java.lang.Math.max;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Function;

public class ProgressLogger {

    private final String prefix;
    private final long total;
    private final AtomicLong count;
    private final long startTime;
    private final Map<String, String> mdcContextMap;
    private final Logger logger;

    public ProgressLogger(final long total) {
        this(null, total);
    }

    public ProgressLogger(final String prefix, final long total) {
        this(prefix, total, null);
    }

    public ProgressLogger(
            final String prefix, final long total, final Map<String, String> mdcContextMap) {
        this.prefix = Objects.requireNonNullElse(prefix, "");
        this.total = total;
        this.startTime = System.currentTimeMillis();
        this.count = new AtomicLong();
        this.logger = LoggerFactory.getLogger(getClass());
        this.mdcContextMap = mdcContextMap;
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
        if (currentCount % max(1, total / 100) == 0) {
            final var percent = 100 * currentCount / total;
            final var currentTime = System.currentTimeMillis();
            final var elementsPerSecond = 1000.0 * currentCount / (currentTime - startTime);
            logger.info(
                    "{}Processed {}% ({} elements per second, {} of {})",
                    prefix, percent, elementsPerSecond, currentCount, total);
        }
    }

    public <T> Consumer<T> watch(final Consumer<T> consumer) {
        return t -> {
            consumer.accept(t);
            step();
        };
    }
}
