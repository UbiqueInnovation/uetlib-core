package ch.ubique.uetlib.core.utils;

import java.time.Duration;
import org.jspecify.annotations.Nullable;

public record TimerResult<R extends @Nullable Object>(R value, Duration duration) {}
