package ch.uetlib.core.reflection;

import org.jspecify.annotations.Nullable;

public class IntrospectionWrapper {

    private final Object object;

    public IntrospectionWrapper(final Object object) {
        this.object = object;
    }

    @Nullable
    public Object getPropertyValue(final String fieldName) {
        try {
            return ReflectionUtils.getFieldValue(object, fieldName);
        } catch (final NoSuchFieldException | IllegalAccessException e) {
            throw new IntrospectionException("could not get property value from object", e);
        }
    }
}
