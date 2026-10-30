package ch.ubique.uetlib.core.validator;

import static ch.ubique.uetlib.core.validator.ValidationTestUtils.*;

import jakarta.validation.Valid;
import java.util.Set;
import org.junit.jupiter.api.Test;

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
