package ch.ubique.backend.core.service;

import ch.ubique.backend.core.io.FileProvider;

import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public record DataAndFileProvider<T>(
        @NonNull T data, @NonNull Collection<@NonNull FileProvider> inputFileProviders)
        implements AutoCloseable {

    public static <T> DataAndFileProvider<T> of(@NonNull final T data) {
        return new DataAndFileProvider<>(data, List.of());
    }

    public static <T> DataAndFileProvider<T> of(
            @NonNull final T data, @NonNull final FileProvider inputFileProvider) {
        return new DataAndFileProvider<>(data, List.of(inputFileProvider));
    }

    @NonNull
    public <R> DataAndFileProvider<R> map(@NonNull final Function<T, R> mapper) {
        return new DataAndFileProvider<>(mapper.apply(data), inputFileProviders);
    }

    @Override
    public void close() throws IOException {
        for (final var inputFileProvider : inputFileProviders) {
            inputFileProvider.close();
        }
    }
}
