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
 * value with plain Jackson — no registration needed, and no way to forget it. Deserialization is
 * handled by {@code OpenEnumModule} instead, since resolving a raw value into known-or-unknown
 * requires the domain's {@link OpenEnumSet}; a mapper without that module fails loudly on reads
 * rather than quietly writing the wrong shape (see the package README).
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
