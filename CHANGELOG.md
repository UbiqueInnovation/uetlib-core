# CHANGELOG

## 2.1.1

### Improvements

- New `LocalizedValue#mapValues` to map only the values of a `LocalizedValue` (Ported from [1.5.1](#151))

## 2.2.0

- All features and fixes from `1.4.3`, `1.4.4` and `1.5.0`
- Remove `JsonLocalizedValue` serialization method `POSIX` and `LocalizedValue#toMapWithPosixLocaleKeys(unused and
  annoying)
- Remove `LocalizedValue#toMapWithUppercaseKeys` (unused and can be achieved with
  `@JsonLocalizedValue(keySerialization = SerializationMethod.UPPERCASE)`)

## 2.1.1

- Fix NPE in updated `InputStreamSupplierFileProvider` on calling `file()`

## 2.1.0

- deprecate `AutoClosableTempFileProvider` for removal
- add new `DataAndFileProvder`, analogous to `DataAndInput` but for `FileProvder` instead of `ByteFile` and deprecate
  `DataAndInput` and `ByteFile` while at it
- New `TempFileProvider` that creates an empty temporary file and deletes it after use (ported to 2.1.0 from 1.4.1)

## 2.0.1

### Bugfixes

- Extend `JsonMapperUtils` to make sure some `java.time` types are serialized as ISO-8601 strings by default.

## 2.0.0

### Breaking Changes

#### Removed deprecated code

- Removed all `MdcUtils::runWithContext`-overloads, renamed to `call`, `supply`, `consume` etc.
- Removed `TimeFormatUtils::parseLocalDate/parseLocalTime/parseLocalDateTime` replaced by
  `TimeFormatUtils::parsePrettyLocalDate/parsePrettyLocalTime/parsePrettyLocalDateTime`
- Removed constructors of `BlurHashClient` without an explicit user agent. Use constructors with user agent.
- Added `JsonMapper` as the first constructor argument to `ObjectMapperToStringConverter`.

#### Jackson 3 support

- Run this OpenRewrite recipe
  `mvn -U org.openrewrite.maven:rewrite-maven-plugin:run -Drewrite.recipeArtifactCoordinates=org.openrewrite.recipe:rewrite-jackson:RELEASE -Drewrite.activeRecipes=org.openrewrite.java.jackson.UpgradeJackson_2_3 -Drewrite.exportDatatables=true`
- And follow https://github.com/FasterXML/jackson/blob/main/jackson3/MIGRATING_TO_JACKSON_3.md
- Removed `ObjectMapperUtils`:
    - Usages of default `ObjectMapper.objectMapper()` are replaced with Spring Boot Autoconfiguration in
      `ch.ubique.backend.spring.boot.jackson.autoconfigure.UetlibSpringBootJacksonAutoConfiguration`.
    - Usages of `ObjectMapperUtils.objectMapper(customizer)` have to be replaced with your own
      `org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer` bean, which will be applied to the
      autoconfigured `ObjectMapper`.
    - Kept `JsonMapperUtils` for some reusable standard `JsonMapper.Builder` customizations.

### Improvements

- Add method variants that either a custom error `message` or a `messageSupplier` for all `NumberUtils.require*`
  methods.

## 1.5.1

### Improvements

- New `LocalizedValue#mapValues` to map only the values of a `LocalizedValue`

## 1.5.0

### Improvements

- New `LocalizedValue` serialization methods:
    - `CAMEL_CASE` to serialize keys in camel case. E.g. `Locale.of("de", "CH")`
      as `deCh`
    - `PASCAL_CASE` to serialize keys in pascal case. E.g. `Locale.of("de", "CH")`
      as `DeCh`
- New `LocalizedValue` deserialization, symmetric to the serialization options.

## 1.4.4

### Improvements

- WebpWriter now supports creating webp animations with individual durations per frame.

## 1.4.3

### Improvements

- Make process timeout configurable in `WebpEncodingConfig`

## 1.4.1

### Improvements

- New `TempFileProvider` that creates an empty temporary file and deletes it after use.
  initially.

## 1.4.0

### Backports from 2.1.0

- New `DataAndFileProvder`, analogous to `DataAndInput` but for `FileProvder` instead of `ByteFile`

## 1.3.3

### Improvements

- Add `ZONE_NAME_GMT`/`ZONE_ID_GMT`/`TIME_UTILS_GMT` to `TimeUtils`.
- Extract http header relevant code from uetlib-spring-boot `HeaderUtility` to new `HttpHeaderUtils`.

## 1.3.0

### Breaking Changes

- Any usage of `MdcUtils.runWithContext` with a lambda that is auto-cast to a `Supplier` must now explicitly be cast to
  either a `Supplier` or `Callable`.

### Improvements

- Add overload to `MdcUtils` for a `Callable<T>`.

## 1.2.7

### Improvements

- `TempFileManager` implements `AutoCloseable` now for convenience.

## 1.2.6

### Improvements

- New `AtLeastOneNull` validation annotation. This represents is the inverse of the existing
  `AtLeastOneNotNull`.

## 1.2.5

### Improvements

- Explicitly allow `@NullOrNotEmpty` on `Collection`s, `Map`s and arrays. Previously, only
  collections were allowed.
- Allow `@NullOrNotBlank` on `CharSequence`s, not just `String`s.

## 1.2.4

### Improvements

- `FileProvider` allows setting prefix and suffix for the temporary file name.

## 1.2.3

### Improvements

- Added `InputStreamUtils.connectWithRedirect` to follow cross-protocol redirects for URLs.

## 1.2.0

### Improvements

- Added `VersionToStringSerializer/StringToVersionSerializer` to read write
  `com.github.zafarkhaja.semver.Version`
  SemVer Strings to/from JSON.
- Registered `VersionToStringSerializer/StringToVersionSerializer` with the default object mapper in
  `ObjectMapperUtils`.
- Added `LocalizedValueValueExtractor` to validate annotations on the generic type `T` of
  `LocalizedValue<T>`
- `NumberUtils` with `requirePositive`/`requireNegative`/`requireNonPositive`/`requireNonNegative`
  methods like
  `Objects::requireNonNull`.

## 1.1.1

### Improvements

- Latest uetlib dependencies with Spring Boot 3.5 support

## 1.0.2

### Improvements

- Fix `MapCollectors::entriesToMapWithValueMapper` to allow mapping values to a new type and expect
  the map value
  instead of the map entry as the mapping function argument

## 1.0.1

### Improvements

- Added support for different key serialization (`DEFAULT`/`POSIX`/`UPPERCASE`) methods of
  `LocalizedValue` objects
  using jackson annotation, e.g.:
    ```
    record KeySerialization(
        @JsonLocalizedValue(keySerialization = SerializationMethod.POSIX)
            LocalizedValue<String> name) {}
    ```
  This can even be combined with the existing unwrapping functionality:
    ```
    record Combined(
        @JsonUnwrapped(prefix = "pre:", suffix = ":suf")
            @JsonLocalizedValue(keySerialization = SerializationMethod.UPPERCASE)
            LocalizedValue<String> name) {}  
    ```

### Breaking Changes

- Removed `JsonUnwrappedLocalizedValueSerializer`. The functionality is unchange, but you do not
  need to set the
  serializer explicitly anymore, see the improvements above and:
    ```diff
    record Unwrapped(
        @JsonUnwrapped(suffix = ":suf")
    -        @JsonSerialize(using = JsonUnwrappedLocalizedValueSerializer.class)
            LocalizedValue<String> name) {}
    ```

## 1.0.0

First release. The code was moved
from [UbiqueInnovation/backend-jvm-lib](https://github.com/UbiqueInnovation/backend-jvm-lib).

