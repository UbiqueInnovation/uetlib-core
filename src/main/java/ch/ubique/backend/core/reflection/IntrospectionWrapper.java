package ch.ubique.backend.core.reflection;

public class IntrospectionWrapper {

    private final Object object;

    public IntrospectionWrapper(final Object object) {
        this.object = object;
    }

    public Object getPropertyValue(final String fieldName) {
        try {
            return ReflectionUtils.getFieldValue(object, fieldName);
        } catch (final NoSuchFieldException | IllegalAccessException e) {
            throw new IntrospectionException("could not get property value from object", e);
        }
    }
}
