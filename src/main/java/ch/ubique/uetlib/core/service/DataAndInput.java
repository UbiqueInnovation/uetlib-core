package ch.ubique.uetlib.core.service;

import ch.ubique.uetlib.core.file.ByteFile;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * @deprecated since 2.1.0, use {@link DataAndFileProvider} instead
 */
@Deprecated(since = "2.1.0")
public record DataAndInput<T>(
        @JsonProperty(required = true) @NotNull T data,
        @JsonProperty(required = true) @NotNull Collection<ByteFile> inputFiles) {

    public DataAndInput(final T data) {
        this(data, List.of());
    }

    public DataAndInput(final T data, final ByteFile inputFile) {
        this(data, List.of(inputFile));
    }

    public <R> DataAndInput<R> map(final Function<T, R> mapper) {
        return new DataAndInput<>(mapper.apply(data), inputFiles);
    }
}
