package ch.ubique.backend.core.utils;

import static java.lang.Math.max;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProgressLogger {

    private final long total;
    private final AtomicLong count;
    private final long startTime;

    private final Logger logger;

    public ProgressLogger(final long total) {
        this.total = total;
        this.startTime = System.currentTimeMillis();
        this.count = new AtomicLong();
        this.logger = LoggerFactory.getLogger(getClass());
    }

    public <T, R> Function<T, R> watch(final Function<T, R> function) {
        return t -> {
            logProgress();
            return function.apply(t);
        };
    }

    private void logProgress() {
        final var currentCount = count.incrementAndGet();
        if (currentCount % max(1, total / 100) == 0) {
            final var percent = 100 * currentCount / total;
            final var currentTime = System.currentTimeMillis();
            final var elementsPerSecond = 1000. * currentCount / (currentTime - startTime);
            logger.info("Processed {}% ({} elements per second)", percent, elementsPerSecond);
        }
    }

    public <T> Consumer<T> watch(final Consumer<T> consumer) {
        return t -> {
            logProgress();
            consumer.accept(t);
        };
    }
}
