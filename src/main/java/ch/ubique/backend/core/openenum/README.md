# Open Enums

Model a value as a closed set of known constants plus an open set of raw values that aren't known
yet — e.g. a status code an external API might add in the future — so they round-trip losslessly
instead of being dropped or failing deserialization.

## Usage

```java
sealed interface Color extends OpenEnumValue<String> {

    OpenEnumSet<Color, String> KNOWN_VALUES = OpenEnumSet.of(KnownColor.class, UnknownColor::new);

    static Collection<Color> values() {
        return KNOWN_VALUES.values();
    }

    static Color valueOfOrUnknown(final String value) {
        return KNOWN_VALUES.valueOfOrUnknown(value);
    }

    enum KnownColor implements Color {
        RED("red"),
        GREEN("green"),
        BLUE("blue");

        private final String value;

        KnownColor(final String value) {
            this.value = value;
        }

        @Override
        public String value() {
            return value;
        }
    }

    record UnknownColor(String value) implements Color, UnknownOpenEnumValue<String> {}
}
```

- `Color.valueOfOrUnknown("red")` returns the known `KnownColor.RED` constant.
- `Color.valueOfOrUnknown("cyan")` returns `new UnknownColor("cyan")`, preserving the raw value
  instead of throwing.
- `Color.values()` lists the known constants, the open-enum analogue of `Enum.values()`.
- A pattern-matching `switch` over `Color` stays exhaustive without a `default` branch — `KnownColor`
  and `UnknownColor` are the only two ways to implement it, so the compiler rejects the switch if a
  case is missing.

`OpenEnumSet.of` only accepts an actual `enum` class for the known constants (enforced by the
`Class<E>`/`Enum<E>` bound), and rejects two constants sharing the same raw value with a
`DuplicateOpenEnumValueException`.

## Jackson

Nothing to annotate or register per domain type. The two directions are handled differently, on
purpose:

- **Serialization** — `@JsonValue` on `OpenEnumValue#value()`. Works on any mapper, including a
  bare `new JsonMapper()`, and cannot be disabled by forgetting to configure something.
- **Deserialization** — `OpenEnumModule`. It matches *any* `OpenEnumValue` and finds the domain's
  `OpenEnumSet` via `OpenEnumSet.forType(...)`, which scans the type for a `static` field of that
  type (matched on type, not name). So a new open enum works the moment it exists.

The asymmetry is the point. A mapper without the module **fails loudly** on reads — Jackson cannot
instantiate a sealed interface. Had serialization also lived in the module, an unconfigured mapper
would instead write silently wrong JSON: the `Known` enum would fall back to `name()` (which
differs from `value()` whenever the wire format isn't the constant name) and `Unknown` would
serialize as a POJO `{"value":"cyan"}`.

The module is registered by `JsonMapperUtils.configureBase(...)` and, in Spring Boot applications,
by `uetlib-spring-boot`'s `UetlibJacksonOpenEnumJsonMapperBuilderCustomizer`. Jackson's
ServiceLoader discovery is opt-in (`MapperBuilder.findAndAddModules()`), so it is not relied on.

```java
var mapper = JsonMapperUtils.jsonMapper();
mapper.writeValueAsString(Color.Known.RED);      // "red"
mapper.readValue("\"cyan\"", Color.class);        // Color.Unknown("cyan")
```
