package ch.ubique.backend.core.localized.annotation;

import static java.lang.annotation.ElementType.*;

import org.jspecify.annotations.NullMarked;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({FIELD, METHOD, PARAMETER, RECORD_COMPONENT})
@Retention(RetentionPolicy.RUNTIME)
@NullMarked
public @interface JsonLocalizedValue {

    SerializationMethod keySerialization() default SerializationMethod.DEFAULT;

    enum SerializationMethod {
        DEFAULT,
        UPPERCASE,
        CAMEL_CASE,
        PASCAL_CASE
    }
}
