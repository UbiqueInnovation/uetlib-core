package ch.ubique.backend.core.validator;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(TYPE)
@Retention(RUNTIME)
@Repeatable(AtLeastOneNull.List.class)
@Constraint(validatedBy = AtLeastOneNullValidator.class)
@Documented
public @interface AtLeastOneNull {

    String message() default
            "At least one of the provided fields must be null. Fields: '{fields}'.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /** Fields to validate */
    String[] fields();

    /**
     * Defines several {@link AtLeastOneNull} annotations on the same element.
     *
     * @see AtLeastOneNull
     */
    @Target(TYPE)
    @Retention(RUNTIME)
    @Documented
    @interface List {

        AtLeastOneNull[] value();
    }
}
