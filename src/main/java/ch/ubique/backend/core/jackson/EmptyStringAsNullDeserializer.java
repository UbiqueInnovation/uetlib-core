package ch.ubique.backend.core.jackson;

import org.jspecify.annotations.Nullable;

import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;
import tools.jackson.databind.BeanProperty;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.deser.std.StdDeserializer;

public class EmptyStringAsNullDeserializer extends StdDeserializer<Object> {

    private final @Nullable JavaType valueType;

    /**
     * used by Jackson before calling {@link #createContextual(DeserializationContext,
     * BeanProperty)} on the instance
     */
    @SuppressWarnings("unused")
    public EmptyStringAsNullDeserializer() {
        super(Object.class);
        valueType = null;
    }

    private EmptyStringAsNullDeserializer(final JavaType valueType) {
        super(valueType);
        this.valueType = valueType;
    }

    @Override
    public ValueDeserializer<?> createContextual(
            final DeserializationContext context, final BeanProperty property) {
        return new EmptyStringAsNullDeserializer(property.getType());
    }

    @Override
    public @Nullable Object deserialize(
            final JsonParser parser, final DeserializationContext context) {
        if (parser.hasToken(JsonToken.VALUE_STRING) && parser.getString().isEmpty()) {
            return null;
        }
        return context.readValue(parser, valueType);
    }
}
