# WebP Utils

[Library](https://developers.google.com/speed/webp/docs/using)

## Requirements

In order for the WebP Utils to work, the `libwebp` or `webp` library must be installed on the system and callable from a
terminal.

### Installation on Windows

The [download-page](https://developers.google.com/speed/webp/download) of the WebP project provides a precompiled
binary, which can be downloaded and installed either individually (required are `cwebp` for singular images
and `img2webp` for animations), or by clicking "Download for Windows" to get the whole package with all executables.

Then, the path to the `bin` folder of the WebP package must be added to the `Path` environment variable.

### Installation on Linux

```bash
apt install webp
```
or 
```bash
apt install libwebp
```

(or another package manager) should do the trick.

### Usage with a Java application that is built as docker container

If the Java application is built as a docker container, the WebP library must be installed in the docker image. There is
one prebuilt image available on , which can be used as a base image for the application
image: [`eclipse-temurin-libwebp`](https://github.com/UbiqueInnovation/eclipse-temurin-libwebp-docker).

Otherwise, the library can also be installed, i.e.

```dockerfile
# example base image:
FROM eclipse-temurin:JAVA_VERSION

# Update package lists and install webp library
RUN apt update
RUN apt install -y webp
```

## Metadata

To add metadata to files, the `webpmux` library (might be bundled in `libwebp`, but different in every distro) must be
installed on the system and callable from a terminal.
Furthermore, the Jackson Serializer is used to convert java classes to XML, though using a custom converted as can be
seen below, so the java dependencies `jackson-core`, `jackson-databind` and `jackson-dataformat-xml` must be present.

Then, there are four more files to be added to the project which deal with the structure and content of the metadata:

`WebpXmpMetadata` is basically the XML root element of the metadata. It should look like the following (`begin` and `id` are
hardcoded as specified by the WebP specification):

```java
@JsonSerialize(using = WebpXmpMetadataSerializer.class)
@JacksonXmlRootElement(localName = "rdf:Description")
public record WebpXmpMetadata(WebpXmpRdf rdf) {
    public WebpXmpMetadata(
            final ImageMetadata imageMetadata) { // A custom metadata class from the project
        this(new WebpXmpRdf(imageMetadata.FIELD_1, imageMetadata.FIELD_2));
    }

    @JacksonXmlProperty(isAttribute = true, localName = "begin")
    public String getBegin() {
        return "\uFEFF";
    }

    @JacksonXmlProperty(isAttribute = true, localName = "id")
    public String getId() {
        return "W5M0MpCehiHzreSzNTczkc9d";
    }
}
```

`WebpXmpRdf` will be embedded in the class above. It should look like the following:

```java
@JacksonXmlRootElement(localName = "rdf:RDF")
public record WebpXmpRdf(
        @JacksonXmlProperty(localName = "rdf:Description") WebpXmpRdfDescription description) {
    public WebpXmpRdf(
            final CLASS FIELD_1, final CLASS FIELD_2) {
        this(new WebpXmpRdfDescription(FIELD_1, FIELD_2));
    }
}
```

`WebpXmpRdfDescription` will be embedded in the class above, it should be a record, like the following (prefix for the
custom metadata fields is always `custom`):

```java
public record WebpXmpRdfDescription(
        @JacksonXmlProperty(isAttribute = true, localName = "custom:FIELD_1") final CLASS FIELD_1,
        @JacksonXmlProperty(isAttribute = true, localName = "custom:FIELD_2") final CLASS FIELD_2) {
}
```

In order for these three classes to be converted into the Rdf specs, the following Converter is needed:

```java
public class WebpXmpMetadataSerializer extends StdSerializer<WebpXmpMetadata> {
    public WebpXmpMetadataSerializer() {
        super(WebpXmpMetadata.class);
    }

    @Override
    public void serialize(
            final WebpXmpMetadata value, final JsonGenerator gen, final SerializerProvider provider)
            throws IOException {
        gen.writeRaw("<?xpacket begin=\"" + value.getBegin() + "\" id=\"" + value.getId() + "\"?>");
        gen.writeRaw("<x:xmpmeta>");
        gen.writeRaw("<rdf:RDF>");
        final var serializer = provider.findValueSerializer(WebpXmpRdfDescription.class);
        serializer.serialize(value.rdf().description(), gen, provider);
        gen.writeRaw("</rdf:RDF>");
        gen.writeRaw("</x:xmpmeta>");
        gen.writeRaw("<?xpacket end=\"w\"?>");
    }
}
```
