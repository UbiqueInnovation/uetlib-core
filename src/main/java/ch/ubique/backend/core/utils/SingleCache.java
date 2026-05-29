package ch.ubique.backend.core.utils;

import com.github.benmanes.caffeine.cache.Cache;

import org.jspecify.annotations.Nullable;

import java.util.function.Supplier;

public final class SingleCache<T> {
    private static final String KEY = "KEY";

    private final Cache<String, T> cache;

    public SingleCache(final Cache<String, T> cache) {
        this.cache = cache;
    }

    public SingleCache(final Cache<String, T> cache, final T initialValue) {
        this(cache);
        set(initialValue);
    }

    public void set(final T value) {
        cache.put(KEY, value);
    }

    @Nullable
    public T getIfPresent() {
        return cache.getIfPresent(KEY);
    }

    public T get(final Supplier<? extends T> valueGetter) {
        return cache.get(KEY, key -> valueGetter.get());
    }
}
