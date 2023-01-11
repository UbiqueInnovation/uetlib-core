package ch.ubique.backend.core.utils;

import com.github.benmanes.caffeine.cache.Cache;
import java.util.function.Supplier;
import org.checkerframework.checker.nullness.qual.PolyNull;

public final class SingleCache<T> {
    private static final String KEY = "KEY";

    private final Cache<String, T> cache;

    public SingleCache(Cache<String, T> cache) {
        this.cache = cache;
    }

    public SingleCache(Cache<String, T> cache, T initialValue) {
        this(cache);
        set(initialValue);
    }

    public void set(T value) {
        cache.put(KEY, value);
    }

    public T getIfPresent() {
        return cache.getIfPresent(KEY);
    }

    public T get(Supplier<? extends @PolyNull T> valueGetter) {
        return cache.get(KEY, key -> valueGetter.get());
    }
}
