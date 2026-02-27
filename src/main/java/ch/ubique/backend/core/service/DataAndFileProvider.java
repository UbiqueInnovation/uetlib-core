package ch.ubique.backend.core.service;

import ch.ubique.backend.core.io.FileProvider;

import jakarta.validation.constraints.NotNull;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public record DataAndFileProvider<T>(
        @NotNull T data, @NotNull Collection<FileProvider> inputFileProviders)
        implements AutoCloseable {

    public DataAndFileProvider(@NotNull final T data) {
        this(data, List.of());
    }

    public DataAndFileProvider(
            @NotNull final T data, @NotNull final FileProvider inputFileProvider) {
        this(data, List.of(inputFileProvider));
    }

    @NotNull
    public <R> DataAndFileProvider<R> map(@NotNull final Function<T, R> mapper) {
        return new DataAndFileProvider<>(mapper.apply(data), inputFileProviders);
    }

    @Override
    public void close() throws IOException {
        for (final var inputFileProvider : inputFileProviders) {
            inputFileProvider.close();
        }
    }
}
