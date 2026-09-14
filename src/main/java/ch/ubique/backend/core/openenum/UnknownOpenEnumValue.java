package ch.ubique.backend.core.openenum;

/**
 * Marker for the carrier of an unknown value. A domain type's {@code Unknown*} record implements
 * this so that {@link OpenEnumValue#isKnown()} and framework integrations can detect unknown values
 * without depending on the concrete type.
 *
 * @param <V> the raw value type
 */
public interface UnknownOpenEnumValue<V> extends OpenEnumValue<V> {}
