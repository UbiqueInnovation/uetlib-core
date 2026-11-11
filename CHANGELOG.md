# CHANGELOG

## 3.0.2

- Add open enums with known constants and lossless unknown values; see the
  [package README](src/main/java/ch/uetlib/core/openenum/README.md).

## 3.0.1

- Clarify configuration-property migration guidance for consumers.

## 3.0.0

- Move Java packages and class references from `ch.ubique.uetlib` to `ch.uetlib`.

- Move image helpers to `ch.uetlib.core.image.*`; standalone WebP codec packages are unchanged.

### Breaking changes

- Move Java packages from `ch.ubique.backend` to `ch.uetlib`, including class-name resources. The combined OpenRewrite
  migration also renames known Spring configuration keys to `ch.uetlib.*`.
- Remove the inherited Ubique Artifactory repository and deployment destination; private consumers must configure their
  own registry.
- Add Apache-2.0 licensing for original code, preserve third-party licenses, and add public project metadata and
  contribution/security guidance.

- Move Maven coordinates from `ch.ubique:uetlib-*` to `ch.uetlib:uetlib-*`.
- Align uetlib parent, BOM and library references with the migration releases.

### Migration

After the migration releases and `uetlib-rewrite` are published, run this from the consuming project, then review the
diff and run its tests:

```sh
mvn -U org.openrewrite.maven:rewrite-maven-plugin:6.46.1:run \
  -Drewrite.recipeArtifactCoordinates=ch.uetlib:uetlib-rewrite:RELEASE \
  -Drewrite.activeRecipes=ch.uetlib.MigrateToPublicUetlib
```

The recipe updates Maven coordinates/versions, Java references and known class-name resources. Review application-owned
packages under the old namespace, Kotlin, reflection patterns, native-access module flags and persisted class names
separately. Use `:dryRun` instead of `:run` to preview a patch.

## 2.7.0

- Add automatic Hibernate Validator support for `@Positive`, `@PositiveOrZero`, `@Negative`,
  and `@NegativeOrZero` on `Duration`, and `@Min` / `@Max` on `Year`.

# 2.6.1

- Allow null in ResourceNotFoundException methods for varargs parameters

## 2.6.0

- Added `LocalizedValue#cascadeMissing(Locale...)`: retains all existing locale entries and fills
  each requested locale using its best existing match, the first matching value in the ordered
  cascade, or any existing value if none match. Rejects an empty cascade and ignores duplicates.
- New jackson `EmptyStringAsNullDeserializer`

## 2.5.2

- Fixed: AbstractProgressLogger now only sets MDC context for logging progress, instead of for every
  step

## 2.5.1

- Fix deserialization of `LocalizedValue<T>` where T is some record and anything else than a
  `String` or plain `Object`
- update uetlib version
- update actions

## 2.5.0

- Add `jackson-module-kotlin` as `uetlib-core` has Kotlin support

## 2.4.4

- Latest uetlib parent and dependencies
- Java 25
- Fix `sonar.coverage.module`

## 2.4.3

### Fixes

- Fixed `LocalizedValueSerializer#serializeWithType` throwing `InvalidDefinitionException` when
  Jackson embeds a type id (e.g. Spring Data Redis's `GenericJacksonJsonRedisSerializer` with
  default typing enabled). Unwrapped (`@JsonUnwrapped`) properties now embed the type id as a
  flattened property alongside the locale keys instead of failing.

## 2.4.2

- updated actions to not run tests twice for sonar
- update uetlib version

## 2.4.1

### Improvements

- New `ResumableDownload#copyURLToFile` that survives dropped connections by resuming via HTTP range
  requests

## Improvements

- Update `uetlib-parent` and `uetlib-test` to their latest versions
- new `SetOfEnumToStringConverter` for persistence of enums as a comma-separated list in a single
  text column in DB

## 2.3.1

### Fixes

- Fixed nullability annotations for `MinMaxCollector`

## 2.3.0

### Improvements

- Marked everything with jspecify

## 2.2.2

### Bugfixes

- Jackson 3.x changed duration serialization to string. This version configures it back as long.

## 2.2.1

### Improvements

