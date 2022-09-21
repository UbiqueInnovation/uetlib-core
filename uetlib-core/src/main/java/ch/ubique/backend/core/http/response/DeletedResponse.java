package ch.ubique.backend.core.http.response;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class DeletedResponse {
    private final List<Integer> ids;

    private DeletedResponse(List<Integer> ids) {
        this.ids = ids;
    }

    public static DeletedResponse from(@NotNull final Integer... ids) {
        return new DeletedResponse(Arrays.asList(ids));
    }

    public static DeletedResponse from(@NotNull final List<Integer> ids) {
        return new DeletedResponse(ids);
    }

    public List<Integer> getIds() {
        return ids;
    }
}
