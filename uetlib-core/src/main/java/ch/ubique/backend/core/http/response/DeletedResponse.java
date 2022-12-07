package ch.ubique.backend.core.http.response;

import jakarta.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

public record DeletedResponse(List<Integer> ids) {

    public DeletedResponse(@NotNull final Integer... ids) {
        this(Arrays.asList(ids));
    }
}
