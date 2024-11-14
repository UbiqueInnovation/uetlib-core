package ch.ubique.backend.core.utils;

import static ch.ubique.backend.core.utils.AnnotationUtils.findAnnotation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

class AnnotationUtilsTest {

    // Tests for Class-Level Annotations
    @Test
    void testFindClassAnnotationDirect() {
        final var annotation = findAnnotation(AnnotatedClass.class, MyAnnotation.class);
        assertTrue(annotation.isPresent(), "Direct annotation should be found on the class");
        assertEquals(
                "Direct Value", annotation.get().value(), "Class annotation value should match");
    }

    @Test
    void testFindClassAnnotationMeta() {
        final var annotation = findAnnotation(MetaAnnotatedClass.class, MyAnnotation.class);
        assertTrue(annotation.isPresent(), "Meta-annotation should be found on the class");
        assertEquals(
                "Meta Annotation", annotation.get().value(), "Class annotation value should match");
    }

    @Test
    void testFindClassAnnotationInherited() {
        final var annotation = findAnnotation(SubAnnotatedClass.class, MyAnnotation.class);
        assertTrue(annotation.isPresent(), "Inherited annotation should be found on the class");
        assertEquals(
                "Direct Value", annotation.get().value(), "Class annotation value should match");
    }

    @Test
    void testClassWithoutAnnotation() {
        final var annotation = findAnnotation(NoAnnotationClass.class, MyAnnotation.class);
        assertFalse(annotation.isPresent(), "Class without annotation should return empty");
    }

    @Test
    void testInnerClassExcluded() {
        final var annotation = findAnnotation(AnnotatedClass.InnerClass.class, MyAnnotation.class);
        assertFalse(annotation.isPresent(), "Inner class should be excluded");
    }

    @Test
    void testStaticNestedClassExcluded() {
        final var annotation =
                findAnnotation(AnnotatedClass.StaticNestedClass.class, MyAnnotation.class);
        assertFalse(annotation.isPresent(), "Static nested class should be excluded");
    }

    // Tests for Method-Level Annotations
    @Test
    void testFindMethodAnnotationDirect() throws NoSuchMethodException {
        final var method = AnnotatedClass.class.getDeclaredMethod("directlyAnnotatedMethod");
        final var annotation = findAnnotation(method, MyAnnotation.class);

        assertTrue(annotation.isPresent(), "Direct annotation should be found on the method");
        assertEquals(
                "Direct Annotation",
                annotation.get().value(),
                "Method annotation value should match");
    }

    @Test
    void testFindMethodAnnotationMeta() throws NoSuchMethodException {
        final var method = AnnotatedClass.class.getDeclaredMethod("metaAnnotatedMethod");
        final var annotation = findAnnotation(method, MyAnnotation.class);

        assertTrue(annotation.isPresent(), "Meta-annotation should be found on the method");
        assertEquals(
                "Meta Annotation",
                annotation.get().value(),
                "Method annotation value should match");
    }

    @Test
    void testFindMethodAnnotationOnOverriddenMethod() throws NoSuchMethodException {
        final var method = SubAnnotatedClass.class.getDeclaredMethod("directlyAnnotatedMethod");
        final var annotation = findAnnotation(method, MyAnnotation.class);

        assertTrue(annotation.isPresent(), "Annotation on overridden method should be found");
        assertEquals(
                "Direct Annotation",
                annotation.get().value(),
                "Method annotation value should match");
    }

    @Test
    void testFindMethodAnnotationOnInterfaceMethod() throws NoSuchMethodException {
        final var method =
                InterfaceImplementingClass.class.getDeclaredMethod("interfaceAnnotatedMethod");
        final var annotation = findAnnotation(method, MyAnnotation.class);

        assertTrue(annotation.isPresent(), "Annotation on interface method should be found");
        assertEquals(
                "Interface Annotation",
                annotation.get().value(),
                "Method annotation value should match");
    }

    @Test
    void testMethodWithoutAnnotation() throws NoSuchMethodException {
        final var method = AnnotatedClass.class.getDeclaredMethod("nonAnnotatedMethod");
        final var annotation = findAnnotation(method, MyAnnotation.class);

        assertFalse(annotation.isPresent(), "Method without annotation should return empty");
    }

    @Test
    void testFindMethodAnnotationOnSuperclassMethodWithoutAnnotation()
            throws NoSuchMethodException {
        final var method = SubAnnotatedClass.class.getDeclaredMethod("nonAnnotatedMethod");
        final var annotation = findAnnotation(method, MyAnnotation.class);

        assertFalse(
                annotation.isPresent(), "Superclass method without annotation should return empty");
    }

    // Example annotations for testing
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.METHOD})
    public @interface MyAnnotation {
        String value() default "Default Value";
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.METHOD})
    @MyAnnotation("Meta Annotation")
    public @interface MetaAnnotation {}

    // Example classes for testing
    @MyAnnotation("Direct Value")
    public static class AnnotatedClass {
        @MyAnnotation("Direct Annotation")
        public void directlyAnnotatedMethod() {}

        @MetaAnnotation
        public void metaAnnotatedMethod() {}

        public void nonAnnotatedMethod() {}

        @SuppressWarnings("InnerClassMayBeStatic")
        public class InnerClass {}

        public static class StaticNestedClass {}
    }

    @MetaAnnotation
    public static class MetaAnnotatedClass {}

    public static class SubAnnotatedClass extends AnnotatedClass {
        @Override
        public void directlyAnnotatedMethod() {}

        @SuppressWarnings("RedundantMethodOverride")
        @Override
        public void nonAnnotatedMethod() {}
    }

    public interface AnnotatedInterface {
        @MyAnnotation("Interface Annotation")
        void interfaceAnnotatedMethod();
    }

    public static class InterfaceImplementingClass implements AnnotatedInterface {
        @Override
        public void interfaceAnnotatedMethod() {}
    }

    public static class NoAnnotationClass {}
}
