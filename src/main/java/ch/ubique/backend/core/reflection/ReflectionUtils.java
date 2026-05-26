package ch.ubique.backend.core.reflection;

import java.util.List;
import java.util.function.Function;

public final class ReflectionUtils {

    private ReflectionUtils() {}

    @SuppressWarnings("java:S3011")
    public static Object getFieldValue(final Object object, final String fieldName)
            throws NoSuchFieldException, IllegalAccessException {
        final var field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(object);
    }
}
