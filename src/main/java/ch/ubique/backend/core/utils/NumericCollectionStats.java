package ch.ubique.backend.core.utils;

import java.util.Map;

public record NumericCollectionStats(
        int total,
        double min,
        double max,
        double average,
        double median,
        Map<Integer, Double> percentiles,
        double sum) {}
