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

    @JsonCreator
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

No module or registration needed — plain Jackson annotations do the whole job:

- `@JsonValue` on `OpenEnumValue#value()` covers **serialization** for every open-enum type
  generically: any implementor (known constant or unknown carrier) serializes as its raw value.
- `@JsonCreator` on each domain interface's `valueOfOrUnknown` (as in `Color` above) covers
  **deserialization** for that specific type — it has to be per-type, since resolving into
  known/unknown needs that domain's own `OpenEnumSet`.

```java
JsonMapper.builder().build().writeValueAsString(KnownColor.RED); // "red"
JsonMapper.builder().build().readValue("\"cyan\"", Color.class); // UnknownColor("cyan")
```
