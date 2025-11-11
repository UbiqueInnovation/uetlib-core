# CHANGELOG

## 1.3.1

### Improvements

- Add `ZONE_NAME_GMT`/`ZONE_ID_GMT`/`TIME_UTILS_GMT` to `TimeUtils`.

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

