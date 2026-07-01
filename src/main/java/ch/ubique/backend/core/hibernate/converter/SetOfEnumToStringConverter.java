package ch.ubique.backend.core.hibernate.converter;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toUnmodifiableSet;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import org.jspecify.annotations.Nullable;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;

@Converter
public abstract class SetOfEnumToStringConverter<E extends Enum<E>>
        implements AttributeConverter<Set<E>, String> {

    private final Function<E, String> enum2String;
    private final Function<String, E> string2Enum;

    protected SetOfEnumToStringConverter(final Class<E> enumClass) {
        this(Enum::name, e -> Enum.valueOf(enumClass, e));
    }

    protected SetOfEnumToStringConverter(
            final Function<E, String> enum2String, final Function<String, E> string2Enum) {
        this.enum2String = enum2String;
        this.string2Enum = string2Enum;
    }

    @Override
    @Nullable
    public String convertToDatabaseColumn(final @Nullable Set<E> enums) {
        if (enums == null) {
            return null;
        }
        return enums.stream().map(enum2String).collect(joining(","));
    }

    @Override
    @Nullable
    public Set<E> convertToEntityAttribute(final @Nullable String joined) {
        if (joined == null) {
            return null;
        }
        return Arrays.stream(joined.split(",")).map(string2Enum).collect(toUnmodifiableSet());
    }
}
