package ch.ubique.backend.core.openenum;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * A value that belongs to an open enum: a closed set of known constants plus an open set of unknown
 * values that must be preserved rather than dropped.
 *
 * <p>Both the known constants (an {@code enum}) and the unknown carrier (a {@code record}
 * implementing {@link UnknownOpenEnumValue}) implement this interface, so integration code can
 * treat them uniformly through {@link #value()}.
 *
 * <p>{@link #value()} is annotated {@code @JsonValue}, so any implementor serializes as its raw
 * value with plain Jackson — no module registration needed. Deserialization still needs a
 * domain-specific {@code @JsonCreator}, since resolving into known/unknown requires that domain's
 * {@link OpenEnumSet} (see the package README).
 *
 * @param <V> the raw value type (e.g. {@code String} or {@code Integer})
 */
public interface OpenEnumValue<V> {

    /** The raw value carried by this code, used for serialization and persistence. */
    @JsonValue
    V value();

    /**
     * Whether this instance is a known constant. Determined by the explicit {@link
     * UnknownOpenEnumValue} marker, not by the implementation mechanism.
     */
    default boolean isKnown() {
        return !(this instanceof UnknownOpenEnumValue);
    }
}
