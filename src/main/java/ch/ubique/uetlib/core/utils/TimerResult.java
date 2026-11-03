package ch.ubique.uetlib.core.utils;

import org.jspecify.annotations.Nullable;

import java.time.Duration;

public record TimerResult<R extends @Nullable Object>(R value, Duration duration) {}
