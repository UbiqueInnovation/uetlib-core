package ch.ubique.backend.core.utils;

import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record NumericCollectionStats(
        @NotNull int total,
        @NotNull double min,
        @NotNull double max,
        @NotNull double average,
        @NotNull double median,
        @NotNull Map<Integer, Double> percentiles,
        @NotNull double sum) {}
