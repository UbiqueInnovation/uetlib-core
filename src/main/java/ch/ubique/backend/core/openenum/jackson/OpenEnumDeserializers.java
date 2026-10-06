package ch.ubique.backend.core.openenum.jackson;

import ch.ubique.backend.core.openenum.OpenEnumSet;
import ch.ubique.backend.core.openenum.OpenEnumValue;

import org.jspecify.annotations.Nullable;

import tools.jackson.databind.BeanDescription;
import tools.jackson.databind.DeserializationConfig;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.deser.Deserializers;

/**
 * Supplies a deserializer for any {@link OpenEnumValue}, resolving the domain's {@link OpenEnumSet}
 * from the type being deserialized.
 *
 * <p>Matching on the interface rather than on registered classes is what removes per-type setup: a
 * new open enum works as soon as it exists, with nothing to remember to wire up.
 */
public final class OpenEnumDeserializers extends Deserializers.Base {

    @Override
    public boolean hasDeserializerFor(
            final DeserializationConfig config, final Class<?> valueType) {

        return OpenEnumValue.class.isAssignableFrom(valueType);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public @Nullable ValueDeserializer<?> findBeanDeserializer(
            final JavaType type,
            final DeserializationConfig config,
            final BeanDescription.Supplier beanDescription) {

        final var rawClass = type.getRawClass();

        if (!OpenEnumValue.class.isAssignableFrom(rawClass)) {
            return null;
        }

        // Only the domain type itself owns an OpenEnumSet. The nested known enum and unknown
        // carrier are deserialized through their shared interface, never directly.
        if (!rawClass.isInterface()) {
            return null;
        }

        final var openEnumSet = OpenEnumSet.forType((Class<OpenEnumValue<Object>>) rawClass);

        return new OpenEnumValueDeserializer(openEnumSet, rawValueTypeOf(type, config));
    }

    /**
     * The {@code V} of {@code OpenEnumValue<V>} as declared by the domain type, so the raw value is
     * read as the right JSON type (string, integer, ...) rather than being guessed.
     */
    private static JavaType rawValueTypeOf(
            final JavaType type, final DeserializationConfig config) {

        final var parameters =
                config.getTypeFactory().findTypeParameters(type, OpenEnumValue.class);

        return parameters == null || parameters.length == 0
                ? config.constructType(Object.class)
                : parameters[0];
    }
}
