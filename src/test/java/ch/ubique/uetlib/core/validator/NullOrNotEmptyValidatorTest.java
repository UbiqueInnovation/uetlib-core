package ch.ubique.uetlib.core.validator;

import static ch.ubique.uetlib.core.validator.ValidationTestUtils.*;

import jakarta.validation.Valid;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

class NullOrNotEmptyValidatorTest {

    @Test
    void test() {
        // Test valid
        assertIsValid(new ModelList(null));
        assertIsValid(new ModelMap(null));
        assertIsValid(new ModelIntegerArray(null));
        assertIsValid(new ModelIntArray(null));

        assertIsValid(new ModelList(List.of(1)));
        assertIsValid(new ModelMap(Map.of("key", "value")));
        assertIsValid(new ModelIntegerArray(new Integer[] {1}));
        assertIsValid(new ModelIntArray(new int[] {1}));

        // Test invalid empty
        assertIsNotValid(new ModelList(List.of()), Set.of("value '[]' must be null or not empty"));
        assertIsNotValid(new ModelMap(Map.of()), Set.of("value '{}' must be null or not empty"));
        assertIsNotValid(
                new ModelIntegerArray(new Integer[] {}),
                Set.of("value '[]' must be null or not empty"));
        assertIsNotValid(
                new ModelIntArray(new int[] {}), Set.of("value '[]' must be null or not empty"));
    }

    @Valid
    record ModelList(@NullOrNotEmpty List<Integer> list) {}

    @Valid
    record ModelMap(@NullOrNotEmpty Map<String, String> map) {}

    @Valid
    record ModelIntegerArray(@NullOrNotEmpty Integer[] array) {}

    @Valid
    record ModelIntArray(@NullOrNotEmpty int[] array) {}
}
