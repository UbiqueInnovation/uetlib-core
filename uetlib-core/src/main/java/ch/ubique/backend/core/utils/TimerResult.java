package ch.ubique.backend.core.utils;

import java.time.Duration;

public record TimerResult<R>(R value, Duration duration) {}
