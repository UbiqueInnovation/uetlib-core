package ch.ubique.uetlib.core.validator;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;

import java.lang.annotation.*;

@Target(TYPE)
@Retention(RUNTIME)
@Repeatable(AllNullOrAllNotNull.List.class)
@Constraint(validatedBy = AllNullOrAllNotNullValidator.class)
@Documented
@NotNull
public @interface AllNullOrAllNotNull {

    String message() default
            "Either all or exactly none the provided fields must be non-null. Fields: '{fields}'.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /** Fields to validate */
    String[] fields() default {};

    /**
     * Defines several {@link AllNullOrAllNotNull} annotations on the same element.
     *
     * @see AllNullOrAllNotNull
     */
    @Target(TYPE)
    @Retention(RUNTIME)
    @Documented
    @interface List {

        AllNullOrAllNotNull[] value();
    }
}
