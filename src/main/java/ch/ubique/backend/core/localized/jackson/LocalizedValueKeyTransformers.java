package ch.ubique.backend.core.localized.jackson;

import ch.ubique.backend.core.localized.LocalizedValue;
import ch.ubique.backend.core.localized.annotation.JsonLocalizedValue;

import com.google.common.base.CaseFormat;

import org.apache.commons.lang3.StringUtils;

import tools.jackson.databind.util.NameTransformer;

import java.util.Locale;

/**
 * Shared {@link NameTransformer} helpers for {@link LocalizedValue} key serialization and
 * deserialization.
 *
 * <p>The canonical internal key format is the lower-hyphen language tag form returned by {@code
 * Locale#toLanguageTag()}, e.g. {@code en} or {@code de-ch}. Other supported key formats are
 * derived from and reversed back to that canonical representation using {@link NameTransformer}s.
 */
final class LocalizedValueKeyTransformers {

    private LocalizedValueKeyTransformers() {}

    static NameTransformer keySerializationTransformer(
            final JsonLocalizedValue.SerializationMethod serializationMethod) {
        return switch (serializationMethod) {
            case DEFAULT -> new IdentityNameTransformer();
            case UPPERCASE -> new UpperCaseNameTransformer();
            case CAMEL_CASE -> new CamelCaseNameTransformer();
            case PASCAL_CASE ->
                    new NameTransformer() {
                        @Override
                        public String transform(final String name) {
                            return convertCase(
                                    name, CaseFormat.LOWER_HYPHEN, CaseFormat.UPPER_CAMEL);
                        }

                        @Override
                        public String reverse(final String transformed) {
                            return convertCase(
                                    transformed, CaseFormat.UPPER_CAMEL, CaseFormat.LOWER_HYPHEN);
                        }

                        @Override
                        public String toString() {
                            return "NameTransformer[LocalizedValue PASCAL_CASE]";
                        }
                    };
        };
    }

    static String convertCase(final String rawKey, final CaseFormat from, final CaseFormat to) {
        if (StringUtils.isBlank(rawKey)) {
            return null;
        }

        try {
            return from.to(to, rawKey);
        } catch (final IllegalArgumentException ignored) {
            return null;
        }
    }

    public static class IdentityNameTransformer extends NameTransformer {
        @Override
        public String transform(final String name) {
            return name;
        }

        @Override
        public String reverse(final String transformed) {
            return transformed;
        }

        @Override
        public String toString() {
            return "NameTransformer[LocalizedValue IDENTITY]";
        }
    }

    public static class UpperCaseNameTransformer extends NameTransformer {
        @Override
        public String transform(final String name) {
            return name == null ? null : name.toUpperCase(Locale.ROOT);
        }

        @Override
        public String reverse(final String transformed) {
            return transformed == null ? null : transformed.toLowerCase(Locale.ROOT);
        }

        @Override
        public String toString() {
            return "NameTransformer[LocalizedValue UPPERCASE]";
        }
    }

    public static class CamelCaseNameTransformer extends NameTransformer {
        @Override
        public String transform(final String name) {
            return convertCase(name, CaseFormat.LOWER_HYPHEN, CaseFormat.LOWER_CAMEL);
        }

        @Override
        public String reverse(final String transformed) {
            return convertCase(transformed, CaseFormat.LOWER_CAMEL, CaseFormat.LOWER_HYPHEN);
        }

        @Override
        public String toString() {
            return "NameTransformer[LocalizedValue CAMEL_CASE]";
        }
    }

    public static class PascalCaseNameTransformer extends NameTransformer {
        @Override
        public String transform(final String name) {
            return convertCase(name, CaseFormat.LOWER_HYPHEN, CaseFormat.UPPER_CAMEL);
        }

        @Override
        public String reverse(final String transformed) {
            return convertCase(transformed, CaseFormat.UPPER_CAMEL, CaseFormat.LOWER_HYPHEN);
        }

        @Override
        public String toString() {
            return "NameTransformer[LocalizedValue PASCAL_CASE]";
        }
    }
}
