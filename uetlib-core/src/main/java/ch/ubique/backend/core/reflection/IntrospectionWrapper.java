package ch.ubique.backend.core.reflection;

public class IntrospectionWrapper {

    private final Object object;

    public IntrospectionWrapper(Object object) {
        this.object = object;
    }

    public Object getPropertyValue(String fieldName) {
        try {
            return ReflectionUtils.getFieldValue(object, fieldName);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IntrospectionException("could not get property value from object",e);
        }
    }
}
