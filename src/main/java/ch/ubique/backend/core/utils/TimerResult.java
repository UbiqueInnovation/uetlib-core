package ch.ubique.backend.core.utils;

import org.jspecify.annotations.Nullable;

import java.time.Duration;

public record TimerResult<R extends @Nullable Object>(@Nullable R value, Duration duration) {}
