package ch.ubique.backend.core.validator;

import static ch.ubique.backend.core.validator.ValidationTestUtils.*;

import org.junit.jupiter.api.Test;

import java.util.Set;

@SuppressWarnings("java:S5961")
class AllNullOrAllNotNullValidatorTest {

    @Test
    void testType() {
        assertIsValid(new AnnotatedType(null, null, null, null));
        assertIsValid(new AnnotatedType(1, 2, null, null));
        assertIsValid(new AnnotatedType(null, null, 3, 4));
        assertIsValid(new AnnotatedType(1, 2, 3, 4));

        assertIsNotValid(
                new AnnotatedType(1, null, null, null),
                Set.of(
                        "Either all or exactly none the provided fields must be non-null. Fields:"
                                + " '[prop1, prop2]'."));
        assertIsNotValid(
                new AnnotatedType(null, 2, null, null),
                Set.of(
                        "Either all or exactly none the provided fields must be non-null. Fields:"
                                + " '[prop1, prop2]'."));
        assertIsNotValid(
                new AnnotatedType(null, null, 3, null),
                Set.of(
                        "Either all or exactly none the provided fields must be non-null. Fields:"
                                + " '[prop3, prop4]'."));
        assertIsNotValid(
                new AnnotatedType(null, null, null, 4),
                Set.of(
                        "Either all or exactly none the provided fields must be non-null. Fields:"
                                + " '[prop3, prop4]'."));
        assertIsNotValid(
                new AnnotatedType(1, null, 3, null),
                Set.of(
                        "Either all or exactly none the provided fields must be non-null. Fields:"
                                + " '[prop1, prop2]'.",
                        "Either all or exactly none the provided fields must be non-null. Fields:"
                                + " '[prop3, prop4]'."));
        assertIsNotValid(
                new AnnotatedType(1, null, null, 4),
                Set.of(
                        "Either all or exactly none the provided fields must be non-null. Fields:"
                                + " '[prop1, prop2]'.",
                        "Either all or exactly none the provided fields must be non-null. Fields:"
                                + " '[prop3, prop4]'."));
        assertIsNotValid(
                new AnnotatedType(null, 2, null, 4),
                Set.of(
                        "Either all or exactly none the provided fields must be non-null. Fields:"
                                + " '[prop1, prop2]'.",
                        "Either all or exactly none the provided fields must be non-null. Fields:"
                                + " '[prop3, prop4]'."));
        assertIsNotValid(
                new AnnotatedType(null, 2, 3, null),
                Set.of(
                        "Either all or exactly none the provided fields must be non-null. Fields:"
                                + " '[prop1, prop2]'.",
                        "Either all or exactly none the provided fields must be non-null. Fields:"
                                + " '[prop3, prop4]'."));
        assertIsNotValid(
                new AnnotatedType(null, 2, null, 4),
                Set.of(
                        "Either all or exactly none the provided fields must be non-null. Fields:"
                                + " '[prop1, prop2]'.",
                        "Either all or exactly none the provided fields must be non-null. Fields:"
                                + " '[prop3, prop4]'."));
        assertIsNotValid(
                new AnnotatedType(1, 2, 3, null),
                Set.of(
                        "Either all or exactly none the provided fields must be non-null. Fields:"
                                + " '[prop3, prop4]'."));
        assertIsNotValid(
                new AnnotatedType(1, 2, null, 4),
                Set.of(
                        "Either all or exactly none the provided fields must be non-null. Fields:"
                                + " '[prop3, prop4]'."));
        assertIsNotValid(
                new AnnotatedType(1, null, 3, 4),
                Set.of(
                        "Either all or exactly none the provided fields must be non-null. Fields:"
                                + " '[prop1, prop2]'."));
        assertIsNotValid(
                new AnnotatedType(null, 2, 3, 4),
                Set.of(
                        "Either all or exactly none the provided fields must be non-null. Fields:"
                                + " '[prop1, prop2]'."));

        // the following are non-annotated and thus all valid (sanity check)
        assertIsValid(new NonAnnotatedType(null, null, null, null));
        assertIsValid(new NonAnnotatedType(1, null, null, null));
        assertIsValid(new NonAnnotatedType(null, 2, null, null));
        assertIsValid(new NonAnnotatedType(null, null, 3, null));
        assertIsValid(new NonAnnotatedType(null, null, null, 4));
        assertIsValid(new NonAnnotatedType(1, 2, null, null));
        assertIsValid(new NonAnnotatedType(1, null, 3, null));
        assertIsValid(new NonAnnotatedType(1, null, null, 4));
        assertIsValid(new NonAnnotatedType(null, 2, null, 4));
        assertIsValid(new NonAnnotatedType(null, 2, 3, null));
        assertIsValid(new NonAnnotatedType(null, 2, null, 4));
        assertIsValid(new NonAnnotatedType(null, null, 3, 4));
        assertIsValid(new NonAnnotatedType(1, 2, 3, null));
        assertIsValid(new NonAnnotatedType(1, 2, null, 4));
        assertIsValid(new NonAnnotatedType(1, null, 3, 4));
        assertIsValid(new NonAnnotatedType(null, 2, 3, 4));
        assertIsValid(new NonAnnotatedType(1, 2, 3, 4));
    }

    @AllNullOrAllNotNull(fields = {"prop1", "prop2"})
    @AllNullOrAllNotNull(fields = {"prop3", "prop4"})
    record AnnotatedType(Integer prop1, Integer prop2, Integer prop3, Integer prop4) {}

    record NonAnnotatedType(Integer prop1, Integer prop2, Integer prop3, Integer prop4) {}
}
