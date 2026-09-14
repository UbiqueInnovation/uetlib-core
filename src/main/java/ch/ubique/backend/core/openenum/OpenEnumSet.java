package ch.ubique.backend.core.openenum;

import org.jspecify.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Resolves a raw value into a known constant or, failing that, an unknown carrier. The known values
 * are indexed once into an immutable map; duplicate raw values among the known constants are
 * rejected at construction time.
 *
 * @param <T> the domain code type (the sealed interface shared by known and unknown)
 * @param <V> the raw value type
 */
public final class OpenEnumSet<T extends OpenEnumValue<V>, V> {

    private final Map<V, T> known;
    private final Function<V, T> unknownFactory;

    private OpenEnumSet(final Map<V, T> known, final Function<V, T> unknownFactory) {
        this.known = known;
        this.unknownFactory = unknownFactory;
    }

    /**
     * Builds an {@code OpenEnumSet} from the known enum's class and a factory for unknown values.
     *
     * <p>{@code E} must be an actual {@code enum} — {@code Class<E>} together with the {@code
     * Enum<E>} bound is what rejects, at the call site, anything that isn't a real enum (a stray
     * record, say), and lets the known constants be pulled via reflection instead of requiring the
     * caller to pass {@code SomeEnum.values()} explicitly.
     *
     * <p>{@code E} isn't required to extend {@code T}: Java's bound grammar can't express "an enum
     * that also implements this other, separately-parameterized interface." The cast below is the
     * one place that trusts the caller got that part right.
     *
     * @param knownType the known enum's class (e.g. {@code SomeEnum.class})
     * @param unknownFactory produces an unknown carrier from a raw value
     * @throws DuplicateOpenEnumValueException if two known constants share a raw value
     */
    @SuppressWarnings("unchecked")
    public static <T extends OpenEnumValue<V>, E extends Enum<E> & OpenEnumValue<V>, V>
            OpenEnumSet<T, V> of(final Class<E> knownType, final Function<V, T> unknownFactory) {
        final var map =
                Arrays.stream(knownType.getEnumConstants())
                        .collect(
                                Collectors.toMap(
                                        OpenEnumValue::value,
                                        e -> (T) e,
                                        (a, _) -> {
                                            throw new DuplicateOpenEnumValueException(
                                                    knownType, a.value());
                                        },
                                        LinkedHashMap::new));
        return new OpenEnumSet<>(Collections.unmodifiableMap(map), unknownFactory);
    }

    /**
     * Returns the known constant for {@code value}, or an unknown carrier preserving it.
     *
     * @throws NullPointerException if {@code value} is null — a null code is missing data, not an
     *     unknown value. Integrations map JSON/SQL null to null upstream rather than routing it
     *     here, so all entry paths treat null the same way.
     */
    public T valueOfOrUnknown(final V value) {
        Objects.requireNonNull(value, "value");
        final var hit = known.get(value);
        return hit != null ? hit : unknownFactory.apply(value);
    }

    /** Whether {@code value} maps to a known constant. {@code null} is never known. */
    public boolean isKnown(final @Nullable V value) {
        return value != null && known.containsKey(value);
    }

    /**
     * All known constants, in the order they were passed to {@link #of}. The open-enum analogue of
     * {@code Enum.values()} — unlike {@link #valueOfOrUnknown}, this never surfaces unknown values,
     * since there is no fixed set of those to enumerate.
     */
    public Collection<T> values() {
        return known.values();
    }
}
