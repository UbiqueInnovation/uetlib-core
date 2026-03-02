package ch.ubique.backend.core.service;

import ch.ubique.backend.core.io.FileProvider;

import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.util.Map;
import java.util.function.Function;

public final class DataAndFileProvider<T> implements AutoCloseable {

    private final T data;
    private final Map<String, FileProvider> fileProviders;

    private DataAndFileProvider(
            @NonNull final T data,
            @NonNull final Map<@NonNull String, @NonNull FileProvider> fileProviders) {
        this.data = data;
        this.fileProviders = fileProviders;
    }

    public static <T> DataAndFileProvider<T> of(@NonNull final T data) {
        return new DataAndFileProvider<>(data, Map.of());
    }

    public static <T> DataAndFileProvider<T> of(
            @NonNull final T data,
            @NonNull final String name,
            @NonNull final FileProvider fileProvider) {
        return new DataAndFileProvider<>(data, Map.of(name, fileProvider));
    }

    public static <T> DataAndFileProvider<T> of(
            @NonNull final T data,
            @NonNull final Map<@NonNull String, @NonNull FileProvider> fileProviders) {
        return new DataAndFileProvider<>(data, fileProviders);
    }

    @NonNull
    public <R> DataAndFileProvider<R> map(@NonNull final Function<T, R> mapper) {
        return new DataAndFileProvider<>(mapper.apply(data), fileProviders);
    }

    @NonNull
    public T data() {
        return data;
    }

    @NonNull
    public Map<String, FileProvider> fileProviders() {
        return fileProviders;
    }

    @Override
    public void close() throws IOException {
        for (final var fileProvider : fileProviders.values()) {
            fileProvider.close();
        }
    }
}
