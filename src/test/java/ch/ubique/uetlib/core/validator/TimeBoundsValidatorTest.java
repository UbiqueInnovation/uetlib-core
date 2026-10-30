package ch.ubique.uetlib.core.validator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.validation.Validation;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.Year;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;

@SuppressWarnings("DataFlowIssue")
class TimeBoundsValidatorTest {

    @Test
    void discoversYearValidatorsAndPreservesNumericValidators() {
        try (final var factory = Validation.buildDefaultValidatorFactory()) {
            final var validator = factory.getValidator();
            for (final var year : new Year[] {null, Year.of(1900), Year.of(2000), Year.of(2100)}) {
                assertEquals(0, validator.validate(new YearRange(year)).size());
            }
            for (final var year :
                    new Year[] {
                        Year.of(1899),
                        Year.of(2101),
                        Year.of(Year.MIN_VALUE),
                        Year.of(Year.MAX_VALUE)
                    }) {
                assertEquals(1, validator.validate(new YearRange(year)).size());
            }
            assertEquals(2, validator.validate(new ImpossibleYearBounds(Year.of(2000))).size());
            assertEquals(0, validator.validate(new ImpossibleYearBounds(null)).size());
            assertEquals(0, validator.validate(new NumericRange(2000)).size());
            assertEquals(1, validator.validate(new NumericRange(1899)).size());
            assertEquals(1, validator.validate(new NumericRange(2101)).size());
        }
    }

    record YearRange(@Min(1900) @Max(2100) @Nullable Year value) {}

    record ImpossibleYearBounds(@Min(Long.MAX_VALUE) @Max(Long.MIN_VALUE) @Nullable Year value) {}

    record NumericRange(@Min(1900) @Max(2100) long value) {}
}
