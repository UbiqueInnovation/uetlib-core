package ch.uetlib.core.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Optional;

public final class AnnotationUtils {

    private AnnotationUtils() {}

    public static <T extends Annotation> Optional<T> findAnnotation(
            final Class<?> clazz, final Class<T> annotationClass) {
        // Exclude inner and static classes
        if (isInnerOrStaticClass(clazz)) {
            return Optional.empty();
        }

        // Check direct annotation
        final var directAnnotation = clazz.getAnnotation(annotationClass);
        if (directAnnotation != null) {
            return Optional.of(directAnnotation);
        }

        // Check meta-annotations
        for (final var annotation : clazz.getAnnotations()) {
            final var metaAnnotation = annotation.annotationType().getAnnotation(annotationClass);
            if (metaAnnotation != null) {
                return Optional.of(metaAnnotation);
            }
        }

        // Traverse superclasses for @Inherited annotations
        var superclass = clazz.getSuperclass();
        while (superclass != null) {
            if (isInnerOrStaticClass(superclass)) {
                superclass = superclass.getSuperclass();
                continue;
            }

            final var inheritedAnnotation = superclass.getAnnotation(annotationClass);
            if (inheritedAnnotation != null) {
                return Optional.of(inheritedAnnotation);
            }
            superclass = superclass.getSuperclass();
        }

        return Optional.empty();
    }

    public static <T extends Annotation> Optional<T> findAnnotation(
            final Method method, final Class<T> annotationClass) {
        // Check direct annotation
        final var directAnnotation = method.getAnnotation(annotationClass);
        if (directAnnotation != null) {
            return Optional.of(directAnnotation);
        }

        // Check meta-annotations
        for (final var annotation : method.getAnnotations()) {
            final var metaAnnotation = annotation.annotationType().getAnnotation(annotationClass);
            if (metaAnnotation != null) {
                return Optional.of(metaAnnotation);
            }
        }

        // Traverse overridden methods
        final var declaringClass = method.getDeclaringClass();
        for (final var interfaces : declaringClass.getInterfaces()) {
            try {
                final var interfaceMethod =
                        interfaces.getMethod(method.getName(), method.getParameterTypes());
                final var inheritedAnnotation =
                        findAnnotation(interfaceMethod, annotationClass).orElse(null);
                if (inheritedAnnotation != null) {
                    return Optional.of(inheritedAnnotation);
                }
            } catch (final NoSuchMethodException ignored) {
                // This interface does not declare the method; check the next one.
            }
        }

        // Check superclass methods
        final var superclass = declaringClass.getSuperclass();
        if (superclass != null) {
            try {
                final var superMethod =
                        superclass.getMethod(method.getName(), method.getParameterTypes());
                return findAnnotation(superMethod, annotationClass);
            } catch (final NoSuchMethodException ignored) {
                // The superclass does not declare the method: the annotation is not inherited.
            }
        }

        return Optional.empty();
    }

    private static boolean isInnerOrStaticClass(final Class<?> clazz) {
        return clazz.getEnclosingClass() != null
                && !java.lang.reflect.Modifier.isStatic(clazz.getModifiers());
    }
}
