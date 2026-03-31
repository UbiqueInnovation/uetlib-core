package ch.ubique.backend.core.localized.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonLocalizedValue {

    SerializationMethod keySerialization() default SerializationMethod.DEFAULT;

    enum SerializationMethod {
        DEFAULT,
        UPPERCASE,
        CAMEL_CASE,
        PASCAL_CASE
    }
}
