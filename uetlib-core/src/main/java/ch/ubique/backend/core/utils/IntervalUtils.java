package ch.ubique.backend.core.utils;

import java.util.stream.IntStream;

public class IntervalUtils {

    private IntervalUtils() {}

    public static IntStream intervalToIntRange(
            final double lowerInclusive, final double upperExclusive) {

        // If the upper bound is an integer, return the open range excluding the upper bound
        if (Math.floor(upperExclusive) == upperExclusive) {
            return IntStream.range((int) lowerInclusive, (int) upperExclusive);
        }

        // The upper bound is not an integer.
        // Therefore, the rounded-down upper bound should be part of the range
        return IntStream.rangeClosed((int) lowerInclusive, (int) upperExclusive);
    }
}
