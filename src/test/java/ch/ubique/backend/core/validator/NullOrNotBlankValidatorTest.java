package ch.ubique.backend.core.validator;

import static ch.ubique.backend.core.validator.ValidationTestUtils.*;

import jakarta.validation.Valid;

import org.junit.jupiter.api.Test;

import java.util.Set;

class NullOrNotBlankValidatorTest {
    @Test
    void test() {
        assertIsValid(new Model(null));
        assertIsValid(new Model("aaaa"));
        assertIsValid(new Model(" aaaa "));

        assertIsNotValid(new Model(""), Set.of("value '' must be null or not blank"));
        assertIsNotValid(new Model("\t"), Set.of("value '\t' must be null or not blank"));
        assertIsNotValid(new Model(" "), Set.of("value ' ' must be null or not blank"));
    }

    @Valid
    record Model(@NullOrNotBlank String s) {}
}
