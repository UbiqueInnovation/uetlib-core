package ch.ubique.uetlib.core.jackson;

import com.github.zafarkhaja.semver.Version;

import tools.jackson.databind.module.SimpleModule;

public class UetlibCoreJacksonVersionModule extends SimpleModule {

    public UetlibCoreJacksonVersionModule() {
        addSerializer(Version.class, new UetlibCoreVersionToStringSerializer());
        addDeserializer(Version.class, new UetlibCoreStringToVersionDeserializer());
    }
}
