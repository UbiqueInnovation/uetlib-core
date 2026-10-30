package ch.ubique.uetlib.core.validator;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/** Validates that the annotated element is null or not blank. Only allowed on char sequences. */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE, RECORD_COMPONENT})
@Retention(RUNTIME)
@Repeatable(NullOrNotBlank.List.class)
@Constraint(validatedBy = NullOrNotBlankValidator.class)
@Documented
public @interface NullOrNotBlank {

    String message() default "value '${validatedValue}' must be null or not blank";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * Defines several {@link NullOrNotBlank} annotations on the same element.
     *
     * @see NullOrNotBlank
     */
    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE, RECORD_COMPONENT})
    @Retention(RUNTIME)
    @Documented
    @interface List {

        NullOrNotBlank[] value();
    }
}