- New `LocalizedValue#mapValues` to map only the values of a `LocalizedValue` (Ported
  from [1.5.2](#152))

## 2.2.0

- All features and fixes from [1.4.3](#143), [1.4.4](#144) and [1.5.0](#150)
- Remove `JsonLocalizedValue` serialization method `POSIX` and
  `LocalizedValue#toMapWithPosixLocaleKeys` (unused and
  annoying)
- Remove `LocalizedValue#toMapWithUppercaseKeys` (unused and can be achieved with
  `@JsonLocalizedValue(keySerialization = SerializationMethod.UPPERCASE)`)

## 2.1.1

- Fix NPE in updated `InputStreamSupplierFileProvider` on calling `file()`

## 2.1.0

- deprecate `AutoClosableTempFileProvider` for removal
- add new `DataAndFileProvder`, analogous to `DataAndInput` but for `FileProvder` instead of
  `ByteFile` and deprecate
  `DataAndInput` and `ByteFile` while at it
- New `TempFileProvider` that creates an empty temporary file and deletes it after use (ported
  to [2.1.0](#210)
  from [1.4.1](#141))

## 2.0.1

### Bugfixes

- Extend `JsonMapperUtils` to make sure some `java.time` types are serialized as ISO-8601 strings by
  default.

## 2.0.0

### Breaking Changes

#### Removed deprecated code

- Removed all `MdcUtils::runWithContext`-overloads, renamed to `call`, `supply`, `consume` etc.
- Removed `TimeFormatUtils::parseLocalDate/parseLocalTime/parseLocalDateTime` replaced by
  `TimeFormatUtils::parsePrettyLocalDate/parsePrettyLocalTime/parsePrettyLocalDateTime`
- Removed constructors of `BlurHashClient` without an explicit user agent. Use constructors with
  user agent.
- Added `JsonMapper` as the first constructor argument to `ObjectMapperToStringConverter`.

#### Jackson 3 support

- Run this OpenRewrite recipe
  `mvn -U org.openrewrite.maven:rewrite-maven-plugin:run -Drewrite.recipeArtifactCoordinates=org.openrewrite.recipe:rewrite-jackson:RELEASE -Drewrite.activeRecipes=org.openrewrite.java.jackson.UpgradeJackson_2_3 -Drewrite.exportDatatables=true`
- And follow https://github.com/FasterXML/jackson/blob/main/jackson3/MIGRATING_TO_JACKSON_3.md
- Removed `ObjectMapperUtils`:
    - Usages of default `ObjectMapper.objectMapper()` are replaced with Spring Boot
      Autoconfiguration in
      `ch.ubique.backend.spring.boot.jackson.autoconfigure.UetlibSpringBootJacksonAutoConfiguration`.
    - Usages of `ObjectMapperUtils.objectMapper(customizer)` have to be replaced with your own
      `org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer` bean, which will
      be applied to the
      autoconfigured `ObjectMapper`.
    - Kept `JsonMapperUtils` for some reusable standard `JsonMapper.Builder` customizations.

### Improvements

- Add method variants that either a custom error `message` or a `messageSupplier` for all
  `NumberUtils.require*`
  methods.

## 1.5.2

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
- Extract http header relevant code from uetlib-spring-boot `HeaderUtility` to new
  `HttpHeaderUtils`.

## 1.3.0

### Breaking Changes

- Any usage of `MdcUtils.runWithContext` with a lambda that is auto-cast to a `Supplier` must now
  explicitly be cast to
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
  methods like `Objects::requireNonNull`.

## 1.1.1

### Improvements

- Latest uetlib dependencies with Spring Boot 3.5 support

## 1.0.2

### Improvements

- Fix `MapCollectors::entriesToMapWithValueMapper` to allow mapping values to a new type and
  expect the map value instead of the map entry as the mapping function argument

## 1.0.1

### Improvements

- Added support for different key serialization (`DEFAULT`/`POSIX`/`UPPERCASE`) methods of
  `LocalizedValue` objects using jackson annotation, e.g.:
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
  need to set the serializer explicitly any more, see the improvements above and:
    ```diff
    record Unwrapped(
        @JsonUnwrapped(suffix = ":suf")
    -        @JsonSerialize(using = JsonUnwrappedLocalizedValueSerializer.class)
            LocalizedValue<String> name) {}
    ```

## 1.0.0

First release. The code was moved
from [UbiqueInnovation/backend-jvm-lib](https://github.com/UbiqueInnovation/backend-jvm-lib).
