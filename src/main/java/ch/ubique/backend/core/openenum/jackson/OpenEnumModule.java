package ch.ubique.backend.core.openenum.jackson;

import ch.ubique.backend.core.openenum.OpenEnumValue;

import tools.jackson.databind.JacksonModule;
import tools.jackson.databind.module.SimpleModule;

/**
 * Teaches Jackson to deserialize any {@link OpenEnumValue}, with no per-type registration.
 *
 * <p>Serialization needs no module: {@code @JsonValue} on {@link OpenEnumValue#value()} covers it
 * for every open enum and cannot be switched off by forgetting to configure a mapper. Only
 * deserialization lives here, because resolving a raw value into known-or-unknown requires the
 * domain's constants.
 *
 * <p>The asymmetry is deliberate. A mapper without this module fails loudly on reads — Jackson
 * cannot instantiate a sealed interface — rather than quietly producing the wrong JSON on writes,
 * which is what moving serialization here as well would risk.
 *
 * <p>Registered for uetlib mappers by {@code JsonMapperUtils.configureOpenEnum}, and for Spring
 * Boot applications by {@code uetlib-spring-boot}.
 */
public final class OpenEnumModule extends SimpleModule {

    public OpenEnumModule() {
        super("OpenEnumModule");
    }

    @Override
    public void setupModule(final JacksonModule.SetupContext context) {
        super.setupModule(context);
        context.addDeserializers(new OpenEnumDeserializers());
    }
}
