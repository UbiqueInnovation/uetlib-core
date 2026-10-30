package ch.ubique.uetlib.core.localized.annotation;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({FIELD, METHOD, PARAMETER, RECORD_COMPONENT})
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
