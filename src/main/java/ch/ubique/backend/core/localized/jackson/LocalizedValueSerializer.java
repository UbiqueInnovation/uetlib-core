package ch.ubique.backend.core.localized.jackson;

import static ch.ubique.backend.core.localized.jackson.LocalizedValueKeyTransformers.keySerializationTransformer;

import static tools.jackson.databind.util.NameTransformer.chainedTransformer;

import ch.ubique.backend.core.localized.LocalizedValue;
import ch.ubique.backend.core.localized.annotation.JsonLocalizedValue;
import ch.ubique.backend.core.localized.annotation.JsonLocalizedValue.SerializationMethod;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.pivovarit.function.ThrowingBiConsumer;

import org.jspecify.annotations.Nullable;

import tools.jackson.core.JsonGenerator;
import tools.jackson.core.JsonToken;
import tools.jackson.databind.BeanProperty;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.ValueSerializer;
import tools.jackson.databind.jsontype.TypeSerializer;
import tools.jackson.databind.util.NameTransformer;

import java.io.Serializable;
import java.util.Map;

/**
 * Serializes a {@link LocalizedValue} to a JSON object. It supports both unwrapped properties and
 * the different key serialization methods configured through annotation @{@link
 * JsonLocalizedValue#keySerialization()}.
 *
 * <p>Serialization always starts from {@link LocalizedValue#toMapWithLanguageTagKeys()}, i.e. the
 * canonical lower-hyphen BCP 47 language tag representation. Additional key formats such as upper
 * case, camel case, or pascal case are derived by applying a {@link NameTransformer} to these
 * canonical keys.
 *
 * <p>For unwrapped serialization Jackson provides a second {@link NameTransformer}. This serializer
 * first applies the key-serialization transformer and then the unwrapping transformer.
 */
public class LocalizedValueSerializer
        extends ValueSerializer<LocalizedValue<? extends Serializable>> {

    @Nullable private final JsonLocalizedValue jsonLocalizedValue;
    @Nullable private final NameTransformer unwrapper;
    private final NameTransformer effectiveTransformer;

    public LocalizedValueSerializer() {
        this(null, null);
    }

    public LocalizedValueSerializer(
            final @Nullable JsonLocalizedValue jsonLocalizedValue,
            final @Nullable NameTransformer unwrapper) {
        this.jsonLocalizedValue = jsonLocalizedValue;
        this.unwrapper = unwrapper;
        this.effectiveTransformer = buildEffectiveTransformer(jsonLocalizedValue, unwrapper);
    }

    @Override
    public void serialize(
            final LocalizedValue localizedValue,
            final JsonGenerator gen,
            final SerializationContext ctxt) {

        if (!isUnwrappingSerializer()) {
            gen.writeStartObject();
        }

        writeProperties(localizedValue, gen);

        if (!isUnwrappingSerializer()) {
            gen.writeEndObject();
        }
    }

    @Override
    public void serializeWithType(
            final LocalizedValue<? extends Serializable> localizedValue,
            final JsonGenerator gen,
            final SerializationContext ctxt,
            final TypeSerializer typeSer) {

        if (isUnwrappingSerializer()) {
            // Unwrapped properties are merged into the surrounding object, so there is no
            // dedicated object boundary to attach a type prefix/suffix to. For inclusion styles
            // that are themselves just another property (PROPERTY / EXISTING_PROPERTY, what
            // Spring Data Redis's GenericJacksonJsonRedisSerializer uses via "@class"), we can
            // still make the value round-trippable by writing the type id as one more flattened
            // property alongside the locale keys -- Jackson's own unwrapped-property
            // deserialization buffers all of a class's unmatched properties together and scans
            // that buffer for the type id property by name, so it will find it here regardless
            // of which sibling property it came from.
            final var inclusion = typeSer.getTypeInclusion();
            if (inclusion == JsonTypeInfo.As.PROPERTY
                    || inclusion == JsonTypeInfo.As.EXISTING_PROPERTY) {
                gen.writeName(typeSer.getPropertyName());
                gen.writeString(typeSer.getTypeIdResolver().idFromValue(ctxt, localizedValue));
                writeProperties(localizedValue, gen);
                return;
            }

            // Other inclusion styles (e.g. WRAPPER_ARRAY, WRAPPER_OBJECT) require their own
            // structural slot (an extra array/object level) that flattened properties can't
            // provide.
            if (ctxt.isEnabled(SerializationFeature.FAIL_ON_UNWRAPPED_TYPE_IDENTIFIERS)) {
                ctxt.reportBadDefinition(
                        handledType(),
                        "Unwrapped property requires use of type information: cannot serialize"
                                + " without disabling"
                                + " `SerializationFeature.FAIL_ON_UNWRAPPED_TYPE_IDENTIFIERS`");
            }
            serialize(localizedValue, gen, ctxt);
            return;
        }

        final var typeIdDef =
                typeSer.writeTypePrefix(
                        gen, ctxt, typeSer.typeId(localizedValue, JsonToken.START_OBJECT));
        writeProperties(localizedValue, gen);
        typeSer.writeTypeSuffix(gen, ctxt, typeIdDef);
    }

    @SuppressWarnings("rawtypes")
    private void writeProperties(final LocalizedValue localizedValue, final JsonGenerator gen) {
        //noinspection unchecked
        final Map<String, Object> stringKeyMap = localizedValue.toMapWithLanguageTagKeys();
        stringKeyMap.forEach(
                ThrowingBiConsumer.sneaky(
                        (key, value) -> {
                            gen.writeName(effectiveTransformer.transform(key));
                            gen.writePOJO(value);
                        }));
    }

    @Override
    public boolean isUnwrappingSerializer() {
        return unwrapper != null;
    }

    @Override
    public Class<?> handledType() {
        return LocalizedValue.class;
    }

    @Override
    public ValueSerializer<?> createContextual(
            final SerializationContext ctxt, final @Nullable BeanProperty property) {
        if (property == null) {
            return new LocalizedValueSerializer();
        }

        final var jsonLocalizedValueAnnotation = property.getAnnotation(JsonLocalizedValue.class);
        return new LocalizedValueSerializer(jsonLocalizedValueAnnotation, unwrapper);
    }

    @Override
    public ValueSerializer<LocalizedValue<? extends Serializable>> unwrappingSerializer(
            final NameTransformer unwrapper) {
        return new LocalizedValueSerializer(jsonLocalizedValue, unwrapper);
    }

    private static NameTransformer buildEffectiveTransformer(
            final @Nullable JsonLocalizedValue jsonLocalizedValue,
            final @Nullable NameTransformer unwrapper) {
        final var keySerializationMethod =
                jsonLocalizedValue != null
                        ? jsonLocalizedValue.keySerialization()
                        : SerializationMethod.DEFAULT;

        final var keySerializationTransformer = keySerializationTransformer(keySerializationMethod);

        return unwrapper != null
                ? chainedTransformer(unwrapper, keySerializationTransformer)
                : keySerializationTransformer;
    }
}
