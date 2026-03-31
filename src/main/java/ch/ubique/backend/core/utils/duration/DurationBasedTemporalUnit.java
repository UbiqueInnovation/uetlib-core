package ch.ubique.backend.core.utils.duration;

import java.time.Duration;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;
import java.util.Objects;

public final class DurationBasedTemporalUnit implements TemporalUnit {

    private static final int SECONDS_PER_DAY = 86400;
    private static final long NANOS_PER_SECOND = 1000_000_000L;
    private static final long NANOS_PER_DAY = NANOS_PER_SECOND * SECONDS_PER_DAY;

    private final Duration duration;

    private DurationBasedTemporalUnit(final Duration duration) {
        if (duration.isZero() || duration.isNegative()) {
            throw new IllegalArgumentException(
                    "Duration unit may not have a zero or negative duration.");
        }
        this.duration = duration;
    }

    public static DurationBasedTemporalUnit of(final Duration duration) {
        return new DurationBasedTemporalUnit(duration);
    }

    public static DurationBasedTemporalUnit ofDays(final long days) {
        return new DurationBasedTemporalUnit(Duration.ofDays(days));
    }

    public static DurationBasedTemporalUnit ofHours(final long hours) {
        return new DurationBasedTemporalUnit(Duration.ofHours(hours));
    }

    public static DurationBasedTemporalUnit ofMinutes(final long minutes) {
        return new DurationBasedTemporalUnit(Duration.ofMinutes(minutes));
    }

    public static DurationBasedTemporalUnit ofSeconds(final long seconds) {
        return new DurationBasedTemporalUnit(Duration.ofSeconds(seconds));
    }

    public static DurationBasedTemporalUnit ofMillis(final long millis) {
        return new DurationBasedTemporalUnit(Duration.ofMillis(millis));
    }

    public static DurationBasedTemporalUnit ofNanos(final long nanos) {
        return new DurationBasedTemporalUnit(Duration.ofNanos(nanos));
    }

    @Override
    public Duration getDuration() {
        return duration;
    }

    @Override
    public boolean isDurationEstimated() {
        return (duration.getSeconds() >= SECONDS_PER_DAY);
    }

    @Override
    public boolean isDateBased() {
        return (duration.getNano() == 0 && duration.getSeconds() % SECONDS_PER_DAY == 0);
    }

    @Override
    public boolean isTimeBased() {
        return (duration.getSeconds() < SECONDS_PER_DAY && NANOS_PER_DAY % duration.toNanos() == 0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R extends Temporal> R addTo(final R temporal, final long amount) {
        return (R) duration.multipliedBy(amount).addTo(temporal);
    }

    @Override
    public long between(final Temporal temporal1Inclusive, final Temporal temporal2Exclusive) {
        return Duration.between(temporal1Inclusive, temporal2Exclusive).dividedBy(duration);
    }

    @Override
    public String toString() {
        return duration.toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof final DurationBasedTemporalUnit that)) {
            return false;
        }
        return Objects.equals(duration, that.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(duration);
    }
}
