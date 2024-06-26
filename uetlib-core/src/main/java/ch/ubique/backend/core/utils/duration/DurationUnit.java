package ch.ubique.backend.core.utils.duration;

import java.time.Duration;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;
import java.util.Objects;

public final class DurationUnit implements TemporalUnit {

    private static final int SECONDS_PER_DAY = 86400;
    private static final long NANOS_PER_SECOND = 1000_000_000L;
    private static final long NANOS_PER_DAY = NANOS_PER_SECOND * SECONDS_PER_DAY;

    private final Duration duration;

    private DurationUnit(final Duration duration) {
        if (duration.isZero() || duration.isNegative())
            throw new IllegalArgumentException(
                    "Duration unit may not have a zero or negative duration.");
        this.duration = duration;
    }

    public static DurationUnit of(final Duration duration) {
        return new DurationUnit(duration);
    }

    public static DurationUnit ofDays(final long days) {
        return new DurationUnit(Duration.ofDays(days));
    }

    public static DurationUnit ofHours(final long hours) {
        return new DurationUnit(Duration.ofHours(hours));
    }

    public static DurationUnit ofMinutes(final long minutes) {
        return new DurationUnit(Duration.ofMinutes(minutes));
    }

    public static DurationUnit ofSeconds(final long seconds) {
        return new DurationUnit(Duration.ofSeconds(seconds));
    }

    public static DurationUnit ofMillis(final long millis) {
        return new DurationUnit(Duration.ofMillis(millis));
    }

    public static DurationUnit ofNanos(final long nanos) {
        return new DurationUnit(Duration.ofNanos(nanos));
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
        if (this == o) return true;
        if (!(o instanceof final DurationUnit that)) {
            return false;
        }
        return Objects.equals(duration, that.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(duration);
    }
}
