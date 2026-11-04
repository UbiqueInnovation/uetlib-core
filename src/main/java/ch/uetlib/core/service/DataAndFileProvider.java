package ch.uetlib.core.service;

import ch.uetlib.core.io.FileProvider;

import java.io.IOException;
import java.util.Map;
import java.util.function.Function;

public final class DataAndFileProvider<T> implements AutoCloseable {

    private final T data;
    private final Map<String, FileProvider> fileProviders;

    private DataAndFileProvider(final T data, final Map<String, FileProvider> fileProviders) {
        this.data = data;
        this.fileProviders = fileProviders;
    }

    public static <T> DataAndFileProvider<T> of(final T data) {
        return new DataAndFileProvider<>(data, Map.of());
    }

    public static <T> DataAndFileProvider<T> of(
            final T data, final String name, final FileProvider fileProvider) {
        return new DataAndFileProvider<>(data, Map.of(name, fileProvider));
    }

    public static <T> DataAndFileProvider<T> of(
            final T data, final Map<String, FileProvider> fileProviders) {
        return new DataAndFileProvider<>(data, fileProviders);
    }

    public <R> DataAndFileProvider<R> map(final Function<T, R> mapper) {
        return new DataAndFileProvider<>(mapper.apply(data), fileProviders);
    }

    public T data() {
        return data;
    }

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
