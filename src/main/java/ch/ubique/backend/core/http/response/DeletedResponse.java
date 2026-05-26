package ch.ubique.backend.core.http.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.util.Arrays;
import java.util.List;

public record DeletedResponse(@JsonProperty(required = true) @NotNull List<Integer> ids) {

    public DeletedResponse(final Integer... ids) {
        this(Arrays.asList(ids));
    }
}
