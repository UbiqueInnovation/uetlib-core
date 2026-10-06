package ch.ubique.backend.core.openenum.jackson;

import ch.ubique.backend.core.openenum.OpenEnumSet;
import ch.ubique.backend.core.openenum.OpenEnumValue;

import org.jspecify.annotations.Nullable;

import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.ValueDeserializer;

/**
 * Resolves a raw value into a known constant or an unknown carrier, via the domain's {@link
 * OpenEnumSet}.
 *
 * <p>Unlike serialization, which is uniform for every open enum, this is per domain type: deciding
 * whether {@code "cyan"} is known requires that domain's constants. The set is discovered from the
 * type itself ({@link OpenEnumSet#forType}), so no per-type registration is needed.
 *
 * @param <T> the domain code type
 * @param <V> the raw value type
 */
public final class OpenEnumValueDeserializer<T extends OpenEnumValue<V>, V>
        extends ValueDeserializer<T> {

    private final OpenEnumSet<T, V> openEnumSet;
    private final JavaType rawValueType;

    public OpenEnumValueDeserializer(
            final OpenEnumSet<T, V> openEnumSet, final JavaType rawValueType) {
        this.openEnumSet = openEnumSet;
        this.rawValueType = rawValueType;
    }

    @Override
    public @Nullable T deserialize(final JsonParser p, final DeserializationContext ctxt) {
        // A JSON null is missing data rather than an unknown value, so it stays null instead of
        // being wrapped in an unknown carrier.
        final V raw = ctxt.readValue(p, rawValueType);

        return raw == null ? null : openEnumSet.valueOfOrUnknown(raw);
    }
}
