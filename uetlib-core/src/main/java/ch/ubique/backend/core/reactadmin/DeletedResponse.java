package ch.ubique.backend.core.reactadmin;

import java.util.Arrays;
import java.util.List;
import javax.validation.constraints.NotNull;

public class DeletedResponse {
    private final List<Integer> ids;

    public static DeletedResponse from(@NotNull Integer... ids) {
        return new DeletedResponse(Arrays.asList(ids));
    }

    public static DeletedResponse from(List<Integer> ids) {
        return new DeletedResponse(ids);
    }

    private DeletedResponse(List<Integer> ids) {
        this.ids = ids;
    }

    public List<Integer> getIds() {
        return ids;
    }
}
